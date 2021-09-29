package service.sitter.utils;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewUtils {


    public static <T> void switchBetweenRecAndText(View root, List<T> list, int recyclerViewId, int recyclerViewIdText) {
        RecyclerView recyclerViewRecommendations = root.findViewById(recyclerViewId);
        TextView viewById = root.findViewById(recyclerViewIdText);
        if (list.isEmpty()) {
            viewById.setVisibility(View.VISIBLE);
            recyclerViewRecommendations.setVisibility(View.GONE);
        } else {
            viewById.setVisibility(View.GONE);
            recyclerViewRecommendations.setVisibility(View.VISIBLE);
        }
    }
}
