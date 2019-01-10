package tube.one.game.app.one;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.*;

import java.io.IOException;

public  class VisualizerDialogFragment extends DialogFragment implements
        TextureView.SurfaceTextureListener  {

    public static VisualizerDialogFragment newInstance() {
        VisualizerDialogFragment fragment = new VisualizerDialogFragment();
//        Bundle b = new Bundle();
//        b.putStringArrayList("PRESETS", presents);
//        fragment.setArguments(b);
        return fragment;
    }

    public VisualizerDialogFragment(){

    }
    private  final String TAG = getClass().getSimpleName();

    private void log(String s){
        Log.d(TAG, s);
    }

    private  boolean enabled = true;


    private Camera mCamera;
    private TextureView mTextureView, overlay;



    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.visualizer_dialog, container, false);


        mTextureView = v.findViewById(R.id.visTexture);
        mTextureView.setTag("cam");
        mTextureView.setSurfaceTextureListener(this);
        mTextureView.setAlpha(.5f);
        overlay = v.findViewById(R.id.visOver);
        overlay.setTag("over");
        overlay.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
                log("Overlay Available!");
//
            //  final Drawable picture = getActivity().getResources().getDrawable(R.drawable.android_robot_icon_2);
            Canvas canvas = overlay.lockCanvas();
             Paint p = new Paint();
             p.setColor(Color.CYAN);
             p.setStrokeWidth(4f);
             canvas.drawLine(canvas.getWidth()/2, canvas.getHeight()/3,
                     canvas.getWidth()/2, canvas.getHeight()/3*2, p);
             canvas.drawLine(canvas.getWidth()/3, canvas.getHeight()/2,
                        canvas.getWidth()/3*2, canvas.getHeight()/2, p);


           // canvas.drawCircle( canvas.getWidth()/2, canvas.getHeight()/2, 200f, p);
            overlay.unlockCanvasAndPost(canvas);

//            overlay.setAlpha(.5f);

            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

            }
        });
        overlay.setAlpha(.5f);

       // ((FullscreenActivity) getActivity()).visualizerCreated();
        return v;

    }



    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

        log("SurfaceTexture Available!");

         log("Camera surface Available!");

            mCamera = Camera.open();

            try {


                //mCamera.setDisplayOrientation(90);
                setCameraDisplayOrientation(1, mCamera);
                mCamera.setPreviewTexture(surface);
                mCamera.startPreview();
            } catch (IOException ioe) {
                // Something bad happened
            }

    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        // Ignored, Camera does all the work for us
    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mCamera.stopPreview();
        mCamera.release();
        return true;
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        // Invoked every time there's a new Camera preview frame
    }

    public  void setCameraDisplayOrientation(int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = getActivity().getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }
        log("Rotation" + rotation);
        log("Degree" + degrees);
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        log("Result" + result);
        camera.setDisplayOrientation(result);
    }

//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//
//        builder.setTitle("Now Playing: ");
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                ((DrawerActivity) getActivity()).visualizerClosed();
//                dismiss();
//            }
//        });
//        ((DrawerActivity) getActivity()).visualizerCreated();
//        return builder.create();
//    }
}