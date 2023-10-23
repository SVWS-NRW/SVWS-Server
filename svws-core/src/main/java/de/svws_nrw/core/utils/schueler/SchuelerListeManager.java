package de.svws_nrw.core.utils.schueler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.gost.GostJahrgang;
import de.svws_nrw.core.data.jahrgang.JahrgangsListeEintrag;
import de.svws_nrw.core.data.klassen.KlassenListeEintrag;
import de.svws_nrw.core.data.kurse.KursListeEintrag;
import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import de.svws_nrw.core.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.types.SchuelerStatus;
import de.svws_nrw.core.types.schule.Schulgliederung;
import de.svws_nrw.core.utils.AttributeWithFilter;
import de.svws_nrw.core.utils.gost.GostAbiturjahrUtils;
import de.svws_nrw.core.utils.jahrgang.JahrgangsUtils;
import de.svws_nrw.core.utils.klassen.KlassenUtils;
import de.svws_nrw.core.utils.kurse.KursUtils;
import de.svws_nrw.core.utils.schule.SchuljahresabschnittsUtils;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zum Verwalten der Schüler-Listen.
 */
public class SchuelerListeManager {

	/** Ein Filter-Attribut für die Schülerliste. Dieses wird nicht für das Filtern der Schüler verwendet, sondern für eine Mehrfachauswahl */
	public final @NotNull AttributeWithFilter<@NotNull Long, @NotNull SchuelerListeEintrag> schueler;
	private static final @NotNull Function<@NotNull SchuelerListeEintrag, @NotNull Long> _schuelerToId = (final @NotNull SchuelerListeEintrag s) -> s.id;
	private final @NotNull HashMap2D<@NotNull Integer, @NotNull Long, @NotNull SchuelerListeEintrag> _mapSchuelerMitStatus = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull SchuelerListeEintrag> _mapSchuelerInJahrgang = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull SchuelerListeEintrag> _mapSchuelerInKlasse = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull SchuelerListeEintrag> _mapSchuelerInKurs = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull SchuelerListeEintrag> _mapSchuelerInSchuljahresabschnitt = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull Integer, @NotNull Long, @NotNull SchuelerListeEintrag> _mapSchuelerInAbiturjahrgang = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull String, @NotNull Long, @NotNull SchuelerListeEintrag> _mapSchuelerInSchulgliederung = new HashMap2D<>();

	/** Das Filter-Attribut für die Jahrgänge */
	public final @NotNull AttributeWithFilter<@NotNull Long, @NotNull JahrgangsListeEintrag> jahrgaenge;
	private static final @NotNull Function<@NotNull JahrgangsListeEintrag, @NotNull Long> _jahrgangToId = (final @NotNull JahrgangsListeEintrag jg) -> jg.id;

	/** Das Filter-Attribut für die Klassen */
	public final @NotNull AttributeWithFilter<@NotNull Long, @NotNull KlassenListeEintrag> klassen;
	private static final @NotNull Function<@NotNull KlassenListeEintrag, @NotNull Long> _klasseToId = (final @NotNull KlassenListeEintrag k) -> k.id;

	/** Das Filter-Attribut für die Kurse */
	public final @NotNull AttributeWithFilter<@NotNull Long, @NotNull KursListeEintrag> kurse;
	private static final @NotNull Function<@NotNull KursListeEintrag, @NotNull Long> _kursToId = (final @NotNull KursListeEintrag k) -> k.id;

	/** Das Filter-Attribut für die Schuljahresabschnitte - die Filterfunktion wird zur Zeit noch nicht genutzt */
	public final @NotNull AttributeWithFilter<@NotNull Long, @NotNull Schuljahresabschnitt> schuljahresabschnitte;
	private static final @NotNull Function<@NotNull Schuljahresabschnitt, @NotNull Long> _schuljahresabschnittToId = (final @NotNull Schuljahresabschnitt sja) -> sja.id;

	/** Das Filter-Attribut für die Abiturjahrgänge - die Filterfunktion wird zur Zeit noch nicht genutzt */
	public final @NotNull AttributeWithFilter<@NotNull Integer, @NotNull GostJahrgang> abiturjahrgaenge;
	private static final @NotNull Function<@NotNull GostJahrgang, @NotNull Integer> _abiturjahrgangToId = (final @NotNull GostJahrgang a) -> a.abiturjahr;

	/** Das Filter-Attribut für die Schulgliederungen */
	public final @NotNull AttributeWithFilter<@NotNull String, @NotNull Schulgliederung> schulgliederungen;
	private static final @NotNull Function<@NotNull Schulgliederung, @NotNull String> _schulgliederungToId = (final @NotNull Schulgliederung sg) -> sg.daten.kuerzel;
	private static final @NotNull Comparator<@NotNull Schulgliederung> _comparatorSchulgliederung = (final @NotNull Schulgliederung a, final @NotNull Schulgliederung b) -> a.ordinal() - b.ordinal();

	/** Das Filter-Attribut für den Schüler-Status */
	public final @NotNull AttributeWithFilter<@NotNull Integer, @NotNull SchuelerStatus> schuelerstatus;
	private static final @NotNull Function<@NotNull SchuelerStatus, @NotNull Integer> _schuelerstatusToId = (final @NotNull SchuelerStatus s) -> s.id;
	private static final @NotNull Comparator<@NotNull SchuelerStatus> _comparatorSchuelerStatus = (final @NotNull SchuelerStatus a, final @NotNull SchuelerStatus b) -> a.ordinal() - b.ordinal();


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schueler                die Liste der Schüler
	 * @param jahrgaenge              die Liste des Jahrgangskatalogs
	 * @param klassen                 die Liste des Klassenkatalogs
	 * @param kurse                   die Liste des Kurskatalogs
	 * @param schuljahresabschnitte   die Liste der Schuljahresabschnitte
	 * @param abiturjahrgaenge        die Liste der Abiturjahrgänge
	 */
	public SchuelerListeManager(final @NotNull List<@NotNull SchuelerListeEintrag> schueler,
			final @NotNull List<@NotNull JahrgangsListeEintrag> jahrgaenge,
			final @NotNull List<@NotNull KlassenListeEintrag> klassen,
			final @NotNull List<@NotNull KursListeEintrag> kurse,
			final @NotNull List<@NotNull Schuljahresabschnitt> schuljahresabschnitte,
			final @NotNull List<@NotNull GostJahrgang> abiturjahrgaenge) {
		this.schueler = new AttributeWithFilter<>(schueler, _schuelerToId, SchuelerUtils.comparator);
		initSchueler();
		this.jahrgaenge = new AttributeWithFilter<>(jahrgaenge, _jahrgangToId, JahrgangsUtils.comparator);
		this.klassen = new AttributeWithFilter<>(klassen, _klasseToId, KlassenUtils.comparator);
		this.kurse = new AttributeWithFilter<>(kurse, _kursToId, KursUtils.comparator);
		this.schuljahresabschnitte = new AttributeWithFilter<>(schuljahresabschnitte, _schuljahresabschnittToId, SchuljahresabschnittsUtils.comparator);
		this.abiturjahrgaenge = new AttributeWithFilter<>(abiturjahrgaenge, _abiturjahrgangToId, GostAbiturjahrUtils.comparator);
		this.schulgliederungen = new AttributeWithFilter<>(Arrays.asList(Schulgliederung.values()), _schulgliederungToId, _comparatorSchulgliederung);
		this.schuelerstatus = new AttributeWithFilter<>(Arrays.asList(SchuelerStatus.values()), _schuelerstatusToId, _comparatorSchuelerStatus);
	}


	private void initSchueler() {
		for (final @NotNull SchuelerListeEintrag s : this.schueler.list()) {
			this._mapSchuelerMitStatus.put(s.status, s.id, s);
			if (s.idJahrgang >= 0)
				this._mapSchuelerInJahrgang.put(s.idJahrgang, s.id, s);
			if (s.idKlasse >= 0)
				this._mapSchuelerInKlasse.put(s.idKlasse, s.id, s);
			for (final long idKurs : s.kurse)
				this._mapSchuelerInKurs.put(idKurs, s.id, s);
			if (s.idSchuljahresabschnitt >= 0)
				this._mapSchuelerInSchuljahresabschnitt.put(s.idSchuljahresabschnitt, s.id, s);
			if (s.abiturjahrgang != null)
				this._mapSchuelerInAbiturjahrgang.put(s.abiturjahrgang, s.id, s);
			if (!s.schulgliederung.isBlank())
				this._mapSchuelerInSchulgliederung.put(s.schulgliederung, s.id, s);
		}
	}


	/**
	 * Gibt eine gefilterte Liste der Schüler zurück. Als Filter werden dabei
	 * die Jahrgänge, die Klassen, die Kurs, die Schulgliederungen und der Schülerstatus
	 * beachtet.
	 *
	 * @return die gefilterte Liste
	 */
	private @NotNull List<@NotNull SchuelerListeEintrag> getFiltered() {
		final @NotNull List<@NotNull SchuelerListeEintrag> result = new ArrayList<>();
		for (final @NotNull SchuelerListeEintrag eintrag : this.schueler.list()) {
			if (this.jahrgaenge.filterAktiv() && (!this.jahrgaenge.filterHasKey(eintrag.idJahrgang)))
				continue;
			if (this.klassen.filterAktiv() && (!this.klassen.filterHasKey(eintrag.idKlasse)))
				continue;
			if (this.kurse.filterAktiv()) {
				boolean hatEinenKurs = false;
				for (final long idKurs : eintrag.kurse)
					if (this.kurse.filterHasKey(idKurs))
						hatEinenKurs = true;
				if (!hatEinenKurs)
					continue;
			}
			if (this.schulgliederungen.filterAktiv() && (!this.schulgliederungen.filterHasKey(eintrag.schulgliederung)))
				continue;
			if (this.schuelerstatus.filterAktiv() && (!this.schuelerstatus.filterHasKey(eintrag.status)))
				continue;
			result.add(eintrag);
		}
		return result;
	}

}
