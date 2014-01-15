package ru.ps.habrtimezone;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import ru.ps.libs.info._Info;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ADescription extends Activity implements OnItemClickListener, OnClickListener {
	private _Info city;
	private ArrayList<Strings> strs_al;
	private ArrayAdapter<Strings> arrayAdapter;
	private ListView outList;
	private Button btnbck;
	private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
	
	private String[] ew_ar = new String[2];
	private String[] ns_ar = new String[2];
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
	ew_ar = getResources().getStringArray(R.array.ew_ar);
	ns_ar = getResources().getStringArray(R.array.ns_ar);
		
	sf.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
	super.onCreate(savedInstanceState);
	Intent intent = getIntent();
	setContentView(R.layout.adescription);
	outList = (ListView) findViewById(R.id.outList);
	btnbck = (Button) findViewById(R.id.btnbck);
	btnbck.setOnClickListener(this);
	if (intent.hasExtra(_.FULLDATA))
	{
		city = intent.getParcelableExtra(_.FULLDATA);
	}
	else
	{
		city = new _Info();
	}
	strs_al = new ArrayList<Strings>();
	for (int i = 0; i < city.size(); i++)
	{
		Strings strs = new Strings(_.names[i],(i == 4 || i == 5)?_.todeg(city.getDouble(i), 5 - i, ew_ar, ns_ar):((i == 18)?sf.format(new Date(city.getLong(i))):city.getString(i)));
		strs_al.add(strs);
	}
	
	arrayAdapter =      
	         new DescriptionAdapter(this,android.R.layout.simple_list_item_1, strs_al);
	         outList.setAdapter(arrayAdapter); 
	         outList.setOnItemClickListener(this);
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
	public class DescriptionAdapter extends ArrayAdapter<Strings> {

	    private ArrayList<Strings> items;
	    private Context context;

	    public DescriptionAdapter(Context context, int textViewResourceId, ArrayList<Strings> items) {
	        super(context, textViewResourceId, items);
	        this.context = context;
	        this.items = items;
	    }

	    public View getView(int position, View convertView, ViewGroup parent) {
	        View view = convertView;
	        if (view == null) {
	            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            view = inflater.inflate(R.layout.litem, null);
	        }
	        LinearLayout itemroot = (LinearLayout) view.findViewById(R.id.itemroot);
	        itemroot.setBackgroundColor((position%2==0)?(0x99333333):(0x99555555));
	        Strings item = items.get(position);
	        if (item!= null) {
	            // My layout has only one TextView
	        	TextView itemView = (TextView) view.findViewById(R.id.item);
	            if (itemView != null) {
	                // do whatever you want with your string and long
	                itemView.setText(String.format("%s", item.getStr1()));
	            }
	            TextView itemSUB = (TextView) view.findViewById(R.id.txt);
	            if (itemSUB != null) {
	                // do whatever you want with your string and long
	            	itemSUB.setText(String.format("%s", item.getStr2()));
	            }
	         }

	        return view;
	    }
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.btnbck:
			finish();
			break;
		default:
			break;
		}
	}
}
