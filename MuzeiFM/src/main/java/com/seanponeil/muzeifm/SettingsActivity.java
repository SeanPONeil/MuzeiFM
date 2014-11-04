package com.seanponeil.muzeifm;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import dagger.ObjectGraph;
import javax.inject.Inject;

public class SettingsActivity extends Activity {

  @Inject @LastFmUsername StringPreference lastFmUsername;

  @InjectView(R.id.username) EditText username;

  @OnClick(R.id.save) void onSave() {
    // TODO: Check to see if username is valid
    lastFmUsername.set(username.getText().toString());
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);
    ButterKnife.inject(this);

    ObjectGraph.create(new DataModule(this)).inject(this);
  }
}
