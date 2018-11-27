package com.sample.kafeel.myapplication;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class jsonviewer {

    String BookNmae="";

    ArrayList<Books> booksArrayList;
    jsonviewer()
    {
        Log.d("My_log","constructor of json viewer=============");
        booksArrayList=new ArrayList<>();
        BookNmae="";
    }
    public ArrayList<Books> jsonViewer(String string, Context context)
    {
        //booksArrayList=new ArrayList<>();
        try {
            JSONArray jsonArray=new JSONArray(string);
            int length=jsonArray.length();
            for(int i=0;i<length;i++)
            {

                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String name=jsonObject.getString("book_name");
                BookNmae=name;
                String des=jsonObject.getString("book_description");
                String image=jsonObject.getString("book_pic");
                String username=jsonObject.getString("user_name");
                booksArrayList.add(new Books(name,des,image,username));
                //MyBaseAdapter adapter=new MyBaseAdapter(context,name,des);
            }
            Log.d("My_log","array"+length);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("My_log","error"+e);
        }
        return booksArrayList;
    }

    public ArrayList<Books> jsonViewer2(String string, Context context)
    {
        booksArrayList=new ArrayList<>();
        try {
            JSONArray jsonArray=new JSONArray(string);
            int length=jsonArray.length();
            for(int i=0;i<length;i++)
            {

                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String name=jsonObject.getString("book_name");
                String des=jsonObject.getString("book_description");
                String image=jsonObject.getString("book_pic");
                String username=jsonObject.getString("user_name");
                String userplace=jsonObject.getString("user_place");
                String useremail=jsonObject.getString("user_email");
                Log.d("My_log","username="+username+useremail);
                booksArrayList.add(new Books(name,des,image,username,userplace,useremail));
                //MyBaseAdapter adapter=new MyBaseAdapter(context,name,des);
            }
            Log.d("My_log","array"+length);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return booksArrayList;
    }

    public String getBookname(int i)
    {
        if (booksArrayList!=null)
        { Log.d("My_log",""+booksArrayList.get(i).book_name);
            return booksArrayList.get(i).book_name;}
        else
        {
            Log.d("My_log","empty array list");
            return "";}
    }

    public String getUserName(int i) {
        return booksArrayList.get(i).user_name;
    }
}
