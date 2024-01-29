package de.svws_nrw.core.abschluss.gost;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.svws_nrw.core.abschluss.gost.belegpruefung.AbiFaecher;
import de.svws_nrw.core.abschluss.gost.belegpruefung.Allgemeines;
import de.svws_nrw.core.abschluss.gost.belegpruefung.Deutsch;
import de.svws_nrw.core.abschluss.gost.belegpruefung.FachWaehlbar;
import de.svws_nrw.core.abschluss.gost.belegpruefung.Fachkombinationen;
import de.svws_nrw.core.abschluss.gost.belegpruefung.Fremdsprachen;
import de.svws_nrw.core.abschluss.gost.belegpruefung.GesellschaftswissenschaftenUndReligion;
import de.svws_nrw.core.abschluss.gost.belegpruefung.KurszahlenUndWochenstunden;
import de.svws_nrw.core.abschluss.gost.belegpruefung.Latinum;
import de.svws_nrw.core.abschluss.gost.belegpruefung.LiterarischKuenstlerisch;
import de.svws_nrw.core.abschluss.gost.belegpruefung.Mathematik;
import de.svws_nrw.core.abschluss.gost.belegpruefung.Naturwissenschaften;
import de.svws_nrw.core.abschluss.gost.belegpruefung.Projektkurse;
import de.svws_nrw.core.abschluss.gost.belegpruefung.Schwerpunkt;
import de.svws_nrw.core.abschluss.gost.belegpruefung.Sport;
import de.svws_nrw.core.adt.map.ArrayMap;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.data.gost.AbiturFachbelegungHalbjahr;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.GostJahrgangFachkombination;
import de.svws_nrw.core.data.gost.GostJahrgangsdaten;
import de.svws_nrw.core.data.gost.GostSchuelerFachwahl;
import de.svws_nrw.core.data.schueler.Sprachendaten;
import de.svws_nrw.core.types.Note;
import de.svws_nrw.core.types.fach.ZulaessigesFach;
import de.svws_nrw.core.types.gost.GostAbiturFach;
import de.svws_nrw.core.types.gost.GostBesondereLernleistung;
import de.svws_nrw.core.types.gost.GostFachbereich;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.types.gost.GostSchriftlichkeit;
import de.svws_nrw.core.utils.gost.GostFachUtils;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.core.utils.schueler.SprachendatenUtils;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt Methoden zur Verfügung um die angegebenen Abiturdaten zu
 * bearbeiten und Auswertungen durchzuführen.
 */
public class AbiturdatenManager {

	/** Das Abiturdaten-Objekt, welches mithilfe dieses Managers bearbeitet wird */
	private final @NotNull Abiturdaten abidaten;

	/** Die Informationen zu den Jahrgangsdaten, sofern welche vorhanden sind - ansonsten null */
	private final GostJahrgangsdaten _jahrgangsdaten;

	/** Der Manager für die Fächer und Fachkombinationen der gymnasialen Oberstufe für diesen Abiturjahrgang */
	private final @NotNull GostFaecherManager faecherManager;

	/** Die Art der durchzuführenden Belegprüfung */
	private final @NotNull GostBelegpruefungsArt pruefungsArt;

	/** Eine HashMap, welche den schnellen Zugriff auf die Fachbelegungen über den Fachbereich ermöglicht */
	private final @NotNull Map<@NotNull GostFachbereich, @NotNull ArrayList<@NotNull AbiturFachbelegung>> mapFachbereiche = new ArrayMap<>(GostFachbereich.values());

	/** Eine HashMap, welche den schnellen Zugriff auf die Prüfungsordnungs-relevanten Fachbelegungen über den Fachbereich ermöglicht */
	private final @NotNull Map<@NotNull GostFachbereich, @NotNull ArrayList<@NotNull AbiturFachbelegung>> mapFachbereicheRelevant = new ArrayMap<>(GostFachbereich.values());


	/** Die Prüfungsergebnisse der einzelnen Teilprüfungen der Belegprüfung */
	private @NotNull List<@NotNull GostBelegpruefung> belegpruefungen = new ArrayList<>();

	/** Die zuletzt durchgeführte Belegprüfung bezüglich der Kurszahlen und der Wochenstunden */
	private KurszahlenUndWochenstunden belegpruefungKurszahlenUndWochenstunden = null;

	/** Die Menge der Belegprüfungsfehler, die bei den durchgeführten Belegprüfungen aufgetreten sind. */
	private @NotNull List<@NotNull GostBelegungsfehler> belegpruefungsfehler = new ArrayList<>();

	/** Gibt an, ob die Belegprüfung insgesamt erfolgreich war oder nicht. */
	private boolean belegpruefungErfolgreich = false;



	/**
	 * Erstellt ein neues Manager-Objekt, welches mit den übergebenen Abiturdaten verknüpft wird.
	 *
	 * @param abidaten       die Abiturdaten
	 * @param gostJahrgang   die Informationen zu dem Abiturjahrgang
	 * @param faecherManager der Manager für die Fächer und Fachkombinationen der Gymnasialen Oberstufe
	 * @param pruefungsArt   die Art der Belegpruefung (z.B. EF1 oder GESAMT)
	 */
	public AbiturdatenManager(final @NotNull Abiturdaten abidaten, final GostJahrgangsdaten gostJahrgang,
			final @NotNull GostFaecherManager faecherManager, final @NotNull GostBelegpruefungsArt pruefungsArt) {
		this.abidaten = abidaten;
		this._jahrgangsdaten = gostJahrgang;
		this.faecherManager = faecherManager;
		this.pruefungsArt = pruefungsArt;
		init();
	}


	/**
	 * Führt die Belegprüfung der Art pruefungs_art für einen Schüler durch, dessen Abiturdaten mit dem angegebenen
	 * Manager verwaltet werden.
	 *
	 * @param pruefungsArt    die Art der Prüfung, die durchgeführt wird
	 *
	 * @return eine Liste mit den durchgefuehrten Belegpruefungen
	 */
	public @NotNull List<@NotNull GostBelegpruefung> getPruefungen(final @NotNull GostBelegpruefungsArt pruefungsArt) {
		final @NotNull ArrayList<@NotNull GostBelegpruefung> pruefungen = new ArrayList<>();
		pruefungen.add(new Deutsch(this, pruefungsArt));
		final @NotNull Fremdsprachen pruefungFremdsprachen = new Fremdsprachen(this, pruefungsArt);
		pruefungen.add(pruefungFremdsprachen);
		pruefungen.add(new Latinum(this, pruefungsArt));
		pruefungen.add(new LiterarischKuenstlerisch(this, pruefungsArt));
		pruefungen.add(new GesellschaftswissenschaftenUndReligion(this, pruefungsArt));
		pruefungen.add(new Mathematik(this, pruefungsArt));
		final @NotNull Naturwissenschaften pruefungNaturwissenschaften = new Naturwissenschaften(this, pruefungsArt);
		pruefungen.add(pruefungNaturwissenschaften);
		pruefungen.add(new Sport(this, pruefungsArt));
		final @NotNull Projektkurse pruefungProjektkurse = new Projektkurse(this, pruefungsArt);
		pruefungen.add(pruefungProjektkurse);
		// Die Prüfung zu dem Schwerpunkt muss nach den Prüfungen des naturwissenschaftlichen und der Fremdsprachen durchgeführt werden, da hier eine Abhängigkeit besteht.
		pruefungen.add(new Schwerpunkt(this, pruefungsArt, pruefungFremdsprachen, pruefungNaturwissenschaften));
		pruefungen.add(new AbiFaecher(this, pruefungsArt));
		// Die Prüfung der Kurszahlen und Wochenstunden ist abhängig von den Projektkursergebnissen - sie muss nach den Projektkursergebnissen durchgeführt werden!!!
		belegpruefungKurszahlenUndWochenstunden = new KurszahlenUndWochenstunden(this, pruefungsArt, pruefungProjektkurse);
		pruefungen.add(belegpruefungKurszahlenUndWochenstunden);
		pruefungen.add(new Allgemeines(this, pruefungsArt));
		// Die Prüfung von schulspezifischen Fachkombinationen
		pruefungen.add(new Fachkombinationen(this, pruefungsArt));
		// Die Prüfung der schulspezifischen Wählbarkeit von Fächern
		pruefungen.add(new FachWaehlbar(this, pruefungsArt));
		return pruefungen;
	}


	/**
	 * Initialisiert bzw. reinitialisert die Datenstrukturen, die für den schnellen Zugriff auf die Daten
	 * eingerichtet werden.
	 */
	public void init() {
		if (abidaten == null)
			return;
		initMapFachbereiche();
		belegpruefungen = getPruefungen(pruefungsArt);
		for (int i = 0; i < belegpruefungen.size(); i++) {
			final @NotNull GostBelegpruefung belegpruefung = belegpruefungen.get(i);
			belegpruefung.pruefe();
		}
		belegpruefungsfehler = GostBelegpruefung.getBelegungsfehlerAlle(belegpruefungen);
		belegpruefungErfolgreich = GostBelegpruefung.istErfolgreich(belegpruefungsfehler);
	}



	/**
	 * Initialisiert bzw. reinitialisiert die Map für den schnellen Zugriff auf Fachbelegungen
	 * anhand des Fachbereichs.
	 */
	private void initMapFachbereiche() {
		// Leere die HashMap und erstelle neue Vektoren für die Zuordnung von Abitur-Fachbelegungen
		mapFachbereiche.clear();
		mapFachbereicheRelevant.clear();
		for (final @NotNull GostFachbereich fachbereich : GostFachbereich.values()) {
			mapFachbereiche.put(fachbereich, new ArrayList<>());
			mapFachbereicheRelevant.put(fachbereich, new ArrayList<>());
		}

		// Durchwandere alle belegten Fächer und weise diese den Fachbereichen zu
		final @NotNull List<@NotNull AbiturFachbelegung> fachbelegungen = abidaten.fachbelegungen;
		for (final AbiturFachbelegung fachbelegung : fachbelegungen) {
			if (zaehleBelegung(fachbelegung) > 0) { // Filtere ggf. leere Belegungen
				final GostFach fach = getFach(fachbelegung);
				final @NotNull List<@NotNull GostFachbereich> fachbereiche = GostFachbereich.getBereiche(fach);
				for (final @NotNull GostFachbereich fachbereich : fachbereiche) {
					List<@NotNull AbiturFachbelegung> listFachbelegungen = mapFachbereiche.get(fachbereich);
					if (listFachbelegungen != null)
						listFachbelegungen.add(fachbelegung);
					listFachbelegungen = mapFachbereicheRelevant.get(fachbereich);
					if (listFachbelegungen != null)
						listFachbelegungen.add(fachbelegung);
				}
			}
		}
	}


	/**
	 * Gibt den zugehörigen Fächer-Manager zurück.
	 *
	 * @return der Fächer-Manager
	 */
	public @NotNull GostFaecherManager faecher() {
		return this.faecherManager;
	}


	/**
	 * gibt die gewählte Prüfungsart zurück
	 *
	 * @return entweder Gesamtprüfung oder EF1-Prüfung
	 */
	public @NotNull GostBelegpruefungsArt getPruefungsArt() {
		return this.pruefungsArt;
	}

	/**
	 * Gibt zurück, ob das angegebene Halbjahr bereits bewertet ist oder nicht.
	 *
	 * @param halbjahr   das Halbjahr
	 *
	 * @return true, falls es bereits bewertet ist
	 */
	public boolean istBewertet(final @NotNull GostHalbjahr halbjahr) {
		return abidaten.bewertetesHalbjahr[halbjahr.id];
	}


	/**
	 * Liefert die in den Abiturdaten enthaltenen Sprachendaten.
	 *
	 * @return Die Sprachendaten (siehe {@link Sprachendaten})
	 */
	public @NotNull Sprachendaten getSprachendaten() {
		return abidaten.sprachendaten;
	}


	/**
	 * Gibt zurück, ob es sich bei der Halbjahresbelegung um eine Belegung handelt, welche mit
	 * null Punkten abgeschlossen wurde und welche daher als nicht belegter Kurs zu werten ist.
	 *
	 * @param halbjahresbelegung   die Halbjahresbelegung eines Kurses
	 *
	 * @return true, fall es sich um einen Null-Punkte-Kurs in der Qualifikationsphase handelt.
	 */
	public static boolean istNullPunkteBelegungInQPhase(@NotNull final AbiturFachbelegungHalbjahr halbjahresbelegung) {
		final GostHalbjahr hj = GostHalbjahr.fromKuerzel(halbjahresbelegung.halbjahrKuerzel);
		if ((hj == null) || (hj.istEinfuehrungsphase()))
			return false;
		return Note.fromKuerzel(halbjahresbelegung.notenkuerzel) == Note.UNGENUEGEND;
	}


	private static String getSchuelerFachwahlFromBelegung(final @NotNull AbiturFachbelegung belegung, final @NotNull GostHalbjahr halbjahr) {
		AbiturFachbelegungHalbjahr halbjahresbelegung = belegung.belegungen[halbjahr.id];
		if (halbjahresbelegung == null) {
			halbjahresbelegung = new AbiturFachbelegungHalbjahr();
			halbjahresbelegung.halbjahrKuerzel = halbjahr.kuerzel;
			belegung.belegungen[halbjahr.id] = halbjahresbelegung;
		}
		final GostKursart kursart = GostKursart.fromKuerzel(halbjahresbelegung.kursartKuerzel);
		if (kursart == null)
			return ("".equals(halbjahresbelegung.kursartKuerzel) ? null : halbjahresbelegung.kursartKuerzel);
		if (istNullPunkteBelegungInQPhase(halbjahresbelegung))
			return null;
		if ((kursart == GostKursart.ZK) || (kursart == GostKursart.LK))
			return kursart.kuerzel;
		return halbjahresbelegung.schriftlich ? "S" : "M";
	}


	/**
	 * Bestimmt die Schüler-Fachwahl für das Fach mit der übergebenen ID
	 *
	 * @param fachID   die ID des Faches
	 *
	 * @return die Schüler-Fachwahl
	 */
	public @NotNull GostSchuelerFachwahl getSchuelerFachwahl(final long fachID) {
		final AbiturFachbelegung belegung = getFachbelegungByID(fachID);
		if (belegung == null)
			return new GostSchuelerFachwahl();
		final @NotNull GostSchuelerFachwahl wahl = new GostSchuelerFachwahl();
		wahl.halbjahre[0] = getSchuelerFachwahlFromBelegung(belegung, GostHalbjahr.EF1);
		wahl.halbjahre[1] = getSchuelerFachwahlFromBelegung(belegung, GostHalbjahr.EF2);
		wahl.halbjahre[2] = getSchuelerFachwahlFromBelegung(belegung, GostHalbjahr.Q11);
		wahl.halbjahre[3] = getSchuelerFachwahlFromBelegung(belegung, GostHalbjahr.Q12);
		wahl.halbjahre[4] = getSchuelerFachwahlFromBelegung(belegung, GostHalbjahr.Q21);
		wahl.halbjahre[5] = getSchuelerFachwahlFromBelegung(belegung, GostHalbjahr.Q22);
		wahl.abiturFach = belegung.abiturFach;
		return wahl;
	}


	/**
	 * Bestimmt die Schüler-Fachwahlen aller belegten Fächer.
	 *
	 * @return die Map mit den Schüler-Fachwahlen
	 */
	public @NotNull Map<@NotNull Long, @NotNull GostSchuelerFachwahl> getSchuelerFachwahlen() {
		final @NotNull HashMap<@NotNull Long, @NotNull GostSchuelerFachwahl> fachwahlen = new HashMap<>();
		final @NotNull List<@NotNull AbiturFachbelegung> fachbelegungen = abidaten.fachbelegungen;
		for (final AbiturFachbelegung fb : fachbelegungen)
			fachwahlen.put(fb.fachID, getSchuelerFachwahl(fb.fachID));
		return fachwahlen;
	}


	/**
	 * Liefert das Fach der gymnasialen Oberstufe für die angegeben Abiturfachbelegung.
	 *
	 * @param belegung   die Fachbelegung (siehe {@link AbiturFachbelegung})
	 *
	 * @return das Fach der gymnasialen Oberstufe (siehe {@link GostFach})
	 */
	public GostFach getFach(final AbiturFachbelegung belegung) {
		if (belegung == null)
			return null;
		return faecherManager.get(belegung.fachID);
	}


	/**
	 * Prüft, ob das Fach in allen angegebenen Halbjahren belegt wurde.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegung      die zu prüfende Fachbelegung
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls das Fach in den Halbjahren belegt wurde, sonst false
	 */
	public boolean pruefeBelegung(final AbiturFachbelegung fachbelegung, final @NotNull GostHalbjahr... halbjahre) {
		if (fachbelegung == null)
			return false;
		if ((halbjahre == null) || (halbjahre.length == 0))
			return true;
		for (final GostHalbjahr halbjahr : halbjahre) {
			final AbiturFachbelegungHalbjahr belegungHalbjahr = fachbelegung.belegungen[halbjahr.id];
			if ((belegungHalbjahr == null) || (belegungHalbjahr.kursartKuerzel == null))
				return false;
			if (istNullPunkteBelegungInQPhase(belegungHalbjahr))
				return false;
		}
		return true;
	}


	/**
	 * Bestimmt die Anzahl der Fachbelegungen, die dem Fach zugeordnet sind.
	 * Wird keine gültige Fachbelegung übergeben, so wird 0 zurückgegeben.
	 *
	 * @param fachbelegung   die Fachbelegung
	 *
	 * @return die Anzahl der Belegungen des Faches
	 */
	public int zaehleBelegung(final AbiturFachbelegung fachbelegung) {
		if (fachbelegung == null)
			return 0;
		int anzahl = 0;
		for (int i = 0; i < GostHalbjahr.maxHalbjahre; i++) {
			final AbiturFachbelegungHalbjahr belegungHalbjahr = fachbelegung.belegungen[i];
			if ((belegungHalbjahr != null) && (!istNullPunkteBelegungInQPhase(belegungHalbjahr)))
				anzahl++;
		}
		return anzahl;
	}


	/**
	 * Zählt die Anzahl der Belegungen für die angegebenen Fachbelegungen in den angegeben Halbjahren.
	 * Ist die Fachbelegung null, so wird 0 zurückgegeben. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so wird ebenfalls 0 zurückgegeben.
	 *
	 * @param fachbelegungen      die Fachbelegungen
	 * @param halbjahre           die Halbjahre
	 *
	 * @return die Anzahl der Belegungen in den Halbjahren und den Fächern
	 */
	public int zaehleBelegungInHalbjahren(final List<@NotNull AbiturFachbelegung> fachbelegungen, final @NotNull GostHalbjahr... halbjahre) {
		if (fachbelegungen == null)
			return 0;
		if ((halbjahre == null) || (halbjahre.length == 0))
			return 0;
		int anzahl = 0;
		for (final @NotNull AbiturFachbelegung fachbelegung : fachbelegungen) {
			for (final @NotNull GostHalbjahr halbjahr : halbjahre) {
				final AbiturFachbelegungHalbjahr belegungHalbjahr = fachbelegung.belegungen[halbjahr.id];
				if ((belegungHalbjahr != null) && (!istNullPunkteBelegungInQPhase(belegungHalbjahr)))
					anzahl++;
			}
		}
		return anzahl;
	}


	/**
	 * Prüft, ob die Belegung des Faches in den angegebenen Halbjahren der angegebenen Kursart entspricht.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegung      die zu prüfende Fachnbelegung
	 * @param kursart           die zu prüfende Kursart
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls die Schriftlichkeit in den Halbjahren gegeben ist, sonst false
	 */
	public boolean pruefeBelegungMitKursart(final AbiturFachbelegung fachbelegung, final @NotNull GostKursart kursart, final @NotNull GostHalbjahr... halbjahre) {
		if (fachbelegung == null)
			return false;
		if ((halbjahre == null) || (halbjahre.length == 0))
			return true;
		for (final GostHalbjahr halbjahr : halbjahre) {
			final AbiturFachbelegungHalbjahr belegungHalbjahr = fachbelegung.belegungen[halbjahr.id];
			if ((belegungHalbjahr == null) || (kursart != GostKursart.fromKuerzel(belegungHalbjahr.kursartKuerzel))
					|| (istNullPunkteBelegungInQPhase(belegungHalbjahr)))
				return false;
		}
		return true;
	}


	/**
	 * Prüft, ob eine Fachbelegung existiert, welche in den angegebenen Halbjahren der angegebenen Kursart
	 * entspricht.
	 * Ist keine Fachbelegung angegeben, so schlägt die Prüfung fehl. Wird bei einer gültigen
	 * Fachbelegung kein Halbjahr angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param kursart           die zu prüfende Kursart
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls die Schriftlichkeit in den Halbjahren gegeben ist, sonst false
	 */
	public boolean pruefeBelegungExistiertMitKursart(final List<@NotNull AbiturFachbelegung> fachbelegungen, final @NotNull GostKursart kursart, final @NotNull GostHalbjahr... halbjahre) {
		if ((fachbelegungen == null) || (fachbelegungen.isEmpty()))
			return false;
		if ((halbjahre == null) || (halbjahre.length == 0))
			return true;
		for (final AbiturFachbelegung fachbelegung : fachbelegungen) {
			if (pruefeBelegungMitKursart(fachbelegung, kursart, halbjahre))
				return true;
		}
		return false;
	}


	/**
	 * Prüft, ob die Belegung des Faches in den angegebenen Halbjahren mindestens einmal die angegebenen Kursart hat.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung nicht erfolgreich, da kein Halbjahr geprüft werden muss und somit die Kursart nicht
	 * einmal existiert.
	 *
	 * @param fachbelegung      die zu prüfende Fachnbelegung
	 * @param kursart           die zu prüfende Kursart
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls die Kursart mindestens einmal in den Halbjahren gegeben ist, sonst false
	 */
	public boolean pruefeBelegungHatMindestensEinmalKursart(final AbiturFachbelegung fachbelegung, final @NotNull GostKursart kursart, final @NotNull GostHalbjahr... halbjahre) {
		if (fachbelegung == null)
			return false;
		if ((halbjahre == null) || (halbjahre.length == 0))
			return false;
		for (final GostHalbjahr halbjahr : halbjahre) {
			final AbiturFachbelegungHalbjahr belegungHalbjahr = fachbelegung.belegungen[halbjahr.id];
			if (belegungHalbjahr == null)
				continue;
			if ((kursart == GostKursart.fromKuerzel(belegungHalbjahr.kursartKuerzel))
					&& (!istNullPunkteBelegungInQPhase(belegungHalbjahr)))
				return true;
		}
		return false;
	}



	/**
	 * Prüft, ob die Belegung des Faches in dem angegebenen Halbjahr der angegebenen Schriftlichkeit entspricht.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl.
	 *
	 * @param fachbelegung      die zu prüfende Fachnbelegung
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahr          das zu prüfende Halbjahr
	 *
	 * @return true, falls die Schriftlichkeit in dem Halbjahr gegeben ist, sonst false
	 */
	public boolean pruefeBelegungMitSchriftlichkeitEinzeln(final AbiturFachbelegung fachbelegung, final @NotNull GostSchriftlichkeit schriftlichkeit, final @NotNull GostHalbjahr halbjahr) {
		if (fachbelegung == null)
			return false;
		final AbiturFachbelegungHalbjahr belegungHalbjahr = fachbelegung.belegungen[halbjahr.id];
		if ((belegungHalbjahr == null) || (istNullPunkteBelegungInQPhase(belegungHalbjahr)))
			return false;
		switch (schriftlichkeit) {
			case BELIEBIG: return true;
			case SCHRIFTLICH: return belegungHalbjahr.schriftlich;
			case MUENDLICH: return !belegungHalbjahr.schriftlich;
			default: return false;
		}
	}


	/**
	 * Prüft, ob die Belegung des Faches in den angegebenen Halbjahren der angegebenen Schriftlichkeit entspricht.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegung      die zu prüfende Fachnbelegung
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls die Schriftlichkeit in den Halbjahren gegeben ist, sonst false
	 */
	public boolean pruefeBelegungMitSchriftlichkeit(final AbiturFachbelegung fachbelegung, final @NotNull GostSchriftlichkeit schriftlichkeit, final @NotNull GostHalbjahr... halbjahre) {
		if (fachbelegung == null)
			return false;
		if ((halbjahre == null) || (halbjahre.length == 0))
			return true;
		for (final GostHalbjahr halbjahr : halbjahre)
			if (!pruefeBelegungMitSchriftlichkeitEinzeln(fachbelegung, schriftlichkeit, halbjahr))
				return false;
		return true;
	}


	/**
	 * Prüft, ob eine Belegung des Faches in den angegebenen Halbjahren nicht der angegebenen Schriftlichkeit entspricht.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegung      die zu prüfende Fachnbelegung
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls die Schriftlichkeit in den Halbjahren nicht gegeben ist, sonst false
	 */
	public boolean pruefeBelegungErfuelltNicht(final AbiturFachbelegung fachbelegung, final @NotNull GostSchriftlichkeit schriftlichkeit, final @NotNull GostHalbjahr... halbjahre) {
		if (fachbelegung == null)
			return false;
		if ((halbjahre == null) || (halbjahre.length == 0))
			return true;
		for (final GostHalbjahr halbjahr : halbjahre) {
			final AbiturFachbelegungHalbjahr belegungHalbjahr = fachbelegung.belegungen[halbjahr.id];
			if ((belegungHalbjahr == null) || (istNullPunkteBelegungInQPhase(belegungHalbjahr))
					|| ((schriftlichkeit != GostSchriftlichkeit.BELIEBIG)
					&& (((schriftlichkeit == GostSchriftlichkeit.SCHRIFTLICH) && (!belegungHalbjahr.schriftlich))
							|| ((schriftlichkeit == GostSchriftlichkeit.MUENDLICH) && (belegungHalbjahr.schriftlich)))))
				return true;
		}
		return false;
	}


	/**
	 * Prüft, ob eine Belegung des Faches in den angegebenen Halbjahren nicht der angegebenen Schriftlichkeit entspricht,
	 * sofern es in dem Halbjahr überhaupt belegt wurde..
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegung      die zu prüfende Fachnbelegung
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls die Schriftlichkeit in den Halbjahren nicht gegeben ist, sonst false
	 */
	public boolean pruefeBelegungErfuelltNichtFallsBelegt(final AbiturFachbelegung fachbelegung, final @NotNull GostSchriftlichkeit schriftlichkeit, final @NotNull GostHalbjahr... halbjahre) {
		if (fachbelegung == null)
			return false;
		if ((halbjahre == null) || (halbjahre.length == 0))
			return true;
		for (final GostHalbjahr halbjahr : halbjahre) {
			final AbiturFachbelegungHalbjahr belegungHalbjahr = fachbelegung.belegungen[halbjahr.id];
			if ((belegungHalbjahr == null) || (istNullPunkteBelegungInQPhase(belegungHalbjahr)))
				continue;
			if (((schriftlichkeit != GostSchriftlichkeit.BELIEBIG)
					&& (((schriftlichkeit == GostSchriftlichkeit.SCHRIFTLICH) && (!belegungHalbjahr.schriftlich))
							|| ((schriftlichkeit == GostSchriftlichkeit.MUENDLICH) && (belegungHalbjahr.schriftlich)))))
				return true;
		}
		return false;
	}


	/**
	 * Prüft, ob die Belegung des Faches in den angegebenen Halbjahren mindestens einmal die angegebene Schritflichkeit hat.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung nicht erfolgreich, da kein Halbjahr geprüft werden muss und somit die Schriftlichkeit nicht
	 * einmal existiert.
	 *
	 * @param fachbelegung      die zu prüfende Fachbelegung
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls die angegebene Schriftlichkeit mindestens einmal in den Halbjahren gegeben ist, sonst false
	 */
	public boolean pruefeBelegungHatMindestensEinmalSchriftlichkeit(final AbiturFachbelegung fachbelegung, final @NotNull GostSchriftlichkeit schriftlichkeit, final @NotNull GostHalbjahr... halbjahre) {
		if (fachbelegung == null)
			return false;
		if ((halbjahre == null) || (halbjahre.length == 0))
			return false;
		for (final GostHalbjahr halbjahr : halbjahre) {
			final AbiturFachbelegungHalbjahr belegungHalbjahr = fachbelegung.belegungen[halbjahr.id];
			if ((belegungHalbjahr == null) || (istNullPunkteBelegungInQPhase(belegungHalbjahr)))
				continue;
			if ((schriftlichkeit.istSchriftlich == null) || (schriftlichkeit.istSchriftlich.equals(belegungHalbjahr.schriftlich)))
				return true;
		}
		return false;
	}




	/**
	 * Prüft, ob eine Fachbelegung existiert, welche in den angegebenen Halbjahren mindestens einmal die angegebene
	 * Schritflichkeit hat.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung nicht erfolgreich, da kein Halbjahr geprüft werden muss und somit die Schriftlichkeit nicht
	 * einmal existiert.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachbelegungen
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls die angegebene Schriftlichkeit bei einer Fachbelegung mindestens einmal in den Halbjahren gegeben ist, sonst false
	 */
	public boolean pruefeBelegungExistiertHatMindestensEinmalSchriftlichkeit(final List<@NotNull AbiturFachbelegung> fachbelegungen, final @NotNull GostSchriftlichkeit schriftlichkeit, final @NotNull GostHalbjahr... halbjahre) {
		if ((fachbelegungen == null) || (fachbelegungen.isEmpty()))
			return false;
		if ((halbjahre == null) || (halbjahre.length == 0))
			return false;
		for (final AbiturFachbelegung fachbelegung : fachbelegungen)
			if (pruefeBelegungHatMindestensEinmalSchriftlichkeit(fachbelegung, schriftlichkeit, halbjahre))
				return true;
		return false;
	}




	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer mit den angegebenen Halbjahren existiert.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 * In dieser Methode wird ggf. auch geprüft, ob weitere Fachbelegungen existieren, welche das gleiche
	 * Statistik-Kürzel haben und Ersatzweise eine Halbjahres-Belegung ersetzen können. Dies ist z.B. bei bilingualen
	 * Fächern nötig oder bei der Unterscheidung von Sport-Profilen.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls eine Fachbelegung mit den Halbjahren existiert, sonst false
	 */
	public boolean pruefeBelegungExistiert(final List<@NotNull AbiturFachbelegung> fachbelegungen, final @NotNull GostHalbjahr... halbjahre) {
		if (fachbelegungen == null)
			return false;
		if ((halbjahre == null) || (halbjahre.length == 0))
			return true;
		for (final AbiturFachbelegung fachbelegung : fachbelegungen) {
			// Beachte alle Fachbelegungen von Fächern des gleichen Statistik-Faches - dies kann bei bilingualen Fächern wichtig sein
			final GostFach fach = faecherManager.get(fachbelegung.fachID);
			if (fach == null)
				continue;
			final List<@NotNull AbiturFachbelegung> alleBelegungen = getFachbelegungByFachkuerzel(fach.kuerzel);
			if ((alleBelegungen == null) || (alleBelegungen.isEmpty()))
				continue;
			boolean hatBelegung = true;
			for (final GostHalbjahr halbjahr : halbjahre) {
				boolean hatHalbjahresBelegung = false;
				for (final AbiturFachbelegung aktFachbelegung : alleBelegungen) {
					final AbiturFachbelegungHalbjahr belegungHalbjahr = aktFachbelegung.belegungen[halbjahr.id];
					if ((belegungHalbjahr != null) && (!istNullPunkteBelegungInQPhase(belegungHalbjahr))) {
						hatHalbjahresBelegung = true;
						break;
					}
				}
				if (!hatHalbjahresBelegung) {
					hatBelegung = false;
					break;
				}
			}
			if (hatBelegung)
				return true;
		}
		return false;
	}


	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer mit dem angegebenen Halbjahr existiert.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl.
	 * In dieser Methode wird ggf. auch geprüft, ob weitere Fachbelegungen existieren, welche das gleiche
	 * Statistik-Kürzel haben und Ersatzweise eine Halbjahres-Belegung ersetzen können. Dies ist z.B. bei bilingualen
	 * Fächern nötig oder bei der Unterscheidung von Sport-Profilen.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param halbjahr          das zu prüfende Halbjahr
	 *
	 * @return true, falls eine Fachbelegung mit dem Halbjahr existiert, sonst false
	 */
	public boolean pruefeBelegungExistiertEinzeln(final List<@NotNull AbiturFachbelegung> fachbelegungen, final @NotNull GostHalbjahr halbjahr) {
		if (fachbelegungen == null)
			return false;
		for (final AbiturFachbelegung fachbelegung : fachbelegungen) {
			// Beachte alle Fachbelegungen von Fächern des gleichen Statistik-Faches - dies kann bei bilingualen Fächern wichtig sein
			final GostFach fach = faecherManager.get(fachbelegung.fachID);
			if (fach == null)
				continue;
			final List<@NotNull AbiturFachbelegung> alleBelegungen = getFachbelegungByFachkuerzel(fach.kuerzel);
			if ((alleBelegungen == null) || (alleBelegungen.isEmpty()))
				continue;
			for (final AbiturFachbelegung aktFachbelegung : alleBelegungen) {
				final AbiturFachbelegungHalbjahr belegungHalbjahr = aktFachbelegung.belegungen[halbjahr.id];
				if ((belegungHalbjahr != null) && (!istNullPunkteBelegungInQPhase(belegungHalbjahr)))
					return true;
			}
		}
		return false;
	}


	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer mit einer durchgängigen Belegung existiert,
	 * die zumindest in der Q1 und der Q2.1 schriftlich ist.
	 * In dieser Methode wird ggf. auch geprüft, ob weitere Fachbelegungen existieren, welche das gleiche
	 * Statistik-Kürzel haben und Ersatzweise eine Halbjahres-Belegung ersetzen können. Dies ist bei bilingualen
	 * Fächern nötig.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 *
	 * @return true, falls eine durchgehend schriftliche Fachbelegung existiert, sonst false
	 */
	public boolean pruefeBelegungExistiertDurchgehendSchriftlich(final List<@NotNull AbiturFachbelegung> fachbelegungen) {
		if (fachbelegungen == null)
			return false;
		for (final AbiturFachbelegung fachbelegung : fachbelegungen) {
			// Beachte alle Fachbelegungen von Fächern des gleichen Statistik-Faches - dies kann bei bilingualen Fächern wichtig sein
			final GostFach fach = faecherManager.get(fachbelegung.fachID);
			if (fach == null)
				continue;
			final List<@NotNull AbiturFachbelegung> alleBelegungen = getFachbelegungByFachkuerzel(fach.kuerzel);
			if ((alleBelegungen == null) || (alleBelegungen.isEmpty()))
				continue;
			boolean hatBelegung = true;
			for (final GostHalbjahr halbjahr : GostHalbjahr.values()) {
				boolean hatHalbjahresBelegung = false;
				for (final AbiturFachbelegung aktFachbelegung : alleBelegungen) {
					final AbiturFachbelegungHalbjahr belegungHalbjahr = aktFachbelegung.belegungen[halbjahr.id];
					if ((belegungHalbjahr != null) && (!istNullPunkteBelegungInQPhase(belegungHalbjahr))
						&& (((halbjahr != GostHalbjahr.Q11) && (halbjahr != GostHalbjahr.Q12) && (halbjahr != GostHalbjahr.Q21)) || (belegungHalbjahr.schriftlich))) {
							hatHalbjahresBelegung = true;
						// "break;" wird hier nicht verwendet, da bei der fehlerhaften Belegung von inhaltsgleichen Fächern (gleiches Statistik-Kürzel) im selben Halbjahr diese Prüfung dennoch korrekt ablaufen sollte
					}
				}
				if (!hatHalbjahresBelegung) {
					hatBelegung = false;
					break;
				}
			}
			if (hatBelegung)
				return true;
		}
		return false;
	}


	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer in dem angegebenen Halbjahr der angegebenen Schriftlichkeit entspricht.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahr          das zu prüfende Halbjahr
	 *
	 * @return true, falls bei einer Fachbelegung die Schriftlichkeit in dem Halbjahr gegeben ist, sonst false
	 */
	public boolean pruefeBelegungExistiertMitSchriftlichkeitEinzeln(final List<AbiturFachbelegung> fachbelegungen, final @NotNull GostSchriftlichkeit schriftlichkeit, final @NotNull GostHalbjahr halbjahr) {
		if (fachbelegungen == null)
			return false;
		for (final AbiturFachbelegung fachbelegung : fachbelegungen) {
			if (pruefeBelegungMitSchriftlichkeitEinzeln(fachbelegung, schriftlichkeit, halbjahr))
				return true;
		}
		return false;
	}


	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer in den angegebenen Halbjahren der angegebenen Schriftlichkeit entspricht.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls bei einer Fachbelegung die Schriftlichkeit in den Halbjahren gegeben ist, sonst false
	 */
	public boolean pruefeBelegungExistiertMitSchriftlichkeit(final List<@NotNull AbiturFachbelegung> fachbelegungen, final @NotNull GostSchriftlichkeit schriftlichkeit, final GostHalbjahr... halbjahre) {
		if (fachbelegungen == null)
			return false;
		for (final AbiturFachbelegung fachbelegung : fachbelegungen) {
			if (pruefeBelegungMitSchriftlichkeit(fachbelegung, schriftlichkeit, halbjahre))
				return true;
		}
		return false;
	}


	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer in den angegebenen Halbjahren mindestens einmal die angegebene Kursart
	 * hat. Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param kursart           die zu prüfende Kursart
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls die Kursart bei einer Fachbelegung mindestens einmal in den Halbjahren gegeben ist, sonst false
	 */
	public boolean pruefeBelegungExistiertHatMindestensEinmalKursart(final List<@NotNull AbiturFachbelegung> fachbelegungen, final @NotNull GostKursart kursart, final GostHalbjahr... halbjahre) {
		if (fachbelegungen == null)
			return false;
		for (final AbiturFachbelegung fachbelegung : fachbelegungen) {
			if (pruefeBelegungHatMindestensEinmalKursart(fachbelegung, kursart, halbjahre))
				return true;
		}
		return false;
	}


	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer in den angegebenen Halbjahren existiert,
	 * bei welchem in mind. einem der Halbjahren die angebene Schriftlichkeit nicht gegeben ist.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true oder false (siehe oben)
	 */
	public boolean pruefeBelegungExistiertErfuelltNicht(final List<@NotNull AbiturFachbelegung> fachbelegungen, final @NotNull GostSchriftlichkeit schriftlichkeit, final GostHalbjahr... halbjahre) {
		if (fachbelegungen == null)
			return false;
		for (final AbiturFachbelegung fachbelegung : fachbelegungen) {
			if (pruefeBelegungErfuelltNicht(fachbelegung, schriftlichkeit, halbjahre))
				return true;
		}
		return false;
	}


	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer in den angegebenen Halbjahren existiert,
	 * bei welchem in mind. einem der Halbjahren die angebene Schriftlichkeit nicht gegeben ist, sofern
	 * das Fach überhaupt belegt wurde.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true oder false (siehe oben)
	 */
	public boolean pruefeBelegungExistiertErfuelltNichtFallsBelegt(final List<@NotNull AbiturFachbelegung> fachbelegungen, final @NotNull GostSchriftlichkeit schriftlichkeit, final GostHalbjahr... halbjahre) {
		if (fachbelegungen == null)
			return false;
		for (final AbiturFachbelegung fachbelegung : fachbelegungen) {
			if (pruefeBelegungErfuelltNichtFallsBelegt(fachbelegung, schriftlichkeit, halbjahre))
				return true;
		}
		return false;
	}



	/**
	 * Prüft, ob die Belegung des Faches in den angegebenen Halbjahren der angegebenen Schriftlichkeit entspricht
	 * und das Fach durchgehend belegbar ist.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegung      die zu prüfende Fachnbelegung
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls die Schriftlichkeit in den Halbjahren gegeben ist, sonst false
	 */
	public boolean pruefeBelegungDurchgehendBelegbar(final AbiturFachbelegung fachbelegung, final @NotNull GostSchriftlichkeit schriftlichkeit, final GostHalbjahr... halbjahre) {
		if (fachbelegung == null)
			return false;
		if (!GostFachUtils.istDurchgehendBelegbarBisQ22(getFach(fachbelegung)))
			return false;
		return pruefeBelegungMitSchriftlichkeit(fachbelegung, schriftlichkeit, halbjahre);
	}


	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer in den angegebenen Halbjahren der angegebenen Schriftlichkeit entspricht
	 * und das Fach durchgängig belegbar ist.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachbelegungen
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls bei einer Fachbelegung die Schriftlichkeit in den Halbjahren gegeben ist, sonst false
	 */
	public boolean pruefeBelegungDurchgehendBelegbarExistiert(final List<@NotNull AbiturFachbelegung> fachbelegungen, final @NotNull GostSchriftlichkeit schriftlichkeit, final GostHalbjahr... halbjahre) {
		if (fachbelegungen == null)
			return false;
		for (final AbiturFachbelegung fachbelegung : fachbelegungen) {
			if (pruefeBelegungDurchgehendBelegbar(fachbelegung, schriftlichkeit, halbjahre))
				return true;
		}
		return false;
	}



	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer in den angegebenen Halbjahren der angegebenen Schriftlichkeit entspricht
	 * und das Fach durchgängig belegt ist.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich sofern das Fach durchgängig belegt wurde, da kein Halbjahr auf die
	 * Schriftlichkeit geprüft werden muss.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachbelegungen
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls bei eine Fachbelegung durchgängig belegt wurde und die Schriftlichkeit in den Halbjahren gegeben ist, sonst false
	 */
	public boolean pruefeBelegungDurchgehendBelegtExistiert(final List<@NotNull AbiturFachbelegung> fachbelegungen, final @NotNull GostSchriftlichkeit schriftlichkeit, final GostHalbjahr... halbjahre) {
		if (fachbelegungen == null)
			return false;
		for (final AbiturFachbelegung fachbelegung : fachbelegungen) {
			if (pruefeBelegung(fachbelegung, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22)
				&& pruefeBelegungMitSchriftlichkeit(fachbelegung, schriftlichkeit, halbjahre))
					return true;
		}
		return false;
	}




	/**
	 * Prüft, ob die Fachbelegung in mindestens einem der Halbjahre die angegebene Kursart aufweist.
	 * Existiert die Fachbelegung nicht (null), so kommt die Kursart auch nicht vor.
	 *
	 * @param fachbelegung   die Fachbelegung
	 * @param kursart        die Kursart
	 *
	 * @return true, falls mindestens einmal die Kursart belegt wurde, sonst false
	 */
	public boolean pruefeAufKursart(final AbiturFachbelegung fachbelegung, final @NotNull GostKursart kursart) {
		if (fachbelegung == null)
			return false;
		for (final AbiturFachbelegungHalbjahr belegunghalbjahr : fachbelegung.belegungen) {
			if ((belegunghalbjahr != null) && (!istNullPunkteBelegungInQPhase(belegunghalbjahr))
					&& GostKursart.fromKuerzel(belegunghalbjahr.kursartKuerzel) == kursart)
				return true;
		}
		return false;
	}


	/**
	 * Filtert die Fachbelegungen und gibt nur die Fachbelegungen zurück, bei denen die
	 * Kursart existiert.
	 * Wird keine Fachbelegung übergeben (null oder leere Liste), so kommt auch keine
	 * Belegung mit der Kursart vor.
	 *
	 * @param fachbelegungen   die Fachbelegungen
	 * @param kursart          die Kursart
	 *
	 * @return eine Liste mit den Fachbelegungen, welche die kursart haben
	 */
	public @NotNull List<@NotNull AbiturFachbelegung> filterBelegungKursartExistiert(final List<@NotNull AbiturFachbelegung> fachbelegungen, final @NotNull GostKursart kursart) {
		final @NotNull ArrayList<@NotNull AbiturFachbelegung> result = new ArrayList<>();
		if ((fachbelegungen == null) || (fachbelegungen.isEmpty()))
			return result;
		for (final AbiturFachbelegung fachbelegung : fachbelegungen) {
			if (pruefeAufKursart(fachbelegung, kursart))
				result.add(fachbelegung);
		}
		return result;
	}


	/**
	 * Prüft, ob die Fachbelegung eine durchgängige Belegung hat. Zusatzkurse können nicht für eine
	 * durchgängige Belegung zählen.
	 *
	 * @param fachbelegung   die zu prüfende Fachbelegung
	 *
	 * @return true, wenn die Belegung durchgängig ist.
	 */
	public boolean pruefeDurchgaengigkeit(final AbiturFachbelegung fachbelegung) {
		if ((fachbelegung == null) || (pruefeAufKursart(fachbelegung, GostKursart.ZK)))
			return false;
		return pruefeBelegung(fachbelegung, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22);
	}


	/**
	 * Zählt die Fachbelegungen, welche eine durchgängige Belegung aufweisen. Zusatzkurse zählen
	 * nicht für eine durchgängige Belegung.
	 * In dieser Methode wird ggf. auch geprüft, ob weitere Fachbelegungen existieren, welche das gleiche
	 * Statistik-Kürzel haben und Ersatzweise eine Halbjahres-Belegung ersetzen können. Dies ist bei bilingualen
	 * Fächern nötig.
	 *
	 * @param fachbelegungen   die zu überprüfenden Fachbelegungen
	 *
	 * @return die Anzahl der durchgängigen Belegungen
	 */
	public int zaehleDurchgaengigeBelegungen(final List<@NotNull AbiturFachbelegung> fachbelegungen) {
		if (fachbelegungen == null)
			return 0;
		int anzahl = 0;
		for (final AbiturFachbelegung fachbelegung : fachbelegungen) {
			final GostFach fach = faecherManager.get(fachbelegung.fachID);
			if (fach == null)
				continue;
			if (fachbelegung.belegungen[GostHalbjahr.EF1.id] == null)
				continue;
			final List<@NotNull AbiturFachbelegung> alleBelegungen = getFachbelegungByFachkuerzel(fach.kuerzel);
			if ((alleBelegungen == null) || (alleBelegungen.isEmpty()))
				continue;
			boolean hatBelegung = true;
			// Beachte zur Fortsetzung alle Fachbelegungen von Fächern des gleichen Statistik-Faches - dies kann bei bilingualen Fächern wichtig sein
			final @NotNull GostHalbjahr@NotNull[] halbjahre = { GostHalbjahr.EF1, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22 };
			for (final GostHalbjahr halbjahr : halbjahre) {
				boolean hatHalbjahresBelegung = false;
				for (final AbiturFachbelegung aktFachbelegung : alleBelegungen) {
					final AbiturFachbelegungHalbjahr belegungHalbjahr = aktFachbelegung.belegungen[halbjahr.id];
					if ((belegungHalbjahr != null) && (!istNullPunkteBelegungInQPhase(belegungHalbjahr))) {
						hatHalbjahresBelegung = true;
						break;
					}
				}
				if (!hatHalbjahresBelegung) {
					hatBelegung = false;
					break;
				}
			}
			if (hatBelegung)
				anzahl++;
		}
		return anzahl;
	}


	/**
	 * Prüft, ob die Fachbelegung eine durchgängige Belegung hat und prüft die Schriftlichkeit
	 * in der Qualifikationsphase. Ein Fach in der Qualifikationsphase gilt als Schriftlich belegt,
	 * sofern die ersten 3 Halbjahre der Qualifikationsphase schriftlich belegt wurden.
	 * - Zusatzkurse können nicht für eine durchgängige Belegung zählen.
	 *
	 * @param fachbelegung   die zu prüfende die zu überprüfenden Fachbelegung
	 *
	 * @return true, wenn die Belegung durchgängig ist und die Schriftlichkeit den Anforderungen genügt.
	 */
	public boolean pruefeDurchgaengigkeitSchriftlich(final AbiturFachbelegung fachbelegung) {
		if (!pruefeDurchgaengigkeit(fachbelegung))
			return false;
		return pruefeBelegungMitSchriftlichkeit(fachbelegung, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21);
	}


	/**
	 * Prüft, ob unter den angegebenen Fachbelegungen ein Fach als Abiturfach von einem der angegebenen Arten
	 * gewählt wurde. Wird keine Art angebeben, so wird jede Fachbelegung akzeptiert und true zurückgegeben.
	 *
	 * @param fachbelegungen   die Fachbelegungen
	 * @param arten            die Arten der Abiturfächer
	 *
	 * @return true, falls unter den Fachbelegungen mindestens ein Fach als Abiturfach von einem der
	 *         angegebenen Arten gewählt wurde und false sonst
	 */
	public boolean pruefeExistiertAbiFach(final List<@NotNull AbiturFachbelegung> fachbelegungen, final GostAbiturFach... arten) {
		if ((arten == null) || (arten.length == 0))
			return true;
		if (fachbelegungen == null)
			return false;
		for (final AbiturFachbelegung fachbelegung : fachbelegungen)
			for (final GostAbiturFach art : arten) {
				final GostAbiturFach abiturFach = GostAbiturFach.fromID(fachbelegung.abiturFach);
				if (abiturFach == art)
					return true;
			}
		return false;
	}


	/**
	 * Prüft, ob ein Abiturfach der übergebenen Art (1-4) existiert oder nicht.
	 *
	 * @param art   die Art des Abiturfaches (siehe {@link GostAbiturFach}
	 *
	 * @return true, falls die Art des Abiturfaches belegt wurde und ansonsten false
	 */
	public boolean hatAbiFach(final @NotNull GostAbiturFach art) {
		for (final @NotNull AbiturFachbelegung fachbelegung : abidaten.fachbelegungen) {
			final GostAbiturFach abiturFach = GostAbiturFach.fromID(fachbelegung.abiturFach);
			if (abiturFach == art)
				return true;
		}
		return false;
	}


	/**
	 * Prüft anhand des Statistik-Kürzels, ob in dem angegebenen Halbjahr eine doppelte Fachbelegung
	 * vorliegt oder nicht. Bei den Fremdsprachen werden nur unterschiedliche Fremdsprachen in einem Halbjahr
	 * akzeptiert und es dürfen mehrere Vertiefungsfächer (VX) in einem Halbjahr vorkommen. Fächer, die
	 * als nicht Prüfungsordnungs-relevant markiert sind werden ignoriert
	 *
	 * @param halbjahr   das zu prüfende Halbjahr
	 *
	 * @return true, falls eine doppelte Belegung vorliegt, sonst false
	 */
	public boolean hatDoppelteFachbelegungInHalbjahr(final @NotNull GostHalbjahr halbjahr) {
		final @NotNull HashSet<@NotNull String> set = new HashSet<>();
		final @NotNull List<@NotNull AbiturFachbelegung> fachbelegungen = abidaten.fachbelegungen;
		for (final AbiturFachbelegung fb : fachbelegungen) {
			final GostFach fach = getFach(fb);
			if ((fach == null) || (!fach.istPruefungsordnungsRelevant))
				continue;
			final AbiturFachbelegungHalbjahr belegungHalbjahr = getBelegungHalbjahr(fb, halbjahr, GostSchriftlichkeit.BELIEBIG);
			if ((belegungHalbjahr == null) || (istNullPunkteBelegungInQPhase(belegungHalbjahr)))
				continue;
			String kuerzel = GostFachUtils.getFremdsprache(fach);
			if (kuerzel == null)
				kuerzel = fach.kuerzel == null ? "" : fach.kuerzel;
			if (!set.add(kuerzel) && (!"VX".equals(kuerzel)))
				return true;
		}
		return false;
	}


	/**
	 * Prüft anhand des Statistik-Kürzels, ob in einem der angegebenen Halbjahre eine doppelte Fachbelegung
	 * vorliegt oder nicht. Bei den Fremdsprachen werden nur unterschiedliche Fremdsprachen in einem Halbjahr
	 * akzeptiert und es dürfen mehrere Vertiefungsfächer (VX) in einem Halbjahr vorkommen.
	 *
	 * @param halbjahre   die zu prüfenden Halbjahre
	 *
	 * @return true, falls eine doppelte Belegung vorliegt, sonst false
	 */
	public boolean hatDoppelteFachbelegung(final @NotNull GostHalbjahr... halbjahre) {
		if ((halbjahre == null) || (halbjahre.length == 0))
			return false;
		for (final GostHalbjahr halbjahr : halbjahre)
			if (hatDoppelteFachbelegungInHalbjahr(halbjahr))
				return true;
		return false;
	}


	/**
	 * Gibt zurück, ob der Projektkurs als besondere Lernleistung verwendet wird.
	 *
	 * @return true, falls der Projektkurs als besondere Lernleistung verwendet wird
	 */
	public boolean istProjektKursBesondereLernleistung() {
		return (GostBesondereLernleistung.PROJEKTKURS.is(abidaten.besondereLernleistung));
	}



	/**
	 * Bestimmt die Fachbelegung des Faches mit der angegebenen ID
	 *
	 * @param fachID   die ID des Faches
	 *
	 * @return die Fachbelegung oder null, falls keine vorhanden ist
	 */
	public AbiturFachbelegung getFachbelegungByID(final long fachID) {
		final @NotNull List<@NotNull AbiturFachbelegung> fachbelegungen = abidaten.fachbelegungen;
		for (final AbiturFachbelegung fb : fachbelegungen) {
			final GostFach fach = getFach(fb);
			if ((fach != null) && (fachID == fach.id))
				return fb;
		}
		return null;
	}


	/**
	 * Bestimmt die erste Fachbelegung mit dem angegebenen Statistik-Kürzel
	 *
	 * @param kuerzel          das Kürzel des Faches, kann null sein (dann wird auch null zurückgegeben)
	 *
	 * @return die Fachbelegung oder null, falls keine vorhanden ist
	 */
	public AbiturFachbelegung getFachbelegungByKuerzel(final String kuerzel) {
		if ((kuerzel == null) || ("".equals(kuerzel)))
			return null;
		final @NotNull List<@NotNull AbiturFachbelegung> fachbelegungen = abidaten.fachbelegungen;
		for (final AbiturFachbelegung fb : fachbelegungen) {
			final GostFach fach = getFach(fb);
			if ((fach != null) && (kuerzel.equals(fach.kuerzel)))
				return fb;
		}
		return null;
	}


	/**
	 * Liefert alle Fachbelegungen der Abiturdaten, welche den angegebenen Fachbereichen zuzuordnen sind.
	 * Wird kein Fachbereich angegeben, so werden alle Fachbelegungen der Abiturdaten zurückgegeben.
	 *
	 * @param fachbereiche   die Fachbereiche
	 *
	 * @return eine Liste der Fachbelegungen aus den Fachbereichen
	 */
	public @NotNull List<@NotNull AbiturFachbelegung> getFachbelegungen(final GostFachbereich... fachbereiche) {
		if ((fachbereiche == null) || (fachbereiche.length == 0))
			return abidaten.fachbelegungen;
		final @NotNull ArrayList<@NotNull AbiturFachbelegung> result = new ArrayList<>();
		for (final GostFachbereich fachbereich : fachbereiche) {
			final List<@NotNull AbiturFachbelegung> fachbelegungen = mapFachbereiche.get(fachbereich);
			if (fachbelegungen == null)
				continue;
			result.addAll(fachbelegungen);
		}
		return result;
	}


	/**
	 * Liefert alle Prüfungsordnungs-relevanten Fachbelegungen der Abiturdaten, welche den angegebenen Fachbereichen
	 * zuzuordnen sind.
	 * Wird kein Fachbereich angegeben, so werden alle Fachbelegungen der Abiturdaten zurückgegeben.
	 *
	 * @param fachbereiche   die Fachbereiche
	 *
	 * @return eine Liste der Fachbelegungen aus den Fachbereichen
	 */
	public @NotNull List<@NotNull AbiturFachbelegung> getRelevanteFachbelegungen(final GostFachbereich... fachbereiche) {
		if ((fachbereiche == null) || (fachbereiche.length == 0))
			return abidaten.fachbelegungen;
		final @NotNull ArrayList<@NotNull AbiturFachbelegung> result = new ArrayList<>();
		for (final GostFachbereich fachbereich : fachbereiche) {
			final List<@NotNull AbiturFachbelegung> fachbelegungen = mapFachbereicheRelevant.get(fachbereich);
			if (fachbelegungen == null)
				continue;
			result.addAll(fachbelegungen);
		}
		return result;
	}


	/**
	 * Liefert alle Fachbelegungen, die bilingual unterrichtet wurden.
	 *
	 * @return eine Liste der Fachbelegungen
	 */
	public @NotNull List<@NotNull AbiturFachbelegung> getFachbelegungenBilingual() {
		final @NotNull List<@NotNull AbiturFachbelegung> result = new ArrayList<>();
		final @NotNull List<@NotNull AbiturFachbelegung> fachbelegungen = abidaten.fachbelegungen;
		for (final AbiturFachbelegung fb : fachbelegungen) {
			if (zaehleBelegung(fb) <= 0)
				continue;
			final GostFach fach = getFach(fb);
			if ((fach != null) && (!GostFachbereich.FREMDSPRACHE.hat(fach)) && (!GostFachbereich.DEUTSCH.hat(fach))
					&& (fach.biliSprache != null) && (!"D".equals(fach.biliSprache)))
				result.add(fb);
		}
		return result;
	}



	/**
	 * Filtert die Fachbelegungen auf neu einsetzende Fremdsprachen.
	 *
	 * @param fachbelegungen   die zu filternden Fachbelegungen
	 *
	 * @return die gefilterten Fachbelegungen
	 */
	public @NotNull List<@NotNull AbiturFachbelegung> filterFremdspracheNeuEinsetzend(final List<@NotNull AbiturFachbelegung> fachbelegungen) {
		if (fachbelegungen == null)
			return Collections.emptyList();
		final @NotNull ArrayList<@NotNull AbiturFachbelegung> result = new ArrayList<>();
		for (final AbiturFachbelegung fb : fachbelegungen) {
			final GostFach fach = getFach(fb);
			if ((fach != null) && fach.istFremdsprache && fach.istFremdSpracheNeuEinsetzend)
				result.add(fb);
		}
		return result;
	}


	/**
	 * Filtert die Fachbelegungen auf fortgeführte Fremdsprachen.
	 *
	 * @param fachbelegungen   die zu filternden Fachbelegungen
	 *
	 * @return die gefilterten Fachbelegungen
	 */
	public @NotNull List<@NotNull AbiturFachbelegung> filterFremdspracheFortgefuehrt(final List<@NotNull AbiturFachbelegung> fachbelegungen) {
		if (fachbelegungen == null)
			return Collections.emptyList();
		final @NotNull ArrayList<@NotNull AbiturFachbelegung> result = new ArrayList<>();
		for (final AbiturFachbelegung fb : fachbelegungen) {
			final GostFach fach = getFach(fb);
			if ((fach != null) && fach.istFremdsprache && !fach.istFremdSpracheNeuEinsetzend)
				result.add(fb);
		}
		return result;
	}


	/**
	 * Filtert die Fachbelegungen danach, ob sie durchgehend belegbar sind
	 *
	 * @param fachbelegungen   die zu filternden Fachbelegungen
	 *
	 * @return die gefilterten Fachbelegungen
	 */
	public @NotNull List<@NotNull AbiturFachbelegung> filterDurchgehendBelegbar(final List<@NotNull AbiturFachbelegung> fachbelegungen) {
		final @NotNull ArrayList<@NotNull AbiturFachbelegung> result = new ArrayList<>();
		if (fachbelegungen == null)
			return result;
		for (final AbiturFachbelegung fb : fachbelegungen) {
			final GostFach fach = getFach(fb);
			if (GostFachUtils.istDurchgehendBelegbarBisQ22(fach))
				result.add(fb);
		}
		return result;
	}


	/**
	 * Filtert die Fachbelegungen. Es werden nur Fachbelegungen behalten, die in den angegebenen Halbjahren eine Belegung aufweisen.
	 * Wird kein Halbjahr angegeben, so wird nichts gefiltert, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegungen    die zu filternden Fachbelegungen
	 * @param halbjahre         die Halbjahre, die belegt sein müssen
	 *
	 * @return die gefilterten Fachbelegungen
	 */
	public @NotNull List<@NotNull AbiturFachbelegung> filterBelegungen(final List<@NotNull AbiturFachbelegung> fachbelegungen, final GostHalbjahr... halbjahre) {
		if (fachbelegungen == null)
			return Collections.emptyList();
		final @NotNull ArrayList<@NotNull AbiturFachbelegung> result = new ArrayList<>();
		for (final AbiturFachbelegung fb : fachbelegungen) {
			if (pruefeBelegung(fb, halbjahre))
				result.add(fb);
		}
		return result;
	}


	/**
	 * Bestimmt die Menge der unterschiedlichen Statistik-Fächer in der angegebenen Fachbelegungen.
	 *
	 * @param fachbelegungen   die Fachbelegungen
	 *
	 * @return die Menge der Statistik-Fächer
	 */
	private @NotNull Set<ZulaessigesFach> getMengeStatistikFaecher(final @NotNull List<@NotNull AbiturFachbelegung> fachbelegungen) {
		final @NotNull HashSet<ZulaessigesFach> faecher = new HashSet<>();
		for (final AbiturFachbelegung fb : fachbelegungen) {
			final GostFach fach = faecherManager.get(fb.fachID);
			if (fach == null)
				continue;
			final @NotNull ZulaessigesFach zulFach = ZulaessigesFach.getByKuerzelASD(fach.kuerzel);
			if (zulFach != ZulaessigesFach.DEFAULT)
				faecher.add(zulFach);
		}
		return faecher;
	}


	/**
	 * Diese Methode zählt die Anzahl der angegebenen Fachbelegungen, welche in allen
	 * Halbjahren belegt sind. Dabei werden Fachbelegungen, welche dem gleichem Statistik-Fach
	 * zuzuordnen sind zusammengefasst. Dies ist bei der Abwahl von bilingualen Sachfächern
	 * relevant.
	 *
	 * @param fachbelegungen   die zu zählenden Fachbelegungen
	 *
	 * @return die Anzahl der Fachbelegungen
	 */
	public int zaehleBelegungenDurchgaengig(final List<@NotNull AbiturFachbelegung> fachbelegungen) {
		if (fachbelegungen == null)
			return 0;
		// Bestimme zunächst die Menge der unterschiedlichen Statistik-Fächer
		final @NotNull Set<ZulaessigesFach> faecher = getMengeStatistikFaecher(fachbelegungen);
		// Bestimme nun die Anzahl der Fachbelegungen, die in den Halbjahren existieren.
		int count = 0;
		for (final ZulaessigesFach zulFach : faecher) {
			boolean vorhanden = true;
			for (final GostHalbjahr halbjahr : GostHalbjahr.values()) {
				boolean belegung_vorhanden = false;
				for (final AbiturFachbelegung fb : fachbelegungen) {
					final GostFach fbFach = faecherManager.get(fb.fachID);
					if (fbFach == null)
						continue;
					final @NotNull ZulaessigesFach fbZulFach = ZulaessigesFach.getByKuerzelASD(fbFach.kuerzel);
					final AbiturFachbelegungHalbjahr belegungHalbjahr = fb.belegungen[halbjahr.id];
					if ((zulFach == fbZulFach) && (belegungHalbjahr != null) && (!istNullPunkteBelegungInQPhase(belegungHalbjahr))) {
						belegung_vorhanden = true;
						break;
					}
				}
				if (!belegung_vorhanden) {
					vorhanden = false;
					break;
				}
			}
			if (vorhanden)
				count++;
		}
		return count;
	}


	/**
	 * Diese Methode zählt die Anzahl der angegebenen Fachbelegungen, welche in allen
	 * Halbjahren belegt sind. Dabei werden Fachbelegungen, welche dem gleichem Statistik-Fach
	 * zuzuordnen sind zusammengefasst. Dies ist bei der Abwahl von bilingualen Sachfächern
	 * relevant.
	 *
	 * @param fachbelegungen   die zu zählenden Fachbelegungen
	 *
	 * @return die Anzahl der Fachbelegungen
	 */
	public int zaehleBelegungenDurchgaengigSchriftlichInQPhase(final List<@NotNull AbiturFachbelegung> fachbelegungen) {
		if (fachbelegungen == null)
			return 0;
		// Bestimme zunächst die Menge der unterschiedlichen Statistik-Fächer
		final @NotNull Set<ZulaessigesFach> faecher = getMengeStatistikFaecher(fachbelegungen);
		// Bestimme nun die Anzahl der Fachbelegungen, die in den Halbjahren existieren.
		int count = 0;
		for (final ZulaessigesFach zulFach : faecher) {
			boolean vorhanden = true;
			for (final GostHalbjahr halbjahr : GostHalbjahr.values()) {
				boolean belegung_vorhanden = false;
				for (final AbiturFachbelegung fb : fachbelegungen) {
					final GostFach fbFach = faecherManager.get(fb.fachID);
					if (fbFach == null)
						continue;
					final @NotNull ZulaessigesFach fbZulFach = ZulaessigesFach.getByKuerzelASD(fbFach.kuerzel);
					if (zulFach == fbZulFach) {
						final AbiturFachbelegungHalbjahr belegungHalbjahr = fb.belegungen[halbjahr.id];
						if ((belegungHalbjahr != null) && (!istNullPunkteBelegungInQPhase(belegungHalbjahr))) {
							boolean istSchriftlichkeitOK = true;
							if (((halbjahr == GostHalbjahr.Q11) || (halbjahr == GostHalbjahr.Q12) || (halbjahr == GostHalbjahr.Q21)) && (!belegungHalbjahr.schriftlich))
								istSchriftlichkeitOK = false;
							if (istSchriftlichkeitOK) {
								belegung_vorhanden = true;
								break;
							}
						}
					}
				}
				if (!belegung_vorhanden) {
					vorhanden = false;
					break;
				}
			}
			if (vorhanden)
				count++;
		}
		return count;
	}


	/**
	 * Filtert die Fachbelegungen. Es werden nur Belegungen behalten, die in den angegebenen Halbjahren die geforderte Schriftlichkeit aufweisen.
	 * Wird kein Halbjahr angegeben, so wird nichts gefiltert, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegungen    die zu filternden Fachbelegungen
	 * @param schriftlichkeit   die geforderte Schriftlichkeit
	 * @param halbjahre         die Halbjahre, die belegt sein müssen
	 *
	 * @return die gefilterten Fachbelegungen
	 */
	public @NotNull List<@NotNull AbiturFachbelegung> filterBelegungenMitSchriftlichkeit(final List<@NotNull AbiturFachbelegung> fachbelegungen, final @NotNull GostSchriftlichkeit schriftlichkeit, final GostHalbjahr... halbjahre) {
		if (fachbelegungen == null)
			return Collections.emptyList();
		final @NotNull ArrayList<@NotNull AbiturFachbelegung> result = new ArrayList<>();
		for (final AbiturFachbelegung fb : fachbelegungen) {
			if (pruefeBelegungMitSchriftlichkeit(fb, schriftlichkeit, halbjahre))
				result.add(fb);
		}
		return result;
	}


	/**
	 * Liefert die erste Fachbelegung für den Fachbereich - sofern eine existiert
	 *
	 * @param fachbereich   der Fachbereich
	 *
	 * @return die Fachbelegung oder null
	 */
	public AbiturFachbelegung getFachbelegung(final @NotNull GostFachbereich fachbereich) {
		final ArrayList<@NotNull AbiturFachbelegung> faecher = mapFachbereiche.get(fachbereich);
		if ((faecher == null) || (faecher.isEmpty()))
			return null;
		return faecher.get(0);
	}


	/**
	 * Liefert die erste Prüfungsordnungs-relevante Fachbelegung für den Fachbereich - sofern eine existiert
	 *
	 * @param fachbereich   der Fachbereich
	 *
	 * @return die Fachbelegung oder null
	 */
	public AbiturFachbelegung getRelevanteFachbelegung(final @NotNull GostFachbereich fachbereich) {
		final ArrayList<@NotNull AbiturFachbelegung> faecher = mapFachbereicheRelevant.get(fachbereich);
		if ((faecher == null) || (faecher.isEmpty()))
			return null;
		return faecher.get(0);
	}


	/**
	 * Liefert alle Fachbelegungen mit dem angegebenen Statistk-Kürzel des Faches
	 *
	 * @param kuerzel   das Kürzel des Faches
	 *
	 * @return eine Liste mit den Fachbelegungen
	 */
	public @NotNull List<@NotNull AbiturFachbelegung> getFachbelegungByFachkuerzel(final String kuerzel) {
		final @NotNull List<@NotNull AbiturFachbelegung> fachbelegungen = new ArrayList<>();
		if (kuerzel == null)
			return fachbelegungen;
		final @NotNull List<@NotNull AbiturFachbelegung> tmpFachbelegungen = abidaten.fachbelegungen;
		for (final AbiturFachbelegung fachbelegung : tmpFachbelegungen) {
			final GostFach fach = faecherManager.get(fachbelegung.fachID);
			if ((fach == null) || (!kuerzel.equals(fach.kuerzel)))
				continue;
			fachbelegungen.add(fachbelegung);
		}
		return fachbelegungen;
	}


	/**
	 * Prüft, ob der Kurs in dem angegebenen Halbjahr mit der angegebenen Schriftlichkeit belegt ist
	 * und gibt ggf. die Belegung zurück.
	 *
	 * @param fachbelegung   die Abiturfachbelegung aus welcher die Belegungsinformationen für das Halbjahr entnommen wird
	 * @param halbjahr       das Halbjahr, in welchem die Belegung gesucht wird.
	 * @param schriftlich    gibt an, ob das Fach schriftlich oder mündlich belegt sein muss
	 *
	 * @return die Belegungsinformationen zu dem Fach
	 */
	public AbiturFachbelegungHalbjahr getBelegungHalbjahr(final @NotNull AbiturFachbelegung fachbelegung, final @NotNull GostHalbjahr halbjahr, final @NotNull GostSchriftlichkeit schriftlich) {
		final AbiturFachbelegungHalbjahr belegungHalbjahr = fachbelegung.belegungen[halbjahr.id];
		return ((belegungHalbjahr != null) && (!istNullPunkteBelegungInQPhase(belegungHalbjahr))
				&& ((schriftlich == GostSchriftlichkeit.BELIEBIG)
				|| ((schriftlich == GostSchriftlichkeit.SCHRIFTLICH) && (belegungHalbjahr.schriftlich))
				|| ((schriftlich == GostSchriftlichkeit.MUENDLICH) && (!belegungHalbjahr.schriftlich))))
				? belegungHalbjahr : null;
	}


	/**
	 * Liefert die erste Sprachbelegung - sofern eine existiert
	 *
	 * @param sprache   das einstellige Kürzel der Sprache
	 *
	 * @return die Fachbelegung für die Sprache
	 */
	public AbiturFachbelegung getSprachbelegung(final String sprache) {
		if (sprache == null)
			return null;
		final @NotNull List<@NotNull AbiturFachbelegung> fachbelegungen = abidaten.fachbelegungen;
		for (final AbiturFachbelegung fb : fachbelegungen) {
			final GostFach fach = getFach(fb);
			if ((fach == null) || (!GostFachUtils.istFremdsprachenfach(fach, sprache)))
				continue;
			if (sprache.equals(GostFachUtils.getFremdsprache(fach)))
				return fb;
		}
		return null;
	}


	/**
	 * Prüft, ob das fach mit der übergebenen ID als Abiturfach möglich ist und mit welcher Kursart
	 * dieses Fach aufgrund der Belegungen im Abitur gewählt werden kann.
	 *
	 * @param id   die ID des Fachs
	 *
	 * @return die mögliche Kursart des Faches im Abitur oder null, falls das Fach nicht als Abiturfach
	 *         ausgewählt werden kann.
	 */
	public GostKursart getMoeglicheKursartAlsAbiturfach(final long id) {
		final GostFach fach = faecherManager.get(id);
		if ((fach == null) || (!fach.istPruefungsordnungsRelevant))
			return null;
		final AbiturFachbelegung belegung = getFachbelegungByID(id);
		if ((belegung == null) || (belegung.letzteKursart == null))
			return null;
		final GostKursart kursart = GostKursart.fromKuerzel(belegung.letzteKursart);
		if ((kursart == null) || ((kursart == GostKursart.LK) && (!fach.istMoeglichAbiLK))
				 || ((kursart == GostKursart.GK) && (!fach.istMoeglichAbiGK))
				 || ((kursart != GostKursart.GK) && (kursart != GostKursart.LK)))
			return null;
		// LK ?
		if (kursart == GostKursart.LK)
			return pruefeBelegungMitKursart(belegung, kursart, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22)
					? kursart : null;
		// GK ?
		if (belegung.belegungen[GostHalbjahr.Q22.id] == null)
			return null;
		// Bestimme die Fachbelegungen für das Fach bzw. Fächer mit gleichem Statistikkürzel bzw. mit den anderen Konfessionen
		// im Falle einer Religion, da ein Konfessionswechsel ggf. auch die Belegung als Abiturfach erlaubt
		final @NotNull List<@NotNull AbiturFachbelegung> fachbelegungen = GostFachbereich.RELIGION.hat(fach)
				? getFachbelegungen(GostFachbereich.RELIGION)
				: getFachbelegungByFachkuerzel(fach.kuerzel);
		return pruefeBelegungExistiertMitSchriftlichkeit(fachbelegungen, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q11)
				&& pruefeBelegungExistiertMitSchriftlichkeit(fachbelegungen, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q12)
				&& pruefeBelegungExistiertMitSchriftlichkeit(fachbelegungen, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q21)
				? kursart : null;
	}


	/**
	 * Liefert für die übergebene Fachbelegung die Halbjahre, in denen das Fach mit einer der angebenen
	 * Kursarten belegt wurde. Ist keine Kursart angegeben, so werden die Halbjahre aller Belegungen
	 * zurückgegeben. Ist keine Fachbelegung angegeben, so wird eine leere Liste zurückgegeben.
	 *
	 * @param fachbelegung   die Fachbelegung
	 * @param kursarten      die Kursarten
	 *
	 * @return eine Liste der Halbjahre in den das Fach mit einer der Kursarten belegt wurde
	 */
	public @NotNull List<@NotNull GostHalbjahr> getHalbjahreKursart(final AbiturFachbelegung fachbelegung, final GostKursart... kursarten) {
		final @NotNull ArrayList<@NotNull GostHalbjahr> halbjahre = new ArrayList<>();
		if (fachbelegung != null) {
			for (final AbiturFachbelegungHalbjahr belegungHalbjahr : fachbelegung.belegungen) {
				if ((belegungHalbjahr == null) || (istNullPunkteBelegungInQPhase(belegungHalbjahr)))
					continue;
				final GostHalbjahr halbjahr = GostHalbjahr.fromKuerzel(belegungHalbjahr.halbjahrKuerzel);
				if (halbjahr == null)
					continue;
				if ((kursarten == null) || (kursarten.length == 0)) {
					halbjahre.add(halbjahr);
					continue;
				}
				for (final GostKursart kursart : kursarten) {
					if (kursart.equals(GostKursart.fromKuerzel(belegungHalbjahr.kursartKuerzel))) {
						halbjahre.add(halbjahr);
						break;
					}
				}
			}
		}
		return halbjahre;
	}



	/**
	 * Gibt die Sprache des bilingualen Bildungsgang zurück oder null, falls keiner gewählt wurde.
	 *
	 * @return die Sprache des bilingualen Bildungsgang oder null
	 */
	public String getBiligualenBildungsgang() {
		return abidaten.bilingualeSprache;
	}


	/**
	 * Prüft bei der Sprachenfolge, ob eine laut Sprachenfolge fortgeführte
	 * Fremdsprache fehlerhafterweise als neu einsetzende Fremdsprache belegt wurde.
	 * Übergebene Fachbelegungen, die keine Fremdsprachen sind werden ignoriert.
	 *
	 * @param fremdsprachen   die zu prüfenden Fachbelegungen
	 *
	 * @return true, falls eine fortgeführte Fremdsprache bei den übergebenen
	 *         Fachbelegungen existiert, ansonsten false
	 */
	public boolean hatFortgefuehrteFremdspracheInSprachendaten(final List<@NotNull AbiturFachbelegung> fremdsprachen) {
		if (fremdsprachen == null)
			return false;
		if (abidaten.sprachendaten == null)
			return false;
		for (final AbiturFachbelegung fs: fremdsprachen) {
			final GostFach fach = getFach(fs);
			if ((fach == null) || (!fach.istFremdsprache))
				continue;
			if (SprachendatenUtils.istFortfuehrbareSpracheInGOSt(abidaten.sprachendaten, GostFachUtils.getFremdsprache(fach))) {
				return true;
			}
		}
		return false;
	}


	/**
	 * Prüft bei der Sprachenfolge, ob für eine laut Sprachenfolge neu einsetzende
	 * Fremdsprache fehlerhafterweise ein Kurs in einer fortgeführten Fremdsprache belegt wurde.
	 * Übergebene Fachbelegungen, die keine Fremdsprachen sind werden ignoriert.
	 *
	 * @param fremdsprachen   die zu prüfenden Fachbelegungen
	 *
	 * @return true, falls eine neu einsetzende Fremdsprache bei den übergebenen
	 *         Fachbelegungen existiert, ansonsten false
	 */
	public boolean hatNeuEinsetzendeFremdspracheInSprachendaten(final List<@NotNull AbiturFachbelegung> fremdsprachen) {
		if (fremdsprachen == null)
			return false;
		if (abidaten.sprachendaten == null)
			return false;
		for (final AbiturFachbelegung fs: fremdsprachen) {
			final GostFach fach = getFach(fs);
			if ((fach == null) || (!fach.istFremdsprache))
				continue;
			if (SprachendatenUtils.istNeueinsetzbareSpracheInGOSt(abidaten.sprachendaten, GostFachUtils.getFremdsprache(fach))) {
				return true;
			}
		}
		return false;
	}


	/**
	 * Prüft, ob die Belegung seit der EF1 vorhanden ist. Hierbei werden
	 * Zusatz-, Vertiefungs- und Projektkurse auch als später einsetzend akzeptiert.
	 * Dies gilt auch für Literatur, instrumental- und vokal-praktische Kurse sowie
	 * für Religion und Philosophie.
	 *
	 * @param fachbelegung   die Abiturfachbelegungen, die geprüft werden
	 *
	 * @return true, falls das Fach seit EF1 durchgängig belegt wurde oder eine der Ausnahmen zutrifft, sonst false
	 */
	public boolean istBelegtSeitEF(final @NotNull AbiturFachbelegung fachbelegung) {
		final GostFach fach = getFach(fachbelegung);
		if (fach == null)
			return false;
		if (GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hat(fach))
			return true;
		if (GostFachbereich.RELIGION.hat(fach))
			return true;
		if ("PL".equals(fach.kuerzel))
			return true;
		for (final AbiturFachbelegungHalbjahr belegungHalbjahr : fachbelegung.belegungen) {
			if ((belegungHalbjahr == null) || (istNullPunkteBelegungInQPhase(belegungHalbjahr)))
				continue;
			final GostHalbjahr halbjahr = GostHalbjahr.fromKuerzel(belegungHalbjahr.halbjahrKuerzel);
			final GostKursart kursart = GostKursart.fromKuerzel(belegungHalbjahr.kursartKuerzel);
			if ((halbjahr == null) || (kursart == null))
				continue;
			if ((kursart == GostKursart.ZK) || (kursart == GostKursart.PJK) || (kursart == GostKursart.VTF))
				continue;
			// Prüfe, ob das vorige Halbjahr auch belegt wurde
			final GostHalbjahr prevHalbjahr = halbjahr.previous();
			if (prevHalbjahr == null)
				continue;
			final AbiturFachbelegungHalbjahr belegungHalbjahrVorher = fachbelegung.belegungen[prevHalbjahr.id];
			if ((belegungHalbjahrVorher == null) || (istNullPunkteBelegungInQPhase(belegungHalbjahrVorher))) {
				// Prüfe, ob eine Belegung des gleichen Statistik-Faches im Halbjahr zuvor existiert - dies kann bei bilingualen Fächern auftreten
				final List<@NotNull AbiturFachbelegung> alleBelegungen = getFachbelegungByFachkuerzel(fach.kuerzel);
				if ((alleBelegungen == null) || (alleBelegungen.size() <= 1))
					return false;
				if (!pruefeBelegungExistiert(alleBelegungen, prevHalbjahr))
					return false;
			}
		}
		return true;
	}


	/**
	 * Gibt zurück, ob ein Zusatzkurs in Geschichte in diesem Jahrgang angeboten
	 * wird oder nicht.
	 *
	 * @return true, wenn einer angeboten wird und ansonsten false
	 */
	public boolean istErlaubtZusatzkursGE() {
		if (_jahrgangsdaten == null)
			return true; // Default: Zusatzkurse werden angeboten
		return _jahrgangsdaten.hatZusatzkursGE;
	}


	/**
	 * Gibt das erste Halbjahr für Zusatzkurse in Geschichte
	 * in diesem Jahrgang zurück.
	 *
	 * @return das erste Halbjahr für Zusatzkurse in Geschichte
	 */
	public @NotNull GostHalbjahr getBeginnZusatzkursGE() {
		if (_jahrgangsdaten == null)
			return GostHalbjahr.Q21; // Default: Zusatzkurse in der Q2
		final GostHalbjahr hj = GostHalbjahr.fromKuerzel(_jahrgangsdaten.beginnZusatzkursGE);
		if (hj == null)
			return GostHalbjahr.Q21; // Default: Zusatzkurse in der Q2
		return hj;
	}


	/**
	 * Gibt zurück, ob ein Zusatzkurs in Sozialwissenschaften in diesem Jahrgang
	 * angeboten wird oder nicht.
	 *
	 * @return true, wenn einer angeboten wird und ansonsten false
	 */
	public boolean istErlaubtZusatzkursSW() {
		if (_jahrgangsdaten == null)
			return true; // Default: Zusatzkurse werden angeboten
		return _jahrgangsdaten.hatZusatzkursSW;
	}


	/**
	 * Gibt das erste Halbjahr für Zusatzkurse in Sozialwissenschaften
	 * in diesem Jahrgang zurück.
	 *
	 * @return das erste Halbjahr für Zusatzkurse in Sozialwissenschaften
	 */
	public @NotNull GostHalbjahr getBeginnZusatzkursSW() {
		if (_jahrgangsdaten == null)
			return GostHalbjahr.Q21; // Default: Zusatzkurse in der Q2
		final GostHalbjahr hj = GostHalbjahr.fromKuerzel(_jahrgangsdaten.beginnZusatzkursSW);
		if (hj == null)
			return GostHalbjahr.Q21; // Default: Zusatzkurse in der Q2
		return hj;
	}


	/**
	 * Gibt alle Fachkombination zurück, welche in der EF.1 gültig sind.
	 *
	 * @return die Liste mit den Fachkombinationen
	 */
	public @NotNull List<@NotNull GostJahrgangFachkombination> getFachkombinationenEF1() {
		final @NotNull List<@NotNull GostJahrgangFachkombination> kombis = new ArrayList<>();
		for (final @NotNull GostJahrgangFachkombination kombi : faecherManager.getFachkombinationen())
			if (kombi.gueltigInHalbjahr[GostHalbjahr.EF1.id])
				kombis.add(kombi);
		return kombis;
	}


	/**
	 * Gibt alle Fachkombination zurück.
	 *
	 * @return die Liste mit den Fachkombinationen
	 */
	public @NotNull List<@NotNull GostJahrgangFachkombination> getFachkombinationenGesamt() {
		return faecherManager.getFachkombinationen();
	}


	/**
	 * Gibt das Ergebnis der Belegprüfung zurück. Dieses enthält eine Liste der Fehler, die bei der Belegprüfung
	 * festgestellt wurden und ob diese erfolgreich gewesen ist oder nicht.
	 *
	 * @return das Ergebnis der Belegprüfung
	 */
	@JsonIgnore
	public @NotNull GostBelegpruefungErgebnis getBelegpruefungErgebnis() {
		final @NotNull GostBelegpruefungErgebnis ergebnis = new GostBelegpruefungErgebnis();
		ergebnis.erfolgreich = belegpruefungErfolgreich;
		for (int i = 0; i < belegpruefungsfehler.size(); i++) {
			final @NotNull GostBelegungsfehler fehler = belegpruefungsfehler.get(i);
			ergebnis.fehlercodes.add(new GostBelegpruefungErgebnisFehler(fehler, pruefungsArt));
		}
		// TODO Ergänze das Ergebnis um einen Log der Belegprüfung
		return ergebnis;
	}

	/**
	 * Gibt das Belegprüfungs-Objekt für die KurszahlenUndWochenstunden zurück, welches für
	 * die Belegprüfung genutzt wurde und die Statistiken dazu erstellt hat.
	 *
	 * @return das Belegprüfungs-Objekt für die KurszahlenUndWochenstunden
	 */
	private @NotNull KurszahlenUndWochenstunden getKurszahlenUndWochenstunden() {
		if (this.belegpruefungKurszahlenUndWochenstunden == null)
			throw new NullPointerException("Die Belegprüfung zu Kurszahlen und Wochenstunden wurde noch nicht erstellt und durchgeführt.");
		return this.belegpruefungKurszahlenUndWochenstunden;
	}


	/**
	 * Berechnet die Wochenstunden, welche von dem Schüler in den einzelnen
	 * Halbjahren der gymnasialen Oberstufe für das Abitur relevant belegt wurden.
	 *
	 * @return ein Array mit den Wochenstunden für die sechs Halbjahre
	 */
	public @NotNull int[] getWochenstunden() {
		final @NotNull KurszahlenUndWochenstunden kuw = getKurszahlenUndWochenstunden();
		final @NotNull int[] stunden = new int[] {0, 0, 0, 0, 0, 0};
		for (final GostHalbjahr hj : GostHalbjahr.values())
			stunden[hj.id] = kuw.getWochenstunden(hj);
		return stunden;
	}

	/**
	 * Berechnet die Anzahl der anrechenbaren Kurse, welche von dem Schüler in den einzelnen
	 * Halbjahren der gymnasialen Oberstufe für das Abitur belegt wurden.
	 *
	 * @return ein Array mit den anrechenbaren Kursen für die sechs Halbjahre
	 */
	public @NotNull int[] getAnrechenbareKurse() {
		final @NotNull KurszahlenUndWochenstunden kuw = getKurszahlenUndWochenstunden();
		final @NotNull int[] anzahl = new int[] {0, 0, 0, 0, 0, 0};
		for (final GostHalbjahr hj : GostHalbjahr.values())
			anzahl[hj.id] = kuw.getKurszahlenAnrechenbar(hj);
		return anzahl;
	}

}
