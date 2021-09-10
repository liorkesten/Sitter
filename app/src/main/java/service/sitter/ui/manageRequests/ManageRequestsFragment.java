package service.sitter.ui.manageRequests;

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

import java.util.ArrayList;
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


        RecyclerView recyclerViewUpcoming = root.findViewById(R.id.recycler_view_upcoming_requests);
        RequestAdapter upcomingRequestsAdapter = new RequestAdapter(request -> { /*TODO Implement this listener*/});
//        ChildAdapter childAdapter = new ChildAdapter(child -> { /*TODO Implement this listener*/});
        recyclerViewUpcoming.setAdapter(upcomingRequestsAdapter);
        List<Request> requests = new ArrayList<>();
//        children.add(new Child("Dana", 5));
//        children.add(new Child("Noam", 3));
//        children.add(new Child("Lior", 27));
//        children.add(new Child("Nir", 26));
//        children.add(new Child("Roy", 26));
//        children.add(new Child("Keren", 26));
        upcomingRequestsAdapter.setRequests(requests);
        recyclerViewUpcoming.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));


//        final TextView textView = binding.textDashboard;
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}