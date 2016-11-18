Tunnels up checked exceptions within streams that would usually
need a try-catch block, to a wrapper surrounding the stream.

```
        // BAD ORIGINAL: HOW YOU MIGHT HAVE TO HANDLE AN INLINE EXCEPTION WITHIN A STREAM
        try {
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
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
```
```
         // GOOD NEW: HOW YOU CAN USE A WRAPPER TO BUBBLE UP THE EXCEPTIONS
         try {
             Object fieldObjs2 = LambdaWrap.withReturn(IllegalAccessException.class,
                     safe -> Arrays.stream(la.getClass().getFields())
                         .map(safe.function(field -> field.get(la)))
                         .collect(Collectors.toList()));

             System.out.println(fieldObjs2);
         } catch (IllegalAccessException e) {
             e.printStackTrace();
         }
```