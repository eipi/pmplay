package controllers;

import models.Activity;
import models.Location;
import models.User;
import play.mvc.Result;

/**
 * Created by dbdon_000 on 21/10/2014.
 */
public interface PacemakerApi {
    // User operations
    Result createUser(String userJson);

    Result createUser(User user, boolean update);

    Result getUserByEmail(String email);

    Result getUser(Long id);

    Result getUsers();

    Result deleteUser(Long id);

    Result updateUser(String userJson);

    Result addActivity(Long userId, Activity activity);

    Result addActivity(Long userId, String type, String location, Double distance, String startTime, String duration);

    Result getActivities(Long userId);

    Result addLocation(Long activityId, Double latitude, Double longitude);

    Result addLocation(Long activityId, Location location);

    Result getLocations(Long actvId);

}
