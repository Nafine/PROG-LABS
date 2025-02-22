package se.ifmo.system.collection.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import se.ifmo.system.collection.util.IdGenerator;
import se.ifmo.system.collection.util.Validatable;

import java.io.IOException;
import java.io.Serializable;

/**
 * Abstract class for representing collection element.
 */
@Getter
@Setter
@ToString
public abstract class CollectionElement implements Serializable, Validatable {
    @JsonIgnore
    protected long id;

    {
        try {
            id = IdGenerator.getInstance().generateId();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            id = -1;
        }
    }
}