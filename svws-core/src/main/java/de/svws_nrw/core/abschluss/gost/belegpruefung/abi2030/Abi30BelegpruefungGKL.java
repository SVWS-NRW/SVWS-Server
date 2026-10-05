package de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefung;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt;
import de.svws_nrw.core.abschluss.gost.GostBelegungsfehler;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.data.gost.AbiturFachbelegungHalbjahr;
import de.svws_nrw.core.data.gost.GostSchuelerGKLWahl;
import de.svws_nrw.core.types.gost.GostFachbereich;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.types.gost.GostSchriftlichkeit;
import de.svws_nrw.core.utils.gost.GostLaufbahnplanungGKLKlausurvorgabe;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse führt für einen Schüler die Belegprüfungen für die gleichwertigen komplexen Leistungsnachweise (GKL)
 * aus.
 */
public final class Abi30BelegpruefungGKL extends GostBelegpruefung {

	/** die Schülerwahlen für die gleichwertigen komplexen Leistungsnachweise (GKL) */
	private final @NotNull GostSchuelerGKLWahl gklWahlen;

	/** eine Map mit der Klausurvorgaben zugeordnet zu ihrer ID */
	private final @NotNull Map<Long, GostLaufbahnplanungGKLKlausurvorgabe> mapKlausurvorgaben;

	/** Die Belegung im Aufgabenfeld 1 */
	private @NotNull List<AbiturFachbelegung> belegungenAF1 = new ArrayList<>();

	/** Die Belegung im Aufgabenfeld 2 (mit Religion) */
	private @NotNull List<AbiturFachbelegung> belegungenAF2 = new ArrayList<>();

	/** Die Belegung im Aufgabenfeld 3 */
	private @NotNull List<AbiturFachbelegung> belegungenAF3 = new ArrayList<>();

	/** Die für GKLs möglichen Halbjahre in der Qualifikationsphase */
	private static final @NotNull List<GostHalbjahr> gklHalbjahreQ = List.of(GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21);

	/** Eine Map, welche den Fach-IDs eine Menge von Halbjahren in der QPhase zuordnet, wo eine GKL möglich ist. */
	private final @NotNull Map<Long, Set<GostHalbjahr>> mapGKLMoeglichQPhase = new HashMap<>();

	/**
	 * Erstellt eine neue Belegprüfung für die gleichwertigen komplexen Leistungsnachweise (GKL).
	 *
	 * @param manager              der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt         die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 * @param gklWahlen            die Schülerwahlen für die gleichwertigen komplexen Leistungsnachweise (GKL)
	 * @param mapKlausurvorgaben   eine Map mit der Klausurvorgaben zugeordnet zu ihrer ID
	 */
	public Abi30BelegpruefungGKL(final @NotNull AbiturdatenManager manager, final @NotNull GostBelegpruefungsArt pruefungsArt,
			final @NotNull GostSchuelerGKLWahl gklWahlen, final @NotNull Map<Long, GostLaufbahnplanungGKLKlausurvorgabe> mapKlausurvorgaben) {
		super(manager, pruefungsArt);
		this.gklWahlen = gklWahlen;
		this.mapKlausurvorgaben = mapKlausurvorgaben;
	}

	@Override
	protected void init() {
		belegungenAF1 = manager.getRelevanteFachbelegungen(GostFachbereich.SPRACHLICH_LITERARISCH_KUENSTLERISCH);
		belegungenAF2 = manager.getRelevanteFachbelegungen(GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH_MIT_RELIGION);
		belegungenAF3 = manager.getRelevanteFachbelegungen(GostFachbereich.MATHEMATISCH_NATURWISSENSCHAFTLICH);
		for (final @NotNull GostLaufbahnplanungGKLKlausurvorgabe vorgabe : mapKlausurvorgaben.values()) {
			if (vorgabe.getVorgabe().istGklMoeglich && gklHalbjahreQ.contains(vorgabe.getHalbjahr())) {
				final Set<GostHalbjahr> setHalbjahre = mapGKLMoeglichQPhase.computeIfAbsent(vorgabe.getFach().id, k -> new HashSet<GostHalbjahr>());
				if (setHalbjahre != null) {
					setHalbjahre.add(vorgabe.getHalbjahr());
				}
			}
		}
	}

	@Override
	protected void pruefeEF1() {
		// nichts zu tun
	}

	private static boolean hatSchriftlicheGKBelegungInEF(final @NotNull List<AbiturFachbelegung> belegungen) {
		for (final @NotNull AbiturFachbelegung bel : belegungen) {
			for (final GostHalbjahr halbjahr : GostHalbjahr.getEinfuehrungsphase()) {
				final AbiturFachbelegungHalbjahr belHj = bel.belegungen[halbjahr.id];
				if ((belHj == null) || (GostKursart.fromKuerzel(belHj.kursartKuerzel) != GostKursart.GK)) {
					continue;
				}
				if (belHj.schriftlich) {
					return true;
				}
			}
		}
		return false;
	}

	private void pruefeKlausurvorgabenBelegung(final long idVorgabe, final @NotNull GostBelegungsfehler fehler) {
		final GostLaufbahnplanungGKLKlausurvorgabe v = mapKlausurvorgaben.get(idVorgabe);
		if (v == null) {
			addFehler(fehler);
		} else {
			final AbiturFachbelegung bel = manager.getFachbelegungByID(v.getFach().id);
			if ((!v.getVorgabe().istGklMoeglich) || !manager.pruefeBelegungMitKursart(bel, GostKursart.GK, v.getHalbjahr())
					|| !manager.pruefeBelegungMitSchriftlichkeitEinzeln(bel, GostSchriftlichkeit.SCHRIFTLICH, v.getHalbjahr())) {
				addFehler(fehler);
			}
		}
	}

	private void pruefeGKLEinfuehrungsphaseAF1() {
		if (gklWahlen.idKlausurvorgabeEF_Sprachen == null) {
			if (hatSchriftlicheGKBelegungInEF(belegungenAF1)) {
				addFehler(GostBelegungsfehler.GOST30_GKL_EF_AF1);
			}
		} else {
			pruefeKlausurvorgabenBelegung(gklWahlen.idKlausurvorgabeEF_Sprachen, GostBelegungsfehler.GOST30_GKL2_EF_AF1);
		}
	}

	private void pruefeGKLEinfuehrungsphaseAF2() {
		if (gklWahlen.idKlausurvorgabeEF_GW == null) {
			if (hatSchriftlicheGKBelegungInEF(belegungenAF2)) {
				addFehler(GostBelegungsfehler.GOST30_GKL_EF_AF2);
			}
		} else {
			pruefeKlausurvorgabenBelegung(gklWahlen.idKlausurvorgabeEF_GW, GostBelegungsfehler.GOST30_GKL2_EF_AF2);
		}
	}

	private void pruefeGKLEinfuehrungsphaseAF3() {
		if (gklWahlen.idKlausurvorgabeEF_NW == null) {
			if (hatSchriftlicheGKBelegungInEF(belegungenAF3)) {
				addFehler(GostBelegungsfehler.GOST30_GKL_EF_AF3);
			}
		} else {
			pruefeKlausurvorgabenBelegung(gklWahlen.idKlausurvorgabeEF_NW, GostBelegungsfehler.GOST30_GKL2_EF_AF3);
		}
	}

	private boolean hatSchriftlicheGKBelegungInQMitGKLMoeglich(final @NotNull List<AbiturFachbelegung> belegungen) {
		for (final @NotNull AbiturFachbelegung bel : belegungen) {
			Set<GostHalbjahr> setHalbjahreMoeglich = mapGKLMoeglichQPhase.get(bel.fachID);
			if (setHalbjahreMoeglich == null) {
				setHalbjahreMoeglich = new HashSet<GostHalbjahr>();
			}
			for (final GostHalbjahr halbjahr : gklHalbjahreQ) {
				final AbiturFachbelegungHalbjahr belHj = bel.belegungen[halbjahr.id];
				if ((belHj == null) || (GostKursart.fromKuerzel(belHj.kursartKuerzel) != GostKursart.GK)) {
					continue;
				}
				if (belHj.schriftlich && setHalbjahreMoeglich.contains(halbjahr)) {
					return true;
				}
			}
		}
		return false;
	}

	private void pruefeGKLQualifikationsphaseAF1() {
		if (gklWahlen.idKlausurvorgabeQ_Sprachen == null) {
			if (hatSchriftlicheGKBelegungInQMitGKLMoeglich(belegungenAF1)) {
				addFehler(GostBelegungsfehler.GOST30_GKL_Q_AF1);
			}
		} else {
			pruefeKlausurvorgabenBelegung(gklWahlen.idKlausurvorgabeQ_Sprachen, GostBelegungsfehler.GOST30_GKL2_Q_AF1);
		}
	}

	private void pruefeGKLQualifikationsphaseAF2() {
		if (gklWahlen.idKlausurvorgabeQ_GW == null) {
			if (hatSchriftlicheGKBelegungInQMitGKLMoeglich(belegungenAF2)) {
				addFehler(GostBelegungsfehler.GOST30_GKL_Q_AF2);
			}
		} else {
			pruefeKlausurvorgabenBelegung(gklWahlen.idKlausurvorgabeQ_GW, GostBelegungsfehler.GOST30_GKL2_Q_AF2);
		}
	}

	private void pruefeGKLQualifikationsphaseAF3() {
		if (gklWahlen.idKlausurvorgabeQ_NW == null) {
			if (hatSchriftlicheGKBelegungInQMitGKLMoeglich(belegungenAF3)) {
				addFehler(GostBelegungsfehler.GOST30_GKL_Q_AF3);
			}
		} else {
			pruefeKlausurvorgabenBelegung(gklWahlen.idKlausurvorgabeQ_NW, GostBelegungsfehler.GOST30_GKL2_Q_AF3);
		}
	}

	@Override
	protected void pruefeGesamt() {
		pruefeGKLEinfuehrungsphaseAF1();
		pruefeGKLEinfuehrungsphaseAF2();
		pruefeGKLEinfuehrungsphaseAF3();
		pruefeGKLQualifikationsphaseAF1();
		pruefeGKLQualifikationsphaseAF2();
		pruefeGKLQualifikationsphaseAF3();
	}

}
