package dscp.dragon_realm.utils.weightMap;

import java.util.Objects;

public class WeightMapItem<E> {
    private int weight;
    private E value;

    public WeightMapItem(E value, int weight){
        this.value = value;
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public E getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WeightMapItem)) return false;
        WeightMapItem<?> that = (WeightMapItem<?>) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
