/*
package com.example.ahmed.listener;

import android.annotation.TargetApi;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.example.ahmed.io.UploadFile;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.zip.GZIPOutputStream;*/


/*
*
 * Created by ahmed on 05/07/16.

*/


/*

public class AppUsageStats {
    private static FileWriter mFileWriter;
    private static File filename;
    private static Context context;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("M-d-yyyy HH:mm:ss");
    public static final String TAG = AppUsageStats.class.getSimpleName();
    private GZIPOutputStream writer;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("ResourceType")
    public static void getStats(Context context){
        UsageStatsManager usm = (UsageStatsManager) context.getSystemService("usagestats");
        int interval = UsageStatsManager.INTERVAL_YEARLY;
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.YEAR, -1);
        long startTime = calendar.getTimeInMillis();

        Log.d(TAG, "Range start:" + dateFormat.format(startTime) );
        Log.d(TAG, "Range end:" + dateFormat.format(endTime));

        UsageEvents uEvents = usm.queryEvents(startTime,endTime);
        while (uEvents.hasNextEvent()){
            UsageEvents.Event e = new UsageEvents.Event();
            uEvents.getNextEvent(e);

            if (e != null){
                Log.d(TAG, "Event: " + e.getPackageName() + "\t" +  e.getTimeStamp());
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static List<UsageStats> getUsageStatsList(Context context){

        UsageStatsManager usm = getUsageStatsManager(context);
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.YEAR, -1);
        long startTime = calendar.getTimeInMillis();

        Log.d(TAG, "Range start:" + dateFormat.format(startTime) );
        Log.d(TAG, "Range end:" + dateFormat.format(endTime));

        List<UsageStats> usageStatsList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,startTime,endTime);
        return usageStatsList;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void printUsageStats(List<UsageStats> usageStatsList) throws IOException {
        for (UsageStats u : usageStatsList){
            Log.d(TAG, "Pkg: " + u.getPackageName() +  "\t" + "ForegroundTime: "
                    + u.getTotalTimeInForeground());

            filename = createFile(u);
            String row = "" + u.getFirstTimeStamp();
            row += "," +

            //String Dir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+"CSVData";
            File directory = new File(Environment.getExternalStorageDirectory(),"CSVData");
            directory = new File(directory, "AppUsage");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            String fileName = "AppUsageStats.csv";
            String filePath = directory + File.separator + fileName;
            File f = new File(filePath );
            CSVWriter writer;
            // File exist
            if(f.exists() && !f.isDirectory()){
                mFileWriter = new FileWriter(filePath , true);
                writer = new CSVWriter(mFileWriter);
            }
            else {
                writer = new CSVWriter(new FileWriter(filePath));
            }
            String[] data = {u.getPackageName(), String.valueOf(u.getTotalTimeInForeground())};

            writer.writeNext(data);

            writer.close();
        }
        UploadFile.uploadFile(context,filename);
    }

    public static void printCurrentUsageStatus(Context context) throws IOException {
        printUsageStats(getUsageStatsList(context));
    }
    @SuppressWarnings("ResourceType")
    private static UsageStatsManager getUsageStatsManager(Context context){
        UsageStatsManager usm = (UsageStatsManager) context.getSystemService("usagestats");
        return usm;
    }
}
*/

