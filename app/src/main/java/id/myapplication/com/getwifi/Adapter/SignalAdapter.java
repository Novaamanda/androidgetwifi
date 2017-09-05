package id.myapplication.com.getwifi.Adapter;

import android.content.Context;
import android.icu.text.DecimalFormat;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import id.myapplication.com.getwifi.utils.DistanceUtil;

/**
 * Created by Manda on 11/08/2017.
 */

public class SignalAdapter extends ArrayAdapter<ScanResult> {
    private static final DecimalFormat df;
    private Context context;
    private List<ScanResult> data;
    private int layout;

    public class ViewHolder {
        private TextView distance;
        private TextView mac;
        private TextView mode;
        private ScanResult signal;
        private TextView ssid;

        public ScanResult getSignal() {
            return this.signal;
        }
    }

    static {
        df = new DecimalFormat("#.##");
    }

    public SignalAdapter(List<ScanResult> data, int layout, Context context) {
        super(context, layout, data);
        this.data = null;
        this.context = context;
        this.data = data;
        this.layout = layout;
    }

    public int getCount() {
        return this.data.size();
    }

    public ScanResult getItem(int i) {
        return (ScanResult) this.data.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View v = convertView;
        if (v == null || v.getTag() == null) {
            v = ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(this.layout, null);
            holder = new ViewHolder();
//                holder.ssid = (TextView) v.findViewById(C0369R.id.lblSSID);
//                holder.mac = (TextView) v.findViewById(C0369R.id.lblMac);
//                holder.mode = (TextView) v.findViewById(C0369R.id.lblMode);
//                holder.distance = (TextView) v.findViewById(C0369R.id.lblDistance);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        holder.signal = getItem(position);
//        holder.ssid.setText(holder.signal.SSID);
//        holder.mac.setText(holder.signal.BSSID);
//        holder.mode.setText(holder.signal.capabilities);
        double distance = DistanceUtil.calculateDistance((double) holder.signal.level,
                (double) holder.signal.frequency);
//        holder.distance.setText(calculateDistanceStr(distance));
//            if (distance < 30.0d) {
//                holder.distance.setTextColor(this.context.getResources().getColor(C0369R.color.text_green));
//            } else if (distance < 80.0d) {
//                holder.distance.setTextColor(this.context.getResources().getColor(C0369R.color.text_orange));
//            } else {
//                holder.distance.setTextColor(this.context.getResources().getColor(C0369R.color.text_red));
//            }
        v.setTag(holder);
        return v;
    }

    private String calculateDistanceStr(double distance) {
        return df.format(distance) + "m";
    }
}
