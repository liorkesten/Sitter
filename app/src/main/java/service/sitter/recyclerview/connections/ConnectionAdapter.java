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
import service.sitter.models.Connection;

public class ConnectionAdapter extends RecyclerView.Adapter<service.sitter.recyclerview.connections.ConnectionViewHolder> {
    private final List<Connection> connections = new ArrayList<>();
    private final service.sitter.recyclerview.connections.IConnectionAdapterListener listener;

    public ConnectionAdapter(@NonNull service.sitter.recyclerview.connections.IConnectionAdapterListener listener) {
        this.listener = listener;
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
        holder.getAgeTextView().setText(connection.getSideBUId());

        holder.rootView.setOnClickListener(v -> listener.onRequestClick(connection));
    }

    @Override
    public int getItemCount() {
        return connections.size();
    }
}

