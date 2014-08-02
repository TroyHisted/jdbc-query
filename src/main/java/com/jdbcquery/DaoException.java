package com.jdbcquery;

/**
 * Represents a generic SQL exception
 *
 * @author Troy Histed
 */
public class DaoException extends RuntimeException {

	private static final long serialVersionUID = -6056741171142667158L;

	/**
	 * Creates a DaoException.
	 * 
	 * @param aDescription
	 *            Description of the exception
	 */
	public DaoException(String aDescription) {
		super(aDescription);
	}

	/**
	 * Creates a DaoException.
	 * 
	 * @param aDescription
	 *            Description of the exception
	 * @param aException
	 *            original exception
	 */
	public DaoException(String aDescription, Exception aException) {
		super(aDescription, aException);
	}
}