package com.example.plugin;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import android.os.Environment;

public class Hello extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {

        if (action.equals("greet")) {

            //get the package name
            String packageName = data.getString(0);

            File crosswalkDB = new File("/data/data/" + packageName + "/app_xwalkcore/Default/databases/file__0/1/");
            File crosswalkDataMigrated = new File("/data/data/" + packageName + "/app_webview/databases/crosswalkDataMigrated.txt/");

            android.util.Log.v("crosswalkDB.exists : ", "" + crosswalkDataMigrated.exists());
            android.util.Log.v("crosswalkDB.length : ", "" + crosswalkDataMigrated.length());

            if(! crosswalkDataMigrated.exists()) {

                //TODO : check if crosswalk is installed, the websql exists and websql not migrated already
                File crosswalkPath = new File("/data/data/" + packageName + "/app_xwalkcore/Default/databases/file__0");
                crosswalkPath.mkdirs();

                File from = new File("/data/data/" + packageName + "/app_webview/databases/file__0/1");
                File to = new File("/data/data/" + packageName + "/app_xwalkcore/Default/databases/file__0/", "1");

                try {
                    //copy the database
                    this.copyFile(from, to);

                    //create a txt file as a flag
                    java.io.FileWriter fileWriter = new java.io.FileWriter(crosswalkDataMigrated);
                    fileWriter.write("crosswalk data migration completed");
                    fileWriter.flush();
                    fileWriter.close();

                } catch (IOException e) {
                    System.err.println("Caught IOException: " + e.getMessage());
                }
            }


            /*
            File directory = new File("/data/data/com.unit11apps.crosswalkMigration/app_xwalkcore");
            if (! directory.exists()){
                directory.mkdir();
            }

            File directory1 = new File("/data/data/com.unit11apps.crosswalkMigration/app_xwalkcore/Default");
            if (! directory1.exists()){
                directory1.mkdir();
            }

            File directory2 = new File("/data/data/com.unit11apps.crosswalkMigration/app_xwalkcore/Default/databases");
            if (! directory2.exists()){
                directory2.mkdir();
            }

            File directory3 = new File("/data/data/com.unit11apps.crosswalkMigration/app_xwalkcore/Default/databases/file__0");
            if (! directory3.exists()){
                directory3.mkdir();
            }


            File from = new File("/data/data/" + packageName + "/app_webview/databases/file__0/1");
            File to = new File("/data/data/" + packageName + "/app_xwalkcore/Default/databases/file__0/", "1");

            try {
                this.copyFile(from, to);
            }catch(IOException e)
            {
                System.err.println("Caught IOException: " + e.getMessage());
            }



            /////////////////

            String path = "/data/data/" + packageName + "/app_xwalkcore/Default/databases/file__0";
            android.util.Log.d("Files in ------- ", "Path: " + path);
            File directory4 = new File(path);
            File[] files = directory4.listFiles();
            android.util.Log.d("Files", "Size: "+ files.length);
            for (int i = 0; i < files.length; i++)
            {
                android.util.Log.d("Files", "FileName:" + files[i].getName());
            }


            /////////////////
    */

            String message = "SUCCESSFULLY MIGRATED DATABASE TO CROSSWALK";
            callbackContext.success(message);

            return true;

        } else {
            
            return false;

        }
    }

    public static void copyFile(File src, File dst) throws IOException
    {
        android.util.Log.d("MIGRATING", "DATABASE:");
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();
        try
        {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        }
        finally
        {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
        }

    }
}
