package com.unipi.atheodoridis.nfccardapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.unipi.atheodoridis.nfccardapp.ui.home.HomeFragment;


public class SuccessFragment extends DialogFragment {


    private TextView textView;
private ImageView imageView;
private Listener mListener;
    public static final String TAG = HomeFragment.class.getSimpleName();
    public static SuccessFragment newInstance() {

        return new SuccessFragment();
    }

    public SuccessFragment() {
        // Required empty public constructor
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_success,container,false);
        initViews(view);
        return view;
    }
    private void initViews(View view) {

        textView = (TextView) view.findViewById(R.id.title);
        imageView =  (ImageView) view.findViewById(R.id.logo);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (ProfileActivity)context;
        mListener.onDialogDisplayed();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener.onDialogDismissed();
    }
}