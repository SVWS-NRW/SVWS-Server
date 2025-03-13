package de.svws_nrw.module.reporting.types.stundenplanung;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.svws_nrw.core.adt.map.ListMap3DLongKeys;
import de.svws_nrw.core.types.Wochentag;
import de.svws_nrw.module.reporting.types.klasse.ReportingKlasse;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse repräsentiert eine Stunde im Raster der Unterrichtsstunden im Rahmen der Stundenplanung.
 */
public class ReportingStundenplanungUnterrichtsrasterstunde extends ReportingStundenplanungZeitelement {

	/** Die ID der Stunde aus dem Unterrichtsraster. */
	protected long id;

	/** Der Stundenplan, zu dem diese Stunde aus dem Unterrichtsraster gehört. */
	protected ReportingStundenplanungStundenplan stundenplan;

	/** Die Nummer der Stunde am Wochentag aus dem Unterrichtsraster. */
	protected int stundeImUnterrichtsraster;

	/** Enthält die Unterrichte nach FachID, WochenTyp und UnterrichtsID für diese Stunde aus dem Unterrichtsraster */
	private ListMap3DLongKeys<ReportingStundenplanungUnterricht> listMapFaecherUnterrichte = new ListMap3DLongKeys<>();

	/** Enthält die Unterrichte nach KlassenID, WochenTyp und UnterrichtsID für diese Stunde aus dem Unterrichtsraster */
	private ListMap3DLongKeys<ReportingStundenplanungUnterricht> listMapKlassenUnterrichte = new ListMap3DLongKeys<>();

	/** Enthält die Unterrichte nach LehrerID, WochenTyp und UnterrichtsID für diese Stunde aus dem Unterrichtsraster */
	private ListMap3DLongKeys<ReportingStundenplanungUnterricht> listMapLehrerUnterrichte = new ListMap3DLongKeys<>();

	/** Enthält die Unterrichte nach RaumID, WochenTyp und UnterrichtsID für diese Stunde aus dem Unterrichtsraster */
	private ListMap3DLongKeys<ReportingStundenplanungUnterricht> listMapRaeumeUnterrichte = new ListMap3DLongKeys<>();

	/** Enthält die Unterrichte nach SchuelerID, WochenTyp und UnterrichtsID für diese Stunde aus dem Unterrichtsraster */
	private ListMap3DLongKeys<ReportingStundenplanungUnterricht> listMapSchuelerUnterrichte = new ListMap3DLongKeys<>();

	/** Die Unterrichte aus dem gegebenen Stundenplan, die dieser Stunde aus dem Unterrichtsraster zugeordnet sind. */
	private final List<ReportingStundenplanungUnterricht> unterrichte = new ArrayList<>();

	/** Eine Map, die die Unterrichte dieser Stunde aus dem Unterrichtsraster zur ID abspeichert. */
	private final Map<Long, ReportingStundenplanungUnterricht> mapUnterrichte = new HashMap<>();


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param id				        Die ID der Rasterstunde aus dem Unterrichtsraster.
	 * @param stundenplan		        Der Stundenplan, zu dem diese Stunde aus dem Unterrichtsraster gehört.
	 * @param stundenbeginn		        Die Uhrzeit in Minuten seit 0 Uhr, wann diese Stunde aus dem Unterrichtsraster beginnt. NULL bedeutet "noch nicht
	 *                                  definiert".
	 * @param stundenende		        Die Uhrzeit in Minuten seit 0 Uhr, wann diese Stunde aus dem Unterrichtsraster endet. NULL bedeutet "noch nicht
	 *                                  definiert".
	 * @param stundeImUnterrichtsraster	Die Nummer der Stunde am Wochentag aus dem Unterrichtsraster.
	 * @param wochentag			        Der {@link Wochentag} an dem diese Stunde aus dem Unterrichtsraster liegt.
	 * @param unterrichte               Die Unterrichte aus dem gegebenen Stundenplan, die dieser Stunde aus dem Unterrichtsraster zugeordnet sind.
	 */
	public ReportingStundenplanungUnterrichtsrasterstunde(final long id, final ReportingStundenplanungStundenplan stundenplan, final Integer stundenbeginn,
			final Integer stundenende, final int stundeImUnterrichtsraster, final Wochentag wochentag,
			final List<ReportingStundenplanungUnterricht> unterrichte) {
		super(stundenbeginn, stundenende, wochentag);
		this.id = id;
		this.stundenplan = stundenplan;
		this.stundeImUnterrichtsraster = stundeImUnterrichtsraster;

		if ((unterrichte != null) && !unterrichte.isEmpty())
			setUnterrichte(unterrichte);
	}


	// ##### Hash und Equals und Compare-Methoden #####

	/**
	 * Hashcode der Klasse
	 * @return Hashcode der Klasse
	 */
	@Override
	public int hashCode() {
		return 31 + Long.hashCode(id);
	}

	/**
	 * Equals der Klasse
	 * @param obj Das Vergleichsobjekt
	 * @return	true, falls es das gleiche Objekt ist, andernfalls false.
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof final ReportingStundenplanungUnterrichtsrasterstunde other))
			return false;
		return (id == other.id);
	}

	/**
	 * Vergleicht dieses Objekt vom Typ {@link ReportingStundenplanungUnterrichtsrasterstunde}
	 * mit einem anderen Objekt vom Typ {@link ReportingStundenplanungZeitelement}.
	 * Der Vergleich erfolgt basierend auf dem Wochentag und der Stunde im Unterrichtsraster.
	 *
	 * @param zeitelement Das Objekt vom Typ {@link ReportingStundenplanungZeitelement}, mit dem verglichen wird.
	 *          Es wird erwartet, dass es sich um ein {@link ReportingStundenplanungUnterrichtsrasterstunde}-Objekt handelt.
	 * @return Eine negative Ganzzahl, null oder eine positive Ganzzahl, wenn dieses Objekt kleiner,
	 *         gleich oder größer ist als das angegebene Objekt.
	 *         Der Wert wird gemäß der Reihenfolge von Wochentag und Stunde zurückgegeben.
	 */
	@Override
	public int compareTo(final @NotNull ReportingStundenplanungZeitelement zeitelement) {
		if (zeitelement instanceof final ReportingStundenplanungUnterrichtsrasterstunde other) {
			if (super.wochentag() == other.wochentag())
				return Integer.compare(this.stundeImUnterrichtsraster, other.stundeImUnterrichtsraster);
			else
				return super.wochentag().compareTo(other.wochentag());
		}
		return super.compareTo(zeitelement);
	}


	// ##### Berechnete Methoden #####

	/**
	 * Fügt die Unterrichte den bestehenden Unterrichten hinzu.
	 *
	 * @param unterrichte Eine Liste von {@link ReportingStundenplanungUnterricht},
	 *                           die dieser Stunde aus dem Unterrichtsraster zugeordnet werden soll.
	 */
	public void addUnterrichte(final List<ReportingStundenplanungUnterricht> unterrichte) {
		if ((unterrichte == null) || unterrichte.isEmpty())
			return;

		this.unterrichte.addAll(unterrichte);
		for (final ReportingStundenplanungUnterricht unterricht : unterrichte) {
			this.mapUnterrichte.put(unterricht.id(), unterricht);
			for (final ReportingLehrer lehrkraft : unterricht.lehrkraefte()) {
				this.listMapLehrerUnterrichte.add(lehrkraft.id(), unterricht.wochentyp(), unterricht.id(), unterricht);
			}
			for (final ReportingKlasse klasse : unterricht.klassen()) {
				this.listMapKlassenUnterrichte.add(klasse.id(), unterricht.wochentyp(), unterricht.id(), unterricht);
			}
			for (final ReportingStundenplanungRaum raum : unterricht.raeume()) {
				this.listMapRaeumeUnterrichte.add(raum.id(), unterricht.wochentyp(), unterricht.id(), unterricht);
			}
			if (unterricht.fach() != null)
				this.listMapFaecherUnterrichte.add(unterricht.fach().id(), unterricht.wochentyp(), unterricht.id(), unterricht);
			for (final ReportingSchueler schueler : unterricht.schueler()) {
				this.listMapSchuelerUnterrichte.add(schueler.id(), unterricht.wochentyp(), unterricht.id(), unterricht);
			}
		}
	}

	/**
	 * Setzt die Unterrichte, die dieser Stunde aus dem Unterrichtsraster zugeordnet sind.
	 * Die bestehende Liste der Unterrichte wird zunächst geleert und anschließend
	 * durch die angegebene ersetzt.
	 *
	 * @param unterrichte Eine Liste von {@link ReportingStundenplanungUnterricht},
	 *                    die dieser Stunde aus dem Unterrichtsraster zugeordnet werden soll.
	 */
	public void setUnterrichte(final List<ReportingStundenplanungUnterricht> unterrichte) {
		this.unterrichte.clear();
		this.mapUnterrichte.clear();
		this.listMapFaecherUnterrichte = new ListMap3DLongKeys<>();
		this.listMapKlassenUnterrichte = new ListMap3DLongKeys<>();
		this.listMapLehrerUnterrichte = new ListMap3DLongKeys<>();
		this.listMapRaeumeUnterrichte = new ListMap3DLongKeys<>();
		this.listMapSchuelerUnterrichte = new ListMap3DLongKeys<>();
		addUnterrichte(unterrichte);
	}

	/**
	 * Gibt den Unterricht dieser Stunde aus dem Unterrichtsraster mit der angegebenen ID zurück.
	 *
	 * @param idUnterricht Die ID des gewünschten Unterrichts.
	 * @return Der zur ID gehörige {@link ReportingStundenplanungUnterricht} oder null, wenn es keinen passenden Unterricht gibt.
	 */
	public ReportingStundenplanungUnterricht unterricht(final long idUnterricht) {
		return mapUnterrichte.get(idUnterricht);
	}

	/**
	 * Liefert den Unterricht einer bestimmten Klasse basierend auf der Klassen-ID
	 * und dem angegebenen Wochentyp. Optional können auch Unterrichte des Wochentyps 0
	 * einbezogen werden.
	 *
	 * @param idKlasse             Die ID der Klasse, deren Unterrichte abgerufen werden sollen.
	 * @param wochentyp            Der Wochentyp, für den die Unterrichte abgefragt werden.
	 * @param inklusiveWochentyp0  Gibt an, ob der Unterricht des Wochentyps 0 ebenfalls einbezogen werden sollen.
	 *
	 * @return Eine Liste von {@link ReportingStundenplanungUnterricht}, die die Unterrichte der angegebenen Klasse für den gewünschten Wochentyp repräsentiert.
	 */
	public List<ReportingStundenplanungUnterricht> unterrichteKlasse(final long idKlasse, final int wochentyp, final boolean inklusiveWochentyp0) {
		return getReportingStundenplanungUnterrichteByIdUndWoche(idKlasse, wochentyp, inklusiveWochentyp0, listMapKlassenUnterrichte);
	}

	/**
	 * Liefert den Unterricht eines bestimmten Lehrers basierend auf der Lehrer-ID
	 * und dem angegebenen Wochentyp. Optional können auch Unterrichte des Wochentyps 0
	 * einbezogen werden.
	 *
	 * @param idLehrer             Die ID des Lehrers, dessen Unterricht abgerufen werden sollen.
	 * @param wochentyp            Der Wochentyp, für den der Unterricht abgefragt werden.
	 * @param inklusiveWochentyp0  Gibt an, ob der Unterricht des Wochentyps 0 ebenfalls einbezogen werden sollen.
	 *
	 * @return Eine Liste von {@link ReportingStundenplanungUnterricht}, die die Unterrichte des angegebenen Lehrers für den gewünschten Wochentyp
	 * repräsentiert.
	 */
	public List<ReportingStundenplanungUnterricht> unterrichteLehrkraft(final long idLehrer, final int wochentyp, final boolean inklusiveWochentyp0) {
		return getReportingStundenplanungUnterrichteByIdUndWoche(idLehrer, wochentyp, inklusiveWochentyp0, listMapLehrerUnterrichte);
	}

	/**
	 * Liefert den Unterricht der Lehrkräfte, basierend auf der Liste der Lehrer-IDs
	 * und dem angegebenen Wochentyp. Optional können auch Unterrichte des Wochentyps 0
	 * einbezogen werden.
	 *
	 * @param idsLehrer            Die Liste der IDs der Lehrkräfte, deren Unterricht abgerufen werden sollen.
	 * @param wochentyp            Der Wochentyp, für den der Unterricht abgefragt werden.
	 * @param inklusiveWochentyp0  Gibt an, ob der Unterricht des Wochentyps 0 ebenfalls einbezogen werden sollen.
	 *
	 * @return Eine Liste von {@link ReportingStundenplanungUnterricht}, die die Unterrichte der angegebenen Lehrkräfte für den gewünschten Wochentyp
	 * repräsentiert.
	 */
	public List<ReportingStundenplanungUnterricht> unterrichteLehrkraefte(final List<Long> idsLehrer, final int wochentyp, final boolean inklusiveWochentyp0) {
		return getReportingStundenplanungUnterrichteByIdsUndWoche(idsLehrer, wochentyp, inklusiveWochentyp0, listMapLehrerUnterrichte);
	}

	/**
	 * Liefert den Unterricht eines bestimmten Schülers basierend auf der Schüler-ID
	 * und dem angegebenen Wochentyp. Optional können auch Unterrichte des Wochentyps 0
	 * einbezogen werden.
	 *
	 * @param idSchueler           Die ID des Schülers, dessen Unterricht abgerufen werden sollen.
	 * @param wochentyp            Der Wochentyp, für den der Unterricht abgefragt werden.
	 * @param inklusiveWochentyp0  Gibt an, ob der Unterricht des Wochentyps 0 ebenfalls einbezogen werden sollen.
	 *
	 * @return Eine Liste von {@link ReportingStundenplanungUnterricht}, die die Unterrichte des angegebenen Schüelers für den gewünschten Wochentyp
	 * repräsentiert.
	 */
	public List<ReportingStundenplanungUnterricht> unterrichteSchueler(final long idSchueler, final int wochentyp, final boolean inklusiveWochentyp0) {
		return getReportingStundenplanungUnterrichteByIdUndWoche(idSchueler, wochentyp, inklusiveWochentyp0, listMapSchuelerUnterrichte);
	}

	/**
	 * Ermittelt eine Liste von Unterrichten für eine gegebene ID und einen bestimmten Wochentyp.
	 *
	 * @param idObjekt            Die eindeutige ID des Objekts, für die die Unterrichte abgerufen werden sollen.
	 * @param wochentyp           Der Wochentyp, für den die Unterrichte abgefragt werden.
	 * @param inklusiveWochentyp0 Gibt an, ob Unterrichte des Wochentyps 0 ebenfalls einbezogen werden sollen.
	 * @param listMapUnterrichte  Eine Datenstruktur vom Typ ListMap3DLongKeys, die die Unterrichte enthält.
	 *
	 * @return Eine Liste von {@link ReportingStundenplanungUnterricht}, die den Kriterien entsprechen.
	 */
	private List<ReportingStundenplanungUnterricht> getReportingStundenplanungUnterrichteByIdUndWoche(final long idObjekt, final int wochentyp,
			final boolean inklusiveWochentyp0, final ListMap3DLongKeys<ReportingStundenplanungUnterricht> listMapUnterrichte) {
		return getReportingStundenplanungUnterrichteByIdsUndWoche(List.of(idObjekt), wochentyp, inklusiveWochentyp0, listMapUnterrichte);
	}

	/**
	 * Ermittelt eine Liste von Unterrichten für eine gegebene ID und einen bestimmten Wochentyp.
	 *
	 * @param idsObjekte          Die Liste der eindeutigen IDs der Objekte, für die die Unterrichte abgerufen werden sollen.
	 * @param wochentyp           Der Wochentyp, für den die Unterrichte abgefragt werden.
	 * @param inklusiveWochentyp0 Gibt an, ob Unterrichte des Wochentyps 0 ebenfalls einbezogen werden sollen.
	 * @param listMapUnterrichte  Eine Datenstruktur vom Typ ListMap3DLongKeys, die die Unterrichte enthält.
	 *
	 * @return Eine Liste von {@link ReportingStundenplanungUnterricht}, die den Kriterien entsprechen.
	 */
	private List<ReportingStundenplanungUnterricht> getReportingStundenplanungUnterrichteByIdsUndWoche(final List<Long> idsObjekte, final int wochentyp,
			final boolean inklusiveWochentyp0, final ListMap3DLongKeys<ReportingStundenplanungUnterricht> listMapUnterrichte) {
		final List<ReportingStundenplanungUnterricht> result = new ArrayList<>();

		if (this.unterrichte.isEmpty() || (idsObjekte == null))
			return result;

		final List<Long> idsObjekteNonNull = new ArrayList<>(idsObjekte.stream().filter(Objects::nonNull).toList());

		if (idsObjekteNonNull.isEmpty())
			return result;

		for (final long idObjekt : idsObjekteNonNull) {
			if (inklusiveWochentyp0) {
				result.addAll(listMapUnterrichte.get12(idObjekt, 0));
			}
			result.addAll(listMapUnterrichte.get12(idObjekt, wochentyp));
		}

		return result;
	}


	// ##### Getter #####

	/**
	 * Liefert die ID der Stunde aus dem Unterrichtsraster.
	 *
	 * @return Die ID der Stunde aus dem Unterrichtsraster.
	 */
	public long id() {
		return id;
	}

	/**
	 * Liefert den Stundenplan, zu dem diese Stunde aus dem Unterrichtsraster gehört.
	 *
	 * @return Der Stundenplan der Unterrichtsstunde.
	 */
	public ReportingStundenplanungStundenplan stundenplan() {
		return stundenplan;
	}

	/**
	 * Liefert die Nummer der Stunde am Wochentag aus dem Unterrichtsraster.
	 *
	 * @return Die Nummer der Stunde am Wochentag.
	 */
	public int stundeImUnterrichtsraster() {
		return stundeImUnterrichtsraster;
	}

	/**
	 * Liefert die Liste der Unterrichtsstunden, die dieser Zeitrasterstunde zugeordnet sind.
	 *
	 * @return Eine Liste der zugeordneten Unterrichtsstunden.
	 */
	public List<ReportingStundenplanungUnterricht> unterrichte() {
		return Collections.unmodifiableList(unterrichte);
	}
}
