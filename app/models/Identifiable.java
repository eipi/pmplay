package models;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by naysayer on 03/12/2014.
 */

public interface Identifiable {

    public Long getId();

    public void setId(Long id);

}
