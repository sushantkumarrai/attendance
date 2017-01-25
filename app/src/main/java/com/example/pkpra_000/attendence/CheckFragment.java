package com.example.pkpra_000.attendence;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CheckFragment extends Fragment {

private static EditText editText,editText1;
    private static String DB_NAME="Attendence";
    private static String TABLE="SUBJECT";
    private static String TABLE1="ATTENDENCE";
   private static Spinner spinner;
   private static EditText fromDate;
    private static EditText toDate;
    private static  TextView resultView;

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

            fromDate.setText(date);
            fromDate.setError(null);

        }
    }
    public static  class  SelectDate1Fragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {


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


            toDate.setText(date);
            toDate.setError(null);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view=inflater.inflate(R.layout.fragment_check, container, false);

          spinner=(Spinner)view.findViewById(R.id.spcheck) ;
          fromDate=(EditText)view.findViewById(R.id.fromdate);
          toDate=(EditText)view.findViewById(R.id.todate);
           resultView=(TextView)view.findViewById(R.id.result);

        Button button = (Button) view.findViewById(R.id.view);
        ArrayList<String> my_array = new ArrayList<String>();
        my_array = getTableValues();
        ArrayAdapter my_Adapter = new ArrayAdapter(getContext(), R.layout.spinner_row,
                my_array);
        spinner.setAdapter(my_Adapter);


        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment pickerFragment = new SelectDateFragment();
                pickerFragment.show(getFragmentManager(),"DatePicker");

            }
        });

        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment pickerFragment = new SelectDate1Fragment();
                pickerFragment.show(getFragmentManager(),"DatePicker");

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int i=0,p=0,t=0,atr=1;
                    resultView.setText(null);
                    String subject = String.valueOf(spinner.getSelectedItem());
                    String fD=fromDate.getText().toString();
                    if(TextUtils.isEmpty(fD)) {
                        fromDate.setError("PLEASE ENTER INITIAL DATE");
                        return;
                    }
                    String tD=toDate.getText().toString();
                    if(TextUtils.isEmpty(tD)){
                        toDate.setError("please enter last date");
                        return;
                    }
                    if (fD.compareTo(tD) > 0){
                        Toast toast=Toast.makeText(getContext(),"From date must old then To date",Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    }

                    SQLiteOpenHelper databseHelper = new DatabaseHandler(getContext());
                    SQLiteDatabase db = databseHelper.getReadableDatabase();
                    Cursor cursor = db.query("SUBJECT", new String[]{"_id"}, "SUBJECT_NAME=?", new String[]{subject}, null, null, null);
                    if (cursor.moveToFirst()){
                        do{
                            i = cursor.getInt(0);

                        }while(cursor.moveToNext());
                    }
                    cursor.close();
                    String where="SUB_ID=? AND PRESENT=? AND DATE>=? AND DATE<=?";
                    Cursor present=db.query(TABLE1,new String[] {"COUNT(SUB_ID)"},where,new String[] {Integer.toString(i),Integer.toString(atr),fD,tD},null,null,null);
                   // Cursor present=db.rawQuery("SELECT COUNT(SUB_ID) FROM ATTENDENCE WHERE (PRESENT=1 AND SUB_ID='i')AND(DATE >='fD' AND DATE<='tD')",null);
                    if(present.moveToFirst()){
                        do{
                            p=present.getInt(0);
                        }while(present.moveToNext());
                    }
                    present.close();
                    String where1="SUB_ID=? AND DATE>=? AND DATE<=?";
                    Cursor total=db.query(TABLE1,new String[] {"COUNT(SUB_ID)"},where1,new String[] {Integer.toString(i),fD,tD},null,null,null,null);
                   // Cursor total=db.rawQuery("SELECT COUNT(TOTAL) FROM ATTENDENCE WHERE (SUB_ID='i')AND (DATE>='fD' AND DATE<='tD')",null);
                    if(total.moveToFirst()){
                        do{
                            t=total.getInt(0);
                        }while(total.moveToNext());
                    }
                    total.close();
                    db.close();
                    int totalClass=t;
                    int totalPresent=p;
                    int totalAbsent=t-p;
                    float attenPresent=0.0f;
                    try {
                         attenPresent = (((float)p * 100)/(float)t);
                    }catch (ArithmeticException e){
                        Toast toast=Toast.makeText(getContext(),"no attendence",Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    resultView.setText("Total no of classes:"+totalClass+"\nTotal present days:"+totalPresent+"\nTotal Absent:"+totalAbsent+"\nAttendance (in%):"+attenPresent+"%" );


                }catch(SQLiteException e){

                }
            }
        });
        return view;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public ArrayList<String> getTableValues() {

        ArrayList<String> array = new ArrayList<String>();
        try {
            SQLiteOpenHelper sqLiteOpenHelper=new DatabaseHandler(getContext());
            SQLiteDatabase mydb =sqLiteOpenHelper.getReadableDatabase();
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
