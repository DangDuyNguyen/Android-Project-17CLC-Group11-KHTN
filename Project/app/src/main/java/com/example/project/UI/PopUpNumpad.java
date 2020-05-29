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
    private Map<Integer, Button> mNumberBtns = new HashMap<>();
    private int mSelectedNumber;
    private OnNumberEditListener mOnNumberEditListener;

    public PopUpNumpad(Context context) {
        super(context);
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setContentView(R.layout.pop_up_numpad);
    }

    public void setOnNumberEditListener(OnNumberEditListener listener) {
        mOnNumberEditListener = listener;
    }

    public void updateNumber(Integer number) {
        mSelectedNumber = number;
    }

    public void setValueCount(int number, int count) {
        mNumberBtns.get(number).setText(number + " (" + count + ")");
    }

    private View createEditNumberView() {
        View view = mInflater.inflate(R.layout.pop_up_numpad, null);

        mNumberBtns.put(1, view.findViewById(R.id.btn1));
        mNumberBtns.put(2, view.findViewById(R.id.btn2));
        mNumberBtns.put(3, view.findViewById(R.id.btn3));
        mNumberBtns.put(4, view.findViewById(R.id.btn4));
        mNumberBtns.put(5, view.findViewById(R.id.btn5));
        mNumberBtns.put(6, view.findViewById(R.id.btn6));
        mNumberBtns.put(7, view.findViewById(R.id.btn7));
        mNumberBtns.put(8, view.findViewById(R.id.btn8));
        mNumberBtns.put(9, view.findViewById(R.id.btn9));

        for (Integer num : mNumberBtns.keySet()) {
            Button btn = mNumberBtns.get(num);
            btn.setTag(num);
            btn.setOnClickListener(editNumberButtonClickListener);
        }

        Button closeBtn = view.findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        Button clearBtn = view.findViewById(R.id.clearBtn);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnNumberEditListener != null) {
                    mOnNumberEditListener.onNumberEdit(0);
                }
                dismiss();
            }
        });

        return view;
    }

    private View.OnClickListener editNumberButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Integer number = (Integer) view.getTag();

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
