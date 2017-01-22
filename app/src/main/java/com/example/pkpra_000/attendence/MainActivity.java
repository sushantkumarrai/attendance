package com.example.pkpra_000.attendence;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View;
import android.widget.AdapterView;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private String[] titles;
    private ListView drawerList;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private int currentPosition=0;


    private class DrawerItemClickListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent,View view,int position,long id){
            selectItem(position);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        titles=getResources().getStringArray(R.array.titles);
        drawerList=(ListView)findViewById(R.id.drawer);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        drawerList.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_activated_1,titles));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        if(savedInstanceState !=null){
            currentPosition=savedInstanceState.getInt("position");
            setActionBarTitle(currentPosition);
        }else{
            selectItem(0);
        }
        drawerToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open_drawer,R.string.close_drawer){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
           drawerLayout.setDrawerListener(drawerToggle);
           getSupportActionBar().setDisplayHomeAsUpEnabled(true);
           getSupportActionBar().setHomeButtonEnabled(true);

           getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener(){
               public void onBackStackChanged(){
                      FragmentManager fragmen=getFragmentManager();
                        Fragment fragment=fragmen.findFragmentByTag("visible_fragment");
                      if(fragment instanceof TopFragment){
                          currentPosition=0;
                      }
                      if(fragment instanceof UpdateFragment){
                          currentPosition=1;
                      }
                     if(fragment instanceof CheckFragment){
                         currentPosition=2;
                     }
                     if(fragment instanceof SubjectFragment){
                         currentPosition=3;
                     }
                     if(fragment instanceof RemoveFragment){
                         currentPosition=4;
                     }
                   setActionBarTitle(currentPosition);
                    drawerList.setItemChecked(currentPosition,true);
               }
           }
           );

    }
        private void selectItem(int position){
            currentPosition=position;
            Fragment fragment;
            switch (position){
                case 1:
                    fragment=new UpdateFragment();
                    break;
                case 2:
                    fragment=new CheckFragment();
                    break;
                case 3:
                    fragment=new SubjectFragment();
                    break;
                case 4:
                    fragment=new RemoveFragment();
                    break;
                default:
                    fragment=new TopFragment();
            }

            FragmentTransaction ft=getFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame,fragment,"visible_fragment");
            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
            setActionBarTitle(position);
            drawerLayout.closeDrawer(drawerList);
        }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position",currentPosition);
    }
  private void setActionBarTitle(int position){
        String title;
        if(position==0){
            title=getResources().getString(R.string.app_name);
        }else{
            title=titles[position];
        }
        getSupportActionBar().setTitle(title);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
