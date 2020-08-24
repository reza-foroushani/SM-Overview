package de.hskl.smoverview;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class MasterAdapter extends ArrayAdapter<Master> {
  /*  LayoutInflater layoutInflater;
    int itemLayout;
    String []from ;
    int[]to;

    public MasterAdapter(Context context,int itemLayout ,Cursor c, String[] from, int[] to,int flags) {
        super(context, c, flags);
        layoutInflater= LayoutInflater.from(context);
        this.itemLayout= itemLayout;
        this.from=from;
        this.to= to;
    }



    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View v = layoutInflater.inflate(itemLayout,viewGroup,false);
        return v;
    }
// Stellt für jedes Listenelementent die Verbindung zwischen den view und der datenquelle
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String  text1 = context.getString(cursor.getColumnIndexOrThrow(from[1]));
        TextView textView1 = (TextView) view.findViewById(to[1]);
        textView1.setText(text1);
        String  text2 = context.getString(cursor.getColumnIndexOrThrow(from[2]));
        TextView textView2 = (TextView) view.findViewById(to[2]);
        textView1.setText(text2);
    }*/

    Context context ;
    int resource ;
     public MasterAdapter(@NonNull Context context, int resource, @NonNull List<Master> objects) {
        super(context, resource, objects);
        //weil wir das context global  geben
        this.context=context;
        this.resource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
         // er braucht context,R.layout.activity_master = resource
        convertView = LayoutInflater.from(context).inflate(resource,parent,false);
        TextView FBName = convertView.findViewById(R.id.FACHBEREICHMASTR_NAME2);
        TextView Beschreichbung = convertView.findViewById(R.id.BESCHREIBUNG_MASTER2);
        Master  CurrentMaster = getItem(position);
        FBName.setText(CurrentMaster.getFachbereichName());
        Beschreichbung.setText(CurrentMaster.getBeschreichbung());
        return convertView;
    }
}
