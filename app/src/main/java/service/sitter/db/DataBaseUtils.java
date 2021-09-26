package service.sitter.db;


import static com.google.common.io.Files.getFileExtension;

import static java.lang.Thread.sleep;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

public class DataBaseUtils {
    private static final String TAG = DataBase.class.getSimpleName();

    private DataBaseUtils() {

    }

    // UploadImage method
    public static void uploadImage(Uri filePath,String imageID, IOnSuccessUploadingImage listener) {
        if (filePath != null) {
            Log.d(TAG, "uploading image");

            // Defining the child of storageReference
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference();
            StorageReference ref = storageReference.child("images/" + imageID);

            // adding listeners on upload or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> {
//                        listener.onSuccess(imageID);
                        Log.d(TAG, String.format("Child image was added successfully: <%s>", imageID));

                    })
                    .addOnFailureListener(e -> Log.e(TAG, e.getMessage()))
                    .addOnProgressListener(
                            taskSnapshot -> {
                                double progress
                                        = (100.0
                                        * taskSnapshot.getBytesTransferred()
                                        / taskSnapshot.getTotalByteCount());
                                Log.d(TAG, "Uploaded " + (int) progress + "%");
                            });
        }
    }


    public static void loadImage(String imageName, IOnSuccessLoadingImage onSuccessLoadingImage) {
        StorageReference mImageStorage = FirebaseStorage.getInstance().getReference();
        StorageReference ref = mImageStorage.child("images").child(imageName);
        ref.getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    onSuccessLoadingImage.onSuccess(uri);
                    String imageUrl = uri.toString();
                    Log.d(TAG, "successful downloaded image: " + imageUrl);
                })
                .addOnFailureListener(e -> Log.e(TAG, e.getMessage()));

    }

}

