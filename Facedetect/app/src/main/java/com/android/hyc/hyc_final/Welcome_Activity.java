package com.android.hyc.hyc_final;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class Welcome_Activity extends AppIntro {
	@Override
	public void init(@Nullable Bundle savedInstanceState) {
		// Add your slide's fragments here.
		// AppIntro will automatically generate the dots indicator and buttons.
		/*addSlide(first_fragment);
		addSlide(second_fragment);
		addSlide(third_fragment);
		addSlide(fourth_fragment);*/

		// Instead of fragments, you can also use our default slide
		// Just set a title, description, background and image. AppIntro will do the rest.
		addSlide(AppIntroFragment.newInstance("Welcome to SmartClound.", "SmartClound made easy and simple.", R.drawable.default_user, Color.rgb(34,34,34)));
		addSlide(AppIntroFragment.newInstance("First_Page", "this is first page", R.drawable.b, Color.rgb(0,188,212)));
		addSlide(AppIntroFragment.newInstance("Third_Page", "this is Third page", R.drawable.b, Color.rgb(233,30,99)));
		addSlide(AppIntroFragment.newInstance("You are all set.               Enjoy it!", "Get Started!", R.drawable.b, Color.rgb(76,175,80)));




		// OPTIONAL METHODS
		// Override bar/separator color.
		//setBarColor(Color.parseColor("#3F51B5"));

		//setSeparatorColor(Color.parseColor("#2196F3"));
		// Hide Skip/Done button.
		showSkipButton(true);
		setProgressButtonEnabled(true);

		// Turn vibration on and set intensity.
		// NOTE: you will probably need to ask VIBRATE permisssion in Manifest.
		setVibrate(true);
		setVibrateIntensity(30);
	}

	@Override
	public void onSkipPressed() {
		Intent intent =new Intent(Welcome_Activity.this,MainActivity.class);
		startActivity(intent);
	}

	@Override
	public void onNextPressed() {

	}

	@Override
	public void onDonePressed() {
		Intent intent =new Intent(Welcome_Activity.this,MainActivity.class);
		startActivity(intent);

	}

	@Override
	public void onSlideChanged() {

	}
}
