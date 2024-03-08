package uns.ac.rs.uks.mapper;

import uns.ac.rs.uks.dto.response.BranchBasicInfoDTO;
import uns.ac.rs.uks.dto.response.BranchDTO;
import uns.ac.rs.uks.model.Branch;

import java.util.List;

public class BranchMapper {

    public static BranchBasicInfoDTO toBasicDTO(Branch branch){
        return branch == null ? null :
                BranchBasicInfoDTO.builder()
                        .id(branch.getId())
                        .name(branch.getName())
                        .build();
    }
    public static BranchDTO toDTO(Branch branch){
        return branch == null ? null :
                BranchDTO.builder()
                        .id(branch.getId())
                        .name(branch.getName())
//                        .code(gitoliteBranch.getCode())
                        .createdAt(branch.getCreatedAt())
                        .updatedAt(branch.getUpdatedAt())
                        .updatedBy(branch.getUpdatedBy() != null ? UserMapper.toDTO(branch.getUpdatedBy()) : null)
                        .repoId(branch.getRepository() != null ? branch.getRepository().getId(): null)
                        .build();
    }
    public static List<BranchDTO> toDTOs(List<Branch> branches) {
        return branches.stream().map(BranchMapper::toDTO).toList();
    }
}
