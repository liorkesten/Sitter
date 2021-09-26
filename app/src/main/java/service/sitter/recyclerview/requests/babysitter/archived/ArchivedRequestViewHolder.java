package service.sitter.recyclerview.requests.babysitter.archived;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import service.sitter.R;

public class ArchivedRequestViewHolder extends RecyclerView.ViewHolder {
    // Main Views
    private TextView textView;
    private final View rootView;

    // Views
    private TextView dateValueTextView;
    private TextView timeValueTextView;
    private TextView descriptionValueTextView;
    private TextView nameValueTextView;
    private ImageView profileImageView;

    public ArchivedRequestViewHolder(@NonNull View itemView) {
        super(itemView);
        // Main views
        rootView = itemView;

        // Views:
        dateValueTextView = itemView.findViewById(R.id.item_babysitter_request_archived_date_value);
        timeValueTextView = itemView.findViewById(R.id.item_babysitter_request_archived_time_value);
        descriptionValueTextView = itemView.findViewById(R.id.item_babysitter_request_archived_desc_value);
        nameValueTextView = itemView.findViewById(R.id.item_babysitter_request_archived_name_value);
        profileImageView = itemView.findViewById(R.id.item_babysitter_request_archived_profile_image);
    }

    public TextView getDateValueTextView() {
        return dateValueTextView;
    }

    public TextView getDescriptionValueTextView() {
        return descriptionValueTextView;
    }

    public TextView getNameValueTextView() {
        return nameValueTextView;
    }

    public ImageView getProfileImageView() {
        return profileImageView;
    }

    public TextView getTimeValueTextView() {
        return timeValueTextView;
    }

    public TextView getTextView() {
        return textView;
    }

    public View getRootView() {
        return rootView;
    }
}
