package de.svws_nrw.core.utils.schueler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.asd.data.kaoa.KAOAAnschlussoptionenKatalogEintrag;
import de.svws_nrw.asd.data.kaoa.KAOABerufsfeldKatalogEintrag;
import de.svws_nrw.asd.data.kaoa.KAOAEbene4KatalogEintrag;
import de.svws_nrw.asd.data.kaoa.KAOAKategorieKatalogEintrag;
import de.svws_nrw.asd.data.kaoa.KAOAMerkmalKatalogEintrag;
import de.svws_nrw.asd.data.kaoa.KAOAZusatzmerkmalKatalogEintrag;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.data.schueler.SchuelerKAoADaten;
import de.svws_nrw.core.data.schueler.SchuelerLernabschnittListeEintrag;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.asd.types.kaoa.KAOAAnschlussoptionen;
import de.svws_nrw.asd.types.kaoa.KAOABerufsfeld;
import de.svws_nrw.asd.types.kaoa.KAOAEbene4;
import de.svws_nrw.asd.types.kaoa.KAOAKategorie;
import de.svws_nrw.asd.types.kaoa.KAOAMerkmal;
import de.svws_nrw.asd.types.kaoa.KAOAZusatzmerkmal;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.utils.AttributMitAuswahl;
import de.svws_nrw.core.utils.AuswahlManager;
import de.svws_nrw.core.utils.MapUtils;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zum Alternative der KAoA-Daten.
 */
public class SchuelerKAoAManager extends AuswahlManager<Long, SchuelerKAoADaten, SchuelerKAoADaten> {

	/** Ein Default-Comparator für den Vergleich von KAoA in KAoA-Listen. */
	public static final @NotNull Comparator<SchuelerKAoADaten> comparator =
			(final @NotNull SchuelerKAoADaten a, final @NotNull SchuelerKAoADaten b) -> {
				int cmp = Long.compare(a.idSchuljahresabschnitt, b.idSchuljahresabschnitt);
				if (cmp != 0)
					return cmp;
				cmp = Long.compare(a.idKategorie, b.idKategorie);
				if (cmp != 0)
					return cmp;
				return Long.compare(a.id, b.id);
			};

	/**
	 * Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ
	 */
	private static final @NotNull Function<SchuelerKAoADaten, Long> _kaoaToId =
			(final @NotNull SchuelerKAoADaten kaoa) -> kaoa.id;
	private final @NotNull Function<KAOAKategorie, Long> _kategorieToId = (final @NotNull KAOAKategorie kategorie) -> {
		final KAOAKategorieKatalogEintrag ke = kategorie.daten(getSchuljahr());
		if (ke == null)
			throw new IllegalArgumentException("Die KAOA-Kategorie %s ist in dem Schuljahr %d nicht gültig.".formatted(kategorie.name(), getSchuljahr()));
		return ke.id;
	};
	private static final @NotNull Comparator<KAOAKategorie> _comparatorKategorie =
			(final @NotNull KAOAKategorie a, final @NotNull KAOAKategorie b) -> a.ordinal() - b.ordinal();
	private final @NotNull Function<KAOAMerkmal, Long> _merkmalToId = (final @NotNull KAOAMerkmal merkmal) -> {
		final KAOAMerkmalKatalogEintrag ke = merkmal.daten(getSchuljahr());
		if (ke == null)
			throw new IllegalArgumentException("Die KAOA-Merkmal %s ist in dem Schuljahr %d nicht gültig.".formatted(merkmal.name(), getSchuljahr()));
		return ke.id;
	};
	private static final @NotNull Comparator<KAOAMerkmal> _comparatorMerkmal =
			(final @NotNull KAOAMerkmal a, final @NotNull KAOAMerkmal b) -> a.ordinal() - b.ordinal();
	private final @NotNull Function<KAOAZusatzmerkmal, Long> _zusatzmerkmalToId = (final @NotNull KAOAZusatzmerkmal zusatzmerkmal) -> {
		final KAOAZusatzmerkmalKatalogEintrag ke = zusatzmerkmal.daten(getSchuljahr());
		if (ke == null)
			throw new IllegalArgumentException(
					"Die KAOA-Zusatzmerkmal %s ist in dem Schuljahr %d nicht gültig.".formatted(zusatzmerkmal.name(), getSchuljahr()));
		return ke.id;
	};
	private static final @NotNull Comparator<KAOAZusatzmerkmal> _comparatorZusatzmerkmal =
			(final @NotNull KAOAZusatzmerkmal a, final @NotNull KAOAZusatzmerkmal b) -> a.ordinal() - b.ordinal();
	private final @NotNull Function<KAOAAnschlussoptionen, Long> _anschlussoptionToId = (final @NotNull KAOAAnschlussoptionen anschlussoption) -> {
		final KAOAAnschlussoptionenKatalogEintrag ke = anschlussoption.daten(getSchuljahr());
		if (ke == null)
			throw new IllegalArgumentException(
					"Die KAOA-Anschlussoption %s ist in dem Schuljahr %d nicht gültig.".formatted(anschlussoption.name(), getSchuljahr()));
		return ke.id;
	};
	private static final @NotNull Comparator<KAOAAnschlussoptionen> _comparatorAnschlussoptionen =
			(final @NotNull KAOAAnschlussoptionen a, final @NotNull KAOAAnschlussoptionen b) -> a.ordinal() - b.ordinal();
	private final @NotNull Function<KAOAEbene4, Long> _ebene4ToId = (final @NotNull KAOAEbene4 ebene4) -> {
		final KAOAEbene4KatalogEintrag ke = ebene4.daten(getSchuljahr());
		if (ke == null)
			throw new IllegalArgumentException("Die KAOA-Ebene 4 %s ist in dem Schuljahr %d nicht gültig.".formatted(ebene4.name(), getSchuljahr()));
		return ke.id;
	};
	private static final @NotNull Comparator<KAOAEbene4> _comparatorEbene4 =
			(final @NotNull KAOAEbene4 a, final @NotNull KAOAEbene4 b) -> a.ordinal() - b.ordinal();
	private static final @NotNull Comparator<KAOABerufsfeld> _comparatorBerufsfelder =
			(final @NotNull KAOABerufsfeld a, final @NotNull KAOABerufsfeld b) -> a.ordinal() - b.ordinal();

	/** Das Filter-Attribut für die Kategorien */
	public final @NotNull AttributMitAuswahl<Long, KAOAKategorie> _kategorien;

	/** Das Filter-Attribut für die Merkmale */
	public final @NotNull AttributMitAuswahl<Long, KAOAMerkmal> _merkmale;

	/** Das Filter-Attribut für die Zusatzmerkmale */
	public final @NotNull AttributMitAuswahl<Long, KAOAZusatzmerkmal> _zusatzmerkmale;

	/** Das Filter-Attribut für die Anschlussoptionen */
	public final @NotNull AttributMitAuswahl<Long, KAOAAnschlussoptionen> _anschlussoptionen;

	/** Das Filter-Attribut für die Ebene4 */
	public final @NotNull AttributMitAuswahl<Long, KAOAEbene4> _ebene4;

	/** Die Berufsfelder */
	private final @NotNull List<KAOABerufsfeld> _berufsfelder;

	/** Die Lernabschnittsdaten */
	public final @NotNull List<SchuelerLernabschnittListeEintrag> _lernabschnitteAuswahl;

	/**
	 * Die Schuljahresabschnitte, in denen für den ausgewählten Schüler entsprechende Lernabschnitte und daraus resultierend anhand des Jahrgangs
	 * entsprechende KaoaKategorien vorhanden sind.
	 */
	public final @NotNull Set<Schuljahresabschnitt> _schuljahresabschnitteFiltered = new HashSet<>();

	/**
	 * Zusätzliche Maps, welche zum schnellen Zugriff auf Teilmengen der Liste
	 * verwendet werden können
	 */
	private final @NotNull Map<Long, List<KAOAMerkmalKatalogEintrag>> _mapMerkmalByKategorie = new HashMap<>();
	private final @NotNull Map<Long, List<KAOAZusatzmerkmalKatalogEintrag>> _mapZusatzmerkmalByMerkmal = new HashMap<>();
	private final @NotNull Map<Long, List<KAOAAnschlussoptionenKatalogEintrag>> _mapAnschlussoptionByZusatzmerkmal = new HashMap<>();
	private final @NotNull Map<Long, List<KAOAEbene4KatalogEintrag>> _mapEbene4ByZusatzmerkmal = new HashMap<>();
	private final @NotNull List<KAOABerufsfeldKatalogEintrag> _berufsfeldEintraege = new ArrayList<>();

	/**
	 * Erstellt einen neuen Manager mit den übergebenen KAoA Daten
	 *
	 * @param schuljahresabschnitt            Der Schuljahresabschnitt, auf den sich dien KAoA-Daten bezieht
	 * @param schuljahresabschnittSchule      Die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnitte           Der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform                       Die Schulform der Schule
	 * @param schuelerKAoA                    KAoA Daten des Schülers
	 * @param lernabschnitteAuswahl    Lernabschnittsdaten des Schülers
	 */
	public SchuelerKAoAManager(final long schuljahresabschnitt, final long schuljahresabschnittSchule,
			final @NotNull List<Schuljahresabschnitt> schuljahresabschnitte, final Schulform schulform,
			final @NotNull List<SchuelerKAoADaten> schuelerKAoA, final @NotNull List<SchuelerLernabschnittListeEintrag> lernabschnitteAuswahl) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, schuelerKAoA,
				comparator, _kaoaToId, _kaoaToId,
				Arrays.asList(new Pair<>("schuljahr", true), new Pair<>("kategorie", true)));
		this._kategorien = new AttributMitAuswahl<>(Arrays.asList(KAOAKategorie.values()), _kategorieToId, _comparatorKategorie,
				_eventHandlerFilterChanged);
		this._merkmale = new AttributMitAuswahl<>(Arrays.asList(KAOAMerkmal.values()), _merkmalToId, _comparatorMerkmal,
				_eventHandlerFilterChanged);
		this._zusatzmerkmale = new AttributMitAuswahl<>(Arrays.asList(KAOAZusatzmerkmal.values()), _zusatzmerkmalToId, _comparatorZusatzmerkmal,
				_eventHandlerFilterChanged);
		this._anschlussoptionen = new AttributMitAuswahl<>(Arrays.asList(KAOAAnschlussoptionen.values()), _anschlussoptionToId,
				_comparatorAnschlussoptionen, _eventHandlerFilterChanged);
		this._berufsfelder = Arrays.asList(KAOABerufsfeld.values());
		this._berufsfelder.sort(_comparatorBerufsfelder);
		this._lernabschnitteAuswahl = lernabschnitteAuswahl;
		this._ebene4 = new AttributMitAuswahl<>(Arrays.asList(KAOAEbene4.values()), _ebene4ToId, _comparatorEbene4, _eventHandlerFilterChanged);
		processSchuljahresabschnitte();
		initKAoA();
	}

	private void initKAoA() {
		for (final @NotNull KAOAKategorie kategorie : this._kategorien.list()) {
			final KAOAKategorieKatalogEintrag kategorieEintrag = kategorie.daten(getSchuljahr());
			if (kategorieEintrag == null)
				continue;
			final @NotNull List<KAOAMerkmalKatalogEintrag> merkmaleOfKategorie = new ArrayList<>();
			for (final @NotNull KAOAMerkmal merkmal : this._merkmale.list()) {
				final KAOAMerkmalKatalogEintrag merkmalEintrag = merkmal.daten(getSchuljahr());
				if (merkmalEintrag == null)
					continue;
				if (merkmalEintrag.kategorie.equals(kategorie.name()))
					merkmaleOfKategorie.add(merkmalEintrag);
				final @NotNull List<KAOAZusatzmerkmalKatalogEintrag> zusatzmerkmaleOfMerkmal = new ArrayList<>();
				for (final @NotNull KAOAZusatzmerkmal zusatzmerkmal : this._zusatzmerkmale.list()) {
					final KAOAZusatzmerkmalKatalogEintrag zusatzmerkmalEintrag = zusatzmerkmal.daten(getSchuljahr());
					if (zusatzmerkmalEintrag == null)
						continue;
					if (zusatzmerkmalEintrag.merkmal.equals(merkmal.name()))
						zusatzmerkmaleOfMerkmal.add(zusatzmerkmalEintrag);
					processZusatzmerkmal(zusatzmerkmalEintrag, zusatzmerkmal.name());
				}
				this._mapZusatzmerkmalByMerkmal.put(merkmalEintrag.id, zusatzmerkmaleOfMerkmal);
			}
			this._mapMerkmalByKategorie.put(kategorieEintrag.id, merkmaleOfKategorie);
		}
		processBerufsfelder();
	}

	private void processZusatzmerkmal(final @NotNull KAOAZusatzmerkmalKatalogEintrag zusatzmerkmalEintrag, final @NotNull String nameZusatzmerkmal) {
		final @NotNull List<KAOAAnschlussoptionenKatalogEintrag> anschlussoptionOfZusatzmerkmal = new ArrayList<>();
		for (final @NotNull KAOAAnschlussoptionen anschlussoption : this._anschlussoptionen.list()) {
			final KAOAAnschlussoptionenKatalogEintrag anschlussoptionEintrag = anschlussoption.daten(getSchuljahr());
			if (anschlussoptionEintrag == null)
				continue;
			for (final @NotNull String anzeigeMerkmal : anschlussoptionEintrag.anzeigeZusatzmerkmal)
				if (anzeigeMerkmal.equals(nameZusatzmerkmal))
					anschlussoptionOfZusatzmerkmal.add(anschlussoptionEintrag);
		}
		this._mapAnschlussoptionByZusatzmerkmal.put(zusatzmerkmalEintrag.id, anschlussoptionOfZusatzmerkmal);

		final @NotNull List<KAOAEbene4KatalogEintrag> ebene4OfZusatzmerkmal = new ArrayList<>();
		for (final @NotNull KAOAEbene4 ebene4 : this._ebene4.list()) {
			final KAOAEbene4KatalogEintrag ebene4Eintrag = ebene4.daten(getSchuljahr());
			if (ebene4Eintrag == null)
				continue;
			if (ebene4Eintrag.zusatzmerkmal.equals(nameZusatzmerkmal))
				ebene4OfZusatzmerkmal.add(ebene4Eintrag);
		}
		this._mapEbene4ByZusatzmerkmal.put(zusatzmerkmalEintrag.id, ebene4OfZusatzmerkmal);
	}

	private void processBerufsfelder() {
		for (final @NotNull KAOABerufsfeld berufsfeld : this._berufsfelder) {
			final KAOABerufsfeldKatalogEintrag eintrag = berufsfeld.daten(this.getSchuljahr());
			if (eintrag != null)
				_berufsfeldEintraege.add(eintrag);
		}
	}

	/**
	 * Prüft, ob der angegebene KAoA-Eintrag durch den Filter durchgelassen wird oder nicht.
	 *
	 * @param eintrag Der zu prüfende KAoA-Eintrag
	 *
	 * @return true, wenn der Eintrag den Filter passiert, und ansonsten false
	 */
	@Override
	protected boolean checkFilter(final @NotNull SchuelerKAoADaten eintrag) {
		if (this._kategorien.auswahlExists() && !this._kategorien.auswahlHasKey(eintrag.idKategorie))
			return false;
		if (this._merkmale.auswahlExists() && !this._merkmale.auswahlHasKey(eintrag.idMerkmal))
			return false;
		if (this._zusatzmerkmale.auswahlExists() && !this._zusatzmerkmale.auswahlHasKey(eintrag.idZusatzmerkmal))
			return false;
		if ((eintrag.idAnschlussoption != null) && this._anschlussoptionen.auswahlExists()
				&& !this._anschlussoptionen.auswahlHasKey(eintrag.idAnschlussoption))
			return false;
		return ((eintrag.idEbene4 != null) && this._ebene4.auswahlExists() && !this._ebene4.auswahlHasKey(eintrag.idEbene4));
	}

	/**
	 * Vergleicht zwei KAoA-Einträge anhand der spezifizierten Ordnung.
	 *
	 * @param a Der erste Eintrag
	 * @param b Der zweite Eintrag
	 *
	 * @return Das Ergebnis des Vergleichs (-1 kleiner, 0 gleich und 1 größer)
	 */
	@Override
	protected int compareAuswahl(final @NotNull SchuelerKAoADaten a, final @NotNull SchuelerKAoADaten b) {
		for (final Pair<String, Boolean> criteria : _order) {
			final String field = criteria.a;
			final boolean asc = (criteria.b == null) || criteria.b;
			if (!("schuljahr".equals(field) || "kategorie".equals(field)))
				throw new DeveloperNotificationException(
						"Fehler bei der Sortierung. Das Sortierkriterium wird vom SchuelerKAoAManager nicht "
								+ "unterstützt" + ".");
			final int cmp = comparator.compare(a, b);
			if (cmp == 0)
				continue;
			return asc ? cmp : -cmp;
		}
		return Long.compare(a.id, b.id);
	}

	/**
	 * Passt bei Änderungen an den Daten ggf. das Auswahl-Objekt an.
	 *
	 * @param eintrag           Der Auswahl-Eintrag
	 * @param schuelerKAoADaten Das neue KAoA-Daten-Objekt zu der Auswahl
	 */
	@Override
	protected boolean onSetDaten(final @NotNull SchuelerKAoADaten eintrag, final @NotNull SchuelerKAoADaten schuelerKAoADaten) {
		boolean updateEintrag = false;
		// Passe ggf. die Daten an ... (beim Patchen der Daten)
		if (schuelerKAoADaten.id != eintrag.id) {
			eintrag.id = schuelerKAoADaten.id;
			updateEintrag = true;
		}
		return updateEintrag;
	}

	/**
	 * Diese Methode gibt eine Liste der Schuljahresabschnitte zurück, in denen es einen Lernabschnitt für den ausgewählten Schüler und entsprechend
	 * der dazugehörigen Jahrgänge KAOAKategorieEinträge gibt.
	 */
	private void processSchuljahresabschnitte() {
		// Sammeln aller in den KaoaKategorien vorhandener Jahrgänge
		final Set<String> availableKuerzelJahrgang = new HashSet<>();
		for (final KAOAKategorie kategorie : this._kategorien.list()) {
			final KAOAKategorieKatalogEintrag daten = kategorie.daten(getSchuljahr());
			if (daten == null)
				throw new DeveloperNotificationException("keine Kategorie für das Schuljahr gefunden");
			for (final String jahrgang : daten.jahrgaenge)
				availableKuerzelJahrgang.add(jahrgang.substring(jahrgang.length() - 2));
		}
		// Filtern der Lernabschnitte des ausgewählten Schülers entsprechend der vorhandenen Jahrgänge
		final List<SchuelerLernabschnittListeEintrag> filteredEintraege = new ArrayList<>();

		for (final SchuelerLernabschnittListeEintrag lernabschnitt : this._lernabschnitteAuswahl) {
			if (availableKuerzelJahrgang.contains(lernabschnitt.jahrgang))
				filteredEintraege.add(lernabschnitt);
		}
		if (filteredEintraege.isEmpty())
			throw new DeveloperNotificationException("Keine passenden Kategorien für die vorhandenen Lernabschnitte gefunden");
		// Hinzufügen der Schuljahresabschnitte entsprechend der gefilterten Lernabschnitte
		final Set<Long> schuljahresabschnittIDs = new HashSet<>();
		for (final SchuelerLernabschnittListeEintrag lernabschnitt : filteredEintraege)
			schuljahresabschnittIDs.add(lernabschnitt.schuljahresabschnitt);
		for (final Schuljahresabschnitt schuljahresabschnitt : this.schuljahresabschnitte.list()) {
			if (schuljahresabschnittIDs.contains(schuljahresabschnitt.id))
				_schuljahresabschnitteFiltered.add(schuljahresabschnitt);
		}
	}

	/**
	 * Gibt das Kürzel vom Jahrgang abhängig vom Schuljahr und der LernabschnittsEinträge des ausgewählten Schülers zurück
	 *
	 * @param schuljahr   Schuljahr
	 *
	 * @return KürzelJahrgang
	 */
	public @NotNull String getKuerzelJahrgangBySchuljahr(final int schuljahr) {
		for (final SchuelerLernabschnittListeEintrag eintrag : _lernabschnitteAuswahl) {
			if ((eintrag.schuljahr == schuljahr))
				return eintrag.jahrgang;
		}
		throw new DeveloperNotificationException("Kein Jahrgang für das Schuljahr %d gefunden.".formatted(schuljahr));
	}

	/**
	 * Gibt alle KAoA Merkmale zurück, die der angegebenen Kategorie zugeordnet sind. Falls keine gefunden wurden, wird eine leere Liste zurückgegeben.
	 *
	 * @param kategorieEintrag Die Kategorie
	 *
	 * @return Die KAoA Merkmale
	 */
	public @NotNull List<KAOAMerkmalKatalogEintrag> getKAOAMerkmaleByKategorie(final @NotNull KAOAKategorieKatalogEintrag kategorieEintrag) {
		return MapUtils.getOrCreateArrayList(this._mapMerkmalByKategorie, kategorieEintrag.id);
	}

	/**
	 * Gibt alle Zusatzmerkmale zurück, die dem angegebenen Merkmal zugeordnet sind. Falls keine gefunden wurden, wird eine leere Liste zurückgegeben.
	 *
	 * @param merkmalEintrag Das Merkmal
	 *
	 * @return Die Zusatzmerkmale
	 */
	public @NotNull List<KAOAZusatzmerkmalKatalogEintrag> getKAOAZusatzmerkmaleByMerkmal(final @NotNull KAOAMerkmalKatalogEintrag merkmalEintrag) {
		return MapUtils.getOrCreateArrayList(this._mapZusatzmerkmalByMerkmal, merkmalEintrag.id);
	}

	/**
	 * Gibt alle KAoA Anschlussoptionen zurück, die dem angegebenen Zusatzmerkmal zugeordnet sind. Falls keine gefunden wurden, wird eine leere Liste
	 * zurückgegeben.
	 *
	 * @param zusatzmerkmalEintrag Das Zusatzmerkmal
	 *
	 * @return Die Anschlussoptionen
	 */
	public @NotNull List<KAOAAnschlussoptionenKatalogEintrag> getKAOAAnschlussoptionenByZusatzmerkmal(final @NotNull KAOAZusatzmerkmalKatalogEintrag zusatzmerkmalEintrag) {
		return MapUtils.getOrCreateArrayList(this._mapAnschlussoptionByZusatzmerkmal, zusatzmerkmalEintrag.id);
	}

	/**
	 * Gibt alle Ebene4 Optionen zurück, die dem angegebenen Zusatzmerkmal zugeordnet sind. Falls keine gefunden wurden, wird eine leere Liste zurückgegeben.
	 *
	 * @param zusatzmerkmalEintrag Das Zusatzmerkmal
	 *
	 * @return Die Ebene4 Optionen
	 */
	public @NotNull List<KAOAEbene4KatalogEintrag> getKAOAEbene4ByZusatzmerkmal(final @NotNull KAOAZusatzmerkmalKatalogEintrag zusatzmerkmalEintrag) {
		return MapUtils.getOrCreateArrayList(this._mapEbene4ByZusatzmerkmal, zusatzmerkmalEintrag.id);
	}

	/**
	 * Gibt alle Berufsfelder zurück.
	 *
	 * @return Die Berufsfelder
	 */
	public @NotNull List<KAOABerufsfeldKatalogEintrag> getKAOABerufsfelder() {
		return _berufsfeldEintraege;
	}
}
