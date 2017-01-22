package com.example.pkpra_000.attendence;


import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;


@RequiresApi(api = Build.VERSION_CODES.N)
public class UpdateFragment extends Fragment {
    private static EditText editText;
    SQLiteDatabase mydb;
    private static String DB_NAME="Attendence";
    private static String TABLE="SUBJECT";
    String subject;
    String date;
    String attendance;

    public static  class  SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {


        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }


        public void onDateSet(DatePicker view, int yy, int mm, int dd) {

            populateSetDate(yy, mm+1, dd);
        }
        public void populateSetDate(int year, int month, int day) {
            //set the date here
            String date = String.valueOf(year) +":"+String.valueOf(month)
                    +":"+String.valueOf(day);

            //  EditText tf=(EditText)getView().findViewById(R.id.edit);

            editText.setText(date);
        }
    }


    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      final  View view = inflater.inflate(R.layout.fragment_update,container,false);
        Button button = (Button) view.findViewById(R.id.updatebutton);
       final Spinner spinner = (Spinner) view.findViewById(R.id.spinner);



       final Spinner spinner1 = (Spinner)view.findViewById(R.id.spat);

        button.setOnClickListener(new View.OnClickListener(){

                                      @Override
                                      public void onClick(View v)
                                      {


                                          try {

                                              int j;


                                              try {
                                                  int i=0,k = 1;
                                                  subject = String.valueOf(spinner.getSelectedItem());
                                                  editText = (EditText) view.findViewById(R.id.edit);
                                                  date = editText.getText().toString();
                                                  attendance = String.valueOf(spinner1.getSelectedItem());
                                                  System.out.println("subject:"+subject);
                                                  if (attendance.equals("A")) {
                                                      j = 0;
                                                  } else {
                                                      j = 1;
                                                  }

                                                  SQLiteOpenHelper databseHelper = new DatabaseHandler(getContext());
                                                  SQLiteDatabase db = databseHelper.getReadableDatabase();
                                                  Cursor cursor = db.query("SUBJECT", new String[]{"_id"}, "SUBJECT_NAME=?", new String[]{subject}, null, null, null);
                                                  if (cursor.moveToFirst()){
                                                      do{
                                                           i = cursor.getInt(cursor.getColumnIndex("_id"));
                                                          // do what ever you want here
                                                      }while(cursor.moveToNext());
                                                  }
                                                  cursor.close();
                                                  SQLiteDatabase db1=databseHelper.getWritableDatabase();
                                                  ContentValues contentValues = new ContentValues();
                                                  contentValues.put("SUB_ID", i);
                                                  contentValues.put("DATE", date);
                                                  contentValues.put("PRESENT", j);
                                                  contentValues.put("TOTAL", k);
                                                  db1.insert("ATTENDENCE", null, contentValues);
                                                  Toast toast = Toast.makeText(getContext(), "Attendence is updated", Toast.LENGTH_SHORT);
                                                  toast.show();
                                                  //  db.insert("SUBJECT",null,contentValues);
                                              } catch (SQLiteException e) {
                                                  Toast toast = Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT);
                                                  toast.show();
                                              }
                                          }catch(SQLiteException e){
                                              Toast toast = Toast.makeText(getContext(), "Onclick problem", Toast.LENGTH_SHORT);
                                              toast.show();
                                          }
                                      }
                                  });
        ArrayList<String> my_array = new ArrayList<String>();
        my_array = getTableValues();
       ArrayAdapter my_Adapter = new ArrayAdapter(getContext(), R.layout.spinner_row,
                my_array);
       spinner.setAdapter(my_Adapter);


         editText= (EditText)view.findViewById(R.id.edit);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment pickerFragment = new SelectDateFragment();
                pickerFragment.show(getFragmentManager(),"DatePicker");

            }
        });





        return view;
}

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