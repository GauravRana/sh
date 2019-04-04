package com.sydehealth.sydehealth.main;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Bundle;

import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.sydehealth.sydehealth.adapters.ConditionPopupAdapter;
import com.sydehealth.sydehealth.adapters.ExpandableListClickListener;
import com.sydehealth.sydehealth.adapters.ListItemClickListener;
import com.sydehealth.sydehealth.adapters.TreatmentImageSelectListener;
import com.sydehealth.sydehealth.adapters.TreatmentPopupAdapter;
import com.sydehealth.sydehealth.adapters.treatment.EmailTreatmentAdapter;
import com.sydehealth.sydehealth.adapters.treatment.TreatmentPopupAdapterNew;
import com.sydehealth.sydehealth.adapters.TreatmentScreenAdapter;
import com.sydehealth.sydehealth.model.EmailTreatmentResponse;
import com.sydehealth.sydehealth.model.TreatmentDataset;
import com.sydehealth.sydehealth.model.SubHeadings;
import com.sydehealth.sydehealth.model.TreatmentImages;
import com.sydehealth.sydehealth.model.Treatments;
import com.sydehealth.sydehealth.utility.UsefullData;
import com.sydehealth.sydehealth.R;
import com.sydehealth.sydehealth.mail_upload.Mail_service;
import com.sydehealth.sydehealth.utility.Definitions;
import com.sydehealth.sydehealth.utility.SaveData;
import com.sydehealth.sydehealth.volley.UserAPI;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EmailActivity extends Fragment implements ExpandableListClickListener, ListItemClickListener, TreatmentImageSelectListener, View.OnClickListener {


    private Context mContext;


    //forConditions
    ArrayList<String> listDataHeader;
    HashMap<String, ArrayList<SubHeadings>> listDataSubHeadings;

    //for Treatments
    ArrayList<TreatmentDataset> treatmentDatasets;
    private JSONArray treatmentJsonArrResult = null;
    private JSONArray treatmentJsonArrResultName = null;

//    ArrayList<TreatmentDataset> selectedTreatmentDatasets;

//    ArrayList<Treatments> alTreatments;
//    ArrayList<String> alSelectedTreatmentId = new ArrayList<>();
//    ArrayList<String> alTempSelectedTreatmentId = new ArrayList<>();

    ConditionPopupAdapter conditionPopupAdapter;
    TreatmentPopupAdapter rvAdapter;

    TreatmentPopupAdapterNew treatmentPopupAdapter;

    ScrollView center_container;
    LinearLayout lay_add_condition, lay_add_photos;
    LinearLayout lay_treatment_opt, ll_startEmail;
    LinearLayout lay_condition_added, lay_treatment_added, lay_treatment, ll_images_added;
    TextView tv_treatment, tv_treatmentDesc, tv_prevention, tv_preventionTips;
    private RecyclerView rv_treatment_result;
    Button iv_back;
    TextView iv_addmore, iv_addmorePhotos, arrow, tv_max;
    TextView iv_editcondition, iv_editTreatment, iv_editPhotos;
    TextView tv_username, tv_emailheader, tv_summary, tv_relatedImages, tv_createemail, tv_startEmail;
    TextView tv_addCondition, tv_addTreatment, tv_addPhotos, tv_treatmentOption, tv_name;
    RelativeLayout rl_add_condition, rl_add_treatment, rl_add_photos;
    Dialog dialog;

    String selectedCondition = "", tempSelectedCondition = "";
    String selectedSubCondition = "", tempSelectedSubCondition = "";
    String tempSelectedConditionName = "" ,tempselectedHeadingName="";
    String selectedConditionName = "", selectedHeadingName = "";
    int selectedGroupPos = -1;

    static UsefullData objUsefullData;

    ArrayList<TreatmentImages> alTreatmentImages = new ArrayList<>();
    public static ArrayList<TreatmentImages> alSelectedImages = new ArrayList<>();
    public static ArrayList<TreatmentImages> alTempSelectedImages = new ArrayList<>();
    ImageLoader imageLoader;
    RecyclerView rv_images;

    TreatmentScreenAdapter treatmentScreenAdapter;

    static ProgressDialog loader, loader_draft, loader_repeat;
    public static Activity activity = null;
    static int w, h;
    static SaveData save_data;
    Button btn_add, btn_add_photos;

    boolean isEdited = false;
    RelativeLayout root;
    LinearLayout keyboard_layout;
    TextInputEditText userInputDialogEditText;
    Button yahoo_txt, outlook_txt, gmail_txt;
    private static final String TAG = "EmailActivity";
    View view;

    public String emailMsg = "Email will send shortly.";


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Tab_activity.ll_bottombar.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //((Tab_activity)getActivity()).ll_email.setBackground(null);
        if(view==null){
            view=inflater.inflate(R.layout.activity_test,container,false);
            mContext = getActivity();
            Tab_activity.ll_bottombar.setVisibility(View.GONE);
            objUsefullData = new UsefullData(mContext);
            objUsefullData.Start_timer();
            activity =getActivity();
            save_data = new SaveData(getActivity());
            alSelectedImages.clear();
            //EmailToast.show(getApplicationContext(),"Email Sent",true);
            initImageLoader();
            init(view);
            listeners();
        }
        return view;
    }


    /**
     * Initialize views
     */
    private void init(View view) {
        center_container = (ScrollView) view.findViewById(R.id.center_container);

        tv_max = (TextView) view.findViewById(R.id.tv_max);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        arrow = (TextView) view.findViewById(R.id.arrow);
        tv_username = (TextView) view.findViewById(R.id.tv_username);
        tv_emailheader = (TextView) view.findViewById(R.id.tv_emailheader);
        tv_summary = (TextView) view.findViewById(R.id.tv_summary);
        tv_createemail = (TextView) view.findViewById(R.id.tv_createemail);
        tv_startEmail = (TextView) view.findViewById(R.id.tv_startEmail);

        tv_addCondition = (TextView) view.findViewById(R.id.tv_addCondition);
        tv_addTreatment = (TextView) view.findViewById(R.id.tv_addTreatment);
        tv_addPhotos = (TextView) view.findViewById(R.id.tv_addPhotos);
        tv_treatmentOption = (TextView) view.findViewById(R.id.tv_treatmentOption);

        rl_add_condition = (RelativeLayout) view.findViewById(R.id.rl_add_condition);
        rl_add_treatment = (RelativeLayout) view.findViewById(R.id.rl_add_treatment);
        rl_add_photos = (RelativeLayout) view.findViewById(R.id.rl_add_photos);

        lay_add_condition = (LinearLayout) view.findViewById(R.id.lay_add_condition);
        lay_treatment_opt = (LinearLayout) view.findViewById(R.id.lay_treatment_opt);
        lay_condition_added = (LinearLayout) view.findViewById(R.id.lay_condition_added);
        lay_treatment_added = (LinearLayout) view.findViewById(R.id.lay_treatment_added);
        lay_add_photos = (LinearLayout) view.findViewById(R.id.lay_add_photos);
        lay_treatment = (LinearLayout) view.findViewById(R.id.lay_treatment);
        ll_images_added = (LinearLayout) view.findViewById(R.id.ll_images_added);
        ll_startEmail = (LinearLayout) view.findViewById(R.id.ll_startEmail);

        ll_startEmail.setAlpha(0.3f);
        ll_startEmail.setEnabled(false);

        iv_back = (Button) view.findViewById(R.id.iv_back);
        iv_editcondition = (TextView) view.findViewById(R.id.iv_editcondition);
        iv_editTreatment = (TextView) view.findViewById(R.id.iv_editTreatment);
        iv_editPhotos = (TextView) view.findViewById(R.id.iv_editPhotos);
        iv_addmore = (TextView) view.findViewById(R.id.iv_addmore);
        iv_addmorePhotos = (TextView) view.findViewById(R.id.iv_addmorePhotos);
        tv_treatment = (TextView) view.findViewById(R.id.tv_treatment);
        tv_treatmentDesc = (TextView) view.findViewById(R.id.tv_treatmentDesc);
        rv_treatment_result = (RecyclerView) view.findViewById(R.id.rv_treatment_result);
        tv_prevention = (TextView) view.findViewById(R.id.tv_prevention);
        tv_relatedImages = (TextView) view.findViewById(R.id.tv_relatedImages);
        tv_preventionTips = (TextView) view.findViewById(R.id.tv_preventionTips);
        rv_images = (RecyclerView) view.findViewById(R.id.rv_images);
        rv_images.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        keyboard_layout = (LinearLayout) view.findViewById(R.id.keyboard_extra_key);

        iv_back.setTypeface(objUsefullData.get_awosome_font());
        iv_editcondition.setTypeface(objUsefullData.get_awosome_font());
        iv_editTreatment.setTypeface(objUsefullData.get_awosome_font());
        iv_editPhotos.setTypeface(objUsefullData.get_awosome_font());
        iv_addmore.setTypeface(objUsefullData.get_awosome_font());
        iv_addmorePhotos.setTypeface(objUsefullData.get_awosome_font());
        arrow.setTypeface(objUsefullData.get_awosome_font());

        tv_max.setTypeface(objUsefullData.get_montserrat_regular());
        tv_name.setTypeface(objUsefullData.get_montserrat_regular());
        tv_emailheader.setTypeface(objUsefullData.get_montserrat_regular());
        tv_summary.setTypeface(objUsefullData.get_montserrat_bold());
        tv_username.setTypeface(objUsefullData.get_montserrat_regular());
        tv_prevention.setTypeface(objUsefullData.get_montserrat_bold());
        tv_preventionTips.setTypeface(objUsefullData.get_montserrat_regular());
        tv_treatment.setTypeface(objUsefullData.get_montserrat_bold());
        tv_treatmentDesc.setTypeface(objUsefullData.get_montserrat_regular());
        tv_relatedImages.setTypeface(objUsefullData.get_montserrat_bold());
        tv_createemail.setTypeface(objUsefullData.get_montserrat_regular());
        tv_startEmail.setTypeface(objUsefullData.get_montserrat_regular());
        tv_addCondition.setTypeface(objUsefullData.get_montserrat_regular());
        tv_addTreatment.setTypeface(objUsefullData.get_montserrat_regular());
        tv_addPhotos.setTypeface(objUsefullData.get_montserrat_regular());
        tv_treatmentOption.setTypeface(objUsefullData.get_montserrat_bold());
        tv_name.setText(objUsefullData.capitalize(save_data.getString(Definitions.SURGERY_NAME).trim()));


    }


    /**
     * Click Listeners
     */
    private void listeners() {
        lay_add_condition.setOnClickListener(this);
        lay_treatment_opt.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        iv_editcondition.setOnClickListener(this);
        iv_editTreatment.setOnClickListener(this);
        iv_addmore.setOnClickListener(this);
        lay_add_photos.setOnClickListener(this);
        iv_editPhotos.setOnClickListener(this);
        iv_addmorePhotos.setOnClickListener(this);
        ll_startEmail.setOnClickListener(this);
    }


    /**
     * Method to set font style on textviews
     */
    private void fn_setFontType() {

    }


    /**
     * Method to initialize image loader
     */
    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                getActivity()).defaultDisplayImageOptions(defaultOptions).memoryCache(
                new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }


    /**
     * Method to show alert messages
     */
    private void showAlert(String message) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_progress_alert);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.9f; // Dim level. 0.0 - no dim, 1.0 - completely opaque
        dialog.getWindow().setAttributes(lp);


        TextView pop_msg_alert = (TextView) dialog.findViewById(R.id.pop_msg_alert);
        Button back_alert = (Button) dialog.findViewById(R.id.back_alert);

        pop_msg_alert.setTypeface(objUsefullData.get_semibold());
        back_alert.setTypeface(objUsefullData.get_google_bold());
        pop_msg_alert.setText(message);

        back_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();


    }


    /**
     * Method to show alert for save email in draft
     */
    private void saveDraft() {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_progress_alert);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.9f; // Dim level. 0.0 - no dim, 1.0 - completely opaque
        dialog.getWindow().setAttributes(lp);


        TextView pop_msg_alert = (TextView) dialog.findViewById(R.id.pop_msg_alert);
        Button save = (Button) dialog.findViewById(R.id.back_alert);
        Button cancel = (Button) dialog.findViewById(R.id.delete_alert);
        cancel.setVisibility(View.VISIBLE);
        pop_msg_alert.setTypeface(objUsefullData.get_semibold());
        save.setTypeface(objUsefullData.get_google_bold());
        cancel.setTypeface(objUsefullData.get_google_bold());
        pop_msg_alert.setText("Do you want to save the email as a draft?");
        save.setText("Save");
        cancel.setText("Cancel");

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent edit = new Intent(getActivity(), Tab_activity.class);
                edit.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(edit);

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent edit = new Intent(getActivity(), Tab_activity.class);
                edit.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(edit);
                //finish();
            }
        });

        dialog.show();


    }


    /**
     * Method to show condition/treatment dialog
     *
     * @param dialogType
     */
    private void openConditonNTreatmentPopup(String dialogType) {
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.add_condition_popup);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.8f; // Dim level. 0.0 - no dim, 1.0 - completely opaque
        dialog.getWindow().setAttributes(lp);

        ExpandableListView expandableListView = (ExpandableListView) dialog.findViewById(R.id.expandable_listview);
        RecyclerView recyclerview = (RecyclerView) dialog.findViewById(R.id.recylerview);
        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        btn_add = (Button) dialog.findViewById(R.id.btn_add);
        TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
        tv_title.setTypeface(objUsefullData.get_montserrat_regular());

        btn_add.setTypeface(objUsefullData.get_montserrat_regular());
        btn_add.setAlpha(0.5f);
        btn_add.setEnabled(false);
        btn_cancel.setTypeface(objUsefullData.get_montserrat_regular());


        if (dialogType.equalsIgnoreCase("conditions")) {

            if (selectedCondition.equals("")) {
                btn_add.setAlpha(0.5f);
                btn_add.setEnabled(false);
            } else {
                btn_add.setAlpha(1f);
                btn_add.setEnabled(true);
            }
            tv_title.setText("Select Condition");
            expandableListView.setVisibility(View.VISIBLE);
            recyclerview.setVisibility(View.GONE);
            if (selectedCondition.equals("") && selectedSubCondition.equals("")) {
                prepareConditionList(false);
            } else {
                prepareConditionList(true);
            }

            if (listDataHeader.size() == 0) {
                showAlert(getResources().getString(R.string.no_condition_found));
            } else {
                conditionPopupAdapter = new ConditionPopupAdapter(getActivity(), listDataHeader, listDataSubHeadings, this);
                expandableListView.setAdapter(conditionPopupAdapter);

                expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

                    int previousItem = -1;

                    @Override
                    public void onGroupExpand(int groupPosition) {

                        expandableListView.smoothScrollToPosition(groupPosition);
                        if (groupPosition != previousItem)
                            expandableListView.collapseGroup(previousItem);
                        previousItem = groupPosition;

                    }
                });


                if (selectedCondition.equals("")) {
                } else {
                    expandableListView.expandGroup(selectedGroupPos);
                }

                expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                        listDataSubHeadings.get(listDataHeader.get(groupPosition)).get(childPosition).setSelected(true);
                        conditionPopupAdapter.notifyDataSetChanged();
                        return true;
                    }
                });

                dialog.show();
            }
        } else if (dialogType.equalsIgnoreCase("treatment")) {
            tv_title.setText("Select Treatment Options");

            expandableListView.setVisibility(View.VISIBLE);
            recyclerview.setVisibility(View.GONE);

//            recyclerview.setHasFixedSize(true);
//            recyclerview.setLayoutManager(new LinearLayoutManager(this));
//
//            if (selectedCondition.equals("")) {
//                showAlert(getResources().getString(R.string.email_condition_error));
//            } else {
//                prepareTreatmentData(selectedCondition);
//                rvAdapter = new TreatmentPopupAdapter(this, alTreatments, this);
//                recyclerview.setAdapter(rvAdapter);
//
//                if(alTreatments.isEmpty()){
//                    showAlert(getResources().getString(R.string.no_treatment));
//                }else {
//                    dialog.show();
//                }
//
//            }

            ///////11 jan 2019 jatin


//            if (selectedCondition.equals("") && selectedSubCondition.equals("")) {
//                prepareConditionList(false);
//            } else {
//                prepareConditionList(true);
//            }

            if (treatmentDatasets == null) {
                fetchTreatmentData(selectedSubCondition, expandableListView);
            } else {
                setTreatmentData(expandableListView);
            }

        }

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();

            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogType.equalsIgnoreCase("conditions")) {
                    selectedCondition = tempSelectedCondition;
                    selectedSubCondition = tempSelectedSubCondition;

                    selectedConditionName = tempSelectedConditionName;
                    selectedHeadingName = tempselectedHeadingName;

                    for (Map.Entry<String, ArrayList<SubHeadings>> ee : listDataSubHeadings.entrySet()) {
                        ArrayList<SubHeadings> alSubHeadings = ee.getValue();
                        for (int j = 0; j < alSubHeadings.size(); j++) {
                            if (alSubHeadings.get(j).getChildId().equals(selectedSubCondition)) {
                                lay_add_condition.setVisibility(View.GONE);
                                lay_condition_added.setVisibility(View.VISIBLE);
//                                tv_treatment.setText(objUsefullData.capitalize(alSubHeadings.get(j).getGroupName().trim()) + " (" + objUsefullData.capitalize(alSubHeadings.get(j).getName().trim() + ")"));
                                tv_treatment.setText(objUsefullData.capitalize(alSubHeadings.get(j).getName().trim()));
                                tv_treatmentDesc.setText(alSubHeadings.get(j).getDesc());
                                tv_preventionTips.setText(alSubHeadings.get(j).getPreventionTips());
                                if (tv_treatmentDesc.getText().toString().equalsIgnoreCase("")) {
                                    tv_treatmentDesc.setVisibility(View.GONE);
                                } else {
                                    tv_treatmentDesc.setVisibility(View.VISIBLE);
                                }
                                if (tv_preventionTips.getText().toString().equalsIgnoreCase("")) {
                                    tv_preventionTips.setVisibility(View.GONE);
//                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                                    params.setMargins(0,0,0,0);
//                                    tv_prevention.setLayoutParams(params);
                                    tv_prevention.setVisibility(View.INVISIBLE);

                                } else {
                                    tv_preventionTips.setVisibility(View.VISIBLE);
                                    tv_prevention.setVisibility(View.VISIBLE);
                                }
                                iv_editcondition.setVisibility(View.VISIBLE);
                                lay_treatment_added.setVisibility(View.GONE);
                                lay_treatment_opt.setVisibility(View.VISIBLE);
                                iv_editTreatment.setVisibility(View.GONE);
                                iv_addmore.setVisibility(View.GONE);
                                //alSelectedTreatmentId.clear();
                                //lay_treatment.removeAllViews();

                                lay_add_photos.setVisibility(View.VISIBLE);
                                ll_images_added.setVisibility(View.GONE);

                                iv_editPhotos.setVisibility(View.GONE);
                                iv_addmorePhotos.setVisibility(View.GONE);

                                rl_add_condition.setBackgroundResource(R.drawable.color_gradient);
                                rl_add_treatment.setBackgroundColor(getResources().getColor(R.color.white));
                                rl_add_photos.setBackgroundColor(getResources().getColor(R.color.white));

                                new Handler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        //center_container.scrollTo(0,lay_add_condition.getBottom());
                                        objUsefullData.make_visible(lay_add_photos);
                                    }
                                });

                                ll_startEmail.setAlpha(1f);
                                ll_startEmail.setEnabled(true);
                                lay_treatment_opt.setAlpha(1f);
                                lay_add_photos.setAlpha(1f);
                                isEdited = true;
                                alTempSelectedImages.clear();
                                alSelectedImages.clear();

                            }
                        }
                    }

                    treatmentDatasets = null;

                } else if (dialogType.equalsIgnoreCase("treatment")) {

//                    alSelectedTreatmentId.clear();
//                    alSelectedTreatmentId.addAll(alTempSelectedTreatmentId);
//
//                    if (alSelectedTreatmentId.size() > 0) {
//
//                        if (alSelectedTreatmentId.size() == 1) {
//                            tv_treatmentOption.setText("Treatment Option");
//                        } else {
//                            tv_treatmentOption.setText("Treatment Options");
//                        }
//                        lay_treatment_opt.setVisibility(View.GONE);
//                        lay_treatment_added.setVisibility(View.VISIBLE);
//                        lay_treatment.removeAllViews();
//                        for (int i = 0; i < alSelectedTreatmentId.size(); i++) {
//                            for (int j = 0; j < alTreatments.size(); j++) {
//                                if (alSelectedTreatmentId.get(i).equalsIgnoreCase(alTreatments.get(j).getId())) {
//                                    View childView = getLayoutInflater().inflate(R.layout.layout_treatment, null);
//                                    TextView tv_treatmentName = (TextView) childView.findViewById(R.id.tv_treatmentName);
//                                    TextView tv_treatmentDesc = (TextView) childView.findViewById(R.id.tv_treatmentDesc);
//
//
//                                    tv_treatmentName.setTypeface(objUsefullData.get_montserrat_bold());
//                                    tv_treatmentDesc.setTypeface(objUsefullData.get_montserrat_regular());
//
//                                    tv_treatmentName.setText(objUsefullData.capitalize(alTreatments.get(j).getName().trim()));
//                                    tv_treatmentDesc.setText(alTreatments.get(j).getDesc());
//
//                                    lay_treatment.addView(childView);
//                                }
//                            }
//                        }
//
//                        iv_editTreatment.setVisibility(View.VISIBLE);
//                        if (alSelectedTreatmentId.size() < 4) {
//                            iv_addmore.setVisibility(View.VISIBLE);
//                        } else {
//                            iv_addmore.setVisibility(View.GONE);
//                        }
//
//                        rl_add_condition.setBackgroundResource(R.drawable.color_gradient);
//                        rl_add_treatment.setBackgroundResource(R.drawable.color_gradient);
//                        rl_add_photos.setBackgroundColor(getResources().getColor(R.color.white));
//
//                        isEdited = true;
//                        new Handler().post(new Runnable() {
//                            @Override
//                            public void run() {
//                                objUsefullData.make_visible(lay_add_photos);
//                            }
//                        });
//
//                    } else {
//                        lay_treatment.removeAllViews();
//                        lay_treatment_added.setVisibility(View.GONE);
//                        lay_treatment_opt.setVisibility(View.VISIBLE);
//
//                        rl_add_condition.setBackgroundResource(R.drawable.color_gradient);
//                        rl_add_treatment.setBackgroundColor(getResources().getColor(R.color.white));
//                        rl_add_photos.setBackgroundColor(getResources().getColor(R.color.white));
//
//                        iv_addmore.setVisibility(View.GONE);
//                        iv_editTreatment.setVisibility(View.GONE);
//
//                    }


                    rv_treatment_result.setHasFixedSize(true);
                    rv_treatment_result.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));

                    EmailTreatmentAdapter emailTreatmentAdapter = new EmailTreatmentAdapter(mContext, getSelectedTreatmentDataset());
                    rv_treatment_result.setAdapter(emailTreatmentAdapter);


                    if (emailTreatmentAdapter.getItemCount() < 4) {
                        iv_addmore.setVisibility(View.VISIBLE);
                    } else {
                        iv_addmore.setVisibility(View.INVISIBLE);
                    }

                    lay_treatment_added.setVisibility(View.VISIBLE);
                    iv_editTreatment.setVisibility(View.VISIBLE);
                    //iv_addmore.setVisibility(View.VISIBLE);
                    lay_treatment_opt.setVisibility(View.GONE);

                    rl_add_treatment.setBackgroundResource(R.drawable.color_gradient);

                    //making jsonObj for server request
                    try {
                        treatmentJsonArrResult = new JSONArray();
                        treatmentJsonArrResultName = new JSONArray();
                        for (int i = 0; i < getSelectedTreatmentDataset().size(); i++) {
                            TreatmentDataset tmp = getSelectedTreatmentDataset().get(i);
                            JSONObject treatment = new JSONObject();
                            JSONObject treatmentName = new JSONObject();

                            JSONArray subTreatmentArr = new JSONArray();
                            JSONArray subTreatmentArrName = new JSONArray();
                            for (int j = 0; j < tmp.getSelectedChildDataSet().size(); j++) {
                                subTreatmentArr.put(tmp.getSelectedChildDataSet().get(j).getId());
                                subTreatmentArrName.put(tmp.getSelectedChildDataSet().get(j).getName());
                            }

                            treatment.put("treatment_id", getSelectedTreatmentDataset().get(i).getId());
                            treatment.put("sub_treatment_id", subTreatmentArr);

                            treatmentName.put("treatment_Name", getSelectedTreatmentDataset().get(i).getHeadingName());
                            treatmentName.put("sub_treatment_id", subTreatmentArrName);



                            treatmentJsonArrResult.put(treatment);
                            treatmentJsonArrResultName.put(treatmentName);
                        }
                        Log.d(TAG, "treatment json:::" + treatmentJsonArrResult.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                dialog.cancel();
            }
        });


    }


    /**
     * Method to show treatment image selection dialog
     *
     * @param key
     */
    private void openAddPhotoDialog(String key) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.add_photos_popup);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.getWindow().setGravity(Gravity.CENTER);

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.8f; // Dim level. 0.0 - no dim, 1.0 - completely opaque
        dialog.getWindow().setAttributes(lp);

        dialog.setCancelable(false);

        RecyclerView recyclerview = (RecyclerView) dialog.findViewById(R.id.recylerview);
        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        btn_add_photos = (Button) dialog.findViewById(R.id.btn_add_photos);
        TextView select_treatment = (TextView) dialog.findViewById(R.id.select_treatment);
        select_treatment.setTypeface(objUsefullData.get_montserrat_regular());
        btn_add_photos.setAlpha(0.5f);
        btn_add_photos.setEnabled(false);

        btn_add_photos.setTypeface(objUsefullData.get_montserrat_regular());
        btn_cancel.setTypeface(objUsefullData.get_montserrat_regular());

        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        alTreatmentImages = objUsefullData.get_screenshots();

        alTempSelectedImages.clear();
        alTempSelectedImages.addAll(alSelectedImages);

        if (key.equalsIgnoreCase("edit")) {
            for (int i = 0; i < alTreatmentImages.size(); i++) {
                for (int j = 0; j < alSelectedImages.size(); j++) {
                    if (alSelectedImages.get(j).getPath().equalsIgnoreCase(alTreatmentImages.get(i).getPath())) {
                        alTreatmentImages.get(i).setSelected(true);
                    }
                }
            }
        }

        treatmentScreenAdapter = new TreatmentScreenAdapter(alTreatmentImages, getActivity(), imageLoader, this, "add");
        recyclerview.setAdapter(treatmentScreenAdapter);
        recyclerview.smoothScrollToPosition(0);


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        btn_add_photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alSelectedImages.clear();
                alSelectedImages.addAll(alTempSelectedImages);
                if (alSelectedImages.size() > 0) {
                    if (alSelectedImages.size() >= 4) {
                        iv_editPhotos.setVisibility(View.VISIBLE);
                        iv_addmorePhotos.setVisibility(View.GONE);
                    } else {
                        iv_editPhotos.setVisibility(View.VISIBLE);
                        iv_addmorePhotos.setVisibility(View.VISIBLE);
                    }
                    lay_add_photos.setVisibility(View.GONE);
                    ll_images_added.setVisibility(View.VISIBLE);
                    treatmentScreenAdapter = new TreatmentScreenAdapter(alSelectedImages, getActivity(), imageLoader, null, "show");
                    rv_images.setAdapter(treatmentScreenAdapter);

                    rl_add_condition.setBackgroundResource(R.drawable.color_gradient);
                    rl_add_treatment.setBackgroundResource(R.drawable.color_gradient);
                    rl_add_photos.setBackgroundResource(R.drawable.color_gradient);

                    objUsefullData.make_visible(ll_images_added);
                    isEdited = true;
                } else {
                    lay_add_photos.setVisibility(View.VISIBLE);
                    ll_images_added.setVisibility(View.GONE);

                    iv_editPhotos.setVisibility(View.GONE);
                    iv_addmorePhotos.setVisibility(View.GONE);
                }
                dialog.cancel();
            }
        });

        dialog.show();
    }


    /**
     * Method to prepare list for conditions data
     */
    private void prepareConditionList(boolean isSelected) {
        listDataHeader = new ArrayList<>();
        listDataSubHeadings = new HashMap<>();

        SaveData saveData = new SaveData(getActivity());
        if (saveData.isExist(Definitions.CONDITIONS_JSON_DATA)) {
            try {
                JSONObject jsonObj = new JSONObject(saveData.getString(Definitions.CONDITIONS_JSON_DATA));
                JSONArray conditions = jsonObj.getJSONArray("conditions_android");

                Log.e("conditions--", conditions + "");


                /*if(conditions.length()==0){
                    objUsefullData.make_alert(getResources().getString(R.string.no_condition_found), true, EmailActivity.this);
                }*/

                for (int i = 0; i < conditions.length(); i++) {
                    JSONObject condObj = conditions.getJSONObject(i);
                    listDataHeader.add(condObj.optString("name"));

                    JSONArray subheadingsArray = condObj.getJSONArray("subheadings");
                    ArrayList<SubHeadings> alSubHeading = new ArrayList<>();
                    for (int j = 0; j < subheadingsArray.length(); j++) {
                        JSONObject treatmentObj = subheadingsArray.getJSONObject(j);
                        SubHeadings subHeadings = new SubHeadings();
                        subHeadings.setGroupId(condObj.optString("id"));
                        subHeadings.setGroupName(condObj.optString("name"));
                        if (!treatmentObj.isNull("description")) {
                            subHeadings.setDesc(treatmentObj.optString("description"));
                        } else {
                            subHeadings.setDesc("");
                        }
                        subHeadings.setChildId(treatmentObj.optString("id"));
                        subHeadings.setName(treatmentObj.optString("name"));
                        subHeadings.setPreventionTips(treatmentObj.optString("prevention_tips"));

                        if (isSelected) {
                            if (selectedSubCondition.equals(subHeadings.getChildId())) {
                                subHeadings.setSelected(true);
                            } else {
                                subHeadings.setSelected(false);
                            }
                        } else {
                            subHeadings.setSelected(false);
                        }
                        alSubHeading.add(subHeadings);
                    }

                    listDataSubHeadings.put(listDataHeader.get(i), alSubHeading);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {

        }
    }


    /**
     * Method to get treatment list as per selected conditions
     *
     * @param groupId
     */
//    private void prepareTreatmentData(String groupId) {
//        alTempSelectedTreatmentId.clear();
//        alTempSelectedTreatmentId.addAll(alSelectedTreatmentId);
//        alTreatments = new ArrayList<>();
//        SaveData saveData = new SaveData(EmailActivity.this);
//
//        if (saveData.isExist(Definitions.CONDITIONS_JSON_DATA)) {
//            try {
//                JSONObject jsonObj = new JSONObject(saveData.getString(Definitions.CONDITIONS_JSON_DATA));
//                JSONArray conditions = jsonObj.getJSONArray("conditions_android");
//
//                for (int i = 0; i < conditions.length(); i++) {
//                    JSONObject condObj = conditions.getJSONObject(i);
//                    if (condObj.optString("id").equals(groupId)) {
//                        JSONArray treatmentArray = condObj.getJSONArray("treatment_options");
//                        for (int j = 0; j < treatmentArray.length(); j++) {
//                            JSONObject treatmentObj = treatmentArray.getJSONObject(j);
//                            Treatments treatments = new Treatments();
//                            treatments.setId(treatmentObj.optString("id"));
//                            treatments.setName(treatmentObj.optString("name"));
//                            treatments.setDesc(treatmentObj.optString("prevention_advice"));
//                            alTreatments.add(treatments);
//                        }
//                    }
//                }
//
//
//                for (int j = 0; j < alTreatments.size(); j++) {
//                    for (int k = 0; k < alSelectedTreatmentId.size(); k++) {
//                        if (alTreatments.get(j).getId().equals(alSelectedTreatmentId.get(k))) {
//                            alTreatments.get(j).setSelected(true);
//                        }
//                    }
//                }
//
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }
    @Override
    public void onListItemClick(int groupPos, String groupId, String groupName, String childId, String childName, boolean isSelected) {
        tempSelectedCondition = groupId;
        tempSelectedSubCondition = childId;

        tempSelectedConditionName = groupName;
        tempselectedHeadingName = childName;

        selectedGroupPos = groupPos;

        if (!isSelected) {
            btn_add.setAlpha(0.5f);
            btn_add.setEnabled(false);
        } else {
            btn_add.setAlpha(1f);
            btn_add.setEnabled(true);
        }
    }

    @Override
    public void onItemClick(String id, boolean isAdded) {

//        if (isAdded && alTempSelectedTreatmentId.size() >= 4) {
//            showAlert(getResources().getString(R.string.maximum_items_error));
//            for (int j = 0; j < alTreatments.size(); j++) {
//                if (alTreatments.get(j).getId().equals(id)) {
//                    alTreatments.get(j).setSelected(false);
//                }
//            }
//            rvAdapter.notifyDataSetChanged();
//        } else {
//            boolean isMatched = false;
//            for (int i = 0; i < alTempSelectedTreatmentId.size(); i++) {
//                if (id.equals(alTempSelectedTreatmentId.get(i))) {
//                    isMatched = true;
//                    if (!isAdded) {
//                        alTempSelectedTreatmentId.remove(id);
//                        //alRemovedTreatmentId.add(id);
//                    }
//                }
//            }
//            if (!isMatched) {
//                if (isAdded) {
//                    alTempSelectedTreatmentId.add(id);
//                }
//            }
//
//            btn_add.setAlpha(1f);
//            btn_add.setEnabled(true);
//
//            for (int j = 0; j < alTreatments.size(); j++) {
//                if (alTreatments.get(j).getId().equals(id)) {
//                    alTreatments.get(j).setSelected(isAdded);
//                }
//            }
//            Log.e("treatmentid", alSelectedTreatmentId + "");
//        }
//
//        rvAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTreatmentSelect(String path, boolean isSelected) {

        if (isSelected && alTempSelectedImages.size() == 4) {
            showAlert(getResources().getString(R.string.maximum_items_error_screenshot));
            for (int j = 0; j < alTreatmentImages.size(); j++) {
                if (alTreatmentImages.get(j).getPath().equals(path)) {
                    alTreatmentImages.get(j).setSelected(false);
                }
            }
            treatmentScreenAdapter.notifyDataSetChanged();
        } else {
            for (int i = 0; i < alTempSelectedImages.size(); i++) {
                if (alTempSelectedImages.get(i).getPath().equalsIgnoreCase(path)) {
                    if (!isSelected) {
                        alTempSelectedImages.remove(i);
                        if (alTempSelectedImages.size() == 0) {
                            btn_add_photos.setAlpha(0.4f);
                            btn_add_photos.setEnabled(false);
                        }

                    }
                }
            }

            if (isSelected) {
                TreatmentImages treatmentImages = new TreatmentImages();
                treatmentImages.setSelected(isSelected);
                treatmentImages.setPath(path);
                alTempSelectedImages.add(treatmentImages);

            }
            btn_add_photos.setAlpha(1f);
            btn_add_photos.setEnabled(true);

            if (alTempSelectedImages.size() == 0) {
                btn_add_photos.setAlpha(0.4f);

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lay_add_condition:
                openConditonNTreatmentPopup("conditions");
                break;
            case R.id.lay_treatment_opt:
                if (selectedCondition.equals("") && selectedSubCondition.equals("")) {
                    showAlert(getResources().getString(R.string.email_condition_error));
                } else {
                    openConditonNTreatmentPopup("treatment");
                }
                break;
            case R.id.iv_back:
                iv_back.setEnabled(false);
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                //finish();
                /*if(isEdited) {
                    saveDraft();
                    isEdited=false;
                }else{
                    Intent edit = new Intent(EmailActivity.this, Tab_activity.class);
                    edit.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(edit);
                    //finish();
                }*/

                break;
            case R.id.iv_editcondition:
                openConditonNTreatmentPopup("conditions");
                break;
            case R.id.iv_editTreatment:
                if (selectedCondition.equals("") && selectedSubCondition.equals("")) {
                    showAlert(getResources().getString(R.string.email_condition_error));
                } else {
                    openConditonNTreatmentPopup("treatment");
                }
                break;
            case R.id.iv_addmore:
                if (selectedCondition.equals("") && selectedSubCondition.equals("")) {
                    showAlert(getResources().getString(R.string.email_condition_error));
                } else {
                    openConditonNTreatmentPopup("treatment");
                }
                break;
            case R.id.lay_add_photos:
                if (selectedCondition.equals("") && selectedSubCondition.equals("")) {
                    showAlert(getResources().getString(R.string.email_condition_error));
                } else if (getSelectedTreatmentDataset().size() == 0) {
                    showAlert(getResources().getString(R.string.email_treatment_error));
                } else if (objUsefullData.get_screenshots().size() == 0 || objUsefullData.get_screenshots().equals(null)) {
                    showAlert(getResources().getString(R.string.screenshot_error));
                } else {
                    openAddPhotoDialog("add");
                }
                break;
            case R.id.iv_editPhotos:
                openAddPhotoDialog("edit");
                break;
            case R.id.iv_addmorePhotos:
                openAddPhotoDialog("edit");
                break;
            case R.id.ll_startEmail:
                send_mail_popup();
                break;
            case R.id.yahoo_txt:
                userInputDialogEditText.setText(userInputDialogEditText.getText().toString() + getResources().getString(R.string.yahoo));
                userInputDialogEditText.setSelection(userInputDialogEditText.getText().length());
                break;
            case R.id.outlook_txt:
                userInputDialogEditText.setText(userInputDialogEditText.getText().toString() + getResources().getString(R.string.outlook));
                userInputDialogEditText.setSelection(userInputDialogEditText.getText().length());
                break;
            case R.id.gmail_txt:
                userInputDialogEditText.setText(userInputDialogEditText.getText().toString() + getResources().getString(R.string.gmail));
                userInputDialogEditText.setSelection(userInputDialogEditText.getText().length());
                break;
        }

    }


    public void send_mail_popup() {
        Dialog maildialog = new Dialog(getActivity(), R.style.alert_dialog);
        maildialog.setContentView(R.layout.dialog_email);

        maildialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        maildialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent_black)));

        maildialog.setCancelable(false);

        yahoo_txt = (Button) maildialog.findViewById(R.id.yahoo_txt);
        outlook_txt = (Button) maildialog.findViewById(R.id.outlook_txt);
        gmail_txt = (Button) maildialog.findViewById(R.id.gmail_txt);
        yahoo_txt.setOnClickListener(this);
        outlook_txt.setOnClickListener(this);
        gmail_txt.setOnClickListener(this);

        /*WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount=1f; // Dim level. 0.0 - no dim, 1.0 - completely opaque
        dialog.getWindow().setAttributes(lp);*/

        objUsefullData.Start_timer();
        /*LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_email, null);
        final AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);
        alertDialogBuilderUserInput.setCancelable(false);
        */
//        final LinearLayout r = (LinearLayout) maildialog.findViewById(R.id.custom_dialog_layout_design_user_input);

        root = (RelativeLayout) maildialog.findViewById(R.id.custom_dialog_layout_design_user_input);
        keyboard_layout = (LinearLayout) maildialog.findViewById(R.id.keyboard_extra_key);
        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                objUsefullData.Start_timer();
                return false;
            }
        });


        TextInputLayout email_layer = (TextInputLayout) maildialog.findViewById(R.id.email_layer);
        final TextView email_error = (TextView) maildialog.findViewById(R.id.email_error);
        final TextView send_email = (TextView) maildialog.findViewById(R.id.send_email);
        userInputDialogEditText = (TextInputEditText) maildialog.findViewById(R.id.userInputDialog);
        final Button send = (Button) maildialog.findViewById(R.id.send);
        final Button cancel = (Button) maildialog.findViewById(R.id.cancel);
        send.setAlpha(0.4f);

        email_error.setTypeface(objUsefullData.get_montserrat_regular());
        email_error.setVisibility(View.GONE);
        userInputDialogEditText.setTypeface(objUsefullData.get_montserrat_regular());
        send.setTypeface(objUsefullData.get_montserrat_regular());
        cancel.setTypeface(objUsefullData.get_montserrat_regular());
        email_layer.setTypeface(objUsefullData.get_montserrat_regular());
        send_email.setTypeface(objUsefullData.get_montserrat_semibold());


        userInputDialogEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);


        //final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();

        userInputDialogEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.userInputDialog || id == EditorInfo.IME_ACTION_DONE) {
                    send.performClick();
                    objUsefullData.Start_timer();
                    return true;
                }
                return false;
            }
        });

        userInputDialogEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(userInputDialogEditText.getText().toString().trim()).matches()) {
//                    Animation shake = AnimationUtils.loadAnimation(Mail_popup.this, R.anim.shake);
//                    userInputDialogEditText.startAnimation(shake);
                    //email_error.setText(EmailActivity.this.getResources().getString(R.string.email_valid));
                    send.setAlpha(0.4f);
                } else {
                    send.setAlpha(1.0f);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                email_error.setText("");
                objUsefullData.Start_timer();
            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                objUsefullData.Start_timer();
                email_error.setText("");
                objUsefullData.hideKeyboardFrom(getActivity(), view);
                if (TextUtils.isEmpty(userInputDialogEditText.getText().toString().trim())) {
//                    Animation shake = AnimationUtils.loadAnimation(Mail_popup.this, R.anim.shake);
//                    userInputDialogEditText.startAnimation(shake);
                    email_error.setText(EmailActivity.this.getResources().getString(R.string.req));
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(userInputDialogEditText.getText().toString().trim()).matches()) {
//                    Animation shake = AnimationUtils.loadAnimation(Mail_popup.this, R.anim.shake);
//                    userInputDialogEditText.startAnimation(shake);
                    email_error.setText(EmailActivity.this.getResources().getString(R.string.email_valid));
                } else {

                    if (!objUsefullData.isNetworkConnected()) {
                        showAlert(getResources().getString(R.string.no_internet));
                    } else {

                        send.setEnabled(false);
                        Intent i = new Intent(getActivity(), Mail_service.class);
                        i.putExtra("condition_id", selectedCondition);
//                        i.putExtra("condition_video_id", mail_data.get("condition_video_id"));
//                        i.putExtra("treatment_option_ids", alSelectedTreatmentId.toString().replace("[", "").replace("]", "").replace(" ", ""));
                        i.putExtra("treatment_option_ids", treatmentJsonArrResult.toString());

                        i.putExtra("treatment_option_name", treatmentJsonArrResultName.toString());

                        i.putExtra("condition_name", selectedConditionName);
                        i.putExtra("sub_heading_name", selectedHeadingName);

//                        i.putExtra("advert_id", mail_data.get("advert_id"));
                        i.putExtra("email", userInputDialogEditText.getText().toString().trim());
                        i.putExtra("sub_heading_id", selectedSubCondition);

                        try {
                            mContext.startService(i);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

//                        try{
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                startForegroundService(new Intent(EmailActivity.this, Mail_service.class));
//                            } else {
//                                startService(new Intent(EmailActivity.this, Mail_service.class));
//                            }
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }


                        maildialog.dismiss();
                        //alert_repeat(userInputDialogEditText.getText().toString());
                        alert_repeat("Email will send shortly.");

                    }
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                objUsefullData.Start_timer();
                objUsefullData.hideKeyboardFrom(getActivity(), view);
                maildialog.dismiss();


            }
        });
        //alertDialogBuilderUserInput.setCancelable(false);
        maildialog.show();

        KeyboardVisibilityEvent.setEventListener(getActivity(),
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if (isOpen) {


//                            if(email_focus) {
//                                Rect r = new Rect();
//
//                                root.getWindowVisibleDisplayFrame(r);
//
//                                int screenHeight = root.getRootView().getHeight();
//                                int keyboardHeight = screenHeight - (r.bottom);
//
//                                int h = getResources().getDimensionPixelSize(R.dimen._20sdp);
//                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, h);
//                                params.setMargins(0, 0, 0, keyboardHeight);
//                                keyboard_layout.setLayoutParams(params);

                            try{
                                show_keyboard_layout();
                            }catch (Exception e){
                                e.printStackTrace();
                            }

//                            }

                        } else {
                            keyboard_layout.setVisibility(View.GONE);
                        }

                    }
                });

    }


    public  void alert_repeat(String email) {
//        objUsefullData.Start_timer();
        MyCustomProgressDialog_repeat obj = new MyCustomProgressDialog_repeat(mContext);
        loader_repeat = obj.ctor(activity, email);

        if (!((Activity) activity).isFinishing()) {
            loader_repeat.show();
        }
    }


    public  class MyCustomProgressDialog_repeat extends ProgressDialog {

        String email_id;

        public  ProgressDialog ctor(Context context, String email) {
            MyCustomProgressDialog_repeat dialog = new MyCustomProgressDialog_repeat(context, R.style.CustomDialog_new);
            dialog.setIndeterminate(true);
            email_id = email;
            dialog.setCancelable(false);


            return dialog;
        }

        public MyCustomProgressDialog_repeat(Context context) {
            super(context);
        }

        public MyCustomProgressDialog_repeat(Context context, int theme) {
            super(context, theme);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.custom_progress_alert);

            Button delete = (Button) findViewById(R.id.delete_alert);
            TextView pop_msg = (TextView) findViewById(R.id.pop_msg_alert);
            Button back = (Button) findViewById(R.id.back_alert);
            LinearLayout extra = (LinearLayout) findViewById(R.id.extra);
            LinearLayout extra_up = (LinearLayout) findViewById(R.id.extra_up);
            pop_msg.setTypeface(objUsefullData.get_semibold());
            back.setTypeface(objUsefullData.get_google_bold());

            // pop_msg.setText("Email to " + email_id + " has been sent.");
            pop_msg.setText(emailMsg);

            delete.setTypeface(objUsefullData.get_google_bold());
            back.setVisibility(View.VISIBLE);
            delete.setVisibility(View.GONE);
            back.setText("OK");

            /*if (!all_file_data.isEmpty()) {
                back.setVisibility(View.VISIBLE);
                delete.setVisibility(View.VISIBLE);
                delete.setText("No");
                back.setText("Yes");
                pop_msg.setText("Email to " + email_id + " has been sent. Do you want to delete all the screenshots for this session?");
            }*/
            w = getContext().getResources().getDimensionPixelSize(R.dimen._60sdp);
            h = getContext().getResources().getDimensionPixelSize(R.dimen._18sdp);
//              delete.setLayoutParams(new LinearLayout.LayoutParams(w, h));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    w,
                    h);
            layoutParams.setMargins(0, 0, h, 0);
            delete.setLayoutParams(layoutParams);
            back.setLayoutParams(new LinearLayout.LayoutParams(w, h));
            extra.setLayoutParams(new LinearLayout.LayoutParams(h, h));
            extra_up.setLayoutParams(new LinearLayout.LayoutParams(h, h));
//            }


            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();

                    activity.finish();
                    save_data.remove(Definitions.MAIL_IN_DRAFT);
                    Intent intent = new Intent(activity, Tab_activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    activity.startActivity(intent);
                }
            });

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    /*if (!all_file_data.isEmpty()) {
                        save_data.remove(Definitions.MAIL_IN_DRAFT);
                        objUsefullData.delete_screenshot_folder();
                        activity.finish();
                        Intent intent = new Intent(activity, Tab_activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        activity.startActivity(intent);
                    } else {
                    */
                    //activity.finish();
                    getActivity().onBackPressed();
                    save_data.remove(Definitions.MAIL_IN_DRAFT);
//                    Intent intent = new Intent(activity, Tab_activity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                    activity.startActivity(intent);
                    //}
//                    return false;
                }
            });

        }

        @Override
        public void show() {
            super.show();

        }

        @Override
        public void dismiss() {
            super.dismiss();

        }
    }


//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        View view = getCurrentFocus();
//        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
//            int scrcoords[] = new int[2];
//            view.getLocationOnScreen(scrcoords);
//            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
//            float y = ev.getRawY() + view.getTop() - scrcoords[1];
//            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
//                ((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
//        }
//        objUsefullData.Start_timer();
//        objUsefullData.schedule_time_events();
//        return super.dispatchTouchEvent(ev);
//    }


    public void show_keyboard_layout() {
        try{
            keyboard_layout.setVisibility(View.VISIBLE);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private void fetchTreatmentData(final String presentationId, ExpandableListView expandableListView) {

        objUsefullData.showProgress();
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();

        headers.put("Accept", Definitions.VERSION);
        headers.put("X-User-Email", save_data.get(Definitions.USER_EMAIL));
        headers.put("X-User-Token", save_data.get(Definitions.USER_TOKEN));


        params.put("presentation_id", presentationId);

        UserAPI.get_JsonObjResp(Definitions.GET_TREATMENT_DATA, headers, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("TAG response", response.toString());
                        objUsefullData.dismissProgress();


                        try {
                            Gson gson = new Gson();
                            EmailTreatmentResponse emailTreatmentResponse = gson.fromJson(response.toString(), EmailTreatmentResponse.class);

                            treatmentDatasets = new ArrayList<>();

                            for (int i = 0; i < emailTreatmentResponse.getPresentation().getTreatmentOptions().size(); i++) {
                                EmailTreatmentResponse.TreatmentOption treatmentOption = emailTreatmentResponse.getPresentation().getTreatmentOptions().get(i);

                                TreatmentDataset heading = new TreatmentDataset();
                                heading.setId(treatmentOption.getTreatmentOption().getId());
                                heading.setHeadingName(treatmentOption.getTreatmentOption().getName());
                                heading.setPreventionAdvice(treatmentOption.getTreatmentOption().getPreventionAdvice());

                                List<EmailTreatmentResponse.SubTreatmentOption> subTreatmentOptions = treatmentOption.getTreatmentOption().getSubTreatmentOptions();

                                ArrayList<EmailTreatmentResponse.SubTreatmentOption_> tmpEmailTreatmentArr
                                        = new ArrayList<>();

                                for (int j = 0; j < subTreatmentOptions.size(); j++) {
                                    tmpEmailTreatmentArr.add(subTreatmentOptions.get(j).getSubTreatmentOption());
                                }

                                heading.setChildDataSet(tmpEmailTreatmentArr);

                                treatmentDatasets.add(heading);
                            }
                            setTreatmentData(expandableListView);
                        } catch (Exception e) {
                            objUsefullData.make_alert(getResources().getString(R.string.no_api_hit), false, mContext);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            objUsefullData.dismissProgress();

                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                objUsefullData.make_alert(getResources().getString(R.string.no_api_hit), false, mContext);
                            } else {
                                objUsefullData.make_alert(getResources().getString(R.string.no_internet_speed), false, mContext);
                            }

                        } catch (Exception e) {

                        }

                    }
                });


    }

    private void setTreatmentData(ExpandableListView expandableListView) {

        if (treatmentDatasets.size() == 0) {
            showAlert(getResources().getString(R.string.no_treatment));
        } else {
            treatmentPopupAdapter = new TreatmentPopupAdapterNew(mContext,
                    treatmentDatasets, EmailActivity.this);
            expandableListView.setAdapter(treatmentPopupAdapter);

            //collapsing all parents
            int count = treatmentPopupAdapter.getGroupCount();
            for (int i = 0; i < count; i++) {

                if (treatmentDatasets.get(i).isSelected()) {
                    expandableListView.expandGroup(i);
                } else {
                    expandableListView.collapseGroup(i);
                }
            }

            expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long id) {

                    boolean isSelected = treatmentDatasets.get(groupPosition).isSelected();

                    int selectedGroupCount = 0;
                    for (TreatmentDataset tmp : treatmentDatasets) {
                        if (tmp.isSelected())
                            selectedGroupCount++;
                    }

                    if (selectedGroupCount < 4 || isSelected) {

                        treatmentDatasets.get(groupPosition).setSelected(!isSelected);
                        treatmentPopupAdapter.notifyDataSetChanged();
                    } else {
                        expandableListView.collapseGroup(groupPosition);
                        showAlert(getResources().getString(R.string.maximum_items_error));
                        return true;
                    }

                    //increment/decrement group count
                    //unselect all child of this group
                    if (isSelected) {
                        selectedGroupCount--;
                        ArrayList<EmailTreatmentResponse.SubTreatmentOption_> tmp = treatmentDatasets.get(groupPosition).getChildDataSet();
                        for (int i = 0; i < tmp.size(); i++) {
                            tmp.get(i).setSelected(false);
                        }
                    } else {
                        selectedGroupCount++;
                    }

                    treatmentPopupAdapter.notifyDataSetChanged();

                    if (selectedGroupCount < 1) {
                        btn_add.setAlpha(0.5f);
                        btn_add.setEnabled(false);
                    } else {
                        btn_add.setAlpha(1f);
                        btn_add.setEnabled(true);
                    }

                    return false;
                }
            });


//                            expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
//
//                                int previousItem = -1;
//
//                                @Override
//                                public void onGroupExpand(int groupPosition) {
//
//                                    expandableListView.smoothScrollToPosition(groupPosition);
//                                    if (groupPosition != previousItem)
//                                        expandableListView.collapseGroup(previousItem);
//                                    previousItem = groupPosition;
//
//                                }
//                            });

            expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    ArrayList<EmailTreatmentResponse.SubTreatmentOption_> selectedGroupChildArr = treatmentDatasets.get(groupPosition).getChildDataSet();

                    int selectedChildCount = 0;
                    for (EmailTreatmentResponse.SubTreatmentOption_ tmp : selectedGroupChildArr) {
                        if (tmp.isSelected())
                            selectedChildCount++;
                    }

                    boolean isSelected = selectedGroupChildArr.get(childPosition).isSelected();
                    if (selectedChildCount < 4 || isSelected) {

                        selectedGroupChildArr.get(childPosition).setSelected(!isSelected);
                        treatmentPopupAdapter.notifyDataSetChanged();
                    } else {
                        showAlert(getResources().getString(R.string.maximum_items_error));
                    }

                    return true;
                }
            });

            if (getSelectedTreatmentDataset().size() < 1) {
                btn_add.setAlpha(0.5f);
                btn_add.setEnabled(false);
            } else {
                btn_add.setAlpha(1f);
                btn_add.setEnabled(true);
            }

            dialog.show();

        }
    }

    private ArrayList<TreatmentDataset> getSelectedTreatmentDataset() {
        if (treatmentDatasets == null)
            treatmentDatasets = new ArrayList<>();
        ArrayList<TreatmentDataset> selectedData = (ArrayList<TreatmentDataset>) treatmentDatasets.clone();
        selectedData.removeIf(treatmentDataset -> !treatmentDataset.isSelected());
        return selectedData;
    }




}