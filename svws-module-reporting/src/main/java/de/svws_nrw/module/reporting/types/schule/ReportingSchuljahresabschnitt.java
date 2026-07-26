package de.svws_nrw.module.reporting.types.schule;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.svws_nrw.module.reporting.types.ReportingBaseType;
import de.svws_nrw.module.reporting.types.ankreuzkompetenz.ReportingAnkreuzkompetenz;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.types.jahrgang.ReportingJahrgang;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKlasse;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKurs;

/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ Schuljahresabschnitt.
 *
 * <p>Ein Schuljahresabschnitt kann entweder einen in der Datenbank angelegten Abschnitt abbilden oder virtuell sein.
 * Virtuelle Abschnitte besitzen eine negative Pseudo-ID und werden über {@link #istVirtuell()} erkannt; siehe dort für
 * den fachlichen Hintergrund.</p>
 */
public class ReportingSchuljahresabschnitt extends ReportingBaseType {

	/**
	 * Die ID des Schuljahresabschnittes. Bei einem Abschnitt aus der Datenbank ist dies dessen Datenbank-ID, bei einem
	 * virtuellen Abschnitt eine negative Pseudo-ID (siehe {@link #istVirtuell()}).
	 */
	protected long id;

	/** Das Schuljahr, in welchem der Schuljahresabschnitt liegt */
	protected int schuljahr;

	/** Die Nummer des Abschnitts im Schuljahr */
	protected int abschnitt;

	/** Die ID des Schuljahresabschnittes, der diesem Abschnitt folgt. */
	protected Long idFolgenderAbschnitt;

	/** Die ID des Schuljahresabschnittes, der diesem Abschnitt vorhergeht. */
	protected Long idVorherigerAbschnitt;

	/** Der Schuljahresabschnitt, der diesem Abschnitt folgt. */
	protected ReportingSchuljahresabschnitt folgenderAbschnitt;

	/** Der Schuljahresabschnitt, der diesem Abschnitt vorhergeht. */
	protected ReportingSchuljahresabschnitt vorherigerAbschnitt;

	/** Die Map der Fächer des Schuljahresabschnitts */
	protected Map<Long, ReportingFach> faecher;

	/** Die Map der Jahrgänge des Schuljahresabschnitts */
	protected Map<Long, ReportingJahrgang> jahrgaenge;

	/** Die Map der Klassen des Schuljahresabschnitts */
	protected Map<Long, ReportingKlasse> klassen;

	/** Die Map der Kurse des Schuljahresabschnitts */
	protected Map<Long, ReportingKurs> kurse;

	/** Die Map der Ankreuzkompetenzen des Schuljahresabschnitts */
	protected Map<Long, ReportingAnkreuzkompetenz> ankreuzkompetenzen;

	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param id 					Die ID des Schuljahresabschnittes
	 * @param schuljahr 			Das Schuljahr, in welchem der Schuljahresabschnitt liegt
	 * @param abschnitt 			Die Nummer des Abschnitts im Schuljahr
	 * @param idFolgenderAbschnitt 	Die ID des Schuljahresabschnittes, der diesem Abschnitt folgt.
	 * @param idVorherigerAbschnitt Die ID des Schuljahresabschnittes, der diesem Abschnitt vorhergeht.
	 * @param folgenderAbschnitt 	Der Schuljahresabschnitt, der diesem Abschnitt folgt.
	 * @param vorherigerAbschnitt 	Der Schuljahresabschnitt, der diesem Abschnitt vorhergeht.
	 * @param faecher				Die Fächer des Schuljahresabschnitts
	 * @param jahrgaenge			Die Jahrgänge des Schuljahresabschnitts
	 * @param klassen				Die Klassen des Schuljahresabschnitts
	 * @param kurse					Die Kurse des Schuljahresabschnitts
	 * @param ankreuzkompetenzen	Die Ankreuzkompetenzen des Schuljahresabschnitts
	 */
	@SuppressWarnings("java:S107") // Konstruktoren mit zu vielen Parametern (gemäß SonarQube) werden aktuell toleriert und nicht refacored (Stand 2026-04).
	public ReportingSchuljahresabschnitt(final long id, final int schuljahr, final int abschnitt, final Long idFolgenderAbschnitt,
			final Long idVorherigerAbschnitt, final ReportingSchuljahresabschnitt folgenderAbschnitt, final ReportingSchuljahresabschnitt vorherigerAbschnitt,
			final Map<Long, ReportingFach> faecher, final Map<Long, ReportingJahrgang> jahrgaenge, final Map<Long, ReportingKlasse> klassen,
			final Map<Long, ReportingKurs> kurse, final Map<Long, ReportingAnkreuzkompetenz> ankreuzkompetenzen) {
		this.id = id;
		this.schuljahr = schuljahr;
		this.abschnitt = abschnitt;
		this.idFolgenderAbschnitt = idFolgenderAbschnitt;
		this.idVorherigerAbschnitt = idVorherigerAbschnitt;
		this.folgenderAbschnitt = folgenderAbschnitt;
		this.vorherigerAbschnitt = vorherigerAbschnitt;
		this.faecher = (faecher != null) ? new HashMap<>(faecher) : new HashMap<>();
		this.jahrgaenge = (jahrgaenge != null) ? new HashMap<>(jahrgaenge) : new HashMap<>();
		this.klassen = (klassen != null) ? new HashMap<>(klassen) : new HashMap<>();
		this.kurse = (kurse != null) ? new HashMap<>(kurse) : new HashMap<>();
		this.ankreuzkompetenzen = (ankreuzkompetenzen != null) ? new HashMap<>(ankreuzkompetenzen) : new HashMap<>();
	}


	// ##### Berechnete Methoden #####

	/**
	 * Gibt an, ob es sich um einen virtuellen Schuljahresabschnitt handelt, also um einen Abschnitt, den es in der
	 * Datenbank der Schule (noch) nicht gibt.
	 *
	 * <p>Virtuelle Abschnitte werden dort benötigt, wo aus fachlichen Daten ein Schuljahr und ein Abschnitt abgeleitet
	 * werden, für die kein Datenbank-Abschnitt existiert. Typischer Fall ist die GOSt-Kursplanung: Der Client erlaubt
	 * Blockungen für Halbjahre, die zeitlich hinter dem letzten angelegten Schuljahresabschnitt der Schule liegen. Ohne
	 * virtuellen Abschnitt liefe ein solcher Report ins Leere, obwohl die benötigten Daten vorhanden sind.</p>
	 *
	 * <p>Ein virtueller Abschnitt liefert alle Daten, die allein vom Schuljahr abhängen, vollständig und korrekt —
	 * Fächer, Jahrgänge und Ankreuzkompetenzen stammen aus schemaweiten Katalogen und benötigen den Abschnitt selbst
	 * nicht. Abschnittsgebundene Daten, also Klassen und Kurse, bleiben dagegen leer: Sie sind ohne
	 * Datenbank-Abschnitt fachlich nicht vorhanden.</p>
	 *
	 * <p>Zur Unterscheidung von den stets positiven Datenbank-IDs tragen virtuelle Abschnitte eine negative, aus
	 * Schuljahr und Abschnitt abgeleitete Pseudo-ID. Diese ID ist rein modulintern und darf nicht als Datenbank-ID
	 * verwendet werden.</p>
	 *
	 * @return true, wenn der Schuljahresabschnitt virtuell ist, andernfalls false.
	 */
	public boolean istVirtuell() {
		return this.id < 0;
	}

	/**
	 * Kurzer Text zum Schuljahresabschnitt im Format 20XX/YY.A
	 *
	 * @return Kurzer Text zum Schuljahresabschnitt
	 */
	public String textSchuljahresabschnittKurz() {
		return "%s/%s.%s".formatted(schuljahr, (schuljahr % 100) + 1, abschnitt);
	}

	/**
	 * Kurzer Text zum Schuljahresabschnitt im Format 20XX/YY A. Halbjahr
	 *
	 * @return Langer Text zum Schuljahresabschnitt
	 */
	public String textSchuljahresabschnittLang() {
		return "%s/%s %s. Halbjahr".formatted(schuljahr, (schuljahr % 100) + 1, abschnitt);
	}

	/**
	 * Gibt das Fach zur ID aus der Liste der Fächer des Schuljahresabschnitts zurück
	 *
	 * @param id	Die ID des Faches
	 *
	 * @return 		Das Fach zur ID oder null, wenn das Fach nicht vorhanden ist.
	 */
	public ReportingFach fach(final long id) {
		return faecher().get(id);
	}

	/**
	 * Gibt die Fächer zu den IDs aus der Liste der Fächer des Schuljahresabschnitts zurück
	 *
	 * @param ids	Die IDs der Fächer
	 *
	 * @return 		Die Fächer zu den IDs oder eine leere Liste, wenn kein Fach vorhanden ist.
	 */
	public List<ReportingFach> faecher(final List<Long> ids) {
		final List<ReportingFach> result = new ArrayList<>();
		if (ids == null) {
			return result;
		}
		final List<Long> idsNonNull = ids.stream().filter(Objects::nonNull).distinct().toList();
		if (idsNonNull.isEmpty()) {
			return result;
		}
		idsNonNull.stream().map(this::fach).filter(Objects::nonNull).forEach(result::add);
		return result;
	}

	/**
	 * Gibt den Jahrgang zur ID aus der Liste der Jahrgänge des Schuljahresabschnitts zurück
	 *
	 * @param id	Die ID des Jahrgangs
	 *
	 * @return 		Der Jahrgang zur ID oder null, wenn der Jahrgang nicht vorhanden ist.
	 */
	public ReportingJahrgang jahrgang(final long id) {
		return jahrgaenge().get(id);
	}

	/**
	 * Gibt die Jahrgänge zu den IDs aus der Liste der Jahrgänge des Schuljahresabschnitts zurück
	 *
	 * @param ids	Die IDs der Jahrgänge
	 *
	 * @return 		Die Jahrgänge zu den IDs oder eine leere Liste, wenn kein Jahrgang vorhanden ist.
	 */
	public List<ReportingJahrgang> jahrgaenge(final List<Long> ids) {
		final List<ReportingJahrgang> result = new ArrayList<>();
		if (ids == null) {
			return result;
		}
		final List<Long> idsNonNull = ids.stream().filter(Objects::nonNull).distinct().toList();
		if (idsNonNull.isEmpty()) {
			return result;
		}
		idsNonNull.stream().map(this::jahrgang).filter(Objects::nonNull).forEach(result::add);
		return result;
	}

	/**
	 * Gibt die Klasse zur ID aus der Liste der Klassen des Schuljahresabschnitts zurück
	 *
	 * @param id	Die ID der Klasse
	 *
	 * @return 		Die Klasse zur ID oder null, wenn die Klasse nicht vorhanden ist.
	 */
	public ReportingKlasse klasse(final long id) {
		return klassen().get(id);
	}

	/**
	 * Gibt die Klassen zu den IDs aus der Liste der Klassen des Schuljahresabschnitts zurück
	 *
	 * @param ids	Die IDs der Klassen
	 *
	 * @return 		Die Klassen zu den IDs oder eine leere Liste, wenn keine Klasse vorhanden ist.
	 */
	public List<ReportingKlasse> klassen(final List<Long> ids) {
		final List<ReportingKlasse> result = new ArrayList<>();
		if (ids == null) {
			return result;
		}
		final List<Long> idsNonNull = ids.stream().filter(Objects::nonNull).distinct().toList();
		if (idsNonNull.isEmpty()) {
			return result;
		}
		idsNonNull.stream().map(this::klasse).filter(Objects::nonNull).forEach(result::add);
		return result;
	}

	/**
	 * Gibt den Kurs zur ID aus der Liste der Kurse des Schuljahresabschnitts zurück
	 *
	 * @param id	Die ID des Kurses
	 *
	 * @return 		Der Kurs zur ID oder null, wenn der Kurs nicht vorhanden ist.
	 */
	public ReportingKurs kurs(final long id) {
		return kurse().get(id);
	}

	/**
	 * Gibt die Kurse zu den IDs aus der Liste der Kurse des Schuljahresabschnitts zurück
	 *
	 * @param ids	Die IDs der Kurse
	 *
	 * @return 		Die Kurse zu den IDs oder eine leere Liste, wenn kein Kurs vorhanden ist.
	 */
	public List<ReportingKurs> kurse(final List<Long> ids) {
		final List<ReportingKurs> result = new ArrayList<>();
		if (ids == null) {
			return result;
		}
		final List<Long> idsNonNull = ids.stream().filter(Objects::nonNull).distinct().toList();
		if (idsNonNull.isEmpty()) {
			return result;
		}
		idsNonNull.stream().map(this::kurs).filter(Objects::nonNull).forEach(result::add);
		return result;
	}

	/**
	 * Die Map der Ankreuzkompetenzen des Schuljahresabschnitts
	 *
	 * @return Inhalt des Feldes ankreuzkompetenzen; nie {@code null}, bei fehlender Zuordnung eine leere Map.
	 */
	public Map<Long, ReportingAnkreuzkompetenz> ankreuzkompetenzen() {
		return ankreuzkompetenzen;
	}

	/**
	 * Gibt die Ankreuzkompetenz zur ID aus der Liste der Ankreuzkompetenzen des Schuljahresabschnitts zurück
	 *
	 * @param id	Die ID der Ankreuzkompetenz
	 *
	 * @return 		Die Ankreuzkompetenz zur ID oder null, wenn die Ankreuzkompetenz nicht vorhanden ist.
	 */
	public ReportingAnkreuzkompetenz ankreuzkompetenz(final long id) {
		return ankreuzkompetenzen().get(id);
	}

	/**
	 * Gibt die Ankreuzkompetenzen zu den IDs aus der Liste der Ankreuzkompetenzen des Schuljahresabschnitts zurück
	 *
	 * @param ids	Die IDs der Ankreuzkompetenzen
	 *
	 * @return 		Die Ankreuzkompetenzen zu den IDs oder eine leere Liste, wenn keine Ankreuzkompetenz vorhanden ist.
	 */
	public List<ReportingAnkreuzkompetenz> ankreuzkompetenzen(final List<Long> ids) {
		final List<ReportingAnkreuzkompetenz> result = new ArrayList<>();
		if (ids == null) {
			return result;
		}
		final List<Long> idsNonNull = ids.stream().filter(Objects::nonNull).distinct().toList();
		if (idsNonNull.isEmpty()) {
			return result;
		}
		idsNonNull.stream().map(this::ankreuzkompetenz).filter(Objects::nonNull).forEach(result::add);
		return result;
	}

	/**
	 * Gibt die sichtbaren Ankreuzkompetenzen des Schuljahresabschnitts zurück, sortiert nach Sortierung.
	 * Es werden nur Ankreuzkompetenzen geliefert, deren Abschnitt 0 (beide Halbjahre) ist oder dem Abschnitt
	 * dieses Schuljahresabschnitts entspricht.
	 *
	 * @param nurAktive 	Falls true, werden ausschließlich aktive Ankreuzkompetenzen zurückgegeben.
	 *
	 * @return 				Die gefilterte und sortierte Liste der Ankreuzkompetenzen.
	 */
	public List<ReportingAnkreuzkompetenz> ankreuzkompetenzenImAbschnitt(final boolean nurAktive) {
		return ankreuzkompetenzen().values().stream()
				.filter(a -> (a.abschnitt() == 0) || (a.abschnitt() == this.abschnitt))
				.filter(ReportingAnkreuzkompetenz::istSichtbar)
				.filter(a -> !nurAktive || a.istAktiv())
				.sorted(Comparator.comparingInt(ReportingAnkreuzkompetenz::sortierung))
				.toList();
	}

	/**
	 * Gibt die sichtbaren Ankreuzkompetenzen des Schuljahresabschnitts zum angegebenen Fach zurück, sortiert nach Sortierung.
	 * Es werden nur Ankreuzkompetenzen geliefert, deren Abschnitt 0 (beide Halbjahre) ist oder dem Abschnitt
	 * dieses Schuljahresabschnitts entspricht.
	 *
	 * @param idFach 		Die ID des Faches, auf das gefiltert wird.
	 * @param nurAktive 	Falls true, werden ausschließlich aktive Ankreuzkompetenzen zurückgegeben.
	 *
	 * @return 				Die gefilterte und sortierte Liste der Ankreuzkompetenzen.
	 */
	public List<ReportingAnkreuzkompetenz> ankreuzkompetenzenImAbschnittFuerFach(final long idFach, final boolean nurAktive) {
		return ankreuzkompetenzenImAbschnitt(nurAktive).stream()
				.filter(a -> (a.fach() != null) && (a.fach().id() == idFach))
				.toList();
	}

	/**
	 * Gibt die sichtbaren Ankreuzkompetenzen des Schuljahresabschnitts zurück, die dem Arbeits- und Sozialverhalten
	 * zugeordnet sind, sortiert nach Sortierung. Es werden nur Ankreuzkompetenzen geliefert, deren Abschnitt 0
	 * (beide Halbjahre) ist oder dem Abschnitt dieses Schuljahresabschnitts entspricht.
	 *
	 * @param nurAktive 	Falls true, werden ausschließlich aktive Ankreuzkompetenzen zurückgegeben.
	 *
	 * @return 				Die gefilterte und sortierte Liste der Ankreuzkompetenzen.
	 */
	public List<ReportingAnkreuzkompetenz> ankreuzkompetenzenImAbschnittFuerASV(final boolean nurAktive) {
		return ankreuzkompetenzenImAbschnitt(nurAktive).stream()
				.filter(ReportingAnkreuzkompetenz::istASV)
				.toList();
	}


	// ##### Getter #####

	/**
	 * Die ID des Schuljahresabschnittes
	 *
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return id;
	}

	/**
	 * Das Schuljahr, in welchem der Schuljahresabschnitt liegt
	 *
	 * @return Inhalt des Feldes schuljahr
	 */
	public int schuljahr() {
		return schuljahr;
	}

	/**
	 * Die Nummer des Abschnitts im Schuljahr
	 *
	 * @return Inhalt des Feldes abschnitt
	 */
	public int abschnitt() {
		return abschnitt;
	}

	/**
	 * Die ID des Schuljahresabschnittes, der diesem Abschnitt folgt.
	 *
	 * @return Inhalt des Feldes idFolgenderAbschnitt
	 */
	public Long idFolgenderAbschnitt() {
		return idFolgenderAbschnitt;
	}

	/**
	 * Die ID des Schuljahresabschnittes, der diesem Abschnitt vorhergeht.
	 *
	 * @return Inhalt des Feldes idVorherigerAbschnitt
	 */
	public Long idVorherigerAbschnitt() {
		return idVorherigerAbschnitt;
	}

	/**
	 * Der Schuljahresabschnitt, der diesem Abschnitt folgt.
	 *
	 * @return Inhalt des Feldes folgenderAbschnitt
	 */
	public ReportingSchuljahresabschnitt folgenderAbschnitt() {
		return folgenderAbschnitt;
	}

	/**
	 * Der Schuljahresabschnitt, der diesem Abschnitt vorhergeht.
	 *
	 * @return Inhalt des Feldes vorherigerAbschnitt
	 */
	public ReportingSchuljahresabschnitt vorherigerAbschnitt() {
		return vorherigerAbschnitt;
	}

	/**
	 * Die Map der Fächer des Schuljahresabschnitts
	 *
	 * @return Inhalt des Feldes faecher; nie {@code null}, bei fehlender Zuordnung eine leere Map.
	 */
	public Map<Long, ReportingFach> faecher() {
		return faecher;
	}

	/**
	 * Die Map der Jahrgänge des Schuljahresabschnitts
	 *
	 * @return Inhalt des Feldes jahrgaenge; nie {@code null}, bei fehlender Zuordnung eine leere Map.
	 */
	public Map<Long, ReportingJahrgang> jahrgaenge() {
		return jahrgaenge;
	}

	/**
	 * Die Map der Klassen des Schuljahresabschnitts
	 *
	 * @return Inhalt des Feldes klassen; nie {@code null}, bei fehlender Zuordnung eine leere Map.
	 */
	public Map<Long, ReportingKlasse> klassen() {
		return klassen;
	}

	/**
	 * Die Map der Kurse des Schuljahresabschnitts
	 *
	 * @return Inhalt des Feldes kurse; nie {@code null}, bei fehlender Zuordnung eine leere Map.
	 */
	public Map<Long, ReportingKurs> kurse() {
		return kurse;
	}
}
