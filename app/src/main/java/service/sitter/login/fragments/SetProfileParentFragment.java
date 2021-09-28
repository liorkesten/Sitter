package service.sitter.login.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import service.sitter.R;
import service.sitter.databinding.FragmentSetProfileParentBinding;
import service.sitter.db.DataBase;
import service.sitter.db.IDataBase;
import service.sitter.models.Child;
import service.sitter.recyclerview.children.ChildAdapter;
import service.sitter.ui.fragments.PaymentFragment;
import service.sitter.utils.DateUtils;

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
        EditText editTextChildName = (EditText) viewInflated.findViewById(R.id.edit_text_child_name);
        EditText editTextChildBirthday = (EditText) viewInflated.findViewById(R.id.edit_text_child_birthday);
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
        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {

        });
        // cancel
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.dismiss());
        builder.setView(viewInflated);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            boolean allGood = true;
            if (editTextChildName.getText().toString().equals("")){
                editTextChildName.setError("Please enter a name");
                allGood = false;
            }
            if (editTextChildBirthday.getText().toString().equals("")){
                editTextChildBirthday.setError("Please enter date of birth");
                allGood = false;
            }
            if (allGood){
                dialog.dismiss();
                Child child = new Child(editTextChildName.getText().toString(),
                        editTextChildBirthday.getText().toString(),
                        lastChildUri);
                children.add(child);
                mutableLiveDataChildren.setValue(children);
                childAdapter.setChildren(children);
            }
        });

        TextWatcher tw = new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                        cal.set(Calendar.MONTH, mon-1);
                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    editTextChildBirthday.setText(current);
                    editTextChildBirthday.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };


        editTextChildBirthday.addTextChangedListener(tw);
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
                Picasso.get().load(lastChildUri).into(imageView);
            }
        }
    }

    private boolean isValidateChild(EditText editTextDate) {
        if (!DateUtils.isValidDate(editTextDate.getText().toString(), "dd/MM/yyyy")){
            editTextDate.setError("you must enter date of child in format dd/NN/yyyy");
            return false;
        }
        return true;
    }

}
