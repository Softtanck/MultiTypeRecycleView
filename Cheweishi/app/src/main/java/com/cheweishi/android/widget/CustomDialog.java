package com.cheweishi.android.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cheweishi.android.R;

public class CustomDialog extends Dialog {

    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int theme) {
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
        private EditText et;
        private View bottom_line;
        private boolean flag;

        private int gravity;

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

        public Builder setMessage(CharSequence message, int gravity) {
            this.message = message;
            this.gravity = gravity;
            return this;
        }

        /**
         * Set the Dialog message from resource
         *
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
            bottom_line.setVisibility(View.GONE);
        }

        public CustomDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final CustomDialog dialog = new CustomDialog(context,
                    R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_normal_layout, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            // set the dialog title
            ((TextView) layout.findViewById(R.id.title)).setText(title);
            // set the confirm button
            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.positiveButton))
                        .setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    layout.findViewById(R.id.positiveButton)
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.positiveButton).setVisibility(
                        View.GONE);
            }
            // set the cancel button
            if (negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.negativeButton))
                        .setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    layout.findViewById(R.id.negativeButton)
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    negativeButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.negativeButton).setVisibility(
                        View.GONE);
            }
            // set the content message
            if (message != null) {
                TextView textView = ((TextView) layout.findViewById(R.id.message));
                if (0 < gravity) {
                    textView.setGravity(Gravity.LEFT);
                    textView.setText(message);
                } else {
                    textView.setGravity(Gravity.CENTER);
                    textView.setText(message);
                }
            } else if (contentView != null) {
                // if no message set
                // add the contentView to the dialog body
                ((LinearLayout) layout.findViewById(R.id.content))
                        .removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content)).addView(
                        contentView, new LayoutParams(LayoutParams.FILL_PARENT,
                                LayoutParams.FILL_PARENT));
            }
            if (showEtFlag == true) {
                layout.findViewById(R.id.message)
                        .setVisibility(View.GONE);
                layout.findViewById(R.id.et_message)
                        .setVisibility(View.VISIBLE);
                setEt(((EditText) layout.findViewById(R.id.et_message)));
            } else {
                layout.findViewById(R.id.message)
                        .setVisibility(View.VISIBLE);
                layout.findViewById(R.id.et_message)
                        .setVisibility(View.GONE);
            }

            bottom_line = layout.findViewById(R.id.dialog_bottomLayout);
            if (flag == true) {
                hideLayout();
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

        public EditText getEt() {
            return et;
        }

        public void setEt(EditText et) {
            this.et = et;
        }

    }
}
