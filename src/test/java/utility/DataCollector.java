package utility;

import java.util.concurrent.ConcurrentHashMap;

public class DataCollector {
	
	// Shared data structure for storing collected data
    private static ConcurrentHashMap<String, String[]> scrapedData = new ConcurrentHashMap<>();

    public static void addData(String testCaseName, String[] data) {
        scrapedData.put(testCaseName, data);
    }

    public static ConcurrentHashMap<String, String[]> getScrapedData() {
        return scrapedData;
    }
	
	

}
