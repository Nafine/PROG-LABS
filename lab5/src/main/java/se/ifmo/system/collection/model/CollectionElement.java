package se.ifmo.system.collection.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import se.ifmo.system.collection.util.IdGenerator;
import se.ifmo.system.collection.util.Validatable;

import java.io.IOException;
import java.io.Serializable;

@Getter
@Setter
@ToString
public abstract class CollectionElement implements Comparable<CollectionElement>, Serializable, Validatable {
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


    @Override
    public int compareTo(@NonNull CollectionElement o) {
        if (this.getClass() != o.getClass()) throw new ClassCastException();
        if (this.equals(o)) return 0;

        return id < o.id ? -1 : 1;
    }
}