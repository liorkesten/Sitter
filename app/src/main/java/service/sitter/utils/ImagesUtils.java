package service.sitter.utils;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import service.sitter.db.DataBaseUtils;

public class ImagesUtils {

    /**
     * Gets imageUri and image view and upload the imageUri into the image view using Picasso.
     *
     * @param imageUri
     * @param imageView
     */
    public static void updateImageView(String imageUri, ImageView imageView) {
        DataBaseUtils.loadImage(imageUri, (uri) -> Picasso.get().load(uri).into(imageView));

    }
}
