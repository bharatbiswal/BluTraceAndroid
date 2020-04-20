package com.mobiona.bluetrace.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mobiona.bluetrace.R;

import java.util.UUID;

public class SettingsFragment extends Fragment {
    private TextView uuidTextView;
   public SettingsFragment(){
       super(R.layout.fragment_settings_layout);
   }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.generateUDID).setOnClickListener(v->{
            String uuidValue=UUID.randomUUID().toString();
            uuidTextView.setText(uuidValue);
        });
        uuidTextView=view.findViewById(R.id.uuidValue);
    }
}
