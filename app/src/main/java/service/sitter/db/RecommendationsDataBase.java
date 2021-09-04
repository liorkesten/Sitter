package service.sitter.db;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import service.sitter.models.Recommendation;

public class RecommendationsDataBase {
    private static final String TAG = RecommendationsDataBase.class.getSimpleName();
    private static final String COLLECTION_FIRESTORE_NAME = "recommendations";

    private final FirebaseFirestore firestore;
    private final Map<String, Recommendation> recommendations = new HashMap<>();
    private final MutableLiveData<List<Recommendation>> mutableLiveData = new MutableLiveData<>();

    /**
     * @param fireStore
     */
    public RecommendationsDataBase(@NonNull FirebaseFirestore fireStore) {
        this.firestore = fireStore;
    }

    /**
     * Add recommendations to data base
     *
     * @param recommendation - new recommendation
     * @return false in case that the recommendation is already exists in the db.
     */
    public boolean addRecommendation(@NonNull Recommendation recommendation) {
        String recommendationUuid = recommendation.getUuid();
        if (recommendations.containsKey(recommendationUuid)) {
            Log.e(TAG, String.format("recommendation already exist : <%s>", recommendationUuid));
            return false;
        }

        recommendations.put(recommendation.getUuid(), recommendation);
        firestore.collection(COLLECTION_FIRESTORE_NAME).document(recommendationUuid).set(recommendation);

        Log.d(TAG, String.format("recommendation was added successfully: <%s>", recommendationUuid));
        return true;
    }


    /**
     * Deletes recommendation from database.
     *
     * @param recommendationUuid to delete
     * @return false if the recommendation doe's not exist in the DB.
     */
    public boolean deleteRecommendation(String recommendationUuid) {
        if (!recommendations.containsKey(recommendationUuid)) {
            Log.e(TAG, String.format("recommendation that should be deleted is not exist : <%s>", recommendationUuid));
            return false;
        }

        recommendations.remove(recommendationUuid);
        firestore.collection(COLLECTION_FIRESTORE_NAME).document(recommendationUuid).delete();

        Log.d(TAG, String.format("recommendation was deleted successfully: <%s>", recommendationUuid));
        return true;
    }

}
