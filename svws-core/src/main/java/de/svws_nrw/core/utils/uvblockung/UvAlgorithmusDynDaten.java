package de.svws_nrw.core.utils.uvblockung;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.core.data.uv.UvFach;
import de.svws_nrw.core.data.uv.UvKlasse;
import de.svws_nrw.core.data.uv.UvKlassenLehrer;
import de.svws_nrw.core.data.uv.UvLehrer;
import de.svws_nrw.core.data.uv.UvLerngruppe;
import de.svws_nrw.core.data.uv.UvLerngruppenLehrer;
import de.svws_nrw.core.data.uv.UvPlanungsabschnitt;
import de.svws_nrw.core.data.uv.regel.UvBlockungRegel;
import de.svws_nrw.core.data.uv.regel.UvBlockungRegelPrioritaet;
import de.svws_nrw.core.data.uv.regel.UvBlockungRegelTyp;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.utils.uv.UvManager;
import de.svws_nrw.core.utils.uv.UvRegelManager;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse implementiert einen Algorithmus um Lerngruppen Lehrkräften zuzuordnen.
 * Dabei gibt es {@link UvBlockungRegel} die bei der Berechnung mit Priorität einbezogen werden.
 *
 * @author Benjamin A. Bartsch
 */
public final class UvAlgorithmusDynDaten {

	/** Debug-Ausgaben. */
	private static final boolean LOG_ADDITIONAL = false;

	/** Ein Logger für Debug-Zwecke. */
	private final @NotNull Logger log;

	/** Ein Random-Objekt für Zufallsentscheidungen bei der Berechnung. */
	private final @NotNull Random rnd;

	/** Die Eingabedaten von der GUI (UV). */
	private final @NotNull UvManager man;

	/** Die Eingabedaten von der GUI (UV-Regeln). */
	private final @NotNull UvRegelManager manRegeln;

	/** Der Planungsabschnitt auf den sich diese Berechnung bezieht. */
	private final @NotNull UvPlanungsabschnitt planungsabschnitt;

	/** Die Menge aller Lehrer (dynamisch). */
	private final @NotNull UvAlgorithmusDynDatenLehrkraft @NotNull [] aLehrkraft;

	/** Die Menge aller Lerngruppen (dynamisch). */
	private final @NotNull UvAlgorithmusDynDatenLerngruppe @NotNull [] aLerngruppe;

	/** Die Menge aller Klassen (dynamisch). */
	private final @NotNull UvAlgorithmusDynDatenKlasse @NotNull [] aKlasse;

	/** Die Menge aller Jahrgänge (dynamisch). */
	private final @NotNull UvAlgorithmusDynDatenJahrgang @NotNull [] aJahrgaenge;

	/** Die Menge aller Fächer (dynamisch). */
	private final @NotNull UvAlgorithmusDynDatenFach @NotNull [] aFaecher;

	/** Mapping von der externen Lehrkraft-ID zum internen Objekt. */
	private final @NotNull Map<Long, UvAlgorithmusDynDatenLehrkraft> mapIDzuDynLehrkraft;

	/** Mapping von der externen Lerngruppen-ID zum internen Objekt. */
	private final @NotNull Map<Long, UvAlgorithmusDynDatenLerngruppe> mapIDzuDynLerngruppe;

	/** Mapping von der externen Klassen-ID zum internen Objekt. */
	private final @NotNull Map<Long, UvAlgorithmusDynDatenKlasse> mapIDzuDynKlasse;

	/** Mapping von der externen Jahrgang-ID zum internen Objekt. */
	private final @NotNull Map<Long, UvAlgorithmusDynDatenJahrgang> mapIDzuDynJahrgang;

	/** Mapping von der externen Fach-ID zum internen Objekt. */
	private final @NotNull Map<Long, UvAlgorithmusDynDatenFach> mapIDzuDynFach;

	/** Die Menge aller Undo-Objekte um Manipulationen schnell rückgängig zu machen. */
	private final @NotNull List<UvAlgorithmusDynDatenUndo> undos;

	/** Bewertung des Malus. */
	private final @NotNull int @NotNull [] malus;

	/** Zur Speicherung der Bewertung des Malus. */
	private final @NotNull int @NotNull [] malusSave;


	/** Die Menge aller Regel-Objekte die sich auf (Lerngruppe, Lehrkraft) beziehen. */
	private final @NotNull List<List<List<UvAlgorithmusDynDatenRegel>>> mapLerngruppeLehrkraftZuRegeln;

	/** Die Menge aller Regel-Objekte die sich auf (Klasse, Lehrkraft) beziehen. */
	private final @NotNull List<List<List<UvAlgorithmusDynDatenRegel>>> mapKlasseLehrkraftZuRegeln;

	/** Die Menge aller Regel-Objekte die sich auf (Fach, Lehrkraft) beziehen. */
	private final @NotNull List<List<List<UvAlgorithmusDynDatenRegel>>> mapFachLehrkraftZuRegeln;

	/**
	 * Der Konstruktor.
	 *
	 * @param log                 Ein Logger für Debug-Zwecke.
	 * @param rnd                 Ein Random-Objekt.
	 * @param man                 Ein {@link UvManager}-Objekt, der alle Daten hat.
	 * @param manRegeln           Ein {@link UvRegelManager}-Objekt, der alle Regeln hat.
	 * @param planungsabschnitt   Der {@link UvPlanungsabschnitt} auf den sich die Manager-Daten beziehen.
	 */
	public UvAlgorithmusDynDaten(
			final @NotNull Logger log,
			final @NotNull Random rnd,
			final @NotNull UvManager man,
			final @NotNull UvRegelManager manRegeln,
			final @NotNull UvPlanungsabschnitt planungsabschnitt) {

		this.log = log;
		this.rnd = rnd;
		this.man = man;
		this.planungsabschnitt = planungsabschnitt;
		this.manRegeln = manRegeln;
		this.undos = new ArrayList<>();
		this.mapIDzuDynLehrkraft = new HashMap<>();
		this.mapIDzuDynKlasse = new HashMap<>();
		this.mapIDzuDynLerngruppe = new HashMap<>();
		this.mapIDzuDynJahrgang = new HashMap<>();
		this.mapIDzuDynFach = new HashMap<>();

		// Malus auf 0 setzen.
		this.malus = new int[UvBlockungRegelPrioritaet.values().length];
		this.malusSave = new int[malus.length];
		for (int i = 0; i < malus.length; i++) {
			malus[i] = 0;
			malusSave[i] = 0;
		}

		// Fächer initialisieren.
		this.aFaecher = createFaecher();

		// Jahrgänge initialisieren.
		this.aJahrgaenge = createJahrgaenge();

		// Lehrkräfte initialisieren.
		this.aLehrkraft = createLehrkraefte();

		// Klassen initialisieren.
		this.aKlasse = createKlassen();

		// Lerngruppen initialisieren.
		this.aLerngruppe = createLerngruppen();

		// Dyn-Lerngruppen vs. Dyn-Lehrkräfte
		this.initLerngruppenLehrkraftPotentielle();

		// Alle Regeln mit Bezug zu (Lerngruppe, Lehrkraft).
		this.mapLerngruppeLehrkraftZuRegeln = new ArrayList<>();
		for (int i = 0; i < aLerngruppe.length; i++) {
			this.mapLerngruppeLehrkraftZuRegeln.add(new ArrayList<>());
			for (int j = 0; j < aLehrkraft.length; j++) {
				this.mapLerngruppeLehrkraftZuRegeln.get(i).add(new ArrayList<>());
			}
		}

		// Alle Regeln mit Bezug zu (Lerngruppe, Lehrkraft).
		this.mapKlasseLehrkraftZuRegeln = new ArrayList<>();
		for (int i = 0; i < aKlasse.length; i++) {
			this.mapKlasseLehrkraftZuRegeln.add(new ArrayList<>());
			for (int j = 0; j < aLehrkraft.length; j++) {
				this.mapKlasseLehrkraftZuRegeln.get(i).add(new ArrayList<>());
			}
		}

		// Alle Regeln mit Bezug zu (Fach, Lehrkraft).
		this.mapFachLehrkraftZuRegeln = new ArrayList<>();
		for (int i = 0; i < aFaecher.length; i++) {
			this.mapFachLehrkraftZuRegeln.add(new ArrayList<>());
			for (int j = 0; j < aLehrkraft.length; j++) {
				this.mapFachLehrkraftZuRegeln.get(i).add(new ArrayList<>());
			}
		}

		// Standardregel: Jede Lerngruppe benötigt 1 Lehrkraft.
		for (int i = 0; i < aLerngruppe.length; i++) {
			final int sollLehrkraftAnzahl = 1;
			final int prioritaet = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;
			final @NotNull UvAlgorithmusDynDatenRegel r28 = new UvAlgorithmusDynDatenRegel28(malus, aLerngruppe[i], sollLehrkraftAnzahl, prioritaet);
			aLerngruppe[i].regeln.add(r28);
		}

		// Standardregel: Jede Lehrkraft hat eine Malus für die Soll-Ist-Abweichung.
		for (int i = 0; i < aLehrkraft.length; i++) {
			final boolean aktiviert = true;
			final int prioritaet = UvBlockungRegelPrioritaet.MITTEL.nr;
			final @NotNull UvAlgorithmusDynDatenRegel r45 = new UvAlgorithmusDynDatenRegel45(malus, aLehrkraft[i], aktiviert, prioritaet);
			aLehrkraft[i].regeln.add(r45);
		}

		// Standardregel: Jede Klasse einen Malus für die Klassenlehreranzahl und stellv. Klassenlehreranzahl.
		for (int i = 0; i < aKlasse.length; i++) {
			final int sollLeitung1 = 1;
			final int prioritaet1 = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;
			final @NotNull UvAlgorithmusDynDatenRegel r02 = new UvAlgorithmusDynDatenRegel02(malus, aKlasse[i], sollLeitung1, prioritaet1);
			aKlasse[i].regeln.add(r02);
			final int sollLeitung2 = 1;
			final int prioritaet2 = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;
			final @NotNull UvAlgorithmusDynDatenRegel r04 = new UvAlgorithmusDynDatenRegel04(malus, aKlasse[i], sollLeitung2, prioritaet2);
			aKlasse[i].regeln.add(r04);
		}

		// Initialisierung aller Regeln.
		this.initRegeln();

		// Ausgabe einiger Log-Informationen.
		if (LOG_ADDITIONAL) {
			this.logging();
		}
	}


	private @NotNull UvAlgorithmusDynDatenFach @NotNull [] createFaecher() {
		// Hole die UV-Fächer.
		final @NotNull List<UvFach> uvFachMenge = new ArrayList<>(man.fachGetMengeAsList());


		// Konvertiere UV-Fach zu DYN-Fach.
		final @NotNull UvAlgorithmusDynDatenFach @NotNull [] temp = new UvAlgorithmusDynDatenFach[uvFachMenge.size()];

		for (int i = 0; i < temp.length; i++) {
			final @NotNull UvFach uvFach = uvFachMenge.get(i);
			temp[i] = new UvAlgorithmusDynDatenFach(i, uvFach);

			if (mapIDzuDynFach.put(uvFach.id, temp[i]) != null) {
				throw new DeveloperNotificationException("Bei der Initialisierung der Fächer gab es die ID=%d doppelt!".formatted(uvFach.id));
			}
		}

		return temp;
	}


	private @NotNull UvAlgorithmusDynDatenJahrgang @NotNull [] createJahrgaenge() {
		// Hole die UV-Jahrgänge.
		final @NotNull List<JahrgangsDaten> uvJahrgangMenge = new ArrayList<>(man.jahrgangsdatenGetMenge());


		// Konvertiere UV-Jahrgang zu DYN-Jahrgang.
		final @NotNull UvAlgorithmusDynDatenJahrgang @NotNull [] temp = new UvAlgorithmusDynDatenJahrgang[uvJahrgangMenge.size()];

		for (int i = 0; i < temp.length; i++) {
			final @NotNull JahrgangsDaten uvJahrgang = uvJahrgangMenge.get(i);
			temp[i] = new UvAlgorithmusDynDatenJahrgang(i, uvJahrgang);

			if (mapIDzuDynJahrgang.put(uvJahrgang.id, temp[i]) != null) {
				throw new DeveloperNotificationException("Bei der Initialisierung der Jahrgänge gab es die ID=%d doppelt!".formatted(uvJahrgang.id));
			}
		}

		return temp;
	}


	private @NotNull UvAlgorithmusDynDatenLehrkraft @NotNull [] createLehrkraefte() {
		// Hole die UV-Lehrer.
		final @NotNull List<UvLehrer> uvLehrerMenge = man.lehrerGetMengeByPlanungsabschnitt(planungsabschnitt);

		// Konvertiere UV-Lehrer zu DYN-Lehrer.
		final @NotNull UvAlgorithmusDynDatenLehrkraft @NotNull [] temp = new UvAlgorithmusDynDatenLehrkraft[uvLehrerMenge.size()];
		for (int i = 0; i < temp.length; i++) {
			final @NotNull UvLehrer uvLehrer = uvLehrerMenge.get(i);

			temp[i] = new UvAlgorithmusDynDatenLehrkraft(i, log, man, planungsabschnitt, uvLehrer);

			if (mapIDzuDynLehrkraft.put(uvLehrer.id, temp[i]) != null) {
				throw new DeveloperNotificationException("Bei der Initialisierung der Lehrkräfte gab es die ID=%d doppelt!".formatted(uvLehrer.id));
			}
		}


		return temp;
	}


	private @NotNull UvAlgorithmusDynDatenKlasse @NotNull [] createKlassen() {

		final @NotNull List<UvKlasse> uvKlassenMenge = man.klasseGetMengeByPlanungsabschnitt(planungsabschnitt);
		final @NotNull UvAlgorithmusDynDatenKlasse @NotNull [] temp = new UvAlgorithmusDynDatenKlasse[uvKlassenMenge.size()];

		for (int i = 0; i < temp.length; i++) {
			final @NotNull UvKlasse uvKlasse = uvKlassenMenge.get(i);
			temp[i] = new UvAlgorithmusDynDatenKlasse(log, rnd, i, uvKlasse, aLehrkraft);

			if (mapIDzuDynKlasse.put(uvKlasse.id, temp[i]) != null) {
				throw new DeveloperNotificationException("Bei der Initialisierung der Klassen gab es die ID=%d doppelt!".formatted(uvKlasse.id));
			}
		}

		return temp;
	}


	private @NotNull UvAlgorithmusDynDatenLerngruppe @NotNull [] createLerngruppen() {
		// Hole die UV-Lerngrupen.
		final @NotNull List<UvLerngruppe> uvLerngruppenMenge = man.lerngruppeGetMengeByPlanungsabschnitt(planungsabschnitt);

		// Konvertiere UV-Lerngruppen zu DYN-Lerngruppen.
		final @NotNull UvAlgorithmusDynDatenLerngruppe @NotNull [] temp = new UvAlgorithmusDynDatenLerngruppe[uvLerngruppenMenge.size()];
		for (int i = 0; i < temp.length; i++) {
			// Hole und konvertiere die Klassen der Lerngruppe.
			final @NotNull UvLerngruppe uvLerngruppe = uvLerngruppenMenge.get(i);
			final @NotNull List<UvAlgorithmusDynDatenKlasse> klassenmenge = createKlassenOfLerngruppe(uvLerngruppe);
			final @NotNull List<UvAlgorithmusDynDatenJahrgang> jahrgangmenge = createJahrgaengeOfLerngruppe(uvLerngruppe);

			// Erzeuge die DYN-Lerngruppe.
			temp[i] = new UvAlgorithmusDynDatenLerngruppe(i, this, man, planungsabschnitt, klassenmenge, jahrgangmenge);

			// Fehler?
			if (mapIDzuDynLerngruppe.put(uvLerngruppe.id, temp[i]) != null) {
				throw new DeveloperNotificationException("Bei der Initialisierung der Lerngruppen gab es die ID=%d doppelt!".formatted(uvLerngruppe.id));
			}
		}

		return temp;
	}


	private @NotNull List<UvAlgorithmusDynDatenJahrgang> createJahrgaengeOfLerngruppe(final @NotNull UvLerngruppe uvLerngruppe) {
		final @NotNull List<JahrgangsDaten> list = man.jahrgangsdatenGetMengeByLerngruppe(uvLerngruppe);

		final @NotNull List<UvAlgorithmusDynDatenJahrgang> jahrgangmenge = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			final @NotNull JahrgangsDaten uvJahrgang = list.get(i);

			final UvAlgorithmusDynDatenJahrgang dynJahrgang = mapIDzuDynJahrgang.get(uvJahrgang.id);
			if (dynJahrgang == null) {
				throw new DeveloperNotificationException("Das Mapping des Jahrgangs UV-ID=%d zu DYN würde nicht gefunden!".formatted(uvJahrgang.id));
			}

			jahrgangmenge.add(dynJahrgang);
		}

		return jahrgangmenge;
	}


	private @NotNull List<UvAlgorithmusDynDatenKlasse> createKlassenOfLerngruppe(final @NotNull UvLerngruppe uvLerngruppe) {

		final @NotNull List<UvKlasse> list = man.klasseGetMengeByLerngruppe(uvLerngruppe);

		final @NotNull List<UvAlgorithmusDynDatenKlasse> klassenmenge = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			final @NotNull UvKlasse uvKlasse = list.get(i);

			final UvAlgorithmusDynDatenKlasse dynKlasse = mapIDzuDynKlasse.get(uvKlasse.id);
			if (dynKlasse == null) {
				throw new DeveloperNotificationException("Das Mapping der Klasse UV-ID=%d zu DYN würde nicht gefunden!".formatted(uvKlasse.id));
			}

			klassenmenge.add(dynKlasse);
		}

		return klassenmenge;
	}


	/**
	 * Liefert das {@link UvAlgorithmusDynDatenFach} der Lerngruppe.
	 *
	 * @param uvLerngruppe   Die angefragte Lerngruppe.
	 *
	 * @return das {@link UvAlgorithmusDynDatenFach} der Lerngruppe.
	 */
	public @NotNull UvAlgorithmusDynDatenFach gibDynFachByLerngruppe(@NotNull final UvLerngruppe uvLerngruppe) {

		final UvAlgorithmusDynDatenFach dynFach = mapIDzuDynFach.get(uvLerngruppe.idFach);
		if (dynFach == null) {
			throw new DeveloperNotificationException("Das Mapping des Faches UV-ID=%d zu DYN würde nicht gefunden!".formatted(uvLerngruppe.id));
		}

		return dynFach;
	}


	/**
	 * Liefert den {@link Logger}.
	 *
	 * @return den {@link Logger}.
	 */
	public @NotNull Logger gibLogger() {
		return log;
	}



	/**
	 * Liefert das {@link Random}-Objekt.
	 *
	 * @return das {@link Random}-Objekt.
	 */
	public @NotNull Random gibRandom() {
		return rnd;
	}


	private UvAlgorithmusDynDatenLerngruppe gibLerngruppeZufaellig() {
		// TODO: Besser in Zukunft mit 50%-iger Chance eine Lerngruppe wählen, die noch keine Lehrkraft hat.
		if (aLerngruppe.length == 0) {
			return null;
		}
		return aLerngruppe[rnd.nextInt(aLerngruppe.length)];
	}


	private UvAlgorithmusDynDatenKlasse gibKlasseZufaellig() {
		// TODO: Besser in Zukunft mit 50%-iger Chance eine Klasse wählen, der noch eine Leitung1 fehlt.
		if (aKlasse.length == 0) {
			return null;
		}
		return aKlasse[rnd.nextInt(aKlasse.length)];
	}


	/**
	 * Liefert die aktuelle Lehrkraft-Lerngruppen-Zuordnung.
	 * <br>Hinweis: Die Liste kann potentiell auch leer sein.
	 *
	 * @return die aktuelle Lehrkraft-Lerngruppen-Zuordnung.
	 */
	public @NotNull List<UvLerngruppenLehrer> gibAktuelleZuordnung() {
		final @NotNull List<UvLerngruppenLehrer> list = new ArrayList<>();

		for (final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe : aLerngruppe) {
			final @NotNull List<UvAlgorithmusDynDatenLehrkraft> listLehrkraefte = lerngruppe.lehrkraefteZugeordnetAlle;
			for (int i = 0; i < listLehrkraefte.size(); i++) {
				final @NotNull UvLerngruppenLehrer le = new UvLerngruppenLehrer();
				le.id = -1;
				le.idLehrer = listLehrkraefte.get(i).uvID;
				le.idLerngruppe = lerngruppe.uvID;
				le.idPlanungsabschnitt = planungsabschnitt.id;
				le.reihenfolge = i + 1;
				le.wochenstunden = lerngruppe.wochenstundenVorgesehenGekuerzt;
				le.wochenstundenAngerechnet = lerngruppe.wochenstundenVorgesehenGekuerzt;

				list.add(le);
			}
		}

		return list;
	}


	/**
	 * Liefert die aktuellen Klasse-Lehrkraft-Zuordnungen (Klassenleitungen).
	 * <br>Hinweis: Die Liste kann potentiell auch leer sein.
	 *
	 * @return die aktuellen Klasse-Lehrkraft-Zuordnungen (Klassenleitungen).
	 */
	public @NotNull List<UvKlassenLehrer> gibAktuelleKlassenleitungen() {
		final @NotNull List<UvKlassenLehrer> list = new ArrayList<>();

		for (final @NotNull UvAlgorithmusDynDatenKlasse klasse : aKlasse) {
			int reihenfolge = 1; // Klassenlehrer beginnen mit Index 1.
			for (final @NotNull UvAlgorithmusDynDatenLehrkraft leitung1und2 : klasse.gibAktuelleMengeLeitung1und2()) {
				final @NotNull UvKlassenLehrer kl = new UvKlassenLehrer();
				kl.id = -1;
				kl.idPlanungsabschnitt = planungsabschnitt.id;
				kl.idKlasse = klasse.uvID;
				kl.idLehrer = leitung1und2.uvID;
				kl.reihenfolge = reihenfolge++;
				list.add(kl);
			}
		}

		return list;
	}


	/**
	 * Liefert den aktuellen Malus der bestimmten {@link UvBlockungRegelPrioritaet}.
	 *
	 * @param prioritaet   Das {@link UvBlockungRegelPrioritaet}-Objekt.
	 *
	 * @return den aktuellen Malus der bestimmten {@link UvBlockungRegelPrioritaet}.
	 */
	public int gibMalusDerPrioritaet(final @NotNull UvBlockungRegelPrioritaet prioritaet) {
		return malus[prioritaet.nr];
	}


	/**
	 * Liefert eine Kopie des aktuellen Malus-Arrays.
	 *
	 * @return eine Kopie des aktuellen Malus-Arrays.
	 */
	public @NotNull int @NotNull [] gibMalusKopie() {
		final @NotNull int @NotNull [] c = new int[malus.length];
		System.arraycopy(malus, 0, c, 0, c.length);
		return c;
	}


	/**
	 * Liefert eine Beschreibung des aktuellen Malus-Zustandes.
	 *
	 * @return eine Beschreibung des aktuellen Malus-Zustandes.
	 */
	public String gibMalusBeschreibung() {
		return Arrays.toString(malus);
	}


	/**
	 * Prüft, ob die übergebene Regel in der Menge fehlerhafter Regeln enthalten ist.
	 *
	 * @param regel   Die zu prüfende Regel.
	 *
	 * @return {@code true}, wenn die Regel als fehlerhaft erkannt wurde, sonst {@code false}
	 */
	public boolean gibIstRegelFehlerhaft(final @NotNull UvBlockungRegel regel) {
		return manRegeln.regelnGetMengeFehlerhaftAsList().contains(regel);
	}


	private @NotNull UvAlgorithmusDynDatenKlasse gibKlasseOrException(final long uvKlasseID) {
		final UvAlgorithmusDynDatenKlasse klasse = mapIDzuDynKlasse.get(uvKlasseID);
		if (klasse == null) {
			throw new DeveloperNotificationException("Ungültige Klasse-ID=%d!".formatted(uvKlasseID));
		}
		return klasse;
	}


	private @NotNull UvAlgorithmusDynDatenLehrkraft gibLehrkraftOrException(final long uvLehrkraftID) {
		final UvAlgorithmusDynDatenLehrkraft lehrer = mapIDzuDynLehrkraft.get(uvLehrkraftID);
		if (lehrer == null) {
			throw new DeveloperNotificationException("Ungültige Lehrkraft-ID=%d!".formatted(uvLehrkraftID));
		}
		return lehrer;
	}


	private @NotNull UvAlgorithmusDynDatenLerngruppe gibLerngruppeOrException(final long uvLerngruppeID) {
		final UvAlgorithmusDynDatenLerngruppe lehrer = mapIDzuDynLerngruppe.get(uvLerngruppeID);
		if (lehrer == null) {
			throw new DeveloperNotificationException("Ungültige Lerngruppe-ID=%d!".formatted(uvLerngruppeID));
		}
		return lehrer;
	}


	private @NotNull UvAlgorithmusDynDatenJahrgang gibJahrgangOrException(final Long uvJahrgangID) {
		final UvAlgorithmusDynDatenJahrgang jahrgang = mapIDzuDynJahrgang.get(uvJahrgangID);
		if (jahrgang == null) {
			throw new DeveloperNotificationException("Ungültige Jahrgang-ID=%d!".formatted(uvJahrgangID));
		}
		return jahrgang;
	}


	private @NotNull UvAlgorithmusDynDatenFach gibFachOrException(final Long uvFachID) {
		final UvAlgorithmusDynDatenFach fach = mapIDzuDynFach.get(uvFachID);
		if (fach == null) {
			throw new DeveloperNotificationException("Ungültige Fach-ID=%d!".formatted(uvFachID));
		}
		return fach;
	}


	private void initLerngruppenLehrkraftPotentielle() {
		final @NotNull List<UvLerngruppe> uvLerngruppen = man.lerngruppeGetMengeByPlanungsabschnitt(planungsabschnitt);
		final @NotNull List<UvLehrer> uvLehrer = man.lehrerGetMengeByPlanungsabschnitt(planungsabschnitt);

		for (int iLerngruppe = 0; iLerngruppe < aLerngruppe.length; iLerngruppe++) {
			final @NotNull UvLerngruppe lerngruppe = uvLerngruppen.get(iLerngruppe);

			for (int iLehrer = 0; iLehrer < aLehrkraft.length; iLehrer++) {
				final @NotNull UvLehrer lehrer = uvLehrer.get(iLehrer);

				if (man.lehrerHatLehrbefaehigungLerngruppe(lehrer, lerngruppe)) {
					aLerngruppe[iLerngruppe].fuegeLehrkraftHinzuf(aLehrkraft[iLehrer]);
				}
			}
		}
	}


	private void initRegeln() {
		// Alle Regel-Typen haben eine Sortierreihenfolge von 10.
		final @NotNull int @NotNull [] sortierungProRegeltyp = new int[UvBlockungRegelTyp.values().length];
		Arrays.fill(sortierungProRegeltyp, 10);

		// Default-Regeln zuerst.
		sortierungProRegeltyp[UvBlockungRegelTyp.KLASSE_DEFAULT_BENOETIGT_A_KLASSENLEHRER.nr] = 0;
		sortierungProRegeltyp[UvBlockungRegelTyp.KLASSE_DEFAULT_BENOETIGT_A_STELLV_KLASSENLEHRER.nr] = 0;
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_WENN_KLASSENLEHRER_DANN_MINDESTENS_A_STUNDEN_IN_KLASSE.nr] = 0;
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_WENN_STELLV_KLASSENLEHRER_DANN_MINDESTENS_A_STUNDEN_IN_KLASSE.nr] = 0;
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_IST_MAXIMAL_A_MAL_KLASSENLEHRER.nr] = 0;
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_IST_MAXIMAL_A_MAL_STELLV_KLASSENLEHRER.nr] = 0;
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_SOLL_IST_AKTIVIERUNG_A.nr] = 0;
		sortierungProRegeltyp[UvBlockungRegelTyp.LERNGRUPPEN_GEMEINSAM_KUERZEN_AUF_A_STUNDEN.nr] = 0;

		// Verboten-Regeln.
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_IN_LERNGRUPPE_B.nr] = 20;
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_IN_KLASSE_B.nr] = 20;
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_IN_JAHRGANG_B.nr] = 20;
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_A_DARF_FACH_B_NICHT_UNTERRICHTEN_IN_DEN_JAHRGAENGEN.nr] = 20;
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_A_DARF_FACH_B_NICHT_UNTERRICHTEN.nr] = 20;
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_ALS_KLASSENLEHRER_IN_KLASSE_B.nr] = 20;
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B.nr] = 20;

		// Fixierungs-Regeln.
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_A_FIXIERT_IN_LERNGRUPPE_B.nr] = 30;
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_A_IST_KLASSENLEHRER_IN_KLASSE_B.nr] = 30;
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_A_IST_STELLV_KLASSENLEHRER_IN_KLASSE_B.nr] = 30;

		// Der Transpiler kann keinen "inline" Comparator, deswegen muss er vorher in dieser Form definiert werden.
		final @NotNull Comparator<UvBlockungRegel> compRegeln = (final @NotNull UvBlockungRegel a, final @NotNull UvBlockungRegel b) -> {
			final int sa = sortierungProRegeltyp[a.typ];
			final int sb = sortierungProRegeltyp[b.typ];
			if (sa != sb) {
				return Integer.compare(sa, sb);
			}
			return Integer.compare(a.typ, b.typ);
		};

		// Sortiere die Regeln.
		final @NotNull List<UvBlockungRegel> regeln = manRegeln.regelnGetMengeAktiviertUndFehlerfreiAsList();
		regeln.sort(compRegeln);

		// Wende die Regeln an.
		for (final @NotNull UvBlockungRegel regel : regeln) {
			initRegel(regel);
		}
	}


	private void initRegel(final @NotNull UvBlockungRegel regel) {
		final @NotNull UvBlockungRegelTyp typ = UvBlockungRegelTyp.ofNr(regel.typ);
		final @NotNull List<Long> parameter = regel.parameter;
		final int prioritaet = regel.prioritaet;

		switch (typ) {
			case UNDEFINIERT -> {
				// überspringen.
			}

			case KLASSE_DEFAULT_BENOETIGT_A_KLASSENLEHRER -> initRegel01KlassenlehrerDefault(parameter, prioritaet);
			case KLASSE_A_BENOETIGT_B_KLASSENLEHRER -> initRegel02KlasseBenoetigtKlassenlehrer(parameter, prioritaet);
			case KLASSE_DEFAULT_BENOETIGT_A_STELLV_KLASSENLEHRER -> initRegel03KlassenlehrerDefault(parameter, prioritaet);
			case KLASSE_A_BENOETIGT_B_STELLV_KLASSENLEHRER -> initRegel04KlasseBenoetigtStellvKlassenlehrer(parameter, prioritaet);
			case LEHRKRAFT_DEFAULT_WENN_KLASSENLEHRER_DANN_MINDESTENS_A_STUNDEN_IN_KLASSE -> initRegel05WennLeitung1Dann(parameter, prioritaet);
			case LEHRKRAFT_A_WENN_KLASSENLEHRER_DANN_MINDESTENS_B_STUNDEN_IN_KLASSE -> initRegel06WennLehrkraftLeitung1Dann(parameter, prioritaet);
			case LEHRKRAFT_DEFAULT_WENN_STELLV_KLASSENLEHRER_DANN_MINDESTENS_A_STUNDEN_IN_KLASSE -> initRegel07WennLeitung2Dann(parameter, prioritaet);
			case LEHRKRAFT_A_WENN_STELLV_KLASSENLEHRER_DANN_MINDESTENS_B_STUNDEN_IN_KLASSE -> initRegel08WennLehrkraftLeitung2Dann(parameter, prioritaet);
			case LEHRKRAFT_DEFAULT_IST_MAXIMAL_A_MAL_KLASSENLEHRER -> initRegel09LehrerMaxLeitung1Default(parameter, prioritaet);
			case LEHRKRAFT_A_IST_MAXIMAL_B_MAL_KLASSENLEHRER -> initRegel10LehrerMaxLeitung1(parameter, prioritaet);
			case LEHRKRAFT_DEFAULT_IST_MAXIMAL_A_MAL_STELLV_KLASSENLEHRER -> initRegel11LehrerMaxLeitung2Default(parameter, prioritaet);
			case LEHRKRAFT_A_IST_MAXIMAL_B_MAL_STELLV_KLASSENLEHRER -> initRegel12LehrerMaxLeitung2(parameter, prioritaet);
			case LEHRKRAFT_A_VERBOTEN_IN_LERNGRUPPE_B -> initRegel13LehrkraftVerbotenInLerngruppe(parameter);
			case LEHRKRAFT_A_FIXIERT_IN_LERNGRUPPE_B -> initRegel14LehrkraftFixiertInLerngruppe(parameter);
			case LEHRKRAFT_A_GERNE_IN_LERNGRUPPE_B -> initRegel15LehrkraftGerneInLerngruppe(parameter, prioritaet);
			case LEHRKRAFT_A_UNGERNE_IN_LERNGRUPPE_B -> initRegel16LehrkraftUngernInLerngruppe(parameter, prioritaet);
			case LERNGRUPPE_A_LIEGT_AM_WOCHENTAG_B_STUNDE_C_WOCHENTYP_D -> initRegel17LerngruppeHatWochentagStundeWochentyp(parameter, prioritaet);
			case LEHRKRAFT_A_VERBOTEN_IN_KLASSE_B -> initRegel18LehrkraftVerbotenInKlasse(parameter);
			case LEHRKRAFT_A_VERBOTEN_IN_JAHRGANG_B -> initRegel19LehrkraftVerbotenInStufe(parameter);
			case LEHRKRAFT_A_HAT_MINDESTENS_B_MAL_FACH_C -> initRegel20LehrkraftHatMindestensFach(parameter, prioritaet);
			case LEHRKRAFT_A_HAT_MAXIMAL_B_MAL_FACH_C -> initRegel21LehrkraftHatMaximalFach(parameter, prioritaet);
			case LEHRKRAFT_A_HAT_MINDESTENS_B_KORREKTUREN -> initRegel22LehrkraftHatKorrekturenMin(parameter, prioritaet);
			case LEHRKRAFT_A_HAT_MAXIMAL_B_KORREKTUREN -> initRegel23LehrkraftHatKorrekturenMax(parameter, prioritaet);
			case LEHRKRAFT_A_DARF_FACH_B_NICHT_UNTERRICHTEN_IN_DEN_JAHRGAENGEN -> initRegel24LehrkraftDarfNichtFachUnterrichtenInDenJahrgaengen(parameter);
			case LEHRKRAFT_A_DARF_FACH_B_NICHT_UNTERRICHTEN -> initRegel25LehrkraftDarfNichtFachUnterrichten(parameter);
			case LEHRKRAFT_A_HAT_MAXIMAL_B_VERSCHIEDENE_JAHRGAENGE -> initRegel26LehrkraftHatMaximaleJahrgangsAnzahl(parameter, prioritaet);
			case LEHRKRAEFTE_A_UND_B_NICHT_IN_DER_SELBEN_KLASSE -> initRegel27LehrkraftNichtInSelberKlasse(parameter, prioritaet);
			case LERNGRUPPE_A_BENOETIGT_B_LEHRKRAEFTE -> initRegel28LerngruppeBenoetigtBLehrkraefte(parameter, prioritaet);
			case LERNGRUPPEN_ERHALTEN_SELBE_LEHRKRAFT -> initRegel29LerngruppenHabenSelbeLehrkraft(parameter, prioritaet);
			case LERNGRUPPEN_ERHALTEN_VERSCHIEDENE_LEHRKRAEFTE -> initRegel30LerngruppenHabenVerschiedeneLehrkraefte(parameter, prioritaet);
			case LERNGRUPPEN_GEMEINSAM_KUERZEN_AUF_A_STUNDEN -> initRegel31LerngruppenGemeinsamKuerzenAuf(parameter);
			case LEHRKRAFT_A_IST_KLASSENLEHRER_IN_KLASSE_B -> initRegel32LehrkraftFixiertAlsLeitung1(parameter);
			case LEHRKRAFT_A_IST_STELLV_KLASSENLEHRER_IN_KLASSE_B -> initRegel33LehrkraftFixiertAlsLeitung2(parameter);
			case LEHRKRAFT_A_VERBOTEN_ALS_KLASSENLEHRER_IN_KLASSE_B -> initRegel34LehrkraftVerbotenAlsLeitung1(parameter);
			case LEHRKRAFT_A_VERBOTEN_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B -> initRegel35LehrkraftVerbotenAlsLeitung2(parameter);
			case LEHRKRAFT_A_GERNE_ALS_KLASSENLEHRER_IN_KLASSE_B -> initRegel36LehrkraftGerneAlsLeitung1(parameter, prioritaet);
			case LEHRKRAFT_A_GERNE_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B -> initRegel37LehrkraftGerneAlsLeitung2(parameter, prioritaet);
			case LEHRKRAFT_A_UNGERNE_ALS_KLASSENLEHRER_IN_KLASSE_B -> initRegel38LehrkraftUngerneAlsLeitung1(parameter, prioritaet);
			case LEHRKRAFT_A_UNGERNE_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B -> initRegel39LehrkraftUngerneAlsLeitung2(parameter, prioritaet);
			case LEHRKRAFT_A_HAT_MAXIMAL_B_LERNGRUPPEN -> initRegel40LehrkraftMaxAnzahlLerngruppen(parameter, prioritaet);
			case LEHRKRAFT_A_HAT_MINDESTENS_B_LERNGRUPPEN -> initRegel41LehrkraftMinAnzahlLerngruppen(parameter, prioritaet);
			case LEHRKRAFT_A_MINDESTENS_B_MAL_IN_LERNGRUPPEN -> initRegel42LehrkraftMinOftInLerngruppen(parameter, prioritaet);
			case LEHRKRAFT_A_MAXIMAL_B_MAL_IN_LERNGRUPPEN -> initRegel43LehrkraftMaxOftInLerngruppen(parameter, prioritaet);
			case LEHRKRAFT_DEFAULT_SOLL_IST_AKTIVIERUNG_A -> initRegel44LehrkraftDefaultSollIstAbweichungAktivierung(parameter, prioritaet);
			case LEHRKRAFT_A_SOLL_IST_AKTIVIERUNG_B -> initRegel45LehrkraftSollIstAbweichungAktivierung(parameter, prioritaet);

			// ...
			default -> throw new IllegalStateException("Unbekannter Regeltyp: " + regel.typ);
		}
	}


	private void initRegel01KlassenlehrerDefault(final @NotNull List<Long> parameter, final int prioritaet) {
		final int neuLeitung1Anzahl = parameter.get(0).intValue();

		for (final @NotNull UvAlgorithmusDynDatenKlasse klasse : aKlasse) {
			// Die alte Regel muss gefunden und verändert werden.
			for (final @NotNull UvAlgorithmusDynDatenRegel r : klasse.regeln) {
				if (r instanceof final UvAlgorithmusDynDatenRegel02 r02) {
					r02.setzeLeitung1AnzahlUndPrioritaet(neuLeitung1Anzahl, prioritaet);
				}
			}
		}
	}


	private void initRegel02KlasseBenoetigtKlassenlehrer(final @NotNull List<Long> parameter, final int prioritaet) {
		final @NotNull UvAlgorithmusDynDatenKlasse klasse = gibKlasseOrException(parameter.get(0));
		final int neuLeitung1Anzahl = parameter.get(1).intValue();

		// Die alte Regel muss gefunden und verändert werden.
		for (final @NotNull UvAlgorithmusDynDatenRegel r : klasse.regeln) {
			if (r instanceof final UvAlgorithmusDynDatenRegel02 r02) {
				r02.setzeLeitung1AnzahlUndPrioritaet(neuLeitung1Anzahl, prioritaet);
			}
		}
	}


	private void initRegel03KlassenlehrerDefault(final @NotNull List<Long> parameter, final int prioritaet) {
		final int neuLeitung2Anzahl = parameter.get(0).intValue();

		for (final @NotNull UvAlgorithmusDynDatenKlasse klasse : aKlasse) {
			// Die alte Regel muss gefunden und verändert werden.
			for (final @NotNull UvAlgorithmusDynDatenRegel r : klasse.regeln) {
				if (r instanceof final UvAlgorithmusDynDatenRegel04 r04) {
					r04.setzeLeitung2AnzahlUndPrioritaet(neuLeitung2Anzahl, prioritaet);
				}
			}
		}
	}


	private void initRegel04KlasseBenoetigtStellvKlassenlehrer(final @NotNull List<Long> parameter, final int prioritaet) {
		final @NotNull UvAlgorithmusDynDatenKlasse klasse = gibKlasseOrException(parameter.get(0));
		final int neuLeitung2Anzahl = parameter.get(1).intValue();

		// Die alte Regel muss gefunden und verändert werden.
		for (final @NotNull UvAlgorithmusDynDatenRegel r : klasse.regeln) {
			if (r instanceof final UvAlgorithmusDynDatenRegel04 r04) {
				r04.setzeLeitung2AnzahlUndPrioritaet(neuLeitung2Anzahl, prioritaet);
			}
		}
	}


	private void initRegel05WennLeitung1Dann(final @NotNull List<Long> parameter, final int prioritaet) {
		final int minKlassenstundenBeiLeitung1 = parameter.get(0).intValue();

		for (final @NotNull UvAlgorithmusDynDatenKlasse klasse : aKlasse) {
			for (final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft : aLehrkraft) {
				final @NotNull UvAlgorithmusDynDatenRegel r06 =
						new UvAlgorithmusDynDatenRegel06(malus, klasse, lehrkraft, minKlassenstundenBeiLeitung1, prioritaet);
				mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r06);
			}
		}
	}


	private void initRegel06WennLehrkraftLeitung1Dann(@NotNull final List<Long> parameter, final int prioritaet) {
		final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrException(parameter.get(0));
		final int minKlassenstundenBeiLeitung1 = parameter.get(1).intValue();

		// Wenn es die Regel aufgrund eines DEFAULTS bereits gibt, lösche sie.
		for (final @NotNull UvAlgorithmusDynDatenKlasse klasse : aKlasse) {
			final @NotNull List<UvAlgorithmusDynDatenRegel> regeln = mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID);
			for (final @NotNull Iterator<UvAlgorithmusDynDatenRegel> iterator = regeln.iterator(); iterator.hasNext();) {
				final @NotNull UvAlgorithmusDynDatenRegel r = iterator.next();
				if (r instanceof UvAlgorithmusDynDatenRegel06) {
					iterator.remove();
				}
			}
		}

		// Füge die Regel nun hinzu.
		for (final @NotNull UvAlgorithmusDynDatenKlasse klasse : aKlasse) {
			final @NotNull UvAlgorithmusDynDatenRegel r06 =
					new UvAlgorithmusDynDatenRegel06(malus, klasse, lehrkraft, minKlassenstundenBeiLeitung1, prioritaet);
			mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r06);
		}
	}


	private void initRegel07WennLeitung2Dann(final @NotNull List<Long> parameter, final int prioritaet) {
		final int minKlassenstundenBeiLeitung2 = parameter.get(0).intValue();

		for (final @NotNull UvAlgorithmusDynDatenKlasse klasse : aKlasse) {
			for (final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft : aLehrkraft) {
				final @NotNull UvAlgorithmusDynDatenRegel r08 =
						new UvAlgorithmusDynDatenRegel08(malus, klasse, lehrkraft, minKlassenstundenBeiLeitung2, prioritaet);
				mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r08);
			}
		}
	}


	private void initRegel08WennLehrkraftLeitung2Dann(@NotNull final List<Long> parameter, final int prioritaet) {
		final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrException(parameter.get(0));
		final int minKlassenstundenBeiLeitung2 = parameter.get(1).intValue();

		// Wenn es die Regel aufgrund eines DEFAULTS bereits gibt, lösche sie.
		for (final @NotNull UvAlgorithmusDynDatenKlasse klasse : aKlasse) {
			final @NotNull List<UvAlgorithmusDynDatenRegel> regeln = mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID);
			for (final @NotNull Iterator<UvAlgorithmusDynDatenRegel> iterator = regeln.iterator(); iterator.hasNext();) {
				final @NotNull UvAlgorithmusDynDatenRegel r = iterator.next();
				if (r instanceof UvAlgorithmusDynDatenRegel08) {
					iterator.remove();
				}
			}
		}

		// Füge die Regel nun hinzu.
		for (final @NotNull UvAlgorithmusDynDatenKlasse klasse : aKlasse) {
			final @NotNull UvAlgorithmusDynDatenRegel r08 =
					new UvAlgorithmusDynDatenRegel08(malus, klasse, lehrkraft, minKlassenstundenBeiLeitung2, prioritaet);
			mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r08);
		}
	}


	private void initRegel09LehrerMaxLeitung1Default(final @NotNull List<Long> parameter, final int prioritaet) {
		final int maxLeitung1 = parameter.get(0).intValue();

		for (final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft : aLehrkraft) {
			final @NotNull UvAlgorithmusDynDatenRegel r10 = new UvAlgorithmusDynDatenRegel10(malus, lehrkraft, maxLeitung1, prioritaet);
			for (final @NotNull UvAlgorithmusDynDatenKlasse klasse : aKlasse) {
				mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r10);
			}
		}
	}


	private void initRegel10LehrerMaxLeitung1(final @NotNull List<Long> parameter, final int prioritaet) {
		final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrException(parameter.get(0));
		final int maxLeitung1 = parameter.get(1).intValue();

		// Wenn es die Regel aufgrund eines DEFAULTS bereits gibt, lösche sie.
		for (final @NotNull UvAlgorithmusDynDatenKlasse klasse : aKlasse) {
			final @NotNull List<UvAlgorithmusDynDatenRegel> regeln = mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID);
			for (final @NotNull Iterator<UvAlgorithmusDynDatenRegel> iterator = regeln.iterator(); iterator.hasNext();) {
				final @NotNull UvAlgorithmusDynDatenRegel r = iterator.next();
				if (r instanceof UvAlgorithmusDynDatenRegel10) {
					iterator.remove();
				}
			}
		}

		// Füge die Regel nun hinzu.
		final @NotNull UvAlgorithmusDynDatenRegel r10 = new UvAlgorithmusDynDatenRegel10(malus, lehrkraft, maxLeitung1, prioritaet);
		for (final @NotNull UvAlgorithmusDynDatenKlasse klasse : aKlasse) {
			mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r10);
		}
	}


	private void initRegel11LehrerMaxLeitung2Default(final @NotNull List<Long> parameter, final int prioritaet) {
		final int maxLeitung2 = parameter.get(0).intValue();

		for (final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft : aLehrkraft) {
			final @NotNull UvAlgorithmusDynDatenRegel r12 = new UvAlgorithmusDynDatenRegel12(malus, lehrkraft, maxLeitung2, prioritaet);
			for (final @NotNull UvAlgorithmusDynDatenKlasse klasse : aKlasse) {
				mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r12);
			}
		}
	}


	private void initRegel12LehrerMaxLeitung2(final @NotNull List<Long> parameter, final int prioritaet) {
		final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrException(parameter.get(0));
		final int maxLeitung2 = parameter.get(1).intValue();

		// Wenn es die Regel aufgrund eines DEFAULTS bereits gibt, lösche sie.
		for (final @NotNull UvAlgorithmusDynDatenKlasse klasse : aKlasse) {
			final @NotNull List<UvAlgorithmusDynDatenRegel> regeln = mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID);
			for (final @NotNull Iterator<UvAlgorithmusDynDatenRegel> iterator = regeln.iterator(); iterator.hasNext();) {
				final @NotNull UvAlgorithmusDynDatenRegel r = iterator.next();
				if (r instanceof UvAlgorithmusDynDatenRegel12) {
					iterator.remove();
				}
			}
		}

		// Füge die Regel nun hinzu.
		final @NotNull UvAlgorithmusDynDatenRegel r12 = new UvAlgorithmusDynDatenRegel12(malus, lehrkraft, maxLeitung2, prioritaet);
		for (final @NotNull UvAlgorithmusDynDatenKlasse klasse : aKlasse) {
			mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r12);
		}
	}


	private void initRegel13LehrkraftVerbotenInLerngruppe(final @NotNull List<Long> parameter) {
		final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrException(parameter.get(0));
		final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe = gibLerngruppeOrException(parameter.get(1));
		lerngruppe.entferneLehrkraft(lehrkraft);
	}


	private void initRegel14LehrkraftFixiertInLerngruppe(final @NotNull List<Long> parameter) {
		final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrException(parameter.get(0));
		final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe = gibLerngruppeOrException(parameter.get(1));
		stateLerngruppeLehrkraftAdd(lerngruppe, lehrkraft, true);
	}


	private void initRegel15LehrkraftGerneInLerngruppe(final @NotNull List<Long> parameter, final int prioritaet) {
		final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrException(parameter.get(0));
		final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe = gibLerngruppeOrException(parameter.get(1));

		final @NotNull UvAlgorithmusDynDatenRegel r15 = new UvAlgorithmusDynDatenRegel15(malus, lerngruppe, lehrkraft, prioritaet);
		mapLerngruppeLehrkraftZuRegeln.get(lerngruppe.interneID).get(lehrkraft.interneID).add(r15);
	}


	private void initRegel16LehrkraftUngernInLerngruppe(final @NotNull List<Long> parameter, final int prioritaet) {
		final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrException(parameter.get(0));
		final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe = gibLerngruppeOrException(parameter.get(1));

		final @NotNull UvAlgorithmusDynDatenRegel r16 = new UvAlgorithmusDynDatenRegel16(malus, lerngruppe, lehrkraft, prioritaet);
		mapLerngruppeLehrkraftZuRegeln.get(lerngruppe.interneID).get(lehrkraft.interneID).add(r16);
	}


	private void initRegel17LerngruppeHatWochentagStundeWochentyp(final @NotNull List<Long> parameter, final int prioritaet) {
		final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe = gibLerngruppeOrException(parameter.get(0));
		final int wochentag = parameter.get(1).intValue();
		final int stunde = parameter.get(2).intValue();
		final int wochentyp = parameter.get(3).intValue();

		final @NotNull UvAlgorithmusDynDatenRegel17 r17 = new UvAlgorithmusDynDatenRegel17(malus, lerngruppe, wochentag, stunde, wochentyp, prioritaet);
		lerngruppe.regeln.add(r17);
		lerngruppe.zeitslots.add(r17);
	}


	private void initRegel18LehrkraftVerbotenInKlasse(@NotNull final List<Long> parameter) {
		final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrException(parameter.get(0));
		final @NotNull UvAlgorithmusDynDatenKlasse klasse = gibKlasseOrException(parameter.get(1));

		for (final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe : aLerngruppe) {
			lerngruppe.entferneLehrkraftWennKlasseUebereinstimmt(lehrkraft, klasse);
		}
	}


	private void initRegel19LehrkraftVerbotenInStufe(final @NotNull List<Long> parameter) {
		final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrException(parameter.get(0));
		final @NotNull UvAlgorithmusDynDatenJahrgang jahrgang = gibJahrgangOrException(parameter.get(1));

		for (final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe : aLerngruppe) {
			lerngruppe.entferneLehrkraftWennStufeUebereinstimmt(lehrkraft, jahrgang);
		}
	}


	private void initRegel20LehrkraftHatMindestensFach(final @NotNull List<Long> parameter, final int prioritaet) {
		final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrException(parameter.get(0));
		final int minimum = parameter.get(1).intValue();
		final @NotNull UvAlgorithmusDynDatenFach fach = gibFachOrException(parameter.get(2));

		final @NotNull UvAlgorithmusDynDatenRegel r20 = new UvAlgorithmusDynDatenRegel20(malus, fach, lehrkraft, minimum, prioritaet);
		mapFachLehrkraftZuRegeln.get(fach.interneID).get(lehrkraft.interneID).add(r20);
	}


	private void initRegel21LehrkraftHatMaximalFach(final @NotNull List<Long> parameter, final int prioritaet) {
		final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrException(parameter.get(0));
		final int maximum = parameter.get(1).intValue();
		final @NotNull UvAlgorithmusDynDatenFach fach = gibFachOrException(parameter.get(2));

		final @NotNull UvAlgorithmusDynDatenRegel r21 = new UvAlgorithmusDynDatenRegel21(malus, fach, lehrkraft, maximum, prioritaet);
		mapFachLehrkraftZuRegeln.get(fach.interneID).get(lehrkraft.interneID).add(r21);
	}


	private void initRegel22LehrkraftHatKorrekturenMin(final @NotNull List<Long> parameter, final int prioritaet) {
		final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrException(parameter.get(0));
		final int minimum = parameter.get(1).intValue();

		final @NotNull UvAlgorithmusDynDatenRegel r22 = new UvAlgorithmusDynDatenRegel22(malus, lehrkraft, minimum, prioritaet);
		lehrkraft.regeln.add(r22);
	}


	private void initRegel23LehrkraftHatKorrekturenMax(final @NotNull List<Long> parameter, final int prioritaet) {
		final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrException(parameter.get(0));
		final int maximum = parameter.get(1).intValue();

		final @NotNull UvAlgorithmusDynDatenRegel r23 = new UvAlgorithmusDynDatenRegel23(malus, lehrkraft, maximum, prioritaet);
		lehrkraft.regeln.add(r23);
	}


	private void initRegel24LehrkraftDarfNichtFachUnterrichtenInDenJahrgaengen(final @NotNull List<Long> parameter) {
		final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrException(parameter.get(0));
		final @NotNull UvAlgorithmusDynDatenFach fach = gibFachOrException(parameter.get(1));

		final @NotNull List<UvAlgorithmusDynDatenJahrgang> jahrgaenge = new ArrayList<>();
		for (int i = 2; i < parameter.size(); i++) {
			final @NotNull UvAlgorithmusDynDatenJahrgang jahrgang = gibJahrgangOrException(parameter.get(i));
			jahrgaenge.add(jahrgang);
		}

		for (final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe : aLerngruppe) {
			lerngruppe.entferneLehrkraftWennJahrgangUndFachUebereinstimmt(lehrkraft, fach, jahrgaenge);
		}
	}


	private void initRegel25LehrkraftDarfNichtFachUnterrichten(final @NotNull List<Long> parameter) {
		final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrException(parameter.get(0));
		final @NotNull UvAlgorithmusDynDatenFach fach = gibFachOrException(parameter.get(1));

		for (final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe : aLerngruppe) {
			lerngruppe.entferneLehrkraftWennFachUebereinstimmt(lehrkraft, fach);
		}
	}


	private void initRegel26LehrkraftHatMaximaleJahrgangsAnzahl(final @NotNull List<Long> parameter, final int prioritaet) {
		final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrException(parameter.get(0));
		final int maximum = parameter.get(1).intValue();

		final @NotNull UvAlgorithmusDynDatenRegel r26 = new UvAlgorithmusDynDatenRegel26(malus, lehrkraft, maximum, prioritaet);
		lehrkraft.regeln.add(r26);
	}


	private void initRegel27LehrkraftNichtInSelberKlasse(final @NotNull List<Long> parameter, final int prioritaet) {
		final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft1 = gibLehrkraftOrException(parameter.get(0));
		final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft2 = gibLehrkraftOrException(parameter.get(1));

		for (final @NotNull UvAlgorithmusDynDatenKlasse klasse : aKlasse) {
			final @NotNull UvAlgorithmusDynDatenRegel r27 = new UvAlgorithmusDynDatenRegel27(malus, klasse, lehrkraft1, lehrkraft2, prioritaet);
			mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft1.interneID).add(r27);
			mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft2.interneID).add(r27);
		}
	}


	private void initRegel28LerngruppeBenoetigtBLehrkraefte(final @NotNull List<Long> parameter, final int prioritaet) {
		final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe = gibLerngruppeOrException(parameter.get(0));
		final int neuesSoll = parameter.get(1).intValue();

		// Die alte Regel muss gefunden und verändert werden.
		for (final @NotNull UvAlgorithmusDynDatenRegel r : lerngruppe.regeln) {
			if (r instanceof final UvAlgorithmusDynDatenRegel28 r28) {
				r28.setzeAuf(neuesSoll, prioritaet);
			}
		}
	}


	private void initRegel29LerngruppenHabenSelbeLehrkraft(final @NotNull List<Long> parameter, final int prioritaet) {
		// Menge aller Lerngruppen erzeugen.
		final @NotNull List<UvAlgorithmusDynDatenLerngruppe> lerngruppenMenge = new ArrayList<>();
		for (int i = 0; i < parameter.size(); i++) {
			final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe = gibLerngruppeOrException(parameter.get(i));
			lerngruppenMenge.add(lerngruppe);
		}

		// Diese Menge jeder Lerngruppe geben
		final @NotNull UvAlgorithmusDynDatenRegel r29 = new UvAlgorithmusDynDatenRegel29(malus, lerngruppenMenge, aLehrkraft.length, prioritaet);
		for (final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe : lerngruppenMenge) {
			lerngruppe.regeln.add(r29);
		}
	}


	private void initRegel30LerngruppenHabenVerschiedeneLehrkraefte(final @NotNull List<Long> parameter, final int prioritaet) {
		// Menge aller Lerngruppen erzeugen.
		final @NotNull List<UvAlgorithmusDynDatenLerngruppe> lerngruppenMenge = new ArrayList<>();
		for (int i = 0; i < parameter.size(); i++) {
			final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe = gibLerngruppeOrException(parameter.get(i));
			lerngruppenMenge.add(lerngruppe);
		}

		// Diese Menge jeder Lerngruppe geben
		final @NotNull UvAlgorithmusDynDatenRegel r30 = new UvAlgorithmusDynDatenRegel30(malus, lerngruppenMenge, aLehrkraft.length, prioritaet);
		for (final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe : lerngruppenMenge) {
			lerngruppe.regeln.add(r30);
		}
	}


	private void initRegel31LerngruppenGemeinsamKuerzenAuf(final @NotNull List<Long> parameter) {
		// Hinweis: Regel 31 bricht die Konvention und setzt das Attribut nicht dynamisch, sondern als PRE-DEFAULT.
		//          Falls der Stunden-Soll irgendwann mal dynamisch änderbar sein soll, dann muss es:
		//          1) bei der Lerngruppe verändert werden (ändert nie einen Malus)
		//          2) Alle Lehrkräfte der Lerngruppe müssen ihre Stundensummen anpassen (ändert den Malus!).
		final int neuerStundenSoll = parameter.get(0).intValue();

		for (int i = 1; i < parameter.size(); i++) {
			final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe = gibLerngruppeOrException(parameter.get(i));
			lerngruppe.setzeStundenSollAuf(neuerStundenSoll);
		}

	}


	private void initRegel32LehrkraftFixiertAlsLeitung1(final @NotNull List<Long> parameter) {
		final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrException(parameter.get(0));
		final @NotNull UvAlgorithmusDynDatenKlasse klasse = gibKlasseOrException(parameter.get(1));
		stateKlassenLeitung1Add(klasse, lehrkraft, true);
	}


	private void initRegel33LehrkraftFixiertAlsLeitung2(final @NotNull List<Long> parameter) {
		final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrException(parameter.get(0));
		final @NotNull UvAlgorithmusDynDatenKlasse klasse = gibKlasseOrException(parameter.get(1));
		stateKlassenLeitung2Add(klasse, lehrkraft, true);
	}


	private void initRegel34LehrkraftVerbotenAlsLeitung1(final @NotNull List<Long> parameter) {
		final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrException(parameter.get(0));
		final @NotNull UvAlgorithmusDynDatenKlasse klasse = gibKlasseOrException(parameter.get(1));
		klasse.entferneLeitung1(lehrkraft);
	}


	private void initRegel35LehrkraftVerbotenAlsLeitung2(final @NotNull List<Long> parameter) {
		final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrException(parameter.get(0));
		final @NotNull UvAlgorithmusDynDatenKlasse klasse = gibKlasseOrException(parameter.get(1));
		klasse.entferneLeitung2(lehrkraft);
	}


	private void initRegel36LehrkraftGerneAlsLeitung1(final @NotNull List<Long> parameter, final int prioritaet) {
		final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrException(parameter.get(0));
		final @NotNull UvAlgorithmusDynDatenKlasse klasse = gibKlasseOrException(parameter.get(1));

		final @NotNull UvAlgorithmusDynDatenRegel r36 = new UvAlgorithmusDynDatenRegel36(malus, klasse, lehrkraft, prioritaet);
		mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r36);
	}


	private void initRegel37LehrkraftGerneAlsLeitung2(final @NotNull List<Long> parameter, final int prioritaet) {
		final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrException(parameter.get(0));
		final @NotNull UvAlgorithmusDynDatenKlasse klasse = gibKlasseOrException(parameter.get(1));

		final @NotNull UvAlgorithmusDynDatenRegel r37 = new UvAlgorithmusDynDatenRegel37(malus, klasse, lehrkraft, prioritaet);
		mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r37);
	}


	private void initRegel38LehrkraftUngerneAlsLeitung1(final @NotNull List<Long> parameter, final int prioritaet) {
		final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrException(parameter.get(0));
		final @NotNull UvAlgorithmusDynDatenKlasse klasse = gibKlasseOrException(parameter.get(1));

		final @NotNull UvAlgorithmusDynDatenRegel r38 = new UvAlgorithmusDynDatenRegel38(malus, klasse, lehrkraft, prioritaet);
		mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r38);
	}


	private void initRegel39LehrkraftUngerneAlsLeitung2(final @NotNull List<Long> parameter, final int prioritaet) {
		final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrException(parameter.get(0));
		final @NotNull UvAlgorithmusDynDatenKlasse klasse = gibKlasseOrException(parameter.get(1));

		final @NotNull UvAlgorithmusDynDatenRegel r39 = new UvAlgorithmusDynDatenRegel39(malus, klasse, lehrkraft, prioritaet);
		mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r39);
	}


	private void initRegel40LehrkraftMaxAnzahlLerngruppen(final @NotNull List<Long> parameter, final int prioritaet) {
		final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrException(parameter.get(0));
		final int max = parameter.get(1).intValue();

		final @NotNull UvAlgorithmusDynDatenRegel r40 = new UvAlgorithmusDynDatenRegel40(malus, lehrkraft, max, prioritaet);
		lehrkraft.regeln.add(r40);
	}


	private void initRegel41LehrkraftMinAnzahlLerngruppen(final @NotNull List<Long> parameter, final int prioritaet) {
		final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrException(parameter.get(0));
		final int min = parameter.get(1).intValue();

		final @NotNull UvAlgorithmusDynDatenRegel r41 = new UvAlgorithmusDynDatenRegel41(malus, lehrkraft, min, prioritaet);
		lehrkraft.regeln.add(r41);
	}


	private void initRegel42LehrkraftMinOftInLerngruppen(final @NotNull List<Long> parameter, final int prioritaet) {
		final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrException(parameter.get(0));
		final int min = parameter.get(1).intValue();

		// Menge aller Lerngruppen erzeugen.
		final @NotNull List<UvAlgorithmusDynDatenLerngruppe> lerngruppenMenge = new ArrayList<>();
		for (int i = 2; i < parameter.size(); i++) {
			final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe = gibLerngruppeOrException(parameter.get(i));
			lerngruppenMenge.add(lerngruppe);
		}

		// Alle Lerngruppen werden erhalten die Regel, aber sie darf nur einmal erzeugt werden.
		final @NotNull UvAlgorithmusDynDatenRegel r42 = new UvAlgorithmusDynDatenRegel42(malus, lehrkraft, min, lerngruppenMenge, prioritaet);
		for (final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe : lerngruppenMenge) {
			mapLerngruppeLehrkraftZuRegeln.get(lerngruppe.interneID).get(lehrkraft.interneID).add(r42);
		}

	}


	private void initRegel43LehrkraftMaxOftInLerngruppen(final @NotNull List<Long> parameter, final int prioritaet) {
		final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrException(parameter.get(0));
		final int max = parameter.get(1).intValue();

		// Menge aller Lerngruppen erzeugen.
		final @NotNull List<UvAlgorithmusDynDatenLerngruppe> lerngruppenMenge = new ArrayList<>();
		for (int i = 2; i < parameter.size(); i++) {
			final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe = gibLerngruppeOrException(parameter.get(i));
			lerngruppenMenge.add(lerngruppe);
		}

		// Alle Lerngruppen werden erhalten die Regel, aber sie darf nur einmal erzeugt werden.
		final @NotNull UvAlgorithmusDynDatenRegel r43 = new UvAlgorithmusDynDatenRegel43(malus, lehrkraft, max, lerngruppenMenge, prioritaet);
		for (final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe : lerngruppenMenge) {
			mapLerngruppeLehrkraftZuRegeln.get(lerngruppe.interneID).get(lehrkraft.interneID).add(r43);
		}
	}


	private void initRegel44LehrkraftDefaultSollIstAbweichungAktivierung(final @NotNull List<Long> parameter, final int prioritaet) {
		final boolean aktiviert = (parameter.get(0) == 1);

		for (final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft : aLehrkraft) {
			// Die alte Regel muss gefunden und verändert werden.
			for (final @NotNull UvAlgorithmusDynDatenRegel r : lehrkraft.regeln) {
				if (r instanceof final UvAlgorithmusDynDatenRegel45 r45) {
					r45.setzeAuf(aktiviert, prioritaet);
				}
			}
		}
	}


	private void initRegel45LehrkraftSollIstAbweichungAktivierung(final @NotNull List<Long> parameter, final int prioritaet) {
		final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrException(parameter.get(0));
		final boolean aktiviert = (parameter.get(1) == 1);

		// Die alte Regel muss gefunden und verändert werden.
		for (final @NotNull UvAlgorithmusDynDatenRegel r : lehrkraft.regeln) {
			if (r instanceof final UvAlgorithmusDynDatenRegel45 r45) {
				r45.setzeAuf(aktiviert, prioritaet);
			}
		}
	}


	private void logging() {
		for (int i = 0; i < aLehrkraft.length; i++) {
			log.logLn(LogLevel.INFO, "Dyn-Lehrer: " + aLehrkraft[i].toString());
		}
		for (int i = 0; i < aLerngruppe.length; i++) {
			log.logLn(LogLevel.INFO, "Dyn-Lerngruppe: " + aLerngruppe[i].toString());
		}
		log.logLn(LogLevel.INFO, "Malus = " + gibMalusBeschreibung());
	}


	private void bewertungSpeichern() {
		System.arraycopy(malus, 0, malusSave, 0, malus.length);
	}


	private boolean bewertungIstSchlechterAlsGespeichert() {
		// Scanne nach Priorität (Niedriger Index = Höhere Priorität).
		for (int i = 0; i < malus.length; i++) {
			if (malus[i] != malusSave[i]) {
				return malus[i] > malusSave[i];
			}
		}
		// Gleich, also nicht schlechter.
		return false;
	}


	private void undoClear() {
		undos.clear();
	}


	/**
	 * Macht alle gespeicherten Undo-Schritte rückgängig.
	 */
	public void undoAll() {
		while (!undos.isEmpty()) {
			final @NotNull UvAlgorithmusDynDatenUndo undo = undos.removeLast();
			final int typ = undo.gibTyp();
			final @NotNull int @NotNull [] data = undo.gibData();

			switch (typ) {
				case 1 -> stateLerngruppeLehrkraftDel(aLerngruppe[data[0]], aLehrkraft[data[1]]);         // Undo von Lehrkraft ADD.
				case 2 -> stateLerngruppeLehrkraftAdd(aLerngruppe[data[0]], aLehrkraft[data[1]], false);  // Undo von Lehrkraft DEL.
				case 3 -> stateKlassenLeitung1Del(aKlasse[data[0]], aLehrkraft[data[1]]);                 // Undo von Klassenleitung 1 ADD.
				case 4 -> stateKlassenLeitung1Add(aKlasse[data[0]], aLehrkraft[data[1]], false);          // Undo von Klassenleitung 1 DEL.
				case 5 -> stateKlassenLeitung2Del(aKlasse[data[0]], aLehrkraft[data[1]]);                 // Undo von Klassenleitung 2 ADD.
				case 6 -> stateKlassenLeitung2Add(aKlasse[data[0]], aLehrkraft[data[1]], false);          // Undo von Klassenleitung 2 DEL.
				// Undo ist unbekannt!
				default ->
					throw new IllegalArgumentException("Undo-Typ unbekannt: " + typ);
			}

		}
	}


	// ################################################################################
	// Strategien manipulieren den Zustand ohne die Bewertung zu verschlechtern.
	// ################################################################################


	/**
	 * Eine zufällige Lerngruppe fügt eine zufällige Lehrkraft hinzu.
	 * Falls die Bewertung sich verschlechtert, wird die Veränderung rückgängig gemacht.
	 */
	public void strategieLerngruppeLehrkraftHinzufuegen() {
		bewertungSpeichern();
		undoClear();

		manipulationLerngruppeLehrkraftAdd();

		if (bewertungIstSchlechterAlsGespeichert()) {
			undoAll();
		}
	}


	/**
	 * Eine zufällige Lerngruppe entfernt eine zufällige zugeordnete Lehrkraft (falls vorhanden).
	 * Falls die Bewertung sich verschlechtert, wird die Veränderung rückgängig gemacht.
	 */
	public void strategieLerngruppeLehrkraftEntfernen() {
		bewertungSpeichern();
		undoClear();

		manipulationLerngruppeLehrkraftDel();

		if (bewertungIstSchlechterAlsGespeichert()) {
			undoAll();
		}
	}


	/**
	 * Eine zufällige Klasse erhält eine zufällige Klassenleitung (Leitung 1).
	 * Falls die Bewertung sich verschlechtert, wird die Veränderung rückgängig gemacht.
	 */
	public void strategieKlassenleitung1Hinzufuegen() {
		bewertungSpeichern();
		undoClear();

		manipulationKlassenleitung1Add();

		if (bewertungIstSchlechterAlsGespeichert()) {
			undoAll();
		}
	}


	/**
	 * Eine zufällige Klasse entfernt eine zufällige aktuell zugeordnete Klassenleitung (Leitung 1).
	 * Falls die Bewertung sich verschlechtert, wird die Veränderung rückgängig gemacht.
	 */
	public void strategieKlassenleitung1Entfernen() {
		bewertungSpeichern();
		undoClear();

		manipulationKlassenleitung1Del();

		if (bewertungIstSchlechterAlsGespeichert()) {
			undoAll();
		}
	}


	/**
	 * Eine zufällige Klasse erhält eine zufällige stellv. Klassenleitung (Leitung 2).
	 * Falls die Bewertung sich verschlechtert, wird die Veränderung rückgängig gemacht.
	 */
	public void strategieKlassenleitung2Hinzufuegen() {
		bewertungSpeichern();
		undoClear();

		manipulationKlassenleitung2Add();

		if (bewertungIstSchlechterAlsGespeichert()) {
			undoAll();
		}
	}


	/**
	 * Eine zufällige Klasse entfernt eine zufällige aktuell zugeordnete stellv. Klassenleitung (Leitung 2).
	 * Falls die Bewertung sich verschlechtert, wird die Veränderung rückgängig gemacht.
	 */
	public void strategieKlassenleitung2Entfernen() {
		bewertungSpeichern();
		undoClear();

		manipulationKlassenleitung2Del();

		if (bewertungIstSchlechterAlsGespeichert()) {
			undoAll();
		}
	}


	// ################################################################################
	// Manipulationen verändern den Zustand. Der Zustand kann sich dabei verschlechtern.
	// ################################################################################


	/**
	 * Eine zufällige Lerngruppe fügt eine zufällige potentielle Lehrkraft hinzu.
	 * Im Gegensatz zur Strategie wird keine Bewertung geprüft – der Zustand kann sich verschlechtern.
	 */
	public void manipulationLerngruppeLehrkraftAdd() {
		final UvAlgorithmusDynDatenLerngruppe lerngruppe = gibLerngruppeZufaellig();
		if (lerngruppe == null) {
			return;
		}

		final UvAlgorithmusDynDatenLehrkraft lehrkraft = lerngruppe.gibLehrkraftPotentiellZufaelligOderNull();
		if (lehrkraft == null) {
			return;
		}

		stateLerngruppeLehrkraftAdd(lerngruppe, lehrkraft, false);
		undos.add(new UvAlgorithmusDynDatenUndo(1, lerngruppe.interneID, lehrkraft.interneID));
	}


	/**
	 * Eine zufällige Lerngruppe entfernt eine zufällige zugeordnete, nicht fixierte Lehrkraft (falls vorhanden).
	 * Im Gegensatz zur Strategie wird keine Bewertung geprüft – der Zustand kann sich verschlechtern.
	 */
	public void manipulationLerngruppeLehrkraftDel() {
		final UvAlgorithmusDynDatenLerngruppe lerngruppe = gibLerngruppeZufaellig();
		if (lerngruppe == null) {
			return;
		}

		final UvAlgorithmusDynDatenLehrkraft lehrkraft = lerngruppe.gibLehrkraftZugeordnetAberNichtFixiertZufaellig();
		if (lehrkraft == null) {
			return;
		}

		stateLerngruppeLehrkraftDel(lerngruppe, lehrkraft);
		undos.add(new UvAlgorithmusDynDatenUndo(2, lerngruppe.interneID, lehrkraft.interneID));
	}


	/**
	 * Eine zufällige Klasse erhält eine zufällige potentielle Klassenleitung (Leitung 1).
	 * Im Gegensatz zur Strategie wird keine Bewertung geprüft – der Zustand kann sich verschlechtern.
	 */
	public void manipulationKlassenleitung1Add() {
		final UvAlgorithmusDynDatenKlasse klasse = gibKlasseZufaellig();
		if (klasse == null) {
			return;
		}

		final UvAlgorithmusDynDatenLehrkraft lehrkraft = klasse.gibLeitung1PotentiellZufaelligOderNull();
		if (lehrkraft == null) {
			return;
		}

		stateKlassenLeitung1Add(klasse, lehrkraft, false);
		undos.add(new UvAlgorithmusDynDatenUndo(3, klasse.interneID, lehrkraft.interneID));
	}


	/**
	 * Eine zufällige Klasse entfernt eine zufällige aktuell zugeordnete, nicht fixierte Klassenleitung (Leitung 1).
	 * Im Gegensatz zur Strategie wird keine Bewertung geprüft – der Zustand kann sich verschlechtern.
	 */
	public void manipulationKlassenleitung1Del() {
		final UvAlgorithmusDynDatenKlasse klasse = gibKlasseZufaellig();
		if (klasse == null) {
			return;
		}

		final UvAlgorithmusDynDatenLehrkraft lehrkraft = klasse.gibLeitung1ZugeordnetAberNichtFixiertZufaellig();
		if (lehrkraft == null) {
			return;
		}

		stateKlassenLeitung1Del(klasse, lehrkraft);
		undos.add(new UvAlgorithmusDynDatenUndo(4, klasse.interneID, lehrkraft.interneID));
	}


	/**
	 * Eine zufällige Klasse erhält eine zufällige potentielle stellv. Klassenleitung (Leitung 2).
	 * Im Gegensatz zur Strategie wird keine Bewertung geprüft – der Zustand kann sich verschlechtern.
	 */
	public void manipulationKlassenleitung2Add() {
		final UvAlgorithmusDynDatenKlasse klasse = gibKlasseZufaellig();
		if (klasse == null) {
			return;
		}

		final UvAlgorithmusDynDatenLehrkraft lehrkraft = klasse.gibLeitung2PotentiellZufaelligOderNull();
		if (lehrkraft == null) {
			return;
		}

		stateKlassenLeitung2Add(klasse, lehrkraft, false);
		undos.add(new UvAlgorithmusDynDatenUndo(5, klasse.interneID, lehrkraft.interneID));
	}


	/**
	 * Eine zufällige Klasse entfernt eine zufällige aktuell zugeordnete, nicht fixierte stellv. Klassenleitung (Leitung 2).
	 * Im Gegensatz zur Strategie wird keine Bewertung geprüft – der Zustand kann sich verschlechtern.
	 */
	public void manipulationKlassenleitung2Del() {
		final UvAlgorithmusDynDatenKlasse klasse = gibKlasseZufaellig();
		if (klasse == null) {
			return;
		}

		final UvAlgorithmusDynDatenLehrkraft lehrkraft = klasse.gibLeitung2ZugeordnetAberNichtFixiertZufaellig();
		if (lehrkraft == null) {
			return;
		}

		stateKlassenLeitung2Del(klasse, lehrkraft);
		undos.add(new UvAlgorithmusDynDatenUndo(6, klasse.interneID, lehrkraft.interneID));
	}


	// ################################################################################
	// Setze-Methoden sind für die externe Manipulation gedacht (z.B. Unit-Tests),
	// sie triggern die jeweilige "state..." Methode.
	// ################################################################################


	/**
	 * Öffentliche Methode zum Verändern des Zustandes:
	 * <br> Fügt eine Lerngruppen-Lehrkraft-Zuordnung hinzu.
	 * <br> Aktualisiert die Bewertung.
	 *
	 * @param uvLerngruppeID   Die UV-ID der {@link UvAlgorithmusDynDatenLerngruppe}.
	 * @param uvLehrkraftID    Die UV-ID der {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param fixiert          Gibt an, ob die Lehrkraft zusätzlich fixiert werden soll.
	 */
	public void setzeLerngruppeLehrkraftAdd(final long uvLerngruppeID, final long uvLehrkraftID, final boolean fixiert) {
		final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe = gibLerngruppeOrException(uvLerngruppeID);
		final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrException(uvLehrkraftID);
		stateLerngruppeLehrkraftAdd(lerngruppe, lehrkraft, fixiert);
	}


	/**
	 * Öffentliche Methode zum Verändern des Zustandes:
	 * <br> Entfernt eine Lerngruppen-Lehrkraft-Zuordnung.
	 * <br> Aktualisiert die Bewertung.
	 *
	 * @param uvLerngruppeID   Die UV-ID der {@link UvAlgorithmusDynDatenLerngruppe}.
	 * @param uvLehrkraftID    Die UV-ID der {@link UvAlgorithmusDynDatenLehrkraft}.
	 */
	public void setzeLerngruppeLehrkraftDel(final long uvLerngruppeID, final long uvLehrkraftID) {
		final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe = gibLerngruppeOrException(uvLerngruppeID);
		final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrException(uvLehrkraftID);
		stateLerngruppeLehrkraftDel(lerngruppe, lehrkraft);
	}


	/**
	 * Öffentliche Methode zum Verändern des Zustandes:
	 * <br> Fügt einer Klasse einen Klassenlehrer (Leitung 1) hinzu.
	 * <br> Aktualisiert die Bewertung.
	 *
	 * @param uvKlasseID      Die UV-ID der {@link UvAlgorithmusDynDatenKlasse}.
	 * @param uvLehrkraftID   Die UV-ID der {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param fixiert         Gibt an, ob die Lehrkraft zusätzlich fixiert werden soll.
	 */
	public void setzeKlassenLeitung1Add(final long uvKlasseID, final long uvLehrkraftID, final boolean fixiert) {
		final @NotNull UvAlgorithmusDynDatenKlasse klasse = gibKlasseOrException(uvKlasseID);
		final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrException(uvLehrkraftID);
		stateKlassenLeitung1Add(klasse, lehrkraft, fixiert);
	}


	/**
	 * Öffentliche Methode zum Verändern des Zustandes:
	 * <br> Entfernt aus einer Klasse die aktuelle Klassenleitung (Leitung 1).
	 * <br> Aktualisiert die Bewertung.
	 *
	 * @param uvKlasseID      Die UV-ID der {@link UvAlgorithmusDynDatenKlasse}.
	 * @param uvLehrkraftID   Die UV-ID der {@link UvAlgorithmusDynDatenLehrkraft}.
	 */
	public void setzeKlassenLeitung1Del(final long uvKlasseID, final long uvLehrkraftID) {
		final @NotNull UvAlgorithmusDynDatenKlasse klasse = gibKlasseOrException(uvKlasseID);
		final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrException(uvLehrkraftID);
		stateKlassenLeitung1Del(klasse, lehrkraft);
	}


	/**
	 * Öffentliche Methode zum Verändern des Zustandes:
	 * <br> Fügt einer Klasse einen stellvertretenden Klassenlehrer (Leitung 2) hinzu.
	 * <br> Aktualisiert die Bewertung.
	 *
	 * @param uvKlasseID      Die UV-ID der {@link UvAlgorithmusDynDatenKlasse}.
	 * @param uvLehrkraftID   Die UV-ID der {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param fixiert         Gibt an, ob die Lehrkraft zusätzlich fixiert werden soll.
	 */
	public void setzeKlassenLeitung2Add(final long uvKlasseID, final long uvLehrkraftID, final boolean fixiert) {
		final @NotNull UvAlgorithmusDynDatenKlasse klasse = gibKlasseOrException(uvKlasseID);
		final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrException(uvLehrkraftID);
		stateKlassenLeitung2Add(klasse, lehrkraft, fixiert);
	}


	/**
	 * Öffentliche Methode zum Verändern des Zustandes:
	 * <br> Entfernt aus einer Klasse die aktuelle stellvertretende Klassenleitung (Leitung 2).
	 * <br> Aktualisiert die Bewertung.
	 *
	 * @param uvKlasseID      Die UV-ID der {@link UvAlgorithmusDynDatenKlasse}.
	 * @param uvLehrkraftID   Die UV-ID der {@link UvAlgorithmusDynDatenLehrkraft}.
	 */
	public void setzeKlassenLeitung2Del(final long uvKlasseID, final long uvLehrkraftID) {
		final @NotNull UvAlgorithmusDynDatenKlasse klasse = gibKlasseOrException(uvKlasseID);
		final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrException(uvLehrkraftID);
		stateKlassenLeitung2Del(klasse, lehrkraft);
	}


	// ################################################################################
	// State-Methoden sind die einzigen Methoden, die Zuordnungen verändern dürfen.
	// Alle Bewertungen werden dabei dynamisch aktualisiert.
	// ################################################################################


	/**
	 * Hauptmethode zum Verändern des Zustandes:
	 * <br> Fügt eine Lerngruppen-Lehrkraft-Zuordnung hinzu.
	 * <br> Aktualisiert die Bewertung.
	 *
	 * @param lerngruppe   Die {@link UvAlgorithmusDynDatenLerngruppe}.
	 * @param lehrkraft    Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param fixiert      Falls die Lehrkraft zusätzlich fixiert werden soll.
	 */
	private void stateLerngruppeLehrkraftAdd(
			final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe,
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft,
			final boolean fixiert) {

		// Konsistenz prüfen.
		if (!lerngruppe.gibIstLehrkraftPotentiell(lehrkraft)) {
			throw new DeveloperNotificationException("Lerngruppe %sd will Lehrkraft %sd hinzufügen, Lehrkraft ist aber keine potentielle!"
					.formatted(lerngruppe.toString(), lehrkraft.toString()));
		}

		// Malus subtrahieren.
		changeMalusLerngruppeLehrkraft(lerngruppe, lehrkraft, -1);

		// Zustand verändern.
		lerngruppe.stateLehrkraftZugeordnetAdd(lehrkraft, fixiert);
		lehrkraft.stateLerngruppeZugeordnetAdd(lerngruppe);

		// Malus addieren.
		changeMalusLerngruppeLehrkraft(lerngruppe, lehrkraft, 1);
	}


	/**
	 * Hauptmethode zum Verändern des Zustandes:
	 * <br> Entfernt eine Lerngruppen-Lehrkraft-Zuordnung.
	 * <br> Aktualisiert die Bewertung.
	 *
	 * @param lerngruppe   Die {@link UvAlgorithmusDynDatenLerngruppe}.
	 * @param lehrkraft    Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 */
	private void stateLerngruppeLehrkraftDel(
			final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe,
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft) {

		// Konsistenz prüfen.
		if (lerngruppe.gibIstLehrkraftFixiert(lehrkraft)) {
			throw new DeveloperNotificationException("Lerngruppe %s will Lehrkraft %s entfernen, Lehrkraft ist aber fixiert!"
					.formatted(lerngruppe.toString(), lehrkraft.toString()));
		}

		// Malus subtrahieren.
		changeMalusLerngruppeLehrkraft(lerngruppe, lehrkraft, -1);

		// Zustand verändern.
		lerngruppe.stateLehrkraftZugeordnetDel(lehrkraft);
		lehrkraft.stateLerngruppeZugeordnetDel(lerngruppe);

		// Malus addieren.
		changeMalusLerngruppeLehrkraft(lerngruppe, lehrkraft, 1);
	}


	/**
	 * Hauptmethode zum Verändern des Zustandes:
	 * <br> Fügt einer Klasse einen Klassenlehrer (Leitung 1) hinzu.
	 * <br> Aktualisiert die Bewertung.
	 *
	 * @param klasse      Die {@link UvAlgorithmusDynDatenKlasse}.
	 * @param lehrkraft   Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param fixiert     Falls die Lehrkraft zusätzlich fixiert werden soll.
	 */
	private void stateKlassenLeitung1Add(
			final @NotNull UvAlgorithmusDynDatenKlasse klasse,
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft,
			final boolean fixiert) {

		// Konsistenz prüfen.
		if (!klasse.gibIstLeitung1Potentiell(lehrkraft)) {
			throw new DeveloperNotificationException("Klasse %s will Lehrkraft %s als Leitung 1 hinzufügen, Lehrkraft ist aber keine potentielle!"
					.formatted(klasse.toString(), lehrkraft.toString()));
		}

		// Malus subtrahieren
		changeMalusKlasseLehrkraft(klasse, lehrkraft, -1);

		// Zustand verändern.
		klasse.stateLeitung1ZugeordnetAdd(lehrkraft, fixiert);
		lehrkraft.stateLeitung1Inc(klasse);

		// Malus addieren.
		changeMalusKlasseLehrkraft(klasse, lehrkraft, 1);
	}


	/**
	 * Hauptmethode zum Verändern des Zustandes:
	 * <br> Entfernt aus der Klasse die aktuelle Klassenleitung (Leitung 1).
	 * <br> Aktualisiert die Bewertung.
	 *
	 * @param klasse      Die {@link UvAlgorithmusDynDatenKlasse}.
	 * @param lehrkraft   Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 */
	private void stateKlassenLeitung1Del(
			final @NotNull UvAlgorithmusDynDatenKlasse klasse,
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft) {

		// Konsistenz prüfen.
		if (klasse.gibIstLeitung1Fixiert(lehrkraft)) {
			throw new DeveloperNotificationException("Klasse %s will Lehrkraft %s als Leitung 1 entfernen, Lehrkraft ist aber fixiert!"
					.formatted(klasse.toString(), lehrkraft.toString()));
		}

		// Malus subtrahieren
		changeMalusKlasseLehrkraft(klasse, lehrkraft, -1);

		// Zustand verändern.
		klasse.stateLeitung1ZugeordnetDel(lehrkraft);
		lehrkraft.stateLeitung1Dec(klasse);

		// Malus addieren.
		changeMalusKlasseLehrkraft(klasse, lehrkraft, 1);
	}


	/**
	 * Hauptmethode zum Verändern des Zustandes:
	 * <br> Fügt einer Klasse einen stellv. Klassenlehrer (Leitung 2) hinzu.
	 * <br> Aktualisiert die Bewertung.
	 *
	 * @param klasse      Die {@link UvAlgorithmusDynDatenKlasse}.
	 * @param lehrkraft   Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param fixiert     Falls die Lehrkraft zusätzlich fixiert werden soll.
	 */
	private void stateKlassenLeitung2Add(
			final @NotNull UvAlgorithmusDynDatenKlasse klasse,
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft,
			final boolean fixiert) {

		// Konsistenz prüfen.
		if (!klasse.gibIstLeitung2Potentiell(lehrkraft)) {
			throw new DeveloperNotificationException("Klasse %s will Lehrkraft %s als Leitung 2 hinzufügen, Lehrkraft ist aber keine potentielle!"
					.formatted(klasse.toString(), lehrkraft.toString()));
		}

		// Malus subtrahieren
		changeMalusKlasseLehrkraft(klasse, lehrkraft, -1);

		// Zustand verändern.
		klasse.stateLeitung2ZugeordnetAdd(lehrkraft, fixiert);
		lehrkraft.stateLeitung2Inc(klasse);

		// Malus addieren.
		changeMalusKlasseLehrkraft(klasse, lehrkraft, 1);
	}


	/**
	 * Hauptmethode zum Verändern des Zustandes:
	 * <br> Entfernt aus der Klasse die aktuelle stellv. Klassenleitung (Leitung 2).
	 * <br> Aktualisiert die Bewertung.
	 *
	 * @param klasse      Die {@link UvAlgorithmusDynDatenKlasse}.
	 * @param lehrkraft   Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 */
	private void stateKlassenLeitung2Del(
			final @NotNull UvAlgorithmusDynDatenKlasse klasse,
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft) {

		// Konsistenz prüfen.
		if (klasse.gibIstLeitung2Fixiert(lehrkraft)) {
			throw new DeveloperNotificationException("Klasse %s will Lehrkraft %s als Leitung 2 entfernen, Lehrkraft ist aber fixiert!"
					.formatted(klasse.toString(), lehrkraft.toString()));
		}

		// Malus subtrahieren
		changeMalusKlasseLehrkraft(klasse, lehrkraft, -1);

		// Zustand verändern.
		klasse.stateLeitung2ZugeordnetDel(lehrkraft);
		lehrkraft.stateLeitung2Dec(klasse);

		// Malus addieren.
		changeMalusKlasseLehrkraft(klasse, lehrkraft, 1);

	}


	// ################################################################################
	// State-Methoden sind die einzigen Methoden, die Zuordnungen verändern dürfen.
	// Alle Bewertungen werden dabei dynamisch aktualisiert.
	// ################################################################################

	private void changeMalusLerngruppeLehrkraft(
			final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe,
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft,
			final int faktor) {

		// Regeln mit Bezug zu "Lerngruppe".
		for (final @NotNull UvAlgorithmusDynDatenRegel regel : lerngruppe.regeln) {
			regel.changeMalus(faktor);
		}

		// Regeln mit Bezug zu "Lehrkraft".
		for (final @NotNull UvAlgorithmusDynDatenRegel regel : lehrkraft.regeln) {
			regel.changeMalus(faktor);
		}

		// Regeln mit Bezug zu "Klasse".
		for (final @NotNull UvAlgorithmusDynDatenKlasse klasse : lerngruppe.klassenmenge) {
			// Regeln mit Bezug zu "Klasse" und "Lehrkraft".
			for (final @NotNull UvAlgorithmusDynDatenRegel regel : mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID)) {
				regel.changeMalus(faktor);
			}
		}

		// Regeln mit Bezug zu "Lerngruppe" und "Lehrkraft".
		for (final @NotNull UvAlgorithmusDynDatenRegel regel : mapLerngruppeLehrkraftZuRegeln.get(lerngruppe.interneID).get(lehrkraft.interneID)) {
			regel.changeMalus(faktor);
		}


		// Regeln mit Bezug zu "Fach" und "Lehrkraft".
		for (final @NotNull UvAlgorithmusDynDatenRegel regel : mapFachLehrkraftZuRegeln.get(lerngruppe.fach.interneID).get(lehrkraft.interneID)) {
			regel.changeMalus(faktor);
		}
	}


	private void changeMalusKlasseLehrkraft(
			final @NotNull UvAlgorithmusDynDatenKlasse klasse,
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft,
			final int faktor) {

		// Regeln mit Bezug zu "Klasse".
		for (final @NotNull UvAlgorithmusDynDatenRegel regel : klasse.regeln) {
			regel.changeMalus(faktor);
		}

		// Regeln mit Bezug zu "Lehrkraft".
		for (final @NotNull UvAlgorithmusDynDatenRegel regel : lehrkraft.regeln) {
			regel.changeMalus(faktor);
		}

		// Regeln mit Bezug zu "Klasse" und "Lehrkraft".
		for (final @NotNull UvAlgorithmusDynDatenRegel regel : mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID)) {
			regel.changeMalus(faktor);
		}

	}


}
