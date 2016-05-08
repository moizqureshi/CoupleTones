package Tests;


import android.test.ActivityInstrumentationTestCase2;

import com.example.moizqureshi.coupletones.Locations;
import com.example.moizqureshi.coupletones.User;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import org.junit.Before;
import org.testng.annotations.Test;
import org.junit.Assert.*;


/**
 * Created by raymondarevalo on 5/6/16.
 */

public class JUnit_User extends ActivityInstrumentationTestCase2<User>{
    private User user;
    private String email;
    private String partnerEmail;
    private Locations locations;

    public JUnit_User(){
        super(User.class);

    }

    /* FOR THE CONSTRUCTOR. DID NOT USE GOOGLE ACCOUNT BECAUSE WE WERE UNABLE TO GET ACCOUNT */
    @Before
    public void setUp(){
        user = new User();
        partnerEmail = "rarevalo@ucsd.edu";
        locations = null;
    }


    /* Since we haven't set the email, we compare getEmail == null. If no errors,
     * then we get the correct email
     */
    @Test
    public void test_getEmail() {
        assertEquals(null, user.getEmail());
    }


    /* We set the email. We then get the email and compare it to the one we set.
     * Returns true and causes no errors if it's the same email
     */
    @Test
    public void test_setPartnerEmail_1() {
    user.setPartnerEmail(partnerEmail);
    assertEquals("rarevalo@ucsd.edu", user.getPartnerEmail()); // Checks if we set the correct email
    }


    /* Don't set partner email. compares if the partner email is set to "--" by default */
    @Test
    public void test_setPartnerEmail_2() {
        assertEquals("--", user.getPartnerEmail());

    }


    /* We set the email and then get the email. Returns true and causes no errors if it's done
     * correctly
     */
    @Test
    public void test_getPartnerEmail() {
    user.setPartnerEmail(partnerEmail);
    assertEquals(partnerEmail, user.getPartnerEmail());

    }


    /* Returns true and causes no errors if location != null. This is true because when creating a
     * new user, a new location is created. Thus making location not null.
     */
    @Test
    public void test_getLocations() {
    assertNotNull( user.getLocations());

    }


    /* We set a partner and then check if it has a partner. If no errors, then it indicates that
     * we do have a partner.
     */
    @Test
    public void test_hasPartner_1() {
        user.setPartnerEmail(partnerEmail);
        assertEquals(true, user.hasPartner());
    }


    /* Don't set a partner. Returns false because we haven't set a partner */
    @Test
    public void test_hasPartner_2() {
        assertEquals(false, user.hasPartner());

    }


    /* Sets a partner, removes a partner, and then checks if it is true that we removed a partner.
     * We then test if hasPartner == false, indicating that we don't have one.
     */
    @Test
    public void test_removePartner_1() {
        user.setPartnerEmail(partnerEmail);
        assertEquals(true, user.removePartner());
        assertEquals(false, user.hasPartner());
    }

    
    /* Doesn't set a partner and attempts to removes one. Returns false because a partner wasn't
     * set.
     */
    @Test
    public void test_removePartner_2() {
        assertEquals(false, user.removePartner());
    }

}
