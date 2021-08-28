package service.sitter.db;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import service.sitter.models.Request;

public class DataBase implements IDataBase {
    private static DataBase instance;
    private final Map<String, Request> requests = new HashMap<>();
    private final MutableLiveData<List<Request>> mutableRequestsLiveData = new MutableLiveData<>();
//    private FireBaseFireStore fireStore;

    private DataBase() {
        mutableRequestsLiveData.setValue(new ArrayList<>());
        // fireStore = FireBaseFireStore.getInstance();
        //TODO Add snapshotevent listener.
    }

    /**
     * Adds new request to the DB.
     *
     * @param newRequest is the request that should be added to the DB/
     */
    public void addRequest(Request newRequest) {
        requests.put(newRequest.getUUID(), newRequest);
        //FIXME Check if the values extraction below is working.
        mutableRequestsLiveData.setValue(new ArrayList(requests.values()));
    }

    /**
     * s
     * Delete Request from the database.
     *
     * @param requestToDelete the request that should be deleted from the DB
     */
    public void deleteRequest(Request requestToDelete) {
        requests.remove(requestToDelete.getUUID());
        mutableRequestsLiveData.setValue(new ArrayList<>(requests.values()));
        // fireStore.collection("requests").document(requestToDelete.getUUID()).delete();
    }

    /**
     * @return IDataBase
     */
    public static IDataBase getInstance() {
        if (instance == null) {
            instance = new DataBase();
        }
        return instance;
    }

    /**
     * @return liveData of requests.
     */
    public LiveData<List<Request>> getRequestsLiveData() {
        return mutableRequestsLiveData;
    }
}
