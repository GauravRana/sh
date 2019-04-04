package com.sydehealth.sydehealth.drawingview;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sydehealth.sydehealth.mail_upload.Screenshots_service;
import com.sydehealth.sydehealth.main.Condition_player;
import com.sydehealth.sydehealth.screen_share.BasicCustomVideoRenderer;
import com.sydehealth.sydehealth.utility.Normal_toast;
import com.sydehealth.sydehealth.utility.UsefullData;
import com.sydehealth.sydehealth.R;
import com.sydehealth.sydehealth.service.Screenshots_deletion_service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import me.tatarka.support.job.JobInfo;
import me.tatarka.support.job.JobScheduler;
import me.tatarka.support.os.PersistableBundle;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    static public DrawingView mDrawingView;
    private int selectedColor;
    private ImageButton mColorPanel;
    private ImageButton mBrush;
    private ImageButton mUndo;
    private Button mSave;
    private ImageButton mCrop;
    private ImageButton mBack;
    private static int COLOR_PANEL = 0;
    //    CropImageView cropImageView;
    Intent i;
    static public String sdcardPath;
    static public Bitmap finalBitmap = null;
    static public Bitmap finalBitmap2 = null;
    UsefullData objUsefullData;
    Button undo_btn, save_btn, back_btn;
    ImageView red_color_btn;
    ImageView blue_color_btn;
    public static LinearLayout colorOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        setContentView(R.layout.activity_drawing);
        i = getIntent();
        sdcardPath = Environment.getExternalStorageDirectory().toString();
        objUsefullData = new UsefullData(MainActivity.this);
        undo_btn = (Button) findViewById(R.id.undo_btn);
        undo_btn.setTypeface(objUsefullData.get_awosome_font());
        red_color_btn = (ImageView) findViewById(R.id.red_color_btn);
        colorOption = (LinearLayout) findViewById(R.id.colorOption);
        //red_color_btn.setTypeface(objUsefullData.get_awosome_font());
        blue_color_btn = (ImageView) findViewById(R.id.blue_color_btn);
        save_btn = (Button) findViewById(R.id.save_btn);
        save_btn.setTypeface(objUsefullData.get_awosome_font());
        back_btn = (Button) findViewById(R.id.back_btn);
        back_btn.setTypeface(objUsefullData.get_awosome_font());
        undo_btn.setOnClickListener(this);
        red_color_btn.setOnClickListener(this);
        blue_color_btn.setOnClickListener(this);
        save_btn.setOnClickListener(this);
        back_btn.setOnClickListener(this);
        initViews();
        //initPaintMode();
        loadImage();

//        mDrawingView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                check_activity(DrawingView.savePath.size() + 1);
//
//                return false;
//            }
//        });

    }

    private void initViews() {
        mDrawingView = (DrawingView) findViewById(R.id.img_screenshot);
        mBrush = (ImageButton) findViewById(R.id.brush);
        mColorPanel = (ImageButton) findViewById(R.id.color_panel);
        mUndo = (ImageButton) findViewById(R.id.undo);
        mSave = (Button) findViewById(R.id.save);
        mSave.setTypeface(objUsefullData.get_semibold());
        mCrop = (ImageButton) findViewById(R.id.back);
        mBack = (ImageButton) findViewById(R.id.crop);
//        cropImageView=(CropImageView) findViewById(R.id.container_cropping);
        mBrush.setOnClickListener(this);
        mColorPanel.setOnClickListener(this);
        mUndo.setOnClickListener(this);
        mSave.setOnClickListener(this);
        mCrop.setOnClickListener(this);
        mBack.setOnClickListener(this);

        initPaintMode();
    }

    private void initPaintMode() {
        mDrawingView.initializePen();
        mDrawingView.setPenSize(20);
        selectedColor = ContextCompat.getColor(this, R.color.screeshot_red);
        mDrawingView.setPenColor(selectedColor);
        red_color_btn.setImageResource(R.drawable.red_color_with_white_border);
        blue_color_btn.setImageResource(R.drawable.blue_color_without_white_border);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:

                finish();

//                if (!mDrawingView.isShown()) {
//                    mDrawingView.loadImage(cropImageView.getCroppedImage());
//                }
//                mDrawingView.setVisibility(View.VISIBLE);
//                cropImageView.setVisibility(View.GONE);
//                mBrush.setImageResource(R.drawable.ic_brush);
//                String[] titles = new String[] { "10","20", "30", "40" };
//                Integer[] images = {R.drawable.stroke_1,
//                        R.drawable.stroke_2,
//                        R.drawable.stroke_3,
//                        R.drawable.stroke_4 };
//                List<RowItem> rowItems = new ArrayList<RowItem>();
//                for (int i = 0; i < titles.length; i++) {
//                    RowItem item = new RowItem(images[i], titles[i]);
//                    rowItems.add(item);
//                }
//
//                final Dialog dialog_pen = new Dialog(this);
//                dialog_pen.setTitle("Select Relation");
//                dialog_pen.setContentView(R.layout.categories);
//                final ListView listCategories = (ListView) dialog_pen
//                        .findViewById(R.id.listCategories);
//                CustomListViewAdapter adapter = new CustomListViewAdapter(this,
//                        R.layout.list_category, rowItems);
//
//                listCategories.setAdapter(adapter);//
//                listCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                    @Override
//                    public void onItemClick(AdapterView<?> parent,
//                                            View view, int position, long id) {
//                        switch(position) {
//                            case 0:
//                                mDrawingView.setPenSize(10);
//                                break;
//                            case 1:
//                                mDrawingView.setPenSize(20);
//                                break;
//                            case 2:
//                                mDrawingView.setPenSize(30);
//                                break;
//                            case 3:
//                                mDrawingView.setPenSize(40);
//                                break;
//                            default:
//                                break;
//                        }
//                        dialog_pen.dismiss();
//                    }
//                });
//                dialog_pen.show();
                break;
            case R.id.blue_color_btn:

                selectedColor = getColor(R.color.screeshot_blue);
                mDrawingView.setPenColor(selectedColor);
                blue_color_btn.setImageResource(R.drawable.blue_color_with_white_border);
                red_color_btn.setImageResource(R.drawable.red_color_without_white_border);
//                if (!mDrawingView.isShown()) {
//                    mDrawingView.loadImage(cropImageView.getCroppedImage());
//                }
//
//                mDrawingView.setVisibility(View.VISIBLE);
//                cropImageView.setVisibility(View.GONE);
//                mColorPanel.setImageResource(R.drawable.ic_color_red);
//                ColorChooserDialog dialog = new ColorChooserDialog(this);
//                dialog.setColorListener(new ColorListener() {
//                    @Override
//                    public void OnColorClick(View v, int color) {
//                        if(color==0xffffffff){
//                            ColorPickerDialogBuilder
//                                    .with(MainActivity.this)
//
//                                    .setTitle("Choose color")
//
////                                .initialColor(currentBackgroundColor)
//                                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
//
//                                    .density(12)
//                                    .setOnColorSelectedListener(new OnColorSelectedListener() {
//                                        @Override
//                                        public void onColorSelected(int selectedColor) {
////                                        toast("onColorSelected: 0x" + Integer.toHexString(selectedColor));
//                                        }
//                                    })
//                                    .setPositiveButton("Done", new ColorPickerClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
//                                            mDrawingView.setPenColor(selectedColor);
//                                        }
//                                    })
//                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                        }
//                                    })
//                                    .build()
//
//                                    .show();
//                        }else {
//                            mDrawingView.setPenColor(color);
//                        }
//                    }
//                });
//                dialog.show();
                break;
            case R.id.undo_btn:
                //check_activity(DrawingView.savePath.size()-1);
//                mDrawingView.setVisibility(View.VISIBLE);
//                cropImageView.setVisibility(View.GONE);
//                blue_color_btn.setImageResource(R.drawable.blue_color_without_white_border);
//                red_color_btn.setImageResource(R.drawable.red_color_without_white_border);


                mDrawingView.undo();


                //initPaintMode();
                //if(mDrawingView.)
                //initPaintMode();
                //mDrawingView.setPenColor(selectedColor);

                if (mDrawingView.getPenColor() == getResources().getColor(R.color.screeshot_red)) {
                    //mDrawingView.setPenColor(getResources().getColor(R.color.screeshot_red));
                    blue_color_btn.setImageResource(R.drawable.blue_color_without_white_border);
                    red_color_btn.setImageResource(R.drawable.red_color_with_white_border);
                } else {
                    //mDrawingView.setPenColor(getResources().getColor(R.color.screeshot_blue));
                    blue_color_btn.setImageResource(R.drawable.blue_color_with_white_border);
                    red_color_btn.setImageResource(R.drawable.red_color_without_white_border);
                }

                break;
            case R.id.save_btn:

//                Log.e("----","---"+save_btn.getText().toString());
                if (save_btn.getText().toString().equalsIgnoreCase(getResources().getString(R.string.ic_tick))) {

                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            this.startForegroundService(new Intent(this, Screenshots_service.class));
                        } else {
                            Intent i = new Intent(MainActivity.this, Screenshots_service.class);
                            startService(i);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        this.startForegroundService(new Intent(this, Screenshots_service.class));
//                    } else {
//                    }

                    Normal_toast.show(MainActivity.this, "Screenshot saved", false);
                    finish();
//                    new final_save_image().execute("0");
                } else {
                    finish();
//                    finalBitmap=cropImageView.getCroppedImage();
//                    new final_save_image().execute("1");
                }

                break;
            case R.id.back:

                finish();
                break;
            case R.id.red_color_btn:

//                red_color_btn.setImageResource(COLOR_PANEL == 0 ? R.drawable.blue_color_with_white_border : R.drawable.red_color_with_white_border);
//                mDrawingView.setPenColor(COLOR_PANEL == 0 ? getColor(R.color.screeshot_blue) : getColor(R.color.screeshot_red));
//                COLOR_PANEL = 1 - COLOR_PANEL;
//                break;

                selectedColor = getColor( R.color.screeshot_red);
                mDrawingView.setPenColor(selectedColor);
                blue_color_btn.setImageResource(R.drawable.blue_color_without_white_border);
                red_color_btn.setImageResource(R.drawable.red_color_with_white_border);
//                if (!cropImageView.isShown()) {
//                    new crop_image().execute();
//                }

                break;
            default:
                break;
        }
    }

    public void loadImage() {
        if (i.getStringExtra("request").equals("normal")) {

            if (Condition_player.paint != null) {
                mDrawingView.loadImage(Condition_player.paint);
            } else {
                finish();
            }

        } else {
            if (BasicCustomVideoRenderer.paint != null) {
                mDrawingView.loadImage(BasicCustomVideoRenderer.paint);
            } else {
                finish();
            }
        }
    }


    //    class crop_image extends AsyncTask<String, Void, Boolean> {
//
//
//        protected void onPreExecute() {
//
//            objUsefullData.showProgress();
//        }
//
//        @Override
//        protected Boolean doInBackground(String... urls) {
//            try {
//
//                mDrawingView.saveImage(sdcardPath, "Drawing_temp", Bitmap.CompressFormat.PNG, 70);
//                cropImageView.setImageUriAsync(Uri.parse("file://"+sdcardPath+"/" + "Drawing_temp" + ".png"));
//                return true;
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return false;
//        }
//
//        protected void onPostExecute(Boolean result) {
//
//            mDrawingView.setVisibility(View.GONE);
//            cropImageView.setVisibility(View.VISIBLE);
//            cropImageView.setAutoZoomEnabled(false);
//            objUsefullData.dismissProgress();
//
//        }
//    }
    class final_save_image extends AsyncTask<String, Void, Boolean> {


        protected void onPreExecute() {

            objUsefullData.showProgress();


        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {

                if (urls[0].equals("0")) {
                    mDrawingView.saveImage(sdcardPath, "Drawing_temp", Bitmap.CompressFormat.PNG, 70);
                    finalBitmap = BitmapFactory.decodeFile(sdcardPath + "/" + "Drawing_temp" + ".png");
                }

                Date now = new Date();
                android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/Jukepad");
                myDir.mkdirs();
                File file = new File(myDir, "/" + now + ".png");
                if (file.exists())
                    file.delete();
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    finalBitmap.compress(Bitmap.CompressFormat.PNG, 40, out);
                    out.flush();
                    out.close();

                    File[] fileList = myDir.listFiles();

                    JobScheduler jobScheduler = JobScheduler.getInstance(MainActivity.this);
                    PersistableBundle extras = new PersistableBundle();
                    extras.putString("file_path", file.getAbsolutePath());
                    JobInfo job = new JobInfo.Builder(fileList.length + 1, new ComponentName(MainActivity.this, Screenshots_deletion_service.class))
                            .setMinimumLatency(86400000)
                            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                            .setRequiresCharging(false)
                            .setExtras(extras)
                            .build();
                    jobScheduler.schedule(job);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

            objUsefullData.dismissProgress();
            Normal_toast.show(MainActivity.this, "Screenshot saved", false);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        objUsefullData.Start_timer();
    }

//    @Override
//    public void onBackPressed() {
//    }

    @Override
    protected void onPause() {
        super.onPause();

        objUsefullData.Pause_timer();
//        ActivityManager activityManager = (ActivityManager) getApplicationContext()
//                .getSystemService(Context.ACTIVITY_SERVICE);
//        activityManager.moveTaskToFront(getTaskId(), 0);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        objUsefullData.Start_timer();
        objUsefullData.schedule_time_events();

        return super.dispatchTouchEvent(ev);
    }


    public void check_activity(int size) {

        if (size > 0) {

//            save_btn.setText(getResources().getString(R.string.ic_tick));
//            save_btn.setBackground(getResources().getDrawable(R.drawable.grident_circle_bg));
            undo_btn.setBackground(getResources().getDrawable(R.drawable.black_circle_layout));

        } else {


//            save_btn.setText(getResources().getString(R.string.ic_cross));
//            save_btn.setBackground(getResources().getDrawable(R.drawable.black_circle_layout));
            undo_btn.setBackground(getResources().getDrawable(R.drawable.undo_start_back));

        }
    }

    private static int dirSize(File dir) {

        if (dir.exists()) {
            int result = 0;
            File[] fileList = dir.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // Recursive call if it's a directory
                if (fileList[i].isDirectory()) {
                    result += dirSize(fileList[i]);
                } else {
                    // Sum the file size in bytes
                    result += fileList[i].length();
                }
            }
            return result; // return the file size
        }
        return 0;
    }


}
