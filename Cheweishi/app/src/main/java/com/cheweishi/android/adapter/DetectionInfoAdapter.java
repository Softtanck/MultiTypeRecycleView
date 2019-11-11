package com.cheweishi.android.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.entity.DTCInfoNative;
import com.cheweishi.android.entity.DetectionInfoNative;
import com.cheweishi.android.utils.DisplayUtil;

public class DetectionInfoAdapter extends BaseAdapter implements
        OnClickListener {
    private Context context;
    private int red;
    private int gray;
    private int grag_background;
    private int yellow;
    private int green;
    private int blue;
    private int gray_normal;
//	private AkeyTextAllInfo allInfo;
//	private List<AkeytestInfo> carStatus;// 车辆状态
//	private List<AkeytestInfo> maintances;// 养护状态
//	private List<AkeytestInfo> dtcs;// 故障码
//	private List<String> dtcNames;

    private List<DetectionInfoNative> detectionInfos;
    private List<DTCInfoNative> dtcInfos;

    private List<Map<String, Object>> data;

    /**
     * 检测详情适配器
     *
     * @param context
     */
    public DetectionInfoAdapter(Context context, List<DetectionInfoNative> detectionInfos, List<DTCInfoNative> dtcInfos) {
        this.context = context;
        this.detectionInfos = detectionInfos;
        this.dtcInfos = dtcInfos;
//		this.allInfo = allInfo;
//		carStatus = allInfo.getAkeyone();
//		maintances = allInfo.getAkeytwo();
//		dtcs = allInfo.getAkeythere();
        red = context.getResources().getColor(R.color.red_deep);
        gray = context.getResources().getColor(R.color.gray_normal);
        yellow = context.getResources().getColor(R.color.maintain_orange);
        green = context.getResources().getColor(R.color.green_deep);
        grag_background = context.getResources().getColor(
                R.color.gray_backgroud);
        gray_normal = context.getResources().getColor(R.color.gray_normal_6);
        blue = context.getResources().getColor(R.color.orange);
//		dtcNames = new ArrayList<String>();
//		paseDtcValue();

    }

    public DetectionInfoAdapter(Context context, List<Map<String, Object>> data) {
        this.context = context;
        this.data = data;
        red = context.getResources().getColor(R.color.red_deep);
        gray = context.getResources().getColor(R.color.gray_normal);
        yellow = context.getResources().getColor(R.color.maintain_orange);
        green = context.getResources().getColor(R.color.green_deep);
        grag_background = context.getResources().getColor(
                R.color.gray_backgroud);
        gray_normal = context.getResources().getColor(R.color.gray_normal_6);
        blue = context.getResources().getColor(R.color.orange);
    }



    @Override
    public int getCount() {
        int count = 0;
        count = data.size();
//        if (!StringUtil.isEmpty(detectionInfos) && !StringUtil.isEmpty(dtcInfos)) {
//            count = detectionInfos.size() + dtcInfos.size();
//            return count;
//        }
//
//        if (!StringUtil.isEmpty(detectionInfos) && StringUtil.isEmpty(dtcInfos)) {
//            count = detectionInfos.size();
//            return count;
//        }

        return count;
    }

    @Override
    public boolean isEnabled(int position) {
//        if (position > 18) {
//            return true;
//        } else {
//            return false;
//        }
        return true;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_detection_info, null);
            holder.rl_parent = (RelativeLayout) convertView
                    .findViewById(R.id.rl_parent);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_nowvalue = (TextView) convertView
                    .findViewById(R.id.tv_now_value);
            // holder.tv_edit = (TextView)
            // convertView.findViewById(R.id.tv_edit);
            holder.img_mark = (TextView) convertView
                    .findViewById(R.id.img_mark);
            holder.tv_hint = (TextView) convertView.findViewById(R.id.tv_hint);
//			holder.line = convertView.findViewById(R.id.line);

            holder.ll_edit = (LinearLayout) convertView
                    .findViewById(R.id.ll_edit);
            holder.ll_edit.setTag(position);
            holder.ll_edit.setOnClickListener(this);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.ll_edit.setTag(position);
        }
        holder.img_mark.setVisibility(View.GONE);

//		if (position == 0 || position == 14 || position == 17
//				|| position == getCount() - 1) {
//			ViewGroup.LayoutParams params = holder.line.getLayoutParams();
//			params.height = DisplayUtil.dip2px(context, 15f);
//			holder.line.setLayoutParams(params);
//		} else {
//			ViewGroup.LayoutParams params = holder.line.getLayoutParams();
//			params.height = DisplayUtil.dip2px(context, 0.5f);
//			holder.line.setLayoutParams(params);
//		}

        // 设置item宽度
//		if (position == 1 || position == 15 || position == 18
//				|| position == getCount()) {
//			holder.rl_parent.setPadding(DisplayUtil.dip2px(context, 10),
//					DisplayUtil.dip2px(context, 15),
//					DisplayUtil.dip2px(context, 10),
//					DisplayUtil.dip2px(context, 15));
//		} else {
        holder.rl_parent.setPadding(DisplayUtil.dip2px(context, 10),
                DisplayUtil.dip2px(context, 15),
                DisplayUtil.dip2px(context, 10),
                DisplayUtil.dip2px(context, 15));
//		}

        // 设置字体大小
//		if (position == 0 || position == 1 || position == 15 || position == 18) {
//			holder.tv_name.setTextSize(17);
//			holder.tv_nowvalue.setTextSize(17);
//		} else {
//			holder.tv_name.setTextSize(16);
//			holder.tv_nowvalue.setTextSize(16);
//		}

        // 分割线设置

        // 设置背景色
        // if (position == 1 || position == 15 || position == 18) {
        // holder.rl_parent.setBackgroundColor(grag_background);
        // } else {
        // holder.rl_parent.setBackgroundColor(white);
        // }
        setCarStatusData(position, holder);
        // 设置字体颜色 TODO 2016-4-6 17:27:01 直接显示而注释掉
//        switch (position) {
//            case 0:// 综合车况
////			holder.tv_name.setText(R.string.comprehensive);
////			holder.tv_nowvalue.setText(allInfo.getSynthesis());
////			setViewGone(holder);
////			holder.tv_name.setTextColor(blue);
////			if (StringUtil.isEquals(context.getText(R.string.youxiu) + "",
////					allInfo.getSynthesis(), true)
////					|| StringUtil.isEquals(context.getText(R.string.lianghao)
////							+ "", allInfo.getSynthesis(), true)) {
////				holder.tv_nowvalue.setTextColor(green);
////			} else if (StringUtil.isEquals(
////					context.getText(R.string.yiban) + "",
////					allInfo.getSynthesis(), true)) {
////				holder.tv_nowvalue.setTextColor(yellow);
////			} else {
////				holder.tv_nowvalue.setTextColor(red);
////			}
//                setNoContrast(position, holder); //总里程
//                break;
//            case 1:// 车辆状态
////			holder.tv_name.setText(R.string.vehicle_status);
////			setViewGone(holder);
////			holder.tv_name.setTextColor(blue);
////			int i = Integer.parseInt(allInfo.getCarState());
////			holder.tv_nowvalue
////					.setText(i + "" + context.getText(R.string.score));
////			if (i > 80) {
////				holder.tv_nowvalue.setTextColor(green);
////			} else if (i <= 80 && i >= 60) {
////				holder.tv_nowvalue.setTextColor(yellow);
////			} else if (i < 60) {
////				holder.tv_nowvalue.setTextColor(red);
////			}
//                setNoContrast(position, holder);//空燃比系数
//                break;
////		case 15:// 养护状态
////			holder.tv_name.setText(R.string.yanghu_status);
////			setViewGone(holder);
////			holder.tv_name.setTextColor(blue);
////			int j = Integer.parseInt(allInfo.getMaintainState());
////			holder.tv_nowvalue.setText(j + "分");
////			if (j > 80) {
////				holder.tv_nowvalue.setTextColor(green);
////			} else if (j <= 80 && j >= 60) {
////				holder.tv_nowvalue.setTextColor(yellow);
////			} else if (j < 60) {
////				holder.tv_nowvalue.setTextColor(red);
////			}
////			break;
////		case 18:// 故障检测
////			holder.tv_name.setText(R.string.fault_detect);
////			holder.ll_edit.setVisibility(View.GONE);
////			holder.tv_name.setTextColor(blue);
////			holder.img_mark.setVisibility(View.GONE);
////			holder.tv_nowvalue.setText(allInfo.getDtc());
////			if (dtcNames.size() > 0
////					&& !StringUtil.isEquals("--", dtcNames.get(0), true)) {
////				holder.tv_hint.setVisibility(View.VISIBLE);
////				holder.tv_nowvalue.setTextColor(red);
////			} else {
////				holder.tv_hint.setVisibility(View.GONE);
////				holder.tv_nowvalue.setTextColor(green);
////			}
////			break;
//            case 2:// --总里程
//                setCarStatusData(position, holder);//蓄电池电压
//                break;
//            case 3:// --空燃比系数
//                setCarStatusData(position, holder);//节气门开度
//                break;
//            case 4:// --蓄电池电压
//                setCarStatusData(position, holder);//发动机负荷
//                break;
//            case 5:// --节气门开度
//                setNoContrast(position, holder);//发动机运行时间
//                break;
//            case 6:// --发动机负荷
//                setNoContrast(position, holder);//百公里油耗
//                break;
//            case 7:// --发动机运行时间(S)holder.tv_name.setText(list.get(position).getName());
//                setNoContrast(position, holder);//剩余油量
//                break;
//            case 8:// --百公里油耗(L/km)
//                setCarStatusData(position, holder);//发动机转速
//                break;
//            case 9:// --剩余油量(L)
//                setCarStatusData(position, holder);//车速
//                break;
//            case 10:// --转速(rad/m)
//                setCarStatusData(position, holder);//环境温度
//                break;
//            case 11:// --车速(km/h)
//                setCarStatusData(position, holder);//水温
//                break;
//            case 12:// --环境温度(℃)
//                setNoContrast(position, holder);//obd时间
//                break;
////		case 13:// 水温(℃)
////			setCarStatusData(position, holder);
////			break;
////		case 14:// obd时间
////			setNoContrast(position, holder);
////			break;
////		case 16:// 距下次年审(天)
////			setMaintenanceData(position, holder);
////			break;
////		case 17:// 距上次保养(km)
////			setMaintenanceData(position, holder);
////			break;
//            default:
//                holder.tv_name.setText(R.string.fault_code);
//                setViewGone(holder);
//                holder.tv_nowvalue.setTextColor(gray_normal);
//                holder.tv_name.setTextColor(gray);
//                // if (dtcNames.size() == 0) {
//                // holder.tv_nowvalue.setText("--");
//                // }else {
//                if (StringUtil.isEmpty(dtcInfos.get(position - detectionInfos.size()).getName()) || StringUtil.isEquals("null", dtcInfos.get(position - detectionInfos.size()).getName(), true)) {
//                    holder.rl_parent.setVisibility(View.GONE);
////				holder.line.setVisibility(View.GONE);
//                } else {
//                    holder.tv_nowvalue.setText(dtcInfos.get(position - detectionInfos.size()).getName());
//
//                }
//                // }
//                break;
//        }

        // /**
        // * 保养状态设置
        // */
        // holder.tv_edit.setOnClickListener(new OnClickListener() {
        //
        // @Override
        // public void onClick(View view) {
        // if (mListener != null) {
        // mListener.onTextViewClick(position);
        // }
        // }
        // });
        return convertView;
    }

    /**
     * 隐藏view
     */
    private void setViewGone(ViewHolder holder) {
        holder.tv_hint.setVisibility(View.GONE);
        holder.ll_edit.setVisibility(View.GONE);
        holder.img_mark.setVisibility(View.GONE);
    }

    /**
     * 车辆状态需比较的数据处理
     *
     * @param position
     * @param holder
     */
    private void setCarStatusData(final int position, ViewHolder holder) {

        holder.tv_name.setText(String.valueOf(data.get(position).get("data")));

        // TODO 暂时不显示值
//        if (StringUtil.isEmpty(detectionInfos.get(position).getValue()) || StringUtil.isEquals("null", detectionInfos.get(position).getValue(), true)) {
//            holder.rl_parent.setVisibility(View.GONE);
////			holder.line.setVisibility(View.GONE);
//        } else {
//            holder.tv_nowvalue.setText(detectionInfos.get(position).getValue());
//        }
        holder.ll_edit.setVisibility(View.GONE);
        holder.tv_hint.setVisibility(View.GONE);
        // TODO 暂时不展示错误状态
//        // s = carStatus.get(position - 2).getValue();
//        if (detectionInfos.get(position).getFault().equals("1")) {
//            holder.tv_nowvalue.setTextColor(red);
//            holder.tv_name.setTextColor(red);
//            holder.img_mark.setVisibility(View.VISIBLE);
//        } else {
//            holder.tv_nowvalue.setTextColor(gray_normal);
//            holder.tv_name.setTextColor(gray);
//            holder.img_mark.setVisibility(View.GONE);
//        }

        holder.tv_nowvalue.setTextColor(gray_normal);
        holder.tv_name.setTextColor(gray);
        holder.img_mark.setVisibility(View.GONE);


        // TODO 旧版本
//        holder.tv_name.setText(detectionInfos.get(position).getName()
//                + detectionInfos.get(position).getUnit());
//        if (StringUtil.isEmpty(detectionInfos.get(position).getValue()) || StringUtil.isEquals("null", detectionInfos.get(position).getValue(), true)) {
//            holder.rl_parent.setVisibility(View.GONE);
////			holder.line.setVisibility(View.GONE);
//        } else {
//            holder.tv_nowvalue.setText(detectionInfos.get(position).getValue());
//        }
//        holder.ll_edit.setVisibility(View.GONE);
//        holder.tv_hint.setVisibility(View.GONE);
//        // s = carStatus.get(position - 2).getValue();
//        if (detectionInfos.get(position).getFault().equals("1")) {
//            holder.tv_nowvalue.setTextColor(red);
//            holder.tv_name.setTextColor(red);
//            holder.img_mark.setVisibility(View.VISIBLE);
//        } else {
//            holder.tv_nowvalue.setTextColor(gray_normal);
//            holder.tv_name.setTextColor(gray);
//            holder.img_mark.setVisibility(View.GONE);
//        }
    }

    /**
     * 养护状态数据处理
     *
     * @param position
     * @param holder
     */
//	private void setMaintenanceData(final int position, ViewHolder holder) {
//		holder.tv_name.setText(maintances.get(position - 16).getName()
//				+ maintances.get(position - 16).getUnit());
//		holder.tv_nowvalue.setText(maintances.get(position - 16).getValue());
//		holder.tv_hint.setVisibility(View.GONE);
//		if (maintances.get(position - 16).getFault().equals("1")) {
//			holder.ll_edit.setVisibility(View.VISIBLE);
//			holder.tv_nowvalue.setTextColor(red);
//			holder.tv_name.setTextColor(red);
//			holder.img_mark.setVisibility(View.VISIBLE);
//		} else {
//			holder.ll_edit.setVisibility(View.GONE);
//			holder.tv_nowvalue.setTextColor(gray_normal);
//			holder.tv_name.setTextColor(gray);
//			holder.img_mark.setVisibility(View.GONE);
//		}
//	}

    /**
     * 无需进行对比的数据填充
     *
     * @param position
     * @param holder
     */
    private void setNoContrast(final int position, ViewHolder holder) {
//		holder.tv_name.setText(carStatus.get(position - 2).getName()
//				+ carStatus.get(position - 2).getUnit());
//		holder.tv_nowvalue.setText(carStatus.get(position - 2).getValue());
//		setViewGone(holder);
//		holder.tv_nowvalue.setTextColor(gray_normal);
//		holder.tv_name.setTextColor(gray);

        holder.tv_name.setText(String.valueOf(data.get(position).get("data")));
        // TODO 暂时不暂时值
//        if (StringUtil.isEmpty(detectionInfos.get(position).getValue()) || StringUtil.isEquals("null", detectionInfos.get(position).getValue(), true)) {
//            holder.rl_parent.setVisibility(View.GONE);
////			holder.line.setVisibility(View.GONE);
//        } else {
//            holder.tv_nowvalue.setText(detectionInfos.get(position).getValue());
//        }
        setViewGone(holder);
        holder.tv_nowvalue.setTextColor(gray_normal);
        holder.tv_name.setTextColor(gray);

        // TODO 旧版本
//        holder.tv_name.setText(detectionInfos.get(position).getName()
//                + detectionInfos.get(position).getUnit());
//        if (StringUtil.isEmpty(detectionInfos.get(position).getValue()) || StringUtil.isEquals("null", detectionInfos.get(position).getValue(), true)) {
//            holder.rl_parent.setVisibility(View.GONE);
////			holder.line.setVisibility(View.GONE);
//        } else {
//            holder.tv_nowvalue.setText(detectionInfos.get(position).getValue());
//        }
//        setViewGone(holder);
//        holder.tv_nowvalue.setTextColor(gray_normal);
//        holder.tv_name.setTextColor(gray);
    }

    /**
     * 故障码处理
     */
//	private void paseDtcValue() {
//		try {
//			JSONArray jsonArray = new JSONArray(dtcs.get(0).getValue());
//			dtcNames.clear();
//			for (int i = 0; i < jsonArray.length(); i++) {
//				JSONObject object = new JSONObject();
//				object = jsonArray.getJSONObject(i);
//				dtcNames.add(object.getString("name"));
//			}
//			if (dtcNames == null || dtcNames.size() == 0) {
//				dtcNames.add("--");
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//	}

    class ViewHolder {
        private TextView tv_name;
        private TextView tv_nowvalue;
        private TextView tv_hint;
        private TextView img_mark;
        // private TextView tv_edit;
        private RelativeLayout rl_parent;
        //		private View line;
        private LinearLayout ll_edit;
    }

    /**
     * 设置点击监听
     */
    private OnViewClickListener mListener = null;

    public void setOnVIewClickListener(OnViewClickListener listener) {
        mListener = listener;
    }

    /**
     * 定义点击监听接口
     *
     * @author mingdasen
     */
    public interface OnViewClickListener {
        void onTextViewClick(int position);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_edit:
                if (mListener != null) {
                    mListener.onTextViewClick((Integer) view.getTag());
                }
                break;

            default:
                break;
        }
    }

}
