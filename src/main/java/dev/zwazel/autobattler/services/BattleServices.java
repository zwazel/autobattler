package dev.zwazel.autobattler.services;

import dev.zwazel.autobattler.BattlerGen2;
import dev.zwazel.autobattler.classes.utils.Vector;
import dev.zwazel.autobattler.classes.utils.json.History;
import dev.zwazel.autobattler.classes.utils.json.HistoryToJson;
import dev.zwazel.autobattler.security.jwt.JwtUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.security.Principal;

@RestController()
@RequestMapping("/api/battle")
public class BattleServices {
    @GetMapping(path = "/getFightHistory")
    public Response getFightHistory(Authentication authentication, Principal principal, HttpServletRequest request) {

        BattlerGen2 battler = new BattlerGen2(false, false, new Vector(10, 10));
        History history = battler.getHistory();
        if (history == null) {
            // return error
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("History doesn't exist!").build();
        }

        JwtUtils jwtUtils = new JwtUtils("");
        String jwt = jwtUtils.getJwtFromCookies(request);
        boolean valid = jwtUtils.validateJwtToken(jwt);
        System.out.println("valid = " + valid);
        System.out.println("jwt = " + jwt);
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        System.out.println("username = " + username);
        return Response.ok().entity(HistoryToJson.toJson(history)).build();
    }
}
