package PortfolioSystem;


import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import org.json.simple.parser.ParseException;


public class Main {

  public static boolean isNegative(double d) {
    return Double.compare(d, 0.0) < 0;
  }

  public static void main(String[] args) throws IOException, ParseException {
    /* TODO: Initialise and run your investment portfolio management system */

    Scanner sc = new Scanner(System.in);

    // Application starts with below Assets
    AssetQuote a1 = new AssetQuote("TSLA", "Tesla", 1633108078, 7752.2, 10, "EQUITY");
    AssetQuote a2 = new AssetQuote("AAPL", "Apple", 1625504878, 2799.2, 20, "EQUITY");
    AssetQuote a3 = new AssetQuote("NVDA", "Nvidia Corporation", 1618412878, 1833.24,
        12, "EQUITY");
    AssetQuote a4 = new AssetQuote("BTC-USD", "Bitcoin", 1612883278, 2000, 0.0445881,
        "CRYPTOCURRENCY");

    //ArrayList<AssetQuote> assetQuotes = new ArrayList<AssetQuote>();
    AssetQuote.assetQuotes.add(a1);
    AssetQuote.assetQuotes.add(a2);
    AssetQuote.assetQuotes.add(a3);
    AssetQuote.assetQuotes.add(a4);

    // Adding the assets the application starts with to the list of purchased assets
    PortfolioSystemOperations.purchasedAssets.add(a1);
    PortfolioSystemOperations.purchasedAssets.add(a2);
    PortfolioSystemOperations.purchasedAssets.add(a3);
    PortfolioSystemOperations.purchasedAssets.add(a4);

    PortfolioSystemOperations pso = new PortfolioSystemOperations();

    String assetSymbol = "";
    double amount = 0.0;
    int option = 0;
    System.out.println("Welcome to Group4's Portfolio Mgmt System");
    System.out.println();
    System.out.println("Please enter your username to log into the system");
    String userName = sc.nextLine();
    System.out.println();
    System.out.println(
        "Thank you " + userName + ". You have the following assets in your portfolio:");
    calculateTotal();
    System.out.println();
    System.out.println("To proceed further, please enter your starting balance: ");
    double startingBalance;
    double balance;

    //validate user input
    while (!sc.hasNextDouble()) {
      System.out.println("Please enter a valid number");
      sc.next();
    }
    startingBalance = sc.nextDouble();
    while (isNegative(startingBalance)) {
      System.out.println("Please enter a positive number: ");
      startingBalance = sc.nextDouble();
    }
    AssetQuote.setAvailableFunds(startingBalance);
    pso.setStartingBalance(startingBalance);

    int userChoice;
    boolean quit = false;

    do {
      System.out.println();
      System.out.println("The Available Asset Symbols to use to the Portfolio Mgmt system are: "
          + "MSFT, TSLA, MCD, TEAM, NVDA, HOOD, V, " +
          "MO, AMD, LUV, LCID, WDS, INDO, SOFI, TSM, X, AFRM, BX, NUE, RBLX, SOXL, NG, AAPL, BTC-USD ");
      System.out.println("The available options are:");
      System.out.println("1. Add Funds");
      System.out.println("2. Withdraw Funds");
      System.out.println("3. Check Funds");
      System.out.println("4. Purchase Asset");
      System.out.println("5. Sell Asset");
      System.out.println("6. Get the total value of assets within my portfolio");
      System.out.println("7. Get Live Summary ");
      System.out.println("8. Get Historical data on an Asset ");
      System.out.println("9. List All Investments");
      System.out.println("10. List Investments By Asset Type");
      System.out.println("11. Get Summary Exchange");
      System.out.println("12. List portfolio purchases in range ");
      System.out.println("13. List portfolio sales in range ");
      System.out.println("14. Trending stocks");
      System.out.println("15. Summary Search");
      System.out.println();
      System.out.println("Please enter a number from 1 to 15 to select an option from the menu."
          + " You can  enter 0 to quit: ");

      //validate user input
      while (!sc.hasNextInt()) {
        System.out.println(
            "Please enter a valid number 1-15 to select an option from the menu or enter 0 to quit: ");
        sc.next();
      }
      userChoice = sc.nextInt();

      switch (userChoice) {
        case 1:
          System.out.print("How much do you want to add: ");
          while (!sc.hasNextDouble()) {
            System.out.println("Please enter a valid number to add to your funds");
            sc.next();
          }
          double addAmount = sc.nextDouble();
          while (isNegative(addAmount)) {
            System.out.print("Please enter a positive number: ");
            addAmount = sc.nextDouble();
          }
          pso.addFunds(addAmount);
          balance = pso.getAvailableFunds();
          System.out.println("Balance: " + balance);
          break;
        case 2:
          System.out.print("How much do you want to withdraw: ");
          while (!sc.hasNextDouble()) {
            System.out.println("Please enter a valid number to withdraw from your funds");
            sc.next();
          }
          double withdrawAmount = sc.nextDouble();
          while (isNegative(withdrawAmount)) {
            System.out.print("Please enter a positive number: ");
            withdrawAmount = sc.nextDouble();
          }
          pso.withdrawFunds(withdrawAmount);
          balance = pso.getAvailableFunds();
          AssetQuote.setAvailableFunds(balance);
          System.out.println("Balance: " + balance);
          break;

        case 3:
          balance = pso.getAvailableFunds();
          AssetQuote.setAvailableFunds(balance);
          System.out.println("Balance: " + balance);
          break;
        case 4:
          try {
            System.out.println("Please provide asset symbol:");
            assetSymbol = sc.next();
            System.out.println("Please provide how many asset units you want to purchase:");

            if (assetSymbol.equals("BTC-USD")) {
              amount = sc.nextDouble();
            } else {
              amount = sc.nextInt();
            }
            pso.purchaseAsset(assetSymbol, amount);
          } catch (IOException e) {
            e.printStackTrace();
          } catch (ParseException e) {
            e.printStackTrace();
          } catch (NullPointerException e) {
            e.getMessage();
          } catch (InputMismatchException e) {
            e.getMessage();
          } catch (IndexOutOfBoundsException e) {
            e.getMessage();
          }
          calculateTotal();
          break;
        case 5:
          try {
            System.out.println("Please provide asset symbol:");
            assetSymbol = sc.next();
            System.out.println("Please provide how many asset units you want to sell:");
            if (assetSymbol.equals("BTC-USD")) {
              amount = sc.nextDouble();
            } else {
              amount = sc.nextInt();
            }
            pso.sellAsset(assetSymbol, amount);
          } catch (IOException e) {
            e.printStackTrace();
          } catch (ParseException e) {
            e.printStackTrace();
          } catch (NullPointerException e) {
            System.out.println("Please choose different option BTC is not available!!!");
          } catch (ConcurrentModificationException e) {
            e.getMessage();
          } catch (InputMismatchException e) {
            e.getMessage();
          } catch (IndexOutOfBoundsException e) {
            e.getMessage();
          }
          calculateTotal();
          break;
        case 6:
          try {
            System.out.println("Total Value of All Assets Based on Current Prices Is:  $" +
                pso.getPortfolioValue());
          } catch (IOException e) {
            e.getMessage();
          } catch (ParseException e) {
            e.getMessage();
          }
          break;

        case 7:
          System.out.print(
              "What Asset/Assets would you like information on (example AAPL,TSLA):  ");
          String assetName = sc.next();
          List<String> assetInformation = Arrays.asList(String.join(",", assetName));
          List<AssetQuote> listOfAssetQuotes;
          listOfAssetQuotes = pso.getAssetInformation(assetInformation);
          for (int i = 0; i < listOfAssetQuotes.size(); i++) {
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("Name: " + listOfAssetQuotes.get(i).getAssetFullName());
            System.out.println("Asset Symbol: " + listOfAssetQuotes.get(i).getAssetSymbol());
            System.out.println("Current value: " + listOfAssetQuotes.get(i).getValue());
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~");
          }
          break;

        case 8:
          String range = new String();
          String interval = new String();

          System.out.println("Please enter a stock symbol to fetch historical data. Examples are: ");
          System.out.println("MSFT, TSLA, MCD, TEAM, NVDA, HOOD, V, MO, AMD, LUV, LCID, WDS," +
                  "INDO, SOFI, TSM, X, AFRM, BX, NUE, RBLX, SOXL, NG, AAPL, BTC-USD");
          String stock = sc.next();

          boolean validRange = false;

          while (!validRange) {
            System.out.println("Please choose a range: ");
            System.out.println("1d, 5d, 1mo, 3mo, 6mo, 1y, 5y, max");
            range = sc.next();
            if (!Arrays.asList("1d", "5d", "1mo", "3mo", "6mo", "1y", "5y", "max").contains(range)) {

              System.out.println("Please enter a valid range");

            } else {
              validRange = true;
            }

          }

          boolean validInterval = false;

          while (!validInterval) {
            System.out.println("Please choose an interval: ");
            System.out.println("1m, 5m, 15m 1d, 1wk, 1mo");
            interval = sc.next();
            if (!Arrays.asList("1m", "5m", "15m", "1d", "1wk", "1mo").contains(interval)) {

              System.out.println("Please enter a valid range");

            } else {
              validInterval = true;
            }

          }


          List assetSymbols = new ArrayList<>();
          assetSymbols.add(stock);

          Date closeDate = new Date();

          List<AssetQuote> assetQuoteList = pso.getHistoricalData(assetSymbols, interval, range);

          System.out.println("Name" + "      | Symbol" + " | Timestamp" + " | Close Price");
          for (int f = 0; f < assetQuoteList.size(); f++) {
            //convert the date from unix time to human-readable time
            closeDate.setTime((long) assetQuoteList.get(f).getTimeStamp() * 1000);
            System.out.println(
                    assetQuoteList.get(f).getAssetFullName() + " | " + assetQuoteList.get(f)
                            .getAssetSymbol()
                            + " | " + closeDate + " | " + assetQuoteList.get(f).getValue());
          }
          break;

        case 9:
          System.out.println(pso.listAllInvestments());
          break;

        case 10:
          boolean validAsset = false;
          String asset = "";

          while (!validAsset) {
            System.out.println("Please choose: 'Stock' or 'Crypto' ");

            asset = sc.next();
            if (!Arrays.asList("Stock", "Crypto").contains(asset)) {

              System.out.println("Please enter 'Stock or 'Crypto'");

            } else {
              validAsset = true;
            }

          }
          System.out.println(asset);
          System.out.println(pso.listPortfolioAssetsByType(asset));
          break;

        case 11:
          //Below prompts the user for the region
          System.out.println("Please enter region for summary (example US): ");
          String region = sc.next();
          System.out.println("Region entered: " + region);
          //Below absorbs the enter key-stroke
          String keyAbsorber = sc.nextLine();
          //exchange prompt for user
          System.out.println("Please enter exchange for summary(Examples SNP,DJI): ");
          String exchange = sc.nextLine();
          System.out.println("Exchange entered: " + exchange);

          String Summary = pso.getExchangeSummary(region, exchange);
    /* Remember to add the fictional asset purchases specified in the assignment to the
       portfolio*/

          System.out.println(Summary);

          break;
        case 12:
          System.out.println("Please input Start Date(dd/MM/yyyy):");
          String start = sc.next();
          System.out.println("Please input End Date(dd/MM/yyyy):");
          String end = sc.next();
          try {
            System.out.println(pso.listPortfolioPurchasesInRange(
                PortfolioSystemOperations.date2epoch(start),
                PortfolioSystemOperations.date2epoch(end)));
          } catch (java.text.ParseException e) {
            e.printStackTrace();
          }

          break;

        case 13:
          System.out.println("Please input Start Date(dd/MM/yyyy):");
          String start1 = sc.next();
          System.out.println("Please input End Date(dd/MM/yyyy):");
          String end1 = sc.next();
          try {
            System.out.println(pso.listPortfolioSalesInRange(
                PortfolioSystemOperations.date2epoch(start1),
                PortfolioSystemOperations.date2epoch(end1)));
          } catch (java.text.ParseException e) {
            e.printStackTrace();
          }

          break;

        case 14:
          ArrayList<String> marketCodes = populateMarketCodes();
          String marketCode = "";

          try {
            while (!marketCode.equals("Q")) {
              System.out.println("Trending stocks");
              System.out.println("Enter 2 Character code for market to check");
              System.out.println("Valid codes are " + marketCodes);
              System.out.println("Press Q to quit");

              Scanner scMarketCode = new Scanner(System.in);
              marketCode = scMarketCode.nextLine();

              if (!marketCode.equals("Q") && marketCodes.contains(marketCode)) {
                ArrayList<String> trendingStocks;
                ArrayList<AssetQuote> trendingStockQuotes;

                trendingStocks = (ArrayList<String>) pso.getTrendingStocks(marketCode);
                trendingStockQuotes = pso.getAssetQuoteDailyChange(trendingStocks);

                if (trendingStockQuotes.isEmpty()) {
                  System.out.println("No trending stocks found for market: " + marketCode);
                } else {
                  System.out.printf("%-10s %-35s %-15s %-15s %-4s  %n", "SYM", "Name",
                          "Current Price",
                          "Day Gain/Loss", "Day Gain/Loss %");
                  for (AssetQuote trending : trendingStockQuotes) {

                    // Set the currency format, extra decimals for values below 1000
                    String currFormat = "";
                    if (trending.getValue() <= 1000) {
                      currFormat = "$#,###.############";

                    } else {
                      currFormat = "$#,###,###,###.####";
                    }

                    // If the quote is a Index value don't display dollar symbol.
                    String assetType = trending.getType();
                    currFormat = assetType.equals("INDEX") ? "####,###,###.###" : currFormat;

                    System.out.printf("%-10s %-35s %-15s %-15s %-4s  %n", trending.getAssetSymbol(),
                            trending.getAssetFullName(),
                            customFormat(currFormat, trending.getValue()),
                            customFormat(currFormat, trending.getDaysGainOrLossValue()),
                            customFormat("##.##%", trending.getDaysGainOrLossPercent() / 100));

                  }
                }
              }


            }
          } catch (IOException e) {
            e.printStackTrace();
          } catch (ParseException e) {
            e.printStackTrace();
          } catch (NullPointerException e) {
            e.printStackTrace();
          }
          break;
        case 15:
          String searchString = "";
          try {
            while (!searchString.equals("Q")) {
              System.out.println("Summary Search");
              System.out.println(
                      "Enter comma separated value with partial symbol"
                              + " or partial name you want summary details for.");
              System.out.println("e.g. MSF,App to search for Microsoft and Apple.");
              System.out.println("Press Q to quit");

              Scanner scSearchString = new Scanner(System.in);
              searchString = scSearchString.nextLine();
              String[] elements = searchString.split(",");
              List<String> searchList = Arrays.asList(elements);
              ArrayList<String> assetSearchList = new ArrayList<String>(searchList);

              if (!searchString.equals("Q") && !assetSearchList.isEmpty()) {
                String result;
                result = pso.listPortfolioAssetsByName(assetSearchList);

                if (result.equals("")) {
                  result = "No match for search list: " + assetSearchList;
                }
                System.out.println(result);

              }


            }
          } catch (IOException e) {
            e.printStackTrace();
          } catch (ParseException e) {
            e.printStackTrace();
          } catch (NullPointerException e) {
            e.printStackTrace();
          }
          break;

        case 0:

          quit = true;

          break;

        default:

          System.out.println("Wrong choice.");

          break;

      }

      System.out.println();

    } while (!quit);

    System.out.println("Thank you for using this Portfolio Mgmt System!");


  }


  // this method calculates the total value of all assets
  public static void calculateTotal() {

    for (AssetQuote aq : AssetQuote.assetQuotes) {
      System.out.println(aq.getAssetSymbol() + "   " + aq.getStockAmount());
    }
  }

  /***
   *  Method to apply a pattern for display numeric values correctly.
   *
   * @param pattern - The pattern to format the number.
   *
   * @param value - The value that needs to be formatted.
   *
   * @return The value returned with pattern
   */
  private static String customFormat(String pattern, double value) {
    DecimalFormat myFormatter = new DecimalFormat(pattern);
    return myFormatter.format(value);
  }

  /**
   * Populate an Array List with valid Market Codes in order to validate user input.
   *
   * @return Return an Array List populated with valid Market Codes
   */
  private static ArrayList<String> populateMarketCodes() {
    ArrayList<String> marketCodes = new ArrayList<>();

    marketCodes.add("US");
    marketCodes.add("AU");
    marketCodes.add("CA");
    marketCodes.add("FR");
    marketCodes.add("DE");
    marketCodes.add("HK");
    marketCodes.add("IT");
    marketCodes.add("ES");
    marketCodes.add("GB");
    marketCodes.add("IN");
    return marketCodes;


  }

}
