package se.ifmo.system.collection.util;

import lombok.Getter;

@Getter
public class Indexable {
    protected static int globalId = 0;
    protected int id;
}
