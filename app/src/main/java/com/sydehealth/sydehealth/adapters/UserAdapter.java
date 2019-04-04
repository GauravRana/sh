package com.sydehealth.sydehealth.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sydehealth.sydehealth.utility.UsefullData;
import com.sydehealth.sydehealth.R;


public class UserAdapter extends ArrayAdapter<Actors> {
	ArrayList<Actors> actorList;
	
	LayoutInflater vi;
	int Resource;
	ViewHolder holder;
	UsefullData usefullData;

	

	public UserAdapter(Context context, int resource, ArrayList<Actors> objects) {
		super(context, resource, objects);
		vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Resource = resource;
		actorList = objects;
		usefullData= new UsefullData(context);
		
		
		
	}
 
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// convert view = design
		View v = convertView;
		if (v == null) {
			holder = new ViewHolder();
			v = vi.inflate(Resource, null);
			holder.user=(TextView) v.findViewById(R.id.user);
			holder.user_icon=(TextView) v.findViewById(R.id.user_icon);
			holder.user_arrow=(TextView) v.findViewById(R.id.user_arrow);
			holder.user_arrow.setTypeface(usefullData.get_awosome_font());
			holder.user_icon.setTypeface(usefullData.get_awosome_font());
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}

		holder.user.setText(actorList.get(position).getid());
		holder.user.setTypeface(usefullData.get_montserrat_regular());


		
		
		return v;

	}

	static class ViewHolder {

		public TextView user;
		public TextView user_icon, user_arrow;


	}



	
}