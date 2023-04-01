package com.example.rdvmanager;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.text.InputType;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

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
        this.setButton(BUTTON_NEGATIVE,context.getString(R.string.cancel),(dialog,which)->{});
    }
    /********************/
    public ImageButton getImageButton(){return this.aImageButton;}
    public EditText getEditText(){return this.aEditText;}
    /********************/
    private ImageButton createImageButton(Context context, int icon_id)
    {
        ImageButton button = new ImageButton(context);
        button.setImageResource(icon_id);
        button.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_corners_yellow));
        button.setColorFilter(ContextCompat.getColor(context,R.color.black),PorterDuff.Mode.SRC_IN);
        return button;
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
        linearLayout.addView(this.createFirstRow(context));
        linearLayout.addView(this.createSecondRow(context));
        return linearLayout;
    }
    /********************/
    private LinearLayout createFirstRow(Context context)
    {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(WRAP_CONTENT,WRAP_CONTENT);
        params.setMargins(0, 0, 10, 20);
        LinearLayout firstRow = new LinearLayout(context);
        firstRow.setOrientation(LinearLayout.HORIZONTAL);
        firstRow.setGravity(Gravity.CENTER_HORIZONTAL);
        firstRow.addView(this.aImageButton,params);
        firstRow.addView(this.aTitleView);
        return firstRow;
    }
    /********************/
    private LinearLayout createSecondRow(Context context)
    {
        LinearLayout secondRow = new LinearLayout(context);
        secondRow.addView(this.aEditText, new LinearLayout.LayoutParams(MATCH_PARENT,WRAP_CONTENT));
        return secondRow;
    }
}