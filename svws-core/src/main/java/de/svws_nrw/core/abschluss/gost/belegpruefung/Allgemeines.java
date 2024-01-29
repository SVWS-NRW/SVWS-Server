package de.svws_nrw.core.abschluss.gost.belegpruefung;

import java.util.List;

import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefung;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt;
import de.svws_nrw.core.abschluss.gost.GostBelegungsfehler;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.types.gost.GostAbiturFach;
import de.svws_nrw.core.types.gost.GostFachbereich;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostSchriftlichkeit;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse bündelt allgemeine Belegprüfungen für einen Schüler für die Prüfung der EF1 bzw.
 * für die Gesamtprüfungen.
 */
public final class Allgemeines extends GostBelegpruefung {

	/**
	 * Erstellt eine neue allgemeine Belegprüfung.
	 *
	 * @param manager        der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 */
	public Allgemeines(final @NotNull AbiturdatenManager manager, final @NotNull GostBelegpruefungsArt pruefungsArt) {
		super(manager, pruefungsArt);
	}


	@Override
	protected void init() {
		// Keine Initialisierung notwendig
	}


	@Override
	protected void pruefeEF1() {
		// Prüfe, ob mehrere Religionsfächer belegt wurden.
		if (manager.zaehleBelegungInHalbjahren(manager.getRelevanteFachbelegungen(GostFachbereich.RELIGION), GostHalbjahr.EF1) > 1)
			addFehler(GostBelegungsfehler.IGF_10);

		// Prüfe anhand des Statistikkürzels, ob inhaltsgleiche Fächer in der EF.1 mehrfach belegt wurden.
		if (manager.hatDoppelteFachbelegungInHalbjahr(GostHalbjahr.EF1))
			addFehler(GostBelegungsfehler.IGF_10);
	}


	@Override
	protected void pruefeGesamt() {
		final @NotNull List<@NotNull AbiturFachbelegung> alleFachbelegungen = manager.getRelevanteFachbelegungen();

		// Prüfe, ob die Fächer seit EF.1 bis zur Abwahl durchgängig belegt wurden - ignoriere Ausnahmen nach Kursart (Zusatz-, Vertiefungs- und Projektkurse) sowie Literatur, instrumental- und vokalpraktische Kurse sowie Religion und Philosophie
		for (int i = 0; i < alleFachbelegungen.size(); i++) {
			final AbiturFachbelegung fachbelegung = alleFachbelegungen.get(i);
			if (!manager.istBelegtSeitEF(fachbelegung))
				addFehler(GostBelegungsfehler.E1BEL_10);
		}

		// Prüfe, ob alle Fächer nicht-Abiturfächern in Q2.2 mündlich belegt sind. Das 4. Abiturfach wird gesondert geprüft
		for (int i = 0; i < alleFachbelegungen.size(); i++) {
			final AbiturFachbelegung fachbelegung = alleFachbelegungen.get(i);
			final GostAbiturFach abiturFach = GostAbiturFach.fromID(fachbelegung.abiturFach);
			if (abiturFach != null)
				continue;
			if (manager.pruefeBelegungMitSchriftlichkeitEinzeln(fachbelegung, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q22)) {
				addFehler(GostBelegungsfehler.ABI_16);
				break;
			}
		}

		// Prüfe, ob in einem Halbjahr mehrere Religionsfächer belegt wurden.
		for (final GostHalbjahr halbjahr : GostHalbjahr.values()) {
			if (manager.zaehleBelegungInHalbjahren(manager.getRelevanteFachbelegungen(GostFachbereich.RELIGION), halbjahr) > 1)
				addFehler(GostBelegungsfehler.IGF_10);
		}

		// Prüfe, ob in einem Halbjahr zwei Fächer (außer Vertiefungsfächer) mit dem gleichen Statistik-Kürzel belegt wurden.
		if (manager.hatDoppelteFachbelegung(GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22))
			addFehler(GostBelegungsfehler.IGF_10);
	}


}
