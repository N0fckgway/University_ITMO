package builder;

import ru.suvorov.utilities.collection.model.Human;

import java.time.ZonedDateTime;

public interface BuilderHuman {
    BuilderHuman setName(String name);
    BuilderHuman setAge(Integer age);
    BuilderHuman setHeight(float height);
    BuilderHuman setBirthday(ZonedDateTime birthday);
    Human getHuman();
}
