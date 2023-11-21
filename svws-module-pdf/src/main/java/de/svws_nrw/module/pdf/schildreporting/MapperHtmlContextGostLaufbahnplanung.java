package de.svws_nrw.module.pdf.schildreporting;

import de.svws_nrw.core.data.schild3.reporting.SchildReportingGostLaufbahnplanungSchuelerFachwahlen;
import de.svws_nrw.core.data.schild3.reporting.SchildReportingGostLaufbahnplanungSchuelerFehler;
import de.svws_nrw.core.data.schild3.reporting.SchildReportingGostLaufbahnplanungSchuelerGrunddaten;
import de.svws_nrw.core.data.schild3.reporting.SchildReportingGostLaufbahnplanungSchuelerHinweise;
import de.svws_nrw.core.data.schild3.reporting.SchildReportingGostLaufbahnplanungSchuelerSummen;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.module.pdf.html.contexts.gost.laufbahnplanung.HtmlContextGostLaufbahnplanungSchueler;
import de.svws_nrw.module.pdf.reptypes.gost.laufbahnplanung.RepGostLaufbahnplanungSchueler;

import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse dient als Mapper zwischen den Schild-3-Reporting-Datenquellen und den vom Server verwendeten html-Contexts, die als Datenbasis
 * für die serverseitige Generierung von PDF-Dateien verwendet werden.
 */
public final class MapperHtmlContextGostLaufbahnplanung {

	private final List<SchildReportingGostLaufbahnplanungSchuelerGrunddaten> schuelerGrunddaten = new ArrayList<>();

	private final List<SchildReportingGostLaufbahnplanungSchuelerSummen> schuelerSummen = new ArrayList<>();

	private final List<SchildReportingGostLaufbahnplanungSchuelerFachwahlen> schuelerFachwahlen = new ArrayList<>();

	private final List<SchildReportingGostLaufbahnplanungSchuelerFehler> schuelerFehler = new ArrayList<>();

	private final List<SchildReportingGostLaufbahnplanungSchuelerHinweise> schuelerHinweise = new ArrayList<>();


	/**
	 * Erzeugt einen Mapper, der die Schild-3-Datenquellen aus einem html-Context füllt.
	 *
	 * @param conn			Verbindung zur Datenbank
	 * @param schuelerIDs	Liste der Schüler-IDs, für die die Daten gesammelt werden sollen.
	 */
	public MapperHtmlContextGostLaufbahnplanung(final DBEntityManager conn, final List<Long> schuelerIDs) {
		final HtmlContextGostLaufbahnplanungSchueler contextGostLaufbahnplanungenSchueler = new HtmlContextGostLaufbahnplanungSchueler(conn, schuelerIDs);
		final List<RepGostLaufbahnplanungSchueler> listGostLaufbahnplanungenSchueler = contextGostLaufbahnplanungenSchueler.getGostLaufbahnplanungenSchueler();

		for (final RepGostLaufbahnplanungSchueler laufbahnSchueler : listGostLaufbahnplanungenSchueler) {
			final Long schuelerID = laufbahnSchueler.id;
			// TODO: Daten des Contexts auf die fünf Datenquellen von Schild 3 aufsplitten.
		}
	}


	/**
	 * @return Liste der Grunddaten der Schüler
	 */
	public List<SchildReportingGostLaufbahnplanungSchuelerGrunddaten> getSchuelerGrunddaten() {
		return schuelerGrunddaten;
	}

	/**
	 * @return Liste der Summen der Schüler
	 */
	public List<SchildReportingGostLaufbahnplanungSchuelerSummen> getSchuelerSummen() {
		return schuelerSummen;
	}

	/**
	 * @return Liste der Fachwahlen der Schüler
	 */
	public List<SchildReportingGostLaufbahnplanungSchuelerFachwahlen> getSchuelerFachwahlen() {
		return schuelerFachwahlen;
	}

	/**
	 * @return Liste der Fehler der Schüler
	 */
	public List<SchildReportingGostLaufbahnplanungSchuelerFehler> getSchuelerFehler() {
		return schuelerFehler;
	}

	/**
	 * @return Liste der Hinweise der Schüler
	 */
	public List<SchildReportingGostLaufbahnplanungSchuelerHinweise> getSchuelerHinweise() {
		return schuelerHinweise;
	}


}
