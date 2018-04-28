package com.example.niraanam.photosservermysql;

import android.content.Context;
import java.util.List;
import android.app.Activity;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Table_ListAdapterClass extends BaseAdapter{

    Context context;
    List<Table_Photo> valueList;

    public Table_ListAdapterClass(List<Table_Photo> listValue, Context context)
    {
        this.context = context;
        this.valueList = listValue;
    }

    @Override
    public int getCount()
    {
        return this.valueList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return this.valueList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewItem viewItem = null;

        if(convertView == null)
        {
            viewItem = new ViewItem();

            LayoutInflater layoutInfiater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInfiater.inflate(R.layout.table_showitems, null);

            viewItem.TextViewModified = (TextView)convertView.findViewById(R.id.txtModified);

            viewItem.TextViewImgName = (TextView)convertView.findViewById(R.id.txtImgName);

            convertView.setTag(viewItem);
        }
        else
        {
            viewItem = (ViewItem) convertView.getTag();
        }

        viewItem.TextViewModified.setText(valueList.get(position).last_modified);

        viewItem.TextViewImgName.setText(valueList.get(position).image_name);



        return convertView;
    }
}

class ViewItem
{
    TextView TextViewModified;

    TextView TextViewImgName;

}