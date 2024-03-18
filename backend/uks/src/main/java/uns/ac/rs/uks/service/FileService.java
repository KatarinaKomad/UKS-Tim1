package uns.ac.rs.uks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uns.ac.rs.uks.dto.request.FileRequest;
import uns.ac.rs.uks.dto.response.CommitsResponseDto;
import uns.ac.rs.uks.dto.response.FileDTO;

import java.util.List;

@Service
public class FileService {

    @Autowired
    private GitoliteService gitoliteService;

    public List<FileDTO> getFiles(FileRequest request) {
        return gitoliteService.getFiles(request.getRepoName(), request.getBranchName(), request.getFilePath());
    }

    public List<CommitsResponseDto> getFileCommits(FileRequest fr) {
        List<CommitsResponseDto> commits = gitoliteService.getFileCommits(fr.getRepoName(), fr.getBranchName(), fr.getFilePath());
        gitoliteService.removeClonedRepo(fr.getRepoName());
        return commits;
    }
}
