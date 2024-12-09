package com.example.streamlined.backend.Controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
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
    public ResponseEntity<RequestEntity> addRequest(
            @RequestParam("request_technician") String requestTechnician,
            @RequestParam("request_location") String requestLocation,
            @RequestParam("datetime") String datetime,
            @RequestParam("description") String description,
            @RequestParam("remarks") String remarks,
            @RequestParam("user_id") Long userId,
            @RequestParam(value = "attachment", required = false) MultipartFile attachment) throws IOException {

        RequestEntity request = new RequestEntity();
        request.setRequest_technician(requestTechnician);
        request.setRequest_location(requestLocation);
        request.setDatetime(datetime);
        request.setDescription(description);
        request.setRemarks(remarks);
        request.setUser_id(userId);

        if (attachment != null && !attachment.isEmpty()) {
            // Encode the attachment to Base64
            String base64Attachment = Base64.getEncoder().encodeToString(attachment.getBytes());
            request.setAttachment(base64Attachment);
        }

        RequestEntity savedRequest = rserv.addRequest(request);
        return ResponseEntity.ok(savedRequest);
    }

    @PutMapping("/update/{request_id}")
    public ResponseEntity<RequestEntity> updateRequest(
            @PathVariable int request_id,
            @RequestParam(value = "request_technician", required = false) String requestTechnician,
            @RequestParam(value = "request_location", required = false) String requestLocation,
            @RequestParam(value = "datetime", required = false) String datetime,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "remarks", required = false) String remarks,
            @RequestParam(value = "urgency_level", required = false) String urgencyLevel,
            @RequestParam(value = "attachment", required = false) MultipartFile attachment) throws IOException {

        Optional<RequestEntity> optionalRequest = rserv.getRequestById(request_id);
        if (!optionalRequest.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        RequestEntity existingRequest = optionalRequest.get();
        if (requestTechnician != null) {
            existingRequest.setRequest_technician(requestTechnician);
        }
        if (requestLocation != null) {
            existingRequest.setRequest_location(requestLocation);
        }
        if (datetime != null) {
            existingRequest.setDatetime(datetime);
        }
        if (description != null) {
            existingRequest.setDescription(description);
        }
        if (remarks != null) {
            existingRequest.setRemarks(remarks);
        }

        if (attachment != null && !attachment.isEmpty()) {
            // Encode the attachment to Base64
            String base64Attachment = Base64.getEncoder().encodeToString(attachment.getBytes());
            existingRequest.setAttachment(base64Attachment);
        }

        RequestEntity updatedRequest = rserv.addRequest(existingRequest);
        return ResponseEntity.ok(updatedRequest);
    }

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

    @RequestMapping("/markViewed/{request_id}")
    public ResponseEntity<RequestEntity> markRequestAsViewed(@PathVariable int request_id) {
        RequestEntity updatedRequest = rserv.markRequestAsViewed(request_id);
        return new ResponseEntity<>(updatedRequest, HttpStatus.OK);
    }

    @PostMapping("/assignTechnician")
    public ResponseEntity<RequestEntity> assignTechniciansToRequest(
            @RequestParam Long requestId,
            @RequestParam List<Long> techIds,
            @RequestParam String scheduledStartDate
    ) {
        try {
            RequestEntity updatedRequest = rserv.assignTechniciansToRequest(requestId, techIds, scheduledStartDate);
            return ResponseEntity.ok(updatedRequest);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/removeTechnician")
    public ResponseEntity<String> removeTechnicianFromRequest(@RequestParam Long request_id, @RequestParam Long tech_id) {
        try {
            rserv.removeTechnicianFromRequest(request_id, tech_id);
            return ResponseEntity.ok("Technician removed successfully from the request.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while removing technician: " + e.getMessage());
        }
    }

    @GetMapping("/attachment/{request_id}")
    public ResponseEntity<byte[]> getAttachment(@PathVariable int request_id) {
        Optional<RequestEntity> optionalRequest = rserv.getRequestById(request_id);
        if (optionalRequest.isEmpty() || optionalRequest.get().getAttachment() == null) {
            return ResponseEntity.notFound().build();
        }

        String base64Attachment = optionalRequest.get().getAttachment();
        byte[] attachmentBytes = Base64.getDecoder().decode(base64Attachment);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=request-" + request_id + ".blob")
                .body(attachmentBytes);
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
            // Sanitize the file name
            fileName = fileName.trim().replaceAll("[\\n\\r]", "");

            // Use the absolute path to the uploads directory
            Path filePath = Paths.get("uploads").resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            // Check if the resource exists and is readable
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
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

    @DeleteMapping("/{request_id}")
    public ResponseEntity<String> deleteRequestById(@PathVariable int request_id) {
        String response = rserv.deleteRequestById(request_id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<String> deleteAllRequests() {
        String result = rserv.deleteAllRequests();
        return ResponseEntity.ok(result);
    }

}
