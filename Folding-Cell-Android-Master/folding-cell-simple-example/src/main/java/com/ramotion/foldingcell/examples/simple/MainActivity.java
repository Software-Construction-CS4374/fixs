package com.ramotion.foldingcell.examples.simple;
/** 
 * NEF: fixed AtLeastOneConstructor, LongVariable, MethodArgumentCouldBeFinal, ShortVariable
 */
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ramotion.foldingcell.FoldingCell;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
	MainActivity(){//fixed AtLeastOneConstructor by adding constructor
		
	}

    @Override
    protected void onCreate(final Bundle instanceState) {
        super.onCreate(instanceState);
        setContentView(R.layout.activity_main);

        // get our folding cell
        final FoldingCell foldCell = (FoldingCell) findViewById(R.id.folding_cell);

        // attach click listener to fold btn
        final Button toggleBtn = (Button) findViewById(R.id.toggle_btn);
        toggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                foldCell.toggle(false);
            }
        });
        //NEF longvariable fixed and methodargumentcouldbefinal
        // attach click listener to toast btn
        final Button instantlyBtn = (Button) findViewById(R.id.toggle_instant_btn);
        instantlyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                foldCell.toggle(true);
            }
        });

    }
}
