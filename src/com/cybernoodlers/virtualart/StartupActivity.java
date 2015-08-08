package com.cybernoodlers.virtualart;

import org.artoolkit.ar.base.camera.CameraPreferencesActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

public class StartupActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	
    	// Locate the print marker text view to enable linking to external PDF
    	TextView printMarkerTextView = (TextView)findViewById(R.id.print_marker_text_view);
    	printMarkerTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }
    
    // Click handler for Camera Settings button
    public void clickCameraSettings(View view) {
    	
    	// Load the camera settings activity
    	Intent cameraSettingsIntent = new Intent(StartupActivity.this, CameraPreferencesActivity.class);
    	startActivity(cameraSettingsIntent);
    }
    
    // Click handler for Get Started button
    public void clickGetStarted(View view) {
    	
    	// Load the select picture activity
    	Intent selectPictureIntent = new Intent(StartupActivity.this, SelectPictureActivity.class);
    	startActivity(selectPictureIntent);
    }
}
