package com.aihao.shortvideojava.ui.publish;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.aihao.libnavannotation.ActivityDestination;
import com.aihao.shortvideojava.R;

@ActivityDestination(pageUrl = "main/tabs/publish", needLogin = true)
public class PublishActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dashboard);

    }
    @Override
    public void onClick(View v) {

    }
}
