package com.example.ahmed.utils;

import android.hardware.Sensor;

/**
 * Created by Khalil on 21-Jun-16.
 */
public class Constants {

    public static String FTP_USER_NAME = "ahmed";
    public static String FTP_PWD = "ahmed";
    public static String FTP_HOST = "vps393079.ovh.net";

    public static String PATH_OF_FILE = "/var/www/html/android_login_api/";


    public static String mainDirectoryName = "CSVData";

    public enum SensorType {
        Accelerometer("AccelerometerData", "ACC_%1s.csv", Sensor.TYPE_ACCELEROMETER);
        //Gyroscope("GyroscopeData", "Gyro_%1s.csv", Sensor.TYPE_GYROSCOPE),
        //Light("LightData", "Light_%1s.csv", Sensor.TYPE_LIGHT),
        //Magnetometer("MagnetometerData", "Magnetometer_%1s.csv", Sensor.TYPE_MAGNETIC_FIELD);

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
