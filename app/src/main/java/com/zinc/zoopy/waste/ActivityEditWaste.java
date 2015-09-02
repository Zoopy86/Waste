package com.zinc.zoopy.waste;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.Iterator;
import java.util.Stack;

import de.greenrobot.event.EventBus;


public class ActivityEditWaste extends AppCompatActivity {

    public static long mID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_waste);
        mID = getIntent().getLongExtra("id", 0);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_waste, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item_waste clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.action_new_waste){
            Intent intent = new Intent(this, ActivityMain.class);
            startActivity(intent);
        }
        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
            overridePendingTransition(R.anim.move_right2, R.anim.move_right);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        EditText amount;
        EditText userNote;
        Button mSaveButton;
        Context mContext;
        TextView mDateTextView;
        TextView mTimeTextView;
        Waste mWaste = Waste.load(Waste.class, mID);
        Button mCategoryButton;
        DateDialog mDateDialog = new DateDialog();
        TimeDialog mTimeDialog = new TimeDialog();
        TextView mStackText;
        Stack<String> mInputStack = new Stack<>();
        Stack<String> mOperationStack = new Stack<>();
        boolean resetInput = false;
        boolean hasFinalResult = false;
        GridView mKeypadGrid;
        KeypadAdapter mKeypadAdapter;
        SlidingUpPanelLayout mSlidingUpPanelLayout;
        ImageView mSlideArrows;
        View rootView;
        public PlaceholderFragment() {
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            EventBus.getDefault().register(this);
            rootView = inflater.inflate(R.layout.fragment_edit_waste, container, false);
            amount = (EditText) rootView.findViewById(R.id.ewa_et_amount);
            amount.setInputType(InputType.TYPE_NULL);
            userNote = (EditText) rootView.findViewById(R.id.ewa_et_usernote);
            mSaveButton = (Button) rootView.findViewById(R.id.ewa_b_save);
            mContext = rootView.getContext();
            mDateTextView = (TextView) rootView.findViewById(R.id.tv_date);
            mTimeTextView = (TextView) rootView.findViewById(R.id.tv_time);
            mCategoryButton = (Button)rootView.findViewById(R.id.b_pick_category);
            mKeypadGrid = (GridView) rootView.findViewById(R.id.grid_calc);
            mStackText = (TextView) rootView.findViewById(R.id.tv_stack);

            mSlideArrows = (ImageView) rootView.findViewById(R.id.img_slide_arrows);
            mSlidingUpPanelLayout = (SlidingUpPanelLayout) rootView.findViewById(R.id.sliding_layout);
            mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            mKeypadAdapter = new KeypadAdapter(mContext);

            mCategoryButton.setText(mWaste.category);
            amount.setText(mWaste.amount.toString());
            userNote.setText(mWaste.userNote);
            mDateTextView.setText(mWaste.dayAdded);
            mTimeTextView.setText(mWaste.timeAdded);
            mCategoryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ActivityPickCategory.class);
                    startActivity(intent);
                    ((Activity) mContext).overridePendingTransition(R.anim.move_left, R.anim.move_left2);
                }
            });
            mSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        mWaste.amount = Float.parseFloat(amount.getText().toString());
                    } catch (Exception e) {
                        Log.e("logtag", "Exception: " + e.toString());
                    }
                    mWaste.userNote = userNote.getText().toString();
                    mWaste.category = mCategoryButton.getText().toString();
                    mWaste.dayAdded = mDateTextView.getText().toString();
                    mWaste.timeAdded = mTimeTextView.getText().toString();
                    mWaste.save();
                    Intent intent = new Intent(mContext, ActivitySortedEntries.class);
                    startActivity(intent);
                }
            });
            mDateTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setDate();
                }
            });
            mTimeTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setTime();
                }
            });

            mKeypadGrid.setAdapter(mKeypadAdapter);
            mKeypadAdapter.setOnButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button btn = (Button) v;
                    // Get the KeypadButton value which is used to identify the
                    // keypad button from the Button's tag
                    KeypadButton keypadButton = (KeypadButton) btn.getTag();

                    // Process keypad button
                    processKeypadInput(keypadButton);
                }
            });

            amount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                }
            });


            rotateArrowsUp();
            mSlidingUpPanelLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {

                @Override
                public void onPanelSlide(View view, float v) {

                }

                @Override
                public void onPanelCollapsed(View view) {
                    rotateArrowsUp();
                }

                @Override
                public void onPanelExpanded(View view) {
                    rotateArrowsDown();
                }

                @Override
                public void onPanelAnchored(View view) {

                }

                @Override
                public void onPanelHidden(View view) {

                }
            });
            return rootView;
        }
        public void rotateArrowsUp(){
            ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(mContext, R.animator.turn_up);
            anim.setTarget(mSlideArrows);
            anim.setDuration(500);
            anim.start();
        }
        public void rotateArrowsDown(){
            ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(mContext, R.animator.turn_down);
            anim.setTarget(mSlideArrows);
            anim.setDuration(500);
            anim.start();
        }

        private void updateTimeView() {
            this.mTimeTextView.setText(Config.timeStringBuilder(mTimeDialog.pHour, mTimeDialog.pMinute));
        }

        private void updateDateView() {
            this.mDateTextView.setText(Config.dateStringBuilder(mDateDialog.pDay, mDateDialog.pMonth, mDateDialog.pYear));
        }

        public void onEvent(EventDialog event) {
            switch (event.dialogID) {
                case DateDialog.DIALOG_ID:
                    updateDateView();
                    break;
                case TimeDialog.DIALOG_ID:
                    updateTimeView();
                    break;
            }
        }
        public void onEvent(EventCategory event) {
            mCategoryButton.setText(event.category);
        }

        public void setDate() {
            mDateDialog.show(getActivity().getSupportFragmentManager(), "datePicker");
        }

        public void setTime() {
            mTimeDialog.show(getActivity().getSupportFragmentManager(), "timePicker");
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            EventBus.getDefault().unregister(this);
        }
        //region CALCULATOR FUNCTIONS

        //CALC Functions
        public void processKeypadInput(KeypadButton keypadButton) {
            String text = keypadButton.getText().toString();
            String currentInput = amount.getText().toString();
            int currentInputLen = currentInput.length();
            String evalResult;
            double userInputValue = Double.NaN;
            switch (keypadButton) {
                case CLOSE:
                    break;
                case DOT:
                    if (currentInput.contains(".") || hasFinalResult) {
                        break;
                    } else {
                        amount.append(text);
                        resetInput = false;
                        break;
                    }
                case BACKSPACE: // Handle backspace
                    // If has operand skip backspace
                    if (resetInput) {
                        break;
                    }

                    int endIndex = currentInputLen - 1;

                    // There is one character at input so reset input to 0
                    if (endIndex < 1) {
                        amount.setText("");
                    }
                    // Trim last character of the input text
                    else {
                        amount.setText(currentInput.subSequence(0, endIndex));
                    }
                    break;
                case SIGN: // Handle -/+ sign
                    // input has text and is different than initial value 0
                    if (currentInputLen > 0 && !currentInput.equals("0")) {
                        // Already has (-) sign. Remove that sign
                        if (currentInput.charAt(0) == '-') {
                            amount.setText
                                    (currentInput.subSequence(1,
                                            currentInputLen));
                        }
                        // Prepend (-) sign
                        else {
                            amount.setText("-" + currentInput);
                        }
                    }
                    break;
                case CE: // Handle clear input
                    amount.setText("0");
                    break;
                case C: // Handle clear input and stack
                    amount.setText("0");
                    clearStacks();
                    break;
                case DIVIDE:
                case PLUS:
                case MINUS:
                case MULTIPLY:
                    if (resetInput) {
                        mInputStack.pop();
                        mOperationStack.pop();
                    } else {
                        if (currentInput.charAt(0) == '-') {
                            mInputStack.add("(" + currentInput + ")");
                        } else {
                            mInputStack.add(currentInput);
                        }
                        mOperationStack.add(currentInput);
                    }

                    mInputStack.add(text);
                    mOperationStack.add(text);

                    dumpInputStack();
                    evalResult = evaluateResult(false);
                    if (evalResult != null)
                        amount.setText(evalResult);

                    resetInput = true;
                    break;
                case CALCULATE:
                    if (mOperationStack.size() == 0)
                        break;

                    mOperationStack.add(currentInput);
                    evalResult = evaluateResult(true);
                    if (evalResult != null) {
                        clearStacks();
                        amount.setText(evalResult);
                        resetInput = false;
                        hasFinalResult = true;
                    }
                    break;
                default:
                    if (Character.isDigit(text.charAt(0))) {
                        if (currentInput.equals("0") ||
                                resetInput || hasFinalResult || currentInput.equals("-0")) {
                            amount.setText(text);
                            resetInput = false;
                            hasFinalResult = false;
                        } else {
                            amount.append(text);
                            resetInput = false;
                        }

                    }
                    break;
            }
        }

        private void clearStacks() {
            mInputStack.clear();
            mOperationStack.clear();
            mStackText.setText("");
        }

        private void dumpInputStack() {
            Iterator<String> it = mInputStack.iterator();
            StringBuilder sb = new StringBuilder();

            while (it.hasNext()) {
                CharSequence iValue = it.next();
                sb.append(iValue);

            }

            mStackText.setText(sb.toString());
        }

        private String evaluateResult(boolean requestedByUser) {
            if ((!requestedByUser && mOperationStack.size() != 4)
                    || (requestedByUser && mOperationStack.size() != 3))
                return null;

            String left = mOperationStack.get(0);
            String operator = mOperationStack.get(1);
            String right = mOperationStack.get(2);
            String tmp = null;
            if (!requestedByUser)
                tmp = mOperationStack.get(3);

            double leftVal = Double.parseDouble(left);
            double rightVal = Double.parseDouble(right);
            double result = Double.NaN;

            if (operator.equals(KeypadButton.DIVIDE.getText())) {
                result = leftVal / rightVal;
            } else if (operator.equals(KeypadButton.MULTIPLY.getText())) {
                result = leftVal * rightVal;
            } else if (operator.equals(KeypadButton.PLUS.getText())) {
                result = leftVal + rightVal;
            } else if (operator.equals(KeypadButton.MINUS.getText())) {
                result = leftVal - rightVal;
            }

            String resultStr = doubleToString(result);
            if (resultStr == null)
                return "NaN";

            mOperationStack.clear();
            if (!requestedByUser) {
                mOperationStack.add(resultStr);
                mOperationStack.add(tmp);
            }

            return resultStr;
        }

        private String doubleToString(double value) {
            if (Double.isNaN(value))
                return null;

            long longVal = (long) value;
            if (longVal == value)
                return Long.toString(longVal);
            else
                return Double.toString(value);

        }

//        private double tryParseUserInput() {
//            String inputStr = amount.getText().toString();
//            double result = Double.NaN;
//            try {
//                result = Double.parseDouble(inputStr);
//
//            } catch (NumberFormatException nfe) {
//            }
//            return result;
//
//        }
        //endregion
    }
}
