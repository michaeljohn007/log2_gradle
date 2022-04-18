package PortfolioSystem;

public class Asset {

  /* Todo - Design and Implement the Asset class as you see fit. Justify the design decisions that
  you make within the implementation */
  private String assetSymbol;

  private String assetFullName;

  private double assetPrice;

  public String getAssetSymbol() {
    return assetSymbol;
  }

  public String getAssetFullName() {
    return assetFullName;
  }

  public void setAssetFullName(String assetFullName) {
    this.assetFullName = assetFullName;
  }

  public double getAssetPrice() {
    return assetPrice;
  }

  public void setAssetPrice(double assetPrice) {
    this.assetPrice = assetPrice;
  }
}
