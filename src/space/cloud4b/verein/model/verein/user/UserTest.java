package space.cloud4b.verein.model.verein.user;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class UserTest {
    User user;

    @Before
    public void setUp() throws Exception {
        user = new User(99, 1, "Bernhard Kämpf", "1234", LocalDate.now(),
                100, 9);
    }

    @Test
    public void getUserId() {
        assertEquals(user.getUserId(), 99);
    }

    @Test
    public void getUserName() {
        assertEquals(user.getUserName(), "Bernhard Kämpf");
    }

    @Test
    public void getSessionId() {
        assertEquals(user.getSessionId(), null);
    }
}