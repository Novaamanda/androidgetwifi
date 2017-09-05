package id.myapplication.com.getwifi.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import id.myapplication.com.getwifi.Model.DataWifi;
import id.myapplication.com.getwifi.R;
import id.myapplication.com.getwifi.utils.DistanceUtil;

/**
 * Created by Manda on 11/08/2017.
 */
public class SignalAdatperRV extends RecyclerView.Adapter<SignalAdatperRV.viewHoldernya> {
    private List<DataWifi> data;
    private Context konteks;

    public static class viewHoldernya extends RecyclerView.ViewHolder {
        TextView txtNama, txtJarak;

        viewHoldernya(View itemView) {
            super(itemView);
            txtNama = (TextView) itemView.findViewById(R.id.txt_nama);
            txtJarak = (TextView) itemView.findViewById(R.id.txt_jarak);
        }
    }

    // Konstruktor.
    public SignalAdatperRV(List data, Context konteks){
        this.data = data;
        this.konteks = konteks;
    }

    @Override
    public SignalAdatperRV.viewHoldernya onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_signal, parent, false);
        viewHoldernya vh = new viewHoldernya(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(SignalAdatperRV.viewHoldernya holder, final int position) {
        double distance = DistanceUtil.calculateDistance((double) data.get(position).getLevel(),
                (double) data.get(position).getFrekuensi());

        holder.txtNama.setText(data.get(position).getSSID());
        holder.txtJarak.setText(String.valueOf(distance));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setData(List<DataWifi> data){
        this.data = data;
    }
}
