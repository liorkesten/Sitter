package service.sitter.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
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
    public static void updateImageView(Context context, String imageUri, ImageView imageView) {
        Log.d("ImageUtils", "cache:\n" + ImagesUtils.cachedUris.toString());
        Log.d("ImageUtils", "got imageUri: " + imageUri);
        if (!cachedUris.containsKey(imageUri)) {
            Log.d("ImageUtils", "imageUri is not in the cache");
            DataBaseUtils.loadImage(imageUri, (uri) -> {
                Log.d("ImageUtils", "Added imageUri to cache:" + uri);
                Glide.with(context).load(uri).into(imageView);
                cachedUris.put(imageUri, uri);
                Log.d("ImageUtils", "Set imageUri into imageView");
            });
        } else {
            Log.d("ImageUtils", "Image uri is in the cache: " + imageUri);
            Glide.with(context).load(cachedUris.get(imageUri)).into(imageView);
        }
    }
}
