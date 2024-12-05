package com.example.streamlined.backend.Controller;

import com.example.streamlined.backend.Entity.RemarksEntity;
import com.example.streamlined.backend.Service.RemarksService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/remarks")
@CrossOrigin(origins = {
    "http://localhost:5173", "http://localhost:3000",// Development environment
    "https://streamlined-service-portal-4amnsogyi-deployed-projects-4069a065.vercel.app", "https://streamlined-service-portal.vercel.app/" // Production environment
}, allowCredentials = "true")
public class RemarksController {

    private final RemarksService remarksService;

    public RemarksController(RemarksService remarksService) {
        this.remarksService = remarksService;
    }

    @PostMapping("/add")
    public ResponseEntity<RemarksEntity> addRemark(
            @RequestParam int request_id,
            @RequestParam String content,
            @RequestParam String createdBy,
            @RequestParam String status) {
        RemarksEntity newRemark = remarksService.addRemark(request_id, content, createdBy, status);
        return ResponseEntity.ok(newRemark);
    }

    @GetMapping("/{request_id}")
    public ResponseEntity<List<RemarksEntity>> getRemarksByRequestId(@PathVariable Long request_id) {
        List<RemarksEntity> remarks = remarksService.getRemarksByRequestId(request_id);
        return ResponseEntity.ok(remarks);
    }

    @DeleteMapping("/{remark_id}")
    public ResponseEntity<Void> deleteRemark(@PathVariable Long remark_id) {
        remarksService.deleteRemark(remark_id);
        return ResponseEntity.noContent().build();
    }
}
