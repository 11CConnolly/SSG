package cc14g17.SECdefects;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CWE20_Improper_Input_ValidationTest {

    private CWE20_Improper_Input_Validation cwe20;

    @Before
    public void setUp() {
        cwe20 = new CWE20_Improper_Input_Validation();
    }

    // Test the bad code to show that it doesn't function as intended
    @Test
    public void badWithdraw() {
        cwe20.badWithdraw(0);
        Assert.assertEquals(0, cwe20.getBalance(), 0.001);

        cwe20.badWithdraw(-100);
        Assert.assertEquals(0, cwe20.getBalance(), 0.001);

        cwe20.badWithdraw(100);
        Assert.assertEquals(-100, cwe20.getBalance(), 0.001);

        cwe20.badWithdraw(1000);
        Assert.assertEquals(-100, cwe20.getBalance(), 0.001);
    }

    // Test to show program has desired functionality
    @Test
    public void goodWithdraw() {
        cwe20.goodWithdraw(0);
        Assert.assertEquals(0, cwe20.getBalance(), 0.001);

        cwe20.goodWithdraw(-100);
        Assert.assertEquals(0, cwe20.getBalance(), 0.001);

        cwe20.goodWithdraw(100);
        Assert.assertEquals(-100, cwe20.getBalance(), 0.001);

        cwe20.goodWithdraw(1000);
        Assert.assertEquals(-100, cwe20.getBalance(), 0.001);
    }
}