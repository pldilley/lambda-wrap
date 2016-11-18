import java.lang.reflect.UndeclaredThrowableException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;


/**
 * Created by paul dilley on 12/11/16.
 * Allows you to tunnel up checked exceptions embedded within streams to a wrapper around the entire stream
 */

public class LambdaWrap {
    /*
     * Provides helper methods for use inside of the wrapper
     */
    public interface LambdaWrapToolkit<E0 extends Throwable, E1 extends Throwable, E2 extends Throwable,
            E3 extends Throwable, E4 extends Throwable, E5 extends Throwable,
            E6 extends Throwable, E7 extends Throwable, E8 extends Throwable,
            E9 extends Throwable> {


        @FunctionalInterface
        interface FunctionChecked<T, R, E0 extends Throwable, E1 extends Throwable, E2 extends Throwable,
                E3 extends Throwable, E4 extends Throwable, E5 extends Throwable,
                E6 extends Throwable, E7 extends Throwable, E8 extends Throwable,
                E9 extends Throwable> {

            R apply(T elem) throws E0, E1, E2, E3, E4, E5, E6, E7, E8, E9;
        }

        default <T, R> Function<T, R> function(FunctionChecked<T, R, E0, E1, E2, E3, E4, E5, E6, E7, E8, E9> f) {
            return elem -> {
                try { return f.apply(elem);
                } catch (RuntimeException ex) { throw ex;
                } catch (Throwable ex) { throw new LambdaWrap.WrapperException(ex); }
            };
        }



        @FunctionalInterface
        interface ConsumerChecked<T, E0 extends Throwable, E1 extends Throwable, E2 extends Throwable,
                E3 extends Throwable, E4 extends Throwable, E5 extends Throwable,
                E6 extends Throwable, E7 extends Throwable, E8 extends Throwable,
                E9 extends Throwable> {

            void accept(T elem) throws E0, E1, E2, E3, E4, E5, E6, E7, E8, E9;
        }

        default  <T> Consumer<T> consumer(ConsumerChecked<T, E0, E1, E2, E3, E4, E5, E6, E7, E8, E9> c) {
            return elem -> {
                try { c.accept(elem); }
                catch (RuntimeException ex) { throw ex; }
                catch (Throwable ex) { throw new LambdaWrap.WrapperException(ex); }
            };
        }



        @FunctionalInterface
        interface PredicateChecked<T, E0 extends Throwable, E1 extends Throwable, E2 extends Throwable,
                E3 extends Throwable, E4 extends Throwable, E5 extends Throwable,
                E6 extends Throwable, E7 extends Throwable, E8 extends Throwable,
                E9 extends Throwable> {

            boolean test(T elem) throws E0, E1, E2, E3, E4, E5, E6, E7, E8, E9;
        }

        default  <T> Predicate<T> predicate(PredicateChecked<T, E0, E1, E2, E3, E4, E5, E6, E7, E8, E9> p) {
            return elem -> {
                try { return p.test(elem); }
                catch (RuntimeException ex) { throw ex; }
                catch (Throwable ex) { throw new LambdaWrap.WrapperException(ex); }
            };
        }
    }

    /*
     * A temporary custom runtime exception to help wrap and bubble up non-runtime exceptions outside of the lambda
     */
    public static class WrapperException extends RuntimeException {
        public WrapperException(Throwable cause) {
            super(cause);
        }
    }

    /*
     * A void exception type for use in polymorphism generic types
     */
    public static class $ extends RuntimeException {}

    /*
     * Provides a returning interface for wrapping the ENTIRE lambda
     */
    @FunctionalInterface
    public interface WrapWithReturn<E0 extends Throwable, E1 extends Throwable, E2 extends Throwable,
                                    E3 extends Throwable, E4 extends Throwable, E5 extends Throwable,
                                    E6 extends Throwable, E7 extends Throwable, E8 extends Throwable,
                                    E9 extends Throwable, R> {

        R doWork(LambdaWrapToolkit<E0, E1, E2, E3, E4, E5, E6, E7, E8, E9> lambdaExceptionWrapper);
    }

    /*
     * Provides a non-returning interface for wrapping the ENTIRE lambda
     */
    @FunctionalInterface
    public interface WrapWithoutReturn<E0 extends Throwable, E1 extends Throwable, E2 extends Throwable,
                                       E3 extends Throwable, E4 extends Throwable, E5 extends Throwable,
                                       E6 extends Throwable, E7 extends Throwable, E8 extends Throwable,
                                       E9 extends Throwable> {

        void doWork(LambdaWrapToolkit<E0, E1, E2, E3, E4, E5, E6, E7, E8, E9> lambdaExceptionWrapper);
    }

    /*
     * Method to wrap the dangerous stream up, inside of the WrapWithReturn lambda.
     * This method makes it necessary for the specified exceptions to be caught.
     * The idea is to create a custom LambdaExceptionWrapper object inside here which is also aware of the specified
     * generic exception types, and requires "checkedFunction" to only capture that kind of exception.
     */
    @SuppressWarnings("unchecked")
    public static <E0 extends Throwable, E1 extends Throwable, E2 extends Throwable, E3 extends Throwable,
                   E4 extends Throwable, E5 extends Throwable, E6 extends Throwable, E7 extends Throwable,
                   E8 extends Throwable, E9 extends Throwable, R> R withReturn(
                           Class<E0> e0, Class<E1> e1, Class<E2> e2, Class<E3> e3, Class<E4> e4, Class<E5> e5,
                           Class<E6> e6, Class<E7> e7, Class<E8> e8, Class<E9> e9,
                           WrapWithReturn<E0, E1, E2, E3, E4, E5, E6, E7, E8, E9, R> work) throws E0, E1, E2, E3, E4, E5, E6, E7, E8, E9 {
        try {
            return work.doWork(new LambdaWrapToolkit<E0, E1, E2, E3, E4, E5, E6, E7, E8, E9>() {});
        } catch (WrapperException ex) {
            Throwable cause = ex.getCause();

            if (e0 != null && e0.isInstance(cause)) throw (E0) cause;
            if (e1 != null && e1.isInstance(cause)) throw (E1) cause;
            if (e2 != null && e2.isInstance(cause)) throw (E2) cause;
            if (e3 != null && e3.isInstance(cause)) throw (E3) cause;
            if (e4 != null && e4.isInstance(cause)) throw (E4) cause;
            if (e5 != null && e5.isInstance(cause)) throw (E5) cause;
            if (e6 != null && e6.isInstance(cause)) throw (E6) cause;
            if (e7 != null && e7.isInstance(cause)) throw (E7) cause;
            if (e8 != null && e8.isInstance(cause)) throw (E8) cause;
            if (e9 != null && e9.isInstance(cause)) throw (E9) cause;

            throw new UndeclaredThrowableException(cause, "Should never occur -> bug in LambdaWrap");
        }
    }

    /*
     * Polymorphism - of withReturn
     */
    public static <E0 extends Throwable, E1 extends Throwable, E2 extends Throwable, E3 extends Throwable, E4 extends Throwable, E5 extends Throwable, E6 extends Throwable, E7 extends Throwable, E8 extends Throwable, R> R withReturn(Class<E0> e0, Class<E1> e1, Class<E2> e2, Class<E3> e3, Class<E4> e4, Class<E5> e5, Class<E6> e6, Class<E7> e7, Class<E8> e8, WrapWithReturn<E0, E1, E2, E3, E4, E5, E6, E7, E8, $, R> work) throws E0, E1, E2, E3, E4, E5, E6, E7, E8 {
        return withReturn(e0, e1, e2, e3, e4, e5, e6, e7, e8, null, work);
    }
    public static <E0 extends Throwable, E1 extends Throwable, E2 extends Throwable, E3 extends Throwable, E4 extends Throwable, E5 extends Throwable, E6 extends Throwable, E7 extends Throwable, R> R withReturn(Class<E0> e0, Class<E1> e1, Class<E2> e2, Class<E3> e3, Class<E4> e4, Class<E5> e5, Class<E6> e6, Class<E7> e7, WrapWithReturn<E0, E1, E2, E3, E4, E5, E6, E7, $, $, R> work) throws E0, E1, E2, E3, E4, E5, E6, E7 {
        return withReturn(e0, e1, e2, e3, e4, e5, e6, e7, null, work);
    }
    public static <E0 extends Throwable, E1 extends Throwable, E2 extends Throwable, E3 extends Throwable, E4 extends Throwable, E5 extends Throwable, E6 extends Throwable, R> R withReturn(Class<E0> e0, Class<E1> e1, Class<E2> e2, Class<E3> e3, Class<E4> e4, Class<E5> e5, Class<E6> e6, WrapWithReturn<E0, E1, E2, E3, E4, E5, E6, $, $, $, R> work) throws E0, E1, E2, E3, E4, E5, E6 {
        return withReturn(e0, e1, e2, e3, e4, e5, e6, null, work);
    }
    public static <E0 extends Throwable, E1 extends Throwable, E2 extends Throwable, E3 extends Throwable, E4 extends Throwable, E5 extends Throwable, R> R withReturn(Class<E0> e0, Class<E1> e1, Class<E2> e2, Class<E3> e3, Class<E4> e4, Class<E5> e5, WrapWithReturn<E0, E1, E2, E3, E4, E5, $, $, $, $, R> work) throws E0, E1, E2, E3, E4, E5 {
        return withReturn(e0, e1, e2, e3, e4, e5, null, work);
    }
    public static <E0 extends Throwable, E1 extends Throwable, E2 extends Throwable, E3 extends Throwable, E4 extends Throwable, R> R withReturn(Class<E0> e0, Class<E1> e1, Class<E2> e2, Class<E3> e3, Class<E4> e4, WrapWithReturn<E0, E1, E2, E3, E4, $, $, $, $, $, R> work) throws E0, E1, E2, E3, E4 {
        return withReturn(e0, e1, e2, e3, e4, null, work);
    }
    public static <E0 extends Throwable, E1 extends Throwable, E2 extends Throwable, E3 extends Throwable, R> R withReturn(Class<E0> e0, Class<E1> e1, Class<E2> e2, Class<E3> e3, WrapWithReturn<E0, E1, E2, E3, $, $, $, $, $, $, R> work) throws E0, E1, E2, E3 {
        return withReturn(e0, e1, e2, e3, null, work);
    }
    public static <E0 extends Throwable, E1 extends Throwable, E2 extends Throwable, R> R withReturn(Class<E0> e0, Class<E1> e1, Class<E2> e2, WrapWithReturn<E0, E1, E2, $, $, $, $, $, $, $, R>work) throws E0, E1, E2 {
        return withReturn(e0, e1, e2, null, work);
    }
    public static <E0 extends Throwable, E1 extends Throwable, R> R withReturn(Class<E0> e0, Class<E1> e1, WrapWithReturn<E0, E1, $, $, $, $, $, $, $, $, R> work) throws E0, E1 {
        return withReturn(e0, e1, null, work);
    }
    public static <E0 extends Throwable, R> R withReturn(Class<E0> e0, WrapWithReturn<E0, $, $, $, $, $, $, $, $, $, R> work) throws E0 {
        return withReturn(e0, null, work);
    }


    /*
     * Polymorphism - of withoutReturn
     */
    public static <E0 extends Throwable, E1 extends Throwable, E2 extends Throwable, E3 extends Throwable, E4 extends Throwable, E5 extends Throwable, E6 extends Throwable, E7 extends Throwable, E8 extends Throwable, E9 extends Throwable> void withoutReturn(Class<E0> e0, Class<E1> e1, Class<E2> e2, Class<E3> e3, Class<E4> e4, Class<E5> e5, Class<E6> e6, Class<E7> e7, Class<E8> e8, Class<E9> e9, WrapWithoutReturn<E0, E1, E2, E3, E4, E5, E6, E7, E8, E9> work) throws E0, E1, E2, E3, E4, E5, E6, E7, E8, E9 {
        withReturn(e0, e1, e2, e3, e4, e5, e6, e7, e8, e9, wrapper -> { work.doWork(wrapper); return null; });
    }
    public static <E0 extends Throwable, E1 extends Throwable, E2 extends Throwable, E3 extends Throwable, E4 extends Throwable, E5 extends Throwable, E6 extends Throwable, E7 extends Throwable, E8 extends Throwable> void withoutReturn(Class<E0> e0, Class<E1> e1, Class<E2> e2, Class<E3> e3, Class<E4> e4, Class<E5> e5, Class<E6> e6, Class<E7> e7, Class<E8> e8, WrapWithoutReturn<E0, E1, E2, E3, E4, E5, E6, E7, E8, $> work) throws E0, E1, E2, E3, E4, E5, E6, E7, E8 {
        withoutReturn(e0, e1, e2, e3, e4, e5, e6, e7, e8, null, work);
    }
    public static <E0 extends Throwable, E1 extends Throwable, E2 extends Throwable, E3 extends Throwable, E4 extends Throwable, E5 extends Throwable, E6 extends Throwable, E7 extends Throwable> void withoutReturn(Class<E0> e0, Class<E1> e1, Class<E2> e2, Class<E3> e3, Class<E4> e4, Class<E5> e5, Class<E6> e6, Class<E7> e7, WrapWithoutReturn<E0, E1, E2, E3, E4, E5, E6, E7, $, $> work) throws E0, E1, E2, E3, E4, E5, E6, E7 {
        withoutReturn(e0, e1, e2, e3, e4, e5, e6, e7, null, work);
    }
    public static <E0 extends Throwable, E1 extends Throwable, E2 extends Throwable, E3 extends Throwable, E4 extends Throwable, E5 extends Throwable, E6 extends Throwable> void withoutReturn(Class<E0> e0, Class<E1> e1, Class<E2> e2, Class<E3> e3, Class<E4> e4, Class<E5> e5, Class<E6> e6, WrapWithoutReturn<E0, E1, E2, E3, E4, E5, E6, $, $, $> work) throws E0, E1, E2, E3, E4, E5, E6 {
        withoutReturn(e0, e1, e2, e3, e4, e5, e6, null, work);
    }
    public static <E0 extends Throwable, E1 extends Throwable, E2 extends Throwable, E3 extends Throwable, E4 extends Throwable, E5 extends Throwable> void withoutReturn(Class<E0> e0, Class<E1> e1, Class<E2> e2, Class<E3> e3, Class<E4> e4, Class<E5> e5, WrapWithoutReturn<E0, E1, E2, E3, E4, E5, $, $, $, $> work) throws E0, E1, E2, E3, E4, E5 {
        withoutReturn(e0, e1, e2, e3, e4, e5, null, work);
    }
    public static <E0 extends Throwable, E1 extends Throwable, E2 extends Throwable, E3 extends Throwable, E4 extends Throwable> void withoutReturn(Class<E0> e0, Class<E1> e1, Class<E2> e2, Class<E3> e3, Class<E4> e4, WrapWithoutReturn<E0, E1, E2, E3, E4, $, $, $, $, $> work) throws E0, E1, E2, E3, E4 {
        withoutReturn(e0, e1, e2, e3, e4, null, work);
    }
    public static <E0 extends Throwable, E1 extends Throwable, E2 extends Throwable, E3 extends Throwable> void withoutReturn(Class<E0> e0, Class<E1> e1, Class<E2> e2, Class<E3> e3, WrapWithoutReturn<E0, E1, E2, E3, $, $, $, $, $, $> work) throws E0, E1, E2, E3 {
        withoutReturn(e0, e1, e2, e3, null, work);
    }
    public static <E0 extends Throwable, E1 extends Throwable, E2 extends Throwable> void withoutReturn(Class<E0> e0, Class<E1> e1, Class<E2> e2, WrapWithoutReturn<E0, E1, E2, $, $, $, $, $, $, $>work) throws E0, E1, E2 {
        withoutReturn(e0, e1, e2, null, work);
    }
    public static <E0 extends Throwable, E1 extends Throwable> void withoutReturn(Class<E0> e0, Class<E1> e1, WrapWithoutReturn<E0, E1, $, $, $, $, $, $, $, $> work) throws E0, E1 {
        withoutReturn(e0, e1, null, work);
    }
    public static <E0 extends Throwable> void withoutReturn(Class<E0> e0, WrapWithoutReturn<E0, $, $, $, $, $, $, $, $, $> work) throws E0 {
        withoutReturn(e0, null, work);
    }
}