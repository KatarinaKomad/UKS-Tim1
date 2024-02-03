package uns.ac.rs.uks.mapper;

import uns.ac.rs.uks.dto.response.BranchBasicInfoDTO;
import uns.ac.rs.uks.dto.response.RepoBasicInfoDTO;
import uns.ac.rs.uks.model.Branch;
import uns.ac.rs.uks.model.Repo;

import java.util.List;

public class BranchMapper {

    public static BranchBasicInfoDTO toDTO(Branch branch){
        return branch == null ? null :
                BranchBasicInfoDTO.builder()
                        .id(branch.getId())
                        .name(branch.getName())
                        .build();
    }

    public static List<BranchBasicInfoDTO> toDTOs(List<Branch> branches){
        return branches.stream().map(BranchMapper::toDTO).toList();
    }
}
