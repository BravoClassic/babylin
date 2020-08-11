package babylinapp;

public class rawMaterialsClass {
    protected String rawMaterialsName;
    protected Integer rawMaterialsQuantity;
    protected Double rawMaterialsPrice;
    protected String rawMaterialsDescription;


    public rawMaterialsClass(){
        this.rawMaterialsName="";
        this.rawMaterialsQuantity=0;
        this.rawMaterialsPrice=0.0;
        this.rawMaterialsDescription="";
    }

    public Double getRawMaterialsPrice() {
        return rawMaterialsPrice;
    }

    public void setRawMaterialsPrice(Double rawMaterialsPrice) {
        this.rawMaterialsPrice = rawMaterialsPrice;
    }

    public String getRawMaterialsDescription() {
        return rawMaterialsDescription;
    }

    public void setRawMaterialsDescription(String rawMaterialsDescription) {
        this.rawMaterialsDescription = rawMaterialsDescription;
    }

    public Integer getRawMaterialsQuantity() {
        return rawMaterialsQuantity;
    }

    public void setRawMaterialsQuantity(Integer rawMaterialsQuantity) {
        this.rawMaterialsQuantity = rawMaterialsQuantity;
    }

    public String getRawMaterialsName() {
        return rawMaterialsName;
    }

    public void setRawMaterialsName(String rawMaterialsName) {
        this.rawMaterialsName = rawMaterialsName;
    }

    public rawMaterialsClass(String sn, Integer sq, Double sp, String sd){
        this.rawMaterialsName=sn;
        this.rawMaterialsQuantity=sq;
        this.rawMaterialsPrice=sp;
        this.rawMaterialsDescription=sd;
    }


}
