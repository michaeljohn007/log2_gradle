package PortfolioSystem;

import static java.lang.Math.abs;

import java.util.ArrayList;

public class AssetQuote {

  public static ArrayList<AssetQuote> assetQuotes = new ArrayList<AssetQuote>();
  public static double totalValue;

  /* You should implement relevant methods for the class. You can add additional attributes to
   * store additional information on each asset if you wish. Carefully consider the information that
   * you can retrieve from the finance API that you use and what information the user would like to
   * view or may find useful */
  public static double availableFunds;
  /**
   * The symbol of the asset e.g. APPL, TSLA, BARC or BTC-USD
   */
  private String assetSymbol;
  /**
   * The full name of the asset e.g. Apple, Tesla, Barclays PLC, Bitcoin USD
   */
  private String assetFullName;
  /**
   * The UNIX timestamp of the asset's quoted value. Using long instead of int to avoid the year
   * 2038 problem.
   */
  private long timeStamp;
  /**
   * The value in USD of the named asset at this point in time.
   */
  private double value;
  private double stockAmount;
  /***
   *  Value to hold type of asset - Equity,Index,Cryptocurrency
   */
  private String assetType;
  /**
   * The value in USD of the named asset when market opened.
   */
  private double openingValue;

  public AssetQuote() {

  }

  public AssetQuote(String assetSymbol, String assetFullName, long timeStamp, double value) {
    this.setAssetSymbol(assetSymbol);
    this.setAssetFullName(assetFullName);
    this.setTimeStamp(timeStamp);
    this.setValue(value);

  }
  public AssetQuote(String assetSymbol, String assetFullName, long timeStamp, double value,
      double stockAmount, String assetType) {
    this.setAssetSymbol(assetSymbol);
    this.setAssetFullName(assetFullName);
    this.setTimeStamp(timeStamp);
    this.setValue(value);
    this.setStockAmount(stockAmount);
    this.setType(assetType);
  }

  public static double getAvailableFunds() {
    return availableFunds;
  }

  public static void setAvailableFunds(double availableFunds) {
    AssetQuote.availableFunds = availableFunds;
  }

  public String getAssetSymbol() {
    return assetSymbol;
  }

  public void setAssetSymbol(String assetSymbol) {
    this.assetSymbol = assetSymbol;
  }

  public String getAssetFullName() {
    return assetFullName;
  }

  public void setAssetFullName(String assetFullName) {
    this.assetFullName = assetFullName;
  }

  public long getTimeStamp() {
    return timeStamp;
  }

  public void setTimeStamp(long timeStamp) {
    this.timeStamp = timeStamp;
  }

  public double getValue() {
    return value;
  }

  public void setValue(double value) {
    this.value = value;
  }

  public String getType() {
    return assetType;
  }

  public void setType(String assetType) {
    this.assetType = assetType;
  }

  public double getOpeningValue() {
    return openingValue;
  }

  public void setOpeningValue(double openingValue) {
    this.openingValue = openingValue;
  }

  public double getDaysGainOrLossValue() {
    return this.getValue() - this.openingValue;
  }

  public double getDaysGainOrLossPercent() {
    return (100 * (this.getValue() - this.openingValue) / abs(this.openingValue));
  }

  public double getStockAmount() {
    return stockAmount;
  }

  public void setStockAmount(double stockAmount) {
    this.stockAmount = stockAmount;
  }

}
