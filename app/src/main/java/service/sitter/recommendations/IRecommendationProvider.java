package service.sitter.recommendations;

import android.content.Context;

public interface IRecommendationProvider {

    void getRecommendations(Context context, String userID);
}
