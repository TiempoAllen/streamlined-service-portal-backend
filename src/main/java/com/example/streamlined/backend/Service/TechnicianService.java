package com.example.streamlined.backend.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.streamlined.backend.Entity.RequestEntity;
import com.example.streamlined.backend.Entity.TechnicianEntity;
import com.example.streamlined.backend.Repository.RequestRepository;
import com.example.streamlined.backend.Repository.TechnicianRepository;

@Service
public class TechnicianService {
	@Autowired
	TechnicianRepository trepo;

	RequestRepository rrepo;

	public TechnicianEntity addTechnician(TechnicianEntity technician) {
        return trepo.save(technician);
    }

    public List<TechnicianEntity> getAllTechnicians() {
        return trepo.findAll();
    }

    public Optional<TechnicianEntity> getTechnicianById(Long tid) {
        return trepo.findById(tid);
    }
 
   
    public TechnicianEntity updateTechnician(Long tid, TechnicianEntity newTechnicianDetails) {
        TechnicianEntity technician = trepo.findById(tid).orElseThrow(() ->
            new NoSuchElementException("Technician " + tid + " does not exist!")
        );

        technician.setTech_name(newTechnicianDetails.getTech_name());
        technician.setTech_phone(newTechnicianDetails.getTech_phone());
        technician.setTech_gender(newTechnicianDetails.getTech_gender());
        technician.setTech_status(newTechnicianDetails.getTech_status());
        technician.setTech_classification(newTechnicianDetails.getTech_classification());

        return trepo.save(technician);
    }

    public TechnicianEntity assignTechnicianToRequest(Long tid, Long requestId, String requestPurpose) {
        TechnicianEntity technician = trepo.findById(tid).orElseThrow(() ->
            new NoSuchElementException("Technician " + tid + " does not exist!")
        );

        technician.setIsavailable(false);
        technician.setPurpose(requestPurpose);
        // Handle relationship in the RequestEntity instead

        return trepo.save(technician);
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
        if(trepo.existsById(tid)) {
            trepo.deleteById(tid);
            return "Technician " + tid + " is successfully deleted!";
        } else {
            return "Technician " + tid + " does not exist.";
        }
    }

}
