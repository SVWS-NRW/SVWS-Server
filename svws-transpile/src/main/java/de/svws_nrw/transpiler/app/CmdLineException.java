package de.svws_nrw.transpiler.app;

import java.io.IOException;

/**
 * Eine Exception, die beim Parsen der Kommandozeile auftreten kann.
 */
public class CmdLineException extends IOException {

	/// Die eindeutige ID der Exception
	private static final long serialVersionUID = -8848580827404924284L;


	/**
	 * Erzeugt eine neue CommandLineException.
	 *
	 * @param type   der Typ der CommandLineException
	 */
	public CmdLineException(final CmdLineExceptionType type) {
		super(type.getMessage());
	}

}
