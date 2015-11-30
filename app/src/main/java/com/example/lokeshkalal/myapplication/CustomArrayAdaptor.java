package com.example.lokeshkalal.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class CustomArrayAdaptor extends BaseAdapter {
    private final Context context;
    private static HashMap<String, Bitmap> thumbnailList;
    private ArrayList<Data> mDataList;
    private static final String TAG = "CustomArrayAdaptor";

    public CustomArrayAdaptor(Context context, ArrayList<Data> values) {
        mDataList = values;
        this.context = context;
        thumbnailList = new HashMap<String, Bitmap>();
    }

    public void setDataList(ArrayList<Data> dataList) {
        mDataList = dataList;
    }


    @Override
    public int getCount() {
        return mDataList.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i(TAG, "getView");
        ViewHolder holder;
        Data data = mDataList.get(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_file, null);

            // configure view holder
            holder = new ViewHolder();
            holder.itemName = (TextView) convertView.findViewById(R.id.item_name);
            holder.itemModel = (TextView) convertView.findViewById(R.id.item_model);
            holder.dofPurchage = (TextView) convertView.findViewById(R.id.item_date);
            holder.warranty = (TextView) convertView.findViewById(R.id.item_warranty);
            holder.image = (RoundedImageView) convertView
                    .findViewById(R.id.icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (data.name != null)
            holder.itemName.setText(data.text);
        else
            holder.itemName.setText("TAG");
        if (data.brandName != null)
            holder.itemModel.setText(data.brandName);
        else
            holder.itemModel.setText("BRAND NAME");
        if (data.date != null) {
            holder.dofPurchage.setText(data.date);
        } else
            holder.dofPurchage.setText("PURCHASE DATE");
        if (data.date != null && data.expiryDate != null) {
            long leftTime = new Date(data.expiryDate).getTime() - new Date(data.date).getTime();
            holder.warranty.setText(milliSecondsToDays(leftTime));
        } else
            holder.warranty.setText("EXPIRY DATE");
        if (data.productImagePath != null) {
            Bitmap bmp = thumbnailList.get(data.productImagePath);
            if (bmp != null) {
                holder.image.setImageBitmap(bmp);
                bmp = null;
            } else {
                holder.image.setImageResource(R.drawable.product);
                holder.uri = data.productImagePath;
                new ImageLoadTask().execute(holder);
            }
        } else {
            holder.image.setImageResource(R.drawable.product);
        }

        return convertView;
    }

    private String milliSecondsToDays(long millSeconds) {
        Integer days = new Integer((int) (millSeconds / (24 * 60 * 60)));
        return days.toString();
    }

    private int getItemColor(int item) {
        int remainDays = (120 * item) % 365;
        int warranty = 1;
        int color;
        int percentage = (remainDays * 100) / (warranty * 365);
        if (percentage >= 80) {
            color = context.getResources().getColor(R.color.color1);
        } else if (percentage < 20) {
            color = context.getResources().getColor(R.color.color2);
        } else {
            color = context.getResources().getColor(R.color.color3);
        }
        return color;
    }

//    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) /*{
//        int targetWidth = 250;
//        int targetHeight = 250;
//        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
//                targetHeight,Bitmap.Config.ARGB_8888);
//
//        Canvas canvas = new Canvas(targetBitmap);
//        Path path = new Path();
//        path.addCircle(((float) targetWidth - 1) / 2,
//                ((float) targetHeight - 1) / 2,
//                (Math.min(((float) targetWidth),
//                        ((float) targetHeight)) / 2),
//                Path.Direction.CCW);
//
//        canvas.clipPath(path);
//        Bitmap sourceBitmap = scaleBitmapImage;
//        canvas.drawBitmap(sourceBitmap,
//                new Rect(0, 0, sourceBitmap.getWidth(),
//                        sourceBitmap.getHeight()),
//                new Rect(0, 0, targetWidth, targetHeight), null);
//        return targetBitmap;
//    }*/


    static class ViewHolder {
        public TextView itemName;
        public TextView itemModel;
        public TextView dofPurchage;
        public TextView warranty;
        public RoundedImageView image;
        public String uri;
    }

    private class ImageLoadTask extends AsyncTask<ViewHolder, String, ViewHolder> {

        @Override
        protected void onPreExecute() {
            Log.i("ImageLoadTask", "Loading image...");
        }

        // PARAM[0] IS IMG URL
        protected ViewHolder doInBackground(ViewHolder... param) {
            String uri = param[0].uri;
            Bitmap thumb1 = null;
            BitmapFactory.Options mOptions = new BitmapFactory.Options();
            mOptions.inSampleSize = 5;
            mOptions.outHeight = 300;
            mOptions.outWidth = 300;
            Log.i("ImageLoadTask", "Attempting to load image URL: " + uri);
            try {
                thumb1 = BitmapFactory.decodeFile(uri, mOptions);
                Log.d("ImageLoadTask", "uri  for image path is" + uri);
                thumbnailList.put(uri, thumb1);
            } catch (IllegalArgumentException e) {
                Log.d("ImageLoadTask", "something went wrong");
            }
            return param[0];
        }

        protected void onProgressUpdate(String... progress) {
            // NO OP
        }

        protected void onPostExecute(ViewHolder holder) {
            Bitmap bmp = thumbnailList.get(holder.uri);
            if (bmp != null) {
                Log.i("ImageLoadTask", "Successfully loaded ");
                if (this != null) {
                    // WHEN IMAGE IS LOADED NOTIFY THE ADAPTER
//                    notifyDataSetChanged();
                    holder.image.setImageBitmap(bmp);
                }
            } else {
                Log.e("ImageLoadTask", "Failed to load " + " image");
            }
        }
    }

}

