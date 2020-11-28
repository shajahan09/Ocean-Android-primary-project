package com.ocean.orcl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Emp_info_Activity extends AppCompatActivity {
    private Connection connection;

    private ArrayList<EmpInfo_Entity> empInfoItems = new ArrayList<EmpInfo_Entity>();
    CustomAdapter_EmpInformation adapter;
    ListView listView;
    private SearchView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_info);
        listView = findViewById(R.id.empInfo_listView);
        search = findViewById(R.id.search_empInfo);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        try {
            connection = com.ocean.orcl.ODBC.Db.createConnection();
            Log.d("connection", "=============emp_DB========Connect===========");
            if (connection != null) {
                empInfoItems = new ArrayList<EmpInfo_Entity>();
            }

            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery("select V_PERSON_NO, V_FNAME, V_LNAME, V_DEPT_NAME, V_DESIG_NAME, V_PHONE_MOBILE, V_EMAIL_OFFICIAL\n" +
                    "from BAS_PERSON\n" +
                    "where N_ACTIVE_FLAG=1\n" +
                    "and N_PERSON_TYPE=0\n" +
                    "and V_PERSON_NO<>'ADMINISTRATOR'\n" +
                    "order by v_fname");

            while (rs.next()) {

                empInfoItems.add(new EmpInfo_Entity(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7)));

            }
            adapter = new CustomAdapter_EmpInformation(this,empInfoItems);
            listView.setAdapter(adapter);
            search.setActivated(true);
            search.setQueryHint("Search First name...");
            search.onActionViewExpanded();
            search.setIconified(false);
            search.setSubmitButtonEnabled(true);
            search.clearFocus();
//            search.setIconifiedByDefault(false);


            connection.close();


        }catch (Exception e) {

            Toast.makeText(Emp_info_Activity.this, "" + e,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                if(empInfoItems.contains(query)){
//                    adapter.getFilter().filter(query);
//                }else{
//                    Toast.makeText(Emp_info_Activity.this, "No Match found",Toast.LENGTH_LONG).show();
//                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                int position =0;
                while (position < empInfoItems.size() -1){
                    if (empInfoItems.get(position).getF_name().contains(newText.toString().toUpperCase().trim())){
                        listView.smoothScrollToPositionFromTop(position,0,200);
                        break;
                    }else {
                        position++;
                    }
                }



                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                Toast.makeText(Emp_info_Activity.this,"posation"+position,Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(getApplicationContext(),EmpInfo_detiles_Activity.class);
                int itemId = (int) adapter.getItemId(position);


//                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("person_number",empInfoItems.get(position).persong_no);
                startActivity(intent);



            }
        });

    }

}
