package de.svws_nrw.module.pdf.html.contexts;

import de.svws_nrw.data.schule.DataSchuleStammdaten;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.module.pdf.html.base.HtmlContext;
import de.svws_nrw.module.pdf.reptypes.RepSchule;
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
		final Context context = new Context();

		RepSchule schule = new RepSchule(DataSchuleStammdaten.getStammdaten(conn));
		context.setVariable("Schule", schule);

		super.setContext(context);
	}

}
