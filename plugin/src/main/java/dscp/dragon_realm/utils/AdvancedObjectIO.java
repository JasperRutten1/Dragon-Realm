package dscp.dragon_realm.utils;

import lombok.NonNull;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdvancedObjectIO<T> {
    private File file;
    private Class<T> type;

    /**
     * constructor
     * @param file the file where a object will be saved/loaded from
     */
    public AdvancedObjectIO(@NonNull File file){
        if(file == null) throw new IllegalArgumentException("file can't be null");

        this.file = file;
    }

    public T loadObject() throws IOException{
        if(file.isDirectory()) throw new IOException("Can not load object from dir");

        if(file.exists()){
            try{
                FileInputStream fileIn = new FileInputStream(file);
                ObjectInputStream objectIn = new ObjectInputStream(fileIn);
                Object object = objectIn.readObject();
                objectIn.close();

                try{
                    return (T) object;
                }
                catch(ClassCastException e){
                    throw new IOException("object is not instance of Type");
                }
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new IOException("file not found");
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new IOException("class not found");
            }
        }
        else return null;
    }

    public List<T> loadObjectsFromDirAsList() throws IOException{
        List<T> objects = new ArrayList<>();
        if(!file.exists()) return objects;
        if(!file.isDirectory()) throw new IOException("file must be dir");

        FileFilter filter = file -> file.getName().endsWith(".dat");
        File[] files = file.listFiles(filter);

        if(files == null) return objects;

        for(File objectFile : files){
            try{
                FileInputStream fileIn = new FileInputStream(file);
                ObjectInputStream objectIn = new ObjectInputStream(fileIn);
                Object object = objectIn.readObject();
                objectIn.close();

                if(type.isInstance(object)){
                    objects.add(type.cast(object));
                }
            }
            catch(IOException | ClassNotFoundException e){
                System.out.println("failed to load object " + objectFile.getName() + " at " + objectFile.getPath());
            }
        }

        return objects;
    }

    public HashMap<String, T> loadObjectsFromDirAsHash() throws IOException{
        HashMap<String, T> objects = new HashMap<>();
        if(!file.exists()) return objects;
        if(!file.isDirectory()) throw new IOException("file must be dir");

        FileFilter filter = file -> file.getName().endsWith(".dat");
        File[] files = file.listFiles(filter);

        if(files == null) return objects;

        for(File objectFile : files){
            try{
                FileInputStream fileIn = new FileInputStream(file);
                ObjectInputStream objectIn = new ObjectInputStream(fileIn);
                Object object = objectIn.readObject();
                objectIn.close();

                if(type.isInstance(object)){
                    objects.put(objectFile.getName().replaceAll(".dat", ""), type.cast(object));
                }
            }
            catch(IOException | ClassNotFoundException e){
                System.out.println("failed to load object " + objectFile.getName() + " at " + objectFile.getPath());
            }
        }

        return  objects;
    }

    public void saveObject(T object) throws IOException{
        if(!(object instanceof Serializable)) throw new IOException("Object is not serializable");

        if(!file.exists()){
            file.createNewFile();
            System.out.println("created new file at " + file.getPath());
        }

        FileOutputStream fileOut = new FileOutputStream(file);
        ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);

        objectOut.writeObject(object);
        objectOut.close();
        System.out.println("successfully wrote object to " + file.getPath());
    }
}
