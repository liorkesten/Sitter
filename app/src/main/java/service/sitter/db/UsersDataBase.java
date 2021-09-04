package service.sitter.db;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import service.sitter.models.User;

public class UsersDataBase {
    private static final String TAG = UsersDataBase.class.getSimpleName();
    private static final String COLLECTION_FIRESTORE_NAME = "users";

    private final FirebaseFirestore firestore;
    private final Map<String, User> users = new HashMap<>();
    private final MutableLiveData<List<User>> mutableLiveData = new MutableLiveData<>();

    /**
     * @param fireStore
     */
    public UsersDataBase(@NonNull FirebaseFirestore fireStore) {
        this.firestore = fireStore;
    }

    /**
     * Add users to data base
     *
     * @param user - new user
     * @return false in case that the user is already exists in the db.
     */
    public boolean addUser(@NonNull User user) {
        String userUuid = user.getUuid();
        if (users.containsKey(userUuid)) {
            Log.e(TAG, String.format("User already exist : <%s>", userUuid));
            return false;
        }

        users.put(user.getUuid(), user);
        firestore.collection(COLLECTION_FIRESTORE_NAME).document(userUuid).set(user);

        Log.d(TAG, String.format("User was added successfully: <%s>", userUuid));
        return true;
    }


    public boolean deleteUser(String userUuid) {
        if (!users.containsKey(userUuid)) {
            Log.e(TAG, String.format("User that should be deleted is not exist : <%s>", userUuid));
            return false;
        }

        users.remove(userUuid);
        firestore.collection(COLLECTION_FIRESTORE_NAME).document(userUuid).delete();

        Log.d(TAG, String.format("User was deleted successfully: <%s>", userUuid));
        return true;
    }

}
