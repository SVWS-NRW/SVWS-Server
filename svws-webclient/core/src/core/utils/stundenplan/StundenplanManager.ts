import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap } from '../../../java/util/HashMap';
import { StundenplanKlasse } from '../../../core/data/stundenplan/StundenplanKlasse';
import { ArrayList } from '../../../java/util/ArrayList';
import { StundenplanKurs } from '../../../core/data/stundenplan/StundenplanKurs';
import { JavaString } from '../../../java/lang/JavaString';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { StundenplanJahrgang } from '../../../core/data/stundenplan/StundenplanJahrgang';
import { DateUtils } from '../../../core/utils/DateUtils';
import { JavaMath } from '../../../java/lang/JavaMath';
import type { Comparator } from '../../../java/util/Comparator';
import { StundenplanSchueler } from '../../../core/data/stundenplan/StundenplanSchueler';
import { StundenplanKlassenunterricht } from '../../../core/data/stundenplan/StundenplanKlassenunterricht';
import { StundenplanLehrer } from '../../../core/data/stundenplan/StundenplanLehrer';
import type { List } from '../../../java/util/List';
import { cast_java_util_List } from '../../../java/util/List';
import { Stundenplan, cast_de_svws_nrw_core_data_stundenplan_Stundenplan } from '../../../core/data/stundenplan/Stundenplan';
import { HashSet } from '../../../java/util/HashSet';
import { StundenplanPausenaufsichtBereich } from '../../../core/data/stundenplan/StundenplanPausenaufsichtBereich';
import { StundenplanPausenaufsicht } from '../../../core/data/stundenplan/StundenplanPausenaufsicht';
import { SetUtils } from '../../../core/utils/SetUtils';
import { CollectionUtils } from '../../../core/utils/CollectionUtils';
import { MapUtils } from '../../../core/utils/MapUtils';
import { Map2DUtils } from '../../../core/utils/Map2DUtils';
import { StundenplanAufsichtsbereich } from '../../../core/data/stundenplan/StundenplanAufsichtsbereich';
import { StundenplanRaum } from '../../../core/data/stundenplan/StundenplanRaum';
import { StundenplanSchiene } from '../../../core/data/stundenplan/StundenplanSchiene';
import { JavaLong } from '../../../java/lang/JavaLong';
import type { Collection } from '../../../java/util/Collection';
import { Class } from '../../../java/lang/Class';
import { Wochentag } from '../../../core/types/Wochentag';
import type { JavaMap } from '../../../java/util/JavaMap';
import { StundenplanKomplett, cast_de_svws_nrw_core_data_stundenplan_StundenplanKomplett } from '../../../core/data/stundenplan/StundenplanKomplett';
import { HashMap2D } from '../../../core/adt/map/HashMap2D';
import { StundenplanUnterrichtsverteilung, cast_de_svws_nrw_core_data_stundenplan_StundenplanUnterrichtsverteilung } from '../../../core/data/stundenplan/StundenplanUnterrichtsverteilung';
import type { JavaSet } from '../../../java/util/JavaSet';
import { LongArrayKey } from '../../../core/adt/LongArrayKey';
import { Map3DUtils } from '../../../core/utils/Map3DUtils';
import { StringUtils } from '../../../core/utils/StringUtils';
import type { JavaIterator } from '../../../java/util/JavaIterator';
import { StundenplanUnterricht, cast_de_svws_nrw_core_data_stundenplan_StundenplanUnterricht } from '../../../core/data/stundenplan/StundenplanUnterricht';
import { StundenplanKalenderwochenzuordnung } from '../../../core/data/stundenplan/StundenplanKalenderwochenzuordnung';
import { HashMap3D } from '../../../core/adt/map/HashMap3D';
import { AVLSet } from '../../../core/adt/set/AVLSet';
import { StundenplanKonfiguration } from '../../../core/data/stundenplan/StundenplanKonfiguration';
import { StundenplanZeitraster } from '../../../core/data/stundenplan/StundenplanZeitraster';
import { StundenplanPausenzeit } from '../../../core/data/stundenplan/StundenplanPausenzeit';
import { StundenplanUnterrichtUtils } from '../../../core/utils/stundenplan/StundenplanUnterrichtUtils';
import { Exception } from '../../../java/lang/Exception';
import { BlockungsUtils } from '../../../core/utils/BlockungsUtils';
import { StundenplanFach } from '../../../core/data/stundenplan/StundenplanFach';
import { ListUtils } from '../../../core/utils/ListUtils';

export class StundenplanManager extends JavaObject {

	/**
	 * Umrechnung der (Soll) Stunden eines Unterrichts in Minuten.
	 */
	public static readonly FAKTOR_WOCHENSTUNDEN_ZU_MINUTEN : number = 45;

	private static readonly _compAufsichtsbereich : Comparator<StundenplanAufsichtsbereich> = { compare : (a: StundenplanAufsichtsbereich, b: StundenplanAufsichtsbereich) => {
		const result : number = JavaString.compareTo(a.kuerzel, b.kuerzel);
		if (result !== 0)
			return result;
		return JavaLong.compare(a.id, b.id);
	} };

	private static readonly _compFach : Comparator<StundenplanFach> = { compare : (a: StundenplanFach, b: StundenplanFach) => {
		if (a.sortierung < b.sortierung)
			return -1;
		if (a.sortierung > b.sortierung)
			return +1;
		const result : number = JavaString.compareTo(a.kuerzel, b.kuerzel);
		if (result !== 0)
			return result;
		return JavaLong.compare(a.id, b.id);
	} };

	private static readonly _compJahrgang : Comparator<StundenplanJahrgang> = { compare : (a: StundenplanJahrgang, b: StundenplanJahrgang) => {
		const result : number = JavaString.compareTo(a.kuerzel, b.kuerzel);
		if (result !== 0)
			return result;
		return JavaLong.compare(a.id, b.id);
	} };

	private static readonly _compKWZ : Comparator<StundenplanKalenderwochenzuordnung> = { compare : (a: StundenplanKalenderwochenzuordnung, b: StundenplanKalenderwochenzuordnung) => {
		if (a.jahr < b.jahr)
			return -1;
		if (a.jahr > b.jahr)
			return +1;
		if (a.kw < b.kw)
			return -1;
		if (a.kw > b.kw)
			return +1;
		if (a.wochentyp < b.wochentyp)
			return -1;
		if (a.wochentyp > b.wochentyp)
			return +1;
		return JavaLong.compare(a.id, b.id);
	} };

	private readonly _compKlassenunterricht : Comparator<StundenplanKlassenunterricht>;

	private static readonly _compKurs : Comparator<StundenplanKurs> = { compare : (a: StundenplanKurs, b: StundenplanKurs) => {
		if (a.sortierung < b.sortierung)
			return -1;
		if (a.sortierung > b.sortierung)
			return +1;
		const result : number = JavaString.compareTo(a.bezeichnung, b.bezeichnung);
		if (result !== 0)
			return result;
		return JavaLong.compare(a.id, b.id);
	} };

	private static readonly _compLehrer : Comparator<StundenplanLehrer> = { compare : (a: StundenplanLehrer, b: StundenplanLehrer) => {
		const result : number = JavaString.compareTo(a.kuerzel, b.kuerzel);
		if (result !== 0)
			return result;
		return JavaLong.compare(a.id, b.id);
	} };

	private static readonly _compPausenaufsicht : Comparator<StundenplanPausenaufsicht> = { compare : (a: StundenplanPausenaufsicht, b: StundenplanPausenaufsicht) => JavaLong.compare(a.id, b.id) };

	private static readonly _compPausenaufsichtBereich : Comparator<StundenplanPausenaufsichtBereich> = { compare : (a: StundenplanPausenaufsichtBereich, b: StundenplanPausenaufsichtBereich) => JavaLong.compare(a.id, b.id) };

	private static readonly _compPausenzeit : Comparator<StundenplanPausenzeit> = { compare : (a: StundenplanPausenzeit, b: StundenplanPausenzeit) => {
		if (a.wochentag < b.wochentag)
			return -1;
		if (a.wochentag > b.wochentag)
			return +1;
		const beginnA : number = (a.beginn === null) ? -1 : a.beginn;
		const beginnB : number = (b.beginn === null) ? -1 : b.beginn;
		if (beginnA < beginnB)
			return -1;
		if (beginnA > beginnB)
			return +1;
		return JavaLong.compare(a.id, b.id);
	} };

	private static readonly _compRaum : Comparator<StundenplanRaum> = { compare : (a: StundenplanRaum, b: StundenplanRaum) => {
		const result : number = JavaString.compareTo(a.kuerzel, b.kuerzel);
		if (result !== 0)
			return result;
		return JavaLong.compare(a.id, b.id);
	} };

	private static readonly _compSchiene : Comparator<StundenplanSchiene> = { compare : (a: StundenplanSchiene, b: StundenplanSchiene) => {
		if (a.idJahrgang < b.idJahrgang)
			return -1;
		if (a.idJahrgang > b.idJahrgang)
			return +1;
		if (a.nummer < b.nummer)
			return -1;
		if (a.nummer > b.nummer)
			return +1;
		return JavaLong.compare(a.id, b.id);
	} };

	private static readonly _compSchueler : Comparator<StundenplanSchueler> = { compare : (a: StundenplanSchueler, b: StundenplanSchueler) => {
		if (a.idKlasse < b.idKlasse)
			return -1;
		if (a.idKlasse > b.idKlasse)
			return +1;
		const cmpNachname : number = JavaString.compareTo(a.nachname, b.nachname);
		if (cmpNachname !== 0)
			return cmpNachname;
		const cmpVorname : number = JavaString.compareTo(a.vorname, b.vorname);
		if (cmpVorname !== 0)
			return cmpVorname;
		return JavaLong.compare(a.id, b.id);
	} };

	private readonly _compUnterricht : Comparator<StundenplanUnterricht>;

	private readonly _compUnterrichtNachJahrgangKlasseFachWochentyp : Comparator<StundenplanUnterricht>;

	private static readonly _compZeitraster : Comparator<StundenplanZeitraster> = { compare : (a: StundenplanZeitraster, b: StundenplanZeitraster) => {
		if (a.wochentag < b.wochentag)
			return -1;
		if (a.wochentag > b.wochentag)
			return +1;
		if (a.unterrichtstunde < b.unterrichtstunde)
			return -1;
		if (a.unterrichtstunde > b.unterrichtstunde)
			return +1;
		return JavaLong.compare(a.id, b.id);
	} };

	private static readonly _compID : Comparator<number> = { compare : (a: number, b: number) => {
		if (a < b)
			return -1;
		if (a > b)
			return +1;
		return 0;
	} };

	private readonly _aufsichtsbereich_by_id : HashMap<number, StundenplanAufsichtsbereich> = new HashMap<number, StundenplanAufsichtsbereich>();

	private _aufsichtsbereich_by_kuerzel : HashMap<string, StundenplanAufsichtsbereich> = new HashMap<string, StundenplanAufsichtsbereich>();

	private _aufsichtsbereichmenge_sortiert : List<StundenplanAufsichtsbereich> = new ArrayList<StundenplanAufsichtsbereich>();

	private readonly _fach_by_id : HashMap<number, StundenplanFach> = new HashMap<number, StundenplanFach>();

	private _fachmenge_sortiert : List<StundenplanFach> = new ArrayList<StundenplanFach>();

	private _fachmenge_verwendet_sortiert : List<StundenplanFach> = new ArrayList<StundenplanFach>();

	private readonly _jahrgang_by_id : HashMap<number, StundenplanJahrgang> = new HashMap<number, StundenplanJahrgang>();

	private _jahrgangmenge_sortiert : List<StundenplanJahrgang> = new ArrayList<StundenplanJahrgang>();

	private _jahrgangmenge_by_idKurs : HashMap<number, List<StundenplanJahrgang>> = new HashMap<number, List<StundenplanJahrgang>>();

	private _jahrgangmenge_by_idKlasse : HashMap<number, List<StundenplanJahrgang>> = new HashMap<number, List<StundenplanJahrgang>>();

	private _jahrgangmenge_by_idUnterricht : HashMap<number, List<StundenplanJahrgang>> = new HashMap<number, List<StundenplanJahrgang>>();

	private _kwz_by_id : HashMap<number, StundenplanKalenderwochenzuordnung> = new HashMap<number, StundenplanKalenderwochenzuordnung>();

	private _kwzmenge_sortiert_ungueltige : List<StundenplanKalenderwochenzuordnung> = new ArrayList<StundenplanKalenderwochenzuordnung>();

	private _kwzmenge_sortiert_gueltige : List<StundenplanKalenderwochenzuordnung> = new ArrayList<StundenplanKalenderwochenzuordnung>();

	private _kwzmenge_sortiert_alle : List<StundenplanKalenderwochenzuordnung> = new ArrayList<StundenplanKalenderwochenzuordnung>();

	private _kwzmenge_sortiert_alle_invers : List<StundenplanKalenderwochenzuordnung> = new ArrayList<StundenplanKalenderwochenzuordnung>();

	private _kwz_by_jahr_and_kw : HashMap2D<number, number, StundenplanKalenderwochenzuordnung> = new HashMap2D<number, number, StundenplanKalenderwochenzuordnung>();

	private _kwzmenge_by_wochentyp : HashMap<number, List<StundenplanKalenderwochenzuordnung>> = new HashMap<number, List<StundenplanKalenderwochenzuordnung>>();

	private readonly _klasse_by_id : HashMap<number, StundenplanKlasse> = new HashMap<number, StundenplanKlasse>();

	private _klassenmenge_sortiert : List<StundenplanKlasse> = new ArrayList<StundenplanKlasse>();

	private _klassenmenge_verwendet_sortiert : List<StundenplanKlasse> = new ArrayList<StundenplanKlasse>();

	private _klassenmenge_by_idKurs : HashMap<number, List<StundenplanKlasse>> = new HashMap<number, List<StundenplanKlasse>>();

	private _klassenmenge_by_idJahrgang : HashMap<number, List<StundenplanKlasse>> = new HashMap<number, List<StundenplanKlasse>>();

	private _klassenmenge_by_idSchueler : HashMap<number, List<StundenplanKlasse>> = new HashMap<number, List<StundenplanKlasse>>();

	private _klassenmenge_by_idPausenzeit : HashMap<number, List<StundenplanKlasse>> = new HashMap<number, List<StundenplanKlasse>>();

	private _klassenmenge_by_idUnterricht : HashMap<number, List<StundenplanKlasse>> = new HashMap<number, List<StundenplanKlasse>>();

	private readonly _klassenunterricht_by_idKlasse_and_idFach : HashMap2D<number, number, StundenplanKlassenunterricht> = new HashMap2D<number, number, StundenplanKlassenunterricht>();

	private _klassenunterrichtmenge : List<StundenplanKlassenunterricht> = new ArrayList<StundenplanKlassenunterricht>();

	private _klassenunterrichtmenge_by_idKlasse : HashMap<number, List<StundenplanKlassenunterricht>> = new HashMap<number, List<StundenplanKlassenunterricht>>();

	private _klassenunterrichtmenge_by_idKlasse_and_idSchiene : HashMap2D<number, number, List<StundenplanKlassenunterricht>> = new HashMap2D<number, number, List<StundenplanKlassenunterricht>>();

	private _klassenunterrichtmenge_by_idSchueler : HashMap<number, List<StundenplanKlassenunterricht>> = new HashMap<number, List<StundenplanKlassenunterricht>>();

	private _klassenunterrichtmenge_by_idLehrer : HashMap<number, List<StundenplanKlassenunterricht>> = new HashMap<number, List<StundenplanKlassenunterricht>>();

	private _klassenunterrichtmenge_by_idSchiene : HashMap<number, List<StundenplanKlassenunterricht>> = new HashMap<number, List<StundenplanKlassenunterricht>>();

	private readonly _kurs_by_id : HashMap<number, StundenplanKurs> = new HashMap<number, StundenplanKurs>();

	private _kursmenge_sortiert : List<StundenplanKurs> = new ArrayList<StundenplanKurs>();

	private _kursmenge_verwendet_sortiert : List<StundenplanKurs> = new ArrayList<StundenplanKurs>();

	private _kursmenge_by_idSchueler : HashMap<number, List<StundenplanKurs>> = new HashMap<number, List<StundenplanKurs>>();

	private _kursmenge_by_idSchiene : HashMap<number, List<StundenplanKurs>> = new HashMap<number, List<StundenplanKurs>>();

	private _kursmenge_by_idLehrer : HashMap<number, List<StundenplanKurs>> = new HashMap<number, List<StundenplanKurs>>();

	private _kursmenge_by_idKlasse : HashMap<number, List<StundenplanKurs>> = new HashMap<number, List<StundenplanKurs>>();

	private _kursmenge_by_idKlasse_and_idSchiene : HashMap2D<number, number, List<StundenplanKurs>> = new HashMap2D<number, number, List<StundenplanKurs>>();

	private _kursmenge_by_idJahrgang : HashMap<number, List<StundenplanKurs>> = new HashMap<number, List<StundenplanKurs>>();

	private readonly _lehrer_by_id : HashMap<number, StundenplanLehrer> = new HashMap<number, StundenplanLehrer>();

	private _lehrermenge_sortiert : List<StundenplanLehrer> = new ArrayList<StundenplanLehrer>();

	private _lehrermenge_verwendet_sortiert : List<StundenplanLehrer> = new ArrayList<StundenplanLehrer>();

	private _lehrermenge_by_idUnterricht : HashMap<number, List<StundenplanLehrer>> = new HashMap<number, List<StundenplanLehrer>>();

	private _lehrermenge_by_idPausenzeit_and_idAufsichtsbereich_and_Wochentyp : HashMap3D<number, number, number, List<StundenplanLehrer>> = new HashMap3D<number, number, number, List<StundenplanLehrer>>();

	private readonly _pausenaufsicht_by_id : HashMap<number, StundenplanPausenaufsicht> = new HashMap<number, StundenplanPausenaufsicht>();

	private _pausenaufsichtmenge : List<StundenplanPausenaufsicht> = new ArrayList<StundenplanPausenaufsicht>();

	private _pausenaufsichtmenge_by_wochentag : HashMap<number, List<StundenplanPausenaufsicht>> = new HashMap<number, List<StundenplanPausenaufsicht>>();

	private _pausenaufsichtmenge_by_idPausenzeit : HashMap<number, List<StundenplanPausenaufsicht>> = new HashMap<number, List<StundenplanPausenaufsicht>>();

	private _pausenaufsichtmenge_by_idLehrer : HashMap<number, List<StundenplanPausenaufsicht>> = new HashMap<number, List<StundenplanPausenaufsicht>>();

	private _pausenaufsichtmenge_by_idAufsichtsbereich : HashMap<number, List<StundenplanPausenaufsicht>> = new HashMap<number, List<StundenplanPausenaufsicht>>();

	private _pausenaufsichtmenge_by_idKlasse_and_idPausenzeit : HashMap2D<number, number, List<StundenplanPausenaufsicht>> = new HashMap2D<number, number, List<StundenplanPausenaufsicht>>();

	private _pausenaufsichtmenge_by_idLehrer_and_idPausenzeit : HashMap2D<number, number, List<StundenplanPausenaufsicht>> = new HashMap2D<number, number, List<StundenplanPausenaufsicht>>();

	private _pausenaufsichtmenge_by_idSchueler_and_idPausenzeit : HashMap2D<number, number, List<StundenplanPausenaufsicht>> = new HashMap2D<number, number, List<StundenplanPausenaufsicht>>();

	private _pausenaufsichtmenge_by_idJahrgang_and_idPausenzeit : HashMap2D<number, number, List<StundenplanPausenaufsicht>> = new HashMap2D<number, number, List<StundenplanPausenaufsicht>>();

	private _pausenaufsichtmenge_by_idPausenzeit_and_idAufsichtsbereich : HashMap2D<number, number, List<StundenplanPausenaufsicht>> = new HashMap2D<number, number, List<StundenplanPausenaufsicht>>();

	private _pausenaufsichtmenge_by_idPausenzeit_and_idAufsichtsbereich_and_Wochentyp : HashMap3D<number, number, number, List<StundenplanPausenaufsicht>> = new HashMap3D<number, number, number, List<StundenplanPausenaufsicht>>();

	private readonly _pausenaufsichtbereich_by_id : HashMap<number, StundenplanPausenaufsichtBereich> = new HashMap<number, StundenplanPausenaufsichtBereich>();

	private _pausenaufsichtbereichmenge : List<StundenplanPausenaufsichtBereich> = new ArrayList<StundenplanPausenaufsichtBereich>();

	private _pausenaufsichtbereichmenge_by_idPausenaufsicht : HashMap<number, List<StundenplanPausenaufsichtBereich>> = new HashMap<number, List<StundenplanPausenaufsichtBereich>>();

	private _pausenaufsichtbereichmenge_by_idAufsichtsbereich : HashMap<number, List<StundenplanPausenaufsichtBereich>> = new HashMap<number, List<StundenplanPausenaufsichtBereich>>();

	private _pausenaufsichtbereichmenge_by_Wochentyp : HashMap<number, List<StundenplanPausenaufsichtBereich>> = new HashMap<number, List<StundenplanPausenaufsichtBereich>>();

	private _pausenaufsichtbereichmenge_by_idPausenaufsicht_and_idAufsichtsbereich : HashMap2D<number, number, List<StundenplanPausenaufsichtBereich>> = new HashMap2D<number, number, List<StundenplanPausenaufsichtBereich>>();

	private _pausenaufsichtbereichmenge_by_idPausenaufsicht_and_Wochentyp : HashMap2D<number, number, List<StundenplanPausenaufsichtBereich>> = new HashMap2D<number, number, List<StundenplanPausenaufsichtBereich>>();

	private _pausenaufsichtbereichmenge_by_idAufsichtsbereich_and_Wochentyp : HashMap2D<number, number, List<StundenplanPausenaufsichtBereich>> = new HashMap2D<number, number, List<StundenplanPausenaufsichtBereich>>();

	private readonly _pausenzeit_by_id : HashMap<number, StundenplanPausenzeit> = new HashMap<number, StundenplanPausenzeit>();

	private _pausenzeit_by_tag_and_beginn_and_ende : HashMap<LongArrayKey, StundenplanPausenzeit> = new HashMap<LongArrayKey, StundenplanPausenzeit>();

	private _pausenzeitmenge_sortiert : List<StundenplanPausenzeit> = new ArrayList<StundenplanPausenzeit>();

	private _pausenzeitmengeOhneLeere_sortiert : List<StundenplanPausenzeit> = new ArrayList<StundenplanPausenzeit>();

	private _pausenzeitmenge_by_wochentag : HashMap<number, List<StundenplanPausenzeit>> = new HashMap<number, List<StundenplanPausenzeit>>();

	private _pausenzeitmenge_by_idKlasse : HashMap<number, List<StundenplanPausenzeit>> = new HashMap<number, List<StundenplanPausenzeit>>();

	private _pausenzeitmenge_by_idSchueler : HashMap<number, List<StundenplanPausenzeit>> = new HashMap<number, List<StundenplanPausenzeit>>();

	private _pausenzeitmenge_by_idLehrer : HashMap<number, List<StundenplanPausenzeit>> = new HashMap<number, List<StundenplanPausenzeit>>();

	private _pausenzeitmenge_by_idJahrgang : HashMap<number, List<StundenplanPausenzeit>> = new HashMap<number, List<StundenplanPausenzeit>>();

	private _pausenzeitmenge_by_idKlasse_and_wochentag : HashMap2D<number, number, List<StundenplanPausenzeit>> = new HashMap2D<number, number, List<StundenplanPausenzeit>>();

	private _pausenzeitmenge_by_idLehrer_and_wochentag : HashMap2D<number, number, List<StundenplanPausenzeit>> = new HashMap2D<number, number, List<StundenplanPausenzeit>>();

	private _pausenzeitmenge_by_idSchueler_and_wochentag : HashMap2D<number, number, List<StundenplanPausenzeit>> = new HashMap2D<number, number, List<StundenplanPausenzeit>>();

	private _pausenzeitmenge_by_idJahrgang_and_wochentag : HashMap2D<number, number, List<StundenplanPausenzeit>> = new HashMap2D<number, number, List<StundenplanPausenzeit>>();

	private _pausenzeitHatSchnittMitZeitraster_by_wochentag : HashMap<number, boolean> = new HashMap<number, boolean>();

	private _pausenzeitHatSchnittMitZeitraster_by_wochentag_idKlasse : HashMap2D<number, number, boolean> = new HashMap2D<number, number, boolean>();

	private _pausenzeitHatSchnittMitZeitraster_by_wochentag_idLehrer : HashMap2D<number, number, boolean> = new HashMap2D<number, number, boolean>();

	private _pausenzeitHatSchnittMitZeitraster_by_wochentag_idSchueler : HashMap2D<number, number, boolean> = new HashMap2D<number, number, boolean>();

	private _pausenzeitHatSchnittMitZeitraster_by_wochentag_idJahrgang : HashMap2D<number, number, boolean> = new HashMap2D<number, number, boolean>();

	private _pausenzeitMinutenMin : number | null = null;

	private _pausenzeitMinutenMax : number | null = null;

	private _pausenzeitMinutenMinOhneLeere : number | null = null;

	private _pausenzeitMinutenMaxOhneLeere : number | null = null;

	private _pausenzeitWochentagMin : number = Wochentag.MONTAG.id;

	private _pausenzeitWochentagMax : number = Wochentag.MONTAG.id;

	private _pausenzeitWochentageAlsEnumRange : Array<Wochentag> = [Wochentag.MONTAG];

	private readonly _raum_by_id : HashMap<number, StundenplanRaum> = new HashMap<number, StundenplanRaum>();

	private _raum_by_kuerzel : HashMap<string, StundenplanRaum> = new HashMap<string, StundenplanRaum>();

	private _raummenge_sortiert : List<StundenplanRaum> = new ArrayList<StundenplanRaum>();

	private _raummenge_verwendet_sortiert : List<StundenplanRaum> = new ArrayList<StundenplanRaum>();

	private readonly _schiene_by_id : HashMap<number, StundenplanSchiene> = new HashMap<number, StundenplanSchiene>();

	private _schienenmenge_sortiert : List<StundenplanSchiene> = new ArrayList<StundenplanSchiene>();

	private _schienenmenge_verwendet_sortiert : List<StundenplanSchiene> = new ArrayList<StundenplanSchiene>();

	private _schienenmenge_by_idJahrgang : HashMap<number, List<StundenplanSchiene>> = new HashMap<number, List<StundenplanSchiene>>();

	private _schienenmenge_by_idUnterricht : HashMap<number, List<StundenplanSchiene>> = new HashMap<number, List<StundenplanSchiene>>();

	private _schienenmenge_by_idKlasse : HashMap<number, List<StundenplanSchiene>> = new HashMap<number, List<StundenplanSchiene>>();

	private readonly _schueler_by_id : HashMap<number, StundenplanSchueler> = new HashMap<number, StundenplanSchueler>();

	private _schuelermenge : List<StundenplanSchueler> = new ArrayList<StundenplanSchueler>();

	private _schuelermenge_by_idKlasse : HashMap<number, List<StundenplanSchueler>> = new HashMap<number, List<StundenplanSchueler>>();

	private _schuelermenge_by_idKurs : HashMap<number, List<StundenplanSchueler>> = new HashMap<number, List<StundenplanSchueler>>();

	private _schuelermenge_by_idUnterricht : HashMap<number, List<StundenplanSchueler>> = new HashMap<number, List<StundenplanSchueler>>();

	private readonly _unterricht_by_id : HashMap<number, StundenplanUnterricht> = new HashMap<number, StundenplanUnterricht>();

	private _unterrichtmenge : List<StundenplanUnterricht> = new ArrayList<StundenplanUnterricht>();

	private _unterrichtmenge_ungueltig : List<StundenplanUnterricht> = new ArrayList<StundenplanUnterricht>();

	private _unterrichtmenge_ungueltig_set : HashSet<number> = new HashSet<number>();

	private _unterrichtmenge_by_idFach : HashMap<number, List<StundenplanUnterricht>> = new HashMap<number, List<StundenplanUnterricht>>();

	private _unterrichtmenge_by_idKlasse : HashMap<number, List<StundenplanUnterricht>> = new HashMap<number, List<StundenplanUnterricht>>();

	private _unterrichtmenge_by_idRaum : HashMap<number, List<StundenplanUnterricht>> = new HashMap<number, List<StundenplanUnterricht>>();

	private _unterrichtmenge_by_idLehrer : HashMap<number, List<StundenplanUnterricht>> = new HashMap<number, List<StundenplanUnterricht>>();

	private _unterrichtmenge_by_idSchueler : HashMap<number, List<StundenplanUnterricht>> = new HashMap<number, List<StundenplanUnterricht>>();

	private _unterrichtmenge_by_idSchiene : HashMap<number, List<StundenplanUnterricht>> = new HashMap<number, List<StundenplanUnterricht>>();

	private _unterrichtmenge_by_idKurs : HashMap<number, List<StundenplanUnterricht>> = new HashMap<number, List<StundenplanUnterricht>>();

	private _unterrichtmenge_by_idZeitraster : HashMap<number, List<StundenplanUnterricht>> = new HashMap<number, List<StundenplanUnterricht>>();

	private _unterrichtmenge_by_idJahrgang : HashMap<number, List<StundenplanUnterricht>> = new HashMap<number, List<StundenplanUnterricht>>();

	private _unterrichtmenge_by_idUnterricht : HashMap<number, List<StundenplanUnterricht>> = new HashMap<number, List<StundenplanUnterricht>>();

	private _unterrichtmenge_by_wochentyp : HashMap<number, List<StundenplanUnterricht>> = new HashMap<number, List<StundenplanUnterricht>>();

	private _unterrichtmenge_by_idKlasse_and_idZeitraster : HashMap2D<number, number, List<StundenplanUnterricht>> = new HashMap2D<number, number, List<StundenplanUnterricht>>();

	private _unterrichtmenge_by_idSchueler_and_idZeitraster : HashMap2D<number, number, List<StundenplanUnterricht>> = new HashMap2D<number, number, List<StundenplanUnterricht>>();

	private _unterrichtmenge_by_idLehrer_and_idZeitraster : HashMap2D<number, number, List<StundenplanUnterricht>> = new HashMap2D<number, number, List<StundenplanUnterricht>>();

	private _unterrichtmenge_by_idJahrgang_and_idZeitraster : HashMap2D<number, number, List<StundenplanUnterricht>> = new HashMap2D<number, number, List<StundenplanUnterricht>>();

	private _unterrichtmenge_by_idKlasse_and_idFach : HashMap2D<number, number, List<StundenplanUnterricht>> = new HashMap2D<number, number, List<StundenplanUnterricht>>();

	private _unterrichtmenge_by_idZeitraster_and_wochentyp : HashMap2D<number, number, List<StundenplanUnterricht>> = new HashMap2D<number, number, List<StundenplanUnterricht>>();

	private _unterrichtmenge_by_idZeitraster_wochentyp_fach : HashMap3D<number, number, number, List<StundenplanUnterricht>> = new HashMap3D<number, number, number, List<StundenplanUnterricht>>();

	private _unterrichtmenge_by_idZeitraster_wochentyp_raum : HashMap3D<number, number, number, List<StundenplanUnterricht>> = new HashMap3D<number, number, number, List<StundenplanUnterricht>>();

	private _unterrichtHatMultiWochen : boolean = false;

	private _unterrichtsgruppenMergeable : List<List<StundenplanUnterricht>> = new ArrayList<List<StundenplanUnterricht>>();

	private readonly _zeitraster_by_id : HashMap<number, StundenplanZeitraster> = new HashMap<number, StundenplanZeitraster>();

	private _zeitraster_by_wochentag_and_stunde : HashMap2D<number, number, StundenplanZeitraster> = new HashMap2D<number, number, StundenplanZeitraster>();

	private _zeitrastermenge : List<StundenplanZeitraster> = new ArrayList<StundenplanZeitraster>();

	private _zeitrastermengeOhneLeere_sortiert : List<StundenplanZeitraster> = new ArrayList<StundenplanZeitraster>();

	private _zeitrastermenge_by_wochentag : HashMap<number, List<StundenplanZeitraster>> = new HashMap<number, List<StundenplanZeitraster>>();

	private _zeitrastermenge_by_stunde : HashMap<number, List<StundenplanZeitraster>> = new HashMap<number, List<StundenplanZeitraster>>();

	private _zeitrasterMinutenMin : number | null = null;

	private _zeitrasterMinutenMax : number | null = null;

	private _zeitrasterMinutenMinOhneLeere : number | null = null;

	private _zeitrasterMinutenMaxOhneLeere : number | null = null;

	private _zeitrasterMinutenMinByStunde : HashMap<number, number | null> = new HashMap<number, number | null>();

	private _zeitrasterMinutenMaxByStunde : HashMap<number, number | null> = new HashMap<number, number | null>();

	private _zeitrasterWochentagMin : number = Wochentag.MONTAG.id;

	private _zeitrasterWochentagMax : number = Wochentag.MONTAG.id;

	private _zeitrasterWochentageAlsEnumRange : Array<Wochentag> = [Wochentag.MONTAG];

	private _zeitrasterStundeMin : number = 1;

	private _zeitrasterStundeMax : number = 1;

	private _zeitrasterStundenRange : Array<number> = [1];

	private _zeitrasterStundeMinOhneLeere : number = 1;

	private _zeitrasterStundeMaxOhneLeere : number = 1;

	private _zeitrasterStundenRangeOhneLeere : Array<number> = [1];

	private _wertWochenminuten_by_idKurs : HashMap<number, number> = new HashMap<number, number>();

	private _wertWochenminuten_by_idKlasse_und_idFach : HashMap2D<number, number, number> = new HashMap2D<number, number, number>();

	private _wertPausenaufsichtMinuten_by_idLehrkraft_and_wochentyp : HashMap2D<number, number, number> = new HashMap2D<number, number, number>();

	private _wertPausenaufsichtAnzahl_by_idLehrkraft_and_wochentyp : HashMap2D<number, number, number> = new HashMap2D<number, number, number>();

	private readonly _stundenplanID : number;

	private _stundenplanWochenTypModell : number = 0;

	private readonly _stundenplanSchuljahresAbschnittID : number;

	private readonly _stundenplanSchuljahr : number;

	private readonly _stundenplanAbschnitt : number;

	private readonly _stundenplanGueltigAb : string;

	private readonly _stundenplanGueltigBis : string;

	private readonly _aktiv : boolean;

	private readonly _stundenplanBezeichnung : string;

	private _stundenplanKonfig : StundenplanKonfiguration = new StundenplanKonfiguration();

	private readonly _stundenplanWarnungen : List<string> = new ArrayList<string>();


	/**
	 * Der {@link StundenplanManager} benötigt vier data-Objekte und baut damit eine Datenstruktur für schnelle Zugriffe auf.
	 *
	 * @param daten                  liefert die Grund-Daten des Stundenplanes.
	 * @param unterrichte            liefert die Informationen zu allen {@link StundenplanUnterricht} im Stundenplan. Die Liste darf leer sein.
	 * @param pausenaufsichten       liefert die Informationen zu allen {@link StundenplanPausenaufsicht} im Stundenplan. Die Liste darf leer sein.
	 * @param unterrichtsverteilung  liefert die Informationen zu der Unterrichtsverteilung eines Stundenplans. Darf NULL sein.
	 */
	public constructor(daten : Stundenplan, unterrichte : List<StundenplanUnterricht>, pausenaufsichten : List<StundenplanPausenaufsicht>, unterrichtsverteilung : StundenplanUnterrichtsverteilung | null);

	/**
	 * Dieser Manager baut mit Hilfe des {@link StundenplanKomplett}-Objektes eine Datenstruktur für schnelle Zugriffe auf.
	 *
	 * @param stundenplanKomplett    Beinhaltet alle relevanten Daten für einen Stundenplan.
	 */
	public constructor(stundenplanKomplett : StundenplanKomplett);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0 : Stundenplan | StundenplanKomplett, __param1? : List<StundenplanUnterricht>, __param2? : List<StundenplanPausenaufsicht>, __param3? : StundenplanUnterrichtsverteilung | null) {
		super();
		if (((__param0 !== undefined) && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.svws_nrw.core.data.stundenplan.Stundenplan')))) && ((__param1 !== undefined) && ((__param1 instanceof JavaObject) && (__param1.isTranspiledInstanceOf('java.util.List'))) || (__param1 === null)) && ((__param2 !== undefined) && ((__param2 instanceof JavaObject) && (__param2.isTranspiledInstanceOf('java.util.List'))) || (__param2 === null)) && ((__param3 !== undefined) && ((__param3 instanceof JavaObject) && (__param3.isTranspiledInstanceOf('de.svws_nrw.core.data.stundenplan.StundenplanUnterrichtsverteilung'))) || (__param3 === null))) {
			const daten : Stundenplan = cast_de_svws_nrw_core_data_stundenplan_Stundenplan(__param0);
			const unterrichte : List<StundenplanUnterricht> = cast_java_util_List(__param1);
			const pausenaufsichten : List<StundenplanPausenaufsicht> = cast_java_util_List(__param2);
			const unterrichtsverteilung : StundenplanUnterrichtsverteilung | null = cast_de_svws_nrw_core_data_stundenplan_StundenplanUnterrichtsverteilung(__param3);
			this._compKlassenunterricht = this.klassenunterrichtCreateComparator();
			this._compUnterricht = this.unterrichtCreateComparator();
			this._compUnterrichtNachJahrgangKlasseFachWochentyp = this.unterrichtCreateComparatorNachJahrgangKlasseFachWochentyp();
			this._stundenplanID = daten.id;
			this._stundenplanWochenTypModell = daten.wochenTypModell;
			this._stundenplanSchuljahresAbschnittID = daten.idSchuljahresabschnitt;
			this._stundenplanSchuljahr = daten.schuljahr;
			this._stundenplanAbschnitt = daten.abschnitt;
			this._stundenplanGueltigAb = daten.gueltigAb;
			this._stundenplanGueltigBis = StundenplanManager.init_gueltig_bis(daten.gueltigAb, daten.gueltigBis);
			this._aktiv = daten.aktiv;
			this._stundenplanBezeichnung = daten.bezeichnungStundenplan;
			let uv : StundenplanUnterrichtsverteilung | null = unterrichtsverteilung;
			if (uv === null) {
				uv = new StundenplanUnterrichtsverteilung();
				uv.id = this._stundenplanID;
			}
			DeveloperNotificationException.ifTrue("Die ID des Stundenplans passt nicht zur ID der StundenplanUnterrichtsverteilung.", daten.id !== uv.id);
			this.initAll(daten.kalenderwochenZuordnung, uv.faecher, daten.jahrgaenge, daten.zeitraster, daten.raeume, daten.pausenzeiten, daten.aufsichtsbereiche, uv.lehrer, uv.schueler, daten.schienen, uv.klassen, uv.klassenunterricht, pausenaufsichten, uv.kurse, unterrichte);
		} else if (((__param0 !== undefined) && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.svws_nrw.core.data.stundenplan.StundenplanKomplett')))) && (__param1 === undefined) && (__param2 === undefined) && (__param3 === undefined)) {
			const stundenplanKomplett : StundenplanKomplett = cast_de_svws_nrw_core_data_stundenplan_StundenplanKomplett(__param0);
			this._compKlassenunterricht = this.klassenunterrichtCreateComparator();
			this._compUnterricht = this.unterrichtCreateComparator();
			this._compUnterrichtNachJahrgangKlasseFachWochentyp = this.unterrichtCreateComparatorNachJahrgangKlasseFachWochentyp();
			this._stundenplanID = stundenplanKomplett.daten.id;
			this._stundenplanWochenTypModell = stundenplanKomplett.daten.wochenTypModell;
			this._stundenplanSchuljahresAbschnittID = stundenplanKomplett.daten.idSchuljahresabschnitt;
			this._stundenplanSchuljahr = stundenplanKomplett.daten.schuljahr;
			this._stundenplanAbschnitt = stundenplanKomplett.daten.abschnitt;
			this._stundenplanGueltigAb = stundenplanKomplett.daten.gueltigAb;
			this._stundenplanGueltigBis = StundenplanManager.init_gueltig_bis(stundenplanKomplett.daten.gueltigAb, stundenplanKomplett.daten.gueltigBis);
			this._aktiv = stundenplanKomplett.daten.aktiv;
			this._stundenplanBezeichnung = stundenplanKomplett.daten.bezeichnungStundenplan;
			DeveloperNotificationException.ifTrue("Die ID des Stundenplans passt nicht zur ID der StundenplanUnterrichtsverteilung.", stundenplanKomplett.daten.id !== stundenplanKomplett.unterrichtsverteilung.id);
			this.initAll(stundenplanKomplett.daten.kalenderwochenZuordnung, stundenplanKomplett.unterrichtsverteilung.faecher, stundenplanKomplett.daten.jahrgaenge, stundenplanKomplett.daten.zeitraster, stundenplanKomplett.daten.raeume, stundenplanKomplett.daten.pausenzeiten, stundenplanKomplett.daten.aufsichtsbereiche, stundenplanKomplett.unterrichtsverteilung.lehrer, stundenplanKomplett.unterrichtsverteilung.schueler, stundenplanKomplett.daten.schienen, stundenplanKomplett.unterrichtsverteilung.klassen, stundenplanKomplett.unterrichtsverteilung.klassenunterricht, stundenplanKomplett.pausenaufsichten, stundenplanKomplett.unterrichtsverteilung.kurse, stundenplanKomplett.unterrichte);
		} else throw new Error('invalid method overload');
	}

	private compareKlassenlistenIDsNachJahrgang(klassen1 : List<number>, klassen2 : List<number>) : number {
		const aJahrgang : StundenplanJahrgang | null = this.jahrgangGetMinimumByKlassenIDs(klassen1);
		const bJahrgang : StundenplanJahrgang | null = this.jahrgangGetMinimumByKlassenIDs(klassen2);
		if ((aJahrgang !== null) || (bJahrgang !== null)) {
			if (aJahrgang === null)
				return -1;
			if (bJahrgang === null)
				return +1;
			const cmpJahrgang : number = StundenplanManager._compJahrgang.compare(aJahrgang, bJahrgang);
			if (cmpJahrgang !== 0)
				return cmpJahrgang;
		}
		return 0;
	}

	private compareKlassenlistenIDsNachStandard(a : List<number>, b : List<number>) : number {
		if (a.size() < b.size())
			return -1;
		if (a.size() > b.size())
			return +1;
		for (let i : number = 0; i < a.size(); i++) {
			const aIdKlasse : number = ListUtils.getNonNullElementAtOrException(a, i).valueOf();
			const bIdKlasse : number = ListUtils.getNonNullElementAtOrException(b, i).valueOf();
			const aKlasse : StundenplanKlasse = DeveloperNotificationException.ifMapGetIsNull(this._klasse_by_id, aIdKlasse);
			const bKlasse : StundenplanKlasse = DeveloperNotificationException.ifMapGetIsNull(this._klasse_by_id, bIdKlasse);
			const cmpKlasse : number = StundenplanUnterrichtUtils.comparatorKlassen.compare(aKlasse, bKlasse);
			if (cmpKlasse !== 0)
				return cmpKlasse;
		}
		return 0;
	}

	private static compareWochentyp(wochentyp1 : number, wochentyp2 : number) : number {
		if (wochentyp1 < wochentyp2)
			return -1;
		if (wochentyp1 > wochentyp2)
			return +1;
		return 0;
	}

	private static init_gueltig_bis(gueltigAb : string, gueltigBis : string | null) : string {
		const infoVon : Array<number> = DateUtils.extractFromDateISO8601(gueltigAb);
		if (gueltigBis !== null) {
			try {
				DateUtils.extractFromDateISO8601(gueltigBis);
				return gueltigBis;
			} catch(ex : any) {
				// empty block
			}
		}
		const jahrAb : number = infoVon[0];
		const monatAb : number = infoVon[1];
		return ((monatAb <= 7) ? jahrAb : (jahrAb + 1)) + "-07-31";
	}

	private initAll(listKWZ : List<StundenplanKalenderwochenzuordnung>, listFach : List<StundenplanFach>, listJahrgang : List<StundenplanJahrgang>, listZeitraster : List<StundenplanZeitraster>, listRaum : List<StundenplanRaum>, listPausenzeit : List<StundenplanPausenzeit>, listAufsichtsbereich : List<StundenplanAufsichtsbereich>, listLehrer : List<StundenplanLehrer>, listSchueler : List<StundenplanSchueler>, listSchiene : List<StundenplanSchiene>, listKlasse : List<StundenplanKlasse>, listKlassenunterricht : List<StundenplanKlassenunterricht>, listPausenaufsicht : List<StundenplanPausenaufsicht>, listKurs : List<StundenplanKurs>, listUnterricht : List<StundenplanUnterricht>) : void {
		DeveloperNotificationException.ifTrue("Das Wochentypmodell des Stundenplanes darf nicht negativ sein!", this._stundenplanWochenTypModell < 0);
		DeveloperNotificationException.ifTrue("Das Wochentypmodell des Stundenplanes darf nicht 1 sein!", this._stundenplanWochenTypModell === 1);
		this.kalenderwochenzuordnungAddAllOhneUpdate(listKWZ);
		this.fachAddAllOhneUpdate(listFach);
		this.jahrgangAddAllOhneUpdate(listJahrgang);
		this.zeitrasterAddAllOhneUpdate(listZeitraster);
		this.raumAddAllOhneUpdate(listRaum);
		this.pausenzeitAddAllOhneUpdate(listPausenzeit);
		this.aufsichtsbereichAddAllOhneUpdate(listAufsichtsbereich);
		this.lehrerAddAllOhneUpdate(listLehrer);
		this.schuelerAddAllOhneUpdate(listSchueler);
		this.klasseAddAllOhneUpdate(listKlasse);
		this.schieneAddAllOhneUpdate(listSchiene);
		this.klassenunterrichtAddAllOhneUpdate(listKlassenunterricht);
		this.pausenaufsichtAddAllOhneUpdate(listPausenaufsicht);
		this.kursAddAllOhneUpdate(listKurs);
		this.unterrichtAddAllOhneUpdate(listUnterricht);
		this.update_all();
	}

	private update_all() : void {
		this.update_unterrichtmenge_ungueltig();
		this.update_kwzmenge_update_kwz_by_jahr_and_kw();
		this.update_aufsichtsbereichmenge();
		this.update_fachmenge();
		this.update_jahrgangmenge();
		this.update_klassenmenge();
		this.update_klassenunterrichtmenge();
		this.update_kursmenge();
		this.update_lehrermenge();
		this.update_pausenaufsichtmenge();
		this.update_pausenaufsichtbereichmenge();
		this.update_raummenge();
		this.update_schienenmenge();
		this.update_schuelermenge();
		this.update_pausenzeitmenge();
		this.update_unterrichtmenge();
		this.update_zeitrastermenge();
		this.update_pausenaufsichtmenge_by_idPausenzeit();
		this.update_pausenzeitmengeOhnePausenaufsicht();
		this.update_unterrichtmenge_by_idZeitraster();
		this.update_zeitrastermengeOhneLeereUnterrichtmenge();
		this.update_pausenzeit_by_tag_and_beginn_and_ende();
		this.update_aufsichtsbereich_by_kuerzel();
		this.update_raum_by_kuerzel();
		this.update_klassenmenge_by_idJahrgang();
		this.update_jahrgangmenge_by_idKlasse();
		this.update_klassenunterrichtmenge_by_idKlasse();
		this.update_klassenunterrichtmenge_by_idSchueler();
		this.update_klassenunterrichtmenge_by_idLehrer();
		this.update_klassenunterrichtmenge_by_idSchiene();
		this.update_kursmenge_by_idSchueler();
		this.update_kursmenge_by_idLehrer();
		this.update_kursmenge_by_idSchiene();
		this.update_schuelermenge_by_idKurs();
		this.update_kursmenge_by_idJahrgang();
		this.update_jahrgangmenge_by_idKurs();
		this.update_pausenaufsichtmenge_by_wochentag();
		this.update_pausenaufsichtmenge_by_idLehrer();
		this.update_pausenaufsichtmenge_by_idLehrer_and_idPausenzeit();
		this.update_pausenaufsichtmenge_by_idAufsichtsbereich();
		this.update_pausenaufsichtbereichmenge_by_idPausenaufsicht();
		this.update_pausenaufsichtbereichmenge_by_idAufsichtsbereich();
		this.update_pausenaufsichtbereichmenge_by_Wochentyp();
		this.update_pausenaufsichtbereichmenge_by_idPausenaufsicht_and_idAufsichtsbereich();
		this.update_pausenaufsichtbereichmenge_by_idPausenaufsicht_and_Wochentyp();
		this.update_pausenaufsichtbereichmenge_by_idAufsichtsbereich_and_Wochentyp();
		this.update_pausenzeitmenge_by_idLehrer();
		this.update_pausenzeitmenge_by_wochentag();
		this.update_klassenmenge_by_idPausenzeit();
		this.update_schienenmenge_by_idJahrgang();
		this.update_schuelermenge_by_idKlasse();
		this.update_klassenmenge_by_idSchueler();
		this.update_lehrermenge_by_idUnterricht();
		this.update_schienenmenge_by_idUnterricht();
		this.update_unterrichtmenge_by_idSchiene();
		this.update_unterrichtmenge_by_idKurs();
		this.update_unterrichtmenge_by_idKlasse_and_idFach();
		this.update_unterrichtmenge_by_idLehrer();
		this.update_unterrichtmenge_by_idLehrer_and_idZeitraster();
		this.update_unterrichtmenge_by_idRaum();
		this.update_unterrichtmenge_by_idFach();
		this.update_unterrichtmenge_by_wochentyp();
		this.update_zeitraster_by_wochentag_and_stunde();
		this.update_zeitrastermenge_by_wochentag();
		this.update_zeitrastermenge_by_stunde();
		this.update_wertPausenaufsichtMinuten_by_idLehrkraft_and_wochentyp();
		this.update_wertPausenaufsichtAnzahl_by_idLehrkraft_and_wochentyp();
		this.update_kwzmenge_by_wochentyp();
		this.update_kursmenge_by_idKlasse();
		this.update_klassenmenge_by_idKurs();
		this.update_pausenzeitmenge_by_idLehrer_and_wochentag();
		this.update_pausenzeitmenge_by_idKlasse();
		this.update_pausenzeitmenge_by_idJahrgang();
		this.update_pausenzeitmenge_by_idSchueler();
		this.update_pausenaufsichtmenge_by_idKlasse_and_idPausenzeit();
		this.update_pausenaufsichtmenge_by_idJahrgang_and_idPausenzeit();
		this.update_pausenaufsichtmenge_by_idSchueler_and_idPausenzeit();
		this.update_pausenaufsichtmenge_by_idPausenzeit_and_idAufsichtsbereich();
		this.update_pausenaufsichtmenge_by_idPausenzeit_and_idAufsichtsbereich_and_Wochentyp();
		this.update_unterrichtmenge_by_idJahrgang();
		this.update_unterrichtmenge_by_idSchueler();
		this.update_jahrgangmenge_by_idUnterricht();
		this.update_klassenunterrichtmenge_by_idKlasse_and_idSchiene();
		this.update_wertWochenminuten_by_idKurs();
		this.update_wertWochenminuten_by_idKlasse_und_idFach();
		this.update_unterrichtmenge_by_idUnterricht();
		this.update_unterrichtsgruppenMergeable();
		this.update_raummenge_verwendet_sortiert();
		this.update_schienenmenge_verwendet_sortiert();
		this.update_lehrermenge_verwendet_sortiert();
		this.update_kursmenge_verwendet_sortiert();
		this.update_fachmenge_verwendet_sortiert();
		this.update_schuelermenge_by_idUnterricht();
		this.update_pausenzeitmenge_by_idKlasse_and_wochentag();
		this.update_pausenzeitmenge_by_idJahrgang_and_wochentag();
		this.update_pausenzeitmenge_by_idSchueler_and_wochentag();
		this.update_unterrichtmenge_by_idKlasse();
		this.update_unterrichtmenge_by_idKlasse_and_idZeitraster();
		this.update_klassenmenge_by_idUnterricht();
		this.update_unterrichtmenge_by_idJahrgang_and_idZeitraster();
		this.update_unterrichtmenge_by_idSchueler_and_idZeitraster();
		this.update_schienenmenge_by_idKlasse();
		this.update_kursmenge_by_idKlasse_and_idSchiene();
		this.update_lehrermenge_by_idPausenzeit_and_idAufsichtsbereich_and_Wochentyp();
		this.update_unterrichtmenge_by_idZeitraster_and_wochentyp();
		this.update_klassenmenge_verwendet_sortiert();
		this.update_pausenzeitHatSchnittMitZeitraster_by_wochentag_idKlasse();
		this.update_pausenzeitHatSchnittMitZeitraster_by_wochentag_idLehrer();
		this.update_pausenzeitHatSchnittMitZeitraster_by_wochentag_idSchueler();
		this.update_pausenzeitHatSchnittMitZeitraster_by_wochentag_idJahrgang();
		this.update_pausenzeitHatSchnittMitZeitraster_by_wochentag();
		this.update_unterrichtmenge_by_idZeitraster_wochentyp_fach();
		this.update_unterrichtmenge_by_idZeitraster_wochentyp_raum();
	}

	/**
	 * Es kann passieren, dass {@link StundenplanUnterricht}-Objekte existieren, aber nicht die zugehörigen
	 * {@link StundenplanKlassenunterricht}-Objekte, dann werden die {@link StundenplanUnterricht}-Objekte verschoben in eine Liste.
	 */
	private update_unterrichtmenge_ungueltig() : void {
		for (const u of this._unterrichtmenge_ungueltig)
			this._unterricht_by_id.put(u.id, u);
		this._unterrichtmenge_ungueltig = new ArrayList();
		this._unterrichtmenge_ungueltig_set = new HashSet();
		for (const u of new ArrayList(this._unterricht_by_id.values())) {
			if (u.idKurs !== null)
				continue;
			let ungueltig : boolean = false;
			for (const idKlasse of u.klassen)
				ungueltig = ungueltig || !this._klassenunterricht_by_idKlasse_and_idFach.contains(idKlasse, u.idFach);
			if (ungueltig) {
				this._unterricht_by_id.remove(u.id);
				this._unterrichtmenge_ungueltig.add(u);
				this._unterrichtmenge_ungueltig_set.add(u.id);
			}
		}
	}

	private update_pausenzeitHatSchnittMitZeitraster_by_wochentag() : void {
		this._pausenzeitHatSchnittMitZeitraster_by_wochentag = new HashMap();
		for (const wochentag of this._pausenzeitmenge_by_wochentag.keySet()) {
			const listP : List<StundenplanPausenzeit> | null = MapUtils.getOrCreateArrayList(this._pausenzeitmenge_by_wochentag, wochentag);
			const listZ : List<StundenplanZeitraster> | null = MapUtils.getOrCreateArrayList(this._zeitrastermenge_by_wochentag, wochentag);
			const schnitt : boolean = this.hatSchnitt(listP, listZ);
			this._pausenzeitHatSchnittMitZeitraster_by_wochentag.put(wochentag, schnitt);
		}
	}

	private update_pausenzeitHatSchnittMitZeitraster_by_wochentag_idJahrgang() : void {
		this._pausenzeitHatSchnittMitZeitraster_by_wochentag_idJahrgang = new HashMap2D();
		for (const idJahrgang of this._pausenzeitmenge_by_idJahrgang_and_wochentag.getKeySet())
			for (const wochentag of this._pausenzeitmenge_by_idJahrgang_and_wochentag.getKeySetOf(idJahrgang)) {
				const listP : List<StundenplanPausenzeit> | null = Map2DUtils.getOrCreateArrayList(this._pausenzeitmenge_by_idJahrgang_and_wochentag, idJahrgang, wochentag);
				const listZ : List<StundenplanZeitraster> | null = MapUtils.getOrCreateArrayList(this._zeitrastermenge_by_wochentag, wochentag);
				const schnitt : boolean = this.hatSchnitt(listP, listZ);
				this._pausenzeitHatSchnittMitZeitraster_by_wochentag_idJahrgang.put(wochentag, idJahrgang, schnitt);
			}
	}

	private update_pausenzeitHatSchnittMitZeitraster_by_wochentag_idSchueler() : void {
		this._pausenzeitHatSchnittMitZeitraster_by_wochentag_idSchueler = new HashMap2D();
		for (const idSchueler of this._pausenzeitmenge_by_idSchueler_and_wochentag.getKeySet())
			for (const wochentag of this._pausenzeitmenge_by_idSchueler_and_wochentag.getKeySetOf(idSchueler)) {
				const listP : List<StundenplanPausenzeit> | null = Map2DUtils.getOrCreateArrayList(this._pausenzeitmenge_by_idSchueler_and_wochentag, idSchueler, wochentag);
				const listZ : List<StundenplanZeitraster> | null = MapUtils.getOrCreateArrayList(this._zeitrastermenge_by_wochentag, wochentag);
				const schnitt : boolean = this.hatSchnitt(listP, listZ);
				this._pausenzeitHatSchnittMitZeitraster_by_wochentag_idSchueler.put(wochentag, idSchueler, schnitt);
			}
	}

	private update_pausenzeitHatSchnittMitZeitraster_by_wochentag_idLehrer() : void {
		this._pausenzeitHatSchnittMitZeitraster_by_wochentag_idLehrer = new HashMap2D();
		for (const idLehrer of this._pausenzeitmenge_by_idLehrer_and_wochentag.getKeySet())
			for (const wochentag of this._pausenzeitmenge_by_idLehrer_and_wochentag.getKeySetOf(idLehrer)) {
				const listP : List<StundenplanPausenzeit> | null = Map2DUtils.getOrCreateArrayList(this._pausenzeitmenge_by_idLehrer_and_wochentag, idLehrer, wochentag);
				const listZ : List<StundenplanZeitraster> | null = MapUtils.getOrCreateArrayList(this._zeitrastermenge_by_wochentag, wochentag);
				const schnitt : boolean = this.hatSchnitt(listP, listZ);
				this._pausenzeitHatSchnittMitZeitraster_by_wochentag_idLehrer.put(wochentag, idLehrer, schnitt);
			}
	}

	private update_pausenzeitHatSchnittMitZeitraster_by_wochentag_idKlasse() : void {
		this._pausenzeitHatSchnittMitZeitraster_by_wochentag_idKlasse = new HashMap2D();
		for (const idKlasse of this._pausenzeitmenge_by_idKlasse_and_wochentag.getKeySet())
			for (const wochentag of this._pausenzeitmenge_by_idKlasse_and_wochentag.getKeySetOf(idKlasse)) {
				const listP : List<StundenplanPausenzeit> | null = Map2DUtils.getOrCreateArrayList(this._pausenzeitmenge_by_idKlasse_and_wochentag, idKlasse, wochentag);
				const listZ : List<StundenplanZeitraster> | null = MapUtils.getOrCreateArrayList(this._zeitrastermenge_by_wochentag, wochentag);
				const schnitt : boolean = this.hatSchnitt(listP, listZ);
				this._pausenzeitHatSchnittMitZeitraster_by_wochentag_idKlasse.put(wochentag, idKlasse, schnitt);
			}
	}

	private hatSchnitt(pausenzeiten : List<StundenplanPausenzeit>, zeitraster : List<StundenplanZeitraster>) : boolean {
		for (const p of pausenzeiten) {
			if (p.beginn === null)
				continue;
			if (p.ende === null)
				continue;
			for (const z of zeitraster) {
				if (z.stundenbeginn === null)
					continue;
				if (z.stundenende === null)
					continue;
				if (this.zeitrasterGetSchneidenSich(p.beginn, p.ende, z.stundenbeginn, z.stundenende))
					return true;
			}
		}
		return false;
	}

	private update_lehrermenge_verwendet_sortiert() : void {
		this._lehrermenge_verwendet_sortiert = new ArrayList();
		for (const lehrer of this._lehrermenge_sortiert)
			if (!MapUtils.getOrCreateArrayList(this._unterrichtmenge_by_idLehrer, lehrer.id).isEmpty())
				this._lehrermenge_verwendet_sortiert.add(lehrer);
	}

	private update_kursmenge_verwendet_sortiert() : void {
		this._kursmenge_verwendet_sortiert = new ArrayList();
		for (const kurs of this._kursmenge_sortiert)
			if (!MapUtils.getOrCreateArrayList(this._unterrichtmenge_by_idKurs, kurs.id).isEmpty())
				this._kursmenge_verwendet_sortiert.add(kurs);
	}

	private update_fachmenge_verwendet_sortiert() : void {
		this._fachmenge_verwendet_sortiert = new ArrayList();
		for (const fach of this._fachmenge_sortiert)
			if (!MapUtils.getOrCreateArrayList(this._unterrichtmenge_by_idFach, fach.id).isEmpty())
				this._fachmenge_verwendet_sortiert.add(fach);
	}

	private update_schuelermenge_by_idUnterricht() : void {
		this._schuelermenge_by_idUnterricht = new HashMap();
		for (const u of this._unterrichtmenge) {
			if (u.idKurs === null) {
				for (const idKlasse of u.klassen) {
					const susDerKlasse : List<StundenplanSchueler> = MapUtils.getOrCreateArrayList(this._schuelermenge_by_idKlasse, idKlasse);
					MapUtils.getOrCreateArrayList(this._schuelermenge_by_idUnterricht, u.id).addAll(susDerKlasse);
				}
			} else {
				const susDesKurses : List<StundenplanSchueler> = MapUtils.getOrCreateArrayList(this._schuelermenge_by_idKurs, u.idKurs);
				MapUtils.getOrCreateArrayList(this._schuelermenge_by_idUnterricht, u.id).addAll(susDesKurses);
			}
			MapUtils.getOrCreateArrayList(this._schuelermenge_by_idUnterricht, u.id).sort(StundenplanManager._compSchueler);
		}
	}

	private update_unterrichtmenge_by_idZeitraster_wochentyp_fach() : void {
		this._unterrichtmenge_by_idZeitraster_wochentyp_fach = new HashMap3D();
		for (const idZ of this._unterrichtmenge_by_idZeitraster_and_wochentyp.getKeySet())
			for (const wt of this._unterrichtmenge_by_idZeitraster_and_wochentyp.getKeySetOf(idZ))
				for (const u of Map2DUtils.getOrCreateArrayList(this._unterrichtmenge_by_idZeitraster_and_wochentyp, idZ, wt))
					if (u.idFach >= 0)
						Map3DUtils.addToList(this._unterrichtmenge_by_idZeitraster_wochentyp_fach, idZ, wt, u.idFach, u);
	}

	private update_unterrichtmenge_by_idZeitraster_wochentyp_raum() : void {
		this._unterrichtmenge_by_idZeitraster_wochentyp_raum = new HashMap3D();
		for (const idZ of this._unterrichtmenge_by_idZeitraster_and_wochentyp.getKeySet())
			for (const wt of this._unterrichtmenge_by_idZeitraster_and_wochentyp.getKeySetOf(idZ))
				for (const u of Map2DUtils.getOrCreateArrayList(this._unterrichtmenge_by_idZeitraster_and_wochentyp, idZ, wt))
					for (const idR of u.raeume)
						Map3DUtils.addToList(this._unterrichtmenge_by_idZeitraster_wochentyp_raum, idZ, wt, idR, u);
	}

	private update_pausenzeit_by_tag_and_beginn_and_ende() : void {
		this._pausenzeit_by_tag_and_beginn_and_ende = new HashMap();
		for (const pausenzeit of this._pausenzeitmenge_sortiert) {
			const beginn : number = (pausenzeit.beginn === null) ? -1 : pausenzeit.beginn;
			const ende : number = (pausenzeit.ende === null) ? -1 : pausenzeit.ende;
			const key : LongArrayKey = new LongArrayKey([pausenzeit.wochentag, beginn, ende]);
			this._pausenzeit_by_tag_and_beginn_and_ende.put(key, pausenzeit);
		}
	}

	private update_aufsichtsbereich_by_kuerzel() : void {
		this._aufsichtsbereich_by_kuerzel = new HashMap();
		for (const aufsichtsbereich of this._aufsichtsbereichmenge_sortiert)
			this._aufsichtsbereich_by_kuerzel.put(aufsichtsbereich.kuerzel, aufsichtsbereich);
	}

	private update_raum_by_kuerzel() : void {
		this._raum_by_kuerzel = new HashMap();
		for (const raum of this._raummenge_sortiert)
			this._raum_by_kuerzel.put(raum.kuerzel, raum);
	}

	private update_unterrichtmenge_by_idUnterricht() : void {
		this._unterrichtmenge_by_idUnterricht = new HashMap();
		for (const menge of this._unterrichtmenge_by_idKurs.values())
			for (const u of menge)
				DeveloperNotificationException.ifMapPutOverwrites(this._unterrichtmenge_by_idUnterricht, u.id, menge);
		for (const menge of this._unterrichtmenge_by_idKlasse_and_idFach.getNonNullValuesAsList())
			for (const u of menge)
				DeveloperNotificationException.ifMapPutOverwrites(this._unterrichtmenge_by_idUnterricht, u.id, menge);
	}

	private update_unterrichtsgruppenMergeable() : void {
		this._unterrichtsgruppenMergeable = new ArrayList();
		if (this._stundenplanWochenTypModell < 2)
			return;
		for (const menge of this._unterrichtmenge_by_idKurs.values())
			this.update_unterrichtsgruppenMergeableHelper1(menge);
		for (const menge of this._unterrichtmenge_by_idKlasse_and_idFach.getNonNullValuesAsList())
			this.update_unterrichtsgruppenMergeableHelper1(menge);
	}

	private update_raummenge_verwendet_sortiert() : void {
		this._raummenge_verwendet_sortiert = new ArrayList();
		for (const raum of this._raummenge_sortiert)
			if (!MapUtils.getOrCreateArrayList(this._unterrichtmenge_by_idRaum, raum.id).isEmpty())
				this._raummenge_verwendet_sortiert.add(raum);
	}

	private update_schienenmenge_verwendet_sortiert() : void {
		this._schienenmenge_verwendet_sortiert = new ArrayList();
		for (const schiene of this._schienenmenge_sortiert)
			if (!MapUtils.getOrCreateArrayList(this._unterrichtmenge_by_idSchiene, schiene.id).isEmpty())
				this._schienenmenge_verwendet_sortiert.add(schiene);
	}

	private update_unterrichtsgruppenMergeableHelper1(menge : List<StundenplanUnterricht>) : void {
		if (menge.size() < this._stundenplanWochenTypModell)
			return;
		const mapProZeitraster : HashMap<number, List<StundenplanUnterricht>> = new HashMap<number, List<StundenplanUnterricht>>();
		for (const u of menge)
			MapUtils.addToList(mapProZeitraster, u.idZeitraster, u);
		for (const mengeProZeitraster of mapProZeitraster.values())
			this.update_unterrichtsgruppenMergeableHelper2(mengeProZeitraster);
	}

	private update_unterrichtsgruppenMergeableHelper2(mengeProZeitraster : List<StundenplanUnterricht>) : void {
		if (mengeProZeitraster.size() < this._stundenplanWochenTypModell)
			return;
		const mapProWochentyp : HashMap<number, StundenplanUnterricht> = new HashMap<number, StundenplanUnterricht>();
		for (const u of mengeProZeitraster)
			mapProWochentyp.put(u.wochentyp, u);
		const gruppe : List<StundenplanUnterricht> = new ArrayList<StundenplanUnterricht>();
		for (let wochentyp : number = 1; wochentyp <= this._stundenplanWochenTypModell; wochentyp++) {
			const u : StundenplanUnterricht | null = mapProWochentyp.get(wochentyp);
			if (u === null)
				return;
			gruppe.add(u);
		}
		this.update_unterrichtsgruppenMergeableHelper3(gruppe);
	}

	private update_unterrichtsgruppenMergeableHelper3(gruppe : List<StundenplanUnterricht>) : void {
		for (let i : number = 1; i < gruppe.size(); i++) {
			const unterrichtA : StundenplanUnterricht = gruppe.get(i - 1);
			const unterrichtB : StundenplanUnterricht = gruppe.get(i);
			if (!JavaObject.equalsTranspiler(unterrichtA.lehrer, (unterrichtB.lehrer)))
				return;
			if (!JavaObject.equalsTranspiler(unterrichtA.klassen, (unterrichtB.klassen)))
				return;
			if (!JavaObject.equalsTranspiler(unterrichtA.raeume, (unterrichtB.raeume)))
				return;
			if (!JavaObject.equalsTranspiler(unterrichtA.schienen, (unterrichtB.schienen)))
				return;
		}
		this._unterrichtsgruppenMergeable.add(gruppe);
	}

	private update_wertWochenminuten_by_idKlasse_und_idFach() : void {
		this._wertWochenminuten_by_idKlasse_und_idFach = new HashMap2D();
		const faktor : number = (this._stundenplanWochenTypModell === 0) ? 1 : this._stundenplanWochenTypModell;
		for (const klasse of this._klassenmenge_sortiert)
			for (const fach of this._fachmenge_sortiert) {
				let summe_minuten : number = 0;
				for (const u of Map2DUtils.getOrCreateArrayList(this._unterrichtmenge_by_idKlasse_and_idFach, klasse.id, fach.id)) {
					const z : StundenplanZeitraster = DeveloperNotificationException.ifMapGetIsNull(this._zeitraster_by_id, u.idZeitraster);
					const ende : number = DeveloperNotificationException.ifNull("z.stundenende", z.stundenende);
					const beginn : number = DeveloperNotificationException.ifNull("z.stundenbeginn", z.stundenbeginn);
					const minuten : number = ende - beginn;
					summe_minuten += (u.wochentyp === 0) ? (minuten * faktor) : minuten;
				}
				const wochenminuten : number = summe_minuten / faktor;
				this._wertWochenminuten_by_idKlasse_und_idFach.put(klasse.id, fach.id, wochenminuten);
			}
	}

	private update_wertWochenminuten_by_idKurs() : void {
		this._wertWochenminuten_by_idKurs = new HashMap();
		const faktor : number = (this._stundenplanWochenTypModell === 0) ? 1 : this._stundenplanWochenTypModell;
		for (const kurs of this._kursmenge_sortiert) {
			let summe_minuten : number = 0;
			for (const u of MapUtils.getOrCreateArrayList(this._unterrichtmenge_by_idKurs, kurs.id)) {
				const z : StundenplanZeitraster = DeveloperNotificationException.ifMapGetIsNull(this._zeitraster_by_id, u.idZeitraster);
				const ende : number = DeveloperNotificationException.ifNull("z.stundenende", z.stundenende);
				const beginn : number = DeveloperNotificationException.ifNull("z.stundenbeginn", z.stundenbeginn);
				const minuten : number = ende - beginn;
				summe_minuten += (u.wochentyp === 0) ? (minuten * faktor) : minuten;
			}
			const wochenminuten : number = summe_minuten / faktor;
			this._wertWochenminuten_by_idKurs.put(kurs.id, wochenminuten);
		}
	}

	private update_schienenmenge_by_idKlasse() : void {
		this._schienenmenge_by_idKlasse = new HashMap();
		for (const klasse of this._klassenmenge_sortiert) {
			const schienenIDs : JavaSet<number> = new HashSet<number>();
			for (const kurs of MapUtils.getOrCreateArrayList(this._kursmenge_by_idKlasse, klasse.id))
				schienenIDs.addAll(kurs.schienen);
			for (const klassenunterricht of MapUtils.getOrCreateArrayList(this._klassenunterrichtmenge_by_idKlasse, klasse.id))
				schienenIDs.addAll(klassenunterricht.schienen);
			for (const idSchiene of schienenIDs) {
				const schiene : StundenplanSchiene = DeveloperNotificationException.ifMapGetIsNull(this._schiene_by_id, idSchiene);
				MapUtils.addToList(this._schienenmenge_by_idKlasse, klasse.id, schiene);
			}
			MapUtils.getOrCreateArrayList(this._schienenmenge_by_idKlasse, klasse.id).sort(StundenplanManager._compSchiene);
		}
	}

	private update_kursmenge_by_idKlasse_and_idSchiene() : void {
		this._kursmenge_by_idKlasse_and_idSchiene = new HashMap2D();
		for (const idKlasse of this._kursmenge_by_idKlasse.keySet())
			for (const kurs of MapUtils.getOrCreateArrayList(this._kursmenge_by_idKlasse, idKlasse))
				if (kurs.schienen.isEmpty()) {
					Map2DUtils.addToList(this._kursmenge_by_idKlasse_and_idSchiene, idKlasse, -1, kurs);
				} else {
					for (const idSchiene of kurs.schienen)
						Map2DUtils.addToList(this._kursmenge_by_idKlasse_and_idSchiene, idKlasse, idSchiene, kurs);
				}
		for (const idKlasse of this._kursmenge_by_idKlasse_and_idSchiene.getKeySet())
			for (const idSchiene of this._kursmenge_by_idKlasse_and_idSchiene.getKeySetOf(idKlasse))
				this._kursmenge_by_idKlasse_and_idSchiene.getOrException(idKlasse, idSchiene).sort(StundenplanManager._compKurs);
	}

	private update_klassenunterrichtmenge_by_idKlasse_and_idSchiene() : void {
		this._klassenunterrichtmenge_by_idKlasse_and_idSchiene = new HashMap2D();
		for (const idKlasse of this._klassenunterrichtmenge_by_idKlasse.keySet())
			for (const klassenunterricht of MapUtils.getOrCreateArrayList(this._klassenunterrichtmenge_by_idKlasse, idKlasse))
				if (klassenunterricht.schienen.isEmpty()) {
					Map2DUtils.addToList(this._klassenunterrichtmenge_by_idKlasse_and_idSchiene, idKlasse, -1, klassenunterricht);
				} else {
					for (const idSchiene of klassenunterricht.schienen)
						Map2DUtils.addToList(this._klassenunterrichtmenge_by_idKlasse_and_idSchiene, idKlasse, idSchiene, klassenunterricht);
				}
		for (const idKlasse of this._klassenunterrichtmenge_by_idKlasse_and_idSchiene.getKeySet())
			for (const idSchiene of this._klassenunterrichtmenge_by_idKlasse_and_idSchiene.getKeySetOf(idKlasse))
				this._klassenunterrichtmenge_by_idKlasse_and_idSchiene.getOrException(idKlasse, idSchiene).sort(this._compKlassenunterricht);
	}

	private update_schienenmenge_by_idUnterricht() : void {
		this._schienenmenge_by_idUnterricht = new HashMap();
		for (const u of this._unterrichtmenge) {
			for (const idSchiene of u.schienen) {
				const schiene : StundenplanSchiene = DeveloperNotificationException.ifMapGetIsNull(this._schiene_by_id, idSchiene);
				MapUtils.addToList(this._schienenmenge_by_idUnterricht, u.id, schiene);
			}
			MapUtils.getOrCreateArrayList(this._schienenmenge_by_idUnterricht, u.id).sort(StundenplanManager._compSchiene);
		}
	}

	private update_klassenmenge_by_idPausenzeit() : void {
		this._klassenmenge_by_idPausenzeit = new HashMap();
		for (const pausenzeit of this._pausenzeitmenge_sortiert)
			if (pausenzeit.klassen.isEmpty()) {
				MapUtils.getOrCreateArrayList(this._klassenmenge_by_idPausenzeit, pausenzeit.id).addAll(this._klassenmenge_sortiert);
			} else {
				for (const idKlasse of pausenzeit.klassen) {
					const klasse : StundenplanKlasse = DeveloperNotificationException.ifMapGetIsNull(this._klasse_by_id, idKlasse);
					MapUtils.addToList(this._klassenmenge_by_idPausenzeit, pausenzeit.id, klasse);
				}
				MapUtils.getOrCreateArrayList(this._klassenmenge_by_idPausenzeit, pausenzeit.id).sort(StundenplanUnterrichtUtils.comparatorKlassen);
			}
	}

	private update_jahrgangmenge_by_idKlasse() : void {
		this._jahrgangmenge_by_idKlasse = new HashMap();
		for (const klasse of this._klassenmenge_sortiert)
			for (const idJahrgang of klasse.jahrgaenge) {
				const jahrgang : StundenplanJahrgang = DeveloperNotificationException.ifMapGetIsNull(this._jahrgang_by_id, idJahrgang);
				MapUtils.addToList(this._jahrgangmenge_by_idKlasse, klasse.id, jahrgang);
			}
	}

	private update_jahrgangmenge_by_idKurs() : void {
		this._jahrgangmenge_by_idKurs = new HashMap();
		for (const kurs of this._kursmenge_sortiert) {
			for (const idJahrgang of kurs.jahrgaenge) {
				const jahrgang : StundenplanJahrgang = DeveloperNotificationException.ifMapGetIsNull(this._jahrgang_by_id, idJahrgang);
				MapUtils.addToList(this._jahrgangmenge_by_idKurs, kurs.id, jahrgang);
			}
			MapUtils.getOrCreateArrayList(this._jahrgangmenge_by_idKurs, kurs.id).sort(StundenplanManager._compJahrgang);
		}
	}

	private update_jahrgangmenge_by_idUnterricht() : void {
		this._jahrgangmenge_by_idUnterricht = new HashMap();
		for (const u of this._unterrichtmenge) {
			const setOfJahrgangIDs : JavaSet<number> = new HashSet<number>();
			if (u.idKurs === null) {
				for (const idKlasse of u.klassen)
					for (const j of DeveloperNotificationException.ifMapGetIsNull(this._jahrgangmenge_by_idKlasse, idKlasse))
						setOfJahrgangIDs.add(j.id);
			} else {
				for (const j of DeveloperNotificationException.ifMapGetIsNull(this._jahrgangmenge_by_idKurs, u.idKurs))
					setOfJahrgangIDs.add(j.id);
			}
			for (const idJahrgang of setOfJahrgangIDs) {
				const j : StundenplanJahrgang = DeveloperNotificationException.ifMapGetIsNull(this._jahrgang_by_id, idJahrgang);
				MapUtils.addToList(this._jahrgangmenge_by_idUnterricht, u.id, j);
			}
			MapUtils.getOrCreateArrayList(this._jahrgangmenge_by_idUnterricht, u.id).sort(StundenplanManager._compJahrgang);
		}
	}

	private update_klassenunterrichtmenge_by_idSchiene() : void {
		this._klassenunterrichtmenge_by_idSchiene = new HashMap();
		for (const klassenunterricht of this._klassenunterrichtmenge)
			for (const idSchiene of klassenunterricht.schienen)
				MapUtils.addToList(this._klassenunterrichtmenge_by_idSchiene, idSchiene, klassenunterricht);
	}

	private update_kursmenge_by_idSchiene() : void {
		this._kursmenge_by_idSchiene = new HashMap();
		for (const kurs of this._kursmenge_sortiert)
			for (const idSchiene of kurs.schienen)
				MapUtils.addToList(this._kursmenge_by_idSchiene, idSchiene, kurs);
	}

	private update_pausenaufsichtmenge_by_idAufsichtsbereich() : void {
		this._pausenaufsichtmenge_by_idAufsichtsbereich = new HashMap();
		const mapCheckDuplicate : HashMap2D<number, number, boolean> = new HashMap2D<number, number, boolean>();
		for (const aufsicht of this._pausenaufsichtmenge) {
			for (const aufsichtsbereich of aufsicht.bereiche) {
				if (!mapCheckDuplicate.contains(aufsichtsbereich.idAufsichtsbereich, aufsicht.id)) {
					MapUtils.addToList(this._pausenaufsichtmenge_by_idAufsichtsbereich, aufsichtsbereich.idAufsichtsbereich, aufsicht);
					mapCheckDuplicate.put(aufsichtsbereich.idAufsichtsbereich, aufsicht.id, true);
				}
			}
		}
	}

	private update_pausenaufsichtmenge() : void {
		this._pausenaufsichtmenge = new ArrayList(this._pausenaufsicht_by_id.values());
		this._pausenaufsichtmenge.sort(StundenplanManager._compPausenaufsicht);
	}

	private update_pausenaufsichtbereichmenge() : void {
		this._pausenaufsichtbereichmenge = new ArrayList(this._pausenaufsichtbereich_by_id.values());
		this._pausenaufsichtbereichmenge.sort(StundenplanManager._compPausenaufsichtBereich);
	}

	private update_pausenaufsichtmenge_by_wochentag() : void {
		this._pausenaufsichtmenge_by_wochentag = new HashMap();
		for (const aufsicht of this._pausenaufsichtmenge) {
			const zeit : StundenplanPausenzeit = DeveloperNotificationException.ifMapGetIsNull(this._pausenzeit_by_id, aufsicht.idPausenzeit);
			MapUtils.addToList(this._pausenaufsichtmenge_by_wochentag, zeit.wochentag, aufsicht);
		}
	}

	private update_pausenaufsichtmenge_by_idPausenzeit() : void {
		this._pausenaufsichtmenge_by_idPausenzeit = new HashMap();
		for (const aufsicht of this._pausenaufsichtmenge)
			MapUtils.addToList(this._pausenaufsichtmenge_by_idPausenzeit, aufsicht.idPausenzeit, aufsicht);
	}

	private update_pausenaufsichtmenge_by_idJahrgang_and_idPausenzeit() : void {
		this._pausenaufsichtmenge_by_idJahrgang_and_idPausenzeit = new HashMap2D();
		for (const aufsicht of this._pausenaufsichtmenge)
			for (const klasse of MapUtils.getOrCreateArrayList(this._klassenmenge_by_idPausenzeit, aufsicht.idPausenzeit))
				for (const jahrgang of MapUtils.getOrCreateArrayList(this._jahrgangmenge_by_idKlasse, klasse.id))
					Map2DUtils.addToListIfNotExists(this._pausenaufsichtmenge_by_idJahrgang_and_idPausenzeit, jahrgang.id, aufsicht.idPausenzeit, aufsicht);
	}

	private update_pausenaufsichtmenge_by_idSchueler_and_idPausenzeit() : void {
		this._pausenaufsichtmenge_by_idSchueler_and_idPausenzeit = new HashMap2D();
		for (const aufsicht of this._pausenaufsichtmenge)
			for (const klasse of MapUtils.getOrCreateArrayList(this._klassenmenge_by_idPausenzeit, aufsicht.idPausenzeit))
				for (const schueler of MapUtils.getOrCreateArrayList(this._schuelermenge_by_idKlasse, klasse.id))
					Map2DUtils.addToList(this._pausenaufsichtmenge_by_idSchueler_and_idPausenzeit, schueler.id, aufsicht.idPausenzeit, aufsicht);
	}

	private update_pausenaufsichtmenge_by_idKlasse_and_idPausenzeit() : void {
		this._pausenaufsichtmenge_by_idKlasse_and_idPausenzeit = new HashMap2D();
		for (const aufsicht of this._pausenaufsichtmenge)
			for (const klasse of MapUtils.getOrCreateArrayList(this._klassenmenge_by_idPausenzeit, aufsicht.idPausenzeit))
				Map2DUtils.addToList(this._pausenaufsichtmenge_by_idKlasse_and_idPausenzeit, klasse.id, aufsicht.idPausenzeit, aufsicht);
	}

	private update_pausenaufsichtmenge_by_idLehrer() : void {
		this._pausenaufsichtmenge_by_idLehrer = new HashMap();
		for (const aufsicht of this._pausenaufsichtmenge)
			if (aufsicht.idLehrer >= 0)
				MapUtils.addToList(this._pausenaufsichtmenge_by_idLehrer, aufsicht.idLehrer, aufsicht);
	}

	private update_pausenaufsichtmenge_by_idLehrer_and_idPausenzeit() : void {
		this._pausenaufsichtmenge_by_idLehrer_and_idPausenzeit = new HashMap2D();
		for (const aufsicht of this._pausenaufsichtmenge)
			if (aufsicht.idLehrer >= 0)
				Map2DUtils.addToList(this._pausenaufsichtmenge_by_idLehrer_and_idPausenzeit, aufsicht.idLehrer, aufsicht.idPausenzeit, aufsicht);
	}

	private update_pausenaufsichtmenge_by_idPausenzeit_and_idAufsichtsbereich() : void {
		this._pausenaufsichtmenge_by_idPausenzeit_and_idAufsichtsbereich = new HashMap2D();
		for (const aufsichtsbereich of this._aufsichtsbereichmenge_sortiert) {
			for (const aufsicht of MapUtils.getOrCreateArrayList(this._pausenaufsichtmenge_by_idAufsichtsbereich, aufsichtsbereich.id)) {
				Map2DUtils.addToList(this._pausenaufsichtmenge_by_idPausenzeit_and_idAufsichtsbereich, aufsicht.idPausenzeit, aufsichtsbereich.id, aufsicht);
			}
		}
	}

	private update_pausenaufsichtmenge_by_idPausenzeit_and_idAufsichtsbereich_and_Wochentyp() : void {
		this._pausenaufsichtmenge_by_idPausenzeit_and_idAufsichtsbereich_and_Wochentyp = new HashMap3D();
		for (const aufsichtsbereich of this._aufsichtsbereichmenge_sortiert)
			for (const aufsicht of MapUtils.getOrCreateArrayList(this._pausenaufsichtmenge_by_idAufsichtsbereich, aufsichtsbereich.id))
				for (const zuordnung of MapUtils.getOrCreateArrayList(this._pausenaufsichtbereichmenge_by_idPausenaufsicht, aufsicht.id))
					if (aufsichtsbereich.id === zuordnung.idAufsichtsbereich)
						Map3DUtils.addToList(this._pausenaufsichtmenge_by_idPausenzeit_and_idAufsichtsbereich_and_Wochentyp, aufsicht.idPausenzeit, aufsichtsbereich.id, zuordnung.wochentyp, aufsicht);
	}

	private update_pausenaufsichtbereichmenge_by_idPausenaufsicht() : void {
		this._pausenaufsichtbereichmenge_by_idPausenaufsicht = new HashMap();
		for (const pausenaufsicht of this._pausenaufsicht_by_id.values())
			MapUtils.getOrCreateArrayList(this._pausenaufsichtbereichmenge_by_idPausenaufsicht, pausenaufsicht.id);
		for (const bereich of this._pausenaufsichtbereich_by_id.values())
			MapUtils.addToList(this._pausenaufsichtbereichmenge_by_idPausenaufsicht, bereich.idPausenaufsicht, bereich);
	}

	private update_pausenaufsichtbereichmenge_by_idAufsichtsbereich() : void {
		this._pausenaufsichtbereichmenge_by_idAufsichtsbereich = new HashMap();
		for (const aufsichtbereich of this._aufsichtsbereich_by_id.values())
			MapUtils.getOrCreateArrayList(this._pausenaufsichtbereichmenge_by_idAufsichtsbereich, aufsichtbereich.id);
		for (const bereich of this._pausenaufsichtbereich_by_id.values())
			MapUtils.addToList(this._pausenaufsichtbereichmenge_by_idAufsichtsbereich, bereich.idAufsichtsbereich, bereich);
	}

	private update_pausenaufsichtbereichmenge_by_Wochentyp() : void {
		this._pausenaufsichtbereichmenge_by_Wochentyp = new HashMap();
		for (let wochentyp : number = 0; wochentyp <= this._stundenplanWochenTypModell; wochentyp++)
			MapUtils.getOrCreateArrayList(this._pausenaufsichtbereichmenge_by_Wochentyp, wochentyp);
		for (const bereich of this._pausenaufsichtbereich_by_id.values())
			MapUtils.addToList(this._pausenaufsichtbereichmenge_by_Wochentyp, bereich.wochentyp, bereich);
	}

	private update_pausenaufsichtbereichmenge_by_idPausenaufsicht_and_idAufsichtsbereich() : void {
		this._pausenaufsichtbereichmenge_by_idPausenaufsicht_and_idAufsichtsbereich = new HashMap2D();
		for (const pausenaufsicht of this._pausenaufsicht_by_id.values())
			for (const aufsichtbereich of this._aufsichtsbereich_by_id.values())
				Map2DUtils.getOrCreateArrayList(this._pausenaufsichtbereichmenge_by_idPausenaufsicht_and_idAufsichtsbereich, pausenaufsicht.id, aufsichtbereich.id);
		for (const bereich of this._pausenaufsichtbereich_by_id.values())
			Map2DUtils.addToList(this._pausenaufsichtbereichmenge_by_idPausenaufsicht_and_idAufsichtsbereich, bereich.idPausenaufsicht, bereich.idAufsichtsbereich, bereich);
	}

	private update_pausenaufsichtbereichmenge_by_idPausenaufsicht_and_Wochentyp() : void {
		this._pausenaufsichtbereichmenge_by_idPausenaufsicht_and_Wochentyp = new HashMap2D();
		for (const pausenaufsicht of this._pausenaufsicht_by_id.values())
			for (let wochentyp : number = 0; wochentyp <= this._stundenplanWochenTypModell; wochentyp++)
				Map2DUtils.getOrCreateArrayList(this._pausenaufsichtbereichmenge_by_idPausenaufsicht_and_Wochentyp, pausenaufsicht.id, wochentyp);
		for (const bereich of this._pausenaufsichtbereich_by_id.values())
			Map2DUtils.addToList(this._pausenaufsichtbereichmenge_by_idPausenaufsicht_and_Wochentyp, bereich.idPausenaufsicht, bereich.wochentyp, bereich);
	}

	private update_pausenaufsichtbereichmenge_by_idAufsichtsbereich_and_Wochentyp() : void {
		this._pausenaufsichtbereichmenge_by_idAufsichtsbereich_and_Wochentyp = new HashMap2D();
		for (const aufsichtbereich of this._aufsichtsbereich_by_id.values())
			for (let wochentyp : number = 0; wochentyp <= this._stundenplanWochenTypModell; wochentyp++)
				Map2DUtils.getOrCreateArrayList(this._pausenaufsichtbereichmenge_by_idAufsichtsbereich_and_Wochentyp, aufsichtbereich.id, wochentyp);
		for (const bereich of this._pausenaufsichtbereich_by_id.values())
			Map2DUtils.addToList(this._pausenaufsichtbereichmenge_by_idAufsichtsbereich_and_Wochentyp, bereich.idAufsichtsbereich, bereich.wochentyp, bereich);
	}

	private update_klassenmenge_by_idSchueler() : void {
		this._klassenmenge_by_idSchueler = new HashMap();
		for (const schueler of this._schuelermenge)
			if (schueler.idKlasse >= 0) {
				const klasse : StundenplanKlasse = DeveloperNotificationException.ifMapGetIsNull(this._klasse_by_id, schueler.idKlasse);
				MapUtils.addToList(this._klassenmenge_by_idSchueler, schueler.id, klasse);
			}
	}

	private update_klassenmenge_by_idJahrgang() : void {
		this._klassenmenge_by_idJahrgang = new HashMap();
		for (const klasse of this._klassenmenge_sortiert)
			for (const idJahrgang of klasse.jahrgaenge)
				MapUtils.addToList(this._klassenmenge_by_idJahrgang, idJahrgang, klasse);
	}

	private update_kursmenge_by_idJahrgang() : void {
		this._kursmenge_by_idJahrgang = new HashMap();
		for (const kurs of this._kursmenge_sortiert)
			for (const idJahrgang of kurs.jahrgaenge)
				MapUtils.addToList(this._kursmenge_by_idJahrgang, idJahrgang, kurs);
	}

	private update_schienenmenge_by_idJahrgang() : void {
		this._schienenmenge_by_idJahrgang = new HashMap();
		for (const schiene of this._schienenmenge_sortiert)
			MapUtils.addToList(this._schienenmenge_by_idJahrgang, schiene.idJahrgang, schiene);
	}

	private update_pausenzeitmenge() : void {
		this._pausenzeitmenge_sortiert = new ArrayList(this._pausenzeit_by_id.values());
		this._pausenzeitmenge_sortiert.sort(StundenplanManager._compPausenzeit);
		this._pausenzeitMinutenMin = null;
		this._pausenzeitMinutenMax = null;
		this._pausenzeitWochentagMin = Wochentag.SONNTAG.id + 1;
		this._pausenzeitWochentagMax = Wochentag.MONTAG.id - 1;
		for (const p of this._pausenzeitmenge_sortiert) {
			this._pausenzeitMinutenMin = BlockungsUtils.minII(this._pausenzeitMinutenMin, p.beginn);
			this._pausenzeitMinutenMax = BlockungsUtils.maxII(this._pausenzeitMinutenMax, p.ende);
			this._pausenzeitWochentagMin = BlockungsUtils.minVI(this._pausenzeitWochentagMin, p.wochentag);
			this._pausenzeitWochentagMax = BlockungsUtils.maxVI(this._pausenzeitWochentagMax, p.wochentag);
		}
		this._pausenzeitWochentagMin = (this._pausenzeitWochentagMin === (Wochentag.SONNTAG.id + 1)) ? Wochentag.MONTAG.id : this._pausenzeitWochentagMin;
		this._pausenzeitWochentagMax = (this._pausenzeitWochentagMax === (Wochentag.MONTAG.id - 1)) ? Wochentag.MONTAG.id : this._pausenzeitWochentagMax;
		this._pausenzeitWochentageAlsEnumRange = Array((this._pausenzeitWochentagMax - this._pausenzeitWochentagMin) + 1).fill(null);
		for (let i : number = 0; i < this._pausenzeitWochentageAlsEnumRange.length; i++)
			this._pausenzeitWochentageAlsEnumRange[i] = Wochentag.fromIDorException(this._pausenzeitWochentagMin + i);
	}

	private update_pausenzeitmengeOhnePausenaufsicht() : void {
		this._pausenzeitmengeOhneLeere_sortiert = new ArrayList();
		for (const zeit of this._pausenzeitmenge_sortiert)
			if (!MapUtils.getOrCreateArrayList(this._pausenaufsichtmenge_by_idPausenzeit, zeit.id).isEmpty())
				this._pausenzeitmengeOhneLeere_sortiert.add(zeit);
		this._pausenzeitMinutenMinOhneLeere = null;
		this._pausenzeitMinutenMaxOhneLeere = null;
		for (const zeit of this._pausenzeitmengeOhneLeere_sortiert) {
			this._pausenzeitMinutenMinOhneLeere = BlockungsUtils.minII(this._pausenzeitMinutenMinOhneLeere, zeit.beginn);
			this._pausenzeitMinutenMaxOhneLeere = BlockungsUtils.maxII(this._pausenzeitMinutenMaxOhneLeere, zeit.ende);
		}
	}

	private update_pausenzeitmenge_by_wochentag() : void {
		this._pausenzeitmenge_by_wochentag = new HashMap();
		for (const zeit of this._pausenzeitmenge_sortiert)
			MapUtils.addToList(this._pausenzeitmenge_by_wochentag, zeit.wochentag, zeit);
	}

	private update_pausenzeitmenge_by_idSchueler() : void {
		this._pausenzeitmenge_by_idSchueler = new HashMap();
		for (const pausenzeit of this._pausenzeitmenge_sortiert)
			for (const klasse of MapUtils.getOrCreateArrayList(this._klassenmenge_by_idPausenzeit, pausenzeit.id))
				for (const schueler of MapUtils.getOrCreateArrayList(this._schuelermenge_by_idKlasse, klasse.id))
					MapUtils.addToList(this._pausenzeitmenge_by_idSchueler, schueler.id, pausenzeit);
	}

	private update_pausenzeitmenge_by_idSchueler_and_wochentag() : void {
		this._pausenzeitmenge_by_idSchueler_and_wochentag = new HashMap2D();
		for (const idSchueler of this._pausenzeitmenge_by_idSchueler.keySet())
			for (const zeit of MapUtils.getOrCreateArrayList(this._pausenzeitmenge_by_idSchueler, idSchueler))
				Map2DUtils.addToList(this._pausenzeitmenge_by_idSchueler_and_wochentag, idSchueler, zeit.wochentag, zeit);
	}

	private update_pausenzeitmenge_by_idKlasse() : void {
		this._pausenzeitmenge_by_idKlasse = new HashMap();
		for (const pausenzeit of this._pausenzeitmenge_sortiert)
			for (const klasse of MapUtils.getOrCreateArrayList(this._klassenmenge_by_idPausenzeit, pausenzeit.id))
				MapUtils.addToList(this._pausenzeitmenge_by_idKlasse, klasse.id, pausenzeit);
	}

	private update_pausenzeitmenge_by_idKlasse_and_wochentag() : void {
		this._pausenzeitmenge_by_idKlasse_and_wochentag = new HashMap2D();
		for (const idKlasse of this._pausenzeitmenge_by_idKlasse.keySet())
			for (const pausenzeit of MapUtils.getOrCreateArrayList(this._pausenzeitmenge_by_idKlasse, idKlasse))
				Map2DUtils.addToList(this._pausenzeitmenge_by_idKlasse_and_wochentag, idKlasse, pausenzeit.wochentag, pausenzeit);
	}

	private update_pausenzeitmenge_by_idLehrer() : void {
		this._pausenzeitmenge_by_idLehrer = new HashMap();
		for (const pausenaufsicht of this._pausenaufsichtmenge)
			if (pausenaufsicht.idLehrer >= 0) {
				const pausenzeit : StundenplanPausenzeit = DeveloperNotificationException.ifMapGetIsNull(this._pausenzeit_by_id, pausenaufsicht.idPausenzeit);
				MapUtils.addToList(this._pausenzeitmenge_by_idLehrer, pausenaufsicht.idLehrer, pausenzeit);
			}
	}

	private update_pausenzeitmenge_by_idLehrer_and_wochentag() : void {
		this._pausenzeitmenge_by_idLehrer_and_wochentag = new HashMap2D();
		for (const idLehrer of this._pausenzeitmenge_by_idLehrer.keySet())
			for (const pausenzeit of MapUtils.getOrCreateArrayList(this._pausenzeitmenge_by_idLehrer, idLehrer))
				Map2DUtils.addToList(this._pausenzeitmenge_by_idLehrer_and_wochentag, idLehrer, pausenzeit.wochentag, pausenzeit);
	}

	private update_pausenzeitmenge_by_idJahrgang_and_wochentag() : void {
		this._pausenzeitmenge_by_idJahrgang_and_wochentag = new HashMap2D();
		for (const idJahrgang of this._pausenzeitmenge_by_idJahrgang.keySet())
			for (const pausenzeit of MapUtils.getOrCreateArrayList(this._pausenzeitmenge_by_idJahrgang, idJahrgang))
				Map2DUtils.addToList(this._pausenzeitmenge_by_idJahrgang_and_wochentag, idJahrgang, pausenzeit.wochentag, pausenzeit);
	}

	private update_pausenzeitmenge_by_idJahrgang() : void {
		this._pausenzeitmenge_by_idJahrgang = new HashMap();
		for (const pausenzeit of this._pausenzeitmenge_sortiert)
			for (const klasse of MapUtils.getOrCreateArrayList(this._klassenmenge_by_idPausenzeit, pausenzeit.id))
				for (const idJahrgang of klasse.jahrgaenge)
					MapUtils.addToListIfNotExists(this._pausenzeitmenge_by_idJahrgang, idJahrgang, pausenzeit);
	}

	private update_klassenmenge_by_idKurs() : void {
		this._klassenmenge_by_idKurs = new HashMap();
		for (const kurs of this._kursmenge_sortiert) {
			for (const schueler of MapUtils.getOrCreateArrayList(this._schuelermenge_by_idKurs, kurs.id))
				if (schueler.idKlasse >= 0) {
					const klasse : StundenplanKlasse = DeveloperNotificationException.ifMapGetIsNull(this._klasse_by_id, schueler.idKlasse);
					MapUtils.addToListIfNotExists(this._klassenmenge_by_idKurs, kurs.id, klasse);
				}
			MapUtils.getOrCreateArrayList(this._klassenmenge_by_idKurs, kurs.id).sort(StundenplanUnterrichtUtils.comparatorKlassen);
		}
	}

	private update_aufsichtsbereichmenge() : void {
		this._aufsichtsbereichmenge_sortiert = new ArrayList(this._aufsichtsbereich_by_id.values());
		this._aufsichtsbereichmenge_sortiert.sort(StundenplanManager._compAufsichtsbereich);
	}

	private update_fachmenge() : void {
		this._fachmenge_sortiert = new ArrayList(this._fach_by_id.values());
		this._fachmenge_sortiert.sort(StundenplanManager._compFach);
	}

	private update_jahrgangmenge() : void {
		this._jahrgangmenge_sortiert = new ArrayList(this._jahrgang_by_id.values());
		this._jahrgangmenge_sortiert.sort(StundenplanManager._compJahrgang);
	}

	private update_kwzmenge_update_kwz_by_jahr_and_kw() : void {
		this._kwzmenge_sortiert_alle = new ArrayList(this._kwz_by_id.values());
		this._kwz_by_jahr_and_kw = new HashMap2D();
		for (const kwz of this._kwzmenge_sortiert_alle)
			DeveloperNotificationException.ifMap2DPutOverwrites(this._kwz_by_jahr_and_kw, kwz.jahr, kwz.kw, kwz);
		const infoVon : Array<number> = DateUtils.extractFromDateISO8601(this._stundenplanGueltigAb);
		const infoBis : Array<number> = DateUtils.extractFromDateISO8601(this._stundenplanGueltigBis);
		const jahrVon : number = infoVon[6];
		const jahrBis : number = infoBis[6];
		const kwVon : number = infoVon[5];
		const kwBis : number = infoBis[5];
		DeveloperNotificationException.ifTrue("Das Start-Datum '" + this._stundenplanGueltigAb + "' ist größer als das End-Datum '" + this._stundenplanGueltigBis + "'!", (jahrVon > jahrBis) || ((jahrVon === jahrBis) && (kwVon > kwBis)));
		for (let jahr : number = jahrVon; jahr <= jahrBis; jahr++) {
			const von : number = (jahr === jahrVon) ? kwVon : 1;
			const bis : number = (jahr === jahrBis) ? kwBis : DateUtils.gibKalenderwochenOfJahr(jahr);
			for (let kw : number = von; kw <= bis; kw++)
				if (!this._kwz_by_jahr_and_kw.contains(jahr, kw)) {
					const kwz : StundenplanKalenderwochenzuordnung = new StundenplanKalenderwochenzuordnung();
					kwz.id = -1;
					kwz.jahr = jahr;
					kwz.kw = kw;
					kwz.wochentyp = this.kalenderwochenzuordnungGetWochentypOrDefault(jahr, kw);
					DeveloperNotificationException.ifMap2DPutOverwrites(this._kwz_by_jahr_and_kw, kwz.jahr, kwz.kw, kwz);
					this._kwzmenge_sortiert_alle.add(kwz);
				}
		}
		this._kwzmenge_sortiert_alle.sort(StundenplanManager._compKWZ);
		this._kwzmenge_sortiert_alle_invers = new ArrayList();
		for (const kwz of this._kwzmenge_sortiert_alle)
			this._kwzmenge_sortiert_alle_invers.addFirst(kwz);
		this._kwzmenge_sortiert_ungueltige = new ArrayList();
		for (const kwz of this._kwz_by_id.values()) {
			const istKleiner : boolean = (kwz.jahr < jahrVon) || ((kwz.jahr === jahrVon) && (kwz.kw < kwVon));
			const istGroesser : boolean = (kwz.jahr > jahrBis) || ((kwz.jahr === jahrBis) && (kwz.kw > kwBis));
			if (istKleiner || istGroesser)
				this._kwzmenge_sortiert_ungueltige.add(kwz);
		}
		this._kwzmenge_sortiert_ungueltige.sort(StundenplanManager._compKWZ);
		this._kwzmenge_sortiert_gueltige = new ArrayList();
		for (const kwz of this._kwzmenge_sortiert_alle) {
			const istKleiner : boolean = (kwz.jahr < jahrVon) || ((kwz.jahr === jahrVon) && (kwz.kw < kwVon));
			const istGroesser : boolean = (kwz.jahr > jahrBis) || ((kwz.jahr === jahrBis) && (kwz.kw > kwBis));
			if (!istKleiner && !istGroesser)
				this._kwzmenge_sortiert_gueltige.add(kwz);
		}
	}

	private update_klassenmenge() : void {
		this._klassenmenge_sortiert = new ArrayList(this._klasse_by_id.values());
		this._klassenmenge_sortiert.sort(StundenplanUnterrichtUtils.comparatorKlassen);
	}

	private update_klassenunterrichtmenge() : void {
		this._klassenunterrichtmenge = new ArrayList(this._klassenunterricht_by_idKlasse_and_idFach.getNonNullValuesAsList());
		this._klassenunterrichtmenge.sort(this._compKlassenunterricht);
	}

	private update_klassenunterrichtmenge_by_idKlasse() : void {
		this._klassenunterrichtmenge_by_idKlasse = new HashMap();
		for (const klassenunterricht of this._klassenunterrichtmenge)
			MapUtils.addToList(this._klassenunterrichtmenge_by_idKlasse, klassenunterricht.idKlasse, klassenunterricht);
	}

	private update_klassenunterrichtmenge_by_idSchueler() : void {
		this._klassenunterrichtmenge_by_idSchueler = new HashMap();
		for (const klassenunterricht of this._klassenunterrichtmenge)
			for (const idSchueler of klassenunterricht.schueler)
				MapUtils.addToList(this._klassenunterrichtmenge_by_idSchueler, idSchueler, klassenunterricht);
	}

	private update_klassenunterrichtmenge_by_idLehrer() : void {
		this._klassenunterrichtmenge_by_idLehrer = new HashMap();
		for (const klassenunterricht of this._klassenunterrichtmenge)
			for (const idLehrer of klassenunterricht.lehrer)
				MapUtils.addToList(this._klassenunterrichtmenge_by_idLehrer, idLehrer, klassenunterricht);
	}

	private update_kursmenge() : void {
		this._kursmenge_sortiert = new ArrayList(this._kurs_by_id.values());
		this._kursmenge_sortiert.sort(StundenplanManager._compKurs);
	}

	private update_kursmenge_by_idSchueler() : void {
		this._kursmenge_by_idSchueler = new HashMap();
		for (const kurs of this._kursmenge_sortiert)
			for (const idSchueler of kurs.schueler)
				MapUtils.addToList(this._kursmenge_by_idSchueler, idSchueler, kurs);
	}

	private update_kursmenge_by_idLehrer() : void {
		this._kursmenge_by_idLehrer = new HashMap();
		for (const kurs of this._kursmenge_sortiert)
			for (const idLehrer of kurs.lehrer)
				MapUtils.addToList(this._kursmenge_by_idLehrer, idLehrer, kurs);
	}

	private update_kursmenge_by_idKlasse() : void {
		this._kursmenge_by_idKlasse = new HashMap();
		for (const kurs of this._kursmenge_sortiert)
			for (const schueler of MapUtils.getOrCreateArrayList(this._schuelermenge_by_idKurs, kurs.id))
				if (schueler.idKlasse >= 0)
					MapUtils.addToListIfNotExists(this._kursmenge_by_idKlasse, schueler.idKlasse, kurs);
	}

	private update_lehrermenge() : void {
		this._lehrermenge_sortiert = new ArrayList(this._lehrer_by_id.values());
		this._lehrermenge_sortiert.sort(StundenplanManager._compLehrer);
	}

	private update_lehrermenge_by_idUnterricht() : void {
		this._lehrermenge_by_idUnterricht = new HashMap();
		for (const u of this._unterrichtmenge) {
			for (const idLehrer of u.lehrer) {
				const lehrer : StundenplanLehrer = DeveloperNotificationException.ifMapGetIsNull(this._lehrer_by_id, idLehrer);
				MapUtils.addToList(this._lehrermenge_by_idUnterricht, u.id, lehrer);
			}
			MapUtils.getOrCreateArrayList(this._lehrermenge_by_idUnterricht, u.id).sort(StundenplanManager._compLehrer);
		}
	}

	private update_lehrermenge_by_idPausenzeit_and_idAufsichtsbereich_and_Wochentyp() : void {
		this._lehrermenge_by_idPausenzeit_and_idAufsichtsbereich_and_Wochentyp = new HashMap3D();
		for (const idPausenZeit of this._pausenaufsichtmenge_by_idPausenzeit_and_idAufsichtsbereich_and_Wochentyp.getKeySet()) {
			const map2 : JavaMap<number, JavaMap<number, List<StundenplanPausenaufsicht>>> | null = this._pausenaufsichtmenge_by_idPausenzeit_and_idAufsichtsbereich_and_Wochentyp.getMap2OrNull(idPausenZeit);
			if (map2 === null)
				continue;
			for (const idAufsichtsbereich of map2.keySet()) {
				const map3 : JavaMap<number, List<StundenplanPausenaufsicht>> | null = this._pausenaufsichtmenge_by_idPausenzeit_and_idAufsichtsbereich_and_Wochentyp.getMap3OrNull(idPausenZeit, idAufsichtsbereich);
				if (map3 === null)
					continue;
				for (const wochentyp of map3.keySet()) {
					for (const pausenaufsicht of this._pausenaufsichtmenge_by_idPausenzeit_and_idAufsichtsbereich_and_Wochentyp.getNonNullOrException(idPausenZeit, idAufsichtsbereich, wochentyp)) {
						const lehrer : StundenplanLehrer = DeveloperNotificationException.ifMapGetIsNull(this._lehrer_by_id, pausenaufsicht.idLehrer);
						Map3DUtils.addToList(this._lehrermenge_by_idPausenzeit_and_idAufsichtsbereich_and_Wochentyp, idPausenZeit, idAufsichtsbereich, wochentyp, lehrer);
					}
				}
			}
		}
	}

	private update_klassenmenge_verwendet_sortiert() : void {
		this._klassenmenge_verwendet_sortiert = new ArrayList();
		for (const klasse of this._klassenmenge_sortiert)
			if (!MapUtils.getOrCreateArrayList(this._unterrichtmenge_by_idKlasse, klasse.id).isEmpty())
				this._klassenmenge_verwendet_sortiert.add(klasse);
	}

	private update_raummenge() : void {
		this._raummenge_sortiert = new ArrayList(this._raum_by_id.values());
		this._raummenge_sortiert.sort(StundenplanManager._compRaum);
	}

	private update_schienenmenge() : void {
		this._schienenmenge_sortiert = new ArrayList(this._schiene_by_id.values());
		this._schienenmenge_sortiert.sort(StundenplanManager._compSchiene);
	}

	private update_schuelermenge() : void {
		this._schuelermenge = new ArrayList(this._schueler_by_id.values());
		this._schuelermenge.sort(StundenplanManager._compSchueler);
	}

	private update_schuelermenge_by_idKlasse() : void {
		this._schuelermenge_by_idKlasse = new HashMap();
		for (const schueler of this._schuelermenge)
			if (schueler.idKlasse >= 0)
				MapUtils.addToList(this._schuelermenge_by_idKlasse, schueler.idKlasse, schueler);
	}

	private update_schuelermenge_by_idKurs() : void {
		this._schuelermenge_by_idKurs = new HashMap();
		for (const kurs of this._kursmenge_sortiert) {
			for (const idSchueler of kurs.schueler) {
				const schueler : StundenplanSchueler = DeveloperNotificationException.ifMapGetIsNull(this._schueler_by_id, idSchueler);
				MapUtils.addToList(this._schuelermenge_by_idKurs, kurs.id, schueler);
			}
			MapUtils.getOrCreateArrayList(this._schuelermenge_by_idKurs, kurs.id).sort(StundenplanManager._compSchueler);
		}
	}

	private update_unterrichtmenge() : void {
		this._unterrichtmenge = new ArrayList(this._unterricht_by_id.values());
		this._unterrichtmenge.sort(this._compUnterricht);
		this._unterrichtHatMultiWochen = false;
		for (const u of this._unterrichtmenge) {
			u.klassen.sort(StundenplanManager._compID);
			u.lehrer.sort(StundenplanManager._compID);
			u.raeume.sort(StundenplanManager._compID);
			u.schienen.sort(StundenplanManager._compID);
			if (u.wochentyp > 0)
				this._unterrichtHatMultiWochen = true;
		}
	}

	private update_unterrichtmenge_by_idLehrer() : void {
		this._unterrichtmenge_by_idLehrer = new HashMap();
		for (const u of this._unterrichtmenge)
			for (const idLehrer of u.lehrer)
				MapUtils.addToList(this._unterrichtmenge_by_idLehrer, idLehrer, u);
	}

	private update_unterrichtmenge_by_idLehrer_and_idZeitraster() : void {
		this._unterrichtmenge_by_idLehrer_and_idZeitraster = new HashMap2D();
		for (const u of this._unterrichtmenge)
			for (const idLehrer of u.lehrer)
				Map2DUtils.addToList(this._unterrichtmenge_by_idLehrer_and_idZeitraster, idLehrer, u.idZeitraster, u);
	}

	private update_unterrichtmenge_by_idKlasse() : void {
		this._unterrichtmenge_by_idKlasse = new HashMap();
		for (const u of this._unterrichtmenge)
			if (u.idKurs === null) {
				for (const idKlasse of u.klassen)
					MapUtils.addToList(this._unterrichtmenge_by_idKlasse, idKlasse, u);
			} else {
				for (const klasse of MapUtils.getOrCreateArrayList(this._klassenmenge_by_idKurs, u.idKurs))
					MapUtils.addToList(this._unterrichtmenge_by_idKlasse, klasse.id, u);
			}
	}

	private update_unterrichtmenge_by_idKlasse_and_idZeitraster() : void {
		this._unterrichtmenge_by_idKlasse_and_idZeitraster = new HashMap2D();
		for (const u of this._unterrichtmenge)
			if (u.idKurs === null) {
				for (const idKlasse of u.klassen)
					Map2DUtils.addToList(this._unterrichtmenge_by_idKlasse_and_idZeitraster, idKlasse, u.idZeitraster, u);
			} else {
				for (const klasse of MapUtils.getOrCreateArrayList(this._klassenmenge_by_idKurs, u.idKurs))
					Map2DUtils.addToList(this._unterrichtmenge_by_idKlasse_and_idZeitraster, klasse.id, u.idZeitraster, u);
			}
	}

	private update_unterrichtmenge_by_idSchueler_and_idZeitraster() : void {
		this._unterrichtmenge_by_idSchueler_and_idZeitraster = new HashMap2D();
		for (const idSchueler of this._unterrichtmenge_by_idSchueler.keySet())
			for (const u of MapUtils.getOrCreateArrayList(this._unterrichtmenge_by_idSchueler, idSchueler))
				Map2DUtils.addToList(this._unterrichtmenge_by_idSchueler_and_idZeitraster, idSchueler, u.idZeitraster, u);
	}

	private update_unterrichtmenge_by_idKurs() : void {
		this._unterrichtmenge_by_idKurs = new HashMap();
		for (const u of this._unterrichtmenge)
			if (u.idKurs !== null)
				MapUtils.addToList(this._unterrichtmenge_by_idKurs, u.idKurs, u);
	}

	private update_unterrichtmenge_by_idKlasse_and_idFach() : void {
		this._unterrichtmenge_by_idKlasse_and_idFach = new HashMap2D();
		for (const u of this._unterrichtmenge)
			if (u.idKurs === null)
				for (const idKlasse of u.klassen)
					Map2DUtils.addToList(this._unterrichtmenge_by_idKlasse_and_idFach, idKlasse, u.idFach, u);
	}

	private update_unterrichtmenge_by_idZeitraster() : void {
		this._unterrichtmenge_by_idZeitraster = new HashMap();
		for (const u of this._unterrichtmenge)
			MapUtils.addToList(this._unterrichtmenge_by_idZeitraster, u.idZeitraster, u);
	}

	private update_unterrichtmenge_by_idZeitraster_and_wochentyp() : void {
		this._unterrichtmenge_by_idZeitraster_and_wochentyp = new HashMap2D();
		for (const u of this._unterrichtmenge) {
			Map2DUtils.addToList(this._unterrichtmenge_by_idZeitraster_and_wochentyp, u.idZeitraster, u.wochentyp, u);
			Map2DUtils.addToList(this._unterrichtmenge_by_idZeitraster_and_wochentyp, u.idZeitraster, -1, u);
		}
		for (const list of this._unterrichtmenge_by_idZeitraster_and_wochentyp.getNonNullValuesAsList())
			list.sort(this._compUnterrichtNachJahrgangKlasseFachWochentyp);
	}

	private update_unterrichtmenge_by_idRaum() : void {
		this._unterrichtmenge_by_idRaum = new HashMap();
		for (const u of this._unterrichtmenge)
			for (const idRaum of u.raeume)
				MapUtils.addToList(this._unterrichtmenge_by_idRaum, idRaum, u);
	}

	private update_unterrichtmenge_by_idFach() : void {
		this._unterrichtmenge_by_idFach = new HashMap();
		for (const u of this._unterrichtmenge)
			if (u.idFach >= 0)
				MapUtils.addToList(this._unterrichtmenge_by_idFach, u.idFach, u);
	}

	private update_unterrichtmenge_by_wochentyp() : void {
		this._unterrichtmenge_by_wochentyp = new HashMap();
		for (const u of this._unterrichtmenge)
			MapUtils.addToList(this._unterrichtmenge_by_wochentyp, u.wochentyp, u);
	}

	private update_unterrichtmenge_by_idSchueler() : void {
		this._unterrichtmenge_by_idSchueler = new HashMap();
		for (const u of this._unterrichtmenge)
			if (u.idKurs === null) {
				for (const idKlasse of u.klassen)
					for (const s of MapUtils.getOrCreateArrayList(this._schuelermenge_by_idKlasse, idKlasse))
						MapUtils.addToList(this._unterrichtmenge_by_idSchueler, s.id, u);
			} else {
				for (const s of MapUtils.getOrCreateArrayList(this._schuelermenge_by_idKurs, u.idKurs))
					MapUtils.addToList(this._unterrichtmenge_by_idSchueler, s.id, u);
			}
	}

	private update_unterrichtmenge_by_idJahrgang() : void {
		this._unterrichtmenge_by_idJahrgang = new HashMap();
		for (const u of this._unterrichtmenge)
			if (u.idKurs === null) {
				for (const idKlasse of u.klassen)
					for (const jahrgang of MapUtils.getOrCreateArrayList(this._jahrgangmenge_by_idKlasse, idKlasse))
						MapUtils.addToListIfNotExists(this._unterrichtmenge_by_idJahrgang, jahrgang.id, u);
			} else {
				for (const jahrgang of MapUtils.getOrCreateArrayList(this._jahrgangmenge_by_idKurs, u.idKurs))
					MapUtils.addToList(this._unterrichtmenge_by_idJahrgang, jahrgang.id, u);
			}
	}

	private update_unterrichtmenge_by_idJahrgang_and_idZeitraster() : void {
		this._unterrichtmenge_by_idJahrgang_and_idZeitraster = new HashMap2D();
		for (const idJahrgang of this._unterrichtmenge_by_idJahrgang.keySet())
			for (const u of MapUtils.getOrCreateArrayList(this._unterrichtmenge_by_idJahrgang, idJahrgang))
				Map2DUtils.addToList(this._unterrichtmenge_by_idJahrgang_and_idZeitraster, idJahrgang, u.idZeitraster, u);
	}

	private update_unterrichtmenge_by_idSchiene() : void {
		this._unterrichtmenge_by_idSchiene = new HashMap();
		for (const u of this._unterrichtmenge)
			for (const idSchiene of u.schienen)
				MapUtils.addToList(this._unterrichtmenge_by_idSchiene, idSchiene, u);
	}

	private update_zeitraster_by_wochentag_and_stunde() : void {
		this._zeitraster_by_wochentag_and_stunde = new HashMap2D();
		for (const zeit of this._zeitrastermenge)
			DeveloperNotificationException.ifMap2DPutOverwrites(this._zeitraster_by_wochentag_and_stunde, zeit.wochentag, zeit.unterrichtstunde, zeit);
	}

	private update_zeitrastermenge() : void {
		this._zeitrastermenge = new ArrayList(this._zeitraster_by_id.values());
		this._zeitrastermenge.sort(StundenplanManager._compZeitraster);
		this._zeitrasterMinutenMinByStunde = new HashMap();
		this._zeitrasterMinutenMaxByStunde = new HashMap();
		this._zeitrasterMinutenMin = null;
		this._zeitrasterMinutenMax = null;
		this._zeitrasterWochentagMin = Wochentag.SONNTAG.id + 1;
		this._zeitrasterWochentagMax = Wochentag.MONTAG.id - 1;
		this._zeitrasterStundeMin = 999;
		this._zeitrasterStundeMax = -999;
		for (const z of this._zeitrastermenge) {
			this._zeitrasterMinutenMin = BlockungsUtils.minII(this._zeitrasterMinutenMin, z.stundenbeginn);
			this._zeitrasterMinutenMax = BlockungsUtils.maxII(this._zeitrasterMinutenMax, z.stundenende);
			this._zeitrasterWochentagMin = BlockungsUtils.minVI(this._zeitrasterWochentagMin, z.wochentag);
			this._zeitrasterWochentagMax = BlockungsUtils.maxVI(this._zeitrasterWochentagMax, z.wochentag);
			this._zeitrasterStundeMin = BlockungsUtils.minVI(this._zeitrasterStundeMin, z.unterrichtstunde);
			this._zeitrasterStundeMax = BlockungsUtils.maxVI(this._zeitrasterStundeMax, z.unterrichtstunde);
			this._zeitrasterMinutenMinByStunde.put(z.unterrichtstunde, BlockungsUtils.minII(this._zeitrasterMinutenMinByStunde.get(z.unterrichtstunde), z.stundenbeginn));
			this._zeitrasterMinutenMaxByStunde.put(z.unterrichtstunde, BlockungsUtils.maxII(this._zeitrasterMinutenMaxByStunde.get(z.unterrichtstunde), z.stundenende));
		}
		this._zeitrasterWochentagMin = (this._zeitrasterWochentagMin === (Wochentag.SONNTAG.id + 1)) ? Wochentag.MONTAG.id : this._zeitrasterWochentagMin;
		this._zeitrasterWochentagMax = (this._zeitrasterWochentagMax === (Wochentag.MONTAG.id - 1)) ? Wochentag.MONTAG.id : this._zeitrasterWochentagMax;
		this._zeitrasterStundeMin = (this._zeitrasterStundeMin === 999) ? 1 : this._zeitrasterStundeMin;
		this._zeitrasterStundeMax = (this._zeitrasterStundeMax === -999) ? 1 : this._zeitrasterStundeMax;
		this._zeitrasterWochentageAlsEnumRange = Array((this._zeitrasterWochentagMax - this._zeitrasterWochentagMin) + 1).fill(null);
		for (let i : number = 0; i < this._zeitrasterWochentageAlsEnumRange.length; i++)
			this._zeitrasterWochentageAlsEnumRange[i] = Wochentag.fromIDorException(this._zeitrasterWochentagMin + i);
		this._zeitrasterStundenRange = Array((this._zeitrasterStundeMax - this._zeitrasterStundeMin) + 1).fill(0);
		for (let i : number = 0; i < this._zeitrasterStundenRange.length; i++)
			this._zeitrasterStundenRange[i] = this._zeitrasterStundeMin + i;
	}

	private update_zeitrastermengeOhneLeereUnterrichtmenge() : void {
		this._zeitrastermengeOhneLeere_sortiert = new ArrayList();
		for (const z of this._zeitrastermenge)
			if (!MapUtils.getOrCreateArrayList(this._unterrichtmenge_by_idZeitraster, z.id).isEmpty())
				this._zeitrastermengeOhneLeere_sortiert.add(z);
		this._zeitrasterMinutenMinOhneLeere = null;
		this._zeitrasterMinutenMaxOhneLeere = null;
		this._zeitrasterStundeMinOhneLeere = 999;
		this._zeitrasterStundeMaxOhneLeere = -999;
		for (const z of this._zeitrastermengeOhneLeere_sortiert) {
			this._zeitrasterMinutenMinOhneLeere = BlockungsUtils.minII(this._zeitrasterMinutenMinOhneLeere, z.stundenbeginn);
			this._zeitrasterMinutenMaxOhneLeere = BlockungsUtils.maxII(this._zeitrasterMinutenMaxOhneLeere, z.stundenende);
			this._zeitrasterStundeMinOhneLeere = BlockungsUtils.minVI(this._zeitrasterStundeMinOhneLeere, z.unterrichtstunde);
			this._zeitrasterStundeMaxOhneLeere = BlockungsUtils.maxVI(this._zeitrasterStundeMaxOhneLeere, z.unterrichtstunde);
		}
		this._zeitrasterStundeMinOhneLeere = (this._zeitrasterStundeMinOhneLeere === 999) ? 1 : this._zeitrasterStundeMinOhneLeere;
		this._zeitrasterStundeMaxOhneLeere = (this._zeitrasterStundeMaxOhneLeere === -999) ? 1 : this._zeitrasterStundeMaxOhneLeere;
		this._zeitrasterStundenRangeOhneLeere = Array((this._zeitrasterStundeMaxOhneLeere - this._zeitrasterStundeMinOhneLeere) + 1).fill(0);
		for (let i : number = 0; i < this._zeitrasterStundenRangeOhneLeere.length; i++)
			this._zeitrasterStundenRangeOhneLeere[i] = this._zeitrasterStundeMinOhneLeere + i;
	}

	private update_zeitrastermenge_by_wochentag() : void {
		this._zeitrastermenge_by_wochentag = new HashMap();
		for (const zeit of this._zeitrastermenge)
			MapUtils.addToList(this._zeitrastermenge_by_wochentag, zeit.wochentag, zeit);
	}

	private update_zeitrastermenge_by_stunde() : void {
		this._zeitrastermenge_by_stunde = new HashMap();
		for (const zeit of this._zeitrastermenge)
			MapUtils.addToList(this._zeitrastermenge_by_stunde, zeit.unterrichtstunde, zeit);
	}

	private update_wertPausenaufsichtMinuten_by_idLehrkraft_and_wochentyp() : void {
		this._wertPausenaufsichtMinuten_by_idLehrkraft_and_wochentyp = new HashMap2D();
		for (const lehrer of this._lehrermenge_sortiert)
			for (let wochentyp : number = 0; wochentyp <= this._stundenplanWochenTypModell; wochentyp++)
				this._wertPausenaufsichtMinuten_by_idLehrkraft_and_wochentyp.put(lehrer.id, wochentyp, 0.0);
		for (const pab of this._pausenaufsichtbereichmenge) {
			const pa : StundenplanPausenaufsicht = DeveloperNotificationException.ifMapGetIsNull(this._pausenaufsicht_by_id, pab.idPausenaufsicht);
			const pz : StundenplanPausenzeit = DeveloperNotificationException.ifMapGetIsNull(this._pausenzeit_by_id, pa.idPausenzeit);
			if (pz.beginn === null)
				continue;
			if (pz.ende === null)
				continue;
			let wert : number = this._wertPausenaufsichtMinuten_by_idLehrkraft_and_wochentyp.getOrException(pa.idLehrer, pab.wochentyp).valueOf();
			wert += pz.ende - pz.beginn;
			this._wertPausenaufsichtMinuten_by_idLehrkraft_and_wochentyp.put(pa.idLehrer, pab.wochentyp, wert);
		}
		for (const lehrer of this._lehrermenge_sortiert) {
			let avg : number = this._wertPausenaufsichtMinuten_by_idLehrkraft_and_wochentyp.getOrException(lehrer.id, 0).valueOf();
			if (this._stundenplanWochenTypModell >= 2) {
				let anteil : number = 0.0;
				for (let wochentyp : number = 1; wochentyp <= this._stundenplanWochenTypModell; wochentyp++)
					anteil += this._wertPausenaufsichtMinuten_by_idLehrkraft_and_wochentyp.getOrException(lehrer.id, wochentyp);
				avg += anteil / this._stundenplanWochenTypModell;
			}
			this._wertPausenaufsichtMinuten_by_idLehrkraft_and_wochentyp.put(lehrer.id, -1, avg);
		}
	}

	private update_wertPausenaufsichtAnzahl_by_idLehrkraft_and_wochentyp() : void {
		this._wertPausenaufsichtAnzahl_by_idLehrkraft_and_wochentyp = new HashMap2D();
		for (const lehrer of this._lehrermenge_sortiert)
			for (let wochentyp : number = 0; wochentyp <= this._stundenplanWochenTypModell; wochentyp++)
				this._wertPausenaufsichtAnzahl_by_idLehrkraft_and_wochentyp.put(lehrer.id, wochentyp, 0.0);
		for (const pab of this._pausenaufsichtbereichmenge) {
			const pa : StundenplanPausenaufsicht = DeveloperNotificationException.ifMapGetIsNull(this._pausenaufsicht_by_id, pab.idPausenaufsicht);
			const pz : StundenplanPausenzeit = DeveloperNotificationException.ifMapGetIsNull(this._pausenzeit_by_id, pa.idPausenzeit);
			if (pz.beginn === null)
				continue;
			if (pz.ende === null)
				continue;
			let wert : number = this._wertPausenaufsichtAnzahl_by_idLehrkraft_and_wochentyp.getOrException(pa.idLehrer, pab.wochentyp).valueOf();
			wert++;
			this._wertPausenaufsichtAnzahl_by_idLehrkraft_and_wochentyp.put(pa.idLehrer, pab.wochentyp, wert);
		}
		for (const lehrer of this._lehrermenge_sortiert) {
			let avg : number = this._wertPausenaufsichtAnzahl_by_idLehrkraft_and_wochentyp.getOrException(lehrer.id, 0).valueOf();
			if (this._stundenplanWochenTypModell >= 2) {
				let anteil : number = 0.0;
				for (let wochentyp : number = 1; wochentyp <= this._stundenplanWochenTypModell; wochentyp++)
					anteil += this._wertPausenaufsichtAnzahl_by_idLehrkraft_and_wochentyp.getOrException(lehrer.id, wochentyp);
				avg += anteil / this._stundenplanWochenTypModell;
			}
			this._wertPausenaufsichtAnzahl_by_idLehrkraft_and_wochentyp.put(lehrer.id, -1, avg);
		}
	}

	private update_kwzmenge_by_wochentyp() : void {
		this._kwzmenge_by_wochentyp = new HashMap();
		for (const kwz of this._kwzmenge_sortiert_alle)
			MapUtils.addToList(this._kwzmenge_by_wochentyp, kwz.wochentyp, kwz);
	}

	private update_klassenmenge_by_idUnterricht() : void {
		this._klassenmenge_by_idUnterricht = new HashMap();
		for (const u of this._unterrichtmenge) {
			if (u.idKurs === null) {
				for (const idKlasse of u.klassen) {
					const klasse : StundenplanKlasse = DeveloperNotificationException.ifMapGetIsNull(this._klasse_by_id, idKlasse);
					MapUtils.addToListIfNotExists(this._klassenmenge_by_idUnterricht, u.id, klasse);
				}
			} else {
				for (const klasse of MapUtils.getOrCreateArrayList(this._klassenmenge_by_idKurs, u.idKurs)) {
					MapUtils.addToListIfNotExists(this._klassenmenge_by_idUnterricht, u.id, klasse);
				}
			}
		}
	}

	/**
	 * Fügt ein {@link StundenplanAufsichtsbereich}-Objekt hinzu.
	 *
	 * @param aufsichtsbereich  Das {@link StundenplanAufsichtsbereich}-Objekt, welches hinzugefügt werden soll.
	 */
	public aufsichtsbereichAdd(aufsichtsbereich : StundenplanAufsichtsbereich) : void {
		this.aufsichtsbereichAddAll(ListUtils.create1(aufsichtsbereich));
	}

	/**
	 * Fügt alle {@link StundenplanAufsichtsbereich}-Objekte hinzu.
	 *
	 * @param listAufsichtsbereich  Die Menge der {@link StundenplanAufsichtsbereich}-Objekte, welche hinzugefügt werden soll.
	 */
	public aufsichtsbereichAddAll(listAufsichtsbereich : List<StundenplanAufsichtsbereich>) : void {
		this.aufsichtsbereichAddAllOhneUpdate(listAufsichtsbereich);
		this.update_all();
	}

	private aufsichtsbereichAddAllOhneUpdate(list : List<StundenplanAufsichtsbereich>) : void {
		const setOfIDs : JavaSet<number> = new HashSet<number>();
		for (const aufsichtsbereich of list) {
			StundenplanManager.aufsichtsbereichCheckAttributes(aufsichtsbereich);
			DeveloperNotificationException.ifTrue("aufsichtsbereichAddAllOhneUpdate: ID=" + aufsichtsbereich.id + " existiert bereits!", this._aufsichtsbereich_by_id.containsKey(aufsichtsbereich.id));
			DeveloperNotificationException.ifTrue("aufsichtsbereichAddAllOhneUpdate: ID=" + aufsichtsbereich.id + " doppelt in der Liste!", !setOfIDs.add(aufsichtsbereich.id));
		}
		for (const aufsichtsbereich of list)
			DeveloperNotificationException.ifMapPutOverwrites(this._aufsichtsbereich_by_id, aufsichtsbereich.id, aufsichtsbereich);
	}

	private static aufsichtsbereichCheckAttributes(aufsichtsbereich : StundenplanAufsichtsbereich) : void {
		DeveloperNotificationException.ifInvalidID("aufsicht.id", aufsichtsbereich.id);
		DeveloperNotificationException.ifStringIsBlank("aufsicht.kuerzel", aufsichtsbereich.kuerzel);
	}

	/**
	 * Liefert TRUE, falls ein {@link StundenplanAufsichtsbereich}-Objekt mit dem Kürzel existiert.
	 * <br>Laufzeit: O(1)
	 *
	 * @param kuerzel  Das Kürzel des {@link StundenplanAufsichtsbereich}-Objektes.
	 *
	 * @return TRUE, falls ein {@link StundenplanAufsichtsbereich}-Objekt mit dem Kürzel existiert.
	 */
	public aufsichtsbereichExistsByKuerzel(kuerzel : string) : boolean {
		return this._aufsichtsbereich_by_kuerzel.containsKey(kuerzel);
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanAufsichtsbereich}-Objekt.
	 *
	 * @param idAufsichtsbereich  Die Datenbank-ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanAufsichtsbereich}-Objekt.
	 */
	public aufsichtsbereichGetByIdOrException(idAufsichtsbereich : number) : StundenplanAufsichtsbereich {
		return DeveloperNotificationException.ifMapGetIsNull(this._aufsichtsbereich_by_id, idAufsichtsbereich);
	}

	/**
	 * Liefert das zum Kürzel zugehörige {@link StundenplanAufsichtsbereich}-Objekt oder null, wenn kein
	 * StundenplanAufsichtsbereich für das Kürzel vorhanden ist.
	 * <br>Laufzeit: O(1)
	 *
	 * @param kuerzelAufsichtsbereich   das Kürzel des angefragten-Objektes.
	 *
	 * @return das zum Kürzel zugehörige {@link StundenplanAufsichtsbereich}-Objekt oder null
	 */
	public aufsichtsbereichGetByKuerzelOrNull(kuerzelAufsichtsbereich : string) : StundenplanAufsichtsbereich | null {
		return this._aufsichtsbereich_by_kuerzel.get(kuerzelAufsichtsbereich);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanAufsichtsbereich}-Objekte.
	 * <br> Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link StundenplanAufsichtsbereich}-Objekte.
	 */
	public aufsichtsbereichGetMengeAsList() : List<StundenplanAufsichtsbereich> {
		return this._aufsichtsbereichmenge_sortiert;
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
	public aufsichtsbereichPatchAttributes(aufsichtsbereich : StundenplanAufsichtsbereich) : void {
		StundenplanManager.aufsichtsbereichCheckAttributes(aufsichtsbereich);
		DeveloperNotificationException.ifMapRemoveFailes(this._aufsichtsbereich_by_id, aufsichtsbereich.id);
		DeveloperNotificationException.ifMapPutOverwrites(this._aufsichtsbereich_by_id, aufsichtsbereich.id, aufsichtsbereich);
		this.update_all();
	}

	private aufsichtsbereichRemoveOhneUpdateById(idAufsichtsbereich : number) : void {
		const bereiche : List<StundenplanPausenaufsichtBereich> = DeveloperNotificationException.ifMapGetIsNull(this._pausenaufsichtbereichmenge_by_idAufsichtsbereich, idAufsichtsbereich);
		this.pausenaufsichtbereichRemoveAllOhneUpdate(bereiche);
		DeveloperNotificationException.ifMapRemoveFailes(this._aufsichtsbereich_by_id, idAufsichtsbereich);
	}

	/**
	 * Entfernt ein {@link StundenplanAufsichtsbereich}-Objekt anhand seiner ID.
	 * <br>Laufzeit: O(|StundenplanAufsichtsbereich|), da aufsichtsbereichUpdate() aufgerufen wird.
	 *
	 * @param idAufsichtsbereich  Die Datenbank-ID des {@link StundenplanAufsichtsbereich}-Objekts, welches entfernt werden soll.
	 */
	public aufsichtsbereichRemoveById(idAufsichtsbereich : number) : void {
		this.aufsichtsbereichRemoveOhneUpdateById(idAufsichtsbereich);
		this.update_all();
	}

	/**
	 * Entfernt alle {@link StundenplanAufsichtsbereich}-Objekte.
	 *
	 * @param listAufsichtsbereich  Die Liste der zu entfernenden {@link StundenplanAufsichtsbereich}-Objekte.
	 */
	public aufsichtsbereichRemoveAll(listAufsichtsbereich : List<StundenplanAufsichtsbereich>) : void {
		const setOfIDs : JavaSet<number> = new HashSet<number>();
		for (const aufsichtsbereich of listAufsichtsbereich) {
			if (!this._aufsichtsbereich_by_id.containsKey(aufsichtsbereich.id))
				throw new DeveloperNotificationException("Es soll ein Aufsichtsbereich gelöscht werden, aber die ID existiert nicht!")
			if (!setOfIDs.add(aufsichtsbereich.id))
				throw new DeveloperNotificationException("In der Liste aller zu löschenden Aufsichtsbereiche ist eine ID doppelt!")
		}
		for (const aufsichtsbereich of listAufsichtsbereich)
			this.aufsichtsbereichRemoveOhneUpdateById(aufsichtsbereich.id);
		this.update_all();
	}

	/**
	 * Liefert zu einem {@link StundenplanKalenderwochenzuordnung}-Objekt und einem {@link StundenplanZeitraster}-Objekt das zugehörige Datum.
	 *
	 * @param kwz   Das {@link StundenplanKalenderwochenzuordnung}-Objekt, welches das Datum zum Teil definiert.
	 * @param zeit  Das {@link StundenplanZeitraster}-Objekt, welches das Datum zum Teil definiert.
	 *
	 * @return zu einem {@link StundenplanKalenderwochenzuordnung}-Objekt und einem {@link StundenplanZeitraster}-Objekt das zugehörige Datum.
	 */
	public datumGetByKwzAndZeitraster(kwz : StundenplanKalenderwochenzuordnung, zeit : StundenplanZeitraster) : string {
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
	public datumGetByKwzAndWochentag(kwz : StundenplanKalenderwochenzuordnung, wochentag : Wochentag) : string {
		return DateUtils.gibDatumDesWochentagsOfJahrAndKalenderwoche(kwz.jahr, kwz.kw, wochentag.id);
	}

	/**
	 * Fügt ein {@link StundenplanFach}-Objekt hinzu.
	 * <br>Laufzeit: O(|StundenplanFach|), da fachUpdate() aufgerufen wird.
	 *
	 * @param fach  Das {@link StundenplanFach}-Objekt, welches hinzugefügt werden soll.
	 */
	public fachAdd(fach : StundenplanFach) : void {
		this.fachAddAll(ListUtils.create1(fach));
	}

	/**
	 * Fügt alle {@link StundenplanFach}-Objekte hinzu.
	 * <br>Laufzeit: O(|StundenplanFach|), da fachUpdate() aufgerufen wird.
	 *
	 * @param listFach  Die Menge der {@link StundenplanFach}-Objekte, welche hinzugefügt werden soll.
	 */
	public fachAddAll(listFach : List<StundenplanFach>) : void {
		this.fachAddAllOhneUpdate(listFach);
		this.update_all();
	}

	private fachAddAllOhneUpdate(list : List<StundenplanFach>) : void {
		const setOfIDs : JavaSet<number> = new HashSet<number>();
		for (const fach of list) {
			StundenplanManager.fachCheckAttributes(fach);
			DeveloperNotificationException.ifTrue("In der Menge der hinzuzufügenden Fächer existiert die ID=" + fach.id + " bereits!", this._fach_by_id.containsKey(fach.id));
			DeveloperNotificationException.ifTrue("In der Menge der hinzuzufügenden Fächer ist die ID=" + fach.id + " doppelt!", !setOfIDs.add(fach.id));
		}
		for (const fach of list)
			DeveloperNotificationException.ifMapPutOverwrites(this._fach_by_id, fach.id, fach);
	}

	private static fachCheckAttributes(fach : StundenplanFach) : void {
		DeveloperNotificationException.ifInvalidID("fach.id", fach.id);
		DeveloperNotificationException.ifTrue("Das Fach-Kürzel darf nicht leer sein!", JavaString.isBlank(fach.kuerzel));
	}

	/**
	 * Liefert das Fach mit der übergebenen ID.
	 *
	 * @param idFach  Die Datenbank-ID des Faches.
	 *
	 * @return  das Fach mit der übergebenen ID.
	 */
	public fachGetByIdOrException(idFach : number) : StundenplanFach {
		return DeveloperNotificationException.ifMapGetIsNull(this._fach_by_id, idFach);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanFach}-Objekte, sortiert nach {@link StundenplanFach#sortierung}.
	 * <br>Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link StundenplanFach}-Objekte, sortiert nach {@link StundenplanFach#sortierung}.
	 */
	public fachGetMengeAsList() : List<StundenplanFach> {
		return this._fachmenge_sortiert;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanFach}-Objekte, sortiert nach {@link StundenplanFach#sortierung}, die gerade verwendet werden.
	 * <br>Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link StundenplanFach}-Objekte, sortiert nach {@link StundenplanFach#sortierung}, die gerade verwendet werden.
	 */
	public fachGetMengeVerwendetAsList() : List<StundenplanFach> {
		return this._fachmenge_verwendet_sortiert;
	}

	/**
	 * Fügt ein {@link StundenplanJahrgang}-Objekt hinzu.
	 *
	 * @param jahrgang  Das {@link StundenplanJahrgang}-Objekt, welches hinzugefügt werden soll.
	 */
	public jahrgangAdd(jahrgang : StundenplanJahrgang) : void {
		this.jahrgangAddAll(ListUtils.create1(jahrgang));
	}

	/**
	 * Fügt alle {@link StundenplanJahrgang}-Objekte hinzu.
	 *
	 * @param listJahrgang  Die Menge der {@link StundenplanJahrgang}-Objekte, welche hinzugefügt werden soll.
	 */
	public jahrgangAddAll(listJahrgang : List<StundenplanJahrgang>) : void {
		this.jahrgangAddAllOhneUpdate(listJahrgang);
		this.update_all();
	}

	private jahrgangAddAllOhneUpdate(list : List<StundenplanJahrgang>) : void {
		const setOfIDs : JavaSet<number> = new HashSet<number>();
		for (const jahrgang of list) {
			StundenplanManager.jahrgangCheckAttributes(jahrgang);
			DeveloperNotificationException.ifTrue("Beim der hinzuzufügenden Liste aller Jahrgänge ist die ID=" + jahrgang.id + " bereits existent!", this._jahrgang_by_id.containsKey(jahrgang.id));
			DeveloperNotificationException.ifTrue("Beim der hinzuzufügenden Liste aller Jahrgänge ist die ID=" + jahrgang.id + " doppelt in der Liste!", !setOfIDs.add(jahrgang.id));
		}
		for (const jahrgang of list)
			DeveloperNotificationException.ifMapPutOverwrites(this._jahrgang_by_id, jahrgang.id, jahrgang);
	}

	private static jahrgangCheckAttributes(jahrgang : StundenplanJahrgang) : void {
		DeveloperNotificationException.ifInvalidID("jahrgang.id", jahrgang.id);
		DeveloperNotificationException.ifTrue("Das Kürzel des Jahrgangs darf nicht leer sein!", JavaString.isBlank(jahrgang.kuerzel));
	}

	/**
	 * Liefert das {@link StundenplanJahrgang}-Objekt mit der übergebenen ID.
	 *
	 * @param idJahrgang  Die Datenbank-ID des {@link StundenplanJahrgang}-Objekts.
	 *
	 * @return das {@link StundenplanJahrgang}-Objekt mit der übergebenen ID.
	 */
	public jahrgangGetByIdOrException(idJahrgang : number) : StundenplanJahrgang {
		return DeveloperNotificationException.ifMapGetIsNull(this._jahrgang_by_id, idJahrgang);
	}

	private jahrgangGetMinimumByKlassenIDs(klassen : List<number>) : StundenplanJahrgang | null {
		let min : StundenplanJahrgang | null = null;
		for (const idKlasse of klassen) {
			const klasse : StundenplanKlasse = DeveloperNotificationException.ifMapGetIsNull(this._klasse_by_id, idKlasse);
			for (const idJahrgang of klasse.jahrgaenge) {
				const jahrgang : StundenplanJahrgang = DeveloperNotificationException.ifMapGetIsNull(this._jahrgang_by_id, idJahrgang);
				if ((min === null) || (StundenplanManager._compJahrgang.compare(jahrgang, min) < 0))
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
	public jahrgangGetMengeAsList() : List<StundenplanJahrgang> {
		return this._jahrgangmenge_sortiert;
	}

	/**
	 * Liefert eine Liste der {@link StundenplanJahrgang}-Objekte für den Kurs mit der angegebenen ID.
	 *
	 * @param idKurs   die ID des Kurses
	 *
	 * @return eine Liste der {@link StundenplanJahrgang}-Objekte.
	 */
	public jahrgangGetMengeByKursIdAsList(idKurs : number) : List<StundenplanJahrgang> {
		const result : List<StundenplanJahrgang> = new ArrayList<StundenplanJahrgang>();
		const kurs : StundenplanKurs = this.kursGetByIdOrException(idKurs);
		for (const idJahrgang of kurs.jahrgaenge)
			result.add(this.jahrgangGetByIdOrException(idJahrgang));
		return result;
	}

	/**
	 * Liefert eine Liste der {@link StundenplanJahrgang}-Objekte für die übergene Menge an Klassen-IDs.
	 *
	 * @param idsKlassen   die IDs der Klassen
	 *
	 * @return eine Liste der {@link StundenplanJahrgang}-Objekte.
	 */
	public jahrgangGetMengeByKlassenIdsAsList(idsKlassen : Collection<number>) : List<StundenplanJahrgang> {
		const set : JavaSet<StundenplanJahrgang> = new HashSet<StundenplanJahrgang>();
		for (const idKlasse of idsKlassen) {
			const klasse : StundenplanKlasse = this.klasseGetByIdOrException(idKlasse);
			for (const idJahrgang of klasse.jahrgaenge)
				set.add(this.jahrgangGetByIdOrException(idJahrgang));
		}
		const result : List<StundenplanJahrgang> = new ArrayList<StundenplanJahrgang>(set);
		result.sort(StundenplanManager._compJahrgang);
		return result;
	}

	/**
	 * Liefert eine Liste der {@link StundenplanJahrgang}-Objekte für den Unterricht mit der angegebenen ID.
	 *
	 * @param idUnterricht   die ID des Unterrichts
	 *
	 * @return eine Liste der {@link StundenplanJahrgang}-Objekte.
	 */
	public jahrgangGetMengeByUnterrichtIdAsList(idUnterricht : number) : List<StundenplanJahrgang> {
		return DeveloperNotificationException.ifMapGetIsNull(this._jahrgangmenge_by_idUnterricht, idUnterricht);
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
	public jahrgangPatchAttributes(jahrgang : StundenplanJahrgang) : void {
		StundenplanManager.jahrgangCheckAttributes(jahrgang);
		DeveloperNotificationException.ifMapRemoveFailes(this._jahrgang_by_id, jahrgang.id);
		DeveloperNotificationException.ifMapPutOverwrites(this._jahrgang_by_id, jahrgang.id, jahrgang);
		this.update_all();
	}

	private jahrgangRemoveOhneUpdateById(idJahrgang : number) : void {
		for (const schiene of MapUtils.getOrCreateArrayList(this._schienenmenge_by_idJahrgang, idJahrgang))
			this.schieneRemoveOhneUpdateById(schiene.id);
		for (const kurs of MapUtils.getOrCreateArrayList(this._kursmenge_by_idJahrgang, idJahrgang))
			kurs.jahrgaenge.remove(idJahrgang);
		for (const klasse of MapUtils.getOrCreateArrayList(this._klassenmenge_by_idJahrgang, idJahrgang))
			klasse.jahrgaenge.remove(idJahrgang);
		DeveloperNotificationException.ifMapRemoveFailes(this._jahrgang_by_id, idJahrgang);
	}

	/**
	 * Entfernt ein {@link StundenplanJahrgang}-Objekt anhand seiner ID.
	 * <br>Laufzeit: O(|StundenplanJahrgang|), da jahrgangUpdate() aufgerufen wird.
	 *
	 * @param idJahrgang  Die Datenbank-ID des {@link StundenplanJahrgang}-Objekts, welches entfernt werden soll.
	 */
	public jahrgangRemoveById(idJahrgang : number) : void {
		this.jahrgangRemoveOhneUpdateById(idJahrgang);
		this.update_all();
	}

	/**
	 * Entfernt alle {@link StundenplanJahrgang}-Objekte.
	 *
	 * @param listJahrgang  Die Liste der zu entfernenden {@link StundenplanJahrgang}-Objekte.
	 */
	public jahrgangRemoveAll(listJahrgang : List<StundenplanJahrgang>) : void {
		for (const jahrgang of listJahrgang)
			this.jahrgangRemoveOhneUpdateById(jahrgang.id);
		this.update_all();
	}

	/**
	 * Fügt ein {@link StundenplanKalenderwochenzuordnung}-Objekt hinzu.
	 *
	 * @param kwz  Das {@link StundenplanKalenderwochenzuordnung}-Objekt, welches hinzugefügt werden soll.
	 */
	public kalenderwochenzuordnungAdd(kwz : StundenplanKalenderwochenzuordnung) : void {
		this.kalenderwochenzuordnungAddAllOhneUpdate(ListUtils.create1(kwz));
		this.update_all();
	}

	/**
	 * Fügt alle {@link StundenplanKalenderwochenzuordnung}-Objekte hinzu.
	 *
	 * @param listKWZ  Die Menge der {@link StundenplanKalenderwochenzuordnung}-Objekte, welche hinzugefügt werden soll.
	 */
	public kalenderwochenzuordnungAddAll(listKWZ : List<StundenplanKalenderwochenzuordnung>) : void {
		this.kalenderwochenzuordnungAddAllOhneUpdate(listKWZ);
		this.update_all();
	}

	private kalenderwochenzuordnungAddAllOhneUpdate(list : List<StundenplanKalenderwochenzuordnung>) : void {
		const setOfIDs : JavaSet<string> = new HashSet<string>();
		for (const kwz of list) {
			this.kalenderwochenzuordnungCheckAttributes(kwz, true);
			DeveloperNotificationException.ifTrue("JAHR=" + kwz.jahr + ", KW=" + kwz.kw + " doppelt in der Liste!", !setOfIDs.add(kwz.jahr + ";" + kwz.kw));
			if (this._kwz_by_jahr_and_kw.contains(kwz.jahr, kwz.kw)) {
				const kwzMapped : StundenplanKalenderwochenzuordnung = this._kwz_by_jahr_and_kw.getOrException(kwz.jahr, kwz.kw);
				DeveloperNotificationException.ifTrue("JAHR=" + kwz.jahr + ", KW=" + kwz.kw + " existiert bereits!", kwzMapped.id >= 0);
			}
		}
		for (const kwz of list)
			this._kwz_by_id.put(kwz.id, kwz);
	}

	private kalenderwochenzuordnungCheckAttributes(kwz : StundenplanKalenderwochenzuordnung, checkID : boolean) : void {
		if (checkID)
			DeveloperNotificationException.ifInvalidID("kwz.id", kwz.id);
		DeveloperNotificationException.ifTrue("Die Kalenderwochenzuordnung hat das ungültige Jahr " + kwz.jahr + "!", (kwz.jahr < DateUtils.MIN_GUELTIGES_JAHR) || (kwz.jahr > DateUtils.MAX_GUELTIGES_JAHR));
		DeveloperNotificationException.ifTrue("Die Kalenderwochenzuordnung hat die ungültige Kalenderwoche " + kwz.kw + "!", (kwz.kw < 1) || (kwz.kw > DateUtils.gibKalenderwochenOfJahr(kwz.jahr)));
		DeveloperNotificationException.ifTrue("Die Kalenderwochenzuordnung hat den ungültigen Wochentyp " + kwz.wochentyp + "!", kwz.wochentyp > this._stundenplanWochenTypModell);
		DeveloperNotificationException.ifTrue("Die Kalenderwochenzuordnung darf keinen negativen Wochentyp haben!", kwz.wochentyp < 0);
	}

	/**
	 * Liefert die Anzahl aller vom Default abweichenden {@link StundenplanKalenderwochenzuordnung}-Objekte.
	 * <br>Laufzeit: O(1)
	 *
	 * @return die Anzahl aller vom Default abweichenden {@link StundenplanKalenderwochenzuordnung}-Objekte.
	 */
	public kalenderwochenzuordnungGetAnzahl() : number {
		return this._kwz_by_id.size();
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanKalenderwochenzuordnung}-Objekt.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idKWZ Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanKalenderwochenzuordnung}-Objekt.
	 */
	public kalenderwochenzuordnungGetByIdOrException(idKWZ : number) : StundenplanKalenderwochenzuordnung {
		return DeveloperNotificationException.ifMapGetIsNull(this._kwz_by_id, idKWZ);
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
	public kalenderwochenzuordnungGetByJahrAndKWOrException(jahr : number, kalenderwoche : number) : StundenplanKalenderwochenzuordnung {
		return DeveloperNotificationException.ifMap2DGetIsNull(this._kwz_by_jahr_and_kw, jahr, kalenderwoche);
	}

	/**
	 * Liefert das dem Jahr und der Kalenderwoche zugeordnete {@link StundenplanKalenderwochenzuordnung}-Objekt der Auswahl-Menge oder das nächstmöglichste.
	 * <br>Hinweis: Einige Objekte dieser Menge können die ID = -1 haben, falls sie erzeugt wurden und nicht aus der DB stammen.
	 * <br>Laufzeit: O(1)
	 *
	 * @param jahr           Das Jahr der Kalenderwoche.
	 * @param kalenderwoche  Die gewünschte Kalenderwoche.
	 *
	 * @return das dem Jahr und der Kalenderwoche zugeordnete {@link StundenplanKalenderwochenzuordnung}-Objekt der Auswahl-Menge oder das nächstmöglichste.
	 */
	public kalenderwochenzuordnungGetByJahrAndKWOrClosest(jahr : number, kalenderwoche : number) : StundenplanKalenderwochenzuordnung {
		const kwz : StundenplanKalenderwochenzuordnung | null = this._kwz_by_jahr_and_kw.getOrNull(jahr, kalenderwoche);
		if (kwz !== null)
			return kwz;
		const kwzFirst : StundenplanKalenderwochenzuordnung = DeveloperNotificationException.ifListGetFirstFailes("_kwz_by_jahr_and_kw", this._kwzmenge_sortiert_alle);
		if ((jahr < kwzFirst.jahr) || ((jahr === kwzFirst.jahr) && (kalenderwoche < kwzFirst.kw)))
			return kwzFirst;
		return DeveloperNotificationException.ifListGetLastFailes("_kwz_by_jahr_and_kw", this._kwzmenge_sortiert_alle);
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
	public kalenderwochenzuordnungGetByDatum(datumISO8601 : string) : StundenplanKalenderwochenzuordnung {
		const e : Array<number> | null = DateUtils.extractFromDateISO8601(datumISO8601);
		const kwJahr : number = e[6];
		const kw : number = e[5];
		return this.kalenderwochenzuordnungGetByJahrAndKWOrClosest(kwJahr, kw);
	}

	/**
	 * Liefert das zum Jahr und KW zugehörige {@link StundenplanKalenderwochenzuordnung}-Objekt, oder NULL.
	 *
	 * @param jahr           Das Jahr der Kalenderwoche. Es muss zwischen {@link DateUtils#MIN_GUELTIGES_JAHR} und {@link DateUtils#MAX_GUELTIGES_JAHR} liegen.
	 * @param kalenderwoche  Die gewünschten Kalenderwoche. Es muss zwischen 1 und {@link DateUtils#gibKalenderwochenOfJahr(int)} liegen.
	 *
	 * @return das zum Jahr und KW zugehörige {@link StundenplanKalenderwochenzuordnung}-Objekt, oder NULL.
	 */
	public kalenderwochenzuordnungGueltigGetByDatumOrNull(jahr : number, kalenderwoche : number) : StundenplanKalenderwochenzuordnung | null {
		const kwz : StundenplanKalenderwochenzuordnung | null = this._kwz_by_jahr_and_kw.getOrNull(jahr, kalenderwoche);
		if (kwz === null)
			return null;
		const infoVon : Array<number> = DateUtils.extractFromDateISO8601(this._stundenplanGueltigAb);
		const infoBis : Array<number> = DateUtils.extractFromDateISO8601(this._stundenplanGueltigBis);
		const jahrVon : number = infoVon[6];
		const jahrBis : number = infoBis[6];
		const kwVon : number = infoVon[5];
		const kwBis : number = infoBis[5];
		const istKleiner : boolean = (kwz.jahr < jahrVon) || ((kwz.jahr === jahrVon) && (kwz.kw < kwVon));
		const istGroesser : boolean = (kwz.jahr > jahrBis) || ((kwz.jahr === jahrBis) && (kwz.kw > kwBis));
		return (istKleiner || istGroesser) ? null : kwz;
	}

	/**
	 * Liefert eine sortierte Liste aller {@link StundenplanKalenderwochenzuordnung}-Objekte,
	 *         die innerhalb der Datumsgrenzen des Stundenplanes liegen.
	 * <br>Hinweis: Einige Objekte dieser Menge können die ID = -1 haben, falls sie erzeugt wurden und nicht aus der DB stammen.
	 * <br>Laufzeit: O(1)
	 *
	 * @return eine sortierte Liste aller {@link StundenplanKalenderwochenzuordnung}-Objekte,
	 *         die innerhalb der Datumsgrenzen des Stundenplanes liegen.
	 */
	public kalenderwochenzuordnungGetMengeGueltigeAsList() : List<StundenplanKalenderwochenzuordnung> {
		return this._kwzmenge_sortiert_gueltige;
	}

	/**
	 * Liefert eine sortierte Liste aller {@link StundenplanKalenderwochenzuordnung}-Objekte.
	 * <br>Hinweis: Einige Objekte dieser Menge können die ID = -1 haben, falls sie erzeugt wurden und nicht aus der DB stammen.
	 * <br>Laufzeit: O(1)
	 *
	 * @return eine sortierte Liste aller {@link StundenplanKalenderwochenzuordnung}-Objekte.
	 */
	public kalenderwochenzuordnungGetMengeAsList() : List<StundenplanKalenderwochenzuordnung> {
		return this._kwzmenge_sortiert_alle;
	}

	/**
	 * Liefert eine sortierte Liste aller {@link StundenplanKalenderwochenzuordnung}-Objekte in inverser Reihenfolge.
	 * <br>Hinweis: Einige Objekte dieser Menge können die ID = -1 haben, falls sie erzeugt wurden und nicht aus der DB stammen.
	 * <br>Laufzeit: O(1)
	 *
	 * @return eine sortierte Liste aller {@link StundenplanKalenderwochenzuordnung}-Objekte in inverser Reihenfolge.
	 */
	public kalenderwochenzuordnungGetMengeInversAsList() : List<StundenplanKalenderwochenzuordnung> {
		return this._kwzmenge_sortiert_alle_invers;
	}

	/**
	 * Liefert eine sortierte Liste aller {@link StundenplanKalenderwochenzuordnung}-Objekte eines bestimmten Wochentyps.
	 * <br>Hinweis: Einige Objekte dieser Menge können die ID = -1 haben, falls sie erzeugt wurden und nicht aus der DB stammen.
	 * <br>Laufzeit: O(1)
	 *
	 * @param wochentyp  Der Typ der Woche (beispielsweise bei AB-Wochen).
	 *
	 * @return eine sortierte Liste aller {@link StundenplanKalenderwochenzuordnung}-Objekte eines bestimmten Wochentyps.
	 */
	public kalenderwochenzuordnungGetMengeByWochentyp(wochentyp : number) : List<StundenplanKalenderwochenzuordnung> {
		return MapUtils.getOrCreateArrayList(this._kwzmenge_by_wochentyp, wochentyp);
	}

	/**
	 * Liefert eine sortierte Liste aller {@link StundenplanKalenderwochenzuordnung}-Objekte, welche außerhalb des gültigen Datumsbereiches liegen.
	 * <br>Hinweis: Wenn die Map Objekte enthält, dann haben diese eine gültige Datenbank-ID.
	 * <br>Laufzeit: O(1)
	 *
	 * @return eine sortierte Liste aller {@link StundenplanKalenderwochenzuordnung}-Objekte, welche außerhalb des gültigen Datumsbereiches liegen.
	 */
	public kalenderwochenzuordnungGetMengeUngueltige() : List<StundenplanKalenderwochenzuordnung> {
		return this._kwzmenge_sortiert_ungueltige;
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
	public kalenderwochenzuordnungGetNextOrNull(kwz : StundenplanKalenderwochenzuordnung) : StundenplanKalenderwochenzuordnung | null {
		this.kalenderwochenzuordnungCheckAttributes(kwz, false);
		const max : number = DateUtils.gibKalenderwochenOfJahr(kwz.jahr);
		return (kwz.kw < max) ? this._kwz_by_jahr_and_kw.getOrNull(kwz.jahr, kwz.kw + 1) : this._kwz_by_jahr_and_kw.getOrNull(kwz.jahr + 1, 1);
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
	public kalenderwochenzuordnungGetPrevOrNull(kwz : StundenplanKalenderwochenzuordnung) : StundenplanKalenderwochenzuordnung | null {
		this.kalenderwochenzuordnungCheckAttributes(kwz, false);
		const max : number = DateUtils.gibKalenderwochenOfJahr(kwz.jahr - 1);
		return (kwz.kw > 1) ? this._kwz_by_jahr_and_kw.getOrNull(kwz.jahr, kwz.kw - 1) : this._kwz_by_jahr_and_kw.getOrNull(kwz.jahr - 1, max);
	}

	/**
	 * Liefert eine String-Darstellung der Kalenderwoche des {@link StundenplanKalenderwochenzuordnung}-Objekts.
	 * <br>Beispiel: Jahr 2023, KW  5 --> "KW 5 (30.01.2023–05.02.2023)"
	 * <br>Beispiel: Jahr 2025, KW  1 --> "KW 1 (30.12.2024–05.01.2025)"
	 * <br>Beispiel: Jahr 2026, KW 53 --> "KW 53 (28.12.2026–03.01.2027)"
	 * <br>Laufzeit: O(1)
	 *
	 * @param kwz  Das {@link StundenplanKalenderwochenzuordnung}-Objekt.
	 *
	 * @return eine String-Darstellung der Kalenderwoche des {@link StundenplanKalenderwochenzuordnung}-Objekts.
	 */
	public kalenderwochenzuordnungGetWocheAsString(kwz : StundenplanKalenderwochenzuordnung) : string {
		const sMo : string = DateUtils.gibDatumDesMontagsOfJahrAndKalenderwoche(kwz.jahr, kwz.kw);
		const sSo : string = DateUtils.gibDatumDesSonntagsOfJahrAndKalenderwoche(kwz.jahr, kwz.kw);
		const sMoGer : string = DateUtils.gibDatumGermanFormat(sMo);
		const sSoGer : string = DateUtils.gibDatumGermanFormat(sSo);
		const sJahrKW : string = "KW " + kwz.kw;
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
	public kalenderwochenzuordnungGetWochentypOrDefault(jahr : number, kalenderwoche : number) : number {
		DeveloperNotificationException.ifSmaller("jahr", jahr, DateUtils.MIN_GUELTIGES_JAHR);
		DeveloperNotificationException.ifGreater("jahr", jahr, DateUtils.MAX_GUELTIGES_JAHR);
		DeveloperNotificationException.ifSmaller("kalenderwoche", kalenderwoche, 1);
		DeveloperNotificationException.ifGreater("kalenderwoche", kalenderwoche, DateUtils.gibKalenderwochenOfJahr(jahr));
		if (this._stundenplanWochenTypModell === 0)
			return 0;
		const z : StundenplanKalenderwochenzuordnung | null = this._kwz_by_jahr_and_kw.getOrNull(jahr, kalenderwoche);
		if (z !== null)
			return z.wochentyp;
		const wochentyp : number = kalenderwoche % this._stundenplanWochenTypModell;
		return (wochentyp === 0) ? this._stundenplanWochenTypModell : wochentyp;
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
	public kalenderwochenzuordnungGetWochentypUsesMapping(jahr : number, kalenderwoche : number) : boolean {
		DeveloperNotificationException.ifSmaller("jahr", jahr, DateUtils.MIN_GUELTIGES_JAHR);
		DeveloperNotificationException.ifGreater("jahr", jahr, DateUtils.MAX_GUELTIGES_JAHR);
		DeveloperNotificationException.ifSmaller("kalenderwoche", kalenderwoche, 1);
		DeveloperNotificationException.ifGreater("kalenderwoche", kalenderwoche, DateUtils.gibKalenderwochenOfJahr(jahr));
		const z : StundenplanKalenderwochenzuordnung | null = this._kwz_by_jahr_and_kw.getOrNull(jahr, kalenderwoche);
		if (z === null)
			return false;
		return (this._stundenplanWochenTypModell >= 2) && (z.id >= 0);
	}

	/**
	 * Aktualisiert das vorhandene {@link StundenplanKalenderwochenzuordnung}-Objekt durch das neue Objekt.
	 * <br>
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
	public kalenderwochenzuordnungPatchAttributes(kwz : StundenplanKalenderwochenzuordnung) : void {
		this.kalenderwochenzuordnungPatchAll(ListUtils.create1(kwz));
	}

	/**
	 * Aktualisiert alle vorhandenen {@link StundenplanKalenderwochenzuordnung}-Objekte der Liste.
	 * <br>
	 * <br>Die folgenden Attribute werden nicht aktualisiert:
	 * <br>{@link StundenplanKalenderwochenzuordnung#id}
	 * <br>
	 * <br>Die folgenden Attribute werden kopiert:
	 * <br>{@link StundenplanKalenderwochenzuordnung#jahr}
	 * <br>{@link StundenplanKalenderwochenzuordnung#kw}
	 * <br>{@link StundenplanKalenderwochenzuordnung#wochentyp}
	 *
	 * @param kwzList  Die Liste der zu aktualisierenden {@link StundenplanKalenderwochenzuordnung}-Objekte.
	 */
	public kalenderwochenzuordnungPatchAll(kwzList : List<StundenplanKalenderwochenzuordnung>) : void {
		for (const kwz of kwzList)
			this.kalenderwochenzuordnungCheckAttributes(kwz, true);
		for (const kwz of kwzList) {
			DeveloperNotificationException.ifMapRemoveFailes(this._kwz_by_id, kwz.id);
			DeveloperNotificationException.ifMapPutOverwrites(this._kwz_by_id, kwz.id, kwz);
		}
		this.update_all();
	}

	private kalenderwochenzuordnungRemoveOhneUpdateById(idKWZ : number) : void {
		DeveloperNotificationException.ifMapRemoveFailes(this._kwz_by_id, idKWZ);
	}

	/**
	 * Entfernt ein {@link StundenplanKalenderwochenzuordnung}-Objekt anhand seiner Datenbank-ID.
	 *
	 * @param idKWZ  Die Datenbank-ID des {@link StundenplanKalenderwochenzuordnung}-Objekts, welches entfernt werden soll.
	 */
	public kalenderwochenzuordnungRemoveById(idKWZ : number) : void {
		this.kalenderwochenzuordnungRemoveOhneUpdateById(idKWZ);
		this.update_all();
	}

	/**
	 * Entfernt alle {@link StundenplanKalenderwochenzuordnung}-Objekte.
	 *
	 * @param listKWZ  Die Liste der zu entfernenden {@link StundenplanKalenderwochenzuordnung}-Objekte.
	 */
	public kalenderwochenzuordnungRemoveAll(listKWZ : List<StundenplanKalenderwochenzuordnung>) : void {
		for (const kwz of listKWZ)
			this.kalenderwochenzuordnungRemoveOhneUpdateById(kwz.id);
		this.update_all();
	}

	/**
	 * Fügt ein {@link StundenplanKlasse}-Objekt hinzu.
	 *
	 * @param klasse  Das {@link StundenplanKlasse}-Objekt, welches hinzugefügt werden soll.
	 */
	public klasseAdd(klasse : StundenplanKlasse) : void {
		this.klasseAddAll(ListUtils.create1(klasse));
	}

	/**
	 * Fügt alle {@link StundenplanKlasse}-Objekte hinzu.
	 *
	 * @param listKlasse  Die Menge der {@link StundenplanKlasse}-Objekte, welche hinzugefügt werden soll.
	 */
	public klasseAddAll(listKlasse : List<StundenplanKlasse>) : void {
		this.klasseAddAllOhneUpdate(listKlasse);
		this.update_all();
	}

	private klasseAddAllOhneUpdate(list : List<StundenplanKlasse>) : void {
		const setOfIDs : JavaSet<number> = new HashSet<number>();
		for (const klasse of list) {
			this.klasseCheckAttributes(klasse);
			DeveloperNotificationException.ifTrue("In der Menge der hinzuzufügenden Klassen ist die ID=" + klasse.id + " bereits existent!", this._klasse_by_id.containsKey(klasse.id));
			DeveloperNotificationException.ifTrue("In der Menge der hinzuzufügenden Klassen ist die ID=" + klasse.id + " doppelt in der Liste!", !setOfIDs.add(klasse.id));
		}
		for (const klasse of list)
			DeveloperNotificationException.ifMapPutOverwrites(this._klasse_by_id, klasse.id, klasse);
	}

	private klasseCheckAttributes(klasse : StundenplanKlasse) : void {
		DeveloperNotificationException.ifInvalidID("klasse.id", klasse.id);
		DeveloperNotificationException.ifTrue("Das Kürzel der Klasse darf nicht leer sein!", JavaString.isBlank(klasse.kuerzel));
		for (const idJahrgang of klasse.jahrgaenge)
			DeveloperNotificationException.ifMapNotContains("_jahrgang_by_id", this._jahrgang_by_id, idJahrgang);
		for (const idSchueler of klasse.schueler)
			DeveloperNotificationException.ifMapNotContains("_schueler_by_id", this._schueler_by_id, idSchueler);
	}

	/**
	 * Liefert das {@link StundenplanKlasse}-Objekt mit der übergebenen ID.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idKlasse  Die Datenbank-ID des {@link StundenplanKlasse}-Objekts.
	 *
	 * @return das {@link StundenplanKlasse}-Objekt mit der übergebenen ID.
	 */
	public klasseGetByIdOrException(idKlasse : number) : StundenplanKlasse {
		return DeveloperNotificationException.ifMapGetIsNull(this._klasse_by_id, idKlasse);
	}

	/**
	 * Liefert eine Beschreibung des {@link StundenplanKlasse}-Objekts in der Form "7b".
	 * <br>Hinweis: Diese Methode liefert niemals eine Exception, stattdessen enthält der String eine Fehlermeldung.
	 *
	 * @param idKlasse Die ID des angefragten-Objektes.
	 *
	 * @return eine Beschreibung des {@link StundenplanKlasse}-Objekts in der Form "7b".
	 */
	public klasseGetBeschreibungKuerzel(idKlasse : number) : string {
		const kl : StundenplanKlasse | null = this._klasse_by_id.get(idKlasse);
		if (kl === null)
			return "Klassen-Objekt [" + idKlasse + "] ???";
		return kl.kuerzel;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanKlasse}-Objekte.
	 * <br>Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link StundenplanKlasse}-Objekte.
	 */
	public klasseGetMengeAsList() : List<StundenplanKlasse> {
		return this._klassenmenge_sortiert;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanKlasse}-Objekte, die derzeit verwendet werden.
	 * <br>Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link StundenplanKlasse}-Objekte, die derzeit verwendet werden.
	 */
	public klasseGetMengeVerwendetAsList() : List<StundenplanKlasse> {
		return this._klassenmenge_verwendet_sortiert;
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
	public klassePatchAttributes(klasse : StundenplanKlasse) : void {
		this.klasseCheckAttributes(klasse);
		DeveloperNotificationException.ifMapRemoveFailes(this._klasse_by_id, klasse.id);
		DeveloperNotificationException.ifMapPutOverwrites(this._klasse_by_id, klasse.id, klasse);
		this.update_all();
	}

	private klasseRemoveOhneUpdateById(idKlasse : number) : void {
		for (const u of MapUtils.getOrCreateArrayList(this._klassenunterrichtmenge_by_idKlasse, idKlasse))
			this.klassenunterrichtRemoveOhneUpdateById(u.idKlasse, u.idFach);
		for (const zeit of MapUtils.getOrCreateArrayList(this._pausenzeitmenge_by_idKlasse, idKlasse))
			zeit.klassen.remove(idKlasse);
		DeveloperNotificationException.ifMapRemoveFailes(this._klasse_by_id, idKlasse);
	}

	/**
	 * Entfernt ein {@link StundenplanKlasse}-Objekt anhand seiner ID.
	 *
	 * @param idKlasse  Die Datenbank-ID des {@link StundenplanKlasse}-Objekts, welches entfernt werden soll.
	 */
	public klasseRemoveById(idKlasse : number) : void {
		this.klasseRemoveOhneUpdateById(idKlasse);
		this.update_all();
	}

	/**
	 * Entfernt alle {@link StundenplanKlasse}-Objekte.
	 *
	 * @param listKlasse  Die Liste der zu entfernenden {@link StundenplanKlasse}-Objekte.
	 */
	public klasseRemoveAll(listKlasse : List<StundenplanKlasse>) : void {
		for (const klasse of listKlasse)
			this.klasseRemoveOhneUpdateById(klasse.id);
		this.update_all();
	}

	/**
	 * Fügt ein {@link StundenplanKlassenunterricht}-Objekt hinzu.
	 *
	 * @param klassenunterricht  Das {@link StundenplanKlassenunterricht}-Objekt, welches hinzugefügt werden soll.
	 */
	public klassenunterrichtAdd(klassenunterricht : StundenplanKlassenunterricht) : void {
		this.klassenunterrichtAddAll(ListUtils.create1(klassenunterricht));
	}

	/**
	 * Fügt alle {@link StundenplanKlassenunterricht}-Objekte hinzu.
	 *
	 * @param listKlassenunterricht  Die Menge der {@link StundenplanKlassenunterricht}-Objekte, welche hinzugefügt werden soll.
	 */
	public klassenunterrichtAddAll(listKlassenunterricht : List<StundenplanKlassenunterricht>) : void {
		this.klassenunterrichtAddAllOhneUpdate(listKlassenunterricht);
		this.update_all();
	}

	private klassenunterrichtAddAllOhneUpdate(list : List<StundenplanKlassenunterricht>) : void {
		const setOfIDs : JavaSet<string> = new HashSet<string>();
		for (const klassenunterricht of list) {
			this.klassenunterrichtCheckAttributes(klassenunterricht);
			DeveloperNotificationException.ifTrue("klassenunterrichtAddAllOhneUpdate: KLASSE=" + klassenunterricht.idKlasse + ", FACH=" + klassenunterricht.idFach + " existiert bereits!", this._klassenunterricht_by_idKlasse_and_idFach.contains(klassenunterricht.idKlasse, klassenunterricht.idFach));
			DeveloperNotificationException.ifTrue("klassenunterrichtAddAllOhneUpdate: ID=" + klassenunterricht.idKlasse + ", FACH=" + klassenunterricht.idFach + " doppelt in der Liste!", !setOfIDs.add(klassenunterricht.idKlasse + ";" + klassenunterricht.idFach));
		}
		for (const klassenunterricht of list)
			DeveloperNotificationException.ifMap2DPutOverwrites(this._klassenunterricht_by_idKlasse_and_idFach, klassenunterricht.idKlasse, klassenunterricht.idFach, klassenunterricht);
	}

	private klassenunterrichtCheckAttributes(klassenunterricht : StundenplanKlassenunterricht) : void {
		DeveloperNotificationException.ifMapNotContains("_klasse_by_id", this._klasse_by_id, klassenunterricht.idKlasse);
		DeveloperNotificationException.ifMapNotContains("_fach_by_id", this._fach_by_id, klassenunterricht.idFach);
		for (const idSchiene of klassenunterricht.schienen)
			DeveloperNotificationException.ifMapNotContains("_schiene_by_id", this._schiene_by_id, idSchiene);
		for (const idLehrkraft of klassenunterricht.lehrer)
			if (!this._lehrer_by_id.containsKey(idLehrkraft))
				this.lehrerAddPseudoLehrkraftOhneUpdate(idLehrkraft, "Klasseunterricht " + this.klassenunterrichtGetBeschreibungKlasseAndFach(klassenunterricht) + " hat ungültige Lehrerreferenz.");
	}

	private klassenunterrichtCreateComparator() : Comparator<StundenplanKlassenunterricht> {
		const comp : Comparator<StundenplanKlassenunterricht> = { compare : (a: StundenplanKlassenunterricht, b: StundenplanKlassenunterricht) => {
			const aKlasse : StundenplanKlasse = DeveloperNotificationException.ifMapGetIsNull(this._klasse_by_id, a.idKlasse);
			const bKlasse : StundenplanKlasse = DeveloperNotificationException.ifMapGetIsNull(this._klasse_by_id, b.idKlasse);
			const cmpKlasse : number = StundenplanUnterrichtUtils.comparatorKlassen.compare(aKlasse, bKlasse);
			if (cmpKlasse !== 0)
				return cmpKlasse;
			const aFach : StundenplanFach = DeveloperNotificationException.ifMapGetIsNull(this._fach_by_id, a.idFach);
			const bFach : StundenplanFach = DeveloperNotificationException.ifMapGetIsNull(this._fach_by_id, b.idFach);
			const cmpFach : number = StundenplanManager._compFach.compare(aFach, bFach);
			if (cmpFach !== 0)
				return cmpFach;
			const cmpLehrer : number = this.lehrerCompareByLehrerIDs(a.lehrer, b.lehrer);
			if (cmpLehrer !== 0)
				return cmpLehrer;
			if (a.wochenstunden < b.wochenstunden)
				return -1;
			if (a.wochenstunden > b.wochenstunden)
				return +1;
			return JavaString.compareTo(a.bezeichnung, b.bezeichnung);
		} };
		return comp;
	}

	/**
	 * Liefert das {@link StundenplanKlassenunterricht}-Objekt der Klasse mit einem bestimmten Fach.
	 *
	 * @param idKlasse   Die Datenbank-ID der Klasse.
	 * @param idFach     Die Datenbank-ID der Faches
	 *
	 * @return das {@link StundenplanKlassenunterricht}-Objekte der Klasse mit einem bestimmten Fach.
	 */
	public klassenunterrichtGetByKlasseIdAndFachIdOrException(idKlasse : number, idFach : number) : StundenplanKlassenunterricht {
		return this._klassenunterricht_by_idKlasse_and_idFach.getOrException(idKlasse, idFach);
	}

	/**
	 * Liefert eine String-Repräsentation {@link StundenplanKlassenunterricht}-Objekts.
	 *
	 * @param klassenunterricht    Das Objekt, welches dargestellt werden soll.
	 *
	 * @return eine String-Repräsentation {@link StundenplanKlassenunterricht}-Objekts.
	 */
	public klassenunterrichtGetBeschreibungKlasseAndFach(klassenunterricht : StundenplanKlassenunterricht) : string | null {
		const kl : StundenplanKlasse | null = this._klasse_by_id.get(klassenunterricht.idKlasse);
		const fa : StundenplanFach | null = this._fach_by_id.get(klassenunterricht.idFach);
		return (kl === null ? "Klasse ???" : kl.bezeichnung) + " " + (fa === null ? "Fach ???" : fa.bezeichnung);
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
	public klassenunterrichtDarfInZelle(klassenunterricht : StundenplanKlassenunterricht, wochentag : number, stunde : number, wochentyp : number) : boolean {
		for (const partner of DeveloperNotificationException.ifMap2DGetIsNull(this._unterrichtmenge_by_idKlasse_and_idFach, klassenunterricht.idKlasse, klassenunterricht.idFach)) {
			const z : StundenplanZeitraster = DeveloperNotificationException.ifMap2DGetIsNull(this._zeitraster_by_wochentag_and_stunde, wochentag, stunde);
			if ((partner.idZeitraster === z.id) && ((partner.wochentyp === 0) || (wochentyp === 0) || (wochentyp === partner.wochentyp)))
				return false;
		}
		return true;
	}

	/**
	 * Liefert eine Liste aller Kollisionen bestimmer Drag-Objekte in Bezug auf eine bestimmte Drop-Zelle.
	 *
	 * @param listKl      Drag: Die Liste der {@link StundenplanKlassenunterricht}-Objekte.
	 * @param listKu      Drag: Die Liste der {@link StundenplanKurs}-Objekte.
	 * @param listU       Drag: Die Liste der {@link StundenplanUnterricht}-Objekte.
	 * @param wochentag   Drop: Der Typ des {@link Wochentag}-Objekts.
	 * @param stunde      Drop: Die Unterrichtsstunde an dem Wochentag.
	 * @param wochentyp   Drop: Der Typ der Woche (beispielsweise bei AB-Wochen).
	 *
	 * @return eine Liste aller Kollisionen bestimmer Drag-Objekte in Bezug auf eine bestimmte Drop-Zelle.
	 */
	public getDropKollisionsbeschreibungZurZelle(listKl : List<StundenplanKlassenunterricht>, listKu : List<StundenplanKurs>, listU : List<StundenplanUnterricht>, wochentag : number, stunde : number, wochentyp : number) : List<string> {
		const beschreibungen : List<string> = new ArrayList<string>();
		const dragLehrer : HashSet<number> = new HashSet<number>();
		const dragKlassen : HashSet<number> = new HashSet<number>();
		const dragSchueler : HashSet<number> = new HashSet<number>();
		const dragRaeume : HashSet<number> = new HashSet<number>();
		for (const kl of listKl) {
			dragLehrer.addAll(kl.lehrer);
			dragKlassen.add(kl.idKlasse);
			dragSchueler.addAll(kl.schueler);
		}
		for (const ku of listKu) {
			dragLehrer.addAll(ku.lehrer);
			dragSchueler.addAll(ku.schueler);
		}
		for (const u of listU) {
			dragLehrer.addAll(u.lehrer);
			dragKlassen.addAll(u.klassen);
			for (const schueler of this.schuelerGetMengeByUnterrichtIdAsList(u.id))
				dragSchueler.add(schueler.id);
			dragRaeume.addAll(u.raeume);
		}
		for (const idLehrkraft of dragLehrer)
			for (const u2 of this.unterrichtGetMengeByLehrerIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(idLehrkraft, wochentag, stunde, wochentyp, true))
				beschreibungen.add("L-Kollision mit " + this.unterrichtGetByID_StringOfFaLeKl(u2.id));
		for (const idKlasse of dragKlassen)
			for (const u2 of this.unterrichtGetMengeByKlasseIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(idKlasse, wochentag, stunde, wochentyp, true))
				beschreibungen.add("K-Kollision mit " + this.unterrichtGetByID_StringOfFaLeKl(u2.id));
		const listS : List<string> = new ArrayList<string>();
		for (const idSchueler of dragSchueler)
			for (const u2 of this.unterrichtGetMengeBySchuelerIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(idSchueler, wochentag, stunde, wochentyp, true))
				listS.add("S-Kollision mit " + this.unterrichtGetByID_StringOfFaLeKl(u2.id));
		if (listS.size() === 1)
			beschreibungen.add(ListUtils.getNonNullElementAtOrException(listS, 0));
		if (listS.size() >= 2)
			beschreibungen.add(ListUtils.getNonNullElementAtOrException(listS, 0) + " (+ " + (listS.size() - 1) + " weitere)");
		for (const idRaum of dragRaeume)
			for (const u2 of this.unterrichtGetMengeByRaumIdAndWochentagAndStundeAndWochentypAndSchieneAndInklusiveOrEmptyList(idRaum, wochentag, stunde, wochentyp, -2, true))
				beschreibungen.add("R-Kollision mit " + this.unterrichtGetByID_StringOfFaLeKl(u2.id));
		return beschreibungen;
	}

	/**
	 * Liefert eine Liste aller Kollisionen des {@link StundenplanKlassenunterricht}-Objekte in Bezug auf eine bestimmte Zelle.
	 * <br>Hinweis: Das sind Kollision von Lehrkärften, Klassen oder SuS.
	 *
	 * @param klassenU    Der {@link StundenplanKlassenunterricht}, welcher gesetzt werden soll.
	 * @param wochentag   Der Typ des {@link Wochentag}-Objekts.
	 * @param stunde      Die Unterrichtsstunde an dem Wochentag.
	 * @param wochentyp   Der Typ der Woche (beispielsweise bei AB-Wochen).
	 *
	 * @return eine Liste aller Kollisionen des {@link StundenplanKlassenunterricht}-Objekte in Bezug auf eine bestimmte Zelle.
	 */
	public klassenunterrichtGetDropKollisionsbeschreibungZurZelle(klassenU : StundenplanKlassenunterricht, wochentag : number, stunde : number, wochentyp : number) : List<string> {
		const beschreibungen : List<string> = new ArrayList<string>();
		for (const idLehrkraft of klassenU.lehrer)
			for (const u2 of this.unterrichtGetMengeByLehrerIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(idLehrkraft, wochentag, stunde, wochentyp, true))
				beschreibungen.add("Lehrkraft " + this.lehrerGetBeschreibungKuerzel(idLehrkraft) + " kollidert mit " + this.unterrichtGetByID_StringOfFaLeKl(u2.id));
		for (const u2 of this.unterrichtGetMengeByKlasseIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(klassenU.idKlasse, wochentag, stunde, wochentyp, true))
			beschreibungen.add("Klasse " + klassenU.bezeichnung + " kollidert mit " + this.unterrichtGetByID_StringOfFaLeKl(u2.id));
		for (const idSchueler of klassenU.schueler) {
			const listS : List<string> = new ArrayList<string>();
			for (const u2 of this.unterrichtGetMengeBySchuelerIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(idSchueler, wochentag, stunde, wochentyp, true))
				listS.add("Schueler " + this.schuelerGetBeschreibungVornameNachname(idSchueler) + " kollidert mit " + this.unterrichtGetByID_StringOfFaLeKl(u2.id));
			if (listS.size() === 1)
				beschreibungen.add(ListUtils.getNonNullElementAtOrException(listS, 0));
			if (listS.size() >= 2)
				beschreibungen.add(ListUtils.getNonNullElementAtOrException(listS, 0) + " [... und " + (listS.size() - 1) + " weitere SuS]");
		}
		return beschreibungen;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanKlassenunterricht}-Objekte.
	 * <br>Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link StundenplanKlassenunterricht}-Objekte.
	 */
	public klassenunterrichtGetMengeAsList() : List<StundenplanKlassenunterricht> {
		return this._klassenunterrichtmenge;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanKlassenunterricht}-Objekte der Klasse.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idKlasse  Die Datenbank-ID der Klasse.
	 *
	 * @return eine Liste aller {@link StundenplanKlassenunterricht}-Objekte der Klasse.
	 */
	public klassenunterrichtGetMengeByKlasseIdAsList(idKlasse : number) : List<StundenplanKlassenunterricht> {
		return MapUtils.getOrCreateArrayList(this._klassenunterrichtmenge_by_idKlasse, idKlasse);
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
	public klassenunterrichtGetMengeByKlasseIdAndSchieneId(idKlasse : number, idSchiene : number) : List<StundenplanKlassenunterricht> {
		return Map2DUtils.getOrCreateArrayList(this._klassenunterrichtmenge_by_idKlasse_and_idSchiene, idKlasse, idSchiene);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanKlassenunterricht}-Objekte des Lehrers.
	 * <br> Laufzeit: O(1)
	 *
	 * @param idLehrer  Die Datenbank-ID des Lehrers.
	 *
	 * @return eine Liste aller {@link StundenplanKlassenunterricht}-Objekte des Lehrers.
	 */
	public klassenunterrichtGetMengeByLehrerIdAsList(idLehrer : number) : List<StundenplanKlassenunterricht> {
		return MapUtils.getOrCreateArrayList(this._klassenunterrichtmenge_by_idLehrer, idLehrer);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanKlassenunterricht}-Objekte des Schülers.
	 * <br> Laufzeit: O(1)
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return eine Liste aller {@link StundenplanKlassenunterricht}-Objekte des Schülers.
	 */
	public klassenunterrichtGetMengeBySchuelerIdAsList(idSchueler : number) : List<StundenplanKlassenunterricht> {
		return MapUtils.getOrCreateArrayList(this._klassenunterrichtmenge_by_idSchueler, idSchueler);
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
	public klassenunterrichtGetWochenminutenSOLL(idKlasse : number, idFach : number) : number {
		return DeveloperNotificationException.ifMap2DGetIsNull(this._klassenunterricht_by_idKlasse_and_idFach, idKlasse, idFach).wochenstunden * StundenplanManager.FAKTOR_WOCHENSTUNDEN_ZU_MINUTEN;
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
	public klassenunterrichtGetWochenminutenIST(idKlasse : number, idFach : number) : number {
		const wochenminuten : number = DeveloperNotificationException.ifMap2DGetIsNull(this._wertWochenminuten_by_idKlasse_und_idFach, idKlasse, idFach).valueOf();
		return StundenplanManager.gerundetAufZweiNachkommastellen(wochenminuten);
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
	public klassenunterrichtGetWochenminutenREST(idKlasse : number, idFach : number) : number {
		return this.klassenunterrichtGetWochenminutenSOLL(idKlasse, idFach) - this.klassenunterrichtGetWochenminutenIST(idKlasse, idFach);
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
	public klassenunterrichtGetWochenstundenSOLL(idKlasse : number, idFach : number) : number {
		return DeveloperNotificationException.ifMap2DGetIsNull(this._klassenunterricht_by_idKlasse_and_idFach, idKlasse, idFach).wochenstunden;
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
	public klassenunterrichtGetWochenstundenIST(idKlasse : number, idFach : number) : number {
		const wochenminuten : number = DeveloperNotificationException.ifMap2DGetIsNull(this._wertWochenminuten_by_idKlasse_und_idFach, idKlasse, idFach).valueOf();
		return StundenplanManager.gerundetAufZweiNachkommastellen(wochenminuten / StundenplanManager.FAKTOR_WOCHENSTUNDEN_ZU_MINUTEN);
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
	public klassenunterrichtGetWochenstundenREST(idKlasse : number, idFach : number) : number {
		return this.klassenunterrichtGetWochenstundenSOLL(idKlasse, idFach) - this.klassenunterrichtGetWochenstundenIST(idKlasse, idFach);
	}

	private klassenunterrichtRemoveOhneUpdateById(idKlasse : number, idFach : number) : void {
		for (const u of DeveloperNotificationException.ifMap2DGetIsNull(this._unterrichtmenge_by_idKlasse_and_idFach, idKlasse, idFach))
			this.unterrichtRemoveByIdOhneUpdate(u.id);
		DeveloperNotificationException.ifMap2DRemoveFailes(this._klassenunterricht_by_idKlasse_and_idFach, idKlasse, idFach);
	}

	/**
	 * Entfernt ein {@link StundenplanKlassenunterricht}-Objekt anhand seiner ID.
	 *
	 * @param idKlasse  Die Datenbank-ID der Klasse.
	 * @param idFach    Die Datenbank-ID des Faches.
	 */
	public klassenunterrichtRemoveById(idKlasse : number, idFach : number) : void {
		this.klassenunterrichtRemoveOhneUpdateById(idKlasse, idFach);
		this.update_all();
	}

	/**
	 * Entfernt alle {@link StundenplanKlassenunterricht}-Objekte.
	 *
	 * @param listKlassenunterricht  Die Liste der zu entfernenden {@link StundenplanKlassenunterricht}-Objekte.
	 */
	public klassenunterrichtRemoveAll(listKlassenunterricht : List<StundenplanKlassenunterricht>) : void {
		for (const klassenunterricht of listKlassenunterricht)
			this.klassenunterrichtRemoveOhneUpdateById(klassenunterricht.idKlasse, klassenunterricht.idFach);
		this.update_all();
	}

	private static gerundetAufZweiNachkommastellen(d : number) : number {
		if (d >= 0)
			return (Math.round(d * 100.0)) / 100.0;
		return -(Math.round(-d * 100.0)) / 100.0;
	}

	/**
	 * Fügt ein {@link StundenplanKurs}-Objekt hinzu.
	 *
	 * @param kurs  Das {@link StundenplanKurs}-Objekt, welches hinzugefügt werden soll.
	 */
	public kursAdd(kurs : StundenplanKurs) : void {
		this.kursAddAll(ListUtils.create1(kurs));
	}

	/**
	 * Fügt alle {@link StundenplanKurs}-Objekte hinzu.
	 *
	 * @param listKurs  Die Menge der {@link StundenplanKurs}-Objekte, welche hinzugefügt werden soll.
	 */
	public kursAddAll(listKurs : List<StundenplanKurs>) : void {
		this.kursAddAllOhneUpdate(listKurs);
		this.update_all();
	}

	private kursAddAllOhneUpdate(list : List<StundenplanKurs>) : void {
		const setOfIDs : JavaSet<number> = new HashSet<number>();
		for (const kurs of list) {
			this.kursCheckAttributes(kurs);
			DeveloperNotificationException.ifTrue("kursAddAllOhneUpdate: ID=" + kurs.id + " existiert bereits!", this._kurs_by_id.containsKey(kurs.id));
			DeveloperNotificationException.ifTrue("kursAddAllOhneUpdate: ID=" + kurs.id + " doppelt in der Liste!", !setOfIDs.add(kurs.id));
		}
		for (const kurs of list)
			DeveloperNotificationException.ifMapPutOverwrites(this._kurs_by_id, kurs.id, kurs);
	}

	private kursCheckAttributes(kurs : StundenplanKurs) : void {
		DeveloperNotificationException.ifInvalidID("kurs.id", kurs.id);
		DeveloperNotificationException.ifStringIsBlank("kurs.bezeichnung", kurs.bezeichnung);
		DeveloperNotificationException.ifSmaller("kurs.wochenstunden", kurs.wochenstunden, 0);
		for (const idSchieneDesKurses of kurs.schienen)
			DeveloperNotificationException.ifMapNotContains("_schiene_by_id", this._schiene_by_id, idSchieneDesKurses);
		for (const idJahrgangDesKurses of kurs.jahrgaenge)
			DeveloperNotificationException.ifMapNotContains("_jahrgang_by_id", this._jahrgang_by_id, idJahrgangDesKurses);
		for (const idSchuelerDesKurses of kurs.schueler)
			DeveloperNotificationException.ifMapNotContains("_schueler_by_id", this._schueler_by_id, idSchuelerDesKurses);
		for (const idLehrerDesKurses of kurs.lehrer)
			if (!this._lehrer_by_id.containsKey(idLehrerDesKurses))
				this.lehrerAddPseudoLehrkraftOhneUpdate(idLehrerDesKurses, "StundenplanKurs " + kurs.bezeichnung + " hat ungültige Lehrerreferenz.");
	}

	/**
	 * Liefert TRUE, falls der Kurs in das jeweilige Zeitraster gesetzt oder verschoben werden darf.
	 *
	 * @param quellKurs      Der {@link StundenplanKurs}, welcher gesetzt oder verschoben werden soll.
	 * @param zielWochentag  Der Typ des {@link Wochentag}-Objekts.
	 * @param zielStunde     Die Unterrichtsstunde an dem Wochentag.
	 * @param zielWochentyp  Der Typ der Woche (beispielsweise bei AB-Wochen).
	 *
	 * @return TRUE, falls der Kurs in das jeweilige Zeitraster gesetzt oder verschoben werden darf.
	 */
	public kursDarfInZelle(quellKurs : StundenplanKurs, zielWochentag : number, zielStunde : number, zielWochentyp : number) : boolean {
		for (const partner of DeveloperNotificationException.ifMapGetIsNull(this._unterrichtmenge_by_idKurs, quellKurs.id)) {
			const z : StundenplanZeitraster = DeveloperNotificationException.ifMap2DGetIsNull(this._zeitraster_by_wochentag_and_stunde, zielWochentag, zielStunde);
			if (partner.idZeitraster !== z.id)
				continue;
			if ((partner.wochentyp === 0) || (zielWochentyp === 0) || (zielWochentyp === partner.wochentyp))
				return false;
		}
		return true;
	}

	/**
	 * Liefert eine Liste aller Kollisionen des {@link StundenplanKurs}-Objekte in Bezug auf eine bestimmte Zelle.
	 * <br>Hinweis: Das sind Kollision von Lehrkärften oder SuS.
	 *
	 * @param kurs        Der {@link StundenplanKurs}, welcher gesetzt werden soll.
	 * @param wochentag   Der Typ des {@link Wochentag}-Objekts.
	 * @param stunde      Die Unterrichtsstunde an dem Wochentag.
	 * @param wochentyp   Der Typ der Woche (beispielsweise bei AB-Wochen).
	 *
	 * @return eine Liste aller Kollisionen des {@link StundenplanKurs}-Objekte in Bezug auf eine bestimmte Zelle.
	 */
	public kursGetDropKollisionsbeschreibungZurZelle(kurs : StundenplanKurs, wochentag : number, stunde : number, wochentyp : number) : List<string> {
		const beschreibungen : List<string> = new ArrayList<string>();
		for (const idLehrkraft of kurs.lehrer)
			for (const u2 of this.unterrichtGetMengeByLehrerIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(idLehrkraft, wochentag, stunde, wochentyp, true))
				beschreibungen.add("Lehrkraft " + this.lehrerGetBeschreibungKuerzel(idLehrkraft) + " kollidert mit " + this.unterrichtGetByID_StringOfFaLeKl(u2.id));
		for (const idSchueler of kurs.schueler) {
			const listS : List<string> = new ArrayList<string>();
			for (const u2 of this.unterrichtGetMengeBySchuelerIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(idSchueler, wochentag, stunde, wochentyp, true))
				listS.add("Schueler " + this.schuelerGetBeschreibungVornameNachname(idSchueler) + " kollidert mit " + this.unterrichtGetByID_StringOfFaLeKl(u2.id));
			if (listS.size() === 1)
				beschreibungen.add(ListUtils.getNonNullElementAtOrException(listS, 0));
			if (listS.size() >= 2)
				beschreibungen.add(ListUtils.getNonNullElementAtOrException(listS, 0) + " [... und " + (listS.size() - 1) + " weitere SuS]");
		}
		return beschreibungen;
	}

	/**
	 * Liefert das {@link StundenplanKurs}-Objekt mit der übergebenen ID.
	 *
	 * @param idKurs  Die Datenbank-ID des {@link StundenplanKurs}-Objekts.
	 *
	 * @return das {@link StundenplanKurs}-Objekt mit der übergebenen ID.
	 */
	public kursGetByIdOrException(idKurs : number) : StundenplanKurs {
		return DeveloperNotificationException.ifMapGetIsNull(this._kurs_by_id, idKurs);
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
	public kursGetHatUnterrichtAm(idKurs : number, wochentyp : number, wochentag : Wochentag, unterrichtstunde : number) : boolean {
		for (const u of this.unterrichtGetMengeByKursIdAndWochentyp(idKurs, wochentyp)) {
			const z : StundenplanZeitraster = this.zeitrasterGetByIdOrException(u.idZeitraster);
			if ((z.wochentag === wochentag.id) && (z.unterrichtstunde === unterrichtstunde))
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
	public kursGetMengeAsList() : List<StundenplanKurs> {
		return this._kursmenge_sortiert;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanKurs}-Objekte, die derzeit verwendet werden.
	 * <br>Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link StundenplanKurs}-Objekte, die derzeit verwendet werden.
	 */
	public kursGetMengeVerwendetAsList() : List<StundenplanKurs> {
		return this._kursmenge_verwendet_sortiert;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanKurs}-Objekte der Klasse.
	 * <br> Laufzeit: O(1)
	 *
	 * @param idKlasse  Die Datenbank-ID der Klasse.
	 *
	 * @return eine Liste aller {@link StundenplanKurs}-Objekte der Klasse.
	 */
	public kursGetMengeByKlasseIdAsList(idKlasse : number) : List<StundenplanKurs> {
		return MapUtils.getOrCreateArrayList(this._kursmenge_by_idKlasse, idKlasse);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanKurs}-Objekte des Lehrers.
	 * <br> Laufzeit: O(1)
	 *
	 * @param idLehrer  Die Datenbank-ID des Lehrers.
	 *
	 * @return eine Liste aller {@link StundenplanKurs}-Objekte des Lehrers.
	 */
	public kursGetMengeByLehrerIdAsList(idLehrer : number) : List<StundenplanKurs> {
		return MapUtils.getOrCreateArrayList(this._kursmenge_by_idLehrer, idLehrer);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanKurs}-Objekte des Schülers.
	 * <br> Laufzeit: O(1)
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return eine Liste aller {@link StundenplanKurs}-Objekte des Schülers.
	 */
	public kursGetMengeBySchuelerIdAsList(idSchueler : number) : List<StundenplanKurs> {
		return MapUtils.getOrCreateArrayList(this._kursmenge_by_idSchueler, idSchueler);
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
	public kursGetMengeGefiltertByWochentypAndWochentagAndStunde(idsKurs : List<number>, wochentyp : number, wochentag : Wochentag, unterrichtstunde : number) : List<number> {
		return CollectionUtils.toFilteredArrayList(idsKurs, { test : (idKurs: number) => this.kursGetHatUnterrichtAm(idKurs, wochentyp, wochentag, unterrichtstunde) });
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
	public kursGetMengeGefiltertByDatumAndStunde(idsKurs : List<number>, datumISO8601 : string, unterrichtstunde : number) : List<number> {
		const e : Array<number> | null = DateUtils.extractFromDateISO8601(datumISO8601);
		const wochentyp : number = this.kalenderwochenzuordnungGetWochentypOrDefault(e[6], e[5]);
		const wochentag : Wochentag = Wochentag.fromIDorException(e[3]);
		return this.kursGetMengeGefiltertByWochentypAndWochentagAndStunde(idsKurs, wochentyp, wochentag, unterrichtstunde);
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
	public kursGetMengeByKlasseIdAndSchieneId(idKlasse : number, idSchiene : number) : List<StundenplanKurs> {
		return Map2DUtils.getOrCreateArrayList(this._kursmenge_by_idKlasse_and_idSchiene, idKlasse, idSchiene);
	}

	/**
	 * Liefert die SOLL-Wochenminuten des {@link StundenplanKurs}.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die SOLL-Wochenminuten des {@link StundenplanKurs}.
	 */
	public kursGetWochenminutenSOLL(idKurs : number) : number {
		return DeveloperNotificationException.ifMapGetIsNull(this._kurs_by_id, idKurs).wochenstunden * StundenplanManager.FAKTOR_WOCHENSTUNDEN_ZU_MINUTEN;
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
	public kursGetWochenminutenIST(idKurs : number) : number {
		const wochenminuten : number = DeveloperNotificationException.ifMapGetIsNull(this._wertWochenminuten_by_idKurs, idKurs).valueOf();
		return StundenplanManager.gerundetAufZweiNachkommastellen(wochenminuten);
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
	public kursGetWochenminutenREST(idKurs : number) : number {
		return this.kursGetWochenminutenSOLL(idKurs) - this.kursGetWochenminutenIST(idKurs);
	}

	/**
	 * Liefert die SOLL-Wochenstunden des {@link StundenplanKurs}.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die SOLL-Wochenstunden des {@link StundenplanKurs}.
	 */
	public kursGetWochenstundenSOLL(idKurs : number) : number {
		return DeveloperNotificationException.ifMapGetIsNull(this._kurs_by_id, idKurs).wochenstunden;
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
	public kursGetWochenstundenIST(idKurs : number) : number {
		const wochenminuten : number = DeveloperNotificationException.ifMapGetIsNull(this._wertWochenminuten_by_idKurs, idKurs).valueOf();
		return StundenplanManager.gerundetAufZweiNachkommastellen(wochenminuten / StundenplanManager.FAKTOR_WOCHENSTUNDEN_ZU_MINUTEN);
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
	public kursGetWochenstundenREST(idKurs : number) : number {
		return this.kursGetWochenstundenSOLL(idKurs) - this.kursGetWochenstundenIST(idKurs);
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
	public kursPatchAttributtes(kurs : StundenplanKurs) : void {
		this.kursCheckAttributes(kurs);
		DeveloperNotificationException.ifMapRemoveFailes(this._kurs_by_id, kurs.id);
		DeveloperNotificationException.ifMapPutOverwrites(this._kurs_by_id, kurs.id, kurs);
		this.update_all();
	}

	private kursRemoveAllOhneUpdate(idKurse : JavaSet<number>) : void {
		for (const idKurs of idKurse)
			DeveloperNotificationException.ifTrue("Es wurde nicht der Kurs (" + idKurs + ") in der Map gefunden!", !this._kurs_by_id.containsKey(idKurs));
		for (const idKurs of idKurse)
			for (const u of MapUtils.getOrCreateArrayList(this._unterrichtmenge_by_idKurs, idKurs))
				this.unterrichtRemoveByIdOhneUpdate(u.id);
		for (const idKurs of idKurse)
			DeveloperNotificationException.ifMapRemoveFailes(this._kurs_by_id, idKurs);
	}

	/**
	 * Entfernt ein {@link StundenplanKurs}-Objekt anhand seiner ID.
	 *
	 * @param idKurs  Die Datenbank-ID des {@link StundenplanKurs}-Objekts, welches entfernt werden soll.
	 */
	public kursRemoveById(idKurs : number) : void {
		this.kursRemoveAllOhneUpdate(SetUtils.create1(idKurs));
		this.update_all();
	}

	/**
	 * Entfernt alle {@link StundenplanKurs}-Objekte.
	 *
	 * @param listKurs  Die Liste der zu entfernenden {@link StundenplanKurs}-Objekte.
	 */
	public kursRemoveAll(listKurs : List<StundenplanKurs>) : void {
		const idKurse : JavaSet<number> = new HashSet<number>();
		for (const kurs of listKurs)
			idKurse.add(kurs.id);
		this.kursRemoveAllOhneUpdate(idKurse);
		this.update_all();
	}

	/**
	 * Erzeugt eine Pseudo-Lehrkraft, falls dies aufgrund ungültiger Referenzen nötig ist.
	 * @param idLehrkraft   Die Datenbank-ID der Lehrkraft, zu der es keine Referenz mehr gibt.
	 * @param warnung       Die warnung für den User, welche von der GUI angezeigt werden soll.
	 */
	private lehrerAddPseudoLehrkraftOhneUpdate(idLehrkraft : number, warnung : string) : void {
		this._stundenplanWarnungen.add(warnung);
		const le : StundenplanLehrer = new StundenplanLehrer();
		le.id = idLehrkraft;
		le.kuerzel = "???";
		le.nachname = "???";
		le.vorname = "???";
		DeveloperNotificationException.ifMapPutOverwrites(this._lehrer_by_id, idLehrkraft, le);
	}

	/**
	 * Fügt ein {@link StundenplanLehrer}-Objekt hinzu.
	 *
	 * @param lehrer  Das {@link StundenplanLehrer}-Objekt, welches hinzugefügt werden soll.
	 */
	public lehrerAdd(lehrer : StundenplanLehrer) : void {
		this.lehrerAddAll(ListUtils.create1(lehrer));
	}

	/**
	 * Fügt alle {@link StundenplanLehrer}-Objekte hinzu.
	 *
	 * @param listLehrer  Die Menge der {@link StundenplanLehrer}-Objekte, welche hinzugefügt werden soll.
	 */
	public lehrerAddAll(listLehrer : List<StundenplanLehrer>) : void {
		this.lehrerAddAllOhneUpdate(listLehrer);
		this.update_all();
	}

	private lehrerAddAllOhneUpdate(list : List<StundenplanLehrer>) : void {
		const setOfIDs : JavaSet<number> = new HashSet<number>();
		for (const lehrer of list) {
			this.lehrerCheckAttributes(lehrer);
			DeveloperNotificationException.ifTrue("lehrerAddAllOhneUpdate: ID=" + lehrer.id + " existiert bereits!", this._lehrer_by_id.containsKey(lehrer.id));
			DeveloperNotificationException.ifTrue("lehrerAddAllOhneUpdate: ID=" + lehrer.id + " doppelt in der Liste!", !setOfIDs.add(lehrer.id));
		}
		for (const lehrer of list)
			DeveloperNotificationException.ifMapPutOverwrites(this._lehrer_by_id, lehrer.id, lehrer);
	}

	private lehrerCheckAttributes(lehrer : StundenplanLehrer) : void {
		DeveloperNotificationException.ifInvalidID("lehrer.id", lehrer.id);
		DeveloperNotificationException.ifStringIsBlank("lehrer.kuerzel", lehrer.kuerzel);
		DeveloperNotificationException.ifStringIsBlank("lehrer.nachname", lehrer.nachname);
		DeveloperNotificationException.ifStringIsBlank("lehrer.vorname", lehrer.vorname);
		for (const idFachDesLehrers of lehrer.faecher)
			DeveloperNotificationException.ifMapNotContains("_fach_by_id", this._fach_by_id, idFachDesLehrers);
	}

	private lehrerCompareByLehrerIDs(a : List<number>, b : List<number>) : number {
		if (a.size() < b.size())
			return -1;
		if (a.size() > b.size())
			return +1;
		for (let i : number = 0; i < a.size(); i++) {
			const aIdLehrer : number = ListUtils.getNonNullElementAtOrException(a, i).valueOf();
			const bIdLehrer : number = ListUtils.getNonNullElementAtOrException(b, i).valueOf();
			const aLehrer : StundenplanLehrer = DeveloperNotificationException.ifMapGetIsNull(this._lehrer_by_id, aIdLehrer);
			const bLehrer : StundenplanLehrer = DeveloperNotificationException.ifMapGetIsNull(this._lehrer_by_id, bIdLehrer);
			const cmpLehrer : number = StundenplanManager._compLehrer.compare(aLehrer, bLehrer);
			if (cmpLehrer !== 0)
				return cmpLehrer;
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
	public lehrerGetByIdOrException(idLehrer : number) : StundenplanLehrer {
		return DeveloperNotificationException.ifMapGetIsNull(this._lehrer_by_id, idLehrer);
	}

	/**
	 * Liefert eine Beschreibung des {@link StundenplanLehrer}-Objekts in der Form "BAR".
	 * <br>Hinweis: Diese Methode liefert niemals eine Exception, stattdessen enthält der String eine Fehlermeldung.
	 *
	 * @param idLehrer Die ID des angefragten-Objektes.
	 *
	 * @return eine Beschreibung des {@link StundenplanLehrer}-Objekts in der Form "BAR".
	 */
	public lehrerGetBeschreibungKuerzel(idLehrer : number) : string {
		const l : StundenplanLehrer | null = this._lehrer_by_id.get(idLehrer);
		if (l === null)
			return "Lehrer-Objekt [" + idLehrer + "] ???";
		return l.kuerzel;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanLehrer}-Objekte.
	 * <br>Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link StundenplanLehrer}-Objekte.
	 */
	public lehrerGetMengeAsList() : List<StundenplanLehrer> {
		return this._lehrermenge_sortiert;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanLehrer}-Objekte, die derzeit verwendet werden.
	 * <br>Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link StundenplanLehrer}-Objekte, die derzeit verwendet werden.
	 */
	public lehrerGetMengeVerwendetAsList() : List<StundenplanLehrer> {
		return this._lehrermenge_verwendet_sortiert;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanLehrer}-Objekte einer bestimmten Pausenzeit und einem bestimmten Aufsichtsbereich
	 * für den angegebenen Wochentyp.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idPausenzeit         die Datenbank-ID der Pausenzeit.
	 * @param idAufsichtsbereich   die Datenbank-ID des Aufsichtsbereichs.
	 * @param wochentyp            der Wochentyp
	 * @param inklWoche0           falls TRUE, wird Unterricht des Wochentyps 0 hinzugefügt.
	 *
	 * @return eine Liste aller {@link StundenplanLehrer}-Objekte einer bestimmten Pausenzeit und einem bestimmten Aufsichtsbereich.
	 */
	public lehrerGetMengeByPausenzeitIdAndAufsichtsbereichIdAndWochentypAndInklusive(idPausenzeit : number, idAufsichtsbereich : number, wochentyp : number, inklWoche0 : boolean) : List<StundenplanLehrer> {
		const list : List<StundenplanLehrer> = new ArrayList<StundenplanLehrer>();
		list.addAll(Map3DUtils.getOrCreateArrayList(this._lehrermenge_by_idPausenzeit_and_idAufsichtsbereich_and_Wochentyp, idPausenzeit, idAufsichtsbereich, wochentyp));
		if (inklWoche0)
			list.addAll(Map3DUtils.getOrCreateArrayList(this._lehrermenge_by_idPausenzeit_and_idAufsichtsbereich_and_Wochentyp, idPausenzeit, idAufsichtsbereich, 0));
		return list;
	}

	/**
	 * Liefert die Minuten aller Pausenaufsichten einer Lehrkraft bezogen auf einen Wochentyp (auf 2 Nachkommastellen gerundet).
	 * <br>Hinweis: Der Wochentyp -1 liefert den Durchschnitt aller Wochentypen.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idLehrer   Die Datenbank-ID der Lehrkraft.
	 * @param wochentyp  Der Wochentyp.
	 *
	 * @return die Minuten aller Pausenaufsichten einer Lehrkraft bezogen auf einen Wochentyp (auf 2 Nachkommastellen gerundet).
	 */
	public lehrerGetPausenaufsichtMinuten(idLehrer : number, wochentyp : number) : number {
		const wochenminuten : number = this._wertPausenaufsichtMinuten_by_idLehrkraft_and_wochentyp.getOrException(idLehrer, wochentyp).valueOf();
		return StundenplanManager.gerundetAufZweiNachkommastellen(wochenminuten);
	}

	/**
	 * Liefert die Anzahl aller Pausenaufsichten einer Lehrkraft bezogen auf einen Wochentyp (auf 2 Nachkommastellen gerundet).
	 * <br>Hinweis: Der Wochentyp -1 liefert den Durchschnitt aller Wochentypen.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idLehrer   Die Datenbank-ID der Lehrkraft.
	 * @param wochentyp  Der Wochentyp.
	 *
	 * @return die Anzahl aller Pausenaufsichten einer Lehrkraft bezogen auf einen Wochentyp (auf 2 Nachkommastellen gerundet).
	 */
	public lehrerGetPausenaufsichtAnzahl(idLehrer : number, wochentyp : number) : number {
		const wochenminuten : number = this._wertPausenaufsichtAnzahl_by_idLehrkraft_and_wochentyp.getOrException(idLehrer, wochentyp).valueOf();
		return StundenplanManager.gerundetAufZweiNachkommastellen(wochenminuten);
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
	public lehrerPatchAttributes(lehrer : StundenplanLehrer) : void {
		this.lehrerCheckAttributes(lehrer);
		DeveloperNotificationException.ifMapRemoveFailes(this._lehrer_by_id, lehrer.id);
		DeveloperNotificationException.ifMapPutOverwrites(this._lehrer_by_id, lehrer.id, lehrer);
		this.update_all();
	}

	private lehrerRemoveOhneUpdateById(idLehrer : number) : void {
		for (const kurs of MapUtils.getOrCreateArrayList(this._kursmenge_by_idLehrer, idLehrer))
			kurs.lehrer.remove(idLehrer);
		for (const ku of MapUtils.getOrCreateArrayList(this._klassenunterrichtmenge_by_idLehrer, idLehrer))
			ku.lehrer.remove(idLehrer);
		for (const u of MapUtils.getOrCreateArrayList(this._unterrichtmenge_by_idLehrer, idLehrer))
			u.lehrer.remove(idLehrer);
		for (const pa of MapUtils.getOrCreateArrayList(this._pausenaufsichtmenge_by_idLehrer, idLehrer))
			this.pausenaufsichtRemoveOhneUpdateById(pa.id);
		DeveloperNotificationException.ifMapRemoveFailes(this._lehrer_by_id, idLehrer);
	}

	/**
	 * Entfernt ein {@link StundenplanLehrer}-Objekt anhand seiner ID.
	 *
	 * @param idLehrer  Die Datenbank-ID des {@link StundenplanLehrer}-Objekts, welches entfernt werden soll.
	 */
	public lehrerRemoveById(idLehrer : number) : void {
		this.lehrerRemoveOhneUpdateById(idLehrer);
		this.update_all();
	}

	/**
	 * Entfernt alle {@link StundenplanLehrer}-Objekte.
	 *
	 * @param listLehrer  Die Liste der zu entfernenden {@link StundenplanLehrer}-Objekte.
	 */
	public lehrerRemoveAll(listLehrer : List<StundenplanLehrer>) : void {
		const setOfIDs : JavaSet<number> = new HashSet<number>();
		for (const lehrer of listLehrer) {
			if (!this._lehrer_by_id.containsKey(lehrer.id))
				throw new DeveloperNotificationException("lehrerRemoveAll: Lehrer-ID existiert nicht!")
			if (!setOfIDs.add(lehrer.id))
				throw new DeveloperNotificationException("lehrerRemoveAll: Doppelte Lehrer-ID in der Liste!")
		}
		for (const lehrer of listLehrer)
			this.lehrerRemoveOhneUpdateById(lehrer.id);
		this.update_all();
	}

	/**
	 * Fügt ein {@link StundenplanPausenaufsicht}-Objekt hinzu.
	 *
	 * @param pausenaufsicht  Das {@link StundenplanPausenaufsicht}-Objekt, welches hinzugefügt werden soll.
	 */
	public pausenaufsichtAdd(pausenaufsicht : StundenplanPausenaufsicht) : void {
		this.pausenaufsichtAddAll(ListUtils.create1(pausenaufsicht));
	}

	/**
	 * Fügt alle {@link StundenplanPausenaufsicht}-Objekte hinzu.
	 *
	 * @param listPausenaufsicht  Die Menge der {@link StundenplanPausenaufsicht}-Objekte, welche hinzugefügt werden soll.
	 */
	public pausenaufsichtAddAll(listPausenaufsicht : List<StundenplanPausenaufsicht>) : void {
		this.pausenaufsichtAddAllOhneUpdate(listPausenaufsicht);
		this.update_all();
	}

	private pausenaufsichtAddAllOhneUpdate(list : List<StundenplanPausenaufsicht>) : void {
		const setOfIDs : JavaSet<number> = new HashSet<number>();
		for (const pausenaufsicht of list) {
			this.pausenaufsichtCheckAttributes(pausenaufsicht);
			DeveloperNotificationException.ifTrue("pausenaufsichtAddAllOhneUpdate: ID=" + pausenaufsicht.id + " existiert bereits!", this._pausenaufsicht_by_id.containsKey(pausenaufsicht.id));
			DeveloperNotificationException.ifTrue("pausenaufsichtAddAllOhneUpdate: ID=" + pausenaufsicht.id + " doppelt in der Liste!", !setOfIDs.add(pausenaufsicht.id));
		}
		for (const pausenaufsicht of list)
			DeveloperNotificationException.ifMapPutOverwrites(this._pausenaufsicht_by_id, pausenaufsicht.id, pausenaufsicht);
		for (const pausenaufsicht of list)
			this.pausenaufsichtbereichAddAllOhneUpdate(pausenaufsicht.bereiche, true);
	}

	private pausenaufsichtCheckAttributes(pausenaufsicht : StundenplanPausenaufsicht) : void {
		DeveloperNotificationException.ifInvalidID("pausenaufsicht.id", pausenaufsicht.id);
		DeveloperNotificationException.ifMapNotContains("_map_idPausenzeit_zu_pausenzeit", this._pausenzeit_by_id, pausenaufsicht.idPausenzeit);
		for (const aufsichtsbereich of pausenaufsicht.bereiche)
			this.pausenaufsichtbereichCheckAttributes(aufsichtsbereich);
		if (!this._lehrer_by_id.containsKey(pausenaufsicht.idLehrer))
			this.lehrerAddPseudoLehrkraftOhneUpdate(pausenaufsicht.idLehrer, "Pausenaufsicht mit ID " + pausenaufsicht.id + " hat ungültige Lehrerreferenz.");
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanPausenaufsicht}-Objekt.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idPausenaufsicht Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanPausenaufsicht}-Objekt.
	 */
	public pausenaufsichtGetByIdOrException(idPausenaufsicht : number) : StundenplanPausenaufsicht {
		return DeveloperNotificationException.ifMapGetIsNull(this._pausenaufsicht_by_id, idPausenaufsicht);
	}

	/**
	 * Liefert eine sortierte Liste aller {@link StundenplanPausenaufsicht}-Objekte.
	 * <br>Laufzeit: O(1)
	 *
	 * @return eine sortierte Liste aller {@link StundenplanPausenaufsicht}-Objekte.
	 */
	public pausenaufsichtGetMengeAsList() : List<StundenplanPausenaufsicht> {
		return this._pausenaufsichtmenge;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenaufsicht}-Objekte eines bestimmten Wochentages.
	 * <br>Laufzeit: O(1)
	 *
	 * @param wochentag  Die ID des ENUMS {@link Wochentag}.
	 *
	 * @return eine Liste aller {@link StundenplanPausenaufsicht}-Objekte eines bestimmten Wochentages.
	 */
	public pausenaufsichtGetMengeByWochentagOrEmptyList(wochentag : number) : List<StundenplanPausenaufsicht> {
		return MapUtils.getOrCreateArrayList(this._pausenaufsichtmenge_by_wochentag, wochentag);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenaufsicht}-Objekte einer bestimmten Pausenzeit.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idPausenzeit  Die Datenbank-ID der Pausenzeit.
	 *
	 * @return eine Liste aller {@link StundenplanPausenaufsicht}-Objekte einer bestimmten Pausenzeit.
	 */
	public pausenaufsichtGetMengeByPausenzeitId(idPausenzeit : number) : List<StundenplanPausenaufsicht> {
		return MapUtils.getOrCreateArrayList(this._pausenaufsichtmenge_by_idPausenzeit, idPausenzeit);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenaufsicht}-Objekte eines bestimmten Lehrers.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idLehrer  die Datenbank-ID des Lehrers.
	 *
	 * @return eine Liste aller {@link StundenplanPausenaufsicht}-Objekte eines bestimmten Lehrers.
	 */
	public pausenaufsichtGetMengeByLehrerId(idLehrer : number) : List<StundenplanPausenaufsicht> {
		return MapUtils.getOrCreateArrayList(this._pausenaufsichtmenge_by_idLehrer, idLehrer);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenaufsicht}-Objekte einer bestimmten Klasse zu einer bestimmten Pausenzeit.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idKlasse      Die Datenbank-ID der Klasse.
	 * @param idPausenzeit  Die Datenbank-ID der Pausenzeit.
	 * @param wochentyp     Der Wochentyp.
	 * @param inklWoche0    falls TRUE, wird eine Pausenaufsicht des Wochentyps 0 hinzugefügt.
	 *
	 * @return eine Liste aller {@link StundenplanPausenaufsicht}-Objekte einer bestimmten Klasse zu einer bestimmten Pausenzeit.
	 */
	public pausenaufsichtGetMengeByKlasseIdAndPausenzeitIdAndWochentypAndInklusive(idKlasse : number, idPausenzeit : number, wochentyp : number, inklWoche0 : boolean) : List<StundenplanPausenaufsicht> {
		const list : List<StundenplanPausenaufsicht> = new ArrayList<StundenplanPausenaufsicht>();
		for (const a of Map2DUtils.getOrCreateArrayList(this._pausenaufsichtmenge_by_idKlasse_and_idPausenzeit, idKlasse, idPausenzeit))
			if (!this.pausenaufsichtbereichGetMengeByPausenaufsichtIdAndWochentypAndInklusive(a.id, wochentyp, inklWoche0).isEmpty())
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
	public pausenaufsichtGetMengeByLehrerIdAndPausenzeitIdAndWochentypAndInklusive(idLehrer : number, idPausenzeit : number, wochentyp : number, inklWoche0 : boolean) : List<StundenplanPausenaufsicht> {
		const list : List<StundenplanPausenaufsicht> = new ArrayList<StundenplanPausenaufsicht>();
		for (const a of Map2DUtils.getOrCreateArrayList(this._pausenaufsichtmenge_by_idLehrer_and_idPausenzeit, idLehrer, idPausenzeit))
			if (!this.pausenaufsichtbereichGetMengeByPausenaufsichtIdAndWochentypAndInklusive(a.id, wochentyp, inklWoche0).isEmpty())
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
	public pausenaufsichtGetMengeBySchuelerIdAndPausenzeitIdAndWochentypAndInklusive(idSchueler : number, idPausenzeit : number, wochentyp : number, inklWoche0 : boolean) : List<StundenplanPausenaufsicht> {
		const list : List<StundenplanPausenaufsicht> = new ArrayList<StundenplanPausenaufsicht>();
		for (const a of Map2DUtils.getOrCreateArrayList(this._pausenaufsichtmenge_by_idSchueler_and_idPausenzeit, idSchueler, idPausenzeit))
			if (!this.pausenaufsichtbereichGetMengeByPausenaufsichtIdAndWochentypAndInklusive(a.id, wochentyp, inklWoche0).isEmpty())
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
	public pausenaufsichtGetMengeByJahrgangIdAndPausenzeitIdAndWochentypAndInklusive(idJahrgang : number, idPausenzeit : number, wochentyp : number, inklWoche0 : boolean) : List<StundenplanPausenaufsicht> {
		const list : List<StundenplanPausenaufsicht> = new ArrayList<StundenplanPausenaufsicht>();
		for (const a of Map2DUtils.getOrCreateArrayList(this._pausenaufsichtmenge_by_idJahrgang_and_idPausenzeit, idJahrgang, idPausenzeit))
			if (!this.pausenaufsichtbereichGetMengeByPausenaufsichtIdAndWochentypAndInklusive(a.id, wochentyp, inklWoche0).isEmpty())
				list.add(a);
		return list;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenaufsicht}-Objekte einer bestimmten Pausenzeit und einem bestimmten Aufsichtsbereich.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idPausenzeit         die Datenbank-ID der Pausenzeit.
	 * @param idAufsichtsbereich   die Datenbank-ID des Aufsichtsbereichs.
	 *
	 * @return eine Liste aller {@link StundenplanPausenaufsicht}-Objekte einer bestimmten Pausenzeit und einem bestimmten Aufsichtsbereich.
	 */
	public pausenaufsichtGetMengeByPausenzeitIdAndAufsichtsbereichId(idPausenzeit : number, idAufsichtsbereich : number) : List<StundenplanPausenaufsicht> {
		return Map2DUtils.getOrCreateArrayList(this._pausenaufsichtmenge_by_idPausenzeit_and_idAufsichtsbereich, idPausenzeit, idAufsichtsbereich);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenaufsicht}-Objekte einer bestimmten Pausenzeit und einem bestimmten Aufsichtsbereich
	 * für den angegebenen Wochentyp.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idPausenzeit         die Datenbank-ID der Pausenzeit.
	 * @param idAufsichtsbereich   die Datenbank-ID des Aufsichtsbereichs.
	 * @param wochentyp            der Wochentyp
	 * @param inklWoche0           falls TRUE, wird Unterricht des Wochentyps 0 hinzugefügt.
	 *
	 * @return eine Liste aller {@link StundenplanPausenaufsicht}-Objekte einer bestimmten Pausenzeit und einem bestimmten Aufsichtsbereich.
	 */
	public pausenaufsichtGetMengeByPausenzeitIdAndAufsichtsbereichIdAndWochentypAndInklusive(idPausenzeit : number, idAufsichtsbereich : number, wochentyp : number, inklWoche0 : boolean) : List<StundenplanPausenaufsicht> {
		const list : List<StundenplanPausenaufsicht> = new ArrayList<StundenplanPausenaufsicht>();
		list.addAll(Map3DUtils.getOrCreateArrayList(this._pausenaufsichtmenge_by_idPausenzeit_and_idAufsichtsbereich_and_Wochentyp, idPausenzeit, idAufsichtsbereich, wochentyp));
		if (inklWoche0)
			list.addAll(Map3DUtils.getOrCreateArrayList(this._pausenaufsichtmenge_by_idPausenzeit_and_idAufsichtsbereich_and_Wochentyp, idPausenzeit, idAufsichtsbereich, 0));
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
	 * <br>{@link StundenplanPausenaufsicht#bereiche}
	 *
	 * @param pausenaufsicht  Das neue {@link StundenplanPausenaufsicht}-Objekt, dessen Attribute kopiert werden.
	 */
	public pausenaufsichtPatchAttributes(pausenaufsicht : StundenplanPausenaufsicht) : void {
		this.pausenaufsichtCheckAttributes(pausenaufsicht);
		DeveloperNotificationException.ifMapRemoveFailes(this._pausenaufsicht_by_id, pausenaufsicht.id);
		DeveloperNotificationException.ifMapPutOverwrites(this._pausenaufsicht_by_id, pausenaufsicht.id, pausenaufsicht);
		this.update_all();
	}

	private pausenaufsichtRemoveAllOhneUpdate(setPausenaufsichtId : JavaSet<number>) : void {
		for (const idPausenaufsicht of setPausenaufsichtId)
			DeveloperNotificationException.ifMapRemoveFailes(this._pausenaufsicht_by_id, idPausenaufsicht);
	}

	private pausenaufsichtRemoveOhneUpdateById(idPausenaufsicht : number) : void {
		const spa : StundenplanPausenaufsicht = DeveloperNotificationException.ifMapGetIsNull(this._pausenaufsicht_by_id, idPausenaufsicht);
		this.pausenaufsichtbereichRemoveAllOhneUpdate(new ArrayList(spa.bereiche));
		DeveloperNotificationException.ifMapRemoveFailes(this._pausenaufsicht_by_id, idPausenaufsicht);
	}

	/**
	 * Entfernt aus dem Stundenplan eine existierendes {@link StundenplanPausenaufsicht}-Objekt.
	 * <br>Laufzeit: O(|StundenplanPausenaufsicht|), da pausenaufsichtUpdate() aufgerufen wird.
	 *
	 * @param idPausenaufsicht  Die ID des {@link StundenplanPausenaufsicht}-Objekts.
	 */
	public pausenaufsichtRemoveById(idPausenaufsicht : number) : void {
		this.pausenaufsichtRemoveAllOhneUpdate(SetUtils.create1(idPausenaufsicht));
		this.update_all();
	}

	/**
	 * Entfernt alle {@link StundenplanPausenaufsicht}-Objekte.
	 *
	 * @param listPausenaufsichtIDs  Die Liste der IDs der zu entfernenden {@link StundenplanPausenaufsicht}-Objekte.
	 */
	public pausenaufsichtRemoveAllById(listPausenaufsichtIDs : JavaSet<number>) : void {
		this.pausenaufsichtRemoveAllOhneUpdate(listPausenaufsichtIDs);
		this.update_all();
	}

	/**
	 * Entfernt alle {@link StundenplanPausenaufsicht}-Objekte.
	 *
	 * @param listPausenaufsicht  Die Liste der zu entfernenden {@link StundenplanPausenaufsicht}-Objekte.
	 */
	public pausenaufsichtRemoveAll(listPausenaufsicht : List<StundenplanPausenaufsicht>) : void {
		const idPausenaufsicht : JavaSet<number> = new HashSet<number>();
		for (const pausenaufsicht of listPausenaufsicht)
			idPausenaufsicht.add(pausenaufsicht.id);
		this.pausenaufsichtRemoveAllOhneUpdate(idPausenaufsicht);
		this.update_all();
	}

	/**
	 * Fügt ein {@link StundenplanPausenaufsichtBereich}-Objekt hinzu.
	 *
	 * @param pausenaufsichtbereich  Das {@link StundenplanPausenaufsichtBereich}-Objekt, welches hinzugefügt werden soll.
	 */
	public pausenaufsichtbereichAdd(pausenaufsichtbereich : StundenplanPausenaufsichtBereich) : void {
		this.pausenaufsichtbereichAddAll(ListUtils.create1(pausenaufsichtbereich));
	}

	/**
	 * Fügt alle {@link StundenplanPausenaufsichtBereich}-Objekte hinzu.
	 *
	 * @param listPausenaufsichtbereich  Die Menge der {@link StundenplanPausenaufsichtBereich}-Objekte, welche hinzugefügt werden soll.
	 */
	public pausenaufsichtbereichAddAll(listPausenaufsichtbereich : List<StundenplanPausenaufsichtBereich>) : void {
		this.pausenaufsichtbereichAddAllOhneUpdate(listPausenaufsichtbereich, false);
		this.update_all();
	}

	private pausenaufsichtbereichAddAllOhneUpdate(list : List<StundenplanPausenaufsichtBereich>, isIntern : boolean) : void {
		const setOfIDs : JavaSet<number> = new HashSet<number>();
		for (const pausenaufsichtbereich of list) {
			this.pausenaufsichtbereichCheckAttributes(pausenaufsichtbereich);
			DeveloperNotificationException.ifTrue("pausenaufsichtAddAllOhneUpdate: ID=" + pausenaufsichtbereich.id + " existiert bereits!", this._pausenaufsichtbereich_by_id.containsKey(pausenaufsichtbereich.id));
			DeveloperNotificationException.ifTrue("pausenaufsichtAddAllOhneUpdate: ID=" + pausenaufsichtbereich.id + " doppelt in der Liste!", !setOfIDs.add(pausenaufsichtbereich.id));
		}
		for (const pausenaufsichtbereich of list) {
			DeveloperNotificationException.ifMapPutOverwrites(this._pausenaufsichtbereich_by_id, pausenaufsichtbereich.id, pausenaufsichtbereich);
			if (!isIntern) {
				const pausenaufsicht : StundenplanPausenaufsicht = DeveloperNotificationException.ifMapGetIsNull(this._pausenaufsicht_by_id, pausenaufsichtbereich.idPausenaufsicht);
				pausenaufsicht.bereiche.add(pausenaufsichtbereich);
			}
		}
	}

	private pausenaufsichtbereichCheckAttributes(pausenaufsichtbereich : StundenplanPausenaufsichtBereich) : void {
		DeveloperNotificationException.ifInvalidID("pausenaufsichtbereich.id", pausenaufsichtbereich.id);
		DeveloperNotificationException.ifMapNotContains("_map_idAufsichtsbereich_zu_aufsichtsbereich", this._aufsichtsbereich_by_id, pausenaufsichtbereich.idAufsichtsbereich);
		DeveloperNotificationException.ifTrue("(pab.wochentyp > 0) && (pab.wochentyp > stundenplanWochenTypModell)", (pausenaufsichtbereich.wochentyp > 0) && (pausenaufsichtbereich.wochentyp > this._stundenplanWochenTypModell));
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanPausenaufsichtBereich}-Objekt.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idPausenaufsichtbereich   die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanPausenaufsichtBereich}-Objekt.
	 */
	public pausenaufsichtbereichGetByIdOrException(idPausenaufsichtbereich : number) : StundenplanPausenaufsichtBereich {
		return DeveloperNotificationException.ifMapGetIsNull(this._pausenaufsichtbereich_by_id, idPausenaufsichtbereich);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenaufsichtBereich}-Objekte einer bestimmten Pausenaufsicht.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idPausenaufsicht   die ID der Pausenaufsicht.
	 *
	 * @return eine Liste aller {@link StundenplanPausenaufsichtBereich}-Objekte einer bestimmten Pausenaufsicht.
	 */
	public pausenaufsichtbereichGetMengeByPausenaufsichtId(idPausenaufsicht : number) : List<StundenplanPausenaufsichtBereich> {
		return MapUtils.getOrCreateArrayList(this._pausenaufsichtbereichmenge_by_idPausenaufsicht, idPausenaufsicht);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenaufsichtBereich}-Objekte eines bestimmten Aufsichtsbereichs.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idAufsichtsbereich   die ID des Aufsichtsbereichs.
	 *
	 * @return eine Liste aller {@link StundenplanPausenaufsichtBereich}-Objekte eines bestimmten Aufsichtsbereich.
	 */
	public pausenaufsichtbereichGetMengeByAufsichtsbereichId(idAufsichtsbereich : number) : List<StundenplanPausenaufsichtBereich> {
		return MapUtils.getOrCreateArrayList(this._pausenaufsichtbereichmenge_by_idAufsichtsbereich, idAufsichtsbereich);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenaufsichtBereich}-Objekte eines bestimmten Wochentyps.
	 * <br>Laufzeit: O(1)
	 *
	 * @param wochentyp   der Wochentyp
	 *
	 * @return eine Liste aller {@link StundenplanPausenaufsichtBereich}-Objekte eines bestimmten Wochentyps.
	 */
	public pausenaufsichtbereichGetMengeByWochentyp(wochentyp : number) : List<StundenplanPausenaufsichtBereich> {
		return MapUtils.getOrCreateArrayList(this._pausenaufsichtbereichmenge_by_Wochentyp, wochentyp);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenaufsichtBereich}-Objekte einer bestimmten Pausenaufsicht und eines
	 * bestimmten Aufsichtsbereichs.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idPausenaufsicht    die ID der Pausenaufsicht.
	 * @param idAufsichtsbereich  die ID des Aufsichtsbereichs.
	 *
	 * @return eine Liste aller {@link StundenplanPausenaufsichtBereich}-Objekte einer bestimmten Pausenaufsicht und eines
	 *         bestimmten Aufsichtsbereichs.
	 */
	public pausenaufsichtbereichGetMengeByPausenaufsichtIdAndAufsichtsbereichId(idPausenaufsicht : number, idAufsichtsbereich : number) : List<StundenplanPausenaufsichtBereich> {
		const list : List<StundenplanPausenaufsichtBereich> = new ArrayList<StundenplanPausenaufsichtBereich>();
		list.addAll(Map2DUtils.getOrCreateArrayList(this._pausenaufsichtbereichmenge_by_idPausenaufsicht_and_idAufsichtsbereich, idPausenaufsicht, idAufsichtsbereich));
		return list;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenaufsichtBereich}-Objekte einer bestimmten Pausenaufsicht für den
	 * angegebenen Wochentyp.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idPausenaufsicht  die ID der Pausenaufsicht.
	 * @param wochentyp         der Wochentyp
	 * @param inklWoche0        falls TRUE, wird Unterricht des Wochentyps 0 hinzugefügt.
	 *
	 * @return eine Liste aller {@link StundenplanPausenaufsichtBereich}-Objekte einer bestimmten Pausenaufsicht für den Wochentyp.
	 */
	public pausenaufsichtbereichGetMengeByPausenaufsichtIdAndWochentypAndInklusive(idPausenaufsicht : number, wochentyp : number, inklWoche0 : boolean) : List<StundenplanPausenaufsichtBereich> {
		const list : List<StundenplanPausenaufsichtBereich> = new ArrayList<StundenplanPausenaufsichtBereich>();
		list.addAll(Map2DUtils.getOrCreateArrayList(this._pausenaufsichtbereichmenge_by_idPausenaufsicht_and_Wochentyp, idPausenaufsicht, wochentyp));
		if (inklWoche0)
			list.addAll(Map2DUtils.getOrCreateArrayList(this._pausenaufsichtbereichmenge_by_idPausenaufsicht_and_Wochentyp, idPausenaufsicht, 0));
		return list;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenaufsichtBereich}-Objekte eines bestimmten Aufsichtsbereichs für den
	 * angegebenen Wochentyp.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idAufsichtsbereich  die ID des Aufsichtsbereichs.
	 * @param wochentyp           der Wochentyp
	 * @param inklWoche0          falls TRUE, wird Unterricht des Wochentyps 0 hinzugefügt.
	 *
	 * @return eine Liste aller {@link StundenplanPausenaufsichtBereich}-Objekte eines bestimmten Aufsichtsbereichs für den Wochentyp.
	 */
	public pausenaufsichtbereichGetMengeByAufsichtsbereichIdAndWochentypAndInklusive(idAufsichtsbereich : number, wochentyp : number, inklWoche0 : boolean) : List<StundenplanPausenaufsichtBereich> {
		const list : List<StundenplanPausenaufsichtBereich> = new ArrayList<StundenplanPausenaufsichtBereich>();
		list.addAll(Map2DUtils.getOrCreateArrayList(this._pausenaufsichtbereichmenge_by_idAufsichtsbereich_and_Wochentyp, idAufsichtsbereich, wochentyp));
		if (inklWoche0)
			list.addAll(Map2DUtils.getOrCreateArrayList(this._pausenaufsichtbereichmenge_by_idAufsichtsbereich_and_Wochentyp, idAufsichtsbereich, 0));
		return list;
	}

	private pausenaufsichtbereichRemoveAllOhneUpdate(pausenaufsichtbereiche : List<StundenplanPausenaufsichtBereich>) : void {
		for (const pausenaufsichtbereich of pausenaufsichtbereiche) {
			const pausenaufsicht : StundenplanPausenaufsicht = DeveloperNotificationException.ifMapGetIsNull(this._pausenaufsicht_by_id, pausenaufsichtbereich.idPausenaufsicht);
			const iter : JavaIterator<StundenplanPausenaufsichtBereich> = pausenaufsicht.bereiche.iterator();
			while (iter.hasNext())
				if (iter.next().id === pausenaufsichtbereich.id)
					iter.remove();
			DeveloperNotificationException.ifMapRemoveFailes(this._pausenaufsichtbereich_by_id, pausenaufsichtbereich.id);
		}
	}

	/**
	 * Entfernt aus dem Stundenplan eine existierendes {@link StundenplanPausenaufsichtBereich}-Objekt.
	 *
	 * @param pausenaufsichtbereiche   die {@link StundenplanPausenaufsichtBereich}-Objekte.
	 */
	public pausenaufsichtbereichRemoveAll(pausenaufsichtbereiche : List<StundenplanPausenaufsichtBereich>) : void {
		this.pausenaufsichtbereichRemoveAllOhneUpdate(pausenaufsichtbereiche);
		this.update_all();
	}

	/**
	 * Fügt ein {@link StundenplanPausenzeit}-Objekt hinzu.
	 *
	 * @param pausenzeit  Das {@link StundenplanPausenzeit}-Objekt, welches hinzugefügt werden soll.
	 */
	public pausenzeitAdd(pausenzeit : StundenplanPausenzeit) : void {
		this.pausenzeitAddAll(ListUtils.create1(pausenzeit));
		this.update_all();
	}

	/**
	 * Fügt alle {@link StundenplanPausenzeit}-Objekte hinzu.
	 *
	 * @param listPausenzeit  Die Menge der {@link StundenplanPausenzeit}-Objekte, welche hinzugefügt werden soll.
	 */
	public pausenzeitAddAll(listPausenzeit : List<StundenplanPausenzeit>) : void {
		this.pausenzeitAddAllOhneUpdate(listPausenzeit);
		this.update_all();
	}

	private pausenzeitAddAllOhneUpdate(list : List<StundenplanPausenzeit>) : void {
		const setOfIDs : JavaSet<number> = new HashSet<number>();
		for (const pausenzeit of list) {
			StundenplanManager.pausenzeitCheckAttributes(pausenzeit);
			DeveloperNotificationException.ifTrue("pausenzeitAddAllOhneUpdate: ID=" + pausenzeit.id + " existiert bereits!", this._pausenzeit_by_id.containsKey(pausenzeit.id));
			DeveloperNotificationException.ifTrue("pausenzeitAddAllOhneUpdate: ID=" + pausenzeit.id + " doppelt in der Liste!", !setOfIDs.add(pausenzeit.id));
		}
		for (const pausenzeit of list)
			DeveloperNotificationException.ifMapPutOverwrites(this._pausenzeit_by_id, pausenzeit.id, pausenzeit);
	}

	private static pausenzeitCheckAttributes(pausenzeit : StundenplanPausenzeit) : void {
		DeveloperNotificationException.ifInvalidID("pause.id", pausenzeit.id);
		Wochentag.fromIDorException(pausenzeit.wochentag);
		if ((pausenzeit.beginn !== null) && (pausenzeit.ende !== null))
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
	public pausenzeitExistsByWochentagAndBeginnAndEnde(wochentag : number, beginnOrNull : number | null, endeOrNull : number | null) : boolean {
		const beginn : number = (beginnOrNull === null) ? -1 : beginnOrNull;
		const ende : number = (endeOrNull === null) ? -1 : endeOrNull;
		const key : LongArrayKey = new LongArrayKey([wochentag, beginn, ende]);
		return this._pausenzeit_by_tag_and_beginn_and_ende.containsKey(key);
	}

	/**
	 * Liefert TRUE, falls es an dem Wochentag eine Überschneidung zwischen Pausenzeiten und Zeitrastern gibt.
	 *
	 * @param wochentag  Die ID des {@link Wochentag}-Objekts.
	 *
	 * @return TRUE, falls es an dem Wochentag eine Überschneidung zwischen Pausenzeiten und Zeitrastern gibt.
	 */
	public pausenzeitHatSchnittMitZeitrasterByWochentag(wochentag : number) : boolean {
		return MapUtils.getOrDefault(this._pausenzeitHatSchnittMitZeitraster_by_wochentag, wochentag, false);
	}

	/**
	 * Liefert TRUE, falls es an dem Wochentag bei der Klasse eine Überschneidung zwischen Pausenzeiten und Zeitrastern gibt.
	 *
	 * @param wochentag  Die ID des {@link Wochentag}-Objekts.
	 * @param idKlasse   Die ID des {@link StundenplanKlasse}-Objekts.
	 *
	 * @return TRUE, falls es an dem Wochentag bei der Klasse eine Überschneidung zwischen Pausenzeiten und Zeitrastern gibt.
	 */
	public pausenzeitHatSchnittMitZeitrasterByWochentagAndKlassenId(wochentag : number, idKlasse : number) : boolean {
		return Map2DUtils.getOrDefault(this._pausenzeitHatSchnittMitZeitraster_by_wochentag_idKlasse, wochentag, idKlasse, false);
	}

	/**
	 * Liefert TRUE, falls es an dem Wochentag bei dem Schueler eine Überschneidung zwischen Pausenzeiten und Zeitrastern gibt.
	 *
	 * @param wochentag   Die ID des {@link Wochentag}-Objekts.
	 * @param idSchueler  Die ID des {@link StundenplanSchueler}-Objekts.
	 *
	 * @return TRUE, falls es an dem Wochentag bei dem Schueler eine Überschneidung zwischen Pausenzeiten und Zeitrastern gibt.
	 */
	public pausenzeitHatSchnittMitZeitrasterByWochentagAndSchuelerId(wochentag : number, idSchueler : number) : boolean {
		return Map2DUtils.getOrDefault(this._pausenzeitHatSchnittMitZeitraster_by_wochentag_idSchueler, wochentag, idSchueler, false);
	}

	/**
	 * Liefert TRUE, falls es an dem Wochentag bei der Lehrkraft eine Überschneidung zwischen Pausenzeiten und Zeitrastern gibt.
	 *
	 * @param wochentag  Die ID des {@link Wochentag}-Objekts.
	 * @param idLehrer   Die ID des {@link StundenplanLehrer}-Objekts.
	 *
	 * @return TRUE, falls es an dem Wochentag bei der Lehrkraft eine Überschneidung zwischen Pausenzeiten und Zeitrastern gibt.
	 */
	public pausenzeitHatSchnittMitZeitrasterByWochentagAndLehrerId(wochentag : number, idLehrer : number) : boolean {
		return Map2DUtils.getOrDefault(this._pausenzeitHatSchnittMitZeitraster_by_wochentag_idLehrer, wochentag, idLehrer, false);
	}

	/**
	 * Liefert TRUE, falls es an dem Wochentag bei dem Jahrgang eine Überschneidung zwischen Pausenzeiten und Zeitrastern gibt.
	 *
	 * @param wochentag   Die ID des {@link Wochentag}-Objekts.
	 * @param idJahrgang  Die ID des {@link StundenplanJahrgang}-Objekts.
	 *
	 * @return TRUE, falls es an dem Wochentag bei dem Jahrgang eine Überschneidung zwischen Pausenzeiten und Zeitrastern gibt.
	 */
	public pausenzeitHatSchnittMitZeitrasterByWochentagAndJahrgangId(wochentag : number, idJahrgang : number) : boolean {
		return Map2DUtils.getOrDefault(this._pausenzeitHatSchnittMitZeitraster_by_wochentag_idJahrgang, wochentag, idJahrgang, false);
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanPausenzeit}-Objekt.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idPausenzeit Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanPausenzeit}-Objekt.
	 */
	public pausenzeitGetByIdOrException(idPausenzeit : number) : StundenplanPausenzeit {
		return DeveloperNotificationException.ifMapGetIsNull(this._pausenzeit_by_id, idPausenzeit);
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
	public pausenzeitGetByIdStringOfUhrzeitBeginn(idPausenzeit : number) : string {
		const pausenzeit : StundenplanPausenzeit = DeveloperNotificationException.ifMapGetIsNull(this._pausenzeit_by_id, idPausenzeit);
		return (pausenzeit.beginn === null) ? "" : DateUtils.getStringOfUhrzeitFromMinuten(pausenzeit.beginn);
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
	public pausenzeitGetByIdStringOfUhrzeitEnde(idPausenzeit : number) : string {
		const pausenzeit : StundenplanPausenzeit = DeveloperNotificationException.ifMapGetIsNull(this._pausenzeit_by_id, idPausenzeit);
		return (pausenzeit.ende === null) ? "" : DateUtils.getStringOfUhrzeitFromMinuten(pausenzeit.ende);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenzeit}-Objekte.
	 * <br>Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link StundenplanPausenzeit}-Objekte.
	 */
	public pausenzeitGetMengeAsList() : List<StundenplanPausenzeit> {
		return this._pausenzeitmenge_sortiert;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenzeit}-Objekte einer bestimmten Klasse.
	 * <br> Laufzeit: O(1)
	 *
	 * @param idKlasse  Die Datenbank-ID der Klasse.
	 *
	 * @return eine Liste aller {@link StundenplanPausenzeit}-Objekte einer bestimmten Klasse.
	 */
	public pausenzeitGetMengeByKlasseIdAsList(idKlasse : number) : List<StundenplanPausenzeit> {
		return MapUtils.getOrCreateArrayList(this._pausenzeitmenge_by_idKlasse, idKlasse);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenzeit}-Objekte einer bestimmten Lehrkraft.
	 * <br> Laufzeit: O(1)
	 *
	 * @param idLehrer  Die Datenbank-ID der Lehrkraft.
	 *
	 * @return eine Liste aller {@link StundenplanPausenzeit}-Objekte einer bestimmten Lehrkraft.
	 */
	public pausenzeitGetMengeByLehrerIdAsList(idLehrer : number) : List<StundenplanPausenzeit> {
		return MapUtils.getOrCreateArrayList(this._pausenzeitmenge_by_idLehrer, idLehrer);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenzeit}-Objekte eines bestimmten Schülers.
	 * <br> Laufzeit: O(1)
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return eine Liste aller {@link StundenplanPausenzeit}-Objekte eines bestimmten Schülers.
	 */
	public pausenzeitGetMengeBySchuelerIdAsList(idSchueler : number) : List<StundenplanPausenzeit> {
		return MapUtils.getOrCreateArrayList(this._pausenzeitmenge_by_idSchueler, idSchueler);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenzeit}-Objekte eines bestimmten Jahrgangs.
	 * <br> Laufzeit: O(1)
	 *
	 * @param idJahrgang  Die Datenbank-ID des Jahrgangs.
	 *
	 * @return eine Liste aller {@link StundenplanPausenzeit}-Objekte eines bestimmten Jahrgangs.
	 */
	public pausenzeitGetMengeByJahrgangIdAsList(idJahrgang : number) : List<StundenplanPausenzeit> {
		return MapUtils.getOrCreateArrayList(this._pausenzeitmenge_by_idJahrgang, idJahrgang);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenzeit}-Objekte eines bestimmten Wochentages, oder eine leere Liste.
	 * <br> Laufzeit: O(1)
	 *
	 * @param wochentag  Die ID des ENUMS {@link Wochentag}.
	 *
	 * @return eine Liste aller {@link StundenplanPausenzeit}-Objekte eines bestimmten Wochentages, oder eine leere Liste.
	 */
	public pausenzeitGetMengeByWochentagOrEmptyList(wochentag : number) : List<StundenplanPausenzeit> {
		return MapUtils.getOrCreateArrayList(this._pausenzeitmenge_by_wochentag, wochentag);
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
	public pausenzeitGetMengeByKlasseIdAndWochentagAsList(idKlasse : number, wochentag : number) : List<StundenplanPausenzeit> {
		return Map2DUtils.getOrCreateArrayList(this._pausenzeitmenge_by_idKlasse_and_wochentag, idKlasse, wochentag);
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
	public pausenzeitGetMengeByLehrerIdAndWochentagAsList(idLehrer : number, wochentag : number) : List<StundenplanPausenzeit> {
		return Map2DUtils.getOrCreateArrayList(this._pausenzeitmenge_by_idLehrer_and_wochentag, idLehrer, wochentag);
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
	public pausenzeitGetMengeBySchuelerIdAndWochentagAsList(idSchueler : number, wochentag : number) : List<StundenplanPausenzeit> {
		return Map2DUtils.getOrCreateArrayList(this._pausenzeitmenge_by_idSchueler_and_wochentag, idSchueler, wochentag);
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
	public pausenzeitGetMengeByJahrgangIdAndWochentagAsList(idJahrgang : number, wochentag : number) : List<StundenplanPausenzeit> {
		return Map2DUtils.getOrCreateArrayList(this._pausenzeitmenge_by_idJahrgang_and_wochentag, idJahrgang, wochentag);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenzeit}-Objekte, die mindestens eine {@link StundenplanPausenaufsicht} beinhalten.
	 * <br>Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link StundenplanPausenzeit}-Objekte, die mindestens eine {@link StundenplanPausenaufsicht} beinhalten.
	 */
	public pausenzeitGetMengeNichtLeereAsList() : List<StundenplanPausenzeit> {
		return this._pausenzeitmengeOhneLeere_sortiert;
	}

	/**
	 * Liefert eine Liste aller Dummy-{@link StundenplanPausenzeit}-Objekte mit ID=-1, welche in diesem Manager noch nicht definiert sind,
	 * die sich aber durch die Default-Werte ergeben. Die Pausenzeiten sind allen Klassen zugeordnet.
	 *
	 * @param tagVon     Der erste Tag (inklusive) des Bereichs.
	 * @param tagBis     Der letzte Tag (inklusive) des Bereichs.
	 *
	 * @return eine Liste aller Dummy-{@link StundenplanPausenzeit}-Objekte mit ID=-1.
	 */
	public pausenzeitGetDummyListe(tagVon : number, tagBis : number) : List<StundenplanPausenzeit> {
		const listDummies : List<StundenplanPausenzeit> = new ArrayList<StundenplanPausenzeit>();
		for (let wochentag : number = tagVon; wochentag <= tagBis; wochentag++) {
			if (this._stundenplanKonfig.defaultVormittagspause1Dauer > 0) {
				const beginn : number = this.zeitrasterGetDefaultStundenendeByStunde(this._stundenplanKonfig.defaultVormittagspause1Nach);
				const ende : number = beginn + this._stundenplanKonfig.defaultVormittagspause1Dauer;
				const key : LongArrayKey = new LongArrayKey([wochentag, beginn, ende]);
				if (!this._pausenzeit_by_tag_and_beginn_and_ende.containsKey(key)) {
					const p : StundenplanPausenzeit = new StundenplanPausenzeit();
					p.id = -1;
					p.wochentag = wochentag;
					p.beginn = beginn;
					p.ende = ende;
					p.bezeichnung = "1. Vormittagspause";
					listDummies.add(p);
				}
			}
			if (this._stundenplanKonfig.defaultVormittagspause2Dauer > 0) {
				const beginn : number = this.zeitrasterGetDefaultStundenendeByStunde(this._stundenplanKonfig.defaultVormittagspause2Nach);
				const ende : number = beginn + this._stundenplanKonfig.defaultVormittagspause2Dauer;
				const key : LongArrayKey = new LongArrayKey([wochentag, beginn, ende]);
				if (!this._pausenzeit_by_tag_and_beginn_and_ende.containsKey(key)) {
					const p : StundenplanPausenzeit = new StundenplanPausenzeit();
					p.id = -1;
					p.wochentag = wochentag;
					p.beginn = beginn;
					p.ende = ende;
					p.bezeichnung = "2. Vormittagspause";
					listDummies.add(p);
				}
			}
			if (this._stundenplanKonfig.defaultMittagspauseDauer > 0) {
				const beginn : number = this.zeitrasterGetDefaultStundenendeByStunde(this._stundenplanKonfig.defaultMittagspauseNach);
				const ende : number = beginn + this._stundenplanKonfig.defaultMittagspauseDauer;
				const key : LongArrayKey = new LongArrayKey([wochentag, beginn, ende]);
				if (!this._pausenzeit_by_tag_and_beginn_and_ende.containsKey(key)) {
					const p : StundenplanPausenzeit = new StundenplanPausenzeit();
					p.id = -1;
					p.wochentag = wochentag;
					p.beginn = beginn;
					p.ende = ende;
					p.bezeichnung = "Mittagspause";
					listDummies.add(p);
				}
			}
		}
		return listDummies;
	}

	/**
	 * Liefert das Minimum aller {@link StundenplanPausenzeit#beginn}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 * <br>Laufzeit: O(1)
	 *
	 * @return das Minimum aller {@link StundenplanPausenzeit#beginn}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 */
	public pausenzeitGetMinutenMin() : number {
		return (this._pausenzeitMinutenMin === null) ? 480 : this._pausenzeitMinutenMin;
	}

	/**
	 * Liefert das Maximum aller {@link StundenplanPausenzeit#ende}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 * <br>Laufzeit: O(1)
	 *
	 * @return das Maximum aller {@link StundenplanPausenzeit#ende}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 */
	public pausenzeitGetMinutenMax() : number {
		return (this._pausenzeitMinutenMax === null) ? 480 : this._pausenzeitMinutenMax;
	}

	/**
	 * Liefert die ID des kleinsten {@link Wochentag} oder den Montag falls es keine Pausenzeiten gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return die ID des kleinsten {@link Wochentag} oder den Montag falls es keine Pausenzeiten gibt.
	 */
	public pausenzeitGetWochentagMin() : number {
		return this._pausenzeitWochentagMin;
	}

	/**
	 * Liefert den kleinsten {@link Wochentag} oder den Montag falls es keine Pausenzeiten gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return den kleinsten {@link Wochentag} oder den Montag falls es keine Pausenzeiten gibt.
	 */
	public pausenzeitGetWochentagMinEnum() : Wochentag {
		return Wochentag.fromIDorException(this._pausenzeitWochentagMin);
	}

	/**
	 * Liefert die ID des größten {@link Wochentag} oder den Montag falls es keine Pausenzeiten gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return die ID des größten {@link Wochentag} oder den Montag falls es keine Pausenzeiten gibt.
	 */
	public pausenzeitGetWochentagMax() : number {
		return this._pausenzeitWochentagMax;
	}

	/**
	 * Liefert den größten {@link Wochentag} oder den Montag falls es keine Pausenzeiten gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return den größten {@link Wochentag} oder den Montag falls es keine Pausenzeiten gibt.
	 */
	public pausenzeitGetWochentagMaxEnum() : Wochentag {
		return Wochentag.fromIDorException(this._pausenzeitWochentagMax);
	}

	/**
	 * Liefert alle verwendeten sortierten {@link Wochentag}-Objekte der {@link StundenplanPausenzeit}.
	 * Das Array beinhaltet alle {@link Wochentag}-Objekte von {@link #pausenzeitGetWochentagMin} bis {@link #pausenzeitGetWochentagMax()}.
	 * <br>Laufzeit: O(1), da Referenz auf ein Array.
	 *
	 * @return alle verwendeten sortierten {@link Wochentag}-Objekte der {@link StundenplanPausenzeit}.
	 */
	public pausenzeitGetWochentageAlsEnumRange() : Array<Wochentag> {
		return this._pausenzeitWochentageAlsEnumRange;
	}

	/**
	 * Liefert das Minimum aller {@link StundenplanPausenzeit#beginn}-Objekte und aller {@link StundenplanZeitraster#stundenbeginn}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 * <br>Laufzeit: O(1)
	 *
	 * @return das Minimum aller {@link StundenplanPausenzeit#beginn}-Objekte und aller {@link StundenplanZeitraster#stundenbeginn}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 */
	public pausenzeitUndZeitrasterGetMinutenMin() : number {
		const min : number | null = BlockungsUtils.minII(this._pausenzeitMinutenMin, this._zeitrasterMinutenMin);
		return (min === null) ? 480 : min;
	}

	/**
	 * Liefert das Minimum aller nicht leeren {@link StundenplanPausenzeit#beginn}-Objekte und aller {@link StundenplanZeitraster#stundenbeginn}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 * <br>Laufzeit: O(1)
	 *
	 * @return das Minimum aller nicht leeren {@link StundenplanPausenzeit#beginn}-Objekte und aller {@link StundenplanZeitraster#stundenbeginn}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 */
	public pausenzeitUndZeitrasterGetMinutenMinOhneLeere() : number {
		const min : number | null = BlockungsUtils.minII(this._pausenzeitMinutenMinOhneLeere, this._zeitrasterMinutenMinOhneLeere);
		return (min === null) ? 480 : min;
	}

	/**
	 * Liefert das Maximum aller {@link StundenplanPausenzeit#ende}-Objekte und aller {@link StundenplanZeitraster#stundenende}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 * <br>Laufzeit: O(1)
	 *
	 * @return das Maximum aller {@link StundenplanPausenzeit#ende}-Objekte und aller {@link StundenplanZeitraster#stundenende}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 */
	public pausenzeitUndZeitrasterGetMinutenMax() : number {
		const max : number | null = BlockungsUtils.maxII(this._pausenzeitMinutenMax, this._zeitrasterMinutenMax);
		return (max === null) ? 480 : max;
	}

	/**
	 * Liefert das Maximum aller nicht leeren {@link StundenplanPausenzeit#ende}-Objekte und aller {@link StundenplanZeitraster#stundenende}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 * <br>Laufzeit: O(1)
	 *
	 * @return das Maximum aller nicht leeren {@link StundenplanPausenzeit#ende}-Objekte und aller {@link StundenplanZeitraster#stundenende}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 */
	public pausenzeitUndZeitrasterGetMinutenMaxOhneLeere() : number {
		const max : number | null = BlockungsUtils.maxII(this._pausenzeitMinutenMaxOhneLeere, this._zeitrasterMinutenMaxOhneLeere);
		return (max === null) ? 480 : max;
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
	public pausenzeitPatchAttributes(pausenzeit : StundenplanPausenzeit) : void {
		StundenplanManager.pausenzeitCheckAttributes(pausenzeit);
		DeveloperNotificationException.ifMapRemoveFailes(this._pausenzeit_by_id, pausenzeit.id);
		DeveloperNotificationException.ifMapPutOverwrites(this._pausenzeit_by_id, pausenzeit.id, pausenzeit);
		this.update_all();
	}

	private pausenzeitRemoveOhneUpdateById(idPausenzeit : number) : void {
		for (const a of MapUtils.getOrCreateArrayList(this._pausenaufsichtmenge_by_idPausenzeit, idPausenzeit))
			this.pausenaufsichtRemoveOhneUpdateById(a.id);
		DeveloperNotificationException.ifMapRemoveFailes(this._pausenzeit_by_id, idPausenzeit);
	}

	/**
	 * Entfernt aus dem Stundenplan eine existierendes {@link StundenplanPausenzeit}-Objekt.
	 *
	 * @param idPausenzeit  Die ID des {@link StundenplanPausenzeit}-Objekts.
	 */
	public pausenzeitRemoveById(idPausenzeit : number) : void {
		this.pausenzeitRemoveOhneUpdateById(idPausenzeit);
		this.update_all();
	}

	/**
	 * Entfernt alle {@link StundenplanPausenzeit}-Objekte.
	 *
	 * @param listPausenzeit  Die Liste der zu entfernenden {@link StundenplanPausenzeit}-Objekte.
	 */
	public pausenzeitRemoveAll(listPausenzeit : List<StundenplanPausenzeit>) : void {
		for (const pausenzeit of listPausenzeit)
			this.pausenzeitRemoveOhneUpdateById(pausenzeit.id);
		this.update_all();
	}

	/**
	 * Fügt ein {@link StundenplanRaum}-Objekt hinzu.
	 *
	 * @param raum  Das {@link StundenplanRaum}-Objekt, welches hinzugefügt werden soll.
	 */
	public raumAdd(raum : StundenplanRaum) : void {
		this.raumAddAll(ListUtils.create1(raum));
	}

	/**
	 * Fügt alle {@link StundenplanRaum}-Objekte hinzu.
	 *
	 * @param listRaum  Die Menge der {@link StundenplanRaum}-Objekte, welche hinzugefügt werden soll.
	 */
	public raumAddAll(listRaum : List<StundenplanRaum>) : void {
		this.raumAddAllOhneUpdate(listRaum);
		this.update_all();
	}

	private raumAddAllOhneUpdate(list : List<StundenplanRaum>) : void {
		const setOfIDs : JavaSet<number> = new HashSet<number>();
		for (const raum of list) {
			StundenplanManager.raumCheckAttributes(raum);
			DeveloperNotificationException.ifTrue("raumAddAllOhneUpdate: ID=" + raum.id + " existiert bereits!", this._raum_by_id.containsKey(raum.id));
			DeveloperNotificationException.ifTrue("raumAddAllOhneUpdate: ID=" + raum.id + " doppelt in der Liste!", !setOfIDs.add(raum.id));
		}
		for (const raum of list)
			DeveloperNotificationException.ifMapPutOverwrites(this._raum_by_id, raum.id, raum);
	}

	private static raumCheckAttributes(raum : StundenplanRaum) : void {
		DeveloperNotificationException.ifInvalidID("raum.id", raum.id);
		DeveloperNotificationException.ifStringIsBlank("raum.kuerzel", raum.kuerzel);
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
	public raumExistsByKuerzel(kuerzel : string) : boolean {
		return this._raum_by_kuerzel.containsKey(kuerzel);
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanRaum}-Objekt.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idRaum Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanRaum}-Objekt.
	 */
	public raumGetByIdOrException(idRaum : number) : StundenplanRaum {
		return DeveloperNotificationException.ifMapGetIsNull(this._raum_by_id, idRaum);
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanRaum}-Objekt oder null, falls der Raum zur ID nicht existiert.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idRaum Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanRaum}-Objekt oder null, falls der Raum zur ID nicht existiert.
	 */
	public raumGetByIdOrNull(idRaum : number) : StundenplanRaum | null {
		return this._raum_by_id.get(idRaum);
	}

	/**
	 * Liefert eine Beschreibung des {@link StundenplanRaum}-Objekts in der Form "042".
	 * <br>Hinweis: Diese Methode liefert niemals eine Exception, stattdessen enthält der String eine Fehlermeldung.
	 *
	 * @param idRaum Die ID des angefragten-Objektes.
	 *
	 * @return eine Beschreibung des {@link StundenplanRaum}-Objekts in der Form "042".
	 */
	public raumGetBeschreibungKuerzel(idRaum : number) : string {
		const ra : StundenplanRaum | null = this._raum_by_id.get(idRaum);
		if (ra === null)
			return "Raum-Objekt [" + idRaum + "] ???";
		return ra.kuerzel;
	}

	/**
	 * Liefert das zum Kürzel zugehörige {@link StundenplanRaum}-Objekt.
	 * <br>Laufzeit: O(1)
	 *
	 * @param kuerzelRaum   das Kürzel des angefragten-Objektes.
	 *
	 * @return das zum Kürzel zugehörige {@link StundenplanRaum}-Objekt.
	 */
	public raumGetByKuerzelOrException(kuerzelRaum : string) : StundenplanRaum {
		return DeveloperNotificationException.ifMapGetIsNull(this._raum_by_kuerzel, kuerzelRaum);
	}

	/**
	 * Liefert das zum Kürzel zugehörige {@link StundenplanRaum}-Objekt oder null, wenn kein
	 * Raum für das Kürzel vorhanden ist.
	 * <br>Laufzeit: O(1)
	 *
	 * @param kuerzelRaum   das Kürzel des angefragten-Objektes.
	 *
	 * @return das zum Kürzel zugehörige {@link StundenplanRaum}-Objekt oder null
	 */
	public raumGetByKuerzelOrNull(kuerzelRaum : string) : StundenplanRaum | null {
		return this._raum_by_kuerzel.get(kuerzelRaum);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanRaum}-Objekte.
	 * <br>Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link StundenplanRaum}-Objekte.
	 */
	public raumGetMengeAsList() : List<StundenplanRaum> {
		return this._raummenge_sortiert;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanRaum}-Objekte, die derzeit verwendet werden.
	 * <br>Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link StundenplanRaum}-Objekte, die derzeit verwendet werden.
	 */
	public raumGetMengeVerwendetAsList() : List<StundenplanRaum> {
		return this._raummenge_verwendet_sortiert;
	}

	/**
	 * Liefert eine nach Güte sortierte Liste aller {@link StundenplanRaum}-Objekte, die für alle Unterrichte des Kurses in Frage kommen.
	 *
	 * @param idKurs   Die Datenbank-ID des Kurses.
	 *
	 * @return eine nach Güte sortierte Liste aller {@link StundenplanRaum}-Objekte, die für alle Unterrichte des Kurses in Frage kommen.
	 */
	public raumGetMengeSortiertNachGueteByKursId(idKurs : number) : List<StundenplanRaum> {
		const listUnterrichtIDs : List<number> | null = new ArrayList<number>();
		for (const u of MapUtils.getOrCreateArrayList(this._unterrichtmenge_by_idKurs, idKurs))
			listUnterrichtIDs.add(u.id);
		return this.raumGetMengeSortiertNachGueteByUnterrichtListe(listUnterrichtIDs);
	}

	/**
	 * Liefert eine nach Güte sortierte Liste aller {@link StundenplanRaum}-Objekte, die für alle Unterrichte des Klassenunterrichts in Frage kommen.
	 *
	 * @param idKlasse   Die Datenbank-ID der Klasse.
	 * @param idFach     Die Datenbank-ID des Faches.
	 *
	 * @return eine nach Güte sortierte Liste aller {@link StundenplanRaum}-Objekte, die für alle Unterrichte des Klassenunterrichts in Frage kommen.
	 */
	public raumGetMengeSortiertNachGueteByKlasseIdAndFachId(idKlasse : number, idFach : number) : List<StundenplanRaum> {
		const listUnterrichtIDs : List<number> | null = new ArrayList<number>();
		for (const u of Map2DUtils.getOrCreateArrayList(this._unterrichtmenge_by_idKlasse_and_idFach, idKlasse, idFach))
			listUnterrichtIDs.add(u.id);
		return this.raumGetMengeSortiertNachGueteByUnterrichtListe(listUnterrichtIDs);
	}

	/**
	 * Liefert eine nach Güte sortierte Liste aller {@link StundenplanRaum}-Objekte, die für die übergebenen Unterrichte in Frage kommen.
	 *
	 * @param listUnterrichtIDs    Die Liste der IDs der {@link StundenplanUnterricht}-Objekte.
	 *
	 * @return eine nach Güte sortierte Liste aller {@link StundenplanRaum}-Objekte, die für die übergebenen Unterrichte in Frage kommen.
	 */
	public raumGetMengeSortiertNachGueteByUnterrichtListe(listUnterrichtIDs : List<number>) : List<StundenplanRaum> {
		let maxSuS : number = 0;
		for (const idUnterricht of listUnterrichtIDs) {
			const sus : number = this.schuelerGetAnzahlByUnterrichtIdOrException(idUnterricht);
			maxSuS = Math.max(maxSuS, sus);
		}
		const listRaumGuete1 : List<StundenplanRaum> = new ArrayList<StundenplanRaum>();
		const listRaumGuete2 : List<StundenplanRaum> = new ArrayList<StundenplanRaum>();
		const listRaumGuete3 : List<StundenplanRaum> = new ArrayList<StundenplanRaum>();
		const listRaumGuete4 : List<StundenplanRaum> = new ArrayList<StundenplanRaum>();
		for (const r of this._raummenge_sortiert) {
			const kollisionen : number = this.raumGetAnzahlAnKollisionenFuerUnterrichte(r.id, listUnterrichtIDs);
			if (r.groesse >= maxSuS) {
				if (kollisionen <= 0) {
					listRaumGuete1.add(r);
				} else {
					listRaumGuete2.add(r);
				}
			} else {
				if (kollisionen <= 0) {
					listRaumGuete3.add(r);
				} else {
					listRaumGuete4.add(r);
				}
			}
		}
		const listRaum : List<StundenplanRaum> = new ArrayList<StundenplanRaum>();
		listRaum.addAll(listRaumGuete1);
		listRaum.addAll(listRaumGuete2);
		listRaum.addAll(listRaumGuete3);
		listRaum.addAll(listRaumGuete4);
		return listRaum;
	}

	/**
	 * Liefert die Anzahl an Kollisionen, welche die übergebenen Unterrichte bezüglich eines Raumes haben.
	 *
	 * @param idRaum              Die Datenbank-ID des Raumes.
	 * @param listUnterrichtIDs   Eine Liste aller Unterrichts-IDs.
	 *
	 * @return die Anzahl an Kollisionen, welche die übergebenen Unterrichte bezüglich eines Raumes haben.
	 */
	public raumGetAnzahlAnKollisionenFuerUnterrichte(idRaum : number, listUnterrichtIDs : List<number>) : number {
		let summe : number = 0;
		for (const idUnterricht of listUnterrichtIDs)
			summe += this.raumGetAnzahlAnKollisionenFuerUnterricht(idRaum, idUnterricht);
		return summe;
	}

	/**
	 * Liefert die Anzahl an Kollisionen, die der übergebenen Unterricht bezüglich eines Raumes haben.
	 *
	 * @param idRaum         Die Datenbank-ID des Raumes.
	 * @param idUnterricht   Die Datenbank-ID des Unterrichts.
	 *
	 * @return die Anzahl an Kollisionen, die der übergebenen Unterricht bezüglich eines Raumes haben.
	 */
	public raumGetAnzahlAnKollisionenFuerUnterricht(idRaum : number, idUnterricht : number) : number {
		const u : StundenplanUnterricht = this.unterrichtGetByIdOrException(idUnterricht);
		let summe : number = 0;
		for (const u2 of this.unterrichtGetMengeByRaumIdAndZeitrasterIdAndWochentypAndInklusiveOrEmptyList(idRaum, u.idZeitraster, u.wochentyp, true)) {
			if (u2.id === idUnterricht)
				continue;
			summe++;
		}
		return summe;
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
	public raumPatchAttributes(raum : StundenplanRaum) : void {
		StundenplanManager.raumCheckAttributes(raum);
		DeveloperNotificationException.ifMapRemoveFailes(this._raum_by_id, raum.id);
		DeveloperNotificationException.ifMapPutOverwrites(this._raum_by_id, raum.id, raum);
		this.update_all();
	}

	private raumRemoveOhneUpdateById(idRaum : number) : void {
		for (const u of MapUtils.getOrCreateArrayList(this._unterrichtmenge_by_idRaum, idRaum))
			u.raeume.remove(idRaum);
		DeveloperNotificationException.ifMapRemoveFailes(this._raum_by_id, idRaum);
	}

	/**
	 * Entfernt aus dem Stundenplan eine existierendes {@link StundenplanRaum}-Objekt.
	 *
	 * @param idRaum  Die ID des {@link StundenplanRaum}-Objekts.
	 */
	public raumRemoveById(idRaum : number) : void {
		this.raumRemoveOhneUpdateById(idRaum);
		this.update_all();
	}

	/**
	 * Entfernt alle {@link StundenplanRaum}-Objekte.
	 *
	 * @param listRaum  Die Liste der zu entfernenden {@link StundenplanRaum}-Objekte.
	 */
	public raumRemoveAll(listRaum : List<StundenplanRaum>) : void {
		const setOfIDs : JavaSet<number> = new HashSet<number>();
		for (const raum of listRaum) {
			if (!this._raum_by_id.containsKey(raum.id))
				throw new DeveloperNotificationException("raumRemoveAll: Raum-ID existiert nicht!")
			if (!setOfIDs.add(raum.id))
				throw new DeveloperNotificationException("raumRemoveAll: Doppelte Raum-ID in der Liste!")
		}
		for (const raum of listRaum)
			this.raumRemoveOhneUpdateById(raum.id);
		this.update_all();
	}

	/**
	 * Fügt ein {@link StundenplanSchiene}-Objekt hinzu.
	 *
	 * @param schiene  Das {@link StundenplanSchiene}-Objekt, welches hinzugefügt werden soll.
	 */
	public schieneAdd(schiene : StundenplanSchiene) : void {
		this.schieneAddAll(ListUtils.create1(schiene));
	}

	/**
	 * Fügt alle {@link StundenplanSchiene}-Objekte hinzu.
	 *
	 * @param listSchiene  Die Menge der {@link StundenplanSchiene}-Objekte, welche hinzugefügt werden soll.
	 */
	public schieneAddAll(listSchiene : List<StundenplanSchiene>) : void {
		this.schieneAddAllOhneUpdate(listSchiene);
		this.update_all();
	}

	private schieneAddAllOhneUpdate(list : List<StundenplanSchiene>) : void {
		const setOfIDs : JavaSet<number> = new HashSet<number>();
		for (const schiene of list) {
			this.schieneCheckAttributes(schiene);
			DeveloperNotificationException.ifTrue("schieneAddAllOhneUpdate: ID=" + schiene.id + " existiert bereits!", this._schiene_by_id.containsKey(schiene.id));
			DeveloperNotificationException.ifTrue("schieneAddAllOhneUpdate: ID=" + schiene.id + " doppelt in der Liste!", !setOfIDs.add(schiene.id));
		}
		for (const schiene of list)
			DeveloperNotificationException.ifMapPutOverwrites(this._schiene_by_id, schiene.id, schiene);
	}

	private schieneCheckAttributes(schiene : StundenplanSchiene) : void {
		DeveloperNotificationException.ifInvalidID("schiene.id", schiene.id);
		DeveloperNotificationException.ifTrue("schiene.nummer <= 0", schiene.nummer <= 0);
		DeveloperNotificationException.ifStringIsBlank("schiene.bezeichnung", schiene.bezeichnung);
		DeveloperNotificationException.ifMapNotContains("_jahrgang_by_id", this._jahrgang_by_id, schiene.idJahrgang);
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanSchiene}-Objekt.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idSchiene Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanSchiene}-Objekt.
	 */
	public schieneGetByIdOrException(idSchiene : number) : StundenplanSchiene {
		return DeveloperNotificationException.ifMapGetIsNull(this._schiene_by_id, idSchiene);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanSchiene}-Objekte.
	 * <br>Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link StundenplanSchiene}-Objekte.
	 */
	public schieneGetMengeAsList() : List<StundenplanSchiene> {
		return this._schienenmenge_sortiert;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanSchiene}-Objekte, die derzeit verwendet werden.
	 * <br>Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link StundenplanSchiene}-Objekte, die derzeit verwendet werden.
	 */
	public schieneGetMengeVerwendetAsList() : List<StundenplanSchiene> {
		return this._schienenmenge_verwendet_sortiert;
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
	public schieneGetMengeByKlasseIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(idKlasse : number, wochentag : number, stunde : number, wochentyp : number, inklWoche0 : boolean) : List<StundenplanSchiene> {
		const list : List<StundenplanSchiene> = new ArrayList<StundenplanSchiene>();
		for (const u of this.unterrichtGetMengeByKlasseIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(idKlasse, wochentag, stunde, wochentyp, inklWoche0))
			ListUtils.addAllIfNotExists(list, MapUtils.getOrCreateArrayList(this._schienenmenge_by_idUnterricht, u.id));
		list.sort(StundenplanManager._compSchiene);
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
	public schieneGetMengeByKlasseId(idKlasse : number) : List<StundenplanSchiene> {
		return MapUtils.getOrCreateArrayList(this._schienenmenge_by_idKlasse, idKlasse);
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
	public schieneGetMengeByLehrerIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(idLehrer : number, wochentag : number, stunde : number, wochentyp : number, inklWoche0 : boolean) : List<StundenplanSchiene> {
		const list : List<StundenplanSchiene> = new ArrayList<StundenplanSchiene>();
		for (const u of this.unterrichtGetMengeByLehrerIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(idLehrer, wochentag, stunde, wochentyp, inklWoche0))
			ListUtils.addAllIfNotExists(list, MapUtils.getOrCreateArrayList(this._schienenmenge_by_idUnterricht, u.id));
		list.sort(StundenplanManager._compSchiene);
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
	public schieneGetMengeBySchuelerIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(idSchueler : number, wochentag : number, stunde : number, wochentyp : number, inklWoche0 : boolean) : List<StundenplanSchiene> {
		const list : List<StundenplanSchiene> = new ArrayList<StundenplanSchiene>();
		for (const u of this.unterrichtGetMengeBySchuelerIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(idSchueler, wochentag, stunde, wochentyp, inklWoche0))
			ListUtils.addAllIfNotExists(list, MapUtils.getOrCreateArrayList(this._schienenmenge_by_idUnterricht, u.id));
		list.sort(StundenplanManager._compSchiene);
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
	public schieneGetMengeByJahrgangIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(idJahrgang : number, wochentag : number, stunde : number, wochentyp : number, inklWoche0 : boolean) : List<StundenplanSchiene> {
		const list : List<StundenplanSchiene> = new ArrayList<StundenplanSchiene>();
		for (const u of this.unterrichtGetMengeByJahrgangIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(idJahrgang, wochentag, stunde, wochentyp, inklWoche0))
			ListUtils.addAllIfNotExists(list, MapUtils.getOrCreateArrayList(this._schienenmenge_by_idUnterricht, u.id));
		list.sort(StundenplanManager._compSchiene);
		return list;
	}

	private schieneRemoveOhneUpdateById(idSchiene : number) : void {
		for (const kurs of MapUtils.getOrCreateArrayList(this._kursmenge_by_idSchiene, idSchiene))
			kurs.schienen.remove(idSchiene);
		for (const ku of MapUtils.getOrCreateArrayList(this._klassenunterrichtmenge_by_idSchiene, idSchiene))
			ku.schienen.remove(idSchiene);
		for (const u of MapUtils.getOrCreateArrayList(this._unterrichtmenge_by_idSchiene, idSchiene))
			u.schienen.remove(idSchiene);
		DeveloperNotificationException.ifMapRemoveFailes(this._schiene_by_id, idSchiene);
	}

	/**
	 * Fügt ein {@link StundenplanSchueler}-Objekt hinzu.
	 *
	 * @param schueler  Das {@link StundenplanSchueler}-Objekt, welches hinzugefügt werden soll.
	 */
	public schuelerAdd(schueler : StundenplanSchueler) : void {
		this.schuelerAddAll(ListUtils.create1(schueler));
	}

	/**
	 * Fügt alle {@link StundenplanSchueler}-Objekte hinzu.
	 *
	 * @param listSchueler  Die Menge der {@link StundenplanSchueler}-Objekte, welche hinzugefügt werden soll.
	 */
	public schuelerAddAll(listSchueler : List<StundenplanSchueler>) : void {
		this.schuelerAddAllOhneUpdate(listSchueler);
		this.update_all();
	}

	private schuelerAddAllOhneUpdate(list : List<StundenplanSchueler>) : void {
		const setOfIDs : JavaSet<number> = new HashSet<number>();
		for (const schueler of list) {
			StundenplanManager.schuelerCheckAttributes(schueler);
			DeveloperNotificationException.ifTrue("schuelerAddAllOhneUpdate: ID=" + schueler.id + " existiert bereits!", this._schueler_by_id.containsKey(schueler.id));
			DeveloperNotificationException.ifTrue("schuelerAddAllOhneUpdate: ID=" + schueler.id + " doppelt in der Liste!", !setOfIDs.add(schueler.id));
		}
		for (const schueler of list)
			DeveloperNotificationException.ifMapPutOverwrites(this._schueler_by_id, schueler.id, schueler);
	}

	private static schuelerCheckAttributes(schueler : StundenplanSchueler) : void {
		DeveloperNotificationException.ifInvalidID("schueler.id", schueler.id);
		DeveloperNotificationException.ifStringIsBlank("schueler.nachname", schueler.nachname);
		DeveloperNotificationException.ifStringIsBlank("schueler.vorname", schueler.vorname);
	}

	/**
	 * Liefert eine Beschreibung des {@link StundenplanSchueler}-Objekts in der Form "Peter Pan".
	 * <br>Hinweis: Diese Methode liefert niemals eine Exception, stattdessen enthält der String eine Fehlermeldung.
	 *
	 * @param idSchueler Die ID des angefragten-Objektes.
	 *
	 * @return eine Beschreibung des {@link StundenplanSchueler}-Objekts in der Form "Peter Pan".
	 */
	public schuelerGetBeschreibungVornameNachname(idSchueler : number) : string {
		const s : StundenplanSchueler | null = this._schueler_by_id.get(idSchueler);
		if (s === null)
			return "Schüler-Objekt [" + idSchueler + "] ???";
		return s.vorname + " " + s.nachname;
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanSchueler}-Objekt.
	 *
	 * @param idSchueler Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanSchueler}-Objekt.
	 */
	public schuelerGetByIdOrException(idSchueler : number) : StundenplanSchueler {
		return DeveloperNotificationException.ifMapGetIsNull(this._schueler_by_id, idSchueler);
	}

	/**
	 * Liefert die Datenbank-ID des Schülers.<br>
	 * Wirft eine Exception, falls in den Daten nicht genau ein Schüler geladen wurde.
	 *
	 * @return  Die Datenbank-ID des Schülers.
	 */
	public schuelerGetIDorException() : number {
		const size : number = this._schuelermenge.size();
		DeveloperNotificationException.ifTrue("getSchuelerID() geht nicht bei " + size + " Schülern!", size !== 1);
		return this._schuelermenge.get(0).id;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanSchueler}-Objekte, sortiert nach {@link #_compSchueler}.
	 * <br>Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link StundenplanSchueler}-Objekte, sortiert nach {@link #_compSchueler}.
	 */
	public schuelerGetMengeAsList() : List<StundenplanSchueler> {
		return this._schuelermenge;
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
	public schuelerGetMengeByKlasseIdAsListOrException(idKlasse : number) : List<StundenplanSchueler> {
		const klasse : StundenplanKlasse = DeveloperNotificationException.ifMapGetIsNull(this._klasse_by_id, idKlasse);
		return MapUtils.getOrCreateArrayList(this._schuelermenge_by_idKlasse, klasse.id);
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
	public schuelerGetMengeByKursIdAsListOrException(idKurs : number) : List<StundenplanSchueler> {
		return MapUtils.getOrCreateArrayList(this._schuelermenge_by_idKurs, idKurs);
	}

	/**
	 * Liefert alle {@link StundenplanSchueler}-Objekte des Unterrichts.
	 * <br>Hinweis: Bei Klassenunterricht werden die SuS aus den Klassen aggregiert, bei Kursunterricht sind es die SuS des Kurses.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idUnterricht  Die Datenbank-ID des Unterrichts.
	 *
	 * @return alle {@link StundenplanSchueler}-Objekte des Unterrichts.
	 * @throws DeveloperNotificationException falls der Unterricht nicht existiert.
	 */
	public schuelerGetMengeByUnterrichtIdAsList(idUnterricht : number) : List<StundenplanSchueler> {
		return MapUtils.getOrCreateArrayList(this._schuelermenge_by_idUnterricht, idUnterricht);
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
	public schuelerGetAnzahlByKursIdAsListOrException(idKurs : number) : number {
		return MapUtils.getOrCreateArrayList(this._schuelermenge_by_idKurs, idKurs).size();
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
	public schuelerGetAnzahlByKlasseIdOrException(idKlasse : number) : number {
		const klasse : StundenplanKlasse = DeveloperNotificationException.ifMapGetIsNull(this._klasse_by_id, idKlasse);
		return MapUtils.getOrCreateArrayList(this._schuelermenge_by_idKlasse, klasse.id).size();
	}

	/**
	 * Liefert die Anzahl der {@link StundenplanSchueler}-Objekte des Unterrichts.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idUnterricht   Die Datenbank-ID des Unterrichts.
	 *
	 * @return die Anzahl der {@link StundenplanSchueler}-Objekte des Unterrichts.
	 * @throws DeveloperNotificationException falls der Klasse nicht existiert.
	 */
	public schuelerGetAnzahlByUnterrichtIdOrException(idUnterricht : number) : number {
		return MapUtils.getOrCreateArrayList(this._schuelermenge_by_idUnterricht, idUnterricht).size();
	}

	/**
	 * Entfernt den Schülers, auch kaskadierend aus {@link StundenplanKlasse}, {@link StundenplanKurs}, {@link StundenplanKlassenunterricht}.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 */
	private schuelerRemoveOhneUpdateById(idSchueler : number) : void {
		for (const klasse of MapUtils.getOrCreateArrayList(this._klassenmenge_by_idSchueler, idSchueler))
			klasse.schueler.remove(idSchueler);
		for (const kurs of MapUtils.getOrCreateArrayList(this._kursmenge_by_idSchueler, idSchueler))
			kurs.schueler.remove(idSchueler);
		for (const ku of MapUtils.getOrCreateArrayList(this._klassenunterrichtmenge_by_idSchueler, idSchueler))
			ku.schueler.remove(idSchueler);
		DeveloperNotificationException.ifMapRemoveFailes(this._schiene_by_id, idSchueler);
	}

	/**
	 * Entfernt aus dem Stundenplan eine existierendes {@link StundenplanSchueler}-Objekt.
	 * <br>Hinweis: Entfernt kaskadierend auch aus {@link StundenplanKlasse}, {@link StundenplanKurs} und {@link StundenplanKlassenunterricht}.
	 *
	 * @param idSchueler  Die ID des {@link StundenplanSchueler}-Objekts.
	 */
	public schuelerRemoveById(idSchueler : number) : void {
		this.schuelerRemoveOhneUpdateById(idSchueler);
		this.update_all();
	}

	/**
	 * Liefert die ID des Schuljahresabschnitts des Stundenplans.
	 *
	 * @return die ID des Schuljahresabschnitts des Stundenplans.
	 */
	public getIDSchuljahresabschnitt() : number {
		return this._stundenplanSchuljahresAbschnittID;
	}

	/**
	 * Liefert das Schuljahr, für welches der Stundenplan gültig ist
	 *
	 * @return das Schuljahr, für welches der Stundenplan gültig ist
	 */
	public getSchuljahr() : number {
		return this._stundenplanSchuljahr;
	}

	/**
	 * Liefert den Abschnitt im Schuljahr, für welchen der Stundenplan gültig ist
	 *
	 * @return der Abschnitt im Schuljahr, für welchen der Stundenplan gültig ist
	 */
	public getAbschnitt() : number {
		return this._stundenplanAbschnitt;
	}

	/**
	 * Liefert das Datum, ab dem der Stundenplan gültig ist.
	 *
	 * @return das Datum, ab dem der Stundenplan gültig ist.
	 */
	public getGueltigAb() : string {
		return this._stundenplanGueltigAb;
	}

	/**
	 * Liefert das Datum, bis wann der Stundenplan gültig ist.
	 *
	 * @return das Datum, bis wann der Stundenplan gültig ist.
	 */
	public getGueltigBis() : string {
		return this._stundenplanGueltigBis;
	}

	/**
	 * Liefert die Information, ob der Stundenplan aktiv ist.
	 *
	 * @return true, wenn der Stundenplan aktiv ist, sonst false.
	 */
	public isAktiv() : boolean {
		return this._aktiv;
	}

	/**
	 * Liefert die textuelle Beschreibung des Stundenplans.
	 *
	 * @return die textuelle Beschreibung des Stundenplans.
	 */
	public getBezeichnungStundenplan() : string {
		return this._stundenplanBezeichnung;
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
	public getWochenTypModell() : number {
		return this._stundenplanWochenTypModell;
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
	public stundenplanGetWochenTypModell() : number {
		return this._stundenplanWochenTypModell;
	}

	/**
	 * Aktualisiert das (globale) Wochentyp-Modell für die Wochen des Stundenplans.
	 * <br>0: Stundenplan gilt jede Woche.
	 * <br>1: Ungültiger Wert --> Wird ersetzt durch 0.
	 * <br>N: Stundenplan wiederholt sich alle N Wochen.
	 * <br>Für alle {@link StundenplanUnterricht#wochentyp} deren Wert ungültig ist, wird der Wert auf 0 gesetzt.
	 * <br>Zudem werden alle (nicht Dummy) {@link StundenplanKalenderwochenzuordnung}-Objekte gelöscht.
	 *
	 * @param modellTypOriginal  Der neue Wert für das (globale) Wochentyp-Modell.
	 */
	public stundenplanSetWochenTypModell(modellTypOriginal : number) : void {
		const modellTyp : number = (modellTypOriginal === 1) ? 0 : modellTypOriginal;
		if (modellTyp === this._stundenplanWochenTypModell)
			return;
		DeveloperNotificationException.ifTrue("Das (globale) Wochentyp-Modell kann nur die Werte (0, 2, 3, ..., N) annehmen!", (modellTyp < 0) || (modellTyp === 1));
		for (const u of this._unterricht_by_id.values())
			if (u.wochentyp > modellTyp)
				u.wochentyp = 0;
		for (const a of this._pausenaufsichtbereich_by_id.values())
			if (a.wochentyp > modellTyp)
				a.wochentyp = 0;
		this._kwz_by_id = new HashMap();
		this._stundenplanWochenTypModell = modellTyp;
		this.update_all();
	}

	/**
	 * Liefert die Anzahl der {@link StundenplanUnterricht#wochentyp}-Korrekturen, bei einer simulierten Veränderung des (globalen) Wochentyp-Modells.
	 *
	 * @param modellTyp  Der neue Wert für das (globale) Wochentyp-Modell.
	 *
	 * @return die Anzahl der {@link StundenplanUnterricht#wochentyp}-Korrekturen, bei einer simulierten Veränderung des (globalen) Wochentyp-Modells.
	 */
	public stundenplanGetWochenTypModellSimulation(modellTyp : number) : number {
		DeveloperNotificationException.ifTrue("Das (globale) Wochentyp-Modell kann nur die Werte (0, 2, 3, ..., N) annehmen!", (modellTyp < 0) || (modellTyp === 1));
		let summe : number = 0;
		for (const u of this._unterricht_by_id.values())
			if (u.wochentyp > modellTyp)
				summe++;
		return summe;
	}

	/**
	 * Liefert zum übergebenen Wochentyp einen passenden verkürzten String.
	 * <br>Beispiel: 0 -> "Alle", 1 -> "A", ...
	 * <br>Laufzeit: O(1)
	 *
	 * @param wochenTyp  Der umzuwandelnde Wochentyp.
	 *
	 * @return zum übergebenen Wochentyp einen passenden String.
	 */
	public stundenplanGetWochenTypAsStringKurz(wochenTyp : number) : string {
		if (wochenTyp <= 0)
			return "Alle";
		const zahl : number = wochenTyp - 1;
		const z2 : number = Math.trunc(zahl / 26);
		const z1 : number = zahl - (z2 * 26);
		return StringUtils.numberToLetterIndex1(z2) + StringUtils.numberToLetterIndex0(z1);
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
	public stundenplanGetWochenTypAsString(wochenTyp : number) : string {
		if (wochenTyp <= 0)
			return "Alle Wochen";
		return this.stundenplanGetWochenTypAsStringKurz(wochenTyp) + "-Woche";
	}

	/**
	 * Liefert die Datenbank-ID des Stundenplans.
	 * <br>Laufzeit: O(1)
	 *
	 * @return die Datenbank-ID des Stundenplans.
	 */
	public stundenplanGetID() : number {
		return this._stundenplanID;
	}

	/**
	 * Liefert den Default-Wert für den Unterrichtsbeginn (z.B. 8:00 Uhr = 8 * 60), kodiert als Minuten seit 0 Uhr.
	 *
	 * @return den Default-Wert für den Unterrichtsbeginn (z.B. 8:00 Uhr = 8 * 60), kodiert als Minuten seit 0 Uhr.
	 */
	public stundenplanGetDefaultUnterrichtsbeginn() : number {
		return this._stundenplanKonfig.defaultUnterrichtsbeginn;
	}

	/**
	 * Setzt den Default-Wert für den Unterrichtsbeginn (z.B. 8:00 Uhr = 8 * 60), kodiert als Minuten seit 0 Uhr.
	 *
	 * @param defaultUnterrichtsbeginn  kodiert als Minuten seit 0 Uhr.
	 */
	public stundenplanSetDefaultUnterrichtsbeginn(defaultUnterrichtsbeginn : number) : void {
		DeveloperNotificationException.ifTrue("'defaultUnterrichtsbeginn' muss >= 0 sein!", defaultUnterrichtsbeginn < 0);
		this._stundenplanKonfig.defaultUnterrichtsbeginn = defaultUnterrichtsbeginn;
	}

	/**
	 * Liefert den Default-Wert für die Dauer einer Unterrichtsstunde (in Minuten).
	 *
	 * @return den Default-Wert für die Dauer einer Unterrichtsstunde (in Minuten).
	 */
	public stundenplanGetDefaultStundendauer() : number {
		return this._stundenplanKonfig.defaultStundendauer;
	}

	/**
	 * Setzt den Default-Wert für die Dauer einer Unterrichtsstunde (in Minuten).
	 *
	 * @param defaultStundendauer  Minuten einer Unterrichtsstunde.
	 */
	public stundenplanSetDefaultStundendauer(defaultStundendauer : number) : void {
		DeveloperNotificationException.ifTrue("'defaultStundendauer' muss > 0 sein!", defaultStundendauer <= 0);
		this._stundenplanKonfig.defaultStundendauer = defaultStundendauer;
	}

	/**
	 * Liefert den Default-Wert für die Pausenzeit für Raumwechsel (in Minuten).
	 *
	 * @return den Default-Wert für die Pausenzeit für Raumwechsel (in Minuten).
	 */
	public stundenplanGetDefaultPausenzeitFuerRaumwechsel() : number {
		return this._stundenplanKonfig.defaultPausenzeitFuerRaumwechsel;
	}

	/**
	 * Setzt den Default-Wert für die Pausenzeit für Raumwechsel (in Minuten).
	 *
	 * @param defaultPausenzeitFuerRaumwechsel  Minuten für einen Raumwechsel.
	 */
	public stundenplanSetDefaultPausenzeitFuerRaumwechsel(defaultPausenzeitFuerRaumwechsel : number) : void {
		DeveloperNotificationException.ifTrue("'defaultPausenzeitFuerRaumwechsel' muss >= 0 sein!", defaultPausenzeitFuerRaumwechsel < 0);
		this._stundenplanKonfig.defaultPausenzeitFuerRaumwechsel = defaultPausenzeitFuerRaumwechsel;
	}

	/**
	 * Liefert den Default-Wert für die 1. Vormittagspause nach welcher welcher Stunde diese beginnt.
	 *
	 * @return den Default-Wert für die 1. Vormittagspause nach welcher welcher Stunde diese beginnt.
	 */
	public stundenplanGetDefaultVormittagspause1Nach() : number {
		return this._stundenplanKonfig.defaultVormittagspause1Nach;
	}

	/**
	 * Setzt den Default-Wert für die 1. Vormittagspause nach welcher welcher Stunde diese beginnt.
	 *
	 * @param defaultVormittagspause1Nach  Stunde nach der die 1. Vormittagspause beginnt.
	 */
	public stundenplanSetDefaultVormittagspause1Nach(defaultVormittagspause1Nach : number) : void {
		DeveloperNotificationException.ifTrue("'defaultVormittagspause1Nach' muss >= 0 sein!", defaultVormittagspause1Nach < 0);
		this._stundenplanKonfig.defaultVormittagspause1Nach = defaultVormittagspause1Nach;
	}

	/**
	 * Liefert den Default-Wert für die Dauer der 1. Vormittagspause.
	 *
	 * @return den Default-Wert für die Dauer der 1. Vormittagspause.
	 */
	public stundenplanGetDefaultVormittagspause1Dauer() : number {
		return this._stundenplanKonfig.defaultVormittagspause1Dauer;
	}

	/**
	 * Setzt den Default-Wert für die Dauer der 1. Vormittagspause.
	 *
	 * @param defaultVormittagspause1Dauer  Dauer der 1. Vormittagspause (in Minuten).
	 */
	public stundenplanSetDefaultVormittagspause1Dauer(defaultVormittagspause1Dauer : number) : void {
		DeveloperNotificationException.ifTrue("'defaultVormittagspause1Nach' muss >= 0 sein!", defaultVormittagspause1Dauer < 0);
		this._stundenplanKonfig.defaultVormittagspause1Dauer = defaultVormittagspause1Dauer;
	}

	/**
	 * Liefert den Default-Wert für die 2. Vormittagspause nach welcher welcher Stunde diese beginnt.
	 *
	 * @return den Default-Wert für die 2. Vormittagspause nach welcher welcher Stunde diese beginnt.
	 */
	public stundenplanGetDefaultVormittagspause2Nach() : number {
		return this._stundenplanKonfig.defaultVormittagspause2Nach;
	}

	/**
	 * Setzt den Default-Wert für die 2. Vormittagspause nach welcher welcher Stunde diese beginnt.
	 *
	 * @param defaultVormittagspause2Nach  Stunde nach der die 2. Vormittagspause beginnt.
	 */
	public stundenplanSetDefaultVormittagspause2Nach(defaultVormittagspause2Nach : number) : void {
		DeveloperNotificationException.ifTrue("'defaultVormittagspause2Nach' muss >= 0 sein!", defaultVormittagspause2Nach < 0);
		this._stundenplanKonfig.defaultVormittagspause2Nach = defaultVormittagspause2Nach;
	}

	/**
	 * Liefert den Default-Wert für die Dauer der 2. Vormittagspause.
	 *
	 * @return den Default-Wert für die Dauer der 2. Vormittagspause.
	 */
	public stundenplanGetDefaultVormittagspause2Dauer() : number {
		return this._stundenplanKonfig.defaultVormittagspause2Dauer;
	}

	/**
	 * Setzt den Default-Wert für die Dauer der 2. Vormittagspause.
	 *
	 * @param defaultVormittagspause2Dauer  Dauer der 2. Vormittagspause (in Minuten).
	 */
	public stundenplanSetDefaultVormittagspause2Dauer(defaultVormittagspause2Dauer : number) : void {
		DeveloperNotificationException.ifTrue("'defaultVormittagspause2Dauer' muss >= 0 sein!", defaultVormittagspause2Dauer < 0);
		this._stundenplanKonfig.defaultVormittagspause2Dauer = defaultVormittagspause2Dauer;
	}

	/**
	 * Liefert den Default-Wert für die Mittagspause nach welcher welcher Stunde diese beginnt.
	 *
	 * @return den Default-Wert für die Mittagspause nach welcher welcher Stunde diese beginnt.
	 */
	public stundenplanGetDefaultMittagspauseNach() : number {
		return this._stundenplanKonfig.defaultMittagspauseNach;
	}

	/**
	 * Setzt den Default-Wert für die Mittagspause nach welcher welcher Stunde diese beginnt.
	 *
	 * @param defaultMittagspauseNach  Stunde nach der die Mittagspause beginnt.
	 */
	public stundenplanSetDefaultMittagspauseNach(defaultMittagspauseNach : number) : void {
		DeveloperNotificationException.ifTrue("'defaultMittagspauseNach' muss >= 0 sein!", defaultMittagspauseNach < 0);
		this._stundenplanKonfig.defaultMittagspauseNach = defaultMittagspauseNach;
	}

	/**
	 * Liefert den Default-Wert für die Dauer der Mittagspause.
	 *
	 * @return den Default-Wert für die Dauer der Mittagspause.
	 */
	public stundenplanGetDefaultMittagspauseDauer() : number {
		return this._stundenplanKonfig.defaultMittagspauseDauer;
	}

	/**
	 * Setzt den Default-Wert für die Dauer der Mittagspause (in Minuten).
	 *
	 * @param defaultMittagspauseDauer  Dauer der Mittagspause (in Minuten).
	 */
	public stundenplanSetDefaultMittagspauseDauer(defaultMittagspauseDauer : number) : void {
		DeveloperNotificationException.ifTrue("'defaultMittagspauseDauer' muss >= 0 sein!", defaultMittagspauseDauer < 0);
		this._stundenplanKonfig.defaultMittagspauseDauer = defaultMittagspauseDauer;
	}

	/**
	 * Setzt das {@link StundenplanKonfiguration}-Objekt.
	 *
	 * @param stundenplanKonfig  Das {@link StundenplanKonfiguration}-Objekt.
	 */
	public stundenplanKonfigSet(stundenplanKonfig : StundenplanKonfiguration) : void {
		this._stundenplanKonfig = stundenplanKonfig;
	}

	/**
	 * Liefert das aktuelle {@link StundenplanKonfiguration}-Objekt.
	 *
	 * @return das aktuelle {@link StundenplanKonfiguration}-Objekt.
	 */
	public stundenplanKonfigGet() : StundenplanKonfiguration {
		return this._stundenplanKonfig;
	}

	/**
	 * Liefert alle Warnungen, die dem User angezeigt werden sollen.
	 *
	 * @return alle Warnungen, die dem User angezeigt werden sollen.
	 */
	public stundenplanGetWarnungen() : List<string> {
		return this._stundenplanWarnungen;
	}

	/**
	 * Fügt ein {@link StundenplanUnterricht}-Objekt hinzu.
	 *
	 * @param unterricht  Das {@link StundenplanUnterricht}-Objekt, welches hinzugefügt werden soll.
	 */
	public unterrichtAdd(unterricht : StundenplanUnterricht) : void {
		this.unterrichtAddAll(ListUtils.create1(unterricht));
	}

	/**
	 * Fügt alle {@link StundenplanUnterricht}-Objekte hinzu.
	 *
	 * @param listUnterricht  Die Menge der {@link StundenplanUnterricht}-Objekte, welche hinzugefügt werden soll.
	 */
	public unterrichtAddAll(listUnterricht : List<StundenplanUnterricht>) : void {
		this.unterrichtAddAllOhneUpdate(listUnterricht);
		this.update_all();
	}

	private unterrichtAddAllOhneUpdate(list : List<StundenplanUnterricht>) : void {
		const setOfIDs : JavaSet<number> = new HashSet<number>();
		for (const u of list) {
			this.unterrichtCheckAttributes(u);
			DeveloperNotificationException.ifTrue("unterrichtAddAllOhneUpdate: ID=" + u.id + " existiert bereits!", this._unterricht_by_id.containsKey(u.id));
			DeveloperNotificationException.ifTrue("unterrichtAddAllOhneUpdate: ID=" + u.id + " doppelt in der Liste!", !setOfIDs.add(u.id));
		}
		this.unterrichtCheckDuplicateInCell(list);
		for (const u of list)
			DeveloperNotificationException.ifMapPutOverwrites(this._unterricht_by_id, u.id, u);
	}

	private unterrichtCheckAttributes(u : StundenplanUnterricht) : void {
		DeveloperNotificationException.ifInvalidID("u.id", u.id);
		DeveloperNotificationException.ifMapNotContains("_zeitraster_by_id", this._zeitraster_by_id, u.idZeitraster);
		DeveloperNotificationException.ifTrue("u.wochentyp > stundenplanWochenTypModell", u.wochentyp > this._stundenplanWochenTypModell);
		DeveloperNotificationException.ifTrue("u.wochentyp < 0", u.wochentyp < 0);
		DeveloperNotificationException.ifTrue("(u.idKurs == null) && (u.klassen.size() != 1)", (u.idKurs === null) && (u.klassen.size() !== 1));
		DeveloperNotificationException.ifMapNotContains("_fach_by_id", this._fach_by_id, u.idFach);
		for (const idLehrkraft of u.lehrer)
			if (!this._lehrer_by_id.containsKey(idLehrkraft))
				this.lehrerAddPseudoLehrkraftOhneUpdate(idLehrkraft, "StundenplanUnterricht " + this.unterrichtGetBeschreibungKurz(u) + " hat ungültige Lehrerreferenz.");
		for (const idKlasseDesUnterrichts of u.klassen)
			DeveloperNotificationException.ifMapNotContains("_klasse_by_id", this._klasse_by_id, idKlasseDesUnterrichts);
		for (const idRaumDesUnterrichts of u.raeume)
			DeveloperNotificationException.ifMapNotContains("_raum_by_id", this._raum_by_id, idRaumDesUnterrichts);
		for (const idSchieneDesUnterrichts of u.schienen)
			DeveloperNotificationException.ifMapNotContains("_schiene_by_id", this._schiene_by_id, idSchieneDesUnterrichts);
	}

	private unterrichtCheckDuplicateInCell(list : List<StundenplanUnterricht>) : void {
		// empty block
	}

	private unterrichtCreateComparator() : Comparator<StundenplanUnterricht> {
		const comp : Comparator<StundenplanUnterricht> = { compare : (a: StundenplanUnterricht, b: StundenplanUnterricht) => {
			const cmpKlassenNachJahrgang : number = this.compareKlassenlistenIDsNachJahrgang(a.klassen, b.klassen);
			if (cmpKlassenNachJahrgang !== 0)
				return cmpKlassenNachJahrgang;
			if ((a.idKurs !== null) && (b.idKurs === null))
				return -1;
			if ((a.idKurs === null) && (b.idKurs !== null))
				return +1;
			const cmpKlasse : number = this.compareKlassenlistenIDsNachStandard(a.klassen, b.klassen);
			if (cmpKlasse !== 0)
				return cmpKlasse;
			const aFach : StundenplanFach = DeveloperNotificationException.ifMapGetIsNull(this._fach_by_id, a.idFach);
			const bFach : StundenplanFach = DeveloperNotificationException.ifMapGetIsNull(this._fach_by_id, b.idFach);
			const cmpFach : number = StundenplanManager._compFach.compare(aFach, bFach);
			if (cmpFach !== 0)
				return cmpFach;
			const cmpLehrer : number = this.lehrerCompareByLehrerIDs(a.lehrer, b.lehrer);
			if (cmpLehrer !== 0)
				return cmpLehrer;
			const cmpWochentyp : number = StundenplanManager.compareWochentyp(a.wochentyp, b.wochentyp);
			if (cmpWochentyp !== 0)
				return cmpWochentyp;
			return JavaLong.compare(a.id, b.id);
		} };
		return comp;
	}

	private unterrichtCreateComparatorNachJahrgangKlasseFachWochentyp() : Comparator<StundenplanUnterricht> {
		const comp : Comparator<StundenplanUnterricht> = { compare : (a: StundenplanUnterricht, b: StundenplanUnterricht) => {
			const listA : List<StundenplanJahrgang> = DeveloperNotificationException.ifMapGetIsNull(this._jahrgangmenge_by_idUnterricht, a.id);
			const listB : List<StundenplanJahrgang> = DeveloperNotificationException.ifMapGetIsNull(this._jahrgangmenge_by_idUnterricht, b.id);
			if ((!listA.isEmpty()) || (!listB.isEmpty())) {
				if (listA.isEmpty())
					return -1;
				if (listB.isEmpty())
					return +1;
				const aJahrgang : StundenplanJahrgang = ListUtils.getNonNullElementAtOrException(listA, 0);
				const bJahrgang : StundenplanJahrgang = ListUtils.getNonNullElementAtOrException(listB, 0);
				const cmpJahrgang : number = StundenplanManager._compJahrgang.compare(aJahrgang, bJahrgang);
				if (cmpJahrgang !== 0)
					return cmpJahrgang;
			}
			const cmpKlasse : number = this.compareKlassenlistenIDsNachStandard(a.klassen, b.klassen);
			if (cmpKlasse !== 0)
				return cmpKlasse;
			const aFach : StundenplanFach = DeveloperNotificationException.ifMapGetIsNull(this._fach_by_id, a.idFach);
			const bFach : StundenplanFach = DeveloperNotificationException.ifMapGetIsNull(this._fach_by_id, b.idFach);
			const cmpFach : number = StundenplanManager._compFach.compare(aFach, bFach);
			if (cmpFach !== 0)
				return cmpFach;
			const cmpWochentyp : number = StundenplanManager.compareWochentyp(a.wochentyp, b.wochentyp);
			if (cmpWochentyp !== 0)
				return cmpWochentyp;
			return JavaLong.compare(a.id, b.id);
		} };
		return comp;
	}

	private unterrichtGetBeschreibungKurz(u : StundenplanUnterricht) : string | null {
		if (u.idKurs === null) {
			let s : string = "";
			const fach : StundenplanFach | null = this._fach_by_id.get(u.idFach);
			s += (fach === null) ? "Fach ???" : fach.kuerzel;
			s += " ";
			for (const idKlasse of u.klassen) {
				const klasse : StundenplanKlasse | null = this._klasse_by_id.get(idKlasse);
				s += (klasse === null) ? "Klasse ???" : klasse.kuerzel;
			}
			return s;
		}
		const kurs : StundenplanKurs | null = this._kurs_by_id.get(u.idKurs);
		return (kurs === null) ? "Kurs ???" : kurs.bezeichnung;
	}

	/**
	 * Liefert das {@link StundenplanUnterricht}-Objekt zur übergebenen ID.
	 * <br>Hinweis: Unnötige Methode, denn man bekommt die Objekte über Zeitraster-Abfragen.
	 *
	 * @param idUnterricht  Die Datenbank-ID des Unterrichts.
	 *
	 * @return das {@link StundenplanUnterricht}-Objekt zur übergebenen ID.
	 */
	public unterrichtGetByIdOrException(idUnterricht : number) : StundenplanUnterricht {
		return DeveloperNotificationException.ifMapGetIsNull(this._unterricht_by_id, idUnterricht);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekte.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekte.
	 */
	public unterrichtGetMengeAsList() : List<StundenplanUnterricht> {
		return this._unterrichtmenge;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekte, die entfernt wurden,
	 * da es derzeit kein zu gehöriges {@link StundenplanKlassenunterricht}-Objekt gibt.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekte.
	 */
	public unterrichtGetMengeUngueltigAsList() : List<StundenplanUnterricht> {
		return this._unterrichtmenge_ungueltig;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekte eines Faches.
	 *
	 * @param idFach   Die Datenbank-ID des Faches.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekte eines Faches.
	 */
	public unterrichtGetMengeByFachId(idFach : number) : List<StundenplanUnterricht> {
		return MapUtils.getOrCreateArrayList(this._unterrichtmenge_by_idFach, idFach);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekte des Wochentyps.
	 *
	 * @param wochentyp Der Wochentyp (0 jede Woche, 1 nur Woche A, 2 nur Woche B, ...)
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekte des Wochentyps.
	 */
	public unterrichtGetMengeByWochentyp(wochentyp : number) : List<StundenplanUnterricht> {
		return MapUtils.getOrCreateArrayList(this._unterrichtmenge_by_wochentyp, wochentyp);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekte einer Klasse.
	 *
	 * @param idKlasse   Die Datenbank-ID der Klasse.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekte einer Klasse.
	 */
	public unterrichtGetMengeByKlasseId(idKlasse : number) : List<StundenplanUnterricht> {
		return MapUtils.getOrCreateArrayList(this._unterrichtmenge_by_idKlasse, idKlasse);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekte einer Klasse mit einem bestimmten Wochentyp.
	 *
	 * @param idKlasse   Die Datenbank-ID der Klasse.
	 * @param wochentyp  Der gewünschten Wochentyp. Der Wert 0 ist nur dann erlaubt, wenn wochenTypModell ebenfalls 0 ist.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekte einer Klasse mit einem bestimmten Wochentyp.
	 */
	public unterrichtGetMengeByKlasseIdAndWochentyp(idKlasse : number, wochentyp : number) : List<StundenplanUnterricht> {
		DeveloperNotificationException.ifTrue("wochentyp > stundenplanWochenTypModell", wochentyp > this._stundenplanWochenTypModell);
		const listU : List<StundenplanUnterricht> = MapUtils.getOrCreateArrayList(this._unterrichtmenge_by_idKlasse, idKlasse);
		return CollectionUtils.toFilteredArrayList(listU, { test : (u: StundenplanUnterricht) => (u.wochentyp === 0) || (u.wochentyp === wochentyp) });
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
	public unterrichtGetMengeByKlasseIdAndJahrAndKW(idKlasse : number, jahr : number, kalenderwoche : number) : List<StundenplanUnterricht> {
		const wochentyp : number = this.kalenderwochenzuordnungGetWochentypOrDefault(jahr, kalenderwoche);
		return this.unterrichtGetMengeByKlasseIdAndWochentyp(idKlasse, wochentyp);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekte eines Klassenunterrichts (Klasse, Fach).
	 *
	 * @param idKlasse   Die Datenbank-ID der Klasse.
	 * @param idFach     Die Datenbank-ID des Faches.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekte eines Klassenunterrichts (Klasse, Fach).
	 */
	public unterrichtGetMengeByKlasseIdAndFachId(idKlasse : number, idFach : number) : List<StundenplanUnterricht> {
		return Map2DUtils.getOrCreateArrayList(this._unterrichtmenge_by_idKlasse_and_idFach, idKlasse, idFach);
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
	public unterrichtGetMengeByKlasseIdAndFachIdAndWochentyp(idKlasse : number, idFach : number, wochentyp : number) : List<StundenplanUnterricht> {
		DeveloperNotificationException.ifTrue("wochentyp > stundenplanWochenTypModell", wochentyp > this._stundenplanWochenTypModell);
		const listU : List<StundenplanUnterricht> = DeveloperNotificationException.ifMap2DGetIsNull(this._unterrichtmenge_by_idKlasse_and_idFach, idKlasse, idFach);
		return CollectionUtils.toFilteredArrayList(listU, { test : (u: StundenplanUnterricht) => (u.wochentyp === 0) || (u.wochentyp === wochentyp) });
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
	public unterrichtGetMengeByKlasseIdAndFachIdAndJahrAndKW(idKlasse : number, idFach : number, jahr : number, kalenderwoche : number) : List<StundenplanUnterricht> {
		const wochentyp : number = this.kalenderwochenzuordnungGetWochentypOrDefault(jahr, kalenderwoche);
		return this.unterrichtGetMengeByKlasseIdAndFachIdAndWochentyp(idKlasse, idFach, wochentyp);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekte eines Kurses.
	 *
	 * @param idkurs     Die ID des Kurses.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekte eines Kurses.
	 */
	public unterrichtGetMengeByKurs(idkurs : number) : List<StundenplanUnterricht> {
		return MapUtils.getOrCreateArrayList(this._unterrichtmenge_by_idKurs, idkurs);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht} eines Kurses mit einem bestimmten Wochentyp.
	 *
	 * @param idkurs     Die ID des Kurses.
	 * @param wochentyp  Der gewünschten Wochentyp. Der Wert 0 ist nur dann erlaubt, wenn wochenTypModell ebenfalls 0 ist.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht} eines Kurses in einer bestimmten Kalenderwoche.
	 */
	public unterrichtGetMengeByKursIdAndWochentyp(idkurs : number, wochentyp : number) : List<StundenplanUnterricht> {
		DeveloperNotificationException.ifTrue("wochentyp > stundenplanWochenTypModell", wochentyp > this._stundenplanWochenTypModell);
		const listU : List<StundenplanUnterricht> = MapUtils.getOrCreateArrayList(this._unterrichtmenge_by_idKurs, idkurs);
		return CollectionUtils.toFilteredArrayList(listU, { test : (u: StundenplanUnterricht) => (u.wochentyp === 0) || (u.wochentyp === wochentyp) });
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht} einer Kursmenge mit einem bestimmten Wochentyp.
	 *
	 * @param idsKurs   Die IDs aller Kurse.
	 * @param wochentyp Der gewünschten Wochentyp. Der Wert 0 ist nur dann erlaubt, wenn wochenTypModell ebenfalls 0 ist.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht} einer Kursmenge mit einem bestimmten Wochentyp.
	 */
	public unterrichtGetMengeByKursIdsAndWochentyp(idsKurs : Array<number>, wochentyp : number) : List<StundenplanUnterricht> {
		const result : List<StundenplanUnterricht> = new ArrayList<StundenplanUnterricht>();
		for (const idKurs of idsKurs)
			result.addAll(this.unterrichtGetMengeByKursIdAndWochentyp(idKurs, wochentyp));
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
	public unterrichtGetMengeByKursIdAndJahrAndKW(idKurs : number, jahr : number, kalenderwoche : number) : List<StundenplanUnterricht> {
		const wochentyp : number = this.kalenderwochenzuordnungGetWochentypOrDefault(jahr, kalenderwoche);
		return this.unterrichtGetMengeByKursIdAndWochentyp(idKurs, wochentyp);
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
	public unterrichtGetMengeByKursIdsAndJahrAndKW(idsKurs : Array<number>, jahr : number, kalenderwoche : number) : List<StundenplanUnterricht> {
		const wochentyp : number = this.kalenderwochenzuordnungGetWochentypOrDefault(jahr, kalenderwoche);
		return this.unterrichtGetMengeByKursIdsAndWochentyp(idsKurs, wochentyp);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekt, die im übergeben Zeitraster und Wochentyp liegen.
	 *
	 * @param idZeitraster  Die Datenbank-ID des Zeitrasters.
	 * @param wochentyp     Der Wochentyp (-1 = alles, 0 jede Woche, 1 nur Woche A, 2 nur Woche B, ...)
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekt, die im übergeben Zeitraster und Wochentyp liegen.
	 */
	public unterrichtGetMengeByZeitrasterIdAndWochentypOrEmptyList(idZeitraster : number, wochentyp : number) : List<StundenplanUnterricht> {
		return Map2DUtils.getOrCreateArrayList(this._unterrichtmenge_by_idZeitraster_and_wochentyp, idZeitraster, wochentyp);
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
	public unterrichtGetMengeByZeitrasterIdAndWochentypAndInklusiveOrEmptyList(idZeitraster : number, wochentyp : number, inklWoche0 : boolean) : List<StundenplanUnterricht> {
		if ((wochentyp === 0) || (!inklWoche0))
			return this.unterrichtGetMengeByZeitrasterIdAndWochentypOrEmptyList(idZeitraster, wochentyp);
		const list : List<StundenplanUnterricht> = new ArrayList<StundenplanUnterricht>();
		list.addAll(this.unterrichtGetMengeByZeitrasterIdAndWochentypOrEmptyList(idZeitraster, wochentyp));
		list.addAll(this.unterrichtGetMengeByZeitrasterIdAndWochentypOrEmptyList(idZeitraster, 0));
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
	public unterrichtGetMengeByWochentagAndStundeAndWochentypOrEmptyList(wochentag : Wochentag, stunde : number, wochentyp : number) : List<StundenplanUnterricht> {
		const zeitraster : StundenplanZeitraster | null = this._zeitraster_by_wochentag_and_stunde.getOrNull(wochentag.id, stunde);
		if (zeitraster !== null)
			return Map2DUtils.getOrCreateArrayList(this._unterrichtmenge_by_idZeitraster_and_wochentyp, zeitraster.id, wochentyp);
		return new ArrayList<StundenplanUnterricht>();
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
	public unterrichtGetMengeByWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(wochentag : Wochentag, stunde : number, wochentyp : number, inklWoche0 : boolean) : List<StundenplanUnterricht> {
		const idZeitraster : number = this.zeitrasterGetByWochentagAndStundeOrException(wochentag.id, stunde).id;
		return this.unterrichtGetMengeByZeitrasterIdAndWochentypAndInklusiveOrEmptyList(idZeitraster, wochentyp, inklWoche0);
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
	public unterrichtGetMengeByKlasseIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(idKlasse : number, wochentag : number, stunde : number, wochentyp : number, inklWoche0 : boolean) : List<StundenplanUnterricht> {
		const list : List<StundenplanUnterricht> = new ArrayList<StundenplanUnterricht>();
		const z : StundenplanZeitraster | null = this._zeitraster_by_wochentag_and_stunde.getOrNull(wochentag, stunde);
		if (z !== null)
			for (const u of Map2DUtils.getOrCreateArrayList(this._unterrichtmenge_by_idKlasse_and_idZeitraster, idKlasse, z.id))
				if ((u.wochentyp === wochentyp) || ((u.wochentyp === 0) && inklWoche0))
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
	public unterrichtGetMengeByKlasseIdAndWochentagAndStundeAndWochentypAndSchieneAndInklusiveOrEmptyList(idKlasse : number, wochentag : number, stunde : number, wochentyp : number, idSchiene : number, inklWoche0 : boolean) : List<StundenplanUnterricht> {
		const list : List<StundenplanUnterricht> = new ArrayList<StundenplanUnterricht>();
		for (const u of this.unterrichtGetMengeByKlasseIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(idKlasse, wochentag, stunde, wochentyp, inklWoche0))
			if (this.unterrichtHatSchiene(u, idSchiene))
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
	public unterrichtGetMengeByLehrerIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(idLehrer : number, wochentag : number, stunde : number, wochentyp : number, inklWoche0 : boolean) : List<StundenplanUnterricht> {
		const list : List<StundenplanUnterricht> = new ArrayList<StundenplanUnterricht>();
		const z : StundenplanZeitraster | null = this._zeitraster_by_wochentag_and_stunde.getOrNull(wochentag, stunde);
		if (z !== null)
			for (const u of Map2DUtils.getOrCreateArrayList(this._unterrichtmenge_by_idLehrer_and_idZeitraster, idLehrer, z.id))
				if ((u.wochentyp === wochentyp) || ((u.wochentyp === 0) && inklWoche0))
					list.add(u);
		return list;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekte einer Lehrkraft.
	 *
	 * @param idLehrer   Die Datenbank-ID der Lehrkraft.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekte einer Lehrkraft.
	 */
	public unterrichtGetMengeByLehrerId(idLehrer : number) : List<StundenplanUnterricht> {
		return MapUtils.getOrCreateArrayList(this._unterrichtmenge_by_idLehrer, idLehrer);
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
	public unterrichtGetMengeByLehrerIdAndWochentagAndStundeAndWochentypAndSchieneAndInklusiveOrEmptyList(idLehrer : number, wochentag : number, stunde : number, wochentyp : number, idSchiene : number, inklWoche0 : boolean) : List<StundenplanUnterricht> {
		const list : List<StundenplanUnterricht> = new ArrayList<StundenplanUnterricht>();
		for (const u of this.unterrichtGetMengeByLehrerIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(idLehrer, wochentag, stunde, wochentyp, inklWoche0))
			if (this.unterrichtHatSchiene(u, idSchiene))
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
	public unterrichtGetMengeBySchuelerIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(idSchueler : number, wochentag : number, stunde : number, wochentyp : number, inklWoche0 : boolean) : List<StundenplanUnterricht> {
		const list : List<StundenplanUnterricht> = new ArrayList<StundenplanUnterricht>();
		const z : StundenplanZeitraster | null = this._zeitraster_by_wochentag_and_stunde.getOrNull(wochentag, stunde);
		if (z !== null)
			for (const u of Map2DUtils.getOrCreateArrayList(this._unterrichtmenge_by_idSchueler_and_idZeitraster, idSchueler, z.id))
				if ((u.wochentyp === wochentyp) || ((u.wochentyp === 0) && inklWoche0))
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
	public unterrichtGetMengeBySchuelerIdAndWochentagAndStundeAndWochentypAndSchieneAndInklusiveOrEmptyList(idSchueler : number, wochentag : number, stunde : number, wochentyp : number, idSchiene : number, inklWoche0 : boolean) : List<StundenplanUnterricht> {
		const list : List<StundenplanUnterricht> = new ArrayList<StundenplanUnterricht>();
		for (const u of this.unterrichtGetMengeBySchuelerIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(idSchueler, wochentag, stunde, wochentyp, inklWoche0))
			if (this.unterrichtHatSchiene(u, idSchiene))
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
	public unterrichtGetMengeByJahrgangIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(idJahrgang : number, wochentag : number, stunde : number, wochentyp : number, inklWoche0 : boolean) : List<StundenplanUnterricht> {
		const list : List<StundenplanUnterricht> = new ArrayList<StundenplanUnterricht>();
		const z : StundenplanZeitraster | null = this._zeitraster_by_wochentag_and_stunde.getOrNull(wochentag, stunde);
		if (z !== null)
			for (const u of Map2DUtils.getOrCreateArrayList(this._unterrichtmenge_by_idJahrgang_and_idZeitraster, idJahrgang, z.id))
				if ((u.wochentyp === wochentyp) || ((u.wochentyp === 0) && inklWoche0))
					list.add(u);
		list.sort(this._compUnterricht);
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
	public unterrichtGetMengeByJahrgangIdAndWochentagAndStundeAndWochentypAndSchieneAndInklusiveOrEmptyList(idJahrgang : number, wochentag : number, stunde : number, wochentyp : number, idSchiene : number, inklWoche0 : boolean) : List<StundenplanUnterricht> {
		const list : List<StundenplanUnterricht> = new ArrayList<StundenplanUnterricht>();
		for (const u of this.unterrichtGetMengeByJahrgangIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(idJahrgang, wochentag, stunde, wochentyp, inklWoche0))
			if (this.unterrichtHatSchiene(u, idSchiene))
				list.add(u);
		return list;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekte des Faches am "wochentag, stunde, wochentyp" einer bestimmten Schiene.
	 * Falls der Parameter "inklWoche0" TRUE ist und der "wochentyp" größer als 0 ist, wird der Unterricht des Wochentyps 0 auch hinzugefügt.
	 *
	 * @param idFach        Die ID des {@link StundenplanFach}-Objektes.
	 * @param wochentag     Die ID des {@link Wochentag}-ENUM.
	 * @param stunde        Die Unterrichtsstunde.
	 * @param wochentyp     Der Wochentyp (0 jede Woche, 1 nur Woche A, 2 nur Woche B, ...)
	 * @param idSchiene     Die Datenbank-ID der Schiene, oder -1, falls Unterricht ohne Schiene gemeint ist, oder -2, falls die Schiene egal ist.
	 * @param inklWoche0    falls TRUE, wird Unterricht des Wochentyps 0 hinzugefügt.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekte des Faches am "wochentag, stunde, wochentyp".
	 */
	public unterrichtGetMengeByFachIdAndWochentagAndStundeAndWochentypAndSchieneAndInklusiveOrEmptyList(idFach : number, wochentag : number, stunde : number, wochentyp : number, idSchiene : number, inklWoche0 : boolean) : List<StundenplanUnterricht> {
		const list : List<StundenplanUnterricht> = new ArrayList<StundenplanUnterricht>();
		const z : StundenplanZeitraster | null = this._zeitraster_by_wochentag_and_stunde.getOrNull(wochentag, stunde);
		if (z === null)
			return list;
		if ((inklWoche0) && (wochentyp !== 0))
			for (const u of Map3DUtils.getOrCreateArrayList(this._unterrichtmenge_by_idZeitraster_wochentyp_fach, z.id, 0, idFach))
				if (this.unterrichtHatSchiene(u, idSchiene) || (idSchiene === -2))
					list.add(u);
		for (const u of Map3DUtils.getOrCreateArrayList(this._unterrichtmenge_by_idZeitraster_wochentyp_fach, z.id, wochentyp, idFach))
			if (this.unterrichtHatSchiene(u, idSchiene) || (idSchiene === -2))
				list.add(u);
		return list;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekte eines Raumes.
	 *
	 * @param idRaum   Die ID des {@link StundenplanRaum}-Objektes.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekte eines Raumes.
	 */
	public unterrichtGetMengeByRaumId(idRaum : number) : List<StundenplanUnterricht> {
		return MapUtils.getOrCreateArrayList(this._unterrichtmenge_by_idRaum, idRaum);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekte des Raumes am "wochentag, stunde, wochentyp" einer bestimmten Schiene.
	 * Falls der Parameter "inklWoche0" TRUE ist und der "wochentyp" größer als 0 ist, wird der Unterricht des Wochentyps 0 auch hinzugefügt.
	 *
	 * @param idRaum        Die ID des {@link StundenplanRaum}-Objektes.
	 * @param wochentag     Die ID des {@link Wochentag}-ENUM.
	 * @param stunde        Die Unterrichtsstunde.
	 * @param wochentyp     Der Wochentyp (0 jede Woche, 1 nur Woche A, 2 nur Woche B, ...)
	 * @param idSchiene     Die Datenbank-ID der Schiene, oder -1, falls Unterricht ohne Schiene gemeint ist, oder -2, falls die Schiene egal ist.
	 * @param inklWoche0    falls TRUE, wird Unterricht des Wochentyps 0 hinzugefügt.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekte des Raumes am "wochentag, stunde, wochentyp".
	 */
	public unterrichtGetMengeByRaumIdAndWochentagAndStundeAndWochentypAndSchieneAndInklusiveOrEmptyList(idRaum : number, wochentag : number, stunde : number, wochentyp : number, idSchiene : number, inklWoche0 : boolean) : List<StundenplanUnterricht> {
		const list : List<StundenplanUnterricht> = new ArrayList<StundenplanUnterricht>();
		const z : StundenplanZeitraster | null = this._zeitraster_by_wochentag_and_stunde.getOrNull(wochentag, stunde);
		if (z === null)
			return list;
		if ((inklWoche0) && (wochentyp !== 0))
			for (const u of Map3DUtils.getOrCreateArrayList(this._unterrichtmenge_by_idZeitraster_wochentyp_raum, z.id, 0, idRaum))
				if (this.unterrichtHatSchiene(u, idSchiene) || (idSchiene === -2))
					list.add(u);
		for (const u of Map3DUtils.getOrCreateArrayList(this._unterrichtmenge_by_idZeitraster_wochentyp_raum, z.id, wochentyp, idRaum))
			if (this.unterrichtHatSchiene(u, idSchiene) || (idSchiene === -2))
				list.add(u);
		return list;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekte des (Raumes, Zeitrasters, Wochentyps).
	 * Falls der Parameter "inklWoche0" TRUE ist und der "wochentyp" größer als 0 ist, wird der Unterricht des Wochentyps 0 auch hinzugefügt.
	 *
	 * @param idRaum        Die ID des {@link StundenplanRaum}-Objektes.
	 * @param idZeitraster  Die ID des {@link StundenplanZeitraster}-Objektes.
	 * @param wochentyp     Der Wochentyp (0 jede Woche, 1 nur Woche A, 2 nur Woche B, ...)
	 * @param inklWoche0    falls TRUE, wird Unterricht des Wochentyps 0 hinzugefügt.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekte des Raumes am "wochentag, stunde, wochentyp".
	 */
	public unterrichtGetMengeByRaumIdAndZeitrasterIdAndWochentypAndInklusiveOrEmptyList(idRaum : number, idZeitraster : number, wochentyp : number, inklWoche0 : boolean) : List<StundenplanUnterricht> {
		const list : List<StundenplanUnterricht> = new ArrayList<StundenplanUnterricht>();
		const z : StundenplanZeitraster | null = this._zeitraster_by_id.get(idZeitraster);
		if (z === null)
			return list;
		if ((inklWoche0) && (wochentyp !== 0))
			for (const u of Map3DUtils.getOrCreateArrayList(this._unterrichtmenge_by_idZeitraster_wochentyp_raum, z.id, 0, idRaum))
				list.add(u);
		for (const u of Map3DUtils.getOrCreateArrayList(this._unterrichtmenge_by_idZeitraster_wochentyp_raum, z.id, wochentyp, idRaum))
			list.add(u);
		return list;
	}

	/**
	 * Liefert eine Liste aller Geschwister-{@link StundenplanUnterricht}-Objekte.
	 * <br> Vorsicht: Der eigene Unterricht wird ebenso mitgeliefert!
	 *
	 * @param idUnterricht   Die Datenbank-ID des Unterrichts
	 *
	 * @return eine eine Liste aller Geschwister-{@link StundenplanUnterricht}-Objekte.
	 */
	public unterrichtGetMengeByUnterrichtId(idUnterricht : number) : List<StundenplanUnterricht> {
		return MapUtils.getOrCreateArrayList(this._unterrichtmenge_by_idUnterricht, idUnterricht);
	}

	/**
	 * Liefert eine String-Repräsentation des Faches/Kurses eines {@link StundenplanUnterricht}-Objektes.
	 * <br> Kurse immer im Format "M-LK1-Suffix".
	 * <br> Klassenunterricht wahlweise "Mathematik" oder "M".
	 *
	 * @param idUnterricht                  Die Datenbank-ID des {@link StundenplanUnterricht}.
	 * @param klassenunterrichtDetailliert  TRUE/FALSE führt zu "Deutsch"/"D" (nur beim Klassenunterricht).
	 *
	 * @return eine String-Repräsentation des Faches/Kurses eines {@link StundenplanUnterricht}-Objektes.
	 */
	public unterrichtGetByIDStringOfFachOderKurs(idUnterricht : number, klassenunterrichtDetailliert : boolean) : string {
		const unterricht : StundenplanUnterricht | null = this._unterricht_by_id.get(idUnterricht);
		if (unterricht === null)
			return "Unterricht ???";
		if (unterricht.idKurs === null) {
			const fach : StundenplanFach = DeveloperNotificationException.ifMapGetIsNull(this._fach_by_id, unterricht.idFach);
			return klassenunterrichtDetailliert ? fach.bezeichnung : fach.kuerzel;
		}
		const kurs : StundenplanKurs = DeveloperNotificationException.ifMapGetIsNull(this._kurs_by_id, unterricht.idKurs);
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
	public unterrichtGetByIDStringOfKlassen(idUnterricht : number) : string {
		const unterricht : StundenplanUnterricht = DeveloperNotificationException.ifMapGetIsNull(this._unterricht_by_id, idUnterricht);
		const kuerzel : AVLSet<string> = new AVLSet<string>();
		for (const idKlasse of unterricht.klassen) {
			const klasse : StundenplanKlasse = DeveloperNotificationException.ifMapGetIsNull(this._klasse_by_id, idKlasse);
			kuerzel.add(klasse.kuerzel);
		}
		return StringUtils.collectionToCommaSeparatedString(kuerzel);
	}

	/**
	 * Liefert eine String-Repräsentation der Raummenge des {@link StundenplanUnterricht}.
	 * <br>Beispiel: "1.01" bei einem Raum und "T1, T2" bei mehreren (z.B. Sporthallen...)
	 *
	 * @param idUnterricht  Die Datenbank-ID des {@link StundenplanUnterricht}.
	 *
	 * @return eine String-Repräsentation der Raummenge des {@link StundenplanUnterricht}.
	 */
	public unterrichtGetByIDStringOfRaeume(idUnterricht : number) : string {
		const unterricht : StundenplanUnterricht = DeveloperNotificationException.ifMapGetIsNull(this._unterricht_by_id, idUnterricht);
		const kuerzel : List<string> = new ArrayList<string>();
		for (const idRaum of unterricht.raeume) {
			const raum : StundenplanRaum = DeveloperNotificationException.ifMapGetIsNull(this._raum_by_id, idRaum);
			kuerzel.add(raum.kuerzel);
		}
		kuerzel.sort({ compare : (o1: string, o2: string) => JavaString.compareTo(o1, o2) });
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
	public unterrichtGetByIDStringOfSchienen(idUnterricht : number) : string {
		const unterricht : StundenplanUnterricht = DeveloperNotificationException.ifMapGetIsNull(this._unterricht_by_id, idUnterricht);
		const kuerzel : AVLSet<string> = new AVLSet<string>();
		for (const idSchiene of unterricht.schienen) {
			const schiene : StundenplanSchiene = DeveloperNotificationException.ifMapGetIsNull(this._schiene_by_id, idSchiene);
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
	public unterrichtGetByIDStringOfAll(idUnterricht : number) : string {
		let sLe : string | null = this.unterrichtGetByIDLehrerFirstAsStringOrEmpty(idUnterricht);
		let sFa : string | null = this.unterrichtGetByIDStringOfFachOderKurs(idUnterricht, false);
		let sKl : string | null = this.unterrichtGetByIDStringOfKlassen(idUnterricht);
		let sRa : string | null = this.unterrichtGetByIDStringOfRaeume(idUnterricht);
		let sSc : string | null = this.unterrichtGetByIDStringOfSchienen(idUnterricht);
		sLe = JavaString.isEmpty(sLe) ? "" : (", " + sLe);
		sFa = JavaString.isEmpty(sFa) ? "" : (", " + sFa);
		sKl = JavaString.isEmpty(sKl) ? "" : (", " + sKl);
		sRa = JavaString.isEmpty(sRa) ? "" : (", " + sRa);
		sSc = JavaString.isEmpty(sSc) ? "" : (", " + sSc);
		return idUnterricht + sLe + sFa + sKl + sRa + sSc;
	}

	/**
	 * Liefert einen String, der diesen Unterricht beschreibt, in der Form 'M BAR 06b'.
	 *
	 * @param idUnterricht  Die Datenbank-ID des Unterrichts.
	 *
	 * @return einen String, der diesen Unterricht beschreibt, in der Form 'M BAR 06b'.
	 */
	private unterrichtGetByID_StringOfFaLeKl(idUnterricht : number) : string {
		const sFa : string | null = this.unterrichtGetByIDStringOfFachOderKurs(idUnterricht, false);
		const sLe : string | null = this.unterrichtGetByIDLehrerFirstAsStringOrEmpty(idUnterricht);
		const sKl : string | null = this.unterrichtGetByIDStringOfKlassen(idUnterricht);
		let s : string | null = sFa;
		s = s + (JavaString.isEmpty(s) ? "" : " ") + sLe;
		s = s + (JavaString.isEmpty(s) ? "" : " ") + sKl;
		return s;
	}

	/**
	 * Liefert die Menge aller {@link StundenplanLehrer}-Objekte des {@link StundenplanUnterricht}.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idUnterricht  Die Datenbank-ID des {@link StundenplanUnterricht}.
	 *
	 * @return die Menge aller {@link StundenplanLehrer}-Objekte des {@link StundenplanUnterricht}.
	 */
	public unterrichtGetByIDLehrerMenge(idUnterricht : number) : List<StundenplanLehrer> {
		return MapUtils.getOrCreateArrayList(this._lehrermenge_by_idUnterricht, idUnterricht);
	}

	/**
	 * Liefert die Menge aller {@link StundenplanLehrer} des {@link StundenplanUnterricht} als kommaseparierter String.
	 * <br>Laufzeit: O(|Ergebnis|)
	 *
	 * @param idUnterricht  Die Datenbank-ID des {@link StundenplanUnterricht}.
	 *
	 * @return die Menge aller {@link StundenplanLehrer} des {@link StundenplanUnterricht} als kommaseparierter String.
	 */
	public unterrichtGetByIDLehrerMengeAsString(idUnterricht : number) : string {
		const lehrkraefteDesUnterrichts : List<StundenplanLehrer> = MapUtils.getOrCreateArrayList(this._lehrermenge_by_idUnterricht, idUnterricht);
		const listeDerKuerzel : List<string> = new ArrayList<string>();
		for (const lehkraft of lehrkraefteDesUnterrichts)
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
	public unterrichtGetByIDLehrerFirstOrNull(idUnterricht : number) : StundenplanLehrer | null {
		const lehrerDesUnterrichts : List<StundenplanLehrer> = MapUtils.getOrCreateArrayList(this._lehrermenge_by_idUnterricht, idUnterricht);
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
	public unterrichtGetByIDLehrerFirstAsStringOrEmpty(idUnterricht : number) : string {
		const lehrkraft : StundenplanLehrer | null = this.unterrichtGetByIDLehrerFirstOrNull(idUnterricht);
		return (lehrkraft === null) ? "" : lehrkraft.kuerzel;
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
	public unterrichtsstundeGetUhrzeitenAsStrings(stunde : number) : List<string> {
		const listUhrzeit : List<string> = new ArrayList<string>();
		const listWochentagVon : List<string> = new ArrayList<string>();
		const listWochentagBis : List<string> = new ArrayList<string>();
		for (let wochentag : number = this._zeitrasterWochentagMin; wochentag <= this._zeitrasterWochentagMax; wochentag++) {
			const sUhrzeit : string = this.unterrichtsstundeGetUhrzeitAsString(wochentag, stunde);
			const sWochentag : string = Wochentag.fromIDorException(wochentag).kuerzel;
			if (listUhrzeit.isEmpty()) {
				listUhrzeit.add(sUhrzeit);
				listWochentagVon.add(sWochentag);
				listWochentagBis.add(sWochentag);
				continue;
			}
			const sUhrzeitDavor : string = DeveloperNotificationException.ifListGetLastFailes("listUhrzeit", listUhrzeit);
			if (JavaObject.equalsTranspiler(sUhrzeitDavor, (sUhrzeit))) {
				listWochentagBis.set(listWochentagBis.size() - 1, sWochentag);
			} else {
				listUhrzeit.add(sUhrzeit);
				listWochentagVon.add(sWochentag);
				listWochentagBis.add(sWochentag);
			}
		}
		if (listUhrzeit.size() <= 1)
			return listUhrzeit;
		for (let i : number = 0; i < listUhrzeit.size(); i++) {
			const sUhrzeit : string = listUhrzeit.get(i);
			const sWochentagVon : string = listWochentagVon.get(i);
			const sWochentagBis : string = listWochentagBis.get(i);
			if (JavaObject.equalsTranspiler(sWochentagVon, (sWochentagBis)))
				listUhrzeit.set(i, sWochentagVon + " " + sUhrzeit);
			else
				listUhrzeit.set(i, sWochentagVon + "–" + sWochentagBis + " " + sUhrzeit);
		}
		return listUhrzeit;
	}

	private unterrichtsstundeGetUhrzeitAsString(wochentag : number, stunde : number) : string {
		const zeitraster : StundenplanZeitraster | null = this._zeitraster_by_wochentag_and_stunde.getOrNull(wochentag, stunde);
		if (zeitraster === null)
			return "???";
		const sBeginn : string = (zeitraster.stundenbeginn === null) ? "??:??" : DateUtils.getStringOfUhrzeitFromMinuten(zeitraster.stundenbeginn);
		const sEnde : string = (zeitraster.stundenende === null) ? "??:??" : DateUtils.getStringOfUhrzeitFromMinuten(zeitraster.stundenende);
		return sBeginn + "–" + sEnde + " Uhr";
	}

	/**
	 * Liefert TRUE, falls es {@link StundenplanUnterricht} gibt, der einen Wochentyp > 0 hat.
	 * <br>Laufzeit: O(1)
	 *
	 * @return TRUE, falls es {@link StundenplanUnterricht} gibt, der einen Wochentyp > 0 hat.
	 */
	public unterrichtHatMultiWochen() : boolean {
		return this._unterrichtHatMultiWochen;
	}

	/**
	 * Liefert TRUE, falls der Unterricht in der übergebenen Schiene liegt, oder falls er in keiner Schiene liegt und idSchiene negativ ist.
	 *
	 * @param u          Der Unterricht der durchsucht wird.
	 * @param idSchiene  Die Datenbank-ID der Schiene nach der gesucht wird.
	 *
	 * @return TRUE, falls der Unterricht in der übergebenen Schiene liegt, oder falls er in keiner Schiene liegt und idSchiene negativ ist.
	 */
	public unterrichtHatSchiene(u : StundenplanUnterricht, idSchiene : number) : boolean {
		const schienen : List<StundenplanSchiene> = MapUtils.getOrCreateArrayList(this._schienenmenge_by_idUnterricht, u.id);
		if (idSchiene < 0)
			return schienen.isEmpty();
		for (const schiene of schienen)
			if (schiene.id === idSchiene)
				return true;
		return false;
	}

	/**
	 * Liefert TRUE, falls ein Unterricht in ein bestimmtes Zeitraster verschoben werden darf.
	 *
	 * @param quellUnterricht  Der {@link StundenplanUnterricht}, welcher verschoben werden soll und noch nicht verschoben wurde.
	 * @param zielZeitraster   Das {@link StundenplanZeitraster}, wohin verschoben werden soll.
	 * @param zielWochentyp    Der Wochentyp, wohin verschoben werden soll.
	 *
	 * @return TRUE, falls ein Unterricht in ein bestimmtes Zeitraster verschoben werden darf.
	 */
	public unterrichtIstVerschiebenErlaubtNach(quellUnterricht : StundenplanUnterricht, zielZeitraster : StundenplanZeitraster, zielWochentyp : number) : boolean {
		for (const partner of DeveloperNotificationException.ifMapGetIsNull(this._unterrichtmenge_by_idUnterricht, quellUnterricht.id)) {
			if (partner.idZeitraster !== zielZeitraster.id)
				continue;
			if (quellUnterricht.id === partner.id)
				continue;
			if ((partner.wochentyp === 0) || (zielWochentyp === 0) || (zielWochentyp === partner.wochentyp))
				return false;
		}
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
	public unterrichtPatchAttributes(u : StundenplanUnterricht) : void {
		this.unterrichtPatchAttributesAll(ListUtils.create1(u));
	}

	/**
	 * Aktualisiert alle {@link StundenplanUnterricht}-Objekte der Liste.
	 * Hinweis: Die ID kann nicht gepatched werden. Sie wird verwendet, um das zu ersetzende Objekte zu identifizieren.
	 *
	 * @param list Die Liste der neuen {@link StundenplanUnterricht}-Objekte.
	 */
	public unterrichtPatchAttributesAll(list : List<StundenplanUnterricht>) : void {
		for (const u of list)
			this.unterrichtCheckAttributes(u);
		this.unterrichtCheckDuplicateInCell(list);
		for (const u of list) {
			DeveloperNotificationException.ifMapRemoveFailes(this._unterricht_by_id, u.id);
			DeveloperNotificationException.ifMapPutOverwrites(this._unterricht_by_id, u.id, u);
		}
		this.update_all();
	}

	private unterrichtRemoveByIdOhneUpdate(idUnterricht : number) : void {
		if (this._unterrichtmenge_ungueltig_set.contains(idUnterricht)) {
			const iter : JavaIterator<StundenplanUnterricht> = this._unterrichtmenge_ungueltig.iterator();
			while (iter.hasNext())
				if (iter.next().id === idUnterricht)
					iter.remove();
		} else {
			DeveloperNotificationException.ifMapRemoveFailes(this._unterricht_by_id, idUnterricht);
		}
	}

	/**
	 * Entfernt alle {@link StundenplanUnterricht}-Objekte.
	 *
	 * @param listUnterricht  Die Liste der zu entfernenden {@link StundenplanUnterricht}-Objekte.
	 */
	public unterrichtRemoveAll(listUnterricht : List<StundenplanUnterricht>) : void {
		for (const u of listUnterricht)
			this.unterrichtRemoveByIdOhneUpdate(u.id);
		this.update_all();
	}

	/**
	 * Liefert die Menge aller Unterrichtsgruppen, die sich zu einem einzigen Unterricht des Wochentyps 0 "mergen" lassen.
	 * <br>Wenn die Liste nicht leer ist, dann sollte die GUI dem Benutzer ein "Mergen" anbieten.
	 * <br>Pro Gruppe müssten alle Unterrichte gelöscht werden und anschließend kann eines der Elemente wieder hinzugefügt werden, jedoch mit Wochentyp 0.
	 *
	 * @return die Menge aller Unterrichtsgruppen, die sich zu einem einzigen Unterricht des Wochentyps 0 "mergen" lassen.
	 */
	public unterrichtsgruppenMergeableGet() : List<List<StundenplanUnterricht>> {
		return this._unterrichtsgruppenMergeable;
	}

	/**
	 * Liefert ein Array der Größe {@link #_stundenplanWochenTypModell} mit TRUE-Werten, falls der Wochentyp (Index) derzeit verwendet wird.
	 *
	 * @return ein Array der Größe {@link #_stundenplanWochenTypModell} mit TRUE-Werten, falls der Wochentyp (Index) derzeit verwendet wird.
	 */
	public wochentypGetMengeVerwendet() : Array<boolean> | null {
		const temp : Array<boolean> | null = Array(this._stundenplanWochenTypModell).fill(false);
		for (let wochentyp : number = 0; wochentyp < this._stundenplanWochenTypModell; wochentyp++)
			temp[wochentyp] = !MapUtils.getOrCreateArrayList(this._unterrichtmenge_by_wochentyp, wochentyp).isEmpty();
		return temp;
	}

	/**
	 * Fügt ein {@link StundenplanZeitraster}-Objekt hinzu.
	 *
	 * @param zeitraster  Das {@link StundenplanZeitraster}-Objekt, welches hinzugefügt werden soll.
	 */
	public zeitrasterAdd(zeitraster : StundenplanZeitraster) : void {
		this.zeitrasterAddAll(ListUtils.create1(zeitraster));
	}

	/**
	 * Fügt alle {@link StundenplanZeitraster}-Objekte hinzu.
	 *
	 * @param listZeitraster  Die Menge der {@link StundenplanZeitraster}-Objekte, welche hinzugefügt werden soll.
	 */
	public zeitrasterAddAll(listZeitraster : List<StundenplanZeitraster>) : void {
		this.zeitrasterAddAllOhneUpdate(listZeitraster);
		this.update_all();
	}

	private zeitrasterAddAllOhneUpdate(list : List<StundenplanZeitraster>) : void {
		const setOfIDs : JavaSet<number> = new HashSet<number>();
		const setOfWochentagStunde : JavaSet<string> = new HashSet<string>();
		for (const z of list) {
			StundenplanManager.zeitrasterCheck(z);
			DeveloperNotificationException.ifTrue("zeitrasterAddAllOhneUpdate: ID=" + z.id + " existiert bereits!", this._zeitraster_by_id.containsKey(z.id));
			DeveloperNotificationException.ifTrue("zeitrasterAddAllOhneUpdate: ID=" + z.id + " doppelt in der Liste!", !setOfIDs.add(z.id));
			DeveloperNotificationException.ifTrue("zeitrasterAddAllOhneUpdate: WOCHENTAG=" + z.wochentag + ", STUNDE=" + z.unterrichtstunde + " existiert bereits!", this._zeitraster_by_wochentag_and_stunde.contains(z.wochentag, z.unterrichtstunde));
			DeveloperNotificationException.ifTrue("zeitrasterAddAllOhneUpdate: WOCHENTAG=" + z.wochentag + ", STUNDE= doppelt in der Liste!", !setOfWochentagStunde.add(z.wochentag + ";" + z.unterrichtstunde));
		}
		for (const z of list)
			DeveloperNotificationException.ifMapPutOverwrites(this._zeitraster_by_id, z.id, z);
	}

	private static zeitrasterCheck(zeitraster : StundenplanZeitraster) : void {
		DeveloperNotificationException.ifInvalidID("zeitraster.id", zeitraster.id);
		Wochentag.fromIDorException(zeitraster.wochentag);
		DeveloperNotificationException.ifTrue("(zeit.unterrichtstunde < 0) || (zeit.unterrichtstunde > 29)", (zeitraster.unterrichtstunde < 0) || (zeitraster.unterrichtstunde > 29));
		if ((zeitraster.stundenbeginn !== null) && (zeitraster.stundenende !== null)) {
			const beginn : number = zeitraster.stundenbeginn.valueOf();
			const ende : number = zeitraster.stundenende.valueOf();
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
	public zeitrasterExistsByWochentagAndStunde(wochentag : number, stunde : number) : boolean {
		return this._zeitraster_by_wochentag_and_stunde.contains(wochentag, stunde);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanZeitraster}-Objekte.
	 *
	 * @return eine Liste aller {@link StundenplanZeitraster}-Objekte.
	 */
	public getListZeitraster() : List<StundenplanZeitraster> {
		return this._zeitrastermenge;
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
	public zeitrasterGetDummyListe(tagVon : number, tagBis : number, stundeVon : number, stundeBis : number) : List<StundenplanZeitraster> {
		const listDummies : List<StundenplanZeitraster> = new ArrayList<StundenplanZeitraster>();
		for (let wochentag : number = tagVon; wochentag <= tagBis; wochentag++)
			for (let stunde : number = stundeVon; stunde <= stundeBis; stunde++)
				if (!this._zeitraster_by_wochentag_and_stunde.contains(wochentag, stunde)) {
					const zeit : StundenplanZeitraster = new StundenplanZeitraster();
					zeit.id = -1;
					zeit.wochentag = wochentag;
					zeit.unterrichtstunde = stunde;
					zeit.stundenbeginn = this.zeitrasterGetDefaultStundenbeginnByStunde(stunde);
					zeit.stundenende = this.zeitrasterGetDefaultStundenendeByStunde(stunde);
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
	 *
	 * @throws DeveloperNotificationException   wenn für die Stunde ein Wert kleiner 0 angegeben wird
	 */
	public zeitrasterGetDefaultStundenbeginnByStunde(stunde : number) : number {
		DeveloperNotificationException.ifTrue("zeitrasterGetDefaultStundenbeginnByStunde: stunde < 0", stunde < 0);
		let start : number = this._stundenplanKonfig.defaultUnterrichtsbeginn + ((stunde - 1) * (this._stundenplanKonfig.defaultStundendauer + this._stundenplanKonfig.defaultPausenzeitFuerRaumwechsel));
		if ((stunde > this._stundenplanKonfig.defaultVormittagspause1Nach) && (this._stundenplanKonfig.defaultVormittagspause1Dauer > 0))
			start += this._stundenplanKonfig.defaultVormittagspause1Dauer - this._stundenplanKonfig.defaultPausenzeitFuerRaumwechsel;
		if ((stunde > this._stundenplanKonfig.defaultVormittagspause2Nach) && (this._stundenplanKonfig.defaultVormittagspause2Dauer > 0))
			start += this._stundenplanKonfig.defaultVormittagspause2Dauer - this._stundenplanKonfig.defaultPausenzeitFuerRaumwechsel;
		if ((stunde > this._stundenplanKonfig.defaultMittagspauseNach) && (this._stundenplanKonfig.defaultMittagspauseDauer > 0))
			start += this._stundenplanKonfig.defaultMittagspauseDauer - this._stundenplanKonfig.defaultPausenzeitFuerRaumwechsel;
		return start;
	}

	/**
	 * Liefert das Default-Stundenende (in Minuten nach 0 Uhr) einer Unterrichtsstunde.
	 * <br>Hinweis: Das Stundenende ist stets 45 Minuten nach dem {@link #zeitrasterGetDefaultStundenbeginnByStunde(int)} der Stunde.
	 *
	 * @param stunde  Die Unterrichtsstunde, nach welcher gefragt wird.
	 *
	 * @return das Default-Stundenende (in Minuten nach 0 Uhr) einer Unterrichtsstunde.
	 */
	public zeitrasterGetDefaultStundenendeByStunde(stunde : number) : number {
		return this.zeitrasterGetDefaultStundenbeginnByStunde(stunde) + this._stundenplanKonfig.defaultStundendauer;
	}

	/**
	 * Liefert eine Liste der {@link StundenplanZeitraster}-Objekte zu einem bestimmten Wochentag.
	 *
	 * @param wochentag der Wochentag der gewünschten Zeitraster-Objekte
	 *
	 * @return eine Liste aller {@link StundenplanZeitraster}-Objekte zum übergebenen Wochentag.
	 */
	public getListZeitrasterZuWochentag(wochentag : Wochentag) : List<StundenplanZeitraster> {
		return CollectionUtils.toFilteredArrayList(this._zeitrastermenge, { test : (z: StundenplanZeitraster) => (wochentag.id === z.wochentag) });
	}

	/**
	 * Liefert eine Liste der {@link StundenplanZeitraster}-Objekte zu einer bestimmten Unterrichtsstunde.
	 *
	 * @param unterrichtstunde   die Unterrichtsstunde der gewünschten Zeitraster-Objekte
	 *
	 * @return eine Liste aller {@link StundenplanZeitraster}-Objekte zur übergebenen Unterrichtsstunde.
	 */
	public getListZeitrasterZuStunde(unterrichtstunde : number) : List<StundenplanZeitraster> {
		return CollectionUtils.toFilteredArrayList(this._zeitrastermenge, { test : (z: StundenplanZeitraster) => (unterrichtstunde === z.unterrichtstunde) });
	}

	/**
	 * Liefert die passende Menge an {@link StundenplanZeitraster}-Objekten, welche das Intervall berühren.
	 *
	 * @param zeitrasterStart    Das {@link StundenplanZeitraster} zu dem es startet.
	 * @param minutenVerstrichen Die verstrichene Zeit (in Minuten) seit der "startzeit" .
	 *
	 * @return die passende Menge an {@link StundenplanZeitraster}-Objekten.
	 */
	public getZeitrasterByStartVerstrichen(zeitrasterStart : StundenplanZeitraster, minutenVerstrichen : number) : List<StundenplanZeitraster> {
		const wochentag : Wochentag | null = Wochentag.fromIDorException(zeitrasterStart.wochentag);
		const startzeit : number = DeveloperNotificationException.ifNull("zeitrasterStart.stundenbeginn ist NULL", zeitrasterStart.stundenbeginn).valueOf();
		return this.getZeitrasterByWochentagStartVerstrichen(wochentag, startzeit, minutenVerstrichen);
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
	public getZeitrasterByWochentagStartVerstrichen(wochentag : Wochentag, beginn : number, minutenVerstrichen : number) : List<StundenplanZeitraster> {
		const ende : number = beginn + minutenVerstrichen;
		return CollectionUtils.toFilteredArrayList(this._zeitrastermenge, { test : (z: StundenplanZeitraster) => (wochentag.id === z.wochentag) && this.zeitrasterGetSchneidenSich(beginn, ende, z.stundenbeginn, z.stundenende) });
	}

	/**
	 * Liefert das {@link StundenplanZeitraster}-Objekt der nächsten Stunde am selben Wochentag, oder NULL.
	 *
	 * @param zeitraster Das aktuelle {@link StundenplanZeitraster}-Objekt.
	 *
	 * @return das {@link StundenplanZeitraster}-Objekt der nächsten Stunde am selben Wochentag, oder NULL.
	 */
	public getZeitrasterNext(zeitraster : StundenplanZeitraster) : StundenplanZeitraster | null {
		return this._zeitraster_by_wochentag_and_stunde.getOrNull(zeitraster.wochentag, zeitraster.unterrichtstunde + 1);
	}

	/**
	 * Liefert den kleinsten Minuten-Wert aller Zeitraster, oder 480 (8 Uhr).
	 * <br>Laufzeit: O(1)
	 *
	 * @return den kleinsten Minuten-Wert aller Zeitraster, oder 480 (8 Uhr).
	 */
	public zeitrasterGetMinutenMin() : number {
		return (this._zeitrasterMinutenMin === null) ? 480 : this._zeitrasterMinutenMin;
	}

	/**
	 * Liefert das Minimum aller {@link StundenplanZeitraster#stundenbeginn}-Objekte einer bestimmten Unterrichtsstunde, oder 480 (8 Uhr) falls keines vorhanden ist.
	 * <br>Laufzeit: O(1)
	 *
	 * @param stunde  Die Unterrichtsstunde, deren Minimum gesucht wird.
	 *
	 * @return das Minimum aller {@link StundenplanZeitraster#stundenbeginn}-Objekte einer bestimmten Unterrichtsstunde, oder 480 (8 Uhr) falls keines vorhanden ist.
	 */
	public zeitrasterGetMinutenMinDerStunde(stunde : number) : number {
		const min : number | null = this._zeitrasterMinutenMinByStunde.get(stunde);
		return (min === null) ? 480 : min;
	}

	/**
	 * Liefert den größten Minuten-Wert aller Zeitraster, oder 480 (8 Uhr).
	 * <br>Laufzeit: O(1)
	 *
	 * @return den größten Minuten-Wert aller Zeitraster, oder 480 (8 Uhr).
	 */
	public zeitrasterGetMinutenMax() : number {
		return (this._zeitrasterMinutenMax === null) ? 480 : this._zeitrasterMinutenMax;
	}

	/**
	 * Liefert das Maximum aller {@link StundenplanZeitraster#stundenbeginn}-Objekte einer bestimmten Unterrichtsstunde, oder 480 (8 Uhr) falls keines vorhanden ist.
	 * <br>Laufzeit: O(1)
	 *
	 * @param stunde  Die Unterrichtsstunde, deren Maximum gesucht wird.
	 *
	 * @return das Maximum aller {@link StundenplanZeitraster#stundenbeginn}-Objekte einer bestimmten Unterrichtsstunde, oder 480 (8 Uhr) falls keines vorhanden ist.
	 */
	public zeitrasterGetMinutenMaxDerStunde(stunde : number) : number {
		const max : number | null = this._zeitrasterMinutenMaxByStunde.get(stunde);
		return (max === null) ? 480 : max;
	}

	/**
	 * Liefert die kleinste Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return die kleinste Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 */
	public zeitrasterGetStundeMin() : number {
		return this._zeitrasterStundeMin;
	}

	/**
	 * Liefert die kleinste nicht leere Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return die kleinste nicht leere Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 */
	public zeitrasterGetStundeMinOhneLeere() : number {
		return this._zeitrasterStundeMinOhneLeere;
	}

	/**
	 * Liefert die größte Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return die größte Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 */
	public zeitrasterGetStundeMax() : number {
		return this._zeitrasterStundeMax;
	}

	/**
	 * Liefert die größte nicht leere Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return die größte nicht leere Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 */
	public zeitrasterGetStundeMaxOhneLeere() : number {
		return this._zeitrasterStundeMaxOhneLeere;
	}

	/**
	 * Liefert die ID des kleinsten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return die ID des kleinsten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 */
	public zeitrasterGetWochentagMin() : number {
		return this._zeitrasterWochentagMin;
	}

	/**
	 * Liefert den kleinsten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return den kleinsten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 */
	public zeitrasterGetWochentagMinEnum() : Wochentag {
		return Wochentag.fromIDorException(this._zeitrasterWochentagMin);
	}

	/**
	 * Liefert die ID des größten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return die ID des größten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 */
	public zeitrasterGetWochentagMax() : number {
		return this._zeitrasterWochentagMax;
	}

	/**
	 * Liefert den größten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return den größten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 */
	public zeitrasterGetWochentagMaxEnum() : Wochentag {
		return Wochentag.fromIDorException(this._zeitrasterWochentagMax);
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanZeitraster}-Objekt.
	 *
	 * @param idZeitraster  Die Datenbank-ID des Zeitrasters.
	 *
	 * @return das zur ID zugehörige {@link StundenplanZeitraster}-Objekt.
	 */
	public zeitrasterGetByIdOrException(idZeitraster : number) : StundenplanZeitraster {
		return DeveloperNotificationException.ifMapGetIsNull(this._zeitraster_by_id, idZeitraster);
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanZeitraster}-Objekt.
	 *
	 * @param idZeitraster  Die Datenbank-ID des Zeitrasters.
	 *
	 * @return das zur ID zugehörige {@link StundenplanZeitraster}-Objekt.
	 */
	public zeitrasterGetByIdOrNull(idZeitraster : number) : StundenplanZeitraster | null {
		return this._zeitraster_by_id.get(idZeitraster);
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
	public zeitrasterGetByIdStringOfUhrzeitBeginn(idZeitraster : number) : string {
		const zeitraster : StundenplanZeitraster = DeveloperNotificationException.ifMapGetIsNull(this._zeitraster_by_id, idZeitraster);
		return (zeitraster.stundenbeginn === null) ? "" : DateUtils.getStringOfUhrzeitFromMinuten(zeitraster.stundenbeginn);
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
	public zeitrasterGetByIdStringOfUhrzeitEnde(idZeitraster : number) : string {
		const zeitraster : StundenplanZeitraster = DeveloperNotificationException.ifMapGetIsNull(this._zeitraster_by_id, idZeitraster);
		return (zeitraster.stundenende === null) ? "" : DateUtils.getStringOfUhrzeitFromMinuten(zeitraster.stundenende);
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
	public zeitrasterGetByWochentagAndStundeOrException(wochentag : number, stunde : number) : StundenplanZeitraster {
		return this._zeitraster_by_wochentag_and_stunde.getOrException(wochentag, stunde);
	}

	/**
	 * Liefert das zu (wochentag, stunde) zugehörige {@link StundenplanZeitraster}-Objekt, falls es existiert, sonst NULL.
	 *
	 * @param wochentag  Die ENUM-ID des {@link Wochentag} des gesuchten Zeitrasters.
	 * @param stunde     Die Unterrichtsstunde des gesuchten Zeitrasters.
	 *
	 * @return das zu (wochentag, stunde) zugehörige {@link StundenplanZeitraster}-Objekt, falls es existiert, sonst NULL.
	 */
	public zeitrasterGetByWochentagAndStundeOrNull(wochentag : number, stunde : number) : StundenplanZeitraster | null {
		return this._zeitraster_by_wochentag_and_stunde.getOrNull(wochentag, stunde);
	}

	/**
	 * Liefert TRUE, falls die Intervalle [beginn1, ende1[ und [beginn2, ende2[ sich schneiden.
	 *
	 * @param iBeginn1  Der Anfang (inklusive) des ersten Intervalls (in Minuten) seit 0 Uhr.
	 * @param iEnde1    Das Ende (exklusive) des ersten Intervalls (in Minuten) seit 0 Uhr.
	 * @param iBeginn2  Der Anfang (inklusive) des zweiten Intervalls (in Minuten) seit 0 Uhr.
	 * @param iEnde2    Das Ende (exklusive) des zweiten Intervalls (in Minuten) seit 0 Uhr.
	 *
	 * @return TRUE, falls die Intervalle [beginn1, ende1[ und [beginn2, ende2[ sich schneiden.
	 */
	public zeitrasterGetSchneidenSich(iBeginn1 : number | null, iEnde1 : number | null, iBeginn2 : number | null, iEnde2 : number | null) : boolean {
		const beginn1 : number = DeveloperNotificationException.ifNull("zeitraster.stundenbeginn1 ist NULL!", iBeginn1).valueOf();
		const ende1 : number = DeveloperNotificationException.ifNull("zeitraster.stundenende1 ist NULL!", iEnde1).valueOf();
		const beginn2 : number = DeveloperNotificationException.ifNull("zeitraster.stundenbeginn2 ist NULL!", iBeginn2).valueOf();
		const ende2 : number = DeveloperNotificationException.ifNull("zeitraster.stundenende2 ist NULL!", iEnde2).valueOf();
		DeveloperNotificationException.ifTrue("beginn1 < 0", beginn1 < 0);
		DeveloperNotificationException.ifTrue("beginn2 < 0", beginn2 < 0);
		DeveloperNotificationException.ifTrue("beginn1 > ende1", beginn1 > ende1);
		DeveloperNotificationException.ifTrue("beginn2 > ende2", beginn2 > ende2);
		return !((ende1 <= beginn2) || (ende2 <= beginn1));
	}

	/**
	 * Liefert TRUE, falls mindestens ein {@link StundenplanZeitraster}-Objekt der Liste sich mit den existierenden Objekten schneidet,
	 * dabei werden optional bestimmte Objekte ignoriert.
	 *
	 * @param checkList   Die Liste aller {@link StundenplanZeitraster}-Objekte, die mit den existierenden Objekten verglichen werden.
	 * @param ignoreList  Die Liste aller {@link StundenplanZeitraster}-Objekte, die bei der Prüfung ignoriert werden sollen.
	 *
	 * @return TRUE, falls mindestens ein {@link StundenplanZeitraster}-Objekt der Liste sich mit den existierenden Objekten schneidet,
	 * dabei werden optional bestimmte Objekte ignoriert.
	 */
	public zeitrasterGetSchneidenSichListeMitIgnore(checkList : List<StundenplanZeitraster>, ignoreList : List<StundenplanZeitraster>) : boolean {
		const mapIgnore : HashMap<number, StundenplanZeitraster> = new HashMap<number, StundenplanZeitraster>();
		for (const z of ignoreList)
			mapIgnore.put(z.id, z);
		for (const z1 of checkList)
			for (const z2 of MapUtils.getOrCreateArrayList(this._zeitrastermenge_by_wochentag, z1.wochentag))
				if (!mapIgnore.containsKey(z2.id) && this.zeitrasterGetSchneidenSich(z1.stundenbeginn, z1.stundenende, z2.stundenbeginn, z2.stundenende))
					return true;
		return false;
	}

	/**
	 * Liefert TRUE, falls das {@link StundenplanZeitraster}-Objekt einen problematischen Zustand hat.
	 * <br> Problem (1): Das Zeitraster schneidet zeitlich an dem Tag ein anderes Zeitraster.
	 * <br> Problem (2): Die Unterrichtsstunde des Vorgängers startet zeitlich später.
	 * <br> Problem (3): Die Unterrichtsstunde des Nachfolgers startet zeitlich früher.
	 *
	 * @param z  Das {@link StundenplanZeitraster}-Objekt, welches überprüft werden soll.
	 *
	 * @return TRUE, falls das {@link StundenplanZeitraster}-Objekt einen problematischen Zustand hat.
	 */
	public zeitrasterGetIstZustandProblematisch(z : StundenplanZeitraster) : boolean {
		let zPrev : StundenplanZeitraster | null = null;
		let zNext : StundenplanZeitraster | null = null;
		for (const zCurr of MapUtils.getOrCreateArrayList(this._zeitrastermenge_by_wochentag, z.wochentag)) {
			if (z.id === zCurr.id)
				continue;
			if (this.zeitrasterGetSchneidenSich(z.stundenbeginn, z.stundenende, zCurr.stundenbeginn, zCurr.stundenende))
				return true;
			const min : number = (zPrev === null) ? -1000 : zPrev.unterrichtstunde;
			if ((zCurr.unterrichtstunde < z.unterrichtstunde) && (zCurr.unterrichtstunde > min))
				zPrev = zCurr;
			const max : number = (zNext === null) ? +1000 : zNext.unterrichtstunde;
			if ((zCurr.unterrichtstunde > z.unterrichtstunde) && (zCurr.unterrichtstunde < max))
				zNext = zCurr;
		}
		if ((z.stundenbeginn !== null)) {
			if ((zPrev !== null) && (zPrev.stundenbeginn !== null) && (zPrev.stundenbeginn > z.stundenbeginn))
				return true;
			if ((zNext !== null) && (zNext.stundenbeginn !== null) && (zNext.stundenbeginn < z.stundenbeginn))
				return true;
		}
		return false;
	}

	/**
	 * Liefert alle verwendeten sortierten Unterrichtsstunden der {@link StundenplanZeitraster}.
	 * Das Array beinhaltet alle Zahlen von {@link #zeitrasterGetStundeMin()} bis {@link #zeitrasterGetStundeMax()}.
	 * <br>Laufzeit: O(1), da Referenz auf ein Array.
	 *
	 * @return alle verwendeten sortierten Unterrichtsstunden der {@link StundenplanZeitraster}.
	 */
	public zeitrasterGetStundenRange() : Array<number> {
		return this._zeitrasterStundenRange;
	}

	/**
	 * Liefert alle verwendeten sortierten Unterrichtsstunden der nicht leeren {@link StundenplanZeitraster}.
	 * Das Array beinhaltet alle Zahlen von {@link #zeitrasterGetStundeMinOhneLeere()} bis {@link #zeitrasterGetStundeMaxOhneLeere()}.
	 * <br>Laufzeit: O(1), da Referenz auf ein Array.
	 *
	 * @return alle verwendeten sortierten Unterrichtsstunden der nicht leeren {@link StundenplanZeitraster}.
	 */
	public zeitrasterGetStundenRangeOhneLeere() : Array<number> {
		return this._zeitrasterStundenRangeOhneLeere;
	}

	/**
	 * Liefert alle verwendeten sortierten {@link Wochentag}-Objekte der {@link StundenplanZeitraster}.
	 * Das Array beinhaltet alle {@link Wochentag}-Objekte von {@link #zeitrasterGetWochentagMin} bis {@link #zeitrasterGetWochentagMax()}.
	 * <br>Laufzeit: O(1), da Referenz auf ein Array.
	 *
	 * @return alle verwendeten sortierten {@link Wochentag}-Objekte der {@link StundenplanZeitraster}.
	 */
	public zeitrasterGetWochentageAlsEnumRange() : Array<Wochentag> {
		return this._zeitrasterWochentageAlsEnumRange;
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
	public zeitrasterHatUnterrichtByWochentagAndStundeAndWochentyp(wochentag : Wochentag, stunde : number, wochentyp : number) : boolean {
		const z : StundenplanZeitraster | null = this._zeitraster_by_wochentag_and_stunde.getOrNull(wochentag.id, stunde);
		if (z !== null)
			return !Map2DUtils.getOrCreateArrayList(this._unterrichtmenge_by_idZeitraster_and_wochentyp, z.id, wochentyp).isEmpty();
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
	public zeitrasterHatUnterrichtByKlasseIdWochentagAndStundeAndWochentyp(idKlasse : number, wochentag : number, stunde : number, wochentyp : number) : boolean {
		const z : StundenplanZeitraster | null = this._zeitraster_by_wochentag_and_stunde.getOrNull(wochentag, stunde);
		if (z !== null)
			for (const u of Map2DUtils.getOrCreateArrayList(this._unterrichtmenge_by_idKlasse_and_idZeitraster, idKlasse, z.id))
				if (u.wochentyp === wochentyp)
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
	public zeitrasterHatUnterrichtByLehrerIdWochentagAndStundeAndWochentyp(idLehrer : number, wochentag : number, stunde : number, wochentyp : number) : boolean {
		const z : StundenplanZeitraster | null = this._zeitraster_by_wochentag_and_stunde.getOrNull(wochentag, stunde);
		if (z !== null)
			for (const u of Map2DUtils.getOrCreateArrayList(this._unterrichtmenge_by_idLehrer_and_idZeitraster, idLehrer, z.id))
				if (u.wochentyp === wochentyp)
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
	public zeitrasterHatUnterrichtBySchuelerIdWochentagAndStundeAndWochentyp(idSchueler : number, wochentag : number, stunde : number, wochentyp : number) : boolean {
		const z : StundenplanZeitraster | null = this._zeitraster_by_wochentag_and_stunde.getOrNull(wochentag, stunde);
		if (z !== null)
			for (const u of Map2DUtils.getOrCreateArrayList(this._unterrichtmenge_by_idSchueler_and_idZeitraster, idSchueler, z.id))
				if (u.wochentyp === wochentyp)
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
	public zeitrasterHatUnterrichtByJahrgangIdWochentagAndStundeAndWochentyp(idJahrgang : number, wochentag : number, stunde : number, wochentyp : number) : boolean {
		const z : StundenplanZeitraster | null = this._zeitraster_by_wochentag_and_stunde.getOrNull(wochentag, stunde);
		if (z !== null)
			for (const u of Map2DUtils.getOrCreateArrayList(this._unterrichtmenge_by_idJahrgang_and_idZeitraster, idJahrgang, z.id))
				if (u.wochentyp === wochentyp)
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
	public zeitrasterHatUnterrichtMitWochentyp0(idZeitraster : number) : boolean {
		return !Map2DUtils.getOrCreateArrayList(this._unterrichtmenge_by_idZeitraster_and_wochentyp, idZeitraster, 0).isEmpty();
	}

	/**
	 * Liefert TRUE, falls das Zeitraster existiert und es mindestens einen Unterricht im Zeitraster mit einem einen Wochentyp 0 gibt.
	 *
	 * @param wochentag  Der {@link Wochentag}-ENUM.
	 * @param stunde     Die Unterrichtsstunde.
	 *
	 * @return TRUE, falls das Zeitraster existiert und es mindestens einen Unterricht im Zeitraster mit einem einen Wochentyp 0 gibt.
	 */
	public zeitrasterHatUnterrichtMitWochentyp0ByWochentagAndStunde(wochentag : Wochentag, stunde : number) : boolean {
		const z : StundenplanZeitraster | null = this._zeitraster_by_wochentag_and_stunde.getOrNull(wochentag.id, stunde);
		return (z !== null) && this.zeitrasterHatUnterrichtMitWochentyp0(z.id);
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
	public zeitrasterHatUnterrichtMitWochentyp0ByKlasseIdWochentagAndStunde(idKlasse : number, wochentag : number, stunde : number) : boolean {
		return this.zeitrasterHatUnterrichtByKlasseIdWochentagAndStundeAndWochentyp(idKlasse, wochentag, stunde, 0);
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
	public zeitrasterHatUnterrichtMitWochentyp0ByLehrerIdWochentagAndStunde(idLehrer : number, wochentag : number, stunde : number) : boolean {
		return this.zeitrasterHatUnterrichtByLehrerIdWochentagAndStundeAndWochentyp(idLehrer, wochentag, stunde, 0);
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
	public zeitrasterHatUnterrichtMitWochentyp0BySchuelerIdWochentagAndStunde(idSchueler : number, wochentag : number, stunde : number) : boolean {
		return this.zeitrasterHatUnterrichtBySchuelerIdWochentagAndStundeAndWochentyp(idSchueler, wochentag, stunde, 0);
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
	public zeitrasterHatUnterrichtMitWochentyp0ByJahrgangIdWochentagAndStunde(idJahrgang : number, wochentag : number, stunde : number) : boolean {
		return this.zeitrasterHatUnterrichtByJahrgangIdWochentagAndStundeAndWochentyp(idJahrgang, wochentag, stunde, 0);
	}

	/**
	 * Liefert TRUE, falls es mindestens einen Unterricht im Zeitraster mit einem einen Wochentyp 1 bis N gibt.
	 *
	 * @param idZeitraster  Die Datenbank-ID des Zeitrasters.
	 *
	 * @return TRUE, falls es mindestens einen Unterricht im Zeitraster mit einem einen Wochentyp 1 bis N gibt.
	 */
	public zeitrasterHatUnterrichtMitWochentyp1BisN(idZeitraster : number) : boolean {
		for (let wochentyp : number = 1; wochentyp <= this._stundenplanWochenTypModell; wochentyp++)
			if (!Map2DUtils.getOrCreateArrayList(this._unterrichtmenge_by_idZeitraster_and_wochentyp, idZeitraster, wochentyp).isEmpty())
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
	public zeitrasterHatUnterrichtMitWochentyp1BisNByWochentagAndStunde(wochentag : Wochentag, stunde : number) : boolean {
		const z : StundenplanZeitraster | null = this._zeitraster_by_wochentag_and_stunde.getOrNull(wochentag.id, stunde);
		return (z !== null) && this.zeitrasterHatUnterrichtMitWochentyp1BisN(z.id);
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
	public zeitrasterHatUnterrichtMitWochentyp1BisNByKlasseIdWochentagAndStunde(idKlasse : number, wochentag : number, stunde : number) : boolean {
		const z : StundenplanZeitraster | null = this._zeitraster_by_wochentag_and_stunde.getOrNull(wochentag, stunde);
		if (z !== null)
			for (const u of Map2DUtils.getOrCreateArrayList(this._unterrichtmenge_by_idKlasse_and_idZeitraster, idKlasse, z.id))
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
	public zeitrasterHatUnterrichtMitWochentyp1BisNByLehrerIdWochentagAndStunde(idLehrer : number, wochentag : number, stunde : number) : boolean {
		const z : StundenplanZeitraster | null = this._zeitraster_by_wochentag_and_stunde.getOrNull(wochentag, stunde);
		if (z !== null)
			for (const u of Map2DUtils.getOrCreateArrayList(this._unterrichtmenge_by_idLehrer_and_idZeitraster, idLehrer, z.id))
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
	public zeitrasterHatUnterrichtMitWochentyp1BisNBySchuelerIdWochentagAndStunde(idSchueler : number, wochentag : number, stunde : number) : boolean {
		const z : StundenplanZeitraster | null = this._zeitraster_by_wochentag_and_stunde.getOrNull(wochentag, stunde);
		if (z !== null)
			for (const u of Map2DUtils.getOrCreateArrayList(this._unterrichtmenge_by_idSchueler_and_idZeitraster, idSchueler, z.id))
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
	public zeitrasterHatUnterrichtMitWochentyp1BisNByJahrgangIdWochentagAndStunde(idJahrgang : number, wochentag : number, stunde : number) : boolean {
		const z : StundenplanZeitraster | null = this._zeitraster_by_wochentag_and_stunde.getOrNull(wochentag, stunde);
		if (z !== null)
			for (const u of Map2DUtils.getOrCreateArrayList(this._unterrichtmenge_by_idJahrgang_and_idZeitraster, idJahrgang, z.id))
				if (u.wochentyp >= 1)
					return true;
		return false;
	}

	/**
	 * Liefert TRUE, falls das Zeitraster existiert und es mindestens einen Unterricht des Faches im Zeitraster mit einem Wochentyp 1 bis N gibt.
	 *
	 * @param idFach     Die ID des {@link StundenplanFach}-Objektes.
	 * @param wochentag  Der {@link Wochentag}-ENUM.
	 * @param stunde     Die Unterrichtsstunde.
	 *
	 * @return TRUE, falls das Zeitraster existiert und es mindestens einen Unterricht des Faches im Zeitraster mit einem Wochentyp 1 bis N gibt.
	 */
	public zeitrasterHatUnterrichtMitWochentyp1BisNByFachIdWochentagAndStunde(idFach : number, wochentag : Wochentag, stunde : number) : boolean {
		const z : StundenplanZeitraster | null = this._zeitraster_by_wochentag_and_stunde.getOrNull(wochentag.id, stunde);
		if (z !== null)
			for (let wochentyp : number = 1; wochentyp <= this._stundenplanWochenTypModell; wochentyp++)
				if (!Map3DUtils.getOrCreateArrayList(this._unterrichtmenge_by_idZeitraster_wochentyp_fach, z.id, wochentyp, idFach).isEmpty())
					return true;
		return false;
	}

	/**
	 * Liefert TRUE, falls das Zeitraster existiert und es mindestens einen Unterricht des Raumes im Zeitraster mit einem Wochentyp 1 bis N gibt.
	 *
	 * @param idRaum     Die ID des {@link StundenplanRaum}-Objektes.
	 * @param wochentag  Der {@link Wochentag}-ENUM.
	 * @param stunde     Die Unterrichtsstunde.
	 *
	 * @return TRUE, falls das Zeitraster existiert und es mindestens einen Unterricht des Raumes im Zeitraster mit einem Wochentyp 1 bis N gibt.
	 */
	public zeitrasterHatUnterrichtMitWochentyp1BisNByRaumIdWochentagAndStunde(idRaum : number, wochentag : Wochentag, stunde : number) : boolean {
		const z : StundenplanZeitraster | null = this._zeitraster_by_wochentag_and_stunde.getOrNull(wochentag.id, stunde);
		if (z !== null)
			for (let wochentyp : number = 1; wochentyp <= this._stundenplanWochenTypModell; wochentyp++)
				if (!Map3DUtils.getOrCreateArrayList(this._unterrichtmenge_by_idZeitraster_wochentyp_raum, z.id, wochentyp, idRaum).isEmpty())
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
	public zeitrasterExistsByWochentag(wochentag : number) : boolean {
		return !MapUtils.getOrCreateArrayList(this._zeitrastermenge_by_wochentag, wochentag).isEmpty();
	}

	/**
	 * Aktualisiert das vorhandene {@link StundenplanZeitraster}-Objekt durch das neue Objekt.
	 * <br>Hinweis: Die ID eines Objekts lässt sich nicht patchen.
	 *
	 * @param zeitraster  Das neue {@link StundenplanZeitraster}-Objekt, welches das alte ersetzt.
	 */
	public zeitrasterPatchAttributes(zeitraster : StundenplanZeitraster) : void {
		this.zeitrasterPatchAttributesAll(ListUtils.create1(zeitraster));
	}

	/**
	 * Aktualisiert die vorhandenen {@link StundenplanZeitraster}-Objekte durch die neuen Objekte.
	 * <br>Hinweis: Die ID der Objekte lassen sich nicht patchen.
	 *
	 * @param zeitrasterList  Die neuen {@link StundenplanZeitraster}-Objekte, welche die alten ersetzen.
	 */
	public zeitrasterPatchAttributesAll(zeitrasterList : List<StundenplanZeitraster>) : void {
		const mapWochentagStunde : HashMap2D<number, number, StundenplanZeitraster> = new HashMap2D<number, number, StundenplanZeitraster>();
		for (const z of zeitrasterList) {
			StundenplanManager.zeitrasterCheck(z);
			DeveloperNotificationException.ifMapNotContains("_zeitraster_by_id", this._zeitraster_by_id, z.id);
			DeveloperNotificationException.ifMap2DPutOverwrites(mapWochentagStunde, z.wochentag, z.unterrichtstunde, z);
		}
		for (const z of zeitrasterList) {
			DeveloperNotificationException.ifMapRemoveFailes(this._zeitraster_by_id, z.id);
			DeveloperNotificationException.ifMapPutOverwrites(this._zeitraster_by_id, z.id, z);
		}
		this.update_all();
	}

	private zeitrasterRemoveOhneUpdate(idZeitraster : number) : void {
		for (const u of MapUtils.getOrCreateArrayList(this._unterrichtmenge_by_idZeitraster, idZeitraster))
			this.unterrichtRemoveByIdOhneUpdate(u.id);
		DeveloperNotificationException.ifMapRemoveFailes(this._zeitraster_by_id, idZeitraster);
	}

	/**
	 * Entfernt aus dem Stundenplan ein existierendes {@link StundenplanZeitraster}-Objekt.
	 * <br> Hinweis: Kaskadierend werden auch alle {@link StundenplanUnterricht}-Objekte gelöscht.
	 *
	 * @param idZeitraster  Die Datenbank-ID des {@link StundenplanZeitraster}-Objekts.
	 */
	public zeitrasterRemoveById(idZeitraster : number) : void {
		this.zeitrasterRemoveOhneUpdate(idZeitraster);
		this.update_all();
	}

	/**
	 * Entfernt alle {@link StundenplanZeitraster}-Objekte aus dem Stundenplan.
	 * <br> Hinweis: Kaskadierend werden auch alle {@link StundenplanUnterricht}-Objekte gelöscht.
	 *
	 * @param listZeitraster  Die {@link StundenplanZeitraster}-Objekte, die entfernt werden sollen.
	 */
	public zeitrasterRemoveAll(listZeitraster : List<StundenplanZeitraster>) : void {
		for (const zeitraster of listZeitraster)
			this.zeitrasterRemoveOhneUpdate(zeitraster.id);
		this.update_all();
	}

	/**
	 * Entfernt alle {@link StundenplanZeitraster}-Objekte, die einen bestimmten Wochentag haben.
	 *
	 * @param wochentagEnumID  Die ID des {@link Wochentag}.
	 */
	public zeitrasterRemoveAllByWochentag(wochentagEnumID : number) : void {
		this.zeitrasterRemoveAll(MapUtils.getOrCreateArrayList(this._zeitrastermenge_by_wochentag, wochentagEnumID));
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.stundenplan.StundenplanManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.stundenplan.StundenplanManager'].includes(name);
	}

	public static class = new Class<StundenplanManager>('de.svws_nrw.core.utils.stundenplan.StundenplanManager');

}

export function cast_de_svws_nrw_core_utils_stundenplan_StundenplanManager(obj : unknown) : StundenplanManager {
	return obj as StundenplanManager;
}
