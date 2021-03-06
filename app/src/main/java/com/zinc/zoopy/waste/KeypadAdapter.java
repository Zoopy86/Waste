package com.zinc.zoopy.waste;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;

/**
 * Created by Zoopy86 on 04-08-15.
 */
public class KeypadAdapter extends BaseAdapter {
    private Context mContext;

    private View.OnClickListener mOnButtonClick;

    public KeypadAdapter(Context c) {
        mContext = c;
    }

    public void setOnButtonClickListener(View.OnClickListener listener) {
        mOnButtonClick = listener;
    }

    public int getCount() {
        return mButtons.length;
    }

    public Object getItem(int position) {
        return mButtons[position];
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ButtonView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        Button btn;
        if (convertView == null) { // if it's not recycled, initialize some attributes
            btn = new Button(mContext);
            btn.setTextColor(Color.WHITE);
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(100, 70);
            btn.setLayoutParams(params);
            btn.setTextSize(16);
            btn.setPadding(0,0,0,0);
            KeypadButton keypadButton = mButtons[position];
            if (keypadButton != KeypadButton.NONE) {
                btn.setOnClickListener(mOnButtonClick);
            }

            btn.setBackgroundResource(R.drawable.keypad_states);
            // Set CalculatorButton enumeration as tag of the button so that we
            // will use this information from our main view to identify what to do
            btn.setTag(keypadButton);
        } else {
            btn = (Button) convertView;
        }

        btn.setText(mButtons[position].getText());
        return btn;
    }

    private KeypadButton[] mButtons = {
            KeypadButton.BACKSPACE,
            KeypadButton.C,
            KeypadButton.SIGN,
            KeypadButton.CLOSE,
            KeypadButton.SEVEN,
            KeypadButton.EIGHT,
            KeypadButton.NINE,
            KeypadButton.MINUS,
            KeypadButton.FOUR,
            KeypadButton.FIVE,
            KeypadButton.SIX,
            KeypadButton.PLUS,
            KeypadButton.ONE,
            KeypadButton.TWO,
            KeypadButton.THREE,
            KeypadButton.MULTIPLY,
            KeypadButton.DOT,
            KeypadButton.ZERO,
            KeypadButton.CALCULATE,
            KeypadButton.DIVIDE,
    };
}
