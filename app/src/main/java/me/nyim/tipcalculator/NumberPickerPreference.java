package me.nyim.tipcalculator;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

public class NumberPickerPreference extends DialogPreference {

        private int MAX_VALUE = 100;
        private int MIN_VALUE = 1;
        private boolean PERCENTAGE = false;

        // enable or disable the 'circular behavior'
        private static final boolean WRAP_SELECTOR_WHEEL = false;

        private NumberPicker picker;
        private int value;

        public NumberPickerPreference(Context context, AttributeSet attrs) {
            super(context, attrs);
            MAX_VALUE = attrs.getAttributeIntValue(null, "max", 100);
            MIN_VALUE = attrs.getAttributeIntValue(null, "min", 1);
            PERCENTAGE = attrs.getAttributeBooleanValue(null, "percentage", false);
        }

        public NumberPickerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            MAX_VALUE = attrs.getAttributeIntValue(null, "max", 100);
            MIN_VALUE = attrs.getAttributeIntValue(null, "min", 1);
            PERCENTAGE = attrs.getAttributeBooleanValue(null, "percentage", false);
        }

        @Override
        protected View onCreateDialogView() {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER;

            picker = new NumberPicker(getContext());
            picker.setLayoutParams(layoutParams);

            FrameLayout dialogView = new FrameLayout(getContext());
            dialogView.addView(picker);

            return dialogView;
        }

        @Override
        protected void onBindDialogView(View view) {
            super.onBindDialogView(view);

            picker.setMinValue(MIN_VALUE);
            picker.setMaxValue(MAX_VALUE);

            if (PERCENTAGE) {
                String[] percentage = new String[MAX_VALUE - MIN_VALUE + 1];
                for (int i = MIN_VALUE; i <= MAX_VALUE; i++) {
                    percentage[i] = String.valueOf(i) + "%";
                }
                picker.setDisplayedValues(percentage);
            }

            picker.setWrapSelectorWheel(WRAP_SELECTOR_WHEEL);
            picker.setValue(getValue());
        }

        @Override
        protected void onDialogClosed(boolean positiveResult) {
            if (positiveResult) {
                picker.clearFocus();
                int newValue = picker.getValue();
                if (callChangeListener(newValue)) {
                    setValue(newValue);
                }
            }
        }

        @Override
        protected Object onGetDefaultValue(TypedArray a, int index) {
            return a.getInt(index, MIN_VALUE);
        }

        @Override
        protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
            setValue(restorePersistedValue ? getPersistedInt(MIN_VALUE) : (Integer) defaultValue);
        }

        public void setValue(int value) {
            this.value = value;
            persistInt(this.value);

            String summary = String.valueOf(value);
            if (PERCENTAGE) { summary += "%"; }
            this.setSummary(String.valueOf(summary));
        }

        public int getValue() {
            return this.value;
        }

}
