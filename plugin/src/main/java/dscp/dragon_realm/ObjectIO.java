package dscp.dragon_realm;

import java.io.*;

public class ObjectIO {
    /**
     * write a object to a file.
     * @param filePath the path of the file where the object will be saved, will create new file if no file found.
     * @param object the object that will be saved.
     */
    public static void writeObjectToFile(String filePath, Object object){
        writeObjectToFile(new File(filePath), object);
    }

    /**
     * write a object to a file.
     * @param file the file where the object will be saved, will create new file if no file found.
     * @param object the object that will be saved.
     */
    public static void writeObjectToFile(File file, Object object){
        if(!(object instanceof Serializable)) throw new IllegalArgumentException("object must be serializable");

        try{
            if(!file.exists()){
                file.createNewFile();
                System.out.println("error in creating new file at " + file.getPath());
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        try{
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);

            objectOut.writeObject(object);
            objectOut.close();
            System.out.println("successfully wrote object to " + file.getPath());
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
            System.out.println("could not find file at " + file.getPath());
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("exception in writing object");
        }
    }

    public static Object loadObjectFromFile(File file){
        if(!file.exists()) throw new IllegalArgumentException("file does not exist");
        try{
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            Object object = objectIn.readObject();
            objectIn.close();
            return object;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("exception in creating fileInput");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("exception in loading in object");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("could not find the class type of this object");
        }
        return null;
    }

    public static Object loadObjectFromFile(String path){
        return loadObjectFromFile(new File(path));
    }
}
