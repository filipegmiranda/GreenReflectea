# GreenReflectea

This is a Subproject of the Green Tea Project intended to provide facilities for relfections operations in Java

The version 7.x signifies that it is build for Java 7+.

## Undestand the Green Reflectea project.

The green-reflectea-utils contain powerful and easy to use methods that will help you develop your own APIs and
frameworks using reflective operations.

    public @interface YourAnnotation{}        
    
    // It this annotation is meat to be in fields of your POJO Classes, you might read all fields containing this
    //annotation
	
	class YourPOJO{
		@YourAnnotation
		private String field;
	}
	
	List<Field> list = ReflecteaUtils.getFieldsAnnotatedWith(YourAnnotation.class,  YourPOJO.class);
	
## Working with private fields
You can easily turn all field of your classes accessible during execution with this API, as long as 
you don't have a SecurityManager, or yours is set to supporte reflection.

Why would you do that?

Because instead of using the Getter or Setter methods while accessing fields with reflection, you can access
the field and optimize up 40 times, which means your code might run 40 times faster

    List<Field> fields = ReflecteaUtils.getFieldsAnnotatedWith(YourAnnotation.class,  YourPOJO.class);	           _      ReflecteaUtils.makeListFieldsRTAccessible(fields);

Summary of Functionalities:
* Write and read directly to private/protected fields with no need to create Instances of Method class
* Tired of verbose code while reading annotations, accessing fields and invoking methods reflectively?
* The Green Refletea will ease your tasks by providing a series of convenince methods that do the job for you
* No need to catch Checked Exceptions
