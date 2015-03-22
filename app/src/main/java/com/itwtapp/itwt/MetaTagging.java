package com.itwtapp.itwt;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class MetaTagging extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meta_tagging);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_meta_tagging, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public ImageView mImageView;
        private Bitmap mImageBitmap;
        String mCurrentPhotoPath;


        public PlaceholderFragment() {
        }

        private void setPic() {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
            int targetW = mImageView.getWidth();
            int targetH = mImageView.getHeight();

		/* Get the size of the image */
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
            int scaleFactor = 1;
            if ((targetW > 0) || (targetH > 0)) {
                scaleFactor = Math.min(photoW/targetW, photoH/targetH);
            }

		/* Set bitmap options to scale the image decode target */
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
            mImageBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

		/* Associate the Bitmap to the ImageView */
            mImageView.setImageBitmap(mImageBitmap);
            mImageView.setVisibility(View.VISIBLE);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_meta_tagging, container, false);

            mImageView = (ImageView) rootView.findViewById(R.id.meta_tagging_image_view);


            Bundle extras = getActivity().getIntent().getExtras();
            mCurrentPhotoPath = extras.getString("ImageURL");
            setPic();

//            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//            bmOptions.inJustDecodeBounds = true;
//            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
//            bmOptions.inJustDecodeBounds = false;
//            bmOptions.inSampleSize = 10;
//            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
//            mImageView.setImageBitmap(bitmap);

//            /* Get the size of the ImageView */
//            int targetW = mImageView.getWidth();
//            int targetH = mImageView.getHeight();
//
//		/* Get the size of the image */
//            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//            bmOptions.inJustDecodeBounds = true;
//            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
//            int photoW = bmOptions.outWidth;
//            int photoH = bmOptions.outHeight;
//
//		/* Figure out which way needs to be reduced less */
//            int scaleFactor = 1;
//            if ((targetW > 0) || (targetH > 0)) {
//                scaleFactor = Math.min(photoW/targetW, photoH/targetH);
//            }
//
//		/* Set bitmap options to scale the image decode target */
//            bmOptions.inJustDecodeBounds = false;
//            bmOptions.inSampleSize = scaleFactor;
//            bmOptions.inPurgeable = true;
//
//		/* Decode the JPEG file into a Bitmap */
//            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
//
//		/* Associate the Bitmap to the ImageView */
//            mImageView.setImageBitmap(bitmap);

            return rootView;
        }
    }
}
