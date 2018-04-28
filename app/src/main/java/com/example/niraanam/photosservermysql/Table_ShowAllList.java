package com.example.niraanam.photosservermysql;

import android.support.v7.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.StrictMode;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    String HttpUrl = "http://150.107.31.104/sql_photo_android/all_sql_table.php";
    List<String> IdList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_showalllist);

        //*** Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        TableListView = (ListView)findViewById(R.id.listView_table);

        progressBar = (ProgressBar)findViewById(R.id.progressBar_table);

        new GetHttpResponse(Table_ShowAllList.this).execute();

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
                //Finishing current activity after open next activity.
                //finish();

            }
        });
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

            Table_ListAdapterClass adapter = new Table_ListAdapterClass(numList, context);

            TableListView.setAdapter(adapter);

        }
    }
}
