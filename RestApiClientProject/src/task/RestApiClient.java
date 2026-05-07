package task;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class RestApiClient {

    public static void main(String[] args) {

        try {

            // User input
            Scanner sc = new Scanner(System.in);

            System.out.print("Enter country name: ");
            String country = sc.nextLine();

            // API URL
            URL url = new URL("https://restcountries.com/v3.1/name/" + country);

            // Open connection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            // Response code
            int responseCode = conn.getResponseCode();

            if (responseCode == 200) {

                // Read API response
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));

                StringBuilder response = new StringBuilder();

                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                // Convert response into JSON
                JSONArray data = new JSONArray(response.toString());

                JSONObject obj = data.getJSONObject(0);

                // Extract details
                String name = obj.getJSONObject("name")
                                 .getString("common");

                String capital = obj.getJSONArray("capital")
                                    .getString(0);

                long population = obj.getLong("population");

                // Currency extraction
                JSONObject currencies = obj.getJSONObject("currencies");

                Iterator<String> keys = currencies.keys();

                String currencyName = "";

                if (keys.hasNext()) {

                    String key = keys.next();

                    currencyName = currencies
                                   .getJSONObject(key)
                                   .getString("name");
                }

                // Display output
                System.out.println("\n----------- Country Information -----------");

                System.out.println("Country    : " + name);

                System.out.println("Capital    : " + capital);

                System.out.printf("Population : %,d\n", population);

                System.out.println("Currency   : " + currencyName);

                System.out.println("-------------------------------------------");

            } else {

                System.out.println("Country not found!");
            }

            sc.close();

        } catch (Exception e) {

            System.out.println("Error occurred!");

            e.printStackTrace();
        }
    }
}