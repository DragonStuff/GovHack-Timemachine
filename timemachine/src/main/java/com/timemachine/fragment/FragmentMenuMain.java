package com.timemachine.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.timemachine.R;
import com.timemachine.TimeMachineMain;
import com.timemachine.adapter.MenuListAdapter;
import com.timemachine.helper.Logger;

/**
 * Created by Jeri on 30/06/2014.
 */
public class FragmentMenuMain extends FragmentTabBase {
    private final String TAG = "FragmentMenuHome";
    ImageView imgProfilePic;
    TextView lblDisplayname;
    TimeMachineMain main;
    ProgressBar progressBar;
    ListView lvMenu;
    String[] title;
    String[] subtitle;
    int[] icon;
    MenuListAdapter mMenuAdapter;
    ToggleButton btn1;
    ToggleButton btn2;
    ToggleButton btn3;
    ToggleButton btn4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        main = ((TimeMachineMain) getActivity());

        View view = inflater.inflate(R.layout.fragment_menu_home, container, false);
        btn1 = (ToggleButton)view.findViewById(R.id.btn1);

        btn1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                main.toggleEventType(0, b);
            }
        });

        btn2 = (ToggleButton)view.findViewById(R.id.btn2);

        btn2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                main.toggleEventType(1, b);
            }
        });

        btn3 = (ToggleButton)view.findViewById(R.id.btn3);

        btn3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                main.toggleEventType(2, b);
            }
        });

        btn4 = (ToggleButton)view.findViewById(R.id.btn4);

        btn4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                main.toggleEventType(3, b);
            }
        });

        //lvMenu = (ListView) view.findViewById(R.id.lvMenu);

        //buildMenuList();

        return view;
    }
    private void buildMenuList(){
        // Generate title
        title = new String[] {
                "Home",
                "Categories",
        };

        // Generate subtitle
        subtitle = new String[] {
                "View relevant information in your selected timeline",
                "Toggle various categories you want to view on the map",
        };

        // Generate icon
        icon = new int[] {
                R.drawable.ic_launcher,
                R.drawable.ic_launcher,
        };

        // Pass string arrays to MenuListAdapter
        mMenuAdapter = new MenuListAdapter(main, title, subtitle,
                icon);

        // Set the MenuListAdapter to the ListView
        lvMenu.setAdapter(mMenuAdapter);

        // Capture listview menu item click
        lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //mMenuAdapter.selectedItem = title[position];
                Logger.d(TAG, "Selected Item : " + mMenuAdapter.selectedItem);
                //mMenuAdapter.notifyDataSetInvalidated();

                //for (int j = 0; j < adapterView.getChildCount(); j++)
                //    adapterView.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);

                // change the background color of the selected element
                //view.setBackgroundColor(getResources().getColor(R.color.ls_green_faded));


                //main.sideMenuClicked(position);
            }
        });
    }
}
