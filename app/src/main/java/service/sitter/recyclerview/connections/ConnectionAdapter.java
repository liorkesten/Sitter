package service.sitter.recyclerview.connections;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import service.sitter.R;
import service.sitter.db.DataBase;
import service.sitter.models.Connection;
import service.sitter.utils.ImagesUtils;

public class ConnectionAdapter extends RecyclerView.Adapter<service.sitter.recyclerview.connections.ConnectionViewHolder> {
    private final List<Connection> connections = new ArrayList<>();
    private final IConnectionAdapterListener listener;
    private final Context context;

    public ConnectionAdapter(@NonNull service.sitter.recyclerview.connections.IConnectionAdapterListener listener, Context context) {
        this.listener = listener;
        this.context = context;
    }

    public void setConnections(List<Connection> connections) {
        this.connections.clear();
        this.connections.addAll(connections);
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public ConnectionViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_connection, parent, false);
        return new service.sitter.recyclerview.connections.ConnectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull service.sitter.recyclerview.connections.ConnectionViewHolder holder, int position) {
        Connection connection = connections.get(position);
        holder.getImageView().setOnClickListener(l -> listener.onConnectionClick(connection));
        DataBase.getInstance().getBabysitter(connection.getSideBUId(), b -> {
            holder.getNameTextView().setText(b.getFullName());
            ImagesUtils.updateImageView(this.context, b.getImage(), holder.getImageView());
        }, null);
    }

    @Override
    public int getItemCount() {
        return connections.size();
    }
}

