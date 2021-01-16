package dscp.dragon_realm.dataContainer;

import java.io.Serializable;

public class ObjectData<T> implements Serializable{
    private static final long serialVersionUID = 655489610473547016L;

    private T object;
    private DataContainerDataType<T> dataType;

    public ObjectData(DataContainerDataType<T> dataType, T object){
        this.object = object;
        this.dataType = dataType;
    }

    public T getObject() {
        return object;
    }

    public DataContainerDataType<T> getDataType() {
        return dataType;
    }
}
