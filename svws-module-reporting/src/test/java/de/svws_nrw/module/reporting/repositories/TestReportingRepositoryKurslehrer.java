package de.svws_nrw.module.reporting.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursLehrer;

/**
 * Prüft, wie das Lerngruppen-Repository die Wochenstunden der zusätzlichen Kurslehrer ermittelt. Der Vertrag: Jede eingetragene Lehrkraft steht in der
 * Map, auch wenn die Datenbank zu ihr keinen Anteil führt. Ein leerer Anteil zählt dabei als 0.0.
 */
class TestReportingRepositoryKurslehrer {

	/** Die ID des Kurses, dessen zusätzliche Lehrkräfte geladen werden. */
	private static final long ID_KURS = 42L;

	/** Die ID einer Lehrkraft mit eingetragenem Wochenstundenanteil. */
	private static final long ID_LEHRER_MIT_ANTEIL = 7L;

	/** Die ID einer Lehrkraft, zu der die Datenbank keinen Wochenstundenanteil führt. */
	private static final long ID_LEHRER_OHNE_ANTEIL = 8L;

	/** Die Datenbankverbindung, über die das Repository die Einträge lädt. */
	private DBEntityManager conn;

	/** Das Repository unter Test. */
	private ReportingRepositoryLerngruppen repository;


	@BeforeEach
	void setUp() {
		conn = mock(DBEntityManager.class);

		final Logger logger = new Logger();
		logger.addConsumer(new LogConsumerList());

		final ReportingContext reportingContext = mock(ReportingContext.class);
		when(reportingContext.logger()).thenReturn(logger);
		when(reportingContext.conn()).thenReturn(conn);

		repository = new ReportingRepositoryLerngruppen(reportingContext);
	}

	/**
	 * Erzeugt den Datenbankeintrag einer zusätzlichen Kurslehrkraft.
	 *
	 * @param idLehrer Die ID der Lehrkraft.
	 * @param anteil   Der Wochenstundenanteil, oder null, falls die Datenbank keinen führt.
	 *
	 * @return Der Eintrag, wie ihn die Abfrage liefert.
	 */
	private static DTOKursLehrer dtoKursLehrer(final long idLehrer, final Double anteil) {
		final DTOKursLehrer dto = new DTOKursLehrer(ID_KURS, idLehrer);
		dto.Anteil = anteil;
		return dto;
	}

	/**
	 * Legt fest, was die Abfrage der zusätzlichen Kurslehrer zurückgibt.
	 *
	 * @param eintraege Die Einträge, die die Abfrage liefert.
	 */
	private void gebeKurslehrerVor(final List<DTOKursLehrer> eintraege) {
		when(conn.queryList(eq(DTOKursLehrer.QUERY_BY_KURS_ID), eq(DTOKursLehrer.class), any())).thenReturn(eintraege);
	}


	/** Zu einer Lehrkraft mit eingetragenem Anteil liefert das Repository genau diesen Wert. */
	@Test
	void kurslehrerMitAnteilLiefertDenEingetragenenWert() {
		gebeKurslehrerVor(List.of(dtoKursLehrer(ID_LEHRER_MIT_ANTEIL, 3.5)));

		assertEquals(3.5, repository.kurslehrerWochenstunden(ID_KURS).get(ID_LEHRER_MIT_ANTEIL));
	}

	/** Fehlt der Anteil in der Datenbank, bleibt die Lehrkraft in der Map und zählt mit 0.0. */
	@Test
	void kurslehrerOhneAnteilZaehltMitNull() {
		gebeKurslehrerVor(List.of(dtoKursLehrer(ID_LEHRER_OHNE_ANTEIL, null)));

		final Map<Long, Double> wochenstunden = repository.kurslehrerWochenstunden(ID_KURS);

		assertTrue(wochenstunden.containsKey(ID_LEHRER_OHNE_ANTEIL));
		assertEquals(0.0, wochenstunden.get(ID_LEHRER_OHNE_ANTEIL));
	}

	/** Ein leerer Anteil bei einer Lehrkraft lässt die Anteile der übrigen Lehrkräfte des Kurses unberührt. */
	@Test
	void leererAnteilLaesstDieUebrigenLehrkraefteUnberuehrt() {
		gebeKurslehrerVor(List.of(dtoKursLehrer(ID_LEHRER_OHNE_ANTEIL, null), dtoKursLehrer(ID_LEHRER_MIT_ANTEIL, 3.5)));

		final Map<Long, Double> wochenstunden = repository.kurslehrerWochenstunden(ID_KURS);

		assertEquals(2, wochenstunden.size());
		assertEquals(0.0, wochenstunden.get(ID_LEHRER_OHNE_ANTEIL));
		assertEquals(3.5, wochenstunden.get(ID_LEHRER_MIT_ANTEIL));
	}

	/** Führt die Datenbank zu einem Kurs keine zusätzlichen Lehrkräfte, ist die Map leer. */
	@Test
	void kursOhneZusaetzlicheLehrkraefteLiefertLeereMap() {
		gebeKurslehrerVor(List.of());

		assertTrue(repository.kurslehrerWochenstunden(ID_KURS).isEmpty());
	}

}
