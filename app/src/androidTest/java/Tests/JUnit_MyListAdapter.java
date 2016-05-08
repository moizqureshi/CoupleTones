package Tests;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.test.AndroidTestCase;
import android.widget.ArrayAdapter;

import com.example.moizqureshi.coupletones.MyListAdapter;

import java.util.List;

/**
 * Created by raymondarevalo on 5/7/16.
 */
/*
public class JUnit_MyListAdapter extends ActivityInstrumentationTestCase2 {
    MyListAdapter myListAdapter;
    public JUnit_MyListAdapter() {
        super(MyListAdapter.class);

    }
}
*/

public class JUnit_MyListAdapter extends AndroidTestCase {
        /* Variables set when making a new MyListAdapter constructor */
        MyListAdapter myListAdapter;
        Context context;
        int resource;
        List<String> objects;


        public JUnit_MyListAdapter() {
            super();
        }

        protected void setUp() throws Exception {
            super.setUp();
            context = null; // TODO change this
            resource = 10;  // TODO change this
            objects = null; // TODO change this
            myListAdapter = new MyListAdapter(context,resource,objects);
        }

        public void test_getView() {

        }

}
