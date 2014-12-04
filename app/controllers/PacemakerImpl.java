package controllers;


import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import models.Activity;
import models.Location;
import models.User;
import persistence.DataLodge;
import play.db.ebean.Model;
import util.DateTimeUtils;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.*;

public class PacemakerImpl extends Controller implements PacemakerApi {

    /**
     * Manages read and write operations for objects extending BaseEntity.
     */
    private final DataLodge db;

    /**
     * A convenience index of users by email. Allows quick lookup and validation.
     * Needs to be build on load/reload, and updated when users are added/deleted.
     */
    private Map<String, User> emailIndex = new HashMap<>();

    public PacemakerImpl() {
        db = new DataLodge();
        rebuildUserIndex();
    }


    private void rebuildUserIndex() {
        emailIndex.clear();
        Collection<User> users = db.getAll(User.class);
        for (User user : users) {
            emailIndex.put(user.getEmail(), user);
        }
    }

    // User operations
    @Override
    public Result createUser(String userJson) {
        User user = new JSONDeserializer<User>().deserialize(userJson, User.class);
        return createUser(user, false);
    }

    /**
     * Convenience method for above
     */
    @Override
    public Result updateUser(String userJson) {
        User user = new JSONDeserializer<User>().deserialize(userJson, User.class);
        return createUser(user, true);
    }

    /**
     * Convenience method for above
     */
    @Override
    public Result createUser(User user, boolean update) {
        if (!update && emailIndex.containsKey(user.getEmail())) {
            return notFound("This email is already registered.");
        } else {
            user = db.edit(user);
            emailIndex.put(user.getEmail(), user);
            return ok(user.toString());
        }
    }

    @Override
    public Result getUserByEmail(String email) {
        if (emailIndex.containsKey(email)) {
            return ok(emailIndex.get(email).toString());
        } else {
            return notFound();
        }
    }

    @Override
    public Result getUser(Long id) {
        User user = db.read(User.class, id);
        if (user != null) {
            return ok(user.toString());
        } else {
            return null;
        }
    }

    @Override
    public Result getUsers() {

        return ok(new JSONSerializer().serialize(db.getAll(User.class)));
    }

    /**
     * For testing purposes only.
     *
     * @param clazz
     * @return
     */
    protected Collection<? extends Model> getAll(Class clazz) {
        return db.getAll(clazz);
    }

    @Override
    public Result deleteUser(Long id) {
        User user = db.read(User.class, id);
        if (user != null) {
            emailIndex.remove(user.getEmail());
            // First delete all children.
            for (Activity actv : user.getActivities()) {
                Activity activity = db.read(Activity.class, actv.getId());
                if (activity != null) {
                    for (Location loc : activity.getRoutes()) {
                        Location location = db.read(Location.class, loc.getId());
                        db.delete(location);
                    }
                    db.delete(activity);
                }
            }

            db.delete(user);

            return ok(user.toString());

        } else {
            return notFound();

        }
    }

    @Override
    public Result addActivity(Long userId, Activity activity) {
        User user = db.read(User.class, userId);
        if (user != null) {
            activity = db.edit(activity);
            user.addActivity(activity);
            return ok(activity.toString());
        } else {
            return notFound("User not found : " + userId);
        }
    }

    @Override
    public Result addActivity(Long userId, String type, String location, Double distance, String startTime, String duration) {
        Activity activity = new Activity();
        activity.setType(type);
        activity.setLocation(location);
        activity.setDistance(distance);
        User user = db.read(User.class, userId);
        if (user != null) {
            // TODO Next line throws an exception when passed bad arg, handle nicer?
            activity.setStartTime(DateTimeUtils.parseDateTime(startTime));
            activity.setDuration(DateTimeUtils.parseDuration(duration));
            return addActivity(userId, activity);
        } else {
            return notFound("User not found : " + userId);
        }
    }

    @Override
    public Result getActivities(Long userId) {
        Collection<Activity> activities = new ArrayList<>();
        User u = db.read(User.class, userId);
        if (u != null) {
            return ok(new JSONSerializer().serialize(u.getActivities()));
        } else {
            return notFound("User not found : " + userId);
        }
    }

    @Override
    public Result addLocation(Long activityId, Double latitude, Double longitude) {
        Location location = new Location();
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return addLocation(activityId, location);
    }

    @Override
    public Result addLocation(Long activityId, Location location) {
        Activity activity = db.read(Activity.class, activityId);
        if (activity != null) {
            location = db.edit(location);
            activity.addRoute(location);
            return ok(location.toString());
        } else {
            return notFound("Activity not found : " + activityId);
        }
    }

    @Override
    public Result getLocations(Long actvId) {
        Collection<Location> locations = new ArrayList<>();
        Activity a = db.read(Activity.class, actvId);
        if (a != null) {
            return ok(new JSONSerializer().serialize(a.getRoutes()));
        } else {
            return notFound("Activity not found : " + actvId);
        }
    }

}