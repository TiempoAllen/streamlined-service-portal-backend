package com.example.streamlined.backend.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.streamlined.backend.Entity.RequestEntity;
import com.example.streamlined.backend.Entity.TechnicianEntity;
import com.example.streamlined.backend.Entity.UserEntity;
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
            // Fetch the existing request by ID
            request = rrepo.findById(request_id).orElseThrow(
                    () -> new NoSuchElementException("Request with ID " + request_id + " does not exist!")
            );

            // Update the status
            request.setStatus(newRequestStatus.getStatus());

            // Handle notifications based on the new status
            // Enclose title in quotation marks
            if ("Approved".equals(newRequestStatus.getStatus())) {
                nserv.addNotification(
                        "Your request  " + request_id + " has been approved.",
                        request.getUser_id(),
                        "User"
                );
            } else if ("Denied".equals(newRequestStatus.getStatus())) {
                request.setDenialReason(newRequestStatus.getDenialReason());
                nserv.addNotification(
                        "Your request " + request_id + " has been denied. Reason: " + newRequestStatus.getDenialReason(),
                        request.getUser_id(),
                        "User"
                );
            } else if ("Done".equals(newRequestStatus.getStatus())) {
                nserv.addNotification(
                        "Your request " + request_id + " is done.",
                        request.getUser_id(),
                        "User"
                );
            } else if ("Cancelled".equals(newRequestStatus.getStatus())) {
                List<UserEntity> admins = urepo.findByIsadmin(true);
                for (UserEntity admin : admins) {
                    nserv.addNotification(
                            "The request " + request_id + " has been cancelled.",
                            admin.getUser_id(),
                            "Admin"
                    );
                }
            }

        } catch (NoSuchElementException ex) {
            // Handle case where the request is not found
            throw new NoSuchElementException("Request with ID " + request_id + " does not exist!");
        } finally {
            // Save and return the updated request
            return rrepo.save(request);
        }
    }

    public RequestEntity markRequestAsViewed(int request_id) {
        try {
            // Find the request by its ID
            RequestEntity request = rrepo.findById(request_id)
                    .orElseThrow(() -> new NoSuchElementException("Request with ID " + request_id + " does not exist!"));

            // Check if the request is already viewed
            if (!request.getIsOpened()) {
                // Mark the request as opened
                request.setIsOpened(true);

                // Notify the user that their request has been viewed, using the title
                nserv.addNotification(
                        "Your request \"" + request.getRequest_id() + "\" has been viewed.",
                        request.getUser_id(),
                        "User"
                );

                // Save the updated request
                request = rrepo.save(request);
            }

            return request;

        } catch (NoSuchElementException ex) {
            // Handle case where the request is not found
            throw new NoSuchElementException("Request with ID " + request_id + " does not exist!");
        }
    }

    public RequestEntity assignTechnicianToRequest(Long request_id, Long tech_id, String scheduledStartDate) {
        RequestEntity request = rrepo.findById(request_id.intValue())
                .orElseThrow(() -> new NoSuchElementException("Request " + request_id + " does not exist!"));

        TechnicianEntity technician = trepo.findById(tech_id.longValue())
                .orElseThrow(() -> new NoSuchElementException("Technician " + tech_id + " does not exist!"));
        // boolean hasConflict = psrepo.hasConflict(tech_id, scheduledStartDate);
        // if (hasConflict) {
        // 	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Personnel is not available during the requested time.");
        // }

        // Set the request's scheduled time and status
        request.setScheduledStartDate(scheduledStartDate);
        request.setStatus("In Progress");
        nserv.addNotification("Your request has been assigned.", request.getUser_id(), "User");

        rrepo.save(request);

        // PersonnelScheduleEntity schedule = new PersonnelScheduleEntity(
        // 		scheduledEndDate, technician, request, null, scheduledStartDate, "Assigned");
        // psrepo.save(schedule);
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

    public String deleteAllRequests() {
        rrepo.deleteAll();
        return "All requests have been successfully deleted!";
    }

}
