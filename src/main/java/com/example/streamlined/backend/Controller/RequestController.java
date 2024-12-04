package com.example.streamlined.backend.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.streamlined.backend.Entity.RequestEntity;
import com.example.streamlined.backend.Repository.RequestRepository;
import com.example.streamlined.backend.Service.RequestService;
import com.example.streamlined.backend.Service.TechnicianService;

@RestController
@RequestMapping("/request")
@CrossOrigin(origins = {
    "http://localhost:5173", "http://localhost:3000",// Development environment
    "https://streamlined-service-portal-4amnsogyi-deployed-projects-4069a065.vercel.app", "https://streamlined-service-portal.vercel.app/" // Production environment
}, allowCredentials = "true")
public class RequestController {

    @Autowired
    TechnicianService tserv;

    @Autowired
    RequestService rserv;
    

    @Autowired
    RequestRepository requestRepository;

    @PostMapping("/add")
    public RequestEntity addRequest(
            @RequestParam("request_technician") String request_technician,
            @RequestParam("request_location") String request_location,
            @RequestParam("datetime") String datetime,
            @RequestParam(value = "preferredStartDate", required = false) String preferredStartDate,
            @RequestParam(value = "preferredEndDate", required = false) String preferredEndDate,
            // @RequestParam(value = "scheduledDate", required = false) String scheduledDate,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam("description") String description,
            @RequestParam("remarks") String remarks,
            @RequestParam("urgency_level") String urgency_level,
            @RequestParam("user_id") Long user_id,
            @RequestParam(value = "attachment", required = false) MultipartFile attachment) throws IOException {

        RequestEntity request = new RequestEntity();
        request.setRequest_technician(request_technician);
        request.setRequest_location(request_location);

        // No need to parse datetime strings, just set them directly
        request.setDatetime(datetime);
        
        // request.setScheduledDate(scheduledDate);
        request.setRemarks(remarks);
        request.setDescription(description);
        request.setUser_id(user_id);

        // Save the file to a local directory or cloud storage
        if (attachment != null && !attachment.isEmpty()) {
            byte[] bytes = attachment.getBytes();
            Path uploadDir = Paths.get("uploads");

            // Check if the directory exists, if not create it
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path path = uploadDir.resolve(attachment.getOriginalFilename());
            Files.write(path, bytes);
            request.setAttachment(path.toString());
        }

        return rserv.addRequest(request);
    }

    @PutMapping("/update/{request_id}")
    public ResponseEntity<RequestEntity> updateRequest(
            @PathVariable int request_id,
            @RequestParam(value = "request_technician", required = false) String request_technician,
            @RequestParam(value = "request_location", required = false) String request_location,
            @RequestParam(value = "datetime", required = false) String datetime, // Keep as String
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "urgency_level", required = false) String urgency_level,
            @RequestParam(value = "preferredDate", required = false) String preferredDate,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value ="remarks", required = false) String remarks,
            @RequestParam(value = "user_id", required = false) Long user_id,
            @RequestParam(value = "attachment", required = false) MultipartFile attachment) throws IOException {

        // Fetch the existing request
        Optional<RequestEntity> optionalRequest = rserv.getRequestById(request_id);
        if (!optionalRequest.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        RequestEntity existingRequest = optionalRequest.get();

        // Update fields if provided
        if (request_technician != null) {
            existingRequest.setRequest_technician(request_technician);
        }
        if (request_location != null) {
            existingRequest.setRequest_location(request_location);
        }
        if (datetime != null) {
            existingRequest.setDatetime(datetime);
        }
     
        if (description != null) {
            existingRequest.setDescription(description);
        }

        if (remarks != null) {
            existingRequest.setDescription(remarks);
        }

        if (user_id != null) {
            existingRequest.setUser_id(user_id);
        }

        // Check if the attachment is provided and save the file if present
        if (attachment != null && !attachment.isEmpty()) {
            byte[] bytes = attachment.getBytes();
            Path uploadDir = Paths.get("uploads");

            // Check if the directory exists, if not create it
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path path = uploadDir.resolve(attachment.getOriginalFilename());
            Files.write(path, bytes);
            existingRequest.setAttachment(path.toString());
        }

        // Save the updated request (ensure you handle your update logic in the service layer)
        RequestEntity updatedRequest = rserv.addRequest(existingRequest);
        return ResponseEntity.ok(updatedRequest);
    }

    /*@PostMapping("/add")
	public RequestEntity addRequest (@RequestBody RequestEntity request) {
        return rserv.addRequest(request);
    }*/
    @GetMapping("/getAllRequest")
    public List<RequestEntity> getAllRequests() {
        return rserv.getAllRequests();
    }

    @GetMapping("/{request_id}")
    public Optional<RequestEntity> getRequestById(@PathVariable int request_id) {
        Optional<RequestEntity> request = rserv.getRequestById(request_id);
        return request;
    }

    @GetMapping("/technician/{request_id}")
    public Long getTechnicianId(@PathVariable int request_id) {
        RequestEntity request = requestRepository.findById(request_id)
                .orElseThrow(() -> new RuntimeException("Request not found with id: " + request_id));
        return request.getTechnicianId();
    }
 

    @PutMapping("/updateStatus")
    public RequestEntity updateStatus(@RequestParam int request_id, @RequestBody RequestEntity newRequestStatus) {
        if ("Denied".equals(newRequestStatus.getStatus()) && newRequestStatus.getDenialReason() == null) {
            throw new IllegalArgumentException("Denial reason must be provided when denying a request.");
        }

        return rserv.updateStatus(request_id, newRequestStatus);
    }

//     @PostMapping("/assignTechnician")
// public ResponseEntity<String> assignTechnicianToRequest(
//         @RequestParam Long request_id,
//         @RequestParam Long tech_id,
//         @RequestParam String scheduledDate) {
//     try {
//         RequestEntity updatedRequest = rserv.assignTechnicianToRequest(request_id, tech_id, scheduledDate);
//         return ResponseEntity.ok("Technician " + tech_id + " assigned to request " + request_id + " successfully");
//     } catch (ResponseStatusException e) {
//         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getReason());
//     } catch (Exception e) {
//         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                              .body("An error occurred while assigning the technician.");
//     }
// }
    // @PutMapping("/submit-evaluation/{request_id}")
    // public RequestEntity submitEvaluation(
    //         @PathVariable int request_id,
    //         @RequestBody RequestEntity requestEntity) {
    //     RequestEntity request = requestRepository.findById(request_id).get();
    //     return requestRepository.save(request);
    // }

    @RequestMapping("/markViewed/{request_id}")
    public ResponseEntity<RequestEntity> markRequestAsViewed(@PathVariable int request_id) {
        RequestEntity updatedRequest = rserv.markRequestAsViewed(request_id);
        return new ResponseEntity<>(updatedRequest, HttpStatus.OK);
    }

    @PostMapping("/assignTechnician")
    public ResponseEntity<String> assignTechnicianToRequest(
            @RequestParam Long request_id,
            @RequestParam Long tech_id,
            @RequestParam String startTime) {

        try {
            RequestEntity updatedRequest = rserv.assignTechnicianToRequest(request_id, tech_id, startTime);
            return ResponseEntity.ok("Technician " + tech_id + " assigned to request " + request_id + " successfully");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while assigning the technician.");
        }
    }

    // @PostMapping("/assignTechnician")
    // public ResponseEntity<String> assignTechnicianToRequest(
    //         @RequestParam Long request_id,
    //         @RequestParam Long tech_id,
    //         @RequestParam String scheduledDate, // String instead of Timestamp
    //         @RequestParam String preferredDate) {  // String instead of Timestamp
    //     rserv.assignTechnicianToRequest(request_id, tech_id, scheduledDate, preferredDate);
    //     return ResponseEntity.ok("Technician " + tech_id + " assigned to request " + request_id + " successfully");
    // }
    @PostMapping("/removeTechnician")
    public ResponseEntity<String> removeTechnicianFromRequest(
            @RequestParam Long request_id) {
        rserv.removeTechnicianFromRequest(request_id);
        return ResponseEntity.ok("Technician removed from request " + request_id + " successfully");
    }

    // @PutMapping("/updateAnnouncement")
    // public RequestEntity updateUser(@RequestParam int request_id, @RequestBody RequestEntity newAnnouncementDetails) {
    // 	return rserv.updateAnnouncement(request_id, newAnnouncementDetails);
    // }
    @DeleteMapping("deleteRequest/{request_id}")
    public String deleteRequest(@PathVariable int request_id) {
        return rserv.deleteRequest(request_id);
    }

    @GetMapping("/attachment/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Path file = Paths.get("uploads").resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not read the file!", e);
        }
    }

    @GetMapping("/uploads/{fileName:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileName) {
        try {
            // Sanitize the file name to remove unwanted characters
            fileName = fileName.trim().replaceAll("[\\n\\r]", ""); // Remove newlines

            // Define the path to the uploads directory
            Path filePath = Paths.get("uploads").resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            // Check if the resource exists and is readable
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok().body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RequestEntity>> getRequestsByUserId(@PathVariable Long userId) {
        List<RequestEntity> requests = rserv.getRequestsByUserId(userId);
        if (requests.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(requests);
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<String> deleteAllRequests() {
        String result = rserv.deleteAllRequests();
        return ResponseEntity.ok(result);
    }

}
