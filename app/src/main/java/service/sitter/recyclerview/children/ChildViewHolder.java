package service.sitter.recyclerview.children;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import service.sitter.R;

public class ChildViewHolder extends RecyclerView.ViewHolder {
    public final View rootView;
    private TextView nameTextView;
    private TextView ageTextView;
    private ImageButton imageChildButtonView;
    private boolean isSelected;

    public ChildViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        // rootView is used for click on the whole print (not specific button)
        handleShowView(itemView);
        rootView = itemView;
        nameTextView = itemView.findViewById(R.id.itemChildName);
        ageTextView = itemView.findViewById(R.id.itemChildAge);
        imageChildButtonView = itemView.findViewById(R.id.item_child_image_button);

        imageChildButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelected = !isSelected;
                imageChildButtonView.setBackgroundColor(isSelected ? Color.GREEN : Color.TRANSPARENT);
            }
        });
    }


    public View getRootView() {
        return rootView;
    }

    public TextView getNameTextView() {
        return nameTextView;
    }

    public TextView getAgeTextView() {
        return ageTextView;
    }

    private void handleShowView(View view) {
        if (getAdapterPosition() > 2) {
            view.setVisibility(View.GONE);
            return;
        }
        view.setVisibility(View.VISIBLE);
    }

    public ImageButton getImageView() {
        return imageChildButtonView;
    }

    public void setImageView(ImageButton imageView) {
        this.imageChildButtonView = imageView;
    }
}
