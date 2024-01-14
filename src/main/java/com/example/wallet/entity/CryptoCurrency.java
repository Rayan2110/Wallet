package com.example.wallet.entity;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class CryptoCurrency {
    private String id;
    private String symbol;
    private String name;
    private String image;

    @SerializedName("current_price")
    private BigDecimal currentPrice;

    @SerializedName("market_cap")
    private long marketCap;

    @SerializedName("market_cap_rank")
    private int marketCapRank;

    @SerializedName("fully_diluted_valuation")
    private long fullyDilutedValuation;

    @SerializedName("total_volume")
    private long totalVolume;

    @SerializedName("high_24h")
    private double high24h;

    @SerializedName("low_24h")
    private double low24h;

    @SerializedName("price_change_24h")
    private double priceChange24h;

    @SerializedName("price_change_percentage_24h")
    private double priceChangePercentage24h;

    @SerializedName("market_cap_change_24h")
    private double marketCapChange24h;

    @SerializedName("market_cap_change_percentage_24h")
    private double marketCapChangePercentage24h;

    @SerializedName("circulating_supply")
    private double circulatingSupply;

    @SerializedName("total_supply")
    private double totalSupply;

    @SerializedName("max_supply")
    private double maxSupply;

    private double ath;

    @SerializedName("ath_change_percentage")
    private double athChangePercentage;

    @SerializedName("ath_date")
    private String athDate;

    private double atl;

    @SerializedName("atl_change_percentage")
    private double atlChangePercentage;

    @SerializedName("atl_date")
    private String atlDate;

    private Object roi;

    @SerializedName("last_updated")
    private String lastUpdated;

    @SerializedName("sparkline_in_7d")
    private Sparkline sparklineIn7d;

    @SerializedName("price_change_percentage_1h_in_currency")
    private double priceChangePercentage1hInCurrency;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public long getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(long marketCap) {
        this.marketCap = marketCap;
    }

    public int getMarketCapRank() {
        return marketCapRank;
    }

    public void setMarketCapRank(int marketCapRank) {
        this.marketCapRank = marketCapRank;
    }

    public long getFullyDilutedValuation() {
        return fullyDilutedValuation;
    }

    public void setFullyDilutedValuation(long fullyDilutedValuation) {
        this.fullyDilutedValuation = fullyDilutedValuation;
    }

    public long getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(long totalVolume) {
        this.totalVolume = totalVolume;
    }

    public double getHigh24h() {
        return high24h;
    }

    public void setHigh24h(double high24h) {
        this.high24h = high24h;
    }

    public double getLow24h() {
        return low24h;
    }

    public void setLow24h(double low24h) {
        this.low24h = low24h;
    }

    public double getPriceChange24h() {
        return priceChange24h;
    }

    public void setPriceChange24h(double priceChange24h) {
        this.priceChange24h = priceChange24h;
    }

    public double getPriceChangePercentage24h() {
        return priceChangePercentage24h;
    }

    public void setPriceChangePercentage24h(double priceChangePercentage24h) {
        this.priceChangePercentage24h = priceChangePercentage24h;
    }

    public double getMarketCapChange24h() {
        return marketCapChange24h;
    }

    public void setMarketCapChange24h(double marketCapChange24h) {
        this.marketCapChange24h = marketCapChange24h;
    }

    public double getMarketCapChangePercentage24h() {
        return marketCapChangePercentage24h;
    }

    public void setMarketCapChangePercentage24h(double marketCapChangePercentage24h) {
        this.marketCapChangePercentage24h = marketCapChangePercentage24h;
    }

    public double getCirculatingSupply() {
        return circulatingSupply;
    }

    public void setCirculatingSupply(double circulatingSupply) {
        this.circulatingSupply = circulatingSupply;
    }

    public double getTotalSupply() {
        return totalSupply;
    }

    public void setTotalSupply(double totalSupply) {
        this.totalSupply = totalSupply;
    }

    public double getMaxSupply() {
        return maxSupply;
    }

    public void setMaxSupply(double maxSupply) {
        this.maxSupply = maxSupply;
    }

    public double getAth() {
        return ath;
    }

    public void setAth(double ath) {
        this.ath = ath;
    }

    public double getAthChangePercentage() {
        return athChangePercentage;
    }

    public void setAthChangePercentage(double athChangePercentage) {
        this.athChangePercentage = athChangePercentage;
    }

    public String getAthDate() {
        return athDate;
    }

    public void setAthDate(String athDate) {
        this.athDate = athDate;
    }

    public double getAtl() {
        return atl;
    }

    public void setAtl(double atl) {
        this.atl = atl;
    }

    public double getAtlChangePercentage() {
        return atlChangePercentage;
    }

    public void setAtlChangePercentage(double atlChangePercentage) {
        this.atlChangePercentage = atlChangePercentage;
    }

    public String getAtlDate() {
        return atlDate;
    }

    public void setAtlDate(String atlDate) {
        this.atlDate = atlDate;
    }

    public Object getRoi() {
        return roi;
    }

    public void setRoi(Object roi) {
        this.roi = roi;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Sparkline getSparklineIn7d() {
        return sparklineIn7d;
    }

    public void setSparklineIn7d(Sparkline sparklineIn7d) {
        this.sparklineIn7d = sparklineIn7d;
    }

    public double getPriceChangePercentage1hInCurrency() {
        return priceChangePercentage1hInCurrency;
    }

    public void setPriceChangePercentage1hInCurrency(double priceChangePercentage1hInCurrency) {
        this.priceChangePercentage1hInCurrency = priceChangePercentage1hInCurrency;
    }

// Constructeurs, getters, setters
}
