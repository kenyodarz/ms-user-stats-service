package co.com.bancolombia.dynamodb.model.utils;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.PojoClassFilter;
import com.openpojo.reflection.PojoField;

public class IgnoreLocalDateTimeFilter implements PojoClassFilter {
  @Override
  public boolean include(PojoClass pojoClass) {
    for (PojoField field : pojoClass.getPojoFields()) {
      if (field.getType().equals(java.time.LocalDateTime.class)) {
        return false; // Excluir esta clase del test
      }
    }
    return true;
  }
}

