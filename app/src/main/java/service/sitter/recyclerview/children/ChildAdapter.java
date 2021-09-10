package service.sitter.recyclerview.children;

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
import service.sitter.models.Child;

public class ChildAdapter extends RecyclerView.Adapter<ChildViewHolder> {
    private final List<Child> children = new ArrayList<>();
    private final IChildAdapterListener listener;

    public ChildAdapter(@NonNull IChildAdapterListener listener) {
        this.listener = listener;
    }

    /**
     * set new requests.
     *
     * @param children are list of new children that the adapter should load.
     */
    public void setChildren(List<Child> children) {
        this.children.clear();
        this.children.addAll(children);
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_child, parent, false);
        return new ChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ChildViewHolder holder, int position) {
        Child child = children.get(position);
        holder.getNameTextView().setText(child.getName());
//        holder.getAgeTextView().setText(child.getAge());

        // Adapter passes the rootView that was clicked. The activity should initialize the adapter with specific listener.
        holder.rootView.setOnClickListener(v -> listener.onRequestClick(child));
    }

    @Override
    public int getItemCount() {
        return children.size();
    }
}
