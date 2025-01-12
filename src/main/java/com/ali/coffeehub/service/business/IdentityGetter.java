package com.ali.coffeehub.service.business;

import java.io.Serializable;

public interface IdentityGetter<ID extends Serializable> extends Serializable {
    ID getId();
}
