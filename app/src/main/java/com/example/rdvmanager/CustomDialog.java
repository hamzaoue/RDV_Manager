package com.example.rdvmanager;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import androidx.core.content.ContextCompat;

import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ImageButton;
import android.widget.EditText;
import android.app.AlertDialog;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

/********************/
public class CustomDialog extends AlertDialog
{
    private final TextView aTitleView;
    private final ImageButton aImageButton ;
    private final EditText aEditText;
    /********************/
    public CustomDialog(Context context, int title_id, int icon_id)
    {
        super(context,R.style.MyDialogTheme);
        this.aImageButton = this.createImageButton(context , icon_id);
        this.aEditText = this.createEditText(context);
        this.aTitleView = this.createTextView(context,title_id);
        this.setView(this.createLayout(context));
        Window window = this.getWindow();
        window.setBackgroundDrawableResource(R.drawable.rounded_corners_black);
        this.setButton(BUTTON_NEGATIVE,context.getString(R.string.cancel),(dialog,which)->{});
    }
    /********************/
    public ImageButton getImageButton(){return this.aImageButton;}
    public EditText getEditText(){return this.aEditText;}
    /********************/
    private ImageButton createImageButton(Context context, int icon_id)
    {
        ImageButton imageButton = new ImageButton(context);
        imageButton.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_corners_yellow));
        imageButton.setImageResource(icon_id);
        imageButton.setColorFilter(ContextCompat.getColor(context, R.color.black), PorterDuff.Mode.SRC_IN);
        return imageButton;
    }
    /********************/
    private EditText createEditText(Context context)
    {
        EditText editText = new EditText(context);
        editText.setTextAppearance(R.style.DialogEditTextStyle);
        editText.setPadding(10,0,10,0);
        editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editText.setMaxLines(10);
        editText.setHorizontallyScrolling(false);
        editText.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_empty_border));
        editText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count)
            {editText.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));}});
        return editText;
    }
    /********************/
    private TextView createTextView(Context context,int title_id)
    {
        TextView textView = new TextView(context);
        textView.setTextAppearance(R.style.DialogTitleStyle);
        textView.setText(title_id);
        return textView;
    }
    /********************/
    private LinearLayout createLayout(Context context)
    {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setPadding(30,30,30,30);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        linearLayout.addView(this.createFirstRow(context));
        linearLayout.addView(this.createSecondRow(context));
        return linearLayout;
    }
    /********************/
    private LinearLayout createFirstRow(Context context)
    {
        LinearLayout firstRow = new LinearLayout(context);
        firstRow.setOrientation(LinearLayout.HORIZONTAL);
        firstRow.setGravity(Gravity.CENTER_HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(WRAP_CONTENT,WRAP_CONTENT);
        params.setMargins(0, 0, 10, 20);
        firstRow.addView(this.aImageButton,params);
        firstRow.addView(this.aTitleView);
        return firstRow;
    }
    /********************/
    private LinearLayout createSecondRow(Context context)
    {
        LinearLayout secondRow = new LinearLayout(context);
        secondRow.setOrientation(LinearLayout.HORIZONTAL);
        secondRow.setGravity(Gravity.CENTER_VERTICAL);
        secondRow.addView(this.aEditText, new LinearLayout.LayoutParams(MATCH_PARENT,WRAP_CONTENT));
        return secondRow;
    }

}
