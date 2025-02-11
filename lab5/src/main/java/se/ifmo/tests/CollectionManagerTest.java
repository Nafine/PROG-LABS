package se.ifmo.tests;

import se.ifmo.system.collection.CollectionManager;

public class CollectionManagerTest {
    public static void main(String[] args) {
        CollectionManager.getInstance().load();

        System.out.println(CollectionManager.getInstance().getCollection());
    }
}
