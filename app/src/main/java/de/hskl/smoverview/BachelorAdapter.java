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

import de.hskl.smoverview.databaseClasses.BachelorDTO;

public class BachelorAdapter extends ArrayAdapter<BachelorDTO>
{
    Context context;
    int resource;

    public BachelorAdapter(@NonNull Context context, int resource, @NonNull List<BachelorDTO> objects)
    {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource,parent,false);
        TextView FBName = convertView.findViewById(R.id.FACHBEREICHBACHELOR_TEXTVIEW);
        BachelorDTO CurrentBachelor = (BachelorDTO) getItem(position);
        FBName.setText(CurrentBachelor.getFachbereich());
        return convertView;
    }

    @Override
    public void clear() {
        super.clear();
    }
}
