package com.ocean.orcl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Blood_Bank_Activity extends AppCompatActivity {
    private Connection connection;
    private static final int REQUEST_CALL =1;
    private ArrayList<Blood_Bank_Entity> bloodBankItems;
    CustomAdapter_BloodBank adapter;
    ListView listView;
    private SearchView search;
    private Spinner spinner;
    EditText callText;
    Button callBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank);
        listView =findViewById(R.id.bloodBank_listView);
//        callBtn = findViewById(R.id.call_btn);
//        callText =findViewById(R.id.call_text);
//        search = findViewById(R.id.search_blood);
        spinner = findViewById(R.id.spinner_bloodBank);
        List<String>catagories = new ArrayList<>();
        catagories.add("All Blood Groups");
        catagories.add("A+");
        catagories.add("A-");
        catagories.add("B+");
        catagories.add("B-");
        catagories.add("AB+");
        catagories.add("AB-");
        catagories.add("O+");
        catagories.add("O-");
        catagories.add("N/A");
        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter<>(Blood_Bank_Activity.this,android.R.layout.simple_spinner_item,catagories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        defaultQueryRun();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                Toast.makeText(AttendenceLog_Activity.this,"posation"+position,Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(),BloodBank_datiles_Activity.class);
                int itemId = (int)adapter.getItemId(position);

                intent.putExtra("last_Name",bloodBankItems.get(position).L_name);
                intent.putExtra("phone_number",bloodBankItems.get(position).Phone_mobile);

                startActivity(intent);

            }
        });
spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(parent.getItemAtPosition(position).equals("All Blood Groups")){
            defaultQueryRun();

            //..............Or Use Do nothing just Fill Nill if Statement ...............
        }else {
            String items = parent.getItemAtPosition(position).toString();
            Toast.makeText(Blood_Bank_Activity.this,"Selected "+items,Toast.LENGTH_LONG).show();
            try {
                connection = com.ocean.orcl.ODBC.Db.createConnection();
                Log.d("connection", "=============BloodBnk_DB===Dropdowm=====Connect===========");
                if (connection != null) {
                    bloodBankItems = new ArrayList<Blood_Bank_Entity>();

                }

                Statement stmt = connection.createStatement();

                ResultSet rs = stmt.executeQuery("select V_FNAME, V_LNAME, V_DEPT_NAME, V_DESIG_NAME, V_PHONE_MOBILE,V_BLOOD_GRP\n" +
                        "from BAS_PERSON\n" +
                        "where N_ACTIVE_FLAG=1\n" +
                        "and N_PERSON_TYPE=0\n" +
                        "and V_PERSON_NO<>'ADMINISTRATOR'\n" +
                        "and(V_BLOOD_GRP = 'ALL' or V_BLOOD_GRP='"+items+"')\n" +
                        "order by v_fname");

                while (rs.next()) {

                    bloodBankItems.add(new Blood_Bank_Entity(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6)));

                }
                adapter = new CustomAdapter_BloodBank(Blood_Bank_Activity.this,bloodBankItems);
                listView.setAdapter(adapter);

                connection.close();


            }catch (Exception e) {

                Toast.makeText(Blood_Bank_Activity.this, "" + e,Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }


        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
});
//callBtn.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View v) {
//       makeCall();
//    }
//});


    }


    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>When {selected} for ALL Blood Group and DropDown menu set Query>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    public void defaultQueryRun(){
        try {
            connection = com.ocean.orcl.ODBC.Db.createConnection();
            Log.d("connection", "=============BloodBnk_DB========Connect===========");
            if (connection != null) {
                bloodBankItems = new ArrayList<Blood_Bank_Entity>();

            }

            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery("select V_FNAME, V_LNAME, V_DEPT_NAME, V_DESIG_NAME, V_PHONE_MOBILE,V_BLOOD_GRP\n" +
                    "from BAS_PERSON\n" +
                    "where N_ACTIVE_FLAG=1\n" +
                    "and N_PERSON_TYPE=0\n" +
                    "and V_PERSON_NO<>'ADMINISTRATOR'\n" +
                    "order by v_fname");


            while (rs.next()) {

                bloodBankItems.add(new Blood_Bank_Entity(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6)));


            }
            adapter = new CustomAdapter_BloodBank(Blood_Bank_Activity.this,bloodBankItems);
            listView.setAdapter(adapter);

            connection.close();


        }catch (Exception e) {

            Toast.makeText(Blood_Bank_Activity.this, "" + e,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }
//public void makeCall(){
//    String number = callText.getText().toString();
//    if(number.trim().length() >0){
//        if(ContextCompat.checkSelfPermission(this,
//                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(Blood_Bank_Activity.this,
//                    new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
//        }else {
//            String dail = "tel:"+number;
//            startActivity(new Intent(Intent.ACTION_CALL,Uri.parse(dail)));
//        }
//    }else {
//        Toast.makeText(getApplicationContext(),"Enter Phone Number",Toast.LENGTH_SHORT).show();
//
//    }
//
//
//}
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if(requestCode == REQUEST_CALL){
//            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                makeCall();
//            }else {
//                Toast.makeText(getApplicationContext(),"permission denied",Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
}
