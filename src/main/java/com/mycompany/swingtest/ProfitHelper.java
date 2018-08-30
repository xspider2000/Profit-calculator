package com.mycompany.swingtest;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONObject;

public class ProfitHelper {

    static private String acessKey = "97d031df1eeba9c220fe5c88e93f2840";

    public static double calculateProfit(double dollars, Date buyDate) throws IOException {
        double spentRublesAtDate = getSpentRublesAtDate(dollars, buyDate);
        double dollarsLatestCost = getDollarsLatestCost(dollars);
        System.err.println(spentRublesAtDate);
        System.err.println(dollarsLatestCost);
        return Math.floor((dollarsLatestCost - spentRublesAtDate) * 100) / 100;
    }

    private static double getSpentRublesAtDate(double dollars, Date date) throws IOException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String dateInFormat = df.format(date);
        String url = "http://data.fixer.io/api/" + dateInFormat + "?symbols=USD,RUB&access_key=" + acessKey;
        JSONObject json = JsonReader.readJsonFromUrl(url);
        if (json.getBoolean("success") == true && json.getString("date").equals(dateInFormat)) {
            double eurToUsd = json.getJSONObject("rates").getDouble("USD");
            double eurToRubles = json.getJSONObject("rates").getDouble("RUB");
            double usdToRubles = Math.floor(eurToRubles / eurToUsd * 1000_000) / 1000_000.;
            return dollars * usdToRubles;
        } else {
            return 0;
        }
    }

    private static double getDollarsLatestCost(double dollars) throws IOException {
        String url = "http://data.fixer.io/api/latest?access_key=" + acessKey;
        JSONObject json = JsonReader.readJsonFromUrl(url);
        if (json.getBoolean("success") == true) {
            double eurToUsd = json.getJSONObject("rates").getDouble("USD");
            double eurToRubles = json.getJSONObject("rates").getDouble("RUB");
            double usdToRubles = Math.floor(eurToRubles / eurToUsd * 1000_000) / 1000_000.;
            return dollars * usdToRubles;
        } else {
            return 0;
        }
    }
}
