package babylinapp;

public class stocksClass {
    protected String stockName;
    protected Integer stockQuantity;
    protected Double stockPrice;
    protected String stockDescription;


    public stocksClass(){
        this.stockName="";
        this.stockQuantity=0;
        this.stockPrice=0.0;
        this.stockDescription="";
    }

    public stocksClass(String sn, Integer sq, Double sp, String sd){
        this.stockName=sn;
        this.stockQuantity=sq;
        this.stockPrice=sp;
        this.stockDescription=sd;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Double getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(Double stockPrice) {
        this.stockPrice = stockPrice;
    }

    public String getStockDescription() {
        return stockDescription;
    }

    public void setStockDescription(String stockDescription) {
        this.stockDescription = stockDescription;
    }
}
