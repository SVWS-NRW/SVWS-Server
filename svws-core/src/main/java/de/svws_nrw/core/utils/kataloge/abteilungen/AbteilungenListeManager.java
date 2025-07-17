package de.svws_nrw.core.utils.kataloge.abteilungen;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.asd.data.klassen.KlassenDaten;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.data.lehrer.LehrerListeEintrag;
import de.svws_nrw.core.data.schule.Abteilung;
import de.svws_nrw.core.data.schule.AbteilungKlassenzuordnung;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.utils.AuswahlManager;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Ein Manager zum Verwalten der Listen von Abteilungen.
 */
public final class AbteilungenListeManager extends AuswahlManager<Long, Abteilung, Abteilung> {


	private static final @NotNull Function<Abteilung, Long> _abteilungToId = (final @NotNull Abteilung a) -> a.id;

	private final @NotNull Map<Long, LehrerListeEintrag> _lehrerById;

	private @NotNull Map<Long, KlassenDaten> _klassenById;

	/** Ein Default-Comparator für den Vergleich von Abteilungen. */
	public static final @NotNull Comparator<Abteilung> comparator =
			(final @NotNull Abteilung a, final @NotNull Abteilung b) -> {
				int cmp = Integer.compare(a.sortierung, b.sortierung);
				if (cmp != 0)
					return cmp;

				if ((a.bezeichnung != null) && (b.bezeichnung != null)) {
					cmp = a.bezeichnung.compareTo(b.bezeichnung);
					if (cmp != 0)
						return cmp;
				}

				return Long.compare(a.id, b.id);
			};

	private final @NotNull Comparator<AbteilungKlassenzuordnung> comparatorKlassenzuordnung =
			(final @NotNull AbteilungKlassenzuordnung a, final @NotNull AbteilungKlassenzuordnung b) -> {
				final KlassenDaten firstClass = _klassenById.get(a.idKlasse);
				final KlassenDaten secondClass = _klassenById.get(b.idKlasse);
				if ((firstClass == null) || (firstClass.kuerzel == null) || (secondClass == null) || (secondClass.kuerzel == null))
					return 0;
				return firstClass.kuerzel.compareTo(secondClass.kuerzel);
			};


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param idSchuljahresabschnittAuswahl    	  der Schuljahresabschnitt, auf den sich die Abteilungsauswahl bezieht
	 * @param idSchuljahresabschnittSchule    der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schuljahresabschnitte           die Liste der Schuljahresabschnitte
	 * @param schulform     				  die Schulform der Schule
	 * @param abteilungen     				  die Liste der Abteilungen
	 * @param lehrer     					  die Liste der Lehrer
	 * @param klassen						  die Liste der Klassen
	 */
	public AbteilungenListeManager(final long idSchuljahresabschnittAuswahl, final long idSchuljahresabschnittSchule,
			final @NotNull List<Schuljahresabschnitt> schuljahresabschnitte, final Schulform schulform,
			final @NotNull List<Abteilung> abteilungen, final @NotNull List<LehrerListeEintrag> lehrer, final @NotNull List<KlassenDaten> klassen) {
		super(idSchuljahresabschnittAuswahl, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, abteilungen, AbteilungenListeManager.comparator,
				_abteilungToId, _abteilungToId, Arrays.asList());
		this._lehrerById = mapLehrer(lehrer);
		this._klassenById = mapKlassen(klassen);
	}

	private static @NotNull Map<Long, LehrerListeEintrag> mapLehrer(final @NotNull List<LehrerListeEintrag> lehrerListe) {
		final Map<Long, LehrerListeEintrag> result = new HashMap<>();
		for (final LehrerListeEintrag v : lehrerListe)
			result.put(v.id, v);
		return result;
	}

	private static @NotNull Map<Long, KlassenDaten> mapKlassen(final @NotNull List<KlassenDaten> klassen) {
		final Map<Long, KlassenDaten> result = new HashMap<>();
		for (final KlassenDaten v : klassen)
			result.put(v.id, v);
		return result;
	}

	/**
	 * Löscht Klassenzuordnungen anhand der IDs
	 *
	 * @param ids    Ids der Klassenzuordnungen
	 */
	public void deleteKlassenzuordnungen(final @NotNull List<Long> ids) {
		if (this._daten == null)
			return;

		for (final Long id: ids) {
			AbteilungKlassenzuordnung toBeDeleted = null;
			for (final AbteilungKlassenzuordnung v : this._daten.klassenzuordnungen) {
				if (v.id == id) {
					toBeDeleted = v;
					break;
				}
			}
			if (toBeDeleted != null)
				this._daten.klassenzuordnungen.remove(toBeDeleted);
		}
	}


	/**
	 * Ein Getter für die Liste der Lehrer
	 *
	 * @return lehrer
	 */
	public @NotNull Map<Long, LehrerListeEintrag> getLehrer() {
		return _lehrerById;
	}

	/**
	 * Ein Getter für die Liste der klassen
	 *
	 * @return klassen
	 */
	public @NotNull Map<Long, KlassenDaten> getKlassen() {
		return _klassenById;
	}


	/**
	 * Ein Getter der Klassen für die aktuelle Auswahl
	 *
	 * @return klassen
	 */
	public @NotNull List<KlassenDaten> getKlassenByAuswahl() {
		final List<KlassenDaten> result = new ArrayList<>();
		if ((this._daten == null) || (this._daten.klassenzuordnungen.isEmpty()))
			return result;
		for (final AbteilungKlassenzuordnung a : this._daten.klassenzuordnungen) {
			final KlassenDaten klasse = _klassenById.get(a.idKlasse);
			if (klasse != null)
				result.add(klasse);
		}
		return result;
	}

	/**
	 * Gibt einen Lehrer anhand der gegebenen ID zurück
	 *
	 * @param id	id des Lehrers
	 *
	 * @return		lehrer
	 */
	public LehrerListeEintrag getLehrerById(final long id) {
		return _lehrerById.get(id);
	}

	/**
	 * Fügt die Liste der AbteilungsKlassenzuordnungen der ausgewählten Abteilung hinzu
	 *
	 * @param zuordnungen    Liste der AbteilungsKlassenzuordnungen
	 */
	public void addKlassenToAuswahl(final @NotNull List<AbteilungKlassenzuordnung> zuordnungen) {
		if (_daten != null) {
			_daten.klassenzuordnungen.addAll(zuordnungen);
			_daten.klassenzuordnungen.sort(comparatorKlassenzuordnung);
		}
	}

	@Override
	protected boolean checkFilter(final Abteilung eintrag) {
		return true;
	}

	@Override
	protected int compareAuswahl(final @NotNull Abteilung a, final @NotNull Abteilung b) {
		for (final Pair<@NotNull String, @NotNull Boolean> criteria : _order) {
			final String field = criteria.a;
			final boolean asc = (criteria.b == null) || criteria.b;
			int cmp = 0;
			if ("bezeichnung".equals(field))
				cmp = a.bezeichnung.compareTo(b.bezeichnung);
			else
				throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt.");
			if (cmp == 0)
				continue;
			return asc ? cmp : -cmp;
		}
		return comparator.compare(a, b);
	}

	// public void useFilter(final @NotNull AbteilungenListeManager srcManager) {
	// 	return;
	// }

}
