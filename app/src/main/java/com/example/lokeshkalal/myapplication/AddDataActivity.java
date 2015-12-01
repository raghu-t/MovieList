package com.example.lokeshkalal.myapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;


public class AddDataActivity extends Activity {


    static final int REQUEST_DESCRIPTION = 0;
    ActionBar mActionBar;
    String name;
    EditText mProductType;
    EditText mBrandName;
    ImageView addPhotoView;
    String newUniqueName;
    EditText mName;
    private Button nextButton;

    EditText etdate;
    EditText etexpDate;

    long id;
    Data data;


    Bitmap roundImage;
    ImageView mRecentImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);
        init();
    }


    private void init() {
        //StartLokestTask;
        mActionBar = getActionBar();
        mActionBar.setElevation(0);

        mName = (EditText) findViewById(R.id.name);

        mProductType = (EditText) findViewById(R.id.edit_product_cat);
        mBrandName = (EditText) findViewById(R.id.edit_product_brand);

        mRecentImage = (ImageView) findViewById(R.id.reciept_image);

        etdate = (EditText) findViewById(R.id.edit_purchase);
        etexpDate = (EditText) findViewById(R.id.edit_exp);


        nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddDataActivity.this, ProductDescriptionActivity.class);
                intent.putExtra("nameByUser", mName.getText().toString());
                intent.putExtra("productBrand", mBrandName.getText().toString());
                intent.putExtra("productType", mProductType.getText().toString());
                intent.putExtra("id", id);
                intent.putExtra("date", data.date);
                intent.putExtra("expDate", data.expiryDate);
                startActivityForResult(intent, REQUEST_DESCRIPTION);
            }
        });
        id = getIntent().getLongExtra("id", -1);
        onChosenTheProduct();

        onLokeshCompleted();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_DESCRIPTION) {
                setResult(RESULT_OK);
                finish();
            }

            /*Bitmap bitmap = BitmapFactory.decodeFile(getProductImagePath());
            addPhotoView.setImageBitmap(bitmap);*/
        }
    }


    /*private String getImagePathForThisData() {
        String folderPath = getExternalCacheDir() + File.separator + name;
        File file = new File(folderPath);
        if (!file.exists()) {
            file.mkdir();
        }
        folderPath = folderPath + File.separator + "product.jpg";
        return folderPath;
    }

    private String getproductImageFilePath() {
        return getExternalCacheDir() + File.separator + name + "_product.jpg";
    }*/


    private void onChosenTheProduct() {
        nextButton.setAlpha(1.0f);
        nextButton.setEnabled(true);

    }

    private void onLokeshCompleted() {

        data = new Data();
        data.id = id;
        data.name = "Product Name"; // get it from lokesh ;
        data.brandName = "Samsung";
        data.productType = "Television";
        data.date = ("28/09/2013");
        data.expiryDate = ("27/09/2015");
        data.billImagePath = getRecieptImagePath();

        mProductType.setText(data.productType);
        mBrandName.setText(data.brandName);
        etdate.setText(data.date);
        etexpDate.setText(data.expiryDate);
        mRecentImage.setImageBitmap(BitmapFactory.decodeFile(getRecieptImagePath()));


    }

    private String getRecieptImagePath() {
        return Environment.getExternalStorageDirectory() + File.separator + id + "_receipt.jpg";
    }

    private String getProductImagePath() {
        return Environment.getExternalStorageDirectory() + File.separator + id + "_product.jpg";
    }







}
