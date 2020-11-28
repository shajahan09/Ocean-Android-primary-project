package com.ocean.orcl;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class CurrentStock_Activity extends AppCompatActivity {
    private Connection connection;
    private ListView listView;
    private CustomAdapter_CurrentStock adapter;
    private ArrayList<CurrentStock_Entity_F> currentStockItems;
    private String manufacture_id,itemGroup_Id,item_Id,quantity;
    private Spinner manufacture_spinner,group_spinner,itemName_spinner,udNo_spinner,quantity_spinner;
    private ArrayList<CurrentStock_Manufacturer_Entity_A> manufactureList;
    private ArrayList<CurrentStock_Group_Entity_B> groupnameList;
    private ArrayList<CurrentStock_ItemName_Entity_C> itemNameList;
//    private ArrayList<CurrentStock_UD_No_Entity_D> udNoList;
    private ArrayList<CurrentStock_Quantity_Entity_E> qtyList;
    private Current_Stock_Manufacturer_Adapter manufacture_adapter;
    private Current_Stock_GroupName_Adapter group_adapter;
    private Current_Stock_ItemName_Adapter itmeName_adapter;
//    private Current_Stock_UN_No_Adapter unNo_adapter;
    private Current_Stock_Quantiy_Adapter qty_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_stock);
        manufacture_spinner = (Spinner) findViewById(R.id.menufacture_spinner);
        group_spinner = (Spinner) findViewById(R.id.group_spinner);
        itemName_spinner = (Spinner) findViewById(R.id.item_spinner);
//        udNo_spinner = (Spinner) findViewById(R.id.ud_no_spinner);
        quantity_spinner = (Spinner) findViewById(R.id.quentity_spinner);
        listView =findViewById(R.id.qty_listView);

        manufacture_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CurrentStock_Manufacturer_Entity_A clickedItem = (CurrentStock_Manufacturer_Entity_A) parent.getItemAtPosition(position);
                String manufacture_name =clickedItem.getMenufacture_Name();
                manufacture_id =clickedItem.getManufacture_Id();
                if(manufacture_name.equals("<< Select Manufacturer >>")){
                    //nothing do
                }else{
                    groupName_initList();
                    Toast.makeText(getApplication(),"selected : "+manufacture_name,Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        group_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CurrentStock_Group_Entity_B clickedItem = (CurrentStock_Group_Entity_B) parent.getItemAtPosition(position);
                String groupname =clickedItem.getItem_groupName();
                itemGroup_Id =clickedItem.getItem_Id();
//                Log.d("group_id","======test==========="+groupId+"\n"+"G_name "+groupname);


                if(itemGroup_Id.equals("-1")){
                    //nothing do
                }else{
                    itemName_initList();
//                    udNo_initList();
//                    quantity_initList();
                    Toast.makeText(getApplication(),"selected : "+groupname,Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        itemName_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               CurrentStock_ItemName_Entity_C clickedItem = (CurrentStock_ItemName_Entity_C) parent.getItemAtPosition(position);
               item_Id =clickedItem.getItem_Id();
               String item_name = clickedItem.getItem_Name();

               if(item_name.equals("<< Select Item Name >>")){
               }else {
                   quantity_initList();
                   Toast.makeText(CurrentStock_Activity.this,"selected : "+item_name,Toast.LENGTH_SHORT).show();

               }
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });
//        udNo_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                CurrentStock_UD_No_Entity_D clickedItem = (CurrentStock_UD_No_Entity_D) parent.getItemAtPosition(position);
//                String ud_no = clickedItem.getUd_no1();
//                if(ud_no.equals("<< Select UD No. >>")){
//
//                }else {
//                    Toast.makeText(getApplication(),"selected : "+ud_no,Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
        quantity_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CurrentStock_Quantity_Entity_E clickedItem = (CurrentStock_Quantity_Entity_E) parent.getItemAtPosition(position);
                 quantity =clickedItem.getQty();
                 String qty1 =clickedItem.getQty1();

//                 if(qty1.equals("<< Quantity: All >>")){
//
//                 }else{

//                     showAllData_query_F();
                     new QueryF_Task().execute();
//                     Toast.makeText(getApplication(),"selected : "+qty1,Toast.LENGTH_SHORT).show();
//                 }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //-----For Dialogue box show in asynTask------
        //        manufacturer_initList();
        new ManufactureInfoTask ().execute();
    }
    private void manufacturer_initList(){

        try {

            connection = com.ocean.orcl.ODBC.Db.createConnection();
            Log.d("connection","================Stock query==Connected===========");
            if(connection != null){
                manufactureList = new ArrayList<>();

                Statement stmt=connection.createStatement();
                String query = "select MANUFACTURER_ID,MANUFACTURER_NAME\n" +
                        "from(\n" +
                        "select 1 sl,-1 MANUFACTURER_ID,'<< Select Manufacturer >>' MANUFACTURER_NAME\n" +
                        "from dual\n" +
                        "union all\n" +
                        "Select distinct 2 sl,MANUFACTURER_ID, MANUFACTURER_NAME\n" +
                        "From INV_MANUFACTURER)\n" +
                        "order by sl,MANUFACTURER_NAME";

                ResultSet rs=stmt.executeQuery(query);

                while(rs.next()) {
                    manufactureList.add(new CurrentStock_Manufacturer_Entity_A(rs.getString(1),rs.getString(2)));
//                    Log.d("value1","==========1==========="+rs.getString(1));
//                    Log.d("value1","==========2==========="+rs.getString(2));
                }
            }


            connection.close();

        }
        catch (Exception e) {

            Toast.makeText(CurrentStock_Activity.this, " " + e,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }
    private void groupName_initList(){

        try {

            connection = com.ocean.orcl.ODBC.Db.createConnection();
            Log.d("connection","================Stock query==Connected===========");
            if(connection != null){
                groupnameList = new ArrayList<>();

                Statement stmt=connection.createStatement();
                String query = "select ITEMGROUP_ID,ITEMGROUP_NAME\n" +
                        "from(\n" +
                        "select 1 sl,-1 ITEMGROUP_ID,'<< Select Group >>' ITEMGROUP_NAME\n" +
                        "from dual\n" +
                        "union all\n" +
                        "SELECT distinct 2 sl, g.ITEMGROUP_ID, g.ITEMGROUP_NAME\n" +
                        "FROM INV_ITEMGROUP g, inv_item i\n" +
                        "where g.ITEMGROUP_ID=i.ITEMGROUP_ID \n" +
                        "and ('"+manufacture_id+"'=-1 or i.MANUFACTURER_ID='"+manufacture_id+"')\n" +
                        ")\n" +
                        "order by sl,ITEMGROUP_NAME";

                ResultSet rs=stmt.executeQuery(query);

                while(rs.next()) {
                    groupnameList.add(new CurrentStock_Group_Entity_B(rs.getString(1),rs.getString(2)));
                    Log.d("value1","======Group====1==========="+rs.getString(1));
                    Log.d("value2","======Group====2==========="+rs.getString(2));

                }
            }
            group_adapter =new Current_Stock_GroupName_Adapter(getApplication(),groupnameList);
            group_spinner.setAdapter(group_adapter);

            connection.close();

        }
        catch (Exception e) {

            Toast.makeText(CurrentStock_Activity.this, " " + e,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }
    private void itemName_initList(){
        try {

            connection = com.ocean.orcl.ODBC.Db.createConnection();
            Log.d("connection","================item_Name query==Connected===========");
            Log.d("item_id_nested","=====Group id====="+itemGroup_Id);
            Log.d("manufacture_id_nested","=====manufacture id====="+manufacture_id);
            if(connection != null){
                        itemNameList = new ArrayList<>();

                Statement stmt=connection.createStatement();
                String query = "select ITEM_ID,ITEM_NAME\n" +
                        "from(\n" +
                        "select 1 sl,-1 ITEM_ID,'<< Select Item Name >>' ITEM_NAME\n" +
                        "from dual\n" +
                        "union all\n" +
                        "SELECT distinct 2 sl, ITEM_ID, ITEM_NAME||' ('||UD_NO||')' ITEM_NAME\n" +
                        "FROM INV_ITEM\n" +
                        "WHERE ('"+manufacture_id+"'=-1 or MANUFACTURER_ID='"+manufacture_id+"') \n" +
                        "and ('"+itemGroup_Id+"'=-1 or ITEMGROUP_ID='"+itemGroup_Id+"')\n" +
                        ")\n" +
                        "order by sl,ITEM_NAME";

                ResultSet rs=stmt.executeQuery(query);

                while(rs.next()) {
//                            groupnameList.add(new CurrentStock_Group_Entity_B(rs.getString(1),rs.getString(2)));
                    Log.d("value1","==========1==========="+rs.getString(1));
                    Log.d("value2","==========2==========="+rs.getString(2));
                    itemNameList.add(new CurrentStock_ItemName_Entity_C(rs.getString(1),rs.getString(2)));
                }
                itmeName_adapter =new Current_Stock_ItemName_Adapter(CurrentStock_Activity.this,itemNameList);
                itemName_spinner.setAdapter(itmeName_adapter);

            }

            connection.close();

        }
        catch (Exception e) {

            Toast.makeText(CurrentStock_Activity.this, " " + e,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
//    private void udNo_initList(){
//        try {
//
//            connection = com.example.testorcl.ODBC.Db.createConnection();
//            Log.d("connection","================UD_No query==Connected===========");
//            Log.d("item_id_nested","=====UD No====="+groupId);
//            if(connection != null){
//                udNoList = new ArrayList<>();
//
//                Statement stmt=connection.createStatement();
//                String query = "select UD_NO,UD_NO1\n" +
//                        "from(\n" +
//                        "select 1 sl,'-1' UD_NO,'<< Select UD No. >>' UD_NO1\n" +
//                        "from dual\n" +
//                        "union all\n" +
//                        "SELECT 2 sl, UD_NO, UD_NO||' ('||ITEM_NAME||')' UD_NO1\n" +
//                        "FROM INV_ITEM\n" +
//                        "WHERE ('"+groupId+"'=-1 or ITEMGROUP_ID='"+groupId+"')\n" +
//                        ")\n" +
//                        "order by sl,UD_NO";
//
//                ResultSet rs=stmt.executeQuery(query);
//
//                while(rs.next()) {
//
//                    Log.d("value1","==========1==========="+rs.getString(1));
//                    Log.d("value2","==========2==========="+rs.getString(2));
//                    udNoList.add(new CurrentStock_UD_No_Entity_D(rs.getString(1),rs.getString(2)));
//                }
//                unNo_adapter =new Current_Stock_UN_No_Adapter(CurrentStock_Activity.this,udNoList);
//                udNo_spinner.setAdapter(unNo_adapter);
//
//            }
//
//            connection.close();
//
//        }
//        catch (Exception e) {
//
//            Toast.makeText(CurrentStock_Activity.this, " " + e,Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//        }
//
//    }
    private void quantity_initList(){
        try {

            connection = com.ocean.orcl.ODBC.Db.createConnection();
            Log.d("connection","================Qty query==Connected===========");
            if(connection != null){
                qtyList = new ArrayList<>();

                Statement stmt=connection.createStatement();
                String query = "select qty,qty1\n" +
                        "from(\n" +
                        "select 2 sl,1 qty,'<< Quantity: >0 >>' qty1\n" +
                        "from dual\n" +
                        "union all\n" +
                        "select 3 sl,2 qty,'<< Quantity: 0 >>' qty1\n" +
                        "from dual\n" +
                        ")\n" +
                        "order by sl,qty";

                ResultSet rs=stmt.executeQuery(query);

                while(rs.next()) {

                    Log.d("value1","==========1==========="+rs.getString(1));
                    Log.d("value2","==========2==========="+rs.getString(2));
                    qtyList.add(new CurrentStock_Quantity_Entity_E(rs.getString(1),rs.getString(2)));
                }
                qty_adapter =new Current_Stock_Quantiy_Adapter(CurrentStock_Activity.this,qtyList);
                quantity_spinner.setAdapter(qty_adapter);

            }

            connection.close();

        }
        catch (Exception e) {

            Toast.makeText(CurrentStock_Activity.this, " " + e,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }


    public void showAllData_query_F(){

    try {

        connection = com.ocean.orcl.ODBC.Db.createConnection();
        Log.d("connection","================Qty F==Connected===========");
        Log.d("F_query","================quantity=========== "+quantity);
        Log.d("F_query","================item id=========== "+item_Id);

        if(connection != null){
            currentStockItems = new ArrayList<CurrentStock_Entity_F>();

            Statement stmt=connection.createStatement();
            String query = "Select LOT_NO,to_char(EXP_DATE,'MON DD,RRRR') EXP_DATE,sum(CURR_STOCK) ||' '||MU_NAME CURR_STOCK\n" +
                    "From INV_ITEMSTOCK_SUMMARY@inv_core s\n" +
                    "where ITEM_ID='"+item_Id+"'\n" +
                    "and ('"+quantity+"' = -1 or ('"+quantity+"'= 1 and CURR_STOCK>0) or ('"+quantity+"' = 2 and CURR_STOCK=0))\n" +
                    "group by LOT_NO,EXP_DATE, MU_NAME\n" +
                    "Order By LOT_NO";

            ResultSet rs=stmt.executeQuery(query);

            while(rs.next()) {
                currentStockItems.add(new CurrentStock_Entity_F(rs.getString(1),rs.getString(2),rs.getString(3)));

//                Log.d("value1","==========1==========="+rs.getString(1));
//                Log.d("value2","==========2==========="+rs.getString(2));
//                Log.d("value2","==========2==========="+rs.getString(3));

            }

        }
//        adapter = new CustomAdapter_CurrentStock(this,currentStockItems);
//        listView.setAdapter(adapter);

        connection.close();

    }
    catch (Exception e) {

        Toast.makeText(CurrentStock_Activity.this, " " + e,Toast.LENGTH_SHORT).show();
        e.printStackTrace();
    }
}
    private class QueryF_Task extends AsyncTask<Void,Void,ArrayList<CurrentStock_Entity_F>> {
        ProgressDialog loadingBar;
        @Override
        protected void onPreExecute() {
            loadingBar = new ProgressDialog(CurrentStock_Activity.this);
            loadingBar.setTitle("Loading...");
            loadingBar.setMessage("Please Wait.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

        }

        @Override
        protected ArrayList<CurrentStock_Entity_F> doInBackground(Void... voids) {
            currentStockItems = new ArrayList<CurrentStock_Entity_F>();
//            groupName_initList();
//            manufacturer_initList();;
            showAllData_query_F();
            return currentStockItems;
        }

        @Override
        protected void onPostExecute(ArrayList<CurrentStock_Entity_F> currentStock_manufacturer_entity_as) {
            loadingBar.dismiss();
            adapter = new CustomAdapter_CurrentStock(CurrentStock_Activity.this,currentStockItems);
            listView.setAdapter(adapter);


        }

    }
    private class ManufactureInfoTask  extends AsyncTask<Void,Void,ArrayList<CurrentStock_Manufacturer_Entity_A>> {
        ProgressDialog loadingBar;
        @Override
        protected void onPreExecute() {
            loadingBar = new ProgressDialog(CurrentStock_Activity.this);
//            loadingBar.setTitle("Loading...");
            loadingBar.setMessage("Please Wait.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

        }

        @Override
        protected ArrayList<CurrentStock_Manufacturer_Entity_A> doInBackground(Void... voids) {
            manufactureList = new ArrayList<>();
//            groupName_initList();
            manufacturer_initList();;

            return manufactureList;
        }

        @Override
        protected void onPostExecute(ArrayList<CurrentStock_Manufacturer_Entity_A> currentStock_manufacturer_entity_as) {
            loadingBar.dismiss();
            manufacture_adapter =new Current_Stock_Manufacturer_Adapter(getApplication(),manufactureList);
            manufacture_spinner.setAdapter(manufacture_adapter);

        }

    }

}
