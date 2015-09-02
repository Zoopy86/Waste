package com.zinc.zoopy.waste;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Stack;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 31-08-15.
 */
public class FragmentNewEntry extends android.support.v4.app.Fragment {

    SlidingUpPanelLayout mSlidingUpPanelLayout;
    private EditText mNumberInput;
    private EditText mTextInput;
    private TextView mDateTextView;
    private TextView mTimeTextView;
    private Button mPickCategoryButton;
    private Button mSubmit;
    private DateDialog mDateDialog = new DateDialog();
    private TimeDialog mTimeDialog = new TimeDialog();
    final Calendar c = Calendar.getInstance();
    private String mDateToSave;
    TextView mStackText;
    Stack<String> mInputStack = new Stack<>();
    Stack<String> mOperationStack = new Stack<>();
    boolean resetInput = false;
    boolean hasFinalResult = false;
    GridView mKeypadGrid;
    KeypadAdapter mKeypadAdapter;
    ImageView mSlideArrows;
    View rootView;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();
        rootView = inflater.inflate(R.layout.fragment_new_entry, container, false);
        initInstances();
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInputs();
            }
        });
        mPickCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ActivityPickCategory.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.move_left, R.anim.move_left2);
            }
        });
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    void initInstances() {
        EventBus.getDefault().register(this);
        mNumberInput = (EditText) rootView.findViewById(R.id.ewa_et_amount);
        mKeypadGrid = (GridView) rootView.findViewById(R.id.grid_calc);
        mTextInput = (EditText) rootView.findViewById(R.id.ewa_et_usernote);
        mDateTextView = (TextView) rootView.findViewById(R.id.tv_date);
        mTimeTextView = (TextView) rootView.findViewById(R.id.tv_time);
        mStackText = (TextView) rootView.findViewById(R.id.tv_stack);
        mPickCategoryButton = (Button) rootView.findViewById(R.id.b_pick_category);
        mSubmit = (Button) rootView.findViewById(R.id.ewa_b_save);
        mSlideArrows = (ImageView) rootView.findViewById(R.id.img_slide_arrows);
        mSlidingUpPanelLayout = (SlidingUpPanelLayout) rootView.findViewById(R.id.sliding_layout);
        mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        mNumberInput.setFocusable(false);
        mNumberInput.setText("0");
        mNumberInput.setTextColor(Color.BLACK);
        mTextInput.setFocusableInTouchMode(true);
        mKeypadAdapter = new KeypadAdapter(context);
        mDateTextView.setText(Config.dateStringBuilder(c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH), c.get(Calendar.YEAR)));
        mDateToSave = (Config.dateStringBuilder(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).toString());
        mTimeTextView.setText(Config.timeStringBuilder(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE)));

        String category = getActivity().getIntent().getStringExtra("category_name");
        if (category == null || category.matches("")) {
            category = getString(R.string.no_category);
        }
        mPickCategoryButton.setText(category);

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

        mNumberInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            }
        });
        rotateArrowsDown();
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
    }
    void rotateArrowsUp(){
        ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(context, R.animator.turn_up);
        anim.setTarget(mSlideArrows);
        anim.setDuration(500);
        anim.start();
    }
    void rotateArrowsDown(){
        ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(context, R.animator.turn_down);
        anim.setTarget(mSlideArrows);
        anim.setDuration(500);
        anim.start();
    }
    private void updateTimeView() {
        this.mTimeTextView.setText(Config.timeStringBuilder(mTimeDialog.pHour, mTimeDialog.pMinute));
    }

    private void updateDateView() {
        this.mDateTextView.setText(Config.dateStringBuilder(mDateDialog.pDay, mDateDialog.pMonth, mDateDialog.pYear));
        mDateToSave = (Config.dateStringBuilder(mDateDialog.pYear, mDateDialog.pMonth, mDateDialog.pDay)).toString();
    }

    public void setDate() {
        mDateDialog.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    public void setTime() {
        mTimeDialog.show(getActivity().getSupportFragmentManager(), "timePicker");
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
        mPickCategoryButton.setText(event.category);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    void saveInputs() {
        Waste Waste = new Waste();
        try {
            Waste.amount = Float.parseFloat(mNumberInput.getText().toString());
            if (Waste.amount == 0) {
                showToast(getString(R.string.error_empty_amount));
            } else {
                Waste.category = mPickCategoryButton.getText().toString();
                if (!Category.getAllNames().contains(Waste.category)) {
                    Category cat = new Category();
                    cat.name = Waste.category;
                    cat.save();
                }
                Waste.unixTime = System.currentTimeMillis();
                Waste.dayAdded = mDateToSave;
                Waste.timeAdded = mTimeTextView.getText().toString();
                Waste.userNote = mTextInput.getText().toString();
                Waste.save();
                EventBus.getDefault().post(new EventSimple());
                resetInputs();
                showToast(getString(R.string.amount) + ": " + Waste.amount + ". \n" +
                        getString(R.string.category) + ": " + Waste.category + ". \n" +
                        getString(R.string.comment) + ": " + Waste.userNote + ". \n" +
                        getString(R.string.date) + ": " + Waste.dayAdded);
            }
        } catch (Exception e) {
            Log.d("Invalid Input", "Exception: " + e.toString());
            showToast(getString(R.string.error_empty_amount));
        }
    }

    void resetInputs() {
        mNumberInput.setText("0");
        mTextInput.setText("");
        mNumberInput.requestFocus();
    }


    void showToast(String s) {
        Toast.makeText(context, s, Toast.LENGTH_LONG).show();
    }

    //region CALCULATOR FUNCTIONS

    //CALC Functions
    public void processKeypadInput(KeypadButton keypadButton) {
        String text = keypadButton.getText().toString();
        String currentInput = mNumberInput.getText().toString();
        int currentInputLen = currentInput.length();
        String evalResult;
        switch (keypadButton) {
            case CLOSE:
                break;
            case DOT:
                if (currentInput.contains(".") || hasFinalResult) {
                    break;
                } else {
                    mNumberInput.append(text);
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
                    mNumberInput.setText("0");
                }
                // Trim last character of the input text
                else {
                    mNumberInput.setText(currentInput.subSequence(0, endIndex));
                }
                break;
            case SIGN: // Handle -/+ sign
                // input has text and is different than initial value 0
                if (currentInputLen > 0 && !currentInput.equals("0")) {
                    // Already has (-) sign. Remove that sign
                    if (currentInput.charAt(0) == '-') {
                        mNumberInput.setText(currentInput.subSequence(1, currentInputLen));
                    }
                    // Prepend (-) sign
                    else {
                        mNumberInput.setText("-" + currentInput);
                    }
                }
                break;
            case CE: // Handle clear input
                mNumberInput.setText("0");
                break;
            case C: // Handle clear input and stack
                mNumberInput.setText("0");
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
                    mNumberInput.setText(evalResult);

                resetInput = true;
                break;
            case CALCULATE:
                if (mOperationStack.size() == 0)
                    break;

                mOperationStack.add(currentInput);
                evalResult = evaluateResult(true);
                if (evalResult != null) {
                    clearStacks();
                    mNumberInput.setText(evalResult);
                    resetInput = false;
                    hasFinalResult = true;
                }
                break;
            default:
                if (Character.isDigit(text.charAt(0))) {
                    if (currentInput.equals("0") ||
                            resetInput || hasFinalResult || currentInput.equals("-0")) {
                        mNumberInput.setText(text);
                        resetInput = false;
                        hasFinalResult = false;
                    } else {
                        mNumberInput.append(text);
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
    //endregion

}
