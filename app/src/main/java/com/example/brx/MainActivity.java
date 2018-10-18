package com.example.brx;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Build;
import android.hardware.Camera;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SurfaceView surfaceview1, surfaceview2,surfaceview3,surfaceview4;
    SurfaceHolder surfaceholder1, surfaceholder2,surfaceholder3, surfaceholder4;
    String TAG = "MainActivity";
    private Camera camera1 = null, camera2 = null,camera3 = null, camera4 = null;
    Camera.Parameters parameters;
    private Button btnSwitch, btnVideo, btnLoc;
    private TextView postionView;
    private String locationProvider;
    private LocationManager locationManager;
    private SeekBar SaturationseekBar = null;
    private SeekBar BrightnessseekBar1 = null,BrightnessseekBar2 = null,BrightnessseekBar3 = null,BrightnessseekBar4 = null;
    private SeekBar ContrastseekBar1 = null,ContrastseekBar2 = null,ContrastseekBar3 = null,ContrastseekBar4 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 判断权限API6以上，否则preview发生错误
//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
//                == PackageManager.PERMISSION_GRANTED) {
//            Log.i("TEST","Granted");
//            //init(barcodeScannerView, getIntent(), null);
//        } else {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{ android.Manifest.permission.CAMERA}, 0);//1 can be another integer
//            Log.i("TEST1","Granted1");
//        }
        setContentView(R.layout.activity_main);
        surfaceview1 = (SurfaceView) findViewById(R.id.surfaceview1);
        surfaceview2 = (SurfaceView) findViewById(R.id.surfaceview2);
        surfaceview3 = (SurfaceView) findViewById(R.id.surfaceview3);
        surfaceview4 = (SurfaceView) findViewById(R.id.surfaceview4);

        surfaceholder1 = surfaceview1.getHolder();
        //surfaceholder1.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceholder1.addCallback(new surfaceholderCallbackBack());

        surfaceholder2 = surfaceview2.getHolder();
        //surfaceholder2.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        //surfaceholder2.addCallback(new surfaceholderCallbackFont());
//        btnSwitch = (Button) findViewById(R.id.btnswitch);
//        btnVideo = (Button) findViewById(R.id.btnvideo);
//        btnLoc = (Button) findViewById(R.id.btnloc);
        postionView = (TextView) findViewById(R.id.textloc);

//        surfaceholder3 = surfaceview3.getHolder();
//        surfaceholder3.addCallback(new surfaceholderCallbackBack());
//        surfaceholder4 = surfaceview4.getHolder();
//        surfaceholder4.addCallback(new surfaceholderCallbackBack());

        //SaturationseekBar = (SeekBar) findViewById(R.id.Saturationseekbar);
        BrightnessseekBar1 = (SeekBar) findViewById(R.id.Brightnessseekbar1);
        ContrastseekBar1 = (SeekBar) findViewById(R.id.Contrastseekbar1);

        BrightnessseekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Nothing handled here
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Nothing handled here
            }
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

                changeBrightness(camera1, progress);
            }
        });
        ContrastseekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Nothing handled here
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Nothing handled here
            }
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

                changeContrast(camera1, progress);
            }
        });
//        btnSwitch.setOnClickListener(new Button.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "后置摄像头打开", Toast.LENGTH_SHORT).show();
//
//                //camera2.stopPreview();
//                camera1.startPreview();
//            }
//        });

//        btnVideo.setOnClickListener(new Button.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "前置摄像头打开", Toast.LENGTH_SHORT).show();
//                camera1.stopPreview();
//                //camera2.startPreview();
//            }
//        });


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            Toast.makeText(this, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
            return;
        }


        //获取Location
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Location location = locationManager.getLastKnownLocation(locationProvider);
            if(location!=null){
                //不为空,显示地理位置经纬度
                showLocation(location);
            }
            //监视地理位置变化
            locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
            return;
        }
    }
    private void OnSeekBarChangeListener(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        Log.d(TAG, "seekid:"+seekBar.getId()+", progess"+progress);
    }

    private void changeBrightness(Camera camera , float newBrightnessValue) {

        Camera.Parameters params = camera.getParameters();
        int min = params.getMinExposureCompensation(); // -3 on my phone
        int max = params.getMaxExposureCompensation(); // 3 on my phone
        float realProgress = newBrightnessValue + min;
        int value=(int)realProgress;
//        if (realProgress < 0) {
//            value = -(int) (realProgress );
//        } else {
//            value = (int) (realProgress * 2 * max);
//        }
        if (value != params.getExposureCompensation()) {
            params.setExposureCompensation(value);
            camera.setParameters(params);
        }
    }
    private void changeContrast(Camera camera , float newContrastValue) {


        Camera.Parameters camParams = camera.getParameters();
        String key="";
        camParams.get(key);
        String parmListStr  = camParams.flatten();
        String[] parms = parmListStr.split(";");
        int maxContrast = 0, curContrast = 0, newContrast = 0;
        for(String str:parms){
            if(str.contains("max-contrast=")){
                String[] values = str.split("=");
                maxContrast = Integer.getInteger(values[1]);
            } else if (str.contains("contrast=")){
                String[] values = str.split("=");
                curContrast = Integer.getInteger(values[1]);
            }
        }

        if (maxContrast > 0 && curContrast >= 0){
            //calculate contrast as per your needs and set it to camera parameters as below
            newContrast = (curContrast + 1) < maxContrast? (curContrast + 1): maxContrast;
            camParams.set("contrast", newContrast);
            camera.setParameters(camParams);
        }


//        Camera.Parameters params = camera.getParameters();
//        int min = params.getContrast(); // -3 on my phone
//        int max = params.getMaxExposureCompensation(); // 3 on my phone
//        float realProgress = newContrastValue + min;
//        int value=(int)realProgress;
////        if (realProgress < 0) {
////            value = -(int) (realProgress );
////        } else {
////            value = (int) (realProgress * 2 * max);
////        }
//        if (value != params.getExposureCompensation()) {
//            params.setExposureCompensation(value);
//            camera.setParameters(params);
//        }
    }
    private void showLocation(Location location){
        String locationStr = "维度：" + location.getLatitude() +"\n"
                + "经度：" + location.getLongitude();
        postionView.setText(locationStr);
    }
    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
    }

    LocationListener locationListener =  new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLocationChanged(Location location) {
            //如果位置发生变化,重新显示
            showLocation(location);

        }
    };

    /**
     * 后置摄像头回调
     */

    class surfaceholderCallbackBack implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // 获取camera对象
            int cameraCount = Camera.getNumberOfCameras();
            if (cameraCount > 0) {
                try
                {
                    camera1 = Camera.open(0); // attempt to get a Camera instance
                }
                catch (Exception e)
                {
                    Log.e(TAG, e.toString());
                }
                //camera1 = Camera.open(0);

                try {
                    // 设置预览监听
                    camera1.setPreviewDisplay(holder);
                    Camera.Parameters parameters = camera1.getParameters();

                    if (MainActivity.this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                        parameters.set("orientation", "portrait");
                        camera1.setDisplayOrientation(90);
                        parameters.setRotation(90);
                    } else {
                        parameters.set("orientation", "landscape");
                        camera1.setDisplayOrientation(0);
                        parameters.setRotation(0);
                    }
                    camera1.setParameters(parameters);
                    // 启动摄像头预览
                    camera1.startPreview();
                    //System.out.println("camera.startpreview");

                } catch (IOException e) {
                    e.printStackTrace();
                    camera1.release();
                    System.out.println("camera.release");
                }
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            camera1.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    if (success) {
                        initCamera();// 实现相机的参数初始化
                        camera.cancelAutoFocus();// 只有加上了这一句，才会自动对焦。
                    }
                }
            });

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }

        // 相机参数的初始化设置
        private void initCamera() {
            parameters = camera1.getParameters();
            parameters.setPictureFormat(PixelFormat.JPEG);
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 1连续对焦
            setDispaly(parameters, camera1);
            camera1.setParameters(parameters);
            camera1.startPreview();
            camera1.cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上
        }

        // 控制图像的正确显示方向
        private void setDispaly(Camera.Parameters parameters, Camera camera) {
            if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
                setDisplayOrientation(camera, 90);
            } else {
                parameters.setRotation(90);
            }

        }

        // 实现的图像的正确显示
        private void setDisplayOrientation(Camera camera, int i) {
            Method downPolymorphic;
            try {
                downPolymorphic = camera.getClass().getMethod("setDisplayOrientation", new Class[]{int.class});
                if (downPolymorphic != null) {
                    downPolymorphic.invoke(camera, new Object[]{i});
                }
            } catch (Exception e) {
                Log.e("Came_e", "图像出错");
            }
        }
    }

    class surfaceholderCallbackFont implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {

            // 获取camera对象
            int cameraCount = Camera.getNumberOfCameras();
            if (cameraCount >= 2) {
                try{
                    camera2 = Camera.open(1);
                }
                catch (Exception e)
                {
                    Log.e(TAG, e.toString());
                }
            }
            try {
                // 设置预览监听
                camera2.setPreviewDisplay(holder);
                Camera.Parameters parameters = camera2.getParameters();

                if (MainActivity.this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                    parameters.set("orientation", "portrait");
                    camera2.setDisplayOrientation(90);
                    parameters.setRotation(90);
                } else {
                    parameters.set("orientation", "landscape");
                    camera2.setDisplayOrientation(0);
                    parameters.setRotation(0);
                }
                camera2.setParameters(parameters);
                // 启动摄像头预览
               camera2.startPreview();
                System.out.println("camera.startpreview");

            } catch (IOException e) {
                e.printStackTrace();
                camera2.release();
                System.out.println("camera.release");
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//            camera2.autoFocus(new Camera.AutoFocusCallback() {
//                @Override
//                public void onAutoFocus(boolean success, Camera camera) {
//                    if (success) {
//                        parameters = camera2.getParameters();
//                        parameters.setPictureFormat(PixelFormat.JPEG);
//                        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 1连续对焦
//                        setDispaly(parameters, camera2);
//                        camera2.setParameters(parameters);
//                        camera2.startPreview();
//                        camera2.cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上
//                        camera.cancelAutoFocus();// 只有加上了这一句，才会自动对焦。
//                    }
//                }
//            });

        }
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }

        // 控制图像的正确显示方向
        private void setDispaly(Camera.Parameters parameters, Camera camera) {
            if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
                setDisplayOrientation(camera, 90);
            } else {
                parameters.setRotation(90);
            }

        }

        // 实现的图像的正确显示
        private void setDisplayOrientation(Camera camera, int i) {
            Method downPolymorphic;
            try {
                downPolymorphic = camera.getClass().getMethod("setDisplayOrientation", new Class[]{int.class});
                if (downPolymorphic != null) {
                    downPolymorphic.invoke(camera, new Object[]{i});
                }
            } catch (Exception e) {
                Log.e("Came_e", "图像出错");
            }
        }
    }
}
