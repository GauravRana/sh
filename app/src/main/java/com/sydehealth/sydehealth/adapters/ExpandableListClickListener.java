package com.sydehealth.sydehealth.adapters;

public interface ExpandableListClickListener {

    void onListItemClick(int groupPos,String groupId, String groupName, String childId, String childName, boolean isSelected);

}
