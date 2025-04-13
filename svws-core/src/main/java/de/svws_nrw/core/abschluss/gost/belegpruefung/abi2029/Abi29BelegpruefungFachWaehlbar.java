package de.svws_nrw.core.abschluss.gost.belegpruefung.abi2029;

import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefung;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt;
import de.svws_nrw.core.abschluss.gost.GostBelegungsfehler;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.data.gost.AbiturFachbelegungHalbjahr;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import jakarta.validation.constraints.NotNull;

/*
 * Bei dieser Implementierung handelt es sich um eine Umsetzung in Bezug auf möglichen zukünftigen
 * Änderungen in der APO-GOSt. Diese basiert auf der aktuellen Implementierung und integriert Aspekte
 * aus dem Eckpunktepapier und auf in den Schulleiterdienstbesprechungen erläuterten Vorhaben.
 * Sie dient der Evaluierung von möglichen Umsetzungsvarianten und als Vorbereitung einer späteren
 * Implementierung der Belegprüfung. Insbesondere sollen erste Versuche mit Laufbahnen mit einem
 * 5. Abiturfach und Projektkursen erprobt werden. Detailaspekte können erst nach Erscheinen der APO-GOSt
 * umgesetzt werden.
 * Es handelt sich also um experimentellen Code, der keine Rückschlüsse auf Details einer zukünftigen APO-GOSt
 * erlaubt.
 */
/**
 * Diese Klasse gruppiert alle Belegprüfungen für einen Schüler für die Prüfung der EF1 bzw.
 * für die Gesamtprüfungen, welche in Bezug auf die schul-spezifische Wählbarkeit von Fächern
 * in dem Abiturjahrgang durchgeführt werden.
 */
public final class Abi29BelegpruefungFachWaehlbar extends GostBelegpruefung {

	/**
	 * Erstellt eine neue Belegprüfung für die schul-spezifische Wählbarkeit von Fächern.
	 *
	 * @param manager        der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 */
	public Abi29BelegpruefungFachWaehlbar(final @NotNull AbiturdatenManager manager, final @NotNull GostBelegpruefungsArt pruefungsArt) {
		super(manager, pruefungsArt);
	}


	@Override
	protected void init() {
		// mache nichts...
	}


	private void pruefeFachbelegungHalbjahr(final @NotNull GostFach fach, final @NotNull AbiturFachbelegung fachbelegung,
			final @NotNull GostHalbjahr halbjahr) {
		final AbiturFachbelegungHalbjahr fbHalbjahr = fachbelegung.belegungen[halbjahr.id];
		if (fbHalbjahr == null)
			return;
		final boolean istwaehlbar = switch (halbjahr) {
			case EF1 -> fach.istMoeglichEF1;
			case EF2 -> fach.istMoeglichEF2;
			case Q11 -> fach.istMoeglichQ11;
			case Q12 -> fach.istMoeglichQ12;
			case Q21 -> fach.istMoeglichQ21;
			case Q22 -> fach.istMoeglichQ22;
			default -> false;
		};
		if (!istwaehlbar)
			addFehler(GostBelegungsfehler.WAEHLBARKEIT_1);
	}


	private static boolean hatLKFachbelegung(final @NotNull AbiturFachbelegung fachbelegung) {
		for (final @NotNull GostHalbjahr halbjahr : GostHalbjahr.getQualifikationsphase()) {
			final AbiturFachbelegungHalbjahr fbHalbjahr = fachbelegung.belegungen[halbjahr.id];
			if ((fbHalbjahr != null) && "LK".equals(fbHalbjahr.kursartKuerzel))
				return true;
		}
		return false;
	}


	private void pruefeFachbelegungAbitur(final @NotNull GostFach fach, final @NotNull AbiturFachbelegung fachbelegung) {
		if (fachbelegung.abiturFach == null)
			return;
		// Prüfe auf LK
		if ((!fach.istMoeglichAbiLK) && ((fachbelegung.abiturFach == 1) || (fachbelegung.abiturFach == 2) || (hatLKFachbelegung(fachbelegung))))
			addFehler(GostBelegungsfehler.WAEHLBARKEIT_3);
		// Prüfe auf GK
		if ((!fach.istMoeglichAbiGK) && ((fachbelegung.abiturFach == 3) || (fachbelegung.abiturFach == 4) || (fachbelegung.abiturFach == 5)))
			addFehler(GostBelegungsfehler.WAEHLBARKEIT_2);
	}


	@Override
	protected void pruefeEF1() {
		for (final @NotNull AbiturFachbelegung fachbelegung : manager.getFachbelegungen()) {
			final GostFach fach = manager.faecher().get(fachbelegung.fachID);
			if (fach == null) {
				addFehler(GostBelegungsfehler.WAEHLBARKEIT_0);
				continue;
			}
			pruefeFachbelegungHalbjahr(fach, fachbelegung, GostHalbjahr.EF1);
		}
	}

	@Override
	protected void pruefeGesamt() {
		for (final @NotNull AbiturFachbelegung fachbelegung : manager.getFachbelegungen()) {
			final GostFach fach = manager.faecher().get(fachbelegung.fachID);
			if (fach == null) {
				addFehler(GostBelegungsfehler.WAEHLBARKEIT_0);
				continue;
			}
			for (final @NotNull GostHalbjahr halbjahr : GostHalbjahr.values())
				pruefeFachbelegungHalbjahr(fach, fachbelegung, halbjahr);
			pruefeFachbelegungAbitur(fach, fachbelegung);
		}
	}

}
