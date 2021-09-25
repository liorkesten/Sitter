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

import service.sitter.models.Connection;

public class ConnectionsDataBase {
    private static final String TAG = ConnectionsDataBase.class.getSimpleName();
    private static final String COLLECTION_FIRESTORE_NAME = "connections";

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
        firestore.collection(COLLECTION_FIRESTORE_NAME).document(connectionUuid).set(connection);

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
        if (!Connections.containsKey(connectionUuid)) {
            Log.e(TAG, String.format("Connection that should be deleted is not exist : <%s>", connectionUuid));
            return false;
        }

        Connections.remove(connectionUuid);
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
                        Log.d(TAG, "All connections extracted successfully");
                    }
                });
        return liveDateConnections;
    }

}
