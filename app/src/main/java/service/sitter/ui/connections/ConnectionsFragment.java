package service.sitter.ui.connections;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import service.sitter.R;
import service.sitter.databinding.FragmentConnectionsBinding;
import service.sitter.db.DataBase;
import service.sitter.db.IDataBase;
import service.sitter.models.Connection;
import service.sitter.models.Recommendation;
import service.sitter.recyclerview.connections.ConnectionAdapter;
import service.sitter.recyclerview.recommendations.RecommendationAdapter;

public class ConnectionsFragment extends Fragment {

    private ConnectionsViewModel connectionsViewModel;
    private FragmentConnectionsBinding binding;
    private List<Connection> connections = new ArrayList<>();
    private List<Recommendation> recommendations = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Set Logic Business Components
        IDataBase db = DataBase.getInstance();

        binding = FragmentConnectionsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        EditText editTextAddConnection = (EditText) root.findViewById(R.id.edit_text_add_connection);
        ImageButton add_connection_button = (ImageButton) root.findViewById(R.id.add_connection_button);

        // Connections:
        RecyclerView myConnectionsRecyclerView = root.findViewById(R.id.recycler_view_my_connections);
        ConnectionAdapter connectionAdapter = new ConnectionAdapter(connection -> { /*TODO Implement this listener*/});
        myConnectionsRecyclerView.setAdapter(connectionAdapter);
        connectionAdapter.setConnections(connections);
        myConnectionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        // Recommendations:
        RecyclerView recommendationsRecyclerView = root.findViewById(R.id.recycler_view_recommendations);
        RecommendationAdapter recommendationAdapter = new RecommendationAdapter(recommendation -> { /*TODO Implement this listener*/});
        recommendationsRecyclerView.setAdapter(recommendationAdapter);
        recommendationAdapter.setRecommendations(recommendations);
        recommendationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}