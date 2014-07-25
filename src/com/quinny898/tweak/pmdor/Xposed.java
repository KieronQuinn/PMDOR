package com.quinny898.tweak.pmdor;

import android.widget.FrameLayout;
import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;

public class Xposed implements IXposedHookInitPackageResources {

	@Override
	public void handleInitPackageResources(InitPackageResourcesParam resparam)
			throws Throwable {
		if (!resparam.packageName.equals("com.google.android.music"))
			return;
		resparam.res.hookLayout("com.google.android.music", "layout",
				"base_activity_content", new XC_LayoutInflated() {
					@Override
					public void handleLayoutInflated(LayoutInflatedParam liparam)
							throws Throwable {
						// Get the FrameLayout containing the "DOWNLOADED ONLY"
						// banner
						FrameLayout f = (FrameLayout) liparam.view
								.findViewById(liparam.res.getIdentifier(
										"download_strip", "id",
										"com.google.android.music"));
						// Set its height to 0, making it always gone regardless
						// of state (visibility is used in the app, child views
						// are also)
						f.getLayoutParams().height = 0;
					}
				});
	}
}
