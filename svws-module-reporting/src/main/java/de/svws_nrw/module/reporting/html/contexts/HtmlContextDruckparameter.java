package de.svws_nrw.module.reporting.html.contexts;

import de.svws_nrw.module.reporting.html.base.HtmlContext;
import jakarta.ws.rs.WebApplicationException;
import org.thymeleaf.context.Context;

import java.util.List;


/**
 * Ein ThymeLeaf-Html-Daten-Context mit Parametern zur Steuerung des Drucks, um ThymeLeaf-html-Templates mit Daten zu füllen und daraus PDF-Dateien zu erstellen.
 */
public final class HtmlContextDruckparameter extends HtmlContext {

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Daten.
	 * @param detaillevel	Parameter, der in Templates verwendet werden kann, um den Detailgrad der Darstellung zu steuern.
	 * @param idsListe		Eine Liste mit IDs, die zur Ausgabe oder Filterung von Daten genutzt werden kann.
	 */
	public HtmlContextDruckparameter(final int detaillevel, final List<Long> idsListe) {
		erzeugeContext(detaillevel, idsListe);
	}


	/**
	 * Erzeugt den Context zum Füllen eines html-Templates.
	 * @param detaillevel  	Parameter, der in Templates verwendet werden kann, um den Detailgrad der Darstellung zu steuern.
	 * @param idsListe 		Eine Liste mit IDs, die zur Ausgabe oder Filterung von Daten genutzt werden kann.
	 */
	private void erzeugeContext(final int detaillevel, final List<Long> idsListe) throws WebApplicationException {

		// Daten-Context für Thymeleaf erzeugen.
		final Context context = new Context();
		context.setVariable("DruckparameterIdsListe", idsListe);
		context.setVariable("DruckparameterDetaillevel", detaillevel);

		super.setContext(context);
	}
}
