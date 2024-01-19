package uns.ac.rs.uks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uns.ac.rs.uks.dto.request.LabelRequest;
import uns.ac.rs.uks.dto.response.LabelDTO;
import uns.ac.rs.uks.exception.NotFoundException;
import uns.ac.rs.uks.mapper.LabelMapper;
import uns.ac.rs.uks.model.Label;
import uns.ac.rs.uks.model.Repo;
import uns.ac.rs.uks.repository.LabelRepository;

import java.util.List;
import java.util.UUID;

@Service
public class LabelService {

    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private RepoService repoService;

    public Label getById(Long labelId) {
        return labelRepository.findById(labelId).orElseThrow(()->new NotFoundException("Label not found."));
    }

    public List<LabelDTO> getAllRepoLabels(UUID repoId) {
        List<Label> labels = labelRepository.findAllByRepositoryId(repoId);
        return LabelMapper.toDTOs(labels);
    }

    public LabelDTO createNewLabel(LabelRequest labelRequest) throws NotFoundException {
        Repo repo = repoService.getById(labelRequest.getRepoId());
        Label label = LabelMapper.fromDTO(labelRequest, repo);
        label = labelRepository.save(label);
        return LabelMapper.toDTO(label);
    }

    public LabelDTO editLabel(LabelRequest labelRequest) throws NotFoundException {
        Label label = getById(labelRequest.getId());
        label.setColor(labelRequest.getColor());
        label.setDescription(labelRequest.getDescription());
        label.setName(labelRequest.getName());
        label = labelRepository.save(label);
        return LabelMapper.toDTO(label);
    }

    public void deleteLabel(Long labelId) {
        labelRepository.deleteById(labelId);
    }

}
