package uns.ac.rs.uks.mapper;

import uns.ac.rs.uks.dto.request.LabelRequest;
import uns.ac.rs.uks.dto.response.LabelDTO;
import uns.ac.rs.uks.model.Label;
import uns.ac.rs.uks.model.Repo;

import java.util.List;

public class LabelMapper {

    public static LabelDTO toDTO(Label label) {
        return LabelDTO.builder()
                .id(label.getId())
                .color(label.getColor())
                .name(label.getName())
                .description(label.getDescription())
                .repoId(label.getRepository() != null ? label.getRepository().getId() : null)
                .build();
    }

    public static List<LabelDTO> toDTOs(List<Label> labels) {
        return labels.stream().map(LabelMapper::toDTO).toList();
    }

    public static Label fromDTO(LabelRequest label, Repo repo) {
        return Label.builder()
                .color(label.getColor())
                .name(label.getName())
                .description(label.getDescription())
                .repository(repo)
                .build();
    }
}
