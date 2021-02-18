package dscp.dragon_realm.NPCs.merchants;

import dscp.dragon_realm.container.Container;
import dscp.dragon_realm.menus.merchants.CosmeticsMerchantMenu;

public enum MerchantType {
    COSMETICS(1, "Cosmetics", new CosmeticsMerchantMenu());

    private int id;
    private String name;
    private Container menu;

    MerchantType(int id, String name, Container menu){
        this.id = id;
        this.name = name;
        this.menu = menu;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Container getMenu() {
        return menu;
    }

    public static MerchantType getType(int id){
        for(MerchantType type : values()){
            if(type.getId() == id) return type;
        }
        return null;
    }

    public static MerchantType getType(String name){
        for(MerchantType type : values()){
            if(type.getName().equals(name)) return type;
        }
        return null;
    }
}
