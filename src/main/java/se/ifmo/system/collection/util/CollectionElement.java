package se.ifmo.system.collection.util;

import lombok.NonNull;

import java.io.Serializable;

public abstract class CollectionElement extends Indexable implements Comparable<CollectionElement>, Serializable, Validatable{
    @Override
    public int compareTo(@NonNull CollectionElement o) {
        if (this.getClass() != o.getClass()) throw new ClassCastException();
        if (this.equals(o)) return 0;

        return id < o.id ? -1 : 1;
    }
}