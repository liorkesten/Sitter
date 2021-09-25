package service.sitter.recyclerview.recommendations;

import service.sitter.models.Recommendation;

public interface IRecommendationAdapterListener {
    void onRequestClick(Recommendation recommendation);
}
