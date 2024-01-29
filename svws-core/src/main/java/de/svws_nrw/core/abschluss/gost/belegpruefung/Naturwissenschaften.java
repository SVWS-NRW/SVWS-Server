package de.svws_nrw.core.abschluss.gost.belegpruefung;

import java.util.List;

import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefung;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt;
import de.svws_nrw.core.abschluss.gost.GostBelegungsfehler;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.types.gost.GostFachbereich;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostSchriftlichkeit;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse gruppiert alle Belegprüfungen für einen Schüler für die Prüfung der EF1 bzw.
 * für die Gesamtprüfungen, welche im Bereich der Naturwissenschaften durchgeführt werden.
 */
public final class Naturwissenschaften extends GostBelegpruefung {

	/// Die Belegungen für alle Fächer der Naturwissenschaften
	private List<@NotNull AbiturFachbelegung> _naturwissenschaften;

	/// Die Belegungen für alle Fächer der klassischen Naturwissenschaften
	private List<@NotNull AbiturFachbelegung> _naturwissenschaftenKlassisch;

	// Die Anzahl der durchgehenden bzw. potentiell durchgehenden Belegungen - mündlich und schriftlich (für die Schwerpunktberechnung)
	private int _anzahlDurchgehend;

	// Die Anzahl der durchgehenden bzw. potentiell durchgehenden Belegungen - nur schriftlich (für die Schwerpunktberechnung)
	private int _anzahlDurchgehendSchriftlich;


	/**
	 * Erstellt eine neue Belegprüfung für das Fach Mathematik.
	 *
	 * @param manager        der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 */
	public Naturwissenschaften(final @NotNull AbiturdatenManager manager, final @NotNull GostBelegpruefungsArt pruefungsArt) {
		super(manager, pruefungsArt);
	}


	@Override
	protected void init() {
		_naturwissenschaften = manager.getRelevanteFachbelegungen(GostFachbereich.NATURWISSENSCHAFTLICH);
		_naturwissenschaftenKlassisch = manager.getRelevanteFachbelegungen(GostFachbereich.NATURWISSENSCHAFTLICH_KLASSISCH);
		_anzahlDurchgehend = 0;
		_anzahlDurchgehendSchriftlich = 0;
	}


	@Override
	protected void pruefeEF1() {
		// Wurde eine durchgehend belegbare klassische Naturwissenschaft in EF.1 belegt?
		if (!manager.pruefeBelegungDurchgehendBelegbarExistiert(_naturwissenschaftenKlassisch, GostSchriftlichkeit.BELIEBIG, GostHalbjahr.EF1))
			addFehler(GostBelegungsfehler.NW_10);
		// Wurde eine klassische Naturwissenschaft in EF.1 schriftlich belegt?
		if (!manager.pruefeBelegungExistiertMitSchriftlichkeitEinzeln(_naturwissenschaftenKlassisch, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1))
			addFehler(GostBelegungsfehler.NW_11);

		// Zähle die durchgehend belegbaren Belegungen
		List<@NotNull AbiturFachbelegung> fachbelegungen = manager.filterDurchgehendBelegbar(_naturwissenschaften);
		fachbelegungen = manager.filterBelegungen(fachbelegungen, GostHalbjahr.EF1);
		_anzahlDurchgehend = fachbelegungen == null ? 0 : fachbelegungen.size();

		// und nun die schriftlich belegten durchgehend belegbaren Belegungen
		fachbelegungen = manager.filterBelegungenMitSchriftlichkeit(fachbelegungen, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1);
		_anzahlDurchgehendSchriftlich = fachbelegungen == null ? 0 : fachbelegungen.size();
	}


	@Override
	protected void pruefeGesamt() {
		// Wurde eine klassische Naturwissenschaft durchgehend belegt?
		if (!manager.pruefeBelegungExistiert(_naturwissenschaftenKlassisch, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22))
			addFehler(GostBelegungsfehler.NW_10);

		// Prüfe, ob in beiden Halbjahren der EF mindestens eine klassische Naturwissenschaft schriftlich belegt wurde.
		if ((!manager.pruefeBelegungExistiertMitSchriftlichkeitEinzeln(_naturwissenschaftenKlassisch, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1))
				|| (!manager.pruefeBelegungExistiertMitSchriftlichkeitEinzeln(_naturwissenschaftenKlassisch, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF2)))
			addFehler(GostBelegungsfehler.NW_11);

		// Zähle die durchgehend belegten Fachbelegungen
		_anzahlDurchgehend = manager.zaehleBelegungenDurchgaengig(_naturwissenschaften);

		// und nun die durchgehend schriftlich belegten Naturwissenschaften (also in Q1.1 bis Q2.1 schriftlich)
		_anzahlDurchgehendSchriftlich = manager.zaehleBelegungenDurchgaengigSchriftlichInQPhase(_naturwissenschaften);
	}


	/**
	 * Gibt die Anzahl der durchgehend belegten bzw. belegbaren Naturwissenschaften zurück.
	 *
	 * @return die Anzahl der durchgehend belegten bzw. belegbaren Naturwissenschaften zurück.
	 */
	public int getAnzahlDurchgehendBelegt() {
		return _anzahlDurchgehend;
	}


	/**
	 * Gibt die Anzahl der durchgehend schriftlich belegten bzw. belegbaren Naturwissenschaften zurück.
	 * Durchgehend schriftlich bedeutet, dass das Fach mind. von Q1.1 bus Q2.1 schriftlich belegt wurde.
	 *
	 * @return die Anzahl der durchgehend schriftlich belegten bzw. belegbaren Naturwissenschaften zurück.
	 */
	public int getAnzahlDurchgehendSchritflichBelegt() {
		return _anzahlDurchgehendSchriftlich;
	}

}
