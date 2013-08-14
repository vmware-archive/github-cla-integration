/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gopivotal.cla.github;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.gopivotal.cla.util.Sets;

@SuppressWarnings("rawtypes")
abstract class AbstractCollection<T> extends AbstractGitHubType<Set> implements Set<T> {

    private volatile Set<T> delegate;

    protected AbstractCollection(String url) {
        super(url, Set.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    final void initialize(Set raw) {
        Set<T> delegate = Sets.asSet();

        for (Map rawItem : (Set<Map>) raw) {
            delegate.add(initialize(rawItem));
        }

        this.delegate = delegate;
    }

    abstract T initialize(Map raw);

    @Override
    public final int size() {
        return this.delegate.size();
    }

    @Override
    public final boolean isEmpty() {
        return this.delegate.isEmpty();
    }

    @Override
    public final boolean contains(Object o) {
        return this.delegate.contains(o);
    }

    @Override
    public final Iterator<T> iterator() {
        return this.delegate.iterator();
    }

    @Override
    public final Object[] toArray() {
        return this.delegate.toArray();
    }

    @Override
    public final <U> U[] toArray(U[] a) {
        return this.delegate.toArray(a);
    }

    @Override
    public final boolean add(T e) {
        return this.delegate.add(e);
    }

    @Override
    public final boolean remove(Object o) {
        return this.delegate.remove(o);
    }

    @Override
    public final boolean containsAll(Collection<?> c) {
        return this.delegate.containsAll(c);
    }

    @Override
    public final boolean addAll(Collection<? extends T> c) {
        return this.delegate.addAll(c);
    }

    @Override
    public final boolean retainAll(Collection<?> c) {
        return this.delegate.retainAll(c);
    }

    @Override
    public final boolean removeAll(Collection<?> c) {
        return this.delegate.removeAll(c);
    }

    @Override
    public final void clear() {
        this.delegate.clear();
    }

    @Override
    public final boolean equals(Object o) {
        return this.delegate.equals(o);
    }

    @Override
    public final int hashCode() {
        return this.delegate.hashCode();
    }

}
