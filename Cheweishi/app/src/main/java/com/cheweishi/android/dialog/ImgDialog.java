package com.cheweishi.android.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.cheweishi.android.R;
import com.cheweishi.android.dialog.RechargeDialog.Builder;

public class ImgDialog extends Dialog {
    public static float moneyAccount;

    public ImgDialog(Context context) {
        super(context);
    }

    public ImgDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String title;
        private CharSequence message;
        private String positiveButtonText;
        private String negativeButtonText;
        private View contentView;
        private boolean showEtFlag = false;
        private DialogInterface.OnClickListener positiveButtonClickListener;
        private DialogInterface.OnClickListener negativeButtonClickListener;
        private ImageButton ib_dialog;
        private View bottom_line;
        private boolean flag;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder(Context context, boolean flag) {
            this.context = context;
            this.flag = flag;
        }

        public Builder setMessage(CharSequence message) {
            this.message = message;
            return this;
        }

        /**
         * Set the Dialog message from resource
         *
         * @param title
         * @return
         */
        public Builder setMessage(int message) {
            this.message = context.getText(message);
            return this;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }


        public Builder setshowCoupon(boolean flag) {
            this.flag = flag;
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public void hideLayout() {
            // Toast.makeText(context, "xiasj", Toast.LENGTH_LONG).show();
            bottom_line.setVisibility(View.GONE);
        }

        public ImgDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final ImgDialog dialog = new ImgDialog(context,
                    R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_img, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            // set the dialog title
            // ((TextView) layout.findViewById(R.id.title)).setText(title);
            // set the confirm button
            ib_dialog = (ImageButton) layout.findViewById(R.id.ib_dialog);

            if (flag) {
                layout.findViewById(R.id.tv_dialog_img_desc).setVisibility(View.VISIBLE);
                ((ImageView) layout.findViewById(R.id.img_dialog)).setImageResource(R.drawable.b_coupon_reg_bind);
            }


            if (positiveButtonText != null) {
//				((Button) layout.findViewById(R.id.positiveButton))
//						.setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    layout.findViewById(R.id.ib_dialog)
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            }

            dialog.setContentView(layout);
            return dialog;
        }

        public boolean isShowEtFlag() {
            return showEtFlag;
        }

        public void setShowEtFlag(boolean showEtFlag) {
            this.showEtFlag = showEtFlag;
        }

    }
}
