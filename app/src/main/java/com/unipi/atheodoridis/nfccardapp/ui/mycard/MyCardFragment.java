package com.unipi.atheodoridis.nfccardapp.ui.mycard;

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

import com.unipi.atheodoridis.nfccardapp.R;

public class MyCardFragment extends Fragment {

    private MyCardViewModel myCardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myCardViewModel =
                new ViewModelProvider(this).get(MyCardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_mycard, container, false);
        final TextView textView = root.findViewById(R.id.text_mycard);
        myCardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}