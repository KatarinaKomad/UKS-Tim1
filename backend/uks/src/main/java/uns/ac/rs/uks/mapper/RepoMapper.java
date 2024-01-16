package uns.ac.rs.uks.mapper;

import uns.ac.rs.uks.dto.response.RepoBasicInfoDTO;
import uns.ac.rs.uks.model.Repo;

import java.util.List;

public class RepoMapper {

    public static RepoBasicInfoDTO toDTO(Repo repo){
        return repo == null ? null :
                RepoBasicInfoDTO.builder()
                        .id(repo.getId())
                        .name(repo.getName())
                        .isPublic(repo.getIsPublic())
                        .owner(UserMapper.toDTO(repo.getOwner()))
                        .forkCount(repo.getForkChildren().size())
                        .starCount(repo.getStaredBy().size())
                        .watchCount(repo.getWatchers().size())
                        .build();
    }

    public static List<RepoBasicInfoDTO> toDTOs(List<Repo> repos){
        return repos.stream().map(RepoMapper::toDTO).toList();
    }
}
