package dscp.dragon_realm.dataContainer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ObjectDataContainer implements Serializable{
    private static final long serialVersionUID = -1328230564984272988L;

    private HashMap<DataContainerDataType<? extends Serializable>, HashMap<String, ObjectData<?>>> dataMap;

    public ObjectDataContainer(){
        this.dataMap = new HashMap<>();
    }

    public HashMap<DataContainerDataType<? extends Serializable>, HashMap<String, ObjectData<?>>> getDataMap() {
        return dataMap;
    }

    public <T extends Serializable> void saveObjectDataToContainer(DataContainerDataType<T> dataType, String key, ObjectData<T> objectData){
        createMapIfNotExist(dataType);
        this.dataMap.get(dataType).put(key, objectData);
    }

    public <T extends Serializable> void saveObjectToContainer(DataContainerDataType<T> dataType, String key, T object){
        ObjectData<T> objectData = new ObjectData<T>(dataType, object);
        saveObjectDataToContainer(dataType, key, objectData);
    }

    public <T extends Serializable> ObjectData<T> loadObjectDataFromContainer(DataContainerDataType<T> dataType, String key){
        createMapIfNotExist(dataType);
        if(!this.dataMap.get(dataType).containsKey(key)){
            return null;
        }
        ObjectData<?> objectData = this.dataMap.get(dataType).get(key);
        if(objectData.getDataType().equals(dataType)){
             T object = dataType.getType().cast(objectData.getObject());
             return new ObjectData<>(dataType, object);
        }
        else return null;
    }

    public <T extends Serializable> T loadObjectFromContainer(DataContainerDataType<T> dataType, String key){
        ObjectData<T> objectData = loadObjectDataFromContainer(dataType, key);
        if(objectData == null) return null;
        else return objectData.getObject();
    }

    private <T extends Serializable> void createMapIfNotExist(DataContainerDataType<T> dataType){
        if(!this.dataMap.containsKey(dataType)) {
            this.dataMap.put(dataType, new HashMap<>());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("-- player data --").append("\n");
        for(Map.Entry<DataContainerDataType<? extends Serializable>, HashMap<String, ObjectData<?>>> entry : dataMap.entrySet()){
            HashMap<String, ObjectData<?>> typeDataMap = entry.getValue();
            sb.append(entry.getKey().getName()).append(" (").append(entry.getValue().size()).append(") :\n");
            for(Map.Entry<String, ObjectData<?>> en : typeDataMap.entrySet()){
                sb.append(" <> ").append(en.getKey()).append(" ==> ").append(en.getValue().getObject()).append("\n");
            }
        }
        sb.append("-- -- -- -- -- --\n");
        return sb.toString();
    }
}
