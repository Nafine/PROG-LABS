package se.ifmo.system.collection.model;

import lombok.*;
import se.ifmo.system.collection.enums.OrganizationType;
import se.ifmo.system.collection.util.IdGenerator;
import se.ifmo.system.collection.util.Validatable;
import se.ifmo.system.exceptions.InvalidDataException;

import java.io.IOException;



@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
public class Organization implements Validatable {
    private int id;

    {
        try {
            id = IdGenerator.getInstance().generateId();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            id = -1;
        }
    }

    @Getter private @NonNull String name;
    @Getter private int employeesCount;
    @Getter private OrganizationType type;

    @Override
    public void validate() throws InvalidDataException {
        if (id <= 0) throw new InvalidDataException(this, "Invalid ID");
        if (name.isEmpty()) throw new InvalidDataException(this, "Organization name cannot be empty");
        if (employeesCount <= 0) throw new InvalidDataException(this, "Organization employeesCount must be greater than 0");
    }
}
