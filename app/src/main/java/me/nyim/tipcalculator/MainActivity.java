package me.nyim.tipcalculator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    private TextView bill_amount = null;
    private TextView tip_amount = null;
    private TextView tip_value = null;
    private TextView total = null;
    private TextView split = null;
    private TextView split_value = null;

    private String amount;
    private String tip_percent;
    private String split_num;
    private String default_tip;
    private String default_split;

    private SharedPreferences pref;

    private final static String BILL_AMOUNT = "BILL_AMOUNT";
    //private final static String TIP_PERCENT = "TIP_PERCENT";
    //private final static String SPLIT_NUM = "SPLIT_NUM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        default_tip = String.valueOf(pref.getInt("default_tip", 15));
        default_split = String.valueOf(pref.getInt("default_split", 2));

        tip_percent = default_tip;
        split_num = default_split;

       if (savedInstanceState == null) {
            amount = "";
        } else {
            amount = savedInstanceState.getString(BILL_AMOUNT);
        }


        bill_amount = (TextView) findViewById(R.id.bill_amount);
        tip_amount = (TextView) findViewById(R.id.tip_amount);
        total = (TextView) findViewById(R.id.total);
        split = (TextView) findViewById(R.id.split_amount);
        tip_value = (TextView) findViewById(R.id.tip_option);
        split_value = (TextView) findViewById(R.id.split_option);

        tip_value.setText(tip_percent.toString() + "%");
        split_value.setText(split_num.toString());

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

        // Might not be the best way to do it but meh
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

        calc_amount();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(BILL_AMOUNT, amount);
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
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.reset) {
            amount = "";
            default_split = String.valueOf(pref.getInt("default_split", 2));
            default_tip = String.valueOf(pref.getInt("default_tip", 15));

            tip_percent = default_tip;
            split_num = default_split;

            tip_value.setText(tip_percent.toString() + "%");
            split_value.setText(split_num.toString());

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

    @Override
    protected void onResume() {
        calc_amount();
        super.onResume();
    }

}
