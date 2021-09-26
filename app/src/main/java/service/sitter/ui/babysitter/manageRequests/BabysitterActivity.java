package service.sitter.ui.babysitter.manageRequests;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import service.sitter.R;
import service.sitter.db.DataBase;
import service.sitter.db.IDataBase;
import service.sitter.models.Babysitter;
import service.sitter.models.Connection;
import service.sitter.models.Parent;
import service.sitter.models.Request;
import service.sitter.models.RequestStatus;
import service.sitter.recyclerview.requests.babysitter.approved.ApprovedRequestAdapter;
import service.sitter.recyclerview.requests.babysitter.archived.ArchivedRequestAdapter;
import service.sitter.recyclerview.requests.babysitter.incoming.IncomingRequestAdapter;
import service.sitter.utils.SharedPreferencesUtils;

public class BabysitterActivity extends AppCompatActivity {
    private static final String TAG = BabysitterActivity.class.getSimpleName();
    private SharedPreferences sp;
    private IDataBase db;
    private Babysitter babysitter;

    private ManageRequestsViewModel dashboardViewModel;
    private BabysitterActivity binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_babysitter);
        // Load db:
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        db = DataBase.getInstance();

        // Create objects for tests
        Babysitter temp_babysitter = new Babysitter("Lior", "Kesten", "kes@gmail.com", "00022221", "LA", "");
        Parent parent = new Parent("Lior", "Kesten", "kes@gmail.com", "00022221", "LA", "", new ArrayList<>(), 100);
        parent.setUuid("e030e301-463c-47c5-ac97-8f207ff79977");
        db.addConnection(new Connection(parent, temp_babysitter));

        // Extract babysitter from SP.
        SharedPreferencesUtils.saveBabysitterToSP(sp, temp_babysitter);
        babysitter = SharedPreferencesUtils.getBabysitterFromSP(sp);

        // Set recycler views
        setRecyclerView(R.id.babysitter_recycler_view_incoming_requests, RequestStatus.Pending);
        setRecyclerView(R.id.babysitter_recycler_view_approved_requests, RequestStatus.Approved);
        setRecyclerView(R.id.babysitter_recycler_view_history_requests, RequestStatus.Archived);
    }


    private void setRecyclerView(int recyclerViewId, RequestStatus requestStatus) {
        RecyclerView recyclerView = findViewById(recyclerViewId);
        recyclerView.setAdapter(getAdapter(requestStatus));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
    }

    private RecyclerView.Adapter getAdapter(RequestStatus requestStatus) {

        switch (requestStatus) {
            case Pending:
                return getIncomingRequestAdapter();

            case Approved:
                // TODO
                return getApprovedRequestAdapter();

            case Archived:
                // TODO
                return getArchivedRequestAdapter();

            case Deleted:
                // TODO
                return null;
            default:
                return null;

        }
    }

    @NonNull
    private IncomingRequestAdapter getIncomingRequestAdapter() {
        IncomingRequestAdapter adapter = new IncomingRequestAdapter(/*TODO*/null, r -> db.acceptRequestByBabysitter(r, babysitter),/*TODO*/ null);
        // SetAdapter
        LiveData<List<Request>> requestsLiveData = db.getLiveDataPendingRequestsOfBabysitter(babysitter.getUuid());
        if (requestsLiveData == null) {
            //TODO
        }
        requestsLiveData.observeForever(requests -> {
            if (requests == null) {
                Log.e(TAG, "Requests is nil");
            } else {
                Log.d(TAG, "Set new requests for request IncomingRequestAdapter adapter-  " + requests);
                adapter.setRequests(requests);
            }
        });

        return adapter;
    }

    @NonNull
    private ApprovedRequestAdapter getApprovedRequestAdapter() {
        ApprovedRequestAdapter adapter = new ApprovedRequestAdapter(/*TODO*/null, /*TODO*/ null, r -> db.cancelRequest(r, babysitter));
        // SetAdapter
        LiveData<List<Request>> requestsLiveData = db.getLiveDataApprovedRequestsOfBabysitter(babysitter.getUuid());
        if (requestsLiveData == null) {
            //TODO
        }
        requestsLiveData.observeForever(requests -> {
            if (requests == null) {
                Log.e(TAG, "Requests is nil");
            } else {
                Log.d(TAG, "Set new requests for  ApprovedRequestAdapter adapter-  " + requests);
                adapter.setRequests(requests);
            }
        });

        return adapter;
    }

    @NonNull
    private ArchivedRequestAdapter getArchivedRequestAdapter() {
        ArchivedRequestAdapter adapter = new ArchivedRequestAdapter(/*TODO*/null);
        // SetAdapter
        LiveData<List<Request>> requestsLiveData = db.getLiveDataArchivedRequestsOfBabysitter(babysitter.getUuid());
        if (requestsLiveData == null) {
            //TODO
        }
        requestsLiveData.observeForever(requests -> {
            if (requests == null) {
                Log.e(TAG, "Requests is nil");
            } else {
                Log.d(TAG, "Set new requests for  ArchivedRequestAdapter adapter-  " + requests);
                adapter.setRequests(requests);
            }
        });

        return adapter;
    }
}