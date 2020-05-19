/*
 * @Description
 * CWE_ID: 22
 * CWE_Entry_Name: Improper Limitation of a Pathname to a Restricted Directory ('Path Traversal')
 *
 * The software uses external input to construct a pathname that is intended to identify a file
 * or directory that is located underneath a restricted parent directory, but the software does
 * not properly neutralize special elements within the pathname that can cause the pathname to resolve
 * to a location that is outside of the restricted directory.
 */

package cc14g17.SECdefects;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class CWE22_Path_Traversal extends AbstractDefectiveProgram {

    /** Flag to indicate if a file has been read */
    private boolean fileRead;

    CWE22_Path_Traversal() {
        fileRead = false;
    }

    @Override
    public void bad() {
        badRead("alice.txt");
    }

    @Override
    public void good() {
        goodRead("alice.txt");
    }


    /**
     * Reads information from a given profile, but without correct path traversal checks
     * so files that aren't meant to be read are able to be read.
     *
     * @param inputFilepath - text supplied by the user to indicate which profile to view
     */
    public void badRead(String inputFilepath) {

        String relativeFilepath = "./src/main/resources/profiles/" + inputFilepath;

        File file = new File(relativeFilepath);

        /* FLAW code doesn't limit the potential input for the file path */

        BufferedReader in = null;

        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));

            String line;
            while((line = in.readLine()) != null)
            {
                IO.printLine(line);
            }

            fileRead = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        fileRead = true;
    }

    /**
     * Reads information from a given profile, with checks to ensure no path traversal is
     * being attempted.
     *
     * @param inputFilepath - text supplied by the user to indicate which profile to view
     */
    public void goodRead(String inputFilepath) {

        String relativeFilepath = "src/main/resources/profiles/" + inputFilepath;

        File file = new File(relativeFilepath);

        /* FIX check absolute against canonical path where canonical disregards special characters*/

        String canonicalFilepath = null;
        String absoluteFilepath = null;

        try {
            canonicalFilepath = file.getCanonicalPath();
            absoluteFilepath = file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (canonicalFilepath == null || absoluteFilepath == null)
            return;

        if (!canonicalFilepath.equals(absoluteFilepath)) {
            IO.printLine("Potential Directory Traversal");
            return;
        }

        BufferedReader in = null;

        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));

            String line;
            while((line = in.readLine()) != null)
            {
                IO.printLine(line);
            }

            fileRead = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        fileRead = true;
    }

    public boolean isFileRead() {
        return fileRead;
    }

    public void setFileRead(boolean fileRead) {
        this.fileRead = fileRead;
    }
}