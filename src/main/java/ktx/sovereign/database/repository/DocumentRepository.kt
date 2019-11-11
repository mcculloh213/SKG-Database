package ktx.sovereign.database.repository

import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import ktx.sovereign.database.AWSConfiguration
import ktx.sovereign.database.AWSConfiguration.isContainMime
import java.io.File
import java.util.*

class DocumentRepository(private val applicationContext: android.app.Application) {

    private var s3Client: AmazonS3Client? = null
    private var transferUtility: TransferUtility? = null
    private var downloadStatus: MutableLiveData<String> = MutableLiveData()
    private var downloadedData: MutableLiveData<Array<Long>> = MutableLiveData()
    private var downloadedFile: MutableLiveData<String> = MutableLiveData()
    private var s3Items: MutableLiveData<MutableList<String>> = MutableLiveData()
    private var tempS3Items: MutableLiveData<MutableList<String>> = MutableLiveData()

    init {
        s3credentialsProvider()
    }

    private fun s3credentialsProvider() {
        // Initialize the AWS Credential
        val cognitoCachingCredentialsProvider = CognitoCachingCredentialsProvider(
                applicationContext,
                AWSConfiguration.ID, // Identity Pool ID
                Regions.US_EAST_2 // Region
        )
        createAmazonS3Client(cognitoCachingCredentialsProvider)
    }
    /**
     * Create a AmazonS3Client constructor and pass the credentialsProvider.
     *
     * @param credentialsProvider
     */
    private fun createAmazonS3Client(credentialsProvider: CognitoCachingCredentialsProvider) {
        // Create an S3 client
        s3Client = AmazonS3Client(credentialsProvider)
        // Set the region of your S3 bucket
        s3Client!!.setRegion(Region.getRegion(Regions.US_EAST_2))
        transferUtility = TransferUtility(s3Client, applicationContext)
    }

    fun getDownloadStatus(): MutableLiveData<String> {
        return downloadStatus
    }

    fun getDownloadedData(): MutableLiveData<Array<Long>> {
        return downloadedData
    }

    fun getDownloadedFile(): MutableLiveData<String> {
        return downloadedFile
    }
    fun getDocumentsList(): MutableLiveData<MutableList<String>> {
        val thread = Thread(Runnable {
            try {
                Looper.prepare()
                s3Items.value?.clear()
                tempS3Items.value?.clear()

                if (AWSConfiguration.isNetworkAvailable(applicationContext)) {
                    val list = getObjectNamesForBucket(AWSConfiguration.BUCKET, s3Client!!).toMutableList()
                    s3Items.postValue(list)
                    tempS3Items.postValue(list)
                } else {
                    val directory = File(AWSConfiguration.path + "Documents/")
                    val list = ArrayList<String>(directory.listFiles().size)
                    directory.listFiles().forEach { list.add(it.absolutePath) }
                    if (!list.isEmpty()) {
                        s3Items.postValue(list)
                        tempS3Items.postValue(list)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("tag", "Exception found while listing $e")
            }
        })
        if (tempS3Items.value != null && tempS3Items.value!!.size != 0)
            return tempS3Items
        else
            thread.start()
        return s3Items
    }
    fun search(query: String): MutableLiveData<MutableList<String>> {
        if (query.isEmpty()) {
            s3Items.postValue(tempS3Items.value)
        } else {
            val filteredList = ArrayList<String>()
            if (tempS3Items.value != null)
                for (file in tempS3Items.value!!)
                    if ((file.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT)) || file.contains(query)) && (file.length > 1 || isContainMime(file)))
                        filteredList.add(file)
            s3Items.postValue(filteredList)
        }
        return s3Items
    }

    fun downloadFile(fileName: String, path: File): MutableLiveData<MutableList<String>> {
        val transferObserver = transferUtility?.download(AWSConfiguration.BUCKET, fileName, path)
        transferObserverListener(transferObserver, fileName)
        return s3Items
    }

    private fun transferObserverListener(transferObserver: TransferObserver?, fileName: String) {
        transferObserver?.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState) {
                downloadStatus.postValue(state.name)
                if ("COMPLETED" == state.name) {
                    downloadedFile.postValue(fileName)
                    s3Items.postValue(s3Items.value)
                }
            }

            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                downloadedData.postValue(arrayOf(bytesCurrent, bytesTotal))
            }

            override fun onError(id: Int, ex: Exception) {
                Log.e("check_observer", "On Error")
                Log.e("error", "error")
                ex.printStackTrace()
                downloadStatus.postValue("FAILED")
            }
        })
    }

    private fun getObjectNamesForBucket(bucket: String, s3Client: AmazonS3): MutableList<String> {
        var objects = s3Client.listObjects(bucket)
        val objectNames = java.util.ArrayList<String>(objects.objectSummaries.size)
        for (s3ObjectSummary in objects.objectSummaries) {
            objectNames.add(s3ObjectSummary.key)
        }
        while (objects.isTruncated) {
            objects = s3Client.listNextBatchOfObjects(objects)
            for (s3ObjectSummary in objects.objectSummaries) {
                objectNames.add(s3ObjectSummary.key)
            }
        }
        return objectNames
    }
}