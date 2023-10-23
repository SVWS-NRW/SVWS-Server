import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap2D } from '../../../core/adt/map/HashMap2D';
import { SchuelerListeEintrag } from '../../../core/data/schueler/SchuelerListeEintrag';
import { KlassenUtils } from '../../../core/utils/klassen/KlassenUtils';
import { KlassenListeEintrag } from '../../../core/data/klassen/KlassenListeEintrag';
import { SchuelerUtils } from '../../../core/utils/schueler/SchuelerUtils';
import { ArrayList } from '../../../java/util/ArrayList';
import { SchuljahresabschnittsUtils } from '../../../core/utils/schule/SchuljahresabschnittsUtils';
import { JavaString } from '../../../java/lang/JavaString';
import { SchuelerStatus } from '../../../core/types/SchuelerStatus';
import type { Comparator } from '../../../java/util/Comparator';
import type { JavaFunction } from '../../../java/util/function/JavaFunction';
import { KursListeEintrag } from '../../../core/data/kurse/KursListeEintrag';
import { JahrgangsListeEintrag } from '../../../core/data/jahrgang/JahrgangsListeEintrag';
import { Schulgliederung } from '../../../core/types/schule/Schulgliederung';
import type { List } from '../../../java/util/List';
import { GostAbiturjahrUtils } from '../../../core/utils/gost/GostAbiturjahrUtils';
import { AttributeWithFilter } from '../../../core/utils/AttributeWithFilter';
import { GostJahrgang } from '../../../core/data/gost/GostJahrgang';
import { JahrgangsUtils } from '../../../core/utils/jahrgang/JahrgangsUtils';
import type { Runnable } from '../../../java/lang/Runnable';
import { KursUtils } from '../../../core/utils/kurse/KursUtils';
import { Arrays } from '../../../java/util/Arrays';
import { Schuljahresabschnitt } from '../../../core/data/schule/Schuljahresabschnitt';

export class SchuelerListeManager extends JavaObject {

	/**
	 * Ein Filter-Attribut für die Schülerliste. Dieses wird nicht für das Filtern der Schüler verwendet, sondern für eine Mehrfachauswahl
	 */
	public readonly schueler : AttributeWithFilter<number, SchuelerListeEintrag>;

	private static readonly _schuelerToId : JavaFunction<SchuelerListeEintrag, number> = { apply : (s: SchuelerListeEintrag) => s.id };

	private readonly _mapSchuelerMitStatus : HashMap2D<number, number, SchuelerListeEintrag> = new HashMap2D();

	private readonly _mapSchuelerInJahrgang : HashMap2D<number, number, SchuelerListeEintrag> = new HashMap2D();

	private readonly _mapSchuelerInKlasse : HashMap2D<number, number, SchuelerListeEintrag> = new HashMap2D();

	private readonly _mapSchuelerInKurs : HashMap2D<number, number, SchuelerListeEintrag> = new HashMap2D();

	private readonly _mapSchuelerInSchuljahresabschnitt : HashMap2D<number, number, SchuelerListeEintrag> = new HashMap2D();

	private readonly _mapSchuelerInAbiturjahrgang : HashMap2D<number, number, SchuelerListeEintrag> = new HashMap2D();

	private readonly _mapSchuelerInSchulgliederung : HashMap2D<string, number, SchuelerListeEintrag> = new HashMap2D();

	/**
	 * Das Filter-Attribut für die Jahrgänge
	 */
	public readonly jahrgaenge : AttributeWithFilter<number, JahrgangsListeEintrag>;

	private static readonly _jahrgangToId : JavaFunction<JahrgangsListeEintrag, number> = { apply : (jg: JahrgangsListeEintrag) => jg.id };

	/**
	 * Das Filter-Attribut für die Klassen
	 */
	public readonly klassen : AttributeWithFilter<number, KlassenListeEintrag>;

	private static readonly _klasseToId : JavaFunction<KlassenListeEintrag, number> = { apply : (k: KlassenListeEintrag) => k.id };

	/**
	 * Das Filter-Attribut für die Kurse
	 */
	public readonly kurse : AttributeWithFilter<number, KursListeEintrag>;

	private static readonly _kursToId : JavaFunction<KursListeEintrag, number> = { apply : (k: KursListeEintrag) => k.id };

	/**
	 * Das Filter-Attribut für die Schuljahresabschnitte - die Filterfunktion wird zur Zeit noch nicht genutzt
	 */
	public readonly schuljahresabschnitte : AttributeWithFilter<number, Schuljahresabschnitt>;

	private static readonly _schuljahresabschnittToId : JavaFunction<Schuljahresabschnitt, number> = { apply : (sja: Schuljahresabschnitt) => sja.id };

	/**
	 * Das Filter-Attribut für die Abiturjahrgänge - die Filterfunktion wird zur Zeit noch nicht genutzt
	 */
	public readonly abiturjahrgaenge : AttributeWithFilter<number, GostJahrgang>;

	private static readonly _abiturjahrgangToId : JavaFunction<GostJahrgang, number> = { apply : (a: GostJahrgang) => a.abiturjahr };

	/**
	 * Das Filter-Attribut für die Schulgliederungen
	 */
	public readonly schulgliederungen : AttributeWithFilter<string, Schulgliederung>;

	private static readonly _schulgliederungToId : JavaFunction<Schulgliederung, string> = { apply : (sg: Schulgliederung) => sg.daten.kuerzel };

	private static readonly _comparatorSchulgliederung : Comparator<Schulgliederung> = { compare : (a: Schulgliederung, b: Schulgliederung) => a.ordinal() - b.ordinal() };

	/**
	 * Das Filter-Attribut für den Schüler-Status
	 */
	public readonly schuelerstatus : AttributeWithFilter<number, SchuelerStatus>;

	private static readonly _schuelerstatusToId : JavaFunction<SchuelerStatus, number> = { apply : (s: SchuelerStatus) => s.id };

	private static readonly _comparatorSchuelerStatus : Comparator<SchuelerStatus> = { compare : (a: SchuelerStatus, b: SchuelerStatus) => a.ordinal() - b.ordinal() };

	/**
	 * Die gefilterte Schüler-Liste, sofern sie schon berechnet wurde
	 */
	private _filtered : List<SchuelerListeEintrag> | null = null;

	/**
	 * Ein Handler für das Ereignis, dass der Schüler-Filter angepasst wurde
	 */
	private readonly _eventHandlerFilterChanged : Runnable = { run : () => this._filtered = null };

	/**
	 * Ein Handler für das Ereignis, dass die Schülerauswahl angepasst wurde
	 */
	private static readonly _eventHandlerSchuelerAuswahlChanged : Runnable = { run : () => {
		// empty block
	} };


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
	public constructor(schueler : List<SchuelerListeEintrag>, jahrgaenge : List<JahrgangsListeEintrag>, klassen : List<KlassenListeEintrag>, kurse : List<KursListeEintrag>, schuljahresabschnitte : List<Schuljahresabschnitt>, abiturjahrgaenge : List<GostJahrgang>) {
		super();
		this.schueler = new AttributeWithFilter(schueler, SchuelerListeManager._schuelerToId, SchuelerUtils.comparator, SchuelerListeManager._eventHandlerSchuelerAuswahlChanged);
		this.initSchueler();
		this.jahrgaenge = new AttributeWithFilter(jahrgaenge, SchuelerListeManager._jahrgangToId, JahrgangsUtils.comparator, this._eventHandlerFilterChanged);
		this.klassen = new AttributeWithFilter(klassen, SchuelerListeManager._klasseToId, KlassenUtils.comparator, this._eventHandlerFilterChanged);
		this.kurse = new AttributeWithFilter(kurse, SchuelerListeManager._kursToId, KursUtils.comparator, this._eventHandlerFilterChanged);
		this.schuljahresabschnitte = new AttributeWithFilter(schuljahresabschnitte, SchuelerListeManager._schuljahresabschnittToId, SchuljahresabschnittsUtils.comparator, this._eventHandlerFilterChanged);
		this.abiturjahrgaenge = new AttributeWithFilter(abiturjahrgaenge, SchuelerListeManager._abiturjahrgangToId, GostAbiturjahrUtils.comparator, this._eventHandlerFilterChanged);
		this.schulgliederungen = new AttributeWithFilter(Arrays.asList(...Schulgliederung.values()), SchuelerListeManager._schulgliederungToId, SchuelerListeManager._comparatorSchulgliederung, this._eventHandlerFilterChanged);
		this.schuelerstatus = new AttributeWithFilter(Arrays.asList(...SchuelerStatus.values()), SchuelerListeManager._schuelerstatusToId, SchuelerListeManager._comparatorSchuelerStatus, this._eventHandlerFilterChanged);
	}

	private initSchueler() : void {
		for (const s of this.schueler.list()) {
			this._mapSchuelerMitStatus.put(s.status, s.id, s);
			if (s.idJahrgang >= 0)
				this._mapSchuelerInJahrgang.put(s.idJahrgang, s.id, s);
			if (s.idKlasse >= 0)
				this._mapSchuelerInKlasse.put(s.idKlasse, s.id, s);
			for (const idKurs of s.kurse)
				this._mapSchuelerInKurs.put(idKurs, s.id, s);
			if (s.idSchuljahresabschnitt >= 0)
				this._mapSchuelerInSchuljahresabschnitt.put(s.idSchuljahresabschnitt, s.id, s);
			if (s.abiturjahrgang !== null)
				this._mapSchuelerInAbiturjahrgang.put(s.abiturjahrgang, s.id, s);
			if (!JavaString.isBlank(s.schulgliederung))
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
	public filtered() : List<SchuelerListeEintrag> {
		if (this._filtered !== null)
			return this._filtered;
		const tmpList : List<SchuelerListeEintrag> = new ArrayList();
		for (const eintrag of this.schueler.list()) {
			if (this.jahrgaenge.filterAktiv() && (!this.jahrgaenge.filterHasKey(eintrag.idJahrgang)))
				continue;
			if (this.klassen.filterAktiv() && (!this.klassen.filterHasKey(eintrag.idKlasse)))
				continue;
			if (this.kurse.filterAktiv()) {
				let hatEinenKurs : boolean = false;
				for (const idKurs of eintrag.kurse)
					if (this.kurse.filterHasKey(idKurs))
						hatEinenKurs = true;
				if (!hatEinenKurs)
					continue;
			}
			if (this.schulgliederungen.filterAktiv() && (!this.schulgliederungen.filterHasKey(eintrag.schulgliederung)))
				continue;
			if (this.schuelerstatus.filterAktiv() && (!this.schuelerstatus.filterHasKey(eintrag.status)))
				continue;
			tmpList.add(eintrag);
		}
		this._filtered = tmpList;
		return this._filtered;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.schueler.SchuelerListeManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_schueler_SchuelerListeManager(obj : unknown) : SchuelerListeManager {
	return obj as SchuelerListeManager;
}
