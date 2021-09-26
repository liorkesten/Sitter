package service.sitter.ui.parent.connections;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import service.sitter.R;
import service.sitter.databinding.FragmentConnectionsBinding;
import service.sitter.db.DataBase;
import service.sitter.db.IDataBase;
import service.sitter.models.Connection;
import service.sitter.models.Parent;
import service.sitter.models.Recommendation;
import service.sitter.recyclerview.connections.ConnectionAdapter;
import service.sitter.recyclerview.recommendations.RecommendationAdapter;
import service.sitter.utils.SharedPreferencesUtils;

public class ConnectionsFragment extends Fragment {
    private static final String TAG = ConnectionsFragment.class.getSimpleName();

    private IDataBase db;
    private SharedPreferences sp;
    private Parent myUser;

    private FragmentConnectionsBinding binding;
    private List<Connection> connections = new ArrayList<>();
    private List<Recommendation> recommendations = new ArrayList<>();

    EditText editTextAddConnection;
    ImageButton add_connection_button;
    RecyclerView myConnectionsRecyclerView;
    RecyclerView recommendationsRecyclerView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Set Logic Business Components
        db = DataBase.getInstance();
        sp = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplication());
        myUser = SharedPreferencesUtils.getParentFromSP(sp);

        binding = FragmentConnectionsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Load UI Components:
        // Connections:
        editTextAddConnection = (EditText) root.findViewById(R.id.edit_text_add_connection);
        add_connection_button = (ImageButton) root.findViewById(R.id.add_connection_button);

        // Recommendations:
        recommendationsRecyclerView = root.findViewById(R.id.recycler_view_recommendations);
        RecommendationAdapter recommendationAdapter = new RecommendationAdapter(recommendation -> { /*TODO Implement this listener*/});
        recommendationsRecyclerView.setAdapter(recommendationAdapter);
        recommendationAdapter.setRecommendations(recommendations);
        recommendationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));


        // Set logic
        setAddConnectionButton();
        setConnectionRecyclerView(root);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setAddConnectionButton() {
        // Default set button to false.
        add_connection_button.setEnabled(false);
        // Enable button only if were inserted 10 chars (length of phone number).
        editTextAddConnection.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                add_connection_button.setEnabled(s.toString().trim().length() == 10);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

        add_connection_button.setOnClickListener(v -> {
            Log.e(TAG, "add_connection_button was clicked");
            // Check if phone number is user in db.
            String phoneNumber = editTextAddConnection.getText().toString();
            db.getBabysitterByPhoneNumber(phoneNumber, b -> db.addConnection(myUser, b));
        });
    }


    private void setConnectionRecyclerView(View root) {
        RecyclerView recyclerViewConnection = root.findViewById(R.id.recycler_view_my_connections);
        ConnectionAdapter connectionAdapter = new ConnectionAdapter(connection -> { /*TODO Implement this listener*/});
        LiveData<List<Connection>> connectionsLiveData = db.getLiveDataConnectionsOfParent(myUser.getUuid());
        if (connectionsLiveData == null) {
            //TODO
        }
        connectionsLiveData.observeForever(connections -> {
            if (connections == null) {
                Log.e(TAG, "Connections is nil");
            } else {
                Log.d(TAG, "Set new connections for connection adapter -  " + connections);
                connectionAdapter.setConnections(connections);
            }
        });
        recyclerViewConnection.setAdapter(connectionAdapter);

        recyclerViewConnection.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
    }
}

