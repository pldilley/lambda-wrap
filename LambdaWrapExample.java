import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

// Only for testing


/**
 * Created by paul dilley on 12/11/16.
 * Allows you to tunnel up checked exceptions embedded within streams to a wrapper around the entire stream
 */

public class LambdaWrapExample {

   public static void main(String args[]) {
       String la = "pdsods";

       // ORIGINAL: HOW YOU MIGHT HAVE TO HANDLE AN INLINE EXCEPTION WITHIN A STREAM
       Object fieldObjs = Arrays.stream(la.getClass().getFields())
               .map(field -> {
                   try {
                       return field.get(la);
                   } catch (IllegalAccessException e) {
                       throw new RuntimeException(e);
                   }
               })
               .collect(Collectors.toList());

       System.out.println(fieldObjs);



       // WRAPPER: HOW YOU CAN USE A WRAPPER TO BUBBLE UP THE EXCEPTIONS
       try {
           Object fieldObjs2 = LambdaWrap.withReturn(IllegalAccessException.class,
                   safe -> Arrays.stream(la.getClass().getFields())
                           .map(safe.function(field -> field.get(la)))
                           .collect(Collectors.toList()));

           System.out.println(fieldObjs2);
       } catch (IllegalAccessException e) {
           e.printStackTrace();
       }

       try {
           Object fieldObjs3 = LambdaWrap.withReturn(IllegalAccessException.class,
                   safe -> Arrays.stream(la.getClass().getFields())
                           .filter(safe.predicate(field -> field.get(la) == Object.class))
                           .collect(Collectors.toList()));

           System.out.println(fieldObjs3);
       } catch (IllegalAccessException e) {
           e.printStackTrace();
       }

       try {
           LambdaWrap.withoutReturn(IllegalAccessException.class,
                   safe -> Arrays.asList(la.getClass().getFields()).forEach(
                           safe.consumer(field -> System.out.println(field.get(la)))
                   ));

       } catch (IllegalAccessException e) {
           e.printStackTrace();
       }
   }
}