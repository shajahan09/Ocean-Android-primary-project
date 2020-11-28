package com.ocean.orcl;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ocean.orcl.ODBC.Db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AttendenceLog_Activity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, CustomAdapter_AttendenceLog.SelectedAttendenceLog {
    private Connection connection;

    private ArrayList<AttendenceLog_Entity> attendenceLogItems;
    private DatePickerDialog.OnDateSetListener mDateSetListener,mDateSetListener2;

   private CustomAdapter_AttendenceLog adapter;
   private ListView listView;
   private SearchView search;
   private SwipeRefreshLayout swipeRefreshL;
   private TextView from_dateFill;
   private String weekend,holiday,late_loginFlag,early_logoutFlag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence_log);

        search =findViewById(R.id.search);
        swipeRefreshL = findViewById(R.id.swipeRefresh);
        listView = findViewById(R.id.attendenceLog_listView);
        from_dateFill = findViewById(R.id.fromDate_Alog);
        TextView loginTime = findViewById(R.id.login_text);
        TextView logoutTime = findViewById(R.id.Logout_text);
////        searchDate =findViewById(R.id.searchDate_btn);


        //<<<<<<<<<<<-------------- Filled default set Textview in Current Date ------------->>>>>>>>>>>>>>>>>>>>>>
        final String dateCurrent2 = new SimpleDateFormat("MMM dd,yyyy", Locale.getDefault()).format(new Date());
        from_dateFill.setText(dateCurrent2.toUpperCase());
        Log.d("CurrentDate","======Default======="+dateCurrent2);
        //<<<<<<<<<<<--------------End Filled default set Textview in Current Date ------------->>>>>>>>>>>>>>>>>>>>>>


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        try {
            connection = com.ocean.orcl.ODBC.Db.createConnection();
            Log.d("connection", "=====================Connect===========");
            if (connection != null) {
               attendenceLogItems = new ArrayList<AttendenceLog_Entity>();

            }

            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery("select V_PERSON_NO, V_EMP_NAME, V_DESIGNATION, V_DEPARTMENT, \n" +
                    "to_char(D_LOGINTIME,'HH12:MI AM') V_LOGINTIME, \n" +
                    "to_char(D_LOGOUTTIME,'HH12:MI AM') V_LOGOUTTIME,\n" +
                    "decode(N_WEEKEND_FLAG,0,'N','Y') V_WEEKEND_FLAG,\n" +
                    "decode(N_HOLIDAY_FLAG,0,'N','Y') V_HOLIDAY_FLAG, \n" +
                    "decode(N_LATE_LOGIN_FLAG,0,'N','Y') V_LATE_LOGIN_FLAG, \n" +
                    "decode(N_EARLY_LOGOUT_FLAG,0,'N','Y') V_EARLY_LOGOUT_FLAG \n" +
                    "from VW_HR_EMP_ATTENDANCE_SUMMARY\n" +
                    "where D_DATE = to_date('"+dateCurrent2.toUpperCase()+"','MON DD,RRRR')\n" +
                    "order by V_EMP_NAME asc");

            while (rs.next()) {

                attendenceLogItems.add(new AttendenceLog_Entity(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10)));
                Log.d("1","======1===="+rs.getString(1));
                Log.d("2","======2===="+rs.getString(2));
                Log.d("3","======3===="+rs.getString(3));
                Log.d("4","======4===="+rs.getString(4));
                Log.d("5","======5+Weekend===="+rs.getString(5));
                Log.d("6","======6+Holiday===="+rs.getString(6));
                Log.d("7","======7+late login flag===="+rs.getString(7));
                Log.d("8","======8+early logout flag===="+rs.getString(8));
                Log.d("9","======9===="+rs.getString(9));
                Log.d("10","======10===="+rs.getString(10));


                weekend =rs.getString(5);
                holiday =rs.getString(6);
                late_loginFlag =rs.getString(7);
                early_logoutFlag =rs.getString(8);

//                if(weekend.equals("N") && holiday.equals("N") && late_loginFlag.equals("Y")){
////                    loginTime.setTextColor(Color.RED);
//                    Log.d("login","=======if======"+loginTime.getText());
//                }else {
////                    loginTime.setTextColor(Color.GREEN);
//                    Log.d("login","=======else======"+loginTime.getText());
//                }if(weekend.equals("N") && holiday.equals("N") && early_logoutFlag.equals("Y")){
////                    logoutTime.setTextColor(Color.RED);
//                    Log.d("logOut","=======if======"+logoutTime.getText());
//                }else {
////                    logoutTime.setTextColor(Color.GREEN);
//                    Log.d("logOut","=======else======"+logoutTime.getText());
//                }

            }
            adapter = new CustomAdapter_AttendenceLog(this,attendenceLogItems,AttendenceLog_Activity.this);
            listView.setAdapter(adapter);
            search.setActivated(true);
            search.setQueryHint("Enter Name Here...");
            search.onActionViewExpanded();
            search.setIconified(false);
            search.clearFocus();

            connection.close();


        }catch (Exception e) {

            Toast.makeText(AttendenceLog_Activity.this, "" + e,
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                attendenceLogItems.clear();
                 if(attendenceLogItems.contains(query)){
                    adapter.getFilter().filter(query);
                }else{
                    Toast.makeText(AttendenceLog_Activity.this, "No Match found",Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

               AttendenceLog_Activity.this.adapter.getFilter().filter(newText);

                return false;
            }
        });

        //<<<<<<<<<<<<<<<<<<<<<<<<<Button search date Query start >>>>>>>>>>>>>>>>>>>>>>>>>
//        searchDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });

        swipeRefreshL.setOnRefreshListener(this);
        dateSet();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent intent = new Intent(getApplicationContext(), AttendenceLog_Details_ativity.class);


                intent.putExtra("person_id",attendenceLogItems.get(position).persongID);
                final String dateChange = from_dateFill.getText().toString();
                intent.putExtra("current_date",dateChange);
                intent.putExtra("personName",attendenceLogItems.get(position).persongName);
                intent.putExtra("personDesignation",attendenceLogItems.get(position).persongDesignaton);
                intent.putExtra("person_department",attendenceLogItems.get(position).personDepartment);
//                intent.putExtra("position",itemId);

                startActivity(intent);

            }
        });
    }

    @Override
    public void onRefresh() {
        adapter = new CustomAdapter_AttendenceLog(this, attendenceLogItems,AttendenceLog_Activity.this);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        swipeRefreshL.setRefreshing(false);

    }

    public void dateSet(){

        from_dateFill.setOnClickListener(new View.OnClickListener() {
            final Calendar cal=Calendar.getInstance();
            int year=0,month=0,day=0;
            @Override
            public void onClick(View v) {
                if (year == 0 || month == 0 || day == 0) {

                    year =cal.get(Calendar.YEAR);
                    month=cal.get(Calendar.MONTH);
                    day =cal.get(Calendar.DAY_OF_MONTH);
                }

                DatePickerDialog mDatePicker=new DatePickerDialog(AttendenceLog_Activity.this, new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday)
                    {
//
                        year = selectedyear;
                        month = selectedmonth;
                        day = selectedday;

                        cal.setTimeInMillis(0);
                        cal.set(year, month, day, 0, 0, 0);
                        Date chosenDate = cal.getTime();

                        // Format the date using style medium and US locale
                        DateFormat df_medium_us = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
                        String date = df_medium_us.format(chosenDate);
                        from_dateFill.setText(date.toUpperCase());

                        try {
                            connection = Db.createConnection();
                            Log.d("connection", "==================Calader Query====Connect===========");
                            final String fromDate1 = from_dateFill.getText().toString();
                            Log.d("fromdate","=====picker Formate====="+fromDate1);

                            if (connection != null) {
                                attendenceLogItems = new ArrayList<AttendenceLog_Entity>();


                            }

                            Statement stmt = connection.createStatement();

//                            ResultSet rs = stmt.executeQuery("select V_PERSON_NO, V_EMP_NAME, V_DESIGNATION, V_DEPARTMENT, \n" +
//                                    "to_char(D_LOGINTIME,'HH12:MI AM') V_LOGINTIME, \n" +
//                                    "to_char(D_LOGOUTTIME,'HH12:MI AM') V_LOGOUTTIME\n" +
//                                    "from VW_HR_EMP_ATTENDANCE_SUMMARY\n" +
//                                    "where D_DATE = to_date('"+fromDate1.toUpperCase()+"','MON DD,RRRR')\n" +
//                                    "order by V_EMP_NAME asc");
                            ResultSet rs = stmt.executeQuery("select V_PERSON_NO, V_EMP_NAME, V_DESIGNATION, V_DEPARTMENT, \n" +
                                    "to_char(D_LOGINTIME,'HH12:MI AM') V_LOGINTIME, \n" +
                                    "to_char(D_LOGOUTTIME,'HH12:MI AM') V_LOGOUTTIME,\n" +
                                    "decode(N_WEEKEND_FLAG,0,'N','Y') V_WEEKEND_FLAG,\n" +
                                    "decode(N_HOLIDAY_FLAG,0,'N','Y') V_HOLIDAY_FLAG, \n" +
                                    "decode(N_LATE_LOGIN_FLAG,0,'N','Y') V_LATE_LOGIN_FLAG, \n" +
                                    "decode(N_EARLY_LOGOUT_FLAG,0,'N','Y') V_EARLY_LOGOUT_FLAG \n" +
                                    "from VW_HR_EMP_ATTENDANCE_SUMMARY\n" +
                                    "where D_DATE = to_date('"+fromDate1.toUpperCase()+"','MON DD,RRRR')\n" +
                                    "order by V_EMP_NAME asc");

                            while (rs.next()) {
                                attendenceLogItems.add(new AttendenceLog_Entity(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10)));

                            }
                            adapter = new CustomAdapter_AttendenceLog(AttendenceLog_Activity.this,attendenceLogItems,AttendenceLog_Activity.this);
                            listView.setAdapter(adapter);
                            connection.close();


                        }catch (Exception e) {

                            Toast.makeText(AttendenceLog_Activity.this, "" + e,
                                    Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, year, month, day);
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                mDatePicker.show();
            }
        });
    }

    @Override
    public void getSelectedAttendence(AttendenceLog_Entity person_log) {
       // Toast.makeText(this, "Get Selected item: "+person_log.getPersongName(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), AttendenceLog_Details_ativity.class);

        intent.putExtra("person_id",person_log.getPersongID());
        final String dateChange = from_dateFill.getText().toString();
        intent.putExtra("current_date",dateChange);
        intent.putExtra("personName",person_log.getPersongName());
        intent.putExtra("personDesignation",person_log.getPersongDesignaton());
        intent.putExtra("person_department",person_log.getPersonDepartment());
              //  intent.putExtra("position",itemId);

        startActivity(intent);
    }
}


