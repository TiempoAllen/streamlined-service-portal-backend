package com.example.streamlined.backend.Service;

import java.time.LocalDate;
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
            nserv.addNotification("A new request has been submitted by " + userFullName + ".", admin.getUser_id(),
                    "Admin");
        }

        return savedRequest;
    }

    // public byte[] getAttachment(int requestId) {
    //     RequestEntity request = rrepo.findById(requestId)
    //             .orElseThrow(() -> new NoSuchElementException("Request with ID " + requestId + " not found."));
    //     return request.getAttachment();
    // }
    // public RequestEntity updateAttachment(int requestId, byte[] newAttachment) {
    //     RequestEntity request = rrepo.findById(requestId)
    //             .orElseThrow(() -> new NoSuchElementException("Request with ID " + requestId + " not found."));
    //     request.setAttachment(newAttachment);
    //     return rrepo.save(request);
    // }
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
                    () -> new NoSuchElementException("Request with ID " + request_id + " does not exist!"));

            // Update the status
            request.setStatus(newRequestStatus.getStatus());

            // Handle notifications based on the new status
            String requestPrefix = "Request " + request_id + ": "; // Prefix with request ID

            if ("Approved".equals(newRequestStatus.getStatus())) {
                nserv.addNotification(
                        requestPrefix + "Your request has been approved!",
                        request.getUser_id(),
                        "User");
            } else if ("Denied".equals(newRequestStatus.getStatus())) {
                request.setDenialReason(newRequestStatus.getDenialReason());
                nserv.addNotification(
                        requestPrefix + "Your request has been denied. Reason: " + newRequestStatus.getDenialReason(),
                        request.getUser_id(),
                        "User");
            } else if ("Done".equals(newRequestStatus.getStatus())) {
                nserv.addNotification(
                        requestPrefix + "Your request is done.",
                        request.getUser_id(),
                        "User");

                // Update the status
                request.setStatus(newRequestStatus.getStatus());

                // Handle notifications based on the new status
                // Enclose title in quotation marks
                if ("Approved".equals(newRequestStatus.getStatus())) {
                    nserv.addNotification(
                            "Your request  " + request_id + " has been approved.",
                            request.getUser_id(),
                            "User");
                } else if ("Denied".equals(newRequestStatus.getStatus())) {
                    request.setDenialReason(newRequestStatus.getDenialReason());
                    nserv.addNotification(
                            "Your request " + request_id + " has been denied. Reason: "
                            + newRequestStatus.getDenialReason(),
                            request.getUser_id(),
                            "User");
                } else if ("Done".equals(newRequestStatus.getStatus())) {
                    request.setCompletedStartDate(LocalDate.now().toString());
                    nserv.addNotification(
                            "Your request " + request_id + " is done.",
                            request.getUser_id(),
                            "User");
                } else if ("Cancelled".equals(newRequestStatus.getStatus())) {
                    List<UserEntity> admins = urepo.findByIsadmin(true);
                    for (UserEntity admin : admins) {
                        nserv.addNotification(
                                requestPrefix + "was cancelled.",
                                admin.getUser_id(),
                                "Admin");
                    }
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

            String requestPrefix = "Request " + request_id + ": ";

            // Check if the request is already viewed
            if (!request.getIsOpened()) {
                // Mark the request as opened
                request.setIsOpened(true);

                // Notify the user that their request has been viewed, using the title
                nserv.addNotification(
                        requestPrefix + "Your request has been viewed.",
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

    public RequestEntity assignTechniciansToRequest(Long request_id, List<Long> tech_ids, String scheduledStartDate) {
        // Fetch the request by ID
        RequestEntity request = rrepo.findById(request_id.intValue())
                .orElseThrow(() -> new NoSuchElementException("Request " + request_id + " does not exist!"));

        // Iterate through the list of technician IDs
        for (Long tech_id : tech_ids) {
            TechnicianEntity technician = trepo.findById(tech_id.longValue())
                    .orElseThrow(() -> new NoSuchElementException("Technician " + tech_id + " does not exist!"));

            // Check if the technician is already assigned
            if (request.getTechnicians().contains(technician)) {
                throw new IllegalArgumentException("Technician " + tech_id + " is already assigned to this request.");
            }

            // Add the technician to the request
            request.getTechnicians().add(technician);
        }

        // Set the request's scheduled start date and status
        request.setScheduledStartDate(scheduledStartDate);
        request.setStatus("In Progress");

        // Notify the user about the assignment
        nserv.addNotification("Your request has been assigned to technicians.", request.getUser_id(), "User");

        // Save and return the updated request
        return rrepo.save(request);
    }

    public RequestEntity removeTechnicianFromRequest(Long request_id, Long tech_id) {
        // Fetch the request by ID
        RequestEntity request = rrepo.findById(request_id.intValue())
                .orElseThrow(() -> new NoSuchElementException("Request " + request_id + " does not exist!"));

        // Fetch the technician by ID
        TechnicianEntity technician = trepo.findById(tech_id)
                .orElseThrow(() -> new NoSuchElementException("Technician " + tech_id + " does not exist!"));

        // Remove the technician from the request's technician list
        if (!request.getTechnicians().remove(technician)) {
            throw new IllegalArgumentException("Technician " + tech_id + " is not assigned to this request.");
        }

        // Save the updated request
        return rrepo.save(request);
    }

    public String deleteRequestById(int request_id) {
        // Check if the request exists
        RequestEntity request = rrepo.findById(request_id)
                .orElseThrow(() -> new NoSuchElementException("Request with ID " + request_id + " does not exist!"));

        // Delete the request
        rrepo.delete(request);

        // Return a confirmation message
        return "Request with ID " + request_id + " has been successfully deleted!";
    }

    public String deleteAllRequests() {
        rrepo.deleteAll();
        return "All requests have been successfully deleted!";
    }

}
