package service.sitter.login.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import service.sitter.R;
import service.sitter.databinding.FragmentSetProfileParentBinding;
import service.sitter.db.DataBase;
import service.sitter.db.IDataBase;
import service.sitter.models.Child;
import service.sitter.recyclerview.children.ChildAdapter;
import service.sitter.ui.fragments.PaymentFragment;

public class SetProfileParentFragment extends Fragment {
    private static final Uri DEFAULT_URI_CHILD_PICTURE = Uri.parse("android.resource://sitter/drawable/child_icon");
    private static final int RESULT_CODE_IMAGE = 100;
    private static final String TAG = SetProfileParentFragment.class.getSimpleName();

    private ImageView imageView;
    private IDataBase db;
    private FragmentSetProfileParentBinding binding;
    private MutableLiveData<List<Child>> mutableLiveDataChildren;
    private List<Child> children;
    private Integer paymentLiveData;
    private Uri lastChildUri;
    private ChildAdapter childAdapter;


    public SetProfileParentFragment() {
        super(R.layout.fragment_set_profile_parent);
        mutableLiveDataChildren = new MutableLiveData<>();
        children = new ArrayList<>();
        lastChildUri = DEFAULT_URI_CHILD_PICTURE;
        db = DataBase.getInstance();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "created");
        binding = FragmentSetProfileParentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        PaymentFragment paymentFragment = new PaymentFragment();
        RecyclerView recyclerView = root.findViewById(R.id.recycler_view_children);
        childAdapter = new ChildAdapter(child -> { /*TODO Implement this listener*/}, false);
        recyclerView.setAdapter(childAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        FloatingActionButton addChildButton = root.findViewById(R.id.fab_add_child);

        // children adapter
        mutableLiveDataChildren.observeForever(children -> childAdapter.setChildren(children));
        paymentFragment.getLiveData().observeForever(payment -> this.paymentLiveData = payment);

        // child button listener
        addChildButton.setOnClickListener(v -> openDialogAddChild());
        paymentLiveData = paymentFragment.getLiveData().getValue();


        // Render fragments
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.payment_fragment_container_view, paymentFragment)
                .commit();

        return root;
    }

    private void openDialogAddChild() {
        // Set UI Components
        lastChildUri = DEFAULT_URI_CHILD_PICTURE;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Child");
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_child, (ViewGroup) getView(), false);
        final EditText editTextChildName = (EditText) viewInflated.findViewById(R.id.edit_text_child_name);
        final EditText editTextChildBirthday = (EditText) viewInflated.findViewById(R.id.edit_text_child_birthday);
        builder.setView(viewInflated);

        // image uploading
        imageView = (ImageView) viewInflated.findViewById(R.id.image_view);
        Button button = (Button) viewInflated.findViewById(R.id.button_load_picture);
        button.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_CODE_IMAGE); //activity result method call
        });

        // confirm
        builder.setPositiveButton(android.R.string.ok, (dialog, which) ->
        {
            dialog.dismiss();
            // add child
            Child child = new Child(editTextChildName.getText().toString(),
                    editTextChildBirthday.getText().toString(),
                    lastChildUri);
            children.add(child);
            mutableLiveDataChildren.setValue(children);
            childAdapter.setChildren(children);
        });
        // cancel
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());
        builder.show();


    }


    public LiveData<List<Child>> getLiveDataChildren() {
        return mutableLiveDataChildren;
    }

    public Integer getPayment() {
        return paymentLiveData;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // hardcoded -1
        if (resultCode == -1) {
            if (requestCode == RESULT_CODE_IMAGE) {
                lastChildUri = Uri.parse(data.getData().toString());
                Bitmap bitmapImage = null;
                try {
                    bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), lastChildUri);
                    imageView.setImageBitmap(bitmapImage);
//                    db.uploadImage(lastChildUri);
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }
    }

}
