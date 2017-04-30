package com.liu.easyoffice.Utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hui on 2016/10/23.
 */

public class FileStore {
    private Context context;
    private String filePath;

    public FileStore(Context context, String fileName) {
        this.context = context;
        this.filePath = context.getCacheDir().getPath() + File.pathSeparator +fileName;
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean saveObject(Serializable ser) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(filePath);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(ser);
            oos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Serializable readObject() {
        File file = new File(filePath);
        if (!file.exists())
            return null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(filePath);
            ois = new ObjectInputStream(fis);
            return (Serializable) ois.readObject();
        } catch (Exception e) {
            Log.e("exception", "ex: " + e);
            e.printStackTrace();
            //反序列话失败删除文件
            if (e instanceof InvalidClassException) {
                File date = context.getFileStreamPath(filePath);
                date.delete();
            }
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public <T> boolean saveList(List<T> list) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        T[] array = (T[]) list.toArray();
        try {
            fos = new FileOutputStream(filePath);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(array);
            oos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("exception", "saveList: " + e);
            return false;
        } finally {
            try {
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public <E> List<E> readList() {
        File file = new File(filePath);
        if (!file.exists())
            return null;
        E[] object;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(filePath);
            ois = new ObjectInputStream(fis);
            object = (E[]) ois.readObject();
            return Arrays.asList(object);
        } catch (Exception e) {
            e.printStackTrace();
            //反序列话失败删除文件
            if (e instanceof InvalidClassException) {
                File date = context.getFileStreamPath(filePath);
                date.delete();
            }
        } finally {
            try {
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
