package com.example.streamlined.backend.Service;

import java.util.HashMap;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.streamlined.backend.Entity.PersonnelScheduleEntity;
import com.example.streamlined.backend.Entity.RequestEntity;
import com.example.streamlined.backend.Entity.TechnicianEntity;
import com.example.streamlined.backend.Entity.UserEntity;
import com.example.streamlined.backend.Repository.PersonnelSchedule;
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

	@Autowired
    PersonnelSchedule psrepo;


    public RequestEntity addRequest(RequestEntity request) {
        // Fetch the user who made the request
        Optional<UserEntity> userOpt = urepo.findById(request.getUser_id().intValue());
        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();

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

    public List<RequestEntity> getRequestsByUserId(Long userId) {
        return rrepo.findAllByUserId(userId);
    }

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

       


	public RequestEntity markRequestAsViewed(int request_id) {
		try {
			// Find the request by its ID
			RequestEntity request = rrepo.findById(request_id)
				.orElseThrow(() -> new NoSuchElementException("Request " + request_id + " does not exist!"));
	
			// Check if the request is already viewed
			if (!request.getIsOpened()) {
				// Mark the request as opened
				request.setIsOpened(true);
	
				// Notify the user that their request has been viewed
				nserv.addNotification(
					"Your request (ID: " + request_id + ") has been viewed.",
					request.getUser_id(),
					"User"
				);
	
				// Save the updated request
				request = rrepo.save(request);
			}
	
			return request;
	
		} catch (NoSuchElementException ex) {
			// Handle case where the request is not found
			throw new NoSuchElementException("Request " + request_id + " does not exist!");
		}
	}
	
	
	


	public RequestEntity assignTechnicianToRequest(Long request_id, Long tech_id, String scheduledStartDate,
	String scheduledEndDate) {
	RequestEntity request = rrepo.findById(request_id.intValue())
			.orElseThrow(() -> new NoSuchElementException("Request " + request_id + " does not exist!"));

	TechnicianEntity technician = trepo.findById(tech_id.longValue())
			.orElseThrow(() -> new NoSuchElementException("Technician " + tech_id + " does not exist!"));

	// Check for conflicts with existing requests for the technician on the same scheduled date
	// for (RequestEntity existingRequest : technician.getRequests()) {
	//     if (scheduledStartDate.equals(existingRequest.getScheduledStartDate())) {
	//         throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Technician is already assigned to another request on this date.");
	//     }
	// }
	boolean hasConflict = psrepo.hasConflict(tech_id, scheduledStartDate, scheduledEndDate);
	if (hasConflict) {
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Personnel is not available during the requested time.");
	}

	// Set the request's scheduled time and status
	request.setScheduledStartDate(scheduledStartDate);
	request.setScheduledEndDate(scheduledEndDate);
	request.setStatus("Assigned");
	nserv.addNotification("Your request has been assigned.", request.getUser_id(), "User");

	rrepo.save(request);

	PersonnelScheduleEntity schedule = new PersonnelScheduleEntity(
			scheduledEndDate, technician, request, null, scheduledStartDate, "Assigned");
	psrepo.save(schedule);

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

    public String deleteRequest(int request_id) {
        String msg = "";

        if (rrepo.findById(request_id) != null) {
            rrepo.deleteById(request_id);
            msg = "Request " + request_id + " is successfully deleted!";
        } else {
            msg = "Request " + request_id + " does not exist.";
        }
        return msg;
    }


	public Map<String, List<String>> getFeedbackHighlights(Long technicianId) {
        List<String> positiveFeedback = rrepo.findPositiveFeedbackByTechnicianId(technicianId);
        List<String> negativeFeedback = rrepo.findNegativeFeedbackByTechnicianId(technicianId);

        Map<String, List<String>> feedbackHighlights = new HashMap<>();
        feedbackHighlights.put("positive", positiveFeedback);
        feedbackHighlights.put("negative", negativeFeedback);

        return feedbackHighlights;
    }



	public String deleteAllRequests() {
		rrepo.deleteAll();
		return "All requests have been successfully deleted!";
	}
  
}
