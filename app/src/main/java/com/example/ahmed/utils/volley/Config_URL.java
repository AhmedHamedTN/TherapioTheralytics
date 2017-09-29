package com.example.ahmed.utils.volley;

//This class is for storing all URLs as a model of URLs

public class Config_URL
{
	private static String base_URL = "http://192.168.43.225/";		//Default configuration for WAMP - 80 is default port for WAMP and 10.0.2.2 is localhost IP in Android Emulator

	// "http://vps393079.ovh.net:80/"
	// Server user login url
	//public static String URL_LOGIN = "http://www.anis.tunisia-webhosting.com/anis.tunisia-webhosting.com/ahmed/cfg/";
	public static String URL_LOGIN = base_URL+"android_login_api/index.php";

	// Server user register url
	//public static String URL_REGISTER = "http://www.anis.tunisia-webhosting.com/anis.tunisia-webhosting.com/ahmed/cfg/";
	public static String URL_REGISTER = base_URL+"android_login_api/index.php";

	// Server user storePath url
	//public static String URL_STORE_PATH = "http://www.anis.tunisia-webhosting.com/anis.tunisia-webhosting.com/ahmed/cfg/insertPath2.php";
	public static String URL_STORE_PATH = base_URL+"android_login_api/insertPath2.php";

	// Server user storePath url
	//public static String URL_STORE_FB = "http://www.anis.tunisia-webhosting.com/anis.tunisia-webhosting.com/ahmed/cfg/insertFB.php";
	public static String URL_STORE_FB = base_URL+"android_login_api/insertFB.php";

	public static String URL_STORE_LOCATION = base_URL+"android_login_api/insertLocation.php";

	public static String URL_STORE_RESULT_BDI = base_URL+"android_login_api/insertResult.php";

	public static String URL_STORE_MOOD = base_URL+"android_login_api/insertMood.php";

	public static String URL_STORE_APPETITE = base_URL+"android_login_api/insertAppetite.php";

	public static String URL_STORE_WEIGHT = base_URL+"android_login_api/insertWeight.php";

	public static String URL_STORE_CONSUMPTION = base_URL+"android_login_api/insertConsumption.php";

	public static String URL_STORE_SLEEP = base_URL+"android_login_api/insertSleep.php";

	public static String URL_EXTRACT_AUDIO_FTR = "http://192.168.43.225:9988/extractAudio";
	//public static String URL_EXTRACT_AUDIO_FTR = "http://vps393079.ovh.net:8888/extractAudio";
	//public static String URL_EXTRACT_AUDIO_FTR = "vps373666.ovh.net:8080/flasktest1/extractAudio";


	public static String URL_EXTRACT_CSV_FTR = "http://192.168.43.225:9988/extractCSV";
	//public static String URL_EXTRACT_CSV_FTR = "http://vps393079.ovh.net:8888/extractCSV";
	//public static String URL_EXTRACT_CSV_FTR = "vps373666.ovh.net:8080/flasktest1/extractCSV";


	//public static String URL_UPLOAD_FILE = "http://192.168.1.175:9999/uploadfile";

	public static String URL_STORE_ACC_File = "http://192.168.43.225/android_login_api/uploadFile_Acc.php";
	public static String URL_STORE_PHONE_File = base_URL+"android_login_api/uploadFile_PhoneCalls.php";
	public static String URL_STORE_PHONE = "http://192.168.43.225/android_login_api/uploadAudio.php";


	//public static String URL_REQ= base_URL+"/android_login_api/rea";

	//public static String URL_INSERT_FILE = base_URL+"/android_login_api/";
/*
	private static final String URL_JSON_OBJECT = "http://api.androidhive.info/volley/person_object.json";
	private static final String URL_JSON_ARRAY = "http://api.androidhive.info/volley/person_array.json";
	private static final String URL_STRING_REQ = "http://api.androidhive.info/volley/string_response.html";
	private static final String URL_IMAGE = "http://api.androidhive.info/volley/volley-image.jpg";

	//If you need any parameter passed with the URL (GET) - U need to modify this functions
	public static String get_JSON_Object_URL()
	{
		return URL_JSON_OBJECT;
	}

	public static String get_JSON_Array_URL()
	{
		return URL_JSON_ARRAY;
	}

	public static String get_String_URL(String Input)
	{
		if(Input.length()>0) {
			return Input;
		}
		return URL_STRING_REQ;
	}

	public static String get_Image_URL()
	{
		return URL_IMAGE;
	}
	*/
}
