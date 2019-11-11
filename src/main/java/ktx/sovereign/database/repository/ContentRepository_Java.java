package ktx.sovereign.database.repository;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.industrialbadger.api.IntelligentMixedReality;
import com.industrialbadger.api.client.ContentClient;
import com.industrialbadger.api.model.imr.Content;
import com.industrialbadger.api.model.imr.Volume;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import ktx.sovereign.database.AWSConfiguration;
import ktx.sovereign.database.entity.Events;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ktx.sovereign.database.AWSConfiguration.isContainMime;
import static ktx.sovereign.database.AWSConfiguration.isNetworkAvailable;

public class ContentRepository_Java {

    private Application applicationContext;
    private TransferUtility transferUtility = null;

    private List<Volume.Volume> volumes;
    private ContentClient response;
    private MutableLiveData<String> downloadStatus;
    private MutableLiveData<Long[]> downloadedData;
    private MutableLiveData<String> downloadedFile;
    private MutableLiveData<List<String>> contents;
    private MutableLiveData<List<String>> tempContents;

    public ContentRepository_Java(Application application) {
        downloadedFile = new MutableLiveData<>();
        downloadedData = new MutableLiveData<>();
        downloadStatus = new MutableLiveData<>();
        contents = new MutableLiveData<>();
        tempContents = new MutableLiveData<>();
        volumes = new ArrayList<>();
        this.applicationContext = application;
        response = IntelligentMixedReality.Companion.getUpdatedContentClient();
        s3credentialsProvider();
    }

    //Initialize AWS Client
    private void s3credentialsProvider() {
        CognitoCachingCredentialsProvider cognitoCachingCredentialsProvider = new CognitoCachingCredentialsProvider(
                applicationContext,
                AWSConfiguration.ID, // Identity Pool ID
                Regions.US_EAST_2 // Region
        );
        createAmazonS3Client(cognitoCachingCredentialsProvider);
    }

    private void createAmazonS3Client(CognitoCachingCredentialsProvider credentialsProvider) {
        AmazonS3Client s3Client = new AmazonS3Client(credentialsProvider);
        s3Client.setRegion(Region.getRegion(Regions.US_EAST_2));
        transferUtility = new TransferUtility(s3Client, applicationContext);
    }

    //Get List of Volumes
    private void getVolumes() {
        if (!isNetworkAvailable(applicationContext))
            return;
        Call<List<Volume.Volume>> call = response.indexVolumes("5c509dcb-15fa-4ff4-899b-aef74e703453");
        call.enqueue(new Callback<List<Volume.Volume>>() {
            @Override
            public void onResponse(@NotNull Call<List<Volume.Volume>> call, @NotNull Response<List<Volume.Volume>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("check_res_volumes", response.body().toString());
                    volumes = response.body();
                    getContents();
                } else
                    Log.e("check_res_volumes", response.message());
            }

            @Override
            public void onFailure(@NotNull Call<List<Volume.Volume>> call, @NotNull Throwable t) {
                t.printStackTrace();
                Toast.makeText(applicationContext, "Failed To Send Event", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Get List of Contents Using Volume Token if Online, Otherwise Get List from Internal Storage
    public MutableLiveData<List<String>> getContents() {
        final List<String> tempList = new ArrayList<>();
        if (tempContents.getValue() != null && tempContents.getValue().size() != 0)
            return tempContents;
        if (isNetworkAvailable(applicationContext)) {
            if (!volumes.isEmpty())
                for (final Volume.Volume volume : volumes) {
                    Call<List<Content.UpdatedContent>> call = response.indexContent(volume.getToken());
                    call.enqueue(new Callback<List<Content.UpdatedContent>>() {
                        @Override
                        public void onResponse(@NotNull Call<List<Content.UpdatedContent>> call, @NotNull Response<List<Content.UpdatedContent>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                for (Content.UpdatedContent content : response.body())
                                    if (volume.getId().equals(content.getToken()))
                                        tempList.add(content.isCredential() + "@" + volume.getId() + "/" + content.getTitle());
                            }
                            contents.postValue(tempList);
                            tempContents.postValue(tempList);
                        }

                        @Override
                        public void onFailure(@NotNull Call<List<Content.UpdatedContent>> call, @NotNull Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
            else
                getVolumes();
        } else {
            File directory = new File(AWSConfiguration.path + "Contents/");
            List<String> list = new ArrayList<>();
            if (directory.listFiles() != null)
                for (File file : directory.listFiles())
                    list.add(file.getName());
            if (!list.isEmpty()) {
                contents.postValue(list);
                tempContents.postValue(list);
            }
        }
        return contents;
    }

    //Get Filtered List of Contents from Based on Query String from Existing List
    public MutableLiveData<List<String>> searchContent(String query) {
        if (query.isEmpty())
            contents.postValue(tempContents.getValue());
        else {
            List<String> filteredList = new ArrayList<>();
            if (tempContents.getValue() != null)
                for (String file : tempContents.getValue())
                    if ((file.toLowerCase().contains(query.toLowerCase()) || file.contains(query)) && ((file.length() > 1) || isContainMime(file)))
                        filteredList.add(file);
            contents.postValue(filteredList);
            postSearchEvent(query);
        }
        return contents;
    }

    //Post An Event When User Search Something
    private void postSearchEvent(String query) {
        if (!isNetworkAvailable(applicationContext))
            return;
        HashMap<String, Object> data = new HashMap<>();
        data.put("query", query);
        Events event = new Events("Search", "SEARCH", System.currentTimeMillis(), TimeZone.getDefault().getID(), data);
        Log.e("check_req_event_search", event.toString());
        Call<JSONObject> call = response.createEvent(event);
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(@NotNull Call<JSONObject> call, @NotNull Response<JSONObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("check_res_event_search", response.body().toString());
                    Toast.makeText(applicationContext, "Event Posted", Toast.LENGTH_SHORT).show();
                } else
                    Log.e("check_res_event_search", response.message());
            }

            @Override
            public void onFailure(@NotNull Call<JSONObject> call, @NotNull Throwable t) {
                t.printStackTrace();
                Toast.makeText(applicationContext, "Failed To Send Event", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Post An Event When User Clicks on Content File
    public void postClickEvent(HashMap<String, Object> data) {
        if (!isNetworkAvailable(applicationContext))
            return;
        Events event = new Events("Search", "CLICK", System.currentTimeMillis(), TimeZone.getDefault().getID(), data);
        Log.e("check_req_event_click", event.toString());
        Call<JSONObject> call = response.createEvent(event);
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(@NotNull Call<JSONObject> call, @NotNull Response<JSONObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("check_res_event_click", response.body().toString());
                    Toast.makeText(applicationContext, "Event Posted", Toast.LENGTH_SHORT).show();
                } else
                    Log.e("check_res_event_click", response.message());
            }

            @Override
            public void onFailure(@NotNull Call<JSONObject> call, @NotNull Throwable t) {
                t.printStackTrace();
                Toast.makeText(applicationContext, "Failed To Send Event", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public MutableLiveData<String> getDownloadedFile() {
        return downloadedFile;
    }

    public MutableLiveData<Long[]> getDownloadedData() {
        return downloadedData;
    }

    public MutableLiveData<String> getDownloadStatus() {
        return downloadStatus;
    }

    public MutableLiveData<List<String>> downloadFile(String fileName, File path) {
        Log.e("check_key", fileName);
        TransferObserver transferObserver = transferUtility.download(AWSConfiguration.BUCKET, fileName, path);
        transferObserverListener(transferObserver, fileName);
        return contents;
    }

    private void transferObserverListener(TransferObserver transferObserver, final String fileName) {
        transferObserver.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                downloadStatus.postValue(state.name());
                if ("COMPLETED".equals(state.name())) {
                    downloadedFile.postValue(fileName);
                    contents.postValue(contents.getValue());
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                downloadedData.postValue(new Long[]{bytesCurrent, bytesTotal});
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e("check_observer", "On Error");
                Log.e("error", "error");
                ex.printStackTrace();
                downloadStatus.postValue("FAILED");
            }
        });
    }
}
