package de.nrw.schule.svws.core.abschluss.gost.belegpruefung;

import java.util.List;

import de.nrw.schule.svws.core.abschluss.gost.AbiturdatenManager;
import de.nrw.schule.svws.core.abschluss.gost.GostBelegpruefung;
import de.nrw.schule.svws.core.abschluss.gost.GostBelegpruefungsArt;
import de.nrw.schule.svws.core.abschluss.gost.GostBelegungsfehler;
import de.nrw.schule.svws.core.data.gost.AbiturFachbelegung;
import de.nrw.schule.svws.core.types.gost.GostFachbereich;
import de.nrw.schule.svws.core.types.gost.GostHalbjahr;
import de.nrw.schule.svws.core.types.gost.GostSchriftlichkeit;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse gruppiert alle Belegprüfungen für einen Schüler für die Prüfung der EF1 bzw. 
 * für die Gesamtprüfungen, welche im Bereich der Naturwissenschaften durchgeführt werden.
 */
public class Naturwissenschaften extends GostBelegpruefung {

	/// Die Belegungen für alle Fächer der Naturwissenschaften
	private List<@NotNull AbiturFachbelegung> naturwissenschaften;

	/// Die Belegungen für alle Fächer der klassischen Naturwissenschaften
	private List<@NotNull AbiturFachbelegung> naturwissenschaften_klassisch;
	
	// Die Anzahl der durchgehenden bzw. potentiell durchgehenden Belegungen - mündlich und schriftlich (für die Schwerpunktberechnung)
	private int anzahl_durchgehend;
	
	// Die Anzahl der durchgehenden bzw. potentiell durchgehenden Belegungen - nur schriftlich (für die Schwerpunktberechnung)
	private int anzahl_schriftlich_durchgehend;
	
	
	/**
	 * Erstellt eine neue Belegprüfung für das Fach Mathematik.
	 * 
	 * @param manager         der Daten-Manager für die Abiturdaten
	 * @param pruefungs_art   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 */
	public Naturwissenschaften(final @NotNull AbiturdatenManager manager, final @NotNull GostBelegpruefungsArt pruefungs_art) {
		super(manager, pruefungs_art);
	}

	
	@Override
	protected void init() {
		naturwissenschaften = manager.getFachbelegungen(GostFachbereich.NATURWISSENSCHAFTLICH);
		naturwissenschaften_klassisch = manager.getFachbelegungen(GostFachbereich.NATURWISSENSCHAFTLICH_KLASSISCH);
		anzahl_durchgehend = 0;
		anzahl_schriftlich_durchgehend = 0;
	}

	
	@Override
	protected void pruefeEF1() {
		// Wurde eine durchgehend belegbare klassische Naturwissenschaft in EF.1 belegt?
		if (!manager.pruefeBelegungDurchgehendBelegbarExistiert(naturwissenschaften_klassisch, GostSchriftlichkeit.BELIEBIG, GostHalbjahr.EF1))
			addFehler(GostBelegungsfehler.NW_10);
		// Wurde eine klassische Naturwissenschaft in EF.1 schriftlich belegt?
		if (!manager.pruefeBelegungExistiertMitSchriftlichkeitEinzeln(naturwissenschaften_klassisch, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1))
			addFehler(GostBelegungsfehler.NW_11);

		// Zähle die durchgehend belegbaren Belegungen
		List<@NotNull AbiturFachbelegung> fachbelegungen = manager.filterDurchgehendBelegbar(naturwissenschaften);
		fachbelegungen = manager.filterBelegungen(fachbelegungen, GostHalbjahr.EF1);
		anzahl_durchgehend = fachbelegungen == null ? 0 : fachbelegungen.size();

		// und nun die schriftlich belegten durchgehend belegbaren Belegungen
		fachbelegungen = manager.filterBelegungenMitSchriftlichkeit(fachbelegungen, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1);
		anzahl_schriftlich_durchgehend = fachbelegungen == null ? 0 : fachbelegungen.size();
	}

	
	@Override
	protected void pruefeGesamt() {
		// Wurde eine klassische Naturwissenschaft durchgehend belegt?
		if (!manager.pruefeBelegungExistiert(naturwissenschaften_klassisch, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22))
			addFehler(GostBelegungsfehler.NW_10);

		// Prüfe, ob in beiden Halbjahren der EF mindestens eine klassische Naturwissenschaft schriftlich belegt wurde.
		if ((!manager.pruefeBelegungExistiertMitSchriftlichkeitEinzeln(naturwissenschaften_klassisch, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1)) ||
			(!manager.pruefeBelegungExistiertMitSchriftlichkeitEinzeln(naturwissenschaften_klassisch, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF2)))
			addFehler(GostBelegungsfehler.NW_11);

		// Zähle die durchgehend belegten Fachbelegungen
		anzahl_durchgehend = manager.zaehleBelegungenDurchgaengig(naturwissenschaften);
		
		// und nun die durchgehend schriftlich belegten Naturwissenschaften (also in Q1.1 bis Q2.1 schriftlich)
		anzahl_schriftlich_durchgehend = manager.zaehleBelegungenDurchgaengigSchriftlichInQPhase(naturwissenschaften);
	}

	
	/**
	 * Gibt die Anzahl der durchgehend belegten bzw. belegbaren Naturwissenschaften zurück.
	 * 
	 * @return die Anzahl der durchgehend belegten bzw. belegbaren Naturwissenschaften zurück.
	 */
	public int getAnzahlDurchgehendBelegt() {
		return anzahl_durchgehend;
	}

	
	/**
	 * Gibt die Anzahl der durchgehend schriftlich belegten bzw. belegbaren Naturwissenschaften zurück.
	 * Durchgehend schriftlich bedeutet, dass das Fach mind. von Q1.1 bus Q2.1 schriftlich belegt wurde.
	 * 
	 * @return die Anzahl der durchgehend schriftlich belegten bzw. belegbaren Naturwissenschaften zurück.
	 */
	public int getAnzahlDurchgehendSchritflichBelegt() {
		return anzahl_schriftlich_durchgehend;
	}
	
}
