package wisc.virgil.virgil;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

/**
 *  This is the Splash screen for the Virgil app.
 *  @author Summer Wilken
 */
public class Splash extends Activity {

//Button to advance to next screen
    /**
     * Moves to the MuseumSelectActivity
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
    }

    public void switchToList(View view) {

        Intent intent = new Intent(this, museumSelectActivity.class);
        startActivity(intent);
        finish();

    }
}