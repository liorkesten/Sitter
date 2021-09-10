package service.sitter.ui.manageRequests;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import service.sitter.R;
import service.sitter.databinding.FragmentManageRequestsBinding;
import service.sitter.models.Child;
import service.sitter.models.Request;
import service.sitter.recyclerview.children.ChildAdapter;
import service.sitter.recyclerview.requests.RequestAdapter;

public class ManageRequestsFragment extends Fragment {

    private ManageRequestsViewModel dashboardViewModel;
    private FragmentManageRequestsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(ManageRequestsViewModel.class);

        binding = FragmentManageRequestsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setRecyclerView(root, R.id.recycler_view_upcoming_requests);
        setRecyclerView(root, R.id.recycler_view_approved_requests);
        setRecyclerView(root, R.id.recycler_view_history_requests);


//        final TextView textView = binding.textDashboard;
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }

    private void setRecyclerView(View root, int recyclerViewId) {
        RecyclerView recyclerViewUpcoming = root.findViewById(recyclerViewId);
        RequestAdapter upcomingRequestsAdapter = new RequestAdapter(request -> { /*TODO Implement this listener*/});
        recyclerViewUpcoming.setAdapter(upcomingRequestsAdapter);
        List<Request> requests = new ArrayList<>();
        List<Child> children = new ArrayList<>();
        requests.add(new Request("111", new Date("10/10/10"), LocalTime.parse("10:00"), LocalTime.parse("11:00"), new Location("noam"), children, 100, "New request"));
        requests.add(new Request("222", new Date("11/11/10"), LocalTime.parse("10:00"), LocalTime.parse("11:00"), new Location("noam"), children, 100, "New request"));

        upcomingRequestsAdapter.setRequests(requests);
        recyclerViewUpcoming.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}