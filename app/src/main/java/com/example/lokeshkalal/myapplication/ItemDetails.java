package com.example.lokeshkalal.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Date;


public class ItemDetails extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        ImageView itemImage = (ImageView) findViewById(R.id.item_image);
        TextView dofPur = (TextView) findViewById(R.id.item_date);
        TextView warranty = (TextView) findViewById(R.id.item_warranty);
        TextView itemName = (TextView) findViewById(R.id.item_name);
        TextView itemModel = (TextView) findViewById(R.id.item_model);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        Intent data = getIntent();
        Uri uri = data.getData();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 3;
        itemName.setText(data.getStringExtra("name"));
        itemModel.setText(data.getStringExtra("brandName"));
        String date = data.getStringExtra("date");
        String expiryDate = data.getStringExtra("expiryDate");
        dofPur.setText(date);
        warranty.setText(expiryDate);

        int percentage = (int) ((new Date(expiryDate).getTime() -
                new Date(date).getTime()) / (System.currentTimeMillis() - new Date(date).getTime()));
        progressBar.setProgress(percentage);

        final Bitmap imageBitmap = BitmapFactory.decodeFile(uri.getPath(), options);
        itemImage.setImageBitmap(imageBitmap);

        Button btn = (Button) findViewById(R.id.view_bill);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBillDialog(imageBitmap);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds it`ems to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_item_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    private void showBillDialog(Bitmap image) {
        LayoutInflater mInflater = LayoutInflater.from(getApplicationContext());
        View view = mInflater.inflate(R.layout.view_bill_dailog, null);
        ImageView imageView = (ImageView)view.findViewById(R.id.view_bill);
        imageView.setImageBitmap(image);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Bill")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        dialog.dismiss();
                    }
                })
                .setView(view)
                .show();
    }
}
