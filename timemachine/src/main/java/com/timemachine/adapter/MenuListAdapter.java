package com.timemachine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.timemachine.R;


/**
 * Created by Jeri on 1/07/2014.
 */
public class MenuListAdapter extends BaseAdapter {
    // Declare Variables
    Context context;
    String[] mTitle;
    String[] mSubTitle;
    int[] mIcon;
    LayoutInflater inflater;
    public String selectedItem = "";

    public MenuListAdapter(Context context, String[] title, String[] subtitle,
                           int[] icon) {
        this.context = context;
        this.mTitle = title;
        this.mSubTitle = subtitle;
        this.mIcon = icon;
    }

    @Override
    public int getCount() {
        return mTitle.length;
    }

    @Override
    public Object getItem(int position) {
        return mTitle[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Declare Variables
        ViewItem viewItem;

        if (convertView == null) {
            viewItem = new ViewItem();
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //View itemView = inflater.inflate(R.layout.menu_list_item, parent, false);
            convertView = inflater.inflate(R.layout.menu_list_item, null);

            // Locate the TextViews in drawer_list_item.xml
            viewItem.txtTitle = (TextView) convertView.findViewById(R.id.title);
            viewItem.txtSubTitle = (TextView) convertView.findViewById(R.id.subtitle);

            // Locate the ImageView in drawer_list_item.xml
            viewItem.imgIcon = (ImageView) convertView.findViewById(R.id.icon);

        }
        else{
            viewItem = (ViewItem) convertView.getTag();
        }



        // Set the results into TextViews
        viewItem.txtTitle.setText(mTitle[position]);
        viewItem.txtSubTitle.setText(mSubTitle[position]);

        //if(mSubTitle[position].length() == 0)
        //    txtSubTitle.setVisibility(View.GONE);

        if(mTitle[position].equalsIgnoreCase("Log Out"))
        {
            viewItem.imgIcon.setColorFilter(context.getResources().getColor(R.color.ts_lock_active));
        }
        else{
            viewItem.imgIcon.setColorFilter(context.getResources().getColor(R.color.ts_midgray));
        }

        // Set the results into ImageView
        viewItem.imgIcon.setImageResource(mIcon[position]);

        convertView.setTag(viewItem);

        return convertView;
    }

    private class ViewItem {
        TextView txtTitle;
        TextView txtSubTitle;
        ImageView imgIcon;
    }
}
