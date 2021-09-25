package service.sitter.recyclerview.requests.babysitter.incoming;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import service.sitter.R;

public class IncomingRequestViewHolder extends RecyclerView.ViewHolder {
    // Main Views
    private TextView textView;
    private final View rootView;

    // Buttons
    private final ImageButton acceptButton;
    private final ImageButton denyButton;

    // Views
    private TextView dateValueTextView;
    private TextView timeValueTextView;
    private TextView descriptionValueTextView;
    private TextView nameValueTextView;
    private ImageView profileImageView;

    public IncomingRequestViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        // Main views
        rootView = itemView;
        // Buttons:
        acceptButton = itemView.findViewById(R.id.item_babysitter_request_incoming_accept_button);
        denyButton = itemView.findViewById(R.id.item_babysitter_request_incoming_deny_button);

        // Views:
        dateValueTextView = itemView.findViewById(R.id.item_babysitter_request_date_value);
        timeValueTextView = itemView.findViewById(R.id.item_babysitter_request_time_value);
        descriptionValueTextView = itemView.findViewById(R.id.item_babysitter_request_desc_value);
        nameValueTextView = itemView.findViewById(R.id.item_babysitter_request_name_value);
        profileImageView = itemView.findViewById(R.id.item_babysitter_request_profile_image);
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

    public ImageButton getAcceptButton() {
        return acceptButton;
    }

    public ImageButton getDenyButton() {
        return denyButton;
    }

    public TextView getTextView() {
        return textView;
    }

    public View getRootView() {
        return rootView;
    }
}
