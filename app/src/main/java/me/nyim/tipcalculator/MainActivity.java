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

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView bill_amount = null;
    private TextView tip_amount = null;
    private TextView total = null;
    private TextView split = null;

    private ArrayList<String> tip_options = null;
    private ArrayList<String> split_options = null;

    private Spinner tip_spinner = null;
    private Spinner split_spinner = null;

    private String amount;
    private String tip_percent;
    private String split_num;

    private final static int MAX_TIP = 30;
    private final static int MAX_SPLIT = 30;
    private final static String BILL_AMOUNT = "BILL_AMOUNT";
    private final static String TIP_PERCENT = "TIP_PERCENT";
    private final static String SPLIT_NUM = "SPLIT_NUM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            amount = "";
            tip_percent = "15%";
            split_num = "1";
        } else {
            amount = savedInstanceState.getString(BILL_AMOUNT);
            tip_percent = savedInstanceState.getString(TIP_PERCENT);
            split_num = savedInstanceState.getString(SPLIT_NUM);
        }

        bill_amount = (TextView) findViewById(R.id.bill_amount);
        tip_amount = (TextView) findViewById(R.id.tip_amount);
        total = (TextView) findViewById(R.id.total);
        split = (TextView) findViewById(R.id.split_amount);

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
        for (int i = 10; i <= MAX_TIP; i++) {
            tmp = String.valueOf(i) + "%";
            tip_options.add(tmp);
        }

        tip_spinner = (Spinner) findViewById(R.id.tip_options);
        ArrayAdapter<String> tip_adapter = new ArrayAdapter<String>(this, R.layout.spinner, tip_options);
        tip_spinner.setAdapter(tip_adapter);
        tip_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tip_percent = adapterView.getItemAtPosition(i).toString();
                calc_amount();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        tip_spinner.setSelection(tip_adapter.getPosition(tip_percent));

        // Populate split numbers for spinner
        split_options = new ArrayList<String>();
        for (int i = 1; i <= MAX_SPLIT; i++) {
            tmp = String.valueOf(i);
            split_options.add(tmp);
        }

        split_spinner = (Spinner) findViewById(R.id.split_options);
        ArrayAdapter<String> split_adapter = new ArrayAdapter<String>(this, R.layout.spinner, split_options);
        split_spinner.setAdapter(split_adapter);
        split_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                split_num = adapterView.getItemAtPosition(i).toString();
                calc_amount();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        split_spinner.setSelection(split_adapter.getPosition(split_num));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(BILL_AMOUNT, amount);
        outState.putString(TIP_PERCENT, tip_percent);
        outState.putString(SPLIT_NUM, split_num);
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
        } else if (id == R.id.reset) {
            amount = "";
            // Reset tip spinner to 15%
            tip_spinner.setSelection(5);
            // and split spinner to 1
            split_spinner.setSelection(0);
            calc_amount();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void calc_amount() {
        BigDecimal f_amount;
        BigDecimal f_tip;
        BigDecimal f_total;
        BigDecimal f_split;

        if (amount.length() == 0 | amount.equals("")) {
            f_amount = BigDecimal.ZERO;
        } else {
            f_amount = new BigDecimal(amount).divide(new BigDecimal("100"));
        }

        tip_percent = tip_percent.replace("%", "");
        f_tip = new BigDecimal(tip_percent).divide(new BigDecimal("100"));
        f_tip = f_tip.multiply(f_amount);
        f_total = f_amount.add(f_tip);

        f_split = f_total.divide(new BigDecimal(split_num), 2, BigDecimal.ROUND_HALF_UP);

        NumberFormat n = NumberFormat.getInstance();
        n.setMinimumFractionDigits(2);
        n.setMaximumFractionDigits(2);

        bill_amount.setText(n.format(f_amount));
        tip_amount.setText(n.format(f_tip));
        total.setText(n.format(f_total));
        split.setText(n.format(f_split));
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
            } else if (amount.length() > 5) {
                return;
            }
            amount = amount + s;
        }
        calc_amount();
    }

    private class KeypadListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            update_amount(((TextView) view).getText().toString());
        }
    }
}
