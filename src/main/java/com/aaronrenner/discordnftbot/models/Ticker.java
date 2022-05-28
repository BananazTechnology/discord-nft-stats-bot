package com.aaronrenner.discordnftbot.models;

/**
 * 
 * @author aaronrenner
 * @version 1.0.1
 */
public enum Ticker {
    ETH("eth", "Ξ"),
    WETH("weth", "Ξ"),
    BTC("btc", "₿"),
    DAI("dai", "◈"),
    SOL("sol", "◎");

    private String ticker;
    private String symbol;

    Ticker(String ticker, String symbol) {
        this.ticker = ticker;
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return ticker;
    }

    public static Ticker fromString(String ticker) {
        if (ticker != null) {
            for (Ticker unit : Ticker.values()) {
                if (ticker.equalsIgnoreCase(unit.ticker)) {
                    return unit;
                }
            }
        }
        return Ticker.valueOf(ticker);
    }
}