package service.sitter.ui.parent.connections;

import android.Manifest;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import service.sitter.models.Babysitter;
import service.sitter.models.Connection;
import service.sitter.models.Parent;
import service.sitter.models.Recommendation;
import service.sitter.recommendations.CallLogsRecommendationProvider;
import service.sitter.recyclerview.connections.ConnectionAdapter;
import service.sitter.recyclerview.recommendations.RecommendationAdapter;
import service.sitter.utils.PrettyToastProvider;
import service.sitter.utils.RecyclerViewUtils;
import service.sitter.utils.SharedPreferencesUtils;

public class ConnectionsFragment extends Fragment {
    private static final String TAG = ConnectionsFragment.class.getSimpleName();
    private ActivityResultLauncher<String[]> activityResultLauncher;


    private IDataBase db;
    private SharedPreferences sp;
    private Parent myUser;

    private FragmentConnectionsBinding binding;
    private List<Connection> connections = new ArrayList<>();
    private List<Recommendation> recommendations = new ArrayList<>();

    EditText editTextAddConnection;
    ImageButton add_connection_button;
    private PrettyToastProvider prettyToastProvider;
    private LiveData<List<Connection>> connectionsLiveData;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Set Logic Business Components
        db = DataBase.getInstance();
        sp = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplication());
        myUser = SharedPreferencesUtils.getParentFromSP(sp);

        binding = FragmentConnectionsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        LayoutInflater inflaterToast = getLayoutInflater();
        prettyToastProvider = new PrettyToastProvider(inflaterToast, root);
        // Load UI Components:
        // Connections:
        editTextAddConnection = (EditText) root.findViewById(R.id.edit_text_add_connection);
        add_connection_button = (ImageButton) root.findViewById(R.id.add_connection_button);


        // Set logic
        setAddConnectionButton();
        setConnectionRecyclerView(root);
        setRecommendationRecyclerView(root);

        // Call recommendations providers
        getRecommendationsFromProviders();

        return root;
    }

    private void getRecommendationsFromProviders() {
        getRecommendationFromCallLogsProvider();
    }

    private void getRecommendationFromCallLogsProvider() {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            Log.e("activityResultLauncher", "" + result.toString());
            Boolean areAllGranted = true;
            for (Boolean b : result.values()) {
                areAllGranted = areAllGranted && b;
            }

            if (areAllGranted) {
                new CallLogsRecommendationProvider().getRecommendations(getContext(), myUser.getUuid());
            }
        });

        String[] appPerms;
        appPerms = new String[]{Manifest.permission.READ_CALL_LOG};
        this.activityResultLauncher.launch(appPerms);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setAddConnectionButton() {
        add_connection_button.setOnClickListener(v -> {
            String phoneNumber = editTextAddConnection.getText().toString();
            if (phoneNumber == null || phoneNumber.length() != 10) {
                //Log.d(TAG, "can't click on add connection button - phone number should be with 10 chars");
                prettyToastProvider.showToast("Please enter valid phone - 10 digits", getActivity().getApplication());
                return;
            }
            //Log.d(TAG, "add_connection_button was clicked");
            prettyToastProvider.showToast("looking for babysitter...", this.getContext());
            db.getBabysitterByPhoneNumber(phoneNumber, new IOnGettingBabysitterFromDb() {
                @Override
                public void babysitterFound(Babysitter babysitter) {
                    db.addConnection(myUser, babysitter);
                }

                @Override
                public void onFailure(String phoneNumber) {
                    prettyToastProvider.showToast(String.format("Could not find any babysitter with phone number %s", phoneNumber), getActivity().getApplication());
                }
            });
        });
    }


    private void setConnectionRecyclerView(View root) {
        RecyclerView recyclerViewConnection = root.findViewById(R.id.recycler_view_my_connections);
        TextView viewById = root.findViewById(R.id.text_recycler_view_connections);
        ConnectionAdapter connectionAdapter = new ConnectionAdapter(this::areYouSureYouWantToDeleteConnectionDialog, getActivity().getApplication());
        connectionsLiveData = db.getLiveDataConnectionsOfParent(myUser.getUuid());
        if (connectionsLiveData == null) {
            //TODO
        }
        connectionsLiveData.observeForever(connections -> {
            if (connections == null) {
                Log.e(TAG, "Connections is nil");
            } else {
                //Log.d(TAG, "Set new connections for connection adapter -  " + connections);
                connectionAdapter.setConnections(connections);
                this.connections.clear();
                this.connections.addAll(connections);
                RecyclerViewUtils.switchBetweenRecAndText(root, connections, R.id.recycler_view_my_connections, R.id.text_recycler_view_connections);
            }
        });
        recyclerViewConnection.setAdapter(connectionAdapter);

        recyclerViewConnection.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
    }

    private void setRecommendationRecyclerView(View root) {
        RecyclerView recyclerView = root.findViewById(R.id.recycler_view_recommendations);
        RecommendationAdapter adapter = new RecommendationAdapter(recommendation -> {
            db.deleteRecommendation(recommendation.getUuid());
            db.addConnection(recommendation.getConnection());
        }, getActivity().getApplication());
        LiveData<List<Recommendation>> recommendationsLiveData = db.getLiveDataRecommendationsOfParent(myUser.getUuid());
        if (recommendationsLiveData == null) {
            //TODO
        }
        recommendationsLiveData.observeForever(recommendations -> {
            if (recommendations == null) {
                Log.e(TAG, "recommendations is nil");
            } else {
                //Log.d(TAG, "Set new recommendations for recommendation adapter -  " + recommendations);
                this.recommendations.clear();
                this.recommendations.addAll(recommendations);
                RecyclerViewUtils.switchBetweenRecAndText(root, recommendations, R.id.recycler_view_recommendations, R.id.text_recycler_view_recommendations);
                adapter.setRecommendations(recommendations);
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
    }





    private void areYouSureYouWantToDeleteConnectionDialog(Connection connection) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Delete connection");
        builder.setMessage("Are you sure you want to delete this connection?");

        builder.setPositiveButton("YES", (dialog, which) -> {
            db.deleteConnection(connection.getUuid());
            dialog.dismiss();
        });

        builder.setNegativeButton("NO", (dialog, which) -> {
            // Do nothing
            dialog.dismiss();
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

}

