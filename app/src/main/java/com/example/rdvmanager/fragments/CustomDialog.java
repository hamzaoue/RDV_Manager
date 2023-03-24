package com.example.rdvmanager.fragments;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import androidx.core.content.ContextCompat;
import android.widget.LinearLayout;
import android.widget.ImageButton;
import com.example.rdvmanager.R;
import android.widget.EditText;
import android.app.AlertDialog;
import android.content.Context;
import android.view.ViewGroup;
/********************/
public class CustomDialog extends AlertDialog
{
    private final ImageButton aImageButton;
    private final EditText aEditText;
    /********************/
    protected CustomDialog(Context context, int title_id, int icon_id)
    {
        super(context,R.style.MyDialogTheme);
        this.setTitle(title_id);
        this.aImageButton = this.createImageButton(context , icon_id);
        this.aEditText = this.createEditText(context);
        this.setView(this.createLayout(context));
        this.setButton(BUTTON_NEGATIVE,context.getString(R.string.cancel),(dialog,which)->hide());
    }
    /********************/
    public ImageButton getImageButton(){return this.aImageButton;}
    public EditText getEditText(){return this.aEditText;}
    /********************/
    private ImageButton createImageButton(Context context, int icon_id)
    {
        ImageButton imageButton = new ImageButton(context);
        imageButton.setBackground(ContextCompat.getDrawable(context, R.drawable.button_bg));
        imageButton.setImageResource(icon_id);
        return imageButton;
    }
    /********************/
    private EditText createEditText(Context context)
    {
        EditText editText = new EditText(context);
        editText.setBackground(ContextCompat.getDrawable(context, R.drawable.edit_text_border));
        editText.setTextColor(ContextCompat.getColor(context, R.color.white));
        editText.setPadding(10, 0,10, 0);
        return editText;
    }
    /********************/
    private LinearLayout createLayout(Context context)
    {
        LinearLayout layout = new LinearLayout(context);
        layout.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
        layout.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        layout.addView(this.aImageButton,new ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        layout.addView(this.aEditText , new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        layout.setOrientation(LinearLayout.HORIZONTAL);
        return layout;
    }
}
