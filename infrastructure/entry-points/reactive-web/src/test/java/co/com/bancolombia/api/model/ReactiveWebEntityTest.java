package co.com.bancolombia.api.model;

import co.com.bancolombia.api.model.utils.IgnoreLocalDateTimeFilter;
import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import java.util.List;
import org.junit.jupiter.api.Test;

class ReactiveWebEntityTest {

    private static final String POJO_PACKAGE = "co.com.bancolombia.api.request";

    @Test
    void testPojoStructureAndBehavior() {
        Validator validator = ValidatorBuilder.create()
                .with(new SetterMustExistRule(), new GetterMustExistRule())
                .with(new SetterTester(), new GetterTester())
                .build();

        List<PojoClass> pojoClasses = PojoClassFactory.getPojoClassesRecursively(POJO_PACKAGE,
                        new IgnoreLocalDateTimeFilter())
                .stream()
                .filter(pojoClass -> !pojoClass.getName().contains("Builder")
                        && !pojoClass.getClazz().isRecord()
                )
                .toList();

        // Validar las clases POJO filtradas
        validator.validate(pojoClasses);
    }

}