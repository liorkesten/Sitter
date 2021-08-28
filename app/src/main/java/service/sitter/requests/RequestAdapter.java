package huji.postpc.exercises.sitter.requests;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import huji.postpc.exercises.sitter.R;
import huji.postpc.exercises.sitter.models.Request;

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
        requests.clear();
        requests.addAll(requests);
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_reuqest, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RequestViewHolder holder, int position) {
        Request request = requests.get(position);
        holder.textView.setText(request.toString()); // TODO Change from to string.

        // Adapter passes the rootView that was clicked. The activity should intilizae the adapter with specific listener.
        holder.rootView.setOnClickListener(v -> listener.onRequestClick(request));
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }
}

