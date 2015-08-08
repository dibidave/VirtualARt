/* Based off of ARBaseLib's Cube class
 * 
 */
package com.cybernoodlers.virtualart;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import org.artoolkit.ar.base.rendering.RenderUtils;

import android.graphics.Bitmap;
import android.opengl.GLES10;
import android.opengl.GLUtils;

public class PictureOverlay {
	
	public PictureOverlay(float width, float height, Bitmap pictureBitmap) {
		setArrays(width, height);
		this.pictureBitmap = pictureBitmap;
	}
	
    
    public void draw(GL10 unused) {
    	
    	GLES10.glEnableClientState(GLES10.GL_VERTEX_ARRAY);
    	GLES10.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    	
    	GLES10.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
    	GLES10.glVertexPointer(3, GLES10.GL_FLOAT, 0, vertexBuffer);
    	GLES10.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
    	
    	GLES10.glDrawElements(GLES10.GL_TRIANGLES, 6, GLES10.GL_UNSIGNED_BYTE, indexBuffer);
        
    	GLES10.glDisableClientState(GLES10.GL_VERTEX_ARRAY);
    	GLES10.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    }
    
	private void setArrays(float width, float height) {
		
		// Calculate the corners of the image based on the width and height
		// Z is always 0
		float topLeft[] = {
			- (width / 2.0f),
			height / 2.0f,
			0
		};
		
		float topRight[] = {
			width / 2.0f,
			height / 2.0f,
			0
		};
		
		float bottomLeft[] = {
			- width / 2.0f,
			- height / 2.0f,
			0
		};
		
		float bottomRight[] = {
			width / 2.0f,
			- height / 2.0f,
			0
		};
		
		// Store the vertices based on the 4 corners
		float vertices[] = {
			topLeft[0], topLeft[1], topLeft[2],
			topRight[0], topRight[1], topRight[2],
			bottomLeft[0], bottomLeft[1], bottomLeft[2],
			bottomRight[0], bottomRight[1], bottomRight[2]
		};

		// Store the indices of the two triangles making up this rectangle
		byte indices[] = { 
    		0, 1, 3,
    		0, 3, 2
		};
		
		// The texture vertices (in st coordinates)
		float texture[] = {
	    	0.0f, 0.0f,
    		1.0f, 0.0f,
    		0.0f, 1.0f,
    		1.0f, 1.0f
		};


		// Convert the arrays into buffers
		vertexBuffer = RenderUtils.buildFloatBuffer(vertices);
		indexBuffer = RenderUtils.buildByteBuffer(indices);
		textureBuffer = RenderUtils.buildFloatBuffer(texture);
    }

	public void loadGLTexture(GL10 gl) {

		// Generate one texture pointer
		GLES10.glGenTextures(1, textures, 0);
		
		// If we got a texture pointer from OpenGL
		if(textures[0] != 0) {
			
			// Bind it to textures array
			GLES10.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
			
			// Create texture filters
			GLES10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
			GLES10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
			
			// Set the texture based on our picture bitmap
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, pictureBitmap, 0);
		}
		
		// If there is an error getting a texture pointer, throw exception
		if(textures[0] == 0) {
			throw new RuntimeException("Error loading texture.");
		}
	}
	
	// The picture to use as a texture
	Bitmap pictureBitmap;
	
	// The texture pointer
	private int[] textures = new int[1];
	
	// The buffers
	private FloatBuffer	vertexBuffer;
    private ByteBuffer	indexBuffer;
	private FloatBuffer textureBuffer;
}
