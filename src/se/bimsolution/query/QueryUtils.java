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


    public HashMap<String, List<String>> idToIfcCSVParser (String CSVfilepath, String delimiter) throws IOException {
        String line;
        HashMap<String, List<String>> parsedData = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(CSVfilepath));
        while((line=br.readLine())!=null){
            String str[] = line.split(delimiter);
            String key = str[1];
            String value = str[0];
            List<String> valueList;

            if (parsedData.containsKey(key)) {
                valueList = parsedData.get(key);
            } else {
                valueList = new ArrayList<>();
                parsedData.put(key, valueList);
            }
            valueList.add(value);
        }
        return parsedData;
    }
}
