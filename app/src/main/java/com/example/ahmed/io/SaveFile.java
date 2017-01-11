package com.example.ahmed.io;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import com.example.ahmed.service.PhoneCallService;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ahmed on 23/06/16.
 */
public class SaveFile {

    //MediaRecorder recorder = TService.recorder;
    File audiofile;
    //String audio_format;
    //public String Audio_Type;
    //int audioSource;

    public File startRecording(String method, String filename, String state) {
        Log.d("Recording Started", "Yo I have been called");
        String out = new SimpleDateFormat("dd-MM-yyyy hh-mm-ss").format(new Date());
        File sampleDir = new File(Environment.getExternalStorageDirectory(), "/RecordedCalls");
        if (!sampleDir.exists()) {
            sampleDir.mkdirs();
        }
        String file_name = filename;
        try {
            //audiofile = File.createTempFile(filename, ".amr", sampleDir);
            audiofile = new File(sampleDir, file_name + "-" + out + state + ".wav");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("save exception", "error" + e);
        }
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();

        Log.d("message", method);
        switch (method) {
            case "DEFAULT":
                PhoneCallService.recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_UPLINK);
                break;
            case "VOICE_COMMUNICATION":
                PhoneCallService.recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
                break;
            case "VOICE_CALL":
                PhoneCallService.recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_UPLINK);
                break;
            default:
                PhoneCallService.recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
                break;
        }
        PhoneCallService.recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        PhoneCallService.recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        PhoneCallService.recorder.setOutputFile(audiofile.getAbsolutePath());
        try {
            PhoneCallService.recorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Log.d("Problem", "recorder.prepare has problem");
        } catch (IOException e) {
            e.printStackTrace();
        }
        PhoneCallService.recorder.start();
        return audiofile;
    }


    public void stopRecording() {
        PhoneCallService.recorder.stop();
    }
}
