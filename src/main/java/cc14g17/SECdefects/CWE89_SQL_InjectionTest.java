package cc14g17.SECdefects;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CWE89_SQL_InjectionTest {

    private CWE89_SQL_Injection cwe89;

    @Before
    public void setUp() { cwe89 = new CWE89_SQL_Injection(); }

    @Test
    public void badLogin() {
        cwe89.badLogin("incorrect", "details");
        assertFalse(cwe89.isLoggedIn());

        cwe89.badLogin("injection", "' OR 1=1 -- ");
        assertFalse(cwe89.isLoggedIn());

        cwe89.badLogin("callum", "connolly");
        assertTrue(cwe89.isLoggedIn());
    }

    @Test
    public void goodLogin() {
        cwe89.goodLogin("incorrect", "details");
        assertFalse(cwe89.isLoggedIn());

        cwe89.goodLogin("injection", "' OR 1=1 -- ");
        assertFalse(cwe89.isLoggedIn());

        cwe89.goodLogin("callum", "connolly");
        assertTrue(cwe89.isLoggedIn());
    }
}