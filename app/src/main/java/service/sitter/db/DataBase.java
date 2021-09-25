package service.sitter.db;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import service.sitter.models.Babysitter;
import service.sitter.models.Connection;
import service.sitter.models.Parent;
import service.sitter.models.Recommendation;
import service.sitter.models.Request;
import service.sitter.models.User;
import service.sitter.models.UserCategory;

public class DataBase implements IDataBase {
    private static DataBase instance;

    private final UsersDataBase usersDb;
    private final RecommendationsDataBase reccomendationsDb;
    private final RequestsDataBase requestsDb;
    private final ConnectionsDataBase collectionsDb;
    private FirebaseFirestore fireStore;

    private DataBase() {
        fireStore = FirebaseFirestore.getInstance();

        usersDb = new UsersDataBase(fireStore);
        reccomendationsDb = new RecommendationsDataBase(fireStore);
        requestsDb = new RequestsDataBase(fireStore);
        collectionsDb = new ConnectionsDataBase(fireStore);

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
        return collectionsDb.addConnection(connection);
    }

    public boolean deleteConnection(String connectionUuid) {
        return collectionsDb.deleteConnection(connectionUuid);
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
    public boolean addParent(@NonNull Parent parent) {
        return usersDb.addParent(parent);
    }

    @Override
    public boolean deleteParent(@NonNull Parent parent) {
        return usersDb.deleteParent(parent);
    }

    @Override
    public boolean addBabysitter(@NonNull Babysitter babysitter) {
        return usersDb.addBabysitter(babysitter);
    }

    @Override
    public boolean deleteBabysitter(@NonNull Babysitter babysitter) {
        return usersDb.deleteBabysitter(babysitter);
    }

    @Override
    public UserCategory getUserCategory(String userUID) throws UserNotFoundException {
        return usersDb.getUserCategory(userUID);
    }

    @Override
    public Parent getParent(String userUID) throws UserNotFoundException {
        return usersDb.getParent(userUID);
    }

    @Override
    public Babysitter getBabysitter(String userUID) throws UserNotFoundException {
        return usersDb.getBabysitter(userUID);
    }


    /**
     //     * Adds new request to the DB.
     //     *
     //     * @param newRequest is the request that should be added to the DB/
     //     */
//    public void addRequest(Request newRequest) {
//        requestsDb.put(newRequest.getUUID(), newRequest);
//        //FIXME Check if the values extraction below is working.
//        mutableRequestsLiveData.setValue(new ArrayList(requestsDb.values()));
//    }
}
