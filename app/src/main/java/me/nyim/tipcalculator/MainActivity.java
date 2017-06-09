package me.nyim.tipcalculator;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    private TextView bill_amount, tip_amount, total, split = null;

    private Button tip_button, split_button = null;
    private AlertDialog tip_alertDialog, split_alertDialog = null;
    private NumberPicker tip_numberPicker, split_numberPicker = null;

    private String amount, tip_percent, split_value, default_tip, default_split = "";
    private int MIN_TIP = 0;
    private int MAX_TIP = 100;
    private int MIN_SPLIT = 1;
    private int MAX_SPLIT = 30;
    private int DEFAULT_TIP = 15;
    private int DEFAULT_SPLIT = 2;

    private SharedPreferences pref;

    private final static String BILL_AMOUNT = "BILL_AMOUNT";
    //private final static String TIP_PERCENT = "TIP_PERCENT";
    //private final static String split_value = "split_value";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        default_tip = String.valueOf(pref.getInt("default_tip", DEFAULT_TIP));
        default_split = String.valueOf(pref.getInt("default_split", DEFAULT_SPLIT));

        tip_percent = default_tip;
        split_value = default_split;

       if (savedInstanceState == null) {
            amount = "";
        } else {
            amount = savedInstanceState.getString(BILL_AMOUNT);
        }


        bill_amount = (TextView) findViewById(R.id.bill_amount);
        tip_amount = (TextView) findViewById(R.id.tip_amount);
        total = (TextView) findViewById(R.id.total);
        split = (TextView) findViewById(R.id.split_amount);

        tip_button = (Button) findViewById(R.id.tip_option);
        split_button = (Button) findViewById(R.id.split_option);

        tip_button.setText(tip_percent.toString() + "%");
        split_button.setText(split_value.toString());

        tip_alertDialog = tip_dialog();
        split_alertDialog = split_dialog();

        tip_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                tip_alertDialog.show();
            }
        });

        split_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                split_alertDialog.show();
            }
        });


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
            default_split = String.valueOf(pref.getInt("default_split", DEFAULT_SPLIT));
            default_tip = String.valueOf(pref.getInt("default_tip", DEFAULT_TIP));

            tip_percent = default_tip;
            split_value = default_split;

            tip_button.setText(tip_percent.toString() + "%");
            split_button.setText(split_value.toString());

            tip_numberPicker.setValue(Integer.parseInt(tip_percent));
            split_numberPicker.setValue(Integer.parseInt(split_value));

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

        String _tip_percent;

        if (amount.length() == 0 | amount.equals("")) {
            f_amount = BigDecimal.ZERO;
        } else {
            f_amount = new BigDecimal(amount).divide(new BigDecimal("100"));
        }

        _tip_percent = tip_percent.replace("%", "");
        f_tip = new BigDecimal(_tip_percent).divide(new BigDecimal("100"));
        f_tip = f_tip.multiply(f_amount);
        f_total = f_amount.add(f_tip);

        f_split = f_total.divide(new BigDecimal(split_value), 2, BigDecimal.ROUND_HALF_UP);

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

    private AlertDialog tip_dialog() {
        final AlertDialog.Builder d = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog, null);

        d.setTitle(R.string.tip_dialog_msg);
        d.setView(dialogView);

        String[] percentage = new String[MAX_TIP - MIN_TIP + 1];
        for (int i = MIN_TIP; i <= MAX_TIP; i++) {
            percentage[i] = String.valueOf(i) + "%";
        }

        tip_numberPicker = (NumberPicker) dialogView.findViewById(R.id.number_picker);
        tip_numberPicker.setMaxValue(MAX_TIP);
        tip_numberPicker.setMinValue(MIN_TIP);
        tip_numberPicker.setDisplayedValues(percentage);
        tip_numberPicker.setValue(Integer.parseInt(tip_percent));
        tip_numberPicker.setWrapSelectorWheel(false);
        tip_numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {}
        });

        d.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String val = String.valueOf(tip_numberPicker.getValue());
                tip_percent = val;
                tip_button.setText(val + "%");
                calc_amount();
            }
        });

        d.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tip_numberPicker.setValue(Integer.parseInt(tip_percent));
            }
        });
        AlertDialog alertDialog = d.create();
        return alertDialog;
    }

    private AlertDialog split_dialog() {
        final AlertDialog.Builder d = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog, null);

        d.setTitle(R.string.split_dialog_msg);
        d.setView(dialogView);

        split_numberPicker = (NumberPicker) dialogView.findViewById(R.id.number_picker);
        split_numberPicker.setMaxValue(MAX_SPLIT);
        split_numberPicker.setMinValue(MIN_SPLIT);
        split_numberPicker.setValue(Integer.parseInt(split_value));
        split_numberPicker.setWrapSelectorWheel(false);
        split_numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {}
        });

        d.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String val = String.valueOf(split_numberPicker.getValue());
                split_value = val;
                split_button.setText(val);
                calc_amount();
            }
        });

        d.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                split_numberPicker.setValue(Integer.parseInt(split_value));
            }
        });
        AlertDialog alertDialog = d.create();
        return alertDialog;
    }
}
