package service.sitter.db;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import service.sitter.models.Recommendation;

public class RecommendationsDataBase {
    private static final String TAG = RecommendationsDataBase.class.getSimpleName();
    private static final String COLLECTION_FIRESTORE_NAME = "recommendations";

    private final FirebaseFirestore firestore;

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

        firestore
                .collection(COLLECTION_FIRESTORE_NAME)
                .document(recommendationUuid)
                .set(recommendation);

        Log.d(TAG, String.format("recommendation was added successfully: <%s>", recommendationUuid));
        return true;
    }

    public void addRecommendations(@NonNull List<Recommendation> recommendations) {
        if (recommendations == null || recommendations.size() == 0) {
            Log.d(TAG, "recommendations is null or empty: " + recommendations);
            return;
        }
        Log.d(TAG, "Adding new recommendations: " + recommendations);
        Map<String, Recommendation> idToRec = recommendations
                .stream()
                .collect(Collectors.toMap(Recommendation::getUuid, item -> item));

        for (Recommendation recommendation : recommendations) {
            firestore.collection(COLLECTION_FIRESTORE_NAME).document(recommendation.getUuid()).set(recommendation);
        }
//        firestore
//                .collection(COLLECTION_FIRESTORE_NAME)
//                .add(idToRec);

        Log.d(TAG, String.format("recommendations was added successfully: <%s>", recommendations));
    }

    /**
     * Deletes recommendation from database.
     *
     * @param recommendationUuid to delete
     * @return false if the recommendation doe's not exist in the DB.
     */
    public boolean deleteRecommendation(String recommendationUuid) {

        firestore
                .collection(COLLECTION_FIRESTORE_NAME)
                .document(recommendationUuid)
                .delete();

        Log.d(TAG, String.format("recommendation was deleted successfully: <%s>", recommendationUuid));
        return true;
    }

    public LiveData<List<Recommendation>> getLiveDataRecommendationsOfParent(String parentUuid) {
        Log.d(TAG, "Listening. parentUuid:" + parentUuid);
        MutableLiveData<List<Recommendation>> liveDataRequests = new MutableLiveData<>();
        firestore
                .collection(COLLECTION_FIRESTORE_NAME)
                .whereEqualTo(FieldPath.of("connection", "sideAUId"), parentUuid)
//                .whereEqualTo("isUsed", false)
                .addSnapshotListener((value, err) -> {
                    Log.d(TAG, "Listening inside the snapshot");
                    if (err != null) {
                        Log.e(TAG, String.format("Failed to extract recommendations of parent <%s>,  due to: %s", parentUuid, err));
                    } else if (value == null) {
                        Log.e(TAG, String.format("Failed to extract recommendations of parent <%s>, due to: value is null", parentUuid));
                    } else {
                        if (value.size() == 0) {
                            Log.d(TAG, String.format("No results for parent: <%s>", parentUuid));
                            return;
                        }
                        List<Recommendation> recommendations = new ArrayList<>();
                        List<DocumentSnapshot> documentSnapshots = value.getDocuments();
                        documentSnapshots.forEach(doc -> recommendations.add(doc.toObject(Recommendation.class)));
                        liveDataRequests.setValue(recommendations);
                        Log.d(TAG, "All recommendations extracted successfully" + recommendations);
                    }
                });
        return liveDataRequests;
    }
}
