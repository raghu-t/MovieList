package com.example.lokeshkalal.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;


public class updateActivity extends ActionBarActivity {
    final static String TAG="Grabhouse";
    GPSTracker gps;
    TextView textview;
    String lat, lon, address;
    Uri uri;
    ImageView imageView;
    private ProgressDialog progress;
    Button update;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        imageView = (ImageView) findViewById(R.id.updateimage);
        textview = (TextView) findViewById(R.id.textView);
        mContext=this;

        update = (Button) findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    uploadimage();
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("Turn on data connection to upload image");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }



            }
        });
        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();

            }
        });
        Intent data=getIntent();
        uri=data.getData();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap imageBitmap = BitmapFactory.decodeFile(uri.getPath(),options);
        imageView.setImageBitmap(imageBitmap);
        gpsTracking();
    }

    void uploadimage() {}

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_update, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    protected void gpsTracking() {

        gps = new GPSTracker(this);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            Log.i(TAG,"lat  "+Math.round(latitude * 10000));
            latitude = Math.round(latitude * 10000.0) / 10000.0;
            longitude = Math.round(longitude * 10000.0) / 10000.0;
            lat = Double.toString(latitude);
            lon = Double.toString(longitude);
            address = getAddressFormLatLang(latitude, longitude);
            String completeaddress = new String("Lat : " + lat + "  Lon: " + lon + "\n" +"Address : "+ address);
            textview.setText(completeaddress);
            textview.invalidate();
            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

    }


    protected String getAddressFormLatLang(double lat, double lang) {
        Log.i(TAG, "MapActivity getAddressFormLatLang");
        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lang, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder();
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    return   strReturnedAddress.append(returnedAddress.getAddressLine(i)).toString();
                }
                Log.i(TAG, "Returned Address = " + strReturnedAddress.toString());

                Toast.makeText(getApplicationContext(), strReturnedAddress.toString(), Toast.LENGTH_LONG).show();
            } else {
                Log.i(TAG, "No Address returned!");
                Toast.makeText(getApplicationContext(), "No Address returned!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}
