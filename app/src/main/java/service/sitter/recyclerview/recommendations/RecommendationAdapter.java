package service.sitter.recyclerview.recommendations;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import service.sitter.R;
import service.sitter.db.DataBase;
import service.sitter.models.Babysitter;
import service.sitter.models.Recommendation;
import service.sitter.ui.parent.connections.IOnGettingBabysitterFromDb;
import service.sitter.utils.ImagesUtils;

public class RecommendationAdapter extends RecyclerView.Adapter<service.sitter.recyclerview.recommendations.RecommendationViewHolder> {
    private final List<Recommendation> recommendations = new ArrayList<>();
    private final service.sitter.recyclerview.recommendations.IRecommendationAdapterListener listener;
    private final Context context;

    public RecommendationAdapter(@NonNull service.sitter.recyclerview.recommendations.IRecommendationAdapterListener listener, Context context) {
        this.listener = listener;
        this.context = context;
    }

    public void setRecommendations(List<Recommendation> recommendations) {
        this.recommendations.clear();
        this.recommendations.addAll(recommendations);
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public RecommendationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_recommendation, parent, false);
        return new RecommendationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendationViewHolder holder, int position) {
        Recommendation recommendation = recommendations.get(position);

        DataBase.getInstance().getBabysitter(recommendation.getConnection().getSideBUId(), new IOnGettingBabysitterFromDb() {
            @Override
            public void babysitterFound(Babysitter babysitter) {
                holder.getNameTextView().setText(babysitter.getFullName());
//            Glide.with(this.context).load(b.getImage()).into(holder.getImageView());
                ImagesUtils.updateImageView(context, babysitter.getImage(), holder.getImageView());
            }

            @Override
            public void onFailure(String phoneNumber) {
            }

        }, null);

        holder.getImageView().setOnClickListener(v -> {
            holder.getRootView().setVisibility(View.GONE);
            listener.onRequestClick(recommendation);
        });
    }

    @Override
    public int getItemCount() {
        return recommendations.size();
    }
}

