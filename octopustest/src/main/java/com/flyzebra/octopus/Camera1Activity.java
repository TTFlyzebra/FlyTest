package com.flyzebra.octopus;

import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Camera1Activity extends AppCompatActivity implements SurfaceHolder.Callback{

    private SurfaceView ac_main_sv;
    private Camera mCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera1);

        ac_main_sv = findViewById(R.id.ac_main_sv);

        ac_main_sv.getHolder().addCallback(this);
        ac_main_sv.getHolder().setFormat(PixelFormat.YCbCr_420_SP);

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        if(mCamera==null){
            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            try {
                mCamera.setPreviewDisplay(ac_main_sv.getHolder());
                Camera.Parameters param = mCamera.getParameters();
                param.setJpegQuality(90);
                param.setPreviewSize(param.getSupportedPreviewSizes().get(0).width,param.getSupportedPreviewSizes().get(0).height);
                param.setPreviewFormat(ImageFormat.NV21);
                mCamera.setParameters(param);
                setCameraDisplayOrientation();
                mCamera.startPreview();
            } catch (Exception e) {
                FlyLog.e(e.toString());
            }
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    private void setCameraDisplayOrientation() {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, info);
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            FlyLog.e("CAMERA_FACING_FRONT");
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            FlyLog.e("CAMERA_FACING_BACK");
            result = (info.orientation - degrees) % 360;
        }
        FlyLog.e("info.orientation=%d, rotation=%d",info.orientation,result);
        //旋转预览的角度
        mCamera.setDisplayOrientation(result);
        //-----------------------------
        //旋转生成的照片角度
        //Camera.Parameters param = mCamera.getParameters();
        //param.setRotation(result);
        //mCamera.setParameters(param);
    }
}