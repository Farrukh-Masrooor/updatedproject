package com.sample.kafeel.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

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
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Tab1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Tab1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tab1 extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ImageView imageView;
    Button button;
    EditText editText;
    SharedPreferences settings;
    GridView gridLayout;
    JsonHandler jsonHandler=null;
    FragmentManager manager;
    FragmentTransaction transaction;
    ArrayList<Books> booksArrayList;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Tab1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tab1.
     */
    // TODO: Rename and change types and number of parameters
    public static Tab1 newInstance(String param1, String param2) {
        Tab1 fragment = new Tab1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            booksArrayList=new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_tab1, container, false);
        //imageView=view.findViewById(R.id.bookimage);
        settings = getActivity().getSharedPreferences("MyPrefsFile", 0);
        jsonHandler=new JsonHandler();
        Glide.with(this).load(R.drawable.fab);
        //button=view.findViewById(R.id.getbookbutton);
        gridLayout=view.findViewById(R.id.gridlayout);

        //editText=view.findViewById(R.id.username);
        //button.setOnClickListener(this);
        //gridLayout.setAdapter(new MyBaseAdapter(getActivity().getApplicationContext(), booksArrayList));
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        LoadData data=new LoadData();
        data.execute();


        gridLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d("My_log","view is clicked");
                BookDetailsFragment fragment=new BookDetailsFragment();
                Bundle args=new Bundle();
                args.putString("book_name",jsonHandler.getBookname(position));
                args.putString("user_name",jsonHandler.getUserName(position));
                fragment.setArguments(args);
                manager = getFragmentManager();

                transaction = manager.beginTransaction().add(R.id.book_root_view, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

//    @Override
//    public void onClick(View view) {
//
//        String s=editText.getText().toString();
//        LoadData da=new LoadData();
//        da.execute(s);
//        //gridLayout.setAdapter(new MyBaseAdapter(getActivity().getApplicationContext(),booksArrayList));
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



    class LoadData extends AsyncTask {

        String url = "http://192.168.56.1/android_connect/getAllBooksDetails.php";

        @Override
        protected Object doInBackground(Object[] objects) {
            //Log.d("My_log", "in async");

            //String name = objects[0].toString();
           // String des = objects[1].toString();
            //String file=objects[2].toString();
            int s = settings.getInt("user_id", 0);
            //settings.getString("user_name","");
            String result = getResult(s);
            if (result!=null){
                //Log.d("My_log","bitmap not null");
            }
            //Log.d("My_log", "in backgrund" +result.toString());
            return result;
        }

        public String getResult( int i) {
            String r=null;

            // Log.v(TAG, "Final Requsting URL is : :"+url);

            String line = "";
            Bitmap bitmap=null;
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
                       // .appendQueryParameter("book_name", name)
                        //.appendQueryParameter("book_description", des)
                        //.appendQueryParameter("book_pic", bitmap)
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
                    //Log.d("My_log", "after connect");
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));

                    if(connection.getInputStream()!=null){
                        //Log.d("My_log","i/p stream not null");
                        byte[] byteimage;


                    while ((line = in.readLine()) != null) {
                        sb.append(line + "\n");
                        x = sb.toString();


                    }

                    responseData = new String(x);
                    //JsonHandler handler=new JsonHandler();
                     booksArrayList=jsonHandler.jsonViewer(responseData,getContext());
                       // Log.d("My_log", "in response data" +responseData);

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
            //Bitmap bitmap= (Bitmap) o;
            //if (bitmap!=null)

            //imageView.setImageBitmap(bitmap);}
            int i=booksArrayList.size();
            //Log.d("My_log","i====="+i);
            if (booksArrayList!=null || !booksArrayList.isEmpty()) {
                gridLayout.setAdapter(new MyBaseAdapter(getActivity().getApplicationContext(),booksArrayList));
              //  Log.d("My_log","onpostexe"+booksArrayList.get(0).book_name);
            }
        }
    }

}
