package de.svws_nrw.module.reporting.html.contexts;

import org.thymeleaf.context.Context;

/**
 * Abstrakte Basisklasse für die Thymeleaf-html-Daten-Contexts.
 */
public abstract class HtmlContext {

	private Context context;

	/**
	 * Erstellt einen neuen Thymeleaf-Daten-Context
	 */
	protected HtmlContext() {
	}

	/**
	 * Rückgabe des Thymeleaf-Daten-Context
	 *
	 * @return Gibt einen Thymeleaf-Daten-Context zurück.
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * Setzen des Thymeleaf-Daten-Context
	 *
	 * @param context Thymeleaf-Daten-Context mit den Daten.
	 */
	public void setContext(final Context context) {
		this.context = context;
	}

}
