package service.sitter.db;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import service.sitter.models.Babysitter;
import service.sitter.models.Child;
import service.sitter.models.Parent;
import service.sitter.models.User;
import service.sitter.models.UserCategory;
import service.sitter.ui.parent.connections.IOnGettingBabysitterFromDb;

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
        DataBaseUtils.uploadImage(Uri.parse(parent.getImage()), parent::setImage);
        // uploading all children to db
        for (Child child : parent.getChildren()) {
            DataBaseUtils.uploadImage(Uri.parse(child.getImage()), child::setImage);
            Log.d(TAG, String.format("Child was added successfully: <%s>", child.getName()));
        }
        firestore.collection(COLLECTION_FIRESTORE_PARENT_NAME).document(parentUuid).set(parent);
        Log.d(TAG, String.format("Parent was added successfully: <%s>", parentUuid));
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
        DataBaseUtils.uploadImage(Uri.parse(babysitter.getImage()), babysitter::setImage);

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


    public void getParent(String userUID, IGetParent applierOnSuccess, OnFailureListener onFailureListener) {
        Task<DocumentSnapshot> documentSnapshotTask = firestore.collection(COLLECTION_FIRESTORE_PARENT_NAME).document(userUID).get().addOnFailureListener(onFailureListener);
        documentSnapshotTask.addOnSuccessListener(snapshot -> applierOnSuccess.parentFound(snapshot.toObject(Parent.class)));
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
        // Always Parent type is returned.
        //TODO Chnage this signature
        getParent(userUID, null, null);

        // In case that the user id is not parent and babysitter
        throw new UserNotFoundException(userUID);
    }

    public void getBabysitterByPhoneNumber(String phoneNumber, IOnGettingBabysitterFromDb listener) {
        Task<QuerySnapshot> querySnapshotTask = firestore.collection(COLLECTION_FIRESTORE_BABYSITTER_NAME).whereEqualTo("phoneNumber", phoneNumber).get();
        querySnapshotTask.addOnSuccessListener(docs -> {
            if (docs.size() == 0) {
                Log.e(TAG, String.format("Babysitter is not found with phone number: <%s>", phoneNumber));
                return;
            }
            if (docs.size() > 1) {
                Log.e(TAG, String.format("There is more then one babysitter with phone number: <%s>", phoneNumber));
                return;
            }
            Babysitter babysitter = docs.toObjects(Babysitter.class).get(0);
            listener.babysitterFound(babysitter);

        });
    }

    public void getParents(List<String> parentsUUID, IApplyOnParents applier) {
        firestore.collection(COLLECTION_FIRESTORE_PARENT_NAME).whereIn("uuid", parentsUUID).addSnapshotListener((docs, err) -> {
            List<Parent> parents = docs.toObjects(Parent.class);
            applier.apply(parents);
        });
    }

    public void getBabysitter(String userUID, IOnGettingBabysitterFromDb applierOnSuccess, OnFailureListener onFailureListener) {
        Task<DocumentSnapshot> documentSnapshotTask = firestore
                .collection(COLLECTION_FIRESTORE_BABYSITTER_NAME)
                .document(userUID)
                .get()
                .addOnFailureListener(onFailureListener);

        documentSnapshotTask.addOnSuccessListener(snapshot -> applierOnSuccess.babysitterFound(snapshot.toObject(Babysitter.class)));
    }
}
