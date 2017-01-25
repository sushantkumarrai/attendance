package com.example.pkpra_000.attendence;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class RemoveFragment extends Fragment {

    SQLiteDatabase mydb;
    private static String DB_NAME="Attendence";
    private static String TABLE="SUBJECT";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_remove, container, false);
       final Spinner spinner=(Spinner)view.findViewById(R.id.rspinner);
        Button button=(Button)view.findViewById(R.id.rbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i=0;
                String rsubject=String.valueOf(spinner.getSelectedItem());
                SQLiteOpenHelper databseHelper = new DatabaseHandler(getContext());
                SQLiteDatabase db = databseHelper.getReadableDatabase();
                Cursor cursor = db.query("SUBJECT", new String[]{"_id"}, "SUBJECT_NAME=?", new String[]{rsubject}, null, null, null);
                if (cursor.moveToFirst()){
                    do{
                        i = cursor.getInt(cursor.getColumnIndex("_id"));
                        // do what ever you want here
                    }while(cursor.moveToNext());
                }
                SQLiteDatabase db1=databseHelper.getWritableDatabase();
                db1.delete("ATTENDENCE","SUB_ID=?",new String[] {Integer.toString(i)});
                db1.delete("SUBJECT","_id=?",new String[] {Integer.toString(i)});
                Toast toast=Toast.makeText(getContext(),"subject is removed="+rsubject,Toast.LENGTH_SHORT);
                toast.show();
                ArrayList<String> my_array1=new ArrayList<String>();
                my_array1 = getTableValues();
                ArrayAdapter my_Adapter = new ArrayAdapter(getContext(), R.layout.spinner_row,
                        my_array1);
                spinner.setAdapter(my_Adapter);
                cursor.close();
                db1.close();

            }
        });
        ArrayList<String> my_array = new ArrayList<String>();
        my_array = getTableValues();
        ArrayAdapter my_Adapter = new ArrayAdapter(getContext(), R.layout.spinner_row,
                my_array);
        spinner.setAdapter(my_Adapter);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public ArrayList<String> getTableValues() {

        ArrayList<String> array = new ArrayList<String>();
        try {
            SQLiteOpenHelper sqLiteOpenHelper=new DatabaseHandler(getContext());
            mydb =sqLiteOpenHelper.getReadableDatabase();
            Cursor allrows = mydb.rawQuery("SELECT SUBJECT_NAME FROM " + TABLE, null);
            System.out.println("COUNT : " + allrows.getCount());

            if (allrows.moveToFirst()) {
                do {

                    String NAME = allrows.getString(0);

                    array.add(NAME);

                } while (allrows.moveToNext());
            }
            allrows.close();
            mydb.close();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error encountered.",
                    Toast.LENGTH_LONG);
        }
        return array;
    }

}
