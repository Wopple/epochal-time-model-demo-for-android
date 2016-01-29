package com.iheartradio.hack.epoch;

import org.junit.Test;

import static org.junit.Assert.*;

public class EpochCursorTest {
    @Test
    public void testSomething() throws Exception {
        Parent parent = new Parent(new Child(1), 2);
        Identity<Child> childCursor = new ChildCursor(new EpochIdentity<>(parent));

        assertEquals(1, childCursor.get().childValue);
        assertEquals(3, childCursor.update(child -> new Child(child.childValue + 2)).childValue);
        assertEquals(3, childCursor.get().childValue);
    }

    private static class Parent {
        public final Child child;
        public final int parentValue;

        private Parent(final Child child, final int parentValue) {
            this.child = child;
            this.parentValue = parentValue;
        }
    }

    private static class Child {
        public final int childValue;

        private Child(final int childValue) {
            this.childValue = childValue;
        }
    }

    private static class ChildCursor extends EpochCursor<Parent, Child> {
        public ChildCursor(final Identity<Parent> identity) {
            super(identity);
        }

        @Override
        protected Child get(final Parent parent) {
            return parent.child;
        }

        @Override
        protected Parent update(final Parent rootValue, final Child child) {
            return new Parent(child, rootValue.parentValue);
        }
    }
}