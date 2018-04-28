package com.example.niraanam.photosservermysql;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

public class Table_ShowSingle extends AppCompatActivity {

    HttpParse httpParse = new HttpParse();
    ProgressDialog pDialog;

    // Http Url For Filter Student Data from Id Sent from previous activity.
    String HttpURL = "http://150.107.31.104/sql_photo_android/table_single_filter.php";

    String finalResult ;
    HashMap<String,String> hashMap = new HashMap<>();
    String ParseResult ;
    HashMap<String,String> ResultHash = new HashMap<>();
    String FinalJSonObject ;

    TextView LatVal,LonVal,AziVal,ImgNVal,ImgPVal,ImgTVal,StampVal;

    String Value1Holder, Value2Holder, Value3Holder, Value4Holder, Value5Holder, Value6Holder, Value7Holder;

    String TempItem;
    ProgressDialog progressDialog2;
    Button theClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_showsingle);

        AziVal = (TextView) findViewById(R.id.txtAzimuth);
        LatVal = (TextView)findViewById(R.id.txtLatitude);
        LonVal = (TextView)findViewById(R.id.txtLongitude);
        ImgNVal = (TextView)findViewById(R.id.txtImageName);
        ImgPVal = (TextView)findViewById(R.id.txtImagePath);
        ImgTVal = (TextView)findViewById(R.id.txtImageDate);
        StampVal = (TextView)findViewById(R.id.txtTimeStamp);

        //Receiving the ListView Clicked item value send by previous activity.
        TempItem = getIntent().getStringExtra("ListViewValue");

        //Calling method to filter Student Record and open selected record.
        HttpWebCall(TempItem);

    }

    //Method to show current record Current Selected Record
    public void HttpWebCall(final String PreviousListViewClickedItem){

        class HttpWebCallFunction extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                pDialog = ProgressDialog.show(Table_ShowSingle.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                pDialog.dismiss();

                //Storing Complete JSon Object into String Variable.
                FinalJSonObject = httpResponseMsg ;

                //Parsing the Stored JSOn String to GetHttpResponse Method.
                new GetHttpResponse(Table_ShowSingle.this).execute();

            }

            @Override
            protected String doInBackground(String... params) {

                ResultHash.put("theID",params[0]);

                ParseResult = httpParse.postRequest(ResultHash, HttpURL);

                return ParseResult;
            }
        }

        HttpWebCallFunction httpWebCallFunction = new HttpWebCallFunction();

        httpWebCallFunction.execute(PreviousListViewClickedItem);
    }


    // Parsing Complete JSON Object.
    private class GetHttpResponse extends AsyncTask<Void, Void, Void>
    {
        public Context context;

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
            try
            {
                if(FinalJSonObject != null)
                {
                    JSONArray jsonArray = null;

                    try {
                        jsonArray = new JSONArray(FinalJSonObject);

                        JSONObject jsonObject;

                        for(int i=0; i<jsonArray.length(); i++)
                        {
                            jsonObject = jsonArray.getJSONObject(i);

                            // Storing Student Name, Phone Number, Class into Variables.
                            Value1Holder = jsonObject.getString("latitude").toString() ;
                            Value2Holder = jsonObject.getString("longitude").toString() ;
                            Value3Holder = jsonObject.getString("azimuth").toString() ;
                            Value4Holder = jsonObject.getString("img_name").toString() ;
                            Value5Holder = jsonObject.getString("img_path").toString() ;
                            Value6Holder = jsonObject.getString("img_datetime").toString() ;
                            Value7Holder = jsonObject.getString("stamp_updated").toString() ;

                        }
                    }
                    catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
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

            // Setting Student Name, Phone Number, Class into TextView after done all process .
            AziVal.setText(Value3Holder);
            LatVal.setText(Value1Holder);
            LonVal.setText(Value2Holder);
            ImgNVal.setText(Value4Holder);
            ImgPVal.setText(Value5Holder);
            ImgTVal.setText(Value6Holder);
            StampVal.setText(Value7Holder);



        }
    }


}