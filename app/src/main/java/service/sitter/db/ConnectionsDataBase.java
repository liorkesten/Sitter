package service.sitter.db;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import service.sitter.models.Connection;
import service.sitter.models.Parent;
import service.sitter.models.User;
import service.sitter.recommendations.IGetConnections;

public class ConnectionsDataBase {
    private static final String TAG = ConnectionsDataBase.class.getSimpleName();
    public static final String COLLECTION_FIRESTORE_NAME = "connections";

    private final FirebaseFirestore firestore;
    private final Map<String, Connection> Connections = new HashMap<>();
    private final MutableLiveData<List<Connection>> mutableLiveData = new MutableLiveData<>();

    /**
     * @param fireStore
     */
    public ConnectionsDataBase(@NonNull FirebaseFirestore fireStore) {
        this.firestore = fireStore;
    }

    /**
     * Add Connections to data base
     *
     * @param connection - new Connection
     * @return false in case that the Connection is already exists in the db.
     */
    public boolean addConnection(@NonNull Connection connection) {
        String connectionUuid = connection.getUuid();
        if (Connections.containsKey(connection)) {
            Log.e(TAG, String.format("Connection already exist : <%s>", connectionUuid));
            return false;
        }

        Connections.put(connectionUuid, connection);
        firestore
                .collection(COLLECTION_FIRESTORE_NAME)
                .document(connectionUuid)
                .set(connection);

        Log.d(TAG, String.format("Connection was added successfully: <%s>", connectionUuid));
        return true;
    }


    /**
     * Deletes Connection from database.
     *
     * @param connectionUuid to delete
     * @return false if the Connection doe's not exist in the DB.
     */
    public boolean deleteConnection(String connectionUuid) {
//        if (!Connections.containsKey(connectionUuid)) {
//            Log.e(TAG, String.format("Connection that should be deleted is not exist : <%s>", connectionUuid));
//            return false;
//        }

//        Connections.remove(connectionUuid);
        firestore.collection(COLLECTION_FIRESTORE_NAME).document(connectionUuid).delete();

        Log.d(TAG, String.format("Connection was deleted successfully: <%s>", connectionUuid));
        return true;
    }

    public LiveData<List<Connection>> getLiveDataConnectionsOfParent(String parentId) {
        MutableLiveData<List<Connection>> liveDateConnections = new MutableLiveData<>();
        firestore
                .collection(COLLECTION_FIRESTORE_NAME)
                .whereEqualTo("sideAUId", parentId)
                .addSnapshotListener((value, err) -> {
                    if (err != null) {
                        Log.e(TAG, String.format("Failed to extract requests of parent <%s>due to: %s", parentId, err));
                    } else if (value == null) {
                        Log.e(TAG, String.format("Failed to extract requests of parent <%s> due to: value is null", parentId));
                    } else {
                        List<Connection> connections = new ArrayList<>();
                        List<DocumentSnapshot> documentSnapshots = value.getDocuments();
                        documentSnapshots.forEach(doc -> connections.add(doc.toObject(Connection.class)));
                        liveDateConnections.setValue(connections);
                        Log.d(TAG, "[getLiveDataConnectionsOfParent] All connections extracted successfully" + connections);
                    }
                });
        return liveDateConnections;
    }

    public LiveData<List<Connection>> getLiveDataConnectionsOfBabysitter(String babysitterUUID) {
        MutableLiveData<List<Connection>> liveDateConnections = new MutableLiveData<>();
        firestore
                .collection(COLLECTION_FIRESTORE_NAME)
                .whereEqualTo("sideBUId", babysitterUUID)
                .addSnapshotListener((value, err) -> {
                    if (err != null) {
                        Log.e(TAG, String.format("Failed to extract requests of babysitter <%s>due to: %s", babysitterUUID, err));
                    } else if (value == null) {
                        Log.e(TAG, String.format("Failed to extract requests of babysitter <%s> due to: value is null", babysitterUUID));
                    } else {
                        List<Connection> connections = new ArrayList<>();
                        List<DocumentSnapshot> documentSnapshots = value.getDocuments();
                        documentSnapshots.forEach(doc -> connections.add(doc.toObject(Connection.class)));
                        liveDateConnections.setValue(connections);
                        Log.d(TAG, "[getLiveDataConnectionsOfBabysitter] All connections extracted successfully" + connections);
                    }
                });
        return liveDateConnections;
    }

    public void getConnectionsOfBabysitter(String babysitterUUID, IApplyOnConnections applier, OnFailureListener onFailureListener) {
        Task<QuerySnapshot> querySnapshotTask = firestore.collection(COLLECTION_FIRESTORE_NAME).whereEqualTo("sideBUId", babysitterUUID).get();
        querySnapshotTask.addOnSuccessListener(connections -> {
            applier.apply(connections.toObjects(Connection.class));
        });
        querySnapshotTask.addOnFailureListener(onFailureListener);
    }

    public void getConnection(User userA, User userB, IGetConnection applier) {
        firestore.collection(COLLECTION_FIRESTORE_NAME)
                .whereEqualTo("sideAUId", userA.getUuid())
                .whereEqualTo("sideBUId", userB.getUuid())
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot == null) {
                        Log.e(TAG, "Unexpected behavior - snapshot is null");
                    } else if (snapshot.size() == 0) {
                        applier.connectionIsNotExist(userA.getUuid(), userB.getUuid());
                    } else if (snapshot.size() == 1) {
                        Connection connection = snapshot.toObjects(Connection.class).get(0);
                        applier.connectionFound(connection);
                    } else {
                        Log.e(TAG, "Unexpected behavior - there is more then 1 connection between 2 users:" + snapshot.toObjects(Connection.class));
                    }
                });
    }

    public void getConnectionsOfParent(String userID, IGetConnections iGetConnections) {
        Log.d(TAG, "[getConnectionsOfParent] Getting connections of parent:" + userID);
        firestore.collection(COLLECTION_FIRESTORE_NAME)
                .whereEqualTo("sideAUId", userID)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot == null) {
                        Log.e(TAG, "Unexpected behavior - snapshot is null");
                    }
                    List<Connection> connections = snapshot.toObjects(Connection.class);
                    Log.d(TAG, "[getConnectionsOfParent] Extracted connections:" + connections);
                    iGetConnections.apply(connections);
                });
    }

    public void getConnectionsOfParents(List<Parent> parents, IGetConnections iGetConnections) {
        if (parents == null || parents.size() == 0) {
            Log.e(TAG, "parents is null or empty" + parents);
            return;
        }
        Log.d(TAG, "[getConnectionsOfParents] Getting connections of parents:" + parents);
        List<String> parentsIDs = parents.stream().map(p -> p.getUuid()).collect(Collectors.toList());
        firestore.collection(COLLECTION_FIRESTORE_NAME)
                .whereIn("sideAUId", parentsIDs)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot == null) {
                        Log.e(TAG, "Unexpected behavior - snapshot is null");
                    }
                    List<Connection> connections = snapshot.toObjects(Connection.class);
                    Log.d(TAG, "[getConnectionsOfParents] Extracted connections:" + connections);
                    iGetConnections.apply(connections);
                });
    }
}
