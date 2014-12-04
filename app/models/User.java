package models;

import com.google.gson.Gson;
import flexjson.JSONSerializer;
import lombok.Getter;
import lombok.Setter;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("serial")
@Entity
@Table(name="Users")
public class User extends Model implements Identifiable  {

    /**
     * Classes extending BaseEntity have a unique Long id for PK.
     */
    @Id
    @GeneratedValue
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    protected String firstName;
    @Getter
    @Setter
    protected String lastName;
    @Getter
    @Setter
    protected String email;
    @Getter
    @Setter
    protected String password;
    @Getter
    @OneToMany(cascade = CascadeType.ALL)
    protected Collection<Activity> activities;

    @Override
    public String toString() {
        return new JSONSerializer().serialize(this);
    }


    /**
     * Default constructor .
     */
    public User() {
        activities = new ArrayList<>();
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static User findByEmail(String email)
    {
        return  User.find.where().eq("email", email).findUnique();
    }

    public static User findById(Long id)
    {
        return find.where().eq("id", id).findUnique();
    }

    public static List<User> findAll()
    {
        return find.all();
    }

    public static void deleteAll()
    {
        for (User user: User.findAll())
        {
            user.delete();
        }
    }

    public static Model.Finder<String, User> find = new Model.Finder<String, User>(String.class, User.class);

}