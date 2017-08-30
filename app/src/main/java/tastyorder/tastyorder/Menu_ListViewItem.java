package tastyorder.tastyorder;

public class Menu_ListViewItem {
    private String nameStr ;
    private String priceStr ;

    public void setName(String name) {
        nameStr = name ;
    }
    public void setPrice(String price) {
        priceStr = price ;
    }

    public String getName() {
        return this.nameStr ;
    }
    public String getPrice() {
        return this.priceStr ;
    }
}
