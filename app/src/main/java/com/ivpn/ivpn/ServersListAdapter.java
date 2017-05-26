package com.ivpn.ivpn;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ivpn.ivpn.network.Server;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter for list of servers
 */
public class ServersListAdapter extends RecyclerView.Adapter<ServerViewHolder> {

    private Context mContext;
    private ArrayList<Server> mServers;
    private OnItemSelectedListener mListener;

    public ServersListAdapter(Context context, OnItemSelectedListener listener) {
        mContext = context;
        mServers = ApplicationSingleton.getInstance().getServers();
        mListener = listener;
    }

    @Override
    public ServerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.servers_list_item, parent, false);
        return new ServerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ServerViewHolder holder, int position) {
        final Server server = mServers.get(position);
        holder.text.setText(mContext.getString(R.string.list_text, server.city, server.countryCode));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemSelected(server);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mServers.size();
    }

    /**
     * interface to listen list callbacks
     */
    public interface OnItemSelectedListener {

        /**
         * calls when list item selected
         *
         * @param server selected server
         */
        void onItemSelected(Server server);
    }
}

class ServerViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.text)
    TextView text;

    ServerViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
