package uns.ac.rs.uks.mapper;

import uns.ac.rs.uks.dto.request.RepoRequest;
import uns.ac.rs.uks.dto.response.ForkParentDTO;
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
                        .description(repo.getDescription())
                        .isPublic(repo.getIsPublic())
                        .owner(UserMapper.toDTO(repo.getOwner()))
                        .forkCount(repo.getForkChildren() != null ? repo.getForkChildren().size() : 0)
                        .starCount(repo.getStaredBy() != null ? repo.getStaredBy().size() : 0)
                        .watchCount(repo.getWatchers() != null ? repo.getWatchers().size() : 0)
                        .defaultBranch(repo.getDefaultBranch() != null ? repo.getDefaultBranch().getId() : 0)
                        .forkParent(repo.getForkParent() != null ? toForkParentDTO(repo.getForkParent()): null)
                        .createdAt(repo.getCreatedAt())
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
                        .description(request.getDescription())
                        .owner(user)
                        .isPublic(request.getIsPublic())
                        .build();
    }

    public static Repo map(Repo oldRepo) {
        Repo newRepo = new Repo();
        newRepo.setId(UUID.randomUUID());
        newRepo.setIsPublic(oldRepo.getIsPublic());
        newRepo.setDefaultBranch(oldRepo.getDefaultBranch());
        newRepo.setBranches(oldRepo.getBranches());
        newRepo.setProjects(oldRepo.getProjects());
        return newRepo;
    }

    public static ForkParentDTO toForkParentDTO(Repo parent) {
        return ForkParentDTO.builder()
                .id(parent.getId())
                .isPublic(parent.getIsPublic())
                .name(parent.getName())
                .owner(UserMapper.toDTO(parent.getOwner()))
                .build();
    }
}
