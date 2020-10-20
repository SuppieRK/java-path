package com.github.suppie.javapath.reflection;

import com.github.suppie.javapath.exceptions.PathNotFoundException;

import java.lang.reflect.InvocationTargetException;

/**
 * Base interface to implement reflection-based access to class items
 */
public interface DeclaredItemsReader {
    String NO_SUCH_FIELD = "No such field %s at target %s";
    String CANNOT_ACCESS_FIELD = "Cannot access field %s at target %s";

    String NO_SUCH_METHOD = "No such method %s at target %s";
    String CANNOT_ACCESS_METHOD = "Cannot access method %s at target %s";
    String UNDERLYING_METHOD_EXCEPTION = "Underlying method %s threw an exception at target %s";

    String NO_SUCH_METHOD_WITH_SINGLE_PARAMETER = "No such method %s at target %s that accepts %s with value %s";
    String CANNOT_ACCESS_METHOD_WITH_SINGLE_PARAMETER = "Cannot access method %s at target %s that accepts %s with value %s";
    String UNDERLYING_METHOD_WITH_SINGLE_PARAMETER_EXCEPTION = "Underlying method %s threw an exception at target %s that accepts %s with value %s";

    /**
     * Access declared field on given target
     *
     * @param target    that presumably allows to access the field
     * @param fieldName to access
     * @return field value
     * @throws PathNotFoundException wrapping either {@link NoSuchFieldException} or
     *                       {@link IllegalAccessException}. The reason for wrapping is
     *                       the fact that all operations performed against specific target
     *                       and both these exceptions do not lead to retrieving desired value.
     */
    default Object readField(final Object target, final String fieldName) throws PathNotFoundException {
        try {
            return target.getClass().getDeclaredField(fieldName).get(target);
        } catch (NoSuchFieldException nsfe) {
            throw new PathNotFoundException(String.format(
                    NO_SUCH_FIELD,
                    fieldName, target.getClass().getName()
            ), nsfe);
        } catch (IllegalAccessException iae) {
            throw new PathNotFoundException(String.format(
                    CANNOT_ACCESS_FIELD,
                    fieldName, target.getClass().getName()
            ), iae);
        }
    }

    /**
     * Invoke declared method on given target
     *
     * @param target     that presumably has this method
     * @param methodName to lookup and invoke
     * @return invocation result for given target
     * @throws PathNotFoundException wrapping either {@link NoSuchMethodException} or
     *                       {@link IllegalAccessException}. The reason for wrapping is
     *                       the fact that all operations performed against specific target
     *                       and both these exceptions do not lead to retrieving desired value.
     */
    default Object readMethod(final Object target, final String methodName) throws PathNotFoundException {
        try {
            return target.getClass().getDeclaredMethod(methodName).invoke(target);
        } catch (NoSuchMethodException nsme) {
            throw new PathNotFoundException(String.format(
                    NO_SUCH_METHOD,
                    methodName, target.getClass().getName()
            ), nsme);
        } catch (IllegalAccessException iae) {
            throw new PathNotFoundException(String.format(
                    CANNOT_ACCESS_METHOD,
                    methodName, target.getClass().getName()
            ), iae);
        } catch (InvocationTargetException ite) {
            throw new PathNotFoundException(String.format(
                    UNDERLYING_METHOD_EXCEPTION,
                    methodName, target.getClass().getName()
            ), ite.getTargetException());
        }
    }

    /**
     * Invoke declared parameterized method on given target
     *
     * @param target     that presumably has this method
     * @param methodName to lookup and invoke
     * @return invocation result for given target
     * @throws PathNotFoundException wrapping either {@link NoSuchMethodException} or
     *                       {@link IllegalAccessException}. The reason for wrapping is
     *                       the fact that all operations performed against specific target
     *                       and both these exceptions do not lead to retrieving desired value.
     */
    default <T> Object readMethodWithSingleParameter(final Object target, final String methodName, final Class<T> argumentType, final T argumentValue) throws PathNotFoundException {
        try {
            return target.getClass().getDeclaredMethod(methodName, argumentType).invoke(target, argumentValue);
        } catch (NoSuchMethodException nsme) {
            throw new PathNotFoundException(String.format(
                    NO_SUCH_METHOD_WITH_SINGLE_PARAMETER,
                    methodName, target.getClass().getName(), argumentType.getName(), argumentValue
            ), nsme);
        } catch (IllegalAccessException iae) {
            throw new PathNotFoundException(String.format(
                    CANNOT_ACCESS_METHOD_WITH_SINGLE_PARAMETER,
                    methodName, target.getClass().getName(), argumentType.getName(), argumentValue), iae);
        } catch (InvocationTargetException ite) {
            throw new PathNotFoundException(String.format(
                    UNDERLYING_METHOD_WITH_SINGLE_PARAMETER_EXCEPTION,
                    methodName, target.getClass().getName(), argumentType.getName(), argumentValue
            ), ite.getTargetException());
        }
    }
}
