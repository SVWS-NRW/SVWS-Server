package de.svws_nrw.core.utils.gost;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import de.svws_nrw.core.data.gost.GostFachwahl;
import de.svws_nrw.core.types.gost.GostKursart;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Manager verwaltet den Zugriff auf die Fachwahlinformationen
 * eines Abiturjahrgangs der gymnasialen Oberstufe.
 */
public class GostFachwahlManager {

	/** Die Liste mit den einzelnen Fachwahlen */ 
	private final @NotNull Vector<@NotNull GostFachwahl> fachwahlen = new Vector<>();

	/** Eine Map, mit einer Zuordnung der Schüler-IDs zu der FachID und der Kursart */
	private final @NotNull HashMap<@NotNull Long, @NotNull HashMap<@NotNull GostKursart, @NotNull HashSet<@NotNull Long>>> mapFachKursart = new HashMap<>();

	/** Eine Map, mit einer Zuordnung der Fachwahlen zu der FachID */
	private final @NotNull HashMap<@NotNull Long, @NotNull Vector<@NotNull GostFachwahl>> mapFach = new HashMap<>(); 

	/** Eine Map, mit einer Zuordnung der Fachwahlen zu der Schüler-ID */
	private final @NotNull HashMap<@NotNull Long, @NotNull Vector<@NotNull GostFachwahl>> mapSchueler = new HashMap<>(); 
	
	
	/**
	 * Erzeugt einen leeren Fachwahl-Manager
	 */
	public GostFachwahlManager() {
	}


	/**
	 * Erzeugt einen neuen Fachwahl-Manager mit den übergebenen Fachwahlen
	 * 
	 * @param fachwahlen   die Fachwahlen
	 */
	public GostFachwahlManager(final @NotNull List<@NotNull GostFachwahl> fachwahlen) {
		for (final @NotNull GostFachwahl fw : fachwahlen)
			add(fw);
	}


	/**
	 * Fügt eine weitere Fachwahl zu dem Manager hinzu
	 * 
	 * @param fachwahl   die hinzuzufügende Fachwahl
	 */
	public void add(final GostFachwahl fachwahl) {
		if (fachwahl == null)
			return;
		
		fachwahlen.add(fachwahl);
		
		Vector<@NotNull GostFachwahl> fwFach = mapFach.get(fachwahl.fachID);
		if (fwFach == null) {
			fwFach = new Vector<>();
			mapFach.put(fachwahl.fachID, fwFach);
		}
		fwFach.add(fachwahl);
		
		Vector<@NotNull GostFachwahl> fwSchueler = mapSchueler.get(fachwahl.schuelerID);
		if (fwSchueler == null) {
			fwSchueler = new Vector<>();
			mapSchueler.put(fachwahl.schuelerID, fwSchueler);
		}
		fwSchueler.add(fachwahl);

		HashMap<@NotNull GostKursart, @NotNull HashSet<@NotNull Long>> mapKursart = mapFachKursart.get(fachwahl.fachID);
		if (mapKursart == null) {
			mapKursart = new HashMap<>();
			mapFachKursart.put(fachwahl.fachID, mapKursart);
		}
		final GostKursart kursart = GostKursart.fromFachwahlOrException(fachwahl);
		HashSet<@NotNull Long> schueler = mapKursart.get(kursart);
		if (schueler == null) {
			schueler = new HashSet<>();
			mapKursart.put(kursart, schueler);
		}
		schueler.add(fachwahl.schuelerID);
	}


	/**
	 * Ermittelt die Fachwahlen zu der übergebenen Fach-ID.
	 * Sind keine Fachwahlen vorhanden, so wird ein leerer Vektor 
	 * zurückgegeben.
	 * 
	 * @param idFach   die ID des Faches
	 * 
	 * @return die Liste der Fachwahlen des Faches
	 */
	public @NotNull List<@NotNull GostFachwahl> getFachwahlen(final long idFach) {
		final Vector<@NotNull GostFachwahl> fwFach = mapSchueler.get(idFach);
		return (fwFach == null) ? new Vector<>() : fwFach;
	}


	/**
	 * Ermittelt die Fachwahlen zu der übergebenen Schüler ID.
	 * Sind keine Fachwahlen vorhanden, so wird ein leerer Vektor zurückgegeben.
	 * 
	 * @param idSchueler   die ID des Schülers
	 * 
	 * @return die Liste der Fachwahlen des Schülers
	 */
	public @NotNull List<@NotNull GostFachwahl> getSchuelerFachwahlen(final long idSchueler) {
		final Vector<@NotNull GostFachwahl> fwSchueler = mapSchueler.get(idSchueler);
		return (fwSchueler == null) ? new Vector<>() : fwSchueler;
	}


	/**
	 * Prüft, ob eine Fachwahl mit dem angegebenen Schüler, Fach und der angegebenen Kursart existiert.
	 *    
	 * @param idSchueler   die ID des Schülers
	 * @param idFach       die ID des Faches
	 * @param kursart      die Kursart der gymnasialen Oberstufe
	 * 
	 * @return true, falls die Fachwahl existiert und ansonsten false
	 */
	public boolean hatFachwahl(final long idSchueler, final long idFach, final @NotNull GostKursart kursart) {
		final HashMap<@NotNull GostKursart, @NotNull HashSet<@NotNull Long>> mapKursart = mapFachKursart.get(idFach);
		if (mapKursart == null)
			return false;
		final HashSet<@NotNull Long> schueler = mapKursart.get(kursart);
		if (schueler == null)
			return false;
		return schueler.contains(idSchueler);
	}

}
