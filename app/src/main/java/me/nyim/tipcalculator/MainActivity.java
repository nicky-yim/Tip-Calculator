package me.nyim.tipcalculator;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SeekBar;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    private EditText bill_total = null;
    private EditText total = null;
    private SeekBar tip_bar = null;
    private EditText tip_percent = null;
    private EditText tip_amount = null;

    private BigDecimal _bill_total;
    private BigDecimal _tip_percent;
    private BigDecimal _tip_amount;
    private BigDecimal _total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bill_total = (EditText) findViewById(R.id.bill_total);
        bill_total.setText("0.00");

        total = (EditText) findViewById(R.id.total);
        total.setKeyListener(null);
        total.setText("0.00");

        tip_bar = (SeekBar) findViewById(R.id.tip_bar);

        tip_percent = (EditText) findViewById(R.id.tip_percent);
        tip_amount = (EditText) findViewById(R.id.tip_amount);
        tip_amount.setKeyListener(null);
        tip_percent.setKeyListener(null);
        tip_amount.setText("0.00");
        tip_percent.setText(String.valueOf(tip_bar.getProgress()));
        //calc();

        tip_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tip_percent = (EditText) findViewById(R.id.tip_percent);
                tip_percent.setText(String.valueOf(i));
                calc();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // bill_total.addTextChangedListener(new CurrencyTextWatcher());
        //total.addTextChangedListener(new CurrencyTextWatcher());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void calc() {
        bill_total = (EditText) findViewById(R.id.bill_total);
        tip_percent = (EditText) findViewById(R.id.tip_percent);
        tip_amount = (EditText) findViewById(R.id.tip_amount);
        total = (EditText) findViewById(R.id.total);

        _bill_total = new BigDecimal(bill_total.getText().toString());
        _tip_percent = new BigDecimal(tip_percent.getText().toString());
        _tip_amount = new BigDecimal(tip_amount.getText().toString());
        _total = new BigDecimal(total.getText().toString());

        _tip_percent = _tip_percent.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
        _tip_amount = _bill_total.multiply(_tip_percent);
        _tip_amount = _tip_amount.setScale(2, RoundingMode.CEILING);
        _total = _bill_total.add(_tip_amount);

        bill_total.setText(_bill_total.toString());
        tip_percent.setText(_tip_percent.multiply(new BigDecimal("100")).toBigInteger().toString());
        tip_amount.setText(_tip_amount.toString());
        total.setText(_total.toString());
    }

}
