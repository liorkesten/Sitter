package service.sitter.recyclerview.requests.babysitter.approved;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

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

public class ApprovedRequestAdapter extends RecyclerView.Adapter<ApprovedRequestViewHolder> {
    // Database:
    private final IDataBase db;
    // Requests
    private final List<Request> requests = new ArrayList<>();
    // Listener
    private IRequestAdapterListener listener;
    private final IRequestAdapterListener calenderButtonListener;
    private final IRequestAdapterListener cancelButtonListener;

    private final UserCategory userCategory;
    private final Context context;

    public ApprovedRequestAdapter(@NonNull IRequestAdapterListener listener, IRequestAdapterListener calenderButtonListener, IRequestAdapterListener cancelButtonListener, UserCategory userCategory, Context context) {
        // Init db.
        db = DataBase.getInstance();
        // Setup listeners
        this.listener = listener;
        this.calenderButtonListener = calenderButtonListener;
        this.cancelButtonListener = cancelButtonListener;

        this.userCategory = userCategory;
        this.context = context;
    }

    private void openPopUpWindow(View v, Request request, Babysitter babysitter, View viewInflated){
        //View viewInflated = LayoutInflater.from(context).inflate(R.layout.popup_layout, (ViewGroup) (v), false);
        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(viewInflated, width, height, focusable);
        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        TextView description = viewInflated.findViewById(R.id.item_babysitter_request_archived_desc_value);
        TextView date = viewInflated.findViewById(R.id.item_babysitter_request_archived_date_value);
        TextView time = viewInflated.findViewById(R.id.item_babysitter_request_archived_time_value);
        // Update values of view holder:

        description.setText(request.getDescription());
        date.setText(request.getDate());
        time.setText(request.getTime());

        // dismiss the popup window when touched
        viewInflated.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    @NonNull
    @Override
    public ApprovedRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_babysitter_request_approved, parent, false);
        return new ApprovedRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApprovedRequestViewHolder holder, int position) {
        // Load
        Request request = requests.get(position);
        holder.getDateValueTextView().setText(request.getDate());
        //holder.getDescriptionValueTextView().setText(request.getDescription());
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
                    View viewInflated = LayoutInflater.from(holder.getRootView().getContext()).inflate(R.layout.popup_layout, (ViewGroup) holder.getRootView(), false);
                    listener = request1 -> openPopUpWindow(holder.getRootView(), request, babysitter, viewInflated);
                    TextView name = viewInflated.findViewById(R.id.item_babysitter_request_archived_name_value);
                    TextView mobility = viewInflated.findViewById(R.id.item_babysitter_mobility_edit);
                    name.setText(babysitter.getFirstName());
                    if (babysitter.isHasCar())
                        mobility.setText(R.string.yes);
                    // Assign fields from parent object.
                    holder.getNameValueTextView().setText(babysitter.getFirstName());
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
        holder.getCalenderButton().setOnClickListener(v -> calenderButtonListener.onButtonClicked(request));
        holder.getCancelButton().setOnClickListener(v -> cancelButtonListener.onButtonClicked(request));
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

