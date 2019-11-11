package com.cheweishi.android.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.adapter.BuyCartListAdapter;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.entity.BuyCartListResponse;
import com.cheweishi.android.entity.ShopPayOrderNative;
import com.cheweishi.android.response.BaseResponse;
import com.cheweishi.android.tools.EmptyTools;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.widget.CustomDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tangce on 8/23/2016.
 */
public class BuyCartActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2, BuyCartListAdapter.onCheckListener, AdapterView.OnItemClickListener {

    @ViewInject(R.id.plr_buy_cart)
    private PullToRefreshListView plr_buy_cart;

    @ViewInject(R.id.left_action)
    private Button left_action;//左边

    @ViewInject(R.id.title)
    private TextView title;//标题

    @ViewInject(R.id.right_action)
    private TextView right_action;//右边

    @ViewInject(R.id.iv_bc_bottom_check)
    private ImageView iv_bc_bottom_check;//底部选择

    @ViewInject(R.id.tv_bc_bottom_money)
    private TextView tv_bc_bottom_money;//合计的价钱

    @ViewInject(R.id.tv_bc_bottom_buy)
    private TextView tv_bc_bottom_buy;//去结算

    @ViewInject(R.id.tv_bc_bottom_total)
    private TextView tv_bc_bottom_total;//合计

    @ViewInject(R.id.tv_bc_bottom_left)
    private TextView tv_bc_bottom_left;//钱的符号

    private int page = 1;//当前页面

    private int total;//总共的数量

    private boolean isHeaderRefresh;//是否为下拉刷新

    private List<BuyCartListResponse.MsgBean> list = new ArrayList<>();

    private BuyCartListAdapter adapter;

    private boolean isCheck = false;

    private boolean isBottomCheck = true;

    private int mOprPosition = -1;//当前操作的位置

    private boolean isAdd;//是否为添加

    private boolean isRefresh = false;

    private PullToRefreshBase.Mode mCurrentMode = PullToRefreshBase.Mode.PULL_FROM_START;

    @Override
    protected void onResume() {
        super.onResume();
        if (!isRefresh) {
            isRefresh = true;
        } else {
            // 重新连接
            list.clear();
            sendPacket(1);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_cart);
        ViewUtils.inject(this);
        init();
    }

    private void init() {
        left_action.setText(getString(R.string.back));
        title.setText(getString(R.string.bug_cart));
        right_action.setText(getString(R.string.car_manager_edit));
        adapter = new BuyCartListAdapter(baseContext, list);
        adapter.setCheckListener(this);
        plr_buy_cart.setAdapter(adapter);
        plr_buy_cart.setOnRefreshListener(this);
        plr_buy_cart.setOnItemClickListener(this);
        plr_buy_cart.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        sendPacket(0);
    }

    @Override
    public void receive(String TAG, String data) {
        switch (TAG) {
            case NetInterface.LIST:
                BuyCartListResponse buyCartListRsp = (BuyCartListResponse) GsonUtil.getInstance().convertJsonStringToObject(data, BuyCartListResponse.class);
                if (!buyCartListRsp.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
                    ProgrosDialog.closeProgrosDialog();
                    showToast(buyCartListRsp.getDesc());
                    return;
                }

                List<BuyCartListResponse.MsgBean> temp = buyCartListRsp.getMsg();

                if (null != temp && 0 < temp.size()) {
                    total = buyCartListRsp.getPage().getTotal();
                    if (isHeaderRefresh) {
                        list = temp;
                    } else {
                        list.addAll(temp);
                    }
                    if (list.size() < total) {
                        plr_buy_cart.onRefreshComplete();
                        plr_buy_cart.setMode(PullToRefreshBase.Mode.BOTH);
                    } else {
                        plr_buy_cart.onRefreshComplete();
                        plr_buy_cart.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    }
                } else {
                    list = temp;
                    iv_bc_bottom_check.setImageResource(R.drawable.mess_yuan);
                    EmptyTools.setEmptyView(baseContext, plr_buy_cart);
                    EmptyTools.setImg(R.drawable.mycar_icon);
                    EmptyTools.setMessage("当前暂时没有任何商品,赶快去添加吧");
                    plr_buy_cart.onRefreshComplete();
                }
                adapter.setData(list);

                tv_bc_bottom_money.setText(calcMoney());
                loginResponse.setToken(buyCartListRsp.getToken());

                ProductDetailActivity.UpdateBuyCartNumber(0, getBuyCartNumber());
                ProductParamDetailActivity.UpdateBuyCartNumber(0, getBuyCartNumber());
                break;
            case NetInterface.ADD_AND_LESS: // 添加或者减少
                BaseResponse response = (BaseResponse) GsonUtil.getInstance().convertJsonStringToObject(data, BaseResponse.class);
                if (!response.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
                    ProgrosDialog.closeProgrosDialog();
                    showToast(response.getDesc());
                    return;
                }

                if (-1 != mOprPosition && null != list && 0 < list.size()) {
                    if (isAdd)
                        list.get(mOprPosition).setQuantity(String.valueOf(Integer.parseInt(list.get(mOprPosition).getQuantity()) + 1));
                    else
                        list.get(mOprPosition).setQuantity(String.valueOf(Integer.parseInt(list.get(mOprPosition).getQuantity()) - 1));
                    adapter.notifyDataSetChanged();
                }

                //计算价钱
                tv_bc_bottom_money.setText(calcMoney());
                loginResponse.setToken(response.getToken());

                ProductDetailActivity.UpdateBuyCartNumber(0, getBuyCartNumber());
                break;
            case NetInterface.DELETE://删除整个商品
                BaseResponse delete = (BaseResponse) GsonUtil.getInstance().convertJsonStringToObject(data, BaseResponse.class);
                if (!delete.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
                    ProgrosDialog.closeProgrosDialog();
                    showToast(delete.getDesc());
                    return;
                }

                isHeaderRefresh = true;
                page = 1;
                sendPacket(1);

                isCheck = false;
                right_action.setText(getString(R.string.car_manager_edit));
                iv_bc_bottom_check.setImageResource(R.drawable.mess_yuan_click);
                tv_bc_bottom_buy.setText(getString(R.string.buy_cart));
                tv_bc_bottom_total.setVisibility(View.VISIBLE);
                tv_bc_bottom_left.setVisibility(View.VISIBLE);
                tv_bc_bottom_money.setVisibility(View.VISIBLE);

                loginResponse.setToken(delete.getToken());
                break;
        }

        ProgrosDialog.closeProgrosDialog();
    }


    /**
     * 获取购物车列表
     *
     * @param type
     */
    private void sendPacket(int type) {
        if (0 == type)
            ProgrosDialog.openDialog(baseContext);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_SHOP_CART + NetInterface.LIST + NetInterface.SUFFIX;
        Map<String, Object> params = new HashMap<>();
        params.put("userId", loginResponse.getDesc());
        params.put("token", loginResponse.getToken());
        params.put("pageNumber", page);
        params.put("pageSize", 10);
        params.put(Constant.PARAMETER_TAG, NetInterface.LIST);
        netWorkHelper.PostJson(url, params, this);
    }

    /**
     * 删除或者增加
     *
     * @param itemId
     * @param isAdd
     */
    private void addPacket(int itemId, boolean isAdd) {
        ProgrosDialog.openDialog(baseContext);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_SHOP_CART + NetInterface.ADD_AND_LESS + NetInterface.SUFFIX;
        Map<String, Object> params = new HashMap<>();
        params.put("userId", loginResponse.getDesc());
        params.put("token", loginResponse.getToken());
        params.put("itemId", itemId);
        params.put("opr", isAdd ? 1 : -1);
        params.put(Constant.PARAMETER_TAG, NetInterface.ADD_AND_LESS);
        netWorkHelper.PostJson(url, params, this);
    }

    /**
     * 删除整类
     *
     * @param itemIds
     */
    private void deletePacket(int[] itemIds) {
        ProgrosDialog.openDialog(baseContext);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_SHOP_CART + NetInterface.DELETE + NetInterface.SUFFIX;
        Map<String, Object> params = new HashMap<>();
        params.put("userId", loginResponse.getDesc());
        params.put("token", loginResponse.getToken());
        params.put("itemIds", itemIds);
        params.put(Constant.PARAMETER_TAG, NetInterface.DELETE);
        netWorkHelper.PostJson(url, params, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        plr_buy_cart.onRefreshComplete();
    }

    @OnClick({R.id.left_action, R.id.right_action, R.id.iv_bc_bottom_check, R.id.tv_bc_bottom_buy})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_action:
                finish();
                break;
            case R.id.right_action:
                if (null == list || 0 == list.size())
                    return;
                if (!isCheck) { // 编辑
                    plr_buy_cart.setMode(PullToRefreshBase.Mode.DISABLED);
                    isCheck = true;
                    right_action.setText(getString(R.string.finish));
                    iv_bc_bottom_check.setImageResource(R.drawable.mess_yuan);
                    tv_bc_bottom_buy.setText(getString(R.string.item_del));
                    tv_bc_bottom_total.setVisibility(View.GONE);
                    tv_bc_bottom_left.setVisibility(View.GONE);
                    tv_bc_bottom_money.setVisibility(View.GONE);
                    clearCheck();
                } else {
                    if (list.size() < total) {
                        plr_buy_cart.setMode(PullToRefreshBase.Mode.BOTH);
                    } else {
                        plr_buy_cart.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    }
                    isCheck = false;
                    right_action.setText(getString(R.string.car_manager_edit));
                    iv_bc_bottom_check.setImageResource(R.drawable.mess_yuan_click);
                    tv_bc_bottom_buy.setText(getString(R.string.buy_cart));
                    tv_bc_bottom_total.setVisibility(View.VISIBLE);
                    tv_bc_bottom_left.setVisibility(View.VISIBLE);
                    tv_bc_bottom_money.setVisibility(View.VISIBLE);
                    setAllChecked();
                    tv_bc_bottom_money.setText(calcMoney());
                }
                break;
            case R.id.iv_bc_bottom_check: // 全选
                if (null == list || 0 == list.size())
                    return;
                if (isBottomCheck) {
                    isBottomCheck = false;
                    clearCheck();
                    iv_bc_bottom_check.setImageResource(R.drawable.mess_yuan);
                } else {
                    isBottomCheck = true;
                    setAllChecked();
                    iv_bc_bottom_check.setImageResource(R.drawable.mess_yuan_click);
                }
                tv_bc_bottom_money.setText(calcMoney());
                break;
            case R.id.tv_bc_bottom_buy: // 购买或者删除
                if (null == list || 0 == list.size())
                    return;
                else if (0 >= getSelectedNumber()) {// 没有任何选中
                    showToast(R.string.confirm_select);
                    return;
                }
                if (isCheck) {//删除
                    showNormalCustomDialog(getString(R.string.confirm_delete), getString(R.string.confirm));
                } else { // 结算
                    plr_buy_cart.onRefreshComplete();
                    Intent intent = new Intent(baseContext, ShopPayOrderActivity.class);
                    intent.putExtra("data", (Serializable) getPayList());
                    startActivity(intent);
                }
                break;
        }
    }

    private List<ShopPayOrderNative> getPayList() {
        if (null == list || 0 == list.size())
            return null;
        List<ShopPayOrderNative> temp = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isChecked()) {
                int tempMoney = 0;
                tempMoney = Integer.valueOf(list.get(i).getProduct().getPrice()) * Integer.valueOf(list.get(i).getQuantity());
                ShopPayOrderNative shopPayOrderNative = new ShopPayOrderNative();
                shopPayOrderNative.setIcon(list.get(i).getProduct().getImage());
                shopPayOrderNative.setMoney(String.valueOf(tempMoney));
                shopPayOrderNative.setName(list.get(i).getProduct().getFullName());
                shopPayOrderNative.setNumber(list.get(i).getQuantity());
                shopPayOrderNative.setId(list.get(i).getId());
                temp.add(shopPayOrderNative);
            }
        }
        return temp;
    }

    /**
     * 获取购物车数量
     *
     * @return
     */
    private int getBuyCartNumber() {
        if (null == list || 0 == list.size())
            return 0;
        int temp = 0;
        for (int i = 0; i < list.size(); i++) {
            temp += Integer.parseInt(list.get(i).getQuantity());
        }
        return temp;
    }

    /**
     * 获取未选中的集合
     *
     * @return
     */
    private List<BuyCartListResponse.MsgBean> getUnSelected() {
        if (null == list || 0 == list.size())
            return null;
        List<BuyCartListResponse.MsgBean> temp = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isChecked())
                temp.add(list.get(i));
        }
        return list;
    }

    /**
     * 获取当前选中的id集合
     *
     * @return
     */
    private int[] getSelectedIds() {
        if (null == list || 0 == list.size())
            return null;
        int[] ids = new int[getSelectedNumber()];
        int j = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isChecked()) {
                ids[j++] = list.get(i).getId();
            }
        }
        return ids;
    }


    /**
     * 获取当前选中的个数
     *
     * @return
     */
    private int getSelectedNumber() {
        int temp = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isChecked()) {
                temp++;
            }
        }
        return temp;
    }

    /**
     * 设置所有被选中
     */
    private void setAllChecked() {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setChecked(true);
        }
        adapter.notifyDataSetInvalidated();
    }

    /**
     * 清楚所有选中
     */
    private void clearCheck() {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setChecked(false);
        }
        adapter.notifyDataSetInvalidated();
    }

    /**
     * 是否所有都选中.
     *
     * @return
     */
    private boolean isAllChecked() {
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).isChecked())
                return false;
            continue;
        }
        return true;
    }


    private String calcMoney() {
        if (null == list && 0 == list.size())
            return "0";
        int tempPrice = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isChecked()) {
                tempPrice += Integer.valueOf(list.get(i).getProduct().getPrice()) * Integer.parseInt(list.get(i).getQuantity());
            }

        }
        return String.valueOf(tempPrice);
    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        isHeaderRefresh = true;
        page = 1;
        sendPacket(1);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        isHeaderRefresh = false;
        page++;
        sendPacket(1);
    }

    @Override
    public void onItemCheckClick(int position) {
        if (null == list || 0 == list.size())
            return;


        list.get(position).setChecked(!list.get(position).isChecked());

        if (!isAllChecked()) {
            isBottomCheck = false;
            iv_bc_bottom_check.setImageResource(R.drawable.mess_yuan);
        } else {
            isBottomCheck = true;
            iv_bc_bottom_check.setImageResource(R.drawable.mess_yuan_click);
        }

        tv_bc_bottom_money.setText(calcMoney());
    }

    @Override
    public void onItemAddClick(int position) {
        if (null == list || 0 == list.size())
            return;
        isAdd = true;
        mOprPosition = position;
        // TODO sendAddPacket
        addPacket(list.get(position).getId(), isAdd);
    }

    @Override
    public void onItemLessClick(int position) {
        if (null == list || 0 == list.size())
            return;
        else if (1 == Integer.parseInt(list.get(position).getQuantity())) {
            showToast(R.string.error_info_less);
            return;
        }
        isAdd = false;
        mOprPosition = position;
        // TODO sendLessPacket
        addPacket(list.get(position).getId(), isAdd);
    }

    private void showNormalCustomDialog(String msg, String buttonMsg) {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setMessage(msg);
        builder.setTitle(getString(R.string.remind));

        builder.setPositiveButton(buttonMsg,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        deletePacket(getSelectedIds());
                    }
                });
        builder.setNegativeButton(getString(R.string.cancel),
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EmptyTools.destory();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null == list || 0 == list.size())
            return;
        Intent shopDetail = new Intent(baseContext, ProductDetailActivity.class);
//        shopDetail.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        shopDetail.putExtra("productId", list.get(position - 1).getProduct().getId());
        startActivity(shopDetail);

    }
}
