package net.saga.sync.gameserver.util.jpa;

import javax.persistence.EntityManager;

@FunctionalInterface
public interface Operation<T> {
    T run(EntityManager em);
}
