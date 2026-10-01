package de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefung;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt;
import de.svws_nrw.core.abschluss.gost.GostBelegungsfehler;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.types.gost.GostFachbereich;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse gruppiert alle Belegprüfungen für einen Schüler für die Prüfung der EF1 bzw.
 * für die Gesamtprüfungen, welche sich auf die Einbringungsverpflichtungen von Kursen beziehen und schon bei der Belegprüfung relevant sind.
 */
public final class Abi30BelegpruefungEinbringung extends GostBelegpruefung {

	/** Die Belegungen für das Fach Sport. */
	private @NotNull List<AbiturFachbelegung> sport = new ArrayList<>();

	/** Die Belegung des Faches Geschichte - oder null, falls es nicht belegt wurde. */
	private @NotNull List<AbiturFachbelegung> geschichte = new ArrayList<>();

	/** Die Belegung des Faches Sozialwissenschaften - oder null, falls es nicht belegt wurde. */
	private @NotNull List<AbiturFachbelegung> sozialwissenschaften = new ArrayList<>();

	/** Die Belegungen für das Fach Kunst. */
	private @NotNull List<AbiturFachbelegung> kunst = new ArrayList<>();

	/** Die Belegungen für das Fach Musik. */
	private @NotNull List<AbiturFachbelegung> musik = new ArrayList<>();

	/** Die Belegungen für das Fach Philosophie. */
	private @NotNull List<AbiturFachbelegung> philosophie = new ArrayList<>();

	/** Die Belegung im Gesellschaftwissenschaftlichen Bereich */
	private @NotNull List<AbiturFachbelegung> gesellschaftswissenschaftenReligion = new ArrayList<>();


	/**
	 * Erstellt eine neue Belegprüfung für Anzahl der einzubringenden Kurse. Die kann ggf. zu hoch sein.
	 *
	 * @param manager                 der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt           die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 * @param pruefungProjektkurse    das Ergebnis für die Belegprüfung der Projektkurse
	 */
	public Abi30BelegpruefungEinbringung(final @NotNull AbiturdatenManager manager, final @NotNull GostBelegpruefungsArt pruefungsArt,
			final @NotNull GostBelegpruefung pruefungProjektkurse) {
		super(manager, pruefungsArt, pruefungProjektkurse);
	}

	@Override
	protected void init() {
		sport = manager.getRelevanteFachbelegungen(GostFachbereich.SPORT);
		geschichte = manager.getRelevanteFachbelegungen(GostFachbereich.GESCHICHTE);
		sozialwissenschaften = manager.getRelevanteFachbelegungen(GostFachbereich.SOZIALWISSENSCHAFTEN);
		kunst = manager.getRelevanteFachbelegungenByFach(Fach.KU);
		musik = manager.getRelevanteFachbelegungenByFach(Fach.MU);
		philosophie = manager.getRelevanteFachbelegungen(GostFachbereich.PHILOSOPHIE);
		gesellschaftswissenschaftenReligion = manager.getRelevanteFachbelegungen(GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH_MIT_RELIGION);
	}

	@Override
	protected void pruefeEF1() {
		// nichts zu tun
	}

	@Override
	protected void pruefeGesamt() {
		final boolean hatSportAbi = (sport.size() == 1) && (sport.get(0).abiturFach != null);
		final int anzahlGE = manager.zaehleBelegungInHalbjahren(geschichte, GostHalbjahr.getQualifikationsphase());
		final int anzahlSW = manager.zaehleBelegungInHalbjahren(sozialwissenschaften, GostHalbjahr.getQualifikationsphase());

		// Die problematische Situation kann sich nur durch eine minimale Belegung bei Geschichte und Sozialwissenschaften sowie Sport im Abitur ergeben.
		if (hatSportAbi && (anzahlGE == 2) && (anzahlSW == 2)) {

			// Wenn zusätzlich Kunst im Abitur gewählt wurde dann sind zwei Kurse zu viel einbringungspflichtig
			final boolean hatKunstAbi = (kunst.size() == 1) && (kunst.get(0).abiturFach != null);
			if (hatKunstAbi) {
				addFehler(GostBelegungsfehler.GOST30_EINBR_2);
				return;
			}

			// Wenn zusätzlich Musik im Abitur gewählt wurde dann sind zwei Kurse zu viel einbringungspflichtig
			final boolean hatMusikAbi = (musik.size() == 1) && (musik.get(0).abiturFach != null);
			if (hatMusikAbi) {
				addFehler(GostBelegungsfehler.GOST30_EINBR_3);
				return;
			}

			// Wenn zusätzlich ein Projektkurs im Abitur gewählt wurde, wessen Leitfach nur in der Q1 belegt wurde, dann sind zwei Kurse zu viel einbringungspflichtig
			final @NotNull Abi30BelegpruefungProjektkurse pruefungProjektkurse = ((@NotNull Abi30BelegpruefungProjektkurse) pruefungen_vorher[0]);
			final AbiturFachbelegung projektkurs = pruefungProjektkurse.getProjektkurs();
			final boolean hatPjkAbi = (projektkurs != null) && (projektkurs.abiturFach != null);
			final AbiturFachbelegung leitfach = (projektkurs == null) || (projektkurs.idReferenzfach == null) ? null
					: manager.getFachbelegungByID(projektkurs.idReferenzfach);
			final int anzahlPjkLeitfach = (leitfach == null) ? 0 : manager.zaehleHalbjahresbelegungen(leitfach, GostHalbjahr.getQualifikationsphase());
			if ((hatPjkAbi) && (anzahlPjkLeitfach == 2)) {
				addFehler(GostBelegungsfehler.GOST30_EINBR_4);
				return;
			}

			// Wenn zusätzlich zwei weitere Gesellschaftswissenschaften im Abitur gewählt wurden, dann sind zwei Kurse zu viel einbringungspflichtig
			int anzahlGWAbi = 0;
			int anzahlREAbi = 0;
			for (final @NotNull AbiturFachbelegung bel : gesellschaftswissenschaftenReligion) {
				final GostFach fach = manager.faecher().get(bel.fachID);
				if ((fach == null) || "GE".equals(fach.kuerzel) || "SW".equals(fach.kuerzel)) {
					continue;
				}
				if (bel.abiturFach != null) {
					if (GostFachbereich.RELIGION.hat(fach)) {
						anzahlREAbi++;
					} else {
						anzahlGWAbi++;
					}
				}
			}
			if (anzahlGWAbi == 2) {
				addFehler(GostBelegungsfehler.GOST30_EINBR_5);
			}
			if ((anzahlGWAbi == 1) && (anzahlREAbi == 1)) {
				addFehler(GostBelegungsfehler.GOST30_EINBR_6);
			}
		}
	}


}
