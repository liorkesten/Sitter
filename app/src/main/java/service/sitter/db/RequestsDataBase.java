package service.sitter.db;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import service.sitter.models.Request;

public class RequestsDataBase {
    private static final String TAG = RequestsDataBase.class.getSimpleName();
    private static final String COLLECTION_FIRESTORE_NAME = "requests";

    private final FirebaseFirestore firestore;
    private final Map<String, Request> Requests = new HashMap<>();
    private final MutableLiveData<List<Request>> mutableLiveData = new MutableLiveData<>();

    /**
     * @param fireStore
     */
    public RequestsDataBase(@NonNull FirebaseFirestore fireStore) {
        this.firestore = fireStore;
    }

    /**
     * Add Requests to data base
     *
     * @param request - new request
     * @return false in case that the request is already exists in the db.
     */
    public boolean addRequest(@NonNull Request request) {
        String requestUuid = request.getUuid();
        if (Requests.containsKey(requestUuid)) {
            Log.e(TAG, String.format("request already exist : <%s>", requestUuid));
            return false;
        }

        Requests.put(request.getUuid(), request);
        firestore.collection(COLLECTION_FIRESTORE_NAME).document(requestUuid).set(request);

        Log.d(TAG, String.format("request was added successfully: <%s>", requestUuid));
        return true;
    }


    /**
     * Deletes request from database.
     *
     * @param requestUuid to delete
     * @return false if the request doe's not exist in the DB.
     */
    public boolean deleteRequest(String requestUuid) {
        if (!Requests.containsKey(requestUuid)) {
            Log.e(TAG, String.format("request that should be deleted is not exist : <%s>", requestUuid));
            return false;
        }

        Requests.remove(requestUuid);
        firestore.collection(COLLECTION_FIRESTORE_NAME).document(requestUuid).delete();

        Log.d(TAG, String.format("request was deleted successfully: <%s>", requestUuid));
        return true;
    }

}
