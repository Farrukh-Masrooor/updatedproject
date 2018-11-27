package com.sample.kafeel.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
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

public class MainActivity extends AppCompatActivity {

    DatabaseHelper db;
    EditText e1, e2, e3;
    Button b1;
    FrameLayout relativeLayout;
    RelativeLayout layout;
    final String PREFS_NAME = "MyPrefsFile";
    final String Login_Prefs = "My_Login_Prefs";
    SharedPreferences settings;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("My_log","oncreate");
        relativeLayout = findViewById(R.id.login_root_view);
        layout = findViewById(R.id.layout);
        isAlreadyLogin();
        db = new DatabaseHelper(this);
        e1 = (EditText) findViewById(R.id.email);
        e2 = (EditText) findViewById(R.id.pass);
        b1 = (Button) findViewById(R.id.register);
    }

    public int getIdFromJson(String string)
    {
        int id=0;
        JSONArray array= null;
        try {
            array = new JSONArray(string);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
//            id=array.getInt(0);
           JSONObject jsonObject = array.getJSONObject(0);
             id=jsonObject.getInt("user_id");
            Log.d("My_log",""+id);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("My_log","json "+e);
        }
        return id;
    }

    public void isAlreadyLogin() {

        settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("my_login", false)) {
            Intent intent = new Intent(this, Main2Activity.class);
            startActivity(intent);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;


    }



    private String isLoginsuccess() {

        String usermail = e1.getText().toString();
        String password = e2.getText().toString();
        return "";
    }

    public void registerUser(View view) {
        //layout.setVisibility(View.INVISIBLE);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction().add(R.id.login_root_view, new RegisterFragment());
       transaction.addToBackStack("tag");
        transaction.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        layout.setVisibility(View.VISIBLE);
        Log.d("My_log","onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        layout.setVisibility(View.VISIBLE);
        Log.d("My_log","onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        layout.setVisibility(View.VISIBLE);
        Log.d("My_log","onstart");
    }



    public void loginCheck(View view) {
        String s1 = e1.getText().toString();
        String s2 = e2.getText().toString();


        if (s1.equals("") || s2.equals("") ) {
            Toast.makeText(getApplicationContext(), "Fields are Empty", Toast.LENGTH_SHORT).show();
        }

        else if (!s1.trim().matches(emailPattern))
            Toast.makeText(this,"invalid email",Toast.LENGTH_SHORT).show();

        else
        {
            LoadData loadData=new LoadData();
            loadData.execute(s1,s2);
        }

    }





    class LoadData extends AsyncTask {

        String url = "http://192.168.56.1/android_connect/checkUserLogin.php";

        @Override
        protected Object doInBackground(Object[] objects) {
            Log.d("My_log", "in async");
            String email = objects[0].toString();
            String passwoed = objects[1].toString();
            String result = getResult(email, passwoed);
            //Log.d("My_log",""+res11ult);
            return result;
        }

        public String getResult(String mail, String pasword) {

            // Log.v(TAG, "Final Requsting URL is : :"+url);

            String line = "";
            String responseData = null;

            try {
                StringBuilder sb = new StringBuilder();
                String x = "";
                URL httpurl = new URL(url);
                //URLConnection tc= httpurl.openConnection();
                HttpURLConnection connection = (HttpURLConnection) httpurl.openConnection();
                Log
                        .d("My_log","url is"+url);
                connection.setRequestMethod("POST");

                Uri.Builder builder = new Uri.Builder()
//                        .appendQueryParameter("user_name", name)
//                        .appendQueryParameter("user_place", place)
                        .appendQueryParameter("user_email", mail)
                        .appendQueryParameter("user_password", pasword);


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
                Log.d("My_log", "after connect");
                if (connection.getResponseCode()==HttpURLConnection.HTTP_OK){
                    Log.d("My_log", "after connect");

            /*URLConnection tc = twitter.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) tc;
            httpConnection.setRequestMethod("POST");*/
                    //tc.setRe
                    //tc.setDoOutput(false);

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));

                    while ((line = in.readLine()) != null) {
                        sb.append(line + "\n");
                        x = sb.toString();
                        Log.d("My_log", "incoming" + x);
                    }
                    responseData = new String(x);}
                else{
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

        @Override
        protected void onPostExecute(Object o) {
            int i=1;
            if(o!=null) {
                if (  Character.isDigit(o.toString().charAt(0))) {
                    try {
                        i = Integer.parseInt(o.toString().trim());
                    } catch (NumberFormatException e) {
                        Log.d("My_log", "+" + e);
                    }
                }
                if ( (o.toString() == null || i == 0)) {
                    Log.d("My_log", "fail");
                    Toast.makeText(getApplicationContext(), "incorrect password or email", Toast.LENGTH_SHORT).show();
                } else {
                    settings.edit().putBoolean("my_login", true).apply();
                    int id = getIdFromJson(o.toString());
                    settings.edit().putInt("user_id", id).apply();

                    Intent intent = new Intent(getApplicationContext(), Main2Activity.class);

//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    //finish();
                    //intent.putExtra("user_id",s);
                    //startActivity(intent);
                }
            }
        }
    }
}

