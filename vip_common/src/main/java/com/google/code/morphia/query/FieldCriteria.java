package com.google.code.morphia.query;


import com.google.code.morphia.logging.Logr;
import com.google.code.morphia.logging.MorphiaLoggerFactory;
import com.google.code.morphia.mapping.MappedClass;
import com.google.code.morphia.mapping.MappedField;
import com.google.code.morphia.mapping.Mapper;
import com.google.code.morphia.utils.ReflectionUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class FieldCriteria extends AbstractCriteria {
    private static final Logr LOG = MorphiaLoggerFactory.get(FieldCriteria.class);

    private final String field;
    private final FilterOperator operator;
    private final Object value;
    private final boolean not;

    protected FieldCriteria(final QueryImpl<?> query, final String field, final FilterOperator op, final Object value,
                            final boolean validateNames, final boolean validateTypes) {
        this(query, field, op, value, validateNames, validateTypes, false);
    }

    protected FieldCriteria(final QueryImpl<?> query, final String fieldName, final FilterOperator op, final Object value,
                            final boolean validateNames, final boolean validateTypes, final boolean not) {
        //validate might modify prop string to translate java field name to db field name
        final StringBuffer sb = new StringBuffer(fieldName);
        final MappedField mf = Mapper.validate(query.getEntityClass(), query.getDatastore().getMapper(), sb, op, value, validateNames,
                                               validateTypes);

        final Mapper mapper = query.getDatastore().getMapper();

        MappedClass mc = null;
        try {
            if (value != null && !ReflectionUtils.isPropertyType(value.getClass())
                && !ReflectionUtils.implementsInterface(value.getClass(), Iterable.class)) {
                if (mf != null && !mf.isTypeMongoCompatible()) {
                    mc = mapper.getMappedClass((mf.isSingleValue()) ? mf.getType() : mf.getSubClass());
                } else {
                    mc = mapper.getMappedClass(value);
                }
            }
        } catch (Exception e) {
            //Ignore these. It is likely they related to mapping validation that is unimportant for queries (the query will 
            // fail/return-empty anyway)
            LOG.debug("Error during mapping of filter criteria: ", e);
        }

        Object mappedValue = mapper.toMongoObject(mf, mc, value);

        final Class<?> type = (mappedValue == null) ? null : mappedValue.getClass();

        //convert single values into lists for $in/$nin
        if (type != null && (op == FilterOperator.IN || op == FilterOperator.NOT_IN)
            && !type.isArray() && !Iterable.class.isAssignableFrom(type)) {
            mappedValue = Collections.singletonList(mappedValue);
        }

        //TODO: investigate and/or add option to control this.
        if (op == FilterOperator.ELEMENT_MATCH && mappedValue instanceof DBObject) {
            ((DBObject) mappedValue).removeField(Mapper.ID_KEY);
        }

        this.field = sb.toString();
        operator = op;
        if (not) {
            this.value = new BasicDBObject("$not", mappedValue);
        } else {
            this.value = mappedValue;
        }
        this.not = not;
    }

    public String getField() {
        return field;
    }

    public boolean isNot() {
        return not;
    }

    public FilterOperator getOperator() {
        return operator;
    }

    public Object getValue() {
        return value;
    }

    public void addTo(final DBObject obj) {
        if (FilterOperator.EQUAL.equals(operator)) {
            obj.put(field, value); // no operator, prop equals value

        } else {
            final Object object = obj.get(field); // operator within inner object
            Map<String, Object> inner;
            if (!(object instanceof Map)) {
                inner = new HashMap<String, Object>();
                obj.put(field, inner);
            } else {
                inner = (Map<String, Object>) object;
            }

            inner.put(operator.val(), not ? new BasicDBObject("$not", value) : value);
        }
    }

    public String getFieldName() {
        return field;
    }

    @Override
    public String toString() {
        return field + " " + operator.val() + " " + value;
    }
}
