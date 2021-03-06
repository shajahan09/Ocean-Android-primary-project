package com.ocean.orcl;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Sales_Chalan_Activity extends AppCompatActivity {
    private Connection connection;
    private Spinner group_spinner,customerName_spinner,itemName_spinner;
    TextView text_formDate,text_toDate;
    String groupItem_id,item_id,customer_id,customer_contact;
    private ListView listView;
    private ArrayList<Billinvoice_Group_Entity> groupNameList;
    private ArrayList<Billinvoice_Customer_Entity> customerNameList;
    private ArrayList<Billinvoice_item_Entity> itemNameList;
    private ArrayList<SalesChalan_Result_Entity> resultList;
    private Billinvoice_Group_Adapter group_adapter;
    private Billinvoice_Customer_Adapter customer_adapter;
    private Billinvoice_item_Adapter item_adapter;
    private SalesChalan_Results_CustomAdapter result_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales__chalan);
        group_spinner =findViewById(R.id.salesChalan_Group_spinner);
        customerName_spinner =findViewById(R.id.salesChalan_customer_spinner);
        itemName_spinner =findViewById(R.id.salesChalan_item_spinner);
        text_formDate =findViewById(R.id.salesChalan_from_date);
        text_toDate =findViewById(R.id.salesChalan_to_date);
        listView =findViewById(R.id.salesChalan_result_listView);
        new CustomerName_Task().execute();
        currentDate();
        dateSetFROM();
        dateSetTO();
        customerName_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Billinvoice_Customer_Entity  clickedItem = (Billinvoice_Customer_Entity) parent.getItemAtPosition(position);
                customer_id =clickedItem.getCustomer_Id();
                if(clickedItem.getCustomer_Name().equals("<< Select Customer >>")){

                }else {
                    groupName_initList();;
                    Toast.makeText(getApplicationContext(),"selected "+clickedItem.getCustomer_Name(),Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        group_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Billinvoice_Group_Entity  clickedItem = (Billinvoice_Group_Entity) parent.getItemAtPosition(position);
                groupItem_id =clickedItem.itemGroup_Id;
                if(clickedItem.getItemGroup_Name().equals("<< Select Group >>")){

                }else {
                    itemName_initList();
                    Toast.makeText(getApplicationContext(),"selected "+clickedItem.getItemGroup_Name(),Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        itemName_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Billinvoice_item_Entity  clickedItem = (Billinvoice_item_Entity) parent.getItemAtPosition(position);
                item_id =clickedItem.getItem_id();
                if(clickedItem.getItem_name().equals("<< Select Item Name >>")){

                }else {
//                    showResult_initList();
                    new Result_Task().execute();
                    Toast.makeText(getApplicationContext(),"selected "+clickedItem.getItem_name(),Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void customerName_initList(){

        try {

            connection = com.ocean.orcl.ODBC.Db.createConnection();
            Log.d("connection","================Chalan Customer==Connected===========");
            if(connection != null){
                customerNameList = new ArrayList<>();

                Statement stmt=connection.createStatement();
                String query = "select CONTACT_ID,CONTACT_NAME\n" +
                        "from(\n" +
                        "select 1 sl,-1 CONTACT_ID,'<< Select Customer >>' CONTACT_NAME\n" +
                        "from dual\n" +
                        "union all\n" +
                        "SELECT 2 sl, CONTACT_ID, CONTACT_NAME\n" +
                        "FROM INV_CONTACT\n" +
                        ")\n" +
                        "order by sl,CONTACT_NAME";

                ResultSet rs=stmt.executeQuery(query);

                while(rs.next()) {
                    customerNameList.add(new Billinvoice_Customer_Entity(rs.getString(1),rs.getString(2)));
                    Log.d("value1","======Customer====1==========="+rs.getString(1));
                    Log.d("value2","======Customer====2==========="+rs.getString(2));

                }

            }


            connection.close();

        }
        catch (Exception e) {

            Toast.makeText(Sales_Chalan_Activity.this, " " + e,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }
    private void groupName_initList(){

        try {

            connection = com.ocean.orcl.ODBC.Db.createConnection();
            Log.d("connection","================Chalan Group==Connected===========");
            if(connection != null){
                groupNameList = new ArrayList<>();

                Statement stmt=connection.createStatement();
                String query = "select ITEMGROUP_ID,ITEMGROUP_NAME\n" +
                        "from(\n" +
                        "select 1 sl,-1 ITEMGROUP_ID,'<< Select Group >>' ITEMGROUP_NAME\n" +
                        "from dual\n" +
                        "union all\n" +
                        "SELECT 2 sl, g.ITEMGROUP_ID, g.ITEMGROUP_NAME\n" +
                        "FROM INV_ITEMGROUP g\n" +
                        ")\n" +
                        "order by sl,ITEMGROUP_NAME";

                ResultSet rs=stmt.executeQuery(query);

                while(rs.next()) {
                    groupNameList.add(new Billinvoice_Group_Entity(rs.getString(1),rs.getString(2)));
//                    Log.d("value1","======Group==ID====1========="+rs.getString(1));
//                    Log.d("value2","======Group==Name==2==========="+rs.getString(2));

                }
                group_adapter =new Billinvoice_Group_Adapter(getApplication(),groupNameList);
                group_spinner.setAdapter(group_adapter);
            }


            connection.close();

        }
        catch (Exception e) {

            Toast.makeText(Sales_Chalan_Activity.this, " " + e,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }
    private void itemName_initList(){

        try {

            connection = com.ocean.orcl.ODBC.Db.createConnection();
            Log.d("connection","================Chalan Item==Connected===========");
            if(connection != null){
                itemNameList = new ArrayList<>();

                Statement stmt=connection.createStatement();
                String query = "select ITEM_ID,ITEM_NAME\n" +
                        "from(\n" +
                        "select 1 sl,-1 ITEM_ID,'<< Select Item Name >>' ITEM_NAME\n" +
                        "from dual\n" +
                        "union all\n" +
                        "SELECT 2 sl, ITEM_ID, ITEM_NAME||' ('||UD_NO||')' ITEM_NAME\n" +
                        "FROM INV_ITEM\n" +
                        "WHERE ('"+groupItem_id+"'=-1 or ITEMGROUP_ID='"+groupItem_id+"')\n" +
                        ")\n" +
                        "order by sl,ITEM_NAME";

                ResultSet rs=stmt.executeQuery(query);

                while(rs.next()) {
                    itemNameList.add(new Billinvoice_item_Entity(rs.getString(1),rs.getString(2)));
                    Log.d("value1","======Item====1==========="+rs.getString(1));
                    Log.d("value2","======Item====2==========="+rs.getString(2));

                }
                item_adapter =new Billinvoice_item_Adapter(getApplication(),itemNameList);
                itemName_spinner.setAdapter(item_adapter);

            }


            connection.close();

        }
        catch (Exception e) {

            Toast.makeText(Sales_Chalan_Activity.this, " " + e,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }
    private void showResult_initList(){

        try {

            connection = com.ocean.orcl.ODBC.Db.createConnection();
//            Log.d("connection","================Show Result==Connected===========");
//            Log.d("fromDate","================fromDate==========="+text_formDate.getText());
//            Log.d("toDate","================toDate==========="+text_toDate.getText());
//            Log.d("query","=========P_CUSTOMER ="+customer_id+" P_Group_ID ="+groupItem_id+" P_ITEM_ID  ="+item_id);
            if(connection != null){
                resultList = new ArrayList<SalesChalan_Result_Entity>();

                Statement stmt=connection.createStatement();
                String query = "Select m.chalan_ID,chalan_NO,to_char(chalan_DATE,'MON DD,RRRR')chalan_DATE,\n" +
                        "Sum((Nvl(C.chalan_RATE,0)*Nvl(C.chalan_QTY, 0))+Nvl(C.VAT_AMT,0)-(Nvl(C.DISCOUNT_AMOUNT,0))) amount\n" +
                        "From vw_Inv_CHALANMst m, vw_Inv_CHALANChd C, Inv_Item I,inv_itemgroup ig,inv_mu u, inv_contact r\n" +
                        "Where m.chalan_Id = c.chalan_Id\n" +
                        "And C.ITEM_ID = I.ITEM_ID\n" +
                        "and  I.MU_ID=u.MU_ID\n" +
                        "And I.ITEMGROUP_ID = ig.ITEMGROUP_ID\n" +
                        "and m.CONTACT_ID=r.CONTACT_ID\n" +
                        "And ('"+customer_id+"' =-1 or m.CONTACT_ID = '"+customer_id+"')\n" +
                        "And ('"+groupItem_id+"' =-1 or ig.ITEMGROUP_ID = '"+groupItem_id+"')\n" +
                        "And ('"+item_id+"' =-1 or C.item_Id = '"+item_id+"')\n"+
                        "AND M.chalan_DATE BETWEEN to_date('"+text_formDate.getText()+"','MON DD,RRRR') AND to_date('"+text_toDate.getText()+"','MON DD,RRRR')\n" +
                        "Group By m.chalan_ID,chalan_NO,chalan_DATE\n" +
                        "Order By m.chalan_ID,chalan_NO,chalan_DATE";

                ResultSet rs=stmt.executeQuery(query);

                while(rs.next()) {
                    resultList.add(new SalesChalan_Result_Entity(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4)));
//                    Log.d("value1","======res====1==========="+rs.getString(1));
//                    Log.d("value2","======res====2==========="+rs.getString(2));
//                    Log.d("value3","======res====3==========="+rs.getString(3));
//                    Log.d("value4","======res====4==========="+rs.getString(4));

                }

            }


            connection.close();

        }
        catch (Exception e) {

            Toast.makeText(Sales_Chalan_Activity.this, " " + e,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }
    private class CustomerName_Task extends AsyncTask<Void,Void,ArrayList<Billinvoice_Customer_Entity>> {
        ProgressDialog loadingBar;
        @Override
        protected void onPreExecute() {
            loadingBar = new ProgressDialog(Sales_Chalan_Activity.this);
//            loadingBar.setTitle("Loading...");
            loadingBar.setMessage("Please Wait.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

        }

        @Override
        protected ArrayList<Billinvoice_Customer_Entity> doInBackground(Void... voids) {
            customerNameList = new ArrayList<>();
            customerName_initList();
            return customerNameList;
        }

        @Override
        protected void onPostExecute(ArrayList<Billinvoice_Customer_Entity> billinvoice_customer_entities) {

            customer_adapter =new Billinvoice_Customer_Adapter(getApplication(),customerNameList);
            customerName_spinner.setAdapter(customer_adapter);
            loadingBar.dismiss();
        }
    }
    private class Result_Task extends AsyncTask<Void,Void,ArrayList<SalesChalan_Result_Entity>> {
        ProgressDialog loadingBar;
        @Override
        protected void onPreExecute() {
            loadingBar = new ProgressDialog(Sales_Chalan_Activity.this);
            loadingBar.setTitle("Loading...");
            loadingBar.setMessage("Please Wait For Results.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

        }

        @Override
        protected ArrayList<SalesChalan_Result_Entity> doInBackground(Void... voids) {
            resultList = new ArrayList<SalesChalan_Result_Entity>();
            showResult_initList();
            return resultList;
        }

        @Override
        protected void onPostExecute(ArrayList<SalesChalan_Result_Entity> bill_result_entities) {
            super.onPostExecute(bill_result_entities);
            result_adapter =new SalesChalan_Results_CustomAdapter(getApplication(),resultList);
            listView.setAdapter(result_adapter);

            loadingBar.dismiss();
        }

    }
    private void dateSetFROM(){

        text_formDate.setOnClickListener(new View.OnClickListener() {
            final Calendar cal=Calendar.getInstance();
            int year=0,month=0,day=0;
            @Override
            public void onClick(View v) {
                if (year == 0 || month == 0 || day == 0) {

                    year =cal.get(Calendar.YEAR);
                    month=cal.get(Calendar.MONTH);
                    day =cal.get(Calendar.DAY_OF_MONTH);
                }

                DatePickerDialog mDatePicker=new DatePickerDialog(Sales_Chalan_Activity.this, new DatePickerDialog.OnDateSetListener()
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
                        text_formDate.setText(date.toUpperCase());
                        new Result_Task().execute();

                    }
                }, year, month, day);
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                mDatePicker.show();
            }
        });
    }
    private void dateSetTO(){

        text_toDate.setOnClickListener(new View.OnClickListener() {
            final Calendar cal=Calendar.getInstance();
            int year=0,month=0,day=0;
            @Override
            public void onClick(View v) {
                if (year == 0 || month == 0 || day == 0) {

                    year =cal.get(Calendar.YEAR);
                    month=cal.get(Calendar.MONTH);
                    day =cal.get(Calendar.DAY_OF_MONTH);
                }

                DatePickerDialog mDatePicker=new DatePickerDialog(Sales_Chalan_Activity.this, new DatePickerDialog.OnDateSetListener()
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
                        text_toDate.setText(date.toUpperCase());
//                        showResult_initList();
                        new Result_Task().execute();

                    }
                }, year, month, day);
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                mDatePicker.show();
            }
        });
    }
    private void currentDate(){
        String currentDate = new SimpleDateFormat("MMM dd,yyyy",Locale.getDefault()).format(new Date());
        text_formDate.setText(currentDate.toUpperCase());
        text_toDate.setText(currentDate.toUpperCase());
    }


}
