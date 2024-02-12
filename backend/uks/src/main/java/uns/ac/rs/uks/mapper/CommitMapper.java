package uns.ac.rs.uks.mapper;

import uns.ac.rs.uks.dto.request.CommitRequest;
import uns.ac.rs.uks.dto.response.CommitDTO;
import uns.ac.rs.uks.model.Branch;
import uns.ac.rs.uks.model.Comment;
import uns.ac.rs.uks.model.Commit;
import uns.ac.rs.uks.model.User;

import java.util.ArrayList;
import java.util.List;

public class CommitMapper {
    public static CommitDTO toDTO(Commit commit){
        return CommitDTO.builder()
                .id(commit.getId())
                .message(commit.getMessage())
                .description(commit.getDescription())
                .committedAt(commit.getCommittedAt())
                .committer(commit.getCommitter() != null ? UserMapper.toDTO(commit.getCommitter()) : null)
                .parentCommit(commit.getParentCommit() != null ? CommitMapper.toDTO(commit.getParentCommit()) : null)
                .branches(commit.getBranches() != null ? BranchMapper.toDTOs(commit.getBranches()) : new ArrayList<>())
                //.comments(commit.getComments())
                .build();
    }

    public static List<CommitDTO> toDTOs(List<Commit> commits) {
        return commits.stream().map(CommitMapper::toDTO).toList();
    }

    public static Commit newCommitFromDTO(CommitRequest commit, Branch branch, Commit parentCommit, User user) {
        ArrayList<Branch> branches = new ArrayList<>();
        branches.add(branch);

        return Commit.builder()
                .message(commit.getMessage())
                .description(commit.getDescription())
                .committedAt(commit.getCommittedAt())
                .branches(branches)
                .parentCommit(parentCommit)
                .committer(user)
                .build();
    }
}
