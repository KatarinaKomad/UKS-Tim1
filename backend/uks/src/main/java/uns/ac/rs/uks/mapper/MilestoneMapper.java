package uns.ac.rs.uks.mapper;

import uns.ac.rs.uks.dto.request.MilestoneRequest;
import uns.ac.rs.uks.dto.response.MilestoneDTO;
import uns.ac.rs.uks.model.Milestone;
import uns.ac.rs.uks.model.Repo;

import java.util.List;

public class MilestoneMapper {

    public static MilestoneDTO toDTO(Milestone milestone) {
        return MilestoneDTO.builder()
                .id(milestone.getId())
                .name(milestone.getName())
                .dueDate(milestone.getDueDate())
                .description(milestone.getDescription())
                .state(milestone.getState())
//                .items(milestone.getItems())
                .repoId(milestone.getRepository() != null ? milestone.getRepository().getId() : null)
                .build();
    }

    public static List<MilestoneDTO> toDTOs(List<Milestone> milestones) {
        return milestones.stream().map(MilestoneMapper::toDTO).toList();
    }

    public static Milestone fromDTO(MilestoneRequest milestone, Repo repo) {
        return Milestone.builder()
                .name(milestone.getName())
                .dueDate(milestone.getDueDate())
                .description(milestone.getDescription())
                .repository(repo)
                .build();
    }

    public static Milestone map(Milestone oldMilestone, MilestoneRequest newMilestone) {
        oldMilestone.setName(newMilestone.getName());
        oldMilestone.setDescription(newMilestone.getDescription());
        oldMilestone.setDueDate(newMilestone.getDueDate());
        return oldMilestone;
    }
}
