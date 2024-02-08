package uns.ac.rs.uks.dto.request.search;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SearchType {
    // type: or type=
    REPO("repo"),
    ISSUE("issue"),
    PR("pr"),
    USER("user");
    private final String name;

    private SearchType(String s) {
        name = s;
    }

    @JsonCreator
    public static SearchType fromString(String value) {
        for (SearchType type : SearchType.values()) {
            if (type.name.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid SearchType value: " + value);
    }

    @JsonValue
    public String getName() {
        return name;
    }
}
