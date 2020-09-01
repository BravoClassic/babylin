package babylinapp;

public class productClass {
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
    protected Integer productID;
    protected String productName;
    protected Integer productQuantity;
    protected Double productPrice;
    protected String productDesc;


    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public productClass(){
        this.productID=0;
        this.productName="";
        this.productQuantity=0;
        this.productPrice=0.00;
        this.productDesc="";

    }
    public productClass(Integer productID, String productName, Integer productQuantity, Double productPrice, String productDesc) {
        super();
        this.productID= productID;
        this.productName = productName;
        this.productQuantity=productQuantity;
        this.productPrice=productPrice;
        this.productDesc=productDesc;
    }

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }
}
