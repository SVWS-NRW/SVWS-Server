package de.svws_nrw.core.utils.gost;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.svws_nrw.asd.data.schueler.Schueler;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.core.adt.LongArrayKey;
import de.svws_nrw.core.adt.map.ArrayMap;
import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.gost.GostBlockungKurs;
import de.svws_nrw.core.data.gost.GostBlockungKursLehrer;
import de.svws_nrw.core.data.gost.GostBlockungRegel;
import de.svws_nrw.core.data.gost.GostBlockungSchiene;
import de.svws_nrw.core.data.gost.GostBlockungsdaten;
import de.svws_nrw.core.data.gost.GostBlockungsergebnis;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.GostFachwahl;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.exceptions.UserNotificationException;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.types.kursblockung.GostKursblockungRegelParameterTyp;
import de.svws_nrw.core.types.kursblockung.GostKursblockungRegelTyp;
import de.svws_nrw.core.utils.DTOUtils;
import de.svws_nrw.core.utils.ListUtils;
import de.svws_nrw.core.utils.Map2DUtils;
import de.svws_nrw.core.utils.MapUtils;
import de.svws_nrw.core.utils.SetUtils;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zur Handhabung von Daten des Typs {@link GostBlockungsdaten}.<br>
 * Hierbei werden auch Hilfsmethoden zur Interpretation der Daten erzeugt.
 */
public class GostBlockungsdatenManager {

	/** Damit man nicht immer "\n" schreibt. */
	private final @NotNull String lineSeparator = System.lineSeparator();

	/** Die Blockungsdaten, die im Manager vorhanden sind. */
	private final @NotNull GostBlockungsdaten dtoDaten; // Darf nicht "daten" heißen, wegen Transpiler-Methoden-Konflikt

	/** Der Fächermanager mit den Fächern der gymnasialen Oberstufe. */
	private final @NotNull GostFaecherManager manFaecher;  // Darf nicht "faecherManager" heißen, wegen Transpiler-Methoden-Konflikt

	/** Ein Comparator für Kurse der Blockung. Dieser vergleicht nur die Kursnummern! */
	private final @NotNull Comparator<GostBlockungKurs> compKursnummer =
			(final @NotNull GostBlockungKurs a, final @NotNull GostBlockungKurs b) -> Integer.compare(a.nummer, b.nummer);

	/** Ein Comparator für Schienen der Blockung */
	private final @NotNull Comparator<GostBlockungSchiene> compSchiene =
			(final @NotNull GostBlockungSchiene a, final @NotNull GostBlockungSchiene b) -> Integer.compare(a.nummer, b.nummer);

	/** Ein Comparator für die Lehrkräfte eines Kurses */
	private final @NotNull Comparator<GostBlockungKursLehrer> compLehrkraefte =
			(final @NotNull GostBlockungKursLehrer a, final @NotNull GostBlockungKursLehrer b) -> {
				final int result = Integer.compare(a.reihenfolge, b.reihenfolge);
				if (result != 0) {
					return result;
				}
				return Long.compare(a.id, b.id);
			};

	/** Ein Comparator für die Ergebnisse sortiert nach ID. */
	private final @NotNull Comparator<GostBlockungsergebnis> compErgebnisseNachID =
			(final @NotNull GostBlockungsergebnis a, final @NotNull GostBlockungsergebnis b) -> Long.compare(a.id, b.id);

	/** Ein Comparator für die Schüler. */
	private final @NotNull Comparator<Schueler> compSchueler;

	/** Ein Comparator für die Fachwahlen (SCHÜLERID, FACH, KURSART) */
	private final @NotNull Comparator<GostFachwahl> compFachwahlen;

	/** Ein Comparator für die {@link GostBlockungsergebnis} nach ihrer Bewertung. */
	private final @NotNull Comparator<GostBlockungsergebnis> compErgebnisse = new GostBlockungsergebnisComparator();

	/** Ein Comparator für Kurse der Blockung (KURSART, FACH, KURSNUMMER) */
	private final @NotNull Comparator<GostBlockungKurs> compKurs1kursart2fach3kursnummer;

	/** Ein Comparator für Kurse der Blockung (FACH, KURSART, KURSNUMMER). */
	private final @NotNull Comparator<GostBlockungKurs> compKurs1fach2kursart3kursnummer;

	/** Ein Comparator für Regeln der Blockung */
	private final @NotNull Comparator<GostBlockungRegel> compRegel;

	/** Eine interne Hashmap zum schnellen Zugriff auf die Kurse anhand ihrer Datenbank-ID. */
	private final @NotNull HashMap<Long, GostBlockungKurs> kursById = new HashMap<>();

	/** Eine interne Hashmap zum schnellen Zugriff auf die Listen der Kurse, welche Fach und Kursart gemeinsam haben, anhand der beiden IDs. */
	private final @NotNull HashMap2D<Long, Integer, List<GostBlockungKurs>> kursmengeByFachIdAndKursartId = new HashMap2D<>();

	/** Eine interne Hashmap zum schnellen Zugriff auf die Listen der Fachwahlen, welche Fach und Kursart gemeinsam haben, anhand der beiden IDs. */
	private final @NotNull HashMap2D<Long, Integer, List<GostFachwahl>> fachwahlmengeByFachIdAndKursartId = new HashMap2D<>();

	/** Eine interne Hashmap zum schnellen Zugriff auf die Schienen anhand ihrer Datenbank-ID. */
	private final @NotNull HashMap<Long, GostBlockungSchiene> schieneById = new HashMap<>();

	/** Eine interne Hashmap zum schnellen Zugriff auf die Regeln anhand ihrer Datenbank-ID. */
	private final @NotNull HashMap<Long, GostBlockungRegel> regelById = new HashMap<>();

	/** Eine interne Hashmap zum schnellen Zugriff auf die Regeln eines bestimmten {@link GostKursblockungRegelTyp}. */
	private final @NotNull Map<GostKursblockungRegelTyp, List<GostBlockungRegel>> regelmengeByRegeltyp = new ArrayMap<>(GostKursblockungRegelTyp.values());

	/** Eine interne Hashmap zum Multi-Key-Zugriff auf die Regeln eines bestimmten {@link GostKursblockungRegelTyp}. */
	private final @NotNull HashMap<LongArrayKey, GostBlockungRegel> regelByMultikey = new HashMap<>();

	/** Eine interne Hashmap zum schnellen Zugriff auf die Schüler anhand ihrer Datenbank-ID. */
	private final @NotNull HashMap<Long, Schueler> schuelerById = new HashMap<>();

	/** Schüler-ID --> List<Fachwahl> = Die Fachwahlen des Schülers der jeweiligen Fachart. */
	private final @NotNull HashMap<Long, List<GostFachwahl>> fachwahlmengeBySchuelerId = new HashMap<>();

	/** (Schüler-ID, Fach-ID) --> Kursart = Die Fachwahl des Schülers die dem Fach die Kursart zuordnet. */
	private final @NotNull HashMap2D<Long, Long, GostFachwahl> fachwahlBySchuelerIdAndFachId = new HashMap2D<>();

	/** Fachart-ID --> List<Fachwahl> = Die Fachwahlen einer Fachart. */
	private final @NotNull HashMap<Long, List<GostFachwahl>> fachwahlmengeByFachartId = new HashMap<>();

	/** Ergebnis-ID --> {@link GostBlockungsergebnis} */
	private final @NotNull HashMap<Long, GostBlockungsergebnis> ergebnisById = new HashMap<>();

	/** Ergebnis-ID --> {@link GostBlockungsergebnisManager} */
	private final @NotNull HashMap<Long, GostBlockungsergebnisManager> ergebnisManagerByErgebnisId = new HashMap<>();

	/** Eine sortierte, gecachte Menge der Kurse nach: (FACH, KURSART, KURSNUMMER). */
	private final @NotNull List<GostBlockungKurs> kursmengeSortiertNachFachKursartKursnummer = new ArrayList<>();

	/** Eine sortierte, gecachte Menge der Kurse nach: (KURSART, FACH, KURSNUMMER) */
	private final @NotNull List<GostBlockungKurs> kursmengeSortiertNachKursartFachKursnummer = new ArrayList<>();

	/** Die maximale Zeit in Millisekunden die der Blockungsalgorithmus verwenden darf. */
	private long maxTimeMillis = 1000;

	/** Map ungültiger Regeln, bei denen Fehlern vorliegen und Map die den jeweiligen Fehler beschreibt. */
	private final @NotNull HashMap<Long, GostBlockungRegel> regelUngueltigById = new HashMap<>();
	private final @NotNull HashMap<Long, String> regelUngueltigBeschreibungById = new HashMap<>();


	/**
	 * Erstellt einen neuen Manager mit den angegebenen Blockungsdaten und dem Fächer-Manager.
	 *
	 * @param daten            die Blockungsdaten
	 * @param faecherManager   der Fächer-Manager
	 */
	public GostBlockungsdatenManager(final @NotNull GostBlockungsdaten daten, final @NotNull GostFaecherManager faecherManager) {
		this.manFaecher = faecherManager;
		this.compKurs1fach2kursart3kursnummer = createComparatorKurs1Fach2Kursart3Nummer();
		this.compKurs1kursart2fach3kursnummer = createComparatorKurs1Kursart2Fach3Nummer();
		this.compFachwahlen = createComparatorFachwahlen();
		this.compRegel = createComparatorRegeln();
		this.compSchueler = createComparatorSchueler();

		// Tiefe Kopie (deep copy) der GostBlockungsdaten.
		this.dtoDaten = new GostBlockungsdaten();
		this.dtoDaten.id = daten.id;
		this.dtoDaten.name = daten.name;
		this.dtoDaten.abijahrgang = daten.abijahrgang;
		this.dtoDaten.gostHalbjahr = daten.gostHalbjahr;
		this.dtoDaten.istAktiv = daten.istAktiv;

		// Kopieren und Mappings aufbauen.
		schieneAddListe(daten.schienen); 		// Muss vor den Kursen erzeugt werden.
		fachwahlAddListe(daten.fachwahlen); 	// Muss vor den Schülern erzeugt werden.
		schuelerAddListe(daten.schueler); 		// Muss vor den Regeln erzeugt werden.
		kursAddListe(daten.kurse);
		regelAddListe(daten.regeln);  			// Muss vor den Ergebnissen erzeugt werden.
		ergebnisAddListe(daten.ergebnisse);
	}

	/**
	 * Liefert eine Kurzdarstellung der Kursart mit der übergebenen ID.
	 *
	 * @param kursart   die ID der Kursart
	 *
	 * @return eine Kurzdarstellung der Kursart
	 */
	public @NotNull String toStringKursartSimple(final int kursart) {
		final GostKursart gKursart = GostKursart.fromIDorNull(kursart);
		return (gKursart == null) ? ("[Kursart-ID = " + kursart + " (ohne Mapping)]") : gKursart.kuerzel;
	}

	/**
	 * Liefert möglichst viele Informationen zum Kurs mit der übergebenen ID.
	 *
	 * @param idKurs   die Datenbank-ID des Kurses
	 *
	 * @return möglichst viele Informationen zum Kurs
	 */
	public @NotNull String toStringKurs(final long idKurs) {
		final GostBlockungKurs kurs = kursById.get(idKurs);
		if (kurs == null) {
			return "[Kurs-ID=%d nicht im Mapping]".formatted(idKurs);
		}

		final GostFach gFach = manFaecher.get(kurs.fach_id);
		@NotNull String sFach = "Fach-ID = " + kurs.fach_id + " (ohne Mapping)";
		if (gFach != null) {
			sFach = (gFach.kuerzelAnzeige == null) ? ("Fach-ID = " + kurs.fach_id + " (ohne 'kuerzelAnzeige')") : gFach.kuerzelAnzeige;
		}

		return "[Kurs " + sFach + "-" + toStringKursartSimple(kurs.kursart) + kurs.nummer + (kurs.suffix.isEmpty() ? "" : "-") + kurs.suffix + "]";
	}

	/**
	 * Liefert eine Kurzdarstellung des Kurses mit der übergebenen ID.
	 *
	 * @param idKurs   die Datenbank-ID des Kurses
	 *
	 * @return eine Kurzdarstellung des Kurses
	 */
	public @NotNull String toStringKursSimple(final long idKurs) {
		final GostBlockungKurs kurs = kursById.get(idKurs);
		if (kurs == null) {
			return "[Kurs (%d) nicht vorhanden]".formatted(idKurs);
		}

		return "(" + kurs.id + ") " + toStringFachSimple(kurs.fach_id) + "-" + toStringKursartSimple(kurs.kursart) + kurs.nummer
				+ (kurs.suffix.isEmpty() ? "" : "-") + kurs.suffix;
	}

	/**
	 * Liefert eine Kurzdarstellung des Kurses (ohne ID, außer der ID ist kein Kurs zugeordnet).
	 *
	 * @param idKurs   die Datenbank-ID des Kurses
	 *
	 * @return eine Kurzdarstellung des Kurses (ohne ID, außer der ID ist kein Kurs zugeordnet)
	 */
	public @NotNull String toStringKursSimpleOhneID(final long idKurs) {
		final GostBlockungKurs kurs = kursById.get(idKurs);
		if (kurs == null) {
			return "[Kurs-ID %d nicht zugeordnet]".formatted(idKurs);
		}

		return toStringFachSimple(kurs.fach_id) + "-" + toStringKursartSimple(kurs.kursart) + kurs.nummer
				+ (kurs.suffix.isEmpty() ? "" : "-") + kurs.suffix;
	}

	/**
	 * Liefert eine Kurzdarstellung des Faches mit der übergebenen ID.
	 *
	 * @param idFach   die Datenbank-ID des Faches
	 *
	 * @return eine Kurzdarstellung des Faches
	 */
	public @NotNull String toStringFachSimple(final long idFach) {
		final GostFach gFach = manFaecher.get(idFach);
		if (gFach == null) {
			return "[Fach-ID = %d (ohne Mapping)]".formatted(idFach);
		}
		if (gFach.kuerzelAnzeige == null) {
			return "[Fach-ID = %d (ohne 'kuerzelAnzeige')]".formatted(idFach);
		}
		return gFach.kuerzelAnzeige;
	}

	/**
	 * Liefert eine Kurzdarstellung der Fachart (Fach, Kursart).
	 *
	 * @param idFach    die Datenbank-ID des Faches
	 * @param kursart   die Datenbank-ID der Kursart
	 *
	 * @return eine Kurzdarstellung der Fachart (Fach, Kursart)
	 */
	public @NotNull String toStringFachartSimple(final long idFach, final int kursart) {
		return toStringFachSimple(idFach) + "-" + toStringKursartSimple(kursart);
	}

	/**
	 * Liefert eine Kurzdarstellung der Fachart (Fach, Kursart).
	 *
	 * @param idFachart   die Fachart (zusammengesetzt aus Fach und Kursart)
	 *
	 * @return eine Kurzdarstellung der Fachart (Fach, Kursart)
	 */
	public @NotNull String toStringFachartSimpleByFachartID(final long idFachart) {
		final long idFach = GostKursart.getFachID(idFachart);
		final int kursart = GostKursart.getKursartID(idFachart);
		return toStringFachSimple(idFach) + "-" + toStringKursartSimple(kursart);
	}

	/**
	 * Liefert möglichst viele Informationen zum Schüler mit der übergebenen ID.
	 *
	 * @param idSchueler   die Datenbank-ID des Schülers
	 *
	 * @return möglichst viele Informationen zum Schüler
	 */
	public @NotNull String toStringSchueler(final long idSchueler) {
		final Schueler schueler = schuelerById.get(idSchueler);
		if (schueler == null) {
			return "[Schüler (%d) ohne Mapping]".formatted(idSchueler);
		}
		return "[Schüler (" + schueler.id + "): " + schueler.nachname + ", " + schueler.vorname + "]";
	}

	/**
	 * Liefert eine Kurzdarstellung des Schüler mit der übergebenen ID.
	 *
	 * @param idSchueler   die Datenbank-ID des Schülers
	 *
	 * @return eine Kurzdarstellung des Schülers
	 */
	public @NotNull String toStringSchuelerSimple(final long idSchueler) {
		final Schueler schueler = schuelerById.get(idSchueler);
		if (schueler == null) {
			return "[Schüler (%d) ohne Mapping]".formatted(idSchueler);
		}
		return schueler.nachname + ", " + schueler.vorname;
	}

	/**
	 * Liefert möglichst viele Informationen zur Schiene mit der übergebenen ID.
	 *
	 * @param idSchiene   die Datenbank-ID der Schiene
	 *
	 * @return möglichst viele Informationen zur Schiene
	 */
	public @NotNull String toStringSchiene(final long idSchiene) {
		final GostBlockungSchiene schiene = schieneById.get(idSchiene);
		if (schiene == null) {
			return "[Schiene (%d) ohne Mapping]".formatted(idSchiene);
		}
		return "[Schiene: ID " + schiene.id + ", Nr. " + schiene.nummer + ", Bez. " + schiene.bezeichnung + ", Stunden " + schiene.wochenstunden + "]";
	}

	/**
	 * Liefert eine Kurzdarstellung zur Schiene mit der übergebenen ID.
	 *
	 * @param idSchiene   die Datenbank-ID der Schiene
	 *
	 * @return eine Kurzdarstellung zur Schiene
	 */
	public @NotNull String toStringSchieneSimple(final long idSchiene) {
		final GostBlockungSchiene schiene = schieneById.get(idSchiene);
		if (schiene == null) {
			return "[Schiene (%d) ohne Mapping]".formatted(idSchiene);
		}
		return "Schiene Nr. " + schiene.nummer;
	}

	/**
	 * Liefert möglichst viele Informationen zur Lehrkraft mit der übergebenen ID.
	 *
	 * @param idKurs        die Datenbank-ID des Kurses
	 * @param idLehrkraft   die Datenbank-ID der Lehrkraft
	 *
	 * @return möglichst viele Informationen zur Lehrkraft
	 */
	public @NotNull String toStringKursLehrkraft(final long idKurs, final long idLehrkraft) {
		final GostBlockungKurs kurs = kursById.get(idKurs);
		if (kurs == null) {
			return "[Lehrkraft (ID=%d)]".formatted(idLehrkraft);
		}
		for (final @NotNull GostBlockungKursLehrer lehrer : kurs.lehrer) {
			if (lehrer.id == idLehrkraft) {
				return "[Lehrkraft (ID=" + idLehrkraft + ") " + lehrer.kuerzel + "]";
			}
		}
		return "[Lehrkraft (ID=%d)]".formatted(idLehrkraft);
	}

	/**
	 * Liefert eine Kurzdarstellung zur übergebenen Fachwahl eines Schülers.
	 *
	 * @param gFachwahl   das {@link GostFachwahl}-Objekt
	 *
	 * @return eine Kurzdarstellung zur Fachwahl eines Schülers
	 */
	public String toStringFachwahlSimple(final @NotNull GostFachwahl gFachwahl) {
		return toStringSchuelerSimple(gFachwahl.schuelerID) + " wählt " + toStringFachartSimple(gFachwahl.fachID, gFachwahl.kursartID);
	}

	/**
	 * Liefert möglichst viele Informationen zur Regel mit der übergebenen ID.
	 *
	 * @param idRegel   die Datenbank-ID der Regel
	 *
	 * @return möglichst viele Informationen zur Regel
	 */
	public @NotNull String toStringRegel(final long idRegel) {
		final GostBlockungRegel regel = regelById.get(idRegel);
		if (regel == null) {
			return "[Regel (%d) ohne Mapping]".formatted(idRegel);
		}
		return "[Regel (" + regel.id + ", Nr. " + regel.typ + "): " + regel.parameter + "]";
	}

	private @NotNull Comparator<GostBlockungRegel> createComparatorRegeln() {
		final @NotNull Comparator<GostBlockungRegel> comp = (final @NotNull GostBlockungRegel a, final @NotNull GostBlockungRegel b) -> {
			// 1. Kriterium Typ
			final int cmp1 = Integer.compare(a.typ, b.typ);
			if (cmp1 != 0) {
				return cmp1;
			}

			final @NotNull GostKursblockungRegelTyp typ = GostKursblockungRegelTyp.fromTyp(a.typ);
			// 2. Kriterium Regeltyp-spezifisch
			final int cmp2 = switch (typ) {
				case KURS_FIXIERE_IN_SCHIENE -> compareRegel1Kurs2Nummer3Id(a, b);
				case KURS_SPERRE_IN_SCHIENE -> compareRegel1Kurs2Nummer3Id(a, b);
				case SCHUELER_FIXIEREN_IN_KURS -> compareRegel1Schueler2Kurs(a, b);
				case SCHUELER_VERBIETEN_IN_KURS -> compareRegel1Schueler2Kurs(a, b);
				case KURS_VERBIETEN_MIT_KURS -> compareRegel1Kurs2Kurs(a, b);
				case KURS_ZUSAMMEN_MIT_KURS -> compareRegel1Kurs2Kurs(a, b);
				case KURS_MIT_DUMMY_SUS_AUFFUELLEN -> compareRegel1Kurs2Id(a, b);
				case KURS_MAXIMALE_SCHUELERANZAHL -> compareRegel1Kurs2Id(a, b);
				case KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN -> compareRegel1Kurs2Id(a, b);
				case SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH -> compareRegel1Schueler2Schueler3Fach(a, b);
				case SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH -> compareRegel1Schueler2Schueler3Fach(a, b);
				case SCHUELER_ZUSAMMEN_MIT_SCHUELER -> compareRegel1Schueler2Schueler(a, b);
				case SCHUELER_VERBIETEN_MIT_SCHUELER -> compareRegel1Schueler2Schueler(a, b);
				case SCHUELER_IGNORIEREN -> compareRegelSchueler(a, b);

				default -> 0;
			};
			if (cmp2 != 0) {
				return cmp2;
			}

			return Long.compare(a.id, b.id);
		};

		return comp;
	}

	private @NotNull Comparator<Schueler> createComparatorSchueler() {
		final @NotNull Comparator<Schueler> comp = (final @NotNull Schueler a, final @NotNull Schueler b) -> {
			final int cmpSchueler = compareSchueler1Nachname2Vorname3Id(a.id, b.id);
			if (cmpSchueler != 0) {
				return cmpSchueler;
			}

			return Long.compare(a.id, b.id);
		};

		return comp;
	}

	private @NotNull Comparator<GostFachwahl> createComparatorFachwahlen() {
		final @NotNull Comparator<GostFachwahl> comp = (final @NotNull GostFachwahl a, final @NotNull GostFachwahl b) -> {
			final int cmpSchueler = compareSchueler1Nachname2Vorname3Id(a.schuelerID, b.schuelerID);
			if (cmpSchueler != 0) {
				return cmpSchueler;
			}

			final int cmpFach = compareFach(a.fachID, b.fachID);
			if (cmpFach != 0) {
				return cmpFach;
			}

			return Integer.compare(a.kursartID, b.kursartID);
		};

		return comp;
	}

	private @NotNull Comparator<GostBlockungKurs> createComparatorKurs1Fach2Kursart3Nummer() {
		final @NotNull Comparator<GostBlockungKurs> comp = (final @NotNull GostBlockungKurs a, final @NotNull GostBlockungKurs b) -> {
			final int cmpFach = compareFach(a.fach_id, b.fach_id);
			if (cmpFach != 0) {
				return cmpFach;
			}

			final int cmpKursart = Integer.compare(a.kursart, b.kursart);
			if (cmpKursart != 0) {
				return cmpKursart;
			}

			return Integer.compare(a.nummer, b.nummer);
		};

		return comp;
	}

	private @NotNull Comparator<GostBlockungKurs> createComparatorKurs1Kursart2Fach3Nummer() {
		final @NotNull Comparator<GostBlockungKurs> comp = (final @NotNull GostBlockungKurs a, final @NotNull GostBlockungKurs b) -> {
			final int k1 = (a.kursart == GostKursart.ZK.id) ? GostKursart.GK.id : a.kursart;
			final int k2 = (b.kursart == GostKursart.ZK.id) ? GostKursart.GK.id : b.kursart;
			final int cmpKursartGKZK = Integer.compare(k1, k2);
			if (cmpKursartGKZK != 0) {
				return cmpKursartGKZK;
			}

			final int cmpFach = compareFach(a.fach_id, b.fach_id);
			if (cmpFach != 0) {
				return cmpFach;
			}

			final int cmpKursart = Integer.compare(a.kursart, b.kursart);
			if (cmpKursart != 0) {
				return cmpKursart;
			}

			return Integer.compare(a.nummer, b.nummer);
		};

		return comp;
	}

	private int compareRegel1Kurs2Id(final @NotNull GostBlockungRegel a, final @NotNull GostBlockungRegel b) {
		final int cmpKurs1 = compareKurs1Kursart2Fach3Nummer4Id(a.parameter.get(0), b.parameter.get(0));
		if (cmpKurs1 != 0) {
			return cmpKurs1;
		}

		return Long.compare(a.id, b.id);
	}

	private int compareRegel1Kurs2Nummer3Id(final @NotNull GostBlockungRegel a, final @NotNull GostBlockungRegel b) {
		final int cmpKurs1 = compareKurs1Kursart2Fach3Nummer4Id(a.parameter.get(0), b.parameter.get(0));
		if (cmpKurs1 != 0) {
			return cmpKurs1;
		}

		final int cmpSchienenNr = Long.compare(a.parameter.get(1), b.parameter.get(1));
		if (cmpSchienenNr != 0) {
			return cmpSchienenNr;
		}

		return Long.compare(a.id, b.id);
	}

	private int compareRegelSchueler(final @NotNull GostBlockungRegel a, final @NotNull GostBlockungRegel b) {
		final int cmpSchueler1 = compareSchueler1Nachname2Vorname3Id(a.parameter.get(0), b.parameter.get(0));
		if (cmpSchueler1 != 0) {
			return cmpSchueler1;
		}

		return Long.compare(a.id, b.id);
	}

	private int compareRegel1Schueler2Kurs(final @NotNull GostBlockungRegel a, final @NotNull GostBlockungRegel b) {
		final int cmpSchueler1 = compareSchueler1Nachname2Vorname3Id(a.parameter.get(0), b.parameter.get(0));
		if (cmpSchueler1 != 0) {
			return cmpSchueler1;
		}

		final int cmpKurs1 = compareKurs1Kursart2Fach3Nummer4Id(a.parameter.get(1), b.parameter.get(1));
		if (cmpKurs1 != 0) {
			return cmpKurs1;
		}

		return Long.compare(a.id, b.id);
	}

	private int compareRegel1Kurs2Kurs(final @NotNull GostBlockungRegel a, final @NotNull GostBlockungRegel b) {
		final int cmpKurs1 = compareKurs1Kursart2Fach3Nummer4Id(a.parameter.get(0), b.parameter.get(0));
		if (cmpKurs1 != 0) {
			return cmpKurs1;
		}

		final int cmpKurs2 = compareKurs1Kursart2Fach3Nummer4Id(a.parameter.get(1), b.parameter.get(1));
		if (cmpKurs2 != 0) {
			return cmpKurs2;
		}

		return Long.compare(a.id, b.id);
	}

	private int compareRegel1Schueler2Schueler3Fach(final @NotNull GostBlockungRegel a, final @NotNull GostBlockungRegel b) {
		final int cmpSchueler1 = compareSchueler1Nachname2Vorname3Id(a.parameter.get(0), b.parameter.get(0));
		if (cmpSchueler1 != 0) {
			return cmpSchueler1;
		}

		final int cmpSchueler2 = compareSchueler1Nachname2Vorname3Id(a.parameter.get(1), b.parameter.get(1));
		if (cmpSchueler2 != 0) {
			return cmpSchueler2;
		}

		final int cmpFach = compareFach(a.parameter.get(2), b.parameter.get(2));
		if (cmpFach != 0) {
			return cmpFach;
		}

		return Long.compare(a.id, b.id);
	}

	private int compareRegel1Schueler2Schueler(final @NotNull GostBlockungRegel a, final @NotNull GostBlockungRegel b) {
		final int cmpSchueler1 = compareSchueler1Nachname2Vorname3Id(a.parameter.get(0), b.parameter.get(0));
		if (cmpSchueler1 != 0) {
			return cmpSchueler1;
		}

		final int cmpSchueler2 = compareSchueler1Nachname2Vorname3Id(a.parameter.get(1), b.parameter.get(1));
		if (cmpSchueler2 != 0) {
			return cmpSchueler2;
		}

		return Long.compare(a.id, b.id);
	}

	private int compareSchueler1Nachname2Vorname3Id(final long idSchueler1, final long idSchueler2) {
		final Schueler a = schuelerById.get(idSchueler1);
		final Schueler b = schuelerById.get(idSchueler2);

		if (a == null) {
			return (b == null) ? 0 : -1;
		}

		if (b == null) {
			return +1;
		}

		final int cNachname = a.nachname.compareTo(b.nachname);
		if (cNachname != 0) {
			return cNachname;
		}

		final int cVorname = a.vorname.compareTo(b.vorname);
		if (cVorname != 0) {
			return cVorname;
		}

		return Long.compare(a.id, b.id);
	}

	private int compareFach(final long idFach1, final long idFach2) {
		final GostFach aFach = manFaecher.get(idFach1);
		final GostFach bFach = manFaecher.get(idFach2);

		if (aFach == null) {
			return (bFach == null) ? 0 : -1;
		}

		return (bFach == null) ? +1 : GostFaecherManager.comp.compare(aFach, bFach);
	}

	private int compareKurs1Kursart2Fach3Nummer4Id(final long idKurs1, final long idKurs2) {
		final GostBlockungKurs aKurs = kursById.get(idKurs1);
		final GostBlockungKurs bKurs = kursById.get(idKurs2);

		if (aKurs == null) {
			return (bKurs == null) ? 0 : -1;
		}

		if (bKurs == null) {
			return +1;
		}

		final int cmpKursart = Long.compare(aKurs.kursart, bKurs.kursart);
		if (cmpKursart != 0) {
			return cmpKursart;
		}

		final int cmpFach = compareFach(aKurs.fach_id, bKurs.fach_id);
		if (cmpFach != 0) {
			return cmpFach;
		}

		final int cmpNummer = Long.compare(aKurs.nummer, bKurs.nummer);
		if (cmpNummer != 0) {
			return cmpNummer;
		}

		return Long.compare(aKurs.id, bKurs.id);
	}

	/**
	 * Fügt das übergebenen Ergebnis der Blockung hinzu.
	 *
	 * @param ergebnis   das {@link GostBlockungsergebnis}-Objekt, welches hinzugefügt wird
	 *
	 * @throws DeveloperNotificationException Falls in den Daten Inkonsistenzen sind.
	 */
	public void ergebnisAdd(final @NotNull GostBlockungsergebnis ergebnis) throws DeveloperNotificationException {
		ergebnisAddListe(ListUtils.create1(ergebnis));
	}

	/**
	 * Fügt die Menge an Ergebnissen {@link GostBlockungsergebnis} hinzu.
	 *
	 * @param ergebnismenge   die Menge an Ergebnissen
	 *
	 * @throws DeveloperNotificationException Falls in den Daten Inkonsistenzen sind.
	 */
	public void ergebnisAddListe(final @NotNull List<GostBlockungsergebnis> ergebnismenge) throws DeveloperNotificationException {
		// Datenkonsistenz überprüfen
		final @NotNull HashSet<Long> setId = new HashSet<>(ergebnisById.keySet());
		for (final @NotNull GostBlockungsergebnis ergebnis : ergebnismenge) {
			DeveloperNotificationException.ifInvalidID("pErgebnis.id", ergebnis.id);
			DeveloperNotificationException.ifInvalidID("pErgebnis.blockungID", ergebnis.blockungID);
			DeveloperNotificationException.ifNull("GostHalbjahr.fromID(" + ergebnis.gostHalbjahr + ")", GostHalbjahr.fromID(ergebnis.gostHalbjahr));
			DeveloperNotificationException.ifTrue("Ergebnis-ID " + ergebnis.id + " Doppelung!", !setId.add(ergebnis.id));
		}

		// Hinzufügen
		for (final @NotNull GostBlockungsergebnis ergebnis : ergebnismenge) {
			final GostBlockungsergebnisManager ergebnisManager = new GostBlockungsergebnisManager(this, ergebnis);
			DeveloperNotificationException.ifMapPutOverwrites(ergebnisById, ergebnis.id, ergebnis);
			DeveloperNotificationException.ifMapPutOverwrites(ergebnisManagerByErgebnisId, ergebnis.id, ergebnisManager);
			dtoDaten.ergebnisse.add(ergebnis);
		}

		// Sortieren
		dtoDaten.ergebnisse.sort(compErgebnisse);
	}

	// #########################################################################
	// ##########                 Kurs-Anfragen                       ##########
	// #########################################################################

	/**
	 * Liefert einen {@link GostBlockungsergebnis} aus der Liste der Ergebnisse.
	 * Wirft eine Exception, falls es keinen Listeneintrag mit dieser ID gibt.
	 *
	 * @param idErgebnis   die Datenbank-ID des Ergebnisses
	 *
	 * @return einen {@link GostBlockungsergebnis}
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public @NotNull GostBlockungsergebnis ergebnisGet(final long idErgebnis) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Es wurde kein Ergebnis mit ID(" + idErgebnis + ") gefunden!", ergebnisById.get(idErgebnis));
	}

	/**
	 * Liefert einen {@link GostBlockungsergebnisManager} für das Ergebnis mit der übergebenen ID.
	 * Wirft eine Exception, falls es keinen Manager für ein Ergebnis mit dieser ID gibt.
	 *
	 * @param idErgebnis   die Datenbank-ID des Ergebnisses
	 *
	 * @return einen {@link GostBlockungsergebnisManager}
	 * @throws DeveloperNotificationException Falls es keinen Manager für ein Ergebnis mit dieser ID gibt.
	 */
	public @NotNull GostBlockungsergebnisManager ergebnisManagerGet(final long idErgebnis) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Es wurde kein Ergebnis mit ID(" + idErgebnis + ") gefunden!",
				ergebnisManagerByErgebnisId.get(idErgebnis));
	}

	/**
	 * Liefert TRUE, falls ein {@link GostBlockungsergebnisManager}-Objekt mit der ID existiert.
	 *
	 * @param idErgebnis   die Datenbank-ID des Ergebnisses
	 *
	 * @return true, falls ein {@link GostBlockungsergebnisManager}-Objekt mit der ID existiert
	 */
	public boolean ergebnisManagerExists(final long idErgebnis) {
		return ergebnisManagerByErgebnisId.containsKey(idErgebnis);
	}

	/**
	 * Liefert die sortierte Menge aller {@link GostBlockungsergebnisManager}.
	 *
	 * @return die sortierte Menge aller {@link GostBlockungsergebnisManager}
	 */
	public @NotNull List<GostBlockungsergebnisManager> ergebnisManagerGetListeUnsortiert() {
		return new ArrayList<>(ergebnisManagerByErgebnisId.values());
	}

	/**
	 * Liefert eine sortierte Menge der {@link GostBlockungsergebnis} nach ihrer Bewertung.
	 *
	 * @return eine sortierte Menge der {@link GostBlockungsergebnis} nach ihrer Bewertung
	 */
	public @NotNull List<GostBlockungsergebnis> ergebnisGetListeSortiertNachBewertung() {
		return new ArrayList<>(dtoDaten.ergebnisse);
	}

	/**
	 * Liefert eine sortierte Menge der {@link GostBlockungsergebnis} nach ihrer ID.
	 *
	 * @return eine sortierte Menge der {@link GostBlockungsergebnis} nach ihrer ID
	 */
	public @NotNull List<GostBlockungsergebnis> ergebnisGetListeSortiertNachID() {
		final @NotNull List<GostBlockungsergebnis> list = new ArrayList<>(dtoDaten.ergebnisse);
		list.sort(compErgebnisseNachID);
		return list;
	}

	/**
	 * Entfernt die Menge an {@link GostBlockungsergebnis}-Objekten anhand ihrer ID.
	 *
	 * @param listeDerErgebnisIDs   die IDs der Ergebnisse
	 *
	 * @throws DeveloperNotificationException Falls es keine Ergebnisse mit diesen IDs gibt.
	 */
	public void ergebnisRemoveListeByIDs(final @NotNull Set<Long> listeDerErgebnisIDs) throws DeveloperNotificationException {
		// Überprüfen
		for (final long idErgebnis : listeDerErgebnisIDs) {
			DeveloperNotificationException.ifMapNotContains("Ergebnis-Map", ergebnisById, idErgebnis);
			DeveloperNotificationException.ifMapNotContains("ErgebnisManager-Map", ergebnisManagerByErgebnisId, idErgebnis);
		}

		// Entfernen des Ergebnisses.
		for (final long idErgebnis : listeDerErgebnisIDs) {
			final @NotNull GostBlockungsergebnis e = ergebnisGet(idErgebnis);
			dtoDaten.ergebnisse.remove(e);
			ergebnisById.remove(e.id);
			ergebnisManagerByErgebnisId.remove(e.id);
		}

		// Neusortierung nicht nötig.
	}

	/**
	 * Entfernt die Menge an {@link GostBlockungsergebnis}-Objekten.
	 *
	 * @param ergebnismenge   die Menge an Ergebnissen
	 *
	 * @throws DeveloperNotificationException Falls es keine Ergebnisse mit diesen IDs gibt.
	 */
	public void ergebnisRemoveListe(final @NotNull List<GostBlockungsergebnis> ergebnismenge) throws DeveloperNotificationException {
		// ID kopieren, da Löschen über Objektidentität nicht funktioniert!
		final @NotNull HashSet<Long> listIDs = new HashSet<>();
		for (final @NotNull GostBlockungsergebnis e : ergebnismenge) {
			listIDs.add(e.id);
		}

		ergebnisRemoveListeByIDs(listIDs);
	}

	/**
	 * Entfernt das Ergebnis mit der übergebenen ID aus der Blockung.
	 *
	 * @param idErgebnis   die Datenbank-ID des zu entfernenden Ergebnisses
	 *
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public void ergebnisRemoveByID(final long idErgebnis) throws DeveloperNotificationException {
		ergebnisRemoveListeByIDs(SetUtils.create1(idErgebnis));
	}

	/**
	 * Entfernt das übergebenen Ergebnis aus der Blockung.
	 *
	 * @param ergebnis   das zu entfernende Ergebnis
	 *
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public void ergebnisRemove(final @NotNull GostBlockungsergebnis ergebnis) throws DeveloperNotificationException {
		ergebnisRemoveListeByIDs(SetUtils.create1(ergebnis.id));
	}

	/**
	 * Sortiert alle Ergebnisse neu (nach ihrer Bewertung).
	 *
	 * @param ergebnis   das Ergebnis mit der neuen Bewertung
	 *
	 * @throws DeveloperNotificationException falls die Daten inkonsistent sind.
	 */
	public void ergebnisUpdateBewertung(final @NotNull GostBlockungsergebnis ergebnis) throws DeveloperNotificationException {
		// Datenkonsistenz überprüfen.
		DeveloperNotificationException.ifInvalidID("pErgebnis.id", ergebnis.id);
		DeveloperNotificationException.ifInvalidID("pErgebnis.blockungID", ergebnis.blockungID);

		// Ergebnisse sortieren.
		dtoDaten.ergebnisse.sort(compErgebnisse);
	}

	/**
	 * Revalidiert alle Ergebnisse. Dies führt zur Aktualisierung aller Ergebnisse.
	 */
	public void ergebnisAlleRevalidieren() {
		for (final GostBlockungsergebnisManager ergebnisManager : ergebnisManagerByErgebnisId.values()) {
			ergebnisManager.stateRevalidateEverything();
		}
	}

	/**
	 * Liefert die aktuelle Anzahl an Ergebnissen, die im Manager gespeichert sind.
	 *
	 * @return die Anzahl an Ergebnissen
	 */
	public int ergebnisGetAnzahl() {
		return dtoDaten.ergebnisse.size();
	}

	/**
	 * Liefert den Wert des 1. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht genügend gesetzten Kurse. <br>
	 * - Die Anzahl der Regelverletzungen. <br>
	 *
	 * @param idErgebnis   die Datenbank-ID des Ergebnisses
	 *
	 * @return den Wert des 1. Bewertungskriteriums
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public int ergebnisGetBewertung1Wert(final long idErgebnis) throws DeveloperNotificationException {
		final @NotNull GostBlockungsergebnis e = ergebnisGet(idErgebnis);
		int summe = 0;
		summe += e.bewertung.anzahlKurseNichtZugeordnet;
		summe += e.bewertung.regelVerletzungen.size();
		return summe;
	}

	/**
	 * Liefert eine Güte des 1. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl der Regelverletzungen. <br>
	 * - Die Anzahl der nicht genügend gesetzten Kurse. <br>
	 *
	 * @param idErgebnis   die Datenbank-ID des Ergebnisses
	 *
	 * @return eine Güte des 1. Bewertungskriteriums im Bereich [0;1], mit 0=optimal
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public double ergebnisGetBewertung1Intervall(final long idErgebnis) throws DeveloperNotificationException {
		final double summe = ergebnisGetBewertung1Wert(idErgebnis);
		return 1 - (1 / ((0.25 * summe) + 1));
	}

	/**
	 * Liefert den Wert des 2. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht zugeordneten Schülerfachwahlen. <br>
	 * - Die Anzahl der Schülerkollisionen. <br>
	 *
	 * @param idErgebnis   die Datenbank-ID des Ergebnisses
	 *
	 * @return den Wert des 2. Bewertungskriteriums
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public int ergebnisGetBewertung2Wert(final long idErgebnis) throws DeveloperNotificationException {
		final @NotNull GostBlockungsergebnis e = ergebnisGet(idErgebnis);
		int summe = 0;
		summe += e.bewertung.anzahlSchuelerNichtZugeordnet;
		summe += e.bewertung.anzahlSchuelerKollisionen;
		return summe;
	}

	/**
	 * Liefert eine Güte des 2. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht zugeordneten Schülerfachwahlen. <br>
	 * - Die Anzahl der Schülerkollisionen. <br>
	 *
	 * @param idErgebnis   die Datenbank-ID des Ergebnisses
	 *
	 * @return eine Güte des 2. Bewertungskriteriums im Bereich [0;1], mit 0=optimal
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public double ergebnisGetBewertung2Intervall(final long idErgebnis) throws DeveloperNotificationException {
		final double summe = ergebnisGetBewertung2Wert(idErgebnis);
		return 1 - (1 / ((0.25 * summe) + 1));
	}

	/**
	 * Liefert den Wert des 3. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Größte Kursdifferenz. <br>
	 * Der Wert 0 und 1 werden unterschieden, sind aber von der Bewertung her Äquivalent.
	 *
	 * @param idErgebnis   die Datenbank-ID des Ergebnisses
	 *
	 * @return den Wert des 3. Bewertungskriteriums
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public int ergebnisGetBewertung3Wert(final long idErgebnis) throws DeveloperNotificationException {
		final @NotNull GostBlockungsergebnis e = ergebnisGet(idErgebnis);
		return e.bewertung.kursdifferenzMax;
	}

	/**
	 * Liefert eine Güte des 3. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Größte Kursdifferenz. <br>
	 * Der Wert 0 und 1 werden unterschieden, sind aber von der Bewertung her Äquivalent.
	 *
	 * @param idErgebnis   die Datenbank-ID des Ergebnisses
	 *
	 * @return eine Güte des 3. Bewertungskriteriums im Bereich [0;1], mit 0=optimal
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public double ergebnisGetBewertung3Intervall(final long idErgebnis) throws DeveloperNotificationException {
		int wert = ergebnisGetBewertung3Wert(idErgebnis);
		if (wert > 0) {
			wert--; // Jede Kursdifferenz wird um 1 reduziert, außer die 0.
		}
		return 1 - (1 / ((0.25 * wert) + 1));
	}

	/**
	 * Liefert den Wert des 4. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl an Kursen mit gleicher Fachart (Fach, Kursart) in einer Schiene. <br>
	 * Dieses Bewertungskriterium wird teilweise absichtlich verletzt, wenn z. B. Schienen erzeugt werden mit dem selben
	 * Fach (Sport-Schiene). Nichtsdestotrotz möchte man häufig nicht die selben Fächer in einer Schiene, aufgrund von
	 * Raumkapazitäten (Fachräume).
	 *
	 * @param idErgebnis   die Datenbank-ID des Ergebnisses
	 *
	 * @return den Wert des 4. Bewertungskriteriums
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public int ergebnisGetBewertung4Wert(final long idErgebnis) throws DeveloperNotificationException {
		final @NotNull GostBlockungsergebnis e = ergebnisGet(idErgebnis);
		return e.bewertung.anzahlKurseMitGleicherFachartProSchiene;
	}

	/**
	 * Liefert eine Güte des 4. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl an Kursen mit gleicher Fachart (Fach, Kursart) in einer Schiene. <br>
	 * Dieses Bewertungskriterium wird teilweise absichtlich verletzt, wenn z. B. Schienen erzeugt werden mit dem selben
	 * Fach (Sport-Schiene). Nichtsdestotrotz möchte man häufig nicht die selben Fächer in einer Schiene, aufgrund von
	 * Raumkapazitäten (Fachräume).
	 *
	 * @param idErgebnis   die Datenbank-ID des Ergebnisses
	 *
	 * @return eine Güte des 4. Bewertungskriteriums im Bereich [0;1], mit 0=optimal
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public double ergebnisGetBewertung4Intervall(final long idErgebnis) throws DeveloperNotificationException {
		final int wert = ergebnisGetBewertung4Wert(idErgebnis);
		return 1 - (1 / ((0.25 * wert) + 1));
	}

	private void kursAddKursOhneSortierung(final @NotNull GostBlockungKurs kurs) throws DeveloperNotificationException {
		// Hinzufügen des Kurses.
		DeveloperNotificationException.ifMapPutOverwrites(kursById, kurs.id, kurs);
		DeveloperNotificationException.ifListAddsDuplicate("Kursmenge nach Fach, Kursart und Kursnummer sortiert", kursmengeSortiertNachFachKursartKursnummer,
				kurs);
		DeveloperNotificationException.ifListAddsDuplicate("Kursmenge nach Kursart, Fach und Kursnummer sortiert", kursmengeSortiertNachKursartFachKursnummer,
				kurs);
		final List<GostBlockungKurs> liste = Map2DUtils.getOrCreateArrayList(kursmengeByFachIdAndKursartId, kurs.fach_id, kurs.kursart);
		liste.add(kurs);
		liste.sort(compKursnummer);
		dtoDaten.kurse.add(kurs);
	}

	/**
	 * Fügt den übergebenen Kurs zu der Blockung hinzu.
	 *
	 * @param kurs   das {@link GostBlockungKurs}-Objekt, welches hinzugefügt wird
	 *
	 * @throws DeveloperNotificationException falls die Daten des Kurses inkonsistent sind.
	 */
	public void kursAdd(final @NotNull GostBlockungKurs kurs) throws DeveloperNotificationException {
		kursAddListe(ListUtils.create1(kurs));
	}

	/**
	 * Fügt die Menge an Kursen hinzu.
	 *
	 * @param kursmenge   die Menge an Kursen
	 *
	 * @throws DeveloperNotificationException Falls die Daten der Kurse inkonsistent sind.
	 */
	public void kursAddListe(final @NotNull List<GostBlockungKurs> kursmenge) throws DeveloperNotificationException {
		// Datenkonsistenz überprüfen.
		final @NotNull HashSet<Long> setId = new HashSet<>();
		for (final @NotNull GostBlockungKurs kAlt : dtoDaten.kurse) {
			setId.add(kAlt.id);
		}
		final int nSchienen = schieneGetAnzahl();
		for (final @NotNull GostBlockungKurs kNeu : kursmenge) {
			DeveloperNotificationException.ifInvalidID("pKurs.id", kNeu.id);
			DeveloperNotificationException.ifNull("manFaecher.get(pKurs.fach_id)", manFaecher.get(kNeu.fach_id));
			DeveloperNotificationException.ifNull("GostKursart.fromIDorNull(pKurs.kursart)", GostKursart.fromIDorNull(kNeu.kursart));
			DeveloperNotificationException.ifTrue("Kurs.wochenstunden %d < 0".formatted(kNeu.wochenstunden), kNeu.wochenstunden < 0);
			DeveloperNotificationException.ifTrue("Kurs.anzahlSchienen %d zu klein!".formatted(kNeu.anzahlSchienen), kNeu.anzahlSchienen < 1);
			DeveloperNotificationException.ifTrue("Kurs.anzahlSchienen %d zu groß!".formatted(kNeu.anzahlSchienen), kNeu.anzahlSchienen > nSchienen);
			DeveloperNotificationException.ifTrue("Kurs.nummer %d zu klein!".formatted(kNeu.nummer), kNeu.nummer < 1);
			DeveloperNotificationException.ifTrue("Kurs.id %d Doppelung!".formatted(kNeu.id), !setId.add(kNeu.id));
		}

		// Hinzufügen der Kurse.
		for (final @NotNull GostBlockungKurs gKurs : kursmenge) {
			kursAddKursOhneSortierung(gKurs);
		}

		// Sortieren der Kursmengen.
		kursmengeSortiertNachFachKursartKursnummer.sort(compKurs1fach2kursart3kursnummer);
		kursmengeSortiertNachKursartFachKursnummer.sort(compKurs1kursart2fach3kursnummer);
	}

	/**
	 * Liefert TRUE, falls der Kurs mit der übergebenen ID existiert.
	 *
	 * @param idKurs   die Datenbank-ID des Kurses
	 *
	 * @return true, falls der Kurs mit der übergebenen ID existiert
	 */
	public boolean kursGetExistiert(final long idKurs) {
		return kursById.get(idKurs) != null;
	}

	/**
	 * Liefert die Anzahl an Kursen.
	 *
	 * @return die Anzahl an Kursen
	 */
	public int kursGetAnzahl() {
		return kursById.size();
	}

	/**
	 * Liefert die Anzahl an Kursen, die keine KOOP-Kurse sind.
	 *
	 * @return die Anzahl an Kursen, die keine KOOP-Kurse sind
	 */
	public int kursGetAnzahlIntener() {
		int nKurse = 0;
		for (final @NotNull GostBlockungKurs k : kursById.values()) {
			if (!k.istKoopKurs) {
				nKurse++;
			}
		}
		return nKurse;
	}

	/**
	 * Liefert den Namen des Kurses der Form [Fach]-[Kursart][Kursnummer][-Suffix], beispielsweise D-GK1.
	 *
	 * @param idKurs   die Datenbank-ID des Kurses
	 *
	 * @return den Namen des Kurses der Form [Fach]-[Kursart][Kursnummer][-Suffix], beispielsweise D-GK1
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public @NotNull String kursGetName(final long idKurs) throws DeveloperNotificationException {
		final @NotNull GostBlockungKurs kurs = kursGet(idKurs);
		final @NotNull GostFach gFach = manFaecher.getOrException(kurs.fach_id);
		final @NotNull String sSuffix = "".equals(kurs.suffix) ? "" : ("-" + kurs.suffix);
		final @NotNull GostKursart kursart = GostKursart.fromID(kurs.kursart);
		return gFach.kuerzelAnzeige + "-" + kursart.kuerzel + kurs.nummer + sSuffix;
	}

	/**
	 * Liefert den Namen des Kurses der Form [Fach]-[Kursart][Kursnummer] ohne den potentiellen Suffix, beispielsweise D-GK1.
	 *
	 * @param idKurs   die Datenbank-ID des Kurses
	 *
	 * @return den Namen des Kurses der Form [Fach]-[Kursart][Kursnummer] ohne den potentiellen Suffix, beispielsweise D-GK1
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public @NotNull String kursGetNameOhneSuffix(final long idKurs) throws DeveloperNotificationException {
		final @NotNull GostBlockungKurs kurs = kursGet(idKurs);
		final @NotNull GostFach gFach = manFaecher.getOrException(kurs.fach_id);
		final @NotNull GostKursart kursart = GostKursart.fromID(kurs.kursart);
		return gFach.kuerzelAnzeige + "-" + kursart.kuerzel + kurs.nummer;
	}

	/**
	 * Liefert das {@link GostBlockungKurs}-Objekt mit der übergebenen ID.
	 *
	 * @param idKurs   die Datenbank-ID des Kurses
	 *
	 * @return das {@link GostBlockungKurs}-Objekt
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public @NotNull GostBlockungKurs kursGet(final long idKurs) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifMapGetIsNull(kursById, idKurs);
	}

	/**
	 * Liefert die Lehrkraft des Kurses, welche die angegebene Nummer hat. <br>
	 * Wirft eine Exception, falls es eine solche Lehrkraft nicht gibt.
	 *
	 * @param idKurs          die Datenbank-ID des Kurses
	 * @param reihenfolgeNr   die Lehrkraft mit der Nummer, die gesucht wird
	 *
	 * @return die Lehrkraft des Kurses mit der Nummer
	 * @throws DeveloperNotificationException Falls es eine solche Lehrkraft nicht gibt.
	 */
	public GostBlockungKursLehrer kursGetLehrkraftMitNummer(final long idKurs, final int reihenfolgeNr) throws DeveloperNotificationException {
		for (final @NotNull GostBlockungKursLehrer lehrkraft : kursGetLehrkraefteSortiert(idKurs)) {
			if (lehrkraft.reihenfolge == reihenfolgeNr) {
				return lehrkraft;
			}
		}
		throw new DeveloperNotificationException("Es gibt im Kurs " + toStringKurs(idKurs) + " keine Lehrkraft mit ReihenfolgeNr. " + reihenfolgeNr + "!");
	}

	/**
	 * Liefert die Lehrkraft des Kurses, welche die angegebene ID hat.
	 *
	 * @param idKurs        die Datenbank-ID des Kurses
	 * @param idLehrkraft   die Datenbank-ID der gesuchten Lehrkraft
	 *
	 * @return die Lehrkraft des Kurses mit der ID
	 * @throws DeveloperNotificationException Falls es eine solche Lehrkraft nicht gibt.
	 */
	public GostBlockungKursLehrer kursGetLehrkraftMitID(final long idKurs, final long idLehrkraft) throws DeveloperNotificationException {
		for (final @NotNull GostBlockungKursLehrer lehrkraft : kursGetLehrkraefteSortiert(idKurs)) {
			if (lehrkraft.id == idLehrkraft) {
				return lehrkraft;
			}
		}
		throw new DeveloperNotificationException("Es gibt im Kurs " + toStringKurs(idKurs) + " keine Lehrkraft mit ID " + idLehrkraft + "!");
	}

	/**
	 * Liefert TRUE, falls im Kurs die Lehrkraft mit der Nummer existiert.
	 *
	 * @param idKurs          die Datenbank-ID des Kurses
	 * @param reihenfolgeNr   die Lehrkraft mit der Nummer, die gesucht wird
	 *
	 * @return true, falls im Kurs die Lehrkraft mit der Nummer existiert
	 * @throws DeveloperNotificationException  Falls der Kurs nicht in der Blockung existiert.
	 */
	public boolean kursGetLehrkraftMitNummerExists(final long idKurs, final int reihenfolgeNr) throws DeveloperNotificationException {
		for (final @NotNull GostBlockungKursLehrer lehrkraft : kursGetLehrkraefteSortiert(idKurs)) {
			if (lehrkraft.reihenfolge == reihenfolgeNr) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Liefert TRUE, falls im Kurs die Lehrkraft mit der ID existiert.
	 *
	 * @param idKurs        die Datenbank-ID des Kurses
	 * @param idLehrkraft   die Datenbank-ID der gesuchten Lehrkraft
	 *
	 * @return true, falls im Kurs die Lehrkraft mit der ID existiert
	 */
	public boolean kursGetLehrkraftMitIDExists(final long idKurs, final long idLehrkraft) {
		for (final @NotNull GostBlockungKursLehrer lehrkraft : kursGetLehrkraefteSortiert(idKurs)) {
			if (lehrkraft.id == idLehrkraft) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Liefert alle Lehrkräfte eines Kurses sortiert nach {@link GostBlockungKursLehrer#reihenfolge}.
	 *
	 * @param idKurs   die Datenbank-ID des Kurses
	 *
	 * @return alle Lehrkräfte eines Kurses sortiert nach {@link GostBlockungKursLehrer#reihenfolge}
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public @NotNull List<GostBlockungKursLehrer> kursGetLehrkraefteSortiert(final long idKurs) throws DeveloperNotificationException {
		return kursGet(idKurs).lehrer;
	}

	/**
	 * Fügt die übergebene Lehrkraft zum Kurs hinzu.
	 *
	 * @param idKurs          die Datenbank-ID des Kurses
	 * @param neueLehrkraft   das {@link GostBlockungKursLehrer}-Objekt
	 *
	 * @throws DeveloperNotificationException falls der Kurs nicht existiert oder die Lehrkraft oder die ReihenfolgeNr bereits im Kurs existiert.
	 */
	public void kursAddLehrkraft(final long idKurs, final @NotNull GostBlockungKursLehrer neueLehrkraft) throws DeveloperNotificationException {
		// Datenkonsistenz überprüfen
		final @NotNull GostBlockungKurs kurs = kursGet(idKurs);
		final @NotNull List<GostBlockungKursLehrer> listOfLehrer = kurs.lehrer;
		for (final @NotNull GostBlockungKursLehrer lehrkraft : listOfLehrer) {
			DeveloperNotificationException.ifTrue(toStringKurs(idKurs) + " hat bereits " + toStringKursLehrkraft(idKurs, lehrkraft.id),
					lehrkraft.id == neueLehrkraft.id);
			DeveloperNotificationException.ifTrue(
					toStringKurs(idKurs) + " hat bereits " + toStringKursLehrkraft(idKurs, lehrkraft.id) + " mit Reihenfolge " + lehrkraft.reihenfolge,
					lehrkraft.reihenfolge == neueLehrkraft.reihenfolge);
		}
		// Hinzufügen
		listOfLehrer.add(neueLehrkraft);
		listOfLehrer.sort(compLehrkraefte);

		// Alle Ergebnisse revalidieren, damit die Bewertung aktuell ist.
		ergebnisAlleRevalidieren();
	}

	/**
	 * Löscht aus dem übergebenen Kurs die angegebene Lehrkraft.
	 *
	 * @param idKurs            die Datenbank-ID des Kurses
	 * @param idAlteLehrkraft   die Datenbank-ID des {@link GostBlockungKursLehrer}-Objekt
	 *
	 * @throws DeveloperNotificationException falls der Kurs nicht existiert oder es eine solche Lehrkraft im Kurs nicht gibt.
	 */
	public void kursRemoveLehrkraft(final long idKurs, final long idAlteLehrkraft) throws DeveloperNotificationException {
		final @NotNull GostBlockungKurs kurs = kursGet(idKurs);
		final @NotNull List<GostBlockungKursLehrer> listOfLehrer = kurs.lehrer;
		for (int i = 0; i < listOfLehrer.size(); i++) {
			if (listOfLehrer.get(i).id == idAlteLehrkraft) {
				listOfLehrer.remove(listOfLehrer.get(i));

				// Alle Ergebnisse revalidieren, damit die Bewertung aktuell ist.
				ergebnisAlleRevalidieren();
				return;
			}
		}
		throw new DeveloperNotificationException(toStringKurs(idKurs) + " enthält nicht " + toStringKursLehrkraft(idKurs, idAlteLehrkraft));
	}

	/**
	 * Liefert eine nach 'Fach, Kursart, Kursnummer' sortierte Kopie der Menge der Kurse.
	 *
	 * @return eine nach 'Fach, Kursart, Kursnummer' sortierte Liste der Kurse
	 */
	public @NotNull List<GostBlockungKurs> kursGetListeSortiertNachFachKursartNummer() {
		return kursmengeSortiertNachFachKursartKursnummer;
	}

	/**
	 * Liefert eine nach 'Kursart, Fach, Kursnummer' sortierte Kopie der Menge der Kurse.
	 *
	 * @return eine nach 'Kursart, Fach, Kursnummer' sortierte Liste der Kurse
	 */
	public @NotNull List<GostBlockungKurs> kursGetListeSortiertNachKursartFachNummer() {
		return kursmengeSortiertNachKursartFachKursnummer;
	}

	/**
	 * Liefert eine nach Kursnummer sortierte Liste der Kurse für das angegebene Fach und die angegebene Kursart.
	 *
	 * @param idFach      die ID des Fachs
	 * @param idKursart   die ID der Kursart
	 *
	 * @return die sortierte Liste der Kurse für das Fach und die Kursart
	 */
	public @NotNull List<GostBlockungKurs> kursGetListeByFachUndKursart(final long idFach, final int idKursart) {
		final List<GostBlockungKurs> liste = kursmengeByFachIdAndKursartId.getOrNull(idFach, idKursart);
		if (liste == null) {
			return new ArrayList<>();
		}
		liste.sort(compKursnummer);
		return liste;
	}

	/**
	 * Liefert TRUE, falls ein Löschen des Kurses erlaubt ist. <br>
	 * Kriterium: Der Kurs muss existieren und das aktuelle Ergebnis muss eine Vorlage sein.
	 *
	 * @param idKurs   die Datenbank-ID des Kurses
	 *
	 * @return true, falls ein Löschen des Kurses erlaubt ist
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public boolean kursGetIsRemoveAllowed(final long idKurs) throws DeveloperNotificationException {
		return (kursGet(idKurs).id == idKurs) && getIstBlockungsVorlage();
	}

	/**
	 * Liefert TRUE, falls der Kurs aufgrund von Regeln in der angegebenen Schiene verboten ist.
	 *
	 * @param idKurs      die Datenbank-ID des Kurses
	 * @param idSchiene   die Datenbank-ID der Schiene
	 *
	 * @return true, falls der Kurs in der angegebenen Schiene verboten ist
	 * @throws DeveloperNotificationException falls der Kurs oder die Schiene in der Blockung nicht existiert.
	 */
	public boolean kursGetIstVerbotenInSchiene(final long idKurs, final long idSchiene) throws DeveloperNotificationException {
		if (kursGetHatSperrungInSchiene(idKurs, idSchiene)) {
			return true;
		}

		final int nummer = schieneGet(idSchiene).nummer;
		final int kursart = kursGet(idKurs).kursart;

		return kursGetIstVerbotenInSchieneDurchTyp06(nummer, kursart)
				|| kursGetIstVerbotenInSchieneDurchTyp01(nummer, kursart);
	}

	private boolean kursGetIstVerbotenInSchieneDurchTyp06(final int nummer, final int kursart) {
		for (final @NotNull GostBlockungRegel regel : regelGetListeOfTyp(GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS)) {
			if ((nummer >= regel.parameter.get(1)) && (nummer <= regel.parameter.get(2))) {
				if (regel.parameter.get(0) != kursart) {
					return true;
				}
			} else {
				if (regel.parameter.get(0) == kursart) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean kursGetIstVerbotenInSchieneDurchTyp01(final int nummer, final int kursart) {
		for (final @NotNull GostBlockungRegel regel : regelGetListeOfTyp(GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS)) {
			if (((nummer >= regel.parameter.get(1)) && (nummer <= regel.parameter.get(2)))
					&& (regel.parameter.get(0) == kursart)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Liefert TRUE, falls der Kurs aufgrund der Regel {@link GostKursblockungRegelTyp#KURS_SPERRE_IN_SCHIENE} in der angegebenen Schiene gesperrt ist.
	 *
	 * @param idKurs      die Datenbank-ID des Kurses
	 * @param idSchiene   die Datenbank-ID der Schiene
	 *
	 * @return true, falls der Kurs aufgrund der Regel {@link GostKursblockungRegelTyp#KURS_SPERRE_IN_SCHIENE} in der angegebenen Schiene gesperrt ist
	 * @throws DeveloperNotificationException falls der Kurs oder die Schiene nicht existiert.
	 */
	public boolean kursGetHatSperrungInSchiene(final long idKurs, final long idSchiene) throws DeveloperNotificationException {
		final @NotNull GostBlockungKurs kurs = kursGet(idKurs);
		final @NotNull GostBlockungSchiene schiene = schieneGet(idSchiene);
		final @NotNull LongArrayKey key = new LongArrayKey(GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, kurs.id, schiene.nummer);
		return regelByMultikey.containsKey(key);
	}

	/**
	 * Liefert die Regel, welche den Kurs in einer Schiene gesperrt hat.
	 *
	 * @param idKurs      die Datenbank-ID des Kurses
	 * @param idSchiene   die Datenbank-ID der Schiene
	 *
	 * @return die Regel, die den Kurs in der Schiene sperrt
	 * @throws DeveloperNotificationException falls die Schiene oder die Regel nicht existiert.
	 */
	public @NotNull GostBlockungRegel kursGetRegelGesperrtInSchiene(final long idKurs, final long idSchiene) throws DeveloperNotificationException {
		final int nrSchiene = schieneGet(idSchiene).nummer;
		final @NotNull LongArrayKey key = new LongArrayKey(GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, nrSchiene);
		return DeveloperNotificationException.ifNull("" + toStringKurs(idKurs) + " ist nicht gesperrt in Schiene " + toStringSchiene(idSchiene) + "!",
				regelByMultikey.get(key));
	}

	/**
	 * Liefert TRUE, falls der Kurs aufgrund der Regel {@link GostKursblockungRegelTyp#KURS_FIXIERE_IN_SCHIENE} in der angegebenen Schiene fixiert ist.
	 *
	 * @param idKurs      die Datenbank-ID des Kurses
	 * @param idSchiene   die Datenbank-ID der Schiene
	 *
	 * @return true, falls der Kurs aufgrund der Regel {@link GostKursblockungRegelTyp#KURS_FIXIERE_IN_SCHIENE} in der angegebenen Schiene fixiert ist
	 * @throws DeveloperNotificationException falls die Schiene nicht existiert.
	 */
	public boolean kursGetHatFixierungInSchiene(final long idKurs, final long idSchiene) throws DeveloperNotificationException {
		final int nrSchiene = schieneGet(idSchiene).nummer;
		final @NotNull LongArrayKey key = new LongArrayKey(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nrSchiene);
		return regelByMultikey.containsKey(key);
	}

	/**
	 * Liefert die Regel, welche den Kurs in einer Schiene fixiert hat.
	 *
	 * @param idKurs      die Datenbank-ID des Kurses
	 * @param idSchiene   die Datenbank-ID der Schiene
	 *
	 * @return die Regel, die den Kurs in der Schiene fixiert
	 * @throws DeveloperNotificationException falls die Schiene oder die Regel nicht existiert.
	 */
	public @NotNull GostBlockungRegel kursGetRegelFixierungInSchiene(final long idKurs, final long idSchiene) throws DeveloperNotificationException {
		final int nrSchiene = schieneGet(idSchiene).nummer;
		final @NotNull LongArrayKey key = new LongArrayKey(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nrSchiene);
		return DeveloperNotificationException.ifNull(toStringKurs(idKurs) + " ist nicht fixiert in Schiene " + toStringSchiene(idSchiene) + "!",
				regelByMultikey.get(key));
	}

	/**
	 * Liefert TRUE, falls der Kurs nicht vollständig fixiert ist.
	 *
	 * @param idKurs   die Datenbank-ID des Kurses
	 *
	 * @return true, falls der Kurs nicht vollständig fixiert ist
	 * @throws DeveloperNotificationException falls der Kurs nicht existiert.
	 */
	public boolean kursIstWeitereFixierungErlaubt(final long idKurs) throws DeveloperNotificationException {
		final int anzahlSchienen = kursGet(idKurs).anzahlSchienen;

		int anzahlFixierungen = 0;
		for (int nr = 1; nr <= schieneGetAnzahl(); nr++) {
			final @NotNull LongArrayKey kFixierungAlt = new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nr });
			final GostBlockungRegel rFixierungAlt = regelGetByLongArrayKeyOrNull(kFixierungAlt);
			if (rFixierungAlt != null) {
				anzahlFixierungen++;
			}
		}

		return anzahlFixierungen < anzahlSchienen;
	}

	/**
	 * Liefert die Regel, welche die Anzahl der DummySuS eines Kurses definiert oder NULL.
	 *
	 * @param idKurs   die Datenbank-ID des Kurses
	 *
	 * @return die Regel, welche die Anzahl der DummySuS eines Kurses definiert oder NULL
	 */
	public GostBlockungRegel kursGetRegelDummySchuelerOrNull(final long idKurs) {
		for (final @NotNull GostBlockungRegel regel : regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN)) {
			if (regel.parameter.get(0) == idKurs) {
				return regel;
			}
		}
		return null;
	}

	/**
	 * Liefert ein Set aller Kurs-IDs.
	 *
	 * @return ein Set aller Kurs-IDs
	 */
	public @NotNull Set<Long> kursmengeGetSetDerIDs() {
		final @NotNull HashSet<Long> setKursID = new HashSet<>();
		for (final @NotNull GostBlockungKurs kurs : kursmengeSortiertNachFachKursartKursnummer) {
			setKursID.add(kurs.id);
		}
		return setKursID;
	}

	/**
	 * Entfernt alle Kurse mit den übergebenen IDs aus der Blockung.
	 * <br>(1) Überprüft, ob es eine Blockungsvorlage ist und ob alle IDs existieren, sonst Exception.
	 * <br>(2) Entfernt dann alle Kurse aus den Datenstrukturen.
	 * <br>(3) Entfernt dann alle Regeln, die einen der Kurse tangieren.
	 * <br>(4) Dann muss der Client den ErgebnisManager über die Löschung des Kurses informieren.
	 *
	 * @param idKurse   die Datenbank-IDs der zu entfernenden Kurse
	 *
	 * @throws DeveloperNotificationException Falls der Kurs nicht existiert oder es sich nicht um eine Blockungsvorlage handelt.
	 */
	public void kurseRemoveByID(final @NotNull Set<Long> idKurse) throws DeveloperNotificationException {
		// (1) Datenkonsistenz überprüfen.
		DeveloperNotificationException.ifTrue("Ein Löschen von Kursen ist nur bei einer Blockungsvorlage erlaubt!", !getIstBlockungsVorlage());
		for (final long idKurs : idKurse) {
			DeveloperNotificationException.ifTrue("Löschen von Kurs.id=" + idKurs + " nicht möglich, da nicht vorhanden!", !kursGetExistiert(idKurs));
		}

		// (2) Entfernen des Kurses.
		for (final long idKurs : idKurse) {
			final @NotNull GostBlockungKurs kurs = this.kursGet(idKurs);
			kursmengeSortiertNachFachKursartKursnummer.remove(kurs); // Neusortierung nicht nötig.
			kursmengeSortiertNachKursartFachKursnummer.remove(kurs); // Neusortierung nicht nötig.
			Map2DUtils.removeFromListAndTrimOrException(kursmengeByFachIdAndKursartId, kurs.fach_id, kurs.kursart, kurs);
			DeveloperNotificationException.ifMapRemoveFailes(kursById, idKurs);
			dtoDaten.kurse.remove(kurs);
		}

		// (3) Sammle alle Regeln, welche die Kurse enthalten und lösche sie.
		final @NotNull HashSet<Long> regelIDs = new HashSet<>();
		for (final @NotNull GostBlockungRegel regel : dtoDaten.regeln) {
			for (final long idKurs : idKurse) {
				if (regelGetHatKursIDs(regel, idKurs)) {
					regelIDs.add(regel.id);
					break;
				}
			}
		}
		regelRemoveListeByIDsOhneRevalidierung(regelIDs);

		// (4) Der Client informiert danach den EINEN ErgebnisManager, welcher dann alles revalidiert.
	}

	/**
	 * Entfernt den Kurs mit der übergebenen ID aus der Blockung.
	 *
	 * @param idKurs   die Datenbank-ID des zu entfernenden Kurses
	 *
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public void kursRemoveByID(final long idKurs) throws DeveloperNotificationException {
		kurseRemoveByID(SetUtils.create1(idKurs));
	}

	/**
	 * Entfernt den übergebenen Kurs aus der Blockung.
	 *
	 * @param kurs   der zu entfernende Kurs
	 *
	 * @throws DeveloperNotificationException falls der Kurs nicht existiert.
	 */
	public void kursRemove(final @NotNull GostBlockungKurs kurs) throws DeveloperNotificationException {
		kurseRemoveByID(SetUtils.create1(kurs.id));
	}

	/**
	 * Entfernt alle {@link GostBlockungKurs}-Objekte.
	 *
	 * @param kurse   die zu entfernenden {@link GostBlockungKurs}-Objekte
	 *
	 * @throws DeveloperNotificationException falls einer der Kurse nicht existiert oder es sich nicht um eine Blockungsvorlage handelt.
	 */
	public void kurseRemove(final @NotNull List<GostBlockungKurs> kurse) throws DeveloperNotificationException {
		// Kopieren der IDs.
		final @NotNull HashSet<Long> idKurse = new HashSet<>();
		for (final @NotNull GostBlockungKurs kursExtern : kurse) {
			idKurse.add(kursExtern.id);
		}

		// Delegieren an die andere Methode.
		kurseRemoveByID(idKurse);
	}

	/**
	 * Kombiniert zwei Kurse zu einem Kurs. Die Regel  {@link GostKursblockungRegelTyp#KURS_MIT_DUMMY_SUS_AUFFUELLEN}
	 * muss dabei ggf. auch kombiniert werden, wobei eine existierende Regel recycled wird.
	 *
	 * @param idKursID1keep     die Kurs-ID des Ziel-Kurses (wird nicht gelöscht)
	 * @param idKursID2delete   die Kurs-ID des Quell-Kurses (wird gelöscht)
	 * @throws DeveloperNotificationException falls es keine Blockungsvorlage ist, oder die Kurse nicht existieren, oder die Kurse identisch sind.
	 */
	public void kursMerge(final long idKursID1keep, final long idKursID2delete) throws DeveloperNotificationException {
		// (1) Datenkonsistenz überprüfen.
		DeveloperNotificationException.ifTrue("Die Kurse müssen sich unterscheiden!", idKursID1keep == idKursID2delete);
		DeveloperNotificationException.ifTrue("Ein Löschen des Kurses ist nur bei einer Blockungsvorlage erlaubt!", !getIstBlockungsVorlage());
		DeveloperNotificationException.ifTrue("Die ID=" + idKursID1keep + " des Ziel-Kurses gibt es nicht!", !kursById.containsKey(idKursID1keep));
		DeveloperNotificationException.ifTrue("Die ID=" + idKursID2delete + " des Quell-Kurses gibt es nicht!",
				!kursById.containsKey(idKursID2delete));

		// (2) Zunächst wird die Regel "KURS_MIT_DUMMY_SUS_AUFFUELLEN" angepasst.
		final GostBlockungRegel regelKursKeep = regelGetKursMitDummySusAuffuellen(idKursID1keep);
		final GostBlockungRegel regelKursDelete = regelGetKursMitDummySusAuffuellen(idKursID2delete);

		if (regelKursDelete != null) {
			if (regelKursKeep != null) {
				// Keep-Regel += Delete-Regel --> Delete-Regel wird in (3) gelöscht.
				final long summe = regelKursDelete.parameter.get(1) + regelKursKeep.parameter.get(1);
				regelRemove(regelKursKeep);
				regelKursKeep.parameter.set(1, summe);
				regelAdd(regelKursKeep);
			} else {
				// Delete-Regel wird zur Keep-Regel. (Löschen, Verändern, Hinzufügen)
				regelRemove(regelKursDelete);
				regelKursDelete.parameter.set(0, idKursID1keep);
				regelAdd(regelKursDelete);
			}
		}

		// (3) Dann wird erst der Kurs komplett gelöscht.
		kurseRemoveByID(SetUtils.create1(idKursID2delete));
	}

	/**
	 * Setzt den Suffix des Kurses.
	 *
	 * @param idKurs   die Datenbank-ID des Kurses
	 * @param suffix   der neue Suffix des Kurses
	 *
	 * @throws DeveloperNotificationException falls der Kurs nicht in der Blockung existiert.
	 */
	public void kursSetSuffix(final long idKurs, final @NotNull String suffix) throws DeveloperNotificationException {
		kursGet(idKurs).suffix = suffix;
	}

	// #########################################################################
	// ##########                Schiene-Anfragen                     ##########
	// #########################################################################

	/**
	 * Fügt die übergebene Schiene zu der Blockung hinzu.
	 * <br>: Wichtig: Beim Ergebnismanager müssen danach die Schienen auch hinzugefügt werden!
	 *
	 * @param schiene   die hinzuzufügende Schiene
	 * @throws DeveloperNotificationException Falls die Schienen-Daten inkonsistent sind.
	 */
	public void schieneAdd(final @NotNull GostBlockungSchiene schiene) throws DeveloperNotificationException {
		schieneAddListe(ListUtils.create1(schiene));
	}

	/**
	 * Fügt die Menge an Schienen hinzu.
	 * <br>: Wichtig: Beim Ergebnismanager müssen danach die Schienen auch hinzugefügt werden!
	 *
	 * @param schienenmenge   die Menge an Schienen
	 * @throws DeveloperNotificationException Falls die Schienen-Daten inkonsistent sind.
	 */
	public void schieneAddListe(final @NotNull List<GostBlockungSchiene> schienenmenge) throws DeveloperNotificationException {
		// Datenkonsistenz überprüfen.
		final @NotNull HashSet<Integer> setNr = new HashSet<>();
		final @NotNull HashSet<Long> setId = new HashSet<>();
		for (final @NotNull GostBlockungSchiene sAlt : dtoDaten.schienen) {
			setId.add(sAlt.id);
			setNr.add(sAlt.nummer);
		}
		for (final @NotNull GostBlockungSchiene sNeu : schienenmenge) {
			DeveloperNotificationException.ifInvalidID("Schiene.id", sNeu.id);
			DeveloperNotificationException.ifTrue("Schiene.bezeichnung darf nicht leer sein!", "".equals(sNeu.bezeichnung));
			DeveloperNotificationException.ifTrue("Schienen-Nr. " + sNeu.nummer + " < 1", sNeu.nummer < 1);
			DeveloperNotificationException.ifTrue("Schienen-WochenStd. " + sNeu.wochenstunden + " < 1", sNeu.wochenstunden < 1);
			DeveloperNotificationException.ifTrue("Schienen-ID-Doppelung " + sNeu.id, !setId.add(sNeu.id));
			DeveloperNotificationException.ifTrue("Schienen-Nr-Doppelung " + sNeu.nummer, !setNr.add(sNeu.nummer));
		}
		for (int nr = 1; nr <= dtoDaten.schienen.size() + schienenmenge.size(); nr++) {
			DeveloperNotificationException.ifTrue("Schienen-Nr. " + nr + " fehlt in der Reihenfolge!", !setNr.contains(nr));
		}

		// Hinzufügen der Schienen.
		for (final @NotNull GostBlockungSchiene schiene : schienenmenge) {
			schieneById.put(schiene.id, schiene);
			dtoDaten.schienen.add(schiene);
		}

		// Sortieren der Schienenmenge.
		dtoDaten.schienen.sort(compSchiene);
	}

	/**
	 * Gibt die Schiene der Blockung anhand von deren ID zurück.
	 *
	 * @param idSchiene   die Datenbank-ID der Schiene
	 *
	 * @return das zugehörige {@link GostBlockungSchiene}-Objekt
	 * @throws DeveloperNotificationException Falls die Schiene nicht in der Blockung existiert.
	 */
	public @NotNull GostBlockungSchiene schieneGet(final long idSchiene) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Schienen-Map.get(" + idSchiene + ")", schieneById.get(idSchiene));
	}

	/**
	 * Liefert TRUE, falls eine Schiene mit der übergebenen ID existiert.
	 *
	 * @param idSchiene   die Datenbank-ID der Schiene
	 *
	 * @return true, falls die Schiene existiert
	 */
	public boolean schieneGetExistiert(final long idSchiene) {
		return schieneById.get(idSchiene) != null;
	}

	/**
	 * Liefert die aktuelle Menge aller Schienen sortiert nach der Schienen-Nummer.
	 *
	 * @return die Menge aller Schienen sortiert nach der Nummer
	 */
	public @NotNull List<GostBlockungSchiene> schieneGetListe() {
		return new ArrayList<>(dtoDaten.schienen);
	}

	/**
	 * Liefert TRUE, falls ein Löschen der Schiene erlaubt ist.
	 *
	 * @param idSchiene   die Datenbank-ID der Schiene
	 *
	 * @return true, falls ein Löschen der Schiene erlaubt ist
	 * @throws DeveloperNotificationException Falls die ID der Schiene nicht existiert.
	 */
	public boolean schieneGetIsRemoveAllowed(final long idSchiene) throws DeveloperNotificationException {
		schieneGet(idSchiene);
		return getIstBlockungsVorlage();
	}

	/**
	 * Ändert das Attribut {@link GostBlockungSchiene#bezeichnung} der Schiene mit der jeweiligen ID.
	 *
	 * @param idSchiene     die Datenbank-ID der Schiene
	 * @param bezeichnung   die neue Bezeichnung
	 *
	 * @throws DeveloperNotificationException Falls die ID der Schiene nicht existiert.
	 */
	public void schienePatchBezeichnung(final long idSchiene, final @NotNull String bezeichnung) throws DeveloperNotificationException {
		schieneGet(idSchiene).bezeichnung = bezeichnung;
	}

	/**
	 * Ändert das Attribut {@link GostBlockungSchiene#wochenstunden} der Schiene mit der jeweiligen ID.
	 *
	 * @param idSchiene       die Datenbank-ID der Schiene
	 * @param wochenstunden   die neuen Wochenstunden
	 */
	public void schienePatchWochenstunden(final long idSchiene, final int wochenstunden) {
		schieneGet(idSchiene).wochenstunden = wochenstunden;
	}

	/**
	 * Entfernt die Schiene mit der übergebenen ID aus der Blockung.
	 * Konsequenz: <br>
	 * (1) Das Löschen der Schiene muss erlaubt sein und die Schiene muss existieren, sonst Exception. <br>
	 * (2) Die Schiene wird entfernt und Schienen mit größerer Nr. werden um 1 reduziert. <br>
	 * (3) Die Regeln müssen bei Schienen-Nummern angepasst werden. <br>
	 *
	 * @param idSchiene   die Datenbank-ID der zu entfernenden Schiene
	 *
	 * @throws DeveloperNotificationException Falls die Schiene nicht existiert oder ein Löschen nicht erlaubt ist.
	 */
	public void schieneRemoveByID(final long idSchiene) throws DeveloperNotificationException {
		// (1)
		DeveloperNotificationException.ifTrue("Ein Löschen einer Schiene ist nur bei einer Blockungsvorlage erlaubt!", !getIstBlockungsVorlage());
		final @NotNull GostBlockungSchiene schieneR = this.schieneGet(idSchiene);
		for (final @NotNull GostBlockungsergebnisManager eManager : ergebnisManagerByErgebnisId.values()) {
			DeveloperNotificationException.ifTrue("Schiene kann nicht gelöscht werden, da sie Kurse enthält!", !eManager.getOfSchieneIstLeer(idSchiene));
		}

		// (2)
		schieneById.remove(idSchiene);
		dtoDaten.schienen.remove(schieneR);
		for (final @NotNull GostBlockungSchiene schiene : dtoDaten.schienen) {
			if (schiene.nummer > schieneR.nummer) {
				schiene.nummer--;
			}
		}

		// (3) Wichtig: Die Ergebnisse dürfen nicht revalidiert werden, da sonst die Schiene aus (2) fehlt!
		final @NotNull Set<Long> setLoeschen = new HashSet<>();
		final @NotNull List<GostBlockungRegel> listHinzufuegen = new ArrayList<>();
		for (final @NotNull GostBlockungRegel r : dtoDaten.regeln) {
			final long[] a = GostKursblockungRegelTyp.getNeueParameterBeiSchienenLoeschung(r, schieneR.nummer);
			// Löschen?
			if (a == null) {
				setLoeschen.add(r.id);
				continue;
			}
			// Manipulation?
			if (DTOUtils.testRegelParameterChanged(r, a)) {
				setLoeschen.add(r.id);
				listHinzufuegen.add(r);
			}
		}
		regelRemoveListeByIDsOhneRevalidierung(setLoeschen);
		for (final @NotNull GostBlockungRegel r : listHinzufuegen) {
			final long[] a = GostKursblockungRegelTyp.getNeueParameterBeiSchienenLoeschung(r, schieneR.nummer);
			if (a != null) {
				for (int i = 0; i < a.length; i++) {
					r.parameter.set(i, a[i]);
				}
			}
		}
		regelAddListeOhneRevalidierung(listHinzufuegen);
	}

	/**
	 * Entfernt die übergebene Schiene aus der Blockung.
	 * <br>Hinweis: Es muss nicht dasselbe Objekt sein, nur die ID muss übereinstimmen.
	 *
	 * @param schiene   die zu entfernende Schiene
	 *
	 * @throws DeveloperNotificationException Falls die Schiene nicht existiert oder ein Löschen nicht erlaubt ist.
	 */
	public void schieneRemove(final @NotNull GostBlockungSchiene schiene) throws DeveloperNotificationException {
		schieneRemoveByID(schiene.id);
	}

	/**
	 * Liefert die Anzahl an Schienen.
	 *
	 * @return die Anzahl an Schienen
	 */
	public int schieneGetAnzahl() {
		return schieneById.size();
	}

	/**
	 * Liefert die Default-Anzahl an Schienen zurück, die für eine neue Blockung verwendet wird.
	 *
	 * @param halbjahr   das Halbjahr, für welches die Blockung angelegt werden soll
	 *
	 * @return die Default-Anzahl an Schienen für eine neue Blockung
	 */
	public static int schieneGetDefaultAnzahl(final @NotNull GostHalbjahr halbjahr) {
		return (halbjahr.id < 2) ? 13 : 11;
	}

	private void regelAddOhneSortierung(final @NotNull GostBlockungRegel regel) throws DeveloperNotificationException {
		// Regel in alle Datenstrukturen eintragen
		final @NotNull LongArrayKey multikey = GostBlockungsdatenManager.regelToMultikey(regel);
		final @NotNull GostKursblockungRegelTyp typ = GostKursblockungRegelTyp.fromTyp(regel.typ);
		DeveloperNotificationException.ifMapPutOverwrites(regelById, regel.id, regel);
		MapUtils.getOrCreateArrayList(regelmengeByRegeltyp, typ).add(regel);
		regelByMultikey.put(multikey, regel);
		dtoDaten.regeln.add(regel);
	}

	/**
	 * Fügt die übergebene Regel zu der Blockung hinzu.
	 *
	 * @param regel   die hinzuzufügende Regel
	 *
	 * @throws DeveloperNotificationException Falls die Daten der Regel inkonsistent sind.
	 */
	public void regelAdd(final @NotNull GostBlockungRegel regel) throws DeveloperNotificationException {
		regelAddListe(ListUtils.create1(regel));
	}

	/**
	 * Fügt eine Menge an Regeln hinzu.
	 *
	 * @param regelmenge   die Menge an Regeln
	 *
	 * @throws DeveloperNotificationException Falls die Daten der Regeln inkonsistent sind.
	 */
	public void regelAddListe(final @NotNull List<GostBlockungRegel> regelmenge) throws DeveloperNotificationException {
		regelAddListeOhneRevalidierung(regelmenge);

		// Alle Ergebnisse revalidieren, damit die Bewertung aktuell ist.
		ergebnisAlleRevalidieren();
	}

	private void regelAddListeOhneRevalidierung(final @NotNull List<GostBlockungRegel> regeln) throws DeveloperNotificationException {
		final @NotNull Set<Long> setIDs = new HashSet<>();
		final @NotNull Set<LongArrayKey> setMultiKey = new HashSet<>();
		final @NotNull Map<Integer, Set<GostBlockungRegel>> mengeByTyp = new HashMap<>();
		final @NotNull List<GostBlockungRegel> regelmengeOkay = new ArrayList<>();

		for (final @NotNull GostBlockungRegel r : regeln) {
			// Ungültige Regel-ID führt sofort zur Exception
			DeveloperNotificationException.ifTrue("%s hat eine ungültige ID %d".formatted(toStringRegel(r.id), r.id),
					r.id < 0);

			// Doppelte Regel-ID führt sofort zur Exception
			DeveloperNotificationException.ifTrue("%s Regel-ID %d Doppelung!".formatted(toStringRegel(r.id), r.id),
					regelById.containsKey(r.id) || regelUngueltigById.containsKey(r.id) || !setIDs.add(r.id));

			// Falsche Parameteranzahl führt sofort zur Exception
			final @NotNull GostKursblockungRegelTyp typ = GostKursblockungRegelTyp.fromTyp(r.typ);
			DeveloperNotificationException.ifTrue("%s hat falsche Parameter-Anzahl!".formatted(toStringRegel(r.id)),
					typ.getParamCount() != r.parameter.size());

			final @NotNull Set<GostBlockungRegel> menge = MapUtils.getOrCreateHashSet(mengeByTyp, r.typ);
			final @NotNull String warnung = regelGetWarnung(r, setMultiKey, menge);
			if (warnung.isEmpty()) {
				regelmengeOkay.add(r);
			} else {
				regelUngueltigById.put(r.id, r);
				regelUngueltigBeschreibungById.put(r.id, warnung);
			}
		}

		// Alle Regeln hinzufügen, die keine Fehler haben.
		for (final @NotNull GostBlockungRegel regel : regelmengeOkay) {
			regelAddOhneSortierung(regel);
		}

		// Sortierung aktualisieren.
		dtoDaten.regeln.sort(compRegel);

		// Sortierung (pro Typ) aktualisieren.
		for (final @NotNull List<GostBlockungRegel> listOfTyp : regelmengeByRegeltyp.values()) {
			listOfTyp.sort(compRegel);
		}
	}

	private @NotNull String regelGetWarnung(
			final @NotNull GostBlockungRegel r,
			final @NotNull Set<LongArrayKey> setMultiKey,
			final @NotNull Set<GostBlockungRegel> menge) {

		// Existiert bereits exakt die selbe Regel?
		final @NotNull LongArrayKey multikey = GostBlockungsdatenManager.regelToMultikey(r);
		if (regelByMultikey.containsKey(multikey) || !setMultiKey.add(multikey)) {
			return "%s existiert bereits als gleiche (nicht als selbe) Regel im MultiMap!".formatted(toStringRegel(r.id));
		}

		// Regeltypspezifische Prüfung
		final @NotNull GostKursblockungRegelTyp typ = GostKursblockungRegelTyp.fromTyp(r.typ);
		return switch (typ) {
			case KURSART_SPERRE_SCHIENEN_VON_BIS -> regelCheckTyp01(r);
			case KURS_FIXIERE_IN_SCHIENE, KURS_SPERRE_IN_SCHIENE -> regelCheckTyp02und03(r);
			case SCHUELER_FIXIEREN_IN_KURS, SCHUELER_VERBIETEN_IN_KURS -> regelCheckTyp04und05(r);
			case KURSART_ALLEIN_IN_SCHIENEN_VON_BIS -> regelCheckTyp06(r, menge);
			case KURS_VERBIETEN_MIT_KURS, KURS_ZUSAMMEN_MIT_KURS -> regelCheckTyp07und08(r);
			case KURS_MIT_DUMMY_SUS_AUFFUELLEN -> regelCheckTyp09(r, menge);
			case LEHRKRAEFTE_BEACHTEN -> regelCheckTyp10(r, menge);
			case SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH, SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH -> regelCheckTyp11und12(r);
			case SCHUELER_ZUSAMMEN_MIT_SCHUELER, SCHUELER_VERBIETEN_MIT_SCHUELER -> regelCheckTyp13und14(r);
			case KURS_MAXIMALE_SCHUELERANZAHL -> regelCheckTyp15(r, menge);
			case SCHUELER_IGNORIEREN -> regelCheckTyp16(r);
			case KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN -> regelCheckTyp17(r);
			case FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE -> regelCheckTyp18(r);
			default -> "%s Regeltypüberprüfung: Der Regeltyp ist unbekannt!".formatted(toStringRegel(r.id));
		};
	}

	private @NotNull String regelCheckTyp01(final @NotNull GostBlockungRegel r) {
		final @NotNull String wKursart = regelCheckReferenzKursart(r, 0);
		if (!wKursart.isEmpty()) {
			return wKursart;
		}
		final @NotNull String wSchiene1 = regelCheckReferenzSchienenNr(r, 1);
		if (!wSchiene1.isEmpty()) {
			return wSchiene1;
		}
		final @NotNull String wSchiene2 = regelCheckReferenzSchienenNr(r, 2);
		if (!wSchiene2.isEmpty()) {
			return wSchiene2;
		}
		final long bis = r.parameter.get(2);
		final long von = r.parameter.get(1);
		if (bis < von) {
			return "%s Die BIS-Schiene %d kann nicht kleiner sein als die VON-Schiene %d!".formatted(toStringRegel(r.id), bis, von);
		}
		return "";
	}

	private @NotNull String regelCheckTyp02und03(final @NotNull GostBlockungRegel r) {
		final @NotNull String wKurs0 = regelCheckReferenzKursID(r, 0);
		if (!wKurs0.isEmpty()) {
			return wKurs0;
		}
		final @NotNull String wSchiene1 = regelCheckReferenzSchienenNr(r, 1);
		if (!wSchiene1.isEmpty()) {
			return wSchiene1;
		}
		return "";
	}

	private @NotNull String regelCheckTyp04und05(final @NotNull GostBlockungRegel r) {
		final @NotNull String wSchueler0 = regelCheckReferenzSchuelerID(r, 0);
		if (!wSchueler0.isEmpty()) {
			return wSchueler0;
		}
		final @NotNull String wKurs1 = regelCheckReferenzKursID(r, 1);
		if (!wKurs1.isEmpty()) {
			return wKurs1;
		}
		return "";
	}

	private @NotNull String regelCheckTyp06(final @NotNull GostBlockungRegel r, final @NotNull Set<GostBlockungRegel> menge) {
		final @NotNull String wKursart0 = regelCheckReferenzKursart(r, 0);
		if (!wKursart0.isEmpty()) {
			return wKursart0;
		}
		final @NotNull String wSchiene1 = regelCheckReferenzSchienenNr(r, 1);
		if (!wSchiene1.isEmpty()) {
			return wSchiene1;
		}
		final @NotNull String wSchiene2 = regelCheckReferenzSchienenNr(r, 2);
		if (!wSchiene2.isEmpty()) {
			return wSchiene2;
		}
		final long bis = r.parameter.get(2);
		final long von = r.parameter.get(1);
		if (bis < von) {
			return "%s Die BIS-Schiene %d kann nicht kleiner sein als die VON-Schiene %d!".formatted(toStringRegel(r.id), bis, von);
		}
		// Duplikat-Check: nur einmal pro Kursart erlaubt
		// Prüfe gegen bereits bestehende Regeln dieses Typs (Cross-Call)
		final @NotNull List<GostBlockungRegel> bestehendeRegeln = MapUtils.getOrCreateArrayList(regelmengeByRegeltyp,
				GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS);
		for (final GostBlockungRegel rAlt : bestehendeRegeln) {
			if (rAlt.parameter.get(0).equals(r.parameter.get(0))) {
				return "Regel 6 - Cross-Call-Dopplung: %s".formatted(toStringRegel(r.id));
			}
		}
		// Prüfe gegen andere Regeln in der aktuellen Liste (Same-Call)
		for (final GostBlockungRegel rAlt : menge) {
			if (rAlt.parameter.get(0).equals(r.parameter.get(0))) {
				return "Regel 6 - Dopplung: %s".formatted(toStringRegel(r.id));
			}
		}
		menge.add(r);
		return "";
	}

	private @NotNull String regelCheckTyp07und08(final @NotNull GostBlockungRegel r) {
		final @NotNull String wKurs0 = regelCheckReferenzKursID(r, 0);
		if (!wKurs0.isEmpty()) {
			return wKurs0;
		}
		final @NotNull String wKurs1 = regelCheckReferenzKursID(r, 1);
		if (!wKurs1.isEmpty()) {
			return wKurs1;
		}
		return "";
	}

	private @NotNull String regelCheckTyp09(final @NotNull GostBlockungRegel r, final @NotNull Set<GostBlockungRegel> menge) {
		final @NotNull String wKurs0 = regelCheckReferenzKursID(r, 0);
		if (!wKurs0.isEmpty()) {
			return wKurs0;
		}
		final long anzahl = r.parameter.get(1);
		if (anzahl < GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN_MIN) {
			return "%s KURS_MIT_DUMMY_SUS_AUFFUELLEN ist mit %d zu klein!".formatted(toStringRegel(r.id), anzahl);
		}
		if (anzahl > GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN_MAX) {
			return "%s KURS_MIT_DUMMY_SUS_AUFFUELLEN ist mit %d zu groß!".formatted(toStringRegel(r.id), anzahl);
		}
		// Duplikat-Check: darf es nur ein Mal pro Kurs geben
		// Prüfe gegen bereits bestehende Regeln dieses Typs (Cross-Call)
		final @NotNull List<GostBlockungRegel> bestehendeRegeln =
				MapUtils.getOrCreateArrayList(regelmengeByRegeltyp, GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN);
		for (final GostBlockungRegel rAlt : bestehendeRegeln) {
			if (rAlt.parameter.get(0).equals(r.parameter.get(0))) {
				return "Regel 9 - Cross-Call-Dopplung: %s".formatted(toStringRegel(r.id));
			}
		}
		// Prüfe gegen andere Regeln in der aktuellen Liste (Same-Call)
		for (final GostBlockungRegel rNeu : menge) {
			if (rNeu.parameter.get(0).equals(r.parameter.get(0))) {
				return "Regel 9 - Dopplung: %s".formatted(toStringRegel(r.id));
			}
		}
		menge.add(r);
		return "";
	}

	private @NotNull String regelCheckTyp10(final @NotNull GostBlockungRegel r, final @NotNull Set<GostBlockungRegel> menge) {
		if (!menge.isEmpty()) {
			return "Regel 10 - Doppelung: %s".formatted(toStringRegel(r.id));
		}
		menge.add(r);
		return "";
	}

	private @NotNull String regelCheckTyp11und12(final @NotNull GostBlockungRegel r) {
		final @NotNull String wSchueler0 = regelCheckReferenzSchuelerID(r, 0);
		if (!wSchueler0.isEmpty()) {
			return wSchueler0;
		}
		final @NotNull String wSchueler1 = regelCheckReferenzSchuelerID(r, 1);
		if (!wSchueler1.isEmpty()) {
			return wSchueler1;
		}
		final @NotNull String wFach2 = regelCheckReferenzFachID(r, 2);
		if (!wFach2.isEmpty()) {
			return wFach2;
		}
		return "";
	}

	private @NotNull String regelCheckTyp13und14(final @NotNull GostBlockungRegel r) {
		final @NotNull String wSchueler0 = regelCheckReferenzSchuelerID(r, 0);
		if (!wSchueler0.isEmpty()) {
			return wSchueler0;
		}
		final @NotNull String wSchueler1 = regelCheckReferenzSchuelerID(r, 1);
		if (!wSchueler1.isEmpty()) {
			return wSchueler1;
		}
		return "";
	}

	private @NotNull String regelCheckTyp15(final @NotNull GostBlockungRegel r, final @NotNull Set<GostBlockungRegel> menge) {
		final @NotNull String wKurs0 = regelCheckReferenzKursID(r, 0);
		if (!wKurs0.isEmpty()) {
			return wKurs0;
		}
		final long anzahl1 = r.parameter.get(1);
		if (anzahl1 < GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL_MIN) {
			return "%s KURS_MAXIMALE_SCHUELERANZAHL ist mit %d zu klein!".formatted(toStringRegel(r.id), anzahl1);
		}
		if (anzahl1 > GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL_MAX) {
			return "%s KURS_MAXIMALE_SCHUELERANZAHL ist mit %d zu groß!".formatted(toStringRegel(r.id), anzahl1);
		}
		// Duplikat-Check: nur einmal pro Kurs erlaubt
		// Prüfe gegen bereits bestehende Regeln dieses Typs (Cross-Call)
		final @NotNull List<GostBlockungRegel> bestehendeRegeln = MapUtils.getOrCreateArrayList(regelmengeByRegeltyp,
				GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL);
		for (final GostBlockungRegel rAlt : bestehendeRegeln) {
			if (rAlt.parameter.get(0).equals(r.parameter.get(0))) {
				return "Regel 15 - Cross-Call-Dopplung: %s".formatted(toStringRegel(r.id));
			}
		}
		// Prüfe gegen andere Regeln in der aktuellen Liste (Same-Call)
		for (final GostBlockungRegel rAlt : menge) {
			if (rAlt.parameter.get(0).equals(r.parameter.get(0))) {
				return "Regel 15 - Dopplung: %s".formatted(toStringRegel(r.id));
			}
		}
		menge.add(r);
		return "";
	}

	private @NotNull String regelCheckTyp16(final @NotNull GostBlockungRegel r) {
		final @NotNull String wSchueler0 = regelCheckReferenzSchuelerID(r, 0);
		if (!wSchueler0.isEmpty()) {
			return wSchueler0;
		}
		return "";
	}

	private @NotNull String regelCheckTyp17(final @NotNull GostBlockungRegel r) {
		final @NotNull String wKurs0 = regelCheckReferenzKursID(r, 0);
		if (!wKurs0.isEmpty()) {
			return wKurs0;
		}
		return "";
	}

	private @NotNull String regelCheckTyp18(final @NotNull GostBlockungRegel r) {
		final @NotNull String wFach0 = regelCheckReferenzFachID(r, 0);
		if (!wFach0.isEmpty()) {
			return wFach0;
		}
		final @NotNull String wKursart1 = regelCheckReferenzKursart(r, 1);
		if (!wKursart1.isEmpty()) {
			return wKursart1;
		}
		final long anzahl2 = r.parameter.get(2);
		if (anzahl2 < GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE_MIN) {
			return "%s FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE ist mit %d zu klein!".formatted(toStringRegel(r.id), anzahl2);
		}
		if (anzahl2 > GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE_MAX) {
			return "%s FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE ist mit %d zu groß!".formatted(toStringRegel(r.id), anzahl2);
		}
		return "";
	}

	private @NotNull String regelCheckReferenzSchuelerID(final @NotNull GostBlockungRegel r, final int index) {
		final long idSchueler = r.parameter.get(index);
		if (schuelerGetOrNull(idSchueler) == null) {
			return "%s hat falsche Schüler-ID-Referenz %d!".formatted(toStringRegel(r.id), idSchueler);
		}
		return "";
	}

	private @NotNull String regelCheckReferenzKursID(final @NotNull GostBlockungRegel r, final int index) {
		final long idKurs = r.parameter.get(index);
		if (!kursGetExistiert(idKurs)) {
			return "%s hat falsche Kurs-ID-Referenz %d!".formatted(toStringRegel(r.id), idKurs);
		}
		return "";
	}

	private @NotNull String regelCheckReferenzSchienenNr(final @NotNull GostBlockungRegel r, final int index) {
		final long nrSchiene = r.parameter.get(index);
		if ((nrSchiene < 1) || (nrSchiene > schieneGetAnzahl())) {
			return "%s hat falsche Schienen-Nr-Referenz %d!".formatted(toStringRegel(r.id), nrSchiene);
		}
		return "";
	}

	private @NotNull String regelCheckReferenzFachID(final @NotNull GostBlockungRegel r, final int index) {
		final long idFach = r.parameter.get(index);
		if (manFaecher.get(idFach) == null) {
			return "%s hat falsche Fach-ID-Referenz %d!".formatted(toStringRegel(r.id), idFach);
		}
		return "";
	}

	private @NotNull String regelCheckReferenzKursart(final @NotNull GostBlockungRegel r, final int index) {
		final long idKursart = r.parameter.get(index);
		if (GostKursart.fromIDorNull((int) idKursart) == null) {
			return "%s hat falsche Kursart-Referenz %d!".formatted(toStringRegel(r.id), idKursart);
		}
		return "";
	}


	/**
	 * Liefert die Anzahl an Regeln.
	 *
	 * @return die Anzahl an Regeln
	 */
	public int regelGetAnzahl() {
		return regelById.size();
	}

	/**
	 * Liefert die Regel mit der übergebenen ID zurück.
	 *
	 * @param idRegel   die Datenbank-ID der Regel
	 *
	 * @return die Regel mit der übergebenen ID
	 * @throws DeveloperNotificationException Falls die Regel nicht existiert.
	 */
	public @NotNull GostBlockungRegel regelGet(final long idRegel) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Regel-Map.get(" + idRegel + ")", regelById.get(idRegel));
	}

	/**
	 * Liefert die {@link GostBlockungRegel} anhand des {@link LongArrayKey}-Schlüssels, oder NULL falls keine existiert.
	 *
	 * @param key   der {@link LongArrayKey}-Schlüssel
	 *
	 * @return die {@link GostBlockungRegel} zum {@link LongArrayKey}-Schlüssel, oder null
	 */
	public GostBlockungRegel regelGetByLongArrayKeyOrNull(final @NotNull LongArrayKey key) {
		return regelByMultikey.get(key);
	}

	/**
	 * Liefert die aktuelle Menge aller Regeln.
	 * Das ist die interne Referenz zur Liste der Regeln im {@link GostBlockungsdaten}-Objekt.
	 * Diese Liste ist stets sortiert nach (TYP, ID).
	 *
	 * @return die Menge aller Regeln sortiert nach (TYP, id)
	 */
	public @NotNull List<GostBlockungRegel> regelGetListe() {
		return dtoDaten.regeln;
	}

	/**
	 * Liefert die aktuelle Menge aller Regeln eines bestimmten {@link GostKursblockungRegelTyp}.
	 *
	 * @param typ   der {@link GostKursblockungRegelTyp}
	 *
	 * @return die Menge aller Regeln eines {@link GostKursblockungRegelTyp}
	 */
	public @NotNull List<GostBlockungRegel> regelGetListeOfTyp(final @NotNull GostKursblockungRegelTyp typ) {
		return MapUtils.getOrCreateArrayList(regelmengeByRegeltyp, typ);
	}

	/**
	 * Liefert die Regel, welche den Kurs in einer Schiene sperrt, oder die Dummy-Regel (ID negativ), falls die Regel nicht existiert.
	 *
	 * @param idKurs      die Datenbank-ID des Kurses
	 * @param nrSchiene   die Nummer der Schiene
	 *
	 * @return die Regel, welche den Kurs in einer Schiene sperrt, oder die Dummy-Regel (ID negativ), falls die Regel nicht existiert
	 */
	public @NotNull GostBlockungRegel regelGetRegelOrDummyKursGesperrtInSchiene(final long idKurs, final int nrSchiene) {
		final @NotNull LongArrayKey key = new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, nrSchiene });

		final GostBlockungRegel regel = regelByMultikey.get(key);
		if (regel != null) {
			return regel;
		}

		return DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, nrSchiene);
	}

	/**
	 * Liefert die Regel, welche den Kurs in einer Schiene fixiert, oder die Dummy-Regel (ID negativ), falls die Regel nicht existiert.
	 *
	 * @param idKurs      die Datenbank-ID des Kurses
	 * @param nrSchiene   die Nummer der Schiene
	 *
	 * @return die Regel, welche den Kurs in einer Schiene fixiert, oder die Dummy-Regel (ID negativ), falls die Regel nicht existiert
	 */
	public @NotNull GostBlockungRegel regelGetRegelOrDummyKursFixierungInSchiene(final long idKurs, final int nrSchiene) {
		final @NotNull LongArrayKey key = new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nrSchiene });

		final GostBlockungRegel regel = regelByMultikey.get(key);
		if (regel != null) {
			return regel;
		}

		return DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nrSchiene);
	}

	/**
	 * Liefert die Regel, welche den Schüler in einem Kurs fixiert, oder die Dummy-Regel (ID negativ), falls die Regel nicht existiert.
	 *
	 * @param idSchueler   die Datenbank-ID des Schülers
	 * @param idKurs       die Datenbank-ID des Kurses
	 *
	 * @return die Regel, welche den Schüler in einem Kurs fixiert, oder die Dummy-Regel (ID negativ), falls die Regel nicht existiert
	 */
	public @NotNull GostBlockungRegel regelGetRegelOrDummySchuelerInKursFixierung(final long idSchueler, final long idKurs) {
		final @NotNull LongArrayKey key = new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs });

		final GostBlockungRegel regel = regelByMultikey.get(key);
		if (regel != null) {
			return regel;
		}

		return DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs);
	}

	/**
	 * Liefert TRUE, falls die Regel mit der übergebenen ID existiert.
	 *
	 * @param idRegel   die Datenbank-ID der Regel
	 *
	 * @return true, falls die Regel mit der übergebenen ID existiert
	 */
	public boolean regelGetExistiert(final long idRegel) {
		return regelById.get(idRegel) != null;
	}

	/**
	 * Liefert TRUE, falls ein Löschen der Regel erlaubt ist.
	 * <br> Hinweis: Die alte Implementierung verlangte noch, dass es sich um eine Blockungsvorlage handelt,
	 *               nun reicht es, dass die Regel existiert.
	 *
	 * @param idRegel   die Datenbank-ID der Regel
	 *
	 * @return true, falls ein Löschen der Regel erlaubt ist
	 */
	public boolean regelGetIsRemoveAllowed(final long idRegel) {
		return regelById.containsKey(idRegel);
	}

	private GostBlockungRegel regelGetKursMitDummySusAuffuellen(final long idKurs) {
		for (final @NotNull GostBlockungRegel r : regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN)) {
			if (r.parameter.get(0) == idKurs) {
				return r;
			}
		}

		return null;
	}

	/**
	 * Liefert TRUE, falls der übergebene Kurs in der übergebenen Regeln enthalten ist.
	 *
	 * @param regel    das {@link GostBlockungRegel}-Objekt
	 * @param idKurs   die Datenbank-ID des Kurses
	 *
	 * @return true, falls der Kurs in der Regel enthalten ist
	 */
	private static boolean regelGetHatKursIDs(final @NotNull GostBlockungRegel regel, final long idKurs) {
		final @NotNull GostKursblockungRegelTyp regelTyp = GostKursblockungRegelTyp.fromTyp(regel.typ);
		for (int i = 0; i < regelTyp.getParamCount(); i++) {
			if ((regelTyp.getParamType(i) == GostKursblockungRegelParameterTyp.KURS_ID) && (regel.parameter.get(i) == idKurs)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Liefert alle Regeln, die aufgrund von Fehlern ungültig sind.
	 *
	 * @return alle Regeln, die aufgrund von Fehlern ungültig sind
	 */
	public @NotNull Map<Long, GostBlockungRegel> regelGetMapUngueltig() {
		return regelUngueltigById;
	}

	/**
	 * Liefert die Beschreibung der jeweiligen ungültigen Regeln.
	 *
	 * @return die Beschreibung der jeweiligen ungültigen Regeln
	 */
	public @NotNull Map<Long, String> regelGetMapUngueltigBeschreibung() {
		return regelUngueltigBeschreibungById;
	}

	/**
	 * Entfernt die Regel mit der übergebenen ID aus der Blockung.
	 *
	 * @param idRegel   die Datenbank-ID der zu entfernenden Regel
	 *
	 * @throws DeveloperNotificationException Falls die Regel nicht existiert.
	 */
	public void regelRemoveByID(final long idRegel) throws DeveloperNotificationException {
		regelRemoveListeByIDs(SetUtils.create1(idRegel));
	}

	/**
	 * Entfernt eine Menge von Regeln.
	 *
	 * @param regelmenge   die Menge an Regeln, die entfernt werden soll
	 *
	 * @throws DeveloperNotificationException Falls die Daten der Regeln inkonsistent sind.
	 */
	public void regelRemoveListe(final @NotNull List<GostBlockungRegel> regelmenge) throws DeveloperNotificationException {
		// IDs im Set sammeln.
		final @NotNull HashSet<Long> setRegelIDs = new HashSet<>();
		for (final @NotNull GostBlockungRegel regel : regelmenge) {
			setRegelIDs.add(regel.id);
		}

		// Alle Regeln löschen.
		regelRemoveListeByIDs(setRegelIDs);
	}

	private void regelRemoveListeByIDsOhneRevalidierung(final @NotNull Set<Long> regelmengeGesamt) throws DeveloperNotificationException {
		// Trenne die Regel-Menge in "ungültig" und "gültig".
		final ArrayList<Long> regelnUngueltig = new ArrayList<>();
		final ArrayList<Long> regelnGueltig = new ArrayList<>();
		for (final long idRegel : regelmengeGesamt) {
			if (regelUngueltigById.containsKey(idRegel)) {
				regelnUngueltig.add(idRegel);
			} else {
				regelnGueltig.add(idRegel);
			}
		}

		// A) Lösche die gültigen Regeln.
		if (!regelnGueltig.isEmpty()) {
			// Überprüfen
			for (final long idRegel : regelnGueltig) {
				final @NotNull GostBlockungRegel regel = regelGet(idRegel);
				final @NotNull GostKursblockungRegelTyp typ = GostKursblockungRegelTyp.fromTyp(regel.typ);
				DeveloperNotificationException.ifTrue("Der Regeltyp ist undefiniert!", typ == GostKursblockungRegelTyp.UNDEFINIERT);
				DeveloperNotificationException.ifTrue("Die Multi-Map enthält die Regel nicht!", !regelByMultikey.containsKey(regelToMultikey(regel)));
			}

			// Löschen
			for (final long idRegel : regelnGueltig) {
				final @NotNull GostBlockungRegel regel = this.regelGet(idRegel);
				final @NotNull GostKursblockungRegelTyp typ = GostKursblockungRegelTyp.fromTyp(regel.typ);
				final @NotNull LongArrayKey multikey = GostBlockungsdatenManager.regelToMultikey(regel);

				// Löschen aus den Datenstrukturen
				regelById.remove(idRegel);
				MapUtils.getOrCreateArrayList(regelmengeByRegeltyp, typ).remove(regel);
				regelByMultikey.remove(multikey);
				dtoDaten.regeln.remove(regel);
			}
		}

		// B) Lösche die ungültigen Regeln.
		if (!regelnUngueltig.isEmpty()) {
			for (final long idRegel : regelnUngueltig) {
				regelUngueltigById.remove(idRegel);
				regelUngueltigBeschreibungById.remove(idRegel);
			}
		}

	}

	/**
	 * Löscht eine Menge an Regeln anhand ihrer IDs.
	 *
	 * @param regelmenge   die Menge der IDs der Regeln
	 *
	 * @throws DeveloperNotificationException falls mindestens eine Regel nicht existiert.
	 */
	public void regelRemoveListeByIDs(final @NotNull Set<Long> regelmenge) throws DeveloperNotificationException {
		// Delegieren.
		regelRemoveListeByIDsOhneRevalidierung(regelmenge);

		// Alle Ergebnisse revalidieren, damit die Bewertung aktuell ist.
		ergebnisAlleRevalidieren();
	}

	private static @NotNull LongArrayKey regelToMultikey(final @NotNull GostBlockungRegel regel) {
		final long[] a = new long[regel.parameter.size() + 1];
		a[0] = regel.typ;
		for (int i = 1; i < a.length; i++) {
			a[i] = regel.parameter.get(i - 1);
		}
		return new LongArrayKey(a);
	}

	/**
	 * Entfernt die übergebene Regel aus der Blockung.
	 *
	 * @param regel   die zu entfernende Regel
	 *
	 * @throws DeveloperNotificationException Falls die Regel nicht existiert.
	 */
	public void regelRemove(final @NotNull GostBlockungRegel regel) throws DeveloperNotificationException {
		regelRemoveListeByIDs(SetUtils.create1(regel.id));
	}

	/**
	 * Liefert die Menge aller Kursarten des Faches, welche in Kursen oder Fachwahlen vorkommen.
	 *
	 * @param idFach   die Datenbank-ID des Faches
	 *
	 * @return die Menge aller Kursarten des Faches, welche in Kursen oder Fachwahlen vorkommen
	 */
	public @NotNull List<GostKursart> fachGetMengeKursarten(final long idFach) {
		final @NotNull HashSet<Integer> idKursarten = new HashSet<>();

		if (kursmengeByFachIdAndKursartId.containsKey1(idFach)) {
			idKursarten.addAll(kursmengeByFachIdAndKursartId.getKeySetOf(idFach));
		}

		if (fachwahlmengeByFachIdAndKursartId.containsKey1(idFach)) {
			idKursarten.addAll(fachwahlmengeByFachIdAndKursartId.getKeySetOf(idFach));
		}

		final @NotNull List<GostKursart> list = new ArrayList<>();
		for (final @NotNull GostKursart kursart : GostKursart.values()) {
			if (idKursarten.contains(kursart.id)) {
				list.add(kursart);
			}
		}

		return list;
	}

	/**
	 * Fügt eine Fachwahl hinzu.
	 *
	 * @param fachwahl   die Fachwahl, die hinzugefügt wird
	 *
	 * @throws DeveloperNotificationException Falls die Fachwahl-Daten inkonsistent sind.
	 */
	public void fachwahlAdd(final @NotNull GostFachwahl fachwahl) throws DeveloperNotificationException {
		fachwahlAddListe(ListUtils.create1(fachwahl));
	}

	/**
	 * Fügt alle Fachwahlen hinzu.
	 *
	 * @param fachwahlmenge   die Menge an Fachwahlen
	 *
	 * @throws DeveloperNotificationException Falls die Fachwahl-Daten inkonsistent sind.
	 */
	public void fachwahlAddListe(final @NotNull List<GostFachwahl> fachwahlmenge) throws DeveloperNotificationException {
		// Datenkonsistenz überprüfen
		final @NotNull Set<LongArrayKey> setSchuelerFach = new HashSet<>();
		for (final @NotNull GostFachwahl fNeu : fachwahlmenge) {
			GostKursart.fromFachwahlOrException(fNeu);
			DeveloperNotificationException.ifTrue("Fachwahl verweist auf ungültiges Fach " + fNeu.fachID, manFaecher.get(fNeu.fachID) == null);
			DeveloperNotificationException.ifTrue("Fachwahl Duplikat!", fachwahlBySchuelerIdAndFachId.contains(fNeu.schuelerID, fNeu.fachID));
			DeveloperNotificationException.ifTrue("Fachwahl Duplikat!", !setSchuelerFach.add(new LongArrayKey(fNeu.schuelerID, fNeu.fachID)));
		}

		// Hinzufügen
		for (final @NotNull GostFachwahl fNeu : fachwahlmenge) {
			// Fachwahl in 2D-Schüler-Fach-Map ablegen
			DeveloperNotificationException.ifMap2DPutOverwrites(fachwahlBySchuelerIdAndFachId, fNeu.schuelerID, fNeu.fachID, fNeu);

			// Fachwahl in Schüler-Liste ablegen
			final @NotNull List<GostFachwahl> fachwahlenDesSchuelers = MapUtils.getOrCreateArrayList(fachwahlmengeBySchuelerId, fNeu.schuelerID);
			fachwahlenDesSchuelers.add(fNeu);
			fachwahlenDesSchuelers.sort(compFachwahlen);

			// Fachwahl in Fachart-Liste ablegen
			final long fachartID = GostKursart.getFachartIDByFachwahl(fNeu);
			fachwahlGetListeOfFachart(fachartID).add(fNeu);

			// Fachwahl in Fach-Kursart-Map ablegen
			Map2DUtils.getOrCreateArrayList(fachwahlmengeByFachIdAndKursartId, fNeu.fachID, fNeu.kursartID).add(fNeu);

			// Fachwahl in Gesamtliste ablegen
			dtoDaten.fachwahlen.add(fNeu);
		}

		dtoDaten.fachwahlen.sort(compFachwahlen);
	}

	/**
	 * Liefert die Anzahl an Fachwahlen.
	 *
	 * @return die Anzahl an Fachwahlen
	 */
	public int fachwahlGetAnzahl() {
		return dtoDaten.fachwahlen.size();
	}

	/**
	 * Liefert den Namen der Fachwahl (Fach-Kursart), beispielsweise 'M-GK'.
	 * <br> Die Information über den Schüler dieser Fachwahl wird nicht dargestellt.
	 *
	 * @param fachwahl   das Fachwahl-Objekt
	 *
	 * @return den Namen der Fachwahl (Fach-Kursart), beispielsweise 'M-GK'
	 * @throws DeveloperNotificationException falls die Fach-Referenz oder die Kursart-Referenz nicht existiert.
	 */
	public @NotNull String fachwahlGetName(final @NotNull GostFachwahl fachwahl) throws DeveloperNotificationException {
		final @NotNull GostFach gFach = manFaecher.getOrException(fachwahl.fachID);
		final @NotNull GostKursart gKursart = GostKursart.fromID(fachwahl.kursartID);
		return gFach.kuerzelAnzeige + "-" + gKursart.kuerzel;
	}

	/**
	 * Liefert die sortierte Menge aller {@link GostFachwahl} einer bestimmten Fachart-ID.
	 * <br> Die Fachart-ID lässt sich mit {@link GostKursart#getFachartID} berechnen.
	 *
	 * @param idFachart   die Fachart-ID berechnet aus Fach-ID und Kursart-ID
	 *
	 * @return die sortierte Menge aller {@link GostFachwahl} einer bestimmten Fachart-ID
	 */
	public @NotNull List<GostFachwahl> fachwahlGetListeOfFachart(final long idFachart) {
		final @NotNull List<GostFachwahl> list = MapUtils.getOrCreateArrayList(fachwahlmengeByFachartId, idFachart);
		list.sort(compFachwahlen);
		return list;
	}

	/**
	 * Liefert die Anzahl verschiedenen Kursarten.
	 *
	 * @return die Anzahl verschiedener Kursarten
	 */
	public int fachwahlGetAnzahlVerwendeterKursarten() {
		final @NotNull HashSet<Integer> setKursartenIDs = new HashSet<>();
		for (final @NotNull GostFachwahl fachwahl : dtoDaten.fachwahlen) {
			setKursartenIDs.add(fachwahl.kursartID);
		}
		return setKursartenIDs.size();
	}

	/**
	 * Fügt einen Schüler hinzu.<br>
	 * Wirft eine Exception, falls die Schüler Daten inkonsistent sind.
	 *
	 * @param schueler   der Schüler, der hinzugefügt wird
	 *
	 * @throws DeveloperNotificationException Falls die Schüler Daten inkonsistent sind.
	 */
	private void schuelerAddOhneSortierung(final @NotNull Schueler schueler) throws DeveloperNotificationException {
		// Schüler in ID-Map ablegen
		DeveloperNotificationException.ifMapPutOverwrites(schuelerById, schueler.id, schueler);

		// Leere Fachwahlen-Liste für Schüler anlegen
		if (!fachwahlmengeBySchuelerId.containsKey(schueler.id)) {
			fachwahlmengeBySchuelerId.put(schueler.id, new ArrayList<>());
		}

		// 2D-Schüler-Fach-Pfad muss nicht erzeugt werden

		// Schüler in Gesamtliste ablegen
		dtoDaten.schueler.add(schueler);
	}

	/**
	 * Fügt einen Schüler hinzu.
	 *
	 * @param schueler   der Schüler, der hinzugefügt wird
	 *
	 * @throws DeveloperNotificationException Falls die Schüler-Daten inkonsistent sind.
	 */
	public void schuelerAdd(final @NotNull Schueler schueler) throws DeveloperNotificationException {
		schuelerAddListe(ListUtils.create1(schueler));
	}

	/**
	 * Fügt alle Schüler hinzu.
	 *
	 * @param schuelermenge   die Menge an Schülern
	 *
	 * @throws DeveloperNotificationException Falls die Schüler-Daten inkonsistent sind.
	 */
	public void schuelerAddListe(final @NotNull List<Schueler> schuelermenge) throws DeveloperNotificationException {
		// Datenkonsistenz überprüfen.
		final @NotNull HashSet<Long> setId = new HashSet<>();
		for (final @NotNull Schueler sAlt : dtoDaten.schueler) {
			setId.add(sAlt.id);
		}
		for (final @NotNull Schueler sNeu : schuelermenge) {
			DeveloperNotificationException.ifInvalidID("schueler.id", sNeu.id);
			DeveloperNotificationException.ifNull("schueler.geschlecht", Geschlecht.fromValue(sNeu.geschlecht));
			DeveloperNotificationException.ifNull("schueler.status", SchuelerStatus.data().getWertByID((long) sNeu.status));
			DeveloperNotificationException.ifTrue("schueler.id %d Doppelung!".formatted(sNeu.id), !setId.add(sNeu.id));
		}

		// hinzufügen
		for (final @NotNull Schueler schueler : schuelermenge) {
			schuelerAddOhneSortierung(schueler);
		}

		// sortieren
		dtoDaten.schueler.sort(compSchueler);
	}

	/**
	 * Liefert die Anzahl an Schülern, die mindestens eine Fachwahl haben.
	 *
	 * @return die Anzahl an Schülern, die mindestens eine Fachwahl haben
	 */
	public int schuelerGetAnzahlMitMindestensEinerFachwahl() {
		final HashSet<Long> setSchuelerIDs = new HashSet<>();
		for (final @NotNull GostFachwahl fachwahl : dtoDaten.fachwahlen) {
			setSchuelerIDs.add(fachwahl.schuelerID);
		}
		return setSchuelerIDs.size();
	}

	/**
	 * Liefert die Anzahl an Schülern.
	 *
	 * @return die Anzahl an Schülern
	 */
	public int schuelerGetAnzahl() {
		return dtoDaten.schueler.size();
	}

	/**
	 * Ermittelt den Schüler für die angegebene ID.
	 *
	 * @param idSchueler   die Datenbank-ID des Schülers
	 *
	 * @return das zugehörige {@link Schueler}-Objekt
	 * @throws DeveloperNotificationException  Falls die Schüler-ID unbekannt ist.
	 */
	public @NotNull Schueler schuelerGet(final long idSchueler) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Schüler-Map.get(" + idSchueler + ")", schuelerById.get(idSchueler));
	}

	/**
	 * Ermittelt den Schüler für die angegebene ID. <br>
	 * Gibt null zurück, falls die Schüler-ID unbekannt ist.
	 *
	 * @param idSchueler   die Datenbank-ID des Schülers
	 *
	 * @return das zugehörige {@link Schueler}-Objekt oder null
	 */
	public Schueler schuelerGetOrNull(final long idSchueler) {
		return schuelerById.get(idSchueler);
	}

	/**
	 * Liefert die aktuelle Menge aller Schüler.
	 * Das ist die interne Referenz zur Liste der Schüler im {@link GostBlockungsdaten}-Objekt.
	 *
	 * @return die Menge aller Schüler
	 */
	public @NotNull List<Schueler> schuelerGetListe() {
		return dtoDaten.schueler;
	}

	/**
	 * Liefert zum Tupel (Schüler, Fach) die jeweilige Kursart.
	 *
	 * @param idSchueler   die Datenbank-ID des Schülers
	 * @param idFach       die Datenbank-ID des Faches
	 *
	 * @return zum Tupel (Schüler, Fach) jeweilige {@link GostKursart}
	 * @throws DeveloperNotificationException falls der Schüler das Fach nicht gewählt hat.
	 */
	public @NotNull GostKursart schuelerGetOfFachKursart(final long idSchueler, final long idFach) throws DeveloperNotificationException {
		final @NotNull GostFachwahl fachwahl = schuelerGetOfFachFachwahl(idSchueler, idFach);
		return GostKursart.fromID(fachwahl.kursartID);
	}

	/**
	 * Liefert zum Tupel (Schüler, Fach) die jeweilige Fachwahl.
	 *
	 * @param idSchueler   die Datenbank-ID des Schülers
	 * @param idFach       die Datenbank-ID des Faches
	 *
	 * @return zum Tupel (Schüler, Fach) jeweilige {@link GostFachwahl}
	 * @throws DeveloperNotificationException falls der Schüler das Fach nicht gewählt hat.
	 */
	public @NotNull GostFachwahl schuelerGetOfFachFachwahl(final long idSchueler, final long idFach) throws DeveloperNotificationException {
		return fachwahlBySchuelerIdAndFachId.getOrException(idSchueler, idFach);
	}

	/**
	 * Liefert zum Tupel (Schüler, Fach) die jeweilige Fachwahl. <br>
	 * Gibt null zurück, falls der Schüler das Fach nicht gewählt hat.
	 *
	 * @param idSchueler   die Datenbank-ID des Schülers
	 * @param idFach       die Datenbank-ID des Faches
	 *
	 * @return zum Tupel (Schüler, Fach) jeweilige {@link GostFachwahl} oder null
	 */
	public GostFachwahl schuelerGetOfFachFachwahlOrNull(final long idSchueler, final long idFach) {
		return fachwahlBySchuelerIdAndFachId.getOrNull(idSchueler, idFach);
	}

	/**
	 * Liefert TRUE, falls der übergebene Schüler das entsprechende Fach gewählt hat.
	 *
	 * @param idSchueler   die Datenbank.ID des Schülers
	 * @param idFach       die Datenbank-ID des Faches der Fachwahl des Schülers
	 *
	 * @return true, falls der Schüler das Fach gewählt hat
	 */
	public boolean schuelerGetHatFach(final long idSchueler, final long idFach) {
		return fachwahlBySchuelerIdAndFachId.contains(idSchueler, idFach);
	}

	/**
	 * Liefert TRUE, falls beide Schüler bezogen auf das Fach die selbe Kursart haben oder eine Exception.
	 *
	 * @param idSchueler1   die Datenbank-ID des 1. Schülers
	 * @param idSchueler2   die Datenbank-ID des 2. Schülers
	 * @param idFach        die Datenbank-ID des Faches
	 *
	 * @return true, falls beide Schüler im Fach die selbe Kursart haben
	 * @throws DeveloperNotificationException falls einer der beiden Schüler das Fach nicht gewählt hat.
	 */
	public boolean schuelerGetHatDieSelbeKursartMitSchuelerInFach(final long idSchueler1, final long idSchueler2, final long idFach)
			throws DeveloperNotificationException {
		final @NotNull GostFachwahl fachwahl1 = fachwahlBySchuelerIdAndFachId.getOrException(idSchueler1, idFach);
		final @NotNull GostFachwahl fachwahl2 = fachwahlBySchuelerIdAndFachId.getOrException(idSchueler2, idFach);
		return fachwahl1.kursartID == fachwahl2.kursartID;
	}

	/**
	 * Liefert TRUE, falls es den Schüler mit der entsprechenden Fachwahl (Fach + Kursart) gibt.
	 *
	 * @param idSchueler   die Datenbank-ID des Schülers
	 * @param idFach       die Datenbank-ID des Faches der Fachwahl des Schülers
	 * @param idKursart    die Datenbank-ID der Kursart der Fachwahl des Schülers
	 *
	 * @return true, falls der Schüler die Fachwahl (Fach + Kursart) hat
	 */
	public boolean schuelerGetHatFachart(final long idSchueler, final long idFach, final int idKursart) {
		if (!fachwahlBySchuelerIdAndFachId.contains(idSchueler, idFach)) {
			return false;
		}
		return fachwahlBySchuelerIdAndFachId.getOrException(idSchueler, idFach).kursartID == idKursart;
	}

	/**
	 * Liefert die Menge aller {@link GostFachwahl} des Schülers.
	 * <br> Bei ungültiger Schüler-ID wird eine leere Liste geliefert.
	 *
	 * @param idSchueler   die Datenbank-ID des Schülers
	 *
	 * @return die Menge aller {@link GostFachwahl} des Schülers
	 */
	public @NotNull List<GostFachwahl> schuelerGetListeOfFachwahlen(final long idSchueler) {
		final List<GostFachwahl> fachwahlen = fachwahlmengeBySchuelerId.get(idSchueler);
		return (fachwahlen == null) ? new ArrayList<>() : fachwahlen;
	}

	/**
	 * Liefert eine Liste der gemeinsamen Fächer (auch in der Kursart übereinstimmend) beider Schüler.
	 *
	 * @param idSchueler1   die Datenbank-ID des 1. Schülers
	 * @param idSchueler2   die Datenbank-ID des 2. Schülers
	 *
	 * @return eine Liste der gemeinsamen Fächer (auch in der Kursart übereinstimmend) beider Schüler
	 */
	public @NotNull List<GostFach> schuelerGetFachListeGemeinsamerFacharten(final long idSchueler1, final long idSchueler2) {
		final @NotNull List<GostFach> temp = new ArrayList<>();

		for (final @NotNull GostFachwahl fachwahl1 : schuelerGetListeOfFachwahlen(idSchueler1)) {
			if (schuelerGetHatFachart(idSchueler2, fachwahl1.fachID, fachwahl1.kursartID)) {
				temp.add(manFaecher.getOrException(fachwahl1.fachID)); // Problem, wenn es die Fach-Referenz nicht (mehr) gibt!
			}
		}

		return temp;
	}

	/**
	 * Liefert TRUE, falls der Schüler aufgrund der Regel {@link GostKursblockungRegelTyp#SCHUELER_VERBIETEN_IN_KURS} im angegebenen Kurs verboten ist.
	 *
	 * @param idSchueler   die Datenbank-ID des Schülers
	 * @param idKurs       die Datenbank-ID des Kurses
	 *
	 * @return true, falls der Schüler aufgrund der Regel {@link GostKursblockungRegelTyp#SCHUELER_VERBIETEN_IN_KURS} im angegebenen Kurs verboten ist
	 */
	public boolean schuelerGetIstVerbotenInKurs(final long idSchueler, final long idKurs) {
		final @NotNull LongArrayKey key = new LongArrayKey(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ, idSchueler, idKurs);
		return regelByMultikey.containsKey(key);
	}

	/**
	 * Liefert die Regel, welche den Schüler in einem Kurs verbietet.
	 *
	 * @param idSchueler   die Datenbank-ID des Schülers
	 * @param idKurs       die Datenbank-ID des Kurses
	 *
	 * @return die Regel, welche den Schüler in einem Kurs verbietet
	 * @throws DeveloperNotificationException falls der Schüler oder der Kurs in der Blockung nicht existiert.
	 */
	public @NotNull GostBlockungRegel schuelerGetRegelVerbotenInKurs(final long idSchueler, final long idKurs) throws DeveloperNotificationException {
		final @NotNull LongArrayKey key = new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ, idSchueler, idKurs });
		return DeveloperNotificationException.ifNull(toStringSchueler(idSchueler) + " hat gar kein Verbot für " + toStringKurs(idKurs) + "!",
				regelByMultikey.get(key));
	}

	/**
	 * Liefert TRUE, falls der Schüler aufgrund der Regel {@link GostKursblockungRegelTyp#SCHUELER_FIXIEREN_IN_KURS} im angegebenen Kurs fixiert ist.
	 *
	 * @param idSchueler   die Datenbank-ID des Schülers
	 * @param idKurs       die Datenbank-ID des Kurses
	 *
	 * @return true, falls der Schüler aufgrund der Regel {@link GostKursblockungRegelTyp#SCHUELER_FIXIEREN_IN_KURS} im angegebenen Kurs fixiert ist
	 */
	public boolean schuelerGetIstFixiertInKurs(final long idSchueler, final long idKurs) {
		final @NotNull LongArrayKey key = new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs });
		return regelByMultikey.containsKey(key);
	}

	/**
	 * Liefert die Regel, welche den Schüler in einem Kurs fixiert.
	 *
	 * @param idSchueler   die Datenbank-ID des Schülers
	 * @param idKurs       die Datenbank-ID des Kurses
	 *
	 * @return die Regel, welche den Schüler in einem Kurs fixiert
	 * @throws DeveloperNotificationException falls der Schüler oder der Kurs in der Blockung nicht existiert.
	 */
	public @NotNull GostBlockungRegel schuelerGetRegelFixiertInKurs(final long idSchueler, final long idKurs) throws DeveloperNotificationException {
		final @NotNull LongArrayKey key = new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs });
		return DeveloperNotificationException.ifNull(toStringSchueler(idSchueler) + " hat gar keine Fixierung für " + toStringKurs(idKurs) + "!",
				regelByMultikey.get(key));
	}

	/**
	 * Gibt die ID der Blockung zurück.
	 *
	 * @return die ID der Blockung
	 */
	public long getID() {
		return dtoDaten.id;
	}

	/**
	 * Setzt die ID dieser Blockung.
	 *
	 * @param idNeu   die Datenbank-ID, welche der Blockung zugewiesen wird
	 * @throws DeveloperNotificationException Falls die übergebene ID ungültig ist.
	 */
	public void setID(final long idNeu) throws DeveloperNotificationException {
		DeveloperNotificationException.ifInvalidID("pBlockungsID", idNeu);
		dtoDaten.id = idNeu;
	}

	/**
	 * Liefert die maximale Blockungszeit in Millisekunden.
	 *
	 * @return die maximale Blockungszeit in Millisekunden
	 */
	public long getMaxTimeMillis() {
		return maxTimeMillis;
	}

	/**
	 * Setzt die maximale Blockungszeit in Millisekunden.
	 *
	 * @param blockungszeit   die maximale Blockungszeit in Millisekunden
	 * @throws DeveloperNotificationException falls der Wert nicht positiv ist.
	 */
	public void setMaxTimeMillis(final long blockungszeit) throws DeveloperNotificationException {
		DeveloperNotificationException.ifTrue("Der Wert muss positiv sein!", blockungszeit <= 0);
		maxTimeMillis = blockungszeit;
	}

	/**
	 * Liefert den Namen der Blockung.
	 *
	 * @return den Namen der Blockung
	 */
	public @NotNull String getName() {
		return dtoDaten.name;
	}

	/**
	 * Setzt den Namen der Blockung
	 *
	 * @param name   der Name, welcher der Blockung zugewiesen wird
	 * @throws UserNotificationException Falls der übergebene String leer ist.
	 */
	public void setName(final @NotNull String name) throws UserNotificationException {
		UserNotificationException.ifTrue("Ein leerer Name ist für die Blockung nicht zulässig.", "".equals(name));
		dtoDaten.name = name;
	}

	/**
	 * Liefert das Halbjahr der gymnasialen Oberstufe, für welches die Blockung angelegt wurde.
	 *
	 * @return das Halbjahr der gymnasialen Oberstufe
	 */
	public @NotNull GostHalbjahr getHalbjahr() {
		return GostHalbjahr.fromIDorException(dtoDaten.gostHalbjahr);
	}

	/**
	 * Setzt das Halbjahr der gymnasialen Oberstufe, für welches die Blockung angelegt wurde.
	 *
	 * @param halbjahr   das Halbjahr der gymnasialen Oberstufe
	 */
	public void setHalbjahr(final @NotNull GostHalbjahr halbjahr) {
		dtoDaten.gostHalbjahr = halbjahr.id;
	}

	/**
	 * Liefert TRUE, falls in dieser Blockung genau 1 Ergebnis (die Blockungsvorlage) vorhanden ist.
	 *
	 * @return true, falls in dieser Blockung genau 1 Ergebnis (die Blockungsvorlage) vorhanden ist
	 */
	public boolean getIstBlockungsVorlage() {
		return dtoDaten.ergebnisse.size() == 1;
	}

	/**
	 * Liefert die Anzahl an Fächern.
	 *
	 * @return die Anzahl an Fächern
	 */
	public int getFaecherAnzahl() {
		return manFaecher.faecher().size();
	}

	/**
	 * Gibt den Fächer-Manager zurück, der für die Blockungsdaten verwendet wird.
	 *
	 * @return der Fächer-Manager (siehe {@link GostFaecherManager})
	 */
	public @NotNull GostFaecherManager faecherManager() {
		return this.manFaecher;
	}

	/**
	 * Gibt die Blockungsdaten zurück.
	 *
	 * @return die Blockungsdaten (siehe {@link GostBlockungsdaten})
	 */
	public @NotNull GostBlockungsdaten daten() {
		return this.dtoDaten;
	}


	/**
	 * Liefert eine String-Representation vieler Daten.
	 *
	 * @return eine String-Representation vieler Daten
	 */
	public @NotNull String getDebugString() {
		final @NotNull StringBuilder sb = new StringBuilder();

		sb.append(lineSeparator);
		sb.append("Ergebnisse = %d".formatted(dtoDaten.ergebnisse.size()));
		sb.append(lineSeparator);

		sb.append(lineSeparator);
		sb.append("Schienen = %d".formatted(dtoDaten.schienen.size()));
		sb.append(lineSeparator);
		for (final @NotNull GostBlockungSchiene s : dtoDaten.schienen) {
			sb.append("    ID=%d, NR=%d, BEZ=%s, W-STD=%d".formatted(s.id, s.nummer, s.bezeichnung, s.wochenstunden));
			sb.append(lineSeparator);
			for (final @NotNull GostBlockungsergebnis e : ergebnisGetListeSortiertNachID()) {
				sb.append("    Hat E %d Schiene %d --> %b".formatted(e.id, s.id, ergebnisManagerGet(e.id).getOfSchieneExists(s.id)));
				sb.append(lineSeparator);
			}
		}

		sb.append(lineSeparator);
		sb.append("Schülermenge = %d".formatted(dtoDaten.schueler.size()));
		sb.append(lineSeparator);
		for (final @NotNull Schueler s : dtoDaten.schueler) {
			sb.append("    %d, %s, %s".formatted(s.id, s.nachname, s.vorname));
			sb.append(lineSeparator);
		}

		sb.append(lineSeparator);
		sb.append("Kurse = %d".formatted(dtoDaten.kurse.size()));
		sb.append(lineSeparator);
		for (final @NotNull GostBlockungKurs k : dtoDaten.kurse) {
			sb.append("    %d, %d, %d, %d".formatted(k.id, k.fach_id, k.kursart, k.nummer));
			sb.append(lineSeparator);
		}

		sb.append(lineSeparator);
		sb.append("Fachwahlen = %d".formatted(dtoDaten.fachwahlen.size()));
		sb.append(lineSeparator);
		for (final @NotNull GostFachwahl fw : dtoDaten.fachwahlen) {
			sb.append("    %d, %d, %d, %d, %b".formatted(fw.fachID, fw.kursartID, fw.schuelerID, fw.abiturfach, fw.istSchriftlich));
			sb.append(lineSeparator);
		}

		sb.append(lineSeparator);
		sb.append("Regeln = %d".formatted(dtoDaten.regeln.size()));
		sb.append(lineSeparator);
		for (final @NotNull GostBlockungRegel r : dtoDaten.regeln) {
			sb.append("    %d, %d, %s".formatted(r.id, r.typ, r.parameter));
			sb.append(lineSeparator);
		}

		return sb.toString();
	}


}
