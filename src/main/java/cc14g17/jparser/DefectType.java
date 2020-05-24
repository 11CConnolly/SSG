package cc14g17.jparser;

/**
 *  Defect types that Test Suites can be generated for
 *  Parameter is included at the start of the name to indicate which parameter
 *  the associated good/bad methods should take.
 *
 *  Further explanation on defect types in included with each example
 */
public enum DefectType {

    /**
     *  Used to fuzz methods taking 1 Integer / int parameter by taking data for the most common values
     *  that exploit integers in order to trigger an exploit
     *
     *  e.g. badCheckIndex(int index) {...}
     */
    INTEGER_ATTACK,

    /**
     *   Used specifically for the CWE20 problem in SECdefects.
     *   Takes in the same data as INTEGER_ATTACKS applying it with a custom written Oracle to check if the program
     *   has been exploited or not
     *
     *   i.e. badWithdraw(int amount)
     */
    INTEGER_VALIDATION,

    /**
     *  Used specifically for the CWE22 problem in SECdefects.
     *  Used to fuzz methods taking in 1 String parameter by extracting data from the most common and applying
     *  the required custom file checks i.e. passwords/passwd.txt
     *
     *  e.g. badReadFile(String path) {...}
     */
    STRING_PATH_TRAVERSAL,

    /**
     * Used to find SQL Injection vulnerabilities with common SQLI exploit strings and escape characters.
     * Generates SQL Injection for methods with 1 String parameter
     *
     * e.g. badQuery(String query)
     */
    STRING_SQL_INJECTION,

    /**
     * Used to find SQL Injection vulnerabilities with common SQLI exploit strings and escape characters.
     * Generates SQL Injection for methods with 2 String parameters and is used for CWE89 in SECdefects
     *
     * e.g. badLogin(String username, String password)
     */
    STRING_SQL_INJECTIONS

}
