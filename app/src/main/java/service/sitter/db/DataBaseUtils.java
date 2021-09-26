package service.sitter.db;


import static com.google.common.io.Files.getFileExtension;

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
    public static void uploadImage(Uri filePath) {
        if (filePath != null) {
            Log.d(TAG, "uploading image");

            // Defining the child of storageReference
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference();
            StorageReference ref = storageReference.child("images/" + filePath.toString());

            // adding listeners on upload or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> Log.d(TAG, "success uploading image"))
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

    public static Uri upload (Uri imageUri){
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        final StorageReference ref = storageRef.child(imageUri.toString());
        UploadTask uploadTask = ref.putFile(imageUri);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
        return null;
    }

    public static void loadImage(String imageName,IOnSuccessLoadingImage onSuccessLoadingImage ) {
        StorageReference mImageStorage = FirebaseStorage.getInstance().getReference();
        StorageReference ref = mImageStorage.child("images").child(imageName);
        ref.getDownloadUrl().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downUri = task.getResult();
                onSuccessLoadingImage.onSuccess(downUri);
                String imageUrl = downUri.toString();
                Log.d(TAG, "successful downloaded image: " + imageUrl);
            } else {
                Log.d(TAG, "Error while downloading image: ");
            }
        });
    }

}

