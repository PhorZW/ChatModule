package com.moc.chatmodule;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Fragments.ChatsFragment;
import Fragments.ReadersFragment;
import Model.Reader;
import de.hdodenhof.circleimageview.CircleImageView;

public class StartActivity extends AppCompatActivity {
    CircleImageView profile_image;
    TextView username;
    private String[] titles = new String[]{"Chats", "Readers"};


    FirebaseUser firebaseUser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Toolbar toolBar = findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("");
        //getActionBar().setTitle("");


        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Meter Readers").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Reader reader = snapshot.getValue(Reader.class);
                username.setText(reader.getUsername());

                if (reader.getImageURL().equals("default")) {

                    profile_image.setImageResource(R.mipmap.ic_launcher);
                }
                else {

                    Glide.with(StartActivity.this).load(reader.getImageURL()).into(profile_image);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager2 viewPager = findViewById(R.id.view_pager);


        FragmentStateAdapter pagerAdapter = pagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);


        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(titles[position])
        ).attach();




        //ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        //pagerAdapter.addFragment(new ChatsFragment(), "Chats");
        //pagerAdapter.addFragment(new ReadersFragment(), "Contacts");





        //tabLayout.setupWithViewPager(viewPager);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(StartActivity.this, MainActivity.class));
                finish();
                return true;
        }

        return false;
    }

    private class ScreenSlidePagerAdapter extends FragmentStateAdapter{

       // private ArrayList<Fragment> fragments;
        //private ArrayList<String> titles;
        // String[] titles = new String[]{"Chats", "Readers"};

        public ScreenSlidePagerAdapter(FragmentActivity fa){
            super(fa);
            //this.fragments= new ArrayList<>();
            //this.titles= new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {

                case 0:
                    return new ChatsFragment();
                case 1:
                    return new ReadersFragment();
            }

            return new ChatsFragment();

        }

        @Override
        public int getItemCount() {
            return titles.length;
        }
    }
    /*class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;



        ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
            this.fragments= new ArrayList<>();
            this.titles= new ArrayList<>();
        }



        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
             titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }*/

}