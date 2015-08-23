package com.zinc.zoopy.waste;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Stack;

import de.greenrobot.event.EventBus;

public class ActivityMain extends AppCompatActivity {
    public static final String TAG = ActivityMain.class.getSimpleName();
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
    Animation toggleKeypad;
    TextView mStackText;
    Stack<String> mInputStack = new Stack<>();
    Stack<String> mOperationStack = new Stack<>();
    boolean resetInput = false;
    boolean hasFinalResult = false;
    GridView mKeypadGrid;
    KeypadAdapter mKeypadAdapter;
    boolean keypadIsOpen;

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
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
                Intent intent = new Intent(ActivityMain.this, ActivityPickCategory.class);
                startActivity(intent);
                overridePendingTransition(R.anim.move_left, R.anim.move_left2);
            }
        });
        Log.d(TAG, "Sorted Waste size: " + Waste.getWhere("Products", "Tickets").size());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("amount", mNumberInput.getText().toString());
        outState.putString("comment", mTextInput.getText().toString());
        outState.putString("category", mPickCategoryButton.getText().toString());
        outState.putString("date", mDateTextView.getText().toString());
        outState.putString("time", mTimeTextView.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mNumberInput.setText(savedInstanceState.getString("amount"));
        mTextInput.setText(savedInstanceState.getString("comment"));
        mPickCategoryButton.setText(savedInstanceState.getString("category"));
        mDateTextView.setText(savedInstanceState.getString("date"));
        mTimeTextView.setText(savedInstanceState.getString("time"));
    }


    void initInstances() {
        EventBus.getDefault().register(this);
        keypadIsOpen = true;
        mNumberInput = (EditText) findViewById(R.id.ewa_et_amount);
        mNumberInput.setInputType(InputType.TYPE_NULL);
        mKeypadGrid = (GridView) findViewById(R.id.grid_calc);
        mTextInput = (EditText) findViewById(R.id.ewa_et_usernote);
        mDateTextView = (TextView) findViewById(R.id.tv_date);
        mTimeTextView = (TextView) findViewById(R.id.tv_time);
        mStackText = (TextView) findViewById(R.id.tv_stack);
        mPickCategoryButton = (Button) findViewById(R.id.b_pick_category);
        mSubmit = (Button) findViewById(R.id.ewa_b_save);


        mKeypadAdapter = new KeypadAdapter(this);
        mDateTextView.setText(Config.dateStringBuilder(c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH), c.get(Calendar.YEAR)));
        mDateToSave = (Config.dateStringBuilder(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).toString());
        mTimeTextView.setText(Config.timeStringBuilder(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE)));

        String category = getIntent().getStringExtra("category_name");
        if (category == null || category.matches("")) {
            category = "No category";
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
                if (!keypadIsOpen) {
                    mKeypadGrid.startAnimation(getToggleKeypad());
                    keypadIsOpen = true;
                    Log.d(TAG, "Bottom" + mKeypadGrid.getBottom());
                }
            }
        });
    }

    private void updateTimeView() {
        this.mTimeTextView.setText(Config.timeStringBuilder(mTimeDialog.pHour, mTimeDialog.pMinute));
    }

    private void updateDateView() {
        this.mDateTextView.setText(Config.dateStringBuilder(mDateDialog.pDay, mDateDialog.pMonth, mDateDialog.pYear));
        mDateToSave = (Config.dateStringBuilder(mDateDialog.pYear, mDateDialog.pMonth, mDateDialog.pDay)).toString();
        Log.d(TAG, "DATE TO SAVE: " + mDateToSave);
    }

    public void setDate() {
        mDateDialog.show(getSupportFragmentManager(), "datePicker");
    }

    public void setTime() {
        mTimeDialog.show(getSupportFragmentManager(), "timePicker");
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
    protected void onStop() {
        Log.d(TAG, "Activity stopped");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "Activity destroyed");
        EventBus.getDefault().unregister(this);
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
                Waste.unixTime = System.currentTimeMillis();
                Waste.dayAdded = mDateToSave;
                Waste.timeAdded = mTimeTextView.getText().toString();
                Waste.userNote = mTextInput.getText().toString();
                Waste.save();
                resetInputs();
                showToast(getString(R.string.amount) + ": " + Waste.amount + ". \n" +
                        getString(R.string.category) + ": " + Waste.category + ". \n" +
                        getString(R.string.comment) + ": "+ Waste.userNote + ". \n" +
                        getString(R.string.date) + ": " + Waste.dayAdded);
            }
        } catch (Exception e) {
            Log.d("Invalid Input", "Exception: " + e.toString());
            showToast(getString(R.string.error_empty_amount));
        }
    }

    void resetInputs() {
        mNumberInput.setText("");
        mTextInput.setText("");
        mNumberInput.requestFocus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item_waste clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.report) {
            Intent intent = new Intent(getApplicationContext(), ActivityReport.class);
            startActivity(intent);
        }
        if(id == R.id.journal){
            Intent intent = new Intent(getApplicationContext(), ActivityJournal.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    void showToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    //region CALCULATOR FUNCTIONS
    private Animation getToggleKeypad() {
        if (keypadIsOpen) {
            toggleKeypad = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.close_keypad);
        } else if (!keypadIsOpen) {
            toggleKeypad = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.open_keypad);
        }
        toggleKeypad.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mKeypadGrid.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!keypadIsOpen) {
                    mKeypadGrid.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return toggleKeypad;
    }

    //CALC Functions
    public void processKeypadInput(KeypadButton keypadButton) {
        String text = keypadButton.getText().toString();
        String currentInput = mNumberInput.getText().toString();
        int currentInputLen = currentInput.length();
        String evalResult;
        switch (keypadButton) {
            case CLOSE:
                if (keypadIsOpen) {
                    mKeypadGrid.startAnimation(getToggleKeypad());
                    keypadIsOpen = false;
                }
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
                    mNumberInput.setText("");
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
                        mNumberInput.setText
                                (currentInput.subSequence(1,
                                        currentInputLen));
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

//    private double tryParseUserInput() {
//        String inputStr = mNumberInput.getText().toString();
//        double result = Double.NaN;
//        try {
//            result = Double.parseDouble(inputStr);
//
//        } catch (NumberFormatException nfe) {
//
//        }
//        return result;
//
//    }
    //endregion

}
