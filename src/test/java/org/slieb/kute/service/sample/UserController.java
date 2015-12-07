package org.slieb.kute.service.sample;

import org.slieb.kute.service.annotations.KuteAction;
import org.slieb.kute.service.annotations.KuteBefore;
import org.slieb.kute.service.annotations.KuteController;
import spark.Request;
import spark.Response;

import static org.slieb.kute.service.annotations.KuteAction.Method.GET;
import static org.slieb.kute.service.annotations.KuteAction.Method.POST;


@KuteController
public class UserController {

    String currentUser;

    @KuteBefore(only = {"userStatus"})
    public void checkUserIsLoggedIn(Request request,
                                    Response response) {
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
    public void checkUserIsNotAlreadyLoggedIn(Request request, Response response) {
        if(currentUser != null) {
            response.body("already logged in!");
            response.status(500);
        }
    }

    @KuteAction(methods = {GET}, value = "/login")
    public String getLogin(Request request,
                           Response response) {
        return new StringBuilder().append("<form method=POST action='/login'>").append(
                "<input type=text name=user />").append("<input type=submit valule=submit />").append(
                "</form>").toString();
    }

    @KuteAction(methods = {POST}, value = "/login")
    public String postLogin(Request request,
                            Response response) {
        String user = request.queryParams("user");
        if (user != null && !user.isEmpty()) {
            currentUser = user;
            return "successfully logged in " + currentUser;
        } else {
            return "failure to log in";
        }
    }

    @KuteAction(methods = {GET, POST}, value = "/currentUser")
    public String userStatus(Request request,
                             Response response) {
        return currentUser;
    }

}
