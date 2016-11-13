package chupalika.pleasepickaplace;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ipb on 11/13/2016.
 */
public class GroupAdapter extends BaseAdapter {
    private ArrayList<Group> groups;
    private LayoutInflater inflater;

    public GroupAdapter(Context context, ArrayList<Group> g) {
        this.groups = g;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {return groups.size();}
    @Override
    public Object getItem(int pos) {return groups.get(pos);}
    @Override
    public long getItemId(int pos) {return pos;}

    @Override
    public View getView(int pos, View convertView, ViewGroup arg) {
        LinearLayout itemView;
        if (convertView == null) {
            itemView = (LinearLayout)inflater.inflate(R.layout.group_item, null);
        }
        else {
            itemView = (LinearLayout)convertView;
        }

        TextView groupname = (TextView)itemView.findViewById(R.id.a_group_item);
        groupname.setText(groups.get(pos).toString());
        return itemView;
    }
}
