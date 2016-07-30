package me.nyim.tipcalculator;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String amount = "";
    private TextView bill_amount = null;
    private TextView tip_amount = null;
    private TextView total = null;
    private ArrayList<String> tip_options = null;
    private Spinner tip_spinner = null;
    private String tip_percent = "15%";

    /*private EditText bill_total = null;
    private EditText total = null;
    private SeekBar tip_bar = null;
    private EditText tip_percent = null;
    private EditText tip_amount = null;

    private BigDecimal _bill_total;
    private BigDecimal _tip_percent;
    private BigDecimal _tip_amount;
    private BigDecimal _total;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bill_amount = (TextView) findViewById(R.id.bill_amount);
        tip_amount = (TextView) findViewById(R.id.tip_amount);
        total = (TextView) findViewById(R.id.total);

        TextView one = (TextView) findViewById(R.id.one);
        TextView two = (TextView) findViewById(R.id.two);
        TextView three = (TextView) findViewById(R.id.three);
        TextView four = (TextView) findViewById(R.id.four);
        TextView five = (TextView) findViewById(R.id.five);
        TextView six = (TextView) findViewById(R.id.six);
        TextView seven = (TextView) findViewById(R.id.seven);
        TextView eight = (TextView) findViewById(R.id.eight);
        TextView nine = (TextView) findViewById(R.id.nine);
        TextView zero = (TextView) findViewById(R.id.zero);
        TextView clr = (TextView) findViewById(R.id.clr);
        TextView del = (TextView) findViewById(R.id.del);
        // For sure not the best way to do it but meh
        one.setOnClickListener(new KeypadListener());
        two.setOnClickListener(new KeypadListener());
        three.setOnClickListener(new KeypadListener());
        four.setOnClickListener(new KeypadListener());
        five.setOnClickListener(new KeypadListener());
        six.setOnClickListener(new KeypadListener());
        seven.setOnClickListener(new KeypadListener());
        eight.setOnClickListener(new KeypadListener());
        nine.setOnClickListener(new KeypadListener());
        zero.setOnClickListener(new KeypadListener());
        clr.setOnClickListener(new KeypadListener());
        del.setOnClickListener(new KeypadListener());

        // Populate tip percentage lists for spinner
        tip_options = new ArrayList<String>();
        String tmp = "";
        for (int i = 10; i <= 30; i++) {
            tmp = String.valueOf(i) + "%";
            tip_options.add(tmp);
        }
        tip_spinner = (Spinner) findViewById(R.id.tip_options);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner, tip_options);
        tip_spinner.setAdapter(adapter);
        tip_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tip_percent = adapterView.getItemAtPosition(i).toString();
                format_amount();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        tip_spinner.setSelection(adapter.getPosition(tip_percent));

        /*bill_total = (EditText) findViewById(R.id.bill_total);
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
        */
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

    private void format_amount() {
        BigDecimal f_amount;
        BigDecimal f_tip;
        BigDecimal f_total;
        if (amount.length() == 0 | amount.equals("")) {
            f_amount = BigDecimal.ZERO;
        } else {
            f_amount = new BigDecimal(amount).divide(new BigDecimal("100"));
        }

        tip_percent = tip_percent.replace("%", "");
        f_tip = new BigDecimal(tip_percent).divide(new BigDecimal("100"));
        f_tip = f_tip.multiply(f_amount);
        f_total = f_amount.add(f_tip);

        NumberFormat n = NumberFormat.getInstance();
        n.setMinimumFractionDigits(2);
        n.setMaximumFractionDigits(2);

        bill_amount.setText(n.format(f_amount));
        tip_amount.setText(n.format(f_tip));
        total.setText(n.format(f_total));
    }

    private void update_amount(String s) {
        if (s.equals("CLR")) {
            amount = "";
        } else if (s.equals("DEL")) {
            if (amount.length() > 0) {
                amount = amount.substring(0, amount.length() - 1);
            }
        } else {
            if (s.equals("0") && amount.length() == 0) {
                return;
            } else if (amount.length() > 4) {
                return;
            }
            amount = amount + s;
        }
        format_amount();
    }

    private class KeypadListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            update_amount(((TextView) view).getText().toString());
        }
    }

    /*private void calc() {
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
    }*/

}
