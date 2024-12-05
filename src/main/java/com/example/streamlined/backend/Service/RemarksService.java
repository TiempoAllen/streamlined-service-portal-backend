package com.example.streamlined.backend.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.streamlined.backend.Entity.RemarksEntity;
import com.example.streamlined.backend.Entity.RequestEntity;
import com.example.streamlined.backend.Repository.RemarksRepository;
import com.example.streamlined.backend.Repository.RequestRepository;

@Service
public class RemarksService {
    private final RemarksRepository remarksRepository;
    private final RequestRepository requestRepository;

    public RemarksService(RemarksRepository remarksRepository, RequestRepository requestRepository) {
        this.remarksRepository = remarksRepository;
        this.requestRepository = requestRepository;
    }

    public RemarksEntity addRemark(Integer request_id, String content, String createdBy, String status) {
        // Validate if the request exists
        if (!requestRepository.existsById(request_id)) {
            throw new RuntimeException("Request not found with ID: " + request_id);
        }
    
        // Create and save the remark
        RemarksEntity remark = new RemarksEntity();
        remark.setRequest_id((long)request_id); // Use Integer directly
        remark.setContent(content);
        remark.setCreatedBy(createdBy);
        remark.setStatus(status);
    
        // Generate current datetime
        String currentDatetime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        remark.setDatetime(currentDatetime);
    
        return remarksRepository.save(remark);
    }
    
    

    public List<RemarksEntity> getRemarksByRequestId(Long request_id) {
        return remarksRepository.findByRequestId(request_id);
    }

    public void deleteRemark(Long remarkId) {
        remarksRepository.deleteById(remarkId);
    }
}
