package service.sitter.login.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import service.sitter.R;
import service.sitter.databinding.FragmentHomeBinding;
import service.sitter.databinding.FragmentSetProfileParentBinding;
import service.sitter.login.SetProfile;
import service.sitter.models.Child;
import service.sitter.recyclerview.children.ChildAdapter;
import service.sitter.ui.fragments.DateFragment;

public class SetProfileParentFragment extends Fragment {

    private FragmentSetProfileParentBinding binding;
    List<Child> children;
    private static final int PICK_IMAGE = 100;
    private static final String TAG = SetProfileParentFragment.class.getSimpleName();


    public SetProfileParentFragment() {
        super(R.layout.fragment_set_profile_parent);
        children = new ArrayList<>();


    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "created");
        binding = FragmentSetProfileParentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        RecyclerView recyclerView = root.findViewById(R.id.recycler_view_children);
        ChildAdapter childAdapter = new ChildAdapter(child -> { /*TODO Implement this listener*/});
        recyclerView.setAdapter(childAdapter);
        children.add(new Child("Daria", 1, "Daria"));
        children.add(new Child("Gali", 3, "Gali"));
        children.add(new Child("Mika", 5, "Mika"));
        childAdapter.setChildren(children);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));


        openDialogAddChild();

        return root;
    }

    private void openDialogAddChild() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Title");
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_child, (ViewGroup) getView(), false);
        final EditText editTextChildName = (EditText) viewInflated.findViewById(R.id.edit_text_child_name);
//        DateFragment dateFragment = new DateFragment("10/10/21");
        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss());
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String textFromEditText = "23-08-2013";
        LocalDate myDate = LocalDate.parse(textFromEditText, dateFormatter);
        String myText = myDate.format(dateFormatter);
        System.out.println("Formatted again: " + myText);

        // image uploading
        ImageView imageView = (ImageView) viewInflated.findViewById(R.id.image_view);
        Button button = (Button) viewInflated.findViewById(R.id.button_load_picture);
        button.setOnClickListener(v -> openGallery());

        builder.show();
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

//    public LiveData<Integer> getLiveData() {
//        return paymentLiveData;
//    }

}
