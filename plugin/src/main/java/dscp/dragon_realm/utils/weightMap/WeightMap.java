package dscp.dragon_realm.utils.weightMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeightMap<E> {
    ArrayList<WeightMapItem<E>> items;

    public WeightMap(){
        this.items = new ArrayList<>();
    }

    public void addItem(E value, int weight){
        this.items.add(new WeightMapItem<>(value, weight));
    }

    public void removeItem(E value){
        this.items.remove(new WeightMapItem<>(value, 0));
    }

    public List<E> getValuesWithWeight(int weight){
        List<E> values = new ArrayList<>();
        for(WeightMapItem<E> item : items){
            if(item.getWeight() == weight) values.add(item.getValue());
        }
        return values;
    }

    public E getRandom(){
        int totalWeight = getTotalWeight();
        int random = new Random().nextInt(totalWeight);
        for(int i = 0, j = 0 ; i < totalWeight && j < items.size() ; i += items.get(j).getWeight() , j++){
            WeightMapItem<E> item = items.get(j);
            if(random >= i && random < i + item.getWeight()) return item.getValue();
        }
        return null;
    }

    private int getTotalWeight(){
        int weight = 0;
        for(WeightMapItem<E> item : items){
            weight += item.getWeight();
        }
        return weight;
    }
}
