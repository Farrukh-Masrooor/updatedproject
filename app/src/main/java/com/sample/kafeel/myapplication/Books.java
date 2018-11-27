package com.sample.kafeel.myapplication;

import android.util.Log;

public class Books {

    String book_name,book_description,book_image,user_name,user_place,user_email;
    Books(String book_name,String book_description,String book_image,String user_name)
    {
        this.book_name=book_name;
        this.book_description=book_description;
        this.book_image=book_image;
        this.user_name=user_name;
        Log.d("My_log","constructor called"+this.book_name);

    }

    Books(String book_name,String book_description,String book_image,String user_name,String user_place,String user_email)
    {
        this.book_name=book_name;
        this.book_description=book_description;
        this.book_image=book_image;
        this.user_name=user_name;
        this.user_name=user_name;
        this.user_place=user_place;
        this.user_email=user_email;
        Log.d("My_log","constructor called"+this.book_name);

    }
}
