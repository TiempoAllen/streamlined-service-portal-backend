package com.example.streamlined.backend.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.streamlined.backend.Entity.PersonnelScheduleEntity;
import com.example.streamlined.backend.Entity.TechnicianEntity;
import com.example.streamlined.backend.Repository.PersonnelSchedule;
import com.example.streamlined.backend.Repository.RequestRepository;
import com.example.streamlined.backend.Repository.TechnicianRepository;

@Service
public class TechnicianService {

    @Autowired
    TechnicianRepository trepo;

    @Autowired
    RequestRepository rrepo;

    @Autowired
    PersonnelSchedule psrepo;

    public List<PersonnelScheduleEntity> getTechnicianScheduleById(Long techId) {
        if (!trepo.existsById(techId)) {
            throw new NoSuchElementException("Technician with ID " + techId + " does not exist.");
        }
        return psrepo.findSchedulesByTechnicianId(techId);
    }

    public TechnicianEntity addTechnician(TechnicianEntity technician) {
        if (technician.getTech_department() == null) {
            technician.setTech_department("Not Specified");
        }
        return trepo.save(technician);
    }

    public List<TechnicianEntity> getAllTechnicians() {
        return trepo.findAll();
    }

    public Optional<TechnicianEntity> getTechnicianById(Long tid) {
        return trepo.findById(tid);
    }

    public TechnicianEntity updateTechnician(Long tid, TechnicianEntity newTechnicianDetails) {
        TechnicianEntity technician = trepo.findById(tid).orElseThrow(()
                -> new NoSuchElementException("Technician " + tid + " does not exist!")
        );

        technician.setTech_name(newTechnicianDetails.getTech_name());
        technician.setTech_phone(newTechnicianDetails.getTech_phone());
        technician.setTech_gender(newTechnicianDetails.getTech_gender());
        technician.setTech_status(newTechnicianDetails.getTech_status());
        technician.setTech_classification(newTechnicianDetails.getTech_classification());

        return trepo.save(technician);
    }

    public TechnicianEntity assignTechnicianToRequest(Long tid, Long requestId, String requestPurpose) {
        TechnicianEntity technician = trepo.findById(tid)
                .orElseThrow(() -> new NoSuchElementException("Technician " + tid + " does not exist!"));

        technician.setIsavailable(false);
        technician.setPurpose(requestPurpose);
        // Handle relationship in the RequestEntity instead

        return trepo.save(technician);
    }

    public List<TechnicianEntity> getAvailablePersonnel(String requestedStartTime, String requestedEndTime) {
        List<TechnicianEntity> allPersonnel = trepo.findAll();
        List<TechnicianEntity> availablePersonnel = new ArrayList<>();

        for (TechnicianEntity technician : allPersonnel) {
            boolean hasConflict = psrepo.hasConflict(
                    technician.getTech_id(), requestedStartTime, requestedEndTime);

            // if (hasConflict) {
            //     throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
            //             "Technician with ID " + technician.getTech_id() + " has a scheduling conflict.");
            // } else {
            //     availablePersonnel.add(technician);
            // }
            if (!hasConflict) {
                availablePersonnel.add(technician);
            }
        }

        return availablePersonnel;
    }

//	public RequestEntity getRequestByTechnician(Long tech_id) {
//        Optional<RequestEntity> request = rrepo.findFirstByTechId(tech_id);
//        if (request.isPresent()) {
//            return request.get();
//        } else {
//            throw new NoSuchElementException("No active request found for technician with ID " + tech_id);
//        }
//    }
    public String deleteTechnician(Long tid) {
        if (trepo.existsById(tid)) {
            trepo.deleteById(tid);
            return "Technician " + tid + " is successfully deleted!";
        } else {
            return "Technician " + tid + " does not exist.";
        }
    }

}
