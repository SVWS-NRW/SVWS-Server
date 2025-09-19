package de.svws_nrw.core.abschluss.bk.d;

import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.schule.BeruflichesGymnasiumStundentafel;
import de.svws_nrw.asd.data.schule.BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit;
import de.svws_nrw.asd.types.schule.BeruflichesGymnasiumPruefungsordnungAnlage;
import de.svws_nrw.core.types.gost.GostAbiturFach;
import jakarta.validation.constraints.NotNull;

/**
 * Der Belegprüfungsalgorithmus für den Bildungsgang der Schulgliederung D01
 * und der Fachklasse 106 00.
 */
public final class BKGymBelegpruefungD3 extends BKGymBelegpruefung {

	/**
	 * Erzeugt einen neue Belegprüfung
	 *
	 * @param manager   der Manager für die Abiturdaten
	 */
	public BKGymBelegpruefungD3(@NotNull final BKGymAbiturdatenManager manager) {
		super(manager);
	}

	@Override
	public void pruefe() {
		// Prüfe die Abiturfächer darauf, ob diese bei mehreren Fachbelegungen eingetragen sind

		// Bestimme die Leistungskursbelegungen. Diese müssen für die weitere Prüfung vorhanden sein.
		final BKGymAbiturFachbelegung lk1 = manager.getAbiFachbelegung(GostAbiturFach.LK1);
		if (lk1 == null)
			addFehler(BKGymBelegungsfehler.LK_1);
		final BKGymAbiturFachbelegung lk2 = manager.getAbiFachbelegung(GostAbiturFach.LK2);
		if (lk2 == null)
			addFehler(BKGymBelegungsfehler.LK_2);

		// Bestimme zunächst die möglichen Stundentafeln laut der Anlage und der beiden belegten Leistungskurse.
		final @NotNull List<BeruflichesGymnasiumStundentafel> mglStundentafeln =
				manager.getStundentafelByLeistungskurse(BeruflichesGymnasiumPruefungsordnungAnlage.D3);
		if (mglStundentafeln.isEmpty())
			addFehler(BKGymBelegungsfehler.LK_3);

		// Bestimme das dritte Abiturfach
		final BKGymAbiturFachbelegung ab3 = manager.getAbiFachbelegung(GostAbiturFach.AB3);
		if (ab3 == null)
			addFehler(BKGymBelegungsfehler.AB_3);

		// Bestimme das vierte Abiturfach
		final BKGymAbiturFachbelegung ab4 = manager.getAbiFachbelegung(GostAbiturFach.AB4);
		if (ab4 == null)
			addFehler(BKGymBelegungsfehler.AB_4);

		// Verlasse die Prüfung, wenn bis hier keine Variante gefunden wurde
		if (mglStundentafeln.isEmpty())
			return;

		// Prüfe die Wahlmöglichkeiten bei den Varianten in Bezug auf AB3 und AB4, verlasse ggf. die Prüfung, wenn keine Wahlmöglichkeit für AB3 und AB4 gefunden wurde
		final @NotNull Map<BeruflichesGymnasiumStundentafel, BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit> mapWahlmoeglichkeiten = manager.getWahlmoeglichekiten(mglStundentafeln);
		if (mapWahlmoeglichkeiten.isEmpty()) {
			addFehler(BKGymBelegungsfehler.AB_5);
			return;
		}


		// 1. Abiturfachkombination prüfen und Zuordnung der Abiturfachs zum Fach (O punkte als nicht belegt)

		// 2. Schritt identifikation der Variante:

		// 3. restliche Fachbelegung prüfen
	}

}
