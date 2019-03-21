/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gopivotal.cla.model;

import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;

import org.junit.Test;

import com.gopivotal.cla.testutil.ToStringTestUtils;

public abstract class AbstractModelTest<T> {

    @Test
    public final void testState() {
        assertState(getInstance());
    }

    @Test
    public final void testToString() {
        assertToString(new ToStringTestUtils(getInstance()));
    }

    @Test
    public final void hasEmptyConstructor() {
        try {
            getInstance().getClass().getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            fail("No empty constructor");
        }
    }

    protected final T setId(T instance, Integer id) {
        try {
            Field field = getField(instance.getClass(), "id");
            AccessController.doPrivileged(new SetAccessiblePrivilegedAction(field));
            field.set(instance, id);

            return instance;
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to set id", e);
        }
    }

    protected abstract T getInstance();

    protected abstract void assertState(T instance);

    protected abstract void assertToString(ToStringTestUtils instance);

    private Field getField(Class<?> klass, String name) {
        for (Field field : klass.getDeclaredFields()) {
            if (name.equals(field.getName())) {
                return field;
            }
        }

        return getField(klass.getSuperclass(), name);
    }

    private static final class SetAccessiblePrivilegedAction implements PrivilegedAction<Void> {

        private final Field field;

        private SetAccessiblePrivilegedAction(Field field) {
            this.field = field;
        }

        @Override
        public Void run() {
            this.field.setAccessible(true);
            return null;
        }

    }

}
