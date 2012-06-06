package dungeongine.bootstrap.base;

import com.google.common.collect.Maps;

import java.util.Map;

public abstract class Base {
    protected static <T extends Base> Declaration<T> declare(Class<T> clazz) throws IllegalStateException {
        return store(new Declaration<>(clazz));
    }

    static final Map<Class<? extends Base>, Declaration<? extends Base>> declarations = Maps.newHashMap();
    private final Map<String, Object> data = Maps.newHashMap();

    private synchronized static <T extends Base> Declaration<T> store(Declaration<T> declaration) throws IllegalStateException {
        if (declarations.containsKey(declaration.clazz))
            throw new IllegalStateException(String.format("Class '%s' already has a declaration!", declaration.clazz.getName()));
        declarations.put(declaration.clazz, declaration);
        return declaration;
    }

    private boolean immutable;

    public Object put(String name, Object newValue) throws IllegalStateException, IllegalArgumentException, ClassCastException {
        if (immutable)
            throw new IllegalStateException("Object is immutable.");
        if (!declarations.containsKey(getClass()) || !declarations.get(getClass()).fields.containsKey(name))
            throw new IllegalArgumentException(String.format("Unknown field '%s'.", name));
        if (!declarations.get(getClass()).fields.get(name).type.isInstance(newValue))
            throw new ClassCastException();
        try {
            return data.put(name, newValue);
        } finally {
            notifyUpdate(name, newValue);
        }
    }

    public Object get(String name) throws IllegalArgumentException {
        if (!declarations.containsKey(getClass()) || !declarations.get(getClass()).fields.containsKey(name))
            throw new IllegalArgumentException(String.format("Unknown field '%s'.", name));
        if (Base.class.isAssignableFrom(declarations.get(getClass()).fields.get(name).type))
            return data.get(name) == null ? null : ((Base) data.get(name)).forceImmutable();
        return data.get(name);
    }

    private void notifyUpdate(String field, Object value) {
        Declaration<?> decl = declarations.get(getClass());
        Declaration.FieldDeclaration<?> fieldDecl = decl.fields.get(field);
        if (fieldDecl.stored)
            decl.updateStorage(this, field, value);
        if (fieldDecl.networked)
            updateNetwork();
    }

    private void updateNetwork() {

    }

    private Base forceImmutable() {
        immutable = true;
        return this;
    }

    public Object getIdentifier() {
        return get(declarations.get(getClass()).firstField.name);
    }
}
