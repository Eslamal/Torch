package com.example.torch;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    ImageButton imageButton;
    private CameraManager cameraManager;
    private String cameraId;
    private boolean isTorchOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageButton = findViewById(R.id.torchbtn);



        // Check if device supports flash
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            // Device doesn't support flash
            imageButton.setEnabled(false);
        } else {
            // Initialize CameraManager
            cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            imageButton.setImageResource(R.drawable.torch_off);

            try {
                // Get the camera ID
                cameraId = cameraManager.getCameraIdList()[0];
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }

            //  torch on button click
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    torch();
                }
            });
        }
    }


    private void torch() {
        try {
            //  torch state
            isTorchOn = !isTorchOn;
            if(!isTorchOn){
                imageButton.setImageResource(R.drawable.torch_off);
            }
            else{
                imageButton.setImageResource(R.drawable.torch_on);
            }

            // Turn on/off the torch
           cameraManager.setTorchMode(cameraId,isTorchOn);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Turn off the torch when the app is stopped
        if (isTorchOn) {
            torch();
        }
    }
}
