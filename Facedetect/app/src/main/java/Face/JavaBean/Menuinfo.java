package Face.JavaBean;

import java.io.Serializable;

/**
 * Created by hyc on 2016/3/30.
 */
public class Menuinfo implements Serializable {
    private String menu_name;
    private int photoId;
    public Menuinfo(String name, int photoId) {
        this.menu_name=name;
        this.photoId=photoId;
    }
    public void setMenuName(String name) {
        this.menu_name = name;
    }
    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }
    public String getMenu_name() {
        return menu_name;
    }
    public int getPhotoId(){
        return photoId;
    }

}
