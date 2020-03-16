package com.google.code.morphia.query;

import com.google.code.morphia.Datastore;
import com.mongodb.DBCollection;

/**
 * An abstract implementation of {@link QueryFactory}.
 */
public abstract class AbstractQueryFactory implements QueryFactory {

  /**
   * @see #createQuery(Datastore, DBCollection, Class, com.mongodb.DBObject)
   */
  public <T> Query<T> createQuery(final Datastore datastore, final DBCollection collection, final Class<T> type) {
    return createQuery(datastore, collection, type, null);
  }
}
