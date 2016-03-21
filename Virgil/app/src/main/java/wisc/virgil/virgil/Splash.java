package wisc.virgil.virgil;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 *  This is the Splash screen for the Virgil app.
 *  @Summer Wilken
 */
public class Splash extends Activity {

//Button to advance to next screen
    /**
     * Moves to the MuseumSelectActivity
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, MuseumSelectActivity.class);
        startActivity(intent);
        finish();
    }
}