package service.sitter.recyclerview.children;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import service.sitter.R;
import service.sitter.models.Child;

public class ChildAdapter extends RecyclerView.Adapter<ChildViewHolder> {

    private static final String TAG = ChildAdapter.class.getSimpleName();


    private final List<Child> children = new ArrayList<>();
    private final IChildAdapterListener listener;
    private final boolean shouldFetchImages;
    private final Context context;
    private final boolean shouldApplyAlpha;

    public ChildAdapter(@NonNull IChildAdapterListener listener, boolean shouldFetchImages, Context context, boolean shouldApplyAlpha) {
        this.listener = listener;
        this.shouldFetchImages = shouldFetchImages;
        this.context = context;
        this.shouldApplyAlpha = shouldApplyAlpha;
    }

    /**
     * set new requests.
     *
     * @param children are list of new children that the adapter should load.
     */
    public void setChildren(List<Child> children) {
        Log.d(TAG, "Get new children array:" + children.toString());
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
    public void onBindViewHolder(@NonNull ChildViewHolder holder, int position) {
        Child child = children.get(position);
        holder.getNameTextView().setText(child.getName());
        String age = Integer.toString(child.getAge());
        holder.getAgeTextView().setText(age);
        if (shouldFetchImages) {
            Log.d(TAG, "shouldFetchImages is true");
            Glide.with(context).load(child.getImage()).into(holder.getImageView());
        } else {
            Log.d(TAG, "shouldFetchImages is false");
            holder.getImageView().setImageURI(Uri.parse(child.getImage()));
        }
        // Set image button:
        ImageButton imageView = holder.getImageView();
        if (!shouldApplyAlpha) {
            imageView.setAlpha(1f);
        }

        holder.getImageView().setOnClickListener(v -> {
            if (shouldApplyAlpha) {
                if (!imageView.isSelected()) {
                    imageView.setAlpha(0.80f);
                    imageView.setSelected(true);
                } else {
                    imageView.setAlpha(0.35f);
                    imageView.setSelected(false);
                }
            }
            listener.onRequestClick(child);
        });
    }

    @Override
    public int getItemCount() {
        return children.size();
    }
}

