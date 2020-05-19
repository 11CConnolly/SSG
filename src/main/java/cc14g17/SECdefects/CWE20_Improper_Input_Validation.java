/*
 * @Description
 * CWE_ID: 20
 * CWE_Entry_Name: Improper Input Validation
 *
 * The product does not validate or incorrectly validates input that
 * can affect the control flow or data flow of a program.
 */

package cc14g17.SECdefects;

public class CWE20_Improper_Input_Validation extends AbstractDefectiveProgram {

    private final int LIMIT = 200;
    private double balance;

    // Constructor to set balance for calculations prior to withdrawing
    CWE20_Improper_Input_Validation() {
        this.balance = 0;
    }

    @Override
    public void bad() {
        badWithdraw(5);
    }

    @Override
    public void good() {
        goodWithdraw(5);
    }

    /**
     * Reduces balance by the amount input with missing validation checks on amount
     *
     * @param amount - input amount
     */
    public void badWithdraw(int amount) {
        /* FLAW negative quantity of orders should not be allowed otherwise attacker
        * can increase balance arbitrarily*/
        if (amount >= LIMIT)
            return;
        balance = balance - amount;
    }

    /**
     * Reduces balance by the amount input with correct validation
     *
     * @param amount - input amount
     */
    public void goodWithdraw(int amount) {
        /* FIX only allow positive quantities by including another validation of input*/
        if (amount < 0 || amount >= LIMIT)
            return;
        balance = balance - amount;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
