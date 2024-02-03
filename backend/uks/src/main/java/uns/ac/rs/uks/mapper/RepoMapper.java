package uns.ac.rs.uks.mapper;

import uns.ac.rs.uks.dto.request.RegistrationRequest;
import uns.ac.rs.uks.dto.request.RepoRequest;
import uns.ac.rs.uks.dto.response.RepoBasicInfoDTO;
import uns.ac.rs.uks.model.Repo;
import uns.ac.rs.uks.model.User;

import java.util.List;
import java.util.UUID;

public class RepoMapper {

    public static RepoBasicInfoDTO toDTO(Repo repo){
        return repo == null ? null :
                RepoBasicInfoDTO.builder()
                        .id(repo.getId())
                        .name(repo.getName())
                        .isPublic(repo.getIsPublic())
                        .owner(UserMapper.toDTO(repo.getOwner()))
                        .forkCount(repo.getForkChildren() != null ? repo.getForkChildren().size() : 0)
                        .starCount(repo.getStaredBy() != null ? repo.getStaredBy().size() : 0)
                        .watchCount(repo.getWatchers() != null ? repo.getWatchers().size() : 0)
                        .defaultBranch(repo.getDefaultBranch() != null ? repo.getDefaultBranch().getId() : 0)
                        .build();
    }

    public static List<RepoBasicInfoDTO> toDTOs(List<Repo> repos){
        return repos.stream().map(RepoMapper::toDTO).toList();
    }

    public static Repo toRepoFromRequest(RepoRequest request, User user){
        return request == null ? null :
                Repo.builder()
                        .id(UUID.randomUUID())
                        .name(request.getName())
                        .owner(user)
                        .isPublic(request.getIsPublic())
                        .build();
    }
}
