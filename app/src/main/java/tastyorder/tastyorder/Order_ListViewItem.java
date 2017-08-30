package tastyorder.tastyorder;

import java.io.Serializable;

public class Order_ListViewItem implements Serializable {
    private String nameStr ;
    private String sizeStr ;
    private String numberStr ;
    private String priceStr ;

    public void setName(String name) {
        nameStr = name ;
    }
    public void setSize(String size) {
        sizeStr = size ;
    }
    public void setNumber(String number) {
        numberStr = number ;
    }
    public void setPrice(String price) {
        priceStr = price ;
    }

    public String getName() {
        return this.nameStr ;
    }
    public String getSize() {
        return this.sizeStr ;
    }
    public String getNumber() { return this.numberStr ; }
    public String getPrice() {
        return this.priceStr ;
    }
}
