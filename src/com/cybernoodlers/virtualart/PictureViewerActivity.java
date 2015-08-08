package com.cybernoodlers.virtualart;

import org.artoolkit.ar.base.ARActivity;
import org.artoolkit.ar.base.rendering.ARRenderer;

import android.os.Bundle;
import android.widget.FrameLayout;

public class PictureViewerActivity extends ARActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture_viewer);
		
		pictureRenderer = new PictureRenderer();
		mainLayout = (FrameLayout) findViewById(R.id.main_layout);
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	protected ARRenderer supplyRenderer() {
		return pictureRenderer;
	}

	@Override
	protected FrameLayout supplyFrameLayout() {
		return mainLayout;
	}
	
	private FrameLayout mainLayout = null;
	private PictureRenderer pictureRenderer = null;
}
