package edu.exa.unicen.dicomseg.activities;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;

import edu.exa.unicen.dicomseg.R;
import edu.exa.unicen.dicomseg.dagger.components.MainActivityComponent;
import edu.exa.unicen.dicomseg.dbhelper.DbHelper;
import edu.exa.unicen.dicomseg.dbhelper.exporters.IDbExporter;
import edu.exa.unicen.dicomseg.dialogs.ResetDatabaseDialog;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Inject
    public IDbExporter dbExporter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivityComponent component = MainActivityComponent.Initializer.init();
        component.inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), FileChooserActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, 1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_exportDB:
                DbHelper dbHelper = new DbHelper(getBaseContext());
                dbExporter.exportDatabase(dbHelper);
                break;
            case R.id.action_resetDB:
                ResetDatabaseDialog resetDatabaseDialog = new ResetDatabaseDialog();
                resetDatabaseDialog.show(getSupportFragmentManager(), "");
                break;
            case R.id.action_exit:
                finishAffinity();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
