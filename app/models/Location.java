package models;

import com.google.gson.Gson;
import flexjson.JSONSerializer;
import lombok.Getter;
import lombok.Setter;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;

@SuppressWarnings("serial")
@Entity
@Table(name="Locations")
public class Location extends Model implements Identifiable  {

    @Id
    @GeneratedValue
    @Getter
    @Setter
    private Long id;

    @Override
    public String toString() {
        return new JSONSerializer().serialize(this);
    }

    @Getter
    @Setter
    protected Double latitude;
    @Getter
    @Setter
    protected Double longitude;

    @Override
    public int hashCode() {
        final int prime = 101;

        int result = 1;

        Field[] fields = this.getClass().getDeclaredFields();
        for (Field f : fields) {
            try {
                Object value = f.get(this);
                result = prime * result + ((value == null) ? 0 : value.hashCode());
            } catch (Throwable t) {
                // should never happen
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            Field[] fields = this.getClass().getDeclaredFields();
            try {
                for (Field f : fields) {
                    Object thisValue = f.get(this);
                    Object thatValue = f.get(obj);

                    if (thisValue == thatValue || (thisValue != null && thisValue.equals(thatValue))) {
                        continue;
                    } else {
                        return false;
                    }
                }
            } catch (IllegalAccessException ex) {
                return false;
            }
        }
        return true;
    }


}

