package de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefung;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt;
import de.svws_nrw.core.abschluss.gost.GostBelegungsfehler;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.types.gost.GostAbiturFach;
import de.svws_nrw.core.types.gost.GostFachbereich;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse gruppiert alle Belegprüfungen für einen Schüler für die Prüfung der EF1 bzw.
 * für die Gesamtprüfungen, welche sich auf die Einbringungsverpflichtungen von Kursen beziehen und schon bei der Belegprüfung relevant sind.
 * Für die Einbringungsverpflichtung dürfen zwingend nur 36 Kurse eingebracht werden. Es gibt jedoch zahlreiche Fälle, wodurch die Zahl
 * überschritten wird. Dies wird in dieser Belegprüfung schrittweise geprüft...
 */
public final class Abi30BelegpruefungEinbringung extends GostBelegpruefung {

	private final @NotNull Abi30BelegpruefungProjektkurse pruefungProjektkurse;
	private final @NotNull Abi30BelegpruefungSchwerpunkt pruefungSchwerpunkt;
	private final @NotNull Abi30BelegpruefungAbiFaecher pruefungAbiFaecher;
	private final @NotNull Abi30BelegpruefungKurszahlenUndWochenstunden pruefungKurszahlenUndWochenstunden;

	/** Die maximale Anzahl der Kurse die während der Berechnung noch einbringungspflichtig sein können. */
	private int kurseOberereGrenze = -1;

	/** Die minimale Anzahl der Kurse die - soweit bereits erkannt - einbringungspflichtig sind. */
	private int kurseUntereGrenze = -1;

	/** Gibt an, ob der gewählte Projektkurs Abiturfach ist oder nicht. */
	private boolean hatPjkAbi;

	/** Das Referenzfach des Projektkurses */
	private @NotNull GostFach referenzfach = new GostFach();

	/** Gibt an, ob bereits eine durchgängige Gesellschaftwissenschaft gefunden wurde. */
	private boolean hatGWDurchgaengig;

	/** Die Belegung des Faches Sport */
	private @NotNull List<AbiturFachbelegung> sport = new ArrayList<>();

	/** Die anzahl der in der Q-Phase belegten Sport-Kurse*/
	private int anzahlSportKurse = 0;


	/**
	 * Erstellt eine neue Belegprüfung für Anzahl der einzubringenden Kurse. Die kann ggf. zu hoch sein.
	 *
	 * @param manager                der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt           die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 * @param pruefungProjektkurse   das Ergebnis für die Belegprüfung der Projektkurse
	 * @param pruefungSchwerpunkt    das Ergebnis für die Belegprüfung zum Schwerpunkt
	 * @param pruefungAbiFaecher     das Ergebnis für die Belegprüfung zu den Abitur-Fächern
	 * @param pruefungKurszahlenUndWochenstunden   das Ergebnis für die Belegprüfung zu den Kurszahlen und Wochenstunden
	 */
	public Abi30BelegpruefungEinbringung(final @NotNull AbiturdatenManager manager, final @NotNull GostBelegpruefungsArt pruefungsArt,
			final @NotNull Abi30BelegpruefungProjektkurse pruefungProjektkurse, final @NotNull Abi30BelegpruefungSchwerpunkt pruefungSchwerpunkt,
			final @NotNull Abi30BelegpruefungAbiFaecher pruefungAbiFaecher,
			final @NotNull Abi30BelegpruefungKurszahlenUndWochenstunden pruefungKurszahlenUndWochenstunden) {
		super(manager, pruefungsArt);
		this.pruefungProjektkurse = pruefungProjektkurse;
		this.pruefungSchwerpunkt = pruefungSchwerpunkt;
		this.pruefungAbiFaecher = pruefungAbiFaecher;
		this.pruefungKurszahlenUndWochenstunden = pruefungKurszahlenUndWochenstunden;
	}

	@Override
	protected void init() {
		// Bestimme für die spätere Prüfung den Projektkurs und das zugehörige Leitfach
		final AbiturFachbelegung projektkurs = pruefungProjektkurse.getProjektkurs();
		final AbiturFachbelegung referenzfachBelegung = (projektkurs == null) || (projektkurs.idReferenzfach == null) ? null
				: manager.getFachbelegungByID(projektkurs.idReferenzfach);
		hatPjkAbi = (projektkurs != null) && (projektkurs.abiturFach != null);
		final GostFach refFach = manager.getFach(referenzfachBelegung);
		if (refFach != null) {
			referenzfach = refFach;
		}

		hatGWDurchgaengig = hatAbifachGWOhnePJK();

		sport = manager.getRelevanteFachbelegungen(GostFachbereich.SPORT);
		anzahlSportKurse = manager.zaehleBelegungInHalbjahrenOhneAT(sport, GostHalbjahr.getQualifikationsphase());
	}

	@Override
	protected void pruefeEF1() {
		// nichts zu tun
	}

	private boolean hatAbifachGWOhnePJK() {
		for (final @NotNull GostAbiturFach abifach : GostAbiturFach.values()) {
			final AbiturFachbelegung bel = pruefungAbiFaecher.getAbiturfach(abifach);
			final GostFach fach = manager.getFach(bel);
			if (fach == null) {
				continue;
			}
			if (GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH.hat(fach)) {
				return true;
			}
		}
		return false;
	}

	private void pruefeSport() {
		final boolean hatSportAbi = (sport.size() == 1) && (sport.get(0).abiturFach != null);
		final boolean hatSportAbiPJK = hatPjkAbi && GostFachbereich.SPORT.hat(referenzfach);
		if (hatSportAbi) {
			kurseUntereGrenze += 4;
		} else if (hatSportAbiPJK) {
			kurseUntereGrenze += 2;
			kurseOberereGrenze -= 2;
		} else {
			kurseOberereGrenze -= 4;
		}
	}

	private void pruefeVertiefungskurse() {
		final @NotNull List<AbiturFachbelegung> vertiefungskurse = manager.getRelevanteFachbelegungen(GostFachbereich.VERTIEFUNGSKURSE);
		final int anzahlVT = manager.zaehleBelegungInHalbjahren(vertiefungskurse, GostHalbjahr.getQualifikationsphase());
		kurseOberereGrenze -= anzahlVT;
	}

	private void pruefeFremdsprachenUndNaturwissenschaften() {
		// Zähle die naturwissenschaftlichen Kurse und prüfe, ob Informatik, Ernährungslehre oder Technik ein Abiturfach ist
		final @NotNull List<AbiturFachbelegung> listNwNeu = manager.getRelevanteFachbelegungen(GostFachbereich.NATURWISSENSCHAFTLICH_NEU_EINSETZEND);
		boolean hatNwNeuAbi = false;
		int anzahlKurseNwPjkAbi = 0;
		int anzahlKurseNw = 0;
		int anzahlNwAbi = 0;
		for (final AbiturFachbelegung nwNeu : listNwNeu) {
			final int anzahl = manager.zaehleHalbjahresbelegungen(nwNeu, GostHalbjahr.getQualifikationsphase());
			anzahlKurseNw += anzahl;
			if ((anzahl == 4) && (nwNeu.abiturFach != null)) {
				hatNwNeuAbi = true;
				anzahlNwAbi++;
			} else if (hatPjkAbi && (nwNeu.fachID == referenzfach.id)) {
				anzahlKurseNwPjkAbi += anzahl;
			}
		}

		// Prüfe dann den Bereich der klassischen Naturwissenschaften und zähle die weiteren Abiturfächer in diesem Bereich
		final @NotNull List<AbiturFachbelegung> listNwKl = manager.getRelevanteFachbelegungen(GostFachbereich.NATURWISSENSCHAFTLICH_KLASSISCH);
		for (final AbiturFachbelegung nwKl : listNwKl) {
			final int anzahl = manager.zaehleHalbjahresbelegungen(nwKl, GostHalbjahr.getQualifikationsphase());
			anzahlKurseNw += anzahl;
			if ((anzahl == 4) && (nwKl.abiturFach != null)) {
				anzahlNwAbi++;
			} else if (hatPjkAbi && (nwKl.fachID == referenzfach.id)) {
				anzahlKurseNwPjkAbi += anzahl;
			}
		}

		// Prüfe Fremdsprachenbelegung
		final @NotNull List<AbiturFachbelegung> listFS = manager.getRelevanteFachbelegungen(GostFachbereich.FREMDSPRACHE);
		int anzahlKurseFs = 0;
		int anzahlFsAbi = 0;
		int anzahlKurseFsPjkAbi = 0;
		for (final AbiturFachbelegung fs : listFS) {
			final int anzahl = manager.zaehleHalbjahresbelegungen(fs, GostHalbjahr.getQualifikationsphase());
			anzahlKurseFs += anzahl;
			if ((anzahl == 4) && (fs.abiturFach != null)) {
				anzahlFsAbi++;
			}
			if (hatPjkAbi && (fs.fachID == referenzfach.id)) {
				anzahlKurseFsPjkAbi += anzahl;
			}
		}

		// Korrektur der Grenzen anhand der gezählten Kurse
		kurseUntereGrenze += 8;   // 1 Fach in FS bzw. NW wird immer einbracht
		anzahlKurseFs -= 4;
		anzahlKurseNw -= 4;
		// Beim einem Schwerpunktfach kommt ggf. zu zwei weiteren Pflichtbelegungen...
		if (pruefungSchwerpunkt.hatNurSchwerpunktFS()) {
			if ((anzahlFsAbi == 2) || ((anzahlFsAbi == 1) && (anzahlKurseFsPjkAbi > 1))) {
				kurseUntereGrenze += 2;
				anzahlKurseFs -= 2;
			}
		} else if (pruefungSchwerpunkt.hatNurSchwerpunktNW()) {
			if (hatNwNeuAbi || (anzahlNwAbi == 2) || ((anzahlNwAbi == 1) && (anzahlKurseNwPjkAbi > 1))) {
				kurseUntereGrenze += 2;
				anzahlKurseNw -= 2;
			}
		} else {
			if ((anzahlFsAbi == 2) || ((anzahlFsAbi == 1) && (anzahlKurseFsPjkAbi > 1))) {
				kurseUntereGrenze += 2;
				anzahlKurseFs -= 2;
			}
			if (hatNwNeuAbi || (anzahlNwAbi == 2) || ((anzahlNwAbi == 1) && (anzahlKurseNwPjkAbi > 1))) {
				kurseUntereGrenze += 2;
				anzahlKurseNw -= 2;
			}
		}
		// Spezialfall: Ein NW-Fach wird fünftes Fach über PJK hat selber aber weniger als 4 Kurse -> Kein Schwerpunktfach...
		if ((anzahlKurseNwPjkAbi > 1) && (anzahlKurseNwPjkAbi < 4)) {
			kurseUntereGrenze += 2;
			anzahlKurseNw -= 2;
		}
		// Spezialfall: Ein FS-Fach wird fünftes Fach über PJK hat selber aber weniger als 4 Kurse -> Kein Schwerpunktfach...
		if ((anzahlKurseFsPjkAbi > 1) && (anzahlKurseFsPjkAbi < 4)) {
			kurseUntereGrenze += 2;
			anzahlKurseFs -= 2;
		}
		// Zähle die zwei Pflichtbelegungen vom Schwerpunktfach immer
		if (anzahlKurseFs >= 2) {
			kurseUntereGrenze += 2;
			anzahlKurseFs -= 2;
		} else {
			kurseUntereGrenze += 2;
			anzahlKurseNw -= 2;
		}
		kurseOberereGrenze -= anzahlKurseFs;
		kurseOberereGrenze -= anzahlKurseNw;
	}

	private void pruefeKunstMusikUndLiteratur() {
		final @NotNull List<AbiturFachbelegung> kumuli = manager.getRelevanteFachbelegungen(GostFachbereich.KUNST_MUSIK_LITERATUR);
		int anzahlKurseKML = 0;
		int anzahlKMLAbi = 0;
		for (final AbiturFachbelegung kml : kumuli) {
			final int anzahl = manager.zaehleHalbjahresbelegungen(kml, GostHalbjahr.getQualifikationsphase());
			anzahlKurseKML += anzahl;
			if ((anzahl == 4) && (kml.abiturFach != null)) {
				anzahlKMLAbi++;
			}
		}

		if (anzahlKMLAbi > 0) {
			kurseUntereGrenze += anzahlKMLAbi * 4;
			kurseOberereGrenze -= (anzahlKurseKML - (anzahlKMLAbi * 4));
		} else {
			kurseUntereGrenze += 2;
			kurseOberereGrenze -= (anzahlKurseKML - 2);
		}
	}


	private int pruefeReligion() {
		int anzahlErsatzfachRE = 0;
		final @NotNull List<AbiturFachbelegung> listRE = manager.getRelevanteFachbelegungen(GostFachbereich.RELIGION);
		boolean istREAbi = false;
		for (final @NotNull AbiturFachbelegung religion : listRE)  {
			if (religion.abiturFach != null) {
				istREAbi = true;
			}
		}
		final int anzahl = manager.zaehleBelegungInHalbjahren(listRE, GostHalbjahr.getQualifikationsphase());
		if (((anzahl == 4) && istREAbi)) {
			kurseUntereGrenze += 4;
		} else if (anzahl >= 2) {
			kurseUntereGrenze += 2;
			kurseOberereGrenze -= (anzahl - 2);
		} else if (anzahl == 1) {
			kurseUntereGrenze += 1;
			anzahlErsatzfachRE = 1;
		} else if (anzahl == 0) {
			anzahlErsatzfachRE = 2;
		}
		return anzahlErsatzfachRE;
	}


	private int pruefePhilosophie(final int anzahlErsatzfachRE, final boolean istGEDurchgaengig, final boolean istSWDurchgaengig) {
		boolean istErsatzfachRE = false;
		final @NotNull List<AbiturFachbelegung> listPL = manager.getRelevanteFachbelegungen(GostFachbereich.PHILOSOPHIE);
		final int anzahlPL = manager.zaehleBelegungInHalbjahren(listPL, GostHalbjahr.getQualifikationsphase());
		final boolean istPLDurchgaengig = (!hatGWDurchgaengig) && (anzahlPL == 4) && !istGEDurchgaengig && !istSWDurchgaengig;
		if (listPL.size() == 1) {
			final @NotNull AbiturFachbelegung philosophie = listPL.getFirst();
			if (((anzahlPL == 4) && (philosophie.abiturFach != null))) {
				kurseUntereGrenze += 4;
			} else if (hatPjkAbi && (philosophie.fachID == referenzfach.id)) {
				kurseUntereGrenze += 2;
			} else if (anzahlPL > 0) {
				if (istPLDurchgaengig) {
					kurseUntereGrenze += 4;
					istErsatzfachRE = true; // Philosophie ist ggf. Ersatzfach, wenn es nicht Abiturfach ist, es fließt hier aber nicht extra in die Einbringungspflicht mit rein
					hatGWDurchgaengig = true;
				} else if (anzahlErsatzfachRE > 0) {
					kurseUntereGrenze += anzahlErsatzfachRE; // Wert ist ja negativ...
					kurseOberereGrenze -= (anzahlPL - anzahlErsatzfachRE);
					istErsatzfachRE = true; // Philosophie ist hier Ersatzfach
				} else {
					kurseOberereGrenze -= anzahlPL;
				}
			}
		}
		return istErsatzfachRE ? 0 : anzahlErsatzfachRE;
	}


	private int pruefeGeschichte(final @NotNull List<AbiturFachbelegung> geschichte, final int anzahlGE, final int anzahlErsatzfachRE, final boolean istGEDurchgaengig) {
		boolean istErsatzfachRE = false;
		boolean istGEAbi = false;
		for (final @NotNull AbiturFachbelegung ge : geschichte)  {
			if (ge.abiturFach != null) {
				istGEAbi = true;
			}
		}
		final boolean istGEErsatzfachRE = (anzahlErsatzfachRE > 0) && ((anzahlGE - 2) - anzahlErsatzfachRE >= 0);
		int restKurse = anzahlGE;
		if (istGEAbi || (!hatGWDurchgaengig && istGEDurchgaengig)) {
			kurseUntereGrenze += 4;
			restKurse -= 4;
			if (istGEErsatzfachRE) {
				istErsatzfachRE = true;
			}
		} else {
			kurseUntereGrenze += 2;
			restKurse -= 2;
		}
		if (istGEErsatzfachRE) {
			kurseUntereGrenze += anzahlErsatzfachRE;
			kurseOberereGrenze += anzahlErsatzfachRE;
			istErsatzfachRE = true;
		}
		kurseOberereGrenze -= restKurse;
		return istErsatzfachRE ? 0 : anzahlErsatzfachRE;
	}


	private int pruefeSozialwissenschaften(final @NotNull List<AbiturFachbelegung> sozialwissenschaften, final int anzahlSW, final int anzahlErsatzfachRE, final boolean istGEDurchgaengig, final boolean istSWDurchgaengig) {
		boolean istErsatzfachRE = false;
		boolean istSWAbi = false;
		for (final @NotNull AbiturFachbelegung sw : sozialwissenschaften)  {
			if (sw.abiturFach != null) {
				istSWAbi = true;
			}
		}
		final boolean istSWErsatzfachRE = (anzahlErsatzfachRE > 0) && ((anzahlSW - 2) - anzahlErsatzfachRE >= 0);
		int restKurse = anzahlSW;
		if (istSWAbi || (!hatGWDurchgaengig && istSWDurchgaengig && !istGEDurchgaengig)) {
			kurseUntereGrenze += 4;
			restKurse -= 4;
			if (istSWErsatzfachRE) {
				istErsatzfachRE = true;
			}
		} else {
			kurseUntereGrenze += 2;
			restKurse -= 2;
		}
		if (istSWErsatzfachRE) {
			kurseUntereGrenze += anzahlErsatzfachRE;
			kurseOberereGrenze += anzahlErsatzfachRE;
			istErsatzfachRE = true;
		}
		kurseOberereGrenze -= restKurse;
		return istErsatzfachRE ? 0 : anzahlErsatzfachRE;
	}

	private int pruefeSonstigeGesellschaftswissenschaft(final @NotNull AbiturFachbelegung gw, final int anzahlErsatzfachRE) {
		int anzahl = manager.zaehleHalbjahresbelegungen(gw, GostHalbjahr.getQualifikationsphase());
		final boolean istErsatzfachRE = (anzahlErsatzfachRE > 0) && ((anzahl - 2) - anzahlErsatzfachRE >= 0);
		final boolean nimmAlsDurchgaengigeGW = !hatGWDurchgaengig && (anzahl == 4);
		if (nimmAlsDurchgaengigeGW) {
			hatGWDurchgaengig = true;
		}
		if (gw.abiturFach != null) {
			kurseUntereGrenze += 4;
			anzahl -= 4;
		} else if (hatPjkAbi && (gw.fachID == referenzfach.id)) {
			kurseUntereGrenze += 2;
			anzahl -= 2;
		}
		if (nimmAlsDurchgaengigeGW) {
			kurseUntereGrenze += anzahl;
		} else {
			if (istErsatzfachRE) {
				kurseUntereGrenze += anzahlErsatzfachRE;
				kurseOberereGrenze += anzahlErsatzfachRE;
			}
			kurseOberereGrenze -= anzahl;
		}
		return istErsatzfachRE ? 0 : anzahlErsatzfachRE;
	}

	private void pruefeSonstigeGesellschaftswissenschaften(final int anzahlErsatzfachRE) {
		int kurseErsatzfachRE = anzahlErsatzfachRE;
		final @NotNull List<AbiturFachbelegung> gesellschaftswissenschaften = manager.getRelevanteFachbelegungen(GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH_SONSTIGE);
		for (final @NotNull AbiturFachbelegung gw : gesellschaftswissenschaften)  {
			kurseErsatzfachRE = pruefeSonstigeGesellschaftswissenschaft(gw, kurseErsatzfachRE);
		}
	}


	private void pruefeGesellschaftswissenschaftenUndReligion() {
		int anzahlErsatzfachRE = pruefeReligion();

		// Bestimme die durchgängigen Fach-Belegungen, bevorzuge Geschichte und Sozialwissenschaften und dann ggf. Philosophie
		final @NotNull List<AbiturFachbelegung> geschichte = manager.getRelevanteFachbelegungen(GostFachbereich.GESCHICHTE);
		final int anzahlGE = manager.zaehleBelegungInHalbjahren(geschichte, GostHalbjahr.getQualifikationsphase());
		final boolean istGEDurchgaengig = (!hatGWDurchgaengig) && (anzahlGE == 4);
		final @NotNull List<AbiturFachbelegung> sozialwissenschaften = manager.getRelevanteFachbelegungen(GostFachbereich.SOZIALWISSENSCHAFTEN);
		final int anzahlSW = manager.zaehleBelegungInHalbjahren(sozialwissenschaften, GostHalbjahr.getQualifikationsphase());
		final boolean istSWDurchgaengig = (!hatGWDurchgaengig) && (anzahlSW == 4);

		anzahlErsatzfachRE = pruefePhilosophie(anzahlErsatzfachRE, istGEDurchgaengig, istSWDurchgaengig);
		anzahlErsatzfachRE = pruefeGeschichte(geschichte, anzahlGE, anzahlErsatzfachRE, istGEDurchgaengig);
		anzahlErsatzfachRE = pruefeSozialwissenschaften(sozialwissenschaften, anzahlSW, anzahlErsatzfachRE, istGEDurchgaengig, istSWDurchgaengig);

		hatGWDurchgaengig = hatGWDurchgaengig || istGEDurchgaengig || istSWDurchgaengig;
		pruefeSonstigeGesellschaftswissenschaften(anzahlErsatzfachRE);
	}


	@Override
	protected void pruefeGesamt() {
		// Führe die Prüfung nur durch, wenn insgesamt genau 40 Kurse gewählt wurden.
		// Ein Projektkurs muss gewählt sein und das Referenzfach dazu bekannt
		// Sport muss mit 4 Kursen belegt sein und AT darf nicht vorkommen
		if ((pruefungKurszahlenUndWochenstunden.getBlockIAnzahlAnrechenbar() != 40) || (referenzfach.id == -1) || (anzahlSportKurse != 4)) {
			return;
		}

		// Wir gehen für die obere Grenze zunächst von allen gewählten Kursen aus
		kurseOberereGrenze = 40;

		// Für die untere Grenze sind Mathe, Deutsch und der PJK gesetzt und werden nicht extra geprüft, da sie immer eingebracht werden müssen
		kurseUntereGrenze = 10;

		pruefeSport();
		pruefeVertiefungskurse();
		pruefeFremdsprachenUndNaturwissenschaften();
		pruefeKunstMusikUndLiteratur();
		pruefeGesellschaftswissenschaftenUndReligion();
		if (kurseOberereGrenze > 36) {
			addFehler(GostBelegungsfehler.GOST30_EINBR_1);
		}
	}


	/**
	 * Gibt zurück, ob das Ergebnis dieser Prüfung ein exaktes Ergebnis geliefert hat. (Zur Fehlersuche)
	 *
	 * @return true, wenn die beiden Grenzen bei der Berechnung übereinstimmen und ansonsten false
	 */
	public boolean istExakt() {
		return (kurseOberereGrenze == kurseUntereGrenze);
	}


	/**
	 * Gibt die Anzahl der einbringungspflichtigen Kurse zurück, sofern die Berechnung erfolgreich war
	 *
	 * @return die Anzahl der einbringungspflichtigen Kurse
	 */
	public int getAnzahlEinbrungungspflichtigerKurse() {
		return kurseOberereGrenze;
	}

}
