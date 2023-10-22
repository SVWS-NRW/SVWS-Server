import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap2D } from '../../../core/adt/map/HashMap2D';
import { SchuelerListeEintrag } from '../../../core/data/schueler/SchuelerListeEintrag';
import { HashMap } from '../../../java/util/HashMap';
import { KlassenUtils } from '../../../core/utils/klassen/KlassenUtils';
import { GostAbiturjahrUtils } from '../../../core/utils/gost/GostAbiturjahrUtils';
import { ArrayList } from '../../../java/util/ArrayList';
import { KlassenListeEintrag } from '../../../core/data/klassen/KlassenListeEintrag';
import { SchuelerUtils } from '../../../core/utils/schueler/SchuelerUtils';
import { SchuljahresabschnittsUtils } from '../../../core/utils/schule/SchuljahresabschnittsUtils';
import { JavaString } from '../../../java/lang/JavaString';
import { GostJahrgang } from '../../../core/data/gost/GostJahrgang';
import { KursListeEintrag } from '../../../core/data/kurse/KursListeEintrag';
import { JahrgangsUtils } from '../../../core/utils/jahrgang/JahrgangsUtils';
import { JahrgangsListeEintrag } from '../../../core/data/jahrgang/JahrgangsListeEintrag';
import type { List } from '../../../java/util/List';
import { KursUtils } from '../../../core/utils/kurse/KursUtils';
import type { JavaMap } from '../../../java/util/JavaMap';
import { Schuljahresabschnitt } from '../../../core/data/schule/Schuljahresabschnitt';

export class SchuelerListeManager extends JavaObject {

	/**
	 * Die Liste, welche als Grundlage in den Manager geladen wurde
	 */
	private readonly _schueler : List<SchuelerListeEintrag> = new ArrayList();

	private readonly _mapSchuelerByID : JavaMap<number, SchuelerListeEintrag> = new HashMap();

	private readonly _mapSchuelerMitStatus : HashMap2D<number, number, SchuelerListeEintrag> = new HashMap2D();

	private readonly _mapSchuelerInJahrgang : HashMap2D<number, number, SchuelerListeEintrag> = new HashMap2D();

	private readonly _mapSchuelerInKlasse : HashMap2D<number, number, SchuelerListeEintrag> = new HashMap2D();

	private readonly _mapSchuelerInKurs : HashMap2D<number, number, SchuelerListeEintrag> = new HashMap2D();

	private readonly _mapSchuelerInSchuljahresabschnitt : HashMap2D<number, number, SchuelerListeEintrag> = new HashMap2D();

	private readonly _mapSchuelerInAbiturjahrgang : HashMap2D<number, number, SchuelerListeEintrag> = new HashMap2D();

	private readonly _mapSchuelerInSchulgliederung : HashMap2D<string, number, SchuelerListeEintrag> = new HashMap2D();

	private readonly _jahrgaenge : List<JahrgangsListeEintrag> = new ArrayList();

	private readonly _mapJahrgangByID : JavaMap<number, JahrgangsListeEintrag> = new HashMap();

	private readonly _klassen : List<KlassenListeEintrag> = new ArrayList();

	private readonly _mapKlasseByID : JavaMap<number, KlassenListeEintrag> = new HashMap();

	private readonly _kurse : List<KursListeEintrag> = new ArrayList();

	private readonly _mapKursByID : JavaMap<number, KursListeEintrag> = new HashMap();

	private readonly _schuljahresabschnitte : List<Schuljahresabschnitt> = new ArrayList();

	private readonly _mapSchuljahresabschnittByID : JavaMap<number, Schuljahresabschnitt> = new HashMap();

	private readonly _abiturjahrgaenge : List<GostJahrgang> = new ArrayList();

	private readonly _mapAbiturjahrgangByID : JavaMap<number, GostJahrgang> = new HashMap();


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
		this.initSchueler(schueler);
		this.initJahrgaenge(jahrgaenge);
		this.initKlassen(klassen);
		this.initKurse(kurse);
		this.initSchuljahresabschnitte(schuljahresabschnitte);
		this.initAbiturjahrgaenge(abiturjahrgaenge);
	}

	private initSchueler(schueler : List<SchuelerListeEintrag>) : void {
		this._schueler.clear();
		this._schueler.addAll(schueler);
		this._schueler.sort(SchuelerUtils.comparator);
		this._mapSchuelerByID.clear();
		for (const s of schueler) {
			this._mapSchuelerByID.put(s.id, s);
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

	private initJahrgaenge(jahrgaenge : List<JahrgangsListeEintrag>) : void {
		this._jahrgaenge.clear();
		this._jahrgaenge.addAll(jahrgaenge);
		this._jahrgaenge.sort(JahrgangsUtils.comparator);
		this._mapJahrgangByID.clear();
		for (const j of jahrgaenge)
			this._mapJahrgangByID.put(j.id, j);
	}

	private initKlassen(klassen : List<KlassenListeEintrag>) : void {
		this._klassen.clear();
		this._klassen.addAll(klassen);
		this._klassen.sort(KlassenUtils.comparator);
		this._mapKlasseByID.clear();
		for (const k of klassen)
			this._mapKlasseByID.put(k.id, k);
	}

	private initKurse(kurse : List<KursListeEintrag>) : void {
		this._kurse.clear();
		this._kurse.addAll(kurse);
		this._kurse.sort(KursUtils.comparator);
		this._mapKursByID.clear();
		for (const k of kurse)
			this._mapKursByID.put(k.id, k);
	}

	private initSchuljahresabschnitte(schuljahresabschnitte : List<Schuljahresabschnitt>) : void {
		this._schuljahresabschnitte.clear();
		this._schuljahresabschnitte.addAll(schuljahresabschnitte);
		this._schuljahresabschnitte.sort(SchuljahresabschnittsUtils.comparator);
		this._mapSchuljahresabschnittByID.clear();
		for (const sja of schuljahresabschnitte)
			this._mapSchuljahresabschnittByID.put(sja.id, sja);
	}

	private initAbiturjahrgaenge(abiturjahrgaenge : List<GostJahrgang>) : void {
		this._abiturjahrgaenge.clear();
		this._abiturjahrgaenge.addAll(abiturjahrgaenge);
		this._abiturjahrgaenge.sort(GostAbiturjahrUtils.comparator);
		this._mapAbiturjahrgangByID.clear();
		for (const j of abiturjahrgaenge)
			this._mapAbiturjahrgangByID.put(j.abiturjahr, j);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.schueler.SchuelerListeManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_schueler_SchuelerListeManager(obj : unknown) : SchuelerListeManager {
	return obj as SchuelerListeManager;
}
