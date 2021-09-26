package service.sitter.utils;

import android.net.Uri;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import service.sitter.db.DataBaseUtils;

public class ImagesUtils {

    public static Map<String, Uri> cachedUris = new HashMap<String, Uri>();

    /**
     * Gets imageUri and image view and upload the imageUri into the image view using Picasso.
     *
     * @param imageUri
     * @param imageView
     */
    public static void updateImageView(String imageUri, ImageView imageView) {
        if (!cachedUris.containsKey(imageUri)) {
            DataBaseUtils.loadImage(imageUri, (uri) -> {
                cachedUris.put(imageUri, uri);
                Picasso.get().load(uri).into(imageView);
            });
        }
        Picasso.get().load(cachedUris.get(imageUri)).into(imageView);
    }
}
