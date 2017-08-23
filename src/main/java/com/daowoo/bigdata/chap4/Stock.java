package com.daowoo.bigdata.chap4;

import java.math.BigDecimal;

/**
 * Created by apple on 23/08/2017.
 */
public class Stock {

    public String getSymbol() {
        return symbol;
    }

    public String getCompany_name() {
        return company_name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Stock(String symbol, String company_name, BigDecimal price) {
        this.symbol = symbol;
        this.company_name = company_name;
        this.price = price;
    }

    String symbol;
    String company_name;
    BigDecimal price;


}
