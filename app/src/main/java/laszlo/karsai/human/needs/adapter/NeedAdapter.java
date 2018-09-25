package laszlo.karsai.human.needs.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import laszlo.karsai.human.needs.R;

public class NeedAdapter extends RecyclerView.Adapter<NeedAdapter.NeedViewHolder>{

    private Context mContext;
    private String[] mHeaders;
    private String[] mValues;

    public NeedAdapter(Context context, String[] headers, String[] values) {
        this.mContext = context;
        this.mHeaders = headers;
        this.mValues = values;
    }

    @NonNull
    @Override
    public NeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NeedViewHolder(
                LayoutInflater.from(mContext)
                    .inflate(R.layout.list_item_need, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NeedViewHolder holder, int position) {
        holder.header.setText(mHeaders[position]);
        holder.value.setText(mValues[position]);
    }

    @Override
    public int getItemCount() {
        return mHeaders.length;
    }

    class NeedViewHolder extends RecyclerView.ViewHolder {

        TextView header;
        TextView value;

        NeedViewHolder(View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.tv_need_header);
            value = itemView.findViewById(R.id.tv_need_value);
        }
    }
}
