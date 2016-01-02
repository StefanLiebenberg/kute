package org.slieb.kute.service.sample;

import org.slieb.kute.service.annotations.KuteAction;
import org.slieb.kute.service.annotations.KuteBefore;
import org.slieb.kute.service.annotations.KuteController;
import spark.Request;
import spark.Response;

import static org.slieb.kute.service.annotations.KuteAction.Method.GET;
import static org.slieb.kute.service.annotations.KuteAction.Method.POST;


@KuteController
@SuppressWarnings("unused")
public class UserController {

    String currentUser;

    @KuteBefore(only = {"userStatus"})
    public void checkUserIsLoggedIn(Response response) {
        if (currentUser == null) {
            response.redirect("/login");
        }
    }

    @KuteAction(methods = {GET}, value = "/logout")
    public String logOut() {
        currentUser = null;
        return "user has been logged out.";
    }

    @KuteBefore(only = {"getLogin", "postLogin"})
    public void checkUserIsNotAlreadyLoggedIn(Response response) {
        if (currentUser != null) {
            response.body("already logged in!");
            response.status(500);
        }
    }

    @KuteAction(methods = {GET}, value = "/login")
    public String getLogin() {
        return "<form method=POST action='/login'>" +
                "<input type=text name=user />" +
                "<input type=submit value=submit />" +
                "</form>";
    }

    @KuteAction(methods = {POST}, value = "/login")
    public String postLogin(Request request) {
        String user = request.queryParams("user");
        if (user != null && !user.isEmpty()) {
            currentUser = user;
            return "successfully logged in " + currentUser;
        } else {
            return "failure to log in";
        }
    }

    @KuteAction(methods = {GET, POST}, value = "/currentUser")
    public String userStatus() {
        return currentUser;
    }

}
