package de.svws_nrw.core.utils.gost;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.gost.GostFachwahl;
import de.svws_nrw.core.data.gost.GostJahrgangFachwahlen;
import de.svws_nrw.core.types.gost.GostAbiturFach;
import de.svws_nrw.core.types.gost.GostKursart;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zur Handhabung von Daten des Typs {@link GostJahrgangFachwahlen}.
 */
public class GostJahrgangsFachwahlenManager {

	/** (Fach-ID, Abifach [1=LK,3,4]) --> ArrayList<Schueler-ID> = Eine Liste der Schüler, welche das angegeben Fach als Abiturfach den entsprechenden Typs (siehe ID von {@link GostAbiturFach}) haben. */
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Integer, @NotNull List<@NotNull Long>> _map2D_fachID_abifachID_schuelerID = new HashMap2D<>();

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
		// TODO interpretiere die Halbjahres-Belegungen
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

}
