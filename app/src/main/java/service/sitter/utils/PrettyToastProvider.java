package service.sitter.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import service.sitter.R;

public class PrettyToastProvider {
    private View layout;

    public PrettyToastProvider(LayoutInflater layoutInflater, View root) {
        layout = layoutInflater.inflate(R.layout.pretty_toast,
                (ViewGroup) root.findViewById(R.id.toast_layout_root));
    }

    public PrettyToastProvider(LayoutInflater layoutInflater, AppCompatActivity activity) {
        layout = layoutInflater.inflate(R.layout.pretty_toast,
                (ViewGroup) activity.findViewById(R.id.toast_layout_root));
    }

    public void showToast(String textForToast, Context context) {
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(textForToast);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
