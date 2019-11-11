package com.cheweishi.android.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.cheweishi.android.adapter.CustomListAdapter;
import com.cheweishi.android.R;
import com.cheweishi.android.interfaces.InsuranceListener;
import com.cheweishi.android.utils.LogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangce on 4/18/2016.
 */
public class CustomCheckDialog extends Dialog {
    public CustomCheckDialog(Context context) {
        this(context, 0);
    }

    public CustomCheckDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected CustomCheckDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder implements View.OnClickListener {
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


        private Button confirm;
        private Button cancel;
        private CustomCheckDialog dialog;
        private InsuranceListener listener;
        private RadioGroup radioGroup;
        private LinearLayout dialog_bottomLayout;
        private ListView lv_insurance;
        private CustomListAdapter adapter;

        private int contentFlag = 0x10;

        public static final int YES_NO = 0x10;

        public static final int SEX = 0x11;

        public static final int LIST = 0x12;

        private String[] yesOrNo = new String[]{"否  ", "是  "};

        private String[] sex = new String[]{"小姐  ", "先生  "};

        private int viewId;

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

        public Builder setMyOnClickListener(InsuranceListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder setContentFlag(int id, int value) {
            this.viewId = id;
            this.contentFlag = value;
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

        public CustomCheckDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            dialog = new CustomCheckDialog(context,
                    R.style.Dialog);
            View layout = inflater.inflate(R.layout.insurance_check_view, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            confirm = (Button) layout.findViewById(R.id.positiveButton);
            cancel = (Button) layout.findViewById(R.id.negativeButton);
            cancel.setOnClickListener(this);
            confirm.setOnClickListener(this);
            // TODO Check的时候
            if (YES_NO == contentFlag) { // 是和否
                radioGroup = (RadioGroup) layout.findViewById(R.id.rg_insurance_yes_no);
                radioGroup.setVisibility(View.VISIBLE);
                ((RadioButton) radioGroup.getChildAt(1)).setChecked(true);
            } else if (SEX == contentFlag) {
                radioGroup = (RadioGroup) layout.findViewById(R.id.rg_insurance_sex);
                radioGroup.setVisibility(View.VISIBLE);
                ((RadioButton) radioGroup.getChildAt(0)).setChecked(true);
            } else if (LIST == contentFlag) {
                dialog_bottomLayout = (LinearLayout) layout.findViewById(R.id.dialog_bottomLayout);
                dialog_bottomLayout.setVisibility(View.GONE);
                lv_insurance = (ListView) layout.findViewById(R.id.lv_insurance);
                lv_insurance.setVisibility(View.VISIBLE);
                List<String> data = new ArrayList<>();
                for (int i = 0; i < 100; i++) {
                    data.add("i");
                }
                //TODO 开始填充数据
                adapter = new CustomListAdapter(data, context);
                lv_insurance.setAdapter(adapter);
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

        @Override
        public void onClick(View v) {

            int id = v.getId();
            switch (id) {
                case R.id.positiveButton: // 确认
                    if (null != listener) {
                        String data = "";
                        int checkedId = radioGroup.getCheckedRadioButtonId();
                        LogHelper.d("checkedId:" + checkedId);
                        if (-1 == checkedId) {
                            dismissDialog();
                            return;
                        }
                        if (YES_NO == contentFlag) {
                            data = yesOrNo[checkedId % 2];
                        } else if (SEX == contentFlag) {
                            data = sex[checkedId % 2];
                        }
                        listener.getResult(viewId, data);
                    }


                    dismissDialog();
                    break;
                case R.id.negativeButton: // 取消
                    dismissDialog();
                    break;
            }
        }


        private void dismissDialog() {
            if (null != dialog) {
                dialog.dismiss();
                dialog = null;
            }
        }
    }

}

