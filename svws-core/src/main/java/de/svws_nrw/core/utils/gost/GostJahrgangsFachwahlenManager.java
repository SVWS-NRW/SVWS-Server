package de.svws_nrw.core.utils.gost;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.gost.GostFachwahl;
import de.svws_nrw.core.data.gost.GostJahrgangFachwahlen;
import de.svws_nrw.core.data.gost.GostJahrgangFachwahlenHalbjahr;
import de.svws_nrw.core.types.gost.GostAbiturFach;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zur Handhabung von Daten des Typs {@link GostJahrgangFachwahlen}.
 */
public class GostJahrgangsFachwahlenManager {

	/** (Fach-ID, Abifach [1=LK,3,4]) --> ArrayList<Schueler-ID> = Eine Liste der Schüler, welche das angegeben Fach als Abiturfach den entsprechenden Typs (siehe ID von {@link GostAbiturFach}) haben. */
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Integer, @NotNull List<@NotNull Long>> _map2D_fachID_abifachID_schuelerID = new HashMap2D<>();

	/** (Fach-ID, Halbjahres-ID [0..5]) --> ArrayList<Schueler-ID> = Eine Liste der Schüler, welche das angegeben Fach in dem Halbjahr als Leistungskurs gewählt haben. */
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Integer, @NotNull List<@NotNull Long>> _map2D_lk_fachID_halbjahrID_schuelerID = new HashMap2D<>();

	/** (Fach-ID, Halbjahres-ID [0..5]) --> ArrayList<Schueler-ID> = Eine Liste der Schüler, welche das angegeben Fach in dem Halbjahr als Grundkurs (oder PJK oder VTF) gewählt haben. */
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Integer, @NotNull List<@NotNull Long>> _map2D_gk_fachID_halbjahrID_schuelerID = new HashMap2D<>();

	/** (Fach-ID, Halbjahres-ID [0..5]) --> ArrayList<Schueler-ID> = Eine Liste der Schüler, welche das angegeben Fach in dem Halbjahr als schriftlichen Grundkurs gewählt haben. */
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Integer, @NotNull List<@NotNull Long>> _map2D_gk_schriftlich_fachID_halbjahrID_schuelerID = new HashMap2D<>();

	/** (Fach-ID, Halbjahres-ID [0..5]) --> ArrayList<Schueler-ID> = Eine Liste der Schüler, welche das angegeben Fach in dem Halbjahr als mündlichen Grundkurs (oder PJK oder VTF) gewählt haben. */
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Integer, @NotNull List<@NotNull Long>> _map2D_gk_muendlich_fachID_halbjahrID_schuelerID = new HashMap2D<>();

	/** (Fach-ID, Halbjahres-ID [0..5]) --> ArrayList<Schueler-ID> = Eine Liste der Schüler, welche das angegeben Fach in dem Halbjahr als Zusatzkurs gewählt haben. */
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Integer, @NotNull List<@NotNull Long>> _map2D_zk_fachID_halbjahrID_schuelerID = new HashMap2D<>();



	/**
	 * Erstellt einen neuen Manager mit den übergebenen Fachwahlen.
	 *
	 * @param jgFachwahlen   die Fachwahlen des Abiturjahrgangs
	 */
	public GostJahrgangsFachwahlenManager(final @NotNull GostJahrgangFachwahlen jgFachwahlen) {
		init(jgFachwahlen);
	}

	/**
	 * Initialisiert den Manager mit den übergebenen Fachwahlen
	 *
	 * @param jgFachwahlen   die Fachwahlen des Abiturjahrgangs
	 */
	private void init(final @NotNull GostJahrgangFachwahlen jgFachwahlen) {
		// Durchwandere die Fachwahlen aller Halbjahre
		for (final GostHalbjahr halbjahr : GostHalbjahr.values()) {
			final GostJahrgangFachwahlenHalbjahr fwHalbjahr = jgFachwahlen.halbjahr[halbjahr.id];
			if (fwHalbjahr != null) {
				for (final GostFachwahl fw : fwHalbjahr.fachwahlen) {
					final GostKursart kursart = GostKursart.fromID(fw.kursartID);
					if (kursart == GostKursart.LK) {
						// Leistungskurs
						List<@NotNull Long> schuelerListe = _map2D_lk_fachID_halbjahrID_schuelerID.getOrNull(fw.fachID, halbjahr.id);
						if (schuelerListe == null) {
							schuelerListe = new ArrayList<>();
							_map2D_lk_fachID_halbjahrID_schuelerID.put(fw.fachID, halbjahr.id, schuelerListe);
						}
						schuelerListe.add(fw.schuelerID);
					} else if ((kursart == GostKursart.GK) || (kursart == GostKursart.PJK) || (kursart == GostKursart.VTF)) {
						// Grundkurs gesamt
						List<@NotNull Long> schuelerListe = _map2D_gk_fachID_halbjahrID_schuelerID.getOrNull(fw.fachID, halbjahr.id);
						if (schuelerListe == null) {
							schuelerListe = new ArrayList<>();
							_map2D_gk_fachID_halbjahrID_schuelerID.put(fw.fachID, halbjahr.id, schuelerListe);
						}
						schuelerListe.add(fw.schuelerID);
						if (fw.istSchriftlich) {
							// Grundkurs schriftlich
							schuelerListe = _map2D_gk_schriftlich_fachID_halbjahrID_schuelerID.getOrNull(fw.fachID, halbjahr.id);
							if (schuelerListe == null) {
								schuelerListe = new ArrayList<>();
								_map2D_gk_schriftlich_fachID_halbjahrID_schuelerID.put(fw.fachID, halbjahr.id, schuelerListe);
							}
							schuelerListe.add(fw.schuelerID);
						} else {
							// Grundkurs muendlich
							schuelerListe = _map2D_gk_muendlich_fachID_halbjahrID_schuelerID.getOrNull(fw.fachID, halbjahr.id);
							if (schuelerListe == null) {
								schuelerListe = new ArrayList<>();
								_map2D_gk_muendlich_fachID_halbjahrID_schuelerID.put(fw.fachID, halbjahr.id, schuelerListe);
							}
							schuelerListe.add(fw.schuelerID);
						}
					} else if (kursart == GostKursart.ZK) {
						// Zusatzkurs
						List<@NotNull Long> schuelerListe = _map2D_zk_fachID_halbjahrID_schuelerID.getOrNull(fw.fachID, halbjahr.id);
						if (schuelerListe == null) {
							schuelerListe = new ArrayList<>();
							_map2D_zk_fachID_halbjahrID_schuelerID.put(fw.fachID, halbjahr.id, schuelerListe);
						}
						schuelerListe.add(fw.schuelerID);
					}
				}
			}
		}
		// Durchwandere die Fachwahlen bezüglich der Abiturfächer
		for (final GostFachwahl fw : jgFachwahlen.abitur.fachwahlen) {
			final GostKursart kursart = GostKursart.fromID(fw.kursartID);
			GostAbiturFach abiFach = GostAbiturFach.LK1;
			if (kursart == GostKursart.GK)
				abiFach = fw.istSchriftlich ? GostAbiturFach.AB3 : GostAbiturFach.AB4;
			List<@NotNull Long> schuelerListe = _map2D_fachID_abifachID_schuelerID.getOrNull(fw.fachID, abiFach.id);
			if (schuelerListe == null) {
				schuelerListe = new ArrayList<>();
				_map2D_fachID_abifachID_schuelerID.put(fw.fachID, abiFach.id, schuelerListe);
			}
			schuelerListe.add(fw.schuelerID);
		}
	}

	/**
	 * Gibt die Schüler-Menge für das angegebene Fach zurück, welche das Fach als das angebene Abiturfach gewählt haben.
	 * Bei der Anfrage bezüglich der Wahl als erstes oder zweites Fach werden immer alle LK-Fachwahlen zurückgegeben.
	 *
	 * @param idFach    die ID des Faches
	 * @param abifach   das Abiturfach (bei LKs werden jeweils alle Fachwahlen genommen)
	 *
	 * @return die Menge der Schüler, welche das Fach als Abiturfach des angegebenen Typs gewählt haben
	 */
	public @NotNull List<@NotNull Long> schuelerGetMengeByFachAndAbifachAsListOrException(final long idFach, final @NotNull GostAbiturFach abifach) {
		int idAbifach = abifach.id;
		if (idAbifach == 2)
			idAbifach = 1;
		final List<@NotNull Long> schuelerListe = _map2D_fachID_abifachID_schuelerID.getOrNull(idFach, idAbifach);
		if (schuelerListe != null)
			return schuelerListe;
		return new ArrayList<>();
	}


	/**
	 * Gibt die Schüler-Menge für das angegebene Fach in dem angegeben Halbjahr zurück, welche das Fach
	 * als Leistungskurs gewählt haben.
	 *
	 * @param idFach     die ID des Faches
	 * @param halbjahr   das Halbjahr der gymnasialen Oberstufe
	 *
	 * @return die Menge der Schüler, welche das Fach in dem Halbjahr als Leistungskurs gewählt haben
	 */
	public @NotNull List<@NotNull Long> schuelerGetMengeLKByFachAndHalbjahrAsListOrException(final long idFach, final @NotNull GostHalbjahr halbjahr) {
		final List<@NotNull Long> schuelerListe = _map2D_lk_fachID_halbjahrID_schuelerID.getOrNull(idFach, halbjahr.id);
		if (schuelerListe != null)
			return schuelerListe;
		return new ArrayList<>();
	}


	/**
	 * Gibt die Schüler-Menge für das angegebene Fach in dem angegeben Halbjahr zurück, welche das Fach
	 * als Grundkurs gewählt haben.
	 * Dabei werden auch Projektkurse und Vertiefungskurse mitgezählt.
	 *
	 * @param idFach     die ID des Faches
	 * @param halbjahr   das Halbjahr der gymnasialen Oberstufe
	 *
	 * @return die Menge der Schüler, welche das Fach in dem Halbjahr als Grundkurs gewählt haben
	 */
	public @NotNull List<@NotNull Long> schuelerGetMengeGKByFachAndHalbjahrAsListOrException(final long idFach, final @NotNull GostHalbjahr halbjahr) {
		final List<@NotNull Long> schuelerListe = _map2D_gk_fachID_halbjahrID_schuelerID.getOrNull(idFach, halbjahr.id);
		if (schuelerListe != null)
			return schuelerListe;
		return new ArrayList<>();
	}


	/**
	 * Gibt die Schüler-Menge für das angegebene Fach in dem angegeben Halbjahr zurück, welche das Fach
	 * als schriftlichen Grundkurs gewählt haben.
	 *
	 * @param idFach     die ID des Faches
	 * @param halbjahr   das Halbjahr der gymnasialen Oberstufe
	 *
	 * @return die Menge der Schüler, welche das Fach in dem Halbjahr als schriftlichen Grundkurs gewählt haben
	 */
	public @NotNull List<@NotNull Long> schuelerGetMengeGKSchriftlichByFachAndHalbjahrAsListOrException(final long idFach, final @NotNull GostHalbjahr halbjahr) {
		final List<@NotNull Long> schuelerListe = _map2D_gk_schriftlich_fachID_halbjahrID_schuelerID.getOrNull(idFach, halbjahr.id);
		if (schuelerListe != null)
			return schuelerListe;
		return new ArrayList<>();
	}


	/**
	 * Gibt die Schüler-Menge für das angegebene Fach in dem angegeben Halbjahr zurück, welche das Fach
	 * als mündlichen Grundkurs gewählt haben.
	 * Dabei werden auch Projektkurse und Vertiefungskurse mitgezählt.
	 *
	 * @param idFach     die ID des Faches
	 * @param halbjahr   das Halbjahr der gymnasialen Oberstufe
	 *
	 * @return die Menge der Schüler, welche das Fach in dem Halbjahr als mündlichen Grundkurs gewählt haben
	 */
	public @NotNull List<@NotNull Long> schuelerGetMengeGKMuendlichByFachAndHalbjahrAsListOrException(final long idFach, final @NotNull GostHalbjahr halbjahr) {
		final List<@NotNull Long> schuelerListe = _map2D_gk_muendlich_fachID_halbjahrID_schuelerID.getOrNull(idFach, halbjahr.id);
		if (schuelerListe != null)
			return schuelerListe;
		return new ArrayList<>();
	}


	/**
	 * Gibt die Schüler-Menge für das angegebene Fach in dem angegeben Halbjahr zurück, welche das Fach
	 * als Zusatzkurs gewählt haben.
	 *
	 * @param idFach     die ID des Faches
	 * @param halbjahr   das Halbjahr der gymnasialen Oberstufe
	 *
	 * @return die Menge der Schüler, welche das Fach in dem Halbjahr als Zusatzkurs gewählt haben
	 */
	public @NotNull List<@NotNull Long> schuelerGetMengeZKByFachAndHalbjahrAsListOrException(final long idFach, final @NotNull GostHalbjahr halbjahr) {
		final List<@NotNull Long> schuelerListe = _map2D_zk_fachID_halbjahrID_schuelerID.getOrNull(idFach, halbjahr.id);
		if (schuelerListe != null)
			return schuelerListe;
		return new ArrayList<>();
	}

}
