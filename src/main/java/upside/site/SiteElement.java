package upside.site;

import upside.utils.Utils;

public interface SiteElement {
    boolean equals(Object o);
    int hashCode();
    boolean deepEquals(SiteElement element);
}

abstract class AbstractSiteElement implements SiteElement {

    final boolean maybeEquals(Object o) {
        return o != null && o.getClass().equals(getClass());
    }

    boolean eq(Object o1, Object o2) {
        return Utils.eq(o1, o2);
    }

}