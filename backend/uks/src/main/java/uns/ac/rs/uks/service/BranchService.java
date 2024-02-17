package uns.ac.rs.uks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uns.ac.rs.uks.dto.response.BranchBasicInfoDTO;
import uns.ac.rs.uks.exception.NotFoundException;
import uns.ac.rs.uks.mapper.BranchMapper;
import uns.ac.rs.uks.mapper.RepoMapper;
import uns.ac.rs.uks.model.Branch;
import uns.ac.rs.uks.model.Repo;
import uns.ac.rs.uks.repository.BranchRepository;
import uns.ac.rs.uks.repository.repo.RepoRepository;

import java.util.List;
import java.util.UUID;

@Service
public class BranchService {

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private RepoRepository repoRepository;

    @Autowired
    private GitoliteService gitoliteService;

    public Branch createDefaultBranch(Repo repo) {
        Branch branch = new Branch();
        branch.setName("main");
        branch.setRepository(repo);
        branchRepository.save(branch);
        return branch;
    }

    public  Branch getById(Long id) {
        return  branchRepository.findById(id).orElseThrow(()->new NotFoundException("Branch not found."));
    }

    public List<BranchBasicInfoDTO> getRepoBranches(UUID id) {
        List<Branch> branches = branchRepository.findAllByRepositoryId(id);
        return BranchMapper.toDTOs(branches);
    }

    public List<BranchBasicInfoDTO> getGitoliteRepoBranches(UUID id){
        var repo = repoRepository.findById(id).orElseThrow(() -> new NotFoundException("Repo not found"));
        return gitoliteService.readRepoBranches(repo.getName().replace(" ", "-"));
    }
}
