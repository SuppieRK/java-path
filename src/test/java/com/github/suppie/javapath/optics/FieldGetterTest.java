package com.github.suppie.javapath.optics;

import com.github.suppie.javapath.exceptions.PathException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FieldGetterTest {
    private static final String FIELD_NAME = "field";
    private static final String FIELD_VALUE = "fieldValue";
    private static final boolean BOOLEAN_FIELD_VALUE = true;

    private final FieldGetter fieldGetter = new FieldGetter(null, FIELD_NAME);

    @Test
    void testFieldGetter_correctScenarios() {
        PublicStringFieldHolder publicStringFieldHolder = new PublicStringFieldHolder();
        ProtectedPublicStringFieldHolder protectedPublicStringFieldHolder = new ProtectedPublicStringFieldHolder();
        PublicGetterStringFieldHolder publicGetterStringFieldHolder = new PublicGetterStringFieldHolder();
        PublicGetterBooleanFieldHolder publicGetterBooleanFieldHolder = new PublicGetterBooleanFieldHolder();

        assertEquals(
                FIELD_VALUE,
                assertDoesNotThrow(() -> fieldGetter.apply(publicStringFieldHolder), "'public class' -> 'public field' must be accessible"),
                "Accessed value of public field must be equal to the expected"
        );
        assertEquals(
                FIELD_VALUE,
                assertDoesNotThrow(() -> fieldGetter.apply(protectedPublicStringFieldHolder), "'protected class' -> 'public field' must be accessible"),
                "Accessed value of public field inside protected class must be equal to the expected"
        );
        assertEquals(
                FIELD_VALUE,
                assertDoesNotThrow(() -> fieldGetter.apply(publicGetterStringFieldHolder), "'public class' -> 'private field' -> 'public boolean isField' must be accessible"),
                "Accessed value of private boolean field with public getter must be equal to the expected"
        );
        assertEquals(
                BOOLEAN_FIELD_VALUE,
                assertDoesNotThrow(() -> fieldGetter.apply(publicGetterBooleanFieldHolder), "'public class' -> 'private field' -> 'public Object getField' must be accessible"),
                "Accessed value of private field with public getter must be equal to the expected"
        );
    }

    @Test
    void testFieldGetter_differentVisibilityModifiers() {
        PackageDefaultStringFieldHolder packageDefaultStringFieldHolder = new PackageDefaultStringFieldHolder();
        ProtectedStringFieldHolder protectedStringFieldHolder = new ProtectedStringFieldHolder();
        PrivateStringFieldHolder privateStringFieldHolder = new PrivateStringFieldHolder();
        PackageDefaultPublicStringFieldHolder packageDefaultPublicStringFieldHolder = new PackageDefaultPublicStringFieldHolder();
        PackageDefaultPackageDefaultStringFieldHolder packageDefaultPackageDefaultStringFieldHolder = new PackageDefaultPackageDefaultStringFieldHolder();
        PackageDefaultProtectedStringFieldHolder packageDefaultProtectedStringFieldHolder = new PackageDefaultProtectedStringFieldHolder();
        PackageDefaultPrivateStringFieldHolder packageDefaultPrivateStringFieldHolder = new PackageDefaultPrivateStringFieldHolder();
        ProtectedPackageDefaultStringFieldHolder protectedPackageDefaultStringFieldHolder = new ProtectedPackageDefaultStringFieldHolder();
        ProtectedProtectedStringFieldHolder protectedProtectedStringFieldHolder = new ProtectedProtectedStringFieldHolder();
        ProtectedPrivateStringFieldHolder protectedPrivateStringFieldHolder = new ProtectedPrivateStringFieldHolder();
        PrivatePublicStringFieldHolder privatePublicStringFieldHolder = new PrivatePublicStringFieldHolder();
        PrivatePackageDefaultStringFieldHolder privatePackageDefaultStringFieldHolder = new PrivatePackageDefaultStringFieldHolder();
        PrivateProtectedStringFieldHolder privateProtectedStringFieldHolder = new PrivateProtectedStringFieldHolder();
        PrivatePrivateStringFieldHolder privatePrivateStringFieldHolder = new PrivatePrivateStringFieldHolder();
        PublicThrowingGetterStringFieldHolder publicThrowingGetterStringFieldHolder = new PublicThrowingGetterStringFieldHolder();
        PackageDefaultGetterStringFieldHolder packageDefaultGetterStringFieldHolder = new PackageDefaultGetterStringFieldHolder();
        ProtectedGetterStringFieldHolder protectedGetterStringFieldHolder = new ProtectedGetterStringFieldHolder();
        PrivateGetterStringFieldHolder privateGetterStringFieldHolder = new PrivateGetterStringFieldHolder();

        assertThrows(PathException.class, () -> fieldGetter.apply(new Object()), "Missing field must NOT be accessible");
        assertThrows(PathException.class, () -> fieldGetter.apply(packageDefaultStringFieldHolder), "'public class' -> 'default field' must NOT be accessible");
        assertThrows(PathException.class, () -> fieldGetter.apply(protectedStringFieldHolder), "'public class' -> 'protected field' must NOT be accessible");
        assertThrows(PathException.class, () -> fieldGetter.apply(privateStringFieldHolder), "'public class' -> 'private field' must NOT be accessible");
        assertThrows(PathException.class, () -> fieldGetter.apply(packageDefaultPublicStringFieldHolder), "'class' -> 'public field' must NOT be accessible");
        assertThrows(PathException.class, () -> fieldGetter.apply(packageDefaultPackageDefaultStringFieldHolder), "'class' -> 'default field' must NOT be accessible");
        assertThrows(PathException.class, () -> fieldGetter.apply(packageDefaultProtectedStringFieldHolder), "'class' -> 'protected field' must NOT be accessible");
        assertThrows(PathException.class, () -> fieldGetter.apply(packageDefaultPrivateStringFieldHolder), "'class' -> 'private field' must NOT be accessible");
        assertThrows(PathException.class, () -> fieldGetter.apply(protectedPackageDefaultStringFieldHolder), "'protected class' -> 'default field' must NOT be accessible");
        assertThrows(PathException.class, () -> fieldGetter.apply(protectedProtectedStringFieldHolder), "'protected class' -> 'protected field' must NOT be accessible");
        assertThrows(PathException.class, () -> fieldGetter.apply(protectedPrivateStringFieldHolder), "'protected class' -> 'private field' must NOT be accessible");
        assertThrows(PathException.class, () -> fieldGetter.apply(privatePublicStringFieldHolder), "'private class' -> 'public field' must NOT be accessible");
        assertThrows(PathException.class, () -> fieldGetter.apply(privatePackageDefaultStringFieldHolder), "'private class' -> 'default field' must NOT be accessible");
        assertThrows(PathException.class, () -> fieldGetter.apply(privateProtectedStringFieldHolder), "'private class' -> 'protected field' must NOT be accessible");
        assertThrows(PathException.class, () -> fieldGetter.apply(privatePrivateStringFieldHolder), "'private class' -> 'private field' must NOT be accessible");
        assertThrows(PathException.class, () -> fieldGetter.apply(publicThrowingGetterStringFieldHolder), "'public class' -> 'private field' -> 'public Object getField' which throws Exception must fail");
        assertThrows(PathException.class, () -> fieldGetter.apply(packageDefaultGetterStringFieldHolder), "'public class' -> 'private field' -> 'default Object getField' must NOT be accessible");
        assertThrows(PathException.class, () -> fieldGetter.apply(protectedGetterStringFieldHolder), "'public class' -> 'private field' -> 'protected Object getField' must NOT be accessible");
        assertThrows(PathException.class, () -> fieldGetter.apply(privateGetterStringFieldHolder), "'public class' -> 'private field' -> 'private Object getField' must NOT be accessible");
    }

    public static class PublicStringFieldHolder {
        public final String field = FIELD_VALUE;
    }

    public static class PackageDefaultStringFieldHolder {
        final String field = FIELD_VALUE;
    }

    public static class ProtectedStringFieldHolder {
        protected final String field = FIELD_VALUE;
    }

    public static class PrivateStringFieldHolder {
        private final String field = FIELD_VALUE;
    }

    static class PackageDefaultPublicStringFieldHolder {
        public final String field = FIELD_VALUE;
    }

    static class PackageDefaultPackageDefaultStringFieldHolder {
        final String field = FIELD_VALUE;
    }

    static class PackageDefaultProtectedStringFieldHolder {
        protected final String field = FIELD_VALUE;
    }

    static class PackageDefaultPrivateStringFieldHolder {
        private final String field = FIELD_VALUE;
    }

    protected static class ProtectedPublicStringFieldHolder {
        public final String field = FIELD_VALUE;
    }

    protected static class ProtectedPackageDefaultStringFieldHolder {
        final String field = FIELD_VALUE;
    }

    protected static class ProtectedProtectedStringFieldHolder {
        protected final String field = FIELD_VALUE;
    }

    protected static class ProtectedPrivateStringFieldHolder {
        private final String field = FIELD_VALUE;
    }

    private static class PrivatePublicStringFieldHolder {
        public final String field = FIELD_VALUE;
    }

    private static class PrivatePackageDefaultStringFieldHolder {
        final String field = FIELD_VALUE;
    }

    private static class PrivateProtectedStringFieldHolder {
        protected final String field = FIELD_VALUE;
    }

    private static class PrivatePrivateStringFieldHolder {
        private final String field = FIELD_VALUE;
    }

    public static class PublicGetterStringFieldHolder {
        private final String field = FIELD_VALUE;

        public String getField() {
            return field;
        }
    }

    public static class PublicThrowingGetterStringFieldHolder {
        private final String field = FIELD_VALUE;

        public String getField() {
            throw new IllegalStateException();
        }
    }

    public static class PackageDefaultGetterStringFieldHolder {
        private final String field = FIELD_VALUE;

        String getField() {
            return field;
        }
    }

    public static class ProtectedGetterStringFieldHolder {
        private final String field = FIELD_VALUE;

        protected String getField() {
            return field;
        }
    }

    public static class PrivateGetterStringFieldHolder {
        private final String field = FIELD_VALUE;

        private String getField() {
            return field;
        }
    }

    public static class PublicGetterBooleanFieldHolder {
        private final boolean field = BOOLEAN_FIELD_VALUE;

        public boolean isField() {
            return field;
        }
    }
}
