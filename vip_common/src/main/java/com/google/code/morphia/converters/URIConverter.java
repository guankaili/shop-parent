package com.google.code.morphia.converters;


import com.google.code.morphia.mapping.MappedField;

import java.net.URI;


/**
 * @author scotthernandez
 */
public class URIConverter extends TypeConverter implements SimpleValueConverter {

    public URIConverter() {
        this(URI.class);
    }

    protected URIConverter(final Class clazz) {
        super(clazz);
    }

    @Override
    public String encode(final Object uri, final MappedField optionalExtraInfo) {
        if (uri == null) {
            return null;
        }

        return uri.toString().replace(".", "%46");
    }

    @Override
    public Object decode(final Class targetClass, final Object val, final MappedField optionalExtraInfo) {
        if (val == null) {
            return null;
        }

        return URI.create(val.toString().replace("%46", "."));
    }
}
