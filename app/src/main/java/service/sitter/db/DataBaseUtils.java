package service.sitter.db;


import android.net.Uri;
import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DataBaseUtils {
    private static final String TAG = DataBaseUtils.class.getSimpleName();

    private DataBaseUtils() {

    }

    // UploadImage method
    public static void uploadImage(Uri filePath, String imageID, IOnSuccessLoadingImage listener) {
        if (filePath != null) {
            //Log.d(TAG, String.format("uploading filepath: <%s> and imageID: <%s>", filePath, imageID));
            // Defining the child of storageReference
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference();
            StorageReference ref = storageReference.child("images/" + imageID);

            // adding listeners on upload or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> {
//                        loadImage(imageID, listener);
//                        if (listener != null) {
//                            listener.onSuccess(imageID);
//                        }
                        //Log.d(TAG, String.format("Child image was added successfully: <%s>", imageID));


                    })
                    .addOnFailureListener(e -> Log.e(TAG, e.getMessage()))
                    .addOnProgressListener(
                            taskSnapshot -> {
                                double progress
                                        = (100.0
                                        * taskSnapshot.getBytesTransferred()
                                        / taskSnapshot.getTotalByteCount());
                                //Log.d(TAG, "Uploaded " + (int) progress + "%");
                            });
        }
    }


    public static void loadImage(String imageName, IOnSuccessLoadingImage onSuccessLoadingImage) {
        StorageReference mImageStorage = FirebaseStorage.getInstance().getReference();
        StorageReference ref = mImageStorage.child("images").child(imageName);
        ref.getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    //Log.d(TAG, "successful downloaded image: " + uri);
                    if (onSuccessLoadingImage != null){
                        onSuccessLoadingImage.onSuccess(uri);
                    }
                    String imageUrl = uri.toString();
                    //Log.d(TAG, "successful downloaded image after apply listener: " + imageUrl);
                })
                .addOnFailureListener(e -> Log.e(TAG, e.getMessage()));

    }

}

