/* Based off of ARToolkit's SimpleInteractiveRenderer
 * 
 */

package com.cybernoodlers.virtualart;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.artoolkit.ar.base.ARToolKit;
import org.artoolkit.ar.base.rendering.ARRenderer;

public class PictureRenderer extends ARRenderer {

	PictureRenderer() {
		pictureOverlay = new PictureOverlay(VirtualARt.getPictureWidth(), VirtualARt.getPictureHeight(), VirtualARt.getSelectedPicture());
	}

	/**
	 * By overriding {@link configureARScene}, the markers and other settings
	 * can be configured after the native library is initialized, but prior to
	 * the rendering actually starting.
	 */
	@Override
	public boolean configureARScene() {

		markerId = ARToolKit.getInstance().addMarker("single;Data/patt.hiro;" + Integer.toString(VirtualARt.getMarkerWidth()));
		
		if (markerId < 0) {
			return false;
		}

		return true;
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		
		pictureOverlay.loadGLTexture(gl);
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
	}

	public void draw(GL10 gl) {
    	
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL10.GL_PROJECTION);
        
        float[] projectionMatrix = ARToolKit.getInstance().getProjectionMatrix();
        
        gl.glLoadMatrixf(projectionMatrix, 0);
        
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glEnable(GL10.GL_TEXTURE);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glEnable(GL10.GL_DEPTH_TEST);
    	gl.glFrontFace(GL10.GL_CW);
		
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        
        if (ARToolKit.getInstance().queryMarkerVisible(markerId)) {

        	float[] markerTransformation = ARToolKit.getInstance().queryMarkerTransformation(markerId);
            
        	gl.glLoadMatrixf(markerTransformation, 0);
        	
        	ARToolKit.getInstance().queryMarkerTransformation(markerId);
        	
        	gl.glPushMatrix();
        	pictureOverlay.draw(gl);
        	gl.glPopMatrix();
        }
	}

	private int markerId = -1;
	private PictureOverlay pictureOverlay;
}
