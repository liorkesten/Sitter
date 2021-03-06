package service.sitter.recyclerview.requests.parent;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import service.sitter.R;

public class RequestViewHolder extends RecyclerView.ViewHolder {
    public TextView textView; //tODO Change to private
    public final View rootView; //tODO Change to private

    public RequestViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        // rootView is used for click on the whole print (not specific button)
        rootView = itemView;
//        textView = itemView.findViewById(R.id.textView);
        textView = itemView.findViewById(R.id.itemRequestDescriptionValue);
//        ageTextView = itemView.findViewById(R.id.itemChildAge);
    }
}
