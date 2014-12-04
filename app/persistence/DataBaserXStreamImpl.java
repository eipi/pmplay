package persistence;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.AbstractDriver;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;
import models.Activity;
import models.Location;
import models.User;

import java.io.*;

/**
 * Class to save an Object to and from disk/db/etc.
 * <p/>
 * Created by naysayer on 02/10/2014.
 */
public class DataBaserXStreamImpl implements IDataBaser {

    public static boolean DELETE_ON_EXIT = Boolean.FALSE;
    private String connString = "default.lodge";
    private XStream xstream = null;
    // Defaulting to JSON.
    private Format fmt = Format.Json;

    public DataBaserXStreamImpl() {
        initializeXstream();
        setConnString();
    }

    public DataBaserXStreamImpl(final String connStringIn) {
        connString = connStringIn;
        initializeXstream();
        setConnString();
    }

    private static void safelyClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable t) {
                //  ignore
            }
        }
    }

    private void setConnString() {
        int sep = connString.lastIndexOf('.');
        connString = (sep != -1 ? connString.substring(0, sep) : connString) + (fmt == Format.Json ? ".json" : ".xml");
    }

    private void initializeXstream() {
        xstream = new XStream(fmt.driver);
        xstream.alias("user", User.class);
        xstream.alias("activity", Activity.class);
        xstream.alias("location", Location.class);

    }

    @Override
    public boolean changeFormat(String format) {

        // TODO a better way?
        if ("xml".equalsIgnoreCase(format)) {
            fmt = Format.Xml;
        } else if ("json".equalsIgnoreCase(format)) {
            fmt = Format.Json;
        }  else {
            return false;
        }
        new File(connString).delete();
        initializeXstream();
        setConnString();
        return true;
    }

    @Override
    public Object read() {

        File file = new File(connString);
        Reader reader = null;
        ObjectInputStream is = null;
        if (!file.isFile()) {
            // Default JSon file not found, try xml.
            changeFormat("xml");
            file = new File(connString);
        }
        if (file.isFile()) {
            try {
                reader = new FileReader(connString);
                is = xstream.createObjectInputStream(reader);
                Object obj = is.readObject();
                if (obj != null) {
                    return obj;
                }
            } catch (Exception ex) {
            } finally {
                safelyClose(is);
                safelyClose(reader);
            }
        }

        return null;
    }

    @Override
    public boolean write(Object workingMemory) {
        Writer writer = null;
        ObjectOutputStream outStream = null;

        try {
            File file = new File(connString);
            if (DELETE_ON_EXIT) {
                file.deleteOnExit();
            }
            writer = new FileWriter(file, false);
            outStream = xstream.createObjectOutputStream(writer);
            outStream.writeObject(workingMemory);
            return true;
        } catch (Throwable t) {
            return false;
        } finally {
            safelyClose(outStream);
            safelyClose(writer);

        }
    }

    private enum Format {
        Json(new JettisonMappedXmlDriver()), Xml(new DomDriver());
        final AbstractDriver driver;

        Format(AbstractDriver drv) {
            this.driver = drv;
        }
    }

}
