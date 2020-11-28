package com.ocean.orcl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter_EmpInformation extends BaseAdapter implements Filterable {
    Context context;
//    private ArrayList<EmpInfo_Entity>personNo;
//    private ArrayList<EmpInfo_Entity>Fname;
//    private ArrayList<EmpInfo_Entity>Lname;
//    private ArrayList<EmpInfo_Entity>DeptName;
//    private ArrayList<EmpInfo_Entity>DesignName;
//    private ArrayList<EmpInfo_Entity>PhoneMobile;
//    private ArrayList<EmpInfo_Entity>EmailOffice;
    private ArrayList<EmpInfo_Entity>empTest;
    private List<EmpInfo_Entity> empAllItems;
    private LayoutInflater inflater;

    public CustomAdapter_EmpInformation(Context context, ArrayList<EmpInfo_Entity> empItemsList) {
           this.context =context;
//        this.personNo = empItemsList;
//        this.Fname = empItemsList;
//        this.Lname = empItemsList;
//        this.DeptName = empItemsList;
//        this.DesignName = empItemsList;
//        this.PhoneMobile = empItemsList;
//        this.EmailOffice = empItemsList;
        this.empTest = empItemsList;
        this.empAllItems = empItemsList;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return empAllItems.size();
    }

    @Override
    public Object getItem(int position) {
        return empAllItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.emp_info_listview, parent, false);
        TextView personNbr = row.findViewById(R.id.person_no_text);
        TextView fName = row.findViewById(R.id.f_name_text);
        TextView lName = row.findViewById(R.id.l_name_text);
        TextView department = row.findViewById(R.id.dept_name_text);
        TextView designation = row.findViewById(R.id.desig_name_text);
        TextView phone = row.findViewById(R.id.phone_mobile_text);
        TextView email = row.findViewById(R.id.email_office_text);

        personNbr.setText(empTest.get(position).persong_no);
        fName.setText(empTest.get(position).F_name);
        lName.setText(empTest.get(position).L_name);
        department.setText(empTest.get(position).Dept_name);
//        datefill.setText(aDate.get(position).personDate);
        designation.setText(empTest.get(position).Desig_name);
        phone.setText(empTest.get(position).Phone_mobile);
        email.setText(empTest.get(position).Email_office);

        //       <<<<<<<<<<<<<<<<<<------ when textView value are null then textView fill are hide----->>>>>>>>>>>>>

        if(personNbr.equals(isEmpty()) || personNbr.equals(null) || personNbr.length()==0 || personNbr.equals(""))
        {
            personNbr.setVisibility(View.GONE);
        } else{
            personNbr.setVisibility(View.VISIBLE);
        }
        if(fName.equals(isEmpty()) || fName.equals(null) || fName.length()==0 || fName.equals(""))
        {
            fName.setVisibility(View.GONE);
        } else{
            fName.setVisibility(View.VISIBLE);
        }if(lName.equals(isEmpty()) || lName.equals(null) || lName.length()==0 || lName.equals(""))
        {
            lName.setVisibility(View.GONE);
        } else{
            lName.setVisibility(View.VISIBLE);
        }if(department.equals(isEmpty()) || department.equals(null) || department.length()==0 || department.equals(""))
        {
            department.setVisibility(View.GONE);
        } else{
            department.setVisibility(View.VISIBLE);
        }if(designation.equals(isEmpty()) || designation.equals(null) || designation.length()==0 || designation.equals(""))
        {
            designation.setVisibility(View.GONE);
        } else{
            designation.setVisibility(View.VISIBLE);
        }if(phone.equals(isEmpty()) || phone.equals(null) || phone.length()==0 || phone.equals(""))
        {
            phone.setVisibility(View.GONE);
        } else{
            phone.setVisibility(View.VISIBLE);
        }if(email.equals(isEmpty()) || email.equals(null) || email.length()==0 || email.equals(""))
        {
            email.setVisibility(View.GONE);
        } else{
            email.setVisibility(View.VISIBLE);
        }

        return row;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<EmpInfo_Entity> FilteredArrList = new ArrayList<EmpInfo_Entity>();

                if (empAllItems == null) {
                    empAllItems = new ArrayList<EmpInfo_Entity>(empAllItems); // saves the original data in mOriginalValues
                }

                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = empAllItems.size();
                    results.values = empAllItems;

                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < empAllItems.size(); i++) {
                        String data = empAllItems.get(i).getF_name();
//                        String data1 = empAllItems.get(i).getDept_name();
                        if (data.toLowerCase().contains(constraint.toString().trim())){
                            FilteredArrList.add(new EmpInfo_Entity(empAllItems.get(i).getPersong_no(),empAllItems.get(i).getF_name(),empAllItems.get(i).getL_name(),empAllItems.get(i).getDept_name(),empAllItems.get(i).getDesig_name(),empAllItems.get(i).getPhone_mobile(),empAllItems.get(i).getEmail_office()));

                        }

                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

//                    personNo = (ArrayList<EmpInfo_Entity>) results.values;
//                    Fname = (ArrayList<EmpInfo_Entity>) results.values;
//                    Lname = (ArrayList<EmpInfo_Entity>) results.values;
//                    DeptName = (ArrayList<EmpInfo_Entity>) results.values;
//                    DesignName = (ArrayList<EmpInfo_Entity>) results.values;
//                    PhoneMobile = (ArrayList<EmpInfo_Entity>) results.values;
//                    EmailOffice = (ArrayList<EmpInfo_Entity>) results.values;// has the filtered values

                    notifyDataSetChanged();

            }
        };
        return filter;
    }

}
