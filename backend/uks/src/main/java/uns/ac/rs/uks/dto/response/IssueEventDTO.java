package uns.ac.rs.uks.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import uns.ac.rs.uks.model.Issue;
import uns.ac.rs.uks.model.IssueEventType;
import uns.ac.rs.uks.model.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueEventDTO {
    private Long id;
    private UserDTO author;
    private String value;
    private IssueEventType type;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
}
