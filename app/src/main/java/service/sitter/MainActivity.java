package service.sitter;

import android.location.Location;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

import service.sitter.databinding.ActivityMainBinding;
import service.sitter.db.DataBase;
import service.sitter.db.IDataBase;
import service.sitter.models.Babysitter;
import service.sitter.models.Connection;
import service.sitter.models.Parent;
import service.sitter.models.Recommendation;
import service.sitter.models.Request;
import service.sitter.models.User;
import service.sitter.models.UserCategory;

import static android.location.LocationManager.GPS_PROVIDER;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IDataBase db = DataBase.getInstance();
        db.addUser(new Parent("Lior", "Kesten", "Kestenlior@gmail.com", "0547718647", "Ramat Hasharon", "image_of_lior"));
        db.addUser(new Babysitter("Dana", "Adam", "dana.adam.mail@gmail.com", "00000000", "Ramat Hasharon", "image_of_dana"));
        db.addRequest(new Request("publisher", "receiver", new Date(), LocalTime.now(), LocalTime.now(), new Location(""), new ArrayList<>(), 40, "hello"));
        db.addConnection(new Connection("lior", "dana"));
        db.addRecommendation(new Recommendation());

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

}