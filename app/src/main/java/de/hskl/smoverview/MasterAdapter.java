package de.hskl.smoverview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import de.hskl.smoverview.databaseClasses.MasterDTO;

public class MasterAdapter extends ArrayAdapter<MasterDTO> {


    Context context ;
    int resource ;
     public MasterAdapter(@NonNull Context context, int resource, @NonNull List<MasterDTO> objects) {
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
        MasterDTO CurrentMaster = getItem(position);
        FBName.setText(CurrentMaster.getFachbereichName());
        Beschreichbung.setText(CurrentMaster.getBeschreichbung());
        return convertView;
    }

    @Override
    public void clear() {
        super.clear();
    }
}
