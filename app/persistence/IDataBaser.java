package persistence;

/**
 * Created by naysayer on 02/10/2014.
 */
public interface IDataBaser {

    boolean write(Object o);

    Object read();

    // Should be in interface? Kind of Impl specific.
    boolean changeFormat(String format);

}
