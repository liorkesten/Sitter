package service.sitter.recyclerview.recommendations;

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
import service.sitter.models.Recommendation;

public class RecommendationAdapter extends RecyclerView.Adapter<service.sitter.recyclerview.recommendations.RecommendationViewHolder> {
    private final List<Recommendation> recommendations = new ArrayList<>();
    private final service.sitter.recyclerview.recommendations.IRecommendationAdapterListener listener;

    public RecommendationAdapter(@NonNull service.sitter.recyclerview.recommendations.IRecommendationAdapterListener listener) {
        this.listener = listener;
    }

    public void setRecommendations(List<Recommendation> recommendations) {
        this.recommendations.clear();
        this.recommendations.addAll(recommendations);
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public service.sitter.recyclerview.recommendations.RecommendationViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_recommendation, parent, false);
        return new service.sitter.recyclerview.recommendations.RecommendationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull service.sitter.recyclerview.recommendations.RecommendationViewHolder holder, int position) {
        Recommendation recommendation = recommendations.get(position);
//        holder.getNameTextView().setText(connection.getName());
//        String age = Integer.toString(connection.getAge());
//        holder.getAgeTextView().setText(age);
        //TODO Delete this images - fetch from DB.
//        switch (connection.getImage()) {
//            case "Daria":
//                holder.getImageView().setImageResource(R.drawable.daria);
//                break;
//            case "Gali":
//                holder.getImageView().setImageResource(R.drawable.gali);
//                break;
//            case "Mika":
//                holder.getImageView().setImageResource(R.drawable.mika);
//                break;
//            default:
//                exit(120);
//        }

        holder.rootView.setOnClickListener(v -> listener.onRequestClick(recommendation));
    }

    @Override
    public int getItemCount() {
        return recommendations.size();
    }
}

