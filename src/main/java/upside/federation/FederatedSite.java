package upside.federation;

import java.util.HashSet;
import java.util.Set;

public class FederatedSite {
    private String name;
    private String description;
    private Set<FederatedCategory> categories;
    private Set<Include> includes;

    public FederatedSite(
            String name, String description,
            Set<FederatedCategory> categories, Set<Include> includes) {
        this.name = name;
        this.description = description;
        this.categories = new HashSet<FederatedCategory>(categories);
        this.includes = new HashSet<Include>(includes);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Set<FederatedCategory> getCategories() {
        return new HashSet<FederatedCategory>(categories);
    }

    public Set<Include> getIncludes() {
        return new HashSet<Include>(includes);
    }
}
