package de.svws_nrw.module.pdf.htmlcontexts;

import de.svws_nrw.module.pdf.HtmlContext;
import org.thymeleaf.context.Context;


/**
 * Ein ThymeLeaf-Html-Daten-Context zum Bereich "Schule", um ThymeLeaf-html-Templates mit Daten zu füllen und daraus PDF-Dateien zu erstellen.
 */
public final class HtmlContextDruckparameter extends HtmlContext {

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Daten.
	 */
	public HtmlContextDruckparameter() {
		erzeugeContext();
	}


	/**
	 * Erzeugt den Context zum Füllen eines html-Templates
	 */
	private void erzeugeContext() {
		final Context context = new Context();

		context.setVariable("DruckparameterDetaillevel", 0);
		context.setVariable("DruckparameterNurBelegteFaecher", false);

		super.setContext(context);
	}
}
