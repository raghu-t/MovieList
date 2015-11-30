package com.example.lokeshkalal.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class ProductDescriptionActivity extends Activity {

    ImageView addTagButton;
    EditText editTextTag;
    LinearLayout tagItem;
    LinearLayout addTagLayout;
    TextView tagText;
    Boolean isTag = false;
    Button tagDelete;
    Button doneButton;
    ImageView mProductPhoto;
    public static final int RESULT_CAPTURE_IMAGE = 2;
    String productNameByUser;
    String productBrand;
    String productItem;
    String date;
    String expDate;

    long id;
    public static final int ADD_CAMERA_IMAGE = 1;
    public static final int ADD_GALLERY_IMAGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);
        init();
    }
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product_description, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    private void init() {
        editTextTag = (EditText) findViewById(R.id.tag_edit_text);
        addTagButton = (ImageView) findViewById(R.id.add_button);
        addTagButton.setOnClickListener(mClickListener);
        addTagLayout = (LinearLayout) findViewById(R.id.addlayout);
        tagItem = (LinearLayout) findViewById(R.id.tag_item);
        tagText = (TextView) findViewById(R.id.tag_text);
        tagDelete = (Button) findViewById(R.id.delete_tag);
        doneButton = (Button) findViewById(R.id.done_btn);
        mProductPhoto = (ImageView) findViewById(R.id.product_photo);

        mProductPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPhotoAction();
            }
        });
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToDB();
            }
        });
        tagDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTagLayout.setVisibility(View.VISIBLE);
                tagText.setText("");
                tagItem.setVisibility(View.GONE);
                isTag = false;
            }
        });

        Intent intent = getIntent();
        productNameByUser = intent.getStringExtra("nameByUser");
        productBrand = intent.getStringExtra("brand");
        productItem = intent.getStringExtra("product");
        date = intent.getStringExtra("date");
        expDate = intent.getStringExtra("expDate");
        id = intent.getLongExtra("id", -1);

    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String text = editTextTag.getText().toString();
            if (text != null || !text.equals("")) {
                addTagLayout.setVisibility(View.GONE);
                tagText.setText(text);
                tagItem.setVisibility(View.VISIBLE);
                isTag = true;
            }
        }
    };


    private void startPhotoAction() {
        CharSequence options[] = new CharSequence[]{"Take camera image", "Add from gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add product image");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    chooseImageFromCamera();
                }
                if (item == 1) {
                    chooseImageFromGallery();
                }
            }
        });
        builder.show();
    }
    private void chooseImageFromCamera() {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(captureIntent, ADD_CAMERA_IMAGE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == ADD_GALLERY_IMAGE) {
                Uri tempUri = data.getData();
                createProductImageFromURIAndStore(tempUri);
            }
            if (requestCode == ADD_CAMERA_IMAGE) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                createProductImageFromBitmapAndStore(bitmap);
            }
            Bitmap bitmap = BitmapFactory.decodeFile(getProductImagePath());
            mProductPhoto.setImageBitmap(bitmap);
        }
    }

    private void createProductImageFromURIAndStore(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (IOException e) {
            Toast.makeText(this, "FILE NOT FOUND", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(getProductImagePath());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createProductImageFromBitmapAndStore(Bitmap bitmap) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(getProductImagePath());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void chooseImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra("outputFormat",
                Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, ADD_GALLERY_IMAGE);
    }
    private void saveToDB() {
        Data data = new Data();
        if (isTag) {
            data.text = tagText.getText().toString();
        }
        data.id = id;
        data.name = productNameByUser; // get it from lokesh ;
        data.brandName = productBrand;
        data.productType = productItem ;
        data.date = date;
        data.expiryDate = expDate;
        data.billImagePath = getRecieptImagePath();
        data.productImagePath = getProductImagePath();
        DatabaseOperation databaseOperation = new DatabaseOperation(this);
        databaseOperation.open();
        databaseOperation.insert(data);
        databaseOperation.close();
        setResult(RESULT_OK);
        finish();

    }

    private String getRecieptImagePath() {
        return Environment.getExternalStorageDirectory() + File.separator + id + "_receipt.jpg";
    }
    private String getProductImagePath() {
        return Environment.getExternalStorageDirectory() + File.separator + id + "_product.jpg";
    }

}
