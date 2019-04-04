package com.sydehealth.sydehealth.adapters;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class Actors {

	
	public String pic,likes,title,description,created_date,id,picture,pic_id,likes2,comnt,username,data;
	public int item_id;
	public ArrayList<String> urls;
	public Bitmap file;
	public boolean selected,permanent;
	
	public Actors() {}

	public Actors(String data, int count,boolean permanent, boolean selected, Bitmap file, ArrayList<String> urls,int item_id, String pic,String likes,String title,String description,String created_date,String id,String picture,String pic_id,String likes2,String comnt,String username) {
		super();
		this.file=file;
		this.pic = pic;
		this.likes=likes;
		this.title=title;
		this.description=description;
		this.created_date=created_date;
		this.item_id=item_id;
		this.id=id;
		this.picture=picture;
		this.pic_id=pic_id;
		this.likes2=likes2;
		this.comnt=comnt;
		this.username=username;
		this.urls=urls;
		this.selected=selected;
		this.permanent=permanent;
		this.data=data;
		
	}

	public boolean getpermanent() {
		return permanent;
	}

	public void setpermanent(boolean permanent) {
		this.permanent = permanent;
	}

	public boolean getselected() {
		return selected;
	}

	public void setselected(boolean selected) {
		this.selected = selected;
	}

	public Bitmap get_file() {
		return file;
	}

	public void set_file(Bitmap file) {
		this.file = file;
	}


	public ArrayList<String> get_urls() {
		return urls;
	}

	public void set_urls(ArrayList<String> urls) {
		this.urls = urls;
	}

	public String getdata() {
		return data;
	}

	public void setdata(String data) {
		this.data = data;
	}
	
	public String getusername() {
		return username;
	}

	public void setusername(String username) {
		this.username = username;
	}
	
	public String getcomnt() {
		return comnt;
	}

	public void setcomnt(String comnt) {
		this.comnt = comnt;
	}
	
	public String getlikes2() {
		return likes2;
	}

	public void setlikes2(String likes2) {
		this.likes2 = likes2;
	}
	
	public String getpic_id() {
		return pic_id;
	}

	public void setpic_id(String pic_id) {
		this.pic_id = pic_id;
	}
	
	public String getpicture() {
		return picture;
	}

	public void setpicture(String picture) {
		this.picture = picture;
	}
	
	public String getid() {
		return id;
	}

	public void setid(String id) {
		this.id = id;
	}

	public int getItem_id() {
		return item_id;
	}

	public void setItem_id(int item_id) {
		this.item_id = item_id;
	}
	
	public String getcreated_date() {
		return created_date;
	}

	public void setcreated_date(String created_date) {
		this.created_date = created_date;
	}
	
	public String getdescription() {
		return description;
	}

	public void setdescription(String description) {
		this.description = description;
	}
	
	public String gettitle() {
		return title;
	}

	public void settitle(String title) {
		this.title = title;
	}
	
	public String getlikes() {
		return likes;
	}

	public void setlikes(String likes) {
		this.likes = likes;
	}

	public String getImage() {
		return pic;
	}

	public void setImage(String pic) {
		this.pic = pic;
	}

}
