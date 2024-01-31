package uns.ac.rs.uks.mapper;

import uns.ac.rs.uks.dto.response.IssueEventDTO;
import uns.ac.rs.uks.model.IssueEvent;

import java.util.List;

public class IssueEventMapper {

    public static IssueEventDTO toDTO(IssueEvent event) {
        return IssueEventDTO.builder()
                .id(event.getId())
                //issueId
                .authorId(event.getAuthor() != null ? event.getAuthor().getId() : null)
                .value(event.getNewValue())
                .type(event.getType())
                .createdAt(event.getCreatedAt())
                .build();
    }

    public static List<IssueEventDTO> toDTOs(List<IssueEvent> events) {
        return events.stream().map(IssueEventMapper::toDTO).toList();
    }


}
