package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 67.
 */
public class Revision67Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 67.
	 */
	public Revision67Updates() {
		super(SchemaRevisionen.REV_67);

		final var tab1 = Schema.tab_TimestampsNotenmodulCredentials;
		pruefeZeitstempel(tab1, tab1.col_tsPasswordHash, tab1.col_tsArt2FA, tab1.col_tsTotpSecret, tab1.col_tsIstErstanmeldung);

		final var tab2 = Schema.tab_TimestampsSchuelerAnkreuzkompetenzen;
		pruefeZeitstempel(tab2, tab2.col_tsStufe);

		final var tab3 = Schema.tab_TimestampsSchuelerLeistungsdaten;
		pruefeZeitstempel(tab3, tab3.col_tsNotenKrz, tab3.col_tsNotenKrzQuartal, tab3.col_tsFehlStd, tab3.col_tsuFehlStd, tab3.col_tsLernentw,
				tab3.col_tsWarnung);

		final var tab4 = Schema.tab_TimestampsSchuelerLernabschnittsdaten;
		pruefeZeitstempel(tab4, tab4.col_tsSumFehlStd, tab4.col_tsSumFehlStdU, tab4.col_tsZeugnisBem, tab4.col_tsASV, tab4.col_tsAUE, tab4.col_tsLELS,
				tab4.col_tsESF, tab4.col_tsBemerkungFSP, tab4.col_tsBemerkungVersetzung);

		final var tab5 = Schema.tab_TimestampsSchuelerTeilleistungen;
		pruefeZeitstempel(tab5, tab5.col_tsDatum, tab5.col_tsLehrer_ID, tab5.col_tsArt_ID, tab5.col_tsBemerkung, tab5.col_tsNotenKrz);

		final var tab6 = Schema.tab_TimestampsSchuelerZP10;
		pruefeZeitstempel(tab6, tab6.col_tsVornote, tab6.col_tsNoteSchriftlichePruefung, tab6.col_tsMuendlichePruefung,
				tab6.col_tsMuendlichePruefungFreiwillig, tab6.col_tsNoteMuendlichePruefung, tab6.col_tsAbschlussnote);
	}

	private void pruefeZeitstempel(final SchemaTabelle tab, final SchemaTabelleSpalte... cols) {
		for (final SchemaTabelleSpalte col : cols) {
			add("Prüft, ob der Zeitstempel tsPasswordHash aufgrund der Umstellung aus UTC in der Zukunft liegt und korrigiert diesen in diesem Fall."
					.formatted(tab.name(), col.name()),
					"UPDATE %1$s SET %2$s = UTC_TIMESTAMP(3) WHERE %2$s > UTC_TIMESTAMP(3);".formatted(tab.name(), col.name()),
					tab
			);
		}
	}

}
