package cc14g17.SECdefects;

public abstract class AbstractDefectiveProgram {
    public abstract void bad();

    public abstract void good();

    /**
     *  Runs good and bad methods to each CWE to ensure they run as intended
     *
     * @param cweName
     */
    void runTests(String cweName) {

        IO.printLine("Running tests for " + cweName);

        good();
        IO.printLine("Finished good() for " + cweName);

        bad();
        IO.printLine("Finished bad() for " + cweName);
    }
}
