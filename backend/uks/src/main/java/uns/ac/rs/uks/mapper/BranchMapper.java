package uns.ac.rs.uks.mapper;

import uns.ac.rs.uks.dto.request.BranchRequest;
import uns.ac.rs.uks.dto.response.BranchDTO;
import uns.ac.rs.uks.model.Branch;
import uns.ac.rs.uks.model.Repo;
import uns.ac.rs.uks.model.User;
import java.util.List;

public class BranchMapper {
    public static BranchDTO toDTO(Branch branch) {
        return BranchDTO.builder()
                .id(branch.getId())
                .name(branch.getName())
                .createdAt(branch.getCreatedAt())
                .updatedAt(branch.getUpdatedAt())
                .repoId(branch.getRepository() != null ? branch.getRepository().getId() : null)
                .updatedBy(branch.getUpdatedBy() != null ? UserMapper.toDTO(branch.getUpdatedBy()) : null)
                //.commits(branch.getCommits())
                .build();
    }

    public static List<BranchDTO> toDTOs(List<Branch> branches) {
        return branches.stream().map(BranchMapper::toDTO).toList();
    }

    public static Branch fromDTO(BranchRequest branch, Repo repo, User user) {
        return Branch.builder()
                .name(branch.getName())
                .createdAt(branch.getCreatedAt())
                .updatedAt(branch.getUpdatedAt())
                .repository(repo)
                .updatedBy(user)
                .build();
    }

    public static Branch map(Branch oldBranch, BranchRequest newBranch) {
        oldBranch.setName(newBranch.getName());
        return  oldBranch;
    }
}
