package com.google.code.morphia.mapping;


import com.google.code.morphia.DatastoreImpl;
import com.google.code.morphia.Key;
import com.google.code.morphia.annotations.Reference;
import com.google.code.morphia.logging.Logr;
import com.google.code.morphia.logging.MorphiaLoggerFactory;
import com.google.code.morphia.mapping.cache.EntityCache;
import com.google.code.morphia.mapping.lazy.LazyFeatureDependencies;
import com.google.code.morphia.mapping.lazy.proxy.ProxiedEntityReference;
import com.google.code.morphia.mapping.lazy.proxy.ProxiedEntityReferenceList;
import com.google.code.morphia.mapping.lazy.proxy.ProxiedEntityReferenceMap;
import com.google.code.morphia.mapping.lazy.proxy.ProxyHelper;
import com.google.code.morphia.utils.IterHelper;
import com.google.code.morphia.utils.ReflectionUtils;
import com.google.code.morphia.utils.IterHelper.IterCallback;
import com.google.code.morphia.utils.IterHelper.MapIterCallback;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.DBRef;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


@SuppressWarnings({"unchecked", "rawtypes"})
class ReferenceMapper implements CustomMapper {
    public static final Logr LOG = MorphiaLoggerFactory.get(ReferenceMapper.class);

    public void toDBObject(final Object entity, final MappedField mf, final DBObject dbObject, final Map<Object, DBObject> involvedObjects,
                           final Mapper mapper) {
        final String name = mf.getNameToStore();

        final Object fieldValue = mf.getFieldValue(entity);

        if (fieldValue == null && !mapper.getOptions().isStoreNulls()) {
            return;
        }

        if (mf.isMap()) {
            writeMap(mf, dbObject, name, fieldValue, mapper);
        } else if (mf.isMultipleValues()) {
            writeCollection(mf, dbObject, name, fieldValue, mapper);
        } else {
            writeSingle(dbObject, name, fieldValue, mapper);
        }

    }

    private void writeSingle(final DBObject dbObject, final String name, final Object fieldValue, final Mapper mapper) {
        if (fieldValue == null) {
            if (mapper.getOptions().isStoreNulls()) {
                dbObject.put(name, null);
            }
        }

        dbObject.put(name, mapper.keyToRef(getKey(fieldValue, mapper)));
    }

    private void writeCollection(final MappedField mf, final DBObject dbObject, final String name, final Object fieldValue,
                                 final Mapper mapper) {
        if (fieldValue != null) {
            final List values = new ArrayList();

            if (ProxyHelper.isProxy(fieldValue) && ProxyHelper.isUnFetched(fieldValue)) {
                final ProxiedEntityReferenceList p = (ProxiedEntityReferenceList) fieldValue;
                final List<Key<?>> getKeysAsList = p.__getKeysAsList();
                for (final Key<?> key : getKeysAsList) {
                    addValue(values, key, mapper);
                }
            } else {

                if (mf.getType().isArray()) {
                    for (final Object o : (Object[]) fieldValue) {
                        addValue(values, o, mapper);
                    }
                } else {
                    for (final Object o : (Iterable) fieldValue) {
                        addValue(values, o, mapper);
                    }
                }
            }
            if (!values.isEmpty() || mapper.getOptions().isStoreEmpties()) {
                dbObject.put(name, values);
            }
        }
    }

    private void addValue(final List values, final Object o, final Mapper mapper) {
        if (o == null && mapper.getOptions().isStoreNulls()) {
            values.add(null);
            return;
        }

        if (o instanceof Key) {
            values.add(mapper.keyToRef((Key) o));
        } else {
            values.add(mapper.keyToRef(getKey(o, mapper)));
        }
    }

    private void writeMap(final MappedField mf, final DBObject dbObject, final String name, final Object fieldValue, final Mapper mapper) {
        final Map<Object, Object> map = (Map<Object, Object>) fieldValue;
        if ((map != null)) {
            final Map values = mapper.getOptions().getObjectFactory().createMap(mf);

            if (ProxyHelper.isProxy(map) && ProxyHelper.isUnFetched(map)) {
                final ProxiedEntityReferenceMap proxy = (ProxiedEntityReferenceMap) map;

                final Map<Object, Key<?>> refMap = proxy.__getReferenceMap();
                for (final Map.Entry<Object, Key<?>> entry : refMap.entrySet()) {
                    final Object key = entry.getKey();
                    values.put(key, mapper.keyToRef(entry.getValue()));
                }
            } else {
                for (final Map.Entry<Object, Object> entry : map.entrySet()) {
                    final String strKey = mapper.getConverters().encode(entry.getKey()).toString();
                    values.put(strKey, mapper.keyToRef(getKey(entry.getValue(), mapper)));
                }
            }
            if (!values.isEmpty() || mapper.getOptions().isStoreEmpties()) {
                dbObject.put(name, values);
            }
        }
    }

    private Key<?> getKey(final Object entity, final Mapper mapper) {
        try {
            if (entity instanceof ProxiedEntityReference) {
                final ProxiedEntityReference proxy = (ProxiedEntityReference) entity;
                return proxy.__getKey();
            }
            final MappedClass mappedClass = mapper.getMappedClass(entity);
            final Object id = mappedClass.getIdField().get(entity);
            if (id == null) {
                throw new MappingException("@Id field cannot be null!");
            }
            return new Key(mappedClass.getCollectionName(), id);
        } catch (IllegalAccessException iae) {
            throw new RuntimeException(iae);
        }
    }

    /**
     * @deprecated use void fromDBObject(final DBObject dbObject, final MappedField mf, final Object entity, EntityCache cache) instead.
     */
    @Deprecated
    void fromDBObject(final DBObject dbObject, final MappedField mf, final Object entity, final Mapper mapper) {
        fromDBObject(dbObject, mf, entity, mapper.createEntityCache(), mapper);
    }

    public void fromDBObject(final DBObject dbObject, final MappedField mf, final Object entity, final EntityCache cache,
                             final Mapper mapper) {
        final Class fieldType = mf.getType();

        final Reference refAnn = mf.getAnnotation(Reference.class);
        if (mf.isMap()) {
            readMap(dbObject, mf, entity, refAnn, cache, mapper);
        } else if (mf.isMultipleValues()) {
            readCollection(dbObject, mf, entity, refAnn, cache, mapper);
        } else {
            readSingle(dbObject, mf, entity, fieldType, refAnn, cache, mapper);
        }

    }

    private void readSingle(final DBObject dbObject, final MappedField mf, final Object entity, final Class fieldType,
                            final Reference refAnn,
                            final EntityCache cache, final Mapper mapper) {

        final DBRef dbRef = (DBRef) mf.getDbObjectValue(dbObject);
        if (dbRef != null) {
            Object resolvedObject = null;
            if (refAnn.lazy() && LazyFeatureDependencies.assertDependencyFullFilled()) {
                if (exists(fieldType, dbRef, cache, mapper)) {
                    resolvedObject = createOrReuseProxy(fieldType, dbRef, cache, mapper);
                } else {
                    if (!refAnn.ignoreMissing()) {
                        throw new MappingException("The reference(" + dbRef.toString() + ") could not be fetched for " + mf.getFullName());
                    }
                }
            } else {
                resolvedObject = resolveObject(dbRef, mf, cache, mapper);
            }

            if (resolvedObject != null) {
                mf.setFieldValue(entity, resolvedObject);
            }

        }
    }

    private void readCollection(final DBObject dbObject, final MappedField mf, final Object entity, final Reference refAnn,
                                final EntityCache cache, final Mapper mapper) {
        // multiple references in a List
        final Class referenceObjClass = mf.getSubClass();
        Collection references = mf.isSet() ? mapper.getOptions().getObjectFactory().createSet(mf)
                                           : mapper.getOptions().getObjectFactory().createList(mf);

        if (refAnn.lazy() && LazyFeatureDependencies.assertDependencyFullFilled()) {
            final Object dbVal = mf.getDbObjectValue(dbObject);
            if (dbVal != null) {
                references = mapper.getProxyFactory()
                                   .createListProxy(references, referenceObjClass, refAnn.ignoreMissing(), mapper.getDatastoreProvider());
                final ProxiedEntityReferenceList referencesAsProxy = (ProxiedEntityReferenceList) references;

                if (dbVal instanceof List) {
                    final List<DBRef> refList = (List) dbVal;
                    final DatastoreImpl dsi = (DatastoreImpl) mapper.getDatastoreProvider().get();
                    final List<Key<Object>> keys = dsi.getKeysByRefs(refList);

                    if (keys.size() != refList.size()) {
                        final String msg = "Some of the references could not be fetched for " + mf.getFullName() + ". " + refList + " != "
                                           + keys;
                        if (!refAnn.ignoreMissing()) {
                            throw new MappingException(msg);
                        } else {
                            LOG.warning(msg);
                        }
                    }

                    referencesAsProxy.__addAll(keys);
                } else {
                    final DBRef dbRef = (DBRef) dbVal;
                    if (!exists(mf.getSubClass(), dbRef, cache, mapper)) {
                        final String msg = "The reference(" + dbRef.toString() + ") could not be fetched for " + mf.getFullName();
                        if (!refAnn.ignoreMissing()) {
                            throw new MappingException(msg);
                        } else {
                            LOG.warning(msg);
                        }
                    } else {
                        referencesAsProxy.__add(mapper.refToKey(dbRef));
                    }
                }
            }
        } else {
            final Object dbVal = mf.getDbObjectValue(dbObject);
            final Collection refs = references;
            new IterHelper<String, Object>().loopOrSingle(dbVal, new IterCallback<Object>() {
                @Override
                public void eval(final Object val) {
                    final DBRef dbRef = (DBRef) val;
                    final Object ent = resolveObject(dbRef, mf, cache, mapper);
                    if (ent == null) {
                        LOG.warning("Null reference found when retrieving value for " + mf.getFullName());
                    } else {
                        refs.add(ent);
                    }
                }
            });
        }

        if (mf.getType().isArray()) {
            mf.setFieldValue(entity, ReflectionUtils.convertToArray(mf.getSubClass(), ReflectionUtils.iterToList(references)));
        } else {
            mf.setFieldValue(entity, references);
        }
    }

    boolean exists(final Class c, final DBRef dbRef, final EntityCache cache, final Mapper mapper) {
        final Key key = mapper.refToKey(dbRef);
        final Boolean cached = cache.exists(key);
        if (cached != null) {
            return cached;
        }

        final DatastoreImpl dsi = (DatastoreImpl) mapper.getDatastoreProvider().get();

        final DBCollection dbColl = dsi.getCollection(c);
        if (!dbColl.getName().equals(dbRef.getCollectionName())) {
            LOG.warning("Class " + c.getName() + " is stored in the '" + dbColl.getName()
                        + "' collection but a reference was found for this type to another collection, '" + dbRef.getCollectionName()
                        + "'. The reference will be loaded using the class anyway. " + dbRef);
        }
        final boolean exists = (dsi.find(dbRef.getCollectionName(), c).disableValidation().filter("_id", dbRef.getId()).asKeyList().size() == 1);
        cache.notifyExists(key, exists);
        return exists;
    }

    Object resolveObject(final DBRef dbRef, final MappedField mf, final EntityCache cache, final Mapper mapper) {
        if (dbRef == null) {
            return null;
        }

        final Key key = mapper.createKey(mf.isSingleValue() ? mf.getType() : mf.getSubClass(), dbRef.getId());

        final Object cached = cache.getEntity(key);
        if (cached != null) {
            return cached;
        }

        //TODO: if _db is null, set it?
//        final DBObject refDbObject = dbRef.fetch();
//
//        if (refDbObject != null) {
//            Object refObj = mapper.getOptions().getObjectFactory().createInstance(mapper, mf, refDbObject);
//            refObj = mapper.fromDb(refDbObject, refObj, cache);
//            cache.putEntity(key, refObj);
//            return refObj;
//        }

        final boolean ignoreMissing = mf.getAnnotation(Reference.class) != null && mf.getAnnotation(Reference.class).ignoreMissing();
        if (!ignoreMissing) {
            throw new MappingException("The reference(" + dbRef.toString() + ") could not be fetched for " + mf.getFullName());
        } else {
            return null;
        }
    }

    private void readMap(final DBObject dbObject, final MappedField mf, final Object entity, final Reference refAnn,
                         final EntityCache cache,
                         final Mapper mapper) {
        final Class referenceObjClass = mf.getSubClass();
        Map m = mapper.getOptions().getObjectFactory().createMap(mf);

        final DBObject dbVal = (DBObject) mf.getDbObjectValue(dbObject);
        if (dbVal != null) {
            if (refAnn.lazy() && LazyFeatureDependencies.assertDependencyFullFilled()) {
                // replace map by proxy to it.
                m = mapper.getProxyFactory().createMapProxy(m, referenceObjClass, refAnn.ignoreMissing(), mapper.getDatastoreProvider());
            }

            final Map map = m;
            new IterHelper<Object, Object>().loopMap(dbVal, new MapIterCallback<Object, Object>() {
                @Override
                public void eval(final Object key, final Object val) {
                    final DBRef dbRef = (DBRef) val;

                    final Object objKey = mapper.getConverters().decode(mf.getMapKeyClass(), key);

                    if (refAnn.lazy() && LazyFeatureDependencies.assertDependencyFullFilled()) {
                        final ProxiedEntityReferenceMap proxiedMap = (ProxiedEntityReferenceMap) map;
                        proxiedMap.__put(objKey, mapper.refToKey(dbRef));
                    } else {
                        final Object resolvedObject = resolveObject(dbRef, mf, cache, mapper);
                        map.put(objKey, resolvedObject);
                    }
                }
            });
        }
        mf.setFieldValue(entity, m);
    }

    private Object createOrReuseProxy(final Class referenceObjClass, final DBRef dbRef, final EntityCache cache, final Mapper mapper) {
        final Key key = mapper.refToKey(dbRef);
        final Object proxyAlreadyCreated = cache.getProxy(key);
        if (proxyAlreadyCreated != null) {
            return proxyAlreadyCreated;
        }
        final Object newProxy = mapper.getProxyFactory().createProxy(referenceObjClass, key, mapper.getDatastoreProvider());
        cache.putProxy(key, newProxy);
        return newProxy;
    }
}
