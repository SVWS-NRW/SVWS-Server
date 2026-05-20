/**
 *
 */
package de.svws_nrw.asd.export.aggregation;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.svws_nrw.asd.data.statistik.KlassenStatistikGesamt;
import de.svws_nrw.asd.data.statistik.SchuelerLernabschnittStatistikGesamt;
import de.svws_nrw.asd.data.statistik.SchuelerStatistikGesamt;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.asd.types.schule.Schulform;

/*
 * UvdKey.java
 *
 * Copyright (c) 2026 Projekt ZeBrAS - Zentrale Bearbeitung der amtlichen Schuldaten
 *
 * Landesbetrieb Information und Technik Nordrhein-Westfalen (IT.NRW)
 * Alle Rechte vorbehalten.
 *
 * Versionshistorie
 * @version 1.00 - 29.04.2026 - Vorname Nachname (knitt01) - erste Version
 */

/**
 * Die Klasse UvdKey ist eine Klasse im Paket de.svws_nrw.asd.export.aggregation des Projekts ZeBrAS.
 *
 * @since 2026
 * @version 1.00 - 29.04.2026
 * @author Vorname Nachname (knitt01)
 *
 */
public class UvdKey {

	/**
	 * Bei A-Schulen der zweistellige Jahrgang und bei BK-Schulen das Klassenkürzel
	 */
	String klasse;
	/**
	 *
	 */
	String bildungsgang;
	/**
	 *
	 */
	String kursart;

	/**
	 * Konstruktor
	 * @param schueler
	 * @param klassenStatistikGesamt
	 * @param idSchuljahresabschnitt
	 * @param jahrgangIds
	 * @param schulform
	 * @param fehlerliste
	 *
	 */
	public UvdKey(final SchuelerStatistikGesamt schueler, final Map<Long, KlassenStatistikGesamt> klassenStatistikGesamt, final long idSchuljahresabschnitt,
			final Map<Long, Long> jahrgangIds, final Schulform schulform, final List<String> fehlerliste) {
		final SchuelerLernabschnittStatistikGesamt lernabschnitt = AggregationUtils.ermittelnLernabschnitt(schueler, idSchuljahresabschnitt);
		String aktJahrgang = Jahrgaenge.data().getSchluesselByIDOrNull(jahrgangIds.get(lernabschnitt.idJahrgang));

		if (aktJahrgang == null) {
			aktJahrgang = "";
			fehlerliste.add("Bei folgendem Schüler konnte kein Jahrgang ermittelt werden: " + schueler.id + " JahrgangsID: " + lernabschnitt.idJahrgang);
		}

		klasse = "";
		bildungsgang = "";
		kursart = "";

		if (Schulform.BK.equals(schulform) || Schulform.SB.equals(schulform)) {
			klasse = klassenStatistikGesamt.get(lernabschnitt.idKlasse).kuerzel;

			if (klasse == null) {
				klasse = "";
				fehlerliste
						.add("Beim Schüler mit der ID: " + schueler.id + " konnte zu folgender Klasse kein Kürzel ermittelt werden: " + lernabschnitt.idKlasse);
			}
		} else {
			klasse = aktJahrgang;
//			kursart = schueler

			if (!Schulform.WB.equals(schulform)) {

			}

		}

	}

	@Override
	public final int hashCode() {
		return Objects.hash(bildungsgang, klasse, kursart);
	}

	@Override
	public final boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final UvdKey other = (UvdKey) obj;
		return Objects.equals(bildungsgang, other.bildungsgang) && Objects.equals(klasse, other.klasse) && Objects.equals(kursart, other.kursart);
	}

	@Override
	public final String toString() {
		return "UvdKey [klasse=" + klasse + ", bildungsgang=" + bildungsgang + ", kursart=" + kursart + "]";
	}


}
