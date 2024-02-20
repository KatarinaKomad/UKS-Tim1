package uns.ac.rs.uks.mapper;

import uns.ac.rs.uks.dto.response.IssueEventDTO;
import uns.ac.rs.uks.dto.response.PullRequestEventDTO;
import uns.ac.rs.uks.model.IssueEvent;
import uns.ac.rs.uks.model.PullRequestEvent;

import java.util.List;

public class PullRequestEventMapper {

    public static PullRequestEventDTO toDTO(PullRequestEvent event) {
        return PullRequestEventDTO.builder()
                .id(event.getId())
                //issueId
                .author(event.getAuthor() != null ? UserMapper.toDTO(event.getAuthor()) : null)
                .value(event.getNewValue())
                .type(event.getType())
                .createdAt(event.getCreatedAt())
                .build();
    }

    public static List<PullRequestEventDTO> toDTOs(List<PullRequestEvent> events) {
        return events.stream().map(PullRequestEventMapper::toDTO).toList();
    }


}
