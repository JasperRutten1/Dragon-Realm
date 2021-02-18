package dscp.dragon_realm.dragonProtect.areaProtect;

import dscp.dragon_realm.dataContainer.ObjectDataContainer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class AreaContainer implements Serializable {
    private static final long serialVersionUID = 3334332144661156187L;

    private ArrayList<ProtectedArea> areas;

    public AreaContainer(){
        this.areas = new ArrayList<>();
    }

    public ArrayList<ProtectedArea> getAreas() {
        return areas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AreaContainer)) return false;
        AreaContainer that = (AreaContainer) o;
        return Objects.equals(areas, that.areas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(areas);
    }

    @Override
    public String toString() {
        if(areas.isEmpty()) return "No protected areas";
        StringBuilder sb = new StringBuilder();
        sb.append("Protected areas: \n");
        for(ProtectedArea area : areas){
            sb.append(area.toString()).append("\n");
        }
        return sb.toString();
    }
}
