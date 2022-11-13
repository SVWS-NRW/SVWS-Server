package de.nrw.schule.svws.base.shell;

import java.io.IOException;

/**
 * Eine Exception, die beim Parsen der Kommandozeile auftreten kann.  
 */
public class CommandLineException extends IOException {
	
	/// Die eindeutige ID der Exception
	private static final long serialVersionUID = -8848580827404924284L;

		
	/**
	 * Erzeugt eine neue CommandLineException.
	 * 
	 * @param type   der Typ der CommandLineException
	 */
	public CommandLineException(CommandLineExceptionType type) {
		super(type.getMessage());
	}

}
