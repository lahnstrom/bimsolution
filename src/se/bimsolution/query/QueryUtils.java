package se.bimsolution.query;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Utility class for QueryMachines
 */
public final class QueryUtils {

    private QueryUtils() { };


    public HashMap<String, String> idToIfc (String CSVfilepath, String delimiter) throws IOException {
        String line = "";
        HashMap<String, String> idToIfc = new HashMap<>();
        List<String> data = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(CSVfilepath));
        while((line=br.readLine())!=null){
            String str[] = line.split(delimiter);
            for(int i=0;i<str.length;i++){
                idToIfc.put(str[0], str[1]);
            }
        }
        return idToIfc;
    }
}
