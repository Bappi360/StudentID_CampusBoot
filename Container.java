import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class Container {
    private final Map<String, Object> beans = new LinkedHashMap<>();

    public void scan(Class<?>... classes) {
        for (Class<?> cls : classes) {
            Component c = cls.getAnnotation(Component.class);
            if (c == null) {
                System.out.println("[Container] Skipped: " + cls.getSimpleName() + " (no @Component)");
                continue;
            }
            try {
                String name = c.value();
                if (name.isEmpty()) {
                    String s = cls.getSimpleName();
                    name = Character.toLowerCase(s.charAt(0)) + s.substring(1);
                }
                Object instance = cls.getDeclaredConstructor().newInstance();
                beans.put(name, instance);
                System.out.println("[Container] Registered: " + name + " (" + cls.getSimpleName() + ")");
            } catch (ReflectiveOperationException e) {
                System.out.println("[Container] Skipped: " + cls.getSimpleName() + " (cannot instantiate: " + e + ")");
            }
        }
    }

    public void injectValues(Map<String, String> config) {
        for (Object bean : beans.values()) {
            for (Field f : bean.getClass().getDeclaredFields()) {
                Value v = f.getAnnotation(Value.class);
                if (v == null) continue;
                String key = v.value();
                String raw = config.get(key);
                if (raw == null) {
                    System.out.println("[Container] Skipped: " + key + " (no config value)");
                    continue;
                }
                try {
                    f.setAccessible(true);
                    Class<?> t = f.getType();
                    if (t == int.class || t == Integer.class) {
                        f.set(bean, Integer.parseInt(raw.trim()));
                    } else if (t == String.class) {
                        f.set(bean, raw);
                    } else {
                        System.out.println("[Container] Skipped: " + key + " (unsupported type " + t.getSimpleName() + ")");
                        continue;
                    }
                    System.out.println("[Container] Injected: " + key + " -> "
                            + bean.getClass().getSimpleName() + "." + f.getName());
                } catch (IllegalAccessException | NumberFormatException e) {
                    System.out.println("[Container] Skipped: " + key + " (" + e.getMessage() + ")");
                }
            }
        }
    }

    public Object getBean(String name) {
        return beans.get(name);
    }

    Iterable<Object> allBeans() {
        return beans.values();
    }
}
