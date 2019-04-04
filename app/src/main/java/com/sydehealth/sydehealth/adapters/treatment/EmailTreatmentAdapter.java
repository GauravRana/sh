package com.sydehealth.sydehealth.adapters.treatment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sydehealth.sydehealth.R;
import com.sydehealth.sydehealth.adapters.OnItemClickListener;
import com.sydehealth.sydehealth.model.EmailTreatmentResponse;
import com.sydehealth.sydehealth.model.TreatmentDataset;
import com.sydehealth.sydehealth.utility.UsefullData;

import java.util.ArrayList;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class EmailTreatmentAdapter extends RecyclerView.Adapter {

    private OnItemClickListener mOnItemClickListener = null;
    private Context mContext = null;
    private ArrayList<TreatmentDataset> mList = null;
    private UsefullData usefullData;

    public EmailTreatmentAdapter(Context context, ArrayList<TreatmentDataset> list) {
        this.mContext = context;
        this.mList = list;
        usefullData = new UsefullData(mContext);
    }

    public void updateDataset(ArrayList<TreatmentDataset> list) {
        this.mList = list;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.selected_treatment_item, parent, false);
        RecyclerView.ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, int position) {
        ViewHolder holder = (ViewHolder) holder1;
        holder.txt_treatment_name.setTypeface(usefullData.get_montserrat_bold());
        holder.txt_treatment_desc.setTypeface(usefullData.get_montserrat_regular());

        holder.txt_treatment_name.setText(String.valueOf(position + 1) + ". " + mList.get(position).getHeadingName());

        if (!mList.get(position).getPreventionAdvice().isEmpty()) {
            holder.txt_treatment_desc.setText(mList.get(position).getPreventionAdvice());
        } else {
            holder.txt_treatment_desc.setVisibility(View.GONE);
        }

        holder.rv_subtreatment_images.setLayoutManager(new GridLayoutManager(mContext, 2));

        ArrayList<EmailTreatmentResponse.SubTreatmentOption_> selectedData = (ArrayList<EmailTreatmentResponse.SubTreatmentOption_>) mList.get(position).getChildDataSet().clone();
        selectedData.removeIf(SubTreatmentOption_ -> !SubTreatmentOption_.isSelected());

        //holder.txt_sub_treatment.setVisibility(selectedData.isEmpty() ? View.GONE : View.VISIBLE);

        SubTreatmentScreenAdapter treatmentScreenAdapterNew =
                new SubTreatmentScreenAdapter(mContext, selectedData);
        holder.rv_subtreatment_images.setAdapter(treatmentScreenAdapterNew);
    }

    @Override
    public int getItemCount() {
        if (mList == null)
            return 0;

        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_treatment_name;
        private TextView txt_treatment_desc;
        private TextView txt_sub_treatment;
        private RecyclerView rv_subtreatment_images;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_treatment_name = itemView.findViewById(R.id.txt_treatment_name);
            txt_treatment_desc = itemView.findViewById(R.id.txt_treatment_desc);
            txt_sub_treatment = itemView.findViewById(R.id.txt_sub_treatment);
            rv_subtreatment_images = itemView.findViewById(R.id.rv_subtreatment_images);

        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
