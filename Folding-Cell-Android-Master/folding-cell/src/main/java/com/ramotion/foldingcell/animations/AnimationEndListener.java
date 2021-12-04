package com.ramotion.foldingcell.animations;

import android.view.animation.Animation;
/**
 * NEF: fixed MethodArgumentCouldbeFinal and AtLeastOneConstructor
 */
/**
 * Just sugar for code clean
 */
public abstract class AnimationEndListener implements Animation.AnimationListener {
	AnimationEndListener(){
		
	}

    @Override
    public void onAnimationStart(final Animation animation) {
        // do nothing
    }

    @Override
    public void onAnimationRepeat(final Animation animation) {
        // do nothing
    }

}
