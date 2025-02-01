package se.ifmo.system.collection.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import se.ifmo.system.collection.enums.OrganizationType;
import se.ifmo.system.collection.util.Indexable;
import se.ifmo.system.collection.util.Validatable;


@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class Organization extends Indexable implements Validatable {
    private @NonNull String name;
    private int employeesCount;
    private OrganizationType type;

    @Override
    public boolean validate() {
        return !name.isEmpty() && employeesCount > 0;
    }
}
