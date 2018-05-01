package com.example.niraanam.photosservermysql;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Table_ShowSingle extends AppCompatActivity implements OnMapReadyCallback{

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



        //*** Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

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

            //new DownLoadImageTask(imgView).execute(Value5Holder);

            /*ImageButton   tran_btn_skip = (ImageButton) findViewById(R.id.imageButton);
            //ImageView   tran_btn_skip = (ImageView) findViewById(R.id.imageView);

            try {

                String FullName = String.valueOf(Value5Holder);
                String[] separated = FullName.split(" ");
                String thelink = separated[0]+"%20"+separated[1];
                //String thelink = Value5Holder;
                Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(thelink)
                        .getContent());
                tran_btn_skip.setImageBitmap(bitmap);
            } catch (Exception e) {
            }*/

            String FullName = String.valueOf(Value5Holder);
            String[] separated = FullName.split(" ");
            String thelink = separated[0]+"%20"+separated[1];

            new DownloadImageFromInternet((ImageView) findViewById(R.id.imageView))
                    .execute(thelink);

            SupportMapFragment mapFragment =
                    (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(Table_ShowSingle.this);

            // Setting Student Name, Phone Number, Class into TextView after done all process .
            AziVal.setText(Value3Holder);
            LatVal.setText(Value1Holder);
            LonVal.setText(Value2Holder);
            ImgNVal.setText(Value4Holder);
            ImgPVal.setText(Value5Holder);
            ImgTVal.setText(Value6Holder);
            StampVal.setText(Value7Holder);

        }

        private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
            ImageView imageView;

            public DownloadImageFromInternet(ImageView imageView) {
                this.imageView = imageView;
                pDialog = ProgressDialog.show(Table_ShowSingle.this,"Loading Photo...",null,true,true);
                //Toast.makeText(getApplicationContext(), "Please wait, Loading Photo...", Toast.LENGTH_LONG).show();
            }

            protected Bitmap doInBackground(String... urls) {
                String imageURL = urls[0];
                Bitmap bimage = null;
                try {
                    InputStream in = new java.net.URL(imageURL).openStream();
                    bimage = BitmapFactory.decodeStream(in);

                } catch (Exception e) {

                }
                return bimage;
            }

            protected void onPostExecute(Bitmap result) {
                pDialog.dismiss();
                imageView.setImageBitmap(result);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {


        boolean isDouble = isDouble(Value1Holder,Value2Holder);
        if (isDouble) {

            double thelat = Double.parseDouble(Value1Holder);
            double thelon = Double.parseDouble(Value2Holder);

            map.addMarker(new MarkerOptions().position(new LatLng(thelat, thelon)).title(Value5Holder));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(thelat,thelon), 12.0f));

        } else {

            map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Failure Location from Database"));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(0,0), 12.0f));
            Toast.makeText(getApplicationContext(),"Failure Location from Databas", Toast.LENGTH_LONG).show();

        }

    }

    public boolean isDouble(String x,String y) {
        boolean isValidDouble = false;
        try
        {
            Double.parseDouble(x);
            Double.parseDouble(y);

            // x y is a valid double

            isValidDouble = true;
        }
        catch (NumberFormatException ex)
        {
            // x y is not an double
        }

        return isValidDouble;
    }
}
