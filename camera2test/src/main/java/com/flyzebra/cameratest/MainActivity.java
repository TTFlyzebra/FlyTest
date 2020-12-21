package com.flyzebra.cameratest;

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
import android.view.Surface;
import android.view.TextureView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private TextureView mTextureView;
    private CameraCaptureSession mCameraCaptureSession;
    private CameraDevice mCameraDevice;
    private Surface mPreviewSurface;
    private String TAG = "TANG";

    //private String mCameraId;
    //private Handler mHandler;
    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //预览用的surface
        mTextureView = (TextureView) this.findViewById(R.id.ac_main_tuv);
        mTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {

            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture arg0, int arg1, int arg2) {
                // TODO 自动生成的方法存根
                mPreviewSurface = new Surface(arg0);
                CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                try {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    manager.openCamera("1", new CameraDevice.StateCallback() {
                        @Override
                        public void onOpened(CameraDevice arg0) {
                            mCameraDevice = arg0;
                            try {
                                mCameraDevice.createCaptureSession(Arrays.asList(mPreviewSurface), new CameraCaptureSession.StateCallback() {
                                    @Override
                                    public void onConfigured(CameraCaptureSession arg0) {
                                        // TODO 自动生成的方法存根
                                        mCameraCaptureSession = arg0;
                                        try {
                                            CaptureRequest.Builder builder;
                                            builder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                                            builder.addTarget(mPreviewSurface);
                                            mCameraCaptureSession.setRepeatingRequest(builder.build(), null, null);
                                        } catch (CameraAccessException e1) {
                                            // TODO 自动生成的 catch 块
                                            e1.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onConfigureFailed(CameraCaptureSession arg0) {
                                        // TODO 自动生成的方法存根
                                    }
                                }, null);
                            } catch (CameraAccessException e) {
                                // TODO 自动生成的 catch 块
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(CameraDevice arg0, int arg1) {
                            // TODO 自动生成的方法存根

                        }

                        @Override
                        public void onDisconnected(CameraDevice arg0) {
                            // TODO 自动生成的方法存根

                        }
                    }, null);
                } catch (CameraAccessException e) {
                    // TODO 自动生成的 catch 块
                    e.printStackTrace();
                }
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture arg0) {
                // TODO 自动生成的方法存根
                return false;
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture arg0, int arg1, int arg2) {
                // TODO 自动生成的方法存根

            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture arg0) {
                // TODO 自动生成的方法存根

            }

        });

        //Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        //for (int cameraId = 0; cameraId < Camera.getNumberOfCameras(); cameraId++) {
        //    Camera.getCameraInfo(cameraId, cameraInfo);
        //    Camera camera = Camera.open(cameraId);
        //    Camera.Parameters params = camera.getParameters();
        //    List<Camera.Size> previewSIzes = params.getSupportedPreviewSizes();
        //    List<Camera.Size> pictureSIzes = params.getSupportedPictureSizes();
        //    List<Camera.Size> videoSIzes = params.getSupportedVideoSizes();
        //    Log.e(TAG, "CAMERA:" + params.toString());
        //    camera.release();
        //}
//
        //Size selectSize = null;
        //try {
        //    CameraManager mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        //    for (final String cameraId : mCameraManager.getCameraIdList()) {
        //        CameraCharacteristics cameraCharacteristics = mCameraManager.getCameraCharacteristics(cameraId);
        //        StreamConfigurationMap streamConfigurationMap = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        //        Size[] sizes = streamConfigurationMap.getOutputSizes(ImageFormat.JPEG);
        //        DisplayMetrics displayMetrics = getResources().getDisplayMetrics(); //因为我这里是将预览铺满屏幕,所以直接获取屏幕分辨率
        //        int deviceWidth = displayMetrics.widthPixels; //屏幕分辨率宽
        //        int deviceHeigh = displayMetrics.heightPixels; //屏幕分辨率高
        //        Log.e(TAG, "getMatchingSize2: 屏幕密度宽度=" + deviceWidth);
        //        Log.e(TAG, "getMatchingSize2: 屏幕密度高度=" + deviceHeigh);
        //        /**
        //         * 循环40次,让宽度范围从最小逐步增加,找到最符合屏幕宽度的分辨率,
        //         * 你要是不放心那就增加循环,肯定会找到一个分辨率,不会出现此方法返回一个null的Size的情况
        //         * ,但是循环越大后获取的分辨率就越不匹配
        //         */
//		//		for (int j = 1; j < 41; j++) {
        //        for (int i = 0; i < sizes.length; i++) { //遍历所有Size
        //            Size itemSize = sizes[i];
        //            Log.e(TAG, "当前itemSize 宽=" + itemSize.getWidth() + "高=" + itemSize.getHeight());
        //            //判断当前Size高度小于屏幕宽度+j*5  &&  判断当前Size高度大于屏幕宽度-j*5  &&  判断当前Size宽度小于当前屏幕高度
        //            if (itemSize.getHeight() < (deviceWidth) && itemSize.getHeight() > (deviceWidth)) {
        //                if (selectSize != null) { //如果之前已经找到一个匹配的宽度
        //                    if (Math.abs(deviceHeigh - itemSize.getWidth()) < Math.abs(deviceHeigh - selectSize.getWidth())) { //求绝对值算出最接近设备高度的尺寸
        //                        selectSize = itemSize;
        //                        continue;
        //                    }
        //                } else {
        //                    selectSize = itemSize;
        //                }
//
        //            }
        //        }
        //        if (selectSize != null) { //如果不等于null 说明已经找到了 跳出循环
        //            break;
        //        }
//		//		}
        //    }
        //} catch (CameraAccessException e) {
        //    e.printStackTrace();
        //}
//
    }


}