package service.sitter.recyclerview.requests.babysitter.archived;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import service.sitter.R;
import service.sitter.db.DataBase;
import service.sitter.db.IDataBase;
import service.sitter.models.Babysitter;
import service.sitter.models.Request;
import service.sitter.models.UserCategory;
import service.sitter.recyclerview.requests.babysitter.IRequestAdapterListener;
import service.sitter.ui.parent.connections.IOnGettingBabysitterFromDb;
import service.sitter.utils.DateUtils;
import service.sitter.utils.ImagesUtils;

public class ArchivedRequestAdapter extends RecyclerView.Adapter<ArchivedRequestViewHolder> {
    // Database:
    private final IDataBase db;
    // Requests
    private final List<Request> requests = new ArrayList<>();
    // Listener
    private final IRequestAdapterListener listener;
    private final UserCategory userCategory;
    private Context context;

    public ArchivedRequestAdapter(@NonNull IRequestAdapterListener listener, UserCategory userCategory, Context context) {
        this.userCategory = userCategory;
        this.context = context;
        // Init db.
        db = DataBase.getInstance();
        // Setup listeners
        this.listener = listener;
    }

    @NonNull
    @Override
    public ArchivedRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_babysitter_request_archived, parent, false);
        return new ArchivedRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArchivedRequestViewHolder holder, int position) {
        // Load
        Request request = requests.get(position);
        // Update values of view holder:
        holder.getDateValueTextView().setText(DateUtils.getFormattedDateFromString(request.getDate()));
        holder.getDescriptionValueTextView().setText(request.getDescription());
        holder.getTimeValueTextView().setText(request.getTime());
        // Fields that are extracted by the parent object (so db is needed).
        if (userCategory == UserCategory.Babysitter) {
            db.getParent(request.getPublisherId(), parent -> {
                // Assign fields from parent object.
                holder.getNameValueTextView().setText(parent.getFirstName());
//                Glide.with(this.context).load(parent.getImage()).into(holder.getProfileImageView());
                ImagesUtils.updateImageView(this.context, parent.getImage(), holder.getProfileImageView());

            }, null);
        } else {
            db.getBabysitter(request.getReceiverId(), new IOnGettingBabysitterFromDb() {
                @Override
                public void babysitterFound(Babysitter babysitter) {
                    // Assign fields from parent object.
                    holder.getNameValueTextView().setText(babysitter.getFullName());
//                Glide.with(this.context).load(babysitter.getImage()).into(holder.getProfileImageView());
                    ImagesUtils.updateImageView(context, babysitter.getImage(), holder.getProfileImageView());
                }

                @Override
                public void onFailure(String phoneNumber) {
                }

            });
        }

        // Set listeners:
        // Adapter passes the rootView that was clicked. The activity should initialize the adapter with specific listener
        if (listener != null) {
            holder.getRootView().setOnClickListener(v -> listener.onButtonClicked(request));
        }
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    /**
     * set new requests.
     *
     * @param requests are list of new requests that the adapter should load.
     */
    public void setRequests(List<Request> requests) {
        this.requests.clear();
        this.requests.addAll(requests);
        notifyDataSetChanged();
    }
}

