package cc14g17.SECdefects;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CWE125_Out_of_Bounds_ReadTest {

    private CWE125_Out_of_Bounds_Read cwe125;

    @Before
    public void setUp() {
        cwe125 = new CWE125_Out_of_Bounds_Read();
    }

    @Test
    public void badGetValue() {
        try {
            Assert.assertEquals(-1, cwe125.badGetValue(-1));
            Assert.assertEquals(0, cwe125.badGetValue(0));
            Assert.assertEquals(1, cwe125.badGetValue(1));
            Assert.assertEquals(8, cwe125.badGetValue(8));
            Assert.assertEquals(9, cwe125.badGetValue(9));
            Assert.assertEquals(-1, cwe125.badGetValue(10));
        } catch (ArrayIndexOutOfBoundsException e) {
            Assert.fail("Exception " + e);
        }
    }

    @Test
    public void goodGetValue() {
        Assert.assertEquals(-1, cwe125.goodGetValue(-1));
        Assert.assertEquals(0, cwe125.goodGetValue(0));
        Assert.assertEquals(1, cwe125.goodGetValue(1));
        Assert.assertEquals(8, cwe125.goodGetValue(8));
        Assert.assertEquals(9, cwe125.goodGetValue(9));
        Assert.assertEquals(-1, cwe125.goodGetValue(10));
    }
}