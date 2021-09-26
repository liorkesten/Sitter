package service.sitter.db;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import service.sitter.models.Babysitter;
import service.sitter.models.Connection;
import service.sitter.models.Parent;
import service.sitter.models.Recommendation;
import service.sitter.models.Request;
import service.sitter.models.RequestStatus;
import service.sitter.models.User;
import service.sitter.models.UserCategory;

public class DataBase implements IDataBase {
    private static DataBase instance;

    private final UsersDataBase usersDb;
    private final RecommendationsDataBase reccomendationsDb;
    private final RequestsDataBase requestsDb;
    private final ConnectionsDataBase connectionsDb;
    private FirebaseFirestore fireStore;
    FirebaseStorage storage;
    private StorageReference storageReference;
    private static final String TAG = DataBase.class.getSimpleName();


    private DataBase() {
        fireStore = FirebaseFirestore.getInstance();
        usersDb = new UsersDataBase(fireStore);
        reccomendationsDb = new RecommendationsDataBase(fireStore);
        requestsDb = new RequestsDataBase(fireStore);
        connectionsDb = new ConnectionsDataBase(fireStore);
        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //TODO Add snapshotevent listener.
    }


    /**
     * Creates DataBase instance
     *
     * @return IDataBase
     */
    public static DataBase getInstance() {
        if (instance == null) {
            instance = new DataBase();
        }
        return instance;
    }

    public boolean addRequest(@NonNull @NotNull Request request) {
        return requestsDb.addRequest(request);
    }

    public boolean deleteRequest(String requestUuid) {
        return requestsDb.deleteRequest(requestUuid);
    }

    public boolean addRecommendation(@NonNull @NotNull Recommendation recommendation) {
        return reccomendationsDb.addRecommendation(recommendation);
    }

    public boolean deleteRecommendation(String recommendationUuid) {
        return reccomendationsDb.deleteRecommendation(recommendationUuid);
    }

    public boolean addConnection(@NonNull @NotNull Connection connection) {
        return connectionsDb.addConnection(connection);
    }

    public boolean deleteConnection(String connectionUuid) {
        return connectionsDb.deleteConnection(connectionUuid);
    }

    public boolean addUser(@NonNull @NotNull User user) {
        return usersDb.addUser(user);
    }

    public boolean deleteUser(String userUuid) {
        return usersDb.deleteUser(userUuid);
    }

    @Override
    public LiveData<List<Request>> getLiveDataPendingRequestsOfParent(String parentId) {
        return requestsDb.getLiveDataPendingRequestsOfParent(parentId);
    }

    @Override
    public LiveData<List<Request>> getLiveDataApprovedRequestsOfParent(String parentId) {
        return requestsDb.getLiveDataApprovedRequestsOfParent(parentId);
    }

    @Override
    public LiveData<List<Request>> getLiveDataDeletedRequestsOfParent(String parentId) {
        return requestsDb.getLiveDataDeletedRequestsOfParent(parentId);

    }

    @Override
    public LiveData<List<Request>> getLiveDataArchivedRequestsOfParent(String parentId) {
        return requestsDb.getLiveDataArchivedRequestsOfParent(parentId);
    }

    @Override
    public boolean addParent(@NonNull Parent parent, IOnSuccessOperatingUser listener) {
        return usersDb.addParent(parent, listener);
    }

    @Override
    public boolean deleteParent(@NonNull Parent parent, IOnSuccessOperatingUser listener) {
        return usersDb.deleteParent(parent, listener);
    }

    @Override
    public boolean addBabysitter(@NonNull Babysitter babysitter, IOnSuccessOperatingUser listener) {
        return usersDb.addBabysitter(babysitter, listener);
    }

    @Override
    public boolean deleteBabysitter(@NonNull Babysitter babysitter, IOnSuccessOperatingUser listener) {
        return usersDb.deleteBabysitter(babysitter, listener);
    }

    @Override
    public UserCategory getUserCategory(String userUID) throws UserNotFoundException {
        return usersDb.getUserCategory(userUID);
    }

    @Override
    public void getParent(String userUID, OnSuccessListener<DocumentSnapshot> onSuccessListener, OnFailureListener onFailureListener) {
        usersDb.getParent(userUID, onSuccessListener, onFailureListener);
    }

    @Override
    public Babysitter getBabysitter(String userUID) throws UserNotFoundException {
        return usersDb.getBabysitter(userUID);
    }

    @Override
    public Babysitter getBabysitterByPhoneNumber(String phonerNumber) throws UserNotFoundException {
        return usersDb.getBabysitterByPhoneNumber(phonerNumber);
    }




    public LiveData<List<Connection>> getLiveDataConnectionsOfParent(String parentId) {
        return connectionsDb.getLiveDataConnectionsOfParent(parentId);
    }

    @Override
    public void acceptRequestByBabysitter(Request r, Babysitter babysitter) {
        requestsDb.acceptRequestByBabysitter(r, babysitter);
    }

    @Override
    public void cancelRequest(Request r, Babysitter babysitter) {
        requestsDb.cancelRequest(r, babysitter);

    }


    @Override
    public LiveData<List<Request>> getLiveDataPendingRequestsOfBabysitter(String babysitterUUID) {
        return getLiveDataRequestsOfBabysitter(babysitterUUID, RequestStatus.Pending);
    }

    @Override
    public LiveData<List<Request>> getLiveDataApprovedRequestsOfBabysitter(String babysitterUUID) {
        return getLiveDataRequestsOfBabysitter(babysitterUUID, RequestStatus.Approved);
    }

    @Override
    public LiveData<List<Request>> getLiveDataDeletedRequestsOfBabysitter(String babysitterUUID) {
        return getLiveDataRequestsOfBabysitter(babysitterUUID, RequestStatus.Deleted);
    }


    @Override
    public LiveData<List<Request>> getLiveDataArchivedRequestsOfBabysitter(String babysitterUUID) {
        return getLiveDataRequestsOfBabysitter(babysitterUUID, RequestStatus.Archived);
    }

    public LiveData<List<Request>> getLiveDataRequestsOfBabysitter(String babysitterUUID, RequestStatus status) {
        MutableLiveData<List<Request>> mutableLiveDataRequests = new MutableLiveData<>(new ArrayList<>());
        IApplyOnConnections applier = connections -> {
            // Extract ID's of parents from connections.
            List<String> parentsUUID = connections.stream().map(Connection::getSideAUId).collect(Collectors.toList());
            if (parentsUUID.isEmpty()) {
                return;
            }
            requestsDb.getRequestsByParentsId(parentsUUID, mutableLiveDataRequests::setValue, status);
        };
        // Listen to changes on connections.
        connectionsDb
                .getLiveDataConnectionsOfBabysitter(babysitterUUID)
                .observeForever(applier::apply);

        // Set default values from connections.
        connectionsDb.getConnectionsOfBabysitter(babysitterUUID, applier::apply, null /*TODO*/);
        return mutableLiveDataRequests;
    }
}
