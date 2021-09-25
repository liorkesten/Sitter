package service.sitter.db;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import service.sitter.models.Babysitter;
import service.sitter.models.Connection;
import service.sitter.models.Parent;
import service.sitter.models.Recommendation;
import service.sitter.models.Request;
import service.sitter.models.User;

/**
 * IDataBase is the interface for application database - needed for mocking and testing.
 */
public interface IDataBase {

    boolean addRequest(@NonNull @NotNull Request request);

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

    public boolean addParent(@NonNull @NotNull Parent parent);

    public boolean deleteParent(@NonNull @NotNull Parent parent);

    public boolean addBabysitter(@NonNull @NotNull Babysitter babysitter);

    public boolean deleteBabysitter(@NonNull @NotNull Babysitter babysitter);
}
