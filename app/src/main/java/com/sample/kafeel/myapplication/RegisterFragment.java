package com.sample.kafeel.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

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

public class RegisterFragment extends Fragment implements View.OnClickListener {
    Button button;
    SharedPreferences settings;
    EditText editText1, editText2, getEditText3, getEditText4;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String email;
    LinearLayout linearLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_fragment, container, false);
        editText1 = view.findViewById(R.id.user_name);
        editText2 = view.findViewById(R.id.user_place);
        getEditText3 = view.findViewById(R.id.user_email);
        getEditText4 = view.findViewById(R.id.user_password);
        button = view.findViewById(R.id.RegiterButton);
        linearLayout=view.findViewById(R.id.linearlayout);

        button.setOnClickListener(this);
        settings = getContext().getSharedPreferences("MyPrefsFile", 0);

        return view;
    }

    public void getResponse(String res) {
    }

    @Override
    public void onClick(View view) {
        createUser();

    }

    private void createUser() {
        String username = editText1.getText().toString();
        String userplace = editText2.getText().toString();
        String usermail = getEditText3.getText().toString();
        String password = getEditText4.getText().toString();
        if ((username.equals("") || userplace.equals("") || usermail.equals("") || password.equals(""))) {
            Toast.makeText(getContext(), "Fields are Empty", Toast.LENGTH_SHORT).show();
        } else if (!usermail.trim().matches(emailPattern))
            Toast.makeText(getContext(), "invalid email", Toast.LENGTH_SHORT).show();
        else {
            LoadData data = new LoadData();
            data.execute(username, userplace, usermail, password);

        }
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.d("My_log","frag onpause");
        getView().setVisibility(View.GONE);
        RelativeLayout lay=getActivity().findViewById(R.id.layout);
        lay.setVisibility(View.VISIBLE);
    }

    public void onBackPressed() {

        if (getFragmentManager().getBackStackEntryCount() == 0) {

        } else {
            Log.d("My_log","frag back poress");
            getFragmentManager().popBackStack();
            LinearLayout lay=getActivity().findViewById(R.id.login_root_view);
            lay.setVisibility(View.VISIBLE);
        }
    }


    class LoadData extends AsyncTask {

        String url = "http://192.168.56.1/android_connect/insertUserDetail.php";

        @Override
        protected Object doInBackground(Object[] objects) {
            Log.d("My_log", "in async");

            String name = objects[0].toString();
            String place = objects[1].toString();
            String mail = objects[2].toString();
            String pass = objects[3].toString();
            String result = getResult(name, place, mail, pass);
            Log.d("My_log", "in backgrund" + result);
            return result;
        }

        public String getResult(String name, String place, String mail, String pasword) {

            // Log.v(TAG, "Final Requsting URL is : :"+url);

            String line = "";
            String responseData = null;

            try {
                StringBuilder sb = new StringBuilder();
                String x = "";
                URL httpurl = new URL(url);
                //URLConnection tc= httpurl.openConnection();
                HttpURLConnection connection = (HttpURLConnection) httpurl.openConnection();
                connection.setRequestMethod("POST");

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("user_name", name)
                        .appendQueryParameter("user_place", place)
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

                if (connection.getResponseCode() != HttpURLConnection.HTTP_CONFLICT) {
                    Log.d("My_log", "after connect");
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));

                    while ((line = in.readLine()) != null) {
                        sb.append(line + "\n");
                        x = sb.toString();
                        //Log.d("My_log", "" + x);
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

        @Override
        protected void onPostExecute(Object o) {

            if (o != null) {
                String s = o.toString();

                int i = Integer.parseInt(s.trim());
                if (s != null && i != 0) {
                    settings.edit().putBoolean("my_login", true).commit();
                    settings.edit().putInt("user_id", i).commit();

                    Toast.makeText(getContext(), "" + s, Toast.LENGTH_SHORT).show();
                    Log.d("My_log", "s" + s + s.equals(0));
                    Intent intent = new Intent(getContext(), Main2Activity.class);
                   // intent.putExtra("user_id", s);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(intent);
                    //finish();
                    //startActivity(intent);
                } else {
                    Log.d("My_log", "failes");
                    Toast.makeText(getContext(), "failes email exists", Toast.LENGTH_SHORT).show();
                }

            } else
                Log.d("My_log", "nul object");
        }
    }
}
