package service.sitter.db;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import service.sitter.models.Babysitter;
import service.sitter.models.Request;
import service.sitter.models.RequestStatus;

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
//        if (!Requests.containsKey(requestUuid)) {
//            Log.e(TAG, String.format("request that should be deleted is not exist : <%s>", requestUuid));
//            return false;
//        }
//        Requests.remove(requestUuid);
        firestore.collection(COLLECTION_FIRESTORE_NAME).document(requestUuid).delete();

        Log.d(TAG, String.format("request was deleted successfully: <%s>", requestUuid));
        return true;
    }

    public LiveData<List<Request>> getLiveDataPendingRequestsOfParent(String parentId) {
        return getLiveDataRequestsOfParent(parentId, RequestStatus.Pending);
    }

    public LiveData<List<Request>> getLiveDataApprovedRequestsOfParent(String parentId) {
        return getLiveDataRequestsOfParent(parentId, RequestStatus.Approved);
    }

    public LiveData<List<Request>> getLiveDataArchivedRequestsOfParent(String parentId) {
        return getLiveDataRequestsOfParent(parentId, RequestStatus.Archived);
    }

    public LiveData<List<Request>> getLiveDataDeletedRequestsOfParent(String parentId) {
        return getLiveDataRequestsOfParent(parentId, RequestStatus.Deleted);
    }

    private LiveData<List<Request>> getLiveDataRequestsOfParent(String parentId, RequestStatus status) {
        MutableLiveData<List<Request>> liveDataRequests = new MutableLiveData<>();
        firestore
                .collection(COLLECTION_FIRESTORE_NAME)
                .whereEqualTo("publisherId", parentId)
                .whereEqualTo("status", status.toString())
                .addSnapshotListener((value, err) -> {
                    if (err != null) {
                        Log.e(TAG, String.format("Failed to extract requests of parent <%s>, with status <%s> due to: %s", parentId, status, err));
                    } else if (value == null) {
                        Log.e(TAG, String.format("Failed to extract requests of parent <%s>, with status <%s> due to: value is null", parentId, status));
                    } else {
                        List<Request> requests = new ArrayList<>();
                        List<DocumentSnapshot> documentSnapshots = value.getDocuments();
                        documentSnapshots.forEach(doc -> requests.add(doc.toObject(Request.class)));
                        liveDataRequests.setValue(requests);
                        Log.d(TAG, "All requests extracted successfully");
                    }
                });
        return liveDataRequests;
    }

    public void acceptRequestByBabysitter(Request r, Babysitter babysitter) {
        // Update request - change status and receiver id.
        r.setStatus(RequestStatus.Approved);
        r.setReceiverId(babysitter.getUuid());
        // Update database
        firestore.collection(COLLECTION_FIRESTORE_NAME).document(r.getUuid()).set(r);
    }

//    public LiveData<List<Request>> getLiveDataPendingRequestsOfBabysitter(String babysitterUUID) {
//        return getLiveDataRequestsOfBabysitter(babysitterUUID, RequestStatus.Pending);
//    }
//
//    private LiveData<List<Request>> getLiveDataRequestsOfBabysitter(String babysitterUUID, RequestStatus status) {
////        MutableLiveData<List<Request>> liveDataRequests = new MutableLiveData<>();
////        firestore
////                .collection(COLLECTION_FIRESTORE_NAME)
////                .whereEqualTo("publisherId", babysitterUUID)
////                .whereEqualTo("status", status.toString())
////                .addSnapshotListener((value, err) -> {
////                    if (err != null) {
////                        Log.e(TAG, String.format("Failed to extract requests of babysitter <%s>, with status <%s> due to: %s", babysitterUUID, status, err));
////                    } else if (value == null) {
////                        Log.e(TAG, String.format("Failed to extract requests of babysitter <%s>, with status <%s> due to: value is null", babysitterUUID, status));
////                    } else {
////                        List<Request> requests = new ArrayList<>();
////                        List<DocumentSnapshot> documentSnapshots = value.getDocuments();
////                        documentSnapshots.forEach(doc -> requests.add(doc.toObject(Request.class)));
////                        liveDataRequests.setValue(requests);
////                        Log.d(TAG, "All requests extracted successfully");
////                    }
////                });
////        return liveDataRequests;
//    }

    public void getRequestsByParentsId(List<String> parentsUUID, IApplyOnRequests applier, RequestStatus requestStatus) {
        firestore
                .collection(COLLECTION_FIRESTORE_NAME)
                .whereIn("publisherId", parentsUUID)
                .whereEqualTo("status", String.valueOf(requestStatus))
                .addSnapshotListener((requests, err) -> {
                    if (err != null || requests == null) {
                        Log.e(TAG, "Err on snapshot");
                        return;
                    }
                    List<Request> parents = requests.toObjects(Request.class);
                    applier.apply(parents);

                });

    }

    public void cancelRequest(Request r, Babysitter babysitter) {
        //TODO add checks that the babysitter is the receiver in the request
        r.setStatus(RequestStatus.Pending);
        r.setReceiverId("");
        // Update database
        firestore.collection(COLLECTION_FIRESTORE_NAME).document(r.getUuid()).set(r);
    }
}
