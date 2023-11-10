package de.svws_nrw.module.pdf.schule;

import de.svws_nrw.data.schule.DataSchuleStammdaten;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.module.pdf.HtmlContext;
import org.thymeleaf.context.Context;


/**
 * Ein ThymeLeaf-Html-Daten-Context zum Bereich "Schule", um ThymeLeaf-html-Templates mit Daten zu füllen und daraus PDF-Dateien zu erstellen.
 */
public final class HtmlContextSchule extends HtmlContext {

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Daten.
	 *
	 * @param conn	Datenbank-Verbindung
	 */
	public HtmlContextSchule(final DBEntityManager conn) {
		erzeugeContext(conn);
	}


	/**
	 * Erzeugt den Context zum Füllen eines html-Templates.
	 *
	 * @param conn  Datenbank-Verbindung
	 */
	private void erzeugeContext(final DBEntityManager conn) {
		// Daten-Context für Thymeleaf erzeugen.
		final Context context = new Context();
		context.setVariable("Schule", DataSchuleStammdaten.getStammdaten(conn));

		super.setContext(context);
	}

}
