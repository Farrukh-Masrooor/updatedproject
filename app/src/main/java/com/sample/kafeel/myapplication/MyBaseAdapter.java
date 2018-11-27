package com.sample.kafeel.myapplication;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyBaseAdapter extends BaseAdapter {
    ArrayList<Books> booksArrayList;
    Context context;

    MyBaseAdapter(Context context,ArrayList<Books> booksArrayList)
    {
        //Log.d("My_log","Mybaseadapter cond");
        this.context=context;


        this.booksArrayList= booksArrayList;
    }
    @Override
    public int getCount() {
        //Log.d("My_log","size"+booksArrayList.size());
        return booksArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return booksArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Log.d("My_log","getview called");
        View view=convertView;
        ViewHolder holder=null;
        if (convertView==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.grid_view_layout, parent, false);
            holder=new ViewHolder(view);
            view.setTag(holder);
        }
        else {
            holder= (ViewHolder) view.getTag();
        }
        Books books=booksArrayList.get(position);
        holder.bookname.setText(books.book_name);
        holder.bookdes.setText(books.book_description);
        holder.username.setText(books.user_name);
        byte[] byteimage = Base64.decode(books.book_image, Base64.DEFAULT);
        holder.bookimage.setImageBitmap(BitmapFactory.decodeByteArray(byteimage,0,byteimage.length));
        return view;
    }

    class ViewHolder
    {
        TextView bookname,bookdes,username;
        ImageView bookimage;
        ViewHolder(View view)
        {
            bookname=view.findViewById(R.id.bookName);
            bookdes=view.findViewById(R.id.bookDescription);
            username=view.findViewById(R.id.bookusername);
            bookimage=view.findViewById(R.id.bookImage);
        }
    }
}
