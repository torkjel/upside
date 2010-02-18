package upside.federation;

import java.util.HashSet;
import java.util.Set;

class FederatedCategory {
    private String name;
    private String description;
    private Set<Include> includes;

    public FederatedCategory(String name, String description, Set<Include> includes) {
        this.name = name;
        this.description = description;
        this.includes = new HashSet<Include>(includes);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Set<Include> getIncludes() {
        return new HashSet<Include>(includes);
    }
}
