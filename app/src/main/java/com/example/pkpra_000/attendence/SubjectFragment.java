package com.example.pkpra_000.attendence;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.pkpra_000.attendence.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubjectFragment extends Fragment {

    EditText editText;
    DatabaseHandler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_subject, container, false);
        Button button=(Button)view.findViewById(R.id.button);
        editText=(EditText)view.findViewById(R.id.subject);
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                String string=editText.getText().toString();
                try{
                    SQLiteOpenHelper databseHelper=new DatabaseHandler(getContext());
                    SQLiteDatabase db=databseHelper.getWritableDatabase();
                    ContentValues contentValues=new ContentValues();
                    contentValues.put("SUBJECT_NAME",string);
                    db.insert("SUBJECT",null,contentValues);
                    Toast toast=Toast.makeText(getContext(),"Entry is done",Toast.LENGTH_SHORT);
                    toast.show();
                }catch(SQLiteException e) {
                    Toast toast =Toast.makeText(getContext(),"Database unavailable",Toast.LENGTH_SHORT);
                    toast.show();
            }
        }
    });

return view;
  /*  @RequiresApi(api = Build.VERSION_CODES.M)
    public void onButtonClicked(View view){


      }*/




}}
