package de.hskl.smoverview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class BachelorIntent extends AppCompatActivity implements View.OnClickListener{

    TextView ausgabe;
    FloatingActionButton addButton;
    ListView addListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bachelor_bachelor);

        ausgabe = findViewById(R.id.TEXT_VIEW_BACHELOR);
        addButton = findViewById(R.id.ADD_BUTTEN);

        addListView = (ListView)findViewById(R.id.ADD_LIST_VIEW);

        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add("Hallo");
        arrayList.add("Hallo");
        arrayList.add("Hallo");
        arrayList.add("Hallo");
        arrayList.add("Hallo");
        arrayList.add("Hallo");
        arrayList.add("Hallo");
        arrayList.add("Hallo");
        arrayList.add("Hallo");
        arrayList.add("Hallo");
        arrayList.add("Hallo");
        arrayList.add("Hallo");
        arrayList.add("Hallo");
        arrayList.add("Hallo");
        arrayList.add("Hallo");
        arrayList.add("Hallo");
        arrayList.add("Hallo");
        arrayList.add("Hallo");



        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,arrayList);

        addListView.setAdapter(arrayAdapter);
    }

    @Override
    public void onClick(View view) {


        if(view.getId() == addButton.getId())
        {
            Intent intentOfAddFach = new Intent(this,Bachelor_Add_Fach.class);
            startActivity(intentOfAddFach);
        }
    }
}