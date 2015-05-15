package citrusdev.com.volumebuttonsdriver;

import static citrusdev.com.volumebuttonsdriver.Constant.ITEM;
import static citrusdev.com.volumebuttonsdriver.Constant.DESCR;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Администратор on 13.05.2015.
 */
public class ListViewAdapter extends BaseAdapter {
    public ArrayList<HashMap> list;
    private Activity activity;

    public ListViewAdapter(Activity activity, ArrayList<HashMap> list) {
        super();
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        TextView tvItem;
        TextView tvDescr;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        if(convertView == null){
            convertView = inflater.inflate(R.layout.main_listview_item, null);
            holder = new ViewHolder();
            holder.tvItem = (TextView) convertView.findViewById(R.id.textViewRowItem);
            holder.tvDescr = (TextView) convertView.findViewById(R.id.textViewDescr);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        HashMap map = list.get(position);
        holder.tvItem.setText((String) map.get(ITEM));
        holder.tvDescr.setText((String) map.get(DESCR));

        return convertView;
    }
}
