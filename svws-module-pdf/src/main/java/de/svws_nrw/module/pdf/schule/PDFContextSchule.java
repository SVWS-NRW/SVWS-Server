package de.svws_nrw.module.pdf.schule;

import de.svws_nrw.data.schule.DataSchuleStammdaten;
import de.svws_nrw.db.DBEntityManager;
import org.thymeleaf.context.Context;


/**
 * Eine Sammlung von Methoden zum Erstellen von Daten-Contexts zum Bereich "Schule" für die html-Templates zur Erstellung von PDF-Dateien.
 */
public final class PDFContextSchule {

	private PDFContextSchule() {
		throw new IllegalStateException("Instantiation not allowed");
	}

	/**
	 * Liefert die Daten in Form eines Context zum Füllen eines html-Templates.
	 *
	 * @param conn  die Datenbank-Verbindung
	 *
	 * @return 		der Context
	 */
	public static Context schule(final DBEntityManager conn) {
		final Context context = new Context();
		context.setVariable("Schule", DataSchuleStammdaten.getStammdaten(conn));
		return context;
	}

}
