package service.sitter.ui.parent.manageRequests;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import service.sitter.R;
import service.sitter.databinding.FragmentManageRequestsBinding;
import service.sitter.db.DataBase;
import service.sitter.db.IDataBase;
import service.sitter.models.Parent;
import service.sitter.models.Request;
import service.sitter.models.RequestStatus;
import service.sitter.recyclerview.requests.babysitter.approved.ApprovedRequestAdapter;
import service.sitter.recyclerview.requests.babysitter.archived.ArchivedRequestAdapter;
import service.sitter.recyclerview.requests.babysitter.incoming.IncomingRequestAdapter;
import service.sitter.utils.SharedPreferencesUtils;

public class ManageRequestsFragment extends Fragment {
    private static final String TAG = ManageRequestsFragment.class.getSimpleName();

    private SharedPreferences sp;
    private IDataBase db;

    private Parent parent;

    private ManageRequestsViewModel dashboardViewModel;
    private FragmentManageRequestsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Load db:
        sp = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplication());
        db = DataBase.getInstance();

        // Extract babysitter from SP.
        parent = SharedPreferencesUtils.getParentFromSP(sp);

        dashboardViewModel =
                new ViewModelProvider(this).get(ManageRequestsViewModel.class);

        binding = FragmentManageRequestsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setRecyclerView(root, R.id.recycler_view_upcoming_requests, RequestStatus.Pending);
        setRecyclerView(root, R.id.recycler_view_approved_requests, RequestStatus.Approved);
        setRecyclerView(root, R.id.recycler_view_history_requests, RequestStatus.Archived);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setRecyclerView(View root, int recyclerViewId, RequestStatus requestStatus) {
        RecyclerView recyclerView = root.findViewById(recyclerViewId);
        recyclerView.setAdapter(getAdapter(requestStatus));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
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
        IncomingRequestAdapter adapter = new IncomingRequestAdapter(/*TODO*/null, /*TODO*/null,/*TODO*/ r -> db.deleteRequest(r.getUuid()) /*TODO add popup that asks if the user sure that he wants to delete the request*/);
        // SetAdapter
        LiveData<List<Request>> requestsLiveData = db.getLiveDataPendingRequestsOfParent(parent.getUuid());
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
        ApprovedRequestAdapter adapter = new ApprovedRequestAdapter(/*TODO*/null, /*TODO*/ null, r -> db.deleteRequest(r.getUuid()) /*TODO add popup that asks if the user sure that he wants to delete the request*/);
        // SetAdapter
        LiveData<List<Request>> requestsLiveData = db.getLiveDataApprovedRequestsOfParent(parent.getUuid());
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
        LiveData<List<Request>> requestsLiveData = db.getLiveDataArchivedRequestsOfParent(parent.getUuid());
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