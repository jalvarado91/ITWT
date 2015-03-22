package com.itwtapp.itwt;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class IntroPortal extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_portal);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_intro_portal, menu);
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

        private static final int ACTION_TAKE_PHOTO_B = 1;

        private static final String BITMAP_STORAGE_KEY = "viewbitmap";
        private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";
//        private ImageView mImageView;
//        private Bitmap mImageBitmap;


        private String mCurrentPhotoPath;

        private static final String JPEG_FILE_PREFIX = "IMG_";
        private static final String JPEG_FILE_SUFFIX = ".jpg";

        private AlbumStorageDirFactory mAlbumStorageDirFactory = null;

        public PlaceholderFragment() {
        }

        private String getAlbumName() {
            return "ITWT";
        }


        private File getAlbumDir() {
            File storageDir = null;

            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

                storageDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);

                if (storageDir != null) {
                    if (! storageDir.mkdirs()) {
                        if (! storageDir.exists()){
                            Log.d("CameraSample", "failed to create directory");
                            return null;
                        }
                    }
                }

            } else {
                Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
            }

            return storageDir;
        }

        private File createImageFile() throws IOException {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
            File albumF = getAlbumDir();
            File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
            return imageF;
        }

        private File setUpPhotoFile() throws IOException {

            File f = createImageFile();
            mCurrentPhotoPath = f.getAbsolutePath();

            return f;
        }


        private void dispatchTakePictureIntent(int actionCode) {

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            switch(actionCode) {
                case ACTION_TAKE_PHOTO_B:
                    File f = null;

                    try {
                        f = setUpPhotoFile();
                        mCurrentPhotoPath = f.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    } catch (IOException e) {
                        e.printStackTrace();
                        f = null;
                        mCurrentPhotoPath = null;
                    }
                    break;

                default:
                    break;
            } // switch

            startActivityForResult(takePictureIntent, actionCode);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            switch (requestCode) {
                case ACTION_TAKE_PHOTO_B: {
                    if (resultCode == RESULT_OK) {
                        handleBigCameraPhoto();
                    }
                    break;
                } // ACTION_TAKE_PHOTO_B
            } // switch
        }

        private void handleBigCameraPhoto() {

            if (mCurrentPhotoPath != null) {
                sendSetPickIntent();
                galleryAddPic();
                mCurrentPhotoPath = null;
            }

        }

        private void sendSetPickIntent() {
            Intent intent = new Intent(getActivity().getApplicationContext(), MetaTagging.class);
            intent.putExtra("ImageURL", mCurrentPhotoPath);
            startActivity(intent);
        }

//        private void setPic() {
//
//		/* There isn't enough memory to open up more than a couple camera photos */
//		/* So pre-scale the target bitmap into which the file is decoded */
//
//		/* Get the size of the ImageView */
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
//            mImageView.setVisibility(View.VISIBLE);
//        }

        private void galleryAddPic() {
            Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
            File f = new File(mCurrentPhotoPath);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            getActivity().sendBroadcast(mediaScanIntent);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_intro_portal, container, false);

            Button dressMeButton = (Button) rootView.findViewById(R.id.dress_me_button);
            ImageButton addShirtButton = (ImageButton) rootView.findViewById(R.id.button_add_shirt);
            addShirtButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = rootView.getContext();
                    String text = "Launch camera to add shirt";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);
                }
            });

            dressMeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(rootView.getContext(), OutfitView.class);
                    startActivity(intent);
                }
            });

            return rootView;
        }
    }
}
