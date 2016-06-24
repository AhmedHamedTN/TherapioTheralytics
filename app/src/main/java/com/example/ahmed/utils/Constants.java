package com.example.ahmed.utils;

import android.hardware.Sensor;
import android.hardware.SensorManager;

/**
 * Created by Khalil on 21-Jun-16.
 */
public class Constants {

  public  static String FTP_USER_NAME= "ahmed@anis.tunisia-webhosting.com";
   public static String FTP_PWD = "ahmedahmed";
    public static String FTP_HOST = "ftp.anis.tunisia-webhosting.com";


    public static String mainDirectoryName = "CSVData";

    public enum SensorType {
        Accelerometer("AccelerometerData", "ACC_%1s.csv", Sensor.TYPE_ACCELEROMETER), Gyroscope("GyroscopeData", "Gyro_%1s.csv", Sensor.TYPE_GYROSCOPE), Light("LightData", "Light_%1s.csv", SensorManager.SENSOR_STATUS_ACCURACY_HIGH);

        public String directoryName;
        public String fileName;
        int type;

        SensorType(String directoryName, String fileName, int type) {
            this.directoryName = directoryName;
            this.fileName = fileName;
            this.type = type;
        }

        public String getDirectoryName() {
            return directoryName;
        }

        public String getFileName() {
            return fileName;
        }

        public int getType() {
            return type;
        }
    }


}
