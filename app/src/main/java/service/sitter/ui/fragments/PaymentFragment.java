package service.sitter.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.slider.Slider;

import service.sitter.R;
import service.sitter.login.fragments.SetProfileParentFragment;

public class PaymentFragment extends Fragment {

    private static final String TAG = PaymentFragment.class.getSimpleName();


    private String currentTime;
    private MutableLiveData<Integer> paymentLiveData;
    com.google.android.material.slider.Slider paymentSlider;
    private static final int DEFAULT_PAYMENT = 50;
    private int defaultPayment;


    public PaymentFragment() {
        super(R.layout.fragment_payment);
        this.defaultPayment = DEFAULT_PAYMENT;
        paymentLiveData = new MutableLiveData<>(DEFAULT_PAYMENT);
    }

    public PaymentFragment(int defaultPayment) {
        super(R.layout.fragment_payment);
        this.defaultPayment = defaultPayment;
        paymentLiveData = new MutableLiveData<>(defaultPayment);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        Log.d("PaymentFragment onViewCreated", "created");

        paymentSlider = (com.google.android.material.slider.Slider) view.findViewById(R.id.paymentSlider);
        paymentSlider.setValue(defaultPayment);
        paymentSlider.addOnChangeListener((slider, value, fromUser) -> {
            Log.d(TAG, "payment changed to: " + value);
            paymentLiveData.setValue((int) value);
        });

        return view;
    }

    public @NonNull
    LiveData<Integer> getLiveData() {
        return paymentLiveData;
    }

}
