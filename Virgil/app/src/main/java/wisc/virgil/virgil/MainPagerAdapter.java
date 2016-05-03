package wisc.virgil.virgil;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 *  Written by   : Munish Kapoor
 *  Original Code:
 *  http://manishkpr.webheavens.com/android-material-design-tabs-collapsible-example/
 **/

    public class MainPagerAdapter extends FragmentStatePagerAdapter {

        CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to
                               // be passed when ViewPagerAdapter is created

        int NumbOfTabs;        // Store the number of tabs, this will also be passed when
                               // the ViewPagerAdapter is created
        VirgilAPI api;


        // Build a Constructor and assign the passed Values to appropriate values in the class
        public MainPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb, VirgilAPI api) {
            super(fm);

            this.Titles = mTitles;
            this.NumbOfTabs = mNumbOfTabsumb;
            this.api = api;
        }

        //This method return the fragment for the every position in the View Pager
        @Override
        public Fragment getItem(int position) {

            Bundle bundle = new Bundle();
            bundle.putSerializable("API", api);

            if(position == 0) // if the position is 0 we are returning the Events tab
            {
                EventsFragment tab1 = new EventsFragment();
                bundle.putInt("POS", position);
                tab1.setArguments(bundle);
                return tab1;
            }
            else    // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
            {
                PostsFragment tab2 = new PostsFragment();
                bundle.putInt("POS", position - 1);
                tab2.setArguments(bundle);
                return tab2;
            }
        }

        // This method return the titles for the Tabs in the Tab Strip

        @Override
        public CharSequence getPageTitle(int position) {
            return Titles[position];
        }

        // This method return the Number of tabs for the tabs Strip

        @Override
        public int getCount() {
            return NumbOfTabs;
        }
    }
