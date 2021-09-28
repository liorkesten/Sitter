package service.sitter.utils;

import static java.lang.System.exit;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import service.sitter.models.Babysitter;
import service.sitter.models.Parent;

public class SharedPreferencesUtils {
    private static final Gson _GSON = new Gson();
    private static final String _PARENT_KEY_SP = "parent";
    private static final String _BABYSITTER_KEY_SP = "babysitter";
    private static final String TAG = SharedPreferencesUtils.class.getSimpleName();
    private static SharedPreferences SP;

    public static void saveParentToSP(SharedPreferences sp, @NonNull Parent parent) {
        Log.d(TAG, String.format("Saving parent into SP.\nsp:<%s>\nparent: <%s>", sp, parent));
        String serializedParent = _GSON.toJson(parent);
        sp.edit().putString(_PARENT_KEY_SP, serializedParent).apply();
    }

    public static void saveBabysitterToSP(SharedPreferences sp, @NonNull Babysitter babysitter) {
        Log.d(TAG, String.format("Saving babysitter into SP.\nsp:<%s>\nparent: <%s>", sp, babysitter));
        String serializedBabysitter = _GSON.toJson(babysitter);
        sp.edit().putString(_BABYSITTER_KEY_SP, serializedBabysitter).apply();
    }

    public static Parent getParentFromSP(SharedPreferences sp) {
        String mySerializedUser = sp.getString(_PARENT_KEY_SP, "");
        if (mySerializedUser.equals("")) {
            Log.e(TAG, "error while trying to extract user uid after that the object saved as json.myUserUid is empty");
            // TODO handle with exit.
//            exit(130);
            return null;
        }
        Parent parent = _GSON.fromJson(mySerializedUser, Parent.class);
        if (parent == null) {
            Log.e(TAG, "error while trying to convert mySerializedUser to json - parent is null. mySerializedUser:" + mySerializedUser);
//            exit(150);
            return null;
        }
        Log.d(TAG, "Extracted parent: " + parent.toString());
        return parent;
    }

    public static Babysitter getBabysitterFromSP(SharedPreferences sp) {
        String mySerializedUser = sp.getString(_BABYSITTER_KEY_SP, "");
        if (mySerializedUser.equals("")) {
            Log.e(TAG, "error while trying to extract user uid after that the object saved as json.myUserUid is empty");
            // TODO handle with exit.
//            exit(130);
            return null;
        }
        Babysitter babysitter = _GSON.fromJson(mySerializedUser, Babysitter.class);
        if (babysitter == null) {
            Log.e(TAG, "error while trying to convert mySerializedUser to json - babysitter is null. mySerializedUser:" + mySerializedUser);
//            exit(150);
            return null;
        }
        Log.d(TAG, "Extracted babysitter: " + babysitter.toString());
        return babysitter;
    }

}
