package com.cybernoodlers.virtualart;

import java.text.DecimalFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class SetDimensionsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_dimensions);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		// Load the dimensions of the selected picture to determine aspect original ratio
		pictureWidth = VirtualARt.getSelectedPicture().getWidth();
		pictureHeight = VirtualARt.getSelectedPicture().getHeight();
		
		// Get the width and height and aspect ratio fields from xml
		widthTextField = (EditText) findViewById(R.id.width_text_field);
		heightTextField = (EditText) findViewById(R.id.height_text_field);
		preserveAspectRatioCheckbox = (CheckBox) findViewById(R.id.preserve_aspect_ratio_checkbox);
		
		// Set the initial width, in case the user has set it already (divided by 10 for cm) 
		double currentWidth = VirtualARt.getPictureWidth() / 10.0;
		
		if(currentWidth != 0) {
			widthTextField.setText(Double.toString(currentWidth));
		}
		
		// Set the initial height, in case the user has set it already (divided by 10 for cm)
		double currentHeight = VirtualARt.getPictureHeight() / 10.0;
		
		if(currentHeight != 0) {
			heightTextField.setText(Double.toString(currentHeight));
		}
		
		// Add a text change listener for when the user enters width or height to preserve aspect ratio
		widthTextField.addTextChangedListener(new OnDimensionEditedListener(widthTextField));
		heightTextField.addTextChangedListener(new OnDimensionEditedListener(heightTextField));
		
		// Add a listener for keyboard input in case the user presses next, done, or enter on the soft keyboard
		heightTextField.setOnEditorActionListener(new OnDoneListener());
		
		// Open up the soft keyboard automatically
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
	}
	
	// Click handler for when the user is ready to view
	public void clickViewPicture(View view) {
		startPictureViewer();
	}
	
	// Start the picture viewer activity
	private void startPictureViewer() {
		
		// Get the width and height from the text fields
		String widthString = widthTextField.getText().toString();
		String heightString = heightTextField.getText().toString();
		
		try {
			
			// Try converting the user's string to a double
			double pictureWidth = Double.parseDouble(widthString);
			
			// Set the picture width to the user's input (times 10 for mm)
			VirtualARt.setPictureWidth((int)(pictureWidth * 10));
		}
		catch (NumberFormatException numberFormatException) {
			
			// If there is an error converting the text, reset the text box
			widthTextField.setText("");
			
			// Let the user know their input is invalid
			AlertDialog widthErrorDialog = VirtualARt.generateErrorDialog(getString(R.string.width_invalid_error_text), this);
			widthErrorDialog.show();
			return;
		}
		
		try {

			// Try converting the user's string to a double
			double pictureHeight = Double.parseDouble(heightString);
			
			// Set the picture height to the user's input (times 10 for mm)
			VirtualARt.setPictureHeight((int)(pictureHeight * 10));
		}
		catch (NumberFormatException numberFormatException) {
			
			// If there is an error converting the text, reset the text box
			heightTextField.setText("");
			
			// Let the user know their input is invalid
			AlertDialog heightErrorDialog = VirtualARt.generateErrorDialog(getString(R.string.height_invalid_error_text), this);
			heightErrorDialog.show();
			return;
		}
		
		// Start the picture viewer activity
		Intent pictureViewerIntent = new Intent(this, PictureViewerActivity.class);
		startActivity(pictureViewerIntent);
	}
	
	// Soft keyboard listener for the last TextView
	private class OnDoneListener implements EditText.OnEditorActionListener {

		@Override
		public boolean onEditorAction(TextView view, int actionId, KeyEvent keyEvent) {
			
			int result = actionId & EditorInfo.IME_MASK_ACTION;
			
			// If the user has pressed done, next, or go on the soft keyboard, start the picture viewer 
			switch(result) {
			case EditorInfo.IME_ACTION_DONE:
			case EditorInfo.IME_ACTION_NEXT:
			case EditorInfo.IME_ACTION_GO:
				startPictureViewer();
				return true;
			default:
				return false;
			}
		}
	}
	
	// Listener for text changes to width and height fields
	private class OnDimensionEditedListener implements TextWatcher {
		
		// Store which view is being edited
		public OnDimensionEditedListener(View view) {
			this.view = view;
		}

		@Override
		public void afterTextChanged(Editable arg0) {
			
			// If another text box is already being edited, don't do anything
			if(isEditingText) {
				return;
			}
			
			// Only proceed if the preserve aspect ratio is checked
			if(preserveAspectRatioCheckbox.isChecked()) {
				
				// Indicate that we are now programmatically editing a textbox
				isEditingText = true;
				
				// If the width field is the one being edited
				if(view == widthTextField) {
					try {
						
						// Convert the user's text into a number
						double width = Double.parseDouble(widthTextField.getText().toString());
						
						// Get the picture's original width to height ratio
						double widthToHeightRatio = pictureWidth * 1.0 / pictureHeight;
						
						DecimalFormat tenthsPrecisionFormat = new DecimalFormat("#.0");
						
						// Set the height field based on the original ratio and the newly entered width
						heightTextField.setText(tenthsPrecisionFormat.format(width / widthToHeightRatio));
					}
					catch (NumberFormatException numberFormatException) {
						
					}
				}
				// If the height field is the one being edited
				else if(view == heightTextField) {

					try {
						
						// Convert the user's text into a number
						double height = Double.parseDouble(heightTextField.getText().toString());
						
						// Get the picture's original height to width ratio
						double heightToWidthRatio = pictureHeight * 1.0 / pictureWidth;
						
						DecimalFormat tenthsPrecisionFormat = new DecimalFormat("#.0");
						
						// Set the width field based on the original ratio and the newly entered height
						widthTextField.setText(tenthsPrecisionFormat.format(height / heightToWidthRatio));
					}
					catch (NumberFormatException numberFormatException) {
						
					}
				}
				
				// Indicate that we are done programmatically editing a textbox
				isEditingText = false;
			}
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			
		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			
		}
		
		private View view;
	}

	// The user-supplied width (mm)
	private int pictureWidth;
	
	// The user-supplied height (mm)
	private int pictureHeight;
	
	// The width EditText field
	private EditText widthTextField = null;
	
	// The height EditText field
	private EditText heightTextField = null;
	
	// The checkbox indicating whether to preserve aspect ratio
	private CheckBox preserveAspectRatioCheckbox = null;
	
	// A state variable indicating whether we are programmatically editing the text box (to prevent infinite TextWatcher triggers)
	private boolean isEditingText = false;
}
