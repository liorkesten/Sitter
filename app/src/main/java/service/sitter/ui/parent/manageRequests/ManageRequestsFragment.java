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
import service.sitter.models.Request;
import service.sitter.models.RequestStatus;
import service.sitter.recyclerview.requests.RequestAdapter;
import service.sitter.utils.SharedPreferencesUtils;

public class ManageRequestsFragment extends Fragment {
    private static final String TAG = ManageRequestsFragment.class.getSimpleName();
    private SharedPreferences sp;
    private ManageRequestsViewModel dashboardViewModel;
    private FragmentManageRequestsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        dashboardViewModel =
                new ViewModelProvider(this).get(ManageRequestsViewModel.class);

        binding = FragmentManageRequestsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setRecyclerView(root, R.id.recycler_view_upcoming_requests, RequestStatus.Pending);
        setRecyclerView(root, R.id.recycler_view_approved_requests, RequestStatus.Approved);
        setRecyclerView(root, R.id.recycler_view_history_requests, RequestStatus.Archived);


//        final TextView textView = binding.textDashboard;
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }

    private void setRecyclerView(View root, int recyclerViewId, RequestStatus requestStatus) {
        RecyclerView recyclerViewUpcoming = root.findViewById(recyclerViewId);
        RequestAdapter requestAdapter = new RequestAdapter(request -> { /*TODO Implement this listener*/});

        LiveData<List<Request>> requestsLiveData = getLiveDataOfRequests(requestStatus);
        if (requestsLiveData == null) {
            //TODO
        }
        requestsLiveData.observeForever(requests -> {
            if (requests == null) {
                Log.e(TAG, "Requests is nil");
            } else {
                Log.d(TAG, "Set new requests for request adapter -  " + requests);
                requestAdapter.setRequests(requests);
            }
        });
        recyclerViewUpcoming.setAdapter(requestAdapter);

        recyclerViewUpcoming.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private LiveData<List<Request>> getLiveDataOfRequests(RequestStatus requestStatus) {
        IDataBase db = DataBase.getInstance();

        //TODO Change this parent from constant to parent from SP.
        //        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String parentId = SharedPreferencesUtils.getParentFromSP(sp).getUuid();
        switch (requestStatus) {
            case Pending:
                return db.getLiveDataPendingRequestsOfParent(parentId);
            case Approved:
                return db.getLiveDataApprovedRequestsOfParent(parentId);
            case Deleted:
                return db.getLiveDataDeletedRequestsOfParent(parentId);
            case Archived:
                return db.getLiveDataArchivedRequestsOfParent(parentId);
            default:
                return null;

        }

    }
}