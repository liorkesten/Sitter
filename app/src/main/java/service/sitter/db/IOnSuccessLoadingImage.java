package service.sitter.db;

import android.net.Uri;


@FunctionalInterface
public interface IOnSuccessLoadingImage {
    public void onSuccess(Uri uri);
}
