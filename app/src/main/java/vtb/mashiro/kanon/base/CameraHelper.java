package vtb.mashiro.kanon.base;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest.Builder;
import android.hardware.camera2.params.OutputConfiguration;
import android.hardware.camera2.params.SessionConfiguration;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;

import androidx.annotation.NonNull;

import com.ori.origami.jni.OriPPN;
import com.origami.utils.Ori;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import vtb.mashiro.kanon.util.Funichi;

public final class CameraHelper {

    // 摄像头设备
    private CameraDevice mCameraDevice;
    // 摄像头会话
    private CameraCaptureSession mCameraSession;
    // 所有展示摄像头画面的地方
    private List<Surface> mSurfaceList;

    private final CameraCaptureSession.StateCallback mSessionCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            mCameraSession = session;
            try {
                Builder builder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                if (mSurfaceList != null) for (Surface surface : mSurfaceList) {
                    builder.addTarget(surface);
                }
                mCameraSession.setRepeatingRequest(builder.build(), null, null);
            } catch (CameraAccessException e) {
                Log.d("CameraHelper", e.getMessage());
            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
            Log.e("CameraHelper", "ConfigureFailed");
        }
    };
    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            mCameraDevice = camera;
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    List<OutputConfiguration> configurationList = new ArrayList<>();
                    for (Surface surface : mSurfaceList) {
                        configurationList.add(new OutputConfiguration(surface));
                    }
                    SessionConfiguration config = new SessionConfiguration(
                            SessionConfiguration.SESSION_REGULAR,
                            configurationList,
                            Executors.newFixedThreadPool(5),
                            mSessionCallback);
                    camera.createCaptureSession(config);
                } else {
                    camera.createCaptureSession(mSurfaceList, mSessionCallback, null);
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            Log.i("CameraHelper", String.format("Disconnected:%s", camera.getId()));
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            Log.e("CameraHelper", String.format("Error:%d", error));
            if(instance != null) instance.end();
        }
    };

    public void end(){
        closeCamera();
        if(instance != null) instance.end();
    }

    // 打开摄像头
    ImageReader imageReader = ImageReader.newInstance(640, 480, ImageFormat.YUV_420_888, 2);
    OriPPN instance;
    byte[] y = null;
    byte[] u = null;
    byte[] v = null;
    byte[] bufferUV = null;

    private void fillBytes(Image.Plane plane, byte[] src, Funichi<byte[]> fill){
        ByteBuffer buffer = plane.getBuffer();
        if(src == null)
            fill.invoke(src = new byte[buffer.capacity()]);
        buffer.get(src, 0, buffer.capacity());
    }

    @SuppressLint("MissingPermission")
    public void openCamera(Context context, String cameraId, Surface... surfaces) {
        if (context == null) return;
        if (mCameraDevice != null) closeCamera();
        Surface surface = imageReader.getSurface();
        instance = OriPPN.instance(Ori.getSaveFilePath(context, Environment.DIRECTORY_MOVIES) + "101.mp4", 640, 480);
        imageReader.setOnImageAvailableListener(reader -> {
            Image image = reader.acquireLatestImage();
            if(image == null) return;
            Image.Plane[] planes = image.getPlanes();
            if(planes == null) return;
            fillBytes(planes[0], y, bytes -> y = bytes);
            fillBytes(planes[1], u, bytes -> u = bytes);
//            fillBytes(planes[2], v, bytes -> v = bytes);
//            Image.Plane plane = planes[0];
//            ByteBuffer buffer = plane.getBuffer();
//            byte[] dst = new byte[buffer.capacity()];
//            buffer.get(dst);
//            int length = dst.length / 4;
//            int[] pix = new int[length];
//            for (int i = 0; i < length; i++) {
//                pix[i] = dst[i]  | (dst[i + 1] << 8) | (dst[i + 1] << 16) | (dst[i + 1] << 32);
//            }
            if(instance != null) {
                instance.writeNV21(y, u);
            }
            image.close();
        }, null);
        mSurfaceList = new ArrayList<>();
        if(surfaces != null && surfaces.length != 0){
            mSurfaceList.addAll(Arrays.asList(surfaces));
        }
        mSurfaceList.add(surface);
        CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        try {
            if(cameraId == null) for (String s : manager.getCameraIdList()) {
                Ori.d("ORI", String.format("getCameraIdList: %s ", s));
                cameraId = s;
            }
            manager.openCamera(cameraId, mStateCallback, null);
        } catch (CameraAccessException e) {Ori.e("ORI", e);}
    }

    // 关闭摄像头
    public void closeCamera() {
        if (mCameraDevice != null) {
            mCameraDevice.close();
            mCameraSession = null;
            mSurfaceList = null;
        }
    }

    // 拍照
    public void Take(Surface surface , CameraCaptureSession.CaptureCallback callback) {
        if (mCameraSession != null) {
            try {
                Builder request = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
                request.addTarget(surface);
                mCameraSession.capture(request.build(), callback, null);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

//    ImageReader.OnImageAvailableListener onImageAvailableListener = reader -> {
//        Image image = reader.acquireLatestImage();
//        if (image == null) return;
//        Image.Plane[] planes = image.getPlanes();
//        int offset = 0;
//        if (planes[1].getPixelStride() == 1) {
//            for (Image.Plane plane : planes) {
//                ByteBuffer buffer = plane.getBuffer();
//                int capacity = buffer.capacity();
//                if (yuv420 == null) {
//                    Log.e("ORI", "IMAGE TYPE-> YUV420P");
//                    yuv420 = new byte[capacity * 2];
//                }
//                buffer.get(yuv420, offset, capacity);
//                offset += capacity;
//            }
//            instance.write(yuv420);
//        } else if (planes[1].getPixelStride() == 2) {
//            ByteBuffer buffer0 = planes[0].getBuffer();
//            int capacity0 = buffer0.capacity();
//            if (yuv420 == null) {
//                Log.e("ORI", "IMAGE TYPE-> YUV420SP");
//                yuv420 = new byte[capacity0 * 2];
//            }
//            buffer0.get(yuv420, offset, capacity0);
//            offset = capacity0;
//            ByteBuffer buffer1 = planes[1].getBuffer();
//            ByteBuffer buffer2 = planes[2].getBuffer();
//            if (bufferUV == null) {
//                bufferUV = new byte[buffer1.capacity()];
//            }
//            buffer1.get(bufferUV, 0, buffer1.capacity());
//            for (int i = 0; i < buffer1.capacity(); i++) {
//                if (i % 2 == 0) {
//                    yuv420[offset] = bufferUV[i];
//                    offset++;
//                }
//            }
//            buffer2.get(bufferUV, 0, buffer2.capacity());
//            for (int i = 0; i < buffer2.capacity(); i++) {
//                if (i % 2 == 0) {
//                    yuv420[offset] = bufferUV[i];
//                    offset++;
//                }
//            }
//        } else {
//            Log.e("ORI", "IMAGE TYPE-> UNKNOWN");
//        }
//        image.close();
//    };

}