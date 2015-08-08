/* VirtualARt - an augmented reality application to visualize art on your wall
 * Logo by Thomas Uebe from the Noun Project
 * Code by David Brown
 * Built off of ARToolkit's sdk sample application
 * OpenGL texturing tutorial from http://stackoverflow.com/questions/7767367/how-to-fill-each-side-of-a-cube-with-different-textures-on-opengl-es-1-1
 */

package com.cybernoodlers.virtualart;

import org.artoolkit.ar.base.assets.AssetHelper;

import android.app.AlertDialog;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

public class VirtualARt extends Application {

    
    @Override
    public void onCreate() {
    	super.onCreate();
    	
    	singletonInstance = this;

    	// Load assets from the Data directory of the phone, where AR marker patterns are stored
		AssetHelper assetHelper = new AssetHelper(getAssets());
		assetHelper.cacheAssetFolder(this, "Data");
    }
    
    // Generate a single button error dialog
    public static AlertDialog generateErrorDialog(String errorMessage, Context activity) {
    	
    	AlertDialog.Builder errorDialogBuilder = new AlertDialog.Builder(activity);
    	errorDialogBuilder.setMessage(errorMessage);
    	errorDialogBuilder.setPositiveButton("OK", null);
    	
    	return errorDialogBuilder.create();
    }
    
    // Set the desired picture width (in mm)
    public static void setPictureWidth(int pictureWidth) {
    	VirtualARt.pictureWidth = pictureWidth;
    }
    
    // Set the desired picture height (in mm)
    public static void setPictureHeight(int pictureHeight) {
    	VirtualARt.pictureHeight = pictureHeight;
    }
    
    // Set the Uri of the desired picture
    public static void setSelectedPictureUri(Uri selectedPictureUri) {
    	VirtualARt.selectedPictureUri = selectedPictureUri;
    }
    
    // Get the desired picture width (in mm)
    public static int getPictureWidth() {
    	return pictureWidth;
    }
    
    // Get the desired picture height (in mm)
    public static int getPictureHeight() {
    	return pictureHeight;
    }
    
    // Set the width of the AR marker (in mm)
    public static void setMarkerWidth(int markerWidth) {
    	VirtualARt.markerWidth = markerWidth;
    }
    
    // Get the width of the AR marker (in mm)
    public static int getMarkerWidth() {
    	return markerWidth;
    }
    
    // Get the Uri of the selected picture (returns null if not selected)
    public static Uri getSelectedPictureUri() {
    	return selectedPictureUri;
    }
    
    // Load the selected picture as a bitmap
    public static Bitmap getSelectedPicture() {
    	
    	try {
    		ContentResolver contentResolver = singletonInstance.getContentResolver();
    		MediaStore.Images.Media.getBitmap(contentResolver, selectedPictureUri);
    		
    		return MediaStore.Images.Media.getBitmap(contentResolver, selectedPictureUri);
    	}
		catch (Exception exception) {
			return null;
		}
    }
    
    // The singleton instance of the application class
    private static VirtualARt singletonInstance = null;
    
    // Marker width (mm)
    private static int markerWidth = 160;
    
    // Picture width (mm)
    private static int pictureWidth = 0;
    
    // Picture height (mm)
    private static int pictureHeight = 0;
    
    // The Uri of the picture
    private static Uri selectedPictureUri = null;
}
