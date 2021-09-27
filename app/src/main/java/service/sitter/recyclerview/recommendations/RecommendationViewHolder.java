package service.sitter.recyclerview.recommendations;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import service.sitter.R;

public class RecommendationViewHolder extends RecyclerView.ViewHolder {
    public final View rootView;
    private TextView nameTextView;
    private TextView ageTextView;
    private ImageButton imageChildButtonView;
    private boolean isSelected;

    public RecommendationViewHolder(@NonNull View itemView) {
        super(itemView);
        // rootView is used for click on the whole print (not specific button)
//        handleShowView(itemView);
        rootView = itemView;
        nameTextView = itemView.findViewById(R.id.item_recommendation_name);
        imageChildButtonView = itemView.findViewById(R.id.item_recommendation_image_button);
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

//    private void handleShowView(View view) {
//        if (getAdapterPosition() > 2) {
//            view.setVisibility(View.GONE);
//            return;
//        }
//        view.setVisibility(View.VISIBLE);
//    }

    public ImageButton getImageView() {
        return imageChildButtonView;
    }

    public void setImageView(ImageButton imageView) {
        this.imageChildButtonView = imageView;
    }
}
