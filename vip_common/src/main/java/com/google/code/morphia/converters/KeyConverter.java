package com.google.code.morphia.converters;


import com.google.code.morphia.Key;
import com.google.code.morphia.mapping.MappedField;
import com.mongodb.DBRef;


/**
 * @author Uwe Schaefer, (us@thomas-daily.de)
 * @author scotthernandez
 */
@SuppressWarnings({"rawtypes" })
public class KeyConverter extends TypeConverter {

  public KeyConverter() {
    super(Key.class);
  }

  @Override
  public Object decode(final Class targetClass, final Object o, final MappedField optionalExtraInfo) {
    if (o == null) {
      return null;
    }
    if (!(o instanceof DBRef)) {
      throw new ConverterException(String.format("cannot convert %s to Key because it isn't a DBRef", o.toString()));
    }

    return getMapper().refToKey((DBRef) o);
  }

  @Override
  public Object encode(final Object t, final MappedField optionalExtraInfo) {
    if (t == null) {
      return null;
    }
    return getMapper().keyToRef((Key) t);
  }

}
