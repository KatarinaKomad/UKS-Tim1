package uns.ac.rs.uks.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uns.ac.rs.uks.dto.request.FileRequest;
import uns.ac.rs.uks.dto.response.CommitsResponseDto;
import uns.ac.rs.uks.dto.response.FileDTO;
import uns.ac.rs.uks.service.FileService;

import java.util.List;

@RestController
@RequestMapping(value = "/file")
@Slf4j
public class FileController {
    @Autowired
    private FileService fileService;

    @PostMapping("/getFiles")
    public List<FileDTO> getFiles(@Valid @RequestBody FileRequest request) {
        return fileService.getFiles(request);
    }


    //    @GetMapping("/commits")
//    public List<CommitsResponseDto> getBranchCommits(@Valid @RequestBody TargetBranchRequest request){
//        return branchService.getCommits(request.getRepoId(), request.getBranchName());
//    }
    @PostMapping("/commits")
    public List<CommitsResponseDto> getFileCommits(@Valid @RequestBody FileRequest request){
        return fileService.getFileCommits(request);
    }
}
