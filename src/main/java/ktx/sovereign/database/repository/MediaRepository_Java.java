package ktx.sovereign.database.repository;

import android.app.Application;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.lifecycle.MutableLiveData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import static ktx.sovereign.database.AWSConfiguration.isContainMime;

public class MediaRepository_Java {

    private Application application;
    private MutableLiveData<List<String>> files;
    private MutableLiveData<List<String>> tempFiles;

    public MediaRepository_Java(Application application) {
        this.application = application;
        files = new MutableLiveData<>();
        tempFiles = new MutableLiveData<>();
    }

    public MutableLiveData<List<String>> getFilePaths() {
        if (tempFiles.getValue() != null && tempFiles.getValue().size() != 0)
            return tempFiles;
        Uri u = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA, MediaStore.Video.DEFAULT_SORT_ORDER};
        Cursor c = null;
        SortedSet<String> dirList = new TreeSet<>();
        ArrayList<String> resultIAV = new ArrayList<>();

        String[] directories = null;
        if (u != null)
            c = application.getContentResolver().query(u, projection, null, null, null);
        if ((c != null) && (c.moveToFirst())) {
            do {
                String tempDir = c.getString(0);
                tempDir = tempDir.substring(0, tempDir.lastIndexOf("/"));
                try {
                    dirList.add(tempDir);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            while (c.moveToNext());
            directories = new String[dirList.size()];
            dirList.toArray(directories);
            c.close();
        }

        for (int i = 0; i < dirList.size(); i++) {
            File imageDir = new File(directories[i]);
            File[] imageList = imageDir.listFiles();
            if (imageList == null)
                continue;
            for (File imagePath : imageList) {
                try {
//                    if(imagePath.isDirectory())
//                        imageList = imagePath.listFiles();
//                    for (File file : imageList)
//                        Log.e("check_list",file.getName());
                    if (imagePath.getName().contains(".jpg") || imagePath.getName().contains(".JPG")
                            || imagePath.getName().contains(".jpeg") || imagePath.getName().contains(".JPEG")
                            || imagePath.getName().contains(".png") || imagePath.getName().contains(".PNG")
                            || imagePath.getName().contains(".gif") || imagePath.getName().contains(".GIF")
                            || imagePath.getName().contains(".bmp") || imagePath.getName().contains(".BMP")
                    ) {
                        String path = imagePath.getAbsolutePath();
                        resultIAV.add(path);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        files.setValue(resultIAV);
        tempFiles.setValue(resultIAV);
        return files;
    }

    public MutableLiveData<List<String>> search(String query) {
        if (query.isEmpty())
            files.postValue(tempFiles.getValue());
        else {
            List<String> filteredList = new ArrayList<>();
            if (tempFiles.getValue() != null)
                for (String file : tempFiles.getValue())
                    if ((file.toLowerCase().contains(query.toLowerCase()) || file.contains(query)) && ((file.length() > 1) || isContainMime(file)))
                        filteredList.add(file);
            files.postValue(filteredList);
        }
        return files;
    }
}
