package se.ifmo.shared.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import se.ifmo.server.collection.util.IdGenerator;
import se.ifmo.server.collection.util.Validatable;
import se.ifmo.shared.exceptions.InvalidDataException;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;

/**
 * Abstract class for representing collection element.
 */
@Getter
@Setter
@ToString
public abstract class CollectionElement implements Serializable, Validatable {
    private static final HashSet<Long> usedIds = new HashSet<>();
    protected long id;
    protected long ownerId;

    {
        try {
            id = IdGenerator.getInstance().generateId();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            id = -1;
        }
    }

    public void setId(long id) throws InvalidDataException {
        if (this.id != id && usedIds.contains(id)) {
            throw new InvalidDataException(id, "Id must be unique");
        }
        this.id = id;
        usedIds.add(id);
    }

}