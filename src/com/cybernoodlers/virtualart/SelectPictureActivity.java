package com.cybernoodlers.virtualart;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SelectPictureActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_picture);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		// Locate the picture preview ImageView
		picturePreview = (ImageView) findViewById(R.id.picture_preview);
		
		// See if the user has already selected a picture
		Bitmap selectedPicture = VirtualARt.getSelectedPicture();
		
		// If they have, load it for preview
		if(selectedPicture != null) {
			picturePreview.setImageBitmap(selectedPicture);
			selectedPictureUri = VirtualARt.getSelectedPictureUri();
		}
	}
	
	// Activity result handler for when the user selects a picture from the gallery
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		// If this is a picture request result, and the user picked a picture
		if(requestCode == RESULT_PICTURE_PICKED
				&& resultCode == RESULT_OK
				&& data != null) {
			
			// Load the picture
			selectedPictureUri = data.getData();
			
			try {
				
				// Let the application know the new selected picture
				VirtualARt.setSelectedPictureUri(selectedPictureUri);
				
				// Reset the picture width and height to 0 so the user must enter it
				VirtualARt.setPictureWidth(0);
				VirtualARt.setPictureHeight(0);
				
				// Load a preview of the picture
				picturePreview.setImageBitmap(VirtualARt.getSelectedPicture());
			}
			catch (Exception exception) {
				
				// Let the user know there was an error loading the picture
				AlertDialog pictureLoadingErrorDialog = VirtualARt.generateErrorDialog(getString(R.string.browse_picture_error_text), this);
				pictureLoadingErrorDialog.show();
			}
		}
	}
	
	// Click handler for when the user clicks the Browse Picture button
	public void clickBrowsePicture(View view) {
		
		// Initialize a new intent to let the picture pick a picture from their gallery
		Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		
		// Start activity 
		startActivityForResult(galleryIntent, RESULT_PICTURE_PICKED);
	}
	
	// Click handler for when the user clicks next
	public void clickNext(View view) {
		
		// If the user hasn't selected an image, they can't proceed
		if(selectedPictureUri == null) {
			AlertDialog noImageSelectedErrorDialog = VirtualARt.generateErrorDialog(getString(R.string.no_picture_selected_error_text), this);
			noImageSelectedErrorDialog.show();
			return;
		}
		
		// Load the set dimensions activity
		Intent setDimensionsIntent = new Intent(this, SetDimensionsActivity.class);
		startActivity(setDimensionsIntent);
	}
	
	// The result id for the gallery browser intent
	private static final int RESULT_PICTURE_PICKED = 1;
	
	// The ImageView containing the picture preview
	private ImageView picturePreview = null;
	
	// The Uri of the user's selected picture
	private Uri selectedPictureUri = null;
}
