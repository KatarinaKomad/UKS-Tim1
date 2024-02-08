package uns.ac.rs.uks.dto.request.search;

import lombok.Data;

import java.util.Date;

@Data
public class RepoSearchDTO {
    private String repoName;
    private String ownerName;
    private Boolean inName;
    private Boolean inDescription;
    private Boolean forksIncluded;
    private Boolean forksOnly;
    private Long numOfForks;
    private Long numOfWatchers;
    private Long numOfStars;
    private Boolean isPublic;
    private Boolean isPrivate;
    private Date created;
}
