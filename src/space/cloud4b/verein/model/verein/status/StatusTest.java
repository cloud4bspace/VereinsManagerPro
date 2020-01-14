package space.cloud4b.verein.model.verein.status;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class StatusTest {
    Status status;

    @Before
    public void setUp() throws Exception {
        status = new Status(1);
    }

    @Test
    public void getElementsAsArrayList() {
        assertTrue(status.getElementsAsArrayList().size() > 0);
    }

    @Test
    public void getStatusId() {
        assertTrue(status.getStatusId() == 1);
    }
}