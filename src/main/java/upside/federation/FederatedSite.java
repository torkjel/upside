package upside.federation;

import java.util.HashSet;
import java.util.Set;

public class FederatedSite {
    private String name;
    private String description;
    private Set<FederatedCategory> categories = new HashSet<FederatedCategory>();

    public FederatedSite(String name, String description, Set<FederatedCategory> categories) {
        this.name = name;
        this.description = description;
        this.categories = new HashSet<FederatedCategory>(categories);
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
}
