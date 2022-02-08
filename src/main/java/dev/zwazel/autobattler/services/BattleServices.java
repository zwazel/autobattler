package dev.zwazel.autobattler.services;

import dev.zwazel.autobattler.BattlerGen2;
import dev.zwazel.autobattler.classes.utils.User;
import dev.zwazel.autobattler.classes.utils.Vector;
import dev.zwazel.autobattler.classes.utils.database.FormationEntity;
import dev.zwazel.autobattler.classes.utils.database.repositories.FormationEntityRepository;
import dev.zwazel.autobattler.classes.utils.database.repositories.UserRepository;
import dev.zwazel.autobattler.classes.utils.json.History;
import dev.zwazel.autobattler.classes.utils.json.HistoryToJson;
import dev.zwazel.autobattler.security.jwt.JwtUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.util.Optional;

@RestController()
@RequestMapping("/api/battle")
public class BattleServices {
    private final UserRepository userRepository;
    private final FormationEntityRepository formationEntityRepository;

    public BattleServices(UserRepository userRepository, FormationEntityRepository formationEntityRepository) {
        this.userRepository = userRepository;
        this.formationEntityRepository = formationEntityRepository;
    }

    @GetMapping(path = "/getFightHistory/{formationId}")
    public Response getFightHistory(@PathVariable long formationId, HttpServletRequest request) {
        JwtUtils jwtUtils = new JwtUtils("");
        String jwt = jwtUtils.getJwtFromCookies(request);
        boolean valid = jwtUtils.validateJwtToken(jwt);
        if (valid) {
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            System.out.println("username = " + username);

            Optional<User> user1 = userRepository.findByUsername(username);
            Optional<User> user2 = userRepository.findByUsername(username);

            if (user1.isPresent() && user2.isPresent()) {
                Optional<FormationEntity> formationEntity = formationEntityRepository.findById(formationId);

                if (formationEntity.isPresent()) {
                    BattlerGen2 battler = new BattlerGen2(user1.get(), formationEntity.get(), user2.get(), formationEntity.get(), false, false, new Vector(10, 10));
                    History history = battler.getHistory();

                    if (history == null) {
                        // return error
                        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("History doesn't exist!").build();
                    } else {
                        return Response.ok().entity(HistoryToJson.toJson(history)).build();
                    }
                } else {
                    // return error
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Formation doesn't exist!").build();
                }
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("User doesn't exist!").build();
            }
        } else {
            // return error
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("JWT is invalid!").build();
        }
    }
}
