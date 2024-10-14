package com.example.streamlined.backend.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.streamlined.backend.Entity.RequestEntity;
import com.example.streamlined.backend.Entity.TechnicianEntity;
import com.example.streamlined.backend.Entity.UserEntity;
import com.example.streamlined.backend.Repository.NotificationRepository;
import com.example.streamlined.backend.Repository.RequestRepository;
import com.example.streamlined.backend.Repository.TechnicianRepository;
import com.example.streamlined.backend.Repository.UserRepository;


@Service
public class RequestService {
	@Autowired
	RequestRepository rrepo;

	@Autowired
    UserRepository urepo;

	@Autowired
	TechnicianRepository trepo;

	@Autowired
	NotificationService nserv;

	public RequestEntity addRequest(RequestEntity request) {
	    // Fetch the user who made the request
	    Optional<UserEntity> userOpt = urepo.findById(request.getUser_id().intValue());
	    if (userOpt.isPresent()) {
	        UserEntity user = userOpt.get();

	        request.setDepartment(user.getDepartment());
	        request.setUser_firstname(user.getFirstname());
	        request.setUser_lastname(user.getLastname());
	    } else {
	        throw new NoSuchElementException("User with ID " + request.getUser_id() + " not found.");
	    }

	    // Set the request status to Pending
	    request.setStatus("Pending");

	    // Save the request
	    RequestEntity savedRequest = rrepo.save(request);

	    // Fetch all admin users
	    List<UserEntity> admins = urepo.findByIsadmin(true);

	    // Add a notification for each admin
	    String userFullName = request.getUser_firstname() + " " + request.getUser_lastname();
	    for (UserEntity admin : admins) {
	        nserv.addNotification("A new request has been submitted by " + userFullName + ".", admin.getUser_id(), "Admin");
	    }

	    return savedRequest;
	}



	/*public RequestEntity addRequest(RequestEntity request) {
		request.setStatus("Pending");
		return rrepo.save(request);
	}*/

	public List<RequestEntity> getAllRequests() {
		return rrepo.findAll();
	}

	public Optional<RequestEntity> getRequestById(int request_id) {
		return rrepo.findById(request_id);
	}

	@SuppressWarnings("finally")
	public RequestEntity updateStatus(int request_id, RequestEntity newRequestStatus) {
	    RequestEntity request = new RequestEntity();
	    try {
	        request = rrepo.findById(request_id).get();

	        request.setStatus(newRequestStatus.getStatus());

	        if ("Approved".equals(newRequestStatus.getStatus())) {
	            nserv.addNotification("Your request (ID: " + request_id + ") has been approved.", request.getUser_id(), "User");
	        } else if ("Denied".equals(newRequestStatus.getStatus())) {
	            request.setDenialReason(newRequestStatus.getDenialReason());
	            nserv.addNotification("Your request (ID: " + request_id + ") has been denied. Reason: " + newRequestStatus.getDenialReason(), request.getUser_id(), "User");
	        } else if ("Done".equals(newRequestStatus.getStatus())) {
	            nserv.addNotification("Your request (ID: " + request_id + ") is done.", request.getUser_id(), "User");
	        }
	    } catch(NoSuchElementException ex) {
	        throw new NoSuchElementException("Request " + request_id + " does not exist!");
	    } finally {
	        return rrepo.save(request);
	    }
	}



	public RequestEntity assignTechnicianToRequest(Long request_id, Long tech_id, String startTime, String endTime) {
	    RequestEntity request = rrepo.findById(request_id.intValue())
	            .orElseThrow(() -> new NoSuchElementException("Request " + request_id + " does not exist!"));

	    TechnicianEntity technician = trepo.findById(tech_id.longValue())
	            .orElseThrow(() -> new NoSuchElementException("Technician " + tech_id + " does not exist!"));

	    // Check for time conflicts with existing requests for the technician
	    for (RequestEntity existingRequest : technician.getRequests()) {
	        if (startTime.compareTo(existingRequest.getEndTime()) < 0 && endTime.compareTo(existingRequest.getStartTime()) > 0) {
	        	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Technician is already assigned to another request during this time.");
	        }
	    }

	 // Set the technician and update time properties on the request
	    request.setTechnicianId(tech_id);
	    request.setTechnician(technician);
	    request.setStartTime(startTime);
	    request.setEndTime(endTime);

	    // Add the request to the technician's list (non-owning side)
	    technician.getRequests().add(request);

	    // Save the request (owning side)
	    rrepo.save(request);


	    return request;
	}


	public RequestEntity removeTechnicianFromRequest(Long request_id) {
	    RequestEntity request = rrepo.findById(request_id.intValue())
	            .orElseThrow(() -> new NoSuchElementException("Request " + request_id + " does not exist!"));

	    TechnicianEntity technician = request.getTechnician();
	    if (technician != null) {
	        // Remove the request from the technician's list
	        technician.removeRequest(request);
	        // Optionally save the technician entity if needed
	        trepo.save(technician);
	    }

	    // Remove the technician from the request
	    request.setTechnician(null);
	    request.setTechnicianId(null); // Optional: Set technicianId to null

	    return rrepo.save(request);
	}


	public String deleteRequest (int request_id) {
		String msg = "";

		if(rrepo.findById(request_id) != null) {
			rrepo.deleteById(request_id);
			msg = "Request " + request_id + " is successfully deleted!";
		} else {
			msg = "Request " + request_id + " does not exist.";
		}
		return msg;
	}
}
