package com.example.liquorstore.controller;

import com.example.liquorstore.dto.LiquorDto;
import com.example.liquorstore.entity.Liquor;
import com.example.liquorstore.service.LiquorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("public/api/v1")
public class LiquorController {
    @Autowired
    public LiquorService liquorService;

    @PostMapping("/addLiquor")
    public ResponseEntity<?> saveliquor(@RequestBody LiquorDto liquorDto) {
        try {
            Liquor liquor = liquorService.addLiquor(liquorDto);
            if (liquor != null) {
                return new ResponseEntity(liquor, HttpStatus.CREATED);
            } else
                return new ResponseEntity("An error occured while adding liquor", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/deleteLiquor/{id}")
    public ResponseEntity<?> deleteLiquor(@PathVariable long id) {
        liquorService.deleteLiquorById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/allLiquors")
    public ResponseEntity<?> getAllLiquors() {
        try {
            return new ResponseEntity<>(liquorService.getAllLiquors(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while fetching liquors", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/liquor/{id}")
    public ResponseEntity<?> getLiquorById(@PathVariable long id) {
        try {
            Liquor liquor = liquorService.getLiquorById(id);
            if (liquor != null) {
                return new ResponseEntity<>(liquor, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Liquor not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while fetching liquor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateLiquor/{id}")
    public ResponseEntity<?> updateLiquor(@PathVariable long id, @RequestBody LiquorDto liquorDto) {
        try {
            Liquor updatedLiquor = liquorService.updateLiquor(id, liquorDto);
            if (updatedLiquor != null) {
                return new ResponseEntity<>(updatedLiquor, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Liquor not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while updating liquor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
