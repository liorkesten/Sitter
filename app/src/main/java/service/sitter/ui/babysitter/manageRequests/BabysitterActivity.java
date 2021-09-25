package service.sitter.ui.babysitter.manageRequests;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import service.sitter.R;

public class BabysitterActivity extends AppCompatActivity {
    private static final String TAG = BabysitterActivity.class.getSimpleName();
    private SharedPreferences sp;
    private ManageRequestsViewModel dashboardViewModel;
    private BabysitterActivity binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_babysitter);
    }
}