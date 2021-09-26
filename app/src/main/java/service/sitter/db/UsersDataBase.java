package service.sitter.db;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import service.sitter.models.Babysitter;
import service.sitter.models.Child;
import service.sitter.models.Parent;
import service.sitter.models.User;
import service.sitter.models.UserCategory;

public class UsersDataBase {
    private static final String TAG = UsersDataBase.class.getSimpleName();
    private static final String COLLECTION_FIRESTORE_NAME = "users";
    private static final String COLLECTION_FIRESTORE_PARENT_NAME = "parents";
    private static final String COLLECTION_FIRESTORE_BABYSITTER_NAME = "babysitters";

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

    /**
     * Add parent to data base
     *
     * @param parent - new parent
     * @return false in case that the user is already exists in the db.
     */
    public boolean addParent(@NonNull Parent parent) {
        String parentUuid = parent.getUuid();
        firestore.collection(COLLECTION_FIRESTORE_PARENT_NAME).document(parentUuid).set(parent);
        Log.d(TAG, String.format("Parent was added successfully: <%s>", parentUuid));
        DataBaseUtils.uploadImage(Uri.parse(parent.getImage()));
        // uploading all children to db
        for (Child child : parent.getChildren()) {
            DataBaseUtils.uploadImage(Uri.parse(child.getImage()));
            Log.d(TAG, String.format("Child was added successfully: <%s>", child.getName()));
        }
        return true;
    }

    /**
     * Delete parent to data base
     *
     * @param parent - new parent
     * @return false in case that the user is already exists in the db.
     */
    public boolean deleteParent(@NonNull Parent parent) {
        String parentUuid = parent.getUuid();
        firestore.collection(COLLECTION_FIRESTORE_PARENT_NAME).document(parentUuid).delete();
        Log.d(TAG, String.format("Parent was deleted successfully: <%s>", parentUuid));
        return true;
    }

    /**
     * Add parent to data base
     *
     * @param babysitter - new babysitter
     * @return false in case that the user is already exists in the db.
     */
    public boolean addBabysitter(@NonNull Babysitter babysitter) {
        String babysitterUuid = babysitter.getUuid();
        firestore.collection(COLLECTION_FIRESTORE_BABYSITTER_NAME).document(babysitterUuid).set(babysitter);
        Log.d(TAG, String.format("Babysitter was added successfully: <%s>", babysitterUuid));
        DataBaseUtils.uploadImage(Uri.parse(babysitter.getImage()));

        return true;
    }


    /**
     * Delete parent to data base
     *
     * @param babysitter - new parent
     * @return false in case that the user is already exists in the db.
     */
    public boolean deleteBabysitter(@NonNull Babysitter babysitter) {
        String babysitterUuid = babysitter.getUuid();
        firestore.collection(COLLECTION_FIRESTORE_BABYSITTER_NAME).document(babysitterUuid).delete();
        Log.d(TAG, String.format("Babysitter was deleted successfully: <%s>", babysitterUuid));
        return true;
    }


    public Parent getParent(String userUID) throws UserNotFoundException {
        DocumentSnapshot snapshot = null;
        Task<DocumentSnapshot> documentSnapshotTask = firestore.collection(COLLECTION_FIRESTORE_PARENT_NAME).document(userUID).get();
        try {
            snapshot = Tasks.await(documentSnapshotTask);
        } catch (ExecutionException | InterruptedException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
        if (snapshot == null) {
            throw new UserNotFoundException(userUID);
        }
        return snapshot.toObject(Parent.class);
    }

    public Babysitter getBabysitter(String userUID) throws UserNotFoundException {
        DocumentSnapshot snapshot = firestore.collection(COLLECTION_FIRESTORE_BABYSITTER_NAME).document(userUID).get().getResult();
        if (snapshot == null) {
            throw new UserNotFoundException(userUID);
        }
        return (Babysitter) snapshot.toObject(Babysitter.class);
    }

    public UserCategory getUserCategory(String userUID) throws UserNotFoundException {
        try {
            // Always Babysitter type is returned.
            return getBabysitter(userUID).getCategory();
        } catch (UserNotFoundException ignored) {
        }
        try {
            // Always Parent type is returned.
            return getParent(userUID).getCategory();
        } catch (UserNotFoundException ignored) {
        }

        // In case that the user id is not parent and babysitter
        throw new UserNotFoundException(userUID);
    }

    public Babysitter getBabysitterByPhoneNumber(String phonerNumber) {
//        QuerySnapshot snapshots = firestore.collection(COLLECTION_FIRESTORE_BABYSITTER_NAME).whereEqualTo("phonerNumber", phonerNumber).get();
//        if (snapshots == null) {
//            throw new UserNotFoundException(userUID);
//        }
//        return (Babysitter) snapshot.toObject(Babysitter.class);
        //TODO Implement this!!!
        Babysitter b = new Babysitter("Noam", "Kesten", "n@gma", "0547718646", "NY", "URL_TO_IMAGE", false);
        b.setUuid("123");
        return b;
    }
}
