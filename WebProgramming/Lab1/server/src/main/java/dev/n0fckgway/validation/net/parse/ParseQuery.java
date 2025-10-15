package dev.n0fckgway.validation.net.parse;

import java.util.HashMap;
import java.util.Map;

public class ParseQuery {
    public static Map<String, String> parse(String queryParams) throws ParseException {

        if (queryParams == null || queryParams.isEmpty()) {
            throw new ParseException("Cannot parse params. Maybe params is null or empty");

        }
        Map<String, String> params = new HashMap<>();
        String[] query = queryParams.split("&");

        for (String fragment: query) {
            String[] parts = fragment.split("=");
            if (parts.length != 2) {
                throw new ParseException("Illegal query fragment: " + fragment);
            }

            String key = parts[0];
            String value = parts[1];
            params.put(key, value);


        }
        return params;
    }

}
