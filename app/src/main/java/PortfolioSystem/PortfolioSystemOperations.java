package PortfolioSystem;


import static java.lang.Math.abs;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class PortfolioSystemOperations implements PortfolioSystem {

  public static ArrayList<AssetQuote> purchasedAssets = new ArrayList<AssetQuote>();
  public static ArrayList<AssetQuote> soldAssets = new ArrayList<AssetQuote>();
  //replace api key with mine
  public String apiKey = "R3MBGL8DDY3ZZsZqYYHww69yGJjXdqDD8KdDrFM7";


  public double funds;



  /**
   * Search a list of Asset quote and find index value for symbol
   *
   * @param assetQuoteList List to search
   * @param searchSymbol   Symbol to search
   * @return The index of the Symbol
   */
  private static int indexForAssetSummaryWithSymbol(ArrayList<AssetQuote> assetQuoteList,
      String searchSymbol) {
    for (int i = 0; i < assetQuoteList.size(); i++) {
      if (assetQuoteList.get(i).getAssetSymbol().equals(searchSymbol)) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Add the specified amount in USD to the total cash funds available within the portfolio system.
   *
   * @param amount the amount of money in USD to add to the system.
   */
  @Override
  public void addFunds(double amount) {
    funds += amount;
  }

  /**
   * Withdraw the specified amount in USD from the total cash funds available within the portfolio
   * management system.
   *
   * @param amount the amount of money in USD to withdraw from the system.
   * @return True if we have successfully withdrawn the funds (sufficient funds are available)
   * otherwise false.
   */
  @Override
  public boolean withdrawFunds(double amount) {
    if (amount <= funds) {
      funds -= amount;
      return true;
    } else {
      System.out.println("You are a student you have no money!");
      return false;
    }

  }

  /**
   * Get the available funds in  the portfolio system.
   *
   * @return the amount of the funds available
   */
  public double getAvailableFunds() {
    return funds;
  }

  /**
   * Set the starting balance of total cash funds in the portfolio system.
   *
   * @param startingBalance the amount of money in USD in the system.
   */
  public void setStartingBalance(double startingBalance) {
    funds = startingBalance;
  }

  /**
   * Record a purchase of the named asset if available funds greater or equal to the total value of the assets (stock
   * or cryptocurrency) being purchased. The price paid should be the real live price of the asset.
   *
   * @param assetSymbol the name of the asset (stock symbol or cryptocurrency) to purchase
   * @param amount      the amount of the asset to purchase
   * @return True if the asset is purchased successfully, otherwise False.
   */
  @Override
  public boolean purchaseAsset(String assetSymbol, double amount)
      throws IOException, ParseException {
    String assetSymb = assetSymbol;
    boolean purchased = false;
    double purchasedAssetValue = 0.0;
    boolean noSuchAsset = false;

    // making an API call using OkHttpClient
    OkHttpClient client = new OkHttpClient().newBuilder()
        .build();
    Request request = new Request.Builder()
        .url("https://yfapi.net/v6/finance/quote?region=US&lang=en&symbols=" + assetSymb)
        .method("GET", null)
        .addHeader("X-API-KEY",
            apiKey)  // modified to use a variable instead of hardcoded API Key value
        .build();
    Response response = null;
    try {
      response = client.newCall(request).execute();
    } catch (IOException e) {
      e.printStackTrace();
    }

    String jsonStr = response.body().string();

    JSONParser jsonParser = new JSONParser();
    Object jsonO = jsonParser.parse(jsonStr);

    JSONObject jsonObject = (JSONObject) jsonO;

    JSONObject quoteResponse = (JSONObject) jsonObject.get("quoteResponse");

    JSONArray resultArray = (JSONArray) quoteResponse.get("result");

    // this part ensured that asset symbol and number of units is not added to the portfolio if the asset doeasn't exist
    if (resultArray.size() == 0) {
      noSuchAsset = true;
      System.out.println("                !!!NO SUCH ASSET!!!");
    }

    String symbol;
    Double regMarkPrice = 0.0;
    String assetLongName = "";
    Long timeStamp = (long) 0;
    String assetType = "";

    for (int i = 0; i < resultArray.size(); i++) {
      JSONObject jsonObj = (JSONObject) resultArray.get(i);
      symbol = (String) jsonObj.get("symbol");
      regMarkPrice = (Double) jsonObj.get("regularMarketPrice");
      assetLongName = (String) jsonObj.get("shortName");
      timeStamp = (Long) jsonObj.get("regularMarketTime");
      assetType = (String) jsonObj.get("quoteType");
      //System.out.println("Testing Parser Loop:  " + symbol + " and Regular market price is:  " + regMarkPrice);
      purchasedAssetValue = amount * regMarkPrice;
    }

    // Code below adds purchased asset to ArrayList
    // if statement checks if the funds are sufficient to buy assets
    if (!assetSymb.equals("xyz") && noSuchAsset == false) { //
      if (AssetQuote.availableFunds >= purchasedAssetValue) {
        AssetQuote.availableFunds -= purchasedAssetValue;
        AssetQuote.totalValue += purchasedAssetValue;

        double assetsValue = regMarkPrice * amount;
        AssetQuote.assetQuotes.add(
            new AssetQuote(assetSymb, assetLongName, timeStamp, assetsValue, amount, assetType));
        AssetQuote.totalValue += regMarkPrice * amount;
        purchasedAssets.add(AssetQuote.assetQuotes.get(AssetQuote.assetQuotes.size() - 1));

        System.out.println("You successfully purchased assets!");
        //System.out.println("Available funds left:  $" + AssetQuote.getAvailableFunds());
        purchased = true;
        AssetQuote.setAvailableFunds(AssetQuote.getAvailableFunds() - regMarkPrice * amount);
        funds -= regMarkPrice * amount;
      } else {
        System.out.println("You don't have enough funds to buy the assets!!!");
      }
    }

    return purchased;

  }

  /**
   * Record a sale of the named asset (stock or cryptocurrency) at the current live market value if
   * we hold that asset. The sale price should be the real live price of the asset at the time of
   * sale retrieved from an appropriate web API. The revenue generated from the sale should be added
   * to the total funds available to the user.
   * <p>
   * Business logic: If we hold greater than 1 units of the specified asset (say 10 units of Microsoft stock
   * MSFT), and the parameter amount is greater than total units of the stock, we should sell the units that
   * maximise our profit. Remember some of the stock could have been purchased on different dates
   * and therefore have been purchased at different price points.
   *
   * @param assetSymbol the name of the asset (stock symbol or cryptocurrency) to sell
   * @param amount      the amount of the asset to sell
   */
  @Override
  public boolean sellAsset(String assetSymbol, double amount) throws IOException, ParseException {
    String assetSymb2 = assetSymbol;
    boolean sold = false;
    double soldAssetValue;

    // making an API call using OkHttpClient
    OkHttpClient client = new OkHttpClient().newBuilder()
        .build();
    Request request = new Request.Builder()
        .url("https://yfapi.net/v6/finance/quote?region=US&lang=en&symbols=" + assetSymb2)
        .method("GET", null)
        .addHeader("X-API-KEY",
            apiKey)  // modified to use a variable instead of hardcoded API Key value
        .build();
    Response response = null;
    try {
      response = client.newCall(request).execute();
    } catch (IOException e) {
      e.printStackTrace();
    }

    String jsonStr = response.body().string();

    JSONParser jsonParser = new JSONParser();
    Object jsonO = jsonParser.parse(jsonStr);

    JSONObject jsonObject = (JSONObject) jsonO;

    JSONObject quoteResponse = (JSONObject) jsonObject.get("quoteResponse");

    JSONArray resultArray = (JSONArray) quoteResponse.get("result");

    String symbol;
    Double regMarkPrice2 = 0.0;
    String assetLongName;
    Long timeStamp;

    for (int i = 0; i < resultArray.size(); i++) {
      JSONObject jsonObj = (JSONObject) resultArray.get(i);
      symbol = (String) jsonObj.get("symbol");
      regMarkPrice2 = (Double) jsonObj.get("regularMarketPrice");
      assetLongName = (String) jsonObj.get("longName");
      timeStamp = (Long) jsonObj.get("regularMarketTime");
      soldAssetValue = amount * regMarkPrice2;
    }

    // code below checks if the asset code is already present in the assets
    boolean found = false;
    int j;
    for (j = 0; j < AssetQuote.assetQuotes.size(); j++) {

      if (AssetQuote.assetQuotes.get(j).getAssetSymbol().equals(assetSymb2)) {
        found = true;
        //System.out.println("Asset was found!!!!!!!!!!!!!!!!");
      }

    }

    // if asset is present in the portfolio the value will be deducted from the asset
    ArrayList<AssetQuote> assetForSale = new ArrayList();
    //ArrayList assetForSaleSorted = new ArrayList();
    if (found) {

      for (AssetQuote aq : AssetQuote.assetQuotes) {
        if (!aq.getAssetSymbol().equals("xyz")) {
          double temp;
          temp = aq.getValue() / aq.getStockAmount();
          if (aq.getAssetSymbol().equals(assetSymb2) && temp <= regMarkPrice2) {

            assetForSale.add(aq);
          } else if (aq.getAssetSymbol().equals(assetSymb2) && temp > regMarkPrice2) {
            assetForSale.add(aq);
          }
        }
      }
      // sort the prices from lowest to highest
      Comparator<AssetQuote> priceComparator = new Comparator<AssetQuote>() {
        @Override
        public int compare(AssetQuote o1, AssetQuote o2) {
          if (o1.getValue() / o1.getStockAmount() > o2.getValue() / o2.getStockAmount()) {
            return 1;
          } else if (o1.getValue() / o1.getStockAmount() < o2.getValue() / o1.getStockAmount()) {
            return -1;
          } else {
            return 0;
          }
        }
      };

      assetForSale.sort(priceComparator);

      // updating the amount, stock count and adding sold stock to soldAssets ArrayList
      if (amount <= assetForSale.get(0).getStockAmount()) {
        for (AssetQuote assQ : AssetQuote.assetQuotes) {
          if (assetForSale.get(0).equals(assQ)) {
            double temp;
            temp = assQ.getValue() / assQ.getStockAmount();
            assQ.setValue(assQ.getValue() - (temp * amount));
            assQ.setStockAmount(assQ.getStockAmount() - amount);
            soldAssets.add(assQ);
            AssetQuote.setAvailableFunds(AssetQuote.getAvailableFunds() + amount * regMarkPrice2);
            funds += amount * regMarkPrice2;
            if (assQ.getStockAmount() == 0) {
              AssetQuote.assetQuotes.remove(assQ);
            }
            sold = true;
          }
        }

      } else {
        System.out.println("You cannot sell more assets than you have in your portfolio!!!");
      }

    } else {
      System.out.println("You cannot sell asset that you don't have in your portfolio!!!");
    }

    return sold;
  }

  /**
   * Returns a list of trending stocks symbols, their current market price and the days gain or loss
   * in price and as a percentage. Yahoo finance provides this information for you.
   *
   * @param region a string country code specifying the region of interest. Examples include US, GB,
   *               FR, DE, HK
   * @return a list of strings each representing trending stock symbols e.g. APPL, TSLA, BARC
   */
  @Override
  public List<String> getTrendingStocks(String region) throws IOException, ParseException {
    ArrayList<String> trendingStocksForRegion = new ArrayList<>();

    try {

      JSONObject jsonObject = getJsonResponseFromApiCall(
          "https://yfapi.net/v1/finance/trending/" + region);

      JSONObject financeObject = (JSONObject) jsonObject.get("finance");
      JSONArray resultArray = (JSONArray) financeObject.get("result");

      if (!resultArray.isEmpty()) {
        // Get quotes array from JSON response
        JSONObject jsonResultsObject = (JSONObject) resultArray.get(0);
        JSONArray arrQuotes = (JSONArray) jsonResultsObject.get("quotes");

        // Loop through array and add symbol to list returned
        for (int i = 0; i < arrQuotes.size(); i++) {
          JSONObject jsonObj = (JSONObject) arrQuotes.get(i);
          trendingStocksForRegion.add((String) jsonObj.get("symbol"));

        }
      }


    } catch (IOException ex) {
      throw ex;

    }

    return trendingStocksForRegion;
  }

  /**
   * Retrieve a set of historic data points for the specified assets.
   *
   * @param assetSymbols a list of strings representing the symbols of the assets for which we need
   *                     to obtain Historic data.
   * @param interval     a String representing the time interval between quotes. Valid values
   *                     include 1m 5m 15m 1d 1wk 1mo
   * @param range        a String representing the time range over which we should obtain historic
   *                     data for the specified assets. Valid values include 1d 5d, 1mo, 3mo, 6mo,
   *                     1y, 5y, max. Where max represents the maximum available duration (lifetime
   *                     of the asset).
   * @return A list of assetQuotes objects.
   */
  @Override
  public List<AssetQuote> getHistoricalData(List<String> assetSymbols, String interval,
                                            String range) throws IOException, ParseException {

    List<AssetQuote> assetQuoteList = new ArrayList<>();

    OkHttpClient client = new OkHttpClient().newBuilder().build();
    Request request =
            new Request.Builder()
                    .url(
                            "https://yfapi.net/v8/finance/spark?interval="
                                    + interval
                                    + "&range="
                                    + range
                                    + "&symbols="
                                    + String.join(",", assetSymbols))
                    .method("GET", null)
                    .addHeader("x-api-key", apiKey)
                    .build();
    Response response = client.newCall(request).execute();

    String jsonStr = response.body().string();
    String assetName = null;
    Double closePrice = null;

    JSONParser jsonParser = new JSONParser();

    JSONObject jsonObjectOuter = (JSONObject) jsonParser.parse(jsonStr);

    // Store full names of assets
    Hashtable<String, String> my_dict = new Hashtable<String, String>();
    my_dict.put("AAPL", "Apple");
    my_dict.put("MSFT", "Microsoft");
    my_dict.put("TSLA", "Telsa");
    my_dict.put("BCS", "Barclays PLC");
    my_dict.put("BTC-USD", "Bitcoin USD");
    my_dict.put("MCD", "McDonald's Corp");
    my_dict.put("TEAM", "Atlassian Corporation");
    my_dict.put("HOOD", "Robinhood Markets Inc");
    my_dict.put("V", "Visa");
    my_dict.put("MO", "Altria Group Inc");
    my_dict.put("AMD", "Advanced Micro Devices");
    my_dict.put("LUV", "Southwest Airlines Co");
    my_dict.put("LCID", "Lucid Group Inc");
    my_dict.put("WDS", "WDS");
    my_dict.put("INDO", "Indonesia Energy Corp");
    my_dict.put("SOFI", "SoFi Technologies Inc");
    my_dict.put("TSM", "Taiwan Semiconductor Mfg");
    my_dict.put("X", "United States Steel Corporation");
    my_dict.put("BX", "Blackstone Inc");
    my_dict.put("NUE", "Nucor Corporation");
    my_dict.put("RBLX", "Roblox Corp");
    my_dict.put("SOXL", "Direxion Daily Semiconductor Bull 3X Shares");
    my_dict.put("NG", "NovaGold Resources Inc");

    //for each asset symbol in the assetSymbols list, grab the inner json object
    for (int i = 0; i < assetSymbols.size(); i++) {
      JSONObject jsonObjectInner = (JSONObject) jsonObjectOuter.get(assetSymbols.get(i));

      if (jsonObjectInner != null) {


        //create array lists for close price and timestamp values
        ArrayList closeList = (ArrayList) jsonObjectInner.get("close");
        ArrayList timestampList = (ArrayList) jsonObjectInner.get("timestamp");
        //for each timestamp, get matching close price and assetName
        for (int e = 0; e < timestampList.size(); e++) {

          Long CloseTime = (Long) timestampList.get(e);

          if (closeList.get(e) != null) {
            closePrice = (Double) closeList.get(e);
          } else {
            closePrice = 0.0;
          }

          //Grab the assetName from the dict. if the asset is not in dict, use the asset symbol as assetName
          if (my_dict.get(jsonObjectInner.get("symbol")) != null) {
            assetName = my_dict.get(jsonObjectInner.get("symbol"));
          } else {
            assetName = jsonObjectInner.get("symbol").toString();
          }
          //After parsing the assetName, closeTime and closePrice, create AssetQuote object
          AssetQuote assetQuote =
                  new AssetQuote(
                          jsonObjectInner.get("symbol").toString(), assetName, CloseTime, closePrice);
          assetQuoteList.add(assetQuote);
        }
      }
    }

    return assetQuoteList;
  }

  /**
   * Returns summary information on an exchange in the region specified.
   *
   * @param region   a string country code specifying the region of interest. Examples include US,
   *                 GB, FR, DE, HK
   * @param exchange a string specifying the exchange we want information on. Examples include FTSE,
   *                 DOW, DASDAQ, DAX
   * @return a String containing exchange summary information. Data includes at a minimum the
   * exchange name, exchange symbol, previous closing value, opening value, gain/loss since opening.
   * Add any additional data you feel is relevant.
   */


  @Override
  public String getExchangeSummary(String region, String exchange)
      throws IOException, ParseException {

    JSONObject jsonObject = getJsonResponseFromApiCall(
        "https://yfapi.net/v6/finance/quote/marketSummary?lang=en&region=" + region);
    JSONObject quoteResponse = (JSONObject) jsonObject.get("marketSummaryResponse");

    String output = "";

    JSONArray results = (JSONArray) quoteResponse.get("result");
    //result of response stored in a jason array

    for (int i = 0; i < results.size(); i++) {
      //For loop cycles through array with results of all exchanges associated with a particular region
      JSONObject jsonObj = (JSONObject) results.get(i);

      String nameOfCurrentExchange = ((String) jsonObj.get("fullExchangeName"));
      //String nameOFCurrentExchange stores the current exchange in the cycle to be compared in an if statement
      String[] splited = nameOfCurrentExchange.split(" ");
      String[] exchangeSplit = exchange.split(" ");

      if (splited[0].equals(exchangeSplit[0])) {
        //Current exchange stored in the for loop is compared with user input

        output += "Exchange Time Zone Name= " + jsonObj.get("exchangeTimezoneName")
            + System.lineSeparator();
        output += "Full Exchange Name= " + jsonObj.get("fullExchangeName")
            + System.lineSeparator();
        output += "Symbol= " + jsonObj.get("symbol") + System.lineSeparator();
        //Element of json object is appended to variable string to be returned to main method, line separator makes the output more readable

        JSONObject marketPRice = (JSONObject) jsonObj.get("regularMarketPrice");
        String mPrice = (String) marketPRice.get("fmt");
        JSONObject lastClosePrice = (JSONObject) jsonObj.get("regularMarketPreviousClose");
        String lClosePrice = (String) lastClosePrice.get("fmt");
        JSONObject ChangeSinceOpening = (JSONObject) jsonObj.get("regularMarketChangePercent");
        String openingChangeInPrice = (String) ChangeSinceOpening.get("fmt");
        //Prices are taken from json objects and converted to string above

        output += "Regular Market Price= " + mPrice + System.lineSeparator();
        output += "Change in price since opening= " + openingChangeInPrice + System.lineSeparator();
        output += "Closing price = " + lClosePrice;
        //prices are appended to the string for return

        break;


      }
    }
    return output;

  }

  private JSONObject getJsonResponseFromApiCall(String url) throws IOException, ParseException {
    OkHttpClient client = new OkHttpClient().newBuilder()
        .build();
    Request request = new Request.Builder()
        .url(url)
        .method("GET", null)
        .addHeader("X-API-KEY", apiKey)
        .build();
    Response response = client.newCall(request).execute();

    String jsonStr = response.body().string();
    JSONParser jsonParser = new JSONParser();
    Object jsonO = jsonParser.parse(jsonStr);
    JSONObject jsonObject = (JSONObject) jsonO;

    return jsonObject;
  }

  /**
   * Retrieve realtime quote data for the assets within the list assetNames from the online
   * exchange.
   *
   * @param assetNames a list of asset symbols for example, "Bitcoin-USD", "Appl", "TSLA"
   * @return A list of AssetQuote objects. Return an empty list if we have no assets in our
   * portfolio.
   */

  @Override
  public List<AssetQuote> getAssetInformation(List<String> assetNames)
      throws IOException, ParseException {

    List<AssetQuote> actualAssetInformation = new ArrayList<>();

    OkHttpClient client = new OkHttpClient().newBuilder()
        .build();
    Request request = new Builder()
        .url("https://yfapi.net/v6/finance/quote?region=US&lang=en&symbols=" + String.join(",",
            assetNames))
        .method("GET", null)
        .addHeader("x-api-key", apiKey)
        .build();

    Response response = null;
    try {
      response = client.newCall(request).execute();

    } catch (IOException e) {
      e.printStackTrace();
    }

    String jsonStr = response.body().string();

    JSONParser jsonParser = new JSONParser();
    Object jsonO = jsonParser.parse(jsonStr);

    JSONObject jsonObject = (JSONObject) jsonO;

    JSONObject quoteResponse = (JSONObject) jsonObject.get("quoteResponse");

    JSONArray results = (JSONArray) quoteResponse.get("result");




    for (int i = 0; i < results.size(); i++) {
      AssetQuote assetQuote = new AssetQuote();
      JSONObject jsonObj = (JSONObject) results.get(i);
      assetQuote.setAssetSymbol((String) jsonObj.get("symbol"));
      assetQuote.setAssetFullName((String) jsonObj.get("shortName"));
      assetQuote.setValue((Double) jsonObj.get("regularMarketPrice"));
      actualAssetInformation.add(assetQuote);
    }

    return actualAssetInformation;
  }

  /**
   * Retrieve the current value of all of the assets in the portfolio based on the current live
   * value of each asset.
   *
   * @return a double representing the value of the portfolio in USD
   */
  @Override
  public double getPortfolioValue() throws ParseException, IOException {

    double total = 0.0;
    // making an API call using OkHttpClient
    OkHttpClient client = new OkHttpClient().newBuilder()
        .build();

    // Getting current value of assets in order to calculate value of all assets in the portfolio
    for (AssetQuote aq : AssetQuote.assetQuotes) {
      Request request =
          new Request.Builder()
              .url("https://yfapi.net/v6/finance/quote?region=US&lang=en&symbols="
                  + aq.getAssetSymbol())
              .method("GET", null)
              .addHeader("X-API-KEY", apiKey)
              .build();
      Response response = null;
      try {
        response = client.newCall(request).execute();
      } catch (IOException e) {
        e.printStackTrace();
      }

      String jsonStr = response.body().string();

      JSONParser jsonParser = new JSONParser();
      Object jsonO = jsonParser.parse(jsonStr);

      JSONObject jsonObject = (JSONObject) jsonO;

      JSONObject quoteResponse = (JSONObject) jsonObject.get("quoteResponse");

      JSONArray resultArray = (JSONArray) quoteResponse.get("result");

      String symbol = "";
      Double regMarkPrice = 0.0;
      String assetLongName;
      Long timeStamp = (long) 0;

      for (int i = 0; i < resultArray.size(); i++) {
        JSONObject jsonObj = (JSONObject) resultArray.get(i);
        symbol = (String) jsonObj.get("symbol");
        regMarkPrice = (Double) jsonObj.get("regularMarketPrice");
        assetLongName = (String) jsonObj.get("longName");
        timeStamp = (Long) jsonObj.get("regularMarketTime");

      }
      total += aq.getStockAmount() * regMarkPrice;
    }
    return total;
  }

  /**
   * Returns a formatted string detailing the name, symbol, average purchase price, current value
   * and amount of each asset within the portfolio. The difference in average purchase price and
   * current price should also be displayed in both USD and as a percentage.
   *
   * @return a String containing summary information on the assets in the portfolio.
   */
  @Override
  public String listAllInvestments() throws IOException, ParseException {

    String investments = new String();
    int i;
    //iterate through the list of assetQuotes and get the values needed for returned string
    for (i = 0; i < AssetQuote.assetQuotes.size(); i++) {

      String name = AssetQuote.assetQuotes.get(i).getAssetFullName();
      String symbol = AssetQuote.assetQuotes.get(i).getAssetSymbol();
      Double stockAmount = AssetQuote.assetQuotes.get(i).getStockAmount();
      Double purchaseValue = AssetQuote.assetQuotes.get(i).getValue();



      Double averagePurchasePrice = purchaseValue/stockAmount;
      List<String> assets = Arrays.asList(String.join(",", symbol));

      List<AssetQuote> assetInformation = getAssetInformation(assets);
      Double currentPrice = assetInformation.get(0).getValue();

      Double currentValue = currentPrice * stockAmount;
      Double priceDifference = currentPrice - averagePurchasePrice;
      Double priceDifferencePercentage = (priceDifference /averagePurchasePrice) * 100;

      //Create a String with values and with new line separator for each asset
      investments += System.lineSeparator()
              + "| Name: " + name + " | Symbol: " + symbol + " | Purchase Price: "
              + Math.round(averagePurchasePrice*100.0)/100.0 +  " | Current Price: " +
              Math.round(currentPrice*100.0)/100.0 + " | Stock Amount: " +  stockAmount + " | Current Value: "
              //convert epoch to human-readable
              + Math.round(currentValue*100.0)/100.0 +
              " | Price Diff $: " +  Math.round(priceDifference *100.0)/100.0 +
              " | Price Diff %: " + Math.round(priceDifferencePercentage*100.0)/100.0;

    }
    return investments;
  }
  /**
   * Retrieve a formatted string containing all of the assets within the portfolio of the specified
   * asset type ("stock" or "cryptocurrencies"). String contains the name, symbol, average purchase
   * price, current value and amount of each asset within the portfolio. The difference in average
   * purchase price and current price are displayed in USD and as a percentage.
   *
   * @param assetType a string specifying the asset type. Valid values are "stock" or "crypto"
   * @return a formatted String containing summary of all of the investments within the portfolio.
   * Return an empty string if we have no assets within our portfolio.
   */
  @Override
  public String listPortfolioAssetsByType(String assetType) throws IOException, ParseException {
    String investments = "";
    int i;
    //iterate through the list of assetQuotes
    for (i = 0; i < AssetQuote.assetQuotes.size(); i++) {

      String symbol = AssetQuote.assetQuotes.get(i).getAssetSymbol();
      String quoteType = AssetQuote.assetQuotes.get(i).getType();

      if (assetType.contains("Crypto")) {

        //if the assetType requested is Crypto and quoteType matches get the values needed for return string
        if (quoteType.contains("CRYPTOCURRENCY")) {

          String name = AssetQuote.assetQuotes.get(i).getAssetFullName();

          Double stockAmount = AssetQuote.assetQuotes.get(i).getStockAmount();
          Double purchaseValue = AssetQuote.assetQuotes.get(i).getValue();

          //calculate the average purchase price
          Double averagePurchasePrice = purchaseValue / stockAmount;

          // create list, add asset
          List<String> assets = Arrays.asList(String.join(",", symbol));
          //use getAssetInformation method to grab the currentPrice of the asset
          List<AssetQuote> assetInformation = getAssetInformation(assets);
          Double currentPrice = assetInformation.get(0).getValue();

          Double currentValue = currentPrice * stockAmount;
          Double priceDifference = currentPrice - averagePurchasePrice;
          //calculate the percentage price difference
          Double priceDifferencePercentage = (priceDifference / averagePurchasePrice) * 100;

          investments += System.lineSeparator()
                  + "| Name: " + name + " | Symbol: " + symbol + " | Purchase Price: "
                  + Math.round(averagePurchasePrice * 100.0) / 100.0 + " | Current Price: " +
                  Math.round(currentPrice * 100.0) / 100.0 + " | Stock Amount: " + stockAmount
                  + " | Current Value: " + Math.round(currentValue * 100.0) / 100.0 +
                  " | Price Diff $: " + Math.round(priceDifference * 100.0) / 100.0 +
                  " | Price Diff %: " + Math.round(priceDifferencePercentage * 100.0) / 100.0;
        }
      }
      else {

        //if the assetType requested is EQUITY get the values needed for return string
        if (assetType.contains("EQUITY"))  {

          String name = AssetQuote.assetQuotes.get(i).getAssetFullName();

          Double stockAmount = AssetQuote.assetQuotes.get(i).getStockAmount();
          Double purchaseValue = AssetQuote.assetQuotes.get(i).getValue();

          Double averagePurchasePrice = purchaseValue / stockAmount;
          List<String> assets = Arrays.asList(String.join(",", symbol));

          List<AssetQuote> assetInformation = getAssetInformation(assets);
          Double currentPrice = assetInformation.get(0).getValue();

          Double currentValue = currentPrice * stockAmount;
          Double priceDifference = currentPrice - averagePurchasePrice;
          Double priceDifferencePercentage = (priceDifference / averagePurchasePrice) * 100;

          investments += System.lineSeparator()
                  + "| Name: " + name + " | Symbol: " + symbol + " | Purchase Price: "
                  + Math.round(averagePurchasePrice * 100.0) / 100.0 + " | Current Price: " +
                  Math.round(currentPrice * 100.0) / 100.0 + " | Stock Amount: " + stockAmount
                  + " | Current Value: " + Math.round(currentValue * 100.0) / 100.0 +
                  " | Price Diff $: " + Math.round(priceDifference * 100.0) / 100.0 +
                  " | Price Diff %: " + Math.round(priceDifferencePercentage * 100.0) / 100.0;




        }

      }

    }
    return investments;
  }

  /**
   * Retrieve a formatted String containing details on all of the assets within the portfolio
   * matching the assetName in full or partially. String contains the name, symbol, average purchase
   * price, current value and amount of each asset within the portfolio. The difference in average
   * purchase price and current price are displayed in USD and as a percentage.
   *
   * @param assetNames a list of Strings containing asset symbols such as "MSFT" or "BTC-USD" or
   *                   full name "Bitcoin USD" or partial string "Bitco"
   * @return A formatted String containing summary information for the assetNames provided in the
   * list. Return an empty string if we have no matching assets.
   */
  @Override
  public String listPortfolioAssetsByName(List<String> assetNames)
      throws IOException, ParseException {
    String result = "";

    // Build the summary table of assets held
    LinkedHashMap<String, AssetQuote> assetSummary = populateAssetQuoteSummary();

    // List of assets found during search
    ArrayList<String> assetInPortfolio = new ArrayList<String>();

    // Check for empty list passed in
    if (assetNames.isEmpty()) {
      result = "";
      return result;
    }

    // Check if Symbol or Name match found from input
    for (String valueToCheck : assetNames) {

      // Get key set for the summary table of assets.
      Set<String> setOfQuotes = assetSummary.keySet();

      // Loop through table
      for (String key : setOfQuotes) {

        AssetQuote assetToCheck = assetSummary.get(key);

        // Check if there is a match on Symbol or full name
        if ((assetToCheck.getAssetSymbol().startsWith(valueToCheck))
            || (assetToCheck.getAssetFullName().startsWith(valueToCheck))) {

          // Add to list if not already there
          if (!assetInPortfolio.contains(assetToCheck.getAssetSymbol())) {
            assetInPortfolio.add(assetToCheck.getAssetSymbol());
          }
        }
      }
    }

    // Check if search found a match in the portfolio
    if (assetInPortfolio.isEmpty()) {
      result = "";
      return result;
    }

    //sort list
    Collections.sort(assetInPortfolio);

    // Get the current price for assets found
    ArrayList<AssetQuote> currentAssetQuoteList = getAssetQuoteDailyChange(
        assetInPortfolio);

    //Set Header for output
    result = String.format("%-25s %-8s %-18s %-18s %-11s %-11s %-11s %n", "Name", "Symbol",
        "Avg Purchase Price", "Total Cost", "Amount held",
        "+/- Current", "+/- Current %");

    for (String currAsset : assetInPortfolio) {
      Double currentPrice = currentAssetQuoteList.get(
          indexForAssetSummaryWithSymbol(currentAssetQuoteList, currAsset)).getValue();

      AssetQuote summaryAsset = assetSummary.get(currAsset);
      Double averagePurchasePrice = summaryAsset.getValue() / summaryAsset.getStockAmount();

      //add the line record for each asset with required details
      result += String.format("%-25s %-8s %-18f %-18f %-11f %-11f %-11f %n",
          summaryAsset.getAssetFullName(), summaryAsset.getAssetSymbol(),
          averagePurchasePrice, summaryAsset.getValue(), summaryAsset.getStockAmount(),
          (currentPrice - averagePurchasePrice),
          (100 * (currentPrice - averagePurchasePrice) / abs(averagePurchasePrice)));


    }

    assetSummary.clear();

    return result;
  }

  /**
   * Retrieve a formatted String containing summary information for all assets within the portfolio
   * purchased between the dates startTimeStamp and endTimeStamp. Summary information contains the
   * purchase price, current price, difference between the purchase and sale price (in USD and as a
   * percentage).
   * <p>
   * If the several units of the asset have been purchased at different time points between the
   * startTimeStamp and endTimeStamp, list each asset purchase separately by date (oldest to most
   * recent).
   *
   * @param startTimeStamp a UNIX timestamp representing the start range date
   * @param endTimeStamp   a UNIX timestamp representing the end range date
   * @return A formatted String containing summary information for all of the assets purchased
   * between the startTimeStamp and endTimeStamp. Return an empty string if we have no matching
   * assets in our portfolio.
   */

  // Khalid
  @Override
  public String listPortfolioPurchasesInRange(long startTimeStamp, long endTimeStamp) {
    String retString = "";
    // Date Comparator lambda expression
    Comparator<AssetQuote> dateComparator = (o1, o2) -> {
      if (o1.getTimeStamp() > o2.getTimeStamp()) {
        return 1;
      } else if (o1.getTimeStamp() < o2.getTimeStamp()) {
        return -1;
      } else {
        return 0;
      }
    };
    // add purchased assets into the list in timestamp range
    ArrayList<AssetQuote> list = new ArrayList<AssetQuote>();
    for (AssetQuote curr : purchasedAssets) {

      SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy 'at' h:mm a");
      String date = sdf.format(curr.getTimeStamp() * 1000);





      if (curr.getTimeStamp() >= startTimeStamp && curr.getTimeStamp() <= endTimeStamp) {
        list.add(curr);
      }
    }
    // sort the list by date comparator(see above comparator lambda)
    list.sort(dateComparator);


    double soldValue = 0.0; // sold value from soldAssets list for the symbol to search
    int tempIndex = 0;
    for (AssetQuote purchaseQuote : list) {  // iterate purchased list just made
      soldValue = 0.0;
      if (purchaseQuote.getValue() != 0) {
        for (AssetQuote soldQuote : soldAssets) {
          if (purchaseQuote.getAssetSymbol().equals(soldQuote.getAssetSymbol())) {
            soldValue = soldQuote.getValue(); // get the sold value from the soldAssets list for the purchased symbol
            break;
          }
        }

        // Insert the Libe break at the first time.
        if(tempIndex > 0)
          retString += "\n";
        tempIndex++;
        // make the formatted string to return
        if (soldValue != 0.0) {
          retString += String.format("Symbol: %s, Purchase Price: %g, Current Price: %g, Difference: %g%%, Date: %s",
                  purchaseQuote.getAssetSymbol(), purchaseQuote.getValue(),
                  purchaseQuote.getValue() / purchaseQuote.getStockAmount(),
                  purchaseQuote.getValue() * 100 / soldValue,
                  epoch2date(purchaseQuote.getTimeStamp()));
        } else {
          retString += String.format("Symbol: %s, Purchase Price: %g, Current Price: %g, Difference: %d%%, Date: %s",
                  purchaseQuote.getAssetSymbol(), purchaseQuote.getValue(),
                  purchaseQuote.getValue() / purchaseQuote.getStockAmount(),
                  100,
                  epoch2date(purchaseQuote.getTimeStamp()));
        }
      }
    }


    return retString;
  }

  // TO DO
  public String toString() {

    return null;
  }

  /**
   * Retrieve a formatted string containing a summary of all of the assets sales between the dates
   * startTimeStamp and endTimeStamp. Summary information contains the average purchase price for
   * each asset, the sale price and the profit or loss (in USD and as a percentage).
   * <p>
   * If the several units of the asset have been sold at different time points between the
   * startTimeStamp and endTimeStamp, list by date (oldest to most recent) each of those individual
   * sales.
   *
   * @param startTimeStamp a UNIX timestamp representing the start range date
   * @param endTimeStamp   a UNIX timestamp representing the end range date
   * @return A formatted String containing summary information for all of the assets sold between
   * the startTimeStamp and endTimeStamp. Return an empty string if we have no matching assets in
   * our portfolio.
   */

  //stop
  @Override
  public String listPortfolioSalesInRange(long startTimeStamp, long endTimeStamp) {
    String retString = "";  // string to return

    // // Date Comparator lambda expression
    Comparator<AssetQuote> dateComparator = (o1, o2) -> {
      if (o1.getTimeStamp() > o2.getTimeStamp()) {
        return 1;
      } else if (o1.getTimeStamp() < o2.getTimeStamp()) {
        return -1;
      } else {
        return 0;
      }
    };

    // add sold assets into the list in timestamp range
    ArrayList<AssetQuote> list = new ArrayList<AssetQuote>();
    for (AssetQuote curr : soldAssets) {
      if (curr.getTimeStamp() >= startTimeStamp && curr.getTimeStamp() <= endTimeStamp) {
        list.add(curr);
      }
    }

    // sort the list by date comparator(see above comparator lambda)
    list.sort(dateComparator);

    double averagePurchasePrice = 0.0;// average purchased price
    int nPurchaseCount = 0;// The number of purchased asset to count average purchased price
    int tempIndex = 0;//

    for (AssetQuote soldQuote : list) {// iterate sold list just made
      if (soldQuote.getValue() != 0) {
        averagePurchasePrice = 0.0;
        nPurchaseCount = 0;
        for (AssetQuote purchaseQuote : soldAssets) {
          if (soldQuote.getAssetSymbol().equals(purchaseQuote.getAssetSymbol())) {

            // get sum of purchased price from purchased assets for the sold asset symbol
            averagePurchasePrice += purchaseQuote.getValue();
            nPurchaseCount++;// increase the number of purchased asset for the sold asset symbol
          }
        }
        // Insert the Libe break at the first time.
        if(tempIndex > 0)
          retString += "\n";
        tempIndex++;

        averagePurchasePrice /= nPurchaseCount;// average purchased price

        // make the formatted string to return.
        retString += String.format("Symbol: %s, Average Purchase Price: %g, Profit or Loss: %g%%, Date: %s",
                soldQuote.getAssetSymbol(), averagePurchasePrice,
                (soldQuote.getValue() - averagePurchasePrice) * 100 / soldQuote.getValue(),
                epoch2date(soldQuote.getTimeStamp()));
      }
    }


    return retString;
  }

  // Khalid
  public static long date2epoch(String strDate) throws java.text.ParseException {

    // Ask user to input start date in dd/MM/yyyy format

    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    Date date = df.parse(strDate);
    long epoch = date.getTime() / 1000; // in seconds
    return epoch;

  }

  public static String epoch2date(long epoch) {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    return sdf.format(new Date(epoch*1000));
  }


  /**
   *  This method call the yahoo api to get quote details for a list of symbols
   *
   * @param symbols List of symbols to search
   * @return List of asset quotes
   */

  public ArrayList<AssetQuote> getAssetQuoteDailyChange(ArrayList<String> symbols)
          throws IOException, ParseException {

    // A list to hold quotes for Trending Stocks
    ArrayList<AssetQuote> trendingQuoteDetails = new ArrayList<AssetQuote>();

    // Get the latest currency values to allow conversion to USD
    LinkedHashMap<String, Double> exchangeRates;
    exchangeRates = populateCurrencyMap();

    if (symbols.isEmpty()) {
      return trendingQuoteDetails;
    }

    try {

      JSONObject jsonObject = getJsonResponseFromApiCall(
              "https://yfapi.net/v6/finance/quote?region=US&lang=en&symbols=" + URLEncoder.encode(
                      String.join(",", symbols), StandardCharsets.UTF_8));

      JSONObject quoteResponse = (JSONObject) jsonObject.get("quoteResponse");

      JSONArray resultArray = (JSONArray) quoteResponse.get("result");

      // Loop through array, create an AssetQuoteForDate object and add to list
      for (int i = 0; i < resultArray.size(); i++) {

        AssetQuote assetQuoteDailyChange = new AssetQuote();

        JSONObject jsonResultsObject = (JSONObject) resultArray.get(i);

        assetQuoteDailyChange.setAssetSymbol((String) jsonResultsObject.get("symbol"));
        assetQuoteDailyChange.setTimeStamp(System.currentTimeMillis() / 1000);

        // Get name from shortname or long name or set a not found value
        if (jsonResultsObject.containsKey("shortName")) {
          assetQuoteDailyChange.setAssetFullName((String) jsonResultsObject.get("shortName"));
        } else if (jsonResultsObject.containsKey("longName")) {
          assetQuoteDailyChange.setAssetFullName((String) jsonResultsObject.get("longName"));
        } else {
          assetQuoteDailyChange.setAssetFullName("Name not returned from API");
        }

        assetQuoteDailyChange.setType((String) jsonResultsObject.get("quoteType"));
        assetQuoteDailyChange.setOpeningValue(
                (Double) jsonResultsObject.get("regularMarketPreviousClose"));
        assetQuoteDailyChange.setValue((Double) jsonResultsObject.get("regularMarketPrice"));

        String quoteCurrency = (String) jsonResultsObject.get("currency");

        // Check if currency USD and convert if not (Exclude market index values)
        if (!quoteCurrency.equals("USD") && !assetQuoteDailyChange.getType().equals("INDEX")) {
          if (exchangeRates.containsKey(quoteCurrency.toUpperCase() + "/USD")) {

            assetQuoteDailyChange.setOpeningValue(
                    assetQuoteDailyChange.getOpeningValue() * exchangeRates.get(
                            quoteCurrency.toUpperCase() + "/USD"));

            assetQuoteDailyChange.setValue(assetQuoteDailyChange.getValue() * exchangeRates.get(
                    quoteCurrency.toUpperCase() + "/USD"));


          }
        }

        trendingQuoteDetails.add(assetQuoteDailyChange);


      }


    } catch (IOException ex) {
      throw ex;

    }

    return trendingQuoteDetails;

  }

  /***
   *  Method to populate the exchange rate table to allow conversion to USD.
   *  Currencies stored - Euro,Canadian Dollar,British Pound,Japanese Yen,Swiss Franc,
   *                      Hong Kong Dollar,Saudi Riyal,Australian Dollar,Russian Ruble.
   * @return Hashtable containing the exchange rates
   * @throws IOException
   * @throws ParseException
   */
  public LinkedHashMap<String, Double> populateCurrencyMap() throws IOException, ParseException {

    LinkedHashMap<String, Double> exchangeRates = new LinkedHashMap<>(9);
    JSONObject jsonObject = getJsonResponseFromApiCall(
            "https://yfapi.net/v6/finance/quote?region=US&lang=en&symbols=EURUSD=X,CADUSD=X,GBPUSD=X,JPYUSD=X,CHFUSD=X,HKDUSD=X,SARUSD=X,AUDUSD=X,RUBUSD=X");

    JSONObject quoteResponse = (JSONObject) jsonObject.get("quoteResponse");
    JSONArray resultArray = (JSONArray) quoteResponse.get("result");

    for (int i = 0; i < resultArray.size(); i++) {
      JSONObject jsonResultsObject = (JSONObject) resultArray.get(i);
      exchangeRates.put((String) jsonResultsObject.get("shortName"),
              (Double) jsonResultsObject.get("regularMarketPrice"));
    }

    return exchangeRates;

  }

  /**
   * Build the summary table of assets in portfolio. Total purchase price and total stock amount
   * held of each asset.
   *
   * @return LinkedHashMap containing summary of assets held
   */
  public LinkedHashMap<String, AssetQuote> populateAssetQuoteSummary() {
    LinkedHashMap<String, AssetQuote> assetSummaries = new LinkedHashMap<String, AssetQuote>();

    // Search assets held
    for (AssetQuote assetQuote : AssetQuote.assetQuotes) {
      // If asset not in table add
      if (!assetSummaries.containsKey(assetQuote.getAssetSymbol())) {
        AssetQuote summaryNew = new AssetQuote();
        summaryNew.setAssetSymbol(assetQuote.getAssetSymbol());
        summaryNew.setAssetFullName(assetQuote.getAssetFullName());
        summaryNew.setStockAmount(assetQuote.getStockAmount());
        summaryNew.setValue(assetQuote.getValue());
        assetSummaries.put(assetQuote.getAssetSymbol(), summaryNew);

      } else {
        // Value already in table, add the stock amount held and purchase value
        AssetQuote summaryExists = assetSummaries.get(assetQuote.getAssetSymbol());
        summaryExists.setStockAmount(assetQuote.getStockAmount() + summaryExists.getStockAmount());
        summaryExists.setValue(assetQuote.getValue() + summaryExists.getValue());

      }

    }
    return assetSummaries;
  }

}
