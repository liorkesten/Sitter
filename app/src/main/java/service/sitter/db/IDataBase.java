package service.sitter.db;

import android.content.Context;
import android.net.Uri;

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
import service.sitter.models.UserCategory;

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

    void uploadImage(Uri filePath);


    LiveData<List<Request>> getLiveDataPendingRequestsOfParent(String parentId);

    LiveData<List<Request>> getLiveDataApprovedRequestsOfParent(String parentId);

    LiveData<List<Request>> getLiveDataDeletedRequestsOfParent(String parentId);

    LiveData<List<Request>> getLiveDataArchivedRequestsOfParent(String parentId);

    boolean addParent(@NonNull @NotNull Parent parent);

    boolean deleteParent(@NonNull @NotNull Parent parent);

    boolean addBabysitter(@NonNull @NotNull Babysitter babysitter);

    boolean deleteBabysitter(@NonNull @NotNull Babysitter babysitter);

    UserCategory getUserCategory(String userUID) throws UserNotFoundException;

    Parent getParent(String userUID) throws UserNotFoundException;

    public Babysitter getBabysitter(String userUID) throws UserNotFoundException;
}
