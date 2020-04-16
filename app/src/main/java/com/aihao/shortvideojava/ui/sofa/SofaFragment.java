package com.aihao.shortvideojava.ui.sofa;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.aihao.libnavannotation.FragmentDestination;
import com.aihao.shortvideojava.R;

@FragmentDestination(pageUrl = "main/tabs/sofa",asStarter = false)
public class SofaFragment extends Fragment {

    private SofaViewModel sofaViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.i( "info","SofaCreate");
        sofaViewModel =
                ViewModelProviders.of(this).get(SofaViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        sofaViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
