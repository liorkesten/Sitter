package service.sitter.recyclerview.requests.parent;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import service.sitter.R;
import service.sitter.models.Request;

public class RequestAdapter extends RecyclerView.Adapter<RequestViewHolder> {
    private final List<Request> requests = new ArrayList<>();
    private final IRequestAdapterListener listener;

    public RequestAdapter(@NonNull IRequestAdapterListener listener) {
        this.listener = listener;
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

    @NonNull
    @NotNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_request, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RequestViewHolder holder, int position) {
        Request request = requests.get(position);
        holder.textView.setText(request.getPublisherId()); // TODO Change from to string.
        Context context = holder.textView.getContext();

        // Adapter passes the rootView that was clicked. The activity should initialize the adapter with specific listener.
        holder.rootView.setOnClickListener(v -> openPopUpWindow(context));
    }


    private void openPopUpWindow(Context context){
        TextView tv = new TextView(context);
        PopupWindow popUp;
        LinearLayout layout = new LinearLayout(context);
        popUp = new PopupWindow();
        popUp.showAtLocation(layout, Gravity.BOTTOM, 10, 10);
        popUp.update(50, 50, 300, 80);
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }
}

