package com.omneAgate.wholeSaler.UndoBar;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.omneAgate.wholeSaler.Util.TamilUtil;
import com.omneAgate.wholeSaler.activity.GlobalAppState;
import com.omneAgate.wholeSaler.activity.R;


public class UndoBarView extends MaxWidthRelativeLayout {

    Context context;
    private TextView mMessage;

    public UndoBarView(Context context) {
        super(context);
        this.context = context;
    }

    public UndoBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public UndoBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }


    public UndoBarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mMessage = (TextView) findViewById(R.id.message);
      /*  Typeface tfBamini = Typeface.createFromAsset(context.getAssets(),"fonts/Bamini.ttf");
        mMessage.setTypeface(tfBamini);*/
    }

    void setMessage(CharSequence message) {
        if (GlobalAppState.language.equals("ta")) {
            Typeface tfBamini = Typeface.createFromAsset(context.getAssets(), "fonts/Bamini.ttf");
            mMessage.setTypeface(tfBamini);
            mMessage.setText(TamilUtil.convertToTamil(TamilUtil.BAMINI, message.toString()));
        } else {
            mMessage.setText(message);
        }

    }


    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.message = mMessage.getText().toString();
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setMessage(ss.message);
    }

    @SuppressWarnings("NullableProblems")
    private static class SavedState extends View.BaseSavedState {

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {

            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        String message;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            message = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(message);
        }
    }
}
