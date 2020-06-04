package com.example.project.UI;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.project.R;

import java.util.HashMap;
import java.util.Map;

public class PopUpNumpad extends Dialog {
    private Context mContext;
    private LayoutInflater mInflater;

    private Map<Integer, Button> mNumberButtons = new HashMap<>();

    private int mSelectedNumber;

    private OnNumberEditListener mOnNumberEditListener;

    public PopUpNumpad(Context context) {
        super(context);
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = createView();
        setContentView(v);
    }

    public void setOnNumberEditListener(OnNumberEditListener l) {
        mOnNumberEditListener = l;
    }

    public void updateNumber(Integer number) {
        mSelectedNumber = number;
    }

    public void setValueCount(int number, int count) {
        mNumberButtons.get(number).setText(number + " (" + count + ")");
    }

    private View createView() {
        View v = mInflater.inflate(R.layout.pop_up_numpad, null);

        mNumberButtons.put(1, v.findViewById(R.id.btn1));
        mNumberButtons.put(2, v.findViewById(R.id.btn2));
        mNumberButtons.put(3, v.findViewById(R.id.btn3));
        mNumberButtons.put(4, v.findViewById(R.id.btn4));
        mNumberButtons.put(5, v.findViewById(R.id.btn5));
        mNumberButtons.put(6, v.findViewById(R.id.btn6));
        mNumberButtons.put(7, v.findViewById(R.id.btn7));
        mNumberButtons.put(8, v.findViewById(R.id.btn8));
        mNumberButtons.put(9, v.findViewById(R.id.btn9));

        for (Integer num : mNumberButtons.keySet()) {
            Button b = mNumberButtons.get(num);
            b.setTag(num);
            b.setOnClickListener(editNumberButtonClickListener);
        }

        Button closeButton = v.findViewById(R.id.closeBtn);
        closeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        Button clearButton = v.findViewById(R.id.clearBtn);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnNumberEditListener != null) {
                    mOnNumberEditListener.onNumberEdit(0);
                }
                dismiss();
            }
        });

        return v;
    }

    private View.OnClickListener editNumberButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Integer number = (Integer) v.getTag();

            if (mOnNumberEditListener != null) {
                mOnNumberEditListener.onNumberEdit(number);
            }
            dismiss();
        }
    };

    public interface OnNumberEditListener {
        boolean onNumberEdit(int number);
    }
}
