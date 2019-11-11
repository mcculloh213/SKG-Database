package ktx.sovereign.database;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import java.io.File;
import java.text.DecimalFormat;

public class AWSConfiguration {
    public static final String BUCKET = "edutechnologic.motoman";
    public static final String FILE_URI = "https://s3.us-east-2.amazonaws.com/edutechnologic.motoman/";
    public static final String ID = "us-east-2:635a68f8-5366-4ea1-ad69-bb3a29d4b46a";
    public static final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Files/";

    public static int getFileImage(String file) {
        if (file.contains(".doc") || file.contains(".docx"))
            return R.drawable.ic_file_doc;
        else if (file.contains(".html"))
            return R.drawable.ic_file_html;
        else if (file.contains(".pdf"))
            return R.drawable.ic_file_pdf;
        else if (file.contains(".ppt") || file.contains(".pptx"))
            return R.drawable.ic_file_ppt;
        else if (file.contains(".xls") || file.contains(".xlsx"))
            return R.drawable.ic_file_xls;
        else if (file.contains(".zip") || file.contains(".rar"))
            return R.drawable.ic_file_zip;
        else if (file.contains(".rtf"))
            return R.drawable.ic_file_rtf;
        else if (file.contains(".wav") || file.contains(".mp3"))
            return R.drawable.ic_file_mp3;
        else if (file.contains(".gif"))
            return R.drawable.ic_file_gif;
        else if (file.contains(".jpg") || file.contains(".jpeg"))
            return R.drawable.ic_file_jpg;
        else if (file.contains(".png"))
            return R.drawable.ic_file_png;
        else if (file.contains(".txt"))
            return R.drawable.ic_file_txt;
        else if (file.contains(".3gp") || file.contains(".mpg") || file.contains(".mpeg") || file.contains(".mpe") || file.contains(".mp4") || file.contains(".avi"))
            return R.drawable.ic_file_mp4;
        else
            return R.drawable.icon_file;
    }

    public static String getFileType(String file) {
        if (file.contains(".doc") || file.contains(".docx"))
            return "application/msword";
        else if (file.contains(".html"))
            return "text/html";
        else if (file.contains(".pdf"))
            return "application/pdf";
        else if (file.contains(".ppt") || file.contains(".pptx"))
            return "application/vnd.ms-powerpoint";
        else if (file.contains(".xls") || file.contains(".xlsx"))
            return "application/vnd.ms-excel";
        else if (file.contains(".zip") || file.contains(".rar"))
            return "application/x-wav";
        else if (file.contains(".rtf"))
            return "application/rtf";
        else if (file.contains(".wav") || file.contains(".mp3"))
            return "audio/x-wav";
        else if (file.contains(".gif"))
            return "image/gif";
        else if (file.contains(".jpg") || file.contains(".jpeg") || file.contains(".png"))
            return "image/*";
        else if (file.contains(".txt"))
            return "text/plain";
        else if (file.contains(".3gp") || file.contains(".mpg") || file.contains(".mpeg") || file.contains(".mpe") || file.contains(".mp4") || file.contains(".avi"))
            return "video/*";
        else
            return "*/*";
    }

    public static boolean isContainMime(String file) {
        if (file.contains(".doc") || file.contains(".docx"))
            return true;
        else if (file.contains(".html"))
            return true;
        else if (file.contains(".pdf"))
            return true;
        else if (file.contains(".ppt") || file.contains(".pptx"))
            return true;
        else if (file.contains(".xls") || file.contains(".xlsx"))
            return true;
        else if (file.contains(".zip") || file.contains(".rar"))
            return true;
        else if (file.contains(".rtf"))
            return true;
        else if (file.contains(".wav") || file.contains(".mp3"))
            return true;
        else if (file.contains(".gif"))
            return true;
        else if (file.contains(".jpg") || file.contains(".jpeg") || file.contains(".png"))
            return true;
        else if (file.contains(".txt"))
            return true;
        else
            return file.contains(".3gp") || file.contains(".mpg") || file.contains(".mpeg") || file.contains(".mpe") || file.contains(".mp4") || file.contains(".avi");
    }
    public static boolean isFileDownloaded(String file) {
        File location = new File(path + file);
        return location.exists();
    }
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getFileSize(Long size) {
        if (size <= 0)
            return "0";
        String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size / Math.log10(1024)));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static File getDocFilePath(String file) {
        return new File(path + "Documents/" + file);
    }

    public static File getContentFilePath(String file) {
        return new File(path + "Contents/" + file);
    }
}
