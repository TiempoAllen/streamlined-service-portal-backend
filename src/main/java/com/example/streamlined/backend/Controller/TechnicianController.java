package com.example.streamlined.backend.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.streamlined.backend.Entity.RequestEntity;
import com.example.streamlined.backend.Entity.TechnicianEntity;
import com.example.streamlined.backend.Service.TechnicianService;

@RestController
@RequestMapping("/technician")
@CrossOrigin(origins = "http://localhost:3000")
public class TechnicianController {
	@Autowired
	TechnicianService tserv;


	@PostMapping("/addTechnician")
    public TechnicianEntity addTechnician(@RequestBody TechnicianEntity technician) {
        return tserv.addTechnician(technician);
    }

    @GetMapping("/getAllTechnician")
    public List<TechnicianEntity> getAllTechnicians() {
        return tserv.getAllTechnicians();
    }

    @GetMapping("/getTechnician/{tid}")
    public Optional<TechnicianEntity> getTechnicianById(@PathVariable Long tid) {
        return tserv.getTechnicianById(tid);
    }

    @PutMapping("/updateTechnician")
    public TechnicianEntity updateTechnician(@RequestParam Long tid, @RequestBody TechnicianEntity newTechnicianDetails) {
        return tserv.updateTechnician(tid, newTechnicianDetails);
    }

    @PutMapping("/assignToRequest")
    public TechnicianEntity assignTechnicianToRequest(
            @RequestParam Long tid,
            @RequestParam Long request_id,
            @RequestParam String request_purpose) {
        return tserv.assignTechnicianToRequest(tid, request_id, request_purpose);
    }

    @DeleteMapping("/deleteTechnician/{tid}")
    public String deleteTechnician(@PathVariable Long tid) {
        return tserv.deleteTechnician(tid);
    }
}
