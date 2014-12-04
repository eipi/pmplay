package controllers;

import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by naysayer on 03/12/2014.
 */
public class Ops extends Controller {

    private static final PacemakerApi API = new PacemakerImpl();

    public static void setJsonHeader() {
        response().setContentType("application/json");
    }

    public static Result getUsers() {
        setJsonHeader();
        return API.getUsers();
    }

    public static Result getUser(Long id) {
        setJsonHeader();
        return API.getUser(id);
    }

    public static Result createUser() {
        setJsonHeader();
        return API.createUser(request().body().asJson().toString());
    }

    public static Result deleteUser(Long id) {
        return API.deleteUser(id);
    }

    public static Result updateUser() {
        return API.updateUser(request().body().asJson().toString());
    }

}
