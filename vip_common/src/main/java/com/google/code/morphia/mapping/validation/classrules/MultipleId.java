package com.google.code.morphia.mapping.validation.classrules;


import java.util.List;
import java.util.Set;

import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.mapping.MappedClass;
import com.google.code.morphia.mapping.MappedField;
import com.google.code.morphia.mapping.validation.ClassConstraint;
import com.google.code.morphia.mapping.validation.ConstraintViolation;
import com.google.code.morphia.mapping.validation.ConstraintViolation.Level;


/**
 * @author Uwe Schaefer, (us@thomas-daily.de)
 */
public class MultipleId implements ClassConstraint {

  public void check(final MappedClass mc, final Set<ConstraintViolation> ve) {

    final List<MappedField> idFields = mc.getFieldsAnnotatedWith(Id.class);

    if (idFields.size() > 1) {
      ve.add(new ConstraintViolation(Level.FATAL, mc, getClass(),
        "More than one @" + Id.class.getSimpleName() + " Field found (" + new FieldEnumString(idFields) + ")."));
    }
  }

}
