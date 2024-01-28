package uns.ac.rs.uks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uns.ac.rs.uks.dto.request.ChangeStateRequest;
import uns.ac.rs.uks.dto.request.MilestoneRequest;
import uns.ac.rs.uks.dto.response.MilestoneDTO;
import uns.ac.rs.uks.exception.NotFoundException;
import uns.ac.rs.uks.mapper.MilestoneMapper;
import uns.ac.rs.uks.model.Milestone;
import uns.ac.rs.uks.model.Repo;
import uns.ac.rs.uks.model.State;
import uns.ac.rs.uks.repository.MilestoneRepository;

import java.util.List;
import java.util.UUID;

@Service
public class MilestoneService {


    @Autowired
    private MilestoneRepository milestoneRepository;
    @Autowired
    private RepoService repoService;

    public Milestone getById(Long milestoneId) {
        return milestoneRepository.findById(milestoneId).orElseThrow(()->new NotFoundException("Milestone not found."));
    }

    public List<MilestoneDTO> getAllRepoMilestones(UUID repoId) {
        List<Milestone> milestones = milestoneRepository.findAllByRepositoryId(repoId);
        return MilestoneMapper.toDTOs(milestones);
    }

    public MilestoneDTO createNewMilestone(MilestoneRequest milestoneRequest) throws NotFoundException {
        Repo repo = repoService.getById(milestoneRequest.getRepoId());
        Milestone milestone = MilestoneMapper.fromDTO(milestoneRequest, repo);
        milestone.setState(State.OPEN);
        milestone = milestoneRepository.save(milestone);
        return MilestoneMapper.toDTO(milestone);
    }

    public MilestoneDTO editMilestone(MilestoneRequest milestoneRequest) throws NotFoundException {
        Milestone milestone = getById(milestoneRequest.getId());
        MilestoneMapper.map(milestone, milestoneRequest);
        milestone = milestoneRepository.save(milestone);
        return MilestoneMapper.toDTO(milestone);
    }

    public void deleteMilestone(Long milestoneId) {
        milestoneRepository.deleteById(milestoneId);
    }
    @Transactional
    public void changeState(ChangeStateRequest changeStateRequest) {
        milestoneRepository.updateState(changeStateRequest.getId(), changeStateRequest.getState());
    }
}
