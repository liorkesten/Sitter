package service.sitter.db;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

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

    public boolean addRequest(@NonNull @NotNull Request request);

    public boolean deleteRequest(String requestUuid);

    public boolean addRecommendation(@NonNull @NotNull Recommendation recommendation);

    public boolean deleteRecommendation(String recommendationUuid);

    public boolean addConnection(@NonNull @NotNull Connection connection);

    public boolean deleteConnection(String connectionUuid);

    public boolean addUser(@NonNull @NotNull User user);

    public boolean deleteUser(String userUuid);

    public boolean addParent(@NonNull @NotNull Parent parent);

    public boolean deleteParent(@NonNull @NotNull Parent parent);

    public boolean addBabysitter(@NonNull @NotNull Babysitter babysitter);

    public boolean deleteBabysitter(@NonNull @NotNull Babysitter babysitter);
}
