package com.example.lokeshkalal.myapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends Activity {

    CustomArrayAdaptor mCustomAdapter;

    private static ArrayList<Data> mDataList;
    private boolean isSearchMode = false;
    private Spinner mSearchSpinner;
    private SearchView mSearchView;
    private DatabaseOperation mDataOperation;
    LayoutInflater mInflater;
    private String[] mSearchTypes = new String[]{"Tag", "Product", "Brand"};
    private String mCurrentSearchType = mSearchTypes[0];

    static final int ADD_GALLERY_IMAGE = 0;
    static final int ADD_CAMERA_IMAGE = 1;
    static final int ADD_DATA = 2;
    private long mainId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDataOperation = new DatabaseOperation(this);
        mDataOperation.open();
        getDefaultDisplayList();
        mCustomAdapter = new CustomArrayAdaptor(this, mDataList);
        ListView myListView = (ListView) findViewById(R.id.listView);
        myListView.setAdapter(mCustomAdapter);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent itemIntent = new Intent(MainActivity.this, ItemDetails.class);
                itemIntent.putExtra("name", mDataList.get(position).text);
                itemIntent.putExtra("brandName",mDataList.get(position).brandName);
                itemIntent.putExtra("date",mDataList.get(position).date);
                itemIntent.putExtra("expiryDate",mDataList.get(position).expiryDate);
                itemIntent.setData(Uri.parse(mDataList.get(position).productImagePath));
                startActivity(itemIntent);
            }
        });
        mInflater = LayoutInflater.from(getApplicationContext());

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getDefaultDisplayList() {
        mDataList = mDataOperation.getAllDataList();
        Log.i("rgu","mDataList "+mDataList.size());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (!isSearchMode) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_add) {
            mainId = System.currentTimeMillis();
            showThumbDialog();
            return true;
    }
        if (id == R.id.menu_search) {
            displaySearchActionBar();
            isSearchMode = true;
            invalidateOptionsMenu();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ADD_GALLERY_IMAGE) {
                Uri tempUri = data.getData();
                createImageFromURIAndStore(tempUri);
            }
            if (requestCode == ADD_CAMERA_IMAGE) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                createImageFromBitmapAndStore(bitmap);
            }
            if(requestCode == ADD_DATA) {
                getDefaultDisplayList();
                mCustomAdapter.setDataList(mDataList);
                mCustomAdapter.notifyDataSetChanged();
                return;
            }
            Intent intent = new Intent(MainActivity.this, AddDataActivity.class);

            intent.putExtra("id", mainId);
            startActivityForResult(intent, ADD_DATA);
        }

    }

    private void displaySearchActionBar() {
        ActionBar actionBar = this.getActionBar();
        View searchLayout = LayoutInflater.from(this).inflate(R.layout.search_layout, null);
        mSearchSpinner = (Spinner) searchLayout.findViewById(R.id.search_spinner);
        mSearchSpinner.setAdapter(mSearchSpinnerAdapter);
        int searchPlateId = searchLayout.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlate = searchLayout.findViewById(searchPlateId);
        searchPlate.setBackgroundColor(getResources().getColor(android.R.color.white));
        mSearchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCurrentSearchType = mSearchTypes[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSearchView = (SearchView) searchLayout.findViewById(R.id.searchview);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                switch (mCurrentSearchType) {
                    case "Tag" :
                        mDataList = mDataOperation.getDataListByText(query);
                        mCustomAdapter.setDataList(mDataList);
                        mCustomAdapter.notifyDataSetChanged();
                        break;
                    case "Product":
                        mDataList = mDataOperation.getDataListByProductType(query);
                        mCustomAdapter.setDataList(mDataList);
                        mCustomAdapter.notifyDataSetChanged();
                        break;
                    case "Brand":
                        mDataList = mDataOperation.getDataListByBrandName(query);
                        mCustomAdapter.setDataList(mDataList);
                        mCustomAdapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        actionBar.setCustomView(searchLayout);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        searchPlate.requestFocus();
    }

    private void showThumbDialog() {
        CharSequence options[] = new CharSequence[]{"Take camera image", "Add from gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add invoice");
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

    private void chooseImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra("outputFormat",
                Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, ADD_GALLERY_IMAGE);
    }

    @Override
    public void onBackPressed() {
        if (isSearchMode) {
            isSearchMode = false;
            ActionBar actionBar = getActionBar();
            actionBar.setDisplayShowCustomEnabled(false);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
            getDefaultDisplayList();
            mCustomAdapter.setDataList(mDataList);
            mCustomAdapter.notifyDataSetChanged();
            invalidateOptionsMenu();
            return;
        }
        super.onBackPressed();
    }

    private SpinnerAdapter mSearchSpinnerAdapter = new SpinnerAdapter() {

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.search_spinner_text_item, null);
            }
            String text = mSearchTypes[position];
            ((TextView) convertView).setText(text);
            return convertView;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public int getCount() {
            return mSearchTypes.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.search_spinner_text_item, null);
            }
            String text = mSearchTypes[position];
            ((TextView) convertView).setText(text);
            return convertView;
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }
    };

    private void createImageFromURIAndStore(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(getRecieptImagePath());
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

    private void createImageFromBitmapAndStore(Bitmap bitmap) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(getRecieptImagePath());
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

    private String getRecieptImagePath() {
        return Environment.getExternalStorageDirectory() + File.separator + mainId + "_receipt.jpg";
    }

}


