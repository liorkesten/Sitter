package service.sitter.recyclerview.requests.babysitter.archived;

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
import service.sitter.models.Parent;
import service.sitter.models.Request;
import service.sitter.recyclerview.requests.babysitter.IRequestAdapterListener;

public class ArchivedRequestAdapter extends RecyclerView.Adapter<ArchivedRequestViewHolder> {
    // Database:
    private final IDataBase db;
    // Requests
    private final List<Request> requests = new ArrayList<>();
    // Listener
    private final IRequestAdapterListener listener;

    public ArchivedRequestAdapter(@NonNull IRequestAdapterListener listener) {
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
        holder.getDateValueTextView().setText(request.getDate());
        holder.getDescriptionValueTextView().setText(request.getDescription());
        holder.getTimeValueTextView().setText(request.getTime());

        // Fields that are extracted by the parent object (so db is needed).
        db.getParent(request.getPublisherId(), doc -> {
            // Convert doc to parent.
            Parent parent = doc.toObject(Parent.class);
            // Assign fields from parent object.
            holder.getNameValueTextView().setText(parent.getFullName());
            // TODO Uncomment below line once Noam finish to update parent,GetImage returns Uri instead of String.
//            holder.getProfileImageView().setImageURI(parent.getImage());
        }, null);

        // Set listeners:
        // Adapter passes the rootView that was clicked. The activity should initialize the adapter with specific listener
        holder.getRootView().setOnClickListener(v -> listener.onButtonClicked(request));
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

