package tastyorder.tastyorder;

import java.io.Serializable;

public class Main_ListViewItem implements Serializable {
    private String ImageUrlStr ;
    private String titleStr ;
    private String descStr ;

    public void setImageUrl(String imageurl) {
        ImageUrlStr = imageurl ;
    }
    public void setTitle(String title) {
        titleStr = title ;
    }
    public void setDesc(String desc) {
        descStr = desc ;
    }

    public String getImageUrl() {
        return this.ImageUrlStr ;
    }
    public String getTitle() {
        return this.titleStr ;
    }
    public String getDesc() {
        return this.descStr ;
    }
}
