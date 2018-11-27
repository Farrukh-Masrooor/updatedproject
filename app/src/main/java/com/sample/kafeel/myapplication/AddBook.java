package com.sample.kafeel.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


public class AddBook extends Fragment implements View.OnClickListener{

    private   int PICK_IMAGE_REQUEST =1 ;
    EditText editText1, editText2;
    Button button,button2;
    ImageView imageView;
    SharedPreferences settings;
    static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath;
    Bitmap bitmap;
    Uri filePath;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_book_layout, container, false);
        editText1 = view.findViewById(R.id.book_name);
        editText2 = view.findViewById(R.id.book_des);
        imageView=view.findViewById(R.id.image_book);
        settings = getActivity().getSharedPreferences("MyPrefsFile", 0);
        button = view.findViewById(R.id.addBookButtoon);
        button2=view.findViewById(R.id.buttongallery);
        button2.setOnClickListener(this);

        button.setOnClickListener(this);
        int s = settings.getInt("user_id", 0);
        Log.d("My_log", "id" + s);
        return view;
    }



    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri photoURI=null;
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                Log.d("My_log","photouri"+photoFile.toString());
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d("My_log","io"+ex);

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                 photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }




    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
////            Bundle extras = data.getExtras();
////            Bitmap imageBitmap = (Bitmap) extras.get("data");
////            imageView.setImageBitmap(imageBitmap);
//            setPic();
//        }
//    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

         bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void onClick(View view) {
        String s1,s2,s3;s1=s2=s3=null;
        if (view.getId()==R.id.addBookButtoon) {
            s1 = editText1.getText().toString();
            s2 = editText2.getText().toString();
            if (s1!=null && s2!=null){
            if (!s1.equals("") && !s2.equals("") ) {
                s3 = convertBitmapToString(bitmap);
                LoadData data = new LoadData();
                data.execute(s1, s2, s3);

                editText1.setText("");
                editText2.setText("");}
            } else
                Toast.makeText(getActivity(), "fields are empty", Toast.LENGTH_SHORT).show();
        }
        else {
            showFileChooser();

            Bitmap bitmap1 = bitmap;

        }
        }


    public String convertBitmapToString(Bitmap bmp){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream); //compress to which format you want.

        byte[] byte_arr = stream.toByteArray();
        String encodedImage = android.util.Base64.encodeToString(byte_arr, android.util.Base64.DEFAULT);
        Log.d("My_log","encodwed "+encodedImage);
        return encodedImage;
    }

    public void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }






    class LoadData extends AsyncTask {

        String url = "http://192.168.56.1/android_connect/bookimage.php";

        @Override
        protected Object doInBackground(Object[] objects) {
            Log.d("My_log", "in async");

            String name = objects[0].toString();
            String des = objects[1].toString();
            String file=objects[2].toString();
            int s = settings.getInt("user_id", 0);
            String result = getResult(name, des,s,file);
            Log.d("My_log", "in backgrund" + result+"\n"+file);
            return result;
        }

        public String getResult(String name, String des,int i,String bitmap) {

            // Log.v(TAG, "Final Requsting URL is : :"+url);

            String line = "";
            String responseData = null;
            String s= String.valueOf(i);
            try {
                StringBuilder sb = new StringBuilder();
                String x = "";
                URL httpurl = new URL(url);
                //URLConnection tc= httpurl.openConnection();
                HttpURLConnection connection = (HttpURLConnection) httpurl.openConnection();
                connection.setRequestMethod("POST");

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("book_name", name)
                        .appendQueryParameter("book_description", des)
                        .appendQueryParameter("book_pic", bitmap)
                       .appendQueryParameter("user_id", s);
//                        .appendQueryParameter("user_password", pasword);


                String query = builder.build().getEncodedQuery();
//
//
                OutputStream out = new BufferedOutputStream(connection.getOutputStream());


                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                out.close();

                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_CONFLICT) {
                    Log.d("My_log", "after connect");
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));

                    while ((line = in.readLine()) != null) {
                        sb.append(line + "\n");
                        x = sb.toString();
                        Log.d("My_log", "" + x);
                    }
                    responseData = new String(x);
                } else {
                    Log.d("My_log", "unable to connect");
                    Toast.makeText(getContext(), "unable to connect", Toast.LENGTH_SHORT);
                    return null;
                }
            } catch (UnknownHostException uh) {
                Log.d("My_log", "Unknown host :");
                uh.printStackTrace();
            } catch (FileNotFoundException e) {
                Log.v("My_log", "FileNotFoundException :");
                e.printStackTrace();
            } catch (IOException e) {
                Log.v("My_log", "IOException :");
                e.printStackTrace();
            } catch (Exception e) {
                Log.v("My_log", "Exception :");
                e.printStackTrace();
            }
            return responseData;
        }



    }
}
