package service.sitter.recyclerview.requests.babysitter.approved;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import service.sitter.R;

public class ApprovedRequestViewHolder extends RecyclerView.ViewHolder {
    // Main Views
    private TextView textView;
    private final View rootView;

    // Buttons
    private final ImageButton calenderButton;
    private final ImageButton cancelButton;

    // Views
    private TextView dateValueTextView;
    private TextView timeValueTextView;
    private TextView descriptionValueTextView;
    private TextView nameValueTextView;
    private ImageView profileImageView;

    public ApprovedRequestViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        // Main views
        rootView = itemView;
        // Buttons:
        calenderButton = itemView.findViewById(R.id.item_babysitter_request_approved_calendar_button);
        cancelButton = itemView.findViewById(R.id.item_babysitter_request_approved_cancel_button);

        // Views:
        dateValueTextView = itemView.findViewById(R.id.item_babysitter_request_approved_date_value);
        timeValueTextView = itemView.findViewById(R.id.item_babysitter_request_approved_time_value);
        descriptionValueTextView = itemView.findViewById(R.id.item_babysitter_request_approved_desc_value);
        nameValueTextView = itemView.findViewById(R.id.item_babysitter_request_approved_name_value);
        profileImageView = itemView.findViewById(R.id.item_babysitter_request_approved_profile_image);
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

    public ImageButton getCalenderButton() {
        return calenderButton;
    }

    public ImageButton getCancelButton() {
        return cancelButton;
    }

    public TextView getTextView() {
        return textView;
    }

    public View getRootView() {
        return rootView;
    }
}
