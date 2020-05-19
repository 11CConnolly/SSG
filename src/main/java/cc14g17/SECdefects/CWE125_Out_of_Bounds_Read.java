/*
 * @Description
 * CWE_ID: 125
 * CWE_Entry_Name: Out-of-bounds Read
 *
 * The program reads data past the end, or before the beginning, of the intended array or buffer
 */

package cc14g17.SECdefects;

public class CWE125_Out_of_Bounds_Read extends AbstractDefectiveProgram {

    /** Array of values to be later read from */
    private int[] aValues = new int[10];

    CWE125_Out_of_Bounds_Read() {
        for (int i = 0; i < aValues.length; i++) {
            aValues[i] = i;
        }
    }

    @Override
    public void bad() {
        badGetValue(0);
    }

    @Override
    public void good() {
        goodGetValue(0);
    }

    /**
     * Returns value of array by index supplied.
     * Does not adequately check the input, so negative values can produce an error
     * and allow for an exception to be thrown.
     *
     * @param index
     * @return value
     * @throws ArrayIndexOutOfBoundsException
     */

    public int badGetValue(int index) throws ArrayIndexOutOfBoundsException {

        int value;

        /* FLAW This will allow a negative value to be accepted as the input array index*/
        if (index < aValues.length) {
            value = aValues[index];
        } else {
            System.out.println("Error bad value");
            value = -1;
        }

        return value;
    }

    /**
     * Returns value of array by index supplied.
     * Adequately checks input, so no error can be thrown with an out of bounds index
     *
     * @param index
     * @return value
     */
    public int goodGetValue(int index) {

        int value;

        /* FIX Include a check to ensure array index is within boundaries*/
        if (index >= 0 && index < aValues.length) {
            value = aValues[index];
        } else {
            System.out.println("Error bad value");
            value = -1;
        }

        return value;
    }
}
