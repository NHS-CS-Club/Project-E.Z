package net.fabricmc.projectez.util;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ArrayListSet<T> implements Set<T> {

    final List<T> base = new ArrayList<>();

    @Override
    public int size() {
        return base.size();
    }

    @Override
    public boolean isEmpty() {
        return base.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return base.contains(o);
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return base.iterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return base.toArray();
    }

    @Override
    public boolean add(T o) {
        if (base.contains(o)) return false;
        base.add(o);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        return base.remove(o);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean addAll(@NotNull Collection c) {
        boolean changed = false;
        for (Object v : c)
            changed |= add((T) v);
        return changed;
    }

    @Override
    public void clear() {
        base.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ArrayListSet)
            return base.equals(((ArrayListSet<?>)o).base);
        else
            return false;
    }

    @Override
    public int hashCode() {
        return base.hashCode();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean removeAll(@NotNull Collection c) {
        return base.removeAll((Collection<T>) c);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean retainAll(@NotNull Collection c) {
        return base.retainAll((Collection<T>) c);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean containsAll(@NotNull Collection c) {
        return base.containsAll((Collection<T>) c);
    }

    @NotNull
    @SuppressWarnings("unchecked")
    @Override
    public T[] toArray(@NotNull Object[] a) {
        return (T[]) base.toArray(a);
    }
}
