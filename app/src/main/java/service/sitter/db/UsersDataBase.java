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
import java.util.UUID;

import service.sitter.models.Babysitter;
import service.sitter.models.Child;
import service.sitter.models.Parent;
import service.sitter.models.User;
import service.sitter.models.UserCategory;
import service.sitter.recommendations.IGetParents;
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
    public boolean addParent(@NonNull Parent parent, IOnSuccessOperatingUser listener) {
        String parentUuid = parent.getUuid();
        String parentImageID = UUID.randomUUID().toString();
        DataBaseUtils.uploadImage(Uri.parse(parent.getImage()), parentImageID, u -> parent.setImage(u.toString()));
        parent.setImage(parentImageID);
        // uploading all children to db
        for (Child child : parent.getChildren()) {
            String imageID = UUID.randomUUID().toString();
            DataBaseUtils.uploadImage(Uri.parse(child.getImage()), imageID, u -> child.setImage(u.toString()));
            child.setImage(imageID);
            Log.d(TAG, String.format("Child was added successfully: <%s>", child.getName()));
        }
        firestore.collection(COLLECTION_FIRESTORE_PARENT_NAME).document(parentUuid).set(parent)
                .addOnSuccessListener(unused -> listener.onSuccess());
        Log.d(TAG, String.format("Parent was added successfully: <%s>", parentUuid));
        return true;
    }

    /**
     * Delete parent to data base
     *
     * @param parent - new parent
     * @return false in case that the user is already exists in the db.
     */
    public boolean deleteParent(@NonNull Parent parent, IOnSuccessOperatingUser listener) {
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
    public boolean addBabysitter(@NonNull Babysitter babysitter, IOnSuccessOperatingUser listener) {
        String babysitterUuid = babysitter.getUuid();
        String imageID = UUID.randomUUID().toString();
        DataBaseUtils.uploadImage(Uri.parse(babysitter.getImage()), imageID, u -> babysitter.setImage(u.toString()));
        babysitter.setImage(imageID);

        firestore.collection(COLLECTION_FIRESTORE_BABYSITTER_NAME).document(babysitterUuid).set(babysitter)
                .addOnSuccessListener(unused -> listener.onSuccess());
        Log.d(TAG, String.format("Babysitter was added successfully: <%s>", babysitterUuid));
        return true;
    }


    /**
     * Delete parent to data base
     *
     * @param babysitter - new parent
     * @return false in case that the user is already exists in the db.
     */
    public boolean deleteBabysitter(@NonNull Babysitter babysitter, IOnSuccessOperatingUser listener) {
        String babysitterUuid = babysitter.getUuid();
        firestore.collection(COLLECTION_FIRESTORE_BABYSITTER_NAME).document(babysitterUuid).delete();
        Log.d(TAG, String.format("Babysitter was deleted successfully: <%s>", babysitterUuid));
        return true;
    }


    public void getParent(String userUID, IGetParent applierOnSuccess, OnFailureListener onFailureListener) {
        if (userUID == null || userUID.equals("")) {
            return;
        }
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
        firestore.collection(COLLECTION_FIRESTORE_PARENT_NAME)
                .whereIn("uuid", parentsUUID)
                .addSnapshotListener((docs, err) -> {
                    List<Parent> parents = docs.toObjects(Parent.class);
                    applier.apply(parents);
                });
    }

    public void getBabysitter(String userUID, IOnGettingBabysitterFromDb applierOnSuccess, OnFailureListener onFailureListener) {
        if (userUID == null || userUID.equals("")) {
            return;
        }
        Task<DocumentSnapshot> documentSnapshotTask = firestore
                .collection(COLLECTION_FIRESTORE_BABYSITTER_NAME)
                .document(userUID)
                .get()
                .addOnFailureListener(onFailureListener);

        documentSnapshotTask.addOnSuccessListener(snapshot -> applierOnSuccess.babysitterFound(snapshot.toObject(Babysitter.class)));
    }

    public void getParentsByPhoneNumbers(List<String> phoneNumbers, IGetParents iGetParents) {
        if (phoneNumbers == null || phoneNumbers.size() == 0) {
            Log.d(TAG, "phone numbers are empty" + phoneNumbers);
            return;
        }
        Log.d(TAG, "getting parents of phone nubmers:" + phoneNumbers);
        firestore.collection(COLLECTION_FIRESTORE_PARENT_NAME)
                .whereIn("phoneNumber", phoneNumbers)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot == null) {
                        //TODO
                        Log.e(TAG, "snapshot is null");
                    }
                    List<Parent> parents = snapshot.toObjects(Parent.class);
                    iGetParents.apply(parents);
                });
    }
}
