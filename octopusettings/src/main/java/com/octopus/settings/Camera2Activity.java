package com.octopus.settings;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.os.Build;
import android.os.Bundle;
import android.util.Range;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.octopus.settings.utils.CameraUtils;
import com.octopus.settings.utils.FlyLog;
import com.octopus.settings.utils.SystemPropTools;

import java.util.Arrays;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class Camera2Activity extends AppCompatActivity implements TextureView.SurfaceTextureListener {
    private TextureView mTextureView;
    private CameraManager mCameraManager;
    private CameraCaptureSession mCameraCaptureSession;
    private CameraDevice mCameraDevice;
    private Surface mPreviewSurface;

    private EditText et_webcam_url;
    private static final String WEBCAM_URL = "persist.sys.webcam.url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2);
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        mTextureView = (TextureView) this.findViewById(R.id.ac_main_tuv);
        mTextureView.setSurfaceTextureListener(this);

        et_webcam_url = findViewById(R.id.et_webcam_url);
        et_webcam_url.setText(SystemPropTools.get(WEBCAM_URL, ""));
    }

    private void openCamera() {
        try {
            if (ActivityCompat.checkSelfPermission(Camera2Activity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mCameraManager.openCamera("0", new CameraDevice.StateCallback() {
                @Override
                public void onOpened(CameraDevice camera) {
                    mCameraDevice = camera;
                    try {
                        mCameraDevice.createCaptureSession(Arrays.asList(mPreviewSurface), new CameraCaptureSession.StateCallback() {
                            @Override
                            public void onConfigured(CameraCaptureSession arg0) {
                                mCameraCaptureSession = arg0;
                                try {
                                    CaptureRequest.Builder builder;
                                    builder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                                    Range<Integer>[] fpsRanges = CameraUtils.getCameraFps(Camera2Activity.this);
                                    builder.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, fpsRanges[0]);
                                    builder.addTarget(mPreviewSurface);
                                    mCameraCaptureSession.setRepeatingRequest(builder.build(), null, null);
                                } catch (CameraAccessException e1) {
                                    e1.printStackTrace();
                                }
                            }

                            @Override
                            public void onConfigureFailed(CameraCaptureSession arg0) {
                            }
                        }, null);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(CameraDevice camera, int arg1) {
                    camera.close();
                }

                @Override
                public void onDisconnected(CameraDevice camera) {
                    camera.close();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void closeCamera() {
        try {
            if (mCameraDevice != null) {
                mCameraDevice.close();
            }
        }catch (Exception e){
            FlyLog.e(e.toString());
        }
    }

    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
        mPreviewSurface = new Surface(surface);
        openCamera();
    }

    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
        closeCamera();
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {

    }

    public void onSaveSetting(View view) {
        try {
            SystemPropTools.set(WEBCAM_URL, et_webcam_url.getText().toString());
            closeCamera();
            openCamera();
        } catch (Exception e) {
            FlyLog.e(e.toString());
        }
    }
}