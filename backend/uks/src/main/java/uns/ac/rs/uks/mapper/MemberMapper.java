package uns.ac.rs.uks.mapper;

import uns.ac.rs.uks.dto.response.RepoMemberDTO;
import uns.ac.rs.uks.model.*;

import java.util.List;

public class MemberMapper {

    public static RepoMemberDTO toRepoMemberDTO(Member member) {
        return (member == null) ? null :
                RepoMemberDTO.builder()
                        .id(member.getUser().getId())
                        .username(member.getUser().getCustomUsername())
                        .firstName(member.getUser().getFirstName())
                        .lastName(member.getUser().getLastName())
                        .email(member.getUser().getEmail())
                        .createdAt(member.getUser().getCreatedAt())
                        .repoId(member.getRepository().getId())
                        .repoName(member.getRepository().getName())
                        .repositoryRole(member.getRepositoryRole())
                        .inviteStatus(member.getInviteStatus())
                        .build();
    }

    public static List<RepoMemberDTO> toRepoMemberDTOs(List<Member> members) {
        return members.stream().map(MemberMapper::toRepoMemberDTO).toList();
    }
}
