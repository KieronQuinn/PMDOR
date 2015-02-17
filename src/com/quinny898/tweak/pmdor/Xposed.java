package com.quinny898.tweak.pmdor;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import android.view.View;
import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;

public class Xposed implements IXposedHookInitPackageResources,
		IXposedHookLoadPackage {

	@Override
	public void handleInitPackageResources(InitPackageResourcesParam resparam)
			throws Throwable {
		if (!resparam.packageName.equals("com.google.android.music"))
			return;
		resparam.res.hookLayout("com.google.android.music", "layout",
				"play_header_list_layout", new XC_LayoutInflated() {
					@Override
					public void handleLayoutInflated(LayoutInflatedParam liparam)
							throws Throwable {
						// Get the FrameLayout containing the "DOWNLOADED ONLY"
						// banner
						View f = liparam.view.findViewById(liparam.res
								.getIdentifier(
										"play_header_status_bar_underlay",
										"id", "com.google.android.music"));
						// Set its height to 0, making it always gone regardless
						// of state (visibility is used in the app, child views
						// are also)
						f.getLayoutParams().height = 0;
						View f2 = liparam.view.findViewById(liparam.res
								.getIdentifier("play_header_banner", "id",
										"com.google.android.music"));
						// Set its height to 0, making it always gone regardless
						// of state (visibility is used in the app, child views
						// are also)
						f2.getLayoutParams().height = 0;
					}
				});
		resparam.res.setReplacement("com.google.android.music", "dimen",
				"play_header_list_banner_height", 0);
	}

	/*
	 * In the Material update we need to override the banner size updater as it
	 * is animated, we can just blank the method
	 */

	public void handleLoadPackage(final LoadPackageParam lpparam)
			throws Throwable {
		if (!lpparam.packageName.equals("com.google.android.music"))
			return;
		findAndHookMethod(
				"com.google.android.play.headerlist.PlayHeaderListLayout",
				lpparam.classLoader, "setBannerFraction", float.class,
				boolean.class, new XC_MethodReplacement() {

					@Override
					protected Object replaceHookedMethod(MethodHookParam param)
							throws Throwable {
						return null;
					}
				});

	}
}
