package com.example.backend.controllers;

import com.example.backend.repositories.UserRepository;
import com.example.backend.repositories.MuseumRepository;
import com.example.backend.models.users;
import com.example.backend.tools.Utils;
import com.example.backend.models.museums;
import com.example.backend.tools.DataValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    MuseumRepository museumRepository;

    @GetMapping("/users")
    public Page<users> getAllUsers(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        return userRepository.findAll(PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, "login")));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<users> updateUser(@PathVariable(value = "id") Long userId,
                                     @RequestBody users userDetails)
            throws DataValidationException
    {
        try {
            users user = userRepository.findById(userId)
                    .orElseThrow(() -> new DataValidationException(" Пользователь с таким индексом не найден"));
            user.email = userDetails.email;
            String np = userDetails.np;
            if (np != null  && !np.isEmpty()) {
                byte[] b = new byte[32];
                new Random().nextBytes(b);
                String salt = new String(Hex.encode(b));
                user.password = Utils.ComputeHash(np, salt);
                user.salt = salt;
            }
            userRepository.save(user);
            return ResponseEntity.ok(user);
        }
        catch (Exception ex) {
            if (ex.getMessage().contains("users.email_UNIQUE"))
                throw new DataValidationException("Пользователь с такой почтой уже есть в базе");
            else
                throw new DataValidationException("Неизвестная ошибка");
        }
    }

    @PostMapping("/users")
    public ResponseEntity<Object> createUser(@RequestBody users user)
            throws Exception {
        try {
            users nc = userRepository.save(user);
            System.out.println(nc.login);
            return new ResponseEntity<Object>(nc, HttpStatus.OK);
        } catch (Exception ex) {
            String error;
            if (ex.getMessage().contains("users.name_UNIQUE"))
                error = "useralreadyexists";
            else
                error = "undefinederror";
            Map<String, String>
                    map = new HashMap<>();
            map.put("error", error);
            return new ResponseEntity<Object>(map, HttpStatus.OK);
        }
    }

    @PostMapping("/users/{id}/addmuseums")
    public ResponseEntity<Object> addMuseums(@PathVariable(value = "id") Long userId,
                                             @RequestBody Set<museums> museums) {
        Optional<users> uu = userRepository.findById(userId);
        int cnt = 0;
        if (uu.isPresent()) {
            users u = uu.get();
            for (museums m : museums) {
                Optional<museums>
                        mm = museumRepository.findById(m.id);
                mm.ifPresent(u::addMuseum);
            }
            userRepository.save(u);
        }
        Map<String, String> response = new HashMap<>();
        response.put("count", String.valueOf(cnt));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/users/{id}/removemuseums")
    public ResponseEntity<Object> removeMuseums(@PathVariable(value = "id") Long userId,
                                                @RequestBody Set<museums> museums) {
        Optional<users> uu = userRepository.findById(userId);
        int cnt = 0;
        if (uu.isPresent()) {
            users u = uu.get();
            for (museums m : u.museums) {
                u.removeMuseum(m);
                cnt++;
            }
            userRepository.save(u);
        }
        Map<String, String> response = new HashMap<>();
        response.put("count", String.valueOf(cnt));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<users> getUser(@PathVariable(value = "id") Long userId)
            throws DataValidationException
    {
        users artist = userRepository.findById(userId)
                .orElseThrow(()-> new DataValidationException("Пользователь с таким индексом не найден"));
        return ResponseEntity.ok(artist);
    }

}