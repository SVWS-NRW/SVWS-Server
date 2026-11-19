package de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030;

import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefung;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt;
import de.svws_nrw.core.abschluss.gost.GostBelegungsfehler;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.types.gost.GostAbiturFach;
import de.svws_nrw.core.types.gost.GostBesondereLernleistung;
import de.svws_nrw.core.utils.gost.GostFachUtils;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse gruppiert die Belegprüfungen für einen Schüler für die Prüfung der EF1 bzw. für die Gesamtprüfungen,
 * welche sich auf eine besondere Lernleistung beziehen (§ 12 (3) S. 4, § 17, § 36 (1) APO-GOSt).
 * Ob bei einer besonderen Lernleistung im Projektkurs ein Projektkurs existiert, wird bei den Projektkursen geprüft (PF_15).
 */
public final class Abi30BelegpruefungBLL extends GostBelegpruefung {

	/**
	 * Erstellt eine neue Belegprüfung für die besondere Lernleistung.
	 *
	 * @param manager        der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 */
	public Abi30BelegpruefungBLL(final @NotNull AbiturdatenManager manager, final @NotNull GostBelegpruefungsArt pruefungsArt) {
		super(manager, pruefungsArt);
	}


	@Override
	protected void init() {
		// Keine Initialisierung notwendig
	}


	@Override
	protected void pruefeEF1() {
		// nichts zu tun, da die besondere Lernleistung in der EF.1 noch nicht festgelegt wird
	}


	@Override
	protected void pruefeGesamt() {
		final boolean istExtern = GostBesondereLernleistung.EXTERNE.is(manager.daten().besondereLernleistung);
		final boolean istImProjektkurs = manager.istProjektKursBesondereLernleistung();
		if (!istExtern && !istImProjektkurs) {
			return;
		}
		// Bestimme, ob ein Projektkurs belegt und ob ein Projektkurs als 5. Abiturfach gewählt ist
		boolean hatProjektkurs = false;
		boolean istProjektkursAbi5 = false;
		for (final @NotNull AbiturFachbelegung fachbelegung : manager.getRelevanteFachbelegungen()) {
			final GostFach fach = manager.getFach(fachbelegung);
			if ((fach == null) || !GostFachUtils.istProjektkurs(fach)) {
				continue;
			}
			if (manager.zaehleBelegung(fachbelegung) > 0) {
				hatProjektkurs = true;
			}
			if (GostAbiturFach.fromID(fachbelegung.abiturFach) == GostAbiturFach.AB5) {
				istProjektkursAbi5 = true;
			}
		}
		// Bei einer externen besonderen Lernleistung ist deren Fach das 5. Abiturfach und nicht ein Projektkurs
		if (istExtern && istProjektkursAbi5) {
			addFehler(GostBelegungsfehler.GOST30_BLL_10);
		}
		// Bei einer besonderen Lernleistung im Projektkurs muss der Projektkurs das 5. Abiturfach sein.
		// Fehlt der Projektkurs, so wird dies bereits bei den Projektkursen gemeldet (PF_15).
		if (istImProjektkurs && hatProjektkurs && !istProjektkursAbi5) {
			addFehler(GostBelegungsfehler.GOST30_BLL_11);
		}
	}

}
