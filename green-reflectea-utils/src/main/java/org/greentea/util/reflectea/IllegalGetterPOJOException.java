package org.greentea.util.reflectea;
/**
 * @author Filipe Gonzaga Miranda
 */
public class IllegalGetterPOJOException extends RuntimeException {
	/**
	 */
	private static final long serialVersionUID = 7878787777777774137L;
	public IllegalGetterPOJOException(String message){
		super(message);
	}
	public IllegalGetterPOJOException(String message, Throwable throwable){
		super(message, throwable);
	}
}
