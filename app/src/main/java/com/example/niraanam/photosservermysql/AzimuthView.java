package com.example.niraanam.photosservermysql;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class AzimuthView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder holder;
    private Camera mCamera;

    public AzimuthView(Context context, Camera camera) {

        super(context);

        mCamera = camera;
        mCamera.setDisplayOrientation(90);
        holder = getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        try{
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();

        } catch(IOException e) {
            //Log.d(getResources().getString(R.string.error), "Camera error on surfaceCreated" + e.getMessage());
        }

    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int i, int j, int k) {

        if(holder.getSurface() == null) {
            return;
        }
        try{
            mCamera.stopPreview();

        } catch(Exception e) {

        }

        try {

            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();

        } catch(IOException e) {
            //Log.d(getResources().getString(R.string.error), "Camera error on surface changed" + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        mCamera.stopPreview();
        mCamera.release();
    }
}
