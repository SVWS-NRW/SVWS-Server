package de.svws_nrw.core.utils.stundenplan;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import de.svws_nrw.core.adt.LongArrayKey;
import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.adt.set.AVLSet;
import de.svws_nrw.core.data.stundenplan.Stundenplan;
import de.svws_nrw.core.data.stundenplan.StundenplanAufsichtsbereich;
import de.svws_nrw.core.data.stundenplan.StundenplanFach;
import de.svws_nrw.core.data.stundenplan.StundenplanJahrgang;
import de.svws_nrw.core.data.stundenplan.StundenplanKalenderwochenzuordnung;
import de.svws_nrw.core.data.stundenplan.StundenplanKlasse;
import de.svws_nrw.core.data.stundenplan.StundenplanKlassenunterricht;
import de.svws_nrw.core.data.stundenplan.StundenplanKomplett;
import de.svws_nrw.core.data.stundenplan.StundenplanKurs;
import de.svws_nrw.core.data.stundenplan.StundenplanLehrer;
import de.svws_nrw.core.data.stundenplan.StundenplanPausenaufsicht;
import de.svws_nrw.core.data.stundenplan.StundenplanPausenzeit;
import de.svws_nrw.core.data.stundenplan.StundenplanRaum;
import de.svws_nrw.core.data.stundenplan.StundenplanSchiene;
import de.svws_nrw.core.data.stundenplan.StundenplanSchueler;
import de.svws_nrw.core.data.stundenplan.StundenplanUnterricht;
import de.svws_nrw.core.data.stundenplan.StundenplanUnterrichtsverteilung;
import de.svws_nrw.core.data.stundenplan.StundenplanZeitraster;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.Wochentag;
import de.svws_nrw.core.utils.BlockungsUtils;
import de.svws_nrw.core.utils.CollectionUtils;
import de.svws_nrw.core.utils.DateUtils;
import de.svws_nrw.core.utils.ListUtils;
import de.svws_nrw.core.utils.Map2DUtils;
import de.svws_nrw.core.utils.MapUtils;
import de.svws_nrw.core.utils.StringUtils;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager für die Daten eines Stundenplanes. Die Daten werden aus vier DTO-Objekten aggregiert.
 *
 * @author Benjamin A. Bartsch
 */
public class StundenplanManager {

	/** Umrechnung der (Soll) Stunden eines Unterrichts in Minuten. */
	public static final int FAKTOR_WOCHENSTUNDEN_ZU_MINUTEN = 45;

	// Comparators
	private static final @NotNull Comparator<@NotNull StundenplanAufsichtsbereich> _compAufsichtsbereich = (final @NotNull StundenplanAufsichtsbereich a, final @NotNull StundenplanAufsichtsbereich b) -> {
		final int result = a.kuerzel.compareTo(b.kuerzel);
		if (result != 0) return result;
		return Long.compare(a.id, b.id);
	};
	private static final @NotNull Comparator<@NotNull StundenplanFach> _compFach = (final @NotNull StundenplanFach a, final @NotNull StundenplanFach b) -> {
		if (a.sortierung < b.sortierung) return -1;
		if (a.sortierung > b.sortierung) return +1;
		final int result = a.kuerzel.compareTo(b.kuerzel);
		if (result != 0) return result;
		return Long.compare(a.id, b.id);
	};
	private static final @NotNull Comparator<@NotNull StundenplanJahrgang> _compJahrgang = (final @NotNull StundenplanJahrgang a, final @NotNull StundenplanJahrgang b) -> {
		final int result = a.kuerzel.compareTo(b.kuerzel);
		if (result != 0) return result;
		return Long.compare(a.id, b.id);
	};
	private static final @NotNull Comparator<@NotNull StundenplanKalenderwochenzuordnung> _compKWZ = (final @NotNull StundenplanKalenderwochenzuordnung a, final @NotNull StundenplanKalenderwochenzuordnung b) -> {
		if (a.jahr < b.jahr) return -1;
		if (a.jahr > b.jahr) return +1;
		if (a.kw < b.kw) return -1;
		if (a.kw > b.kw) return +1;
		if (a.wochentyp < b.wochentyp) return -1;
		if (a.wochentyp > b.wochentyp) return +1;
		return Long.compare(a.id, b.id);
	};
	private static final @NotNull Comparator<@NotNull StundenplanKlasse> _compKlasse = (final @NotNull StundenplanKlasse a, final @NotNull StundenplanKlasse b) -> {
		final int result = a.kuerzel.compareTo(b.kuerzel);
		if (result != 0) return result;
		return Long.compare(a.id, b.id);
	};
	private final @NotNull Comparator<@NotNull StundenplanKlassenunterricht> _compKlassenunterricht;
	private static final @NotNull Comparator<@NotNull StundenplanKurs> _compKurs = (final @NotNull StundenplanKurs a, final @NotNull StundenplanKurs b) -> {
		if (a.sortierung < b.sortierung) return -1;
		if (a.sortierung > b.sortierung) return +1;
		final int result = a.bezeichnung.compareTo(b.bezeichnung);
		if (result != 0) return result;
		return Long.compare(a.id, b.id);
	};
	private static final @NotNull Comparator<@NotNull StundenplanLehrer> _compLehrer = (final @NotNull StundenplanLehrer a, final @NotNull StundenplanLehrer b) -> {
		final int result = a.kuerzel.compareTo(b.kuerzel);
		if (result != 0) return result;
		return Long.compare(a.id, b.id);
	};
	private static final @NotNull Comparator<@NotNull StundenplanPausenaufsicht> _compPausenaufsicht = (final @NotNull StundenplanPausenaufsicht a, final @NotNull StundenplanPausenaufsicht b) -> Long.compare(a.id, b.id);
	private static final @NotNull Comparator<@NotNull StundenplanPausenzeit> _compPausenzeit = (final @NotNull StundenplanPausenzeit a, final @NotNull StundenplanPausenzeit b) -> {
		if (a.wochentag < b.wochentag) return -1;
		if (a.wochentag > b.wochentag) return +1;
		final int beginnA = a.beginn == null ? -1 : a.beginn;
		final int beginnB = b.beginn == null ? -1 : b.beginn;
		if (beginnA < beginnB) return -1;
		if (beginnA > beginnB) return +1;
		return Long.compare(a.id, b.id);
	};
	private static final @NotNull Comparator<@NotNull StundenplanRaum> _compRaum = (final @NotNull StundenplanRaum a, final @NotNull StundenplanRaum b) -> {
		final int result = a.kuerzel.compareTo(b.kuerzel);
		if (result != 0) return result;
		return Long.compare(a.id, b.id);
	};
	private static final @NotNull Comparator<@NotNull StundenplanSchiene> _compSchiene = (final @NotNull StundenplanSchiene a, final @NotNull StundenplanSchiene b) -> {
		if (a.idJahrgang < b.idJahrgang) return -1;
		if (a.idJahrgang > b.idJahrgang) return +1;
		if (a.nummer < b.nummer) return -1;
		if (a.nummer > b.nummer) return +1;
		return Long.compare(a.id, b.id);
	};
	private static final @NotNull Comparator<@NotNull StundenplanSchueler> _compSchueler = (final @NotNull StundenplanSchueler a, final @NotNull StundenplanSchueler b) -> {
		if (a.idKlasse < b.idKlasse) return -1;
		if (a.idKlasse > b.idKlasse) return +1;
		final int cmpNachname = a.nachname.compareTo(b.nachname);
		if (cmpNachname != 0) return cmpNachname;
		final int cmpVorname = a.vorname.compareTo(b.vorname);
		if (cmpVorname != 0) return cmpVorname;
		return Long.compare(a.id, b.id);
	};

	private final @NotNull Comparator<@NotNull StundenplanUnterricht> _compUnterricht;

	private static final @NotNull Comparator<@NotNull StundenplanZeitraster> _compZeitraster = (final @NotNull StundenplanZeitraster a, final @NotNull StundenplanZeitraster b) -> {
		if (a.wochentag < b.wochentag) return -1;
		if (a.wochentag > b.wochentag) return +1;
		if (a.unterrichtstunde < b.unterrichtstunde) return -1;
		if (a.unterrichtstunde > b.unterrichtstunde) return +1;
		return Long.compare(a.id, b.id);
	};

	// StundenplanAufsichtsbereich
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanAufsichtsbereich> _aufsichtsbereich_by_id = new HashMap<>();
	private final @NotNull HashMap<@NotNull String, @NotNull StundenplanAufsichtsbereich> _aufsichtsbereich_by_kuerzel = new HashMap<>();
	private final @NotNull List<@NotNull StundenplanAufsichtsbereich> _aufsichtsbereichmenge_sortiert = new ArrayList<>();

	// StundenplanFach
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanFach> _fach_by_id = new HashMap<>();
	private final @NotNull List<@NotNull StundenplanFach> _fachmenge_sortiert = new ArrayList<>();

	// StundenplanJahrgang
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanJahrgang> _jahrgang_by_id = new HashMap<>();
	private final @NotNull List<@NotNull StundenplanJahrgang> _jahrgangmenge_sortiert = new ArrayList<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanJahrgang>> _jahrgangmenge_by_idKurs = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanJahrgang>> _jahrgangmenge_by_idKlasse = new HashMap<>();

	// StundenplanKalenderwochenzuordnung
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanKalenderwochenzuordnung> _kwz_by_id = new HashMap<>();
	private final @NotNull List<@NotNull StundenplanKalenderwochenzuordnung> _kwzmenge_sortiert = new ArrayList<>();
	private final @NotNull HashMap2D<@NotNull Integer, @NotNull Integer, @NotNull StundenplanKalenderwochenzuordnung> _kwz_by_jahr_and_kw = new HashMap2D<>();

	// StundenplanKlasse
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanKlasse> _klasse_by_id = new HashMap<>();
	private final @NotNull List<@NotNull StundenplanKlasse> _klassenmenge_sortiert = new ArrayList<>();
	private final @NotNull List<@NotNull StundenplanKlasse> _klassenmenge_sichtbar_sortiert = new ArrayList<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanKlasse>> _klassenmenge_by_idKurs = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanKlasse>> _klassenmenge_by_idJahrgang = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanKlasse>> _klassenmenge_by_idSchueler = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanKlasse>> _klassenmenge_by_idPausenzeit = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanKlasse>> _klassenmenge_by_idUnterricht = new HashMap<>();

	// StundenplanKlassenunterricht
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull StundenplanKlassenunterricht> _klassenunterricht_by_idKlasse_and_idFach = new HashMap2D<>();
	private final @NotNull List<@NotNull StundenplanKlassenunterricht> _klassenunterrichtmenge = new ArrayList<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanKlassenunterricht>> _klassenunterrichtmenge_by_idKlasse = new HashMap<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull List<@NotNull StundenplanKlassenunterricht>> _klassenunterrichtmenge_by_idKlasse_and_idSchiene = new HashMap2D<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanKlassenunterricht>> _klassenunterrichtmenge_by_idSchueler = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanKlassenunterricht>> _klassenunterrichtmenge_by_idLehrer = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanKlassenunterricht>> _klassenunterrichtmenge_by_idSchiene = new HashMap<>();

	// StundenplanKurs
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanKurs> _kurs_by_id = new HashMap<>();
	private final @NotNull List<@NotNull StundenplanKurs> _kursmenge = new ArrayList<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanKurs>> _kursmenge_by_idSchueler = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanKurs>> _kursmenge_by_idSchiene = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanKurs>> _kursmenge_by_idLehrer = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanKurs>> _kursmenge_by_idKlasse = new HashMap<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull List<@NotNull StundenplanKurs>> _kursmenge_by_idKlasse_and_idSchiene = new HashMap2D<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanKurs>> _kursmenge_by_idJahrgang = new HashMap<>();

	// StundenplanLehrer
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanLehrer> _lehrer_by_id = new HashMap<>();
	private final @NotNull List<@NotNull StundenplanLehrer> _lehrermenge_sortiert = new ArrayList<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanLehrer>> _lehrermenge_by_idUnterricht = new HashMap<>();

	// StundenplanPausenaufsicht
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanPausenaufsicht> _pausenaufsicht_by_id = new HashMap<>();
	private final @NotNull List<@NotNull StundenplanPausenaufsicht> _pausenaufsichtmenge = new ArrayList<>();
	private final @NotNull HashMap<@NotNull Integer, @NotNull List<@NotNull StundenplanPausenaufsicht>> _pausenaufsichtmenge_by_wochentag = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanPausenaufsicht>> _pausenaufsichtmenge_by_idPausenzeit = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanPausenaufsicht>> _pausenaufsichtmenge_by_idLehrer = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanPausenaufsicht>> _pausenaufsichtmenge_by_idAufsichtsbereich = new HashMap<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull List<@NotNull StundenplanPausenaufsicht>> _pausenaufsichtmenge_by_idKlasse_and_idPausenzeit = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull List<@NotNull StundenplanPausenaufsicht>> _pausenaufsichtmenge_by_idLehrer_and_idPausenzeit = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull List<@NotNull StundenplanPausenaufsicht>> _pausenaufsichtmenge_by_idSchueler_and_idPausenzeit = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull List<@NotNull StundenplanPausenaufsicht>> _pausenaufsichtmenge_by_idJahrgang_and_idPausenzeit = new HashMap2D<>();

	// StundenplanPausenzeit
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanPausenzeit> _pausenzeit_by_id = new HashMap<>();
	private final @NotNull HashMap<@NotNull LongArrayKey, @NotNull StundenplanPausenzeit> _pausenzeit_by_tag_and_beginn_and_ende = new HashMap<>();
	private final @NotNull List<@NotNull StundenplanPausenzeit> _pausenzeitmenge_sortiert = new ArrayList<>();
	private final @NotNull List<@NotNull StundenplanPausenzeit> _pausenzeitmengeOhneLeere_sortiert = new ArrayList<>();
	private final @NotNull HashMap<@NotNull Integer, @NotNull List<@NotNull StundenplanPausenzeit>> _pausenzeitmenge_by_wochentag = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanPausenzeit>> _pausenzeitmenge_by_idKlasse = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanPausenzeit>> _pausenzeitmenge_by_idSchueler = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanPausenzeit>> _pausenzeitmenge_by_idLehrer = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanPausenzeit>> _pausenzeitmenge_by_idJahrgang = new HashMap<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Integer, @NotNull List<@NotNull StundenplanPausenzeit>> _pausenzeitmenge_by_idKlasse_and_wochentag = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Integer, @NotNull List<@NotNull StundenplanPausenzeit>> _pausenzeitmenge_by_idLehrer_and_wochentag = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Integer, @NotNull List<@NotNull StundenplanPausenzeit>> _pausenzeitmenge_by_idSchueler_and_wochentag = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Integer, @NotNull List<@NotNull StundenplanPausenzeit>> _pausenzeitmenge_by_idJahrgang_and_wochentag = new HashMap2D<>();
	private Integer _pausenzeitMinutenMin = null;
	private Integer _pausenzeitMinutenMax = null;
	private Integer _pausenzeitMinutenMinOhneLeere = null;
	private Integer _pausenzeitMinutenMaxOhneLeere = null;

	private int _pausenzeitWochentagMin = Wochentag.MONTAG.id;
	private int _pausenzeitWochentagMax = Wochentag.MONTAG.id;
	private @NotNull Wochentag @NotNull [] _pausenzeitWochentageAlsEnumRange = new Wochentag[] {Wochentag.MONTAG};

	// StundenplanRaum
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanRaum> _raum_by_id = new HashMap<>();
	private final @NotNull HashMap<@NotNull String, @NotNull StundenplanRaum> _raum_by_kuerzel = new HashMap<>();
	private final @NotNull List<@NotNull StundenplanRaum> _raummenge_sortiert = new ArrayList<>();

	// StundenplanSchiene
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanSchiene> _schiene_by_id = new HashMap<>();
	private final @NotNull List<@NotNull StundenplanSchiene> _schienenmenge = new ArrayList<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanSchiene>> _schienenmenge_by_idJahrgang = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanSchiene>> _schienenmenge_by_idUnterricht = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanSchiene>> _schienenmenge_by_idKlasse = new HashMap<>();

	// StundenplanSchueler
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanSchueler> _schueler_by_id = new HashMap<>();
	private final @NotNull List<@NotNull StundenplanSchueler> _schuelermenge = new ArrayList<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanSchueler>> _schuelermenge_by_idKlasse = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanSchueler>> _schuelermenge_by_idKurs = new HashMap<>();

	// StundenplanUnterricht
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanUnterricht> _unterricht_by_id = new HashMap<>();
	private final @NotNull List<@NotNull StundenplanUnterricht> _unterrichtmenge = new ArrayList<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanUnterricht>> _unterrichtmenge_by_idKlasse = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanUnterricht>> _unterrichtmenge_by_idRaum = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanUnterricht>> _unterrichtmenge_by_idLehrer = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanUnterricht>> _unterrichtmenge_by_idSchueler = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanUnterricht>> _unterrichtmenge_by_idSchiene = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanUnterricht>> _unterrichtmenge_by_idKurs = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanUnterricht>> _unterrichtmenge_by_idZeitraster = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanUnterricht>> _unterrichtmenge_by_idJahrgang = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanUnterricht>> _unterrichtmenge_by_idUnterricht = new HashMap<>();  // u --> Partner von u (inklusive u selbst)
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull List<@NotNull StundenplanUnterricht>> _unterrichtmenge_by_idKlasse_and_idZeitraster = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull List<@NotNull StundenplanUnterricht>> _unterrichtmenge_by_idRaum_and_idZeitraster = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull List<@NotNull StundenplanUnterricht>> _unterrichtmenge_by_idSchueler_and_idZeitraster = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull List<@NotNull StundenplanUnterricht>> _unterrichtmenge_by_idLehrer_and_idZeitraster = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull List<@NotNull StundenplanUnterricht>> _unterrichtmenge_by_idJahrgang_and_idZeitraster = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull List<@NotNull StundenplanUnterricht>> _unterrichtmenge_by_idKlasse_and_idFach = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Integer, @NotNull List<@NotNull StundenplanUnterricht>> _unterrichtmenge_by_idZeitraster_and_wochentyp = new HashMap2D<>();
	private boolean _unterrichtHatMultiWochen = false;

	// StundenplanZeitraster
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanZeitraster> _zeitraster_by_id = new HashMap<>();
	private final @NotNull HashMap2D<@NotNull Integer, @NotNull Integer, @NotNull StundenplanZeitraster> _zeitraster_by_wochentag_and_stunde = new HashMap2D<>();
	private final @NotNull List<@NotNull StundenplanZeitraster> _zeitrastermenge = new ArrayList<>();
	private final @NotNull List<@NotNull StundenplanZeitraster> _zeitrastermengeOhneLeere_sortiert = new ArrayList<>();
	private final @NotNull HashMap<@NotNull Integer, @NotNull List<@NotNull StundenplanZeitraster>> _zeitrastermenge_by_wochentag = new HashMap<>();
	private final @NotNull HashMap<@NotNull Integer, @NotNull List<@NotNull StundenplanZeitraster>> _zeitrastermenge_by_stunde = new HashMap<>();
	private Integer _zeitrasterMinutenMin = null;
	private Integer _zeitrasterMinutenMax = null;
	private Integer _zeitrasterMinutenMinOhneLeere = null;
	private Integer _zeitrasterMinutenMaxOhneLeere = null;
	private final @NotNull HashMap<@NotNull Integer, Integer> _zeitrasterMinutenMinByStunde = new HashMap<>();
	private final @NotNull HashMap<@NotNull Integer, Integer> _zeitrasterMinutenMaxByStunde = new HashMap<>();

	private int _zeitrasterWochentagMin = Wochentag.MONTAG.id;
	private int _zeitrasterWochentagMax = Wochentag.MONTAG.id;
	private @NotNull Wochentag @NotNull [] _zeitrasterWochentageAlsEnumRange = new Wochentag[] {Wochentag.MONTAG};

	private int _zeitrasterStundeMin = 1;
	private int _zeitrasterStundeMax = 1;
	private @NotNull int @NotNull [] _zeitrasterStundenRange = new int[] {1};

	private int _zeitrasterStundeMinOhneLeere = 1;
	private int _zeitrasterStundeMaxOhneLeere = 1;
	private @NotNull int @NotNull [] _zeitrasterStundenRangeOhneLeere = new int[] {1};

	// wert ... by
	private final @NotNull HashMap<@NotNull Long, @NotNull Double> _wertWochenminuten_by_idKurs = new HashMap<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull Double> _wertWochenminuten_by_idKlasse_und_idFach = new HashMap2D<>();

	// Stundenplan
	private final long _stundenplanID;
	private final int _stundenplanWochenTypModell;
	private final long _stundenplanSchuljahresAbschnittID;
	private final @NotNull String _stundenplanGueltigAb;
	private final @NotNull String _stundenplanGueltigBis;
	private final @NotNull String _stundenplanBezeichnung;



	/**
	 * Der {@link StundenplanManager} benötigt vier data-Objekte und baut damit eine Datenstruktur für schnelle Zugriffe auf.
	 *
	 * @param daten                 liefert die Grund-Daten des Stundenplanes.
	 * @param unterrichte           liefert die Informationen zu allen {@link StundenplanUnterricht} im Stundenplan. Die Liste darf leer sein.
	 * @param pausenaufsichten      liefert die Informationen zu allen {@link StundenplanPausenaufsicht} im Stundenplan. Die Liste darf leer sein.
	 * @param unterrichtsverteilung liefert die Informationen zu der Unterrichtsverteilung eines Stundenplans. Darf NULL sein.
	 */
	public StundenplanManager(final @NotNull Stundenplan daten, final @NotNull List<@NotNull StundenplanUnterricht> unterrichte, final @NotNull List<@NotNull StundenplanPausenaufsicht> pausenaufsichten, final StundenplanUnterrichtsverteilung unterrichtsverteilung) {
		_compKlassenunterricht = klassenunterrichtCreateComparator();
		_compUnterricht = unterrichtCreateComparator();
		_stundenplanID = daten.id;
		_stundenplanWochenTypModell = daten.wochenTypModell;
		_stundenplanSchuljahresAbschnittID = daten.idSchuljahresabschnitt;
		_stundenplanGueltigAb = daten.gueltigAb;
		_stundenplanGueltigBis = init_gueltig_bis(daten.gueltigAb, daten.gueltigBis);
		_stundenplanBezeichnung = daten.bezeichnungStundenplan;

		// Spezialfall: "unterrichtsverteilung" ist NULL
		StundenplanUnterrichtsverteilung uv = unterrichtsverteilung;
		if (uv == null) {
			uv = new StundenplanUnterrichtsverteilung();
			uv.id = _stundenplanID;
		}

		// Spezielle Prüfungen.
		DeveloperNotificationException.ifTrue("Stundenplan.id != StundenplanUnterrichtsverteilung.id", daten.id != uv.id);

		// Initialisierungen der Maps und Prüfung der Integrität.
		initAll(daten.kalenderwochenZuordnung,
				uv.faecher,
				daten.jahrgaenge,
				daten.zeitraster,
				daten.raeume,
				daten.pausenzeiten,
				daten.aufsichtsbereiche,
				uv.lehrer,
				uv.schueler,
				daten.schienen,
				uv.klassen,
				uv.klassenunterricht,
				pausenaufsichten,
				uv.kurse,
				unterrichte
				);

	}

	/**
	 * Dieser Manager baut mit Hilfe des {@link StundenplanKomplett}-Objektes eine Datenstruktur für schnelle Zugriffe auf.
	 *
	 * @param stundenplanKomplett  Beinhaltet alle relevanten Daten für einen Stundenplan.
	 */
	public StundenplanManager(final @NotNull StundenplanKomplett stundenplanKomplett) {
		_compKlassenunterricht = klassenunterrichtCreateComparator();
		_compUnterricht = unterrichtCreateComparator();
		_stundenplanID = stundenplanKomplett.daten.id;
		_stundenplanWochenTypModell = stundenplanKomplett.daten.wochenTypModell;
		_stundenplanSchuljahresAbschnittID = stundenplanKomplett.daten.idSchuljahresabschnitt;
		_stundenplanGueltigAb = stundenplanKomplett.daten.gueltigAb;
		_stundenplanGueltigBis = init_gueltig_bis(stundenplanKomplett.daten.gueltigAb, stundenplanKomplett.daten.gueltigBis);
		_stundenplanBezeichnung = stundenplanKomplett.daten.bezeichnungStundenplan;

		// Spezielle Prüfungen.
		DeveloperNotificationException.ifTrue("Stundenplan.id != StundenplanUnterrichtsverteilung.id", stundenplanKomplett.daten.id != stundenplanKomplett.unterrichtsverteilung.id);

		initAll(stundenplanKomplett.daten.kalenderwochenZuordnung,
				stundenplanKomplett.unterrichtsverteilung.faecher,
				stundenplanKomplett.daten.jahrgaenge,
				stundenplanKomplett.daten.zeitraster,
				stundenplanKomplett.daten.raeume,
				stundenplanKomplett.daten.pausenzeiten,
				stundenplanKomplett.daten.aufsichtsbereiche,
				stundenplanKomplett.unterrichtsverteilung.lehrer,
				stundenplanKomplett.unterrichtsverteilung.schueler,
				stundenplanKomplett.daten.schienen,
				stundenplanKomplett.unterrichtsverteilung.klassen,
				stundenplanKomplett.unterrichtsverteilung.klassenunterricht,
				stundenplanKomplett.pausenaufsichten,
				stundenplanKomplett.unterrichtsverteilung.kurse,
				stundenplanKomplett.unterrichte);
	}

	private static @NotNull String init_gueltig_bis(final @NotNull String gueltigAb, final String gueltigBis) {
		final @NotNull int[] infoVon = DateUtils.extractFromDateISO8601(gueltigAb);

		// Wenn "gueltigBis" valide ist, dann übernehmen...
		if (gueltigBis != null) {
			try {
				DateUtils.extractFromDateISO8601(gueltigBis);
				return gueltigBis;
			} catch (@SuppressWarnings("unused") final Exception ex) {
				// nichts
			}
		}

		// ... andernfalls, einen Default-Wert nehmen --> Letzter Tag im Schuljahr --> = 31.7.XXXX
		final int jahrAb = infoVon[0];
		final int monatAb = infoVon[1];
		return (monatAb <= 7 ? jahrAb : jahrAb + 1) + "-07-31";
	}

	private void initAll(final @NotNull List<@NotNull StundenplanKalenderwochenzuordnung> listKWZ,
			             final @NotNull List<@NotNull StundenplanFach> listFach,
			             final @NotNull List<@NotNull StundenplanJahrgang> listJahrgang,
			             final @NotNull List<@NotNull StundenplanZeitraster> listZeitraster,
			             final @NotNull List<@NotNull StundenplanRaum> listRaum,
			             final @NotNull List<@NotNull StundenplanPausenzeit> listPausenzeit,
			             final @NotNull List<@NotNull StundenplanAufsichtsbereich> listAufsichtsbereich,
			             final @NotNull List<@NotNull StundenplanLehrer> listLehrer,
			             final @NotNull List<@NotNull StundenplanSchueler> listSchueler,
			             final @NotNull List<@NotNull StundenplanSchiene> listSchiene,
			             final @NotNull List<@NotNull StundenplanKlasse> listKlasse,
			             final @NotNull List<@NotNull StundenplanKlassenunterricht> listKlassenunterricht,
			             final @NotNull List<@NotNull StundenplanPausenaufsicht> listPausenaufsicht,
			             final @NotNull List<@NotNull StundenplanKurs> listKurs,
			             final @NotNull List<@NotNull StundenplanUnterricht> listUnterricht) {

		DeveloperNotificationException.ifTrue("stundenplanWochenTypModell < 0", _stundenplanWochenTypModell < 0);
		DeveloperNotificationException.ifTrue("stundenplanWochenTypModell == 1", _stundenplanWochenTypModell == 1);

		kalenderwochenzuordnungAddAllOhneUpdate(listKWZ);          // ✔, referenziert ---
		fachAddAllOhneUpdate(listFach);                            // ✔, referenziert ---
		jahrgangAddAllOhneUpdate(listJahrgang);                    // ✔, referenziert ---
		zeitrasterAddAllOhneUpdate(listZeitraster);                // ✔, referenziert ---
		raumAddAllOhneUpdate(listRaum);                            // ✔, referenziert ---
		pausenzeitAddAllOhneUpdate(listPausenzeit);                // ✔, referenziert ---
		aufsichtsbereichAddAllOhneUpdate(listAufsichtsbereich);    // ✔, referenziert ---
		lehrerAddAllOhneUpdate(listLehrer);                        // ✔, referenziert [Fach]
		schuelerAddAllOhneUpdate(listSchueler);                    // ✔, referenziert Klasse
		klasseAddAllOhneUpdate(listKlasse);                        // ✔, referenziert [Jahrgang], [Schueler]
		schieneAddAllOhneUpdate(listSchiene);                      // ✔, referenziert Jahrgang
		klassenunterrichtAddAllOhneUpdate(listKlassenunterricht);  // ✔, referenziert Klasse, Fach, [Schiene], [Schueler], [Lehrer]
		pausenaufsichtAddAllOhneUpdate(listPausenaufsicht);        // ✔, referenziert Lehrer, Pausenzeit, [Aufsichtsbereich]
		kursAddAllOhneUpdate(listKurs);                            // ✔, referenziert [Schienen], [Jahrgang], [Schüler]
		unterrichtAddAllOhneUpdate(listUnterricht);                // ✔, referenziert Zeitraster, Kurs, Fach, [Lehrer], [Klasse], [Raum], [Schiene]

		update_all();
	}

	private void update_all() {

		// 0. Ordnung (Sortierte Mengen als Listen)
		update_kwzmenge_update_kwz_by_jahr_and_kw();                 // ---
		update_aufsichtsbereichmenge();                              // ---
		update_fachmenge();                                          // ---
		update_jahrgangmenge();                                      // ---
		update_klassenmenge();                                       // ---
		update_klassenunterrichtmenge();                             // ---
		update_kursmenge();                                          // ---
		update_lehrermenge();                                        // ---
		update_pausenaufsichtmenge();                                // ---
		update_raummenge();                                          // ---
		update_schienenmenge();                                      // ---
		update_schuelermenge();                                      // ---
		update_pausenzeitmenge();                                    // ---
		update_unterrichtmenge();                                    // ---
		update_zeitrastermenge();                                    // ---
		update_pausenaufsichtmenge_by_idPausenzeit();                // _pausenaufsichtmenge
		update_pausenzeitmengeOhnePausenaufsicht();                  // _pausenzeitmenge_sortiert,  _pausenaufsichtmenge_by_idPausenzeit
		update_unterrichtmenge_by_idZeitraster();                    // _unterrichtmenge
		update_zeitrastermengeOhneLeereUnterrichtmenge();            // _zeitrastermenge, _unterrichtmenge_by_idZeitraster

		// 1. Ordnung (nur Referenzen zu den sortierten Mengen)

		update_pausenzeit_by_tag_and_beginn_and_ende();              // _pausenzeitmenge_sortiert
		update_aufsichtsbereich_by_kuerzel();                        // _aufsichtsbereichmenge_sortiert
		update_raum_by_kuerzel();                                    // _raummenge_sortiert
		update_klassenmenge_by_idJahrgang();                         // _klassenmenge
		update_jahrgangmenge_by_idKlasse();                          // _klassenmenge
		update_klassenunterrichtmenge_by_idKlasse();                 // _klassenunterrichtmenge
		update_klassenunterrichtmenge_by_idSchueler();               // _klassenunterrichtmenge
		update_klassenunterrichtmenge_by_idLehrer();                 // _klassenunterrichtmenge
		update_klassenunterrichtmenge_by_idSchiene();                // _klassenunterrichtmenge
		update_kursmenge_by_idSchueler();                            // _kursmenge
		update_kursmenge_by_idLehrer();                              // _kursmenge
		update_kursmenge_by_idSchiene();                             // _kursmenge
		update_schuelermenge_by_idKurs();                            // _kursmenge
		update_kursmenge_by_idJahrgang();                            // _kursmenge
		update_jahrgangmenge_by_idKurs();                            // _kursmenge
		update_pausenaufsichtmenge_by_wochentag();                   // _pausenaufsichtmenge
		update_pausenaufsichtmenge_by_idLehrer();                    // _pausenaufsichtmenge
		update_pausenaufsichtmenge_by_idLehrer_and_idPausenzeit();   // _pausenaufsichtmenge
		update_pausenaufsichtmenge_by_idAufsichtsbereich();          // _pausenaufsichtmenge
		update_pausenzeitmenge_by_idLehrer();                        // _pausenaufsichtmenge
		update_pausenzeitmenge_by_wochentag();                       // _pausenzeitmenge
		update_klassenmenge_by_idPausenzeit();                       // _pausenzeitmenge, _klassenmenge
		update_schienenmenge_by_idJahrgang();                        // _schienenmenge
		update_schuelermenge_by_idKlasse();                          // _schuelermenge
		update_klassenmenge_by_idSchueler();                         // _schuelermenge
		update_lehrermenge_by_idUnterricht();                        // _unterrichtmenge
		update_schienenmenge_by_idUnterricht();                      // _unterrichtmenge
		update_unterrichtmenge_by_idSchiene();                       // _unterrichtmenge
		update_unterrichtmenge_by_idKurs();                          // _unterrichtmenge
		update_unterrichtmenge_by_idKlasse_and_idFach();             // _unterrichtmenge
		update_unterrichtmenge_by_idZeitraster_and_wochentyp();      // _unterrichtmenge
		update_unterrichtmenge_by_idLehrer();                        // _unterrichtmenge
		update_unterrichtmenge_by_idLehrer_and_idZeitraster();       // _unterrichtmenge
		update_unterrichtmenge_by_idRaum();                          // _unterrichtmenge
		update_unterrichtmenge_by_idRaum_and_idZeitraster();         // _unterrichtmenge
		update_zeitraster_by_wochentag_and_stunde();                 // _zeitrastermenge
		update_zeitrastermenge_by_wochentag();                       // _zeitrastermenge
		update_zeitrastermenge_by_stunde();                          // _zeitrastermenge

		// 2. Ordnung

		update_kursmenge_by_idKlasse();                              // _kursmenge, _schuelermenge_by_idKurs
		update_klassenmenge_by_idKurs();                             // _kursmenge, _schuelermenge_by_idKurs
		update_pausenzeitmenge_by_idLehrer_and_wochentag();          // _pausenzeitmenge_by_idLehrer
		update_pausenzeitmenge_by_idKlasse();                        // _pausenzeitmenge, _klassenmenge_by_idPausenzeit
		update_pausenzeitmenge_by_idJahrgang();                      // _pausenzeitmenge, _klassenmenge_by_idPausenzeit
		update_pausenzeitmenge_by_idSchueler();                      // _pausenzeitmenge, _klassenmenge_by_idPausenzeit, _schuelermenge_by_idKlasse
		update_pausenaufsichtmenge_by_idKlasse_and_idPausenzeit();   // _pausenaufsichtmenge, _klassenmenge_by_idPausenzeit
		update_pausenaufsichtmenge_by_idJahrgang_and_idPausenzeit(); // _pausenaufsichtmenge, _klassenmenge_by_idPausenzeit, _jahrgangmenge_by_idKlasse
		update_pausenaufsichtmenge_by_idSchueler_and_idPausenzeit(); // _pausenaufsichtmenge, _klassenmenge_by_idPausenzeit, _schuelermenge_by_idKlasse
		update_unterrichtmenge_by_idJahrgang();                      // _unterrichtmenge, _jahrgangmenge_by_idKlasse, _jahrgangmenge_by_idKurs
		update_unterrichtmenge_by_idSchueler();                      // _unterrichtmenge, _schuelermenge_by_idKlasse, _schuelermenge_by_idKurs
		update_klassenunterrichtmenge_by_idKlasse_and_idSchiene();   // _klassenunterrichtmenge_by_idKlasse
		update_wertWochenminuten_by_idKurs();                        // _kursmenge, _unterrichtmenge_by_idKurs
		update_wertWochenminuten_by_idKlasse_und_idFach();           // _klassenmenge, _fachmenge, _unterrichtmenge_by_idKlasse_and_idFach
		update_unterrichtmenge_by_idUnterricht();                    // _unterrichtmenge_by_idKurs, _unterrichtmenge_by_idKlasse_and_idFach

		// 3. Ordnung
		update_pausenzeitmenge_by_idKlasse_and_wochentag();          // _pausenzeitmenge_by_idKlasse
		update_pausenzeitmenge_by_idJahrgang_and_wochentag();        // _pausenzeitmenge_by_idJahrgang
		update_pausenzeitmenge_by_idSchueler_and_wochentag();        // _pausenzeitmenge_by_idSchueler
		update_unterrichtmenge_by_idKlasse();                        // _unterrichtmenge, _klassenmenge_by_idKurs
		update_unterrichtmenge_by_idKlasse_and_idZeitraster();       // _unterrichtmenge, _klassenmenge_by_idKurs
		update_klassenmenge_by_idUnterricht();                       // _unterrichtmenge, _klassenmenge_by_idKurs
		update_unterrichtmenge_by_idJahrgang_and_idZeitraster();     // _unterrichtmenge_by_idJahrgang
		update_unterrichtmenge_by_idSchueler_and_idZeitraster();     // _unterrichtmenge_by_idSchueler
		update_schienenmenge_by_idKlasse();                          // _klassenmenge, _kursmenge_by_idKlasse, _klassenunterrichtmenge_by_idKlasse
		update_kursmenge_by_idKlasse_and_idSchiene();                // _kursmenge_by_idKlasse
	}

	private void update_pausenzeit_by_tag_and_beginn_and_ende() {
		_pausenzeit_by_tag_and_beginn_and_ende.clear();
		for (final @NotNull StundenplanPausenzeit pausenzeit : _pausenzeitmenge_sortiert) {
			final long beginn = pausenzeit.beginn == null ? -1 : pausenzeit.beginn;
			final long ende = pausenzeit.ende == null ? -1 : pausenzeit.ende;
			final @NotNull LongArrayKey key = new LongArrayKey(new long[] {pausenzeit.wochentag, beginn, ende});
			_pausenzeit_by_tag_and_beginn_and_ende.put(key, pausenzeit);
		}
	}

	private void update_aufsichtsbereich_by_kuerzel() {
		_aufsichtsbereich_by_kuerzel.clear();
		for (final @NotNull StundenplanAufsichtsbereich aufsichtsbereich : _aufsichtsbereichmenge_sortiert)
			_aufsichtsbereich_by_kuerzel.put(aufsichtsbereich.kuerzel, aufsichtsbereich);
	}

	private void update_raum_by_kuerzel() {
		_raum_by_kuerzel.clear();
		for (final @NotNull StundenplanRaum raum : _raummenge_sortiert)
			_raum_by_kuerzel.put(raum.kuerzel, raum);
	}

	private void update_unterrichtmenge_by_idUnterricht() {
		_unterrichtmenge_by_idUnterricht.clear();

		// Kurs-Unterricht-Gruppen hinzufügen.
		for (final @NotNull List<@NotNull StundenplanUnterricht> menge : _unterrichtmenge_by_idKurs.values())
			for (final @NotNull StundenplanUnterricht u : menge)
				DeveloperNotificationException.ifMapPutOverwrites(_unterrichtmenge_by_idUnterricht, u.id, menge);

		// Klassen-Unterricht-Gruppen hinzufügen.
		for (final @NotNull List<@NotNull StundenplanUnterricht> menge : _unterrichtmenge_by_idKlasse_and_idFach.getNonNullValuesAsList())
			for (final @NotNull StundenplanUnterricht u : menge)
				DeveloperNotificationException.ifMapPutOverwrites(_unterrichtmenge_by_idUnterricht, u.id, menge);
	}

	private void update_wertWochenminuten_by_idKlasse_und_idFach() {
		_wertWochenminuten_by_idKlasse_und_idFach.clear();

		final double faktor = (_stundenplanWochenTypModell == 0) ? 1 : _stundenplanWochenTypModell;
		for (final @NotNull StundenplanKlasse klasse : _klassenmenge_sortiert)
			for (final @NotNull StundenplanFach fach : _fachmenge_sortiert) {
				double summe_minuten = 0;
				for (final @NotNull StundenplanUnterricht u : Map2DUtils.getOrCreateArrayList(_unterrichtmenge_by_idKlasse_and_idFach, klasse.id, fach.id)) {
					final @NotNull StundenplanZeitraster z = DeveloperNotificationException.ifMapGetIsNull(_zeitraster_by_id, u.idZeitraster);
					final @NotNull Integer ende = DeveloperNotificationException.ifNull("z.stundenende", z.stundenende);
					final @NotNull Integer beginn = DeveloperNotificationException.ifNull("z.stundenbeginn", z.stundenbeginn);
					final int minuten = ende - beginn;
					summe_minuten += (u.wochentyp == 0) ? minuten * faktor : minuten;
				}

				final double wochenminuten = summe_minuten / faktor;
				_wertWochenminuten_by_idKlasse_und_idFach.put(klasse.id, fach.id, wochenminuten);
			}

	}

	private void update_wertWochenminuten_by_idKurs() {
		_wertWochenminuten_by_idKurs.clear();

		final double faktor = (_stundenplanWochenTypModell == 0) ? 1 : _stundenplanWochenTypModell;
		for (final @NotNull StundenplanKurs kurs : _kursmenge) {
			double summe_minuten = 0;
			for (final @NotNull StundenplanUnterricht u : MapUtils.getOrCreateArrayList(_unterrichtmenge_by_idKurs, kurs.id)) {
				final @NotNull StundenplanZeitraster z = DeveloperNotificationException.ifMapGetIsNull(_zeitraster_by_id, u.idZeitraster);
				final @NotNull Integer ende = DeveloperNotificationException.ifNull("z.stundenende", z.stundenende);
				final @NotNull Integer beginn = DeveloperNotificationException.ifNull("z.stundenbeginn", z.stundenbeginn);
				final int minuten = ende - beginn;
				summe_minuten += (u.wochentyp == 0) ? minuten * faktor : minuten;
			}

			final double wochenminuten = summe_minuten / faktor;
			_wertWochenminuten_by_idKurs.put(kurs.id, wochenminuten);
		}

	}

	private void update_schienenmenge_by_idKlasse() {
		_schienenmenge_by_idKlasse.clear();

		for (final @NotNull StundenplanKlasse klasse : _klassenmenge_sortiert) {
			final @NotNull HashSet<@NotNull Long> schienenIDs = new HashSet<>();

			// Sammeln der Schienen aller Kurse (der Klasse).
			for (final @NotNull StundenplanKurs kurs : MapUtils.getOrCreateArrayList(_kursmenge_by_idKlasse, klasse.id))
				schienenIDs.addAll(kurs.schienen);

			// Sammeln der Schienen aller Klassenunterrichte (der Klasse).
			for (final @NotNull StundenplanKlassenunterricht klassenunterricht : MapUtils.getOrCreateArrayList(_klassenunterrichtmenge_by_idKlasse, klasse.id))
				schienenIDs.addAll(klassenunterricht.schienen);

			// Konvertieren und hinzufügen.
			for (final @NotNull Long idSchiene : schienenIDs) {
				final @NotNull StundenplanSchiene schiene = DeveloperNotificationException.ifMapGetIsNull(_schiene_by_id, idSchiene);
				MapUtils.addToList(_schienenmenge_by_idKlasse, klasse.id, schiene);
			}

			// Sortieren
			MapUtils.getOrCreateArrayList(_schienenmenge_by_idKlasse, klasse.id).sort(_compSchiene);
		}
	}

	private void update_kursmenge_by_idKlasse_and_idSchiene() {
		_kursmenge_by_idKlasse_and_idSchiene.clear();

		// Hinzufügen
		for (final @NotNull Long idKlasse : _kursmenge_by_idKlasse.keySet())
			for (final @NotNull StundenplanKurs kurs : MapUtils.getOrCreateArrayList(_kursmenge_by_idKlasse, idKlasse))
				if (kurs.schienen.isEmpty()) {
					Map2DUtils.addToList(_kursmenge_by_idKlasse_and_idSchiene, idKlasse, -1L, kurs);
				} else {
					for (final @NotNull Long idSchiene : kurs.schienen)
						Map2DUtils.addToList(_kursmenge_by_idKlasse_and_idSchiene, idKlasse, idSchiene, kurs);
				}

		// Sortieren
		for (final @NotNull Long idKlasse : _kursmenge_by_idKlasse_and_idSchiene.getKeySet())
			for (final @NotNull Long idSchiene : _kursmenge_by_idKlasse_and_idSchiene.getKeySetOf(idKlasse))
				_kursmenge_by_idKlasse_and_idSchiene.getNonNullOrException(idKlasse, idSchiene).sort(_compKurs);
	}

	private void update_klassenunterrichtmenge_by_idKlasse_and_idSchiene() {
		_klassenunterrichtmenge_by_idKlasse_and_idSchiene.clear();

		// Hinzufügen
		for (final @NotNull Long idKlasse : _klassenunterrichtmenge_by_idKlasse.keySet())
			for (final @NotNull StundenplanKlassenunterricht klassenunterricht : MapUtils.getOrCreateArrayList(_klassenunterrichtmenge_by_idKlasse, idKlasse))
				if (klassenunterricht.schienen.isEmpty()) {
					Map2DUtils.addToList(_klassenunterrichtmenge_by_idKlasse_and_idSchiene, idKlasse, -1L, klassenunterricht);
				} else {
					for (final @NotNull Long idSchiene : klassenunterricht.schienen)
						Map2DUtils.addToList(_klassenunterrichtmenge_by_idKlasse_and_idSchiene, idKlasse, idSchiene, klassenunterricht);
				}

		// Sortieren
		for (final @NotNull Long idKlasse : _klassenunterrichtmenge_by_idKlasse_and_idSchiene.getKeySet())
			for (final @NotNull Long idSchiene : _klassenunterrichtmenge_by_idKlasse_and_idSchiene.getKeySetOf(idKlasse))
				_klassenunterrichtmenge_by_idKlasse_and_idSchiene.getNonNullOrException(idKlasse, idSchiene).sort(_compKlassenunterricht);
	}

	private void update_schienenmenge_by_idUnterricht() {
		_schienenmenge_by_idUnterricht.clear();
		for (final @NotNull StundenplanUnterricht u : _unterrichtmenge) {
			for (final @NotNull Long idSchiene : u.schienen) {
				final @NotNull StundenplanSchiene schiene = DeveloperNotificationException.ifMapGetIsNull(_schiene_by_id, idSchiene);
				MapUtils.addToList(_schienenmenge_by_idUnterricht, u.id, schiene);
			}
			MapUtils.getOrCreateArrayList(_schienenmenge_by_idUnterricht, u.id).sort(_compSchiene);
		}
	}

	private void update_klassenmenge_by_idPausenzeit() {
		_klassenmenge_by_idPausenzeit.clear();
		for (final @NotNull StundenplanPausenzeit pausenzeit : _pausenzeitmenge_sortiert)
			if (pausenzeit.klassen.isEmpty()) {
				MapUtils.getOrCreateArrayList(_klassenmenge_by_idPausenzeit, pausenzeit.id).addAll(_klassenmenge_sortiert);
			} else {
				for (final @NotNull Long idKlasse : pausenzeit.klassen) {
					final @NotNull StundenplanKlasse klasse = DeveloperNotificationException.ifMapGetIsNull(_klasse_by_id, idKlasse);
					MapUtils.addToList(_klassenmenge_by_idPausenzeit, pausenzeit.id, klasse);
				}
				MapUtils.getOrCreateArrayList(_klassenmenge_by_idPausenzeit, pausenzeit.id).sort(_compKlasse);
			}
	}

	private void update_jahrgangmenge_by_idKlasse() {
		_jahrgangmenge_by_idKlasse.clear();
		for (final @NotNull StundenplanKlasse klasse : _klassenmenge_sortiert)
			for (final @NotNull Long idJahrgang : klasse.jahrgaenge) {
				final @NotNull StundenplanJahrgang jahrgang = DeveloperNotificationException.ifMapGetIsNull(_jahrgang_by_id, idJahrgang);
				MapUtils.addToList(_jahrgangmenge_by_idKlasse, klasse.id, jahrgang);
			}
	}

	private void update_jahrgangmenge_by_idKurs() {
		_jahrgangmenge_by_idKurs.clear();
		for (final @NotNull StundenplanKurs kurs : _kursmenge) {
			for (final @NotNull Long idJahrgang : kurs.jahrgaenge) {
				final @NotNull StundenplanJahrgang jahrgang = DeveloperNotificationException.ifMapGetIsNull(_jahrgang_by_id, idJahrgang);
				MapUtils.addToList(_jahrgangmenge_by_idKurs, kurs.id, jahrgang);
			}
			MapUtils.getOrCreateArrayList(_jahrgangmenge_by_idKurs, kurs.id).sort(_compJahrgang);
		}
	}

	private void update_klassenunterrichtmenge_by_idSchiene() {
		_klassenunterrichtmenge_by_idSchiene.clear();
		for (final @NotNull StundenplanKlassenunterricht klassenunterricht : _klassenunterrichtmenge)
			for (final @NotNull Long idSchiene : klassenunterricht.schienen)
				MapUtils.addToList(_klassenunterrichtmenge_by_idSchiene, idSchiene, klassenunterricht);
	}

	private void update_kursmenge_by_idSchiene() {
		_kursmenge_by_idSchiene.clear();
		for (final @NotNull StundenplanKurs kurs : _kursmenge)
			for (final @NotNull Long idSchiene : kurs.schienen)
				MapUtils.addToList(_kursmenge_by_idSchiene, idSchiene, kurs);
	}

	private void update_pausenaufsichtmenge_by_idAufsichtsbereich() {
		_pausenaufsichtmenge_by_idAufsichtsbereich.clear();
		for (final @NotNull StundenplanPausenaufsicht aufsicht : _pausenaufsichtmenge)
			for (final @NotNull Long idAufsichtsbereich : aufsicht.bereiche)
				MapUtils.addToList(_pausenaufsichtmenge_by_idAufsichtsbereich, idAufsichtsbereich, aufsicht);
	}

	private void update_pausenaufsichtmenge() {
		_pausenaufsichtmenge.clear();
		_pausenaufsichtmenge.addAll(_pausenaufsicht_by_id.values());
		_pausenaufsichtmenge.sort(_compPausenaufsicht);
	}

	private void update_pausenaufsichtmenge_by_wochentag() {
		_pausenaufsichtmenge_by_wochentag.clear();
		for (final @NotNull StundenplanPausenaufsicht aufsicht : _pausenaufsichtmenge) {
			final @NotNull StundenplanPausenzeit zeit = DeveloperNotificationException.ifMapGetIsNull(_pausenzeit_by_id, aufsicht.idPausenzeit);
			MapUtils.addToList(_pausenaufsichtmenge_by_wochentag, zeit.wochentag, aufsicht);
		}
	}

	private void update_pausenaufsichtmenge_by_idPausenzeit() {
		_pausenaufsichtmenge_by_idPausenzeit.clear();
		for (final @NotNull StundenplanPausenaufsicht aufsicht : _pausenaufsichtmenge)
			MapUtils.addToList(_pausenaufsichtmenge_by_idPausenzeit, aufsicht.idPausenzeit, aufsicht);
	}

	private void update_pausenaufsichtmenge_by_idJahrgang_and_idPausenzeit() {
		_pausenaufsichtmenge_by_idJahrgang_and_idPausenzeit.clear();
		for (final @NotNull StundenplanPausenaufsicht aufsicht : _pausenaufsichtmenge)
			for (final @NotNull StundenplanKlasse klasse : MapUtils.getOrCreateArrayList(_klassenmenge_by_idPausenzeit, aufsicht.idPausenzeit))
				for (final @NotNull StundenplanJahrgang jahrgang : MapUtils.getOrCreateArrayList(_jahrgangmenge_by_idKlasse, klasse.id))
					Map2DUtils.addToListIfNotExists(_pausenaufsichtmenge_by_idJahrgang_and_idPausenzeit, jahrgang.id, aufsicht.idPausenzeit, aufsicht);
	}

	private void update_pausenaufsichtmenge_by_idSchueler_and_idPausenzeit() {
		_pausenaufsichtmenge_by_idSchueler_and_idPausenzeit.clear();
		for (final @NotNull StundenplanPausenaufsicht aufsicht : _pausenaufsichtmenge)
			for (final @NotNull StundenplanKlasse klasse : MapUtils.getOrCreateArrayList(_klassenmenge_by_idPausenzeit, aufsicht.idPausenzeit))
				for (final @NotNull StundenplanSchueler schueler : MapUtils.getOrCreateArrayList(_schuelermenge_by_idKlasse, klasse.id))
					Map2DUtils.addToList(_pausenaufsichtmenge_by_idSchueler_and_idPausenzeit, schueler.id, aufsicht.idPausenzeit, aufsicht);
	}

	private void update_pausenaufsichtmenge_by_idKlasse_and_idPausenzeit() {
		_pausenaufsichtmenge_by_idKlasse_and_idPausenzeit.clear();
		for (final @NotNull StundenplanPausenaufsicht aufsicht : _pausenaufsichtmenge)
			for (final @NotNull StundenplanKlasse klasse : MapUtils.getOrCreateArrayList(_klassenmenge_by_idPausenzeit, aufsicht.idPausenzeit))
				Map2DUtils.addToList(_pausenaufsichtmenge_by_idKlasse_and_idPausenzeit, klasse.id, aufsicht.idPausenzeit, aufsicht);
	}

	private void update_pausenaufsichtmenge_by_idLehrer() {
		_pausenaufsichtmenge_by_idLehrer.clear();
		for (final @NotNull StundenplanPausenaufsicht aufsicht : _pausenaufsichtmenge)
			if (aufsicht.idLehrer >= 0)
				MapUtils.addToList(_pausenaufsichtmenge_by_idLehrer, aufsicht.idLehrer, aufsicht);
	}

	private void update_pausenaufsichtmenge_by_idLehrer_and_idPausenzeit() {
		_pausenaufsichtmenge_by_idLehrer_and_idPausenzeit.clear();
		for (final @NotNull StundenplanPausenaufsicht aufsicht : _pausenaufsichtmenge)
			if (aufsicht.idLehrer >= 0)
				Map2DUtils.addToList(_pausenaufsichtmenge_by_idLehrer_and_idPausenzeit, aufsicht.idLehrer, aufsicht.idPausenzeit, aufsicht);
	}

	private void update_klassenmenge_by_idSchueler() {
		_klassenmenge_by_idSchueler.clear();
		for (final @NotNull StundenplanSchueler schueler : _schuelermenge)
			if (schueler.idKlasse >= 0) {
				final @NotNull StundenplanKlasse klasse = DeveloperNotificationException.ifMapGetIsNull(_klasse_by_id, schueler.idKlasse);
				MapUtils.addToList(_klassenmenge_by_idSchueler, schueler.id, klasse);
			}
	}

	private void update_klassenmenge_by_idJahrgang() {
		_klassenmenge_by_idJahrgang.clear();
		for (final @NotNull StundenplanKlasse klasse : _klassenmenge_sortiert)
			for (final @NotNull Long idJahrgang : klasse.jahrgaenge)
				MapUtils.addToList(_klassenmenge_by_idJahrgang, idJahrgang, klasse);
	}

	private void update_kursmenge_by_idJahrgang() {
		_kursmenge_by_idJahrgang.clear();
		for (final @NotNull StundenplanKurs kurs : _kursmenge)
			for (final @NotNull Long idJahrgang : kurs.jahrgaenge)
				MapUtils.addToList(_kursmenge_by_idJahrgang, idJahrgang, kurs);
	}

	private void update_schienenmenge_by_idJahrgang() {
		_schienenmenge_by_idJahrgang.clear();
		for (final @NotNull StundenplanSchiene schiene : _schienenmenge)
			MapUtils.addToList(_schienenmenge_by_idJahrgang, schiene.idJahrgang, schiene);
	}

	private void update_pausenzeitmenge() {
		_pausenzeitmenge_sortiert.clear();
		_pausenzeitmenge_sortiert.addAll(_pausenzeit_by_id.values());
		_pausenzeitmenge_sortiert.sort(_compPausenzeit);

		_pausenzeitMinutenMin = null;
		_pausenzeitMinutenMax = null;
		_pausenzeitWochentagMin = Wochentag.SONNTAG.id + 1;
		_pausenzeitWochentagMax = Wochentag.MONTAG.id - 1;
		for (final @NotNull StundenplanPausenzeit p : _pausenzeitmenge_sortiert) {
			_pausenzeitMinutenMin = BlockungsUtils.minII(_pausenzeitMinutenMin, p.beginn);
			_pausenzeitMinutenMax = BlockungsUtils.maxII(_pausenzeitMinutenMax, p.ende);
			_pausenzeitWochentagMin = BlockungsUtils.minVI(_pausenzeitWochentagMin, p.wochentag);
			_pausenzeitWochentagMax = BlockungsUtils.maxVI(_pausenzeitWochentagMax, p.wochentag);
		}
		_pausenzeitWochentagMin = (_pausenzeitWochentagMin == Wochentag.SONNTAG.id + 1) ? Wochentag.MONTAG.id : _pausenzeitWochentagMin;
		_pausenzeitWochentagMax = (_pausenzeitWochentagMax == Wochentag.MONTAG.id - 1) ? Wochentag.MONTAG.id : _pausenzeitWochentagMax;

		// _pausenzeitWochentageAlsEnumRange
		_pausenzeitWochentageAlsEnumRange = new Wochentag[_pausenzeitWochentagMax - _pausenzeitWochentagMin + 1];
		for (int i = 0; i < _pausenzeitWochentageAlsEnumRange.length; i++)
			_pausenzeitWochentageAlsEnumRange[i] = Wochentag.fromIDorException(_pausenzeitWochentagMin + i);
	}

	private void update_pausenzeitmengeOhnePausenaufsicht() {
		_pausenzeitmengeOhneLeere_sortiert.clear();
		for (final @NotNull StundenplanPausenzeit zeit : _pausenzeitmenge_sortiert)
			if (!MapUtils.getOrCreateArrayList(_pausenaufsichtmenge_by_idPausenzeit, zeit.id).isEmpty())
				_pausenzeitmengeOhneLeere_sortiert.add(zeit);

		_pausenzeitMinutenMinOhneLeere = null;
		_pausenzeitMinutenMaxOhneLeere = null;
		for (final @NotNull StundenplanPausenzeit zeit : _pausenzeitmengeOhneLeere_sortiert) {
			_pausenzeitMinutenMinOhneLeere = BlockungsUtils.minII(_pausenzeitMinutenMinOhneLeere, zeit.beginn);
			_pausenzeitMinutenMaxOhneLeere = BlockungsUtils.maxII(_pausenzeitMinutenMaxOhneLeere, zeit.ende);
		}
	}

	private void update_pausenzeitmenge_by_wochentag() {
		_pausenzeitmenge_by_wochentag.clear();
		for (final @NotNull StundenplanPausenzeit zeit : _pausenzeitmenge_sortiert)
			MapUtils.addToList(_pausenzeitmenge_by_wochentag, zeit.wochentag, zeit);
	}

	private void update_pausenzeitmenge_by_idSchueler() {
		_pausenzeitmenge_by_idSchueler.clear();
		for (final @NotNull StundenplanPausenzeit pausenzeit : _pausenzeitmenge_sortiert)
			for (final @NotNull StundenplanKlasse klasse : MapUtils.getOrCreateArrayList(_klassenmenge_by_idPausenzeit, pausenzeit.id))
				for (final @NotNull StundenplanSchueler schueler : MapUtils.getOrCreateArrayList(_schuelermenge_by_idKlasse, klasse.id))
					MapUtils.addToList(_pausenzeitmenge_by_idSchueler, schueler.id, pausenzeit);
	}

	private void update_pausenzeitmenge_by_idSchueler_and_wochentag() {
		_pausenzeitmenge_by_idSchueler_and_wochentag.clear();
		for (final @NotNull Long idSchueler : _pausenzeitmenge_by_idSchueler.keySet())
			for (final @NotNull StundenplanPausenzeit zeit : MapUtils.getOrCreateArrayList(_pausenzeitmenge_by_idSchueler, idSchueler))
				Map2DUtils.addToList(_pausenzeitmenge_by_idSchueler_and_wochentag, idSchueler, zeit.wochentag, zeit);
	}

	private void update_pausenzeitmenge_by_idKlasse() {
		_pausenzeitmenge_by_idKlasse.clear();
		for (final @NotNull StundenplanPausenzeit pausenzeit : _pausenzeitmenge_sortiert)
			for (final @NotNull StundenplanKlasse klasse : MapUtils.getOrCreateArrayList(_klassenmenge_by_idPausenzeit, pausenzeit.id))
				MapUtils.addToList(_pausenzeitmenge_by_idKlasse, klasse.id, pausenzeit);
	}

	private void update_pausenzeitmenge_by_idKlasse_and_wochentag() {
		_pausenzeitmenge_by_idKlasse_and_wochentag.clear();
		for (final @NotNull Long idKlasse : _pausenzeitmenge_by_idKlasse.keySet())
			for (final @NotNull StundenplanPausenzeit pausenzeit : MapUtils.getOrCreateArrayList(_pausenzeitmenge_by_idKlasse, idKlasse))
				Map2DUtils.addToList(_pausenzeitmenge_by_idKlasse_and_wochentag, idKlasse, pausenzeit.wochentag, pausenzeit);
	}

	private void update_pausenzeitmenge_by_idLehrer() {
		_pausenzeitmenge_by_idLehrer.clear();
		for (final @NotNull StundenplanPausenaufsicht pausenaufsicht : _pausenaufsichtmenge)
			if (pausenaufsicht.idLehrer >= 0) {
				final @NotNull StundenplanPausenzeit pausenzeit = DeveloperNotificationException.ifMapGetIsNull(_pausenzeit_by_id, pausenaufsicht.idPausenzeit);
				MapUtils.addToList(_pausenzeitmenge_by_idLehrer, pausenaufsicht.idLehrer, pausenzeit);
			}
	}

	private void update_pausenzeitmenge_by_idLehrer_and_wochentag() {
		_pausenzeitmenge_by_idLehrer_and_wochentag.clear();
		for (final @NotNull Long idLehrer : _pausenzeitmenge_by_idLehrer.keySet())
			for (final @NotNull StundenplanPausenzeit pausenzeit : MapUtils.getOrCreateArrayList(_pausenzeitmenge_by_idLehrer, idLehrer))
				Map2DUtils.addToList(_pausenzeitmenge_by_idLehrer_and_wochentag, idLehrer, pausenzeit.wochentag, pausenzeit);
	}

	private void update_pausenzeitmenge_by_idJahrgang_and_wochentag() {
		_pausenzeitmenge_by_idJahrgang_and_wochentag.clear();
		for (final @NotNull Long idJahrgang : _pausenzeitmenge_by_idJahrgang.keySet())
			for (final @NotNull StundenplanPausenzeit pausenzeit : MapUtils.getOrCreateArrayList(_pausenzeitmenge_by_idJahrgang, idJahrgang))
				Map2DUtils.addToList(_pausenzeitmenge_by_idJahrgang_and_wochentag, idJahrgang, pausenzeit.wochentag, pausenzeit);
	}

	private void update_pausenzeitmenge_by_idJahrgang() {
		_pausenzeitmenge_by_idJahrgang.clear();
		for (final @NotNull StundenplanPausenzeit pausenzeit : _pausenzeitmenge_sortiert)
			for (final @NotNull StundenplanKlasse  klasse : MapUtils.getOrCreateArrayList(_klassenmenge_by_idPausenzeit, pausenzeit.id))
				for (final @NotNull Long idJahrgang : klasse.jahrgaenge)
					MapUtils.addToListIfNotExists(_pausenzeitmenge_by_idJahrgang, idJahrgang, pausenzeit);
	}

	private void update_klassenmenge_by_idKurs() {
		_klassenmenge_by_idKurs.clear();

		for (final @NotNull StundenplanKurs kurs : _kursmenge) {
			for (final @NotNull StundenplanSchueler schueler : MapUtils.getOrCreateArrayList(_schuelermenge_by_idKurs, kurs.id))
				if (schueler.idKlasse >= 0) {
					final @NotNull StundenplanKlasse klasse = DeveloperNotificationException.ifMapGetIsNull(_klasse_by_id, schueler.idKlasse);
					MapUtils.addToListIfNotExists(_klassenmenge_by_idKurs, kurs.id, klasse);
				}
			MapUtils.getOrCreateArrayList(_klassenmenge_by_idKurs, kurs.id).sort(_compKlasse);
		}
	}

	private void update_aufsichtsbereichmenge() {
		_aufsichtsbereichmenge_sortiert.clear();
		_aufsichtsbereichmenge_sortiert.addAll(_aufsichtsbereich_by_id.values());
		_aufsichtsbereichmenge_sortiert.sort(_compAufsichtsbereich);
	}

	private void update_fachmenge() {
		_fachmenge_sortiert.clear();
		_fachmenge_sortiert.addAll(_fach_by_id.values());
		_fachmenge_sortiert.sort(_compFach);
	}

	private void update_jahrgangmenge() {
		_jahrgangmenge_sortiert.clear();
		_jahrgangmenge_sortiert.addAll(_jahrgang_by_id.values());
		_jahrgangmenge_sortiert.sort(_compJahrgang);
	}

	private void update_kwzmenge_update_kwz_by_jahr_and_kw() {
		// _list_kwz
		_kwzmenge_sortiert.clear();
		_kwzmenge_sortiert.addAll(_kwz_by_id.values());

		// _map2d_jahr_kw_zu_kwz (Original - Objekte)
		_kwz_by_jahr_and_kw.clear();
		for (final @NotNull StundenplanKalenderwochenzuordnung kwz : _kwzmenge_sortiert)
			DeveloperNotificationException.ifMap2DPutOverwrites(_kwz_by_jahr_and_kw, kwz.jahr, kwz.kw, kwz);

		// _map2d_jahr_kw_zu_kwz (Pseudo - Objekte)
		final @NotNull int[] infoVon = DateUtils.extractFromDateISO8601(_stundenplanGueltigAb);
		final @NotNull int[] infoBis = DateUtils.extractFromDateISO8601(_stundenplanGueltigBis);
		final int jahrVon = infoVon[6]; // 6 = kalenderwochenjahr
		final int jahrBis = infoBis[6]; // 6 = kalenderwochenjahr
		final int kwVon = infoVon[5]; // 5 = kalenderwoche
		final int kwBis = infoBis[5]; // 5 = kalenderwoche
		DeveloperNotificationException.ifTrue("Das Start-Datum '" + _stundenplanGueltigAb + "' ist größer als das End-Datum '" + _stundenplanGueltigBis + "'!", (jahrVon > jahrBis) || ((jahrVon == jahrBis) && (kwVon > kwBis)));

		for (int jahr = jahrVon; jahr <= jahrBis; jahr++) {
			final int von = (jahr == jahrVon) ? kwVon : 1;
			final int bis = (jahr == jahrBis) ? kwBis : DateUtils.gibKalenderwochenOfJahr(jahr);
			for (int kw = von; kw <= bis; kw++)
				if (!_kwz_by_jahr_and_kw.contains(jahr, kw)) { // Überschreibe Original-Objekte der DB nicht!
					final @NotNull StundenplanKalenderwochenzuordnung kwz = new StundenplanKalenderwochenzuordnung();
					kwz.id = -1;
					kwz.jahr = jahr;
					kwz.kw = kw;
					kwz.wochentyp = kalenderwochenzuordnungGetWochentypOrDefault(jahr, kw);
					// Hinzufügen
					DeveloperNotificationException.ifMap2DPutOverwrites(_kwz_by_jahr_and_kw, kwz.jahr, kwz.kw, kwz);
					_kwzmenge_sortiert.add(kwz);
				}
		}

		_kwzmenge_sortiert.sort(_compKWZ);
	}

	private void update_klassenmenge() {
		_klassenmenge_sortiert.clear();
		_klassenmenge_sortiert.addAll(_klasse_by_id.values());
		_klassenmenge_sortiert.sort(_compKlasse);
		for (final StundenplanKlasse kl : _klassenmenge_sortiert)
			if (kl.istSichtbar)
				_klassenmenge_sichtbar_sortiert.add(kl);
	}

	private void update_klassenunterrichtmenge() {
		_klassenunterrichtmenge.clear();
		_klassenunterrichtmenge.addAll(_klassenunterricht_by_idKlasse_and_idFach.getNonNullValuesAsList());
		_klassenunterrichtmenge.sort(_compKlassenunterricht);
	}

	private void update_klassenunterrichtmenge_by_idKlasse() {
		_klassenunterrichtmenge_by_idKlasse.clear();
		for (final @NotNull StundenplanKlassenunterricht klassenunterricht : _klassenunterrichtmenge)
			MapUtils.addToList(_klassenunterrichtmenge_by_idKlasse, klassenunterricht.idKlasse, klassenunterricht);
	}

	private void update_klassenunterrichtmenge_by_idSchueler() {
		_klassenunterrichtmenge_by_idSchueler.clear();
		for (final @NotNull StundenplanKlassenunterricht klassenunterricht : _klassenunterrichtmenge)
			for (final @NotNull Long idSchueler : klassenunterricht.schueler)
				MapUtils.addToList(_klassenunterrichtmenge_by_idSchueler, idSchueler, klassenunterricht);
	}

	private void update_klassenunterrichtmenge_by_idLehrer() {
		_klassenunterrichtmenge_by_idLehrer.clear();
		for (final @NotNull StundenplanKlassenunterricht klassenunterricht : _klassenunterrichtmenge)
			for (final @NotNull Long idLehrer : klassenunterricht.lehrer)
				MapUtils.addToList(_klassenunterrichtmenge_by_idLehrer, idLehrer, klassenunterricht);
	}

	private void update_kursmenge() {
		_kursmenge.clear();
		_kursmenge.addAll(_kurs_by_id.values());
		_kursmenge.sort(_compKurs);
	}

	private void update_kursmenge_by_idSchueler() {
		_kursmenge_by_idSchueler.clear();
		for (final @NotNull StundenplanKurs kurs : _kursmenge)
			for (final @NotNull Long idSchueler : kurs.schueler)
				MapUtils.addToList(_kursmenge_by_idSchueler, idSchueler, kurs);
	}

	private void update_kursmenge_by_idLehrer() {
		_kursmenge_by_idLehrer.clear();
		for (final @NotNull StundenplanKurs kurs : _kursmenge)
			for (final @NotNull Long idLehrer : kurs.lehrer)
				MapUtils.addToList(_kursmenge_by_idLehrer, idLehrer, kurs);
	}

	private void update_kursmenge_by_idKlasse() {
		_kursmenge_by_idKlasse.clear();
		for (final @NotNull StundenplanKurs kurs : _kursmenge)
			for (final @NotNull StundenplanSchueler schueler : MapUtils.getOrCreateArrayList(_schuelermenge_by_idKurs, kurs.id))
				if (schueler.idKlasse >= 0)
					MapUtils.addToListIfNotExists(_kursmenge_by_idKlasse, schueler.idKlasse, kurs);
	}

	private void update_lehrermenge() {
		_lehrermenge_sortiert.clear();
		_lehrermenge_sortiert.addAll(_lehrer_by_id.values());
		_lehrermenge_sortiert.sort(_compLehrer);
	}

	private void update_lehrermenge_by_idUnterricht() {
		_lehrermenge_by_idUnterricht.clear();
		for (final @NotNull StundenplanUnterricht u : _unterrichtmenge) {
			for (final @NotNull Long idLehrer : u.lehrer) {
				final @NotNull StundenplanLehrer lehrer = DeveloperNotificationException.ifMapGetIsNull(_lehrer_by_id, idLehrer);
				MapUtils.addToList(_lehrermenge_by_idUnterricht, u.id, lehrer);
			}
			MapUtils.getOrCreateArrayList(_lehrermenge_by_idUnterricht, u.id).sort(_compLehrer);
		}
	}


	private void update_raummenge() {
		_raummenge_sortiert.clear();
		_raummenge_sortiert.addAll(_raum_by_id.values());
		_raummenge_sortiert.sort(_compRaum);
	}

	private void update_schienenmenge() {
		_schienenmenge.clear();
		_schienenmenge.addAll(_schiene_by_id.values());
		_schienenmenge.sort(_compSchiene);
	}

	private void update_schuelermenge() {
		_schuelermenge.clear();
		_schuelermenge.addAll(_schueler_by_id.values());
		_schuelermenge.sort(_compSchueler);
	}

	private void update_schuelermenge_by_idKlasse() {
		_schuelermenge_by_idKlasse.clear();
		for (final @NotNull StundenplanSchueler schueler : _schuelermenge)
			if (schueler.idKlasse >= 0)
				MapUtils.addToList(_schuelermenge_by_idKlasse, schueler.idKlasse, schueler);
	}

	private void update_schuelermenge_by_idKurs() {
		_schuelermenge_by_idKurs.clear();
		for (final @NotNull StundenplanKurs kurs : _kursmenge) {
			for (final @NotNull Long idSchueler : kurs.schueler) {
				final @NotNull StundenplanSchueler schueler = DeveloperNotificationException.ifMapGetIsNull(_schueler_by_id, idSchueler);
				MapUtils.addToList(_schuelermenge_by_idKurs, kurs.id, schueler);
			}
			MapUtils.getOrCreateArrayList(_schuelermenge_by_idKurs, kurs.id).sort(_compSchueler);
		}
	}

    private void update_unterrichtmenge() {
		_unterrichtmenge.clear();
		_unterrichtmenge.addAll(_unterricht_by_id.values());
		_unterrichtmenge.sort(_compUnterricht);

		_unterrichtHatMultiWochen = false;
		for (final @NotNull StundenplanUnterricht u : _unterrichtmenge)
			if (u.wochentyp > 0) {
				_unterrichtHatMultiWochen = true;
				break;
			}
	}

	private void update_unterrichtmenge_by_idLehrer() {
		_unterrichtmenge_by_idLehrer.clear();
		for (final @NotNull StundenplanUnterricht u : _unterrichtmenge)
			for (final @NotNull Long idLehrer : u.lehrer)
				MapUtils.addToList(_unterrichtmenge_by_idLehrer, idLehrer, u);
	}

	private void update_unterrichtmenge_by_idLehrer_and_idZeitraster() {
		_unterrichtmenge_by_idLehrer_and_idZeitraster.clear();
		for (final @NotNull StundenplanUnterricht u : _unterrichtmenge)
			for (final @NotNull Long idLehrer : u.lehrer)
				Map2DUtils.addToList(_unterrichtmenge_by_idLehrer_and_idZeitraster, idLehrer, u.idZeitraster, u);
	}

	private void update_unterrichtmenge_by_idKlasse() {
		_unterrichtmenge_by_idKlasse.clear();

		for (final @NotNull StundenplanUnterricht u : _unterrichtmenge)
			if (u.idKurs == null) {
				// Klassenunterricht
				for (final @NotNull Long idKlasse : u.klassen)
					MapUtils.addToList(_unterrichtmenge_by_idKlasse, idKlasse, u);
			} else {
				// Kursunterricht
				for (final @NotNull StundenplanKlasse klasse : MapUtils.getOrCreateArrayList(_klassenmenge_by_idKurs, u.idKurs))
					MapUtils.addToList(_unterrichtmenge_by_idKlasse, klasse.id, u);
			}
	}

    private void update_unterrichtmenge_by_idKlasse_and_idZeitraster() {
		_unterrichtmenge_by_idKlasse_and_idZeitraster.clear();

		for (final @NotNull StundenplanUnterricht u : _unterrichtmenge)
			if (u.idKurs == null) {
				// Klassenunterricht
				for (final @NotNull Long idKlasse : u.klassen)
					Map2DUtils.addToList(_unterrichtmenge_by_idKlasse_and_idZeitraster, idKlasse, u.idZeitraster, u);
			} else {
				// Kursunterricht
				for (final @NotNull StundenplanKlasse klasse : MapUtils.getOrCreateArrayList(_klassenmenge_by_idKurs, u.idKurs))
					Map2DUtils.addToList(_unterrichtmenge_by_idKlasse_and_idZeitraster, klasse.id, u.idZeitraster, u);
			}
    }

	private void update_unterrichtmenge_by_idSchueler_and_idZeitraster() {
		_unterrichtmenge_by_idSchueler_and_idZeitraster.clear();
		for (final @NotNull Long idSchueler : _unterrichtmenge_by_idSchueler.keySet())
			for (final @NotNull StundenplanUnterricht u : MapUtils.getOrCreateArrayList(_unterrichtmenge_by_idSchueler, idSchueler))
				Map2DUtils.addToList(_unterrichtmenge_by_idSchueler_and_idZeitraster, idSchueler, u.idZeitraster, u);
	}

	private void update_unterrichtmenge_by_idKurs() {
		_unterrichtmenge_by_idKurs.clear();
		for (final @NotNull StundenplanUnterricht u : _unterrichtmenge)
			if (u.idKurs != null)
				MapUtils.addToList(_unterrichtmenge_by_idKurs, u.idKurs, u);
	}

	private void update_unterrichtmenge_by_idKlasse_and_idFach() {
		_unterrichtmenge_by_idKlasse_and_idFach.clear();
		for (final @NotNull StundenplanUnterricht u : _unterrichtmenge)
			if (u.idKurs == null)
				for (final @NotNull Long idKlasse : u.klassen)
					Map2DUtils.addToList(_unterrichtmenge_by_idKlasse_and_idFach, idKlasse, u.idFach, u);
	}

	private void update_unterrichtmenge_by_idZeitraster() {
		_unterrichtmenge_by_idZeitraster.clear();
		for (final @NotNull StundenplanUnterricht u : _unterrichtmenge)
			MapUtils.addToList(_unterrichtmenge_by_idZeitraster, u.idZeitraster, u);
	}

	private void update_unterrichtmenge_by_idZeitraster_and_wochentyp() {
		_unterrichtmenge_by_idZeitraster_and_wochentyp.clear();
		for (final @NotNull StundenplanUnterricht u : _unterrichtmenge)
			Map2DUtils.addToList(_unterrichtmenge_by_idZeitraster_and_wochentyp, u.idZeitraster, u.wochentyp, u);
	}

	private void update_unterrichtmenge_by_idRaum() {
		_unterrichtmenge_by_idRaum.clear();
		for (final @NotNull StundenplanUnterricht u : _unterrichtmenge)
			for (final @NotNull Long idRaum : u.raeume)
				MapUtils.addToList(_unterrichtmenge_by_idRaum, idRaum, u);
	}

	private void update_unterrichtmenge_by_idRaum_and_idZeitraster() {
		_unterrichtmenge_by_idRaum_and_idZeitraster.clear();
		for (final @NotNull StundenplanUnterricht u : _unterrichtmenge)
			for (final @NotNull Long idRaum : u.raeume)
				Map2DUtils.addToList(_unterrichtmenge_by_idRaum_and_idZeitraster, idRaum, u.idZeitraster, u);
	}

	private void update_unterrichtmenge_by_idSchueler() {
		_unterrichtmenge_by_idSchueler.clear();
		for (final @NotNull StundenplanUnterricht u : _unterrichtmenge)
			if (u.idKurs == null) {
				// Klassenunterricht
				for (final @NotNull Long idKlasse : u.klassen)
					for (final @NotNull StundenplanSchueler s : MapUtils.getOrCreateArrayList(_schuelermenge_by_idKlasse, idKlasse))
						MapUtils.addToList(_unterrichtmenge_by_idSchueler, s.id, u);
			} else {
				// Kursunterricht
				for (final @NotNull StundenplanSchueler s : MapUtils.getOrCreateArrayList(_schuelermenge_by_idKurs, u.idKurs))
					MapUtils.addToList(_unterrichtmenge_by_idSchueler, s.id, u);
			}
	}

	private void update_unterrichtmenge_by_idJahrgang() {
		_unterrichtmenge_by_idJahrgang.clear();
		for (final @NotNull StundenplanUnterricht u : _unterrichtmenge)
			if (u.idKurs == null) {
				// Klassenunterricht
				for (final @NotNull Long idKlasse : u.klassen)
					for (final @NotNull StundenplanJahrgang jahrgang : MapUtils.getOrCreateArrayList(_jahrgangmenge_by_idKlasse, idKlasse))
						MapUtils.addToListIfNotExists(_unterrichtmenge_by_idJahrgang, jahrgang.id, u);
			} else {
				// Kursunterricht
				for (final @NotNull StundenplanJahrgang jahrgang : MapUtils.getOrCreateArrayList(_jahrgangmenge_by_idKurs, u.idKurs))
					MapUtils.addToList(_unterrichtmenge_by_idJahrgang, jahrgang.id, u);
			}
	}

	private void update_unterrichtmenge_by_idJahrgang_and_idZeitraster() {
		_unterrichtmenge_by_idJahrgang_and_idZeitraster.clear();
		for (final @NotNull Long idJahrgang : _unterrichtmenge_by_idJahrgang.keySet())
			for (final @NotNull StundenplanUnterricht u : MapUtils.getOrCreateArrayList(_unterrichtmenge_by_idJahrgang, idJahrgang))
				Map2DUtils.addToList(_unterrichtmenge_by_idJahrgang_and_idZeitraster, idJahrgang, u.idZeitraster, u);
	}

	private void update_unterrichtmenge_by_idSchiene() {
		_unterrichtmenge_by_idSchiene.clear();
		for (final @NotNull StundenplanUnterricht u : _unterrichtmenge)
			for (final @NotNull Long idSchiene : u.schienen)
				MapUtils.addToList(_unterrichtmenge_by_idSchiene, idSchiene, u);
	}

	private void update_zeitraster_by_wochentag_and_stunde() {
		_zeitraster_by_wochentag_and_stunde.clear();
		for (final @NotNull StundenplanZeitraster zeit : _zeitrastermenge)
			DeveloperNotificationException.ifMap2DPutOverwrites(_zeitraster_by_wochentag_and_stunde, zeit.wochentag, zeit.unterrichtstunde, zeit);
	}

	private void update_zeitrastermenge() {
		_zeitrastermenge.clear();
		_zeitrastermenge.addAll(_zeitraster_by_id.values());
		_zeitrastermenge.sort(_compZeitraster);

		_zeitrasterMinutenMinByStunde.clear();
		_zeitrasterMinutenMaxByStunde.clear();

		_zeitrasterMinutenMin = null;
		_zeitrasterMinutenMax = null;
		_zeitrasterWochentagMin = Wochentag.SONNTAG.id + 1;
		_zeitrasterWochentagMax = Wochentag.MONTAG.id - 1;
		_zeitrasterStundeMin = 999;
		_zeitrasterStundeMax = -999;
		for (final @NotNull StundenplanZeitraster z : _zeitrastermenge) {
			_zeitrasterMinutenMin = BlockungsUtils.minII(_zeitrasterMinutenMin, z.stundenbeginn);
			_zeitrasterMinutenMax = BlockungsUtils.maxII(_zeitrasterMinutenMax, z.stundenende);
			_zeitrasterWochentagMin = BlockungsUtils.minVI(_zeitrasterWochentagMin, z.wochentag);
			_zeitrasterWochentagMax = BlockungsUtils.maxVI(_zeitrasterWochentagMax, z.wochentag);
			_zeitrasterStundeMin = BlockungsUtils.minVI(_zeitrasterStundeMin, z.unterrichtstunde);
			_zeitrasterStundeMax = BlockungsUtils.maxVI(_zeitrasterStundeMax, z.unterrichtstunde);
			_zeitrasterMinutenMinByStunde.put(z.unterrichtstunde, BlockungsUtils.minII(_zeitrasterMinutenMinByStunde.get(z.unterrichtstunde), z.stundenbeginn));
			_zeitrasterMinutenMaxByStunde.put(z.unterrichtstunde, BlockungsUtils.maxII(_zeitrasterMinutenMaxByStunde.get(z.unterrichtstunde), z.stundenende));
		}
		_zeitrasterWochentagMin = (_zeitrasterWochentagMin == Wochentag.SONNTAG.id + 1) ? Wochentag.MONTAG.id : _zeitrasterWochentagMin;
		_zeitrasterWochentagMax = (_zeitrasterWochentagMax == Wochentag.MONTAG.id - 1) ? Wochentag.MONTAG.id : _zeitrasterWochentagMax;
		_zeitrasterStundeMin = (_zeitrasterStundeMin ==  999) ? 1 : _zeitrasterStundeMin;
		_zeitrasterStundeMax = (_zeitrasterStundeMax == -999) ? 1 : _zeitrasterStundeMax;


		// _zeitrasterWochentageAlsEnumRange
		_zeitrasterWochentageAlsEnumRange = new Wochentag[_zeitrasterWochentagMax - _zeitrasterWochentagMin + 1];
		for (int i = 0; i < _zeitrasterWochentageAlsEnumRange.length; i++)
			_zeitrasterWochentageAlsEnumRange[i] = Wochentag.fromIDorException(_zeitrasterWochentagMin + i);

		// _zeitrasterStundenRange
		_zeitrasterStundenRange = new int[_zeitrasterStundeMax - _zeitrasterStundeMin + 1];
		for (int i = 0; i < _zeitrasterStundenRange.length; i++)
			_zeitrasterStundenRange[i] = _zeitrasterStundeMin + i;
	}

	private void update_zeitrastermengeOhneLeereUnterrichtmenge() {
		_zeitrastermengeOhneLeere_sortiert.clear();
		for (final @NotNull StundenplanZeitraster z : _zeitrastermenge)
			if (!MapUtils.getOrCreateArrayList(_unterrichtmenge_by_idZeitraster, z.id).isEmpty())
				_zeitrastermengeOhneLeere_sortiert.add(z);

		_zeitrasterMinutenMinOhneLeere = null;
		_zeitrasterMinutenMaxOhneLeere = null;
		_zeitrasterStundeMinOhneLeere = 999;
		_zeitrasterStundeMaxOhneLeere = -999;
		for (final @NotNull StundenplanZeitraster z : _zeitrastermengeOhneLeere_sortiert) {
			_zeitrasterMinutenMinOhneLeere = BlockungsUtils.minII(_zeitrasterMinutenMinOhneLeere, z.stundenbeginn);
			_zeitrasterMinutenMaxOhneLeere = BlockungsUtils.maxII(_zeitrasterMinutenMaxOhneLeere, z.stundenende);
			_zeitrasterStundeMinOhneLeere = BlockungsUtils.minVI(_zeitrasterStundeMinOhneLeere, z.unterrichtstunde);
			_zeitrasterStundeMaxOhneLeere = BlockungsUtils.maxVI(_zeitrasterStundeMaxOhneLeere, z.unterrichtstunde);
		}
		_zeitrasterStundeMinOhneLeere = (_zeitrasterStundeMinOhneLeere ==  999) ? 1 : _zeitrasterStundeMinOhneLeere;
		_zeitrasterStundeMaxOhneLeere = (_zeitrasterStundeMaxOhneLeere == -999) ? 1 : _zeitrasterStundeMaxOhneLeere;

		// _uZeitrasterStundenRangeOhneLeere
		_zeitrasterStundenRangeOhneLeere = new int[_zeitrasterStundeMaxOhneLeere - _zeitrasterStundeMinOhneLeere + 1];
		for (int i = 0; i < _zeitrasterStundenRangeOhneLeere.length; i++)
			_zeitrasterStundenRangeOhneLeere[i] = _zeitrasterStundeMinOhneLeere + i;
	}

	private void update_zeitrastermenge_by_wochentag() {
		_zeitrastermenge_by_wochentag.clear();
		for (final @NotNull StundenplanZeitraster zeit : _zeitrastermenge)
			MapUtils.addToList(_zeitrastermenge_by_wochentag, zeit.wochentag, zeit);
	}

	private void update_zeitrastermenge_by_stunde() {
		_zeitrastermenge_by_stunde.clear();
		for (final @NotNull StundenplanZeitraster zeit : _zeitrastermenge)
			MapUtils.addToList(_zeitrastermenge_by_stunde, zeit.unterrichtstunde, zeit);
	}

	private void update_klassenmenge_by_idUnterricht() {
		_klassenmenge_by_idUnterricht.clear();

		for (final @NotNull StundenplanUnterricht u : _unterrichtmenge) {
			if (u.idKurs == null) {
				// Bei Klassenunterricht sind die Klassen in diesem Objekts direkt vorhanden.
				for (final @NotNull Long idKlasse : u.klassen) {
					final @NotNull StundenplanKlasse klasse = DeveloperNotificationException.ifMapGetIsNull(_klasse_by_id, idKlasse);
					MapUtils.addToListIfNotExists(_klassenmenge_by_idUnterricht, u.id, klasse);
				}
			} else {
				// Bei Kursunterricht muss der Kurs nach der Klassenmenge gefragt werden.
				// Der Kurs aggregierte seine Klassenmenge bereits über die Schüler.
				for (final @NotNull StundenplanKlasse klasse : MapUtils.getOrCreateArrayList(_klassenmenge_by_idKurs, u.idKurs)) {
					MapUtils.addToListIfNotExists(_klassenmenge_by_idUnterricht, u.id, klasse);
				}
			}
		}

	}

	// #####################################################################
	// #################### StundenplanAufsichtsbereich ####################
	// #####################################################################

	/**
	 * Fügt ein {@link StundenplanAufsichtsbereich}-Objekt hinzu.
	 *
	 * @param aufsichtsbereich  Das {@link StundenplanAufsichtsbereich}-Objekt, welches hinzugefügt werden soll.
	 */
	public void aufsichtsbereichAdd(final @NotNull StundenplanAufsichtsbereich aufsichtsbereich) {
		aufsichtsbereichAddAll(ListUtils.create1(aufsichtsbereich));
	}

	/**
	 * Fügt alle {@link StundenplanAufsichtsbereich}-Objekte hinzu.
	 *
	 * @param listAufsichtsbereich  Die Menge der {@link StundenplanAufsichtsbereich}-Objekte, welche hinzugefügt werden soll.
	 */
	public void aufsichtsbereichAddAll(final @NotNull List<@NotNull StundenplanAufsichtsbereich> listAufsichtsbereich) {
		aufsichtsbereichAddAllOhneUpdate(listAufsichtsbereich);
		update_all();
	}

	private void aufsichtsbereichAddAllOhneUpdate(final @NotNull List<@NotNull StundenplanAufsichtsbereich> list) {
		// check all
		final @NotNull HashSet<@NotNull Long> setOfIDs = new HashSet<>();
		for (final @NotNull StundenplanAufsichtsbereich aufsichtsbereich : list) {
			aufsichtsbereichCheckAttributes(aufsichtsbereich);
			DeveloperNotificationException.ifTrue("aufsichtsbereichAddAllOhneUpdate: ID=" + aufsichtsbereich.id + " existiert bereits!", _aufsichtsbereich_by_id.containsKey(aufsichtsbereich.id));
			DeveloperNotificationException.ifTrue("aufsichtsbereichAddAllOhneUpdate: ID=" + aufsichtsbereich.id + " doppelt in der Liste!", !setOfIDs.add(aufsichtsbereich.id));
		}

		// add all
		for (final @NotNull StundenplanAufsichtsbereich aufsichtsbereich : list)
			DeveloperNotificationException.ifMapPutOverwrites(_aufsichtsbereich_by_id, aufsichtsbereich.id, aufsichtsbereich);
	}

	private static void aufsichtsbereichCheckAttributes(final @NotNull StundenplanAufsichtsbereich aufsichtsbereich) {
		DeveloperNotificationException.ifInvalidID("aufsicht.id", aufsichtsbereich.id);
		DeveloperNotificationException.ifStringIsBlank("aufsicht.kuerzel", aufsichtsbereich.kuerzel);
		// aufsicht.beschreibung darf "blank" sein
	}

	/**
	 * Liefert TRUE, falls ein {@link StundenplanAufsichtsbereich}-Objekt mit dem Kürzel existiert.
	 * <br>Laufzeit: O(1)
	 *
	 * @param kuerzel  Das Kürzel des {@link StundenplanAufsichtsbereich}-Objektes.
	 *
	 * @return TRUE, falls ein {@link StundenplanAufsichtsbereich}-Objekt mit dem Kürzel existiert.
	 */
	public boolean aufsichtsbereichExistsByKuerzel(final @NotNull String kuerzel) {
		return _aufsichtsbereich_by_kuerzel.containsKey(kuerzel);
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanAufsichtsbereich}-Objekt.
	 *
	 * @param idAufsichtsbereich  Die Datenbank-ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanAufsichtsbereich}-Objekt.
	 */
	public @NotNull StundenplanAufsichtsbereich aufsichtsbereichGetByIdOrException(final long idAufsichtsbereich) {
		return DeveloperNotificationException.ifMapGetIsNull(_aufsichtsbereich_by_id, idAufsichtsbereich);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanAufsichtsbereich}-Objekte.
	 * <br> Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link StundenplanAufsichtsbereich}-Objekte.
	 */
	public @NotNull List<@NotNull StundenplanAufsichtsbereich> aufsichtsbereichGetMengeAsList() {
		return _aufsichtsbereichmenge_sortiert;
	}

	/**
	 * Aktualisiert das vorhandene {@link StundenplanAufsichtsbereich}-Objekt durch das neue Objekt.
	 * <br>Die folgenden Attribute werden nicht aktualisiert:
	 * <br>{@link StundenplanAufsichtsbereich#id}
	 * <br>
	 * <br>Die folgenden Attribute werden kopiert:
	 * <br>{@link StundenplanAufsichtsbereich#beschreibung}
	 * <br>{@link StundenplanAufsichtsbereich#kuerzel}
	 *
	 * @param aufsichtsbereich  Das neue {@link StundenplanAufsichtsbereich}-Objekt, dessen Attribute kopiert werden.
	 */
	public void aufsichtsbereichPatchAttributes(final @NotNull StundenplanAufsichtsbereich aufsichtsbereich) {
		// check
		aufsichtsbereichCheckAttributes(aufsichtsbereich);

		// replace
		DeveloperNotificationException.ifMapRemoveFailes(_aufsichtsbereich_by_id, aufsichtsbereich.id);
		DeveloperNotificationException.ifMapPutOverwrites(_aufsichtsbereich_by_id, aufsichtsbereich.id, aufsichtsbereich);

		// update
		update_all();
	}

	private void aufsichtsbereichRemoveOhneUpdateById(final long idAufsichtsbereich) {
		// Kaskade: StundenplanPausenaufsicht
		for (final @NotNull StundenplanPausenaufsicht aufsicht: MapUtils.getOrCreateArrayList(_pausenaufsichtmenge_by_idAufsichtsbereich, idAufsichtsbereich))
			aufsicht.bereiche.remove(idAufsichtsbereich);

		// Entfernen
		DeveloperNotificationException.ifMapRemoveFailes(_aufsichtsbereich_by_id, idAufsichtsbereich);
	}

	/**
	 * Entfernt ein {@link StundenplanAufsichtsbereich}-Objekt anhand seiner ID.
	 * <br>Laufzeit: O(|StundenplanAufsichtsbereich|), da aufsichtsbereichUpdate() aufgerufen wird.
	 *
	 * @param idAufsichtsbereich  Die Datenbank-ID des {@link StundenplanAufsichtsbereich}-Objekts, welches entfernt werden soll.
	 */
	public void aufsichtsbereichRemoveById(final long idAufsichtsbereich) {
		aufsichtsbereichRemoveOhneUpdateById(idAufsichtsbereich);

		update_all();
	}

	/**
	 * Entfernt alle {@link StundenplanAufsichtsbereich}-Objekte.
	 *
	 * @param listAufsichtsbereich  Die Liste der zu entfernenden {@link StundenplanAufsichtsbereich}-Objekte.
	 */
	public void aufsichtsbereichRemoveAll(final @NotNull List<@NotNull StundenplanAufsichtsbereich> listAufsichtsbereich) {
		// check
		final @NotNull HashSet<@NotNull Long> setOfIDs = new HashSet<>();
		for (final @NotNull StundenplanAufsichtsbereich aufsichtsbereich : listAufsichtsbereich) {
			if (!_aufsichtsbereich_by_id.containsKey(aufsichtsbereich.id))
				throw new DeveloperNotificationException("aufsichtsbereichRemoveAll: Aufsichtsbereich-ID existiert nicht!");
			if (!setOfIDs.add(aufsichtsbereich.id))
				throw new DeveloperNotificationException("aufsichtsbereichRemoveAll: Doppelte Aufsichtsbereich-ID in der Liste!");
		}

		// remove
		for (final @NotNull StundenplanAufsichtsbereich aufsichtsbereich : listAufsichtsbereich)
			aufsichtsbereichRemoveOhneUpdateById(aufsichtsbereich.id);

		update_all();
	}

	// #####################################################################
	// #################### Datum ##########################################
	// #####################################################################

	/**
	 * Liefert zu einem {@link StundenplanKalenderwochenzuordnung}-Objekt und einem {@link StundenplanZeitraster}-Objekt das zugehörige Datum.
	 *
	 * @param kwz   Das {@link StundenplanKalenderwochenzuordnung}-Objekt, welches das Datum zum Teil definiert.
	 * @param zeit  Das {@link StundenplanZeitraster}-Objekt, welches das Datum zum Teil definiert.
	 *
	 * @return zu einem {@link StundenplanKalenderwochenzuordnung}-Objekt und einem {@link StundenplanZeitraster}-Objekt das zugehörige Datum.
	 */
	public @NotNull String datumGetByKwzAndZeitraster(final @NotNull StundenplanKalenderwochenzuordnung kwz, final @NotNull StundenplanZeitraster zeit) {
		return DateUtils.gibDatumDesWochentagsOfJahrAndKalenderwoche(kwz.jahr, kwz.kw, zeit.wochentag);
	}

	/**
	 * Liefert zu einem {@link StundenplanKalenderwochenzuordnung}-Objekt und der Nummer des Wochentags das zugehörige Datum.
	 *
	 * @param kwz   Das {@link StundenplanKalenderwochenzuordnung}-Objekt, welches das Datum zum Teil definiert.
	 * @param wochentag  Die Nummer des Wochentags, der das Datum zum Teil definiert.
	 *
	 * @return zu einem {@link StundenplanKalenderwochenzuordnung}-Objekt und der Nummer des Wochentags das zugehörige Datum.
	 */
	public @NotNull String datumGetByKwzAndWochentag(final @NotNull StundenplanKalenderwochenzuordnung kwz, final @NotNull Wochentag wochentag) {
		return DateUtils.gibDatumDesWochentagsOfJahrAndKalenderwoche(kwz.jahr, kwz.kw, wochentag.id);
	}

	// #####################################################################
	// #################### StundenplanFach ################################
	// #####################################################################

	/**
	 * Fügt ein {@link StundenplanFach}-Objekt hinzu.
	 * <br>Laufzeit: O(|StundenplanFach|), da fachUpdate() aufgerufen wird.
	 *
	 * @param fach  Das {@link StundenplanFach}-Objekt, welches hinzugefügt werden soll.
	 */
	public void fachAdd(final @NotNull StundenplanFach fach) {
		fachAddAll(ListUtils.create1(fach));
	}

	/**
	 * Fügt alle {@link StundenplanFach}-Objekte hinzu.
	 * <br>Laufzeit: O(|StundenplanFach|), da fachUpdate() aufgerufen wird.
	 *
	 * @param listFach  Die Menge der {@link StundenplanFach}-Objekte, welche hinzugefügt werden soll.
	 */
	public void fachAddAll(final @NotNull List<@NotNull StundenplanFach> listFach) {
		fachAddAllOhneUpdate(listFach);
		update_all();
	}

	private void fachAddAllOhneUpdate(final @NotNull List<@NotNull StundenplanFach> list) {
		// check all
		final @NotNull HashSet<@NotNull Long> setOfIDs = new HashSet<>();
		for (final @NotNull StundenplanFach fach : list) {
			fachCheckAttributes(fach);
			DeveloperNotificationException.ifTrue("fachAddAllOhneUpdate: ID=" + fach.id + " existiert bereits!", _fach_by_id.containsKey(fach.id));
			DeveloperNotificationException.ifTrue("fachAddAllOhneUpdate: ID=" + fach.id + " doppelt in der Liste!", !setOfIDs.add(fach.id));
		}

		// add all
		for (final @NotNull StundenplanFach fach : list)
			DeveloperNotificationException.ifMapPutOverwrites(_fach_by_id, fach.id, fach);
	}

	private static void fachCheckAttributes(final @NotNull StundenplanFach fach) {
		DeveloperNotificationException.ifInvalidID("fach.id", fach.id);
		// "fach.bezeichnung" darf blank sein.
		DeveloperNotificationException.ifStringIsBlank("fach.kuerzel", fach.kuerzel);
	}

	/**
	 * Liefert das Fach mit der übergebenen ID.
	 *
	 * @param idFach  Die Datenbank-ID des Faches.
	 *
	 * @return  das Fach mit der übergebenen ID.
	 */
	public @NotNull StundenplanFach fachGetByIdOrException(final long idFach) {
		return DeveloperNotificationException.ifMapGetIsNull(_fach_by_id, idFach);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanFach}-Objekte, sortiert nach {@link StundenplanFach#sortierung}.
	 * <br>Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link StundenplanFach}-Objekte, sortiert nach {@link StundenplanFach#sortierung}.
	 */
	public @NotNull List<@NotNull StundenplanFach> fachGetMengeAsList() {
		return _fachmenge_sortiert;
	}

	// #####################################################################
	// #################### StundenplanJahrgang ############################
	// #####################################################################

	/**
	 * Fügt ein {@link StundenplanJahrgang}-Objekt hinzu.
	 *
	 * @param jahrgang  Das {@link StundenplanJahrgang}-Objekt, welches hinzugefügt werden soll.
	 */
	public void jahrgangAdd(final @NotNull StundenplanJahrgang jahrgang) {
		jahrgangAddAll(ListUtils.create1(jahrgang));
	}

	/**
	 * Fügt alle {@link StundenplanJahrgang}-Objekte hinzu.
	 *
	 * @param listJahrgang  Die Menge der {@link StundenplanJahrgang}-Objekte, welche hinzugefügt werden soll.
	 */
	public void jahrgangAddAll(final @NotNull List<@NotNull StundenplanJahrgang> listJahrgang) {
		jahrgangAddAllOhneUpdate(listJahrgang);
		update_all();
	}

	private void jahrgangAddAllOhneUpdate(final @NotNull List<@NotNull StundenplanJahrgang> list) {
		// check all
		final @NotNull HashSet<@NotNull Long> setOfIDs = new HashSet<>();
		for (final @NotNull StundenplanJahrgang jahrgang : list) {
			jahrgangCheckAttributes(jahrgang);
			DeveloperNotificationException.ifTrue("jahrgangAddAllOhneUpdate: ID=" + jahrgang.id + " existiert bereits!", _jahrgang_by_id.containsKey(jahrgang.id));
			DeveloperNotificationException.ifTrue("jahrgangAddAllOhneUpdate: ID=" + jahrgang.id + " doppelt in der Liste!", !setOfIDs.add(jahrgang.id));
		}

		// add all
		for (final @NotNull StundenplanJahrgang jahrgang : list)
			DeveloperNotificationException.ifMapPutOverwrites(_jahrgang_by_id, jahrgang.id, jahrgang);
	}

	private static void jahrgangCheckAttributes(final @NotNull StundenplanJahrgang jahrgang) {
		DeveloperNotificationException.ifInvalidID("jahrgang.id", jahrgang.id);
		// "jahrgang.bezeichnung" darf blank sein.
		DeveloperNotificationException.ifStringIsBlank("jahrgang.kuerzel", jahrgang.kuerzel);
	}

	/**
	 * Liefert das {@link StundenplanJahrgang}-Objekt mit der übergebenen ID.
	 *
	 * @param idJahrgang  Die Datenbank-ID des {@link StundenplanJahrgang}-Objekts.
	 *
	 * @return das {@link StundenplanJahrgang}-Objekt mit der übergebenen ID.
	 */
	public @NotNull StundenplanJahrgang jahrgangGetByIdOrException(final long idJahrgang) {
		return DeveloperNotificationException.ifMapGetIsNull(_jahrgang_by_id, idJahrgang);
	}

	private StundenplanJahrgang jahrgangGetMinimumByKlassenIDs(final @NotNull List<@NotNull Long> klassen) {
		StundenplanJahrgang min = null;

		for (final @NotNull Long idKlasse : klassen) {
			final @NotNull StundenplanKlasse klasse = DeveloperNotificationException.ifMapGetIsNull(_klasse_by_id, idKlasse);
			for (final @NotNull Long idJahrgang : klasse.jahrgaenge) {
				final @NotNull StundenplanJahrgang jahrgang = DeveloperNotificationException.ifMapGetIsNull(_jahrgang_by_id, idJahrgang);
				if ((min == null) || (_compJahrgang.compare(jahrgang, min) < 0))
					min = jahrgang;
			}
		}

		return min;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanJahrgang}-Objekte.
	 * <br>Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link StundenplanJahrgang}-Objekte.
	 */
	public @NotNull List<@NotNull StundenplanJahrgang> jahrgangGetMengeAsList() {
		return _jahrgangmenge_sortiert;
	}

	/**
	 * Aktualisiert das vorhandene {@link StundenplanJahrgang}-Objekt durch das neue Objekt.
	 * <br>Die folgenden Attribute werden nicht aktualisiert:
	 * <br>{@link StundenplanJahrgang#id}
	 * <br>
	 * <br>Die folgenden Attribute werden kopiert:
	 * <br>{@link StundenplanJahrgang#bezeichnung}
	 * <br>{@link StundenplanJahrgang#kuerzel}
	 *
	 * @param jahrgang  Das neue {@link StundenplanJahrgang}-Objekt, dessen Attribute kopiert werden.
	 */
	public void jahrgangPatchAttributes(final @NotNull StundenplanJahrgang jahrgang) {
		// check
		jahrgangCheckAttributes(jahrgang);

		// replace
		DeveloperNotificationException.ifMapRemoveFailes(_jahrgang_by_id, jahrgang.id);
		DeveloperNotificationException.ifMapPutOverwrites(_jahrgang_by_id, jahrgang.id, jahrgang);

		// update
		update_all();
	}

	private void jahrgangRemoveOhneUpdateById(final long idJahrgang) {
		// Kaskade: StundenplanSchiene
		for (final @NotNull StundenplanSchiene schiene: MapUtils.getOrCreateArrayList(_schienenmenge_by_idJahrgang, idJahrgang))
			schieneRemoveOhneUpdateById(schiene.id);

		// Kaskade: StundenplanKurs
		for (final @NotNull StundenplanKurs kurs: MapUtils.getOrCreateArrayList(_kursmenge_by_idJahrgang, idJahrgang))
			kurs.jahrgaenge.remove(idJahrgang);

		// Kaskade: StundenplanKlasse
		for (final @NotNull StundenplanKlasse klasse: MapUtils.getOrCreateArrayList(_klassenmenge_by_idJahrgang, idJahrgang))
			klasse.jahrgaenge.remove(idJahrgang);

		DeveloperNotificationException.ifMapRemoveFailes(_jahrgang_by_id, idJahrgang);
	}

	/**
	 * Entfernt ein {@link StundenplanJahrgang}-Objekt anhand seiner ID.
	 * <br>Laufzeit: O(|StundenplanJahrgang|), da jahrgangUpdate() aufgerufen wird.
	 *
	 * @param idJahrgang  Die Datenbank-ID des {@link StundenplanJahrgang}-Objekts, welches entfernt werden soll.
	 */
	public void jahrgangRemoveById(final long idJahrgang) {
		jahrgangRemoveOhneUpdateById(idJahrgang);

		update_all();
	}

	/**
	 * Entfernt alle {@link StundenplanJahrgang}-Objekte.
	 *
	 * @param listJahrgang  Die Liste der zu entfernenden {@link StundenplanJahrgang}-Objekte.
	 */
	public void jahrgangRemoveAll(final @NotNull List<@NotNull StundenplanJahrgang> listJahrgang) {
		for (final @NotNull StundenplanJahrgang jahrgang : listJahrgang)
			jahrgangRemoveOhneUpdateById(jahrgang.id);

		update_all();
	}

	// #####################################################################
	// #################### StundenplanKalenderwochenzuordnung #############
	// #####################################################################

	/**
	 * Fügt ein {@link StundenplanKalenderwochenzuordnung}-Objekt hinzu.
	 *
	 * @param kwz  Das {@link StundenplanKalenderwochenzuordnung}-Objekt, welches hinzugefügt werden soll.
	 */
	public void kalenderwochenzuordnungAdd(final @NotNull StundenplanKalenderwochenzuordnung kwz) {
		kalenderwochenzuordnungAddAll(ListUtils.create1(kwz));
	}

	/**
	 * Fügt alle {@link StundenplanKalenderwochenzuordnung}-Objekte hinzu.
	 *
	 * @param listKWZ  Die Menge der {@link StundenplanKalenderwochenzuordnung}-Objekte, welche hinzugefügt werden soll.
	 */
	public void kalenderwochenzuordnungAddAll(final @NotNull List<@NotNull StundenplanKalenderwochenzuordnung> listKWZ) {
		kalenderwochenzuordnungAddAllOhneUpdate(listKWZ);
		update_all();
	}

	private void kalenderwochenzuordnungAddAllOhneUpdate(final @NotNull List<@NotNull StundenplanKalenderwochenzuordnung> list) {
		// check all
		final @NotNull HashSet<@NotNull String> setOfIDs = new HashSet<>();
		for (final @NotNull StundenplanKalenderwochenzuordnung kwz : list) {
			kalenderwochenzuordnungCheck(kwz, true);
			DeveloperNotificationException.ifTrue("kalenderwochenzuordnungAddAllOhneUpdate: JAHR=" + kwz.jahr + ", KW=" + kwz.kw + " existiert bereits!", _kwz_by_jahr_and_kw.contains(kwz.jahr, kwz.kw));
			DeveloperNotificationException.ifTrue("kalenderwochenzuordnungAddAllOhneUpdate: JAHR=" + kwz.jahr + ", KW=" + kwz.kw + " doppelt in der Liste!", !setOfIDs.add(kwz.jahr + ";" + kwz.kw));
		}

		// add all
		for (final @NotNull StundenplanKalenderwochenzuordnung kwz : list)
			DeveloperNotificationException.ifMap2DPutOverwrites(_kwz_by_jahr_and_kw, kwz.jahr, kwz.kw, kwz);
	}

	private void kalenderwochenzuordnungCheck(final @NotNull StundenplanKalenderwochenzuordnung kwz, final boolean checkID) {
		if (checkID)
			DeveloperNotificationException.ifInvalidID("kwz.id", kwz.id);
		DeveloperNotificationException.ifTrue("(kwz.jahr < DateUtils.MIN_GUELTIGES_JAHR) || (kwz.jahr > DateUtils.MAX_GUELTIGES_JAHR)", (kwz.jahr < DateUtils.MIN_GUELTIGES_JAHR) || (kwz.jahr > DateUtils.MAX_GUELTIGES_JAHR));
		DeveloperNotificationException.ifTrue("(kwz.kw < 1) || (kwz.kw > DateUtils.gibKalenderwochenOfJahr(kwz.jahr))", (kwz.kw < 1) || (kwz.kw > DateUtils.gibKalenderwochenOfJahr(kwz.jahr)));
		DeveloperNotificationException.ifTrue("kwz.wochentyp > stundenplanWochenTypModell", kwz.wochentyp > _stundenplanWochenTypModell);
		DeveloperNotificationException.ifTrue("kwz.wochentyp < 0", kwz.wochentyp < 0);
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanKalenderwochenzuordnung}-Objekt.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idKWZ Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanKalenderwochenzuordnung}-Objekt.
	 */
	public @NotNull StundenplanKalenderwochenzuordnung kalenderwochenzuordnungGetByIdOrException(final long idKWZ) {
		return DeveloperNotificationException.ifMapGetIsNull(_kwz_by_id, idKWZ);
	}

	/**
	 * Liefert das dem Jahr und der Kalenderwoche zugeordnete {@link StundenplanKalenderwochenzuordnung}-Objekt der Auswahl-Menge.
	 * <br>Hinweis: Einige Objekte dieser Menge können die ID = -1 haben, falls sie erzeugt wurden und nicht aus der DB stammen.
	 * <br>Laufzeit: O(1)
	 *
	 * @param jahr           Das Jahr der Kalenderwoche.
	 * @param kalenderwoche  Die gewünschte Kalenderwoche.
	 *
	 * @return das dem Jahr und der Kalenderwoche zugeordnete {@link StundenplanKalenderwochenzuordnung}-Objekt der Auswahl-Menge.
	 */
	public @NotNull StundenplanKalenderwochenzuordnung kalenderwochenzuordnungGetByJahrAndKWOrException(final int jahr, final int kalenderwoche) {
		return DeveloperNotificationException.ifMap2DGetIsNull(_kwz_by_jahr_and_kw, jahr, kalenderwoche);
	}

	/**
	 * Liefert das dem Datum zugeordnete {@link StundenplanKalenderwochenzuordnung}-Objekt der Auswahl-Menge oder das nächstmöglichste.
	 * <br>Hinweis: Einige Objekte dieser Menge können die ID = -1 haben, falls sie erzeugt wurden und nicht aus der DB stammen.
	 * <br>Laufzeit: O(1)
	 *
	 * @param datumISO8601 Das Datum im ISO8601-Format uuuu-MM-dd (z.B. 2014-03-14).
	 *
	 * @return das dem Datum zugeordnete {@link StundenplanKalenderwochenzuordnung}-Objekt der Auswahl-Menge oder das nächstmöglichste.
	 */
	public @NotNull StundenplanKalenderwochenzuordnung kalenderwochenzuordnungGetByDatum(final @NotNull String datumISO8601) {
		final int[] e = DateUtils.extractFromDateISO8601(datumISO8601);

		final int kwJahr = e[6];
		final int kw = e[5];
		final StundenplanKalenderwochenzuordnung kwz = _kwz_by_jahr_and_kw.getOrNull(kwJahr, kw);

		// Datum innerhalb von "First" und "Last"?
		if (kwz != null)
			return kwz;

		// Datum kleiner First?
		final @NotNull StundenplanKalenderwochenzuordnung kwzFirst = DeveloperNotificationException.ifListGetFirstFailes("_kwz_by_jahr_and_kw", _kwzmenge_sortiert);
		if ((kwJahr < kwzFirst.jahr) || ((kwJahr == kwzFirst.jahr) && (kw < kwzFirst.kw)))
			return kwzFirst;

		// Datum größer Last
		return DeveloperNotificationException.ifListGetLastFailes("_kwz_by_jahr_and_kw", _kwzmenge_sortiert);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanKalenderwochenzuordnung}-Objekte.
	 * <br>Hinweis: Einige Objekte dieser Menge können die ID = -1 haben, falls sie erzeugt wurden und nicht aus der DB stammen.
	 * <br>Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link StundenplanKalenderwochenzuordnung}-Objekte.
	 */
	public @NotNull List<@NotNull StundenplanKalenderwochenzuordnung> kalenderwochenzuordnungGetMengeAsList() {
		return _kwzmenge_sortiert;
	}

	/**
	 * Liefert das nächste {@link StundenplanKalenderwochenzuordnung}-Objekt falls dieses gültig ist, sonst NULL.
	 * <br>Hinweis: Ein {@link StundenplanKalenderwochenzuordnung}-Objekt ist gültig, wenn es im Datumsbereich des Stundenplanes ist.
	 * <br>Hinweis: Einige Objekte dieser Menge können die ID = -1 haben, falls sie erzeugt wurden und nicht aus der DB stammen.
	 * <br>Laufzeit: O(1)
	 *
	 * @param kwz  Das aktuelle {@link StundenplanKalenderwochenzuordnung}-Objekt.
	 *
	 * @return das nächste {@link StundenplanKalenderwochenzuordnung}-Objekt falls dieses gültig ist, sonst NULL.
	 */
	public StundenplanKalenderwochenzuordnung kalenderwochenzuordnungGetNextOrNull(final @NotNull StundenplanKalenderwochenzuordnung kwz) {
		kalenderwochenzuordnungCheck(kwz, false);
		final int max = DateUtils.gibKalenderwochenOfJahr(kwz.jahr);
		return (kwz.kw < max) ? _kwz_by_jahr_and_kw.getOrNull(kwz.jahr, kwz.kw + 1)
                              : _kwz_by_jahr_and_kw.getOrNull(kwz.jahr + 1, 1);
	}

	/**
	 * Liefert das vorherige {@link StundenplanKalenderwochenzuordnung}-Objekt falls dieses gültig ist, sonst NULL.
	 * <br>Hinweis: Ein {@link StundenplanKalenderwochenzuordnung}-Objekt ist gültig, wenn es im Datumsbereich des Stundenplanes ist.
	 * <br>Hinweis: Einige Objekte dieser Menge können die ID = -1 haben, falls sie erzeugt wurden und nicht aus der DB stammen.
	 * <br>Laufzeit: O(1)
	 *
	 * @param kwz  Das aktuelle {@link StundenplanKalenderwochenzuordnung}-Objekt.
	 *
	 * @return das vorherige {@link StundenplanKalenderwochenzuordnung}-Objekt falls dieses gültig ist, sonst NULL.
	 */
	public StundenplanKalenderwochenzuordnung kalenderwochenzuordnungGetPrevOrNull(final @NotNull StundenplanKalenderwochenzuordnung kwz) {
		kalenderwochenzuordnungCheck(kwz, false);
		final int max = DateUtils.gibKalenderwochenOfJahr(kwz.jahr - 1);
		return (kwz.kw > 1) ? _kwz_by_jahr_and_kw.getOrNull(kwz.jahr, kwz.kw - 1)
		                    : _kwz_by_jahr_and_kw.getOrNull(kwz.jahr - 1, max);
	}

	/**
	 * Liefert eine String-Darstellung der Kalenderwoche des {@link StundenplanKalenderwochenzuordnung}-Objekts.
	 * <br>Beispiel: Jahr 2023, KW  5 --> "KW 5 (30.01.2023–05.02.2023)"
	 * <br>Beispiel: Jahr 2025, KW  1 --> "KW 1 (30.12.2024–05.01.2025)"
	 * <br>Beispiel: Jahr 2026, KW 53 --> "KW 53 (28.12.2026–03.01.2027)"
	 * <br>Laufzeit: O(1)

	 * @param kwz  Das {@link StundenplanKalenderwochenzuordnung}-Objekt.
	 *
	 * @return eine String-Darstellung der Kalenderwoche des {@link StundenplanKalenderwochenzuordnung}-Objekts.
	 */
	public @NotNull String kalenderwochenzuordnungGetWocheAsString(final @NotNull StundenplanKalenderwochenzuordnung kwz) {
		final @NotNull String sMo = DateUtils.gibDatumDesMontagsOfJahrAndKalenderwoche(kwz.jahr, kwz.kw);
		final @NotNull String sSo = DateUtils.gibDatumDesSonntagsOfJahrAndKalenderwoche(kwz.jahr, kwz.kw);
		final @NotNull String sMoGer = DateUtils.gibDatumGermanFormat(sMo);
		final @NotNull String sSoGer = DateUtils.gibDatumGermanFormat(sSo);
		final @NotNull String sJahrKW = "KW " + kwz.kw;
		return sJahrKW + " (" + sMoGer + "–" + sSoGer + ")";
	}

	/**
	 * Liefert den zugeordneten Wochentyp, oder den Default-Wochentyp, welcher sich aus der Kalenderwoche berechnet.
	 * <br>Laufzeit: O(1)
	 *
	 * @param jahr           Das Jahr der Kalenderwoche. Es muss zwischen {@link DateUtils#MIN_GUELTIGES_JAHR} und {@link DateUtils#MAX_GUELTIGES_JAHR} liegen.
	 * @param kalenderwoche  Die gewünschten Kalenderwoche. Es muss zwischen 1 und {@link DateUtils#gibKalenderwochenOfJahr(int)} liegen.
	 *
	 * @return den zugeordneten Wochentyp, oder den Default-Wochentyp, welcher sich aus der Kalenderwoche berechnet.
	 */
	public int kalenderwochenzuordnungGetWochentypOrDefault(final int jahr, final int kalenderwoche) {
		// Überprüfen
		DeveloperNotificationException.ifSmaller("jahr", jahr, DateUtils.MIN_GUELTIGES_JAHR);
		DeveloperNotificationException.ifGreater("jahr", jahr, DateUtils.MAX_GUELTIGES_JAHR);
		DeveloperNotificationException.ifSmaller("kalenderwoche", kalenderwoche, 1);
		DeveloperNotificationException.ifGreater("kalenderwoche", kalenderwoche, DateUtils.gibKalenderwochenOfJahr(jahr));

		// Fall: Das globale Modell hat keine Multiwochen.
		if (_stundenplanWochenTypModell == 0)
			return 0;

		// Fall: Eine Zuordnung ist definiert.
		final StundenplanKalenderwochenzuordnung z = _kwz_by_jahr_and_kw.getOrNull(jahr, kalenderwoche);
		if (z != null)
			return z.wochentyp;

		// Default: Der Wert berechnet sich modulo der Kalenderwoche.
		final int wochentyp = kalenderwoche % _stundenplanWochenTypModell;
		return wochentyp == 0 ? _stundenplanWochenTypModell : wochentyp; // 0 wird zu stundenplanWochenTypModell rotiert!
	}

	/**
	 * Liefert TRUE, falls intern ein Mapping von "Jahr, Kalenderwoche" den Wochentyp verwendet wird.
	 * <br>Hinweis: Das Mapping muss existieren UND {@link #_stundenplanWochenTypModell} muss mindestens 2 sein.
	 * <br>Laufzeit: O(1)
	 *
	 * @param jahr           Das Jahr der Kalenderwoche. Es muss zwischen {@link DateUtils#MIN_GUELTIGES_JAHR} und {@link DateUtils#MAX_GUELTIGES_JAHR} liegen.
	 * @param kalenderwoche  Die gewünschten Kalenderwoche. Es muss zwischen 1 und {@link DateUtils#gibKalenderwochenOfJahr(int)} liegen.
	 *
	 * @return TRUE, falls intern ein Mapping von "Jahr, Kalenderwoche" den Wochentyp verwendet wird.
	 */
	public boolean kalenderwochenzuordnungGetWochentypUsesMapping(final int jahr, final int kalenderwoche) {
		// Überprüfen
		DeveloperNotificationException.ifSmaller("jahr", jahr, DateUtils.MIN_GUELTIGES_JAHR);
		DeveloperNotificationException.ifGreater("jahr", jahr, DateUtils.MAX_GUELTIGES_JAHR);
		DeveloperNotificationException.ifSmaller("kalenderwoche", kalenderwoche, 1);
		DeveloperNotificationException.ifGreater("kalenderwoche", kalenderwoche, DateUtils.gibKalenderwochenOfJahr(jahr));

		// Berechnen
		final StundenplanKalenderwochenzuordnung z = _kwz_by_jahr_and_kw.getOrNull(jahr, kalenderwoche);
		if (z == null)
			return false;

		return (_stundenplanWochenTypModell >= 2) && (z.id >= 0);
	}

	/**
	 * Aktualisiert das vorhandene {@link StundenplanKalenderwochenzuordnung}-Objekt durch das neue Objekt.
	 * <br>Die folgenden Attribute werden nicht aktualisiert:
	 * <br>{@link StundenplanKalenderwochenzuordnung#id}
	 * <br>
	 * <br>Die folgenden Attribute werden kopiert:
	 * <br>{@link StundenplanKalenderwochenzuordnung#jahr}
	 * <br>{@link StundenplanKalenderwochenzuordnung#kw}
	 * <br>{@link StundenplanKalenderwochenzuordnung#wochentyp}
	 *
	 * @param kwz  Das neue {@link StundenplanKalenderwochenzuordnung}-Objekt, dessen Attribute kopiert werden.
	 */
	public void kalenderwochenzuordnungPatchAttributes(final @NotNull StundenplanKalenderwochenzuordnung kwz) {
		// check
		kalenderwochenzuordnungCheck(kwz, true);

		// replace
		DeveloperNotificationException.ifMapRemoveFailes(_kwz_by_id, kwz.id);
		DeveloperNotificationException.ifMapPutOverwrites(_kwz_by_id, kwz.id, kwz);

		// update
		update_all();
	}

	private void kalenderwochenzuordnungRemoveOhneUpdateById(final long idKWZ) {
		DeveloperNotificationException.ifMapRemoveFailes(_kwz_by_id, idKWZ);
	}

	/**
	 * Entfernt ein {@link StundenplanKalenderwochenzuordnung}-Objekt anhand seiner Datenbank-ID.
	 *
	 * @param idKWZ  Die Datenbank-ID des {@link StundenplanKalenderwochenzuordnung}-Objekts, welches entfernt werden soll.
	 */
	public void kalenderwochenzuordnungRemoveById(final long idKWZ) {
		kalenderwochenzuordnungRemoveOhneUpdateById(idKWZ);

		update_all();
	}

	/**
	 * Entfernt alle {@link StundenplanKalenderwochenzuordnung}-Objekte.
	 *
	 * @param listKWZ  Die Liste der zu entfernenden {@link StundenplanKalenderwochenzuordnung}-Objekte.
	 */
	public void kalenderwochenzuordnungRemoveAll(final @NotNull List<@NotNull StundenplanKalenderwochenzuordnung> listKWZ) {
		for (final @NotNull StundenplanKalenderwochenzuordnung kwz : listKWZ)
			kalenderwochenzuordnungRemoveOhneUpdateById(kwz.id);

		update_all();
	}

	// #####################################################################
	// #################### StundenplanKlasse ##############################
	// #####################################################################

	/**
	 * Fügt ein {@link StundenplanKlasse}-Objekt hinzu.
	 *
	 * @param klasse  Das {@link StundenplanKlasse}-Objekt, welches hinzugefügt werden soll.
	 */
	public void klasseAdd(final @NotNull StundenplanKlasse klasse) {
		klasseAddAll(ListUtils.create1(klasse));
	}


	/**
	 * Fügt alle {@link StundenplanKlasse}-Objekte hinzu.
	 *
	 * @param listKlasse  Die Menge der {@link StundenplanKlasse}-Objekte, welche hinzugefügt werden soll.
	 */
	public void klasseAddAll(final @NotNull List<@NotNull StundenplanKlasse> listKlasse) {
		klasseAddAllOhneUpdate(listKlasse);
		update_all();
	}

	private void klasseAddAllOhneUpdate(final @NotNull List<@NotNull StundenplanKlasse> list) {
		// check all
		final @NotNull HashSet<@NotNull Long> setOfIDs = new HashSet<>();
		for (final @NotNull StundenplanKlasse klasse : list) {
			klasseCheckAttributes(klasse);
			DeveloperNotificationException.ifTrue("klasseAddAllOhneUpdate: ID=" + klasse.id + " existiert bereits!", _klasse_by_id.containsKey(klasse.id));
			DeveloperNotificationException.ifTrue("klasseAddAllOhneUpdate: ID=" + klasse.id + " doppelt in der Liste!", !setOfIDs.add(klasse.id));
		}

		// add all
		for (final @NotNull StundenplanKlasse klasse : list)
			DeveloperNotificationException.ifMapPutOverwrites(_klasse_by_id, klasse.id, klasse);
	}

	private void klasseCheckAttributes(final @NotNull StundenplanKlasse klasse) {
		DeveloperNotificationException.ifInvalidID("klasse.id", klasse.id);
		DeveloperNotificationException.ifStringIsBlank("klasse.kuerzel", klasse.kuerzel);
		// klasse.bezeichnung darf "blank" sein
		for (final @NotNull Long idJahrgang : klasse.jahrgaenge)
			DeveloperNotificationException.ifMapNotContains("_jahrgang_by_id", _jahrgang_by_id, idJahrgang);
		// Folgende zwei Zeilen nicht mehr nötig, sobald aus DTO entfernt.
		for (final @NotNull Long idSchueler : klasse.schueler)
			DeveloperNotificationException.ifMapNotContains("_schueler_by_id", _schueler_by_id, idSchueler);
	}

	private int klasseCompareByKlassenIDs(final @NotNull List<@NotNull Long> a, final @NotNull List<@NotNull Long> b) {
		if (a.size() < b.size()) return -1;
		if (a.size() > b.size()) return +1;
		for (int i = 0; i < a.size(); i++) {
			final @NotNull Long aIdKlasse = ListUtils.getNonNullElementAtOrException(a, i);
			final @NotNull Long bIdKlasse = ListUtils.getNonNullElementAtOrException(b, i);
			final @NotNull StundenplanKlasse aKlasse = DeveloperNotificationException.ifMapGetIsNull(_klasse_by_id, aIdKlasse);
			final @NotNull StundenplanKlasse bKlasse = DeveloperNotificationException.ifMapGetIsNull(_klasse_by_id, bIdKlasse);
			final int cmpKlasse = _compKlasse.compare(aKlasse, bKlasse);
			if (cmpKlasse != 0) return cmpKlasse;
		}
		return 0;
	}

	/**
	 * Liefert das {@link StundenplanKlasse}-Objekt mit der übergebenen ID.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idKlasse  Die Datenbank-ID des {@link StundenplanKlasse}-Objekts.
	 *
	 * @return das {@link StundenplanKlasse}-Objekt mit der übergebenen ID.
	 */
	public @NotNull StundenplanKlasse klasseGetByIdOrException(final long idKlasse) {
		return DeveloperNotificationException.ifMapGetIsNull(_klasse_by_id, idKlasse);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanKlasse}-Objekte.
	 * <br>Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link StundenplanKlasse}-Objekte.
	 */
	public @NotNull List<@NotNull StundenplanKlasse> klasseGetMengeAsList() {
		return _klassenmenge_sortiert;
	}

	/**
	 * Liefert eine Liste aller sichtbaren {@link StundenplanKlasse}-Objekte.
	 * <br>Laufzeit: O(1)
	 *
	 * @return eine Liste aller sichtbaren {@link StundenplanKlasse}-Objekte.
	 */
	public @NotNull List<@NotNull StundenplanKlasse> klasseGetMengeSichtbarAsList() {
		return _klassenmenge_sichtbar_sortiert;
	}

	/**
	 * Aktualisiert das vorhandene {@link StundenplanKlasse}-Objekt durch das neue Objekt.
	 * <br>Die folgenden Attribute werden nicht aktualisiert:
	 * <br>{@link StundenplanKlasse#id}
	 * <br>
	 * <br>Die folgenden Attribute werden kopiert:
	 * <br>{@link StundenplanKlasse#bezeichnung}
	 * <br>{@link StundenplanKlasse#kuerzel}
	 * <br>{@link StundenplanKlasse#jahrgaenge}
	 * <br>{@link StundenplanKlasse#schueler}
	 *
	 * @param klasse  Das neue {@link StundenplanKlasse}-Objekt, dessen Attribute kopiert werden.
	 */
	public void klassePatchAttributes(final @NotNull StundenplanKlasse klasse) {
		// check
		klasseCheckAttributes(klasse);

		// replace
		DeveloperNotificationException.ifMapRemoveFailes(_klasse_by_id, klasse.id);
		DeveloperNotificationException.ifMapPutOverwrites(_klasse_by_id, klasse.id, klasse);

		// update
		update_all();
	}

	private void klasseRemoveOhneUpdateById(final long idKlasse) {
		// Kaskade: StundenplanUnterricht
		for (final @NotNull StundenplanKlassenunterricht u : MapUtils.getOrCreateArrayList(_klassenunterrichtmenge_by_idKlasse, idKlasse))
			klassenunterrichtRemoveOhneUpdateById(u.idKlasse, u.idFach);

		// Kaskade: StundenplanPausenzeit
		for (final @NotNull StundenplanPausenzeit zeit : MapUtils.getOrCreateArrayList(_pausenzeitmenge_by_idKlasse, idKlasse))
			zeit.klassen.remove(idKlasse);

		// Entfernen
		DeveloperNotificationException.ifMapRemoveFailes(_klasse_by_id, idKlasse);
	}

	/**
	 * Entfernt ein {@link StundenplanKlasse}-Objekt anhand seiner ID.
	 *
	 * @param idKlasse  Die Datenbank-ID des {@link StundenplanKlasse}-Objekts, welches entfernt werden soll.
	 */
	public void klasseRemoveById(final long idKlasse) {
		klasseRemoveOhneUpdateById(idKlasse);

		update_all();
	}

	/**
	 * Entfernt alle {@link StundenplanKlasse}-Objekte.
	 *
	 * @param listKlasse  Die Liste der zu entfernenden {@link StundenplanKlasse}-Objekte.
	 */
	public void klasseRemoveAll(final @NotNull List<@NotNull StundenplanKlasse> listKlasse) {
		for (final @NotNull StundenplanKlasse klasse : listKlasse)
			klasseRemoveOhneUpdateById(klasse.id);

		update_all();
	}

	// #####################################################################
	// #################### StundenplanKlassenunterricht ###################
	// #####################################################################

	/**
	 * Fügt ein {@link StundenplanKlassenunterricht}-Objekt hinzu.
	 *
	 * @param klassenunterricht  Das {@link StundenplanKlassenunterricht}-Objekt, welches hinzugefügt werden soll.
	 */
	public void klassenunterrichtAdd(final @NotNull StundenplanKlassenunterricht klassenunterricht) {
		klassenunterrichtAddAll(ListUtils.create1(klassenunterricht));
	}

	/**
	 * Fügt alle {@link StundenplanKlassenunterricht}-Objekte hinzu.
	 *
	 * @param listKlassenunterricht  Die Menge der {@link StundenplanKlassenunterricht}-Objekte, welche hinzugefügt werden soll.
	 */
	public void klassenunterrichtAddAll(final @NotNull List<@NotNull StundenplanKlassenunterricht> listKlassenunterricht) {
		klassenunterrichtAddAllOhneUpdate(listKlassenunterricht);
		update_all();
	}

	private void klassenunterrichtAddAllOhneUpdate(final @NotNull List<@NotNull StundenplanKlassenunterricht> list) {
		// check all
		final @NotNull HashSet<@NotNull String> setOfIDs = new HashSet<>();
		for (final @NotNull StundenplanKlassenunterricht klassenunterricht : list) {
			klassenunterrichtCheckAttributes(klassenunterricht);
			DeveloperNotificationException.ifTrue("klassenunterrichtAddAllOhneUpdate: KLASSE=" + klassenunterricht.idKlasse + ", FACH=" + klassenunterricht.idFach + " existiert bereits!", _klassenunterricht_by_idKlasse_and_idFach.contains(klassenunterricht.idKlasse, klassenunterricht.idFach));
			DeveloperNotificationException.ifTrue("klassenunterrichtAddAllOhneUpdate: ID=" + klassenunterricht.idKlasse + ", FACH=" + klassenunterricht.idFach + " doppelt in der Liste!", !setOfIDs.add(klassenunterricht.idKlasse + ";" + klassenunterricht.idFach));
		}

		// add all
		for (final @NotNull StundenplanKlassenunterricht klassenunterricht : list)
			DeveloperNotificationException.ifMap2DPutOverwrites(_klassenunterricht_by_idKlasse_and_idFach, klassenunterricht.idKlasse, klassenunterricht.idFach, klassenunterricht);
	}

	private void klassenunterrichtCheckAttributes(final @NotNull StundenplanKlassenunterricht klassenunterricht) {
		DeveloperNotificationException.ifMapNotContains("_klasse_by_id", _klasse_by_id, klassenunterricht.idKlasse);
		DeveloperNotificationException.ifMapNotContains("_fach_by_id", _fach_by_id, klassenunterricht.idFach);
		for (final @NotNull Long idSchiene : klassenunterricht.schienen)
			DeveloperNotificationException.ifMapNotContains("_schiene_by_id", _schiene_by_id, idSchiene);
	}

	private @NotNull Comparator<@NotNull StundenplanKlassenunterricht> klassenunterrichtCreateComparator() {

		final @NotNull Comparator<@NotNull StundenplanKlassenunterricht> comp = (final @NotNull StundenplanKlassenunterricht a, final @NotNull StundenplanKlassenunterricht b) -> {
				final @NotNull StundenplanKlasse aKlasse = DeveloperNotificationException.ifMapGetIsNull(_klasse_by_id, a.idKlasse);
				final @NotNull StundenplanKlasse bKlasse = DeveloperNotificationException.ifMapGetIsNull(_klasse_by_id, b.idKlasse);
				final int cmpKlasse = _compKlasse.compare(aKlasse, bKlasse);
				if (cmpKlasse != 0) return cmpKlasse;

				final @NotNull StundenplanFach aFach = DeveloperNotificationException.ifMapGetIsNull(_fach_by_id, a.idFach);
				final @NotNull StundenplanFach bFach = DeveloperNotificationException.ifMapGetIsNull(_fach_by_id, b.idFach);
				final int cmpFach = _compFach.compare(aFach, bFach);
				if (cmpFach != 0) return cmpFach;

				final int cmpLehrer = lehrerCompareByLehrerIDs(a.lehrer, b.lehrer);
				if (cmpLehrer != 0) return cmpLehrer;

				if (a.wochenstunden < b.wochenstunden) return -1;
				if (a.wochenstunden > b.wochenstunden) return +1;
				return a.bezeichnung.compareTo(b.bezeichnung);
			};

		return comp;
	}

	/**
	 * Liefert TRUE, falls der Klassenunterricht in das jeweilige Zeitraster gesetzt oder verschoben werden darf.
	 *
	 * @param klassenunterricht  Der {@link StundenplanKlassenunterricht}, welcher gesetzt oder verschoben werden soll.
	 * @param wochentag          Der Typ des {@link Wochentag}-Objekts.
	 * @param stunde             Die Unterrichtsstunde an dem Wochentag.
	 * @param wochentyp          Der Typ der Woche (beispielsweise bei AB-Wochen).
	 *
	 * @return TRUE, falls der Klassenunterricht in das jeweilige Zeitraster gesetzt oder verschoben werden darf.
	 */
	public boolean klassenunterrichtDarfInZelle(final @NotNull StundenplanKlassenunterricht klassenunterricht, final int wochentag, final int stunde, final int wochentyp) {
		for (final @NotNull StundenplanUnterricht partner : DeveloperNotificationException.ifMap2DGetIsNull(_unterrichtmenge_by_idKlasse_and_idFach, klassenunterricht.idKlasse, klassenunterricht.idFach)) {
			final @NotNull StundenplanZeitraster z = DeveloperNotificationException.ifMap2DGetIsNull(_zeitraster_by_wochentag_and_stunde, wochentag, stunde);
			if ((partner.idZeitraster == z.id) && ((partner.wochentyp == 0) || (wochentyp == 0) || (wochentyp == partner.wochentyp)))
				return false;
		}
		return true;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanKlassenunterricht}-Objekte.
	 * <br>Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link StundenplanKlassenunterricht}-Objekte.
	 */
	public @NotNull List<@NotNull StundenplanKlassenunterricht> klassenunterrichtGetMengeAsList() {
		return _klassenunterrichtmenge;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanKlassenunterricht}-Objekte der Klasse.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idKlasse  Die Datenbank-ID der Klasse.
	 *
	 * @return eine Liste aller {@link StundenplanKlassenunterricht}-Objekte der Klasse.
	 */
	public @NotNull List<@NotNull StundenplanKlassenunterricht> klassenunterrichtGetMengeByKlasseIdAsList(final long idKlasse) {
		return MapUtils.getOrCreateArrayList(_klassenunterrichtmenge_by_idKlasse, idKlasse);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanKlassenunterricht}-Objekte der Klasse einer bestimmten Schiene.
	 * <br>Hinweis: Ist die ID der Schiene -1, sind alle {@link StundenplanKlassenunterricht}-Objekte ohne Schienenzugehörigkeit gemeint.
	 *
	 * @param idKlasse      Die Datenbank-ID der Klasse.
	 * @param idSchiene     Die Datenbank-ID der Schiene, oder -1, falls Unterricht ohne Schiene gemeint ist.
	 *
	 * @return eine Liste aller {@link StundenplanKlassenunterricht}-Objekte der Klasse einer bestimmten Schiene.
	 */
	public @NotNull List<@NotNull StundenplanKlassenunterricht> klassenunterrichtGetMengeByKlasseIdAndSchieneId(final long idKlasse, final long idSchiene) {
		return Map2DUtils.getOrCreateArrayList(_klassenunterrichtmenge_by_idKlasse_and_idSchiene, idKlasse, idSchiene);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanKlassenunterricht}-Objekte des Lehrers.
	 * <br> Laufzeit: O(1)
	 *
	 * @param idLehrer  Die Datenbank-ID des Lehrers.
	 *
	 * @return eine Liste aller {@link StundenplanKlassenunterricht}-Objekte des Lehrers.
	 */
	public @NotNull List<@NotNull StundenplanKlassenunterricht> klassenunterrichtGetMengeByLehrerIdAsList(final long idLehrer) {
		return MapUtils.getOrCreateArrayList(_klassenunterrichtmenge_by_idLehrer, idLehrer);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanKlassenunterricht}-Objekte des Schülers.
	 * <br> Laufzeit: O(1)
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return eine Liste aller {@link StundenplanKlassenunterricht}-Objekte des Schülers.
	 */
	public @NotNull List<@NotNull StundenplanKlassenunterricht> klassenunterrichtGetMengeBySchuelerIdAsList(final long idSchueler) {
		return MapUtils.getOrCreateArrayList(_klassenunterrichtmenge_by_idSchueler, idSchueler);
	}


	/**
	 * Liefert die SOLL-Wochenminuten des {@link StundenplanKlassenunterricht}.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idKlasse  Die Datenbank-ID der Klasse.
	 * @param idFach    Die Datenbank-ID des Faches.
	 *
	 * @return die SOLL-Wochenminuten des {@link StundenplanKlassenunterricht}.
	 */
	public int klassenunterrichtGetWochenminutenSOLL(final long idKlasse, final long idFach) {
		return DeveloperNotificationException.ifMap2DGetIsNull(_klassenunterricht_by_idKlasse_and_idFach, idKlasse, idFach).wochenstunden * FAKTOR_WOCHENSTUNDEN_ZU_MINUTEN;
	}

	/**
	 * Liefert die IST-Wochenminuten des {@link StundenplanKlassenunterricht} auf 2 Nachkommastellen gerundet.
	 * <br>Hinweis 1: Durch AB-Wochen, ist der Rückgabewert eine Kommazahl, da nur Stundenanteile gesetzt sein können.
	 * <br>Hinweis 2: Der Wert kann größer als der SOLL-Wert sein, wenn mehr Unterricht als nötig gesetzt wurde.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idKlasse  Die Datenbank-ID der Klasse.
	 * @param idFach    Die Datenbank-ID des Faches.
	 *
	 * @return die IST-Wochenminuten des {@link StundenplanKlassenunterricht} auf 2 Nachkommastellen gerundet.
	 */
	public double klassenunterrichtGetWochenminutenIST(final long idKlasse, final long idFach) {
		final double wochenminuten = DeveloperNotificationException.ifMap2DGetIsNull(_wertWochenminuten_by_idKlasse_und_idFach, idKlasse, idFach);
		return gerundetAufZweiNachkommastellen(wochenminuten);
	}

	/**
	 * Liefert die Differenz aus SOLL-Wochenminuten minus IST-Wochenminuten des {@link StundenplanKlassenunterricht} auf 2 Nachkommastellen gerundet.
	 * <br>Hinweis 1: Durch AB-Wochen, ist der Rückgabewert eine Kommazahl, da nur Stundenanteile gesetzt sein können.
	 * <br>Hinweis 2: Der Wert kann negativ sein.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idKlasse  Die Datenbank-ID der Klasse.
	 * @param idFach    Die Datenbank-ID des Faches.
	 *
	 * @return die Differenz aus SOLL-Wochenminuten minus IST-Wochenminuten des {@link StundenplanKlassenunterricht} auf 2 Nachkommastellen gerundet.
	 */
	public double klassenunterrichtGetWochenminutenREST(final long idKlasse, final long idFach) {
		return klassenunterrichtGetWochenminutenSOLL(idKlasse, idFach) - klassenunterrichtGetWochenminutenIST(idKlasse, idFach);
	}

	/**
	 * Liefert die SOLL-Wochenstunden des {@link StundenplanKlassenunterricht}.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idKlasse  Die Datenbank-ID der Klasse.
	 * @param idFach    Die Datenbank-ID des Faches.
	 *
	 * @return die SOLL-Wochenstunden des {@link StundenplanKlassenunterricht}.
	 */
	public int klassenunterrichtGetWochenstundenSOLL(final long idKlasse, final long idFach) {
		return DeveloperNotificationException.ifMap2DGetIsNull(_klassenunterricht_by_idKlasse_and_idFach, idKlasse, idFach).wochenstunden;
	}

	/**
	 * Liefert die IST-Wochenstunden des {@link StundenplanKlassenunterricht} auf 2 Nachkommastellen gerundet.
	 * <br>Hinweis 1: Durch AB-Wochen, ist der Rückgabewert eine Kommazahl, da nur Stundenanteile gesetzt sein können.
	 * <br>Hinweis 2: Durch Zeitraster, die nicht 45-Minuten entsprechen, können nur Stundenanteile gesetzt sein.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idKlasse  Die Datenbank-ID der Klasse.
	 * @param idFach    Die Datenbank-ID des Faches.
	 *
	 * @return die IST-Wochenstunden des {@link StundenplanKlassenunterricht} auf 2 Nachkommastellen gerundet.
	 */
	public double klassenunterrichtGetWochenstundenIST(final long idKlasse, final long idFach) {
		final double wochenminuten = DeveloperNotificationException.ifMap2DGetIsNull(_wertWochenminuten_by_idKlasse_und_idFach, idKlasse, idFach);
		return gerundetAufZweiNachkommastellen(wochenminuten / FAKTOR_WOCHENSTUNDEN_ZU_MINUTEN);
	}

	/**
	 * Liefert die Differenz aus SOLL-Wochenstunden minus IST-Wochenstunden des {@link StundenplanKlassenunterricht} auf 2 Nachkommastellen gerundet.
	 * <br>Hinweis 1: Durch AB-Wochen, ist der Rückgabewert eine Kommazahl, da nur Stundenanteile gesetzt sein können.
	 * <br>Hinweis 2: Durch Zeitraster, die nicht 45 min entsprechen, können nur Stundenanteile gesetzt sein.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idKlasse  Die Datenbank-ID der Klasse.
	 * @param idFach    Die Datenbank-ID des Faches.
	 *
	 * @return die Differenz aus SOLL-Wochenstunden minus IST-Wochenstunden des {@link StundenplanKlassenunterricht} auf 2 Nachkommastellen gerundet.
	 */
	public double klassenunterrichtGetWochenstundenREST(final long idKlasse, final long idFach) {
		return klassenunterrichtGetWochenstundenSOLL(idKlasse, idFach) - klassenunterrichtGetWochenstundenIST(idKlasse, idFach);
	}

	private void klassenunterrichtRemoveOhneUpdateById(final long idKlasse, final long idFach) {
		// Kaskade: StundenplanUnterricht (des Klassenunterrichts)
		for (final @NotNull StundenplanUnterricht u : DeveloperNotificationException.ifMap2DGetIsNull(_unterrichtmenge_by_idKlasse_and_idFach, idKlasse, idFach))
			unterrichtRemoveByIdOhneUpdate(u.id);

		// Entfernen
		DeveloperNotificationException.ifMap2DRemoveFailes(_klassenunterricht_by_idKlasse_and_idFach, idKlasse, idFach);
	}

	/**
	 * Entfernt ein {@link StundenplanKlassenunterricht}-Objekt anhand seiner ID.
	 *
	 * @param idKlasse  Die Datenbank-ID der Klasse.
	 * @param idFach    Die Datenbank-ID des Faches.
	 */
	public void klassenunterrichtRemoveById(final long idKlasse, final long idFach) {
		klassenunterrichtRemoveOhneUpdateById(idKlasse, idFach);

		update_all();
	}

	/**
	 * Entfernt alle {@link StundenplanKlassenunterricht}-Objekte.
	 *
	 * @param listKlassenunterricht  Die Liste der zu entfernenden {@link StundenplanKlassenunterricht}-Objekte.
	 */
	public void klassenunterrichtRemoveAll(final @NotNull List<@NotNull StundenplanKlassenunterricht> listKlassenunterricht) {
		for (final @NotNull StundenplanKlassenunterricht klassenunterricht : listKlassenunterricht)
			klassenunterrichtRemoveOhneUpdateById(klassenunterricht.idKlasse, klassenunterricht.idFach);

		update_all();
	}

	private static double gerundetAufZweiNachkommastellen(final double d) { // TODO sinnvolle Benennung
		if (d >= 0)
			return (Math.round(d * 100.0)) / 100.0;
		return -(Math.round(-d * 100.0)) / 100.0;
	}

	// #####################################################################
	// #################### StundenplanKurs ################################
	// #####################################################################

	/**
	 * Fügt ein {@link StundenplanKurs}-Objekt hinzu.
	 *
	 * @param kurs  Das {@link StundenplanKurs}-Objekt, welches hinzugefügt werden soll.
	 */
	public void kursAdd(final @NotNull StundenplanKurs kurs) {
		kursAddAll(ListUtils.create1(kurs));
	}

	/**
	 * Fügt alle {@link StundenplanKurs}-Objekte hinzu.
	 *
	 * @param listKurs  Die Menge der {@link StundenplanKurs}-Objekte, welche hinzugefügt werden soll.
	 */
	public void kursAddAll(final @NotNull List<@NotNull StundenplanKurs> listKurs) {
		kursAddAllOhneUpdate(listKurs);
		update_all();
	}

	private void kursAddAllOhneUpdate(final @NotNull List<@NotNull StundenplanKurs> list) {
		// check all
		final @NotNull HashSet<@NotNull Long> setOfIDs = new HashSet<>();
		for (final @NotNull StundenplanKurs kurs : list) {
			kursCheckAttributes(kurs);
			DeveloperNotificationException.ifTrue("kursAddAllOhneUpdate: ID=" + kurs.id + " existiert bereits!", _kurs_by_id.containsKey(kurs.id));
			DeveloperNotificationException.ifTrue("kursAddAllOhneUpdate: ID=" + kurs.id + " doppelt in der Liste!", !setOfIDs.add(kurs.id));
		}

		// add all
		for (final @NotNull StundenplanKurs kurs : list)
			DeveloperNotificationException.ifMapPutOverwrites(_kurs_by_id, kurs.id, kurs);
	}

	private void kursCheckAttributes(final @NotNull StundenplanKurs kurs) {
		DeveloperNotificationException.ifInvalidID("kurs.id", kurs.id);
		DeveloperNotificationException.ifStringIsBlank("kurs.bezeichnung", kurs.bezeichnung);
		DeveloperNotificationException.ifSmaller("kurs.wochenstunden", kurs.wochenstunden, 0);
		for (final @NotNull Long idSchieneDesKurses : kurs.schienen)
			DeveloperNotificationException.ifMapNotContains("_schiene_by_id", _schiene_by_id, idSchieneDesKurses);
		for (final @NotNull Long idJahrgangDesKurses : kurs.jahrgaenge)
			DeveloperNotificationException.ifMapNotContains("_jahrgang_by_id", _jahrgang_by_id, idJahrgangDesKurses);
		for (final @NotNull Long idSchuelerDesKurses : kurs.schueler)
			DeveloperNotificationException.ifMapNotContains("_schueler_by_id", _schueler_by_id, idSchuelerDesKurses);
		for (final @NotNull Long idLehrerDesKurses : kurs.lehrer)
			DeveloperNotificationException.ifMapNotContains("_lehrer_by_id", _lehrer_by_id, idLehrerDesKurses);
	}

	/**
	 * Liefert TRUE, falls der Kurs in das jeweilige Zeitraster gesetzt oder verschoben werden darf.
	 *
	 * @param kurs       Der {@link StundenplanKurs}, welcher gesetzt oder verschoben werden soll.
	 * @param wochentag  Der Typ des {@link Wochentag}-Objekts.
	 * @param stunde     Die Unterrichtsstunde an dem Wochentag.
	 * @param wochentyp  Der Typ der Woche (beispielsweise bei AB-Wochen).
	 *
	 * @return TRUE, falls der Kurs in das jeweilige Zeitraster gesetzt oder verschoben werden darf.
	 */
	public boolean kursDarfInZelle(final @NotNull StundenplanKurs kurs, final int wochentag, final int stunde, final int wochentyp) {
		for (final @NotNull StundenplanUnterricht partner : DeveloperNotificationException.ifMapGetIsNull(_unterrichtmenge_by_idKurs, kurs.id)) {
			final @NotNull StundenplanZeitraster z = DeveloperNotificationException.ifMap2DGetIsNull(_zeitraster_by_wochentag_and_stunde, wochentag, stunde);
			if ((partner.idZeitraster == z.id) && ((partner.wochentyp == 0) || (wochentyp == 0) || (wochentyp == partner.wochentyp)))
				return false;
		}
		return true;
	}

	/**
	 * Liefert das {@link StundenplanKurs}-Objekt mit der übergebenen ID.
	 *
	 * @param idKurs  Die Datenbank-ID des {@link StundenplanKurs}-Objekts.
	 *
	 * @return das {@link StundenplanKurs}-Objekt mit der übergebenen ID.
	 */
	public @NotNull StundenplanKurs kursGetByIdOrException(final long idKurs) {
		return DeveloperNotificationException.ifMapGetIsNull(_kurs_by_id, idKurs);
	}

	/**
	 * Liefert TRUE, falls der übergebene Kurs am (Wochentyp / Wochentag / Unterrichtsstunde) stattfindet.
	 * <br>Laufzeit: O(|Unterrichtmenge des Kurses|)
	 *
	 * @param idKurs            Die Datenbank-ID des Kurses.
	 * @param wochentyp         Der Typ der Woche (beispielsweise bei AB-Wochen).
	 * @param wochentag         Der gewünschte {@link Wochentag}.
	 * @param unterrichtstunde  Die gewünschte Unterrichtsstunde.
	 *
	 * @return TRUE, falls der übergebene Kurs am (wochentyp / wochentag / Unterrichtsstunde) stattfindet.
	 */
	public boolean kursGetHatUnterrichtAm(final long idKurs, final int wochentyp, final @NotNull Wochentag wochentag, final int unterrichtstunde) {
		for (final @NotNull StundenplanUnterricht u : unterrichtGetMengeByKursIdAndWochentyp(idKurs, wochentyp)) {
			final @NotNull StundenplanZeitraster z =  zeitrasterGetByIdOrException(u.idZeitraster);
			if ((z.wochentag == wochentag.id) && (z.unterrichtstunde == unterrichtstunde))
				return true;
		}
		return false;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanKurs}-Objekte.
	 * <br>Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link StundenplanKurs}-Objekte.
	 */
	public @NotNull List<@NotNull StundenplanKurs> kursGetMengeAsList() {
		return _kursmenge;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanKurs}-Objekte der Klasse.
	 * <br> Laufzeit: O(1)
	 *
	 * @param idKlasse  Die Datenbank-ID der Klasse.
	 *
	 * @return eine Liste aller {@link StundenplanKurs}-Objekte der Klasse.
	 */
	public @NotNull List<@NotNull StundenplanKurs> kursGetMengeByKlasseIdAsList(final long idKlasse) {
		return MapUtils.getOrCreateArrayList(_kursmenge_by_idKlasse, idKlasse);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanKurs}-Objekte des Lehrers.
	 * <br> Laufzeit: O(1)
	 *
	 * @param idLehrer  Die Datenbank-ID des Lehrers.
	 *
	 * @return eine Liste aller {@link StundenplanKurs}-Objekte des Lehrers.
	 */
	public @NotNull List<@NotNull StundenplanKurs> kursGetMengeByLehrerIdAsList(final long idLehrer) {
		return MapUtils.getOrCreateArrayList(_kursmenge_by_idLehrer, idLehrer);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanKurs}-Objekte des Schülers.
	 * <br> Laufzeit: O(1)
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return eine Liste aller {@link StundenplanKurs}-Objekte des Schülers.
	 */
	public @NotNull List<@NotNull StundenplanKurs> kursGetMengeBySchuelerIdAsList(final long idSchueler) {
		return MapUtils.getOrCreateArrayList(_kursmenge_by_idSchueler, idSchueler);
	}

	/**
	 * Liefert gefilterte Kurs-IDs, deren Unterricht zu (Wochentyp / Wochentag / Unterrichtsstunde) passt.
	 *
	 * @param idsKurs          Die Liste aller Kurs-IDs.
	 * @param wochentyp        Der Typ der Woche (beispielsweise bei AB-Wochen).
	 * @param wochentag        Der gewünschte {@link Wochentag}.
	 * @param unterrichtstunde Die gewünschte Unterrichtsstunde.
	 *
	 * @return gefilterte Kurs-IDs, deren Unterricht zu (Wochentyp / Wochentag / Unterrichtsstunde) passt.
	 */
	public @NotNull List<@NotNull Long> kursGetMengeGefiltertByWochentypAndWochentagAndStunde(final @NotNull List<@NotNull Long> idsKurs, final int wochentyp, final @NotNull Wochentag wochentag, final int unterrichtstunde) {
		return CollectionUtils.toFilteredArrayList(idsKurs, (final @NotNull Long idKurs) -> kursGetHatUnterrichtAm(idKurs, wochentyp, wochentag, unterrichtstunde));
	}

	/**
	 * Liefert gefilterte Kurs-IDs, deren Unterricht zu (Datum / Unterrichtsstunde) passt.
	 *
	 * @param idsKurs          Die Liste aller Kurs-IDs.
	 * @param datumISO8601     Das Datum. Daraus ergibt sich (Wochentyp / Wochentag).
	 * @param unterrichtstunde Die gewünschte Unterrichtsstunde.
	 *
	 * @return gefilterte Kurs-IDs, deren Unterricht zu (Datum / Unterrichtsstunde) passt.
	 */
	public @NotNull List<@NotNull Long> kursGetMengeGefiltertByDatumAndStunde(final @NotNull List<@NotNull Long> idsKurs, final @NotNull String datumISO8601, final int unterrichtstunde) {
		final int[] e = DateUtils.extractFromDateISO8601(datumISO8601);
		final int wochentyp = kalenderwochenzuordnungGetWochentypOrDefault(e[6], e[5]); // 6 = kalenderwochenjahr, 5 = kalenderwoche
		final @NotNull Wochentag wochentag = Wochentag.fromIDorException(e[3]); // 3 = tagInWoche
		return kursGetMengeGefiltertByWochentypAndWochentagAndStunde(idsKurs, wochentyp, wochentag, unterrichtstunde);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanKurs}-Objekte der Klasse einer bestimmten Schiene.
	 * <br>Hinweis: Ist die ID der Schiene -1, sind alle {@link StundenplanKurs}-Objekte ohne Schienenzugehörigkeit gemeint.
	 *
	 * @param idKlasse      Die Datenbank-ID der Klasse.
	 * @param idSchiene     Die Datenbank-ID der Schiene, oder -1, falls Unterricht ohne Schiene gemeint ist.
	 *
	 * @return eine Liste aller {@link StundenplanKurs}-Objekte der Klasse einer bestimmten Schiene.
	 */
	public @NotNull List<@NotNull StundenplanKurs> kursGetMengeByKlasseIdAndSchieneId(final long idKlasse, final long idSchiene) {
		return Map2DUtils.getOrCreateArrayList(_kursmenge_by_idKlasse_and_idSchiene, idKlasse, idSchiene);
	}



	/**
	 * Liefert die SOLL-Wochenminuten des {@link StundenplanKurs}.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die SOLL-Wochenminuten des {@link StundenplanKurs}.
	 */
	public int kursGetWochenminutenSOLL(final long idKurs) {
		return DeveloperNotificationException.ifMapGetIsNull(_kurs_by_id, idKurs).wochenstunden * FAKTOR_WOCHENSTUNDEN_ZU_MINUTEN;
	}

	/**
	 * Liefert die IST-Wochenminuten des {@link StundenplanKurs} auf 2 Nachkommastellen gerundet.
	 * <br>Hinweis 1: Durch AB-Wochen, ist der Rückgabewert eine Kommazahl, da nur Stundenanteile gesetzt sein können.
	 * <br>Hinweis 2: Der Wert kann größer als der SOLL-Wert sein, wenn mehr Unterricht als nötig gesetzt wurde.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die IST-Wochenminuten des {@link StundenplanKurs} auf 2 Nachkommastellen gerundet.
	 */
	public double kursGetWochenminutenIST(final long idKurs) {
		final double wochenminuten = DeveloperNotificationException.ifMapGetIsNull(_wertWochenminuten_by_idKurs, idKurs);
		return gerundetAufZweiNachkommastellen(wochenminuten);
	}

	/**
	 * Liefert die Differenz aus SOLL-Wochenminuten minus IST-Wochenminuten des {@link StundenplanKurs} auf 2 Nachkommastellen gerundet.
	 * <br>Hinweis 1: Durch AB-Wochen, ist der Rückgabewert eine Kommazahl, da nur Stundenanteile gesetzt sein können.
	 * <br>Hinweis 2: Der Wert kann negativ sein.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Differenz aus SOLL-Wochenminuten minus IST-Wochenminuten des {@link StundenplanKurs} auf 2 Nachkommastellen gerundet.
	 */
	public double kursGetWochenminutenREST(final long idKurs) {
		return kursGetWochenminutenSOLL(idKurs) - kursGetWochenminutenIST(idKurs);
	}

	/**
	 * Liefert die SOLL-Wochenstunden des {@link StundenplanKurs}.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die SOLL-Wochenstunden des {@link StundenplanKurs}.
	 */
	public int kursGetWochenstundenSOLL(final long idKurs) {
		return DeveloperNotificationException.ifMapGetIsNull(_kurs_by_id, idKurs).wochenstunden;
	}

	/**
	 * Liefert die IST-Wochenstunden des {@link StundenplanKurs} auf 2 Nachkommastellen gerundet.
	 * <br>Hinweis 1: Durch AB-Wochen, ist der Rückgabewert eine Kommazahl, da nur Stundenanteile gesetzt sein können.
	 * <br>Hinweis 2: Durch Zeitraster, die nicht 45-Minuten entsprechen, können nur Stundenanteile gesetzt sein.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses auf 2 Nachkommastellen gerundet.
	 *
	 * @return die IST-Wochenstunden des {@link StundenplanKurs} auf 2 Nachkommastellen gerundet.
	 */
	public double kursGetWochenstundenIST(final long idKurs) {
		final double wochenminuten = DeveloperNotificationException.ifMapGetIsNull(_wertWochenminuten_by_idKurs, idKurs);
		return gerundetAufZweiNachkommastellen(wochenminuten / FAKTOR_WOCHENSTUNDEN_ZU_MINUTEN);
	}

	/**
	 * Liefert die Differenz aus SOLL-Wochenstunden minus IST-Wochenstunden des {@link StundenplanKurs} auf 2 Nachkommastellen gerundet.
	 * <br>Hinweis 1: Durch AB-Wochen, ist der Rückgabewert eine Kommazahl, da nur Stundenanteile gesetzt sein können.
	 * <br>Hinweis 2: Durch Zeitraster, die nicht 45 min entsprechen, können nur Stundenanteile gesetzt sein.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Differenz aus SOLL-Wochenstunden minus IST-Wochenstunden des {@link StundenplanKurs} auf 2 Nachkommastellen gerundet.
	 */
	public double kursGetWochenstundenREST(final long idKurs) {
		return kursGetWochenstundenSOLL(idKurs) - kursGetWochenstundenIST(idKurs);
	}

	/**
	 * Aktualisiert das vorhandene {@link StundenplanKurs}-Objekt durch das neue Objekt.
	 * <br>Die folgenden Attribute werden nicht aktualisiert:
	 * <br>{@link StundenplanKurs#id}
	 * <br>
	 * <br>Die folgenden Attribute werden kopiert:
	 * <br>{@link StundenplanKurs#bezeichnung}
	 * <br>{@link StundenplanKurs#wochenstunden}
	 * <br>{@link StundenplanKurs#jahrgaenge}
	 * <br>{@link StundenplanKurs#schienen}
	 * <br>{@link StundenplanKurs#schueler}
	 *
	 * @param kurs  Das neue {@link StundenplanKurs}-Objekt, dessen Attribute kopiert werden.
	 */
	public void kursPatchAttributtes(final @NotNull StundenplanKurs kurs) {
		kursCheckAttributes(kurs);

		// Altes Objekt durch neues Objekt ersetzen
		DeveloperNotificationException.ifMapRemoveFailes(_kurs_by_id, kurs.id);
		DeveloperNotificationException.ifMapPutOverwrites(_kurs_by_id, kurs.id, kurs);

		update_all();
	}

	private void kursRemoveOhneUpdateById(final long idKurs) {
		// Kaskade: StundenplanUnterricht
		for (final @NotNull StundenplanUnterricht u : MapUtils.getOrCreateArrayList(_unterrichtmenge_by_idKurs, idKurs))
			unterrichtRemoveByIdOhneUpdate(u.id);

		// Entfernen
		DeveloperNotificationException.ifMapRemoveFailes(_kurs_by_id, idKurs);
	}

	/**
	 * Entfernt ein {@link StundenplanKurs}-Objekt anhand seiner ID.
	 *
	 * @param idKurs  Die Datenbank-ID des {@link StundenplanKurs}-Objekts, welches entfernt werden soll.
	 */
	public void kursRemoveById(final long idKurs) {
		kursRemoveOhneUpdateById(idKurs);

		update_all();
	}

	/**
	 * Entfernt alle {@link StundenplanKurs}-Objekte.
	 *
	 * @param listKurs  Die Liste der zu entfernenden {@link StundenplanKurs}-Objekte.
	 */
	public void kursRemoveAll(final @NotNull List<@NotNull StundenplanKurs> listKurs) {
		for (final @NotNull StundenplanKurs kurs : listKurs)
			kursRemoveOhneUpdateById(kurs.id);

		update_all();
	}

	// #####################################################################
	// #################### StundenplanLehrer ##############################
	// #####################################################################

	/**
	 * Fügt ein {@link StundenplanLehrer}-Objekt hinzu.
	 *
	 * @param lehrer  Das {@link StundenplanLehrer}-Objekt, welches hinzugefügt werden soll.
	 */
	public void lehrerAdd(final @NotNull StundenplanLehrer lehrer) {
		lehrerAddAll(ListUtils.create1(lehrer));
	}

	/**
	 * Fügt alle {@link StundenplanLehrer}-Objekte hinzu.
	 *
	 * @param listLehrer  Die Menge der {@link StundenplanLehrer}-Objekte, welche hinzugefügt werden soll.
	 */
	public void lehrerAddAll(final @NotNull List<@NotNull StundenplanLehrer> listLehrer) {
		lehrerAddAllOhneUpdate(listLehrer);
		update_all();
	}

	private void lehrerAddAllOhneUpdate(final @NotNull List<@NotNull StundenplanLehrer> list) {
		// check all
		final @NotNull HashSet<@NotNull Long> setOfIDs = new HashSet<>();
		for (final @NotNull StundenplanLehrer lehrer : list) {
			lehrerCheckAttributes(lehrer);
			DeveloperNotificationException.ifTrue("lehrerAddAllOhneUpdate: ID=" + lehrer.id + " existiert bereits!", _lehrer_by_id.containsKey(lehrer.id));
			DeveloperNotificationException.ifTrue("lehrerAddAllOhneUpdate: ID=" + lehrer.id + " doppelt in der Liste!", !setOfIDs.add(lehrer.id));
		}

		// add all
		for (final @NotNull StundenplanLehrer lehrer : list)
			DeveloperNotificationException.ifMapPutOverwrites(_lehrer_by_id, lehrer.id, lehrer);
	}

	private void lehrerCheckAttributes(final @NotNull StundenplanLehrer lehrer) {
		DeveloperNotificationException.ifInvalidID("lehrer.id", lehrer.id);
		DeveloperNotificationException.ifStringIsBlank("lehrer.kuerzel", lehrer.kuerzel);
		DeveloperNotificationException.ifStringIsBlank("lehrer.nachname", lehrer.nachname);
		DeveloperNotificationException.ifStringIsBlank("lehrer.vorname", lehrer.vorname);
		for (final @NotNull Long idFachDesLehrers : lehrer.faecher)
			DeveloperNotificationException.ifMapNotContains("_fach_by_id", _fach_by_id, idFachDesLehrers);
	}

	private int lehrerCompareByLehrerIDs(final @NotNull List<@NotNull Long> a, final @NotNull List<@NotNull Long> b) {
		if (a.size() < b.size()) return -1;
		if (a.size() > b.size()) return +1;
		for (int i = 0; i < a.size(); i++) {
			final @NotNull Long aIdLehrer = ListUtils.getNonNullElementAtOrException(a, i);
			final @NotNull Long bIdLehrer = ListUtils.getNonNullElementAtOrException(b, i);
			final @NotNull StundenplanLehrer aLehrer = DeveloperNotificationException.ifMapGetIsNull(_lehrer_by_id, aIdLehrer);
			final @NotNull StundenplanLehrer bLehrer = DeveloperNotificationException.ifMapGetIsNull(_lehrer_by_id, bIdLehrer);
			final int cmpLehrer = _compLehrer.compare(aLehrer, bLehrer);
			if (cmpLehrer != 0) return cmpLehrer;
		}
		return 0;
	}

	/**
	 * Liefert das {@link StundenplanLehrer}-Objekt mit der übergebenen ID.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idLehrer  Die Datenbank-ID des {@link StundenplanLehrer}-Objekts.
	 *
	 * @return das {@link StundenplanLehrer}-Objekt mit der übergebenen ID.
	 */
	public @NotNull StundenplanLehrer lehrerGetByIdOrException(final long idLehrer) {
		return DeveloperNotificationException.ifMapGetIsNull(_lehrer_by_id, idLehrer);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanLehrer}-Objekte.
	 * <br>Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link StundenplanLehrer}-Objekte.
	 */
	public @NotNull List<@NotNull StundenplanLehrer> lehrerGetMengeAsList() {
		return _lehrermenge_sortiert;
	}

	/**
	 * Aktualisiert das vorhandene {@link StundenplanLehrer}-Objekt durch das neue Objekt.
	 * <br>Die folgenden Attribute werden nicht aktualisiert:
	 * <br>{@link StundenplanLehrer#id}
	 * <br>
	 * <br>Die folgenden Attribute werden kopiert:
	 * <br>{@link StundenplanLehrer#kuerzel}
	 * <br>{@link StundenplanLehrer#nachname}
	 * <br>{@link StundenplanLehrer#vorname}
	 * <br>{@link StundenplanLehrer#faecher}
	 *
	 * @param lehrer  Das neue {@link StundenplanLehrer}-Objekt, dessen Attribute kopiert werden.
	 */
	public void lehrerPatchAttributes(final @NotNull StundenplanLehrer lehrer) {
		// check
		lehrerCheckAttributes(lehrer);

		// replace
		DeveloperNotificationException.ifMapRemoveFailes(_lehrer_by_id, lehrer.id);
		DeveloperNotificationException.ifMapPutOverwrites(_lehrer_by_id, lehrer.id, lehrer);

		// update
		update_all();
	}

	private void lehrerRemoveOhneUpdateById(final long idLehrer) {
		// Kaskade: StundenplanKurs
		for (final @NotNull StundenplanKurs kurs : MapUtils.getOrCreateArrayList(_kursmenge_by_idLehrer, idLehrer))
			kurs.lehrer.remove(idLehrer);

		// Kaskade: StundenplanKlassenunterricht
		for (final @NotNull StundenplanKlassenunterricht ku : MapUtils.getOrCreateArrayList(_klassenunterrichtmenge_by_idLehrer, idLehrer))
			ku.lehrer.remove(idLehrer);

		// Kaskade: StundenplanUnterricht
		for (final @NotNull StundenplanUnterricht u : MapUtils.getOrCreateArrayList(_unterrichtmenge_by_idLehrer, idLehrer))
			u.lehrer.remove(idLehrer);

		// Kaskade: StundenplanPausenaufsicht
		for (final @NotNull StundenplanPausenaufsicht pa : MapUtils.getOrCreateArrayList(_pausenaufsichtmenge_by_idLehrer, idLehrer))
			pausenaufsichtRemoveOhneUpdateById(pa.id);

		// Entfernen
		DeveloperNotificationException.ifMapRemoveFailes(_lehrer_by_id, idLehrer);
	}

	/**
	 * Entfernt ein {@link StundenplanLehrer}-Objekt anhand seiner ID.
	 *
	 * @param idLehrer  Die Datenbank-ID des {@link StundenplanLehrer}-Objekts, welches entfernt werden soll.
	 */
	public void lehrerRemoveById(final long idLehrer) {
		lehrerRemoveOhneUpdateById(idLehrer);

		update_all();
	}

	/**
	 * Entfernt alle {@link StundenplanLehrer}-Objekte.
	 *
	 * @param listLehrer  Die Liste der zu entfernenden {@link StundenplanLehrer}-Objekte.
	 */
	public void lehrerRemoveAll(final @NotNull List<@NotNull StundenplanLehrer> listLehrer) {
		// check
		final @NotNull HashSet<@NotNull Long> setOfIDs = new HashSet<>();
		for (final @NotNull StundenplanLehrer lehrer : listLehrer) {
			if (!_lehrer_by_id.containsKey(lehrer.id))
				throw new DeveloperNotificationException("lehrerRemoveAll: Lehrer-ID existiert nicht!");
			if (!setOfIDs.add(lehrer.id))
				throw new DeveloperNotificationException("lehrerRemoveAll: Doppelte Lehrer-ID in der Liste!");
		}

		// remove
		for (final @NotNull StundenplanLehrer lehrer : listLehrer)
			lehrerRemoveOhneUpdateById(lehrer.id);

		update_all();
	}

	// #####################################################################
	// #################### StundenplanPausenaufsicht ######################
	// #####################################################################

	/**
	 * Fügt ein {@link StundenplanPausenaufsicht}-Objekt hinzu.
	 *
	 * @param pausenaufsicht  Das {@link StundenplanPausenaufsicht}-Objekt, welches hinzugefügt werden soll.
	 */
	public void pausenaufsichtAdd(final @NotNull StundenplanPausenaufsicht pausenaufsicht) {
		pausenaufsichtAddAll(ListUtils.create1(pausenaufsicht));
	}

	/**
	 * Fügt alle {@link StundenplanPausenaufsicht}-Objekte hinzu.
	 *
	 * @param listPausenaufsicht  Die Menge der {@link StundenplanPausenaufsicht}-Objekte, welche hinzugefügt werden soll.
	 */
	public void pausenaufsichtAddAll(final @NotNull List<@NotNull StundenplanPausenaufsicht> listPausenaufsicht) {
		pausenaufsichtAddAllOhneUpdate(listPausenaufsicht);
		update_all();
	}

	private void pausenaufsichtAddAllOhneUpdate(final @NotNull List<@NotNull StundenplanPausenaufsicht> list) {
		// check all
		final @NotNull HashSet<@NotNull Long> setOfIDs = new HashSet<>();
		for (final @NotNull StundenplanPausenaufsicht pausenaufsicht : list) {
			pausenaufsichtCheckAttributes(pausenaufsicht);
			DeveloperNotificationException.ifTrue("pausenaufsichtAddAllOhneUpdate: ID=" + pausenaufsicht.id + " existiert bereits!", _pausenaufsicht_by_id.containsKey(pausenaufsicht.id));
			DeveloperNotificationException.ifTrue("pausenaufsichtAddAllOhneUpdate: ID=" + pausenaufsicht.id + " doppelt in der Liste!", !setOfIDs.add(pausenaufsicht.id));
		}

		// add all
		for (final @NotNull StundenplanPausenaufsicht pausenaufsicht : list)
			DeveloperNotificationException.ifMapPutOverwrites(_pausenaufsicht_by_id, pausenaufsicht.id, pausenaufsicht);
	}

	private void pausenaufsichtCheckAttributes(final @NotNull StundenplanPausenaufsicht pausenaufsicht) {
		DeveloperNotificationException.ifInvalidID("pausenaufsicht.id", pausenaufsicht.id);
		DeveloperNotificationException.ifMapNotContains("_map_idLehrer_zu_lehrer", _lehrer_by_id, pausenaufsicht.idLehrer);
		DeveloperNotificationException.ifMapNotContains("_map_idPausenzeit_zu_pausenzeit", _pausenzeit_by_id, pausenaufsicht.idPausenzeit);
		DeveloperNotificationException.ifTrue("(pa.wochentyp > 0) && (pa.wochentyp > stundenplanWochenTypModell)", (pausenaufsicht.wochentyp > 0) && (pausenaufsicht.wochentyp > _stundenplanWochenTypModell));
		for (final @NotNull Long idAufsichtsbereich : pausenaufsicht.bereiche)
			DeveloperNotificationException.ifMapNotContains("_aufsichtsbereich_by_id", _aufsichtsbereich_by_id, idAufsichtsbereich);
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanPausenaufsicht}-Objekt.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idPausenaufsicht Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanPausenaufsicht}-Objekt.
	 */
	public @NotNull StundenplanPausenaufsicht pausenaufsichtGetByIdOrException(final long idPausenaufsicht) {
		return DeveloperNotificationException.ifMapGetIsNull(_pausenaufsicht_by_id, idPausenaufsicht);
	}

	/**
	 * Liefert eine sortierte Liste aller {@link StundenplanPausenaufsicht}-Objekte.
	 * <br>Laufzeit: O(1)
	 *
	 * @return eine sortierte Liste aller {@link StundenplanPausenaufsicht}-Objekte.
	 */
	public @NotNull List<@NotNull StundenplanPausenaufsicht> pausenaufsichtGetMengeAsList() {
		return _pausenaufsichtmenge;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenaufsicht}-Objekte eines bestimmten Wochentages.
	 * <br>Laufzeit: O(1)
	 *
	 * @param wochentag  Die ID des ENUMS {@link Wochentag}.
	 *
	 * @return eine Liste aller {@link StundenplanPausenaufsicht}-Objekte eines bestimmten Wochentages.
	 */
	public @NotNull List<@NotNull StundenplanPausenaufsicht> pausenaufsichtGetMengeByWochentagOrEmptyList(final int wochentag) {
		return MapUtils.getOrCreateArrayList(_pausenaufsichtmenge_by_wochentag, wochentag);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenaufsicht}-Objekte einer bestimmten Pausenzeit.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idPausenzeit  Die Datenbank-ID der Pausenzeit.
	 *
	 * @return eine Liste aller {@link StundenplanPausenaufsicht}-Objekte einer bestimmten Pausenzeit.
	 */
	public @NotNull List<@NotNull StundenplanPausenaufsicht> pausenaufsichtGetMengeByPausenzeitId(final long idPausenzeit) {
		return MapUtils.getOrCreateArrayList(_pausenaufsichtmenge_by_idPausenzeit, idPausenzeit);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenaufsicht}-Objekte einer bestimmten Klasse zu einer bestimmten Pausenzeit.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idKlasse      Die Datenbank-ID der Klasse.
	 * @param idPausenzeit  Die Datenbank-ID der Pausenzeit.
	 * @param wochentyp     Der Wochentyp
	 * @param inklWoche0    falls TRUE, wird Unterricht des Wochentyps 0 hinzugefügt.
	 *
	 * @return eine Liste aller {@link StundenplanPausenaufsicht}-Objekte einer bestimmten Klasse zu einer bestimmten Pausenzeit.
	 */
	public @NotNull List<@NotNull StundenplanPausenaufsicht> pausenaufsichtGetMengeByKlasseIdAndPausenzeitIdAndWochentypAndInklusive(final long idKlasse, final long idPausenzeit, final int wochentyp, final boolean inklWoche0) {
		final @NotNull List<@NotNull StundenplanPausenaufsicht> list = new ArrayList<>();

		for (final @NotNull StundenplanPausenaufsicht a : Map2DUtils.getOrCreateArrayList(_pausenaufsichtmenge_by_idKlasse_and_idPausenzeit, idKlasse, idPausenzeit))
			if  ((a.wochentyp == wochentyp) || ((a.wochentyp == 0) && inklWoche0))
				list.add(a);

		return list;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenaufsicht}-Objekte eines bestimmten Lehrers zu einer bestimmten Pausenzeit.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idLehrer      Die Datenbank-ID des Lehrers.
	 * @param idPausenzeit  Die Datenbank-ID der Pausenzeit.
	 * @param wochentyp     Der Wochentyp
	 * @param inklWoche0    falls TRUE, wird Unterricht des Wochentyps 0 hinzugefügt.
	 *
	 * @return eine Liste aller {@link StundenplanPausenaufsicht}-Objekte eines bestimmten Lehrers zu einer bestimmten Pausenzeit.
	 */
	public @NotNull List<@NotNull StundenplanPausenaufsicht> pausenaufsichtGetMengeByLehrerIdAndPausenzeitIdAndWochentypAndInklusive(final long idLehrer, final long idPausenzeit, final int wochentyp, final boolean inklWoche0) {
		final @NotNull List<@NotNull StundenplanPausenaufsicht> list = new ArrayList<>();

		for (final @NotNull StundenplanPausenaufsicht a : Map2DUtils.getOrCreateArrayList(_pausenaufsichtmenge_by_idLehrer_and_idPausenzeit, idLehrer, idPausenzeit))
			if  ((a.wochentyp == wochentyp) || ((a.wochentyp == 0) && inklWoche0))
				list.add(a);

		return list;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenaufsicht}-Objekte eines bestimmten Schülers zu einer bestimmten Pausenzeit.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idSchueler    Die Datenbank-ID des Schülers.
	 * @param idPausenzeit  Die Datenbank-ID der Pausenzeit.
	 * @param wochentyp     Der Wochentyp
	 * @param inklWoche0    falls TRUE, wird Unterricht des Wochentyps 0 hinzugefügt.
	 *
	 * @return eine Liste aller {@link StundenplanPausenaufsicht}-Objekte eines bestimmten Schülers zu einer bestimmten Pausenzeit.
	 */
	public @NotNull List<@NotNull StundenplanPausenaufsicht> pausenaufsichtGetMengeBySchuelerIdAndPausenzeitIdAndWochentypAndInklusive(final long idSchueler, final long idPausenzeit, final int wochentyp, final boolean inklWoche0) {
		final @NotNull List<@NotNull StundenplanPausenaufsicht> list = new ArrayList<>();

		for (final @NotNull StundenplanPausenaufsicht a : Map2DUtils.getOrCreateArrayList(_pausenaufsichtmenge_by_idSchueler_and_idPausenzeit, idSchueler, idPausenzeit))
			if  ((a.wochentyp == wochentyp) || ((a.wochentyp == 0) && inklWoche0))
				list.add(a);

		return list;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenaufsicht}-Objekte eines bestimmten Jahrgangs zu einer bestimmten Pausenzeit.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idJahrgang    Die Datenbank-ID des Jahrgangs.
	 * @param idPausenzeit  Die Datenbank-ID der Pausenzeit.
	 * @param wochentyp     Der Wochentyp
	 * @param inklWoche0    falls TRUE, wird Unterricht des Wochentyps 0 hinzugefügt.
	 *
	 * @return eine Liste aller {@link StundenplanPausenaufsicht}-Objekte eines bestimmten Jahrgangs zu einer bestimmten Pausenzeit.
	 */
	public @NotNull List<@NotNull StundenplanPausenaufsicht> pausenaufsichtGetMengeByJahrgangIdAndPausenzeitIdAndWochentypAndInklusive(final long idJahrgang, final long idPausenzeit, final int wochentyp, final boolean inklWoche0) {
		final @NotNull List<@NotNull StundenplanPausenaufsicht> list = new ArrayList<>();

		for (final @NotNull StundenplanPausenaufsicht a : Map2DUtils.getOrCreateArrayList(_pausenaufsichtmenge_by_idJahrgang_and_idPausenzeit, idJahrgang, idPausenzeit))
			if  ((a.wochentyp == wochentyp) || ((a.wochentyp == 0) && inklWoche0))
				list.add(a);

		return list;
	}

	/**
	 * Aktualisiert das vorhandene {@link StundenplanPausenaufsicht}-Objekt durch das neue Objekt.
	 * <br>Die folgenden Attribute werden nicht aktualisiert:
	 * <br>{@link StundenplanPausenaufsicht#id}
	 * <br>
	 * <br>Die folgenden Attribute werden kopiert:
	 * <br>{@link StundenplanPausenaufsicht#idLehrer}
	 * <br>{@link StundenplanPausenaufsicht#idPausenzeit}
	 * <br>{@link StundenplanPausenaufsicht#wochentyp}
	 * <br>{@link StundenplanPausenaufsicht#bereiche}
	 *
	 * @param pausenaufsicht  Das neue {@link StundenplanPausenaufsicht}-Objekt, dessen Attribute kopiert werden.
	 */
	public void pausenaufsichtPatchAttributes(final @NotNull StundenplanPausenaufsicht pausenaufsicht) {
		// check
		pausenaufsichtCheckAttributes(pausenaufsicht);

		// replace
		DeveloperNotificationException.ifMapRemoveFailes(_pausenaufsicht_by_id, pausenaufsicht.id);
		DeveloperNotificationException.ifMapPutOverwrites(_pausenaufsicht_by_id, pausenaufsicht.id, pausenaufsicht);

		// update
		update_all();
	}

	private void pausenaufsichtRemoveOhneUpdateById(final long idPausenaufsicht) {
		DeveloperNotificationException.ifMapRemoveFailes(_pausenaufsicht_by_id, idPausenaufsicht);
	}

	/**
	 * Entfernt aus dem Stundenplan eine existierendes {@link StundenplanPausenaufsicht}-Objekt.
	 * <br>Laufzeit: O(|StundenplanPausenaufsicht|), da pausenaufsichtUpdate() aufgerufen wird.
	 *
	 * @param idPausenaufsicht  Die ID des {@link StundenplanPausenaufsicht}-Objekts.
	 */
	public void pausenaufsichtRemoveById(final long idPausenaufsicht) {
		pausenaufsichtRemoveOhneUpdateById(idPausenaufsicht);

		update_all();
	}

	// #####################################################################
	// #################### StundenplanPausenzeit ##########################
	// #####################################################################

	/**
	 * Fügt ein {@link StundenplanPausenzeit}-Objekt hinzu.
	 *
	 * @param pausenzeit  Das {@link StundenplanPausenzeit}-Objekt, welches hinzugefügt werden soll.
	 */
	public void pausenzeitAdd(final @NotNull StundenplanPausenzeit pausenzeit) {
		pausenzeitAddAll(ListUtils.create1(pausenzeit));
		update_all();
	}

	/**
	 * Fügt alle {@link StundenplanPausenzeit}-Objekte hinzu.
	 *
	 * @param listPausenzeit  Die Menge der {@link StundenplanPausenzeit}-Objekte, welche hinzugefügt werden soll.
	 */
	public void pausenzeitAddAll(final @NotNull List<@NotNull StundenplanPausenzeit> listPausenzeit) {
		pausenzeitAddAllOhneUpdate(listPausenzeit);
		update_all();
	}

	private void pausenzeitAddAllOhneUpdate(final @NotNull List<@NotNull StundenplanPausenzeit> list) {
		// check all
		final @NotNull HashSet<@NotNull Long> setOfIDs = new HashSet<>();
		for (final @NotNull StundenplanPausenzeit pausenzeit : list) {
			pausenzeitCheckAttributes(pausenzeit);
			DeveloperNotificationException.ifTrue("pausenzeitAddAllOhneUpdate: ID=" + pausenzeit.id + " existiert bereits!", _pausenzeit_by_id.containsKey(pausenzeit.id));
			DeveloperNotificationException.ifTrue("pausenzeitAddAllOhneUpdate: ID=" + pausenzeit.id + " doppelt in der Liste!", !setOfIDs.add(pausenzeit.id));
		}

		// add all
		for (final @NotNull StundenplanPausenzeit pausenzeit : list)
			DeveloperNotificationException.ifMapPutOverwrites(_pausenzeit_by_id, pausenzeit.id, pausenzeit);
	}

	private static void pausenzeitCheckAttributes(final @NotNull StundenplanPausenzeit pausenzeit) {
		DeveloperNotificationException.ifInvalidID("pause.id", pausenzeit.id);
		Wochentag.fromIDorException(pausenzeit.wochentag);
		if ((pausenzeit.beginn != null) && (pausenzeit.ende != null))
			DeveloperNotificationException.ifTrue("pausenzeit.beginn >= pausenzeit.ende", pausenzeit.beginn >= pausenzeit.ende);
	}

	/**
	 * Liefert TRUE, falls ein {@link StundenplanPausenzeit}-Objekt mit (Tag, Beginn, Ende) existiert.
	 * <br>Laufzeit: O(1)
	 *
	 * @param wochentag     Der {@link Wochentag}  des {@link StundenplanPausenzeit}-Objektes.
	 * @param beginnOrNull  Die Uhrzeit in Minuten seit 0 Uhr, wann die Pause beginnt. NULL bedeutet "noch nicht definiert".
	 * @param endeOrNull    Die Uhrzeit in Minuten seit 0 Uhr, wann die Pause endet. NULL bedeutet "noch nicht definiert".
	 *
	 * @return TRUE, falls ein {@link StundenplanPausenzeit}-Objekt mit (Tag, Beginn, Ende) existiert.
	 */
	public boolean pausenzeitExistsByWochentagAndBeginnAndEnde(final int wochentag, final Integer beginnOrNull, final Integer endeOrNull) {
		final long beginn = beginnOrNull == null ? -1 : beginnOrNull;
		final long ende = endeOrNull == null ? -1 : endeOrNull;
		final @NotNull LongArrayKey key = new LongArrayKey(new long[] {wochentag, beginn, ende});
		return _pausenzeit_by_tag_and_beginn_and_ende.containsKey(key);
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanPausenzeit}-Objekt.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idPausenzeit Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanPausenzeit}-Objekt.
	 */
	public @NotNull StundenplanPausenzeit pausenzeitGetByIdOrException(final long idPausenzeit) {
		return DeveloperNotificationException.ifMapGetIsNull(_pausenzeit_by_id, idPausenzeit);
	}

	/**
	 * Liefert die Beginn-Uhrzeit der {@link StundenplanPausenzeit} oder den leeren String, falls diese NULL ist.
	 * <br>Beispiel: "09:30" oder ""
	 * <br>Laufzeit: O(1)
	 *
	 * @param idPausenzeit  Die Datenbank-ID des {@link StundenplanPausenzeit}.
	 *
	 * @return die Beginn-Uhrzeit der {@link StundenplanPausenzeit} oder den leeren String, falls diese NULL ist.
	 */
	public @NotNull String pausenzeitGetByIdStringOfUhrzeitBeginn(final long idPausenzeit) {
		final @NotNull StundenplanPausenzeit pausenzeit =  DeveloperNotificationException.ifMapGetIsNull(_pausenzeit_by_id, idPausenzeit);
		return (pausenzeit.beginn == null) ? "" : DateUtils.getStringOfUhrzeitFromMinuten(pausenzeit.beginn);
	}

	/**
	 * Liefert die End-Uhrzeit der {@link StundenplanPausenzeit} oder den leeren String, falls diese NULL ist.
	 * <br>Beispiel: "10:15" oder ""
	 * <br>Laufzeit: O(1)
	 *
	 * @param idPausenzeit  Die Datenbank-ID des {@link StundenplanPausenzeit}.
	 *
	 * @return die End-Uhrzeit der {@link StundenplanPausenzeit} oder den leeren String, falls diese NULL ist.
	 */
	public @NotNull String pausenzeitGetByIdStringOfUhrzeitEnde(final long idPausenzeit) {
		final @NotNull StundenplanPausenzeit pausenzeit =  DeveloperNotificationException.ifMapGetIsNull(_pausenzeit_by_id, idPausenzeit);
		return (pausenzeit.ende == null) ? "" : DateUtils.getStringOfUhrzeitFromMinuten(pausenzeit.ende);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenzeit}-Objekte.
	 * <br>Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link StundenplanPausenzeit}-Objekte.
	 */
	public @NotNull List<@NotNull StundenplanPausenzeit> pausenzeitGetMengeAsList() {
		return _pausenzeitmenge_sortiert;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenzeit}-Objekte einer bestimmten Klasse.
	 * <br> Laufzeit: O(1)
	 *
	 * @param idKlasse  Die Datenbank-ID der Klasse.
	 *
	 * @return eine Liste aller {@link StundenplanPausenzeit}-Objekte einer bestimmten Klasse.
	 */
	public @NotNull List<@NotNull StundenplanPausenzeit> pausenzeitGetMengeByKlasseIdAsList(final long idKlasse) {
		return MapUtils.getOrCreateArrayList(_pausenzeitmenge_by_idKlasse, idKlasse);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenzeit}-Objekte einer bestimmten Lehrkraft.
	 * <br> Laufzeit: O(1)
	 *
	 * @param idLehrer  Die Datenbank-ID der Lehrkraft.
	 *
	 * @return eine Liste aller {@link StundenplanPausenzeit}-Objekte einer bestimmten Lehrkraft.
	 */
	public @NotNull List<@NotNull StundenplanPausenzeit> pausenzeitGetMengeByLehrerIdAsList(final long idLehrer) {
		return MapUtils.getOrCreateArrayList(_pausenzeitmenge_by_idLehrer, idLehrer);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenzeit}-Objekte eines bestimmten Schülers.
	 * <br> Laufzeit: O(1)
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return eine Liste aller {@link StundenplanPausenzeit}-Objekte eines bestimmten Schülers.
	 */
	public @NotNull List<@NotNull StundenplanPausenzeit> pausenzeitGetMengeBySchuelerIdAsList(final long idSchueler) {
		return MapUtils.getOrCreateArrayList(_pausenzeitmenge_by_idSchueler, idSchueler);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenzeit}-Objekte eines bestimmten Jahrgangs.
	 * <br> Laufzeit: O(1)
	 *
	 * @param idJahrgang  Die Datenbank-ID des Jahrgangs.
	 *
	 * @return eine Liste aller {@link StundenplanPausenzeit}-Objekte eines bestimmten Jahrgangs.
	 */
	public @NotNull List<@NotNull StundenplanPausenzeit> pausenzeitGetMengeByJahrgangIdAsList(final long idJahrgang) {
		return MapUtils.getOrCreateArrayList(_pausenzeitmenge_by_idJahrgang, idJahrgang);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenzeit}-Objekte eines bestimmten Wochentages, oder eine leere Liste.
	 * <br> Laufzeit: O(1)
	 *
	 * @param wochentag  Die ID des ENUMS {@link Wochentag}.
	 *
	 * @return eine Liste aller {@link StundenplanPausenzeit}-Objekte eines bestimmten Wochentages, oder eine leere Liste.
	 */
	public @NotNull List<@NotNull StundenplanPausenzeit> pausenzeitGetMengeByWochentagOrEmptyList(final int wochentag) {
		return MapUtils.getOrCreateArrayList(_pausenzeitmenge_by_wochentag, wochentag);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenzeit}-Objekte einer bestimmten Klasse zu einem bestimmten Wochentag.
	 * <br> Laufzeit: O(1)
	 *
	 * @param idKlasse   Die Datenbank-ID der Klasse.
     * @param wochentag  Die ID des ENUMS {@link Wochentag}.
   	 *
	 * @return eine Liste aller {@link StundenplanPausenzeit}-Objekte einer bestimmten Klasse zu einem bestimmten Wochentag.
	 */
	public @NotNull List<@NotNull StundenplanPausenzeit> pausenzeitGetMengeByKlasseIdAndWochentagAsList(final long idKlasse, final int wochentag) {
		return Map2DUtils.getOrCreateArrayList(_pausenzeitmenge_by_idKlasse_and_wochentag, idKlasse, wochentag);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenzeit}-Objekte einer bestimmten Lehrkraft.
	 * <br> Laufzeit: O(1)
	 *
	 * @param idLehrer   Die Datenbank-ID der Lehrkraft.
	 * @param wochentag  Die ID des ENUMS {@link Wochentag}.
	 *
	 * @return eine Liste aller {@link StundenplanPausenzeit}-Objekte einer bestimmten Lehrkraft.
	 */
	public @NotNull List<@NotNull StundenplanPausenzeit> pausenzeitGetMengeByLehrerIdAndWochentagAsList(final long idLehrer, final int wochentag) {
		return Map2DUtils.getOrCreateArrayList(_pausenzeitmenge_by_idLehrer_and_wochentag, idLehrer, wochentag);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenzeit}-Objekte eines bestimmten Schülers.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param wochentag   Die ID des ENUMS {@link Wochentag}.
	 *
	 * @return eine Liste aller {@link StundenplanPausenzeit}-Objekte eines bestimmten Schülers.
	 */
	public @NotNull List<@NotNull StundenplanPausenzeit> pausenzeitGetMengeBySchuelerIdAndWochentagAsList(final long idSchueler, final int wochentag) {
		return Map2DUtils.getOrCreateArrayList(_pausenzeitmenge_by_idSchueler_and_wochentag, idSchueler, wochentag);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenzeit}-Objekte eines bestimmten Jahrgangs.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idJahrgang  Die Datenbank-ID des Jahrgangs.
	 * @param wochentag   Die ID des ENUMS {@link Wochentag}.
	 *
	 * @return eine Liste aller {@link StundenplanPausenzeit}-Objekte eines bestimmten Jahrgangs.
	 */
	public @NotNull List<@NotNull StundenplanPausenzeit> pausenzeitGetMengeByJahrgangIdAndWochentagAsList(final long idJahrgang, final int wochentag) {
		return Map2DUtils.getOrCreateArrayList(_pausenzeitmenge_by_idJahrgang_and_wochentag, idJahrgang, wochentag);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenzeit}-Objekte, die mindestens eine {@link StundenplanPausenaufsicht} beinhalten.
	 * <br>Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link StundenplanPausenzeit}-Objekte, die mindestens eine {@link StundenplanPausenaufsicht} beinhalten.
	 */
	public @NotNull List<@NotNull StundenplanPausenzeit> pausenzeitGetMengeNichtLeereAsList() {
		return _pausenzeitmengeOhneLeere_sortiert;
	}

	/**
	 * Liefert das Minimum aller {@link StundenplanPausenzeit#beginn}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 * <br>Laufzeit: O(1)
	 *
	 * @return das Minimum aller {@link StundenplanPausenzeit#beginn}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 */
	public int pausenzeitGetMinutenMin() {
		return _pausenzeitMinutenMin == null ? 480 : _pausenzeitMinutenMin;
	}

	/**
	 * Liefert das Maximum aller {@link StundenplanPausenzeit#ende}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 * <br>Laufzeit: O(1)
	 *
	 * @return das Maximum aller {@link StundenplanPausenzeit#ende}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 */
	public int pausenzeitGetMinutenMax() {
		return _pausenzeitMinutenMax == null ? 480 : _pausenzeitMinutenMax;
	}

	/**
	 * Liefert die ID des kleinsten {@link Wochentag} oder den Montag falls es keine Pausenzeiten gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return die ID des kleinsten {@link Wochentag} oder den Montag falls es keine Pausenzeiten gibt.
	 */
	public int pausenzeitGetWochentagMin() {
		return _pausenzeitWochentagMin;
	}

	/**
	 * Liefert den kleinsten {@link Wochentag} oder den Montag falls es keine Pausenzeiten gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return den kleinsten {@link Wochentag} oder den Montag falls es keine Pausenzeiten gibt.
	 */
	public @NotNull Wochentag pausenzeitGetWochentagMinEnum() {
		return Wochentag.fromIDorException(_pausenzeitWochentagMin);
	}

	/**
	 * Liefert die ID des größten {@link Wochentag} oder den Montag falls es keine Pausenzeiten gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return die ID des größten {@link Wochentag} oder den Montag falls es keine Pausenzeiten gibt.
	 */
	public int pausenzeitGetWochentagMax() {
		return _pausenzeitWochentagMax;
	}

	/**
	 * Liefert den größten {@link Wochentag} oder den Montag falls es keine Pausenzeiten gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return den größten {@link Wochentag} oder den Montag falls es keine Pausenzeiten gibt.
	 */
	public @NotNull Wochentag pausenzeitGetWochentagMaxEnum() {
		return Wochentag.fromIDorException(_pausenzeitWochentagMax);
	}

	/**
	 * Liefert alle verwendeten sortierten {@link Wochentag}-Objekte der {@link StundenplanPausenzeit}.
 	 * Das Array beinhaltet alle {@link Wochentag}-Objekte von {@link #pausenzeitGetWochentagMin} bis {@link #pausenzeitGetWochentagMax()}.
	 * <br>Laufzeit: O(1), da Referenz auf ein Array.
	 *
	 * @return alle verwendeten sortierten {@link Wochentag}-Objekte der {@link StundenplanPausenzeit}.
	 */
	public @NotNull Wochentag @NotNull [] pausenzeitGetWochentageAlsEnumRange() {
		return _pausenzeitWochentageAlsEnumRange;
	}

	/**
	 * Liefert das Minimum aller {@link StundenplanPausenzeit#beginn}-Objekte und aller {@link StundenplanZeitraster#stundenbeginn}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 * <br>Laufzeit: O(1)
	 *
	 * @return das Minimum aller {@link StundenplanPausenzeit#beginn}-Objekte und aller {@link StundenplanZeitraster#stundenbeginn}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 */
	public int pausenzeitUndZeitrasterGetMinutenMin() {
		final Integer min = BlockungsUtils.minII(_pausenzeitMinutenMin, _zeitrasterMinutenMin);
		return min == null ? 480 : min;
	}

	/**
	 * Liefert das Minimum aller nicht leeren {@link StundenplanPausenzeit#beginn}-Objekte und aller {@link StundenplanZeitraster#stundenbeginn}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 * <br>Laufzeit: O(1)
	 *
	 * @return das Minimum aller nicht leeren {@link StundenplanPausenzeit#beginn}-Objekte und aller {@link StundenplanZeitraster#stundenbeginn}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 */
	public int pausenzeitUndZeitrasterGetMinutenMinOhneLeere() {
		final Integer min = BlockungsUtils.minII(_pausenzeitMinutenMinOhneLeere, _zeitrasterMinutenMinOhneLeere);
		return min == null ? 480 : min;
	}

	/**
	 * Liefert das Maximum aller {@link StundenplanPausenzeit#ende}-Objekte und aller {@link StundenplanZeitraster#stundenende}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 * <br>Laufzeit: O(1)
	 *
	 * @return das Maximum aller {@link StundenplanPausenzeit#ende}-Objekte und aller {@link StundenplanZeitraster#stundenende}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 */
	public int pausenzeitUndZeitrasterGetMinutenMax() {
		final Integer max = BlockungsUtils.maxII(_pausenzeitMinutenMax, _zeitrasterMinutenMax);
		return max == null ? 480 : max;
	}

	/**
	 * Liefert das Maximum aller nicht leeren {@link StundenplanPausenzeit#ende}-Objekte und aller {@link StundenplanZeitraster#stundenende}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 * <br>Laufzeit: O(1)
	 *
	 * @return das Maximum aller nicht leeren {@link StundenplanPausenzeit#ende}-Objekte und aller {@link StundenplanZeitraster#stundenende}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 */
	public int pausenzeitUndZeitrasterGetMinutenMaxOhneLeere() {
		final Integer max = BlockungsUtils.maxII(_pausenzeitMinutenMaxOhneLeere, _zeitrasterMinutenMaxOhneLeere);
		return max == null ? 480 : max;
	}

	/**
	 * Aktualisiert das vorhandene {@link StundenplanPausenzeit}-Objekt durch das neue Objekt.
	 * <br>Die folgenden Attribute werden nicht aktualisiert:
	 * <br>{@link StundenplanPausenzeit#id}
	 * <br>
	 * <br>Die folgenden Attribute werden kopiert:
	 * <br>{@link StundenplanPausenzeit#beginn}
	 * <br>{@link StundenplanPausenzeit#bezeichnung}
	 * <br>{@link StundenplanPausenzeit#ende}
	 * <br>{@link StundenplanPausenzeit#wochentag}
	 *
	 * @param pausenzeit  Das neue {@link StundenplanPausenzeit}-Objekt, dessen Attribute kopiert werden.
	 */
	public void pausenzeitPatchAttributes(final @NotNull StundenplanPausenzeit pausenzeit) {
		// check
		pausenzeitCheckAttributes(pausenzeit);

		// replace
		DeveloperNotificationException.ifMapRemoveFailes(_pausenzeit_by_id, pausenzeit.id);
		DeveloperNotificationException.ifMapPutOverwrites(_pausenzeit_by_id, pausenzeit.id, pausenzeit);

		// update
		update_all();
	}

	private void pausenzeitRemoveOhneUpdateById(final long idPausenzeit) {
		// Kaskade: StundenplanPausenaufsicht (der Pausenzeit)
		for (final @NotNull StundenplanPausenaufsicht a : MapUtils.getOrCreateArrayList(_pausenaufsichtmenge_by_idPausenzeit, idPausenzeit))
			pausenaufsichtRemoveOhneUpdateById(a.id);

		// Entfernen
		DeveloperNotificationException.ifMapRemoveFailes(_pausenzeit_by_id, idPausenzeit);
	}

	/**
	 * Entfernt aus dem Stundenplan eine existierendes {@link StundenplanPausenzeit}-Objekt.
	 *
	 * @param idPausenzeit  Die ID des {@link StundenplanPausenzeit}-Objekts.
	 */
	public void pausenzeitRemoveById(final long idPausenzeit) {
		pausenzeitRemoveOhneUpdateById(idPausenzeit);

		update_all();
	}

	/**
	 * Entfernt alle {@link StundenplanPausenzeit}-Objekte.
	 *
	 * @param listPausenzeit  Die Liste der zu entfernenden {@link StundenplanPausenzeit}-Objekte.
	 */
	public void pausenzeitRemoveAll(final @NotNull List<@NotNull StundenplanPausenzeit> listPausenzeit) {
		for (final @NotNull StundenplanPausenzeit pausenzeit : listPausenzeit)
			pausenzeitRemoveOhneUpdateById(pausenzeit.id);

		update_all();
	}

	// #####################################################################
	// #################### StundenplanRaum ################################
	// #####################################################################

	/**
	 * Fügt ein {@link StundenplanRaum}-Objekt hinzu.
	 *
	 * @param raum  Das {@link StundenplanRaum}-Objekt, welches hinzugefügt werden soll.
	 */
	public void raumAdd(final @NotNull StundenplanRaum raum) {
		raumAddAll(ListUtils.create1(raum));
	}

	/**
	 * Fügt alle {@link StundenplanRaum}-Objekte hinzu.
	 *
	 * @param listRaum  Die Menge der {@link StundenplanRaum}-Objekte, welche hinzugefügt werden soll.
	 */
	public void raumAddAll(final @NotNull List<@NotNull StundenplanRaum> listRaum) {
		raumAddAllOhneUpdate(listRaum);
		update_all();
	}

	private void raumAddAllOhneUpdate(final @NotNull List<@NotNull StundenplanRaum> list) {
		// check all
		final @NotNull HashSet<@NotNull Long> setOfIDs = new HashSet<>();
		for (final @NotNull StundenplanRaum raum : list) {
			raumCheckAttributes(raum);
			DeveloperNotificationException.ifTrue("raumAddAllOhneUpdate: ID=" + raum.id + " existiert bereits!", _raum_by_id.containsKey(raum.id));
			DeveloperNotificationException.ifTrue("raumAddAllOhneUpdate: ID=" + raum.id + " doppelt in der Liste!", !setOfIDs.add(raum.id));
		}

		// add all
		for (final @NotNull StundenplanRaum raum : list)
			DeveloperNotificationException.ifMapPutOverwrites(_raum_by_id, raum.id, raum);
	}

	private static void raumCheckAttributes(final @NotNull StundenplanRaum raum) {
		DeveloperNotificationException.ifInvalidID("raum.id", raum.id);
		DeveloperNotificationException.ifStringIsBlank("raum.kuerzel", raum.kuerzel);
		// raum.beschreibung darf "blank" sein!
		DeveloperNotificationException.ifTrue("raum.groesse < 0", raum.groesse < 0);
	}

	/**
	 * Liefert TRUE, falls ein {@link StundenplanRaum}-Objekt mit dem Kürzel existiert.
	 * <br>Laufzeit: O(1)
	 *
	 * @param kuerzel  Das Kürzel des {@link StundenplanRaum}-Objektes.
	 *
	 * @return TRUE, falls ein {@link StundenplanRaum}-Objekt mit dem Kürzel existiert.
	 */
	public boolean raumExistsByKuerzel(final @NotNull String kuerzel) {
		return _raum_by_kuerzel.containsKey(kuerzel);
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanRaum}-Objekt.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idRaum Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanRaum}-Objekt.
	 */
	public @NotNull StundenplanRaum raumGetByIdOrException(final long idRaum) {
		return DeveloperNotificationException.ifMapGetIsNull(_raum_by_id, idRaum);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanRaum}-Objekte.
	 * <br>Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link StundenplanRaum}-Objekte.
	 */
	public @NotNull List<@NotNull StundenplanRaum> raumGetMengeAsList() {
		return _raummenge_sortiert;
	}

	/**
	 * Aktualisiert das vorhandene {@link StundenplanRaum}-Objekt durch das neue Objekt.
	 * <br>Die folgenden Attribute werden nicht aktualisiert:
	 * <br>{@link StundenplanRaum#id}
	 * <br>
	 * <br>Die folgenden Attribute werden kopiert:
	 * <br>{@link StundenplanRaum#beschreibung}
	 * <br>{@link StundenplanRaum#groesse}
	 * <br>{@link StundenplanRaum#kuerzel}
	 *
	 * @param raum  Das neue {@link StundenplanRaum}-Objekt, dessen Attribute kopiert werden.
	 */
	public void raumPatchAttributes(final @NotNull StundenplanRaum raum) {
		// check
		raumCheckAttributes(raum);

		// replace
		DeveloperNotificationException.ifMapRemoveFailes(_raum_by_id, raum.id);
		DeveloperNotificationException.ifMapPutOverwrites(_raum_by_id, raum.id, raum);

		// update
		update_all();
	}

	private void raumRemoveOhneUpdateById(final long idRaum) {
		// Kaskade: StundenplanUnterricht
		for (final @NotNull StundenplanUnterricht u : MapUtils.getOrCreateArrayList(_unterrichtmenge_by_idRaum, idRaum))
			u.raeume.remove(idRaum);

		// Remove
		DeveloperNotificationException.ifMapRemoveFailes(_raum_by_id, idRaum);
	}

	/**
	 * Entfernt aus dem Stundenplan eine existierendes {@link StundenplanRaum}-Objekt.
	 *
	 * @param idRaum  Die ID des {@link StundenplanRaum}-Objekts.
	 */
	public void raumRemoveById(final long idRaum) {
		raumRemoveOhneUpdateById(idRaum);

		update_all();
	}

	/**
	 * Entfernt alle {@link StundenplanRaum}-Objekte.
	 *
	 * @param listRaum  Die Liste der zu entfernenden {@link StundenplanRaum}-Objekte.
	 */
	public void raumRemoveAll(final @NotNull List<@NotNull StundenplanRaum> listRaum) {
		// check
		final @NotNull HashSet<@NotNull Long> setOfIDs = new HashSet<>();
		for (final @NotNull StundenplanRaum raum : listRaum) {
			if (!_raum_by_id.containsKey(raum.id))
				throw new DeveloperNotificationException("raumRemoveAll: Raum-ID existiert nicht!");
			if (!setOfIDs.add(raum.id))
				throw new DeveloperNotificationException("raumRemoveAll: Doppelte Raum-ID in der Liste!");
		}

		for (final @NotNull StundenplanRaum raum : listRaum)
			raumRemoveOhneUpdateById(raum.id);

		update_all();
	}

	// #####################################################################
	// #################### StundenplanSchiene #############################
	// #####################################################################

	/**
	 * Fügt ein {@link StundenplanSchiene}-Objekt hinzu.
	 *
	 * @param schiene  Das {@link StundenplanSchiene}-Objekt, welches hinzugefügt werden soll.
	 */
	public void schieneAdd(final @NotNull StundenplanSchiene schiene) {
		schieneAddAll(ListUtils.create1(schiene));
	}

	/**
	 * Fügt alle {@link StundenplanSchiene}-Objekte hinzu.
	 *
	 * @param listSchiene  Die Menge der {@link StundenplanSchiene}-Objekte, welche hinzugefügt werden soll.
	 */
	public void schieneAddAll(final @NotNull List<@NotNull StundenplanSchiene> listSchiene) {
		schieneAddAllOhneUpdate(listSchiene);
		update_all();
	}

	private void schieneAddAllOhneUpdate(final @NotNull List<@NotNull StundenplanSchiene> list) {
		// check all
		final @NotNull HashSet<@NotNull Long> setOfIDs = new HashSet<>();
		for (final @NotNull StundenplanSchiene schiene : list) {
			schieneCheckAttributes(schiene);
			DeveloperNotificationException.ifTrue("schieneAddAllOhneUpdate: ID=" + schiene.id + " existiert bereits!", _schiene_by_id.containsKey(schiene.id));
			DeveloperNotificationException.ifTrue("schieneAddAllOhneUpdate: ID=" + schiene.id + " doppelt in der Liste!", !setOfIDs.add(schiene.id));
		}

		// add all
		for (final @NotNull StundenplanSchiene schiene : list)
			DeveloperNotificationException.ifMapPutOverwrites(_schiene_by_id, schiene.id, schiene);
	}

	private void schieneCheckAttributes(final @NotNull StundenplanSchiene schiene) {
		DeveloperNotificationException.ifInvalidID("schiene.id", schiene.id);
		DeveloperNotificationException.ifTrue("schiene.nummer <= 0", schiene.nummer <= 0);
		DeveloperNotificationException.ifStringIsBlank("schiene.bezeichnung", schiene.bezeichnung);
		DeveloperNotificationException.ifMapNotContains("_jahrgang_by_id", _jahrgang_by_id, schiene.idJahrgang);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanSchiene}-Objekte.
	 * <br>Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link StundenplanSchiene}-Objekte.
	 */
	public @NotNull List<@NotNull StundenplanSchiene> schieneGetMengeAsList() {
		return _schienenmenge;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanSchiene}-Objekte der Klasse am "wochentag, stunde, wochentyp".
	 * Falls der Parameter "inklWoche0" TRUE ist und der "wochentyp" größer als 0 ist, wird der Unterricht des Wochentyps 0 auch hinzugefügt.
	 *
	 * @param idKlasse      Die Datenbank-ID der Klasse.
	 * @param wochentag     Die ID des {@link Wochentag}-ENUM.
	 * @param stunde        Die Unterrichtsstunde.
	 * @param wochentyp     Der Wochentyp (0 jede Woche, 1 nur Woche A, 2 nur Woche B, ...)
	 * @param inklWoche0    falls TRUE, wird Unterricht des Wochentyps 0 hinzugefügt.
	 *
	 * @return eine Liste aller {@link StundenplanSchiene}-Objekten, der Klasse am "wochentag, stunde, wochentyp".
	 */
	public @NotNull List<@NotNull StundenplanSchiene> schieneGetMengeByKlasseIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(final long idKlasse, final int wochentag, final int stunde, final int wochentyp, final boolean inklWoche0) {
		final @NotNull List<@NotNull StundenplanSchiene> list = new ArrayList<>();

		for (final @NotNull StundenplanUnterricht u : unterrichtGetMengeByKlasseIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(idKlasse, wochentag, stunde, wochentyp, inklWoche0))
			ListUtils.addAllIfNotExists(list, MapUtils.getOrCreateArrayList(_schienenmenge_by_idUnterricht, u.id));

		list.sort(_compSchiene);
		return list;
	}

	/**
	 * Liefert eine sortierte Liste aller {@link StundenplanSchiene}-Objekte der Klasse.
	 * <br>Hinweis: Es handelt sich um die Schienen aller {@link StundenplanKurs} und aller {@link StundenplanKlassenunterricht}- Objekte.
	 *
	 * @param idKlasse  Die Datenbank-ID der Klasse.
	 *
	 * @return eine sortierte Liste aller {@link StundenplanSchiene}-Objekte der Klasse.
	 */
	public @NotNull List<@NotNull StundenplanSchiene> schieneGetMengeByKlasseId(final long idKlasse) {
		return MapUtils.getOrCreateArrayList(_schienenmenge_by_idKlasse, idKlasse);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanSchiene}-Objekte des Lehrers am "wochentag, stunde, wochentyp".
	 * Falls der Parameter "inklWoche0" TRUE ist und der "wochentyp" größer als 0 ist, wird der Unterricht des Wochentyps 0 auch hinzugefügt.
	 *
	 * @param idLehrer      Die Datenbank-ID des Lehrers.
	 * @param wochentag     Die ID des {@link Wochentag}-ENUM.
	 * @param stunde        Die Unterrichtsstunde.
	 * @param wochentyp     Der Wochentyp (0 jede Woche, 1 nur Woche A, 2 nur Woche B, ...)
	 * @param inklWoche0    falls TRUE, wird Unterricht des Wochentyps 0 hinzugefügt.
	 *
	 * @return eine Liste aller {@link StundenplanSchiene}-Objekten, des Lehrers am "wochentag, stunde, wochentyp".
	 */
	public @NotNull List<@NotNull StundenplanSchiene> schieneGetMengeByLehrerIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(final long idLehrer, final int wochentag, final int stunde, final int wochentyp, final boolean inklWoche0) {
		final @NotNull List<@NotNull StundenplanSchiene> list = new ArrayList<>();
		for (final @NotNull StundenplanUnterricht u : unterrichtGetMengeByLehrerIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(idLehrer, wochentag, stunde, wochentyp, inklWoche0))
			ListUtils.addAllIfNotExists(list, MapUtils.getOrCreateArrayList(_schienenmenge_by_idUnterricht, u.id));
		list.sort(_compSchiene);
		return list;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanSchiene}-Objekte des Schülers am "wochentag, stunde, wochentyp".
	 * Falls der Parameter "inklWoche0" TRUE ist und der "wochentyp" größer als 0 ist, wird der Unterricht des Wochentyps 0 auch hinzugefügt.
	 *
	 * @param idSchueler    Die Datenbank-ID des Schülers.
	 * @param wochentag     Die ID des {@link Wochentag}-ENUM.
	 * @param stunde        Die Unterrichtsstunde.
	 * @param wochentyp     Der Wochentyp (0 jede Woche, 1 nur Woche A, 2 nur Woche B, ...)
	 * @param inklWoche0    falls TRUE, wird Unterricht des Wochentyps 0 hinzugefügt.
	 *
	 * @return eine Liste aller {@link StundenplanSchiene}-Objekten, des Schülers am "wochentag, stunde, wochentyp".
	 */
	public @NotNull List<@NotNull StundenplanSchiene> schieneGetMengeBySchuelerIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(final long idSchueler, final int wochentag, final int stunde, final int wochentyp, final boolean inklWoche0) {
		final @NotNull List<@NotNull StundenplanSchiene> list = new ArrayList<>();
		for (final @NotNull StundenplanUnterricht u : unterrichtGetMengeBySchuelerIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(idSchueler, wochentag, stunde, wochentyp, inklWoche0))
			ListUtils.addAllIfNotExists(list, MapUtils.getOrCreateArrayList(_schienenmenge_by_idUnterricht, u.id));
		list.sort(_compSchiene);
		return list;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanSchiene}-Objekte des Jahrgangs am "wochentag, stunde, wochentyp".
	 * Falls der Parameter "inklWoche0" TRUE ist und der "wochentyp" größer als 0 ist, wird der Unterricht des Wochentyps 0 auch hinzugefügt.
	 *
	 * @param idJahrgang    Die Datenbank-ID des Jahrgangs.
	 * @param wochentag     Die ID des {@link Wochentag}-ENUM.
	 * @param stunde        Die Unterrichtsstunde.
	 * @param wochentyp     Der Wochentyp (0 jede Woche, 1 nur Woche A, 2 nur Woche B, ...)
	 * @param inklWoche0    falls TRUE, wird Unterricht des Wochentyps 0 hinzugefügt.
	 *
	 * @return eine Liste aller {@link StundenplanSchiene}-Objekten, des Jahrgangs am "wochentag, stunde, wochentyp".
	 */
	public @NotNull List<@NotNull StundenplanSchiene> schieneGetMengeByJahrgangIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(final long idJahrgang, final int wochentag, final int stunde, final int wochentyp, final boolean inklWoche0) {
		final @NotNull List<@NotNull StundenplanSchiene> list = new ArrayList<>();
		for (final @NotNull StundenplanUnterricht u : unterrichtGetMengeByJahrgangIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(idJahrgang, wochentag, stunde, wochentyp, inklWoche0))
			ListUtils.addAllIfNotExists(list, MapUtils.getOrCreateArrayList(_schienenmenge_by_idUnterricht, u.id));
		list.sort(_compSchiene);
		return list;
	}

	private void schieneRemoveOhneUpdateById(final long idSchiene) {
		// Kaskade: StundenplanKurs
		for (final @NotNull StundenplanKurs kurs : MapUtils.getOrCreateArrayList(_kursmenge_by_idSchiene, idSchiene))
			kurs.schienen.remove(idSchiene);

		// Kaskade: StundenplanKlassenunterricht
		for (final @NotNull StundenplanKlassenunterricht ku : MapUtils.getOrCreateArrayList(_klassenunterrichtmenge_by_idSchiene, idSchiene))
			ku.schienen.remove(idSchiene);

		// Kaskade: StundenplanUnterricht
		for (final @NotNull StundenplanUnterricht u : MapUtils.getOrCreateArrayList(_unterrichtmenge_by_idSchiene, idSchiene))
			u.schienen.remove(idSchiene);

		DeveloperNotificationException.ifMapRemoveFailes(_schiene_by_id, idSchiene);
	}

	// #####################################################################
	// #################### StundenplanSchueler ############################
	// #####################################################################

	/**
	 * Fügt ein {@link StundenplanSchueler}-Objekt hinzu.
	 *
	 * @param schueler  Das {@link StundenplanSchueler}-Objekt, welches hinzugefügt werden soll.
	 */
	public void schuelerAdd(final @NotNull StundenplanSchueler schueler) {
		schuelerAddAll(ListUtils.create1(schueler));
	}


	/**
	 * Fügt alle {@link StundenplanSchueler}-Objekte hinzu.
	 *
	 * @param listSchueler  Die Menge der {@link StundenplanSchueler}-Objekte, welche hinzugefügt werden soll.
	 */
	public void schuelerAddAll(final @NotNull List<@NotNull StundenplanSchueler> listSchueler) {
		schuelerAddAllOhneUpdate(listSchueler);
		update_all();
	}

	private void schuelerAddAllOhneUpdate(final @NotNull List<@NotNull StundenplanSchueler> list) {
		// check all
		final @NotNull HashSet<@NotNull Long> setOfIDs = new HashSet<>();
		for (final @NotNull StundenplanSchueler schueler : list) {
			schuelerCheckAttributes(schueler);
			DeveloperNotificationException.ifTrue("schuelerAddAllOhneUpdate: ID=" + schueler.id + " existiert bereits!", _schueler_by_id.containsKey(schueler.id));
			DeveloperNotificationException.ifTrue("schuelerAddAllOhneUpdate: ID=" + schueler.id + " doppelt in der Liste!", !setOfIDs.add(schueler.id));
		}

		// add all
		for (final @NotNull StundenplanSchueler schueler : list)
			DeveloperNotificationException.ifMapPutOverwrites(_schueler_by_id, schueler.id, schueler);
	}

	private static void schuelerCheckAttributes(final @NotNull StundenplanSchueler schueler) {
		DeveloperNotificationException.ifInvalidID("schueler.id", schueler.id);
		DeveloperNotificationException.ifStringIsBlank("schueler.nachname", schueler.nachname);
		DeveloperNotificationException.ifStringIsBlank("schueler.vorname", schueler.vorname);
		// schueler.idKlasse nicht nötig, ein Schüler kann auch keine Klasse haben. Die Zuordnung erfolgt über StundenplanKlasse.
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanSchueler}-Objekt.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idSchueler Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanSchueler}-Objekt.
	 */
	public @NotNull StundenplanSchueler schuelerGetByIdOrException(final long idSchueler) {
		return DeveloperNotificationException.ifMapGetIsNull(_schueler_by_id, idSchueler);
	}

	/**
	 * Liefert die Datenbank-ID des Schülers.<br>
	 * Wirft eine Exception, falls in den Daten nicht genau ein Schüler geladen wurde.
	 *
	 * @return  Die Datenbank-ID des Schülers.
	 */
	public long schuelerGetIDorException() {
		final int size = _schuelermenge.size();
		DeveloperNotificationException.ifTrue("getSchuelerID() geht nicht bei " + size + " Schülern!", size != 1);
		return _schuelermenge.get(0).id;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanSchueler}-Objekte, sortiert nach {@link #_compSchueler}.
	 * <br>Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link StundenplanSchueler}-Objekte, sortiert nach {@link #_compSchueler}.
	 */
	public @NotNull List<@NotNull StundenplanSchueler> schuelerGetMengeAsList() {
		return _schuelermenge;
	}

	/**
	 * Liefert alle {@link StundenplanSchueler}-Objekte der Klasse.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idKlasse  Die Datenbank-ID der Klasse.
	 *
	 * @return alle {@link StundenplanSchueler}-Objekte der Klasse.
	 * @throws DeveloperNotificationException falls die Klasse nicht existiert.
	 */
	public @NotNull List<@NotNull StundenplanSchueler> schuelerGetMengeByKlasseIdAsListOrException(final long idKlasse) throws DeveloperNotificationException {
		final @NotNull StundenplanKlasse klasse = DeveloperNotificationException.ifMapGetIsNull(_klasse_by_id, idKlasse);
		return MapUtils.getOrCreateArrayList(_schuelermenge_by_idKlasse, klasse.id);
	}

	/**
	 * Liefert die Anzahl der {@link StundenplanSchueler}-Objekte der Klasse.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idKlasse  Die Datenbank-ID der Klasse.
	 *
	 * @return die Anzahl der {@link StundenplanSchueler}-Objekte der Klasse.
	 * @throws DeveloperNotificationException falls die Klasse nicht existiert.
	 */
	public int schuelerGetAnzahlByKlasseIdOrException(final long idKlasse) throws DeveloperNotificationException {
		final @NotNull StundenplanKlasse klasse = DeveloperNotificationException.ifMapGetIsNull(_klasse_by_id, idKlasse);
		return MapUtils.getOrCreateArrayList(_schuelermenge_by_idKlasse, klasse.id).size();
	}

	/**
	 * Liefert alle {@link StundenplanSchueler}-Objekte des Kurses.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return alle {@link StundenplanSchueler}-Objekte des Kurses.
	 * @throws DeveloperNotificationException falls der Kurs nicht existiert.
	 */
	public @NotNull List<@NotNull StundenplanSchueler> schuelerGetMengeByKursIdAsListOrException(final long idKurs) throws DeveloperNotificationException {
		return MapUtils.getOrCreateArrayList(_schuelermenge_by_idKurs, idKurs);
	}

	/**
	 * Liefert die Anzahl der  {@link StundenplanSchueler}-Objekte des Kurses.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl der  {@link StundenplanSchueler}-Objekte des Kurses.
	 * @throws DeveloperNotificationException falls der Kurs nicht existiert.
	 */
	public int schuelerGetAnzahlByKursIdAsListOrException(final long idKurs) throws DeveloperNotificationException {
		return MapUtils.getOrCreateArrayList(_schuelermenge_by_idKurs, idKurs).size();
	}

	/**
	 * Entfernt den Schülers, auch kaskadierend aus {@link StundenplanKlasse}, {@link StundenplanKurs}, {@link StundenplanKlassenunterricht}.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 */
	private void schuelerRemoveOhneUpdateById(final long idSchueler) {
		// Kaskade: StundenplanKlasse
		// Folgende zwei Zeilen nicht mehr nötig, sobald aus DTO entfernt.
		for (final @NotNull StundenplanKlasse klasse : MapUtils.getOrCreateArrayList(_klassenmenge_by_idSchueler, idSchueler))
			klasse.schueler.remove(idSchueler);

		// Kaskade: StundenplanKurs
		for (final @NotNull StundenplanKurs kurs : MapUtils.getOrCreateArrayList(_kursmenge_by_idSchueler, idSchueler))
			kurs.schueler.remove(idSchueler);

		// Kaskade: StundenplanKlassenunterricht
		for (final @NotNull StundenplanKlassenunterricht ku: MapUtils.getOrCreateArrayList(_klassenunterrichtmenge_by_idSchueler, idSchueler))
			ku.schueler.remove(idSchueler);

		// Remove
		DeveloperNotificationException.ifMapRemoveFailes(_schiene_by_id, idSchueler);
	}

	/**
	 * Entfernt aus dem Stundenplan eine existierendes {@link StundenplanSchueler}-Objekt.
	 * <br>Hinweis: Entfernt kaskadierend auch aus {@link StundenplanKlasse}, {@link StundenplanKurs} und {@link StundenplanKlassenunterricht}.
	 *
	 * @param idSchueler  Die ID des {@link StundenplanSchueler}-Objekts.
	 */
	public void schuelerRemoveById(final long idSchueler) {
		schuelerRemoveOhneUpdateById(idSchueler);

		update_all();
	}

	// #####################################################################
	// #################### Stundenplan ####################################
	// #####################################################################

	/**
	 * Liefert die ID des Schuljahresabschnitts des Stundenplans.
	 *
	 * @return die ID des Schuljahresabschnitts des Stundenplans.
	 */
	public long getIDSchuljahresabschnitt() {
		return _stundenplanSchuljahresAbschnittID;
	}

	/**
	 * Liefert das Datum, ab dem der Stundenplan gültig ist.
	 *
	 * @return das Datum, ab dem der Stundenplan gültig ist.
	 */
	public @NotNull String getGueltigAb() {
		return _stundenplanGueltigAb;
	}

	/**
	 * Liefert das Datum, bis wann der Stundenplan gültig ist.
	 *
	 * @return das Datum, bis wann der Stundenplan gültig ist.
	 */
	public @NotNull String getGueltigBis() {
		return _stundenplanGueltigBis;
	}

	/**
	 * Liefert die textuelle Beschreibung des Stundenplans.
	 *
	 * @return die textuelle Beschreibung des Stundenplans.
	 */
	public @NotNull String getBezeichnungStundenplan() {
		return _stundenplanBezeichnung;
	}

	/**
	 * Liefert das (globale) Wochentyp-Modell für die Wochen des Stundenplans. <br>
	 * 0: Stundenplan gilt jede Woche. <br>
	 * 1: Ungültiger Wert. <br>
	 * N: Stundenplan wiederholt sich alle N Wochen. <br>
	 * <br>Laufzeit: O(1)
	 *
	 * @return das (globale) Wochentyp-Modell für die Wochen des Stundenplans.
	 */
	public int getWochenTypModell() {
		return _stundenplanWochenTypModell;
	}

	/**
	 * Liefert das (globale) Wochentyp-Modell für die Wochen des Stundenplans. <br>
	 * 0: Stundenplan gilt jede Woche. <br>
	 * 1: Ungültiger Wert. <br>
	 * N: Stundenplan wiederholt sich alle N Wochen. <br>
	 * <br>Laufzeit: O(1)
	 *
	 * @return das (globale) Wochentyp-Modell für die Wochen des Stundenplans.
	 */
	public int stundenplanGetWochenTypModell() {
		return _stundenplanWochenTypModell;
	}

	/**
	 * Liefert zum übergebenen Wochentyp einen passenden String.
	 * <br>Beispiel: 0 -> "Alle Wochen", 1 -> "A-Woche", ...
	 * <br>Laufzeit: O(1)
	 *
	 * @param wochenTyp  Der umzuwandelnde Wochentyp.
	 *
	 * @return zum übergebenen Wochentyp einen passenden String.
	 */
	public @NotNull String stundenplanGetWochenTypAsString(final int wochenTyp) {
		if (wochenTyp <= 0)
			return "Alle Wochen";

		final int zahl = wochenTyp - 1; // 0-basierter Wochentyp
		final int z2 = zahl / 26;       // 2. Stelle im 26. System
		final int z1 = zahl - z2 * 26;  // 1. Stelle im 26. System

		return StringUtils.numberToLetterIndex1(z2) + StringUtils.numberToLetterIndex0(z1) + "-Woche";
	}

	/**
	 * Liefert die Datenbank-ID des Stundenplans.
	 * <br>Laufzeit: O(1)
	 *
	 * @return die Datenbank-ID des Stundenplans.
	 */
	public long stundenplanGetID() {
		return _stundenplanID;
	}

	// #####################################################################
	// #################### StundenplanUnterricht ##########################
	// #####################################################################

	/**
	 * Fügt ein {@link StundenplanUnterricht}-Objekt hinzu.
	 *
	 * @param unterricht  Das {@link StundenplanUnterricht}-Objekt, welches hinzugefügt werden soll.
	 */
	public void unterrichtAdd(final @NotNull StundenplanUnterricht unterricht) {
		unterrichtAddAll(ListUtils.create1(unterricht));
	}

	/**
	 * Fügt alle {@link StundenplanUnterricht}-Objekte hinzu.
	 *
	 * @param listUnterricht  Die Menge der {@link StundenplanUnterricht}-Objekte, welche hinzugefügt werden soll.
	 */
	public void unterrichtAddAll(final @NotNull List<@NotNull StundenplanUnterricht> listUnterricht) {
		unterrichtAddAllOhneUpdate(listUnterricht);
		update_all();
	}

	private void unterrichtAddAllOhneUpdate(final @NotNull List<@NotNull StundenplanUnterricht> list) {
		// check all
		final @NotNull HashSet<@NotNull Long> setOfIDs = new HashSet<>();
		for (final @NotNull StundenplanUnterricht u : list) {
			unterrichtCheckAttributes(u);
			DeveloperNotificationException.ifTrue("unterrichtAddAllOhneUpdate: ID=" + u.id + " existiert bereits!", _unterricht_by_id.containsKey(u.id));
			DeveloperNotificationException.ifTrue("unterrichtAddAllOhneUpdate: ID=" + u.id + " doppelt in der Liste!", !setOfIDs.add(u.id));
		}

		// check duplicates (tag, stunde)
		unterrichtCheckDuplicateInCell(list);

		// add all
		for (final @NotNull StundenplanUnterricht u : list)
			DeveloperNotificationException.ifMapPutOverwrites(_unterricht_by_id, u.id, u);
	}

	private void unterrichtCheckAttributes(final @NotNull StundenplanUnterricht u) {
		DeveloperNotificationException.ifInvalidID("u.id", u.id);
		DeveloperNotificationException.ifMapNotContains("_zeitraster_by_id", _zeitraster_by_id, u.idZeitraster);
		DeveloperNotificationException.ifTrue("u.wochentyp > stundenplanWochenTypModell", u.wochentyp > _stundenplanWochenTypModell);
		DeveloperNotificationException.ifTrue("u.wochentyp < 0", u.wochentyp < 0); // 0 ist erlaubt!
		DeveloperNotificationException.ifTrue("(u.idKurs == null) && (u.klassen.size() != 1)", (u.idKurs == null) && (u.klassen.size() != 1)); // WENN Klassenunterricht, DANN genau 1 Klasse.

		DeveloperNotificationException.ifMapNotContains("_fach_by_id", _fach_by_id, u.idFach);
		for (final @NotNull Long idLehrkraftDesUnterrichts : u.lehrer)
			DeveloperNotificationException.ifMapNotContains("_lehrer_by_id", _lehrer_by_id, idLehrkraftDesUnterrichts);
		for (final @NotNull Long idKlasseDesUnterrichts : u.klassen)
			DeveloperNotificationException.ifMapNotContains("_klasse_by_id", _klasse_by_id, idKlasseDesUnterrichts);
		for (final @NotNull Long idRaumDesUnterrichts : u.raeume)
			DeveloperNotificationException.ifMapNotContains("_raum_by_id", _raum_by_id, idRaumDesUnterrichts);
		for (final @NotNull Long idSchieneDesUnterrichts : u.schienen)
			DeveloperNotificationException.ifMapNotContains("_schiene_by_id", _schiene_by_id, idSchieneDesUnterrichts);
	}

	// TODO unterrichtCheckDuplicateInCell
	private void unterrichtCheckDuplicateInCell(@SuppressWarnings("unused") final @NotNull List<@NotNull StundenplanUnterricht> list) {
		// Simuliere die neue Lage für Kursunterricht (idKurs, tag, stunde) -->
		// final @NotNull HashSet<@NotNull String> _menge_by_idKurs = new HashSet<>()
	}

	private @NotNull Comparator<@NotNull StundenplanUnterricht> unterrichtCreateComparator() {
		final @NotNull Comparator<@NotNull StundenplanUnterricht> comp = (final @NotNull StundenplanUnterricht a, final @NotNull StundenplanUnterricht b) -> {
			// Sortierung nach Jahrgang
			final StundenplanJahrgang aJahrgang = jahrgangGetMinimumByKlassenIDs(a.klassen);
			final StundenplanJahrgang bJahrgang = jahrgangGetMinimumByKlassenIDs(b.klassen);
			if ((aJahrgang != null) || (bJahrgang != null)) {
				if (aJahrgang == null) return -1;
				if (bJahrgang == null) return +1;
				final int cmpJahrgang = _compJahrgang.compare(aJahrgang, bJahrgang);
				if (cmpJahrgang != 0) return cmpJahrgang;
			}

			// Sortierung nach Kurs
			if ((a.idKurs != null) && (b.idKurs == null)) return -1;
			if ((a.idKurs == null) && (b.idKurs != null)) return +1;

			// Sortierung nach Klassen-Listen
			final int cmpKlasse = klasseCompareByKlassenIDs(a.klassen, b.klassen);
			if (cmpKlasse != 0) return cmpKlasse;

			// Sortierung nach Fach
			final @NotNull StundenplanFach aFach = DeveloperNotificationException.ifMapGetIsNull(_fach_by_id, a.idFach);
			final @NotNull StundenplanFach bFach = DeveloperNotificationException.ifMapGetIsNull(_fach_by_id, b.idFach);
			final int cmpFach = _compFach.compare(aFach, bFach);
			if (cmpFach != 0) return cmpFach;

			// Sortierung nach Lehrkraft-Listen
			final int cmpLehrer = lehrerCompareByLehrerIDs(a.lehrer, b.lehrer);
			if (cmpLehrer != 0) return cmpLehrer;

			// Sortierung nach Wochentyp
			if (a.wochentyp < b.wochentyp) return -1;
			if (a.wochentyp > b.wochentyp) return +1;

			// Sortierung nach ID
			return Long.compare(a.id, b.id);
		};

		return comp;
	}

	/**
	 * Liefert das {@link StundenplanUnterricht}-Objekt zur übergebenen ID.
	 * <br>Laufzeit: O(1)
	 * <br>Hinweis: Unnötige Methode, denn man bekommt die Objekte über Zeitraster-Abfragen.
	 *
	 * @param idUnterricht  Die Datenbank-ID des Unterrichts.
	 *
	 * @return das {@link StundenplanUnterricht}-Objekt zur übergebenen ID.
	 */
	public @NotNull StundenplanUnterricht unterrichtGetByIdOrException(final long idUnterricht) {
		return DeveloperNotificationException.ifMapGetIsNull(_unterricht_by_id, idUnterricht);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekte einer Klasse mit einem bestimmten Wochentyp.
	 *
	 * @param idKlasse   Die Datenbank-ID der Klasse.
	 * @param wochentyp  Der gewünschten Wochentyp. Der Wert 0 ist nur dann erlaubt, wenn wochenTypModell ebenfalls 0 ist.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekte einer Klasse mit einem bestimmten Wochentyp.
	 */
	public @NotNull List<@NotNull StundenplanUnterricht> unterrichtGetMengeByKlasseIdAndWochentyp(final long idKlasse, final int wochentyp) {
		// Überprüfen
		DeveloperNotificationException.ifTrue("wochentyp > stundenplanWochenTypModell", wochentyp > _stundenplanWochenTypModell);
		// Filtern
		final @NotNull List<@NotNull StundenplanUnterricht> listU = MapUtils.getOrCreateArrayList(_unterrichtmenge_by_idKlasse, idKlasse);
		return CollectionUtils.toFilteredArrayList(listU, (final @NotNull StundenplanUnterricht u) -> (u.wochentyp == 0) || (u.wochentyp == wochentyp));
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekte einer Klasse in einer bestimmten Kalenderwoche.
	 *
	 * @param idKlasse       Die Datenbank-ID der Klasse.
	 * @param jahr           Das Jahr der Kalenderwoche (muss zwischen 2000 und 3000 liegen).
	 * @param kalenderwoche  Die gewünschten Kalenderwoche (muss zwischen 1 und 53 liegen).
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekte einer Klasse in einer bestimmten Kalenderwoche.
	 */
	public @NotNull List<@NotNull StundenplanUnterricht> unterrichtGetMengeByKlasseIdAndJahrAndKW(final long idKlasse, final int jahr, final int kalenderwoche) {
		final int wochentyp = kalenderwochenzuordnungGetWochentypOrDefault(jahr, kalenderwoche);
		return unterrichtGetMengeByKlasseIdAndWochentyp(idKlasse, wochentyp);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekte eines Klassenunterrichts (Klasse, Fach) mit einem bestimmten Wochentyp.
	 *
	 * @param idKlasse   Die Datenbank-ID der Klasse.
	 * @param idFach     Die Datenbank-ID des Faches.
	 * @param wochentyp  Der gewünschten Wochentyp. Der Wert 0 ist nur dann erlaubt, wenn wochenTypModell ebenfalls 0 ist.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekte eines Klassenunterrichts (Klasse, Fach) mit einem bestimmten Wochentyp.
	 */
	public @NotNull List<@NotNull StundenplanUnterricht> unterrichtGetMengeByKlasseIdAndFachIdAndWochentyp(final long idKlasse, final long idFach, final int wochentyp) {
		// Überprüfen
		DeveloperNotificationException.ifTrue("wochentyp > stundenplanWochenTypModell", wochentyp > _stundenplanWochenTypModell);

		// Filtern
		final @NotNull List<@NotNull StundenplanUnterricht> listU = DeveloperNotificationException.ifMap2DGetIsNull(_unterrichtmenge_by_idKlasse_and_idFach, idKlasse, idFach);
		return CollectionUtils.toFilteredArrayList(listU, (final @NotNull StundenplanUnterricht u) -> (u.wochentyp == 0) || (u.wochentyp == wochentyp));
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekte eines Klassenunterrichts (Klasse, Fach) in einer bestimmten Kalenderwoche.
	 *
	 * @param idKlasse       Die Datenbank-ID der Klasse.
	 * @param idFach         Die Datenbank-ID des Faches.
	 * @param jahr           Das Jahr der Kalenderwoche (muss zwischen 2000 und 3000 liegen).
	 * @param kalenderwoche  Die gewünschten Kalenderwoche (muss zwischen 1 und 53 liegen).
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekte eines Klassenunterrichts (Klasse, Fach) in einer bestimmten Kalenderwoche.
	 */
	public @NotNull List<@NotNull StundenplanUnterricht> unterrichtGetMengeByKlasseIdAndFachIdAndJahrAndKW(final long idKlasse, final long idFach, final int jahr, final int kalenderwoche) {
		final int wochentyp = kalenderwochenzuordnungGetWochentypOrDefault(jahr, kalenderwoche);
		return unterrichtGetMengeByKlasseIdAndFachIdAndWochentyp(idKlasse, idFach, wochentyp);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht} eines Kurses mit einem bestimmten Wochentyp.
	 *
	 * @param idkurs     Die ID des Kurses.
	 * @param wochentyp  Der gewünschten Wochentyp. Der Wert 0 ist nur dann erlaubt, wenn wochenTypModell ebenfalls 0 ist.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht} eines Kurses in einer bestimmten Kalenderwoche.
	 */
	public @NotNull List<@NotNull StundenplanUnterricht> unterrichtGetMengeByKursIdAndWochentyp(final long idkurs, final int wochentyp) {
		// Überprüfen
		DeveloperNotificationException.ifTrue("wochentyp > stundenplanWochenTypModell", wochentyp > _stundenplanWochenTypModell);

		// Filtern
		final @NotNull List<@NotNull StundenplanUnterricht> listU = MapUtils.getOrCreateArrayList(_unterrichtmenge_by_idKurs, idkurs);
		return CollectionUtils.toFilteredArrayList(listU, (final @NotNull StundenplanUnterricht u) -> (u.wochentyp == 0) || (u.wochentyp == wochentyp));
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht} einer Kursmenge mit einem bestimmten Wochentyp.
	 *
	 * @param idsKurs   Die IDs aller Kurse.
	 * @param wochentyp Der gewünschten Wochentyp. Der Wert 0 ist nur dann erlaubt, wenn wochenTypModell ebenfalls 0 ist.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht} einer Kursmenge mit einem bestimmten Wochentyp.
	 */
	public @NotNull List<@NotNull StundenplanUnterricht> unterrichtGetMengeByKursIdsAndWochentyp(final @NotNull long[] idsKurs, final int wochentyp) {
		// Daten filtern.
		final @NotNull ArrayList<@NotNull StundenplanUnterricht> result = new ArrayList<>();
		for (final long idKurs : idsKurs)
			result.addAll(unterrichtGetMengeByKursIdAndWochentyp(idKurs, wochentyp));
		return result;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht} eines Kurses in einer bestimmten Kalenderwoche.
	 *
	 * @param idKurs        Die ID des Kurses.
	 * @param jahr          Das Jahr der Kalenderwoche (muss zwischen 2000 und 3000 liegen).
	 * @param kalenderwoche Die gewünschten Kalenderwoche (muss zwischen 1 und 53 liegen).
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht} eines Kurses in einer bestimmten Kalenderwoche.
	 */
	public @NotNull List<@NotNull StundenplanUnterricht> unterrichtGetMengeByKursIdAndJahrAndKW(final long idKurs, final int jahr, final int kalenderwoche) {
		final int wochentyp = kalenderwochenzuordnungGetWochentypOrDefault(jahr, kalenderwoche);
		return unterrichtGetMengeByKursIdAndWochentyp(idKurs, wochentyp);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht} einer Kursmenge in einer bestimmten Kalenderwoche.
	 *
	 * @param idsKurs       Die IDs aller Kurse.
	 * @param jahr          Das Jahr der Kalenderwoche (muss zwischen 2000 und 3000 liegen).
	 * @param kalenderwoche Die gewünschten Kalenderwoche (muss zwischen 1 und 53 liegen).
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht} einer Kursmenge in einer bestimmten Kalenderwoche.
	 */
	public @NotNull List<@NotNull StundenplanUnterricht> unterrichtGetMengeByKursIdsAndJahrAndKW(final @NotNull long[] idsKurs, final int jahr, final int kalenderwoche) {
		final int wochentyp = kalenderwochenzuordnungGetWochentypOrDefault(jahr, kalenderwoche);
		return unterrichtGetMengeByKursIdsAndWochentyp(idsKurs, wochentyp);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekt, die im übergeben Zeitraster und Wochentyp liegen.
	 *
	 * @param idZeitraster  Die Datenbank-ID des Zeitrasters.
	 * @param wochentyp     Der Wochentyp (0 jede Woche, 1 nur Woche A, 2 nur Woche B, ...)
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekt, die im übergeben Zeitraster und Wochentyp liegen.
	 */
	public @NotNull List<@NotNull StundenplanUnterricht> unterrichtGetMengeByZeitrasterIdAndWochentypOrEmptyList(final long idZeitraster, final int wochentyp) {
		return Map2DUtils.getOrCreateArrayList(_unterrichtmenge_by_idZeitraster_and_wochentyp, idZeitraster, wochentyp);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekten, die im übergebenen Zeitraster und Wochentyp liegen.
	 * Falls der Parameter inklWoche0 TRUE ist, wird Unterricht des Wochentyps 0 hinzugefügt.
	 *
	 * @param idZeitraster  Die Datenbank-ID des Zeitrasters.
	 * @param wochentyp     Der Wochentyp
	 * @param inklWoche0    falls TRUE, wird Unterricht des Wochentyps 0 hinzugefügt.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekten, die im übergebenen Zeitraster und Wochentyp liegen.
	 */
	public @NotNull List<@NotNull StundenplanUnterricht> unterrichtGetMengeByZeitrasterIdAndWochentypAndInklusiveOrEmptyList(final long idZeitraster, final int wochentyp, final boolean inklWoche0) {
		if ((wochentyp == 0) || (!inklWoche0))
			return unterrichtGetMengeByZeitrasterIdAndWochentypOrEmptyList(idZeitraster, wochentyp);

		final @NotNull List<@NotNull StundenplanUnterricht> list = new ArrayList<>();
		list.addAll(unterrichtGetMengeByZeitrasterIdAndWochentypOrEmptyList(idZeitraster, wochentyp));
		list.addAll(unterrichtGetMengeByZeitrasterIdAndWochentypOrEmptyList(idZeitraster, 0));
		return list;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekt, die im übergeben Zeitraster und Wochentyp liegen.
	 *
	 * @param wochentag  Der {@link Wochentag}-ENUM.
	 * @param stunde     Die Unterrichtsstunde.
	 * @param wochentyp  Der Wochentyp (0 jede Woche, 1 nur Woche A, 2 nur Woche B, ...)
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekt, die im übergeben Zeitraster und Wochentyp liegen.
	 */
	public @NotNull List<@NotNull StundenplanUnterricht> unterrichtGetMengeByWochentagAndStundeAndWochentypOrEmptyList(final @NotNull Wochentag wochentag, final int stunde, final int wochentyp) {
		final StundenplanZeitraster zeitraster = _zeitraster_by_wochentag_and_stunde.getOrNull(wochentag.id, stunde);
		if (zeitraster != null)
			return Map2DUtils.getOrCreateArrayList(_unterrichtmenge_by_idZeitraster_and_wochentyp, zeitraster.id, wochentyp);
		return new ArrayList<>();
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekten, die in der Stundenplanzelle "wochentag, stunde" und "wochentyp" liegen.
	 * Falls der Parameter inklWoche0 TRUE ist, wird Unterricht des Wochentyps 0 hinzugefügt.
	 *
	 * @param wochentag     Der {@link Wochentag}-ENUM.
	 * @param stunde        Die Unterrichtsstunde.
	 * @param wochentyp     Der Wochentyp (0 jede Woche, 1 nur Woche A, 2 nur Woche B, ...)
	 * @param inklWoche0    falls TRUE, wird Unterricht des Wochentyps 0 hinzugefügt.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekten, die in der Stundenplanzelle "wochentag, stunde" und "wochentyp" liegen.
	 */
	public @NotNull List<@NotNull StundenplanUnterricht> unterrichtGetMengeByWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(final @NotNull Wochentag wochentag, final int stunde, final int wochentyp, final boolean inklWoche0) {
		final long idZeitraster = zeitrasterGetByWochentagAndStundeOrException(wochentag.id, stunde).id;
		return unterrichtGetMengeByZeitrasterIdAndWochentypAndInklusiveOrEmptyList(idZeitraster, wochentyp, inklWoche0);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekte der Klasse am "wochentag, stunde, wochentyp".
	 * Falls der Parameter "inklWoche0" TRUE ist und der "wochentyp" größer als 0 ist, wird der Unterricht des Wochentyps 0 auch hinzugefügt.
	 *
	 * @param idKlasse      Die Datenbank-ID der Klasse.
	 * @param wochentag     Die ID des {@link Wochentag}-ENUM.
	 * @param stunde        Die Unterrichtsstunde.
	 * @param wochentyp     Der Wochentyp (0 jede Woche, 1 nur Woche A, 2 nur Woche B, ...)
	 * @param inklWoche0    falls TRUE, wird Unterricht des Wochentyps 0 hinzugefügt.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekten, der Klasse am "wochentag, stunde, wochentyp".
	 */
	public @NotNull List<@NotNull StundenplanUnterricht> unterrichtGetMengeByKlasseIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(final long idKlasse, final int wochentag, final int stunde, final int wochentyp, final boolean inklWoche0) {
		final @NotNull List<@NotNull StundenplanUnterricht> list = new ArrayList<>();

		final StundenplanZeitraster z = _zeitraster_by_wochentag_and_stunde.getOrNull(wochentag, stunde);
		if (z != null)
			for (final @NotNull StundenplanUnterricht u : Map2DUtils.getOrCreateArrayList(_unterrichtmenge_by_idKlasse_and_idZeitraster, idKlasse, z.id))
				if  ((u.wochentyp == wochentyp) || ((u.wochentyp == 0) && inklWoche0))
					list.add(u);

		return list;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekte der Klasse am "wochentag, stunde, wochentyp" einer bestimmten Schiene.
	 * Falls der Parameter "inklWoche0" TRUE ist und der "wochentyp" größer als 0 ist, wird der Unterricht des Wochentyps 0 auch hinzugefügt.
	 *
	 * @param idKlasse      Die Datenbank-ID der Klasse.
	 * @param wochentag     Die ID des {@link Wochentag}-ENUM.
	 * @param stunde        Die Unterrichtsstunde.
	 * @param wochentyp     Der Wochentyp (0 jede Woche, 1 nur Woche A, 2 nur Woche B, ...)
	 * @param idSchiene     Die Datenbank-ID der Schiene, oder -1, falls Unterricht ohne Schiene gemeint ist.
	 * @param inklWoche0    falls TRUE, wird Unterricht des Wochentyps 0 hinzugefügt.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekten, der Klasse am "wochentag, stunde, wochentyp".
	 */
	public @NotNull List<@NotNull StundenplanUnterricht> unterrichtGetMengeByKlasseIdAndWochentagAndStundeAndWochentypAndSchieneAndInklusiveOrEmptyList(final long idKlasse, final int wochentag, final int stunde, final int wochentyp, final long idSchiene, final boolean inklWoche0) {
		final @NotNull List<@NotNull StundenplanUnterricht> list = new ArrayList<>();

		for (final @NotNull StundenplanUnterricht u : unterrichtGetMengeByKlasseIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(idKlasse, wochentag, stunde, wochentyp, inklWoche0))
			if (unterrichtHatSchiene(u, idSchiene))
				list.add(u);

		return list;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekte der Lehrkraft am "wochentag, stunde, wochentyp".
	 * Falls der Parameter "inklWoche0" TRUE ist und der "wochentyp" größer als 0 ist, wird der Unterricht des Wochentyps 0 auch hinzugefügt.
	 *
	 * @param idLehrer      Die Datenbank-ID der Lehrkraft.
	 * @param wochentag     Die ID des {@link Wochentag}-ENUM.
	 * @param stunde        Die Unterrichtsstunde.
	 * @param wochentyp     Der Wochentyp (0 jede Woche, 1 nur Woche A, 2 nur Woche B, ...)
	 * @param inklWoche0    falls TRUE, wird Unterricht des Wochentyps 0 hinzugefügt.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekte der Lehrkraft am "wochentag, stunde, wochentyp".
	 */
	public @NotNull List<@NotNull StundenplanUnterricht> unterrichtGetMengeByLehrerIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(final long idLehrer, final int wochentag, final int stunde, final int wochentyp, final boolean inklWoche0) {
		final @NotNull List<@NotNull StundenplanUnterricht> list = new ArrayList<>();

		final StundenplanZeitraster z = _zeitraster_by_wochentag_and_stunde.getOrNull(wochentag, stunde);
		if (z != null)
			for (final @NotNull StundenplanUnterricht u : Map2DUtils.getOrCreateArrayList(_unterrichtmenge_by_idLehrer_and_idZeitraster, idLehrer, z.id))
				if  ((u.wochentyp == wochentyp) || ((u.wochentyp == 0) && inklWoche0))
					list.add(u);

		return list;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekte der Lehrkraft am "wochentag, stunde, wochentyp" einer bestimmten Schiene.
	 * Falls der Parameter "inklWoche0" TRUE ist und der "wochentyp" größer als 0 ist, wird der Unterricht des Wochentyps 0 auch hinzugefügt.
	 *
	 * @param idLehrer      Die Datenbank-ID der Lehrkraft.
	 * @param wochentag     Die ID des {@link Wochentag}-ENUM.
	 * @param stunde        Die Unterrichtsstunde.
	 * @param wochentyp     Der Wochentyp (0 jede Woche, 1 nur Woche A, 2 nur Woche B, ...)
	 * @param idSchiene     Die Datenbank-ID der Schiene, oder -1, falls Unterricht ohne Schiene gemeint ist.
	 * @param inklWoche0    falls TRUE, wird Unterricht des Wochentyps 0 hinzugefügt.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekte der Lehrkraft am "wochentag, stunde, wochentyp".
	 */
	public @NotNull List<@NotNull StundenplanUnterricht> unterrichtGetMengeByLehrerIdAndWochentagAndStundeAndWochentypAndSchieneAndInklusiveOrEmptyList(final long idLehrer, final int wochentag, final int stunde, final int wochentyp, final long idSchiene, final boolean inklWoche0) {
		final @NotNull List<@NotNull StundenplanUnterricht> list = new ArrayList<>();

		for (final @NotNull StundenplanUnterricht u : unterrichtGetMengeByLehrerIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(idLehrer, wochentag, stunde, wochentyp, inklWoche0))
			if (unterrichtHatSchiene(u, idSchiene))
				list.add(u);

		return list;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekte des Schülers am "wochentag, stunde, wochentyp".
	 * Falls der Parameter "inklWoche0" TRUE ist und der "wochentyp" größer als 0 ist, wird der Unterricht des Wochentyps 0 auch hinzugefügt.
	 *
	 * @param idSchueler    Die Datenbank-ID des Schülers.
	 * @param wochentag     Die ID des {@link Wochentag}-ENUM.
	 * @param stunde        Die Unterrichtsstunde.
	 * @param wochentyp     Der Wochentyp (0 jede Woche, 1 nur Woche A, 2 nur Woche B, ...)
	 * @param inklWoche0    falls TRUE, wird Unterricht des Wochentyps 0 hinzugefügt.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekte des Schülers am "wochentag, stunde, wochentyp".
	 */
	public @NotNull List<@NotNull StundenplanUnterricht> unterrichtGetMengeBySchuelerIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(final long idSchueler, final int wochentag, final int stunde, final int wochentyp, final boolean inklWoche0) {
		final @NotNull List<@NotNull StundenplanUnterricht> list = new ArrayList<>();

		final StundenplanZeitraster z = _zeitraster_by_wochentag_and_stunde.getOrNull(wochentag, stunde);
		if (z != null)
			for (final @NotNull StundenplanUnterricht u : Map2DUtils.getOrCreateArrayList(_unterrichtmenge_by_idSchueler_and_idZeitraster, idSchueler, z.id))
				if  ((u.wochentyp == wochentyp) || ((u.wochentyp == 0) && inklWoche0))
					list.add(u);

		return list;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekte des Schülers am "wochentag, stunde, wochentyp" einer bestimmten Schiene.
	 * Falls der Parameter "inklWoche0" TRUE ist und der "wochentyp" größer als 0 ist, wird der Unterricht des Wochentyps 0 auch hinzugefügt.
	 *
	 * @param idSchueler    Die Datenbank-ID des Schülers.
	 * @param wochentag     Die ID des {@link Wochentag}-ENUM.
	 * @param stunde        Die Unterrichtsstunde.
	 * @param wochentyp     Der Wochentyp (0 jede Woche, 1 nur Woche A, 2 nur Woche B, ...)
	 * @param idSchiene     Die Datenbank-ID der Schiene, oder -1, falls Unterricht ohne Schiene gemeint ist.
	 * @param inklWoche0    falls TRUE, wird Unterricht des Wochentyps 0 hinzugefügt.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekte des Schülers am "wochentag, stunde, wochentyp".
	 */
	public @NotNull List<@NotNull StundenplanUnterricht> unterrichtGetMengeBySchuelerIdAndWochentagAndStundeAndWochentypAndSchieneAndInklusiveOrEmptyList(final long idSchueler, final int wochentag, final int stunde, final int wochentyp, final long idSchiene, final boolean inklWoche0) {
		final @NotNull List<@NotNull StundenplanUnterricht> list = new ArrayList<>();

		for (final @NotNull StundenplanUnterricht u : unterrichtGetMengeBySchuelerIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(idSchueler, wochentag, stunde, wochentyp, inklWoche0))
			if (unterrichtHatSchiene(u, idSchiene))
				list.add(u);

		return list;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekte des Jahrgangs am "wochentag, stunde, wochentyp".
	 * Falls der Parameter "inklWoche0" TRUE ist und der "wochentyp" größer als 0 ist, wird der Unterricht des Wochentyps 0 auch hinzugefügt.
	 *
	 * @param idJahrgang    Die Datenbank-ID des Jahrgangs.
	 * @param wochentag     Die ID des {@link Wochentag}-ENUM.
	 * @param stunde        Die Unterrichtsstunde.
	 * @param wochentyp     Der Wochentyp (0 jede Woche, 1 nur Woche A, 2 nur Woche B, ...)
	 * @param inklWoche0    falls TRUE, wird Unterricht des Wochentyps 0 hinzugefügt.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekte des Jahrgangs am "wochentag, stunde, wochentyp".
	 */
	public @NotNull List<@NotNull StundenplanUnterricht> unterrichtGetMengeByJahrgangIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(final long idJahrgang, final int wochentag, final int stunde, final int wochentyp, final boolean inklWoche0) {
		final @NotNull List<@NotNull StundenplanUnterricht> list = new ArrayList<>();

		final StundenplanZeitraster z = _zeitraster_by_wochentag_and_stunde.getOrNull(wochentag, stunde);
		if (z != null)
			for (final @NotNull StundenplanUnterricht u : Map2DUtils.getOrCreateArrayList(_unterrichtmenge_by_idJahrgang_and_idZeitraster, idJahrgang, z.id))
				if  ((u.wochentyp == wochentyp) || ((u.wochentyp == 0) && inklWoche0))
					list.add(u);

		list.sort(_compUnterricht);
		return list;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekte des Jahrgangs am "wochentag, stunde, wochentyp" einer bestimmten Schiene.
	 * Falls der Parameter "inklWoche0" TRUE ist und der "wochentyp" größer als 0 ist, wird der Unterricht des Wochentyps 0 auch hinzugefügt.
	 *
	 * @param idJahrgang    Die Datenbank-ID des Jahrgangs.
	 * @param wochentag     Die ID des {@link Wochentag}-ENUM.
	 * @param stunde        Die Unterrichtsstunde.
	 * @param wochentyp     Der Wochentyp (0 jede Woche, 1 nur Woche A, 2 nur Woche B, ...)
	 * @param idSchiene     Die Datenbank-ID der Schiene, oder -1, falls Unterricht ohne Schiene gemeint ist.
	 * @param inklWoche0    falls TRUE, wird Unterricht des Wochentyps 0 hinzugefügt.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekte des Jahrgangs am "wochentag, stunde, wochentyp".
	 */
	public @NotNull List<@NotNull StundenplanUnterricht> unterrichtGetMengeByJahrgangIdAndWochentagAndStundeAndWochentypAndSchieneAndInklusiveOrEmptyList(final long idJahrgang, final int wochentag, final int stunde, final int wochentyp, final long idSchiene, final boolean inklWoche0) {
		final @NotNull List<@NotNull StundenplanUnterricht> list = new ArrayList<>();

		for (final @NotNull StundenplanUnterricht u : unterrichtGetMengeByJahrgangIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(idJahrgang, wochentag, stunde, wochentyp, inklWoche0))
			if (unterrichtHatSchiene(u, idSchiene))
				list.add(u);

		return list;
	}

	/**
	 * Liefert eine String-Repräsentation des das Fach- oder Kurs-Kürzel eines {@link StundenplanUnterricht}.
	 * <br>Beispiel: "M-LK1-Suffix" bei Kursen und "M" Fachkürzel bei Klassenunterricht.
	 * <br>Laufzeit: O(1)
	 * @param idUnterricht  Die Datenbank-ID des {@link StundenplanUnterricht}.
	 *
	 * @return eine String-Repräsentation des das Fach- oder Kurs-Kürzel eines {@link StundenplanUnterricht}.
	 */
	public @NotNull String unterrichtGetByIDStringOfFachOderKursKuerzel(final long idUnterricht) {
		final @NotNull StundenplanUnterricht unterricht =  DeveloperNotificationException.ifMapGetIsNull(_unterricht_by_id, idUnterricht);

		// Klassenunterricht?
		if (unterricht.idKurs == null) {
			final @NotNull StundenplanFach fach =  DeveloperNotificationException.ifMapGetIsNull(_fach_by_id, unterricht.idFach);
			return fach.kuerzel;
		}

		// Kursunterricht
		final @NotNull StundenplanKurs kurs =  DeveloperNotificationException.ifMapGetIsNull(_kurs_by_id, unterricht.idKurs);
		return kurs.bezeichnung;
	}

	/**
	 * Liefert eine String-Repräsentation der Klassenmenge des {@link StundenplanUnterricht}.
	 * <br>Beispiel: "5a" bei einer Klasse und "7a,7b" bei mehreren (z.B. Französisch...)
	 * <br>Laufzeit: O(1)
	 * @param idUnterricht  Die Datenbank-ID des {@link StundenplanUnterricht}.
	 *
	 * @return eine String-Repräsentation der Klassenmenge des {@link StundenplanUnterricht}.
	 */
	public @NotNull String unterrichtGetByIDStringOfKlassen(final long idUnterricht) {
		final @NotNull StundenplanUnterricht unterricht =  DeveloperNotificationException.ifMapGetIsNull(_unterricht_by_id, idUnterricht);

		// Klassenkürzel sammeln und sortieren.
		final @NotNull AVLSet<@NotNull String> kuerzel = new AVLSet<>();

		for (final @NotNull Long idKlasse : unterricht.klassen) {
			final @NotNull StundenplanKlasse klasse =  DeveloperNotificationException.ifMapGetIsNull(_klasse_by_id, idKlasse);
			kuerzel.add(klasse.kuerzel);
		}

		return StringUtils.collectionToCommaSeparatedString(kuerzel);
	}

	/**
	 * Liefert eine String-Repräsentation der Raummenge des {@link StundenplanUnterricht}.
	 * <br>Beispiel: "1.01" bei einem Raum und "T1, T2" bei mehreren (z.B. Sporthallen...)
	 * <br>Laufzeit: O(1)
	 * @param idUnterricht  Die Datenbank-ID des {@link StundenplanUnterricht}.
	 *
	 * @return eine String-Repräsentation der Raummenge des {@link StundenplanUnterricht}.
	 */
	public @NotNull String unterrichtGetByIDStringOfRaeume(final long idUnterricht) {
		final @NotNull StundenplanUnterricht unterricht =  DeveloperNotificationException.ifMapGetIsNull(_unterricht_by_id, idUnterricht);

		// Klassenkürzel sammeln und sortieren.
		final @NotNull AVLSet<@NotNull String> kuerzel = new AVLSet<>();
		for (final @NotNull Long idRaum : unterricht.raeume) {
			final @NotNull StundenplanRaum raum =  DeveloperNotificationException.ifMapGetIsNull(_raum_by_id, idRaum);
			kuerzel.add(raum.kuerzel);
		}

		return StringUtils.collectionToCommaSeparatedString(kuerzel);
	}

	/**
	 * Liefert eine String-Repräsentation der Schienenmenge des {@link StundenplanUnterricht}.
	 * <br>Beispiel: "EFB01" bei einem Raum und "EFB01, Q1B07"
	 * <br>Laufzeit: O(1)
	 * @param idUnterricht  Die Datenbank-ID des {@link StundenplanUnterricht}.
	 *
	 * @return eine String-Repräsentation der Schienenmenge des {@link StundenplanUnterricht}.
	 */
	public @NotNull String unterrichtGetByIDStringOfSchienen(final long idUnterricht) {
		final @NotNull StundenplanUnterricht unterricht =  DeveloperNotificationException.ifMapGetIsNull(_unterricht_by_id, idUnterricht);

		// Klassenkürzel sammeln und sortieren.
		final @NotNull AVLSet<@NotNull String> kuerzel = new AVLSet<>();
		for (final @NotNull Long idSchiene : unterricht.schienen) {
			final @NotNull StundenplanSchiene schiene =  DeveloperNotificationException.ifMapGetIsNull(_schiene_by_id, idSchiene);
			kuerzel.add(schiene.bezeichnung);
		}

		return StringUtils.collectionToCommaSeparatedString(kuerzel);
	}

	/**
	 * Liefert einen String aller Daten des Unterrichts (für Debug-Zwecke).
	 *
	 * @param idUnterricht  Die Datenbank-ID des Unterrichts.
	 *
	 * @return einen String aller Daten des Unterrichts (für Debug-Zwecke).
	 */
	public @NotNull String unterrichtGetByIDStringOfAll(final long idUnterricht) {
		String sLe = unterrichtGetByIDLehrerFirstAsStringOrEmpty(idUnterricht);
		String sFa = unterrichtGetByIDStringOfFachOderKursKuerzel(idUnterricht);
		String sKl = unterrichtGetByIDStringOfKlassen(idUnterricht);
		String sRa = unterrichtGetByIDStringOfRaeume(idUnterricht);
		String sSc = unterrichtGetByIDStringOfSchienen(idUnterricht);
		sLe = sLe.isEmpty() ? "" : ", " + sLe;
		sFa = sFa.isEmpty() ? "" : ", " + sFa;
		sKl = sKl.isEmpty() ? "" : ", " + sKl;
		sRa = sRa.isEmpty() ? "" : ", " + sRa;
		sSc = sSc.isEmpty() ? "" : ", " + sSc;
		return idUnterricht + sLe + sFa + sKl + sRa + sSc;
	}

	/**
	 * Liefert die Menge aller {@link StundenplanLehrer}-Objekte des {@link StundenplanUnterricht}.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idUnterricht  Die Datenbank-ID des {@link StundenplanUnterricht}.
	 *
	 * @return die Menge aller {@link StundenplanLehrer}-Objekte des {@link StundenplanUnterricht}.
	 */
	public @NotNull List<@NotNull StundenplanLehrer> unterrichtGetByIDLehrerMenge(final long idUnterricht) {
		return MapUtils.getOrCreateArrayList(_lehrermenge_by_idUnterricht, idUnterricht);
	}

	/**
	 * Liefert die Menge aller {@link StundenplanLehrer} des {@link StundenplanUnterricht} als kommaseparierter String.
	 * <br>Laufzeit: O(|Ergebnis|)
	 *
	 * @param idUnterricht  Die Datenbank-ID des {@link StundenplanUnterricht}.
	 *
	 * @return die Menge aller {@link StundenplanLehrer} des {@link StundenplanUnterricht} als kommaseparierter String.
	 */
	public @NotNull String unterrichtGetByIDLehrerMengeAsString(final long idUnterricht) {
		final @NotNull List<@NotNull StundenplanLehrer> lehrkraefteDesUnterrichts = MapUtils.getOrCreateArrayList(_lehrermenge_by_idUnterricht, idUnterricht);

		final @NotNull List<@NotNull String> listeDerKuerzel = new ArrayList<>();
		for (final @NotNull StundenplanLehrer lehkraft : lehrkraefteDesUnterrichts)
			listeDerKuerzel.add(lehkraft.kuerzel);

		return StringUtils.collectionToCommaSeparatedString(listeDerKuerzel);
	}

	/**
	 * Liefert die erste {@link StundenplanLehrer} des {@link StundenplanUnterricht} oder NULL falls nicht existent.
	 * <br>Laufzeit: O(|Ergebnis|)
	 *
	 * @param idUnterricht  Die Datenbank-ID des {@link StundenplanUnterricht}.
	 *
	 * @return eine String-Repräsentation der Raummenge des {@link StundenplanUnterricht}.
	 */
	public StundenplanLehrer unterrichtGetByIDLehrerFirstOrNull(final long idUnterricht) {
		final @NotNull List<@NotNull StundenplanLehrer> lehrerDesUnterrichts = MapUtils.getOrCreateArrayList(_lehrermenge_by_idUnterricht, idUnterricht);
		return lehrerDesUnterrichts.isEmpty() ? null : DeveloperNotificationException.ifListGetFirstFailes("lehrerDesUnterrichts.first", lehrerDesUnterrichts);
	}

	/**
	 * Liefert die erste {@link StundenplanLehrer} des {@link StundenplanUnterricht} oder NULL falls nicht existent.
	 * <br>Laufzeit: O(|Ergebnis|)
	 *
	 * @param idUnterricht  Die Datenbank-ID des {@link StundenplanUnterricht}.
	 *
	 * @return eine String-Repräsentation der Raummenge des {@link StundenplanUnterricht}.
	 */
	public @NotNull String unterrichtGetByIDLehrerFirstAsStringOrEmpty(final long idUnterricht) {
		final StundenplanLehrer lehrkraft = unterrichtGetByIDLehrerFirstOrNull(idUnterricht);
		return lehrkraft == null ? "" : lehrkraft.kuerzel;
	}

	/**
	 * Liefert TRUE, falls es {@link StundenplanUnterricht} gibt, der einen Wochentyp > 0 hat.
	 * <br>Laufzeit: O(1)
	 *
	 * @return TRUE, falls es {@link StundenplanUnterricht} gibt, der einen Wochentyp > 0 hat.
	 */
	public boolean unterrichtHatMultiWochen() {
		return _unterrichtHatMultiWochen;
	}

	/**
	 * Liefert TRUE, falls der Unterricht in der übergebenen Schiene liegt, oder falls er in keiner Schiene liegt und idSchiene negativ ist.
	 *
	 * @param u          Der Unterricht der durchsucht wird.
	 * @param idSchiene  Die Datenbank-ID der Schiene nach der gesucht wird.
	 *
	 * @return TRUE, falls der Unterricht in der übergebenen Schiene liegt, oder falls er in keiner Schiene liegt und idSchiene negativ ist.
	 */
	public boolean unterrichtHatSchiene(final @NotNull StundenplanUnterricht u, final long idSchiene) {
		final @NotNull List<@NotNull StundenplanSchiene> schienen = MapUtils.getOrCreateArrayList(_schienenmenge_by_idUnterricht, u.id);

		// Unterricht ohne Schienen gesucht?
		if (idSchiene < 0)
			return schienen.isEmpty();

		// Unterricht einer bestimmten Schiene gesucht.
		for (final @NotNull StundenplanSchiene schiene : schienen)
			if (schiene.id == idSchiene)
				return true;

		return false;
	}

	/**
	 * Liefert TRUE, falls ein Unterricht in ein bestimmtes Zeitraster verschoben werden darf.
	 *
	 * @param u  Der {@link StundenplanUnterricht}, welcher verschoben werden soll.
	 * @param z  Das {@link StundenplanZeitraster}, wohin verschoben werden soll.
	 *
	 * @return TRUE, falls ein Unterricht in ein bestimmtes Zeitraster verschoben werden darf.
	 */
	public boolean unterrichtIstVerschiebenErlaubt(final @NotNull StundenplanUnterricht u, final @NotNull StundenplanZeitraster z) {
		for (final @NotNull StundenplanUnterricht partner : DeveloperNotificationException.ifMapGetIsNull(_unterrichtmenge_by_idUnterricht, u.id))
			if ((partner.idZeitraster == z.id) && ((partner.wochentyp == 0) || (u.wochentyp == 0) || (u.wochentyp == partner.wochentyp)))
				return false;
		return true;
	}

	/**
	 * Aktualisiert das vorhandene {@link StundenplanUnterricht}-Objekt durch das neue Objekt.
	 * <br>Die folgenden Attribute werden nicht aktualisiert:
	 * <br>{@link StundenplanUnterricht#id}
	 * <br>
	 * <br>Die folgenden Attribute werden kopiert:
	 * <br>{@link StundenplanUnterricht#idZeitraster}
	 * <br>{@link StundenplanUnterricht#wochentyp}
	 * <br>{@link StundenplanUnterricht#idKurs}
	 * <br>{@link StundenplanUnterricht#idFach}
	 * <br>{@link StundenplanUnterricht#lehrer}
	 * <br>{@link StundenplanUnterricht#klassen}
	 * <br>{@link StundenplanUnterricht#raeume}
	 * <br>{@link StundenplanUnterricht#schienen}
	 *
	 * @param u  Das neue {@link StundenplanUnterricht}-Objekt, dessen Attribute kopiert werden.
	 */
	public void unterrichtPatchAttributes(final @NotNull StundenplanUnterricht u) {
		unterrichtPatchAttributesAll(ListUtils.create1(u));
	}

	/**
	 * Aktualisiert alle {@link StundenplanUnterricht}-Objekte der Liste.
	 * Hinweis: Die ID kann nicht gepatched werden. Sie wird verwendet, um das zu ersetzende Objekte zu identifizieren.
	 *
	 * @param list Die Liste der neuen {@link StundenplanUnterricht}-Objekte.
	 */
	public void unterrichtPatchAttributesAll(final @NotNull List<@NotNull StundenplanUnterricht> list) {
		// check
		for (final StundenplanUnterricht u : list)
			unterrichtCheckAttributes(u);

		// check duplicates (tag, stunde)
		unterrichtCheckDuplicateInCell(list);

		// replace
		for (final StundenplanUnterricht u : list) {
			DeveloperNotificationException.ifMapRemoveFailes(_unterricht_by_id, u.id);
			DeveloperNotificationException.ifMapPutOverwrites(_unterricht_by_id, u.id, u);
		}

		// update
		update_all();
	}

	private void unterrichtRemoveByIdOhneUpdate(final long idUnterricht) {
		DeveloperNotificationException.ifMapRemoveFailes(_unterricht_by_id, idUnterricht);
	}

	/**
	 * Entfernt aus dem Stundenplan ein existierendes {@link StundenplanUnterricht}-Objekt.
	 *
	 * @param idUnterricht  Die Datenbank-ID des {@link StundenplanUnterricht}-Objekts.
	 */
	public void unterrichtRemoveById(final long idUnterricht) {
		unterrichtRemoveByIdOhneUpdate(idUnterricht);

		update_all();
	}

	/**
	 * Entfernt alle {@link StundenplanUnterricht}-Objekte.
	 *
	 * @param listUnterricht  Die Liste der zu entfernenden {@link StundenplanUnterricht}-Objekte.
	 */
	public void unterrichtRemoveAll(final @NotNull List<@NotNull StundenplanUnterricht> listUnterricht) {
		for (final @NotNull StundenplanUnterricht unterricht : listUnterricht)
			unterrichtRemoveByIdOhneUpdate(unterricht.id);

		update_all();
	}

	/**
	 * Liefert eine String-Menge aller Uhrzeiten der Zeitraster einer bestimmten Unterrichtsstunde. Dabei werden identische Uhrzeiten zusammengefasst.
	 * <br>Beispiel: "08:00-8:45", falls sie nicht abweichen.
	 * <br>Beispiel: "Mo-Mi 08:00-8:45", "Do 07:55-8:40", "Fr 07:40-8:25", falls sie abweichen.
	 *
	 * @param stunde  Die Nr. der Unterrichtsstunde.
	 *
	 * @return eine String-Menge aller Uhrzeiten der Zeitraster einer bestimmten Unterrichtsstunde. Dabei werden identische Uhrzeiten zusammengefasst.
	 */
	public @NotNull List<@NotNull String> unterrichtsstundeGetUhrzeitenAsStrings(final int stunde) {
		final @NotNull List<@NotNull String> listUhrzeit = new ArrayList<>();
		final @NotNull List<@NotNull String> listWochentagVon = new ArrayList<>();
		final @NotNull List<@NotNull String> listWochentagBis = new ArrayList<>();

		for (int wochentag = _zeitrasterWochentagMin; wochentag <= _zeitrasterWochentagMax; wochentag++) {
			final @NotNull String sUhrzeit = unterrichtsstundeGetUhrzeitAsString(wochentag, stunde);
			final @NotNull String sWochentag = Wochentag.fromIDorException(wochentag).kuerzel;

			if (listUhrzeit.isEmpty()) {
				listUhrzeit.add(sUhrzeit);
				listWochentagVon.add(sWochentag);
				listWochentagBis.add(sWochentag);
				continue;
			}

			final @NotNull String sUhrzeitDavor = DeveloperNotificationException.ifListGetLastFailes("listUhrzeit", listUhrzeit);

			if (sUhrzeitDavor.equals(sUhrzeit)) {
				listWochentagBis.set(listWochentagBis.size() - 1, sWochentag);
			} else {
				listUhrzeit.add(sUhrzeit);
				listWochentagVon.add(sWochentag);
				listWochentagBis.add(sWochentag);
			}

		}

		// Fall: Alle Zeiten sind identisch.
		if (listUhrzeit.size() <= 1)
			return listUhrzeit;

		// Fall: Unterschiedliche Zeiten benötigen als Prefix den Wochentag.
		for (int i = 0; i < listUhrzeit.size(); i++) {
			final @NotNull String sUhrzeit = listUhrzeit.get(i);
			final @NotNull String sWochentagVon = listWochentagVon.get(i);
			final @NotNull String sWochentagBis = listWochentagBis.get(i);
			if (sWochentagVon.equals(sWochentagBis))
				listUhrzeit.set(i, sWochentagVon + " " + sUhrzeit);
			else
				listUhrzeit.set(i, sWochentagVon + "–" + sWochentagBis + " " + sUhrzeit);
		}

		return listUhrzeit;
	}

	private @NotNull String unterrichtsstundeGetUhrzeitAsString(final int wochentag, final int stunde) {
		final StundenplanZeitraster zeitraster = _zeitraster_by_wochentag_and_stunde.getOrNull(wochentag, stunde);

		if (zeitraster == null)
			return "???";

		final @NotNull String sBeginn = (zeitraster.stundenbeginn == null) ? "??:??" : DateUtils.getStringOfUhrzeitFromMinuten(zeitraster.stundenbeginn);
		final @NotNull String sEnde = (zeitraster.stundenende == null) ? "??:??" : DateUtils.getStringOfUhrzeitFromMinuten(zeitraster.stundenende);

		return sBeginn + "–" + sEnde + " Uhr";
	}

	// #####################################################################
	// #################### StundenplanZeitraster ##########################
	// #####################################################################

	/**
	 * Fügt ein {@link StundenplanZeitraster}-Objekt hinzu.
	 *
	 * @param zeitraster  Das {@link StundenplanZeitraster}-Objekt, welches hinzugefügt werden soll.
	 */
	public void zeitrasterAdd(final @NotNull StundenplanZeitraster zeitraster) {
		zeitrasterAddAll(ListUtils.create1(zeitraster));
	}


	/**
	 * Fügt alle {@link StundenplanZeitraster}-Objekte hinzu.
	 *
	 * @param listZeitraster  Die Menge der {@link StundenplanZeitraster}-Objekte, welche hinzugefügt werden soll.
	 */
	public void zeitrasterAddAll(final @NotNull List<@NotNull StundenplanZeitraster> listZeitraster) {
		zeitrasterAddAllOhneUpdate(listZeitraster);
		update_all();
	}

	private void zeitrasterAddAllOhneUpdate(final @NotNull List<@NotNull StundenplanZeitraster> list) {
		// check all
		final @NotNull HashSet<@NotNull Long> setOfIDs = new HashSet<>();
		final @NotNull HashSet<@NotNull String> setOfWochentagStunde = new HashSet<>();
		for (final @NotNull StundenplanZeitraster z : list) {
			zeitrasterCheck(z);
			DeveloperNotificationException.ifTrue("zeitrasterAddAllOhneUpdate: ID=" + z.id + " existiert bereits!", _zeitraster_by_id.containsKey(z.id));
			DeveloperNotificationException.ifTrue("zeitrasterAddAllOhneUpdate: ID=" + z.id + " doppelt in der Liste!", !setOfIDs.add(z.id));
			DeveloperNotificationException.ifTrue("zeitrasterAddAllOhneUpdate: WOCHENTAG=" + z.wochentag + ", STUNDE=" + z.unterrichtstunde + " existiert bereits!", _zeitraster_by_wochentag_and_stunde.contains(z.wochentag, z.unterrichtstunde));
			DeveloperNotificationException.ifTrue("zeitrasterAddAllOhneUpdate: WOCHENTAG=" + z.wochentag + ", STUNDE=" + " doppelt in der Liste!", !setOfWochentagStunde.add(z.wochentag + ";" + z.unterrichtstunde));
		}

		// add all
		for (final @NotNull StundenplanZeitraster z : list)
			DeveloperNotificationException.ifMapPutOverwrites(_zeitraster_by_id, z.id, z);
	}

	private static void zeitrasterCheck(final @NotNull StundenplanZeitraster zeitraster) {
		DeveloperNotificationException.ifInvalidID("zeitraster.id", zeitraster.id);
		Wochentag.fromIDorException(zeitraster.wochentag);
		DeveloperNotificationException.ifTrue("(zeit.unterrichtstunde < 0) || (zeit.unterrichtstunde > 29)", (zeitraster.unterrichtstunde < 0) || (zeitraster.unterrichtstunde > 29));
		if ((zeitraster.stundenbeginn != null) && (zeitraster.stundenende != null)) {
			final int beginn = zeitraster.stundenbeginn;
			final int ende = zeitraster.stundenende;
			DeveloperNotificationException.ifTrue("beginn >= ende", beginn >= ende);
		}
	}

	/**
	 * Liefert TRUE, falls ein {@link StundenplanZeitraster}-Objekt mit (Tag, Stunde) existiert.
	 * <br>Laufzeit: O(1)
	 *
	 * @param wochentag  Die ENUM-ID des {@link Wochentag} des {@link StundenplanZeitraster}-Objekts.
	 * @param stunde     Die Unterrichtsstunde des {@link StundenplanZeitraster}-Objekts.
	 *
	 * @return TRUE, falls ein {@link StundenplanZeitraster}-Objekt mit (Tag, Stunde) existiert.
	 */
	public boolean zeitrasterExistsByWochentagAndStunde(final int wochentag, final int stunde) {
		return _zeitraster_by_wochentag_and_stunde.contains(wochentag, stunde);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanZeitraster}-Objekte.
	 *
	 * @return eine Liste aller {@link StundenplanZeitraster}-Objekte.
	 */
	public @NotNull List<@NotNull StundenplanZeitraster> getListZeitraster() {
		return _zeitrastermenge;
	}

	/**
	 * Liefert eine Liste aller Dummy-{@link StundenplanZeitraster}-Objekte, welche in diesem Manager noch nicht definiert sind.
	 *
	 * @param tagVon     Der erste Tag (inklusive) des Bereichs.
	 * @param tagBis     Der letzte Tag (inklusive) des Bereichs.
	 * @param stundeVon  Die erste Stunde (inklusive) des Bereichs.
	 * @param stundeBis  Die letzte Stunde (inklusive) des Bereichs.
	 *
	 * @return eine Liste aller Dummy-{@link StundenplanZeitraster}-Objekte, welche in diesem Manager noch nicht definiert sind.
	 */
	public @NotNull List<@NotNull StundenplanZeitraster> zeitrasterGetDummyListe(final int tagVon, final int tagBis, final int stundeVon, final int stundeBis) {
		final @NotNull List<@NotNull StundenplanZeitraster> listDummies = new ArrayList<>();

		for (int wochentag = tagVon; wochentag <= tagBis; wochentag++)
			for (int stunde = stundeVon; stunde <= stundeBis; stunde++)
				if (!_zeitraster_by_wochentag_and_stunde.contains(wochentag, stunde)) {
					final @NotNull StundenplanZeitraster zeit = new StundenplanZeitraster();
					zeit.id = -1; // Dummy
					zeit.wochentag = wochentag;
					zeit.unterrichtstunde = stunde;
					zeit.stundenbeginn = zeitrasterGetDefaultStundenbeginnByStunde(stunde);
					zeit.stundenende = zeitrasterGetDefaultStundenendeByStunde(stunde);
					listDummies.add(zeit);
				}

		return listDummies;
	}

	/**
	 * Liefert den Default-Stundenbeginn (in Minuten nach 0 Uhr) einer Unterrichtsstunde.
	 * <br>Hinweis: Der Unterricht beginnt um 8 Uhr und nach 45 Minuten sind stets 5 Minuten Pause.
	 *
	 * @param stunde  Die Unterrichtsstunde, nach welcher gefragt wird.
	 *
	 * @return den Default-Stundenbeginn (in Minuten nach 0 Uhr) einer Unterrichtsstunde.
	 */
	public int zeitrasterGetDefaultStundenbeginnByStunde(final int stunde) {
		return 480 + (stunde - 1) * (45 + 5);
	}

	/**
	 * Liefert das Default-Stundenende (in Minuten nach 0 Uhr) einer Unterrichtsstunde.
	 * <br>Hinweis: Das Stundenende ist stets 45 Minuten nach dem {@link #zeitrasterGetDefaultStundenbeginnByStunde(int)} der Stunde.
	 *
	 * @param stunde  Die Unterrichtsstunde, nach welcher gefragt wird.
	 *
	 * @return das Default-Stundenende (in Minuten nach 0 Uhr) einer Unterrichtsstunde.
	 */
	public int zeitrasterGetDefaultStundenendeByStunde(final int stunde) {
		return zeitrasterGetDefaultStundenbeginnByStunde(stunde) + 45;
	}

	/**
	 * Liefert eine Liste der {@link StundenplanZeitraster}-Objekte zu einem bestimmten Wochentag.
	 *
	 * @param wochentag der Wochentag der gewünschten Zeitraster-Objekte
	 *
	 * @return eine Liste aller {@link StundenplanZeitraster}-Objekte zum übergebenen Wochentag.
	 */
	public @NotNull List<@NotNull StundenplanZeitraster> getListZeitrasterZuWochentag(final @NotNull Wochentag wochentag) {
		return CollectionUtils.toFilteredArrayList(_zeitrastermenge, (final @NotNull StundenplanZeitraster z) -> (wochentag.id == z.wochentag));
	}

	/**
	 * Liefert eine Liste der {@link StundenplanZeitraster}-Objekte zu einer bestimmten Unterrichtsstunde.
	 *
	 * @param unterrichtstunde   die Unterrichtsstunde der gewünschten Zeitraster-Objekte
	 *
	 * @return eine Liste aller {@link StundenplanZeitraster}-Objekte zur übergebenen Unterrichtsstunde.
	 */
	public @NotNull List<@NotNull StundenplanZeitraster> getListZeitrasterZuStunde(final int unterrichtstunde) {
		return CollectionUtils.toFilteredArrayList(_zeitrastermenge, (final @NotNull StundenplanZeitraster z) -> (unterrichtstunde == z.unterrichtstunde));
	}

	/**
	 * Liefert die passende Menge an {@link StundenplanZeitraster}-Objekten, welche das Intervall berühren.
	 *
	 * @param zeitrasterStart    Das {@link StundenplanZeitraster} zu dem es startet.
	 * @param minutenVerstrichen Die verstrichene Zeit (in Minuten) seit der "startzeit" .
	 *
	 * @return die passende Menge an {@link StundenplanZeitraster}-Objekten.
	 */
	public @NotNull List<@NotNull StundenplanZeitraster> getZeitrasterByStartVerstrichen(final @NotNull StundenplanZeitraster zeitrasterStart, final int minutenVerstrichen) {
		final Wochentag wochentag = Wochentag.fromIDorException(zeitrasterStart.wochentag);
		final int startzeit = DeveloperNotificationException.ifNull("zeitrasterStart.stundenbeginn ist NULL", zeitrasterStart.stundenbeginn);
		return getZeitrasterByWochentagStartVerstrichen(wochentag, startzeit, minutenVerstrichen);
	}

	/**
	 * Liefert die passende Menge an {@link StundenplanZeitraster}-Objekten, welche das Zeit-Intervall berühren.<br>
	 *
	 * @param wochentag          Der {@link Wochentag} des Zeit-Intervalls.
	 * @param beginn             Der Beginn des Zeit-Intervalls.
	 * @param minutenVerstrichen Daraus ergibt sich das Ende des Zeit-Intervalls.
	 *
	 * @return die passende Menge an {@link StundenplanZeitraster}-Objekten, welche das Intervall berührt.
	 */
	public @NotNull List<@NotNull StundenplanZeitraster> getZeitrasterByWochentagStartVerstrichen(final @NotNull Wochentag wochentag, final int beginn, final int minutenVerstrichen) {
		final int ende = beginn + minutenVerstrichen;
		return CollectionUtils.toFilteredArrayList(_zeitrastermenge, (final @NotNull StundenplanZeitraster z) -> (wochentag.id == z.wochentag) &&  zeitrasterGetSchneidenSich(beginn, ende, z.stundenbeginn, z.stundenende));
	}

	/**
	 * Liefert das {@link StundenplanZeitraster}-Objekt der nächsten Stunde am selben Wochentag, oder NULL.
	 *
	 * @param zeitraster Das aktuelle {@link StundenplanZeitraster}-Objekt.
	 *
	 * @return das {@link StundenplanZeitraster}-Objekt der nächsten Stunde am selben Wochentag, oder NULL.
	 */
	public StundenplanZeitraster getZeitrasterNext(final @NotNull StundenplanZeitraster zeitraster) {
		return _zeitraster_by_wochentag_and_stunde.getOrNull(zeitraster.wochentag, zeitraster.unterrichtstunde + 1);
	}

	/**
	 * Liefert den kleinsten Minuten-Wert aller Zeitraster, oder 480 (8 Uhr).
	 * <br>Laufzeit: O(1)
	 *
	 * @return den kleinsten Minuten-Wert aller Zeitraster, oder 480 (8 Uhr).
	 */
	public int zeitrasterGetMinutenMin() {
		return (_zeitrasterMinutenMin == null) ? 480 : _zeitrasterMinutenMin;
	}

	/**
	 * Liefert das Minimum aller {@link StundenplanZeitraster#stundenbeginn}-Objekte einer bestimmten Unterrichtsstunde, oder 480 (8 Uhr) falls keines vorhanden ist.
	 * <br>Laufzeit: O(1)
	 *
	 * @param stunde  Die Unterrichtsstunde, deren Minimum gesucht wird.
	 *
	 * @return das Minimum aller {@link StundenplanZeitraster#stundenbeginn}-Objekte einer bestimmten Unterrichtsstunde, oder 480 (8 Uhr) falls keines vorhanden ist.
	 */
	public int zeitrasterGetMinutenMinDerStunde(final int stunde) {
		final Integer min = _zeitrasterMinutenMinByStunde.get(stunde); // Beide Fälle von NULL können auftreten!
		return (min == null) ? 480 : min;
	}

	/**
	 * Liefert den größten Minuten-Wert aller Zeitraster, oder 480 (8 Uhr).
	 * <br>Laufzeit: O(1)
	 *
	 * @return den größten Minuten-Wert aller Zeitraster, oder 480 (8 Uhr).
	 */
	public int zeitrasterGetMinutenMax() {
		return (_zeitrasterMinutenMax == null) ? 480 : _zeitrasterMinutenMax;
	}

	/**
	 * Liefert das Maximum aller {@link StundenplanZeitraster#stundenbeginn}-Objekte einer bestimmten Unterrichtsstunde, oder 480 (8 Uhr) falls keines vorhanden ist.
	 * <br>Laufzeit: O(1)
	 *
	 * @param stunde  Die Unterrichtsstunde, deren Maximum gesucht wird.
	 *
	 * @return das Maximum aller {@link StundenplanZeitraster#stundenbeginn}-Objekte einer bestimmten Unterrichtsstunde, oder 480 (8 Uhr) falls keines vorhanden ist.
	 */
	public int zeitrasterGetMinutenMaxDerStunde(final int stunde) {
		final Integer max = _zeitrasterMinutenMaxByStunde.get(stunde); // Beide Fälle von NULL können auftreten!
		return (max == null) ? 480 : max;
	}

	/**
	 * Liefert die kleinste Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return die kleinste Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 */
	public int zeitrasterGetStundeMin() {
		return _zeitrasterStundeMin;
	}

	/**
	 * Liefert die kleinste nicht leere Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return die kleinste nicht leere Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 */
	public int zeitrasterGetStundeMinOhneLeere() {
		return _zeitrasterStundeMinOhneLeere;
	}

	/**
	 * Liefert die größte Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return die größte Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 */
	public int zeitrasterGetStundeMax() {
		return _zeitrasterStundeMax;
	}

	/**
	 * Liefert die größte nicht leere Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return die größte nicht leere Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 */
	public int zeitrasterGetStundeMaxOhneLeere() {
		return _zeitrasterStundeMaxOhneLeere;
	}

	/**
	 * Liefert die ID des kleinsten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return die ID des kleinsten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 */
	public int zeitrasterGetWochentagMin() {
		return _zeitrasterWochentagMin;
	}

	/**
	 * Liefert den kleinsten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return den kleinsten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 */
	public @NotNull Wochentag zeitrasterGetWochentagMinEnum() {
		return Wochentag.fromIDorException(_zeitrasterWochentagMin);
	}

	/**
	 * Liefert die ID des größten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return die ID des größten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 */
	public int zeitrasterGetWochentagMax() {
		return _zeitrasterWochentagMax;
	}

	/**
	 * Liefert den größten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return den größten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 */
	public @NotNull Wochentag zeitrasterGetWochentagMaxEnum() {
		return Wochentag.fromIDorException(_zeitrasterWochentagMax);
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanZeitraster}-Objekt.
	 *
	 * @param idZeitraster  Die Datenbank-ID des Zeitrasters.
	 *
	 * @return das zur ID zugehörige {@link StundenplanZeitraster}-Objekt.
	 */
	public @NotNull StundenplanZeitraster zeitrasterGetByIdOrException(final long idZeitraster) {
		return DeveloperNotificationException.ifMapGetIsNull(_zeitraster_by_id, idZeitraster);
	}

	/**
	 * Liefert die Beginn-Uhrzeit des {@link StundenplanZeitraster} oder den leeren String, falls diese NULL ist.
	 * <br>Beispiel: "09:30" oder ""
	 * <br>Laufzeit: O(1)
	 *
	 * @param idZeitraster  Die Datenbank-ID des {@link StundenplanZeitraster}.
	 *
	 * @return die Beginn-Uhrzeit des {@link StundenplanZeitraster} oder den leeren String, falls diese NULL ist.
	 */
	public @NotNull String zeitrasterGetByIdStringOfUhrzeitBeginn(final long idZeitraster) {
		final @NotNull StundenplanZeitraster zeitraster =  DeveloperNotificationException.ifMapGetIsNull(_zeitraster_by_id, idZeitraster);
		return (zeitraster.stundenbeginn == null) ? "" : DateUtils.getStringOfUhrzeitFromMinuten(zeitraster.stundenbeginn);
	}

	/**
	 * Liefert die End-Uhrzeit des {@link StundenplanZeitraster} oder den leeren String, falls diese NULL ist.
	 * <br>Beispiel: "10:15" oder ""
	 * <br>Laufzeit: O(1)
	 *
	 * @param idZeitraster  Die Datenbank-ID des {@link StundenplanZeitraster}.
	 *
	 * @return die End-Uhrzeit des {@link StundenplanZeitraster} oder den leeren String, falls diese NULL ist.
	 */
	public @NotNull String zeitrasterGetByIdStringOfUhrzeitEnde(final long idZeitraster) {
		final @NotNull StundenplanZeitraster zeitraster =  DeveloperNotificationException.ifMapGetIsNull(_zeitraster_by_id, idZeitraster);
		return (zeitraster.stundenende == null) ? "" : DateUtils.getStringOfUhrzeitFromMinuten(zeitraster.stundenende);
	}

	/**
	 * Liefert das zu (wochentag, stunde) zugehörige {@link StundenplanZeitraster}-Objekt.
	 *
	 * @param wochentag  Die ENUM-ID des {@link Wochentag} des gesuchten Zeitrasters.
	 * @param stunde     Die Unterrichtsstunde des gesuchten Zeitrasters.
	 *
	 * @return das zu (wochentag, stunde) zugehörige {@link StundenplanZeitraster}-Objekt.
	 * @throws DeveloperNotificationException falls kein Zeitraster-Eintrag existiert
	 */
	public @NotNull StundenplanZeitraster zeitrasterGetByWochentagAndStundeOrException(final int wochentag, final int stunde) {
		return _zeitraster_by_wochentag_and_stunde.getNonNullOrException(wochentag, stunde);
	}

	/**
	 * Liefert das zu (wochentag, stunde) zugehörige {@link StundenplanZeitraster}-Objekt, falls es existiert, sonst NULL.
	 *
	 * @param wochentag  Die ENUM-ID des {@link Wochentag} des gesuchten Zeitrasters.
	 * @param stunde     Die Unterrichtsstunde des gesuchten Zeitrasters.
	 *
	 * @return das zu (wochentag, stunde) zugehörige {@link StundenplanZeitraster}-Objekt, falls es existiert, sonst NULL.
	 */
	public StundenplanZeitraster zeitrasterGetByWochentagAndStundeOrNull(final int wochentag, final int stunde) {
		return _zeitraster_by_wochentag_and_stunde.getOrNull(wochentag, stunde);
	}

	/**
	 * Liefert TRUE, falls die Intervalle [beginn1, ende1] und [beginn2, ende2] sich schneiden.
	 *
	 * @param beginn1  Der Anfang (inklusive) des ersten Intervalls (in Minuten) seit 0 Uhr.
	 * @param ende1    Das Ende (inklusive) des ersten Intervalls (in Minuten) seit 0 Uhr.
	 * @param iBeginn2 Der Anfang (inklusive) des zweiten Intervalls (in Minuten) seit 0 Uhr.
	 * @param iEnde2   Das Ende (inklusive) des zweiten Intervalls (in Minuten) seit 0 Uhr.
	 *
	 * @return TRUE, falls die Intervalle [beginn1, ende1] und [beginn2, ende2] sich schneiden.
	 */
	public boolean zeitrasterGetSchneidenSich(final int beginn1, final int ende1, final Integer iBeginn2, final Integer iEnde2) {
		final int beginn2 = DeveloperNotificationException.ifNull("zeitraster.stundenbeginn ist NULL!", iBeginn2);
		final int ende2 = DeveloperNotificationException.ifNull("zeitraster.stundenende ist NULL!", iEnde2);
		DeveloperNotificationException.ifTrue("beginn1 < 0", beginn1 < 0);
		DeveloperNotificationException.ifTrue("beginn2 < 0", beginn2 < 0);
		DeveloperNotificationException.ifTrue("beginn1 > ende1", beginn1 > ende1);
		DeveloperNotificationException.ifTrue("beginn2 > ende2", beginn2 > ende2);
		return !((ende1 < beginn2) || (ende2 < beginn1));
	}

	/**
	 * Liefert alle verwendeten sortierten Unterrichtsstunden der {@link StundenplanZeitraster}.
	 * Das Array beinhaltet alle Zahlen von {@link #zeitrasterGetStundeMin()} bis {@link #zeitrasterGetStundeMax()}.
	 * <br>Laufzeit: O(1), da Referenz auf ein Array.
	 *
	 * @return alle verwendeten sortierten Unterrichtsstunden der {@link StundenplanZeitraster}.
	 */
	public @NotNull int @NotNull [] zeitrasterGetStundenRange() {
		return _zeitrasterStundenRange;
	}

	/**
	 * Liefert alle verwendeten sortierten Unterrichtsstunden der nicht leeren {@link StundenplanZeitraster}.
	 * Das Array beinhaltet alle Zahlen von {@link #zeitrasterGetStundeMinOhneLeere()} bis {@link #zeitrasterGetStundeMaxOhneLeere()}.
	 * <br>Laufzeit: O(1), da Referenz auf ein Array.
	 *
	 * @return alle verwendeten sortierten Unterrichtsstunden der nicht leeren {@link StundenplanZeitraster}.
	 */
	public @NotNull int @NotNull [] zeitrasterGetStundenRangeOhneLeere() {
		return _zeitrasterStundenRangeOhneLeere;
	}

	/**
	 * Liefert alle verwendeten sortierten {@link Wochentag}-Objekte der {@link StundenplanZeitraster}.
	 * Das Array beinhaltet alle {@link Wochentag}-Objekte von {@link #zeitrasterGetWochentagMin} bis {@link #zeitrasterGetWochentagMax()}.
	 * <br>Laufzeit: O(1), da Referenz auf ein Array.
	 *
	 * @return alle verwendeten sortierten {@link Wochentag}-Objekte der {@link StundenplanZeitraster}.
	 */
	public @NotNull Wochentag @NotNull [] zeitrasterGetWochentageAlsEnumRange() {
		return _zeitrasterWochentageAlsEnumRange;
	}

	/**
	 * Liefert TRUE, falls es in der Stundenplanzelle "wochtag, stunde" Unterricht eines "wochentyps" gibt.
	 *
	 * @param wochentag  Der {@link Wochentag}-ENUM.
	 * @param stunde     Die Unterrichtsstunde.
	 * @param wochentyp  Der Wochentyp (0 jede Woche, 1 nur Woche A, 2 nur Woche B, ...)
	 *
	 * @return TRUE, falls es in der Stundenplanzelle "wochtag, stunde" Unterricht eines "wochentyps" gibt.
	 */
	public boolean zeitrasterHatUnterrichtByWochentagAndStundeAndWochentyp(final @NotNull Wochentag wochentag, final int stunde, final int wochentyp) {
		final StundenplanZeitraster z = _zeitraster_by_wochentag_and_stunde.getOrNull(wochentag.id, stunde);

		if (z != null)
			return !Map2DUtils.getOrCreateArrayList(_unterrichtmenge_by_idZeitraster_and_wochentyp, z.id, wochentyp).isEmpty();

		return false;
	}

	/**
	 * Liefert TRUE, falls die Klasse in der Stundenplanzelle "wochtag, stunde" Unterricht eines "wochentyps" hat.
	 *
	 * @param idKlasse   Die Datenbank-ID der Klasse.
	 * @param wochentag  Die ENUM-ID des {@link Wochentag} des Zeitrasters.
	 * @param stunde     Die Unterrichtsstunde.
	 * @param wochentyp  Der Wochentyp (0 jede Woche, 1 nur Woche A, 2 nur Woche B, ...)
	 *
	 * @return TRUE, falls die Klasse in der Stundenplanzelle "wochtag, stunde" Unterricht eines "wochentyps" hat.
	 */
	public boolean zeitrasterHatUnterrichtByKlasseIdWochentagAndStundeAndWochentyp(final long idKlasse, final int wochentag, final int stunde, final int wochentyp) {
		final StundenplanZeitraster z = _zeitraster_by_wochentag_and_stunde.getOrNull(wochentag, stunde);

		if (z != null)
			for (final @NotNull StundenplanUnterricht u : Map2DUtils.getOrCreateArrayList(_unterrichtmenge_by_idKlasse_and_idZeitraster, idKlasse, z.id))
				if (u.wochentyp == wochentyp)
					return true;

		return false;
	}

	/**
	 * Liefert TRUE, falls die Lehrkraft in der Stundenplanzelle "wochtag, stunde" Unterricht eines "wochentyps" hat.
	 *
	 * @param idLehrer   Die Datenbank-ID der Lehrkraft.
	 * @param wochentag  Die ENUM-ID des {@link Wochentag} des Zeitrasters.
	 * @param stunde     Die Unterrichtsstunde.
	 * @param wochentyp  Der Wochentyp (0 jede Woche, 1 nur Woche A, 2 nur Woche B, ...)
	 *
	 * @return TRUE, falls die Lehrkraft in der Stundenplanzelle "wochtag, stunde" Unterricht eines "wochentyps" hat.
	 */
	public boolean zeitrasterHatUnterrichtByLehrerIdWochentagAndStundeAndWochentyp(final long idLehrer, final int wochentag, final int stunde, final int wochentyp) {
		final StundenplanZeitraster z = _zeitraster_by_wochentag_and_stunde.getOrNull(wochentag, stunde);

		if (z != null)
			for (final @NotNull StundenplanUnterricht u : Map2DUtils.getOrCreateArrayList(_unterrichtmenge_by_idLehrer_and_idZeitraster, idLehrer, z.id))
				if (u.wochentyp == wochentyp)
					return true;

		return false;
	}

	/**
	 * Liefert TRUE, falls der Schüler in der Stundenplanzelle "wochtag, stunde" Unterricht eines "wochentyps" hat.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param wochentag   Die ENUM-ID des {@link Wochentag} des Zeitrasters.
	 * @param stunde      Die Unterrichtsstunde.
	 * @param wochentyp   Der Wochentyp (0 jede Woche, 1 nur Woche A, 2 nur Woche B, ...)
	 *
	 * @return TRUE, falls der Schüler in der Stundenplanzelle "wochtag, stunde" Unterricht eines "wochentyps" hat.
	 */
	public boolean zeitrasterHatUnterrichtBySchuelerIdWochentagAndStundeAndWochentyp(final long idSchueler, final int wochentag, final int stunde, final int wochentyp) {
		final StundenplanZeitraster z = _zeitraster_by_wochentag_and_stunde.getOrNull(wochentag, stunde);

		if (z != null)
			for (final @NotNull StundenplanUnterricht u : Map2DUtils.getOrCreateArrayList(_unterrichtmenge_by_idSchueler_and_idZeitraster, idSchueler, z.id))
				if (u.wochentyp == wochentyp)
					return true;

		return false;
	}

	/**
	 * Liefert TRUE, falls der Jahrgang in der Stundenplanzelle "wochtag, stunde" Unterricht eines "wochentyps" hat.
	 *
	 * @param idJahrgang  Die Datenbank-ID des Jahrgangs.
	 * @param wochentag   Die ENUM-ID des {@link Wochentag} des Zeitrasters.
	 * @param stunde      Die Unterrichtsstunde.
	 * @param wochentyp   Der Wochentyp (0 jede Woche, 1 nur Woche A, 2 nur Woche B, ...)
	 *
	 * @return TRUE, falls der Jahrgang in der Stundenplanzelle "wochtag, stunde" Unterricht eines "wochentyps" hat.
	 */
	public boolean zeitrasterHatUnterrichtByJahrgangIdWochentagAndStundeAndWochentyp(final long idJahrgang, final int wochentag, final int stunde, final int wochentyp) {
		final StundenplanZeitraster z = _zeitraster_by_wochentag_and_stunde.getOrNull(wochentag, stunde);

		if (z != null)
			for (final @NotNull StundenplanUnterricht u : Map2DUtils.getOrCreateArrayList(_unterrichtmenge_by_idJahrgang_and_idZeitraster, idJahrgang, z.id))
				if (u.wochentyp == wochentyp)
					return true;

		return false;
	}

	/**
	 * Liefert TRUE, falls es mindestens einen Unterricht im Zeitraster mit einem einen Wochentyp 0 gibt.
	 *
	 * @param idZeitraster  Die Datenbank-ID des Zeitrasters.
	 *
	 * @return TRUE, falls es mindestens einen Unterricht im Zeitraster mit einem einen Wochentyp 0 gibt.
	 */
	public boolean zeitrasterHatUnterrichtMitWochentyp0(final long idZeitraster) {
		return !Map2DUtils.getOrCreateArrayList(_unterrichtmenge_by_idZeitraster_and_wochentyp, idZeitraster, 0).isEmpty();
	}

	/**
	 * Liefert TRUE, falls das Zeitraster existiert und es mindestens einen Unterricht im Zeitraster mit einem einen Wochentyp 0 gibt.

	 * @param wochentag  Der {@link Wochentag}-ENUM.
	 * @param stunde     Die Unterrichtsstunde.
	 *
	 * @return TRUE, falls das Zeitraster existiert und es mindestens einen Unterricht im Zeitraster mit einem einen Wochentyp 0 gibt.
	 */
	public boolean zeitrasterHatUnterrichtMitWochentyp0ByWochentagAndStunde(final @NotNull Wochentag wochentag, final int stunde) {
		final StundenplanZeitraster z = _zeitraster_by_wochentag_and_stunde.getOrNull(wochentag.id, stunde);
		return (z != null) && zeitrasterHatUnterrichtMitWochentyp0(z.id);
	}

	/**
	 * Liefert TRUE, falls die Klasse am "wochtag, stunde" Unterricht mit Wochentyp 0 hat.
     *
	 * @param idKlasse   Die Datenbank-ID der Klasse.
	 * @param wochentag  Die ID des {@link Wochentag}-ENUM.
	 * @param stunde     Die Unterrichtsstunde.
	 *
	 * @return TRUE, falls die Klasse am "wochtag, stunde" Unterricht mit Wochentyp 0 hat.
	 */
	public boolean zeitrasterHatUnterrichtMitWochentyp0ByKlasseIdWochentagAndStunde(final long idKlasse, final int wochentag, final int stunde) {
		return zeitrasterHatUnterrichtByKlasseIdWochentagAndStundeAndWochentyp(idKlasse, wochentag, stunde, 0);
	}

	/**
	 * Liefert TRUE, falls die Lehrkraft am "wochtag, stunde" Unterricht mit Wochentyp 0 hat.
     *
	 * @param idLehrer   Die Datenbank-ID der Lehrkraft.
	 * @param wochentag  Die ID des {@link Wochentag}-ENUM.
	 * @param stunde     Die Unterrichtsstunde.
	 *
	 * @return TRUE, falls die Lehrkraft am "wochtag, stunde" Unterricht mit Wochentyp 0 hat.
	 */
	public boolean zeitrasterHatUnterrichtMitWochentyp0ByLehrerIdWochentagAndStunde(final long idLehrer, final int wochentag, final int stunde) {
		return zeitrasterHatUnterrichtByLehrerIdWochentagAndStundeAndWochentyp(idLehrer, wochentag, stunde, 0);
	}

	/**
	 * Liefert TRUE, falls der Schüler am "wochtag, stunde" Unterricht mit Wochentyp 0 hat.
     *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param wochentag   Die ID des {@link Wochentag}-ENUM.
	 * @param stunde      Die Unterrichtsstunde.
	 *
	 * @return TRUE, falls der Schüler am "wochtag, stunde" Unterricht mit Wochentyp 0 hat.
	 */
	public boolean zeitrasterHatUnterrichtMitWochentyp0BySchuelerIdWochentagAndStunde(final long idSchueler, final int wochentag, final int stunde) {
		return zeitrasterHatUnterrichtBySchuelerIdWochentagAndStundeAndWochentyp(idSchueler, wochentag, stunde, 0);
	}

	/**
	 * Liefert TRUE, falls der Jahrgang am "wochtag, stunde" Unterricht mit Wochentyp 0 hat.
     *
	 * @param idJahrgang  Die Datenbank-ID des Jahrgangs.
	 * @param wochentag   Die ID des {@link Wochentag}-ENUM.
	 * @param stunde      Die Unterrichtsstunde.
	 *
	 * @return TRUE, falls der Jahrgang am "wochtag, stunde" Unterricht mit Wochentyp 0 hat.
	 */
	public boolean zeitrasterHatUnterrichtMitWochentyp0ByJahrgangIdWochentagAndStunde(final long idJahrgang, final int wochentag, final int stunde) {
		return zeitrasterHatUnterrichtByJahrgangIdWochentagAndStundeAndWochentyp(idJahrgang, wochentag, stunde, 0);
	}

	/**
	 * Liefert TRUE, falls es mindestens einen Unterricht im Zeitraster mit einem einen Wochentyp 1 bis N gibt.
	 *
	 * @param idZeitraster  Die Datenbank-ID des Zeitrasters.
	 *
	 * @return TRUE, falls es mindestens einen Unterricht im Zeitraster mit einem einen Wochentyp 1 bis N gibt.
	 */
	public boolean zeitrasterHatUnterrichtMitWochentyp1BisN(final long idZeitraster) {
		for (int wochentyp = 1; wochentyp <= _stundenplanWochenTypModell; wochentyp++)
			if (!Map2DUtils.getOrCreateArrayList(_unterrichtmenge_by_idZeitraster_and_wochentyp, idZeitraster, wochentyp).isEmpty())
				return true;
		return false;
	}

	/**
	 * Liefert TRUE, falls das Zeitraster existiert und es mindestens einen Unterricht im Zeitraster mit einem Wochentyp 1 bis N gibt.
	 *
	 * @param wochentag  Der {@link Wochentag}-ENUM.
	 * @param stunde     Die Unterrichtsstunde.
	 *
	 * @return TRUE, falls das Zeitraster existiert und es mindestens einen Unterricht im Zeitraster mit einem Wochentyp 1 bis N gibt.
	 */
	public boolean zeitrasterHatUnterrichtMitWochentyp1BisNByWochentagAndStunde(final @NotNull Wochentag wochentag, final int stunde) {
		final StundenplanZeitraster z = _zeitraster_by_wochentag_and_stunde.getOrNull(wochentag.id, stunde);
		return (z != null) && zeitrasterHatUnterrichtMitWochentyp1BisN(z.id);
	}

	/**
	 * Liefert TRUE, falls die Klasse am "wochentag, stunde" Unterricht mit einem Wochentyp von 1 bis N hat.
	 *
	 * @param idKlasse   Die Datenbank-ID der Klasse.
	 * @param wochentag  Der ID des {@link Wochentag}-ENUM.
	 * @param stunde     Die Unterrichtsstunde.
	 *
	 * @return TRUE, falls die Klasse am "wochentag, stunde" Unterricht mit einem Wochentyp von 1 bis N hat.
	 */
	public boolean zeitrasterHatUnterrichtMitWochentyp1BisNByKlasseIdWochentagAndStunde(final long idKlasse, final int wochentag, final int stunde) {
		final StundenplanZeitraster z = _zeitraster_by_wochentag_and_stunde.getOrNull(wochentag, stunde);

		if (z != null)
			for (final @NotNull StundenplanUnterricht u : Map2DUtils.getOrCreateArrayList(_unterrichtmenge_by_idKlasse_and_idZeitraster, idKlasse, z.id))
				if (u.wochentyp >= 1)
					return true;

		return false;
	}

	/**
	 * Liefert TRUE, falls die Lehrkraft am "wochentag, stunde" Unterricht mit einem Wochentyp von 1 bis N hat.
	 *
	 * @param idLehrer   Die Datenbank-ID der Lehrkraft.
	 * @param wochentag  Der ID des {@link Wochentag}-ENUM.
	 * @param stunde     Die Unterrichtsstunde.
	 *
	 * @return TRUE, falls die Lehrkraft am "wochentag, stunde" Unterricht mit einem Wochentyp von 1 bis N hat.
	 */
	public boolean zeitrasterHatUnterrichtMitWochentyp1BisNByLehrerIdWochentagAndStunde(final long idLehrer, final int wochentag, final int stunde) {
		final StundenplanZeitraster z = _zeitraster_by_wochentag_and_stunde.getOrNull(wochentag, stunde);

		if (z != null)
			for (final @NotNull StundenplanUnterricht u : Map2DUtils.getOrCreateArrayList(_unterrichtmenge_by_idLehrer_and_idZeitraster, idLehrer, z.id))
				if (u.wochentyp >= 1)
					return true;

		return false;
	}

	/**
	 * Liefert TRUE, falls der Schüler am "wochentag, stunde" Unterricht mit einem Wochentyp von 1 bis N hat.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param wochentag   Der ID des {@link Wochentag}-ENUM.
	 * @param stunde      Die Unterrichtsstunde.
	 *
	 * @return TRUE, falls der Schüler am "wochentag, stunde" Unterricht mit einem Wochentyp von 1 bis N hat.
	 */
	public boolean zeitrasterHatUnterrichtMitWochentyp1BisNBySchuelerIdWochentagAndStunde(final long idSchueler, final int wochentag, final int stunde) {
		final StundenplanZeitraster z = _zeitraster_by_wochentag_and_stunde.getOrNull(wochentag, stunde);

		if (z != null)
			for (final @NotNull StundenplanUnterricht u : Map2DUtils.getOrCreateArrayList(_unterrichtmenge_by_idSchueler_and_idZeitraster, idSchueler, z.id))
				if (u.wochentyp >= 1)
					return true;

		return false;
	}

	/**
	 * Liefert TRUE, falls der Jahrgang am "wochentag, stunde" Unterricht mit einem Wochentyp von 1 bis N hat.
	 *
	 * @param idJahrgang  Die Datenbank-ID des Jahrgangs.
	 * @param wochentag   Der ID des {@link Wochentag}-ENUM.
	 * @param stunde      Die Unterrichtsstunde.
	 *
	 * @return TRUE, falls der Jahrgang am "wochentag, stunde" Unterricht mit einem Wochentyp von 1 bis N hat.
	 */
	public boolean zeitrasterHatUnterrichtMitWochentyp1BisNByJahrgangIdWochentagAndStunde(final long idJahrgang, final int wochentag, final int stunde) {
		final StundenplanZeitraster z = _zeitraster_by_wochentag_and_stunde.getOrNull(wochentag, stunde);

		if (z != null)
			for (final @NotNull StundenplanUnterricht u : Map2DUtils.getOrCreateArrayList(_unterrichtmenge_by_idJahrgang_and_idZeitraster, idJahrgang, z.id))
				if (u.wochentyp >= 1)
					return true;

		return false;
	}

	/**
	 * Liefert TRUE, falls ein {@link StundenplanZeitraster}-Objekt mit dem Wochentag existiert.
	 *
	 * @param wochentag  Der Wochentag, deren Zeitrastermenge überprüft wird.
	 *
	 * @return TRUE, falls ein {@link StundenplanZeitraster}-Objekt mit dem Wochentag existiert.
	 */
	public boolean zeitrasterExistsByWochentag(final int wochentag) {
		return !MapUtils.getOrCreateArrayList(_zeitrastermenge_by_wochentag, wochentag).isEmpty();
	}

	/**
	 * Aktualisiert das vorhandene {@link StundenplanZeitraster}-Objekt durch das neue Objekt.
	 * <br>Hinweis: Die ID eines Objekts lässt sich nicht patchen.
	 *
	 * @param zeitraster  Das neue {@link StundenplanZeitraster}-Objekt, welches das alte ersetzt.
	 */
	public void zeitrasterPatchAttributes(final @NotNull StundenplanZeitraster zeitraster) {
		zeitrasterPatchAttributesAll(ListUtils.create1(zeitraster));
	}

	/**
	 * Aktualisiert die vorhandenen {@link StundenplanZeitraster}-Objekte durch die neuen Objekte.
	 * <br>Hinweis: Die ID der Objekte lassen sich nicht patchen.
	 *
	 * @param zeitrasterList  Die neuen {@link StundenplanZeitraster}-Objekte, welche die alten ersetzen.
	 */
	public void zeitrasterPatchAttributesAll(final @NotNull List<@NotNull StundenplanZeitraster> zeitrasterList) {
		// check
		final @NotNull HashMap2D<@NotNull Integer, @NotNull Integer, @NotNull StundenplanZeitraster> mapWochentagStunde = new HashMap2D<>();
		for (final @NotNull StundenplanZeitraster z : zeitrasterList) {
			zeitrasterCheck(z);
			DeveloperNotificationException.ifMapNotContains("_zeitraster_by_id", _zeitraster_by_id, z.id);
			DeveloperNotificationException.ifMap2DPutOverwrites(mapWochentagStunde, z.wochentag, z.unterrichtstunde, z);
		}

		// replace
		for (final @NotNull StundenplanZeitraster z : zeitrasterList) {
			DeveloperNotificationException.ifMapRemoveFailes(_zeitraster_by_id, z.id);
			DeveloperNotificationException.ifMapPutOverwrites(_zeitraster_by_id, z.id, z);
		}

		// update
		update_all();
	}

	private void zeitrasterRemoveOhneUpdate(final long idZeitraster) {
		// Kaskade: StundenplanUnterricht
		for (final @NotNull StundenplanUnterricht u : MapUtils.getOrCreateArrayList(_unterrichtmenge_by_idZeitraster, idZeitraster))
			unterrichtRemoveByIdOhneUpdate(u.id);

		// Remove
		DeveloperNotificationException.ifMapRemoveFailes(_zeitraster_by_id, idZeitraster);
	}

	/**
	 * Entfernt aus dem Stundenplan ein existierendes {@link StundenplanZeitraster}-Objekt.
	 * <br> Hinweis: Kaskadierend werden auch alle {@link StundenplanUnterricht}-Objekte gelöscht.
	 *
	 * @param idZeitraster  Die Datenbank-ID des {@link StundenplanZeitraster}-Objekts.
	 */
	public void zeitrasterRemoveById(final long idZeitraster) {
		zeitrasterRemoveOhneUpdate(idZeitraster);

		update_all();
	}

	/**
	 * Entfernt alle {@link StundenplanZeitraster}-Objekte aus dem Stundenplan.
	 * <br> Hinweis: Kaskadierend werden auch alle {@link StundenplanUnterricht}-Objekte gelöscht.
	 *
	 * @param listZeitraster  Die {@link StundenplanZeitraster}-Objekte, die entfernt werden sollen.
	 */
	public void zeitrasterRemoveAll(final @NotNull List<@NotNull StundenplanZeitraster> listZeitraster) {
		for (final @NotNull StundenplanZeitraster zeitraster : listZeitraster)
			zeitrasterRemoveOhneUpdate(zeitraster.id);

		update_all();
	}

	/**
	 * Entfernt alle {@link StundenplanZeitraster}-Objekte, die einen bestimmten Wochentag haben.
	 *
	 * @param wochentagEnumID  Die ID des {@link Wochentag}.
	 */
	public void zeitrasterRemoveAllByWochentag(final int wochentagEnumID) {
		zeitrasterRemoveAll(MapUtils.getOrCreateArrayList(_zeitrastermenge_by_wochentag, wochentagEnumID));
	}

}
