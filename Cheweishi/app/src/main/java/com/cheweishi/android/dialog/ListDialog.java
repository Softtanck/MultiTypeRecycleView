package com.cheweishi.android.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.adapter.AreaListDialogAdapter;
import com.cheweishi.android.entity.AreaListResponse;
import com.cheweishi.android.utils.StringUtil;
import com.cheweishi.android.widget.CustomDialog.Builder;

import java.util.List;

public class ListDialog extends Dialog {
    public static float moneyAccount;
    private OnItemClickListener onItemClickListener;

    public ListDialog(Context context) {
        super(context);
    }

    public ListDialog(Context context, int theme) {
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
        private OnItemClickListener onItemListener;
        private ListView lv_dialog;
        private View bottom_line;
        private boolean flag;
        private List<AreaListResponse.MsgBean> array;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder(Context context, List<AreaListResponse.MsgBean> array,
                       OnItemClickListener onItemClickListener) {
            this.context = context;
            this.onItemListener = onItemClickListener;
            this.array = array;
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

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public void hideLayout() {
            // Toast.makeText(context, "xiasj", Toast.LENGTH_LONG).show();
            bottom_line.setVisibility(View.GONE);
        }

        public ListDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final ListDialog dialog = new ListDialog(context, R.style.Dialog);

            View layout = inflater.inflate(R.layout.dialog_list, null);
            setLv_dialog((ListView) layout.findViewById(R.id.lv_dialog));

//			ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
//					R.layout.item_oil, array);
            AreaListDialogAdapter adapter = new AreaListDialogAdapter(context, array);
            if (!StringUtil.isEmpty(title))
                ((TextView) layout.findViewById(R.id.title)).setText(title);
            lv_dialog.setAdapter(adapter);
            lv_dialog.setOnItemClickListener(onItemListener);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            dialog.setContentView(layout);
            return dialog;
        }

        public boolean isShowEtFlag() {
            return showEtFlag;
        }

        public void setShowEtFlag(boolean showEtFlag) {
            this.showEtFlag = showEtFlag;
        }

        public ListView getLv_dialog() {
            return lv_dialog;
        }

        public void setLv_dialog(ListView lv_dialog) {
            this.lv_dialog = lv_dialog;
        }

        public Builder setItems(List<AreaListResponse.MsgBean> array,
                                OnItemClickListener onItemListener) {
            this.array = array;
            this.onItemListener = onItemListener;
            return this;
        }

    }

}
