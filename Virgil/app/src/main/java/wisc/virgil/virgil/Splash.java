package wisc.virgil.virgil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Summer on 4/9/2016.
 */
public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, MuseumSelectActivity.class);
        startActivity(intent);
        finish();
    }
}
