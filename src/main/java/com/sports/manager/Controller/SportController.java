package com.sports.manager.Controller;

import com.sports.manager.Dto.SportDto;
import com.sports.manager.Entity.Admin;
import com.sports.manager.Entity.Sport;
import com.sports.manager.Service.SportService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sports")
public class SportController {

    public SportController(SportService sportService) {
        this.sportService = sportService;
    }

    private final SportService sportService;



    @PostMapping("/create")
    public ResponseEntity<?> createSport(@RequestBody SportDto sportDto, HttpServletRequest request){

        Admin admin = (Admin) request.getAttribute("user");

        Sport savedSport = sportService.createSport(sportDto, admin);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedSport);

    }


    @GetMapping("/")
    public List<Sport> getAllSports(){
        return sportService.getAllSports();
    }

    @GetMapping("/admin")
    public List<Sport> getAllSportsByAdmin(HttpServletRequest request){
        Admin admin = (Admin) request.getAttribute("user");

        int adminId = admin.getId();

        return sportService.getAllSportsByAdmin(adminId);
    }

    @DeleteMapping("/{sportId}")
    public ResponseEntity<?> deleteSport(@PathVariable int sportId, HttpServletRequest request) {
        Admin admin = (Admin) request.getAttribute("user"); // user injected from JWT filter
        try {
            sportService.deleteSport(sportId, admin);
            return ResponseEntity.ok("{\"message\": \"Sport deleted successfully.\"}");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error deleting sport: " + e.getMessage() + "\"}");
        }
    }


}

//admin : admin route
//sport model changed - send teamsize in request