package com.example.niraanam.photosservermysql;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.StrictMode;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;


public class Table_ShowAllList extends AppCompatActivity {

    ListView TableListView;
    ProgressBar progressBar;
    String HttpUrl;
    int fixlist=0;
    int add = 20;
    List<String> IdList = new ArrayList<>();

    Table_ListAdapterClass adapter;

    Button more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_showalllist);

        //*** Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        more= (Button) findViewById(R.id.btnmore);



        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                TableListView.smoothScrollToPosition(adapter.getCount());



                add = add+20;
                HttpUrl = "http://150.107.31.104/sql_photo_android/all_sql_table.php?datanumber="+add;
                //fixlist = fixlist+add;

                new GetHttpResponse(Table_ShowAllList.this).execute();

                //TableListView.smoothScrollToPosition(fixlist);

                //Toast.makeText(getApplicationContext(), "TableListView.smoothScrollToPosition : "+ fixlist, Toast.LENGTH_LONG).show();

            }
        });

        TableListView = (ListView)findViewById(R.id.listView_table);

        progressBar = (ProgressBar)findViewById(R.id.progressBar_table);

        isInternetOn();

        //Adding ListView Item click Listener.
        TableListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // TODO Auto-generated method stub

                Intent intent = new Intent(Table_ShowAllList.this,Table_ShowSingle.class);

                // Sending ListView clicked value using intent.
                intent.putExtra("ListViewValue", IdList.get(position).toString());

                startActivity(intent);


            }
        });
    }

    public final  boolean isInternetOn() {

        if(isInternetAvailable()== true){

            HttpUrl = "http://150.107.31.104/sql_photo_android/all_sql_table.php";

            new GetHttpResponse(Table_ShowAllList.this).execute();


        }else{

            Toast.makeText(getApplicationContext(), " No Internet Connection!!! ", Toast.LENGTH_LONG).show();

            progressBar.setVisibility(View.GONE);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Table_ShowAllList.this);
            alertDialogBuilder.setTitle("Warning!!!");
            alertDialogBuilder.setMessage("You have no Internet connection, Please check for retrieving data from Database Server");

            alertDialogBuilder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    Intent intent = new Intent(Table_ShowAllList.this, Table_ShowAllList.class);
                    startActivity(intent);
                    finish();

                }
            });
            // set negative button: No message
            alertDialogBuilder.setNegativeButton("Exit the app", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    Table_ShowAllList.this.finish();

                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            // show alert
            alertDialog.show();

        }
        return false;
    }

    public boolean isInternetAvailable() {
        try {
            Process p1 = Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal==0);
            return reachable;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }


    // JSON parse class started from here.
    private class GetHttpResponse extends AsyncTask<Void, Void, Void>
    {
        public Context context;

        String JSonResult;

        List<Table_Photo> numList;

        public GetHttpResponse(Context context)
        {
            this.context = context;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0)
        {
            // Passing HTTP URL to HttpServicesClass Class.
            HttpServicesClass httpServicesClass = new HttpServicesClass(HttpUrl);
            try
            {
                httpServicesClass.ExecutePostRequest();

                if(httpServicesClass.getResponseCode() == 200)
                {
                    JSonResult = httpServicesClass.getResponse();

                    if(JSonResult != null)
                    {
                        JSONArray jsonArray = null;

                        try {
                            jsonArray = new JSONArray(JSonResult);

                            JSONObject jsonObject;

                            Table_Photo photo;

                            numList = new ArrayList<Table_Photo>();

                            for(int i=0; i<jsonArray.length(); i++)
                            {
                                photo = new Table_Photo();

                                jsonObject = jsonArray.getJSONObject(i);

                                // Adding Soil Id TO IdList Array.
                                IdList.add(jsonObject.getString("id").toString());

                                //Adding Num_mark point.

                                photo.last_modified = jsonObject.getString("stamp_updated").toString();

                                photo.image_name = jsonObject.getString("img_name").toString();

                                numList.add(photo);

                            }
                        }
                        catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
                else
                {
                    Toast.makeText(context, httpServicesClass.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)

        {
            progressBar.setVisibility(View.GONE);

            TableListView.setVisibility(View.VISIBLE);

            adapter = new Table_ListAdapterClass(numList, context);

            TableListView.setAdapter(adapter);

            //fixlist = adapter.getCount()-1;

            //fixlist = fixlist+10;

            //TableListView.smoothScrollToPosition(fixlist);


            fixlist = adapter.getCount();

            TableListView.smoothScrollToPosition(fixlist);

            Toast.makeText(getApplicationContext(), "TableListView.smoothScrollToPosition : "+ fixlist, Toast.LENGTH_LONG).show();

        }
    }
}
