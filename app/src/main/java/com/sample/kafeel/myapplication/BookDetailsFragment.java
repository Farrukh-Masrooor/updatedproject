package com.sample.kafeel.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.SecureDirectoryStream;
import java.util.ArrayList;

public class BookDetailsFragment extends Fragment {
    TextView textView1,textView2,textView3,textView4,textView5;
    ArrayList<Books> booksArrayList;
    //private JsonHandler jsonHandler;
    ImageView imageView;
    String s1,s2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.bookdetailslayout,container,false);
        textView1=view.findViewById(R.id.bookname);
        textView2=view.findViewById(R.id.bookdes);
        textView3=view.findViewById(R.id.username);
        textView4=view.findViewById(R.id.userplace);
        textView5=view.findViewById(R.id.usermail);
        imageView=view.findViewById(R.id.imageOfbook);
        Bundle args=getArguments();
       // jsonHandler=new JsonHandler();
        //textView.setText(args.getString("book_name")+args.getString("user_name"));
        s1=args.getString("book_name");
        s2=args.getString("user_name");
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        LoadData data=new LoadData();
        data.execute(s1,s2);
    }

    class LoadData extends AsyncTask {

        String url = "http://192.168.56.1/android_connect/getBookUser.php";

        @Override
        protected Object doInBackground(Object[] objects) {
            Log.d("My_log", "in async");

            String bookname = objects[0].toString();
            String username = objects[1].toString();
            //String file=objects[2].toString();
            //int s = settings.getInt("user_id", 0);
            //settings.getString("user_name","");
            String result = getResult(bookname,username);
            if (result!=null){Log.d("My_log","bitmap not null");}
            //Log.d("My_log", "in backgrund" +result.toString());
            return result;
        }

        public String getResult(String bookname,String username ) {
            String r=null;

            // Log.v(TAG, "Final Requsting URL is : :"+url);

            String line = "";
            Bitmap bitmap=null;
            String responseData = null;
           // String s= String.valueOf(i);
            try {
                StringBuilder sb = new StringBuilder();
                String x = "";
                URL httpurl = new URL(url);
                //URLConnection tc= httpurl.openConnection();
                HttpURLConnection connection = (HttpURLConnection) httpurl.openConnection();
                connection.setRequestMethod("POST");

                Uri.Builder builder = new Uri.Builder()
                         .appendQueryParameter("book_name", bookname)
                        .appendQueryParameter("user_name", username);
                        //.appendQueryParameter("book_pic", bitmap)
                        //.appendQueryParameter("user_id", s);
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

                    if(connection.getInputStream()!=null){
                        Log.d("My_log","i/p stream not null");
                        byte[] byteimage;


                        while ((line = in.readLine()) != null) {
                            sb.append(line + "\n");
                            x = sb.toString();


                        }

                        responseData = new String(x);
                        //JsonHandler handler=new JsonHandler();
                       // booksArrayList=jsonHandler.jsonViewer2(responseData,getContext());
                         Log.d("My_log", "in response data" +responseData);

//                    //responseData=    responseData.split(",")[0];
//                        Log.d("My_log", "in response data" +responseData);
//                    //byteimage=responseData.getBytes();
//                        //responseData.replace("-","+");
//                     byteimage= Base64.decode(responseData,Base64.DEFAULT);
//
//                    //Glide.with(getActivity()).load(byteimage);
//                    Log.d("My_log","byte after base64 decode");
//                    bitmap= BitmapFactory.decodeByteArray(byteimage,0,byteimage.length);
//                    //Log.d("My_log",bitmap.toString());
//                    if (bitmap!=null){
//                        Log.d("My_log","bitmap not nulll;");
//                    }
                    }
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
                Log.v("My_log", "Exception :"+e);
                e.printStackTrace();
            }

            return responseData;

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            String s=o.toString();
            if (s!=null)
                jsonViewer2(s);
//            //Bitmap bitmap= (Bitmap) o;
//            //if (bitmap!=null)
//
//            //imageView.setImageBitmap(bitmap);}
//            int i=booksArrayList.size();
//            Log.d("My_log","i====="+i);
//            if (booksArrayList!=null || !booksArrayList.isEmpty()) {
//               // gridLayout.setAdapter(new MyBaseAdapter(getActivity().getApplicationContext(),booksArrayList));
//                Log.d("My_log","onpostexe"+booksArrayList.get(0).book_name);
            }
        }


    public  void jsonViewer2(String string)
    {
        booksArrayList=new ArrayList<>();
        try {
            JSONArray jsonArray=new JSONArray(string);
            int length=jsonArray.length();
            for(int i=0;i<length;i++)
            {

                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String name=jsonObject.getString("book_name");
                textView1.setText(name);
                String des=jsonObject.getString("book_description");
                textView2.setText(des);
                String image=jsonObject.getString("book_pic");

                byte[] byteimage = Base64.decode(image, Base64.DEFAULT);
                imageView.setImageBitmap(BitmapFactory.decodeByteArray(byteimage,0,byteimage.length));

                String username=jsonObject.getString("user_name");
                textView3.setText(username);
                String userplace=jsonObject.getString("user_place");
                textView4.setText(userplace);
                String useremail=jsonObject.getString("user_email");
                textView5.setText(useremail);
                Log.d("My_log","username="+username+useremail);
                //booksArrayList.add(new Books(name,des,image,username,userplace,useremail));
                //MyBaseAdapter adapter=new MyBaseAdapter(context,name,des);
            }
            Log.d("My_log","array"+length);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    }



