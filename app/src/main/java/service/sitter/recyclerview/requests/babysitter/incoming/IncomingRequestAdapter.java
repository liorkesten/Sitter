package service.sitter.recyclerview.requests.babysitter.incoming;

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
import service.sitter.models.Request;
import service.sitter.models.UserCategory;
import service.sitter.recyclerview.requests.babysitter.IRequestAdapterListener;
import service.sitter.utils.ImagesUtils;

public class IncomingRequestAdapter extends RecyclerView.Adapter<IncomingRequestViewHolder> {
    // Database:
    private final IDataBase db;
    // Requests
    private final List<Request> requests = new ArrayList<>();
    // Listener
    private final IRequestAdapterListener listener;
    private final IRequestAdapterListener acceptButtonListener;
    private final IRequestAdapterListener denyButtonListener;
    private final UserCategory userCategory;
    private Context context;

    public IncomingRequestAdapter(@NonNull IRequestAdapterListener listener, IRequestAdapterListener acceptButtonListener, IRequestAdapterListener denyButtonListener, UserCategory userCategory, Context context) {
        this.userCategory = userCategory;
        this.context = context;
        // Init db.
        db = DataBase.getInstance();
        // Setup listeners
        this.listener = listener;
        this.acceptButtonListener = acceptButtonListener;
        this.denyButtonListener = denyButtonListener;
    }

    @NonNull
    @Override
    public IncomingRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_babysitter_request_incoming, parent, false);
        return new IncomingRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncomingRequestViewHolder holder, int position) {
        // Load
        Request request = requests.get(position);
        // Update values of view holder:
        holder.getDateValueTextView().setText(request.getDate());
        holder.getDescriptionValueTextView().setText(request.getDescription());
        holder.getTimeValueTextView().setText(request.getTime());

        // Fields that are extracted by the parent object (so db is needed).
        if (userCategory == UserCategory.Babysitter) {

            db.getParent(request.getPublisherId(), parent -> {
                // Assign fields from parent object.
                holder.getNameValueTextView().setText(parent.getFullName());
//                Glide.with(this.context).load(parent.getImage()).into(holder.getProfileImageView());
                ImagesUtils.updateImageView(this.context, parent.getImage(), holder.getProfileImageView());

            }, null);
        }

        // Set listeners:
        // Adapter passes the rootView that was clicked. The activity should initialize the adapter with specific listener
        if (listener != null) {
            holder.getRootView().setOnClickListener(v -> listener.onButtonClicked(request));
        }
        // In case that the acceptButtonListener is null, the button should be gone.
        if (acceptButtonListener == null) {
            holder.getAcceptButton().setVisibility(View.GONE);
        } else {
            holder.getAcceptButton().setOnClickListener(v -> acceptButtonListener.onButtonClicked(request));
        }
        holder.getDenyButton().setOnClickListener(v -> denyButtonListener.onButtonClicked(request));
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

