package service.sitter.db;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.OnFailureListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import service.sitter.models.Babysitter;
import service.sitter.models.Connection;
import service.sitter.models.Parent;
import service.sitter.models.Recommendation;
import service.sitter.models.Request;
import service.sitter.models.User;
import service.sitter.models.UserCategory;
import service.sitter.recommendations.IGetConnections;
import service.sitter.recommendations.IGetParents;
import service.sitter.ui.parent.connections.IOnGettingBabysitterFromDb;
import service.sitter.ui.parent.home.IOnUploadingRequest;

/**
 * IDataBase is the interface for application database - needed for mocking and testing.
 */
public interface IDataBase {

    boolean addRequest(@NonNull @NotNull Request request, IOnUploadingRequest listener);

    boolean deleteRequest(String requestUuid);

    boolean addRecommendation(@NonNull @NotNull Recommendation recommendation);

    boolean deleteRecommendation(String recommendationUuid);

    boolean addConnection(@NonNull @NotNull Connection connection);

    boolean deleteConnection(String connectionUuid);

    boolean addUser(@NonNull @NotNull User user);

    boolean deleteUser(String userUuid);

    LiveData<List<Request>> getLiveDataPendingRequestsOfParent(String parentId);

    LiveData<List<Request>> getLiveDataApprovedRequestsOfParent(String parentId);

    LiveData<List<Request>> getLiveDataDeletedRequestsOfParent(String parentId);

    LiveData<List<Request>> getLiveDataArchivedRequestsOfParent(String parentId);

    boolean addParent(@NonNull @NotNull Parent parent, IOnSuccessOperatingUser listener);

    boolean deleteParent(@NonNull @NotNull Parent parent, IOnSuccessOperatingUser listener);

    boolean addBabysitter(@NonNull @NotNull Babysitter babysitter, IOnSuccessOperatingUser listener);

    boolean deleteBabysitter(@NonNull @NotNull Babysitter babysitter, IOnSuccessOperatingUser listener);

    UserCategory getUserCategory(String userUID) throws UserNotFoundException;

    void getParent(String userUID, IGetParent applierOnSuccess, OnFailureListener onFailureListener);

    public Babysitter getBabysitter(String userUID) throws UserNotFoundException;

    public void getBabysitterByPhoneNumber(String phonerNumber, IOnGettingBabysitterFromDb listener);

    LiveData<List<Connection>> getLiveDataConnectionsOfParent(String parentId);

    void acceptRequestByBabysitter(Request r, Babysitter babysitter);

    LiveData<List<Request>> getLiveDataPendingRequestsOfBabysitter(String uuid);

    LiveData<List<Request>> getLiveDataApprovedRequestsOfBabysitter(String uuid);

    LiveData<List<Request>> getLiveDataArchivedRequestsOfBabysitter(String uuid);

    LiveData<List<Request>> getLiveDataDeletedRequestsOfBabysitter(String uuid);


    void cancelRequest(Request r, Babysitter babysitter);

    void addConnection(User userA, User userB);

    void getBabysitter(String userUID, IOnGettingBabysitterFromDb applierOnSuccess, OnFailureListener onFailureListener);

    LiveData<List<Recommendation>> getLiveDataRecommendationsOfParent(String uuid);

    void addRecommendations(List<Recommendation> recommendations);

    void getConnectionsOfParent(String userID, IGetConnections iGetConnections);

    void getConnectionsOfParents(List<Parent> parents, IGetConnections iGetConnections);

    void getParentsByPhoneNumbers(List<String> phoneNumbers, IGetParents iGetParents);
}
