package com.ramotion.foldingcell.examples.listview;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
//
import com.ramotion.foldingcell.FoldingCell;
import com.ramotion.foldingcell.examples.R;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Example of using Folding Cell with ListView and ListAdapter
 */
public class MainActivity extends AppCompatActivity {
	MainActivity(){ //NEF AtLeastOneConstructor fix
	}

    @Override
  //NEF MethodArgumentCouldBeFinalFix and longvariable
    protected void onCreate(final Bundle savedInstState) {
        super.onCreate(savedInstState);
        setContentView(R.layout.activity_main);

        // get our list view
        ListView theListView = findViewById(R.id.mainListView);

        // prepare elements to display
        final ArrayList<Item> items = Item.getTestingList();

        // add custom btn handler to first list item
        items.get(0).setRequestBtnClickListener(new View.OnClickListener() {
            @Override
          //NEF MethodArgumentCouldBeFinal Fix and shortvariable fix
            public void onClick(final View view) {
                Toast.makeText(getApplicationContext(), "CUSTOM HANDLER FOR FIRST BUTTON", Toast.LENGTH_SHORT).show();
            }
        });

        // create custom adapter that holds elements and their state (we need hold a id's of unfolded elements for reusable elements)
        final FoldingCellListAdapter adapter = new FoldingCellListAdapter(this, items);

        // add default btn handler for each request btn on each item if custom handler not found
        adapter.setDefaultRequestBtnClickListener(new View.OnClickListener() {
            @Override
          //NEF MethodArgumentCouldBeFinalFix and shortvariable fix
            public void onClick(final View view) {
                Toast.makeText(getApplicationContext(), "DEFAULT HANDLER FOR ALL BUTTONS", Toast.LENGTH_SHORT).show();
            }
        });

        // set elements to adapter
        theListView.setAdapter(adapter);

        // set on click event listener to list view
        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //NEF MethodArgumentCouldBeFinalFix and shortvariable 
            public void onItemClick(final AdapterView<?> adapterView, final View view,final int position,final long value) {
                // toggle clicked cell state
                ((FoldingCell) view).toggle(false);
                // register in adapter that state for selected cell is toggled
                adapter.registerToggle(pos);
            }
        });

    }
}
