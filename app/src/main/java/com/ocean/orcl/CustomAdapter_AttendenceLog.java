package com.ocean.orcl;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;


public class CustomAdapter_AttendenceLog  extends BaseAdapter implements Filterable {
    Context context;
    private ArrayList<AttendenceLog_Entity> aPERSON_NO;
    private ArrayList<AttendenceLog_Entity> aEMP_NAME;
    private ArrayList<AttendenceLog_Entity> aDESIGNATION;
    private ArrayList<AttendenceLog_Entity> aDEPARTMENT;
//    private ArrayList<AttendenceLog_Entity> aDate;
    private ArrayList<AttendenceLog_Entity> aLOGINTIME;
    private ArrayList<AttendenceLog_Entity> aLOGOUTTIME;
    private ArrayList<AttendenceLog_Entity> weekend;
    private ArrayList<AttendenceLog_Entity> holiday;
    private ArrayList<AttendenceLog_Entity> aLateLoginFlag;
    private ArrayList<AttendenceLog_Entity> aEarlyLogoutFlag;
    private ArrayList<AttendenceLog_Entity> allviewItems;
    private LayoutInflater inflater;
    private SelectedAttendenceLog selectedAttendenceLog;


    CustomAdapter_AttendenceLog( Context context, ArrayList<AttendenceLog_Entity> mItemsList, SelectedAttendenceLog log) {
        this.aPERSON_NO = mItemsList;
        this.aEMP_NAME = mItemsList;
        this.aDESIGNATION = mItemsList;
        this.aDEPARTMENT = mItemsList;
//        this.aDate = mItemsList;
        this.aLOGINTIME = mItemsList;
        this.aLOGOUTTIME = mItemsList;
        this.weekend = mItemsList;
        this.holiday = mItemsList;
        this.aLateLoginFlag =mItemsList;
        this.aEarlyLogoutFlag =mItemsList;
        this.allviewItems =mItemsList;
        inflater = LayoutInflater.from(context);

        this.selectedAttendenceLog = log;

    }

    @Override
    public int getCount() {
        return aEMP_NAME.size();
    }

    @Override
    public Object getItem(int position) {
        return aEMP_NAME.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.attendenceloglistview, parent, false);
        TextView personName = row.findViewById(R.id.personName_text);
        TextView empID = row.findViewById(R.id.empId_text);
        TextView designation = row.findViewById(R.id.designation_text);
        TextView depertment = row.findViewById(R.id.department_text);
//        TextView datefill = row.findViewById(R.id.date_text);
        TextView loginTime = row.findViewById(R.id.login_text);
        TextView logoutTime = row.findViewById(R.id.Logout_text);

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedAttendenceLog.getSelectedAttendence(aPERSON_NO.get(position));
            }
        });

        empID.setText(aPERSON_NO.get(position).persongID);
        personName.setText(aEMP_NAME.get(position).persongName);
        designation.setText(aDESIGNATION.get(position).persongDesignaton);
        depertment.setText(aDEPARTMENT.get(position).personDepartment);
//        datefill.setText(aDate.get(position).personDate);
        loginTime.setText(aLOGINTIME.get(position).persongLoginTime);
        logoutTime.setText(aLOGOUTTIME.get(position).personLogoutTime);



//            images.setImageResource(rImg[position]);
//            mytitle.setText(rtitle[position]);

        if(weekend.get(position).getWeekend().equals("N")  && holiday.get(position).getHoliday().equals("N") && aLateLoginFlag.get(position).getLateLogin_Flag().equals("Y")){
                    loginTime.setTextColor(Color.RED);
        }else {
//                    loginTime.setTextColor(Color.GREEN);
            loginTime.setTextColor(Color.parseColor("#4D850D"));
        }if(weekend.get(position).getWeekend().equals("N")  && holiday.get(position).getHoliday().equals("N") && aEarlyLogoutFlag.get(position).getEarlyLogOut_Flag().equals("Y")){
                    logoutTime.setTextColor(Color.RED);
        }else {
//                    logoutTime.setTextColor(Color.GREEN);
            logoutTime.setTextColor(Color.parseColor("#4D850D"));
        }


        return row;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<AttendenceLog_Entity> FilteredArrList = new ArrayList<AttendenceLog_Entity>();

                if (allviewItems == null) {
                    allviewItems = new ArrayList<AttendenceLog_Entity>(allviewItems); // saves the original data in mOriginalValues
                }

                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = allviewItems.size();
                    results.values = allviewItems;

                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < allviewItems.size(); i++) {
                        String data = allviewItems.get(i).persongName;
                        if (data.toLowerCase().contains(constraint.toString().trim())) {
//                            FilteredArrList.add(new AttendenceLog_Entity(allviewItems.get(i).persongID,allviewItems.get(i).persongName,allviewItems.get(i).persongDesignaton,allviewItems.get(i).personDepartment,allviewItems.get(i).persongLoginTime,allviewItems.get(i).personLogoutTime));
                            FilteredArrList.add(new AttendenceLog_Entity(allviewItems.get(i).persongID,allviewItems.get(i).persongName,allviewItems.get(i).persongDesignaton,allviewItems.get(i).personDepartment,allviewItems.get(i).persongLoginTime,allviewItems.get(i).personLogoutTime,allviewItems.get(i).weekend,allviewItems.get(i).holiday,allviewItems.get(i).lateLogin_Flag,allviewItems.get(i).personLogoutTime));

                        }

                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if(results.count == 0){
                    notifyDataSetInvalidated();
                }else {
                    aEMP_NAME = (ArrayList<AttendenceLog_Entity>) results.values;
                    aDEPARTMENT = (ArrayList<AttendenceLog_Entity>) results.values;
                    aDESIGNATION = (ArrayList<AttendenceLog_Entity>) results.values;
//                aDate = (ArrayList<AttendenceLog_Entity>) results.values;
                    aLOGINTIME = (ArrayList<AttendenceLog_Entity>) results.values;
                    aLOGOUTTIME = (ArrayList<AttendenceLog_Entity>) results.values;
                    aPERSON_NO = (ArrayList<AttendenceLog_Entity>) results.values;// has the filtered values
                    notifyDataSetChanged();
                }
            }
        };
        return filter;
    }


    public interface SelectedAttendenceLog{
        void getSelectedAttendence( AttendenceLog_Entity person_log);
    }

}
