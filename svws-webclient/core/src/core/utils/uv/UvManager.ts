import { JavaObject } from '../../../java/lang/JavaObject';
import { UvLehrer } from '../../../core/data/uv/UvLehrer';
import { HashMap } from '../../../java/util/HashMap';
import { LehrerUnterrichtsfach } from '../../../core/data/lehrer/LehrerUnterrichtsfach';
import { UvPlanungsabschnittSchueler } from '../../../core/data/uv/UvPlanungsabschnittSchueler';
import { ArrayList } from '../../../java/util/ArrayList';
import { JavaString } from '../../../java/lang/JavaString';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { UvSchuelergruppeSchueler } from '../../../core/data/uv/UvSchuelergruppeSchueler';
import { DateUtils } from '../../../core/utils/DateUtils';
import { GostKursart } from '../../../core/types/gost/GostKursart';
import { ListMap2DLongKeys } from '../../../core/adt/map/ListMap2DLongKeys';
import type { Comparator } from '../../../java/util/Comparator';
import { FachDaten } from '../../../core/data/fach/FachDaten';
import type { List } from '../../../java/util/List';
import { ListMap4DLongKeys } from '../../../core/adt/map/ListMap4DLongKeys';
import { UvZeitrasterEintrag } from '../../../core/data/uv/UvZeitrasterEintrag';
import { HashSet } from '../../../java/util/HashSet';
import { UvKurs } from '../../../core/data/uv/UvKurs';
import { UvLerngruppenSchiene } from '../../../core/data/uv/UvLerngruppenSchiene';
import { ListMap1DLongKeys } from '../../../core/adt/map/ListMap1DLongKeys';
import { UvRaumgruppe } from '../../../core/data/uv/UvRaumgruppe';
import { MapUtils } from '../../../core/utils/MapUtils';
import { UvLerngruppe, cast_de_svws_nrw_core_data_uv_UvLerngruppe } from '../../../core/data/uv/UvLerngruppe';
import { UvUnterrichtRaum } from '../../../core/data/uv/UvUnterrichtRaum';
import { JavaLong } from '../../../java/lang/JavaLong';
import type { Collection } from '../../../java/util/Collection';
import { cast_java_util_Collection } from '../../../java/util/Collection';
import { Class } from '../../../java/lang/Class';
import { Wochentag } from '../../../core/types/Wochentag';
import type { JavaMap } from '../../../java/util/JavaMap';
import { HashMap2D } from '../../../core/adt/map/HashMap2D';
import { UvSchuelergruppe } from '../../../core/data/uv/UvSchuelergruppe';
import type { JavaSet } from '../../../java/util/JavaSet';
import { UvLehrerPflichtstundensoll } from '../../../core/data/uv/UvLehrerPflichtstundensoll';
import { ListMap3DLongKeys } from '../../../core/adt/map/ListMap3DLongKeys';
import { JahrgangsDaten } from '../../../core/data/jahrgang/JahrgangsDaten';
import { UvGrunddatenBundle } from '../../../core/data/uv/UvGrunddatenBundle';
import { UvZeitraster } from '../../../core/data/uv/UvZeitraster';
import { UvRaum } from '../../../core/data/uv/UvRaum';
import { UvUnterricht } from '../../../core/data/uv/UvUnterricht';
import { UvPlanungsabschnitt } from '../../../core/data/uv/UvPlanungsabschnitt';
import { UvPlanungsabschnittsdatenBundle } from '../../../core/data/uv/UvPlanungsabschnittsdatenBundle';
import { UvLerngruppenLehrer } from '../../../core/data/uv/UvLerngruppenLehrer';
import { UvStundentafel } from '../../../core/data/uv/UvStundentafel';
import { UvPlanungsabschnittZeitraster } from '../../../core/data/uv/UvPlanungsabschnittZeitraster';
import { UvStundentafelFach } from '../../../core/data/uv/UvStundentafelFach';
import { UvSchiene } from '../../../core/data/uv/UvSchiene';
import { UvFach } from '../../../core/data/uv/UvFach';
import { JavaInteger } from '../../../java/lang/JavaInteger';
import { UvLehrerAnrechnungsstunden } from '../../../core/data/uv/UvLehrerAnrechnungsstunden';
import { UvUnterrichtLerngruppenlehrer } from '../../../core/data/uv/UvUnterrichtLerngruppenlehrer';
import { UvPlanungsabschnittLehrer } from '../../../core/data/uv/UvPlanungsabschnittLehrer';
import { UvKlasse } from '../../../core/data/uv/UvKlasse';
import { UvKlassenLehrer } from '../../../core/data/uv/UvKlassenLehrer';
import { ListUtils } from '../../../core/utils/ListUtils';

export class UvManager extends JavaObject {

	private jahrgangById: JavaMap<number, JahrgangsDaten> | null = null;

	private fachdatenById: JavaMap<number, FachDaten> | null = null;

	private readonly planungsabschnittById: JavaMap<number, UvPlanungsabschnitt> = new HashMap<number, UvPlanungsabschnitt>();

	private readonly planungsabschnittMenge: List<UvPlanungsabschnitt> = new ArrayList<UvPlanungsabschnitt>();

	private readonly lehrerById: JavaMap<number, UvLehrer> = new HashMap<number, UvLehrer>();

	private readonly lehrerMenge: List<UvLehrer> = new ArrayList<UvLehrer>();

	private readonly lehrerByIdKLehrer: JavaMap<number, UvLehrer> = new HashMap<number, UvLehrer>();

	private readonly lehrerUnterrichtsfachByIstKLehrerAndId: HashMap2D<boolean, number, LehrerUnterrichtsfach> = new HashMap2D<boolean, number, LehrerUnterrichtsfach>();

	private readonly lehrerUnterrichtsfachMenge: List<LehrerUnterrichtsfach> = new ArrayList<LehrerUnterrichtsfach>();

	private lehrerUnterrichtsfachByIdKLehrerAndIdFach: ListMap2DLongKeys<LehrerUnterrichtsfach> = new ListMap2DLongKeys<LehrerUnterrichtsfach>();

	private lehrerUnterrichtsfachByIdLehrerAndIdFach: ListMap2DLongKeys<LehrerUnterrichtsfach> = new ListMap2DLongKeys<LehrerUnterrichtsfach>();

	private lehrerMengeByIdFachAndSek: ListMap2DLongKeys<UvLehrer> = new ListMap2DLongKeys<UvLehrer>();

	private planungsabschnittLehrerByIdPlanungsabschnittAndIdLehrer: ListMap2DLongKeys<UvPlanungsabschnittLehrer> = new ListMap2DLongKeys<UvPlanungsabschnittLehrer>();

	private readonly planungsabschnittLehrerMenge: List<UvPlanungsabschnittLehrer> = new ArrayList<UvPlanungsabschnittLehrer>();

	private readonly lehrerAnrechnungsstundenById: JavaMap<number, UvLehrerAnrechnungsstunden> = new HashMap<number, UvLehrerAnrechnungsstunden>();

	private readonly lehrerAnrechnungsstundenMenge: List<UvLehrerAnrechnungsstunden> = new ArrayList<UvLehrerAnrechnungsstunden>();

	private readonly lehrerAnrechnungsstundenMengeByLehrerId: JavaMap<number, List<UvLehrerAnrechnungsstunden>> = new HashMap<number, List<UvLehrerAnrechnungsstunden>>();

	private readonly lehrerPflichtstundensollById: JavaMap<number, UvLehrerPflichtstundensoll> = new HashMap<number, UvLehrerPflichtstundensoll>();

	private readonly lehrerPflichtstundensollMenge: List<UvLehrerPflichtstundensoll> = new ArrayList<UvLehrerPflichtstundensoll>();

	private readonly lehrerPflichtstundensollMengeByLehrerId: JavaMap<number, List<UvLehrerPflichtstundensoll>> = new HashMap<number, List<UvLehrerPflichtstundensoll>>();

	private readonly raumMenge: List<UvRaum> = new ArrayList<UvRaum>();

	private readonly raumgruppeById: JavaMap<number, UvRaumgruppe> = new HashMap<number, UvRaumgruppe>();

	private readonly raumgruppeMenge: List<UvRaumgruppe> = new ArrayList<UvRaumgruppe>();

	private raumByIdRaumgruppeAndIdRaum: ListMap2DLongKeys<UvRaum> = new ListMap2DLongKeys<UvRaum>();

	private readonly raumByKuerzel: JavaMap<string, UvRaum> = new HashMap<string, UvRaum>();

	private readonly stundentafelById: JavaMap<number, UvStundentafel> = new HashMap<number, UvStundentafel>();

	private readonly stundentafelMenge: List<UvStundentafel> = new ArrayList<UvStundentafel>();

	private stundentafelFachByIdStundentafelAndAbschnittAndIdFachAndIdStundentafelFach: ListMap4DLongKeys<UvStundentafelFach> = new ListMap4DLongKeys<UvStundentafelFach>();

	private readonly stundentafelFachMenge: List<UvStundentafelFach> = new ArrayList<UvStundentafelFach>();

	private readonly fachById: JavaMap<number, UvFach> = new HashMap<number, UvFach>();

	private readonly fachMenge: List<UvFach> = new ArrayList<UvFach>();

	private fachMengeByIdFach: ListMap1DLongKeys<UvFach> = new ListMap1DLongKeys<UvFach>();

	private readonly zeitrasterById: JavaMap<number, UvZeitraster> = new HashMap<number, UvZeitraster>();

	private readonly zeitrasterMenge: List<UvZeitraster> = new ArrayList<UvZeitraster>();

	private zeitrasterEintragByIdZeitrasterAndIdZeitrasterEintrag: ListMap2DLongKeys<UvZeitrasterEintrag> = new ListMap2DLongKeys<UvZeitrasterEintrag>();

	private readonly zeitrasterEintragMenge: List<UvZeitrasterEintrag> = new ArrayList<UvZeitrasterEintrag>();

	private planungsabschnittZeitrasterByIdPlanungsabschnittAndIdZeitraster: ListMap2DLongKeys<UvPlanungsabschnittZeitraster> = new ListMap2DLongKeys<UvPlanungsabschnittZeitraster>();

	private readonly planungsabschnittZeitrasterMenge: List<UvPlanungsabschnittZeitraster> = new ArrayList<UvPlanungsabschnittZeitraster>();

	private planungsabschnittSchuelerByIdPlanungsabschnittAndIdJahrgangAndIdKlasseAndIdSchueler: ListMap4DLongKeys<UvPlanungsabschnittSchueler> = new ListMap4DLongKeys<UvPlanungsabschnittSchueler>();

	private readonly planungsabschnittSchuelerMenge: List<UvPlanungsabschnittSchueler> = new ArrayList<UvPlanungsabschnittSchueler>();

	private schuelergruppeByIdPlanungsabschnittAndIdSchuelergruppe: ListMap2DLongKeys<UvSchuelergruppe> = new ListMap2DLongKeys<UvSchuelergruppe>();

	private readonly schuelergruppeMenge: List<UvSchuelergruppe> = new ArrayList<UvSchuelergruppe>();

	private schuelergruppeSchuelerByIdPlanungsabschnittAndIdSchuelergruppeAndIdSchueler: ListMap3DLongKeys<UvSchuelergruppeSchueler> = new ListMap3DLongKeys<UvSchuelergruppeSchueler>();

	private readonly schuelergruppeSchuelerMenge: List<UvSchuelergruppeSchueler> = new ArrayList<UvSchuelergruppeSchueler>();

	private planungsabschnittSchuelerMengeByIdPlanungsabschnittAndIdSchuelergruppe: ListMap2DLongKeys<UvPlanungsabschnittSchueler> = new ListMap2DLongKeys<UvPlanungsabschnittSchueler>();

	private klasseByIdPlanungsabschnittAndIdSchuelergruppeAndIdKlasse: ListMap3DLongKeys<UvKlasse> = new ListMap3DLongKeys<UvKlasse>();

	private readonly klasseMenge: List<UvKlasse> = new ArrayList<UvKlasse>();

	private klassenLehrerByIdPlanungsabschnittAndIdKlasseAndIdLehrer: ListMap3DLongKeys<UvKlassenLehrer> = new ListMap3DLongKeys<UvKlassenLehrer>();

	private readonly klassenLehrerMenge: List<UvKlassenLehrer> = new ArrayList<UvKlassenLehrer>();

	private kursByIdPlanungsabschnittAndIdSchuelergruppeAndIdKurs: ListMap3DLongKeys<UvKurs> = new ListMap3DLongKeys<UvKurs>();

	private readonly kursMenge: List<UvKurs> = new ArrayList<UvKurs>();

	private schieneByIdPlanungsabschnittAndNummerAndIdSchiene: ListMap3DLongKeys<UvSchiene> = new ListMap3DLongKeys<UvSchiene>();

	private readonly schieneMenge: List<UvSchiene> = new ArrayList<UvSchiene>();

	private lerngruppeByIdPlanungsabschnittAndIdLerngruppe: ListMap2DLongKeys<UvLerngruppe> = new ListMap2DLongKeys<UvLerngruppe>();

	private readonly lerngruppeMenge: List<UvLerngruppe> = new ArrayList<UvLerngruppe>();

	private lerngruppeMengeByIdSchuelergruppe: ListMap1DLongKeys<UvLerngruppe> = new ListMap1DLongKeys<UvLerngruppe>();

	private lerngruppeByIdKursAndIdKlasseAndIdFach: ListMap3DLongKeys<UvLerngruppe> = new ListMap3DLongKeys<UvLerngruppe>();

	private lerngruppenSchieneByIdPlanungsabschnittAndIdLerngruppeAndIdSchiene: ListMap3DLongKeys<UvLerngruppenSchiene> = new ListMap3DLongKeys<UvLerngruppenSchiene>();

	private readonly lerngruppenSchieneMenge: List<UvLerngruppenSchiene> = new ArrayList<UvLerngruppenSchiene>();

	private lerngruppenLehrerByIdPlanungsabschnittAndIdLerngruppeAndIdLehrer: ListMap3DLongKeys<UvLerngruppenLehrer> = new ListMap3DLongKeys<UvLerngruppenLehrer>();

	private readonly lerngruppenLehrerMenge: List<UvLerngruppenLehrer> = new ArrayList<UvLerngruppenLehrer>();

	private unterrichtByIdPlanungsabschnittAndIdLerngruppeAndIdZeitrasterEintragAndIdUnterricht: ListMap4DLongKeys<UvUnterricht> = new ListMap4DLongKeys<UvUnterricht>();

	private readonly unterrichtMenge: List<UvUnterricht> = new ArrayList<UvUnterricht>();

	private unterrichtRaumByIdPlanungsabschnittAndIdUnterrichtAndIdRaum: ListMap3DLongKeys<UvUnterrichtRaum> = new ListMap3DLongKeys<UvUnterrichtRaum>();

	private readonly unterrichtRaumMenge: List<UvUnterrichtRaum> = new ArrayList<UvUnterrichtRaum>();

	private unterrichtLerngruppenlehrerByIdPlanungsabschnittAndIdUnterrichtAndIdLerngruppenlehrer: ListMap3DLongKeys<UvUnterrichtLerngruppenlehrer> = new ListMap3DLongKeys<UvUnterrichtLerngruppenlehrer>();

	private readonly unterrichtLerngruppenlehrerMenge: List<UvUnterrichtLerngruppenlehrer> = new ArrayList<UvUnterrichtLerngruppenlehrer>();

	private readonly compJahrgangsdaten: Comparator<JahrgangsDaten> = { compare: (a: JahrgangsDaten, b: JahrgangsDaten) => {
		const cmp: number = JavaInteger.compare(a.sortierung, b.sortierung);
		return (cmp === 0) ? JavaLong.compare(a.id, b.id) : cmp;
	} };

	private readonly compLehrer: Comparator<UvLehrer> = { compare: (a: UvLehrer, b: UvLehrer) => {
		const result: number = JavaString.compareTo(a.kuerzel, b.kuerzel);
		if (result !== 0) {
			return result;
		}
		return JavaLong.compare(a.id, b.id);
	} };

	private readonly compPlanungsabschnittLehrer: Comparator<UvPlanungsabschnittLehrer> = { compare: (a: UvPlanungsabschnittLehrer, b: UvPlanungsabschnittLehrer) => {
		const aL: UvLehrer = this.lehrerGetByPlanungsabschnittLehrer(a);
		const bL: UvLehrer = this.lehrerGetByPlanungsabschnittLehrer(b);
		return this.compLehrer.compare(aL, bL);
	} };

	private readonly compLehrerPflichtstundensoll: Comparator<UvLehrerPflichtstundensoll> = { compare: (a: UvLehrerPflichtstundensoll, b: UvLehrerPflichtstundensoll) => {
		const aL: UvLehrer = this.lehrerGetByLehrerPflichtstundensoll(a);
		const bL: UvLehrer = this.lehrerGetByLehrerPflichtstundensoll(b);
		let result: number = this.compLehrer.compare(aL, bL);
		if (result !== 0) {
			return result;
		}
		result = JavaString.compareTo(b.gueltigVon, a.gueltigVon);
		if (result !== 0) {
			return result;
		}
		return JavaLong.compare(a.id, b.id);
	} };

	private readonly compLehrerAnrechnungsstunden: Comparator<UvLehrerAnrechnungsstunden> = { compare: (a: UvLehrerAnrechnungsstunden, b: UvLehrerAnrechnungsstunden) => {
		const aL: UvLehrer = this.lehrerGetByLehrerAnrechnungsstunden(a);
		const bL: UvLehrer = this.lehrerGetByLehrerAnrechnungsstunden(b);
		let result: number = this.compLehrer.compare(aL, bL);
		if (result !== 0) {
			return result;
		}
		result = JavaString.compareTo(b.gueltigVon, a.gueltigVon);
		if (result !== 0) {
			return result;
		}
		if ((a.gueltigBis === null) && (b.gueltigBis !== null)) {
			return -1;
		}
		if ((a.gueltigBis !== null) && (b.gueltigBis === null)) {
			return 1;
		}
		if ((a.gueltigBis !== null) && (b.gueltigBis !== null)) {
			result = JavaString.compareTo(a.gueltigBis, b.gueltigBis);
		}
		if (result !== 0) {
			return result;
		}
		result = JavaString.compareTo(a.anrechnungsgrundKrz, b.anrechnungsgrundKrz);
		if (result !== 0) {
			return result;
		}
		return JavaLong.compare(a.id, b.id);
	} };

	private readonly compRaumgruppe: Comparator<UvRaumgruppe> = { compare: (a: UvRaumgruppe, b: UvRaumgruppe) => JavaString.compareTo(a.bezeichnung, b.bezeichnung) };

	private readonly compRaum: Comparator<UvRaum> = { compare: (a: UvRaum, b: UvRaum) => JavaString.compareTo(a.kuerzel, b.kuerzel) };

	private readonly compFach: Comparator<UvFach> = { compare: (a: UvFach, b: UvFach) => {
		const fdA: FachDaten = this.fachdatenGetByFach(a);
		const fdB: FachDaten = this.fachdatenGetByFach(b);
		let result: number = JavaInteger.compare(fdA.sortierung, fdB.sortierung);
		if (result !== 0) {
			return result;
		}
		result = JavaString.compareTo(fdA.bezeichnung, fdB.bezeichnung);
		if (result !== 0) {
			return result;
		}
		result = JavaString.compareTo(a.gueltigVon, b.gueltigVon);
		if (result !== 0) {
			return result;
		}
		return JavaLong.compare(a.id, b.id);
	} };

	private readonly compStundentafelFach: Comparator<UvStundentafelFach> = { compare: (a: UvStundentafelFach, b: UvStundentafelFach) => {
		const fachA: UvFach = this.fachGetByStundentafelFach(a);
		const fachB: UvFach = this.fachGetByStundentafelFach(b);
		return this.compFach.compare(fachA, fachB);
	} };

	private readonly compZeitraster: Comparator<UvZeitraster> = { compare: (a: UvZeitraster, b: UvZeitraster) => {
		const result: number = JavaString.compareTo(a.gueltigVon, b.gueltigVon);
		if (result !== 0) {
			return result;
		}
		return JavaLong.compare(a.id, b.id);
	} };

	private readonly compZeitrasterEintrag: Comparator<UvZeitrasterEintrag> = { compare: (a: UvZeitrasterEintrag, b: UvZeitrasterEintrag) => {
		let result: number = JavaLong.compare(a.idZeitraster, b.idZeitraster);
		if (result !== 0) {
			return result;
		}
		result = JavaInteger.compare(a.wochentag, b.wochentag);
		if (result !== 0) {
			return result;
		}
		result = JavaInteger.compare(a.stunde, b.stunde);
		if (result !== 0) {
			return result;
		}
		return JavaLong.compare(a.id, b.id);
	} };

	private readonly compPlanungsabschnittZeitraster: Comparator<UvPlanungsabschnittZeitraster> = { compare: (a: UvPlanungsabschnittZeitraster, b: UvPlanungsabschnittZeitraster) => {
		const result: number = JavaLong.compare(a.idPlanungsabschnitt, b.idPlanungsabschnitt);
		if (result !== 0) {
			return result;
		}
		return JavaLong.compare(a.idZeitraster, b.idZeitraster);
	} };

	private readonly compPlanungsabschnittSchueler: Comparator<UvPlanungsabschnittSchueler> = { compare: (a: UvPlanungsabschnittSchueler, b: UvPlanungsabschnittSchueler) => {
		let result: number = JavaString.compareTo(a.daten.nachname, b.daten.nachname);
		if (result !== 0) {
			return result;
		}
		result = JavaString.compareTo(a.daten.vorname, b.daten.vorname);
		if (result !== 0) {
			return result;
		}
		return JavaLong.compare(a.idSchueler, b.idSchueler);
	} };

	private readonly compSchuelergruppe: Comparator<UvSchuelergruppe> = { compare: (a: UvSchuelergruppe, b: UvSchuelergruppe) => {
		const result: number = JavaString.compareTo(a.bezeichnung, b.bezeichnung);
		if (result !== 0) {
			return result;
		}
		return JavaLong.compare(a.id, b.id);
	} };

	private readonly compSchuelergruppeSchueler: Comparator<UvSchuelergruppeSchueler> = { compare: (a: UvSchuelergruppeSchueler, b: UvSchuelergruppeSchueler) => {
		const schuelerA: UvPlanungsabschnittSchueler = this.planungsabschnittSchuelerGetBySchuelergruppeSchueler(a);
		const schuelerB: UvPlanungsabschnittSchueler = this.planungsabschnittSchuelerGetBySchuelergruppeSchueler(b);
		return this.compPlanungsabschnittSchueler.compare(schuelerA, schuelerB);
	} };

	private readonly compKlasse: Comparator<UvKlasse> = { compare: (a: UvKlasse, b: UvKlasse) => {
		const result: number = JavaLong.compare(a.idPlanungsabschnitt, b.idPlanungsabschnitt);
		if (result !== 0) {
			return result;
		}
		return JavaString.compareTo(a.kuerzel, b.kuerzel);
	} };

	private readonly compKurs: Comparator<UvKurs> = { compare: (a: UvKurs, b: UvKurs) => {
		let result: number = JavaLong.compare(a.idPlanungsabschnitt, b.idPlanungsabschnitt);
		if (result !== 0) {
			return result;
		}
		result = JavaString.compareTo(a.kursart, b.kursart);
		if (result !== 0) {
			return result;
		}
		return JavaInteger.compare(a.kursnummer, b.kursnummer);
	} };

	private readonly compSchiene: Comparator<UvSchiene> = { compare: (a: UvSchiene, b: UvSchiene) => {
		const result: number = JavaLong.compare(a.idPlanungsabschnitt, b.idPlanungsabschnitt);
		if (result !== 0) {
			return result;
		}
		return JavaInteger.compare(a.nummer, b.nummer);
	} };

	private readonly compLerngruppe: Comparator<UvLerngruppe> = { compare: (a: UvLerngruppe, b: UvLerngruppe) => {
		const result: number = JavaLong.compare(a.idPlanungsabschnitt, b.idPlanungsabschnitt);
		if (result !== 0) {
			return result;
		}
		const aKlasse: UvKlasse | null = this.klasseGetByLerngruppe(a);
		const bKlasse: UvKlasse | null = this.klasseGetByLerngruppe(b);
		if ((aKlasse !== null) && (bKlasse !== null)) {
			return this.compKlasse.compare(aKlasse, bKlasse);
		}
		const aKurs: UvKurs | null = this.kursGetByLerngruppe(a);
		const bKurs: UvKurs | null = this.kursGetByLerngruppe(b);
		if ((aKurs !== null) && (bKurs !== null)) {
			return this.compKurs.compare(aKurs, bKurs);
		}
		if ((aKlasse !== null) && (bKurs !== null)) {
			return -1;
		}
		if ((aKurs !== null) && (bKlasse !== null)) {
			return 1;
		}
		return JavaLong.compare(a.id, b.id);
	} };

	private readonly compLerngruppenLehrer: Comparator<UvLerngruppenLehrer> = { compare: (a: UvLerngruppenLehrer, b: UvLerngruppenLehrer) => {
		const result: number = JavaInteger.compare(a.reihenfolge, b.reihenfolge);
		if (result !== 0) {
			return result;
		}
		return this.compLehrer.compare(this.lehrerGetByLerngruppenLehrer(a), this.lehrerGetByLerngruppenLehrer(b));
	} };

	private readonly compKlassenLehrer: Comparator<UvKlassenLehrer> = { compare: (a: UvKlassenLehrer, b: UvKlassenLehrer) => {
		const result: number = JavaInteger.compare(a.reihenfolge, b.reihenfolge);
		if (result !== 0) {
			return result;
		}
		return JavaLong.compare(a.id, b.id);
	} };

	private readonly compUnterricht: Comparator<UvUnterricht> = { compare: (a: UvUnterricht, b: UvUnterricht) => {
		let result: number = JavaLong.compare(a.idPlanungsabschnitt, b.idPlanungsabschnitt);
		if (result !== 0) {
			return result;
		}
		result = JavaLong.compare(a.idLerngruppe, b.idLerngruppe);
		if (result !== 0) {
			return result;
		}
		return JavaLong.compare(a.id, b.id);
	} };


	/**
	 * Konstruktor
	 */
	public constructor();

	/**
	 * Konstruktor
	 * @param jahrgaenge die {@link JahrgangsDaten}-Objekte, die hinzugefügt werden sollen
	 * @param fachdaten die {@link FachDaten}-Objekte, die hinzugefügt werden sollen
	 */
	public constructor(jahrgaenge: Collection<JahrgangsDaten>, fachdaten: Collection<FachDaten>);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0?: Collection<JahrgangsDaten>, __param1?: Collection<FachDaten>) {
		super();
		if ((__param0 === undefined) && (__param1 === undefined)) {
			// empty method body
		} else if (((__param0 !== undefined) && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('java.util.Collection'))) || (__param0 === null)) && ((__param1 !== undefined) && ((__param1 instanceof JavaObject) && (__param1.isTranspiledInstanceOf('java.util.Collection'))) || (__param1 === null))) {
			const jahrgaenge: Collection<JahrgangsDaten> = cast_java_util_Collection(__param0);
			const fachdaten: Collection<FachDaten> = cast_java_util_Collection(__param1);
			this.jahrgangsdatenAddAll(jahrgaenge);
			this.fachdatenAddAll(fachdaten);
		} else throw new Error('invalid method overload');
	}

	/**
	 * Füge alle Grunddaten zum Manager hinzu.
	 * @param gData die {@link UvGrunddatenBundle}, die hinzugefügt werden sollen
	 */
	public grunddatenBundleAdd(gData: UvGrunddatenBundle): void {
		this.grunddatenBundleAddOhneUpdate(gData);
		this.updateAll();
	}

	private grunddatenBundleAddOhneUpdate(gData: UvGrunddatenBundle): void {
		this.lehrerAddAllOhneUpdate(gData.lehrer);
		this.lehrerPflichtstundensollAddAllOhneUpdate(gData.lehrerPflichtstundensoll);
		this.lehrerAnrechnungsstundenAddAllOhneUpdate(gData.lehrerAnrechnungsstunden);
		this.raumgruppeAddAllOhneUpdate(gData.raumgruppen);
		this.raumAddAllOhneUpdate(gData.raeume);
		this.stundentafelAddAllOhneUpdate(gData.stundentafeln);
		this.fachAddAllOhneUpdate(gData.faecher);
		this.stundentafelFachAddAllOhneUpdate(gData.stundentafelfaecher);
		this.lehrerUnterrichtsfachAddAllOhneUpdate(gData.lehrerUnterrichtsfaecher);
		this.zeitrasterAddAllOhneUpdate(gData.zeitraster);
		this.zeitrasterEintragAddAllOhneUpdate(gData.zeitrastereintraege);
	}

	/**
	 * Füge alle Planungsabschnitts-Daten zum Manager hinzu.
	 * @param pData die Datensammlung
	 */
	public planungsabschnittsdatenBundleAdd(pData: UvPlanungsabschnittsdatenBundle): void {
		this.planungsabschnittsdatenBundleAddOhneUpdate(pData);
		this.updateAll();
	}

	private planungsabschnittsdatenBundleAddOhneUpdate(pData: UvPlanungsabschnittsdatenBundle): void {
		if (pData.planungsabschnitt !== null) {
			this.planungsabschnittAddOhneUpdate(pData.planungsabschnitt);
		}
		this.planungsabschnittLehrerAddAllOhneUpdate(pData.planungsabschnittlehrer);
		this.planungsabschnittSchuelerAddAllOhneUpdate(pData.planungsabschnittschueler);
		this.planungsabschnittZeitrasterAddAllOhneUpdate(pData.planungsabschnittzeitraster);
		this.klasseAddAllOhneUpdate(pData.klassen);
		this.klassenLehrerAddAllOhneUpdate(pData.klassenlehrer);
		this.kursAddAllOhneUpdate(pData.kurse);
		this.schuelergruppeAddAllOhneUpdate(pData.schuelergruppen);
		this.schuelergruppeSchuelerAddAllOhneUpdate(pData.schuelergruppenschueler);
		this.schieneAddAllOhneUpdate(pData.schienen);
		this.lerngruppeAddAllOhneUpdate(pData.lerngruppen);
		this.lerngruppenLehrerAddAllOhneUpdate(pData.lerngruppenlehrer);
		this.lerngruppenSchieneAddAllOhneUpdate(pData.lerngruppenschienen);
		this.unterrichtAddAllOhneUpdate(pData.unterrichte);
		this.unterrichtRaumAddAllOhneUpdate(pData.unterrichtraeume);
		this.unterrichtLerngruppenlehrerAddAllOhneUpdate(pData.unterrichtlerngruppenlehrer);
	}

	private updateAll(): void {
		this.updateLehrerMenge();
		this.updateLehrerByIdKLehrer();
		this.updateLehrerUnterrichtsfachMenge();
		this.updateLehrerUnterrichtsfachByIdKLehrerAndIdFach();
		this.updateLehrerUnterrichtsfachByIdLehrerAndIdFach();
		this.updatePlanungsabschnittLehrerMenge();
		this.updateLehrerAnrechnungsstundenMenge();
		this.updateLehrerPflichtstundensollMenge();
		this.updateRaumgruppeMenge();
		this.updateRaumMenge();
		this.updateFachMenge();
		this.updateStundentafelMenge();
		this.updateStundentafelFachMenge();
		this.updateZeitrasterMenge();
		this.updateZeitrasterEintragMenge();
		this.updatePlanungsabschnittZeitrasterMenge();
		this.updatePlanungsabschnittSchuelerMenge();
		this.updateSchuelergruppeMenge();
		this.updateSchuelergruppeSchuelerMenge();
		this.updateKlasseMenge();
		this.updateKlassenLehrerMenge();
		this.updateKursMenge();
		this.updateSchieneMenge();
		this.updateLerngruppeMenge();
		this.updateLerngruppenLehrerMenge();
		this.updateLerngruppenSchieneMenge();
		this.updateUnterrichtMenge();
		this.updateUnterrichtRaumMenge();
		this.updateUnterrichtLerngruppenlehrerMenge();
		this.updateLehrerMengeByIdFachAndSek();
		this.updateLehrerAnrechnungsstundenMengeByLehrerId();
		this.updateZeitrasterEintragByIdZeitrasterAndIdZeitrasterEintrag();
		this.updateLehrerPflichtstundensollMengeByLehrerId();
		this.updatePlanungsabschnittLehrerByIdPlanungsabschnittAndIdLehrer();
		this.updatePlanungsabschnittZeitrasterByIdPlanungsabschnittAndIdZeitraster();
		this.updatePlanungsabschnittSchuelerByIdPlanungsabschnittAndIdJahrgangAndIdKlasseAndIdSchueler();
		this.updatePlanungsabschnittSchuelerMengeByIdPlanungsabschnittAndIdSchuelergruppe();
		this.updateRaumByIdRaumgruppeAndIdRaum();
		this.updateRaumByKuerzel();
		this.updateStundentafelFachByIdStundentafelAndAbschnittAndIdFachAndIdStundentafelFach();
		this.updateFachMengeByIdFach();
		this.updateSchuelergruppeByIdPlanungsabschnittAndIdSchuelergruppe();
		this.updateKlasseByIdPlanungsabschnittAndIdSchuelergruppeAndIdKlasse();
		this.updateKlassenLehrerByIdPlanungsabschnittAndIdKlasseAndIdLehrer();
		this.updateKursByIdPlanungsabschnittAndIdSchuelergruppeAndIdKurs();
		this.updateSchieneByIdPlanungsabschnittAndNummerAndIdSchiene();
		this.updateLerngruppeByIdPlanungsabschnittAndIdLerngruppe();
		this.updateLerngruppenLehrerByIdPlanungsabschnittAndIdLerngruppeAndIdLehrer();
		this.updateLerngruppenSchieneByIdPlanungsabschnittAndIdLerngruppeAndIdSchiene();
		this.updateLerngruppeMengeByIdSchuelergruppe();
		this.updateLerngruppeByIdKursAndIdKlasseAndIdFach();
		this.updateUnterrichtByIdPlanungsabschnittAndIdLerngruppeAndIdZeitrasterEintragAndIdUnterricht();
		this.updateUnterrichtRaumByIdPlanungsabschnittAndIdUnterrichtAndIdRaum();
		this.updateUnterrichtLerngruppenlehrerByIdPlanungsabschnittAndIdUnterrichtAndIdLerngruppenlehrer();
	}

	private updateLehrerByIdKLehrer(): void {
		this.lehrerByIdKLehrer.clear();
		for (const lehrer of this.lehrerMenge) {
			if (lehrer.idKLehrer === null) {
				continue;
			}
			this.lehrerByIdKLehrer.put(lehrer.idKLehrer, lehrer);
		}
	}

	private updateLehrerPflichtstundensollMengeByLehrerId(): void {
		this.lehrerPflichtstundensollMengeByLehrerId.clear();
		for (const p of this.lehrerPflichtstundensollMenge) {
			MapUtils.getOrCreateArrayList(this.lehrerPflichtstundensollMengeByLehrerId, p.idLehrer).add(p);
		}
	}

	private updateLehrerAnrechnungsstundenMengeByLehrerId(): void {
		this.lehrerAnrechnungsstundenMengeByLehrerId.clear();
		for (const anr of this.lehrerAnrechnungsstundenMenge) {
			MapUtils.getOrCreateArrayList(this.lehrerAnrechnungsstundenMengeByLehrerId, anr.idLehrer).add(anr);
		}
	}

	private updatePlanungsabschnittLehrerByIdPlanungsabschnittAndIdLehrer(): void {
		this.planungsabschnittLehrerByIdPlanungsabschnittAndIdLehrer = new ListMap2DLongKeys();
		for (const anr of this.planungsabschnittLehrerMenge) {
			this.planungsabschnittLehrerByIdPlanungsabschnittAndIdLehrer.addSingle(anr.idPlanungsabschnitt, anr.idLehrer, anr);
		}
	}

	private updateRaumByIdRaumgruppeAndIdRaum(): void {
		this.raumByIdRaumgruppeAndIdRaum = new ListMap2DLongKeys();
		for (const raum of this.raumMenge) {
			this.raumByIdRaumgruppeAndIdRaum.addSingle((raum.idRaumgruppe === null) ? -1 : raum.idRaumgruppe, raum.id, raum);
		}
	}

	private updateRaumByKuerzel(): void {
		this.raumByKuerzel.clear();
		for (const raum of this.raumMenge) {
			this.raumByKuerzel.put(raum.kuerzel, raum);
		}
	}

	private updateStundentafelFachByIdStundentafelAndAbschnittAndIdFachAndIdStundentafelFach(): void {
		this.stundentafelFachByIdStundentafelAndAbschnittAndIdFachAndIdStundentafelFach = new ListMap4DLongKeys();
		for (const fach of this.stundentafelFachMenge) {
			this.stundentafelFachByIdStundentafelAndAbschnittAndIdFachAndIdStundentafelFach.addSingle(fach.idStundentafel, fach.abschnitt, fach.idFach, fach.id, fach);
		}
	}

	private updateFachMengeByIdFach(): void {
		this.fachMengeByIdFach = new ListMap1DLongKeys();
		for (const fach of this.fachMenge) {
			this.fachMengeByIdFach.add(fach.idFach, fach);
		}
	}

	private updateZeitrasterEintragByIdZeitrasterAndIdZeitrasterEintrag(): void {
		this.zeitrasterEintragByIdZeitrasterAndIdZeitrasterEintrag = new ListMap2DLongKeys();
		for (const eintrag of this.zeitrasterEintragMenge) {
			this.zeitrasterEintragByIdZeitrasterAndIdZeitrasterEintrag.addSingle(eintrag.idZeitraster, eintrag.id, eintrag);
		}
	}

	private updatePlanungsabschnittZeitrasterByIdPlanungsabschnittAndIdZeitraster(): void {
		this.planungsabschnittZeitrasterByIdPlanungsabschnittAndIdZeitraster = new ListMap2DLongKeys();
		for (const zuordnung of this.planungsabschnittZeitrasterMenge) {
			this.planungsabschnittZeitrasterByIdPlanungsabschnittAndIdZeitraster.addSingle(zuordnung.idPlanungsabschnitt, zuordnung.idZeitraster, zuordnung);
		}
	}

	private updatePlanungsabschnittSchuelerByIdPlanungsabschnittAndIdJahrgangAndIdKlasseAndIdSchueler(): void {
		this.planungsabschnittSchuelerByIdPlanungsabschnittAndIdJahrgangAndIdKlasseAndIdSchueler = new ListMap4DLongKeys();
		for (const zuordnung of this.planungsabschnittSchuelerMenge) {
			this.planungsabschnittSchuelerByIdPlanungsabschnittAndIdJahrgangAndIdKlasseAndIdSchueler.addSingle(zuordnung.idPlanungsabschnitt, zuordnung.idJahrgang, (zuordnung.idKlasse !== null) ? zuordnung.idKlasse : -1, zuordnung.idSchueler, zuordnung);
		}
	}

	private updateSchuelergruppeMenge(): void {
		this.schuelergruppeMenge.clear();
		this.schuelergruppeMenge.addAll(this.schuelergruppeByIdPlanungsabschnittAndIdSchuelergruppe.getAllValues());
		this.schuelergruppeMenge.sort(this.compSchuelergruppe);
	}

	private updateSchuelergruppeByIdPlanungsabschnittAndIdSchuelergruppe(): void {
		this.schuelergruppeByIdPlanungsabschnittAndIdSchuelergruppe = new ListMap2DLongKeys();
		for (const schuelergruppe of this.schuelergruppeMenge) {
			this.schuelergruppeByIdPlanungsabschnittAndIdSchuelergruppe.addSingle(schuelergruppe.idPlanungsabschnitt, schuelergruppe.id, schuelergruppe);
		}
	}

	private updateSchuelergruppeSchuelerMenge(): void {
		this.schuelergruppeSchuelerMenge.clear();
		this.schuelergruppeSchuelerMenge.addAll(this.schuelergruppeSchuelerByIdPlanungsabschnittAndIdSchuelergruppeAndIdSchueler.getAllValues());
		this.schuelergruppeSchuelerMenge.sort(this.compSchuelergruppeSchueler);
	}

	private updateSchuelergruppeSchuelerByIdPlanungsabschnittAndIdSchuelergruppeAndIdSchueler(): void {
		this.schuelergruppeSchuelerByIdPlanungsabschnittAndIdSchuelergruppeAndIdSchueler = new ListMap3DLongKeys();
		for (const zuordnung of this.schuelergruppeSchuelerMenge) {
			this.schuelergruppeSchuelerByIdPlanungsabschnittAndIdSchuelergruppeAndIdSchueler.addSingle(zuordnung.idPlanungsabschnitt, zuordnung.idSchuelergruppe, zuordnung.idSchueler, zuordnung);
		}
	}

	private updateKlasseMenge(): void {
		this.klasseMenge.clear();
		this.klasseMenge.addAll(this.klasseByIdPlanungsabschnittAndIdSchuelergruppeAndIdKlasse.getAllValues());
		this.klasseMenge.sort(this.compKlasse);
	}

	private updateKlasseByIdPlanungsabschnittAndIdSchuelergruppeAndIdKlasse(): void {
		this.klasseByIdPlanungsabschnittAndIdSchuelergruppeAndIdKlasse = new ListMap3DLongKeys();
		for (const klasse of this.klasseMenge) {
			this.klasseByIdPlanungsabschnittAndIdSchuelergruppeAndIdKlasse.addSingle(klasse.idPlanungsabschnitt, klasse.idSchuelergruppe, klasse.id, klasse);
		}
	}

	private updateKlassenLehrerMenge(): void {
		this.klassenLehrerMenge.clear();
		this.klassenLehrerMenge.addAll(this.klassenLehrerByIdPlanungsabschnittAndIdKlasseAndIdLehrer.getAllValues());
		this.klassenLehrerMenge.sort(this.compKlassenLehrer);
	}

	private updateKlassenLehrerByIdPlanungsabschnittAndIdKlasseAndIdLehrer(): void {
		this.klassenLehrerByIdPlanungsabschnittAndIdKlasseAndIdLehrer = new ListMap3DLongKeys();
		for (const kl of this.klassenLehrerMenge) {
			this.klassenLehrerByIdPlanungsabschnittAndIdKlasseAndIdLehrer.addSingle(kl.idPlanungsabschnitt, kl.idKlasse, kl.idLehrer, kl);
		}
	}

	private updateKursMenge(): void {
		this.kursMenge.clear();
		this.kursMenge.addAll(this.kursByIdPlanungsabschnittAndIdSchuelergruppeAndIdKurs.getAllValues());
		this.kursMenge.sort(this.compKurs);
	}

	private updateKursByIdPlanungsabschnittAndIdSchuelergruppeAndIdKurs(): void {
		this.kursByIdPlanungsabschnittAndIdSchuelergruppeAndIdKurs = new ListMap3DLongKeys();
		for (const kurs of this.kursMenge) {
			this.kursByIdPlanungsabschnittAndIdSchuelergruppeAndIdKurs.addSingle(kurs.idPlanungsabschnitt, kurs.idSchuelergruppe, kurs.id, kurs);
		}
	}

	private updateSchieneMenge(): void {
		this.schieneMenge.clear();
		this.schieneMenge.addAll(this.schieneByIdPlanungsabschnittAndNummerAndIdSchiene.getAllValues());
		this.schieneMenge.sort(this.compSchiene);
	}

	private updateSchieneByIdPlanungsabschnittAndNummerAndIdSchiene(): void {
		this.schieneByIdPlanungsabschnittAndNummerAndIdSchiene = new ListMap3DLongKeys();
		for (const schiene of this.schieneMenge) {
			this.schieneByIdPlanungsabschnittAndNummerAndIdSchiene.addSingle(schiene.idPlanungsabschnitt, schiene.nummer, schiene.id, schiene);
		}
	}

	private updateLerngruppeMenge(): void {
		this.lerngruppeMenge.clear();
		this.lerngruppeMenge.addAll(this.lerngruppeByIdPlanungsabschnittAndIdLerngruppe.getAllValues());
		this.lerngruppeMenge.sort(this.compLerngruppe);
	}

	private updateLerngruppeByIdPlanungsabschnittAndIdLerngruppe(): void {
		this.lerngruppeByIdPlanungsabschnittAndIdLerngruppe = new ListMap2DLongKeys();
		for (const lerngruppe of this.lerngruppeMenge) {
			this.lerngruppeByIdPlanungsabschnittAndIdLerngruppe.addSingle(lerngruppe.idPlanungsabschnitt, lerngruppe.id, lerngruppe);
		}
	}

	private updateLerngruppenLehrerMenge(): void {
		this.lerngruppenLehrerMenge.clear();
		this.lerngruppenLehrerMenge.addAll(this.lerngruppenLehrerByIdPlanungsabschnittAndIdLerngruppeAndIdLehrer.getAllValues());
		this.lerngruppenLehrerMenge.sort(this.compLerngruppenLehrer);
	}

	private updateLerngruppenLehrerByIdPlanungsabschnittAndIdLerngruppeAndIdLehrer(): void {
		this.lerngruppenLehrerByIdPlanungsabschnittAndIdLerngruppeAndIdLehrer = new ListMap3DLongKeys();
		for (const lgl of this.lerngruppenLehrerMenge) {
			this.lerngruppenLehrerByIdPlanungsabschnittAndIdLerngruppeAndIdLehrer.addSingle(lgl.idPlanungsabschnitt, lgl.idLerngruppe, lgl.idLehrer, lgl);
		}
	}

	private updateLerngruppenSchieneMenge(): void {
		this.lerngruppenSchieneMenge.clear();
		this.lerngruppenSchieneMenge.addAll(this.lerngruppenSchieneByIdPlanungsabschnittAndIdLerngruppeAndIdSchiene.getAllValues());
	}

	private updateLerngruppenSchieneByIdPlanungsabschnittAndIdLerngruppeAndIdSchiene(): void {
		this.lerngruppenSchieneByIdPlanungsabschnittAndIdLerngruppeAndIdSchiene = new ListMap3DLongKeys();
		for (const lgs of this.lerngruppenSchieneMenge) {
			this.lerngruppenSchieneByIdPlanungsabschnittAndIdLerngruppeAndIdSchiene.addSingle(lgs.idPlanungsabschnitt, lgs.idLerngruppe, lgs.idSchiene, lgs);
		}
	}

	private updatePlanungsabschnittSchuelerMengeByIdPlanungsabschnittAndIdSchuelergruppe(): void {
		this.planungsabschnittSchuelerMengeByIdPlanungsabschnittAndIdSchuelergruppe = new ListMap2DLongKeys();
		for (const grSchue of this.schuelergruppeSchuelerMenge) {
			this.planungsabschnittSchuelerMengeByIdPlanungsabschnittAndIdSchuelergruppe.add(grSchue.idPlanungsabschnitt, grSchue.idSchuelergruppe, this.planungsabschnittSchuelerByIdPlanungsabschnittAndIdJahrgangAndIdKlasseAndIdSchueler.getSingle14OrException(grSchue.idPlanungsabschnitt, grSchue.idSchueler));
		}
	}

	private updateLerngruppeMengeByIdSchuelergruppe(): void {
		this.lerngruppeMengeByIdSchuelergruppe = new ListMap1DLongKeys();
		for (const lerngruppe of this.lerngruppeMenge) {
			const kurs: UvKurs | null = this.kursGetByLerngruppe(lerngruppe);
			if (kurs !== null) {
				this.lerngruppeMengeByIdSchuelergruppe.add(kurs.idSchuelergruppe, lerngruppe);
			} else {
				this.lerngruppeMengeByIdSchuelergruppe.add(DeveloperNotificationException.ifNull(JavaString.format("Die Lerngruppe %d ist weder einer Klasse noch einem Kurs zugeordnet.", lerngruppe.id), this.klasseGetByLerngruppe(lerngruppe)).idSchuelergruppe, lerngruppe);
			}
		}
	}

	private updateLerngruppeByIdKursAndIdKlasseAndIdFach(): void {
		this.lerngruppeByIdKursAndIdKlasseAndIdFach = new ListMap3DLongKeys();
		for (const lerngruppe of this.lerngruppeMenge) {
			this.lerngruppeByIdKursAndIdKlasseAndIdFach.addSingle((lerngruppe.idKurs === null) ? -1 : lerngruppe.idKurs, (lerngruppe.idKlasse === null) ? -1 : lerngruppe.idKlasse, (lerngruppe.idFach === null) ? -1 : lerngruppe.idFach, lerngruppe);
		}
	}

	private updateUnterrichtMenge(): void {
		this.unterrichtMenge.clear();
		this.unterrichtMenge.addAll(this.unterrichtByIdPlanungsabschnittAndIdLerngruppeAndIdZeitrasterEintragAndIdUnterricht.getAllValues());
		this.unterrichtMenge.sort(this.compUnterricht);
	}

	private updateUnterrichtByIdPlanungsabschnittAndIdLerngruppeAndIdZeitrasterEintragAndIdUnterricht(): void {
		this.unterrichtByIdPlanungsabschnittAndIdLerngruppeAndIdZeitrasterEintragAndIdUnterricht = new ListMap4DLongKeys();
		for (const unterricht of this.unterrichtMenge) {
			this.unterrichtByIdPlanungsabschnittAndIdLerngruppeAndIdZeitrasterEintragAndIdUnterricht.addSingle(unterricht.idPlanungsabschnitt, unterricht.idLerngruppe, (unterricht.idZeitrasterEintrag === null) ? -1 : unterricht.idZeitrasterEintrag, unterricht.id, unterricht);
		}
	}

	private updateUnterrichtRaumMenge(): void {
		this.unterrichtRaumMenge.clear();
		this.unterrichtRaumMenge.addAll(this.unterrichtRaumByIdPlanungsabschnittAndIdUnterrichtAndIdRaum.getAllValues());
	}

	private updateUnterrichtRaumByIdPlanungsabschnittAndIdUnterrichtAndIdRaum(): void {
		this.unterrichtRaumByIdPlanungsabschnittAndIdUnterrichtAndIdRaum = new ListMap3DLongKeys();
		for (const zuordnung of this.unterrichtRaumMenge) {
			this.unterrichtRaumByIdPlanungsabschnittAndIdUnterrichtAndIdRaum.addSingle(zuordnung.idPlanungsabschnitt, zuordnung.idUnterricht, zuordnung.idRaum, zuordnung);
		}
	}

	private updateUnterrichtLerngruppenlehrerMenge(): void {
		this.unterrichtLerngruppenlehrerMenge.clear();
		this.unterrichtLerngruppenlehrerMenge.addAll(this.unterrichtLerngruppenlehrerByIdPlanungsabschnittAndIdUnterrichtAndIdLerngruppenlehrer.getAllValues());
	}

	private updateUnterrichtLerngruppenlehrerByIdPlanungsabschnittAndIdUnterrichtAndIdLerngruppenlehrer(): void {
		this.unterrichtLerngruppenlehrerByIdPlanungsabschnittAndIdUnterrichtAndIdLerngruppenlehrer = new ListMap3DLongKeys();
		for (const zuordnung of this.unterrichtLerngruppenlehrerMenge) {
			this.unterrichtLerngruppenlehrerByIdPlanungsabschnittAndIdUnterrichtAndIdLerngruppenlehrer.addSingle(zuordnung.idPlanungsabschnitt, zuordnung.idUnterricht, zuordnung.idLerngruppenLehrer, zuordnung);
		}
	}

	private updateLehrerMengeByIdFachAndSek(): void {
		this.lehrerMengeByIdFachAndSek = new ListMap2DLongKeys();
		for (const lehramt of this.lehrerUnterrichtsfachMenge) {
			const lehrer: UvLehrer = this.lehrerGetByLehrerUnterrichtsfachOrException(lehramt);
			if (lehramt.istSek1) {
				this.lehrerMengeByIdFachAndSek.add(lehramt.idFach, 1, lehrer);
			}
			if (lehramt.istSek2) {
				this.lehrerMengeByIdFachAndSek.add(lehramt.idFach, 2, lehrer);
			}
			if (lehramt.istSek1 && lehramt.istSek2) {
				this.lehrerMengeByIdFachAndSek.add(lehramt.idFach, 3, lehrer);
			}
		}
	}

	/**
	 * Fügt alle {@link JahrgangsDaten}-Objekte hinzu.
	 * @param jahrgaenge eine Sammlung der hinzuzufügenden {@link JahrgangsDaten}-Objekte
	 */
	private jahrgangsdatenAddAll(jahrgaenge: Collection<JahrgangsDaten>): void {
		this.jahrgangById = new HashMap();
		for (const jd of jahrgaenge) {
			DeveloperNotificationException.ifMapPutOverwrites(this.jahrgangById, jd.id, jd);
		}
	}

	private jahrgangsdatenByIdMapGet(): JavaMap<number, JahrgangsDaten> {
		return DeveloperNotificationException.ifNull("Keine Jahrgangsdaten im UvManager enthalten.", this.jahrgangById);
	}

	/**
	 * Liefert die Jahrgangsdaten zu einer ID
	 * @param id die ID, zu der die Jahrgangsdaten gesucht werden
	 * @return die {@link JahrgangsDaten}
	 */
	public jahrgangsdatenGetById(id: number): JahrgangsDaten {
		return DeveloperNotificationException.ifNull(JavaString.format("Jahrgang mit ID %d nicht im UvManager enthalten.", id), this.jahrgangsdatenByIdMapGet().get(id));
	}

	/**
	 * Gibt alle {@link JahrgangsDaten}-Objekte zurück.
	 * Hinweis: Wenn es keine {@link JahrgangsDaten}-Objekte gibt, ist die Liste leer.
	 * @return eine List aller {@link JahrgangsDaten}-Objekte
	 */
	public jahrgangsdatenGetMenge(): List<JahrgangsDaten> {
		if (this.jahrgangById === null) {
			return new ArrayList();
		}
		return new ArrayList<JahrgangsDaten>(this.jahrgangById.values());
	}

	/**
	 * Liefert das zur Stundentafel zugehörige {@link JahrgangsDaten}-Objekt. <br>
	 * @param st die {@link UvStundentafel}
	 * @return das zur Stundentafel zugehörige {@link JahrgangsDaten}-Objekt.
	 */
	public jahrgangsdatenGetByStundentafel(st: UvStundentafel): JahrgangsDaten {
		return DeveloperNotificationException.ifMapGetIsNull(this.jahrgangsdatenByIdMapGet(), st.idJahrgang);
	}

	/**
	 * Fügt alle {@link FachDaten}-Objekte hinzu.
	 * @param fachdaten eine Sammlung der hinzuzufügenden {@link FachDaten}-Objekte
	 */
	private fachdatenAddAll(fachdaten: Collection<FachDaten>): void {
		this.fachdatenById = new HashMap();
		for (const fd of fachdaten) {
			DeveloperNotificationException.ifMapPutOverwrites(this.fachdatenById, fd.id, fd);
		}
	}

	/**
	 * Gibt alle {@link FachDaten}-Objekte zurück.
	 * @return eine List aller {@link FachDaten}-Objekte
	 */
	public fachdatenGetMenge(): List<FachDaten> {
		return new ArrayList<FachDaten>(DeveloperNotificationException.ifNull("Fachdaten nicht im UvManager enthalten.", this.fachdatenById).values());
	}

	/**
	 * Liefert das zum {@link UvStundentafelFach} zugehörige {@link FachDaten}-Objekt. <br>
	 * @param stf das {@link UvStundentafelFach}
	 * @return das zum {@link UvStundentafelFach} zugehörige {@link FachDaten}-Objekt.
	 */
	public fachdatenGetByStundentafelFach(stf: UvStundentafelFach): FachDaten {
		return this.fachdatenGetByFach(this.fachGetByStundentafelFach(stf));
	}

	/**
	 * Liefert das zum {@link UvFach} zugehörige {@link FachDaten}-Objekt. <br>
	 * @param fach das {@link UvFach}
	 * @return das zum {@link UvFach} zugehörige {@link FachDaten}-Objekt.
	 */
	public fachdatenGetByFach(fach: UvFach): FachDaten {
		return DeveloperNotificationException.ifMapGetIsNull(DeveloperNotificationException.ifNull("FachDaten nicht im UvManager enthalten.", this.fachdatenById), fach.idFach);
	}

	/**
	 * Liefert das zum {@link LehrerUnterrichtsfach} zugehörige {@link FachDaten}-Objekt. <br>
	 * @param fach das {@link LehrerUnterrichtsfach}
	 * @return das zum {@link LehrerUnterrichtsfach} zugehörige {@link FachDaten}-Objekt.
	 */
	public fachdatenGetByLehrerUnterrichtsfach(fach: LehrerUnterrichtsfach): FachDaten {
		return DeveloperNotificationException.ifMapGetIsNull(DeveloperNotificationException.ifNull("FachDaten nicht im UvManager enthalten.", this.fachdatenById), fach.idFach);
	}

	/**
	 * Fügt ein {@link UvPlanungsabschnitt}-Objekt hinzu.
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}, der hinzugefügt werden soll
	 */
	public planungsabschnittAdd(planungsabschnitt: UvPlanungsabschnitt): void {
		this.planungsabschnittAddOhneUpdate(planungsabschnitt);
		this.updateAll();
	}

	private planungsabschnittAddOhneUpdate(planungsabschnitt: UvPlanungsabschnitt): void {
		this.planungsabschnittById.put(planungsabschnitt.id, planungsabschnitt);
	}

	/**
	 * Ersetzt das vorhandene {@link UvPlanungsabschnitt}-Objekt durch das neue DTO ohne Löschkaskade
	 * und baut die Indizes neu auf. Das bisherige Objekt wird nicht verändert.
	 *
	 * @param planungsabschnitt die vollständigen neuen Daten mit unveränderter ID
	 */
	public planungsabschnittPatchAttributes(planungsabschnitt: UvPlanungsabschnitt): void {
		this.planungsabschnittGetByIdOrException(planungsabschnitt.id);
		this.planungsabschnittById.put(planungsabschnitt.id, planungsabschnitt);
		this.updateAll();
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvPlanungsabschnitt}-Objekt. <br>
	 * @param idPlanungsabschnitt   Die ID des angefragten-Objektes.
	 * @return das zur ID zugehörige {@link UvPlanungsabschnitt}-Objekt.
	 */
	public planungsabschnittGetByIdOrException(idPlanungsabschnitt: number): UvPlanungsabschnitt {
		return DeveloperNotificationException.ifMapGetIsNull(this.planungsabschnittById, idPlanungsabschnitt);
	}

	/**
	 * Prüft, ob ein {@link UvPlanungsabschnitt}-Objekt im Manager existiert.
	 * @param planungsabschnitt das zu prüfende {@link UvPlanungsabschnitt}-Objekt
	 * @return {@code true}, wenn das Objekt im Manager existiert, sonst {@code false}
	 */
	public planungsabschnittIsGeladen(planungsabschnitt: UvPlanungsabschnitt): boolean {
		return this.planungsabschnittById.containsKey(planungsabschnitt.id);
	}

	private updateLehrerMenge(): void {
		this.lehrerMenge.clear();
		this.lehrerMenge.addAll(this.lehrerById.values());
		this.lehrerMenge.sort(this.compLehrer);
	}

	/**
	 * Fügt ein {@link UvLehrer}-Objekt hinzu.
	 * @param lehrer Das {@link UvLehrer}-Objekt, welches hinzugefügt werden soll.
	 */
	public lehrerAdd(lehrer: UvLehrer): void {
		this.lehrerAddAll(ListUtils.create1(lehrer));
	}

	private lehrerAddOhneUpdate(lehrer: UvLehrer): void {
		this.lehrerAddAllOhneUpdate(ListUtils.create1(lehrer));
	}

	/**
	 * Fügt alle {@link UvLehrer}-Objekte hinzu.
	 * @param listLehrer Die Menge der {@link UvLehrer}-Objekte, welche hinzugefügt werden soll.
	 */
	public lehrerAddAll(listLehrer: Collection<UvLehrer>): void {
		this.lehrerAddAllOhneUpdate(listLehrer);
		this.updateAll();
	}

	private lehrerAddAllOhneUpdate(list: Collection<UvLehrer>): void {
		const setOfIDs: HashSet<number> = new HashSet<number>();
		for (const lehrer of list) {
			UvManager.lehrerCheck(lehrer);
			DeveloperNotificationException.ifTrue("lehrerAddAllOhneUpdate: ID=" + lehrer.id + " existiert bereits!", this.lehrerById.containsKey(lehrer.id));
			DeveloperNotificationException.ifTrue("lehrerAddAllOhneUpdate: ID=" + lehrer.id + " doppelt in der Liste!", !setOfIDs.add(lehrer.id));
		}
		for (const lehrer of list) {
			DeveloperNotificationException.ifMapPutOverwrites(this.lehrerById, lehrer.id, lehrer);
		}
	}

	private static lehrerCheck(lehrer: UvLehrer): void {
		DeveloperNotificationException.ifInvalidID("lehrer.id", lehrer.id);
		DeveloperNotificationException.ifFalse(JavaString.format("Zugangsdatum von Lehrer mit ID %d nicht gültig.", lehrer.id), ((lehrer.datumZugang === null) || JavaString.isEmpty(lehrer.datumZugang.trim())) || DateUtils.isValidDate(lehrer.datumZugang));
		DeveloperNotificationException.ifFalse(JavaString.format("Abgangsdatum von Lehrer mit ID %d nicht gültig.", lehrer.id), (lehrer.datumAbgang === null) || DateUtils.isValidDate(lehrer.datumAbgang));
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvLehrer}-Objekt. <br>
	 * @param idLehrer Die ID des angefragten-Objektes.
	 * @return das zur ID zugehörige {@link UvLehrer}-Objekt.
	 */
	public lehrerGetByIdOrException(idLehrer: number): UvLehrer {
		return DeveloperNotificationException.ifMapGetIsNull(this.lehrerById, idLehrer);
	}

	/**
	 * Liefert das zur ID gehörende {@link UvLehrer}-Objekt direkt aus dem Index oder {@code null}.
	 *
	 * @param idLehrer die ID des gesuchten Objekts
	 * @return das gespeicherte Objekt oder {@code null}, falls es nicht vorhanden ist
	 */
	public lehrerGetByIdOrNull(idLehrer: number): UvLehrer | null {
		return this.lehrerById.get(idLehrer);
	}

	/**
	 * Liefert eine Liste aller {@link UvLehrer}-Objekte. <br>
	 * @return eine Liste aller {@link UvLehrer}-Objekte.
	 */
	public lehrerGetMengeAsList(): List<UvLehrer> {
		return new ArrayList<UvLehrer>(this.lehrerMenge);
	}

	/**
	 * Aktualisiert das vorhandene {@link UvLehrer}-Objekt durch das neue Objekt.
	 * @param lehrer Das neue {@link UvLehrer}-Objekt.
	 */
	public lehrerAllPatchAttributes(lehrer: Collection<UvLehrer>): void {
		this.lehrerAllPatchAttributesOhneUpdate(lehrer);
		this.updateAll();
	}

	private lehrerAllPatchAttributesOhneUpdate(lehrer: Collection<UvLehrer>): void {
		for (const neu of new HashSet(lehrer)) {
			DeveloperNotificationException.ifMapRemoveFailes(this.lehrerById, neu.id);
		}
		this.lehrerAddAllOhneUpdate(lehrer);
	}

	private lehrerRemoveOhneUpdateById(idLehrer: number): void {
		const lehrer: UvLehrer = this.lehrerGetByIdOrException(idLehrer);
		this.planungsabschnittLehrerByIdPlanungsabschnittAndIdLehrer.removeAllByKey2(idLehrer);
		this.lehrerAnrechnungsstundenRemoveAllOhneUpdate(this.lehrerAnrechnungsstundenGetMengeByLehrer(lehrer));
		this.lehrerPflichtstundensollRemoveAllOhneUpdate(this.lehrerPflichtstundensollGetMengeByLehrer(lehrer));
		this.klassenLehrerByIdPlanungsabschnittAndIdKlasseAndIdLehrer.removeAllByKey3(idLehrer);
		const unterrichtsfaecher: List<LehrerUnterrichtsfach> = new ArrayList<LehrerUnterrichtsfach>();
		for (const unterrichtsfach of this.lehrerUnterrichtsfachMenge) {
			if ((!unterrichtsfach.istKLehrer && (unterrichtsfach.idLehrer === idLehrer)) || (unterrichtsfach.istKLehrer && (lehrer.idKLehrer !== null) && (unterrichtsfach.idLehrer === lehrer.idKLehrer))) {
				unterrichtsfaecher.add(unterrichtsfach);
			}
		}
		this.lehrerUnterrichtsfachRemoveAllOhneUpdate(unterrichtsfaecher);
		DeveloperNotificationException.ifMapRemoveFailes(this.lehrerById, idLehrer);
	}

	/**
	 * Entfernt ein existierendes {@link UvLehrer}-Objekt.
	 * @param idLehrer Die ID des {@link UvLehrer}-Objekts.
	 */
	public lehrerRemoveById(idLehrer: number): void {
		this.lehrerRemoveOhneUpdateById(idLehrer);
		this.updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvLehrer}-Objekt.
	 * @param lehrer das zu entfernende {@link UvLehrer}-Objekt.
	 */
	public lehrerRemove(lehrer: UvLehrer): void {
		this.lehrerRemoveOhneUpdate(lehrer);
		this.updateAll();
	}

	private lehrerRemoveOhneUpdate(lehrer: UvLehrer): void {
		this.lehrerRemoveOhneUpdateById(lehrer.id);
	}

	/**
	 * Entfernt alle {@link UvLehrer}-Objekte.
	 * @param listLehrer Die Liste der zu entfernenden {@link UvLehrer}-Objekte.
	 */
	public lehrerRemoveAll(listLehrer: Collection<UvLehrer>): void {
		this.lehrerRemoveAllOhneUpdate(listLehrer);
		this.updateAll();
	}

	private lehrerRemoveAllOhneUpdate(listLehrer: Collection<UvLehrer>): void {
		const setLehrer: JavaSet<UvLehrer> | null = new HashSet<UvLehrer>(listLehrer);
		for (const lehrer of setLehrer) {
			this.lehrerRemoveOhneUpdateById(lehrer.id);
		}
	}

	/**
	 * Entfernt alle {@link UvLehrer}-Objekte.
	 * @param listLehrerIds Die Liste der zu entfernenden {@link UvLehrer}-Objekte.
	 */
	public lehrerRemoveAllById(listLehrerIds: List<number>): void {
		for (const idLehrer of listLehrerIds) {
			this.lehrerRemoveOhneUpdateById(idLehrer);
		}
		this.updateAll();
	}

	private updateLehrerUnterrichtsfachMenge(): void {
		this.lehrerUnterrichtsfachMenge.clear();
		this.lehrerUnterrichtsfachMenge.addAll(this.lehrerUnterrichtsfachByIstKLehrerAndId.getNonNullValuesAsList());
	}

	private updateLehrerUnterrichtsfachByIdKLehrerAndIdFach(): void {
		this.lehrerUnterrichtsfachByIdKLehrerAndIdFach = new ListMap2DLongKeys();
		for (const unterrichtsfach of this.lehrerUnterrichtsfachMenge) {
			if (!unterrichtsfach.istKLehrer) {
				continue;
			}
			this.lehrerUnterrichtsfachByIdKLehrerAndIdFach.addSingle(unterrichtsfach.idLehrer, unterrichtsfach.idFach, unterrichtsfach);
		}
	}

	private updateLehrerUnterrichtsfachByIdLehrerAndIdFach(): void {
		this.lehrerUnterrichtsfachByIdLehrerAndIdFach = new ListMap2DLongKeys();
		for (const unterrichtsfach of this.lehrerUnterrichtsfachMenge) {
			this.lehrerUnterrichtsfachByIdLehrerAndIdFach.addSingle(this.lehrerGetByLehrerUnterrichtsfachOrException(unterrichtsfach).id, unterrichtsfach.idFach, unterrichtsfach);
		}
	}

	/**
	 * Fügt ein {@link LehrerUnterrichtsfach}-Objekt hinzu.
	 * @param unterrichtsfach Das {@link LehrerUnterrichtsfach}-Objekt, welches hinzugefügt werden soll.
	 */
	public lehrerUnterrichtsfachAdd(unterrichtsfach: LehrerUnterrichtsfach): void {
		this.lehrerUnterrichtsfachAddAll(ListUtils.create1(unterrichtsfach));
	}

	/**
	 * Fügt alle {@link LehrerUnterrichtsfach}-Objekte hinzu.
	 * @param list Die Menge der {@link LehrerUnterrichtsfach}-Objekte, welche hinzugefügt werden soll.
	 */
	public lehrerUnterrichtsfachAddAll(list: Collection<LehrerUnterrichtsfach>): void {
		this.lehrerUnterrichtsfachAddAllOhneUpdate(list);
		this.updateAll();
	}

	private lehrerUnterrichtsfachAddAllOhneUpdate(list: Collection<LehrerUnterrichtsfach>): void {
		for (const la of list) {
			UvManager.lehrerUnterrichtsfachCheck(la);
			DeveloperNotificationException.ifTrue("lehrerUnterrichtsfachAddAllOhneUpdate: istKLehrer=" + la.istKLehrer + ", ID=" + la.id + " existiert bereits!", this.lehrerUnterrichtsfachByIstKLehrerAndId.contains(la.istKLehrer, la.id));
			this.lehrerUnterrichtsfachByIstKLehrerAndId.put(la.istKLehrer, la.id, la);
		}
	}

	private static lehrerUnterrichtsfachCheck(unterrichtsfach: LehrerUnterrichtsfach): void {
		DeveloperNotificationException.ifInvalidID("unterrichtsfach.id", unterrichtsfach.id);
		DeveloperNotificationException.ifInvalidID("unterrichtsfach.idLehrer", unterrichtsfach.idLehrer);
		DeveloperNotificationException.ifInvalidID("unterrichtsfach.idFach", unterrichtsfach.idFach);
	}

	/**
	 * Liefert das zugehörige {@link LehrerUnterrichtsfach}-Objekt. <br>
	 * @param istKLehrer gibt an, ob der Eintrag zu einem K-Lehrer gehört.
	 * @param idUnterrichtsfach Die ID des angefragten Objektes.
	 * @return das zugehörige {@link LehrerUnterrichtsfach}-Objekt.
	 */
	public lehrerUnterrichtsfachGetByIdOrException(istKLehrer: boolean, idUnterrichtsfach: number): LehrerUnterrichtsfach {
		return this.lehrerUnterrichtsfachByIstKLehrerAndId.getOrException(istKLehrer, idUnterrichtsfach);
	}

	/**
	 * Liefert eine Liste aller {@link LehrerUnterrichtsfach}-Objekte. <br>
	 * @return eine Liste aller {@link LehrerUnterrichtsfach}-Objekte.
	 */
	public lehrerUnterrichtsfachGetMengeAsList(): List<LehrerUnterrichtsfach> {
		return new ArrayList<LehrerUnterrichtsfach>(this.lehrerUnterrichtsfachMenge);
	}

	/**
	 * Aktualisiert das vorhandene {@link LehrerUnterrichtsfach}-Objekt durch das neue Objekt.
	 * @param unterrichtsfaecher Die neuen {@link LehrerUnterrichtsfach}-Objekte.
	 */
	public lehrerUnterrichtsfachAllPatchAttributes(unterrichtsfaecher: Collection<LehrerUnterrichtsfach>): void {
		this.lehrerUnterrichtsfachRemoveAllOhneUpdate(unterrichtsfaecher);
		this.lehrerUnterrichtsfachAddAllOhneUpdate(unterrichtsfaecher);
		this.updateAll();
	}

	private lehrerUnterrichtsfachRemoveOhneUpdate(unterrichtsfach: LehrerUnterrichtsfach): void {
		this.lehrerUnterrichtsfachByIstKLehrerAndId.removeOrException(unterrichtsfach.istKLehrer, unterrichtsfach.id);
	}

	/**
	 * Entfernt ein existierendes {@link LehrerUnterrichtsfach}-Objekt.
	 * @param istKLehrer gibt an, ob der Eintrag zu einem K-Lehrer gehört.
	 * @param idUnterrichtsfach Die ID des {@link LehrerUnterrichtsfach}-Objekts.
	 */
	public lehrerUnterrichtsfachRemoveById(istKLehrer: boolean, idUnterrichtsfach: number): void {
		this.lehrerUnterrichtsfachByIstKLehrerAndId.removeOrException(istKLehrer, idUnterrichtsfach);
		this.updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link LehrerUnterrichtsfach}-Objekt.
	 * @param unterrichtsfach das zu entfernende {@link LehrerUnterrichtsfach}-Objekt.
	 */
	public lehrerUnterrichtsfachRemove(unterrichtsfach: LehrerUnterrichtsfach): void {
		this.lehrerUnterrichtsfachRemoveOhneUpdate(unterrichtsfach);
		this.updateAll();
	}

	/**
	 * Entfernt alle {@link LehrerUnterrichtsfach}-Objekte.
	 * @param list Die Liste der zu entfernenden {@link LehrerUnterrichtsfach}-Objekte.
	 */
	public lehrerUnterrichtsfachRemoveAll(list: Collection<LehrerUnterrichtsfach>): void {
		this.lehrerUnterrichtsfachRemoveAllOhneUpdate(list);
		this.updateAll();
	}

	private lehrerUnterrichtsfachRemoveAllOhneUpdate(list: Collection<LehrerUnterrichtsfach>): void {
		const set: JavaSet<LehrerUnterrichtsfach> | null = new HashSet<LehrerUnterrichtsfach>(list);
		for (const la of set) {
			this.lehrerUnterrichtsfachRemoveOhneUpdate(la);
		}
	}

	/**
	 * Liefert die Liste der {@link LehrerUnterrichtsfach}-Objekte zu einem {@link UvLehrer}.
	 * @param lehrer der {@link UvLehrer}
	 * @return die Liste der {@link LehrerUnterrichtsfach}-Objekte, oder eine leere Liste
	 */
	public lehrerUnterrichtsfachGetMengeByLehrer(lehrer: UvLehrer): List<LehrerUnterrichtsfach> {
		return this.lehrerUnterrichtsfachByIdLehrerAndIdFach.get1(lehrer.id);
	}

	private updatePlanungsabschnittLehrerMenge(): void {
		this.planungsabschnittLehrerMenge.clear();
		this.planungsabschnittLehrerMenge.addAll(this.planungsabschnittLehrerByIdPlanungsabschnittAndIdLehrer.getAllValues());
		this.planungsabschnittLehrerMenge.sort(this.compPlanungsabschnittLehrer);
	}

	/**
	 * Fügt ein {@link UvPlanungsabschnittLehrer}-Objekt hinzu.
	 * @param lehrer Das {@link UvPlanungsabschnittLehrer}-Objekt, welches hinzugefügt werden soll.
	 */
	public planungsabschnittLehrerAdd(lehrer: UvPlanungsabschnittLehrer): void {
		this.planungsabschnittLehrerAddAll(ListUtils.create1(lehrer));
	}

	private planungsabschnittLehrerAddOhneUpdate(lehrer: UvPlanungsabschnittLehrer): void {
		this.planungsabschnittLehrerAddAllOhneUpdate(ListUtils.create1(lehrer));
	}

	/**
	 * Fügt alle {@link UvPlanungsabschnittLehrer}-Objekte hinzu.
	 * @param listLehrer Die Menge der {@link UvPlanungsabschnittLehrer}-Objekte, welche hinzugefügt werden soll.
	 */
	public planungsabschnittLehrerAddAll(listLehrer: Collection<UvPlanungsabschnittLehrer>): void {
		this.planungsabschnittLehrerAddAllOhneUpdate(listLehrer);
		this.updateAll();
	}

	private planungsabschnittLehrerAddAllOhneUpdate(list: Collection<UvPlanungsabschnittLehrer>): void {
		const setOfIDs: HashSet<UvPlanungsabschnittLehrer> = new HashSet<UvPlanungsabschnittLehrer>();
		for (const lehrer of list) {
			this.planungsabschnittLehrerCheck(lehrer);
			DeveloperNotificationException.ifTrue("planungsabschnittLehrerAddAllOhneUpdate: ID=" + lehrer.idPlanungsabschnitt + " existiert bereits!", this.planungsabschnittLehrerByIdPlanungsabschnittAndIdLehrer.containsKey12(lehrer.idPlanungsabschnitt, lehrer.idLehrer));
			DeveloperNotificationException.ifTrue("planungsabschnittLehrerAddAllOhneUpdate: ID=" + lehrer.idPlanungsabschnitt + " doppelt in der Liste!", !setOfIDs.add(lehrer));
		}
		for (const lehrer of list) {
			DeveloperNotificationException.ifListMap2DLongKeysPutOverwrites(this.planungsabschnittLehrerByIdPlanungsabschnittAndIdLehrer, lehrer.idPlanungsabschnitt, lehrer.idLehrer, lehrer);
		}
	}

	private planungsabschnittLehrerCheck(lehrer: UvPlanungsabschnittLehrer): void {
		DeveloperNotificationException.ifInvalidID("lehrer.idPlanungsabschnitt", lehrer.idPlanungsabschnitt);
		DeveloperNotificationException.ifInvalidID("lehrer.idLehrer", lehrer.idLehrer);
		if (!this.planungsabschnittById.containsKey(lehrer.idPlanungsabschnitt)) {
			throw new DeveloperNotificationException("planungsabschnittLehrerCheck: Der UvPlanungsabschnitt mit der ID " + lehrer.idPlanungsabschnitt + " existiert nicht.");
		}
		if (!this.lehrerById.containsKey(lehrer.idLehrer)) {
			throw new DeveloperNotificationException("planungsabschnittLehrerCheck: Der UvLehrer mit der ID " + lehrer.idLehrer + " existiert nicht.");
		}
	}

	private planungsabschnittLehrerRemoveOhneUpdateById(idPlanungsabschnitt: number, idLehrer: number): void {
		this.planungsabschnittLehrerByIdPlanungsabschnittAndIdLehrer.removeOrException(idPlanungsabschnitt, idLehrer);
	}

	/**
	 * Entfernt ein existierendes {@link UvPlanungsabschnittLehrer}-Objekt.
	 * @param idPlanungsabschnitt die ID des {@link UvPlanungsabschnitt}-Objekts.
	 * @param idLehrer Die ID des {@link UvLehrer}-Objekts.
	 */
	public planungsabschnittLehrerRemoveById(idPlanungsabschnitt: number, idLehrer: number): void {
		this.planungsabschnittLehrerRemoveOhneUpdateById(idPlanungsabschnitt, idLehrer);
		this.updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvPlanungsabschnittLehrer}-Objekt.
	 * @param lehrer das zu entfernende {@link UvPlanungsabschnittLehrer}-Objekt.
	 */
	public planungsabschnittLehrerRemove(lehrer: UvPlanungsabschnittLehrer): void {
		this.planungsabschnittLehrerRemoveOhneUpdate(lehrer);
		this.updateAll();
	}

	private planungsabschnittLehrerRemoveOhneUpdate(lehrer: UvPlanungsabschnittLehrer): void {
		this.planungsabschnittLehrerRemoveOhneUpdateById(lehrer.idPlanungsabschnitt, lehrer.idLehrer);
	}

	/**
	 * Entfernt alle {@link UvPlanungsabschnittLehrer}-Objekte.
	 * @param listLehrer Die Liste der zu entfernenden {@link UvPlanungsabschnittLehrer}-Objekte.
	 */
	public planungsabschnittLehrerRemoveAll(listLehrer: Collection<UvPlanungsabschnittLehrer>): void {
		this.planungsabschnittLehrerRemoveAllOhneUpdate(listLehrer);
		this.updateAll();
	}

	private planungsabschnittLehrerRemoveAllOhneUpdate(listLehrer: Collection<UvPlanungsabschnittLehrer>): void {
		const setLehrer: JavaSet<UvPlanungsabschnittLehrer> | null = new HashSet<UvPlanungsabschnittLehrer>(listLehrer);
		for (const lehrer of setLehrer) {
			this.planungsabschnittLehrerRemoveOhneUpdateById(lehrer.idPlanungsabschnitt, lehrer.idLehrer);
		}
	}

	/**
	 * Entfernt alle {@link UvPlanungsabschnittLehrer}-Objekte.
	 * @param idPlanungsabschnitt die ID des {@link UvPlanungsabschnitt}-Objekts.
	 * @param listLehrerIds Die Liste der zu entfernenden {@link UvPlanungsabschnittLehrer}-Objekte.
	 */
	public planungsabschnittLehrerRemoveAllById(idPlanungsabschnitt: number, listLehrerIds: List<number>): void {
		for (const idLehrer of listLehrerIds) {
			this.planungsabschnittLehrerRemoveById(idPlanungsabschnitt, idLehrer);
		}
		this.updateAll();
	}

	/**
	 * Liefert die Liste aller {@link UvPlanungsabschnittLehrer}-Objekte zu einem {@link UvPlanungsabschnitt}.
	 *
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}
	 * @return die Liste der {@link UvPlanungsabschnittLehrer}-Objekte
	 */
	public planungsabschnittLehrerGetMengeByPlanungsabschnitt(planungsabschnitt: UvPlanungsabschnitt): List<UvPlanungsabschnittLehrer> {
		return this.planungsabschnittLehrerByIdPlanungsabschnittAndIdLehrer.get1(planungsabschnitt.id);
	}

	private updateLehrerAnrechnungsstundenMenge(): void {
		this.lehrerAnrechnungsstundenMenge.clear();
		this.lehrerAnrechnungsstundenMenge.addAll(this.lehrerAnrechnungsstundenById.values());
		this.lehrerAnrechnungsstundenMenge.sort(this.compLehrerAnrechnungsstunden);
	}

	/**
	 * Fügt ein {@link UvLehrerAnrechnungsstunden}-Objekt hinzu.
	 * @param anrechnungsstunde Das {@link UvLehrerAnrechnungsstunden}-Objekt, welches hinzugefügt werden soll.
	 */
	public lehrerAnrechnungsstundenAdd(anrechnungsstunde: UvLehrerAnrechnungsstunden): void {
		this.lehrerAnrechnungsstundenAddAll(ListUtils.create1(anrechnungsstunde));
	}

	private lehrerAnrechnungsstundenAddOhneUpdate(anrechnungsstunde: UvLehrerAnrechnungsstunden): void {
		this.lehrerAnrechnungsstundenAddAllOhneUpdate(ListUtils.create1(anrechnungsstunde));
	}

	/**
	 * Fügt alle {@link UvLehrerAnrechnungsstunden}-Objekte hinzu.
	 * @param listAnrechnungsstunden Die Menge der {@link UvLehrerAnrechnungsstunden}-Objekte, welche hinzugefügt werden soll.
	 */
	public lehrerAnrechnungsstundenAddAll(listAnrechnungsstunden: Collection<UvLehrerAnrechnungsstunden>): void {
		this.lehrerAnrechnungsstundenAddAllOhneUpdate(listAnrechnungsstunden);
		this.updateAll();
	}

	private lehrerAnrechnungsstundenAddAllOhneUpdate(list: Collection<UvLehrerAnrechnungsstunden>): void {
		const setOfIDs: HashSet<number> = new HashSet<number>();
		for (const anrechnungsstunde of list) {
			this.lehrerAnrechnungsstundenCheck(anrechnungsstunde);
			DeveloperNotificationException.ifTrue("lehrerAnrechnungsstundenAddAllOhneUpdate: ID=" + anrechnungsstunde.id + " existiert bereits!", this.lehrerAnrechnungsstundenById.containsKey(anrechnungsstunde.id));
			DeveloperNotificationException.ifTrue("lehrerAnrechnungsstundenAddAllOhneUpdate: ID=" + anrechnungsstunde.id + " doppelt in der Liste!", !setOfIDs.add(anrechnungsstunde.id));
		}
		for (const anrechnungsstunde of list) {
			DeveloperNotificationException.ifMapPutOverwrites(this.lehrerAnrechnungsstundenById, anrechnungsstunde.id, anrechnungsstunde);
		}
	}

	private lehrerAnrechnungsstundenCheck(anrechnungsstunde: UvLehrerAnrechnungsstunden): void {
		DeveloperNotificationException.ifInvalidID("anrechnungsstunde.id", anrechnungsstunde.id);
		DeveloperNotificationException.ifInvalidID("anrechnungsstunde.idLehrer", anrechnungsstunde.idLehrer);
		if (!this.lehrerById.containsKey(anrechnungsstunde.idLehrer)) {
			throw new DeveloperNotificationException("lehrerAnrechnungsstundenCheck: Der UvLehrer mit der ID " + anrechnungsstunde.idLehrer + " existiert nicht.");
		}
		DeveloperNotificationException.ifFalse(JavaString.format("Gültigkeitsbeginn von UvLehrerAnrechnungsstunden mit ID %d nicht gültig.", anrechnungsstunde.id), DateUtils.isValidDate(anrechnungsstunde.gueltigVon));
		DeveloperNotificationException.ifFalse(JavaString.format("Gültigkeitsende von UvLehrerAnrechnungsstunden mit ID %d nicht gültig.", anrechnungsstunde.id), (anrechnungsstunde.gueltigBis === null) || DateUtils.isValidDate(anrechnungsstunde.gueltigBis));
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvLehrerAnrechnungsstunden}-Objekt. <br>
	 * @param idAnrechnungsstunde Die ID des angefragten-Objektes.
	 * @return das zur ID zugehörige {@link UvLehrerAnrechnungsstunden}-Objekt.
	 */
	public lehrerAnrechnungsstundenGetByIdOrException(idAnrechnungsstunde: number): UvLehrerAnrechnungsstunden {
		return DeveloperNotificationException.ifMapGetIsNull(this.lehrerAnrechnungsstundenById, idAnrechnungsstunde);
	}

	/**
	 * Liefert eine Liste aller {@link UvLehrerAnrechnungsstunden}-Objekte. <br>
	 * @return eine Liste aller {@link UvLehrerAnrechnungsstunden}-Objekte.
	 */
	public lehrerAnrechnungsstundenGetMengeAsList(): List<UvLehrerAnrechnungsstunden> {
		return new ArrayList<UvLehrerAnrechnungsstunden>(this.lehrerAnrechnungsstundenMenge);
	}

	/**
	 * Aktualisiert das vorhandene {@link UvLehrerAnrechnungsstunden}-Objekt durch das neue Objekt.
	 * @param anrechnungsstunden Die neuen {@link UvLehrerAnrechnungsstunden}-Objekte.
	 */
	public lehrerAnrechnungsstundenAllPatchAttributes(anrechnungsstunden: Collection<UvLehrerAnrechnungsstunden>): void {
		this.lehrerAnrechnungsstundenAllPatchAttributesOhneUpdate(anrechnungsstunden);
		this.updateAll();
	}

	private lehrerAnrechnungsstundenAllPatchAttributesOhneUpdate(anrechnungsstunden: Collection<UvLehrerAnrechnungsstunden>): void {
		this.lehrerAnrechnungsstundenRemoveAllOhneUpdate(anrechnungsstunden);
		this.lehrerAnrechnungsstundenAddAllOhneUpdate(anrechnungsstunden);
	}

	private lehrerAnrechnungsstundenRemoveOhneUpdateById(idAnrechnungsstunde: number): void {
		DeveloperNotificationException.ifMapRemoveFailes(this.lehrerAnrechnungsstundenById, idAnrechnungsstunde);
	}

	/**
	 * Entfernt ein existierendes {@link UvLehrerAnrechnungsstunden}-Objekt.
	 * @param idAnrechnungsstunde Die ID des {@link UvLehrerAnrechnungsstunden}-Objekts.
	 */
	public lehrerAnrechnungsstundenRemoveById(idAnrechnungsstunde: number): void {
		this.lehrerAnrechnungsstundenRemoveOhneUpdateById(idAnrechnungsstunde);
		this.updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvLehrerAnrechnungsstunden}-Objekt.
	 * @param anrechnungsstunde das zu entfernende {@link UvLehrerAnrechnungsstunden}-Objekt.
	 */
	public lehrerAnrechnungsstundenRemove(anrechnungsstunde: UvLehrerAnrechnungsstunden): void {
		this.lehrerAnrechnungsstundenRemoveOhneUpdate(anrechnungsstunde);
		this.updateAll();
	}

	private lehrerAnrechnungsstundenRemoveOhneUpdate(anrechnungsstunde: UvLehrerAnrechnungsstunden): void {
		this.lehrerAnrechnungsstundenRemoveOhneUpdateById(anrechnungsstunde.id);
	}

	/**
	 * Entfernt alle {@link UvLehrerAnrechnungsstunden}-Objekte.
	 * @param listAnrechnungsstunden Die Liste der zu entfernenden {@link UvLehrerAnrechnungsstunden}-Objekte.
	 */
	public lehrerAnrechnungsstundenRemoveAll(listAnrechnungsstunden: Collection<UvLehrerAnrechnungsstunden>): void {
		this.lehrerAnrechnungsstundenRemoveAllOhneUpdate(listAnrechnungsstunden);
		this.updateAll();
	}

	private lehrerAnrechnungsstundenRemoveAllOhneUpdate(listAnrechnungsstunden: Collection<UvLehrerAnrechnungsstunden>): void {
		const setAnrechnungsstunden: JavaSet<UvLehrerAnrechnungsstunden> | null = new HashSet<UvLehrerAnrechnungsstunden>(listAnrechnungsstunden);
		for (const anrechnungsstunde of setAnrechnungsstunden) {
			this.lehrerAnrechnungsstundenRemoveOhneUpdateById(anrechnungsstunde.id);
		}
	}

	private updateLehrerPflichtstundensollMenge(): void {
		this.lehrerPflichtstundensollMenge.clear();
		this.lehrerPflichtstundensollMenge.addAll(this.lehrerPflichtstundensollById.values());
		this.lehrerPflichtstundensollMenge.sort(this.compLehrerPflichtstundensoll);
	}

	/**
	 * Fügt ein {@link UvLehrerPflichtstundensoll}-Objekt hinzu.
	 * @param pflichtstundensoll Das {@link UvLehrerPflichtstundensoll}-Objekt, welches hinzugefügt werden soll.
	 */
	public lehrerPflichtstundensollAdd(pflichtstundensoll: UvLehrerPflichtstundensoll): void {
		this.lehrerPflichtstundensollAddAll(ListUtils.create1(pflichtstundensoll));
	}

	private lehrerPflichtstundensollAddOhneUpdate(pflichtstundensoll: UvLehrerPflichtstundensoll): void {
		this.lehrerPflichtstundensollAddAllOhneUpdate(ListUtils.create1(pflichtstundensoll));
	}

	/**
	 * Fügt alle {@link UvLehrerPflichtstundensoll}-Objekte hinzu.
	 * @param listPflichtstundensoll Die Menge der {@link UvLehrerPflichtstundensoll}-Objekte, welche hinzugefügt werden soll.
	 */
	public lehrerPflichtstundensollAddAll(listPflichtstundensoll: Collection<UvLehrerPflichtstundensoll>): void {
		this.lehrerPflichtstundensollAddAllOhneUpdate(listPflichtstundensoll);
		this.updateAll();
	}

	private lehrerPflichtstundensollAddAllOhneUpdate(list: Collection<UvLehrerPflichtstundensoll>): void {
		const setOfIDs: HashSet<number> = new HashSet<number>();
		for (const pflichtstundensoll of list) {
			this.lehrerPflichtstundensollCheck(pflichtstundensoll);
			DeveloperNotificationException.ifTrue("lehrerPflichtstundensollAddAllOhneUpdate: ID=" + pflichtstundensoll.id + " existiert bereits!", this.lehrerPflichtstundensollById.containsKey(pflichtstundensoll.id));
			DeveloperNotificationException.ifTrue("lehrerPflichtstundensollAddAllOhneUpdate: ID=" + pflichtstundensoll.id + " doppelt in der Liste!", !setOfIDs.add(pflichtstundensoll.id));
		}
		for (const pflichtstundensoll of list) {
			DeveloperNotificationException.ifMapPutOverwrites(this.lehrerPflichtstundensollById, pflichtstundensoll.id, pflichtstundensoll);
		}
	}

	private lehrerPflichtstundensollCheck(pflichtstundensoll: UvLehrerPflichtstundensoll): void {
		DeveloperNotificationException.ifInvalidID("pflichtstundensoll.id", pflichtstundensoll.id);
		DeveloperNotificationException.ifInvalidID("pflichtstundensoll.idLehrer", pflichtstundensoll.idLehrer);
		if (!this.lehrerById.containsKey(pflichtstundensoll.idLehrer)) {
			throw new DeveloperNotificationException("lehrerPflichtstundensollCheck: Der UvLehrer mit der ID " + pflichtstundensoll.idLehrer + " existiert nicht.");
		}
		DeveloperNotificationException.ifFalse(JavaString.format("Gültigkeitsbeginn von UvLehrerPflichtstundensoll mit ID %d nicht gültig.", pflichtstundensoll.id), DateUtils.isValidDate(pflichtstundensoll.gueltigVon));
		DeveloperNotificationException.ifFalse(JavaString.format("Gültigkeitsende von UvLehrerPflichtstundensoll mit ID %d nicht gültig.", pflichtstundensoll.id), (pflichtstundensoll.gueltigBis === null) || DateUtils.isValidDate(pflichtstundensoll.gueltigBis));
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvLehrerPflichtstundensoll}-Objekt. <br>
	 * @param idPflichtstundensoll Die ID des angefragten-Objektes.
	 * @return das zur ID zugehörige {@link UvLehrerPflichtstundensoll}-Objekt.
	 */
	public lehrerPflichtstundensollGetByIdOrException(idPflichtstundensoll: number): UvLehrerPflichtstundensoll {
		return DeveloperNotificationException.ifMapGetIsNull(this.lehrerPflichtstundensollById, idPflichtstundensoll);
	}

	/**
	 * Liefert eine Liste aller {@link UvLehrerPflichtstundensoll}-Objekte. <br>
	 * @return eine Liste aller {@link UvLehrerPflichtstundensoll}-Objekte.
	 */
	public lehrerPflichtstundensollGetMengeAsList(): List<UvLehrerPflichtstundensoll> {
		return new ArrayList<UvLehrerPflichtstundensoll>(this.lehrerPflichtstundensollMenge);
	}

	/**
	 * Aktualisiert das vorhandene {@link UvLehrerPflichtstundensoll}-Objekt durch das neue Objekt.
	 * @param pflichtstundensoll Die neuen {@link UvLehrerPflichtstundensoll}-Objekte.
	 */
	public lehrerPflichtstundensollAllPatchAttributes(pflichtstundensoll: Collection<UvLehrerPflichtstundensoll>): void {
		this.lehrerPflichtstundensollAllPatchAttributesOhneUpdate(pflichtstundensoll);
		this.updateAll();
	}

	private lehrerPflichtstundensollAllPatchAttributesOhneUpdate(pflichtstundensoll: Collection<UvLehrerPflichtstundensoll>): void {
		this.lehrerPflichtstundensollRemoveAllOhneUpdate(pflichtstundensoll);
		this.lehrerPflichtstundensollAddAllOhneUpdate(pflichtstundensoll);
	}

	private lehrerPflichtstundensollRemoveOhneUpdateById(idPflichtstundensoll: number): void {
		DeveloperNotificationException.ifMapRemoveFailes(this.lehrerPflichtstundensollById, idPflichtstundensoll);
	}

	/**
	 * Entfernt ein existierendes {@link UvLehrerPflichtstundensoll}-Objekt.
	 * @param idPflichtstundensoll Die ID des {@link UvLehrerPflichtstundensoll}-Objekts.
	 */
	public lehrerPflichtstundensollRemoveById(idPflichtstundensoll: number): void {
		this.lehrerPflichtstundensollRemoveOhneUpdateById(idPflichtstundensoll);
		this.updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvLehrerPflichtstundensoll}-Objekt.
	 * @param pflichtstundensoll das zu entfernende {@link UvLehrerPflichtstundensoll}-Objekt.
	 */
	public lehrerPflichtstundensollRemove(pflichtstundensoll: UvLehrerPflichtstundensoll): void {
		this.lehrerPflichtstundensollRemoveOhneUpdate(pflichtstundensoll);
		this.updateAll();
	}

	private lehrerPflichtstundensollRemoveOhneUpdate(pflichtstundensoll: UvLehrerPflichtstundensoll): void {
		this.lehrerPflichtstundensollRemoveOhneUpdateById(pflichtstundensoll.id);
	}

	/**
	 * Entfernt alle {@link UvLehrerPflichtstundensoll}-Objekte.
	 * @param listPflichtstundensoll Die Liste der zu entfernenden {@link UvLehrerPflichtstundensoll}-Objekte.
	 */
	public lehrerPflichtstundensollRemoveAll(listPflichtstundensoll: Collection<UvLehrerPflichtstundensoll>): void {
		this.lehrerPflichtstundensollRemoveAllOhneUpdate(listPflichtstundensoll);
		this.updateAll();
	}

	private lehrerPflichtstundensollRemoveAllOhneUpdate(listPflichtstundensoll: Collection<UvLehrerPflichtstundensoll>): void {
		const setPflichtstundensoll: JavaSet<UvLehrerPflichtstundensoll> | null = new HashSet<UvLehrerPflichtstundensoll>(listPflichtstundensoll);
		for (const pflichtstundensoll of setPflichtstundensoll) {
			this.lehrerPflichtstundensollRemoveOhneUpdateById(pflichtstundensoll.id);
		}
	}

	private updateRaumMenge(): void {
		this.raumMenge.clear();
		this.raumMenge.addAll(this.raumByIdRaumgruppeAndIdRaum.getAllValues());
		this.raumMenge.sort(this.compRaum);
	}

	/**
	 * Fügt ein {@link UvRaum}-Objekt hinzu.
	 * @param raum Das {@link UvRaum}-Objekt, welches hinzugefügt werden soll.
	 */
	public raumAdd(raum: UvRaum): void {
		this.raumAddAll(ListUtils.create1(raum));
	}

	private raumAddOhneUpdate(raum: UvRaum): void {
		this.raumAddAllOhneUpdate(ListUtils.create1(raum));
	}

	/**
	 * Fügt alle {@link UvRaum}-Objekte hinzu.
	 * @param listRaeume Die Menge der {@link UvRaum}-Objekte, welche hinzugefügt werden soll.
	 */
	public raumAddAll(listRaeume: Collection<UvRaum>): void {
		this.raumAddAllOhneUpdate(listRaeume);
		this.updateAll();
	}

	private raumAddAllOhneUpdate(list: Collection<UvRaum>): void {
		const setOfIDs: JavaSet<number> | null = new HashSet<number>();
		for (const raum of list) {
			this.raumCheck(raum);
			DeveloperNotificationException.ifTrue("raumAddAllOhneUpdate: ID=" + raum.id + " existiert bereits!", this.raumByIdRaumgruppeAndIdRaum.containsKey2(raum.id));
			DeveloperNotificationException.ifTrue("raumAddAllOhneUpdate: ID=" + raum.id + " doppelt in der Liste!", !setOfIDs.add(raum.id));
		}
		for (const raum of list) {
			DeveloperNotificationException.ifListMap2DLongKeysPutOverwrites(this.raumByIdRaumgruppeAndIdRaum, (raum.idRaumgruppe === null) ? -1 : raum.idRaumgruppe, raum.id, raum);
		}
	}

	private raumCheck(raum: UvRaum): void {
		DeveloperNotificationException.ifInvalidID("raum.id", raum.id);
		if (raum.idRaumgruppe !== null) {
			DeveloperNotificationException.ifInvalidID("raum.idRaumgruppe", raum.idRaumgruppe);
			if (!this.raumgruppeById.containsKey(raum.idRaumgruppe)) {
				throw new DeveloperNotificationException("raumCheck: Die UvRaumgruppe mit der ID " + raum.idRaumgruppe + " existiert nicht.");
			}
		}
		DeveloperNotificationException.ifFalse(JavaString.format("Gültigkeitsbeginn von UvRaum mit ID %d nicht gültig.", raum.id), DateUtils.isValidDate(raum.gueltigVon));
		DeveloperNotificationException.ifFalse(JavaString.format("Gültigkeitsende von UvRaum mit ID %d nicht gültig.", raum.id), (raum.gueltigBis === null) || DateUtils.isValidDate(raum.gueltigBis));
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvRaum}-Objekt. <br>
	 * @param idRaum Die ID des angefragten-Objektes.
	 * @return das zur ID zugehörige {@link UvRaum}-Objekt.
	 */
	public raumGetByIdOrException(idRaum: number): UvRaum {
		return this.raumByIdRaumgruppeAndIdRaum.getSingle2OrException(idRaum);
	}

	/**
	 * Liefert das zur ID gehörende {@link UvRaum}-Objekt direkt aus dem Index oder {@code null}.
	 *
	 * @param idRaum die ID des gesuchten Objekts
	 * @return das gespeicherte Objekt oder {@code null}, falls es nicht vorhanden ist
	 */
	public raumGetByIdOrNull(idRaum: number): UvRaum | null {
		return this.raumByIdRaumgruppeAndIdRaum.getSingle2OrNull(idRaum);
	}

	/**
	 * Liefert eine Liste aller {@link UvRaum}-Objekte. <br>
	 * @return eine Liste aller {@link UvRaum}-Objekte.
	 */
	public raumGetMengeAsList(): List<UvRaum> {
		return new ArrayList<UvRaum>(this.raumMenge);
	}

	/**
	 * Aktualisiert das vorhandene {@link UvRaum}-Objekt durch das neue Objekt.
	 * @param raeume Die neuen {@link UvRaum}-Objekte.
	 */
	public raumAllPatchAttributes(raeume: Collection<UvRaum>): void {
		this.raumAllPatchAttributesOhneUpdate(raeume);
		this.updateAll();
	}

	private raumAllPatchAttributesOhneUpdate(raeume: Collection<UvRaum>): void {
		this.raumRemoveAllOhneUpdate(raeume);
		this.raumAddAllOhneUpdate(raeume);
	}

	private raumRemoveOhneUpdateById(idRaum: number): void {
		this.raumByIdRaumgruppeAndIdRaum.removeAllByKey2(idRaum);
	}

	/**
	 * Entfernt ein existierendes {@link UvRaum}-Objekt.
	 * @param idRaum Die ID des {@link UvRaum}-Objekts.
	 */
	public raumRemoveById(idRaum: number): void {
		this.raumRemoveOhneUpdateById(idRaum);
		this.updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvRaum}-Objekt.
	 * @param raum das zu entfernende {@link UvRaum}-Objekt.
	 */
	public raumRemove(raum: UvRaum): void {
		this.raumRemoveOhneUpdate(raum);
		this.updateAll();
	}

	private raumRemoveOhneUpdate(raum: UvRaum): void {
		this.raumRemoveOhneUpdateById(raum.id);
	}

	/**
	 * Entfernt alle {@link UvRaum}-Objekte.
	 * @param listRaeume Die Liste der zu entfernenden {@link UvRaum}-Objekte.
	 */
	public raumRemoveAll(listRaeume: Collection<UvRaum>): void {
		this.raumRemoveAllOhneUpdate(listRaeume);
		this.updateAll();
	}

	private raumRemoveAllOhneUpdate(listRaeume: Collection<UvRaum>): void {
		const setRaeume: JavaSet<UvRaum> | null = new HashSet<UvRaum>(listRaeume);
		for (const raum of setRaeume) {
			this.raumRemoveOhneUpdateById(raum.id);
		}
	}

	/**
	 * Entfernt alle {@link UvRaum}-Objekte.
	 * @param listRaumIds Die Liste der zu entfernenden {@link UvRaum}-Objekte.
	 */
	public raumRemoveAllById(listRaumIds: List<number>): void {
		for (const idRaum of listRaumIds) {
			this.raumRemoveOhneUpdateById(idRaum);
		}
		this.updateAll();
	}

	private updateRaumgruppeMenge(): void {
		this.raumgruppeMenge.clear();
		this.raumgruppeMenge.addAll(this.raumgruppeById.values());
		this.raumgruppeMenge.sort(this.compRaumgruppe);
	}

	/**
	 * Fügt eine {@link UvRaumgruppe} hinzu.
	 * @param raumgruppe Die {@link UvRaumgruppe}, die hinzugefügt werden soll.
	 */
	public raumgruppeAdd(raumgruppe: UvRaumgruppe): void {
		this.raumgruppeAddAll(ListUtils.create1(raumgruppe));
	}

	private raumgruppeAddOhneUpdate(raumgruppe: UvRaumgruppe): void {
		this.raumgruppeAddAllOhneUpdate(ListUtils.create1(raumgruppe));
	}

	/**
	 * Fügt alle {@link UvRaumgruppe}-Objekte hinzu.
	 * @param listRaumgruppen Die Menge der {@link UvRaumgruppe}-Objekte, welche hinzugefügt werden soll.
	 */
	public raumgruppeAddAll(listRaumgruppen: Collection<UvRaumgruppe>): void {
		this.raumgruppeAddAllOhneUpdate(listRaumgruppen);
		this.updateAll();
	}

	private raumgruppeAddAllOhneUpdate(list: Collection<UvRaumgruppe>): void {
		const setOfIDs: JavaSet<number> | null = new HashSet<number>();
		for (const raumgruppe of list) {
			UvManager.raumgruppeCheck(raumgruppe);
			DeveloperNotificationException.ifTrue("raumgruppeAddAllOhneUpdate: ID=" + raumgruppe.id + " existiert bereits!", this.raumgruppeById.containsKey(raumgruppe.id));
			DeveloperNotificationException.ifTrue("raumgruppeAddAllOhneUpdate: ID=" + raumgruppe.id + " doppelt in der Liste!", !setOfIDs.add(raumgruppe.id));
		}
		for (const raumgruppe of list) {
			DeveloperNotificationException.ifMapPutOverwrites(this.raumgruppeById, raumgruppe.id, raumgruppe);
		}
	}

	private static raumgruppeCheck(raumgruppe: UvRaumgruppe): void {
		DeveloperNotificationException.ifInvalidID("raumgruppe.id", raumgruppe.id);
		DeveloperNotificationException.ifFalse(JavaString.format("Gültigkeitsbeginn von UvRaumgruppe mit ID %d nicht gültig.", raumgruppe.id), DateUtils.isValidDate(raumgruppe.gueltigVon));
		DeveloperNotificationException.ifFalse(JavaString.format("Gültigkeitsende von UvRaumgruppe mit ID %d nicht gültig.", raumgruppe.id), (raumgruppe.gueltigBis === null) || DateUtils.isValidDate(raumgruppe.gueltigBis));
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvRaumgruppe}-Objekt. <br>
	 * @param idRaumgruppe Die ID des angefragten-Objektes.
	 * @return das zur ID zugehörige {@link UvRaumgruppe}-Objekt.
	 */
	public raumgruppeGetByIdOrException(idRaumgruppe: number): UvRaumgruppe {
		return DeveloperNotificationException.ifMapGetIsNull(this.raumgruppeById, idRaumgruppe);
	}

	/**
	 * Liefert das zur ID gehörende {@link UvRaumgruppe}-Objekt direkt aus dem Index oder {@code null}.
	 *
	 * @param idRaumgruppe die ID des gesuchten Objekts
	 * @return das gespeicherte Objekt oder {@code null}, falls es nicht vorhanden ist
	 */
	public raumgruppeGetByIdOrNull(idRaumgruppe: number): UvRaumgruppe | null {
		return this.raumgruppeById.get(idRaumgruppe);
	}

	/**
	 * Liefert eine Liste aller {@link UvRaumgruppe}-Objekte. <br>
	 * @return eine Liste aller {@link UvRaumgruppe}-Objekte.
	 */
	public raumgruppeGetMengeAsList(): List<UvRaumgruppe> {
		return new ArrayList<UvRaumgruppe>(this.raumgruppeMenge);
	}

	/**
	 * Aktualisiert das vorhandene {@link UvRaumgruppe}-Objekt durch das neue Objekt.
	 * @param raumgruppen Die neuen {@link UvRaumgruppe}-Objekte.
	 */
	public raumgruppeAllPatchAttributes(raumgruppen: Collection<UvRaumgruppe>): void {
		this.raumgruppeAllPatchAttributesOhneUpdate(raumgruppen);
		this.updateAll();
	}

	private raumgruppeAllPatchAttributesOhneUpdate(raumgruppen: Collection<UvRaumgruppe>): void {
		for (const neu of new HashSet(raumgruppen)) {
			DeveloperNotificationException.ifMapRemoveFailes(this.raumgruppeById, neu.id);
		}
		this.raumgruppeAddAllOhneUpdate(raumgruppen);
	}

	private raumgruppeRemoveOhneUpdateById(idRaumgruppe: number): void {
		DeveloperNotificationException.ifMapRemoveFailes(this.raumgruppeById, idRaumgruppe);
		for (const raum of this.raumByIdRaumgruppeAndIdRaum.get1(idRaumgruppe)) {
			raum.idRaumgruppe = null;
		}
	}

	/**
	 * Entfernt ein existierendes {@link UvRaumgruppe}-Objekt.
	 * @param idRaumgruppe Die ID des {@link UvRaumgruppe}-Objekts.
	 */
	public raumgruppeRemoveById(idRaumgruppe: number): void {
		this.raumgruppeRemoveOhneUpdateById(idRaumgruppe);
		this.updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvRaumgruppe}-Objekt.
	 * @param raumgruppe das zu entfernende {@link UvRaumgruppe}-Objekt.
	 */
	public raumgruppeRemove(raumgruppe: UvRaumgruppe): void {
		this.raumgruppeRemoveOhneUpdate(raumgruppe);
		this.updateAll();
	}

	private raumgruppeRemoveOhneUpdate(raumgruppe: UvRaumgruppe): void {
		this.raumgruppeRemoveOhneUpdateById(raumgruppe.id);
	}

	/**
	 * Entfernt alle {@link UvRaumgruppe}-Objekte.
	 * @param listRaumgruppen Die Liste der zu entfernenden {@link UvRaumgruppe}-Objekte.
	 */
	public raumgruppeRemoveAll(listRaumgruppen: Collection<UvRaumgruppe>): void {
		this.raumgruppeRemoveAllOhneUpdate(listRaumgruppen);
		this.updateAll();
	}

	private raumgruppeRemoveAllOhneUpdate(listRaumgruppen: Collection<UvRaumgruppe>): void {
		const setRaumgruppen: JavaSet<UvRaumgruppe> | null = new HashSet<UvRaumgruppe>(listRaumgruppen);
		for (const raumgruppe of setRaumgruppen) {
			this.raumgruppeRemoveOhneUpdateById(raumgruppe.id);
		}
	}

	private updateStundentafelMenge(): void {
		this.stundentafelMenge.clear();
		this.stundentafelMenge.addAll(this.stundentafelById.values());
	}

	/**
	 * Fügt ein {@link UvStundentafel}-Objekt hinzu.
	 * @param stundentafel Das {@link UvStundentafel}-Objekt, welches hinzugefügt werden soll.
	 */
	public stundentafelAdd(stundentafel: UvStundentafel): void {
		this.stundentafelAddAll(ListUtils.create1(stundentafel));
	}

	private stundentafelAddOhneUpdate(stundentafel: UvStundentafel): void {
		this.stundentafelAddAllOhneUpdate(ListUtils.create1(stundentafel));
	}

	/**
	 * Fügt alle {@link UvStundentafel}-Objekte hinzu.
	 * @param listStundentafeln Die Menge der {@link UvStundentafel}-Objekte, welche hinzugefügt werden soll.
	 */
	public stundentafelAddAll(listStundentafeln: Collection<UvStundentafel>): void {
		this.stundentafelAddAllOhneUpdate(listStundentafeln);
		this.updateAll();
	}

	private stundentafelAddAllOhneUpdate(list: Collection<UvStundentafel>): void {
		const setOfIDs: HashSet<number> = new HashSet<number>();
		for (const stundentafel of list) {
			UvManager.stundentafelCheck(stundentafel);
			DeveloperNotificationException.ifTrue("stundentafelAddAllOhneUpdate: ID=" + stundentafel.id + " existiert bereits!", this.stundentafelById.containsKey(stundentafel.id));
			DeveloperNotificationException.ifTrue("stundentafelAddAllOhneUpdate: ID=" + stundentafel.id + " doppelt in der Liste!", !setOfIDs.add(stundentafel.id));
		}
		for (const stundentafel of list) {
			DeveloperNotificationException.ifMapPutOverwrites(this.stundentafelById, stundentafel.id, stundentafel);
		}
	}

	private static stundentafelCheck(stundentafel: UvStundentafel): void {
		DeveloperNotificationException.ifInvalidID("stundentafel.id", stundentafel.id);
		DeveloperNotificationException.ifInvalidID("stundentafel.idJahrgang", stundentafel.idJahrgang);
		DeveloperNotificationException.ifFalse(JavaString.format("Gültigkeitsbeginn von UvStundentafel mit ID %d nicht gültig.", stundentafel.id), DateUtils.isValidDate(stundentafel.gueltigVon));
		DeveloperNotificationException.ifFalse(JavaString.format("Gültigkeitsende von UvStundentafel mit ID %d nicht gültig.", stundentafel.id), (stundentafel.gueltigBis === null) || DateUtils.isValidDate(stundentafel.gueltigBis));
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvStundentafel}-Objekt. <br>
	 * @param idStundentafel Die ID des angefragten-Objektes.
	 * @return das zur ID zugehörige {@link UvStundentafel}-Objekt.
	 */
	public stundentafelGetByIdOrException(idStundentafel: number): UvStundentafel {
		return DeveloperNotificationException.ifMapGetIsNull(this.stundentafelById, idStundentafel);
	}

	/**
	 * Liefert das zur ID gehörende {@link UvStundentafel}-Objekt direkt aus dem Index oder {@code null}.
	 *
	 * @param idStundentafel die ID des gesuchten Objekts
	 * @return das gespeicherte Objekt oder {@code null}, falls es nicht vorhanden ist
	 */
	public stundentafelGetByIdOrNull(idStundentafel: number): UvStundentafel | null {
		return this.stundentafelById.get(idStundentafel);
	}

	/**
	 * Liefert eine Liste aller {@link UvStundentafel}-Objekte. <br>
	 * @return eine Liste aller {@link UvStundentafel}-Objekte.
	 */
	public stundentafelGetMengeAsList(): List<UvStundentafel> {
		return new ArrayList<UvStundentafel>(this.stundentafelMenge);
	}

	/**
	 * Aktualisiert das vorhandene {@link UvStundentafel}-Objekt durch das neue Objekt.
	 * @param stundentafeln Die neuen {@link UvStundentafel}-Objekte.
	 */
	public stundentafelAllPatchAttributes(stundentafeln: Collection<UvStundentafel>): void {
		this.stundentafelAllPatchAttributesOhneUpdate(stundentafeln);
		this.updateAll();
	}

	private stundentafelAllPatchAttributesOhneUpdate(stundentafeln: Collection<UvStundentafel>): void {
		for (const neu of new HashSet(stundentafeln)) {
			DeveloperNotificationException.ifMapRemoveFailes(this.stundentafelById, neu.id);
		}
		this.stundentafelAddAllOhneUpdate(stundentafeln);
	}

	private stundentafelRemoveOhneUpdateById(idStundentafel: number): void {
		DeveloperNotificationException.ifMapRemoveFailes(this.stundentafelById, idStundentafel);
		const listFaecher: List<UvStundentafelFach> = this.stundentafelFachByIdStundentafelAndAbschnittAndIdFachAndIdStundentafelFach.get1(idStundentafel);
		this.stundentafelFachRemoveAllOhneUpdate(listFaecher);
	}

	/**
	 * Entfernt ein existierendes {@link UvStundentafel}-Objekt.
	 * @param idStundentafel Die ID des {@link UvStundentafel}-Objekts.
	 */
	public stundentafelRemoveById(idStundentafel: number): void {
		this.stundentafelRemoveOhneUpdateById(idStundentafel);
		this.updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvStundentafel}-Objekt.
	 * @param stundentafel das zu entfernende {@link UvStundentafel}-Objekt.
	 */
	public stundentafelRemove(stundentafel: UvStundentafel): void {
		this.stundentafelRemoveOhneUpdate(stundentafel);
		this.updateAll();
	}

	private stundentafelRemoveOhneUpdate(stundentafel: UvStundentafel): void {
		this.stundentafelRemoveOhneUpdateById(stundentafel.id);
	}

	/**
	 * Entfernt alle {@link UvStundentafel}-Objekte.
	 * @param listStundentafeln Die Liste der zu entfernenden {@link UvStundentafel}-Objekte.
	 */
	public stundentafelRemoveAll(listStundentafeln: Collection<UvStundentafel>): void {
		this.stundentafelRemoveAllOhneUpdate(listStundentafeln);
		this.updateAll();
	}

	private stundentafelRemoveAllOhneUpdate(listStundentafeln: Collection<UvStundentafel>): void {
		const setStundentafeln: JavaSet<UvStundentafel> | null = new HashSet<UvStundentafel>(listStundentafeln);
		for (const stundentafel of setStundentafeln) {
			this.stundentafelRemoveOhneUpdateById(stundentafel.id);
		}
	}

	/**
	 * Entfernt alle {@link UvStundentafel}-Objekte.
	 * @param listStundentafelIds Die Liste der IDs der zu entfernenden {@link UvStundentafel}-Objekte.
	 */
	public stundentafelRemoveAllById(listStundentafelIds: List<number>): void {
		for (const idStundentafel of listStundentafelIds) {
			this.stundentafelRemoveOhneUpdateById(idStundentafel);
		}
		this.updateAll();
	}

	private updateStundentafelFachMenge(): void {
		this.stundentafelFachMenge.clear();
		for (const key1 of this.stundentafelFachByIdStundentafelAndAbschnittAndIdFachAndIdStundentafelFach.keySet1()) {
			this.stundentafelFachMenge.addAll(this.stundentafelFachByIdStundentafelAndAbschnittAndIdFachAndIdStundentafelFach.get1(key1));
		}
		this.stundentafelFachMenge.sort(this.compStundentafelFach);
	}

	/**
	 * Fügt ein {@link UvStundentafelFach}-Objekt hinzu.
	 * @param fach Das {@link UvStundentafelFach}-Objekt, welches hinzugefügt werden soll.
	 */
	public stundentafelFachAdd(fach: UvStundentafelFach): void {
		this.stundentafelFachAddAll(ListUtils.create1(fach));
	}

	private stundentafelFachAddOhneUpdate(fach: UvStundentafelFach): void {
		this.stundentafelFachAddAllOhneUpdate(ListUtils.create1(fach));
	}

	/**
	 * Fügt alle {@link UvStundentafelFach}-Objekte hinzu.
	 * @param listFaecher Die Menge der {@link UvStundentafelFach}-Objekte, welche hinzugefügt werden soll.
	 */
	public stundentafelFachAddAll(listFaecher: Collection<UvStundentafelFach>): void {
		this.stundentafelFachAddAllOhneUpdate(listFaecher);
		this.updateAll();
	}

	private stundentafelFachAddAllOhneUpdate(list: Collection<UvStundentafelFach>): void {
		const setOfIDs: HashSet<number> = new HashSet<number>();
		for (const fach of list) {
			this.stundentafelFachCheck(fach);
			DeveloperNotificationException.ifTrue("stundentafelFachAddAllOhneUpdate: ID=" + fach.id + " existiert bereits!", this.stundentafelFachByIdStundentafelAndAbschnittAndIdFachAndIdStundentafelFach.containsKey123(fach.idStundentafel, fach.abschnitt, fach.idFach));
			DeveloperNotificationException.ifTrue("stundentafelFachAddAllOhneUpdate: ID=" + fach.id + " doppelt in der Liste!", !setOfIDs.add(fach.id));
		}
		for (const fach of list) {
			this.stundentafelFachByIdStundentafelAndAbschnittAndIdFachAndIdStundentafelFach.addSingle(fach.idStundentafel, fach.abschnitt, fach.idFach, fach.id, fach);
		}
	}

	private stundentafelFachCheck(fach: UvStundentafelFach): void {
		DeveloperNotificationException.ifInvalidID("fach.id", fach.id);
		DeveloperNotificationException.ifInvalidID("fach.idStundentafel", fach.idStundentafel);
		DeveloperNotificationException.ifInvalidID("fach.idFach", fach.idFach);
		if (!this.stundentafelById.containsKey(fach.idStundentafel)) {
			throw new DeveloperNotificationException("stundentafelFachCheck: Die UvStundentafel mit der ID " + fach.idStundentafel + " existiert nicht.");
		}
		if (!this.fachById.containsKey(fach.idFach)) {
			throw new DeveloperNotificationException("stundentafelFachCheck: Das UvFach mit der ID " + fach.idFach + " existiert nicht.");
		}
	}

	/**
	 * Liefert eine Liste aller {@link UvStundentafelFach}-Objekte. <br>
	 * @return eine Liste aller {@link UvStundentafelFach}-Objekte.
	 */
	public stundentafelFachGetMengeAsList(): List<UvStundentafelFach> {
		return new ArrayList<UvStundentafelFach>(this.stundentafelFachMenge);
	}

	/**
	 * Liefert das zur ID gehörige {@link UvStundentafelFach}-Objekt zurück.
	 * @param idStundentafelfach die ID des {@link UvStundentafelFach}-Objekts.
	 * @return das {@link UvStundentafelFach}-Objekt
	 */
	public stundentafelFachGetByIdOrException(idStundentafelfach: number): UvStundentafelFach {
		return this.stundentafelFachByIdStundentafelAndAbschnittAndIdFachAndIdStundentafelFach.getSingle4OrException(idStundentafelfach);
	}

	/**
	 * Aktualisiert das vorhandene {@link UvStundentafelFach}-Objekt durch das neue Objekt.
	 * @param faecher Die neuen {@link UvStundentafelFach}-Objekte.
	 */
	public stundentafelFachAllPatchAttributes(faecher: Collection<UvStundentafelFach>): void {
		this.stundentafelFachAllPatchAttributesOhneUpdate(faecher);
		this.updateAll();
	}

	private stundentafelFachAllPatchAttributesOhneUpdate(faecher: Collection<UvStundentafelFach>): void {
		for (const neu of new HashSet(faecher)) {
			this.stundentafelFachRemoveOhneUpdate(this.stundentafelFachGetByIdOrException(neu.id));
		}
		this.stundentafelFachAddAllOhneUpdate(faecher);
	}

	private stundentafelFachRemoveOhneUpdateById(idStundentafelfach: number): void {
		this.stundentafelFachRemoveOhneUpdate(this.stundentafelFachGetByIdOrException(idStundentafelfach));
	}

	/**
	 * Entfernt ein existierendes {@link UvStundentafelFach}-Objekt.
	 * @param idFach Die ID des {@link UvStundentafelFach}-Objekts.
	 */
	public stundentafelFachRemoveById(idFach: number): void {
		this.stundentafelFachRemoveOhneUpdateById(idFach);
		this.updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvStundentafelFach}-Objekt.
	 * @param fach das zu entfernende {@link UvStundentafelFach}-Objekt.
	 */
	public stundentafelFachRemove(fach: UvStundentafelFach): void {
		this.stundentafelFachRemoveOhneUpdate(fach);
		this.updateAll();
	}

	private stundentafelFachRemoveOhneUpdate(fach: UvStundentafelFach): void {
		this.stundentafelFachByIdStundentafelAndAbschnittAndIdFachAndIdStundentafelFach.removeOrException(fach.idStundentafel, fach.abschnitt, fach.idFach, fach.id);
	}

	/**
	 * Entfernt alle {@link UvStundentafelFach}-Objekte.
	 * @param listFaecher Die Liste der zu entfernenden {@link UvStundentafelFach}-Objekte.
	 */
	public stundentafelFachRemoveAll(listFaecher: Collection<UvStundentafelFach>): void {
		this.stundentafelFachRemoveAllOhneUpdate(listFaecher);
		this.updateAll();
	}

	private stundentafelFachRemoveAllOhneUpdate(listFaecher: Collection<UvStundentafelFach>): void {
		const setFaecher: JavaSet<UvStundentafelFach> | null = new HashSet<UvStundentafelFach>(listFaecher);
		for (const fach of setFaecher) {
			this.stundentafelFachRemoveOhneUpdate(fach);
		}
	}

	/**
	 * Entfernt alle {@link UvStundentafelFach}-Objekte mit den angegebenen IDs.
	 * @param listIds Die Liste der IDs der zu entfernenden {@link UvStundentafelFach}-Objekte.
	 */
	public stundentafelFachRemoveAllById(listIds: List<number>): void {
		for (const id of listIds) {
			this.stundentafelFachRemoveOhneUpdateById(id);
		}
		this.updateAll();
	}

	private updateFachMenge(): void {
		this.fachMenge.clear();
		this.fachMenge.addAll(this.fachById.values());
		this.fachMenge.sort(this.compFach);
	}

	/**
	 * Fügt ein {@link UvFach}-Objekt hinzu.
	 * @param fach Das {@link UvFach}-Objekt, welches hinzugefügt werden soll.
	 */
	public fachAdd(fach: UvFach): void {
		this.fachAddAll(ListUtils.create1(fach));
	}

	private fachAddOhneUpdate(fach: UvFach): void {
		this.fachAddAllOhneUpdate(ListUtils.create1(fach));
	}

	/**
	 * Fügt alle {@link UvFach}-Objekte hinzu.
	 * @param listFaecher Die Menge der {@link UvFach}-Objekte, welche hinzugefügt werden soll.
	 */
	public fachAddAll(listFaecher: Collection<UvFach>): void {
		this.fachAddAllOhneUpdate(listFaecher);
		this.updateAll();
	}

	private fachAddAllOhneUpdate(list: Collection<UvFach>): void {
		const setOfIDs: HashSet<number> = new HashSet<number>();
		for (const fach of list) {
			UvManager.fachCheck(fach);
			DeveloperNotificationException.ifTrue("fachAddAllOhneUpdate: ID=" + fach.id + " existiert bereits!", this.fachById.containsKey(fach.id));
			DeveloperNotificationException.ifTrue("fachAddAllOhneUpdate: ID=" + fach.id + " doppelt in der Liste!", !setOfIDs.add(fach.id));
		}
		for (const fach of list) {
			DeveloperNotificationException.ifMapPutOverwrites(this.fachById, fach.id, fach);
		}
	}

	private static fachCheck(fach: UvFach): void {
		DeveloperNotificationException.ifInvalidID("fach.id", fach.id);
		DeveloperNotificationException.ifInvalidID("fach.idFach", fach.idFach);
		DeveloperNotificationException.ifFalse(JavaString.format("Gültigkeitsbeginn von UvFach mit ID %d nicht gültig.", fach.id), DateUtils.isValidDate(fach.gueltigVon));
		DeveloperNotificationException.ifFalse(JavaString.format("Gültigkeitsende von UvFach mit ID %d nicht gültig.", fach.id), (fach.gueltigBis === null) || DateUtils.isValidDate(fach.gueltigBis));
	}

	/**
	 * Liefert das {@link UvFach}-Objekt zur angegebenen ID
	 * @param idFach die ID des Fachs
	 * @return das {@link UvFach}
	 */
	public fachGetByIdOrException(idFach: number): UvFach {
		return DeveloperNotificationException.ifMapGetIsNull(this.fachById, idFach);
	}

	/**
	 * Liefert das zur ID gehörende {@link UvFach}-Objekt direkt aus dem Index oder {@code null}.
	 *
	 * @param idFach die ID des gesuchten Objekts
	 * @return das gespeicherte Objekt oder {@code null}, falls es nicht vorhanden ist
	 */
	public fachGetByIdOrNull(idFach: number): UvFach | null {
		return this.fachById.get(idFach);
	}

	/**
	 * Liefert eine Liste aller {@link UvFach}-Objekte. <br>
	 * @return eine Liste aller {@link UvFach}-Objekte.
	 */
	public fachGetMengeAsList(): List<UvFach> {
		return new ArrayList<UvFach>(this.fachMenge);
	}

	/**
	 * Aktualisiert das vorhandene {@link UvFach}-Objekt durch das neue Objekt.
	 * @param faecher Die neuen {@link UvFach}-Objekte.
	 */
	public fachAllPatchAttributes(faecher: Collection<UvFach>): void {
		this.fachAllPatchAttributesOhneUpdate(faecher);
		this.updateAll();
	}

	/**
	 * Aktualisiert das vorhandene {@link UvFach}-Objekt durch das neue Objekt.
	 * @param fach das neue {@link UvFach}-Objekt.
	 */
	public fachPatchAttributes(fach: UvFach): void {
		this.fachRemoveOhneUpdate(fach);
		this.fachAddOhneUpdate(fach);
		this.updateAll();
	}

	private fachAllPatchAttributesOhneUpdate(faecher: Collection<UvFach>): void {
		this.fachRemoveAllOhneUpdate(faecher);
		this.fachAddAllOhneUpdate(faecher);
	}

	private fachRemoveOhneUpdateById(idFach: number): void {
		DeveloperNotificationException.ifMapRemoveFailes(this.fachById, idFach);
	}

	/**
	 * Entfernt ein existierendes {@link UvFach}-Objekt.
	 * @param idFach Die ID des {@link UvFach}-Objekts.
	 */
	public fachRemoveById(idFach: number): void {
		this.fachRemoveOhneUpdateById(idFach);
		this.updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvFach}-Objekt.
	 * @param fach das zu entfernende {@link UvFach}-Objekt.
	 */
	public fachRemove(fach: UvFach): void {
		this.fachRemoveOhneUpdate(fach);
		this.updateAll();
	}

	private fachRemoveOhneUpdate(fach: UvFach): void {
		this.fachRemoveOhneUpdateById(fach.id);
	}

	/**
	 * Entfernt alle {@link UvFach}-Objekte.
	 * @param listFaecher Die Liste der zu entfernenden {@link UvFach}-Objekte.
	 */
	public fachRemoveAll(listFaecher: Collection<UvFach>): void {
		this.fachRemoveAllOhneUpdate(listFaecher);
		this.updateAll();
	}

	private fachRemoveAllOhneUpdate(listFaecher: Collection<UvFach>): void {
		const setFaecher: JavaSet<UvFach> | null = new HashSet<UvFach>(listFaecher);
		for (const fach of setFaecher) {
			this.fachRemoveOhneUpdateById(fach.id);
		}
	}

	/**
	 * Entfernt alle {@link UvFach}-Objekte mit den angegebenen IDs.
	 * @param listIds Die Liste der IDs der zu entfernenden {@link UvFach}-Objekte.
	 */
	public fachRemoveAllById(listIds: List<number>): void {
		for (const id of listIds) {
			this.fachRemoveOhneUpdateById(id);
		}
		this.updateAll();
	}

	private updateZeitrasterMenge(): void {
		this.zeitrasterMenge.clear();
		this.zeitrasterMenge.addAll(this.zeitrasterById.values());
		this.zeitrasterMenge.sort(this.compZeitraster);
	}

	/**
	 * Fügt ein {@link UvZeitraster}-Objekt hinzu.
	 * @param zeitraster Das {@link UvZeitraster}-Objekt, welches hinzugefügt werden soll.
	 */
	public zeitrasterAdd(zeitraster: UvZeitraster): void {
		this.zeitrasterAddAll(ListUtils.create1(zeitraster));
	}

	private zeitrasterAddOhneUpdate(zeitraster: UvZeitraster): void {
		this.zeitrasterAddAllOhneUpdate(ListUtils.create1(zeitraster));
	}

	/**
	 * Fügt alle {@link UvZeitraster}-Objekte hinzu.
	 * @param listZeitraster Die Menge der {@link UvZeitraster}-Objekte, welche hinzugefügt werden soll.
	 */
	public zeitrasterAddAll(listZeitraster: Collection<UvZeitraster>): void {
		this.zeitrasterAddAllOhneUpdate(listZeitraster);
		this.updateAll();
	}

	private zeitrasterAddAllOhneUpdate(list: Collection<UvZeitraster>): void {
		const setOfIDs: HashSet<number> = new HashSet<number>();
		for (const zeitraster of list) {
			UvManager.zeitrasterCheck(zeitraster);
			DeveloperNotificationException.ifTrue("zeitrasterAddAllOhneUpdate: ID=" + zeitraster.id + " existiert bereits!", this.zeitrasterById.containsKey(zeitraster.id));
			DeveloperNotificationException.ifTrue("zeitrasterAddAllOhneUpdate: ID=" + zeitraster.id + " doppelt in der Liste!", !setOfIDs.add(zeitraster.id));
		}
		for (const zeitraster of list) {
			DeveloperNotificationException.ifMapPutOverwrites(this.zeitrasterById, zeitraster.id, zeitraster);
		}
	}

	private static zeitrasterCheck(zeitraster: UvZeitraster): void {
		DeveloperNotificationException.ifInvalidID("zeitraster.id", zeitraster.id);
		DeveloperNotificationException.ifTrue(JavaString.format("Bezeichnung von UvZeitraster mit ID %d darf nicht leer sein.", zeitraster.id), (zeitraster.bezeichnung === null) || JavaString.isBlank(zeitraster.bezeichnung));
		DeveloperNotificationException.ifFalse(JavaString.format("Gültigkeitsbeginn von UvZeitraster mit ID %d nicht gültig.", zeitraster.id), DateUtils.isValidDate(zeitraster.gueltigVon));
		DeveloperNotificationException.ifFalse(JavaString.format("Gültigkeitsende von UvZeitraster mit ID %d nicht gültig.", zeitraster.id), (zeitraster.gueltigBis === null) || DateUtils.isValidDate(zeitraster.gueltigBis));
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvZeitraster}-Objekt. <br>
	 * @param idZeitraster Die ID des angefragten-Objektes.
	 * @return das zur ID zugehörige {@link UvZeitraster}-Objekt.
	 */
	public zeitrasterGetByIdOrException(idZeitraster: number): UvZeitraster {
		return DeveloperNotificationException.ifMapGetIsNull(this.zeitrasterById, idZeitraster);
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvZeitraster}-Objekt oder {@code null}. <br>
	 * @param idZeitraster Die ID des angefragten-Objektes.
	 * @return das zur ID zugehörige {@link UvZeitraster}-Objekt oder {@code null}.
	 */
	public zeitrasterGetByIdOrNull(idZeitraster: number): UvZeitraster | null {
		return this.zeitrasterById.get(idZeitraster);
	}

	/**
	 * Liefert eine Liste aller {@link UvZeitraster}-Objekte. <br>
	 * @return eine Liste aller {@link UvZeitraster}-Objekte.
	 */
	public zeitrasterGetMengeAsList(): List<UvZeitraster> {
		return new ArrayList<UvZeitraster>(this.zeitrasterMenge);
	}

	/**
	 * Aktualisiert das vorhandene {@link UvZeitraster}-Objekt durch das neue Objekt.
	 * @param listZeitraster Die neuen {@link UvZeitraster}-Objekte.
	 */
	public zeitrasterAllPatchAttributes(listZeitraster: Collection<UvZeitraster>): void {
		this.zeitrasterAllPatchAttributesOhneUpdate(listZeitraster);
		this.updateAll();
	}

	private zeitrasterAllPatchAttributesOhneUpdate(listZeitraster: Collection<UvZeitraster>): void {
		this.zeitrasterRemoveAllOhneUpdate(listZeitraster);
		this.zeitrasterAddAllOhneUpdate(listZeitraster);
	}

	private zeitrasterRemoveOhneUpdateById(idZeitraster: number): void {
		DeveloperNotificationException.ifMapRemoveFailes(this.zeitrasterById, idZeitraster);
	}

	/**
	 * Entfernt ein existierendes {@link UvZeitraster}-Objekt.
	 * @param idZeitraster Die ID des {@link UvZeitraster}-Objekts.
	 */
	public zeitrasterRemoveById(idZeitraster: number): void {
		this.zeitrasterRemoveOhneUpdateById(idZeitraster);
		this.updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvZeitraster}-Objekt.
	 * @param zeitraster das zu entfernende {@link UvZeitraster}-Objekt.
	 */
	public zeitrasterRemove(zeitraster: UvZeitraster): void {
		this.zeitrasterRemoveOhneUpdate(zeitraster);
		this.updateAll();
	}

	private zeitrasterRemoveOhneUpdate(zeitraster: UvZeitraster): void {
		this.zeitrasterRemoveOhneUpdateById(zeitraster.id);
	}

	/**
	 * Entfernt alle {@link UvZeitraster}-Objekte.
	 * @param listZeitraster Die Liste der zu entfernenden {@link UvZeitraster}-Objekte.
	 */
	public zeitrasterRemoveAll(listZeitraster: Collection<UvZeitraster>): void {
		this.zeitrasterRemoveAllOhneUpdate(listZeitraster);
		this.updateAll();
	}

	private zeitrasterRemoveAllOhneUpdate(listZeitraster: Collection<UvZeitraster>): void {
		const setZeitraster: JavaSet<UvZeitraster> | null = new HashSet<UvZeitraster>(listZeitraster);
		for (const zeitraster of setZeitraster) {
			this.zeitrasterRemoveOhneUpdateById(zeitraster.id);
		}
	}

	/**
	 * Entfernt alle {@link UvZeitraster}-Objekte mit den angegebenen IDs.
	 * @param listIds Die Liste der IDs der zu entfernenden {@link UvZeitraster}-Objekte.
	 */
	public zeitrasterRemoveAllById(listIds: List<number>): void {
		for (const id of listIds) {
			this.zeitrasterRemoveOhneUpdateById(id);
		}
		this.updateAll();
	}

	private updateZeitrasterEintragMenge(): void {
		this.zeitrasterEintragMenge.clear();
		this.zeitrasterEintragMenge.addAll(this.zeitrasterEintragByIdZeitrasterAndIdZeitrasterEintrag.getAllValues());
		this.zeitrasterEintragMenge.sort(this.compZeitrasterEintrag);
	}

	/**
	 * Fügt ein {@link UvZeitrasterEintrag}-Objekt hinzu.
	 * @param eintrag Das {@link UvZeitrasterEintrag}-Objekt, welches hinzugefügt werden soll.
	 */
	public zeitrasterEintragAdd(eintrag: UvZeitrasterEintrag): void {
		this.zeitrasterEintragAddAll(ListUtils.create1(eintrag));
	}

	private zeitrasterEintragAddOhneUpdate(eintrag: UvZeitrasterEintrag): void {
		this.zeitrasterEintragAddAllOhneUpdate(ListUtils.create1(eintrag));
	}

	/**
	 * Fügt alle {@link UvZeitrasterEintrag}-Objekte hinzu.
	 * @param listEintraege Die Menge der {@link UvZeitrasterEintrag}-Objekte, welche hinzugefügt werden soll.
	 */
	public zeitrasterEintragAddAll(listEintraege: Collection<UvZeitrasterEintrag>): void {
		this.zeitrasterEintragAddAllOhneUpdate(listEintraege);
		this.updateAll();
	}

	private zeitrasterEintragAddAllOhneUpdate(list: Collection<UvZeitrasterEintrag>): void {
		const setOfIDs: HashSet<number> = new HashSet<number>();
		for (const eintrag of list) {
			this.zeitrasterEintragCheck(eintrag);
			DeveloperNotificationException.ifTrue("zeitrasterEintragAddAllOhneUpdate: ID=" + eintrag.id + " existiert bereits!", this.zeitrasterEintragByIdZeitrasterAndIdZeitrasterEintrag.containsKey12(eintrag.idZeitraster, eintrag.id));
			DeveloperNotificationException.ifTrue("zeitrasterEintragAddAllOhneUpdate: ID=" + eintrag.id + " doppelt in der Liste!", !setOfIDs.add(eintrag.id));
		}
		for (const eintrag of list) {
			this.zeitrasterEintragByIdZeitrasterAndIdZeitrasterEintrag.addSingle(eintrag.idZeitraster, eintrag.id, eintrag);
			this.zeitrasterEintragMenge.add(eintrag);
		}
	}

	private zeitrasterEintragCheck(eintrag: UvZeitrasterEintrag): void {
		DeveloperNotificationException.ifInvalidID("eintrag.id", eintrag.id);
		DeveloperNotificationException.ifInvalidID("eintrag.idZeitraster", eintrag.idZeitraster);
		if (!this.zeitrasterById.containsKey(eintrag.idZeitraster)) {
			throw new DeveloperNotificationException("zeitrasterEintragCheck: Das UvZeitraster mit der ID " + eintrag.idZeitraster + " existiert nicht.");
		}
		DeveloperNotificationException.ifTrue(JavaString.format("Wochentag von UvZeitrasterEintrag mit ID %d muss zwischen 1 und 7 liegen.", eintrag.id), (eintrag.wochentag < 1) || (eintrag.wochentag > 7));
		DeveloperNotificationException.ifTrue(JavaString.format("Stunde von UvZeitrasterEintrag mit ID %d muss größer als 0 sein.", eintrag.id), eintrag.stunde < 1);
		DeveloperNotificationException.ifTrue(JavaString.format("Beginn von UvZeitrasterEintrag mit ID %d muss >= 0 sein.", eintrag.id), eintrag.beginn < 0);
		DeveloperNotificationException.ifTrue(JavaString.format("Ende von UvZeitrasterEintrag mit ID %d muss > Beginn sein.", eintrag.id), eintrag.ende <= eintrag.beginn);
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvZeitrasterEintrag}-Objekt. <br>
	 * @param idZeitraster Die ID des Zeitrasters.
	 * @param idEintrag Die ID des angefragten Eintrags.
	 * @return das zur ID zugehörige {@link UvZeitrasterEintrag}-Objekt.
	 */
	public zeitrasterEintragGetByIdOrException(idZeitraster: number, idEintrag: number): UvZeitrasterEintrag {
		return this.zeitrasterEintragByIdZeitrasterAndIdZeitrasterEintrag.getSingle12OrException(idZeitraster, idEintrag);
	}

	/**
	 * Liefert den Zeitrastereintrag direkt anhand seiner Datensatz-ID oder {@code null}.
	 *
	 * @param idEintrag die ID des Zeitrastereintrags
	 * @return der gespeicherte Eintrag oder {@code null}, falls er nicht vorhanden ist
	 */
	public zeitrasterEintragGetByIdOrNull(idEintrag: number): UvZeitrasterEintrag | null {
		return this.zeitrasterEintragByIdZeitrasterAndIdZeitrasterEintrag.getSingle2OrNull(idEintrag);
	}

	/**
	 * Liefert eine Liste aller {@link UvZeitrasterEintrag}-Objekte. <br>
	 * @return eine Liste aller {@link UvZeitrasterEintrag}-Objekte.
	 */
	public zeitrasterEintragGetMengeAsList(): List<UvZeitrasterEintrag> {
		return new ArrayList<UvZeitrasterEintrag>(this.zeitrasterEintragMenge);
	}

	/**
	 * Aktualisiert das vorhandene {@link UvZeitrasterEintrag}-Objekt durch das neue Objekt.
	 * @param listEintraege Die neuen {@link UvZeitrasterEintrag}-Objekte.
	 */
	public zeitrasterEintragAllPatchAttributes(listEintraege: Collection<UvZeitrasterEintrag>): void {
		this.zeitrasterEintragAllPatchAttributesOhneUpdate(listEintraege);
		this.updateAll();
	}

	private zeitrasterEintragAllPatchAttributesOhneUpdate(listEintraege: Collection<UvZeitrasterEintrag>): void {
		for (const neu of new HashSet(listEintraege)) {
			const alt: UvZeitrasterEintrag = this.zeitrasterEintragByIdZeitrasterAndIdZeitrasterEintrag.getSingle2OrException(neu.id);
			this.zeitrasterEintragRemoveOhneUpdate(alt);
		}
		this.zeitrasterEintragAddAllOhneUpdate(listEintraege);
	}

	private zeitrasterEintragRemoveOhneUpdateById(idZeitraster: number, idEintrag: number): void {
		const eintrag: UvZeitrasterEintrag = this.zeitrasterEintragGetByIdOrException(idZeitraster, idEintrag);
		this.zeitrasterEintragByIdZeitrasterAndIdZeitrasterEintrag.removeSingleOrException(idZeitraster, idEintrag);
		this.zeitrasterEintragMenge.remove(eintrag);
	}

	/**
	 * Entfernt ein existierendes {@link UvZeitrasterEintrag}-Objekt.
	 * @param idZeitraster Die ID des Zeitrasters.
	 * @param idEintrag Die ID des {@link UvZeitrasterEintrag}-Objekts.
	 */
	public zeitrasterEintragRemoveById(idZeitraster: number, idEintrag: number): void {
		this.zeitrasterEintragRemoveOhneUpdateById(idZeitraster, idEintrag);
		this.updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvZeitrasterEintrag}-Objekt.
	 * @param eintrag das zu entfernende {@link UvZeitrasterEintrag}-Objekt.
	 */
	public zeitrasterEintragRemove(eintrag: UvZeitrasterEintrag): void {
		this.zeitrasterEintragRemoveOhneUpdate(eintrag);
		this.updateAll();
	}

	private zeitrasterEintragRemoveOhneUpdate(eintrag: UvZeitrasterEintrag): void {
		this.zeitrasterEintragRemoveOhneUpdateById(eintrag.idZeitraster, eintrag.id);
	}

	/**
	 * Entfernt alle {@link UvZeitrasterEintrag}-Objekte.
	 * @param listEintraege Die Liste der zu entfernenden {@link UvZeitrasterEintrag}-Objekte.
	 */
	public zeitrasterEintragRemoveAll(listEintraege: Collection<UvZeitrasterEintrag>): void {
		this.zeitrasterEintragRemoveAllOhneUpdate(listEintraege);
		this.updateAll();
	}

	private zeitrasterEintragRemoveAllOhneUpdate(listEintraege: Collection<UvZeitrasterEintrag>): void {
		const setEintraege: JavaSet<UvZeitrasterEintrag> | null = new HashSet<UvZeitrasterEintrag>(listEintraege);
		for (const eintrag of setEintraege) {
			this.zeitrasterEintragRemoveOhneUpdateById(eintrag.idZeitraster, eintrag.id);
		}
	}

	/**
	 * Entfernt alle {@link UvZeitrasterEintrag}-Objekte für ein bestimmtes Zeitraster.
	 * @param idZeitraster Die ID des Zeitrasters.
	 */
	public zeitrasterEintragRemoveAllByZeitraster(idZeitraster: number): void {
		const list: List<UvZeitrasterEintrag> | null = new ArrayList<UvZeitrasterEintrag>(this.zeitrasterEintragByIdZeitrasterAndIdZeitrasterEintrag.get1(idZeitraster));
		this.zeitrasterEintragRemoveAllOhneUpdate(list);
		this.updateAll();
	}

	private updatePlanungsabschnittZeitrasterMenge(): void {
		this.planungsabschnittZeitrasterMenge.clear();
		this.planungsabschnittZeitrasterMenge.addAll(this.planungsabschnittZeitrasterByIdPlanungsabschnittAndIdZeitraster.getAllValues());
		this.planungsabschnittZeitrasterMenge.sort(this.compPlanungsabschnittZeitraster);
	}

	/**
	 * Fügt ein {@link UvPlanungsabschnittZeitraster}-Objekt hinzu.
	 * @param zuordnung Das {@link UvPlanungsabschnittZeitraster}-Objekt, welches hinzugefügt werden soll.
	 */
	public planungsabschnittZeitrasterAdd(zuordnung: UvPlanungsabschnittZeitraster): void {
		this.planungsabschnittZeitrasterAddAll(ListUtils.create1(zuordnung));
	}

	private planungsabschnittZeitrasterAddOhneUpdate(zuordnung: UvPlanungsabschnittZeitraster): void {
		this.planungsabschnittZeitrasterAddAllOhneUpdate(ListUtils.create1(zuordnung));
	}

	/**
	 * Fügt alle {@link UvPlanungsabschnittZeitraster}-Objekte hinzu.
	 * @param listZuordnungen Die Menge der {@link UvPlanungsabschnittZeitraster}-Objekte, welche hinzugefügt werden soll.
	 */
	public planungsabschnittZeitrasterAddAll(listZuordnungen: Collection<UvPlanungsabschnittZeitraster>): void {
		this.planungsabschnittZeitrasterAddAllOhneUpdate(listZuordnungen);
		this.updateAll();
	}

	private planungsabschnittZeitrasterAddAllOhneUpdate(list: Collection<UvPlanungsabschnittZeitraster>): void {
		const setOfIDs: HashSet<UvPlanungsabschnittZeitraster> = new HashSet<UvPlanungsabschnittZeitraster>();
		for (const zuordnung of list) {
			this.planungsabschnittZeitrasterCheck(zuordnung);
			DeveloperNotificationException.ifTrue("planungsabschnittZeitrasterAddAllOhneUpdate: Zuordnung existiert bereits!", this.planungsabschnittZeitrasterByIdPlanungsabschnittAndIdZeitraster.containsKey12(zuordnung.idPlanungsabschnitt, zuordnung.idZeitraster));
			DeveloperNotificationException.ifTrue("planungsabschnittZeitrasterAddAllOhneUpdate: Zuordnung doppelt in der Liste!", !setOfIDs.add(zuordnung));
		}
		for (const zuordnung of list) {
			this.planungsabschnittZeitrasterByIdPlanungsabschnittAndIdZeitraster.addSingle(zuordnung.idPlanungsabschnitt, zuordnung.idZeitraster, zuordnung);
			this.planungsabschnittZeitrasterMenge.add(zuordnung);
		}
	}

	private planungsabschnittZeitrasterCheck(zuordnung: UvPlanungsabschnittZeitraster): void {
		DeveloperNotificationException.ifInvalidID("zuordnung.idPlanungsabschnitt", zuordnung.idPlanungsabschnitt);
		DeveloperNotificationException.ifInvalidID("zuordnung.idZeitraster", zuordnung.idZeitraster);
		if (!this.planungsabschnittById.containsKey(zuordnung.idPlanungsabschnitt)) {
			throw new DeveloperNotificationException("planungsabschnittZeitrasterCheck: Der UvPlanungsabschnitt mit der ID " + zuordnung.idPlanungsabschnitt + " existiert nicht.");
		}
		if (!this.zeitrasterById.containsKey(zuordnung.idZeitraster)) {
			throw new DeveloperNotificationException("planungsabschnittZeitrasterCheck: Das UvZeitraster mit der ID " + zuordnung.idZeitraster + " existiert nicht.");
		}
	}

	/**
	 * Aktualisiert das vorhandene {@link UvPlanungsabschnittZeitraster}-Objekt durch das neue Objekt.
	 * @param pazr Die neuen {@link UvPlanungsabschnittZeitraster}-Objekte.
	 */
	public planungsabschnittZeitrasterAllPatchAttributes(pazr: Collection<UvPlanungsabschnittZeitraster>): void {
		this.planungsabschnittZeitrasterAllPatchAttributesOhneUpdate(pazr);
		this.updateAll();
	}

	private planungsabschnittZeitrasterAllPatchAttributesOhneUpdate(pazr: Collection<UvPlanungsabschnittZeitraster>): void {
		this.planungsabschnittZeitrasterRemoveAllOhneUpdate(pazr);
		this.planungsabschnittZeitrasterAddAllOhneUpdate(pazr);
	}

	/**
	 * Liefert eine Liste aller {@link UvPlanungsabschnittZeitraster}-Objekte. <br>
	 * @return eine Liste aller {@link UvPlanungsabschnittZeitraster}-Objekte.
	 */
	public planungsabschnittZeitrasterGetMengeAsList(): List<UvPlanungsabschnittZeitraster> {
		return new ArrayList<UvPlanungsabschnittZeitraster>(this.planungsabschnittZeitrasterMenge);
	}

	private planungsabschnittZeitrasterRemoveOhneUpdateById(idPlanungsabschnitt: number, idZeitraster: number): void {
		const zuordnung: UvPlanungsabschnittZeitraster = this.planungsabschnittZeitrasterGetByIdOrException(idPlanungsabschnitt, idZeitraster);
		this.planungsabschnittZeitrasterByIdPlanungsabschnittAndIdZeitraster.removeOrException(idPlanungsabschnitt, idZeitraster);
		this.planungsabschnittZeitrasterMenge.remove(zuordnung);
	}

	/**
	 * Entfernt ein existierendes {@link UvPlanungsabschnittZeitraster}-Objekt.
	 * @param idPlanungsabschnitt die ID des Planungsabschnitts.
	 * @param idZeitraster Die ID des Zeitrasters.
	 */
	public planungsabschnittZeitrasterRemoveById(idPlanungsabschnitt: number, idZeitraster: number): void {
		this.planungsabschnittZeitrasterRemoveOhneUpdateById(idPlanungsabschnitt, idZeitraster);
		this.updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvPlanungsabschnittZeitraster}-Objekt.
	 * @param zuordnung das zu entfernende {@link UvPlanungsabschnittZeitraster}-Objekt.
	 */
	public planungsabschnittZeitrasterRemove(zuordnung: UvPlanungsabschnittZeitraster): void {
		this.planungsabschnittZeitrasterRemoveOhneUpdate(zuordnung);
		this.updateAll();
	}

	private planungsabschnittZeitrasterRemoveOhneUpdate(zuordnung: UvPlanungsabschnittZeitraster): void {
		this.planungsabschnittZeitrasterRemoveOhneUpdateById(zuordnung.idPlanungsabschnitt, zuordnung.idZeitraster);
	}

	/**
	 * Entfernt alle {@link UvPlanungsabschnittZeitraster}-Objekte.
	 * @param listZuordnungen Die Liste der zu entfernenden {@link UvPlanungsabschnittZeitraster}-Objekte.
	 */
	public planungsabschnittZeitrasterRemoveAll(listZuordnungen: Collection<UvPlanungsabschnittZeitraster>): void {
		this.planungsabschnittZeitrasterRemoveAllOhneUpdate(listZuordnungen);
		this.updateAll();
	}

	private planungsabschnittZeitrasterRemoveAllOhneUpdate(listZuordnungen: Collection<UvPlanungsabschnittZeitraster>): void {
		const setZuordnungen: JavaSet<UvPlanungsabschnittZeitraster> | null = new HashSet<UvPlanungsabschnittZeitraster>(listZuordnungen);
		for (const zuordnung of setZuordnungen) {
			this.planungsabschnittZeitrasterRemoveOhneUpdateById(zuordnung.idPlanungsabschnitt, zuordnung.idZeitraster);
		}
	}

	/**
	 * Entfernt alle {@link UvPlanungsabschnittZeitraster}-Objekte für einen Planungsabschnitt.
	 * @param idPlanungsabschnitt die ID des {@link UvPlanungsabschnitt}-Objekts.
	 * @param listZeitrasterIds Die Liste der IDs der zu entfernenden Zeitraster.
	 */
	public planungsabschnittZeitrasterRemoveAllById(idPlanungsabschnitt: number, listZeitrasterIds: List<number>): void {
		for (const idZeitraster of listZeitrasterIds) {
			this.planungsabschnittZeitrasterRemoveOhneUpdateById(idPlanungsabschnitt, idZeitraster);
		}
		this.updateAll();
	}

	/**
	 * Entfernt alle {@link UvPlanungsabschnittZeitraster}-Objekte für einen Planungsabschnitt.
	 * @param idPlanungsabschnitt die ID des Planungsabschnitts.
	 */
	public planungsabschnittZeitrasterRemoveAllByPlanungsabschnitt(idPlanungsabschnitt: number): void {
		const list: List<UvPlanungsabschnittZeitraster> | null = new ArrayList<UvPlanungsabschnittZeitraster>(this.planungsabschnittZeitrasterByIdPlanungsabschnittAndIdZeitraster.get1(idPlanungsabschnitt));
		this.planungsabschnittZeitrasterRemoveAllOhneUpdate(list);
		this.updateAll();
	}

	private updatePlanungsabschnittSchuelerMenge(): void {
		this.planungsabschnittSchuelerMenge.clear();
		this.planungsabschnittSchuelerMenge.addAll(this.planungsabschnittSchuelerByIdPlanungsabschnittAndIdJahrgangAndIdKlasseAndIdSchueler.getAllValues());
		this.planungsabschnittSchuelerMenge.sort(this.compPlanungsabschnittSchueler);
	}

	/**
	 * Fügt ein {@link UvPlanungsabschnittSchueler}-Objekt hinzu.
	 * @param zuordnung Das {@link UvPlanungsabschnittSchueler}-Objekt, welches hinzugefügt werden soll.
	 */
	public planungsabschnittSchuelerAdd(zuordnung: UvPlanungsabschnittSchueler): void {
		this.planungsabschnittSchuelerAddAll(ListUtils.create1(zuordnung));
	}

	private planungsabschnittSchuelerAddOhneUpdate(zuordnung: UvPlanungsabschnittSchueler): void {
		this.planungsabschnittSchuelerAddAllOhneUpdate(ListUtils.create1(zuordnung));
	}

	/**
	 * Fügt alle {@link UvPlanungsabschnittSchueler}-Objekte hinzu.
	 * @param listZuordnungen Die Menge der {@link UvPlanungsabschnittSchueler}-Objekte, welche hinzugefügt werden soll.
	 */
	public planungsabschnittSchuelerAddAll(listZuordnungen: Collection<UvPlanungsabschnittSchueler>): void {
		this.planungsabschnittSchuelerAddAllOhneUpdate(listZuordnungen);
		this.updateAll();
	}

	private planungsabschnittSchuelerAddAllOhneUpdate(list: Collection<UvPlanungsabschnittSchueler>): void {
		const setOfIDs: HashSet<UvPlanungsabschnittSchueler> = new HashSet<UvPlanungsabschnittSchueler>();
		for (const zuordnung of list) {
			this.planungsabschnittSchuelerCheck(zuordnung);
			DeveloperNotificationException.ifTrue("planungsabschnittSchuelerAddAllOhneUpdate: Zuordnung existiert bereits!", this.planungsabschnittSchuelerByIdPlanungsabschnittAndIdJahrgangAndIdKlasseAndIdSchueler.containsKey14(zuordnung.idPlanungsabschnitt, zuordnung.idSchueler));
			DeveloperNotificationException.ifTrue("planungsabschnittSchuelerAddAllOhneUpdate: Zuordnung doppelt in der Liste!", !setOfIDs.add(zuordnung));
		}
		for (const zuordnung of list) {
			this.planungsabschnittSchuelerByIdPlanungsabschnittAndIdJahrgangAndIdKlasseAndIdSchueler.addSingle(zuordnung.idPlanungsabschnitt, zuordnung.idJahrgang, (zuordnung.idKlasse !== null) ? zuordnung.idKlasse : -1, zuordnung.idSchueler, zuordnung);
			this.planungsabschnittSchuelerMenge.add(zuordnung);
		}
	}

	private planungsabschnittSchuelerCheck(zuordnung: UvPlanungsabschnittSchueler): void {
		DeveloperNotificationException.ifInvalidID("zuordnung.idPlanungsabschnitt", zuordnung.idPlanungsabschnitt);
		DeveloperNotificationException.ifInvalidID("zuordnung.idSchueler", zuordnung.idSchueler);
		if (!this.planungsabschnittById.containsKey(zuordnung.idPlanungsabschnitt)) {
			throw new DeveloperNotificationException("planungsabschnittSchuelerCheck: Der UvPlanungsabschnitt mit der ID " + zuordnung.idPlanungsabschnitt + " existiert nicht.");
		}
	}

	/**
	 * Aktualisiert die vorhandenen {@link UvPlanungsabschnittSchueler}-Objekte durch die neuen Objekte.
	 * @param pas Die neuen {@link UvPlanungsabschnittSchueler}-Objekte.
	 */
	public planungsabschnittSchuelerAllPatchAttributes(pas: Collection<UvPlanungsabschnittSchueler>): void {
		this.planungsabschnittSchuelerAllPatchAttributesOhneUpdate(pas);
		this.updateAll();
	}

	private planungsabschnittSchuelerAllPatchAttributesOhneUpdate(pas: Collection<UvPlanungsabschnittSchueler>): void {
		for (const neu of new HashSet(pas)) {
			const alt: UvPlanungsabschnittSchueler = this.planungsabschnittSchuelerGetByIdOrException(neu.idPlanungsabschnitt, neu.idSchueler);
			this.planungsabschnittSchuelerByIdPlanungsabschnittAndIdJahrgangAndIdKlasseAndIdSchueler.removeOrException(alt.idPlanungsabschnitt, alt.idJahrgang, (alt.idKlasse === null) ? -1 : alt.idKlasse, alt.idSchueler);
			this.planungsabschnittSchuelerMenge.remove(alt);
		}
		this.planungsabschnittSchuelerAddAllOhneUpdate(pas);
	}

	/**
	 * Liefert eine Liste aller {@link UvPlanungsabschnittSchueler}-Objekte. <br>
	 * @return eine Liste aller {@link UvPlanungsabschnittSchueler}-Objekte.
	 */
	public planungsabschnittSchuelerGetMengeAsList(): List<UvPlanungsabschnittSchueler> {
		return new ArrayList<UvPlanungsabschnittSchueler>(this.planungsabschnittSchuelerMenge);
	}

	private planungsabschnittSchuelerRemoveOhneUpdateById(idPlanungsabschnitt: number, idSchueler: number): void {
		const pas: UvPlanungsabschnittSchueler = this.planungsabschnittSchuelerGetByIdOrException(idPlanungsabschnitt, idSchueler);
		this.planungsabschnittSchuelerByIdPlanungsabschnittAndIdJahrgangAndIdKlasseAndIdSchueler.removeOrException(idPlanungsabschnitt, pas.idJahrgang, (pas.idKlasse !== null) ? pas.idKlasse : -1, idSchueler);
		this.planungsabschnittSchuelerMenge.remove(pas);
		const sgs: List<UvSchuelergruppeSchueler> = this.schuelergruppeSchuelerByIdPlanungsabschnittAndIdSchuelergruppeAndIdSchueler.get3(pas.idSchueler);
		this.schuelergruppeSchuelerRemoveAllOhneUpdate(sgs);
	}

	/**
	 * Entfernt ein existierendes {@link UvPlanungsabschnittSchueler}-Objekt.
	 * @param idPlanungsabschnitt die ID des Planungsabschnitts.
	 * @param idSchueler Die ID des Schülers.
	 */
	public planungsabschnittSchuelerRemoveById(idPlanungsabschnitt: number, idSchueler: number): void {
		this.planungsabschnittSchuelerRemoveOhneUpdateById(idPlanungsabschnitt, idSchueler);
		this.updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvPlanungsabschnittSchueler}-Objekt.
	 * @param zuordnung das zu entfernende {@link UvPlanungsabschnittSchueler}-Objekt.
	 */
	public planungsabschnittSchuelerRemove(zuordnung: UvPlanungsabschnittSchueler): void {
		this.planungsabschnittSchuelerRemoveOhneUpdate(zuordnung);
		this.updateAll();
	}

	private planungsabschnittSchuelerRemoveOhneUpdate(zuordnung: UvPlanungsabschnittSchueler): void {
		this.planungsabschnittSchuelerRemoveOhneUpdateById(zuordnung.idPlanungsabschnitt, zuordnung.idSchueler);
	}

	/**
	 * Entfernt alle {@link UvPlanungsabschnittSchueler}-Objekte.
	 * @param listZuordnungen Die Liste der zu entfernenden {@link UvPlanungsabschnittSchueler}-Objekte.
	 */
	public planungsabschnittSchuelerRemoveAll(listZuordnungen: Collection<UvPlanungsabschnittSchueler>): void {
		this.planungsabschnittSchuelerRemoveAllOhneUpdate(listZuordnungen);
		this.updateAll();
	}

	private planungsabschnittSchuelerRemoveAllOhneUpdate(listZuordnungen: Collection<UvPlanungsabschnittSchueler>): void {
		const setZuordnungen: JavaSet<UvPlanungsabschnittSchueler> | null = new HashSet<UvPlanungsabschnittSchueler>(listZuordnungen);
		for (const zuordnung of setZuordnungen) {
			this.planungsabschnittSchuelerRemoveOhneUpdateById(zuordnung.idPlanungsabschnitt, zuordnung.idSchueler);
		}
	}

	/**
	 * Entfernt alle {@link UvPlanungsabschnittSchueler}-Objekte für einen Planungsabschnitt.
	 * @param idPlanungsabschnitt die ID des {@link UvPlanungsabschnitt}-Objekts.
	 * @param listSchuelerIds Die Liste der IDs der zu entfernenden Schüler.
	 */
	public planungsabschnittSchuelerRemoveAllById(idPlanungsabschnitt: number, listSchuelerIds: List<number>): void {
		for (const idSchueler of listSchuelerIds) {
			this.planungsabschnittSchuelerRemoveOhneUpdateById(idPlanungsabschnitt, idSchueler);
		}
		this.updateAll();
	}

	/**
	 * Entfernt alle {@link UvPlanungsabschnittSchueler}-Objekte für einen Planungsabschnitt.
	 * @param idPlanungsabschnitt die ID des Planungsabschnitts.
	 */
	public planungsabschnittSchuelerRemoveAllByPlanungsabschnitt(idPlanungsabschnitt: number): void {
		const list: List<UvPlanungsabschnittSchueler> | null = new ArrayList<UvPlanungsabschnittSchueler>(this.planungsabschnittSchuelerByIdPlanungsabschnittAndIdJahrgangAndIdKlasseAndIdSchueler.get1(idPlanungsabschnitt));
		this.planungsabschnittSchuelerRemoveAllOhneUpdate(list);
		this.updateAll();
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvPlanungsabschnittSchueler}-Objekt. <br>
	 * @param idPlanungsabschnitt die ID des Planungsabschnitts.
	 * @param idSchueler die ID des Schülers.
	 * @return das zur ID zugehörige {@link UvPlanungsabschnittSchueler}-Objekt.
	 */
	public planungsabschnittSchuelerGetByIdOrException(idPlanungsabschnitt: number, idSchueler: number): UvPlanungsabschnittSchueler {
		return this.planungsabschnittSchuelerByIdPlanungsabschnittAndIdJahrgangAndIdKlasseAndIdSchueler.getSingle14OrException(idPlanungsabschnitt, idSchueler);
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvPlanungsabschnittSchueler}-Objekt oder {@code null}.
	 *
	 * @param idPlanungsabschnitt die ID des Planungsabschnitts
	 * @param idSchueler die ID des Schülers
	 * @return das zugehörige {@link UvPlanungsabschnittSchueler}-Objekt oder {@code null}
	 */
	public planungsabschnittSchuelerGetByIdOrNull(idPlanungsabschnitt: number, idSchueler: number): UvPlanungsabschnittSchueler | null {
		return this.planungsabschnittSchuelerByIdPlanungsabschnittAndIdJahrgangAndIdKlasseAndIdSchueler.getSingle14OrNull(idPlanungsabschnitt, idSchueler);
	}

	/**
	 * Fügt ein {@link UvSchuelergruppe}-Objekt hinzu.
	 * @param schuelergruppe Das {@link UvSchuelergruppe}-Objekt, welches hinzugefügt werden soll.
	 */
	public schuelergruppeAdd(schuelergruppe: UvSchuelergruppe): void {
		this.schuelergruppeAddAll(ListUtils.create1(schuelergruppe));
	}

	private schuelergruppeAddOhneUpdate(schuelergruppe: UvSchuelergruppe): void {
		this.schuelergruppeAddAllOhneUpdate(ListUtils.create1(schuelergruppe));
	}

	/**
	 * Fügt alle {@link UvSchuelergruppe}-Objekte hinzu.
	 * @param listSchuelergruppen Die Menge der {@link UvSchuelergruppe}-Objekte, welche hinzugefügt werden soll.
	 */
	public schuelergruppeAddAll(listSchuelergruppen: Collection<UvSchuelergruppe>): void {
		this.schuelergruppeAddAllOhneUpdate(listSchuelergruppen);
		this.updateAll();
	}

	private schuelergruppeAddAllOhneUpdate(list: Collection<UvSchuelergruppe>): void {
		const setOfIDs: HashSet<number> = new HashSet<number>();
		for (const schuelergruppe of list) {
			this.schuelergruppeCheck(schuelergruppe);
			DeveloperNotificationException.ifTrue("schuelergruppeAddAllOhneUpdate: Schuelergruppe existiert bereits!", this.schuelergruppeByIdPlanungsabschnittAndIdSchuelergruppe.containsKey12(schuelergruppe.idPlanungsabschnitt, schuelergruppe.id));
			DeveloperNotificationException.ifTrue("schuelergruppeAddAllOhneUpdate: ID=" + schuelergruppe.id + " doppelt in der Liste!", !setOfIDs.add(schuelergruppe.id));
		}
		for (const schuelergruppe of list) {
			this.schuelergruppeByIdPlanungsabschnittAndIdSchuelergruppe.addSingle(schuelergruppe.idPlanungsabschnitt, schuelergruppe.id, schuelergruppe);
			this.schuelergruppeMenge.add(schuelergruppe);
		}
	}

	/**
	 * Ersetzt das vorhandene {@link UvSchuelergruppe}-Objekt anhand seiner ID ohne Löschkaskade
	 * und baut die Indizes neu auf. Das übergebene Objekt muss die vollständigen neuen Daten
	 * enthalten; das bisherige Manager-Objekt darf zuvor nicht verändert werden.
	 *
	 * @param schuelergruppe die neuen Daten mit unveränderter ID und unverändertem Planungsabschnitt
	 */
	public schuelergruppePatchAttributes(schuelergruppe: UvSchuelergruppe): void {
		const alt: UvSchuelergruppe = this.schuelergruppeGetByIdOrException(schuelergruppe.id);
		this.schuelergruppeCheck(schuelergruppe);
		DeveloperNotificationException.ifTrue("schuelergruppePatchAttributes: Der Planungsabschnitt darf nicht geändert werden.", alt.idPlanungsabschnitt !== schuelergruppe.idPlanungsabschnitt);
		this.schuelergruppeByIdPlanungsabschnittAndIdSchuelergruppe.removeSingleOrException(alt.idPlanungsabschnitt, alt.id);
		this.schuelergruppeByIdPlanungsabschnittAndIdSchuelergruppe.addSingle(schuelergruppe.idPlanungsabschnitt, schuelergruppe.id, schuelergruppe);
		this.updateAll();
	}

	private schuelergruppeCheck(schuelergruppe: UvSchuelergruppe): void {
		DeveloperNotificationException.ifInvalidID("schuelergruppe.id", schuelergruppe.id);
		DeveloperNotificationException.ifInvalidID("schuelergruppe.idPlanungsabschnitt", schuelergruppe.idPlanungsabschnitt);
		if (!this.planungsabschnittById.containsKey(schuelergruppe.idPlanungsabschnitt)) {
			throw new DeveloperNotificationException("schuelergruppeCheck: Der UvPlanungsabschnitt mit der ID " + schuelergruppe.idPlanungsabschnitt + " existiert nicht.");
		}
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvSchuelergruppe}-Objekt. <br>
	 * @param idSchuelergruppe Die ID des angefragten-Objektes.
	 * @return das zur ID zugehörige {@link UvSchuelergruppe}-Objekt.
	 */
	public schuelergruppeGetByIdOrException(idSchuelergruppe: number): UvSchuelergruppe {
		return this.schuelergruppeByIdPlanungsabschnittAndIdSchuelergruppe.getSingle2OrException(idSchuelergruppe);
	}

	/**
	 * Liefert das zur ID gehörende {@link UvSchuelergruppe}-Objekt direkt aus dem Index oder {@code null}.
	 *
	 * @param idSchuelergruppe die ID des gesuchten Objekts
	 * @return das gespeicherte Objekt oder {@code null}, falls es nicht vorhanden ist
	 */
	public schuelergruppeGetByIdOrNull(idSchuelergruppe: number): UvSchuelergruppe | null {
		return this.schuelergruppeByIdPlanungsabschnittAndIdSchuelergruppe.getSingle2OrNull(idSchuelergruppe);
	}

	/**
	 * Liefert eine Liste aller {@link UvSchuelergruppe}-Objekte. <br>
	 * @return eine Liste aller {@link UvSchuelergruppe}-Objekte.
	 */
	public schuelergruppeGetMengeAsList(): List<UvSchuelergruppe> {
		return new ArrayList<UvSchuelergruppe>(this.schuelergruppeMenge);
	}

	private schuelergruppeRemoveOhneUpdateById(idPlanungsabschnitt: number, idSchuelergruppe: number): void {
		const schuelergruppe: UvSchuelergruppe = this.schuelergruppeByIdPlanungsabschnittAndIdSchuelergruppe.getSingle12OrException(idPlanungsabschnitt, idSchuelergruppe);
		this.schuelergruppeByIdPlanungsabschnittAndIdSchuelergruppe.removeSingleOrException(idPlanungsabschnitt, idSchuelergruppe);
		this.schuelergruppeMenge.remove(schuelergruppe);
	}

	/**
	 * Entfernt ein existierendes {@link UvSchuelergruppe}-Objekt.
	 * @param idPlanungsabschnitt die ID des Planungsabschnitts.
	 * @param idSchuelergruppe Die ID der Schülergruppe.
	 */
	public schuelergruppeRemoveById(idPlanungsabschnitt: number, idSchuelergruppe: number): void {
		this.schuelergruppeRemoveOhneUpdateById(idPlanungsabschnitt, idSchuelergruppe);
		this.updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvSchuelergruppe}-Objekt.
	 * @param schuelergruppe das zu entfernende {@link UvSchuelergruppe}-Objekt.
	 */
	public schuelergruppeRemove(schuelergruppe: UvSchuelergruppe): void {
		this.schuelergruppeRemoveOhneUpdate(schuelergruppe);
		this.updateAll();
	}

	private schuelergruppeRemoveOhneUpdate(schuelergruppe: UvSchuelergruppe): void {
		this.schuelergruppeRemoveOhneUpdateById(schuelergruppe.idPlanungsabschnitt, schuelergruppe.id);
	}

	/**
	 * Entfernt alle {@link UvSchuelergruppe}-Objekte.
	 * @param listSchuelergruppen Die Liste der zu entfernenden {@link UvSchuelergruppe}-Objekte.
	 */
	public schuelergruppeRemoveAll(listSchuelergruppen: Collection<UvSchuelergruppe>): void {
		this.schuelergruppeRemoveAllOhneUpdate(listSchuelergruppen);
		this.updateAll();
	}

	private schuelergruppeRemoveAllOhneUpdate(listSchuelergruppen: Collection<UvSchuelergruppe>): void {
		const setSchuelergruppen: JavaSet<UvSchuelergruppe> | null = new HashSet<UvSchuelergruppe>(listSchuelergruppen);
		for (const schuelergruppe of setSchuelergruppen) {
			this.schuelergruppeRemoveOhneUpdateById(schuelergruppe.idPlanungsabschnitt, schuelergruppe.id);
		}
	}

	/**
	 * Liefert eine Liste aller {@link UvSchuelergruppe}-Objekte für einen Planungsabschnitt. <br>
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}.
	 * @return eine Liste aller {@link UvSchuelergruppe}-Objekte für den Planungsabschnitt.
	 */
	public schuelergruppeGetMengeByPlanungsabschnitt(planungsabschnitt: UvPlanungsabschnitt): List<UvSchuelergruppe> {
		return this.schuelergruppeByIdPlanungsabschnittAndIdSchuelergruppe.get1(planungsabschnitt.id);
	}

	/**
	 * Fügt ein {@link UvSchuelergruppeSchueler}-Objekt hinzu.
	 * @param zuordnung Das {@link UvSchuelergruppeSchueler}-Objekt, welches hinzugefügt werden soll.
	 */
	public schuelergruppeSchuelerAdd(zuordnung: UvSchuelergruppeSchueler): void {
		this.schuelergruppeSchuelerAddAll(ListUtils.create1(zuordnung));
	}

	private schuelergruppeSchuelerAddOhneUpdate(zuordnung: UvSchuelergruppeSchueler): void {
		this.schuelergruppeSchuelerAddAllOhneUpdate(ListUtils.create1(zuordnung));
	}

	/**
	 * Fügt alle {@link UvSchuelergruppeSchueler}-Objekte hinzu.
	 * @param listZuordnungen Die Menge der {@link UvSchuelergruppeSchueler}-Objekte, welche hinzugefügt werden soll.
	 */
	public schuelergruppeSchuelerAddAll(listZuordnungen: Collection<UvSchuelergruppeSchueler>): void {
		this.schuelergruppeSchuelerAddAllOhneUpdate(listZuordnungen);
		this.updateAll();
	}

	private schuelergruppeSchuelerAddAllOhneUpdate(list: Collection<UvSchuelergruppeSchueler>): void {
		for (const zuordnung of list) {
			this.schuelergruppeSchuelerCheck(zuordnung);
			DeveloperNotificationException.ifTrue("schuelergruppeSchuelerAddAllOhneUpdate: Zuordnung existiert bereits!", this.schuelergruppeSchuelerByIdPlanungsabschnittAndIdSchuelergruppeAndIdSchueler.containsKey123(zuordnung.idPlanungsabschnitt, zuordnung.idSchuelergruppe, zuordnung.idSchueler));
		}
		for (const zuordnung of list) {
			this.schuelergruppeSchuelerByIdPlanungsabschnittAndIdSchuelergruppeAndIdSchueler.addSingle(zuordnung.idPlanungsabschnitt, zuordnung.idSchuelergruppe, zuordnung.idSchueler, zuordnung);
			this.schuelergruppeSchuelerMenge.add(zuordnung);
		}
	}

	private schuelergruppeSchuelerCheck(zuordnung: UvSchuelergruppeSchueler): void {
		DeveloperNotificationException.ifInvalidID("zuordnung.idPlanungsabschnitt", zuordnung.idPlanungsabschnitt);
		DeveloperNotificationException.ifInvalidID("zuordnung.idSchuelergruppe", zuordnung.idSchuelergruppe);
		DeveloperNotificationException.ifInvalidID("zuordnung.idSchueler", zuordnung.idSchueler);
		if (!this.planungsabschnittById.containsKey(zuordnung.idPlanungsabschnitt)) {
			throw new DeveloperNotificationException("schuelergruppeSchuelerCheck: Der UvPlanungsabschnitt mit der ID " + zuordnung.idPlanungsabschnitt + " existiert nicht.");
		}
		if (!this.schuelergruppeByIdPlanungsabschnittAndIdSchuelergruppe.containsKey12(zuordnung.idPlanungsabschnitt, zuordnung.idSchuelergruppe)) {
			throw new DeveloperNotificationException("schuelergruppeSchuelerCheck: Die UvSchuelergruppe mit der ID " + zuordnung.idSchuelergruppe + " existiert nicht.");
		}
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvSchuelergruppeSchueler}-Objekt. <br>
	 * @param idPlanungsabschnitt Die ID des Planungsabschnitts.
	 * @param idSchuelergruppe Die ID der Schülergruppe.
	 * @param idSchueler Die ID des Schülers.
	 * @return das zur ID zugehörige {@link UvSchuelergruppeSchueler}-Objekt.
	 */
	public schuelergruppeSchuelerGetByIdOrException(idPlanungsabschnitt: number, idSchuelergruppe: number, idSchueler: number): UvSchuelergruppeSchueler {
		return this.schuelergruppeSchuelerByIdPlanungsabschnittAndIdSchuelergruppeAndIdSchueler.getSingle123OrException(idPlanungsabschnitt, idSchuelergruppe, idSchueler);
	}

	/**
	 * Liefert eine Liste aller {@link UvSchuelergruppeSchueler}-Objekte. <br>
	 * @return eine Liste aller {@link UvSchuelergruppeSchueler}-Objekte.
	 */
	public schuelergruppeSchuelerGetMengeAsList(): List<UvSchuelergruppeSchueler> {
		return new ArrayList<UvSchuelergruppeSchueler>(this.schuelergruppeSchuelerMenge);
	}

	private schuelergruppeSchuelerRemoveOhneUpdateById(idPlanungsabschnitt: number, idSchuelergruppe: number, idSchueler: number): void {
		this.schuelergruppeSchuelerRemoveOhneUpdate(this.schuelergruppeSchuelerGetByIdOrException(idPlanungsabschnitt, idSchuelergruppe, idSchueler));
	}

	/**
	 * Entfernt ein existierendes {@link UvSchuelergruppeSchueler}-Objekt.
	 * @param idPlanungsabschnitt Die ID des Planungsabschnitts.
	 * @param idSchuelergruppe Die ID der Schülergruppe.
	 * @param idSchueler Die ID des Schülers.
	 */
	public schuelergruppeSchuelerRemoveById(idPlanungsabschnitt: number, idSchuelergruppe: number, idSchueler: number): void {
		this.schuelergruppeSchuelerRemoveOhneUpdateById(idPlanungsabschnitt, idSchuelergruppe, idSchueler);
		this.updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvSchuelergruppeSchueler}-Objekt.
	 * @param zuordnung das zu entfernende {@link UvSchuelergruppeSchueler}-Objekt.
	 */
	public schuelergruppeSchuelerRemove(zuordnung: UvSchuelergruppeSchueler): void {
		this.schuelergruppeSchuelerRemoveOhneUpdate(zuordnung);
		this.updateAll();
	}

	private schuelergruppeSchuelerRemoveOhneUpdate(zuordnung: UvSchuelergruppeSchueler): void {
		this.schuelergruppeSchuelerByIdPlanungsabschnittAndIdSchuelergruppeAndIdSchueler.removeSingleOrException(zuordnung.idPlanungsabschnitt, zuordnung.idSchuelergruppe, zuordnung.idSchueler);
		this.schuelergruppeSchuelerMenge.remove(zuordnung);
	}

	/**
	 * Entfernt alle {@link UvSchuelergruppeSchueler}-Objekte.
	 * @param listZuordnungen Die Liste der zu entfernenden {@link UvSchuelergruppeSchueler}-Objekte.
	 */
	public schuelergruppeSchuelerRemoveAll(listZuordnungen: Collection<UvSchuelergruppeSchueler>): void {
		this.schuelergruppeSchuelerRemoveAllOhneUpdate(listZuordnungen);
		this.updateAll();
	}

	private schuelergruppeSchuelerRemoveAllOhneUpdate(listZuordnungen: Collection<UvSchuelergruppeSchueler>): void {
		const setZuordnungen: JavaSet<UvSchuelergruppeSchueler> | null = new HashSet<UvSchuelergruppeSchueler>(listZuordnungen);
		for (const zuordnung of setZuordnungen) {
			this.schuelergruppeSchuelerRemoveOhneUpdate(zuordnung);
		}
	}

	/**
	 * Liefert eine Liste aller {@link UvSchuelergruppeSchueler}-Objekte für einen Planungsabschnitt. <br>
	 * @param idPlanungsabschnitt die ID des Planungsabschnitts.
	 * @return eine Liste aller {@link UvSchuelergruppeSchueler}-Objekte für den Planungsabschnitt.
	 */
	public schuelergruppeSchuelerGetMengeByPlanungsabschnitt(idPlanungsabschnitt: number): List<UvSchuelergruppeSchueler> {
		return this.schuelergruppeSchuelerByIdPlanungsabschnittAndIdSchuelergruppeAndIdSchueler.get1(idPlanungsabschnitt);
	}

	/**
	 * Liefert eine Liste aller {@link UvSchuelergruppeSchueler}-Objekte für eine Schülergruppe. <br>
	 * @param idPlanungsabschnitt die ID des Planungsabschnitts.
	 * @param idSchuelergruppe die ID der Schülergruppe.
	 * @return eine Liste aller {@link UvSchuelergruppeSchueler}-Objekte für die Schülergruppe.
	 */
	public schuelergruppeSchuelerGetMengeBySchuelergruppe(idPlanungsabschnitt: number, idSchuelergruppe: number): List<UvSchuelergruppeSchueler> {
		return new ArrayList<UvSchuelergruppeSchueler>(this.schuelergruppeSchuelerByIdPlanungsabschnittAndIdSchuelergruppeAndIdSchueler.get12(idPlanungsabschnitt, idSchuelergruppe));
	}

	/**
	 * Fügt ein {@link UvKlasse}-Objekt hinzu.
	 * @param klasse Das {@link UvKlasse}-Objekt, welches hinzugefügt werden soll.
	 */
	public klasseAdd(klasse: UvKlasse): void {
		this.klasseAddAll(ListUtils.create1(klasse));
	}

	private klasseAddOhneUpdate(klasse: UvKlasse): void {
		this.klasseAddAllOhneUpdate(ListUtils.create1(klasse));
	}

	/**
	 * Fügt alle {@link UvKlasse}-Objekte hinzu.
	 * @param listKlassen Die Menge der {@link UvKlasse}-Objekte, welche hinzugefügt werden soll.
	 */
	public klasseAddAll(listKlassen: Collection<UvKlasse>): void {
		this.klasseAddAllOhneUpdate(listKlassen);
		this.updateAll();
	}

	private klasseAddAllOhneUpdate(list: Collection<UvKlasse>): void {
		const setOfIDs: HashSet<number> = new HashSet<number>();
		for (const klasse of list) {
			this.klasseCheck(klasse);
			DeveloperNotificationException.ifTrue("klasseAddAllOhneUpdate: Klasse existiert bereits!", this.klasseByIdPlanungsabschnittAndIdSchuelergruppeAndIdKlasse.containsKey13(klasse.idPlanungsabschnitt, klasse.id));
			DeveloperNotificationException.ifTrue("klasseAddAllOhneUpdate: ID=" + klasse.id + " doppelt in der Liste!", !setOfIDs.add(klasse.id));
		}
		for (const klasse of list) {
			this.klasseByIdPlanungsabschnittAndIdSchuelergruppeAndIdKlasse.addSingle(klasse.idPlanungsabschnitt, klasse.idSchuelergruppe, klasse.id, klasse);
			this.klasseMenge.add(klasse);
		}
	}

	/**
	 * Ersetzt das vorhandene {@link UvKlasse}-Objekt anhand seiner ID ohne Löschkaskade
	 * und baut die Indizes neu auf. Das übergebene Objekt muss die vollständigen neuen Daten
	 * enthalten; das bisherige Manager-Objekt darf zuvor nicht verändert werden.
	 *
	 * @param klasse die neuen Daten mit unveränderter ID und unverändertem Planungsabschnitt
	 */
	public klassePatchAttributes(klasse: UvKlasse): void {
		const alt: UvKlasse = this.klasseGetByIdOrException(klasse.id);
		this.klasseCheck(klasse);
		DeveloperNotificationException.ifTrue("klassePatchAttributes: Der Planungsabschnitt darf nicht geändert werden.", alt.idPlanungsabschnitt !== klasse.idPlanungsabschnitt);
		this.klasseByIdPlanungsabschnittAndIdSchuelergruppeAndIdKlasse.removeSingleOrException(alt.idPlanungsabschnitt, alt.idSchuelergruppe, alt.id);
		this.klasseByIdPlanungsabschnittAndIdSchuelergruppeAndIdKlasse.addSingle(klasse.idPlanungsabschnitt, klasse.idSchuelergruppe, klasse.id, klasse);
		this.updateAll();
	}

	private klasseCheck(klasse: UvKlasse): void {
		DeveloperNotificationException.ifInvalidID("klasse.id", klasse.id);
		DeveloperNotificationException.ifInvalidID("klasse.idPlanungsabschnitt", klasse.idPlanungsabschnitt);
		if (!this.planungsabschnittById.containsKey(klasse.idPlanungsabschnitt)) {
			throw new DeveloperNotificationException("klasseCheck: Der UvPlanungsabschnitt mit der ID " + klasse.idPlanungsabschnitt + " existiert nicht.");
		}
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvKlasse}-Objekt. <br>
	 * @param idKlasse Die ID des angefragten-Objektes.
	 * @return das zur ID zugehörige {@link UvKlasse}-Objekt.
	 */
	public klasseGetByIdOrException(idKlasse: number): UvKlasse {
		return this.klasseByIdPlanungsabschnittAndIdSchuelergruppeAndIdKlasse.getSingle3OrException(idKlasse);
	}

	/**
	 * Liefert das zur ID gehörende {@link UvKlasse}-Objekt direkt aus dem Index oder {@code null}.
	 *
	 * @param idKlasse die ID des gesuchten Objekts
	 * @return das gespeicherte Objekt oder {@code null}, falls es nicht vorhanden ist
	 */
	public klasseGetByIdOrNull(idKlasse: number): UvKlasse | null {
		return this.klasseByIdPlanungsabschnittAndIdSchuelergruppeAndIdKlasse.getSingle3OrNull(idKlasse);
	}

	/**
	 * Liefert eine Liste aller {@link UvKlasse}-Objekte. <br>
	 * @return eine Liste aller {@link UvKlasse}-Objekte.
	 */
	public klasseGetMengeAsList(): List<UvKlasse> {
		return new ArrayList<UvKlasse>(this.klasseMenge);
	}

	private klasseRemoveOhneUpdateById(idPlanungsabschnitt: number, idKlasse: number): void {
		const klasse: UvKlasse = this.klasseGetByIdOrException(idKlasse);
		this.klasseByIdPlanungsabschnittAndIdSchuelergruppeAndIdKlasse.removeSingleOrException(idPlanungsabschnitt, klasse.idSchuelergruppe, idKlasse);
		for (const lerngruppe of new ArrayList(this.lerngruppeByIdKursAndIdKlasseAndIdFach.get2(idKlasse))) {
			this.lerngruppeRemoveOhneUpdateById(idPlanungsabschnitt, lerngruppe.id);
		}
		this.klassenLehrerRemoveByKlasseOhneUpdate(klasse);
		this.klasseMenge.remove(klasse);
	}

	/**
	 * Entfernt ein existierendes {@link UvKlasse}-Objekt.
	 * @param idPlanungsabschnitt die ID des Planungsabschnitts.
	 * @param idKlasse Die ID der Klasse.
	 */
	public klasseRemoveById(idPlanungsabschnitt: number, idKlasse: number): void {
		this.klasseRemoveOhneUpdateById(idPlanungsabschnitt, idKlasse);
		this.updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvKlasse}-Objekt.
	 * @param klasse das zu entfernende {@link UvKlasse}-Objekt.
	 */
	public klasseRemove(klasse: UvKlasse): void {
		this.klasseRemoveOhneUpdate(klasse);
		this.updateAll();
	}

	private klasseRemoveOhneUpdate(klasse: UvKlasse): void {
		this.klasseRemoveOhneUpdateById(klasse.idPlanungsabschnitt, klasse.id);
	}

	/**
	 * Entfernt alle {@link UvKlasse}-Objekte.
	 * @param listKlassen Die Liste der zu entfernenden {@link UvKlasse}-Objekte.
	 */
	public klasseRemoveAll(listKlassen: Collection<UvKlasse>): void {
		this.klasseRemoveAllOhneUpdate(listKlassen);
		this.updateAll();
	}

	private klasseRemoveAllOhneUpdate(listKlassen: Collection<UvKlasse>): void {
		const setKlassen: JavaSet<UvKlasse> | null = new HashSet<UvKlasse>(listKlassen);
		for (const klasse of setKlassen) {
			this.klasseRemoveOhneUpdateById(klasse.idPlanungsabschnitt, klasse.id);
		}
	}

	/**
	 * Liefert eine Liste aller {@link UvKlasse}-Objekte für einen Planungsabschnitt. <br>
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}.
	 * @return eine Liste aller {@link UvKlasse}-Objekte für den Planungsabschnitt.
	 */
	public klasseGetMengeByPlanungsabschnitt(planungsabschnitt: UvPlanungsabschnitt): List<UvKlasse> {
		return this.klasseByIdPlanungsabschnittAndIdSchuelergruppeAndIdKlasse.get1(planungsabschnitt.id);
	}

	/**
	 * Fügt ein {@link UvKlassenLehrer}-Objekt hinzu.
	 * @param zuordnung Das {@link UvKlassenLehrer}-Objekt, welches hinzugefügt werden soll.
	 */
	public klassenLehrerAdd(zuordnung: UvKlassenLehrer): void {
		this.klassenLehrerAddAll(ListUtils.create1(zuordnung));
	}

	private klassenLehrerAddOhneUpdate(zuordnung: UvKlassenLehrer): void {
		this.klassenLehrerAddAllOhneUpdate(ListUtils.create1(zuordnung));
	}

	/**
	 * Fügt alle {@link UvKlassenLehrer}-Objekte hinzu.
	 * @param listZuordnungen Die Menge der {@link UvKlassenLehrer}-Objekte, welche hinzugefügt werden soll.
	 */
	public klassenLehrerAddAll(listZuordnungen: Collection<UvKlassenLehrer>): void {
		this.klassenLehrerAddAllOhneUpdate(listZuordnungen);
		this.updateAll();
	}

	private klassenLehrerAddAllOhneUpdate(list: Collection<UvKlassenLehrer>): void {
		const setOfIDs: HashSet<number> = new HashSet<number>();
		for (const zuordnung of list) {
			this.klassenLehrerCheck(zuordnung);
			DeveloperNotificationException.ifTrue("klassenLehrerAddAllOhneUpdate: Zuordnung existiert bereits!", this.klassenLehrerByIdPlanungsabschnittAndIdKlasseAndIdLehrer.containsKey123(zuordnung.idPlanungsabschnitt, zuordnung.idKlasse, zuordnung.idLehrer));
			DeveloperNotificationException.ifTrue("klassenLehrerAddAllOhneUpdate: ID=" + zuordnung.id + " doppelt in der Liste!", !setOfIDs.add(zuordnung.id));
		}
		for (const zuordnung of list) {
			this.klassenLehrerByIdPlanungsabschnittAndIdKlasseAndIdLehrer.addSingle(zuordnung.idPlanungsabschnitt, zuordnung.idKlasse, zuordnung.idLehrer, zuordnung);
			this.klassenLehrerMenge.add(zuordnung);
		}
	}

	private klassenLehrerCheck(zuordnung: UvKlassenLehrer): void {
		DeveloperNotificationException.ifInvalidID("zuordnung.id", zuordnung.id);
		DeveloperNotificationException.ifInvalidID("zuordnung.idPlanungsabschnitt", zuordnung.idPlanungsabschnitt);
		DeveloperNotificationException.ifInvalidID("zuordnung.idKlasse", zuordnung.idKlasse);
		DeveloperNotificationException.ifInvalidID("zuordnung.idLehrer", zuordnung.idLehrer);
		if (!this.planungsabschnittById.containsKey(zuordnung.idPlanungsabschnitt)) {
			throw new DeveloperNotificationException("klassenLehrerCheck: Der UvPlanungsabschnitt mit der ID " + zuordnung.idPlanungsabschnitt + " existiert nicht.");
		}
		if (!this.lehrerById.containsKey(zuordnung.idLehrer)) {
			throw new DeveloperNotificationException("klassenLehrerCheck: Der UvLehrer mit der ID " + zuordnung.idLehrer + " existiert nicht.");
		}
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvKlassenLehrer}-Objekt. <br>
	 * @param idPlanungsabschnitt Die ID des Planungsabschnitts.
	 * @param idKlasse Die ID der Klasse.
	 * @param idLehrer Die ID des Lehrers.
	 * @return das zur ID zugehörige {@link UvKlassenLehrer}-Objekt.
	 */
	public klassenLehrerGetByIdOrException(idPlanungsabschnitt: number, idKlasse: number, idLehrer: number): UvKlassenLehrer {
		return this.klassenLehrerByIdPlanungsabschnittAndIdKlasseAndIdLehrer.getSingle123OrException(idPlanungsabschnitt, idKlasse, idLehrer);
	}

	/**
	 * Liefert eine Liste aller {@link UvKlassenLehrer}-Objekte. <br>
	 * @return eine Liste aller {@link UvKlassenLehrer}-Objekte.
	 */
	public klassenLehrerGetMengeAsList(): List<UvKlassenLehrer> {
		return new ArrayList<UvKlassenLehrer>(this.klassenLehrerMenge);
	}

	private klassenLehrerRemoveOhneUpdateById(idPlanungsabschnitt: number, idKlasse: number, idLehrer: number): void {
		this.klassenLehrerRemoveOhneUpdate(this.klassenLehrerGetByIdOrException(idPlanungsabschnitt, idKlasse, idLehrer));
	}

	/**
	 * Entfernt ein existierendes {@link UvKlassenLehrer}-Objekt.
	 * @param idPlanungsabschnitt Die ID des Planungsabschnitts.
	 * @param idKlasse Die ID der Klasse.
	 * @param idLehrer Die ID des Lehrers.
	 */
	public klassenLehrerRemoveById(idPlanungsabschnitt: number, idKlasse: number, idLehrer: number): void {
		this.klassenLehrerRemoveOhneUpdateById(idPlanungsabschnitt, idKlasse, idLehrer);
		this.updateAll();
	}

	/**
	 * Entfernt alle {@link UvKlassenLehrer}-Objekte für die angegebene {@link UvKlasse}.
	 * @param klasse die {@link UvKlasse}, deren Klassenlehrer entfernt werden sollen.
	 */
	private klassenLehrerRemoveByKlasseOhneUpdate(klasse: UvKlasse): void {
		this.klassenLehrerByIdPlanungsabschnittAndIdKlasseAndIdLehrer.removeAllByKey2(klasse.id);
	}

	/**
	 * Entfernt ein existierendes {@link UvKlassenLehrer}-Objekt.
	 * @param zuordnung das zu entfernende {@link UvKlassenLehrer}-Objekt.
	 */
	public klassenLehrerRemove(zuordnung: UvKlassenLehrer): void {
		this.klassenLehrerRemoveOhneUpdate(zuordnung);
		this.updateAll();
	}

	private klassenLehrerRemoveOhneUpdate(zuordnung: UvKlassenLehrer): void {
		this.klassenLehrerByIdPlanungsabschnittAndIdKlasseAndIdLehrer.removeSingleOrException(zuordnung.idPlanungsabschnitt, zuordnung.idKlasse, zuordnung.idLehrer);
		this.klassenLehrerMenge.remove(zuordnung);
	}

	/**
	 * Entfernt alle {@link UvKlassenLehrer}-Objekte.
	 * @param listZuordnungen Die Liste der zu entfernenden {@link UvKlassenLehrer}-Objekte.
	 */
	public klassenLehrerRemoveAll(listZuordnungen: Collection<UvKlassenLehrer>): void {
		this.klassenLehrerRemoveAllOhneUpdate(listZuordnungen);
		this.updateAll();
	}

	private klassenLehrerRemoveAllOhneUpdate(listZuordnungen: Collection<UvKlassenLehrer>): void {
		const setZuordnungen: JavaSet<UvKlassenLehrer> | null = new HashSet<UvKlassenLehrer>(listZuordnungen);
		for (const zuordnung of setZuordnungen) {
			this.klassenLehrerRemoveOhneUpdate(zuordnung);
		}
	}

	/**
	 * Aktualisiert die vorhandenen {@link UvKlassenLehrer}-Objekte anhand der übergebenen neuen Werte.
	 * @param listZuordnungen die Sammlung der aktualisierten {@link UvKlassenLehrer}-Objekte
	 */
	public klassenLehrerAllPatchAttributes(listZuordnungen: Collection<UvKlassenLehrer>): void {
		this.klassenLehrerAllPatchAttributesOhneUpdate(listZuordnungen);
		this.updateAll();
	}

	private klassenLehrerAllPatchAttributesOhneUpdate(listZuordnungen: Collection<UvKlassenLehrer>): void {
		for (const neu of new HashSet(listZuordnungen)) {
			this.klassenLehrerRemoveOhneUpdate(this.klassenLehrerGetByZuordnungsIdOrException(neu.id));
		}
		this.klassenLehrerAddAllOhneUpdate(listZuordnungen);
	}

	/**
	 * Liefert die aktuelle Klassenlehrerzuordnung anhand ihrer unveränderlichen Datensatz-ID.
	 *
	 * @param id die ID der Zuordnung
	 * @return die gespeicherte Zuordnung
	 */
	public klassenLehrerGetByZuordnungsIdOrException(id: number): UvKlassenLehrer {
		for (const zuordnung of this.klassenLehrerByIdPlanungsabschnittAndIdKlasseAndIdLehrer.getAllValues()) {
			if (zuordnung.id === id) {
				return zuordnung;
			}
		}
		throw new DeveloperNotificationException("Klassenlehrer-Zuordnung mit ID " + id + " existiert nicht.");
	}

	/**
	 * Liefert eine Liste aller {@link UvKlassenLehrer}-Objekte für einen Planungsabschnitt. <br>
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}.
	 * @return eine Liste aller {@link UvKlassenLehrer}-Objekte für den Planungsabschnitt.
	 */
	public klassenLehrerGetMengeByPlanungsabschnitt(planungsabschnitt: UvPlanungsabschnitt): List<UvKlassenLehrer> {
		return this.klassenLehrerByIdPlanungsabschnittAndIdKlasseAndIdLehrer.get1(planungsabschnitt.id);
	}

	/**
	 * Liefert eine Liste aller {@link UvKlassenLehrer}-Objekte für eine {@link UvKlasse}. <br>
	 * @param klasse die {@link UvKlasse}.
	 * @return eine Liste aller {@link UvKlassenLehrer}-Objekte für die {@link UvKlasse}.
	 */
	public klassenLehrerGetMengeByKlasse(klasse: UvKlasse): List<UvKlassenLehrer> {
		return this.klassenLehrerByIdPlanungsabschnittAndIdKlasseAndIdLehrer.get12(klasse.idPlanungsabschnitt, klasse.id);
	}

	/**
	 * Fügt ein {@link UvKurs}-Objekt hinzu.
	 * @param kurs Das {@link UvKurs}-Objekt, welches hinzugefügt werden soll.
	 */
	public kursAdd(kurs: UvKurs): void {
		this.kursAddAll(ListUtils.create1(kurs));
	}

	private kursAddOhneUpdate(kurs: UvKurs): void {
		this.kursAddAllOhneUpdate(ListUtils.create1(kurs));
	}

	/**
	 * Fügt alle {@link UvKurs}-Objekte hinzu.
	 * @param listKurse Die Menge der {@link UvKurs}-Objekte, welche hinzugefügt werden soll.
	 */
	public kursAddAll(listKurse: Collection<UvKurs>): void {
		this.kursAddAllOhneUpdate(listKurse);
		this.updateAll();
	}

	private kursAddAllOhneUpdate(list: Collection<UvKurs>): void {
		const setOfIDs: HashSet<number> = new HashSet<number>();
		for (const kurs of list) {
			this.kursCheck(kurs);
			DeveloperNotificationException.ifTrue("kursAddAllOhneUpdate: Kurs existiert bereits!", this.kursByIdPlanungsabschnittAndIdSchuelergruppeAndIdKurs.containsKey13(kurs.idPlanungsabschnitt, kurs.id));
			DeveloperNotificationException.ifTrue("kursAddAllOhneUpdate: ID=" + kurs.id + " doppelt in der Liste!", !setOfIDs.add(kurs.id));
		}
		for (const kurs of list) {
			this.kursByIdPlanungsabschnittAndIdSchuelergruppeAndIdKurs.addSingle(kurs.idPlanungsabschnitt, kurs.idSchuelergruppe, kurs.id, kurs);
			this.kursMenge.add(kurs);
		}
	}

	/**
	 * Ersetzt das vorhandene {@link UvKurs}-Objekt anhand seiner ID ohne Löschkaskade
	 * und baut die Indizes neu auf. Das übergebene Objekt muss die vollständigen neuen Daten
	 * enthalten; das bisherige Manager-Objekt darf zuvor nicht verändert werden.
	 *
	 * @param kurs die neuen Daten mit unveränderter ID und unverändertem Planungsabschnitt
	 */
	public kursPatchAttributes(kurs: UvKurs): void {
		const alt: UvKurs = this.kursGetByIdOrException(kurs.id);
		this.kursCheck(kurs);
		DeveloperNotificationException.ifTrue("kursPatchAttributes: Der Planungsabschnitt darf nicht geändert werden.", alt.idPlanungsabschnitt !== kurs.idPlanungsabschnitt);
		this.kursByIdPlanungsabschnittAndIdSchuelergruppeAndIdKurs.removeSingleOrException(alt.idPlanungsabschnitt, alt.idSchuelergruppe, alt.id);
		this.kursByIdPlanungsabschnittAndIdSchuelergruppeAndIdKurs.addSingle(kurs.idPlanungsabschnitt, kurs.idSchuelergruppe, kurs.id, kurs);
		this.updateAll();
	}

	private kursCheck(kurs: UvKurs): void {
		DeveloperNotificationException.ifInvalidID("kurs.id", kurs.id);
		DeveloperNotificationException.ifInvalidID("kurs.idPlanungsabschnitt", kurs.idPlanungsabschnitt);
		if (!this.planungsabschnittById.containsKey(kurs.idPlanungsabschnitt)) {
			throw new DeveloperNotificationException("kursCheck: Der UvPlanungsabschnitt mit der ID " + kurs.idPlanungsabschnitt + " existiert nicht.");
		}
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvKurs}-Objekt. <br>
	 * @param idKurs Die ID des angefragten-Objektes.
	 * @return das zur ID zugehörige {@link UvKurs}-Objekt.
	 */
	public kursGetByIdOrException(idKurs: number): UvKurs {
		return this.kursByIdPlanungsabschnittAndIdSchuelergruppeAndIdKurs.getSingle3OrException(idKurs);
	}

	/**
	 * Liefert das zur ID gehörende {@link UvKurs}-Objekt direkt aus dem Index oder {@code null}.
	 *
	 * @param idKurs die ID des gesuchten Objekts
	 * @return das gespeicherte Objekt oder {@code null}, falls es nicht vorhanden ist
	 */
	public kursGetByIdOrNull(idKurs: number): UvKurs | null {
		return this.kursByIdPlanungsabschnittAndIdSchuelergruppeAndIdKurs.getSingle3OrNull(idKurs);
	}

	/**
	 * Liefert eine Liste aller {@link UvKurs}-Objekte. <br>
	 * @return eine Liste aller {@link UvKurs}-Objekte.
	 */
	public kursGetMengeAsList(): List<UvKurs> {
		return new ArrayList<UvKurs>(this.kursMenge);
	}

	private kursRemoveOhneUpdateById(idPlanungsabschnitt: number, idKurs: number): void {
		const kurs: UvKurs = this.kursGetByIdOrException(idKurs);
		this.kursByIdPlanungsabschnittAndIdSchuelergruppeAndIdKurs.removeSingleOrException(idPlanungsabschnitt, kurs.idSchuelergruppe, idKurs);
		const lerngruppe: UvLerngruppe | null = this.lerngruppeGetByKursIdOrNull(idKurs);
		if (lerngruppe !== null) {
			this.lerngruppeRemoveOhneUpdateById(lerngruppe.idPlanungsabschnitt, lerngruppe.id);
		}
	}

	/**
	 * Entfernt ein existierendes {@link UvKurs}-Objekt.
	 * @param idPlanungsabschnitt die ID des Planungsabschnitts.
	 * @param idKurs Die ID des Kurses.
	 */
	public kursRemoveById(idPlanungsabschnitt: number, idKurs: number): void {
		this.kursRemoveOhneUpdateById(idPlanungsabschnitt, idKurs);
		this.updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvKurs}-Objekt.
	 * @param kurs das zu entfernende {@link UvKurs}-Objekt.
	 */
	public kursRemove(kurs: UvKurs): void {
		this.kursRemoveOhneUpdate(kurs);
		this.updateAll();
	}

	private kursRemoveOhneUpdate(kurs: UvKurs): void {
		this.kursRemoveOhneUpdateById(kurs.idPlanungsabschnitt, kurs.id);
	}

	/**
	 * Entfernt alle {@link UvKurs}-Objekte.
	 * @param listKurse Die Liste der zu entfernenden {@link UvKurs}-Objekte.
	 */
	public kursRemoveAll(listKurse: Collection<UvKurs>): void {
		this.kursRemoveAllOhneUpdate(listKurse);
		this.updateAll();
	}

	private kursRemoveAllOhneUpdate(listKurse: Collection<UvKurs>): void {
		const setKurse: JavaSet<UvKurs> | null = new HashSet<UvKurs>(listKurse);
		for (const kurs of setKurse) {
			this.kursRemoveOhneUpdateById(kurs.idPlanungsabschnitt, kurs.id);
		}
	}

	/**
	 * Liefert eine Liste aller {@link UvKurs}-Objekte für einen Planungsabschnitt. <br>
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}.
	 * @return eine Liste aller {@link UvKurs}-Objekte für den Planungsabschnitt.
	 */
	public kursGetMengeByPlanungsabschnitt(planungsabschnitt: UvPlanungsabschnitt): List<UvKurs> {
		return this.kursByIdPlanungsabschnittAndIdSchuelergruppeAndIdKurs.get1(planungsabschnitt.id);
	}

	/**
	 * Fügt ein {@link UvSchiene}-Objekt hinzu.
	 * @param schiene Das {@link UvSchiene}-Objekt, welches hinzugefügt werden soll.
	 */
	public schieneAdd(schiene: UvSchiene): void {
		this.schieneAddAll(ListUtils.create1(schiene));
	}

	private schieneAddOhneUpdate(schiene: UvSchiene): void {
		this.schieneAddAllOhneUpdate(ListUtils.create1(schiene));
	}

	/**
	 * Fügt alle {@link UvSchiene}-Objekte hinzu.
	 * @param listSchienen Die Menge der {@link UvSchiene}-Objekte, welche hinzugefügt werden soll.
	 */
	public schieneAddAll(listSchienen: Collection<UvSchiene>): void {
		this.schieneAddAllOhneUpdate(listSchienen);
		this.updateAll();
	}

	private schieneAddAllOhneUpdate(list: Collection<UvSchiene>): void {
		const setOfIDs: HashSet<number> = new HashSet<number>();
		for (const schiene of list) {
			this.schieneCheck(schiene);
			DeveloperNotificationException.ifTrue("schieneAddAllOhneUpdate: Schiene existiert bereits!", this.schieneByIdPlanungsabschnittAndNummerAndIdSchiene.containsKey13(schiene.idPlanungsabschnitt, schiene.id));
			DeveloperNotificationException.ifTrue("schieneAddAllOhneUpdate: ID=" + schiene.id + " doppelt in der Liste!", !setOfIDs.add(schiene.id));
		}
		for (const schiene of list) {
			this.schieneByIdPlanungsabschnittAndNummerAndIdSchiene.addSingle(schiene.idPlanungsabschnitt, schiene.nummer, schiene.id, schiene);
			this.schieneMenge.add(schiene);
		}
	}

	/**
	 * Ersetzt das vorhandene {@link UvSchiene}-Objekt anhand seiner ID ohne Löschkaskade
	 * und baut die Indizes neu auf. Das übergebene Objekt muss die vollständigen neuen Daten
	 * enthalten; das bisherige Manager-Objekt darf zuvor nicht verändert werden.
	 *
	 * @param schiene die neuen Daten mit unveränderter ID und unverändertem Planungsabschnitt
	 */
	public schienePatchAttributes(schiene: UvSchiene): void {
		const alt: UvSchiene = this.schieneGetByIdOrException(schiene.id);
		this.schieneCheck(schiene);
		DeveloperNotificationException.ifTrue("schienePatchAttributes: Der Planungsabschnitt darf nicht geändert werden.", alt.idPlanungsabschnitt !== schiene.idPlanungsabschnitt);
		this.schieneByIdPlanungsabschnittAndNummerAndIdSchiene.removeSingleOrException(alt.idPlanungsabschnitt, alt.nummer, alt.id);
		this.schieneByIdPlanungsabschnittAndNummerAndIdSchiene.addSingle(schiene.idPlanungsabschnitt, schiene.nummer, schiene.id, schiene);
		this.updateAll();
	}

	private schieneCheck(schiene: UvSchiene): void {
		DeveloperNotificationException.ifInvalidID("schiene.id", schiene.id);
		DeveloperNotificationException.ifInvalidID("schiene.idPlanungsabschnitt", schiene.idPlanungsabschnitt);
		if (!this.planungsabschnittById.containsKey(schiene.idPlanungsabschnitt)) {
			throw new DeveloperNotificationException("schieneCheck: Der UvPlanungsabschnitt mit der ID " + schiene.idPlanungsabschnitt + " existiert nicht.");
		}
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvSchiene}-Objekt. <br>
	 * @param idSchiene Die ID des angefragten-Objektes.
	 * @return das zur ID zugehörige {@link UvSchiene}-Objekt.
	 */
	public schieneGetByIdOrException(idSchiene: number): UvSchiene {
		return this.schieneByIdPlanungsabschnittAndNummerAndIdSchiene.getSingle3OrException(idSchiene);
	}

	/**
	 * Liefert das zur ID gehörende {@link UvSchiene}-Objekt direkt aus dem Index oder {@code null}.
	 *
	 * @param idSchiene die ID des gesuchten Objekts
	 * @return das gespeicherte Objekt oder {@code null}, falls es nicht vorhanden ist
	 */
	public schieneGetByIdOrNull(idSchiene: number): UvSchiene | null {
		return this.schieneByIdPlanungsabschnittAndNummerAndIdSchiene.getSingle3OrNull(idSchiene);
	}

	/**
	 * Liefert eine Liste aller {@link UvSchiene}-Objekte. <br>
	 * @return eine Liste aller {@link UvSchiene}-Objekte.
	 */
	public schieneGetMengeAsList(): List<UvSchiene> {
		return new ArrayList<UvSchiene>(this.schieneMenge);
	}

	private schieneRemoveOhneUpdateById(idSchiene: number): void {
		this.schieneRemoveOhneUpdate(this.schieneGetByIdOrException(idSchiene));
	}

	/**
	 * Entfernt ein existierendes {@link UvSchiene}-Objekt.
	 * @param idSchiene Die ID der Schiene.
	 */
	public schieneRemoveById(idSchiene: number): void {
		this.schieneRemoveOhneUpdateById(idSchiene);
		this.updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvSchiene}-Objekt.
	 * @param schiene das zu entfernende {@link UvSchiene}-Objekt.
	 */
	public schieneRemove(schiene: UvSchiene): void {
		this.schieneRemoveOhneUpdate(schiene);
		this.updateAll();
	}

	private schieneRemoveOhneUpdate(schiene: UvSchiene): void {
		this.schieneByIdPlanungsabschnittAndNummerAndIdSchiene.removeSingleOrException(schiene.idPlanungsabschnitt, schiene.nummer, schiene.id);
		this.schieneMenge.remove(schiene);
	}

	/**
	 * Entfernt alle {@link UvSchiene}-Objekte.
	 * @param listSchienen Die Liste der zu entfernenden {@link UvSchiene}-Objekte.
	 */
	public schieneRemoveAll(listSchienen: Collection<UvSchiene>): void {
		this.schieneRemoveAllOhneUpdate(listSchienen);
		this.updateAll();
	}

	private schieneRemoveAllOhneUpdate(listSchienen: Collection<UvSchiene>): void {
		const setSchienen: JavaSet<UvSchiene> | null = new HashSet<UvSchiene>(listSchienen);
		for (const schiene of setSchienen) {
			this.schieneRemoveOhneUpdate(schiene);
		}
	}

	/**
	 * Liefert eine Liste aller {@link UvSchiene}-Objekte für einen Planungsabschnitt. <br>
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}.
	 * @return eine Liste aller {@link UvSchiene}-Objekte für den Planungsabschnitt.
	 */
	public schieneGetMengeByPlanungsabschnitt(planungsabschnitt: UvPlanungsabschnitt): List<UvSchiene> {
		return this.schieneByIdPlanungsabschnittAndNummerAndIdSchiene.get1(planungsabschnitt.id);
	}

	/**
	 * Fügt ein {@link UvLerngruppe}-Objekt hinzu.
	 * @param lerngruppe Das {@link UvLerngruppe}-Objekt, welches hinzugefügt werden soll.
	 */
	public lerngruppeAdd(lerngruppe: UvLerngruppe): void {
		this.lerngruppeAddAll(ListUtils.create1(lerngruppe));
	}

	private lerngruppeAddOhneUpdate(lerngruppe: UvLerngruppe): void {
		this.lerngruppeAddAllOhneUpdate(ListUtils.create1(lerngruppe));
	}

	/**
	 * Fügt alle {@link UvLerngruppe}-Objekte hinzu.
	 * @param listLerngruppen Die Menge der {@link UvLerngruppe}-Objekte, welche hinzugefügt werden soll.
	 */
	public lerngruppeAddAll(listLerngruppen: Collection<UvLerngruppe>): void {
		this.lerngruppeAddAllOhneUpdate(listLerngruppen);
		this.updateAll();
	}

	private lerngruppeAddAllOhneUpdate(list: Collection<UvLerngruppe>): void {
		const setOfIDs: HashSet<number> = new HashSet<number>();
		for (const lerngruppe of list) {
			this.lerngruppeCheck(lerngruppe);
			DeveloperNotificationException.ifTrue("lerngruppeAddAllOhneUpdate: Lerngruppe existiert bereits!", this.lerngruppeByIdPlanungsabschnittAndIdLerngruppe.containsKey12(lerngruppe.idPlanungsabschnitt, lerngruppe.id));
			DeveloperNotificationException.ifTrue("lerngruppeAddAllOhneUpdate: ID=" + lerngruppe.id + " doppelt in der Liste!", !setOfIDs.add(lerngruppe.id));
		}
		for (const lerngruppe of list) {
			this.lerngruppeByIdPlanungsabschnittAndIdLerngruppe.addSingle(lerngruppe.idPlanungsabschnitt, lerngruppe.id, lerngruppe);
			this.lerngruppeMenge.add(lerngruppe);
		}
	}

	/**
	 * Ersetzt das vorhandene {@link UvLerngruppe}-Objekt anhand seiner ID ohne Löschkaskade
	 * und baut die Indizes neu auf. Das übergebene Objekt muss die vollständigen neuen Daten
	 * enthalten; das bisherige Manager-Objekt darf zuvor nicht verändert werden.
	 *
	 * @param lerngruppe die neuen Daten mit unveränderter ID und unverändertem Planungsabschnitt
	 */
	public lerngruppePatchAttributes(lerngruppe: UvLerngruppe): void {
		const alt: UvLerngruppe = this.lerngruppeGetByIdOrException(lerngruppe.id);
		this.lerngruppeCheck(lerngruppe);
		DeveloperNotificationException.ifTrue("lerngruppePatchAttributes: Der Planungsabschnitt darf nicht geändert werden.", alt.idPlanungsabschnitt !== lerngruppe.idPlanungsabschnitt);
		this.lerngruppeByIdPlanungsabschnittAndIdLerngruppe.removeSingleOrException(alt.idPlanungsabschnitt, alt.id);
		this.lerngruppeByIdPlanungsabschnittAndIdLerngruppe.addSingle(lerngruppe.idPlanungsabschnitt, lerngruppe.id, lerngruppe);
		this.updateAll();
	}

	private lerngruppeCheck(lerngruppe: UvLerngruppe): void {
		DeveloperNotificationException.ifInvalidID("lerngruppe.id", lerngruppe.id);
		DeveloperNotificationException.ifInvalidID("lerngruppe.idPlanungsabschnitt", lerngruppe.idPlanungsabschnitt);
		if (!this.planungsabschnittById.containsKey(lerngruppe.idPlanungsabschnitt)) {
			throw new DeveloperNotificationException("lerngruppeCheck: Der UvPlanungsabschnitt mit der ID " + lerngruppe.idPlanungsabschnitt + " existiert nicht.");
		}
		UvManager.lerngruppeCheckZuordnung(lerngruppe);
	}

	/**
	 * Prüft ausschließlich die Zuordnung einer Lerngruppe: entweder Kurs ohne Klasse und Fach
	 * oder Klasse und Fach ohne Kurs. Die Existenz der referenzierten Objekte wird nicht geprüft.
	 * Bei einem Patch muss der vollständige resultierende Datensatz übergeben werden.
	 *
	 * @param lerngruppe die zu prüfende Lerngruppe
	 * @throws DeveloperNotificationException falls die Zuordnung unvollständig oder widersprüchlich ist
	 */
	public static lerngruppeCheckZuordnung(lerngruppe: UvLerngruppe): void {
		const kursunterricht: boolean = (lerngruppe.idKurs !== null) && (lerngruppe.idKlasse === null) && (lerngruppe.idFach === null);
		const klassenunterricht: boolean = (lerngruppe.idKurs === null) && (lerngruppe.idKlasse !== null) && (lerngruppe.idFach !== null);
		if (!kursunterricht && !klassenunterricht) {
			throw new DeveloperNotificationException("Eine Lerngruppe muss entweder einem Kurs ohne Klasse und Fach oder einer Klasse und einem Fach ohne Kurs zugeordnet sein.");
		}
	}

	/**
	 * Prüft, ob die angegebene Lerngruppe ein Korrekturfach ist.
	 *
	 * Eine Lerngruppe ist ein Korrekturfach, wenn
	 * - sie einem Leistungskurs zugeordnet ist oder
	 * - das zugehörige Fach Mathematik, Deutsch, Englisch, Latein oder Französisch ist.
	 *
	 * @param lerngruppe   Die zu prüfende {@link UvLerngruppe}.
	 *
	 * @return {@code true}, wenn die Lerngruppe ein Korrekturfach ist, sonst {@code false}.
	 */
	public lerngruppeIstKorrekturfach(lerngruppe: UvLerngruppe): boolean {
		const fach: UvFach | null = this.fachGetByLerngruppe(lerngruppe);
		if (fach === null) {
			return false;
		}
		const kuerzelStatistik: string | null = this.fachdatenGetByFach(fach).kuerzelStatistik;
		if (JavaObject.equalsTranspiler("M", (kuerzelStatistik)) || JavaObject.equalsTranspiler("D", (kuerzelStatistik)) || JavaObject.equalsTranspiler("E", (kuerzelStatistik)) || JavaObject.equalsTranspiler("L", (kuerzelStatistik)) || JavaObject.equalsTranspiler("F", (kuerzelStatistik))) {
			return true;
		}
		const kurs: UvKurs | null = this.kursGetByLerngruppe(lerngruppe);
		if (kurs === null) {
			return false;
		}
		const kursart: GostKursart | null = GostKursart.fromKuerzel(kurs.kursart);
		if (kursart === null) {
			return false;
		}
		return kursart as unknown === GostKursart.LK as unknown;
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvLerngruppe}-Objekt. <br>
	 * @param idLerngruppe Die ID des angefragten-Objektes.
	 * @return das zur ID zugehörige {@link UvLerngruppe}-Objekt.
	 */
	public lerngruppeGetByIdOrException(idLerngruppe: number): UvLerngruppe {
		return this.lerngruppeByIdPlanungsabschnittAndIdLerngruppe.getSingle2OrException(idLerngruppe);
	}

	/**
	 * Liefert das zur ID gehörende {@link UvLerngruppe}-Objekt direkt aus dem Index oder {@code null}.
	 *
	 * @param idLerngruppe die ID des gesuchten Objekts
	 * @return das gespeicherte Objekt oder {@code null}, falls es nicht vorhanden ist
	 */
	public lerngruppeGetByIdOrNull(idLerngruppe: number): UvLerngruppe | null {
		return this.lerngruppeByIdPlanungsabschnittAndIdLerngruppe.getSingle2OrNull(idLerngruppe);
	}

	/**
	 * Liefert eine Liste aller {@link UvLerngruppe}-Objekte. <br>
	 * @return eine Liste aller {@link UvLerngruppe}-Objekte.
	 */
	public lerngruppeGetMengeAsList(): List<UvLerngruppe> {
		return new ArrayList<UvLerngruppe>(this.lerngruppeMenge);
	}

	/**
	 * Liefert die einem Kurs zugeordnete {@link UvLerngruppe} oder {@code null}.
	 *
	 * @param idKurs die ID des Kurses
	 * @return die zugeordnete {@link UvLerngruppe} oder {@code null}
	 */
	public lerngruppeGetByKursIdOrNull(idKurs: number): UvLerngruppe | null {
		return this.lerngruppeByIdKursAndIdKlasseAndIdFach.getSingle1OrNull(idKurs);
	}

	private lerngruppeRemoveOhneUpdateById(idPlanungsabschnitt: number, idLerngruppe: number): void {
		const lerngruppe: UvLerngruppe = this.lerngruppeGetByIdOrException(idLerngruppe);
		this.lerngruppeByIdPlanungsabschnittAndIdLerngruppe.removeSingleOrException(idPlanungsabschnitt, idLerngruppe);
		this.unterrichtRemoveAllOhneUpdate(this.unterrichtGetMengeByLerngruppe(idPlanungsabschnitt, idLerngruppe));
		const schienen: List<UvLerngruppenSchiene> = this.lerngruppenSchieneByIdPlanungsabschnittAndIdLerngruppeAndIdSchiene.get2(idLerngruppe);
		this.lerngruppenSchieneRemoveAllOhneUpdate(schienen);
		this.lerngruppenLehrerRemoveAllOhneUpdate(this.lerngruppenLehrerGetMengeByLerngruppe(lerngruppe));
	}

	/**
	 * Entfernt ein existierendes {@link UvLerngruppe}-Objekt.
	 * @param idPlanungsabschnitt die ID des Planungsabschnitts.
	 * @param idLerngruppe Die ID der Lerngruppe.
	 */
	public lerngruppeRemoveById(idPlanungsabschnitt: number, idLerngruppe: number): void {
		this.lerngruppeRemoveOhneUpdateById(idPlanungsabschnitt, idLerngruppe);
		this.updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvLerngruppe}-Objekt.
	 * @param lerngruppe das zu entfernende {@link UvLerngruppe}-Objekt.
	 */
	public lerngruppeRemove(lerngruppe: UvLerngruppe): void {
		this.lerngruppeRemoveOhneUpdate(lerngruppe);
		this.updateAll();
	}

	private lerngruppeRemoveOhneUpdate(lerngruppe: UvLerngruppe): void {
		if (lerngruppe.idKurs !== null) {
			this.kursRemoveOhneUpdateById(lerngruppe.idPlanungsabschnitt, lerngruppe.idKurs);
			return;
		}
		this.lerngruppeRemoveOhneUpdateById(lerngruppe.idPlanungsabschnitt, lerngruppe.id);
	}

	/**
	 * Entfernt alle {@link UvLerngruppe}-Objekte.
	 * @param listLerngruppen Die Liste der zu entfernenden {@link UvLerngruppe}-Objekte.
	 */
	public lerngruppeRemoveAll(listLerngruppen: Collection<UvLerngruppe>): void {
		this.lerngruppeRemoveAllOhneUpdate(listLerngruppen);
		this.updateAll();
	}

	private lerngruppeRemoveAllOhneUpdate(listLerngruppen: Collection<UvLerngruppe>): void {
		const setLerngruppen: JavaSet<UvLerngruppe> | null = new HashSet<UvLerngruppe>(listLerngruppen);
		for (const lerngruppe of setLerngruppen) {
			this.lerngruppeRemoveOhneUpdate(lerngruppe);
		}
	}

	/**
	 * Liefert eine Liste aller {@link UvLerngruppe}-Objekte für einen Planungsabschnitt. <br>
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}.
	 * @return eine Liste aller {@link UvLerngruppe}-Objekte für den Planungsabschnitt.
	 */
	public lerngruppeGetMengeByPlanungsabschnitt(planungsabschnitt: UvPlanungsabschnitt): List<UvLerngruppe> {
		return this.lerngruppeByIdPlanungsabschnittAndIdLerngruppe.get1(planungsabschnitt.id);
	}

	/**
	 * Fügt ein {@link UvLerngruppenLehrer}-Objekt hinzu.
	 * @param zuordnung Das {@link UvLerngruppenLehrer}-Objekt, welches hinzugefügt werden soll.
	 */
	public lerngruppenLehrerAdd(zuordnung: UvLerngruppenLehrer): void {
		this.lerngruppenLehrerAddAll(ListUtils.create1(zuordnung));
	}

	private lerngruppenLehrerAddOhneUpdate(zuordnung: UvLerngruppenLehrer): void {
		this.lerngruppenLehrerAddAllOhneUpdate(ListUtils.create1(zuordnung));
	}

	/**
	 * Fügt alle {@link UvLerngruppenLehrer}-Objekte hinzu.
	 * @param listZuordnungen Die Menge der {@link UvLerngruppenLehrer}-Objekte, welche hinzugefügt werden soll.
	 */
	public lerngruppenLehrerAddAll(listZuordnungen: Collection<UvLerngruppenLehrer>): void {
		this.lerngruppenLehrerAddAllOhneUpdate(listZuordnungen);
		this.updateAll();
	}

	private lerngruppenLehrerAddAllOhneUpdate(list: Collection<UvLerngruppenLehrer>): void {
		const setOfIDs: HashSet<number> = new HashSet<number>();
		for (const zuordnung of list) {
			this.lerngruppenLehrerCheck(zuordnung);
			DeveloperNotificationException.ifTrue("lerngruppenLehrerAddAllOhneUpdate: Zuordnung existiert bereits!", this.lerngruppenLehrerByIdPlanungsabschnittAndIdLerngruppeAndIdLehrer.containsKey123(zuordnung.idPlanungsabschnitt, zuordnung.idLerngruppe, zuordnung.idLehrer));
			DeveloperNotificationException.ifTrue("lerngruppenLehrerAddAllOhneUpdate: ID=" + zuordnung.id + " doppelt in der Liste!", !setOfIDs.add(zuordnung.id));
		}
		for (const zuordnung of list) {
			this.lerngruppenLehrerByIdPlanungsabschnittAndIdLerngruppeAndIdLehrer.addSingle(zuordnung.idPlanungsabschnitt, zuordnung.idLerngruppe, zuordnung.idLehrer, zuordnung);
			this.lerngruppenLehrerMenge.add(zuordnung);
		}
	}

	private lerngruppenLehrerCheck(zuordnung: UvLerngruppenLehrer): void {
		DeveloperNotificationException.ifInvalidID("zuordnung.id", zuordnung.id);
		DeveloperNotificationException.ifInvalidID("zuordnung.idPlanungsabschnitt", zuordnung.idPlanungsabschnitt);
		DeveloperNotificationException.ifInvalidID("zuordnung.idLerngruppe", zuordnung.idLerngruppe);
		DeveloperNotificationException.ifInvalidID("zuordnung.idLehrer", zuordnung.idLehrer);
		if (!this.planungsabschnittById.containsKey(zuordnung.idPlanungsabschnitt)) {
			throw new DeveloperNotificationException("lerngruppenLehrerCheck: Der UvPlanungsabschnitt mit der ID " + zuordnung.idPlanungsabschnitt + " existiert nicht.");
		}
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvLerngruppenLehrer}-Objekt. <br>
	 * @param idPlanungsabschnitt Die ID des Planungsabschnitts.
	 * @param idLerngruppe Die ID der Lerngruppe.
	 * @param idLehrer Die ID des Lehrers.
	 * @return das zur ID zugehörige {@link UvLerngruppenLehrer}-Objekt.
	 */
	public lerngruppenLehrerGetByIdOrException(idPlanungsabschnitt: number, idLerngruppe: number, idLehrer: number): UvLerngruppenLehrer {
		return this.lerngruppenLehrerByIdPlanungsabschnittAndIdLerngruppeAndIdLehrer.getSingle123OrException(idPlanungsabschnitt, idLerngruppe, idLehrer);
	}

	/**
	 * Liefert eine Liste aller {@link UvLerngruppenLehrer}-Objekte. <br>
	 * @return eine Liste aller {@link UvLerngruppenLehrer}-Objekte.
	 */
	public lerngruppenLehrerGetMengeAsList(): List<UvLerngruppenLehrer> {
		return new ArrayList<UvLerngruppenLehrer>(this.lerngruppenLehrerMenge);
	}

	private lerngruppenLehrerRemoveOhneUpdateById(idPlanungsabschnitt: number, idLerngruppe: number, idLehrer: number): void {
		this.lerngruppenLehrerRemoveOhneUpdate(this.lerngruppenLehrerGetByIdOrException(idPlanungsabschnitt, idLerngruppe, idLehrer));
	}

	/**
	 * Entfernt ein existierendes {@link UvLerngruppenLehrer}-Objekt.
	 * @param idPlanungsabschnitt Die ID des Planungsabschnitts.
	 * @param idLerngruppe Die ID der Lerngruppe.
	 * @param idLehrer Die ID des Lehrers.
	 */
	public lerngruppenLehrerRemoveById(idPlanungsabschnitt: number, idLerngruppe: number, idLehrer: number): void {
		this.lerngruppenLehrerRemoveOhneUpdateById(idPlanungsabschnitt, idLerngruppe, idLehrer);
		this.updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvLerngruppenLehrer}-Objekt.
	 * @param zuordnung das zu entfernende {@link UvLerngruppenLehrer}-Objekt.
	 */
	public lerngruppenLehrerRemove(zuordnung: UvLerngruppenLehrer): void {
		this.lerngruppenLehrerRemoveOhneUpdate(zuordnung);
		this.updateAll();
	}

	private lerngruppenLehrerRemoveOhneUpdate(zuordnung: UvLerngruppenLehrer): void {
		this.lerngruppenLehrerRemoveOhneKaskadeOhneUpdate(zuordnung);
		this.unterrichtLerngruppenlehrerRemoveAllOhneUpdate(new ArrayList(this.unterrichtLerngruppenlehrerByIdPlanungsabschnittAndIdUnterrichtAndIdLerngruppenlehrer.get13(zuordnung.idPlanungsabschnitt, zuordnung.id)));
	}

	private lerngruppenLehrerRemoveOhneKaskadeOhneUpdate(zuordnung: UvLerngruppenLehrer): void {
		this.lerngruppenLehrerByIdPlanungsabschnittAndIdLerngruppeAndIdLehrer.removeSingleOrException(zuordnung.idPlanungsabschnitt, zuordnung.idLerngruppe, zuordnung.idLehrer);
		this.lerngruppenLehrerMenge.remove(zuordnung);
	}

	/**
	 * Entfernt alle {@link UvLerngruppenLehrer}-Objekte.
	 * @param listZuordnungen Die Liste der zu entfernenden {@link UvLerngruppenLehrer}-Objekte.
	 */
	public lerngruppenLehrerRemoveAll(listZuordnungen: Collection<UvLerngruppenLehrer>): void {
		this.lerngruppenLehrerRemoveAllOhneUpdate(listZuordnungen);
		this.updateAll();
	}

	private lerngruppenLehrerRemoveAllOhneUpdate(listZuordnungen: Collection<UvLerngruppenLehrer>): void {
		const setZuordnungen: JavaSet<UvLerngruppenLehrer> | null = new HashSet<UvLerngruppenLehrer>(listZuordnungen);
		for (const zuordnung of setZuordnungen) {
			this.lerngruppenLehrerRemoveOhneUpdate(zuordnung);
		}
	}

	/**
	 * Aktualisiert die vorhandenen {@link UvLerngruppenLehrer}-Objekte anhand der übergebenen neuen Werte.
	 * @param listZuordnungen die Sammlung der aktualisierten {@link UvLerngruppenLehrer}-Objekte
	 */
	public lerngruppenLehrerAllPatchAttributes(listZuordnungen: Collection<UvLerngruppenLehrer>): void {
		this.lerngruppenLehrerAllPatchAttributesOhneUpdate(listZuordnungen);
		this.updateAll();
	}

	private lerngruppenLehrerAllPatchAttributesOhneUpdate(listZuordnungen: Collection<UvLerngruppenLehrer>): void {
		for (const zuordnung of new HashSet(listZuordnungen)) {
			this.lerngruppenLehrerRemoveOhneKaskadeOhneUpdate(this.lerngruppenLehrerGetByZuordnungsIdOrException(zuordnung.id));
		}
		this.lerngruppenLehrerAddAllOhneUpdate(listZuordnungen);
	}

	/**
	 * Liefert die aktuelle Lerngruppenlehrerzuordnung anhand ihrer unveränderlichen Datensatz-ID.
	 *
	 * @param id die ID der Zuordnung
	 * @return die gespeicherte Zuordnung
	 */
	public lerngruppenLehrerGetByZuordnungsIdOrException(id: number): UvLerngruppenLehrer {
		for (const zuordnung of this.lerngruppenLehrerByIdPlanungsabschnittAndIdLerngruppeAndIdLehrer.getAllValues()) {
			if (zuordnung.id === id) {
				return zuordnung;
			}
		}
		throw new DeveloperNotificationException("Lerngruppenlehrer-Zuordnung mit ID " + id + " existiert nicht.");
	}

	/**
	 * Liefert eine Liste aller {@link UvLerngruppenLehrer}-Objekte für einen Planungsabschnitt. <br>
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}.
	 * @return eine Liste aller {@link UvLerngruppenLehrer}-Objekte für den Planungsabschnitt.
	 */
	public lerngruppenLehrerGetMengeByPlanungsabschnitt(planungsabschnitt: UvPlanungsabschnitt): List<UvLerngruppenLehrer> {
		return this.lerngruppenLehrerByIdPlanungsabschnittAndIdLerngruppeAndIdLehrer.get1(planungsabschnitt.id);
	}

	/**
	 * Liefert eine Liste aller {@link UvLerngruppenLehrer}-Objekte für eine {@link UvLerngruppe}. <br>
	 * @param lerngruppe die {@link UvLerngruppe}.
	 * @return eine Liste aller {@link UvLerngruppenLehrer}-Objekte für die {@link UvLerngruppe}.
	 */
	public lerngruppenLehrerGetMengeByLerngruppe(lerngruppe: UvLerngruppe): List<UvLerngruppenLehrer> {
		return this.lerngruppenLehrerByIdPlanungsabschnittAndIdLerngruppeAndIdLehrer.get12(lerngruppe.idPlanungsabschnitt, lerngruppe.id);
	}

	/**
	 * Fügt ein {@link UvLerngruppenSchiene}-Objekt hinzu.
	 * @param zuordnung Das {@link UvLerngruppenSchiene}-Objekt, welches hinzugefügt werden soll.
	 */
	public lerngruppenSchieneAdd(zuordnung: UvLerngruppenSchiene): void {
		this.lerngruppenSchieneAddAll(ListUtils.create1(zuordnung));
	}

	private lerngruppenSchieneAddOhneUpdate(zuordnung: UvLerngruppenSchiene): void {
		this.lerngruppenSchieneAddAllOhneUpdate(ListUtils.create1(zuordnung));
	}

	/**
	 * Fügt alle {@link UvLerngruppenSchiene}-Objekte hinzu.
	 * @param listZuordnungen Die Menge der {@link UvLerngruppenSchiene}-Objekte, welche hinzugefügt werden soll.
	 */
	public lerngruppenSchieneAddAll(listZuordnungen: Collection<UvLerngruppenSchiene>): void {
		this.lerngruppenSchieneAddAllOhneUpdate(listZuordnungen);
		this.updateAll();
	}

	private lerngruppenSchieneAddAllOhneUpdate(list: Collection<UvLerngruppenSchiene>): void {
		for (const zuordnung of list) {
			this.lerngruppenSchieneCheck(zuordnung);
			DeveloperNotificationException.ifTrue("lerngruppenSchieneAddAllOhneUpdate: Zuordnung existiert bereits!", this.lerngruppenSchieneByIdPlanungsabschnittAndIdLerngruppeAndIdSchiene.containsKey123(zuordnung.idPlanungsabschnitt, zuordnung.idLerngruppe, zuordnung.idSchiene));
		}
		for (const zuordnung of list) {
			this.lerngruppenSchieneByIdPlanungsabschnittAndIdLerngruppeAndIdSchiene.addSingle(zuordnung.idPlanungsabschnitt, zuordnung.idLerngruppe, zuordnung.idSchiene, zuordnung);
			this.lerngruppenSchieneMenge.add(zuordnung);
		}
	}

	private lerngruppenSchieneCheck(zuordnung: UvLerngruppenSchiene): void {
		DeveloperNotificationException.ifInvalidID("zuordnung.idPlanungsabschnitt", zuordnung.idPlanungsabschnitt);
		DeveloperNotificationException.ifInvalidID("zuordnung.idLerngruppe", zuordnung.idLerngruppe);
		DeveloperNotificationException.ifInvalidID("zuordnung.idSchiene", zuordnung.idSchiene);
		if (!this.planungsabschnittById.containsKey(zuordnung.idPlanungsabschnitt)) {
			throw new DeveloperNotificationException("lerngruppenSchieneCheck: Der UvPlanungsabschnitt mit der ID " + zuordnung.idPlanungsabschnitt + " existiert nicht.");
		}
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvLerngruppenSchiene}-Objekt. <br>
	 * @param idPlanungsabschnitt Die ID des Planungsabschnitts.
	 * @param idLerngruppe Die ID der Lerngruppe.
	 * @param idSchiene Die ID der Schiene.
	 * @return das zur ID zugehörige {@link UvLerngruppenSchiene}-Objekt.
	 */
	public lerngruppenSchieneGetByIdOrException(idPlanungsabschnitt: number, idLerngruppe: number, idSchiene: number): UvLerngruppenSchiene {
		return this.lerngruppenSchieneByIdPlanungsabschnittAndIdLerngruppeAndIdSchiene.getSingle123OrException(idPlanungsabschnitt, idLerngruppe, idSchiene);
	}

	/**
	 * Liefert eine Liste aller {@link UvLerngruppenSchiene}-Objekte. <br>
	 * @return eine Liste aller {@link UvLerngruppenSchiene}-Objekte.
	 */
	public lerngruppenSchieneGetMengeAsList(): List<UvLerngruppenSchiene> {
		return new ArrayList<UvLerngruppenSchiene>(this.lerngruppenSchieneMenge);
	}

	private lerngruppenSchieneRemoveOhneUpdateById(idPlanungsabschnitt: number, idLerngruppe: number, idSchiene: number): void {
		this.lerngruppenSchieneRemoveOhneUpdate(this.lerngruppenSchieneGetByIdOrException(idPlanungsabschnitt, idLerngruppe, idSchiene));
	}

	/**
	 * Entfernt ein existierendes {@link UvLerngruppenSchiene}-Objekt.
	 * @param idPlanungsabschnitt Die ID des Planungsabschnitts.
	 * @param idLerngruppe Die ID der Lerngruppe.
	 * @param idSchiene Die ID der Schiene.
	 */
	public lerngruppenSchieneRemoveById(idPlanungsabschnitt: number, idLerngruppe: number, idSchiene: number): void {
		this.lerngruppenSchieneRemoveOhneUpdateById(idPlanungsabschnitt, idLerngruppe, idSchiene);
		this.updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvLerngruppenSchiene}-Objekt.
	 * @param zuordnung das zu entfernende {@link UvLerngruppenSchiene}-Objekt.
	 */
	public lerngruppenSchieneRemove(zuordnung: UvLerngruppenSchiene): void {
		this.lerngruppenSchieneRemoveOhneUpdate(zuordnung);
		this.updateAll();
	}

	private lerngruppenSchieneRemoveOhneUpdate(zuordnung: UvLerngruppenSchiene): void {
		this.lerngruppenSchieneByIdPlanungsabschnittAndIdLerngruppeAndIdSchiene.removeSingleOrException(zuordnung.idPlanungsabschnitt, zuordnung.idLerngruppe, zuordnung.idSchiene);
		this.lerngruppenSchieneMenge.remove(zuordnung);
	}

	/**
	 * Entfernt alle {@link UvLerngruppenSchiene}-Objekte.
	 * @param listZuordnungen Die Liste der zu entfernenden {@link UvLerngruppenSchiene}-Objekte.
	 */
	public lerngruppenSchieneRemoveAll(listZuordnungen: Collection<UvLerngruppenSchiene>): void {
		this.lerngruppenSchieneRemoveAllOhneUpdate(listZuordnungen);
		this.updateAll();
	}

	private lerngruppenSchieneRemoveAllOhneUpdate(listZuordnungen: Collection<UvLerngruppenSchiene>): void {
		const setZuordnungen: JavaSet<UvLerngruppenSchiene> | null = new HashSet<UvLerngruppenSchiene>(listZuordnungen);
		for (const zuordnung of setZuordnungen) {
			this.lerngruppenSchieneRemoveOhneUpdate(zuordnung);
		}
	}

	/**
	 * Liefert eine Liste aller {@link UvLerngruppenSchiene}-Objekte für einen Planungsabschnitt. <br>
	 * @param idPlanungsabschnitt die ID des Planungsabschnitts.
	 * @return eine Liste aller {@link UvLerngruppenSchiene}-Objekte für den Planungsabschnitt.
	 */
	public lerngruppenSchieneGetMengeByPlanungsabschnitt(idPlanungsabschnitt: number): List<UvLerngruppenSchiene> {
		return this.lerngruppenSchieneByIdPlanungsabschnittAndIdLerngruppeAndIdSchiene.get1(idPlanungsabschnitt);
	}

	/**
	 * Liefert eine Liste aller {@link UvLerngruppenSchiene}-Objekte für eine Lerngruppe. <br>
	 * @param idPlanungsabschnitt die ID des Planungsabschnitts.
	 * @param idLerngruppe die ID der Lerngruppe.
	 * @return eine Liste aller {@link UvLerngruppenSchiene}-Objekte für die Lerngruppe.
	 */
	public lerngruppenSchieneGetMengeByLerngruppe(idPlanungsabschnitt: number, idLerngruppe: number) : List<UvLerngruppenSchiene>;

	/**
	 * Gibt die Menge der {@link UvLerngruppenSchiene}n zur übergebenen {@link UvLerngruppe} zurück.
	 * @param lerngruppe die {@link UvLerngruppe}, zu der die Schienen gesucht werden
	 * @return die Menge der zugeordneten {@link UvLerngruppenSchiene}-Objekte
	 */
	public lerngruppenSchieneGetMengeByLerngruppe(lerngruppe: UvLerngruppe) : List<UvLerngruppenSchiene>;

	/**
	 * Implementation for method overloads of 'lerngruppenSchieneGetMengeByLerngruppe'
	 */
	public lerngruppenSchieneGetMengeByLerngruppe(__param0: UvLerngruppe | number, __param1?: number): List<UvLerngruppenSchiene> {
		if (((__param0 !== undefined) && typeof __param0 === "number") && ((__param1 !== undefined) && typeof __param1 === "number")) {
			const idPlanungsabschnitt: number = __param0 as number;
			const idLerngruppe: number = __param1 as number;
			return new ArrayList<UvLerngruppenSchiene>(this.lerngruppenSchieneByIdPlanungsabschnittAndIdLerngruppeAndIdSchiene.get12(idPlanungsabschnitt, idLerngruppe));
		} else if (((__param0 !== undefined) && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.svws_nrw.core.data.uv.UvLerngruppe')))) && (__param1 === undefined)) {
			const lerngruppe: UvLerngruppe = cast_de_svws_nrw_core_data_uv_UvLerngruppe(__param0);
			return this.lerngruppenSchieneByIdPlanungsabschnittAndIdLerngruppeAndIdSchiene.get12(lerngruppe.idPlanungsabschnitt, lerngruppe.id);
		} else throw new Error('invalid method overload');
	}

	/**
	 * Fügt ein {@link UvUnterricht}-Objekt hinzu.
	 * @param unterricht Das {@link UvUnterricht}-Objekt, welches hinzugefügt werden soll.
	 */
	public unterrichtAdd(unterricht: UvUnterricht): void {
		this.unterrichtAddAll(ListUtils.create1(unterricht));
	}

	private unterrichtAddOhneUpdate(unterricht: UvUnterricht): void {
		this.unterrichtAddAllOhneUpdate(ListUtils.create1(unterricht));
	}

	/**
	 * Fügt alle {@link UvUnterricht}-Objekte hinzu.
	 * @param listUnterricht Die Menge der {@link UvUnterricht}-Objekte, welche hinzugefügt werden soll.
	 */
	public unterrichtAddAll(listUnterricht: Collection<UvUnterricht>): void {
		this.unterrichtAddAllOhneUpdate(listUnterricht);
		this.updateAll();
	}

	private unterrichtAddAllOhneUpdate(list: Collection<UvUnterricht>): void {
		const setOfIDs: HashSet<number> = new HashSet<number>();
		for (const unterricht of list) {
			this.unterrichtCheck(unterricht);
			DeveloperNotificationException.ifTrue("unterrichtAddAllOhneUpdate: Unterricht existiert bereits!", this.unterrichtByIdPlanungsabschnittAndIdLerngruppeAndIdZeitrasterEintragAndIdUnterricht.containsKey1234(unterricht.idPlanungsabschnitt, unterricht.idLerngruppe, (unterricht.idZeitrasterEintrag === null) ? -1 : unterricht.idZeitrasterEintrag, unterricht.id));
			DeveloperNotificationException.ifTrue("unterrichtAddAllOhneUpdate: ID=" + unterricht.id + " doppelt in der Liste!", !setOfIDs.add(unterricht.id));
		}
		for (const unterricht of list) {
			this.unterrichtByIdPlanungsabschnittAndIdLerngruppeAndIdZeitrasterEintragAndIdUnterricht.addSingle(unterricht.idPlanungsabschnitt, unterricht.idLerngruppe, (unterricht.idZeitrasterEintrag === null) ? -1 : unterricht.idZeitrasterEintrag, unterricht.id, unterricht);
			this.unterrichtMenge.add(unterricht);
		}
	}

	private unterrichtCheck(unterricht: UvUnterricht): void {
		DeveloperNotificationException.ifInvalidID("unterricht.id", unterricht.id);
		DeveloperNotificationException.ifInvalidID("unterricht.idPlanungsabschnitt", unterricht.idPlanungsabschnitt);
		DeveloperNotificationException.ifInvalidID("unterricht.idLerngruppe", unterricht.idLerngruppe);
		if (!this.planungsabschnittById.containsKey(unterricht.idPlanungsabschnitt)) {
			throw new DeveloperNotificationException("unterrichtCheck: Der UvPlanungsabschnitt mit der ID " + unterricht.idPlanungsabschnitt + " existiert nicht.");
		}
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvUnterricht}-Objekt. <br>
	 * @param idUnterricht Die ID des angefragten-Objektes.
	 * @return das zur ID zugehörige {@link UvUnterricht}-Objekt.
	 */
	public unterrichtGetByIdOrException(idUnterricht: number): UvUnterricht {
		return this.unterrichtByIdPlanungsabschnittAndIdLerngruppeAndIdZeitrasterEintragAndIdUnterricht.getSingle4OrException(idUnterricht);
	}

	/**
	 * Liefert das zur ID gehörende {@link UvUnterricht}-Objekt direkt aus dem Index oder {@code null}.
	 *
	 * @param idUnterricht die ID des gesuchten Objekts
	 * @return das gespeicherte Objekt oder {@code null}, falls es nicht vorhanden ist
	 */
	public unterrichtGetByIdOrNull(idUnterricht: number): UvUnterricht | null {
		return this.unterrichtByIdPlanungsabschnittAndIdLerngruppeAndIdZeitrasterEintragAndIdUnterricht.getSingle4OrNull(idUnterricht);
	}

	/**
	 * Liefert eine Liste aller {@link UvUnterricht}-Objekte. <br>
	 * @return eine Liste aller {@link UvUnterricht}-Objekte.
	 */
	public unterrichtGetMengeAsList(): List<UvUnterricht> {
		return new ArrayList<UvUnterricht>(this.unterrichtMenge);
	}

	private unterrichtRemoveOhneUpdateById(idUnterricht: number): void {
		this.unterrichtRemoveOhneUpdate(this.unterrichtGetByIdOrException(idUnterricht));
	}

	/**
	 * Entfernt ein existierendes {@link UvUnterricht}-Objekt anhand seiner ID.
	 * @param idUnterricht Die ID des Unterrichts.
	 */
	public unterrichtRemoveById(idUnterricht: number): void {
		this.unterrichtRemoveOhneUpdateById(idUnterricht);
		this.updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvUnterricht}-Objekt.
	 * @param unterricht das zu entfernende {@link UvUnterricht}-Objekt.
	 */
	public unterrichtRemove(unterricht: UvUnterricht): void {
		this.unterrichtRemoveOhneUpdate(unterricht);
		this.updateAll();
	}

	private unterrichtRemoveOhneUpdate(unterricht: UvUnterricht): void {
		this.unterrichtRemoveOhneKaskadeOhneUpdate(unterricht);
		this.unterrichtRaumRemoveAllOhneUpdate(new ArrayList(this.unterrichtRaumGetMengeByUnterricht(unterricht)));
		this.unterrichtLerngruppenlehrerRemoveAllOhneUpdate(new ArrayList(this.unterrichtLerngruppenlehrerGetMengeByUnterricht(unterricht)));
	}

	private unterrichtRemoveOhneKaskadeOhneUpdate(unterricht: UvUnterricht): void {
		this.unterrichtByIdPlanungsabschnittAndIdLerngruppeAndIdZeitrasterEintragAndIdUnterricht.removeOrException(unterricht.idPlanungsabschnitt, unterricht.idLerngruppe, (unterricht.idZeitrasterEintrag === null) ? -1 : unterricht.idZeitrasterEintrag, unterricht.id);
		this.unterrichtMenge.remove(unterricht);
	}

	/**
	 * Entfernt alle {@link UvUnterricht}-Objekte.
	 * @param listUnterricht Die Liste der zu entfernenden {@link UvUnterricht}-Objekte.
	 */
	public unterrichtRemoveAll(listUnterricht: Collection<UvUnterricht>): void {
		this.unterrichtRemoveAllOhneUpdate(listUnterricht);
		this.updateAll();
	}

	private unterrichtRemoveAllOhneUpdate(listUnterricht: Collection<UvUnterricht>): void {
		const setUnterricht: JavaSet<UvUnterricht> | null = new HashSet<UvUnterricht>(listUnterricht);
		for (const unterricht of setUnterricht) {
			this.unterrichtRemoveOhneUpdate(unterricht);
		}
	}

	/**
	 * Liefert eine Liste aller {@link UvUnterricht}-Objekte für einen Planungsabschnitt. <br>
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}.
	 * @return eine Liste aller {@link UvUnterricht}-Objekte für den Planungsabschnitt.
	 */
	public unterrichtGetMengeByPlanungsabschnitt(planungsabschnitt: UvPlanungsabschnitt): List<UvUnterricht> {
		return this.unterrichtByIdPlanungsabschnittAndIdLerngruppeAndIdZeitrasterEintragAndIdUnterricht.get1(planungsabschnitt.id);
	}

	/**
	 * Liefert eine Liste aller {@link UvUnterricht}-Objekte für eine Lerngruppe. <br>
	 * @param idPlanungsabschnitt die ID des Planungsabschnitts.
	 * @param idLerngruppe die ID der Lerngruppe.
	 * @return eine Liste aller {@link UvUnterricht}-Objekte für die Lerngruppe.
	 */
	public unterrichtGetMengeByLerngruppe(idPlanungsabschnitt: number, idLerngruppe: number): List<UvUnterricht> {
		return new ArrayList<UvUnterricht>(this.unterrichtByIdPlanungsabschnittAndIdLerngruppeAndIdZeitrasterEintragAndIdUnterricht.get12(idPlanungsabschnitt, idLerngruppe));
	}

	/**
	 * Aktualisiert die vorhandenen {@link UvUnterricht}-Objekte durch die neuen Objekte.
	 * @param listUnterricht Die neuen {@link UvUnterricht}-Objekte.
	 */
	public unterrichtAllPatchAttributes(listUnterricht: Collection<UvUnterricht>): void {
		this.unterrichtAllPatchAttributesOhneUpdate(listUnterricht);
		this.updateAll();
	}

	private unterrichtAllPatchAttributesOhneUpdate(listUnterricht: Collection<UvUnterricht>): void {
		for (const unterricht of new HashSet(listUnterricht)) {
			this.unterrichtRemoveOhneKaskadeOhneUpdate(this.unterrichtGetByIdOrException(unterricht.id));
		}
		this.unterrichtAddAllOhneUpdate(listUnterricht);
	}

	/**
	 * Fügt ein {@link UvUnterrichtRaum}-Objekt hinzu.
	 * @param zuordnung Das {@link UvUnterrichtRaum}-Objekt, welches hinzugefügt werden soll.
	 */
	public unterrichtRaumAdd(zuordnung: UvUnterrichtRaum): void {
		this.unterrichtRaumAddAll(ListUtils.create1(zuordnung));
	}

	private unterrichtRaumAddOhneUpdate(zuordnung: UvUnterrichtRaum): void {
		this.unterrichtRaumAddAllOhneUpdate(ListUtils.create1(zuordnung));
	}

	/**
	 * Fügt alle {@link UvUnterrichtRaum}-Objekte hinzu.
	 * @param listZuordnungen Die Menge der {@link UvUnterrichtRaum}-Objekte, welche hinzugefügt werden soll.
	 */
	public unterrichtRaumAddAll(listZuordnungen: Collection<UvUnterrichtRaum>): void {
		this.unterrichtRaumAddAllOhneUpdate(listZuordnungen);
		this.updateAll();
	}

	private unterrichtRaumAddAllOhneUpdate(list: Collection<UvUnterrichtRaum>): void {
		for (const zuordnung of list) {
			this.unterrichtRaumCheck(zuordnung);
			DeveloperNotificationException.ifTrue("unterrichtRaumAddAllOhneUpdate: Zuordnung existiert bereits!", this.unterrichtRaumByIdPlanungsabschnittAndIdUnterrichtAndIdRaum.containsKey123(zuordnung.idPlanungsabschnitt, zuordnung.idUnterricht, zuordnung.idRaum));
		}
		for (const zuordnung of list) {
			this.unterrichtRaumByIdPlanungsabschnittAndIdUnterrichtAndIdRaum.addSingle(zuordnung.idPlanungsabschnitt, zuordnung.idUnterricht, zuordnung.idRaum, zuordnung);
			this.unterrichtRaumMenge.add(zuordnung);
		}
	}

	private unterrichtRaumCheck(zuordnung: UvUnterrichtRaum): void {
		DeveloperNotificationException.ifInvalidID("zuordnung.idPlanungsabschnitt", zuordnung.idPlanungsabschnitt);
		DeveloperNotificationException.ifInvalidID("zuordnung.idUnterricht", zuordnung.idUnterricht);
		DeveloperNotificationException.ifInvalidID("zuordnung.idRaum", zuordnung.idRaum);
		if (!this.planungsabschnittById.containsKey(zuordnung.idPlanungsabschnitt)) {
			throw new DeveloperNotificationException("unterrichtRaumCheck: Der UvPlanungsabschnitt mit der ID " + zuordnung.idPlanungsabschnitt + " existiert nicht.");
		}
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvUnterrichtRaum}-Objekt. <br>
	 * @param idPlanungsabschnitt Die ID des Planungsabschnitts.
	 * @param idUnterricht Die ID des Unterrichts.
	 * @param idRaum Die ID des Raums.
	 * @return das zur ID zugehörige {@link UvUnterrichtRaum}-Objekt.
	 */
	public unterrichtRaumGetByIdOrException(idPlanungsabschnitt: number, idUnterricht: number, idRaum: number): UvUnterrichtRaum {
		return this.unterrichtRaumByIdPlanungsabschnittAndIdUnterrichtAndIdRaum.getSingle123OrException(idPlanungsabschnitt, idUnterricht, idRaum);
	}

	/**
	 * Liefert eine Liste aller {@link UvUnterrichtRaum}-Objekte. <br>
	 * @return eine Liste aller {@link UvUnterrichtRaum}-Objekte.
	 */
	public unterrichtRaumGetMengeAsList(): List<UvUnterrichtRaum> {
		return new ArrayList<UvUnterrichtRaum>(this.unterrichtRaumMenge);
	}

	private unterrichtRaumRemoveOhneUpdateById(idPlanungsabschnitt: number, idUnterricht: number, idRaum: number): void {
		this.unterrichtRaumRemoveOhneUpdate(this.unterrichtRaumGetByIdOrException(idPlanungsabschnitt, idUnterricht, idRaum));
	}

	/**
	 * Entfernt ein existierendes {@link UvUnterrichtRaum}-Objekt.
	 * @param idPlanungsabschnitt Die ID des Planungsabschnitts.
	 * @param idUnterricht Die ID des Unterrichts.
	 * @param idRaum Die ID des Raums.
	 */
	public unterrichtRaumRemoveById(idPlanungsabschnitt: number, idUnterricht: number, idRaum: number): void {
		this.unterrichtRaumRemoveOhneUpdateById(idPlanungsabschnitt, idUnterricht, idRaum);
		this.updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvUnterrichtRaum}-Objekt.
	 * @param zuordnung das zu entfernende {@link UvUnterrichtRaum}-Objekt.
	 */
	public unterrichtRaumRemove(zuordnung: UvUnterrichtRaum): void {
		this.unterrichtRaumRemoveOhneUpdate(zuordnung);
		this.updateAll();
	}

	private unterrichtRaumRemoveOhneUpdate(zuordnung: UvUnterrichtRaum): void {
		this.unterrichtRaumByIdPlanungsabschnittAndIdUnterrichtAndIdRaum.removeSingleOrException(zuordnung.idPlanungsabschnitt, zuordnung.idUnterricht, zuordnung.idRaum);
		this.unterrichtRaumMenge.remove(zuordnung);
	}

	/**
	 * Entfernt alle {@link UvUnterrichtRaum}-Objekte.
	 * @param listZuordnungen Die Liste der zu entfernenden {@link UvUnterrichtRaum}-Objekte.
	 */
	public unterrichtRaumRemoveAll(listZuordnungen: Collection<UvUnterrichtRaum>): void {
		this.unterrichtRaumRemoveAllOhneUpdate(listZuordnungen);
		this.updateAll();
	}

	private unterrichtRaumRemoveAllOhneUpdate(listZuordnungen: Collection<UvUnterrichtRaum>): void {
		const setZuordnungen: JavaSet<UvUnterrichtRaum> | null = new HashSet<UvUnterrichtRaum>(listZuordnungen);
		for (const zuordnung of setZuordnungen) {
			this.unterrichtRaumRemoveOhneUpdate(zuordnung);
		}
	}

	/**
	 * Liefert eine Liste aller {@link UvUnterrichtRaum}-Objekte für einen Planungsabschnitt. <br>
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}.
	 * @return eine Liste aller {@link UvUnterrichtRaum}-Objekte für den Planungsabschnitt.
	 */
	public unterrichtRaumGetMengeByPlanungsabschnitt(planungsabschnitt: UvPlanungsabschnitt): List<UvUnterrichtRaum> {
		return this.unterrichtRaumByIdPlanungsabschnittAndIdUnterrichtAndIdRaum.get1(planungsabschnitt.id);
	}

	/**
	 * Liefert eine Liste aller {@link UvUnterrichtRaum}-Objekte für einen {@link UvUnterricht}. <br>
	 * @param unterricht der {@link UvUnterricht}.
	 * @return eine Liste aller {@link UvUnterrichtRaum}-Objekte für den {@link UvUnterricht}.
	 */
	public unterrichtRaumGetMengeByUnterricht(unterricht: UvUnterricht): List<UvUnterrichtRaum> {
		return this.unterrichtRaumByIdPlanungsabschnittAndIdUnterrichtAndIdRaum.get12(unterricht.idPlanungsabschnitt, unterricht.id);
	}

	/**
	 * Fügt ein {@link UvUnterrichtLerngruppenlehrer}-Objekt hinzu.
	 * @param zuordnung Das {@link UvUnterrichtLerngruppenlehrer}-Objekt, welches hinzugefügt werden soll.
	 */
	public unterrichtLerngruppenlehrerAdd(zuordnung: UvUnterrichtLerngruppenlehrer): void {
		this.unterrichtLerngruppenlehrerAddAll(ListUtils.create1(zuordnung));
	}

	private unterrichtLerngruppenlehrerAddOhneUpdate(zuordnung: UvUnterrichtLerngruppenlehrer): void {
		this.unterrichtLerngruppenlehrerAddAllOhneUpdate(ListUtils.create1(zuordnung));
	}

	/**
	 * Fügt alle {@link UvUnterrichtLerngruppenlehrer}-Objekte hinzu.
	 * @param listZuordnungen Die Menge der {@link UvUnterrichtLerngruppenlehrer}-Objekte, welche hinzugefügt werden soll.
	 */
	public unterrichtLerngruppenlehrerAddAll(listZuordnungen: Collection<UvUnterrichtLerngruppenlehrer>): void {
		this.unterrichtLerngruppenlehrerAddAllOhneUpdate(listZuordnungen);
		this.updateAll();
	}

	private unterrichtLerngruppenlehrerAddAllOhneUpdate(list: Collection<UvUnterrichtLerngruppenlehrer>): void {
		for (const zuordnung of list) {
			this.unterrichtLerngruppenlehrerCheck(zuordnung);
			DeveloperNotificationException.ifTrue("unterrichtLerngruppenlehrerAddAllOhneUpdate: Zuordnung existiert bereits!", this.unterrichtLerngruppenlehrerByIdPlanungsabschnittAndIdUnterrichtAndIdLerngruppenlehrer.containsKey123(zuordnung.idPlanungsabschnitt, zuordnung.idUnterricht, zuordnung.idLerngruppenLehrer));
		}
		for (const zuordnung of list) {
			this.unterrichtLerngruppenlehrerByIdPlanungsabschnittAndIdUnterrichtAndIdLerngruppenlehrer.addSingle(zuordnung.idPlanungsabschnitt, zuordnung.idUnterricht, zuordnung.idLerngruppenLehrer, zuordnung);
			this.unterrichtLerngruppenlehrerMenge.add(zuordnung);
		}
	}

	private unterrichtLerngruppenlehrerCheck(zuordnung: UvUnterrichtLerngruppenlehrer): void {
		DeveloperNotificationException.ifInvalidID("zuordnung.idPlanungsabschnitt", zuordnung.idPlanungsabschnitt);
		DeveloperNotificationException.ifInvalidID("zuordnung.idUnterricht", zuordnung.idUnterricht);
		DeveloperNotificationException.ifInvalidID("zuordnung.idLerngruppenLehrer", zuordnung.idLerngruppenLehrer);
		if (!this.planungsabschnittById.containsKey(zuordnung.idPlanungsabschnitt)) {
			throw new DeveloperNotificationException("unterrichtLerngruppenlehrerCheck: Der UvPlanungsabschnitt mit der ID " + zuordnung.idPlanungsabschnitt + " existiert nicht.");
		}
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvUnterrichtLerngruppenlehrer}-Objekt. <br>
	 * @param idPlanungsabschnitt Die ID des Planungsabschnitts.
	 * @param idUnterricht Die ID des Unterrichts.
	 * @param idLerngruppenLehrer Die ID des Lerngruppenlehrers.
	 * @return das zur ID zugehörige {@link UvUnterrichtLerngruppenlehrer}-Objekt.
	 */
	public unterrichtLerngruppenlehrerGetByIdOrException(idPlanungsabschnitt: number, idUnterricht: number, idLerngruppenLehrer: number): UvUnterrichtLerngruppenlehrer {
		return this.unterrichtLerngruppenlehrerByIdPlanungsabschnittAndIdUnterrichtAndIdLerngruppenlehrer.getSingle123OrException(idPlanungsabschnitt, idUnterricht, idLerngruppenLehrer);
	}

	/**
	 * Liefert eine Liste aller {@link UvUnterrichtLerngruppenlehrer}-Objekte. <br>
	 * @return eine Liste aller {@link UvUnterrichtLerngruppenlehrer}-Objekte.
	 */
	public unterrichtLerngruppenlehrerGetMengeAsList(): List<UvUnterrichtLerngruppenlehrer> {
		return new ArrayList<UvUnterrichtLerngruppenlehrer>(this.unterrichtLerngruppenlehrerMenge);
	}

	private unterrichtLerngruppenlehrerRemoveOhneUpdateById(idPlanungsabschnitt: number, idUnterricht: number, idLerngruppenLehrer: number): void {
		this.unterrichtLerngruppenlehrerRemoveOhneUpdate(this.unterrichtLerngruppenlehrerGetByIdOrException(idPlanungsabschnitt, idUnterricht, idLerngruppenLehrer));
	}

	/**
	 * Entfernt ein existierendes {@link UvUnterrichtLerngruppenlehrer}-Objekt.
	 * @param idPlanungsabschnitt Die ID des Planungsabschnitts.
	 * @param idUnterricht Die ID des Unterrichts.
	 * @param idLerngruppenLehrer Die ID des Lerngruppenlehrers.
	 */
	public unterrichtLerngruppenlehrerRemoveById(idPlanungsabschnitt: number, idUnterricht: number, idLerngruppenLehrer: number): void {
		this.unterrichtLerngruppenlehrerRemoveOhneUpdateById(idPlanungsabschnitt, idUnterricht, idLerngruppenLehrer);
		this.updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvUnterrichtLerngruppenlehrer}-Objekt.
	 * @param zuordnung das zu entfernende {@link UvUnterrichtLerngruppenlehrer}-Objekt.
	 */
	public unterrichtLerngruppenlehrerRemove(zuordnung: UvUnterrichtLerngruppenlehrer): void {
		this.unterrichtLerngruppenlehrerRemoveOhneUpdate(zuordnung);
		this.updateAll();
	}

	private unterrichtLerngruppenlehrerRemoveOhneUpdate(zuordnung: UvUnterrichtLerngruppenlehrer): void {
		this.unterrichtLerngruppenlehrerByIdPlanungsabschnittAndIdUnterrichtAndIdLerngruppenlehrer.removeSingleOrException(zuordnung.idPlanungsabschnitt, zuordnung.idUnterricht, zuordnung.idLerngruppenLehrer);
		this.unterrichtLerngruppenlehrerMenge.remove(zuordnung);
	}

	/**
	 * Entfernt alle {@link UvUnterrichtLerngruppenlehrer}-Objekte.
	 * @param listZuordnungen Die Liste der zu entfernenden {@link UvUnterrichtLerngruppenlehrer}-Objekte.
	 */
	public unterrichtLerngruppenlehrerRemoveAll(listZuordnungen: Collection<UvUnterrichtLerngruppenlehrer>): void {
		this.unterrichtLerngruppenlehrerRemoveAllOhneUpdate(listZuordnungen);
		this.updateAll();
	}

	private unterrichtLerngruppenlehrerRemoveAllOhneUpdate(listZuordnungen: Collection<UvUnterrichtLerngruppenlehrer>): void {
		const setZuordnungen: JavaSet<UvUnterrichtLerngruppenlehrer> | null = new HashSet<UvUnterrichtLerngruppenlehrer>(listZuordnungen);
		for (const zuordnung of setZuordnungen) {
			this.unterrichtLerngruppenlehrerRemoveOhneUpdate(zuordnung);
		}
	}

	/**
	 * Liefert eine Liste aller {@link UvUnterrichtLerngruppenlehrer}-Objekte für einen Planungsabschnitt. <br>
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}.
	 * @return eine Liste aller {@link UvUnterrichtLerngruppenlehrer}-Objekte für den Planungsabschnitt.
	 */
	public unterrichtLerngruppenlehrerGetMengeByPlanungsabschnitt(planungsabschnitt: UvPlanungsabschnitt): List<UvUnterrichtLerngruppenlehrer> {
		return this.unterrichtLerngruppenlehrerByIdPlanungsabschnittAndIdUnterrichtAndIdLerngruppenlehrer.get1(planungsabschnitt.id);
	}

	/**
	 * Liefert eine Liste aller {@link UvUnterrichtLerngruppenlehrer}-Objekte für einen Unterricht. <br>
	 * @param unterricht der {@link UvUnterricht}.
	 * @return eine Liste aller {@link UvUnterrichtLerngruppenlehrer}-Objekte für den Unterricht.
	 */
	public unterrichtLerngruppenlehrerGetMengeByUnterricht(unterricht: UvUnterricht): List<UvUnterrichtLerngruppenlehrer> {
		return this.unterrichtLerngruppenlehrerByIdPlanungsabschnittAndIdUnterrichtAndIdLerngruppenlehrer.get12(unterricht.idPlanungsabschnitt, unterricht.id);
	}

	/**
	 * Liefert eine Liste aller {@link UvLehrerPflichtstundensoll}-Objekte für einen Lehrer. <br>
	 * @param lehrer der {@link UvLehrer}
	 * @return eine Liste aller {@link UvLehrerPflichtstundensoll}-Objekte für den Lehrer.
	 */
	public lehrerPflichtstundensollGetMengeByLehrer(lehrer: UvLehrer): List<UvLehrerPflichtstundensoll> {
		const list: List<UvLehrerPflichtstundensoll> | null = this.lehrerPflichtstundensollMengeByLehrerId.get(lehrer.id);
		return (list === null) ? new ArrayList() : new ArrayList(list);
	}

	/**
	 * Liefert eine Liste aller {@link UvLehrer}-Objekte für den aktuellen Planungsabschnitt. <br>
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}
	 * @return eine Liste aller {@link UvLehrer}-Objekte für den aktuellen Planungsabschnitt.
	 */
	public lehrerGetMengeByPlanungsabschnitt(planungsabschnitt: UvPlanungsabschnitt): List<UvLehrer> {
		const list: List<UvLehrer> = new ArrayList<UvLehrer>();
		for (const planungsabschnittLehrer of this.planungsabschnittLehrerByIdPlanungsabschnittAndIdLehrer.get1(planungsabschnitt.id)) {
			list.add(this.lehrerGetByPlanungsabschnittLehrer(planungsabschnittLehrer));
		}
		list.sort(this.compLehrer);
		return list;
	}

	/**
	 * Liefert einen {@link UvLehrer}, der dem angegebenen {@link UvPlanungsabschnittLehrer} zugeordnet ist.
	 *
	 * @param planungsabschnittLehrer Der {@link UvPlanungsabschnittLehrer}, für den der zugehörige {@link UvLehrer} abgerufen werden soll.
	 * @return Der {@link UvLehrer}, der dem angegebenen {@link UvPlanungsabschnittLehrer} entspricht.
	 * @throws DeveloperNotificationException Wenn kein {@link UvLehrer} mit der angegebenen ID gefunden wird.
	 */
	public lehrerGetByPlanungsabschnittLehrer(planungsabschnittLehrer: UvPlanungsabschnittLehrer): UvLehrer {
		return DeveloperNotificationException.ifMapGetIsNull(this.lehrerById, planungsabschnittLehrer.idLehrer);
	}

	/**
	 * Liefert den {@link UvPlanungsabschnittSchueler}, der durch die übergebene {@link UvSchuelergruppeSchueler}
	 * identifiziert wird.
	 *
	 * @param schuelergruppeSchueler Das {@link UvSchuelergruppeSchueler}-Objekt, welches die Identifikationsinformationen
	 *                               für den Planungsabschnittsschüler beinhaltet.
	 * @return Der gefundene {@link UvPlanungsabschnittSchueler}.
	 */
	public planungsabschnittSchuelerGetBySchuelergruppeSchueler(schuelergruppeSchueler: UvSchuelergruppeSchueler): UvPlanungsabschnittSchueler {
		return this.planungsabschnittSchuelerGetByIdOrException(schuelergruppeSchueler.idPlanungsabschnitt, schuelergruppeSchueler.idSchueler);
	}

	/**
	 * Liefert eine Liste aller {@link UvLehrerAnrechnungsstunden}-Objekte für einen Lehrer. <br>
	 * @param lehrer der {@link UvLehrer}
	 * @return eine Liste aller {@link UvLehrerAnrechnungsstunden}-Objekte für den Lehrer.
	 */
	public lehrerAnrechnungsstundenGetMengeByLehrer(lehrer: UvLehrer): List<UvLehrerAnrechnungsstunden> {
		const list: List<UvLehrerAnrechnungsstunden> | null = this.lehrerAnrechnungsstundenMengeByLehrerId.get(lehrer.id);
		return (list === null) ? new ArrayList() : new ArrayList(list);
	}

	/**
	 * Liefert {@code true}, wenn der Lehrer für diesen Planungsabschnitt angelegt ist.
	 * @param lehrer der {@link UvLehrer}
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}
	 * @return {@code true}, wenn der Lehrer für diesen Planungsabschnitt angelegt ist.
	 */
	public lehrerIsInPlanungsabschnitt(lehrer: UvLehrer, planungsabschnitt: UvPlanungsabschnitt): boolean {
		return this.planungsabschnittLehrerByIdPlanungsabschnittAndIdLehrer.containsKey12(planungsabschnitt.id, lehrer.id);
	}

	/**
	 * Liefert eine Liste aller fehlenden {@link UvLehrer}-Objekte, die zwar im Zeitraum des angegebenen Planungsabschnitts an der Schule tätig waren, aber
	 * nicht für den Planungsabschnitt angelegt sind.
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}
	 * @return eine Liste aller fehlenden {@link UvLehrer}-Objekte für den aktuellen Planungsabschnitt.
	 */
	public lehrerGetMengeTaetigAberNichtInPlanungsabschnitt(planungsabschnitt: UvPlanungsabschnitt): List<UvLehrer> {
		const result: List<UvLehrer> = new ArrayList<UvLehrer>();
		for (const l of this.lehrerMenge) {
			if (this.lehrerIsInPlanungsabschnitt(l, planungsabschnitt)) {
				continue;
			}
			const taetigImZeitraum: boolean = ((l.datumZugang === null) || (JavaString.compareTo(l.datumZugang, planungsabschnitt.gueltigBis) <= 0)) && ((l.datumAbgang === null) || (JavaString.compareTo(l.datumAbgang, planungsabschnitt.gueltigVon) >= 0));
			if (taetigImZeitraum) {
				result.add(l);
			}
		}
		return result;
	}

	/**
	 * Prüft für die übergebene Menge, welche {@link UvLehrer} referenziert werden und gibt diese als Menge zurück.
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}
	 * @param lehrer die zu prüfende Menge
	 * @return die Menge der verwendeten {@link UvLehrer}
	 */
	public lehrerGetMengeVerwendetInPlanungsabschnittByLehrerMenge(planungsabschnitt: UvPlanungsabschnitt, lehrer: Collection<UvLehrer>): JavaSet<UvLehrer> {
		const result: JavaSet<UvLehrer> = new HashSet<UvLehrer>();
		for (const l of new HashSet(lehrer)) {
			if (this.lerngruppenLehrerByIdPlanungsabschnittAndIdLerngruppeAndIdLehrer.containsKey13(planungsabschnitt.id, l.id)) {
				result.add(l);
			}
		}
		return result;
	}

	/**
	 * Prüft für die übergebene Menge, welche {@link UvLehrer} referenziert werden und gibt diese als Menge zurück.
	 * @param lehrer die zu prüfende Menge
	 * @return die Menge der verwendeten {@link UvLehrer}
	 */
	public lehrerGetMengeVerwendetByLehrerMenge(lehrer: Collection<UvLehrer>): JavaSet<UvLehrer> {
		const result: JavaSet<UvLehrer> = new HashSet<UvLehrer>();
		for (const l of new HashSet(lehrer)) {
			if (this.planungsabschnittLehrerByIdPlanungsabschnittAndIdLehrer.containsKey2(l.id)) {
				result.add(l);
			}
		}
		return result;
	}

	/**
	 * Liefert eine Liste aller {@link UvRaum}-Objekte für eine {@link UvRaumgruppe}. <br>
	 * @param raumgruppe die {@link UvRaumgruppe}
	 * @return eine Liste aller {@link UvRaum}-Objekte für die {@link UvRaumgruppe}.
	 */
	public raumGetMengeByRaumgruppe(raumgruppe: UvRaumgruppe): List<UvRaum> {
		return this.raumByIdRaumgruppeAndIdRaum.get1(raumgruppe.id);
	}

	/**
	 * Liefert den {@link UvRaum} zum übergebenen Kürzel oder {@code null}, falls kein Raum mit diesem Kürzel existiert.
	 *
	 * @param kuerzel das Kürzel des gesuchten {@link UvRaum}s
	 * @return den zugehörigen {@link UvRaum} oder {@code null}, falls kein passender Raum existiert
	 */
	public raumGetByKuerzelOrNull(kuerzel: string): UvRaum | null {
		return this.raumByKuerzel.get(kuerzel);
	}

	/**
	 * Liefert eine Liste aller {@link UvStundentafelFach}-Objekte für eine Stundentafel. <br>
	 * @param stundentafel die {@link UvStundentafel}
	 * @param abschnitt der Abschnitt des Schuljahres
	 * @return eine Liste aller {@link UvStundentafelFach}-Objekte für die Stundentafel.
	 */
	public stundentafelFachGetMengeByStundentafelAndAbschnitt(stundentafel: UvStundentafel, abschnitt: number): List<UvStundentafelFach> {
		const list: List<UvStundentafelFach> | null = this.stundentafelFachByIdStundentafelAndAbschnittAndIdFachAndIdStundentafelFach.get12(stundentafel.id, abschnitt);
		return (list === null) ? new ArrayList() : new ArrayList(list);
	}

	/**
	 * Liefert das {@link UvFach}-Objekt für ein {@link UvStundentafelFach}-Objekt.
	 * @param stundentafelFach das {@link UvStundentafelFach}-Objekt
	 * @return das zugehörige {@link UvFach}-Objekt
	 */
	public fachGetByStundentafelFach(stundentafelFach: UvStundentafelFach): UvFach {
		return this.fachGetByIdOrException(stundentafelFach.idFach);
	}

	/**
	 * Liefert eine Liste aller {@link UvFach}-Objekte, die im Gültigkeitsintervall
	 * der übergebenen {@link UvStundentafel} gültig sind.
	 *
	 * @param stundentafel   die {@link UvStundentafel}, deren Gültigkeitsintervall geprüft wird
	 * @param abschnitt      das Halbjahr
	 * @return die Liste der Fächer, die im Gültigkeitsintervall der Stundentafel gültig sind
	 */
	public fachGetMengeGueltigUndFehlendByStundentafel(stundentafel: UvStundentafel, abschnitt: number): List<UvFach> {
		const list: List<UvFach> = new ArrayList<UvFach>();
		for (const fach of this.fachMenge) {
			if (this.stundentafelFachByIdStundentafelAndAbschnittAndIdFachAndIdStundentafelFach.containsKey123(stundentafel.id, abschnitt, fach.id)) {
				continue;
			}
			if (DateUtils.intervallUeberlappt(stundentafel.gueltigVon, stundentafel.gueltigBis, fach.gueltigVon, fach.gueltigBis)) {
				list.add(fach);
			}
		}
		return list;
	}

	/**
	 * Prüft, ob das übergebene {@link UvFach}-Objekt einen Gültigkeitskonflikt mit einem anderen
	 * {@link UvFach}-Objekt derselben Fachart hat.
	 *
	 * @param fach   das zu prüfende Fach
	 * @return das andere Fach mit Gültigkeitskonflikt oder {@code null}, wenn kein Konflikt besteht
	 */
	public fachGetByGueltigkeitskonfliktMitFach(fach: UvFach): UvFach | null {
		return this.fachGetByGueltigkeitskonfliktMitNeueGueltigkeit(fach, fach.gueltigVon, fach.gueltigBis);
	}

	/**
	 * Prüft, ob das übergebene {@link UvFach}-Objekt einen Gültigkeitskonflikt mit einem anderen
	 * {@link UvFach}-Objekt derselben Fachart hat.
	 *
	 * @param fach   das zu prüfende Fach
	 * @param gueltigVon der neue Beginn des Gültigkeitsintervalls
	 * @param gueltigBis das neue Ende des Gültigkeitsintervalls
	 * @return das andere Fach mit Gültigkeitskonflikt oder {@code null}, wenn kein Konflikt besteht
	 */
	public fachGetByGueltigkeitskonfliktMitNeueGueltigkeit(fach: UvFach, gueltigVon: string, gueltigBis: string | null): UvFach | null {
		for (const otherFach of this.fachMenge) {
			if ((otherFach.idFach !== fach.idFach) || (otherFach.id === fach.id)) {
				continue;
			}
			if (DateUtils.intervallUeberlappt(gueltigVon, gueltigBis, otherFach.gueltigVon, otherFach.gueltigBis)) {
				return otherFach;
			}
		}
		return null;
	}

	/**
	 * Liefert eine Liste aller {@link UvRaumgruppe}-Objekte, die im Gültigkeitsintervall
	 * des übergebenen {@link UvRaum} gültig sind.
	 *
	 * @param raum   der {@link UvRaum}, dessen Gültigkeitsintervall geprüft wird
	 * @return die Liste der Raumgruppen, die im Gültigkeitsintervall des Raums gültig sind
	 */
	public raumgruppeGetMengeGueltigByRaum(raum: UvRaum): List<UvRaumgruppe> {
		return this.raumgruppeGetMengeGueltigByZeitraum(raum.gueltigVon, raum.gueltigBis);
	}

	/**
	 * Liefert eine Liste aller {@link UvRaumgruppe}-Objekte, die im übergebenen Gültigkeitsintervall gültig sind.
	 *
	 * @param gueltigVon Beginn des Gültigkeitsintervalls
	 * @param gueltigBis Ende des Gültigkeitsintervalls
	 * @return die Liste der Raumgruppen, die im Gültigkeitsintervall des Raums gültig sind
	 */
	public raumgruppeGetMengeGueltigByZeitraum(gueltigVon: string, gueltigBis: string | null): List<UvRaumgruppe> {
		const list: List<UvRaumgruppe> = new ArrayList<UvRaumgruppe>();
		for (const raumgruppe of this.raumgruppeMenge) {
			if (DateUtils.intervallUeberlappt(gueltigVon, gueltigBis, raumgruppe.gueltigVon, raumgruppe.gueltigBis)) {
				list.add(raumgruppe);
			}
		}
		return list;
	}

	/**
	 * Gibt eine Liste aller {@link UvFach}-Objekte zurück, die zu den gegebenen {@link FachDaten} gehören.
	 *
	 * @param fachdaten   Die {@link FachDaten} (Schulfach).
	 *
	 * @return Eine Liste der {@link UvFach}-Objekte zu den {@link FachDaten}.
	 */
	public fachGetMengeByFachdaten(fachdaten: FachDaten): List<UvFach> {
		return this.fachMengeByIdFach.get(fachdaten.id);
	}

	/**
	 * Liefert das zum Fach und Planungsabschnitt gehörige {@link UvFach}-Objekt oder {@code null}.
	 *
	 * @param idFach die ID des Schulfachs
	 * @param planungsabschnitt der Planungsabschnitt
	 * @return das passende {@link UvFach}-Objekt oder {@code null}
	 */
	public fachGetByIdFachAndPlanungsabschnittOrNull(idFach: number, planungsabschnitt: UvPlanungsabschnitt): UvFach | null {
		for (const fach of this.fachMengeByIdFach.get(idFach)) {
			if (DateUtils.intervallUeberlappt(planungsabschnitt.gueltigVon, planungsabschnitt.gueltigBis, fach.gueltigVon, fach.gueltigBis)) {
				return fach;
			}
		}
		return null;
	}

	/**
	 * Liefert eine Liste aller {@link UvZeitrasterEintrag}-Objekte für ein bestimmtes Zeitraster. <br>
	 * @param idZeitraster Die ID des Zeitrasters.
	 * @return eine Liste aller {@link UvZeitrasterEintrag}-Objekte für das Zeitraster.
	 */
	public zeitrasterEintragGetMengeByZeitraster(idZeitraster: number): List<UvZeitrasterEintrag> {
		return this.zeitrasterEintragByIdZeitrasterAndIdZeitrasterEintrag.get1(idZeitraster);
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvPlanungsabschnittZeitraster}-Objekt. <br>
	 * @param idPlanungsabschnitt Die ID des Planungsabschnitts.
	 * @param idZeitraster Die ID des Zeitrasters.
	 * @return das zur ID zugehörige {@link UvPlanungsabschnittZeitraster}-Objekt.
	 */
	public planungsabschnittZeitrasterGetByIdOrException(idPlanungsabschnitt: number, idZeitraster: number): UvPlanungsabschnittZeitraster {
		return this.planungsabschnittZeitrasterByIdPlanungsabschnittAndIdZeitraster.getSingle12OrException(idPlanungsabschnitt, idZeitraster);
	}

	/**
	 * Liefert eine Liste aller {@link UvPlanungsabschnittZeitraster}-Objekte für einen bestimmten Planungsabschnitt. <br>
	 * @param idPlanungsabschnitt Die ID des Planungsabschnitts.
	 * @return eine Liste aller {@link UvPlanungsabschnittZeitraster}-Objekte für den Planungsabschnitt.
	 */
	public planungsabschnittZeitrasterGetMengeByPlanungsabschnitt(idPlanungsabschnitt: number): List<UvPlanungsabschnittZeitraster> {
		return this.planungsabschnittZeitrasterByIdPlanungsabschnittAndIdZeitraster.get1(idPlanungsabschnitt);
	}

	/**
	 * Prüft, ob ein {@link UvZeitraster} einem {@link UvPlanungsabschnitt} zugeordnet ist.
	 * @param zeitraster das {@link UvZeitraster}
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}
	 * @return {@code true}, wenn das Zeitraster dem Planungsabschnitt zugeordnet ist, sonst {@code false}.
	 */
	public zeitrasterIsInPlanungsabschnitt(zeitraster: UvZeitraster, planungsabschnitt: UvPlanungsabschnitt): boolean {
		return this.planungsabschnittZeitrasterByIdPlanungsabschnittAndIdZeitraster.containsKey12(planungsabschnitt.id, zeitraster.id);
	}

	/**
	 * Liefert eine Liste aller {@link UvZeitraster}-Objekte, die einem Planungsabschnitt zugeordnet sind. <br>
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}.
	 * @return eine Liste aller {@link UvZeitraster}-Objekte für den Planungsabschnitt.
	 */
	public zeitrasterGetMengeByPlanungsabschnitt(planungsabschnitt: UvPlanungsabschnitt): List<UvZeitraster> {
		const result: List<UvZeitraster> = new ArrayList<UvZeitraster>();
		for (const zuordnung of this.planungsabschnittZeitrasterByIdPlanungsabschnittAndIdZeitraster.get1(planungsabschnitt.id)) {
			result.add(this.zeitrasterGetByIdOrException(zuordnung.idZeitraster));
		}
		return result;
	}

	/**
	 * Liefert eine Liste aller {@link UvZeitraster}-Objekte, die im Gültigkeitsintervall des übergebenen
	 * {@link UvPlanungsabschnitt} gültig sind.
	 *
	 * @param planungsabschnitt   der {@link UvPlanungsabschnitt}, dessen Gültigkeitsintervall geprüft wird
	 * @return die Liste der {@link UvZeitraster}, die im Gültigkeitsintervall gültig sind
	 */
	public zeitrasterGetMengeGueltigByPlanungsabschnitt(planungsabschnitt: UvPlanungsabschnitt): List<UvZeitraster> {
		const set: JavaSet<UvZeitraster> = new HashSet<UvZeitraster>();
		for (const zeitraster of this.zeitrasterMenge) {
			if (DateUtils.intervallUeberlappt(planungsabschnitt.gueltigVon, planungsabschnitt.gueltigBis, zeitraster.gueltigVon, zeitraster.gueltigBis)) {
				set.add(zeitraster);
			}
		}
		set.addAll(this.zeitrasterGetMengeByPlanungsabschnitt(planungsabschnitt));
		const list: List<UvZeitraster> = new ArrayList<UvZeitraster>(set);
		list.sort(this.compZeitraster);
		return list;
	}

	/**
	 * Liefert die zu einem {@link UvPlanungsabschnittZeitraster} gehörigen Jahrgänge.
	 * @param zeitraster das {@link UvPlanungsabschnittZeitraster}-Objekt
	 * @return die Liste der zugeordneten {@link JahrgangsDaten}
	 */
	public jahrgangsdatenGetMengeByPlanungsabschnittZeitraster(zeitraster: UvPlanungsabschnittZeitraster): List<JahrgangsDaten> {
		const result: List<JahrgangsDaten> = new ArrayList<JahrgangsDaten>();
		for (const jahrgangId of zeitraster.idsJahrgaenge) {
			result.add(DeveloperNotificationException.ifMapGetIsNull(this.jahrgangsdatenByIdMapGet(), jahrgangId));
		}
		return result;
	}

	/**
	 * Liefert das {@link UvPlanungsabschnittZeitraster}-Objekt für den übergebenen {@link UvPlanungsabschnitt} und das {@link UvZeitraster}.
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}
	 * @param zeitraster das {@link UvZeitraster}
	 * @return das zugehörige {@link UvPlanungsabschnittZeitraster}-Objekt
	 */
	public planungsabschnittZeitrasterGetByPlanungsabschnittAndZeitraster(planungsabschnitt: UvPlanungsabschnitt, zeitraster: UvZeitraster): UvPlanungsabschnittZeitraster {
		return this.planungsabschnittZeitrasterByIdPlanungsabschnittAndIdZeitraster.getSingle12OrException(planungsabschnitt.id, zeitraster.id);
	}

	/**
	 * Liefert die zu einem {@link UvPlanungsabschnittZeitraster} gehörigen Jahrgänge.
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}
	 * @param zeitraster das {@link UvZeitraster}
	 * @return die Liste der zugeordneten {@link JahrgangsDaten}
	 */
	public jahrgangsdatenGetMengeByPlanungsabschnittAndZeitraster(planungsabschnitt: UvPlanungsabschnitt, zeitraster: UvZeitraster): List<JahrgangsDaten> {
		return this.jahrgangsdatenGetMengeByPlanungsabschnittZeitraster(this.planungsabschnittZeitrasterGetByPlanungsabschnittAndZeitraster(planungsabschnitt, zeitraster));
	}

	/**
	 * Liefert die Liste aller in Stundentafeln verwendeter {@link UvFach}-Objekte.
	 * @return die Liste aller in Stundentafeln verwendeter {@link UvFach}-Objekte.
	 */
	public fachGetMengeVerwendetInStundentafel(): List<UvFach> {
		const result: List<UvFach> = new ArrayList<UvFach>(this.fachGetMengeAsList());
		const it = result.iterator();
		while (it.hasNext()) {
			const fach: UvFach | null = it.next();
			if (!this.stundentafelFachByIdStundentafelAndAbschnittAndIdFachAndIdStundentafelFach.containsKey3(fach.id)) {
				it.remove();
			}
		}
		return result;
	}

	/**
	 * Liefert eine Liste aller {@link UvPlanungsabschnittSchueler}-Objekte für einen bestimmten Planungsabschnitt. <br>
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}.
	 * @return eine Liste aller {@link UvPlanungsabschnittSchueler}-Objekte für den Planungsabschnitt.
	 */
	public planungsabschnittSchuelerGetMengeByPlanungsabschnitt(planungsabschnitt: UvPlanungsabschnitt): List<UvPlanungsabschnittSchueler> {
		return this.planungsabschnittSchuelerByIdPlanungsabschnittAndIdJahrgangAndIdKlasseAndIdSchueler.get1(planungsabschnitt.id);
	}

	/**
	 * Liefert zum Planungsabschnitt und der Nummer der Schiene das {@link UvSchiene}-Objekt, falls es existiert, sonst {@code null}.
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}
	 * @param nummer die Nummer der Schiene
	 * @return die Schiene, falls sie existiert
	 */
	public schieneGetByPlanungsabschnittAndNummer(planungsabschnitt: UvPlanungsabschnitt, nummer: number): UvSchiene | null {
		return this.schieneByIdPlanungsabschnittAndNummerAndIdSchiene.getSingle12OrNull(planungsabschnitt.id, nummer);
	}

	/**
	 * Liefert alle Schüler einer Gruppe
	 * @param gruppe die {@link UvSchuelergruppe}
	 * @return die Liste von {@link UvPlanungsabschnittSchueler}-Objekten
	 */
	public planungsabschnittSchuelerGetMengeBySchuelergruppe(gruppe: UvSchuelergruppe): List<UvPlanungsabschnittSchueler> {
		return this.planungsabschnittSchuelerMengeByIdPlanungsabschnittAndIdSchuelergruppe.get2(gruppe.id);
	}

	/**
	 * Liefert alle Schüler einer {@link UvKlasse}
	 * @param klasse die {@link UvKlasse}
	 * @return die Liste von {@link UvPlanungsabschnittSchueler}-Objekten
	 */
	public planungsabschnittSchuelerGetMengeByKlasse(klasse: UvKlasse): List<UvPlanungsabschnittSchueler> {
		return this.planungsabschnittSchuelerMengeByIdPlanungsabschnittAndIdSchuelergruppe.get2(klasse.idSchuelergruppe);
	}

	/**
	 * Gibt zurück, ob der Schülerjahrgang in die Schülergruppe passt.
	 * @param schueler der zu prüfende {@link UvSchuelergruppeSchueler}
	 * @return {@code true}, falls der Jahrgang in der Gruppe zugelassen ist, sonst {@code false}
	 */
	public schuelergruppeSchuelerHatZurGruppePassendenJahrgang(schueler: UvSchuelergruppeSchueler): boolean {
		const s: UvPlanungsabschnittSchueler = this.planungsabschnittSchuelerGetBySchuelergruppeSchueler(schueler);
		const g: UvSchuelergruppe = this.schuelergruppeGetByIdOrException(schueler.idSchuelergruppe);
		return g.idsJahrgaengeErlaubt.contains(s.idJahrgang);
	}

	/**
	 * Gibt zurück, ob die {@link UvSchuelergruppe} Schüler mit falscher Jahrgangszugehörigkeit beinhaltet
	 * @param gruppe die zu prüfende {@link UvSchuelergruppe}
	 * @return {@code true}, falls es widersprüchliche Daten gibt, sonst {@code false}
	 */
	public schuelergruppeHatSchuelerMitFalschemJahrgang(gruppe: UvSchuelergruppe): boolean {
		for (const schueler of this.schuelergruppeSchuelerByIdPlanungsabschnittAndIdSchuelergruppeAndIdSchueler.get2(gruppe.id)) {
			if (!this.schuelergruppeSchuelerHatZurGruppePassendenJahrgang(schueler)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gibt zurück, ob der Schülerjahrgang in die Schülergruppe passt.
	 * @param schueler der zu prüfende {@link UvPlanungsabschnittSchueler}
	 * @param gruppe die zu prüfende {@link UvSchuelergruppe}
	 * @return {@code true}, falls der Jahrgang in der Gruppe zugelassen ist, sonst {@code false}
	 */
	public planungsabschnittSchuelerHatZurGruppePassendenJahrgang(schueler: UvPlanungsabschnittSchueler, gruppe: UvSchuelergruppe): boolean {
		const s: UvSchuelergruppeSchueler = this.schuelergruppeSchuelerByIdPlanungsabschnittAndIdSchuelergruppeAndIdSchueler.getSingle123OrException(schueler.idPlanungsabschnitt, gruppe.id, schueler.idSchueler);
		return this.schuelergruppeSchuelerHatZurGruppePassendenJahrgang(s);
	}

	/**
	 * Liefert die {@link UvSchuelergruppe} zu einer {@link UvKlasse}
	 * @param klasse die {@link UvKlasse}, zu der die {@link UvSchuelergruppe} gesucht wird
	 * @return die {@link UvSchuelergruppe}
	 */
	public schuelergruppeGetByKlasse(klasse: UvKlasse): UvSchuelergruppe {
		return this.schuelergruppeGetByIdOrException(klasse.idSchuelergruppe);
	}

	/**
	 * Liefert die {@link UvSchuelergruppe} zu einem {@link UvKurs}
	 * @param kurs der {@link UvKurs}, zu dem die {@link UvSchuelergruppe} gesucht wird
	 * @return die {@link UvSchuelergruppe}
	 */
	public schuelergruppeGetByKurs(kurs: UvKurs): UvSchuelergruppe {
		return this.schuelergruppeGetByIdOrException(kurs.idSchuelergruppe);
	}

	/**
	 * Prüft für die übergebene Menge, welche {@link UvKlasse}n referenziert werden und gibt diese als Menge zurück.
	 * @param klassen die zu prüfende Menge
	 * @return die Menge der verwendeten {@link UvKlasse}n
	 */
	public klasseGetMengeVerwendetByKlasseMenge(klassen: Collection<UvKlasse>): JavaSet<UvKlasse> {
		const result: JavaSet<UvKlasse> = new HashSet<UvKlasse>();
		for (const klasse of new HashSet(klassen)) {
			if (this.planungsabschnittSchuelerByIdPlanungsabschnittAndIdJahrgangAndIdKlasseAndIdSchueler.containsKey3(klasse.id) || this.lerngruppeByIdKursAndIdKlasseAndIdFach.containsKey2(klasse.id)) {
				result.add(klasse);
			}
		}
		return result;
	}

	/**
	 * Prüft für die übergebene Menge, welche {@link UvSchiene}n referenziert werden und gibt diese als Menge zurück.
	 * @param schienen die zu prüfende Menge
	 * @return die Menge der verwendeten {@link UvSchiene}n
	 */
	public schieneGetMengeVerwendetBySchieneMenge(schienen: Collection<UvSchiene>): JavaSet<UvSchiene> {
		const result: JavaSet<UvSchiene> = new HashSet<UvSchiene>();
		for (const schiene of new HashSet(schienen)) {
			if (this.lerngruppenSchieneByIdPlanungsabschnittAndIdLerngruppeAndIdSchiene.containsKey3(schiene.id)) {
				result.add(schiene);
			}
		}
		return result;
	}

	/**
	 * Prüft für die übergebene Menge, welche {@link UvSchuelergruppe}n referenziert werden und gibt diese als Menge zurück.
	 * @param schuelergruppen die zu prüfende Menge
	 * @return die Menge der verwendeten {@link UvSchuelergruppe}n
	 */
	public schuelergruppeGetMengeVerwendetBySchuelergruppeMenge(schuelergruppen: Collection<UvSchuelergruppe>): JavaSet<UvSchuelergruppe> {
		const result: JavaSet<UvSchuelergruppe> = new HashSet<UvSchuelergruppe>();
		for (const schuelergruppe of new HashSet(schuelergruppen)) {
			if (this.klasseByIdPlanungsabschnittAndIdSchuelergruppeAndIdKlasse.containsKey2(schuelergruppe.id) || this.kursByIdPlanungsabschnittAndIdSchuelergruppeAndIdKurs.containsKey2(schuelergruppe.id)) {
				result.add(schuelergruppe);
			}
		}
		return result;
	}

	/**
	 * Gibt die Menge der {@link UvLerngruppenSchiene}n zur übergebenen {@link UvSchiene} zurück.
	 * @param schiene die {@link UvSchiene}, zu der die {@link UvLerngruppe}n gesucht werden
	 * @return die Menge der zugeordneten {@link UvLerngruppenSchiene}-Objekte
	 */
	public lerngruppenSchieneGetMengeBySchiene(schiene: UvSchiene): List<UvLerngruppenSchiene> {
		return this.lerngruppenSchieneByIdPlanungsabschnittAndIdLerngruppeAndIdSchiene.get13(schiene.idPlanungsabschnitt, schiene.id);
	}

	/**
	 * Liefert das zur {@link UvLerngruppenSchiene} zugehörige {@link UvLerngruppe}-Objekt. <br>
	 * @param schiene die {@link UvLerngruppenSchiene}, zu der die {@link UvLerngruppe} gesucht wird.
	 * @return das zugehörige {@link UvLerngruppe}-Objekt.
	 */
	public lerngruppeGetByLerngruppenSchiene(schiene: UvLerngruppenSchiene): UvLerngruppe {
		return this.lerngruppeByIdPlanungsabschnittAndIdLerngruppe.getSingle12OrException(schiene.idPlanungsabschnitt, schiene.idLerngruppe);
	}

	/**
	 * Liefert die Fachdaten zum übergebenen Kurs
	 * @param kurs der {@link UvKurs}
	 * @return die {@link FachDaten}
	 */
	public fachdatenGetByKurs(kurs: UvKurs): FachDaten {
		return this.fachdatenGetByFach(this.fachGetByKurs(kurs));
	}

	/**
	 * Liefert das Fach zum übergebenen Kurs
	 * @param kurs der {@link UvKurs}
	 * @return das {@link UvFach}
	 */
	public fachGetByKurs(kurs: UvKurs): UvFach {
		return this.fachGetByIdOrException(kurs.idFach);
	}

	/**
	 * Liefert den Kurs zur Lerngruppe oder {@code null}, falls es sich um Klassenunterricht handelt
	 * @param lerngruppe die {@link UvLerngruppe}
	 * @return der zugeordnete {@link UvKurs} oder {@code null}, falls es sich um Klassenunterricht handelt
	 */
	public kursGetByLerngruppe(lerngruppe: UvLerngruppe): UvKurs | null {
		if (lerngruppe.idKurs === null) {
			return null;
		}
		return this.kursGetByIdOrException(lerngruppe.idKurs);
	}

	/**
	 * Liefert die Klasse zur Lerngruppe oder {@code null}, falls es sich um Kursunterricht handelt
	 * @param lerngruppe die {@link UvLerngruppe}
	 * @return die zugeordnete {@link UvKlasse} oder {@code null}, falls es sich um Kursunterricht handelt
	 */
	public klasseGetByLerngruppe(lerngruppe: UvLerngruppe): UvKlasse | null {
		if (lerngruppe.idKlasse === null) {
			return null;
		}
		return this.klasseGetByIdOrException(lerngruppe.idKlasse);
	}

	/**
	 * Liefert alle {@link UvKlasse}-Objekte zur {@link UvLerngruppe}.
	 * Bei Klassenunterricht enthält die Liste genau die zugeordnete Klasse.
	 * Bei Kursunterricht enthält die Liste alle Klassen der zugehörigen Schülergruppe.
	 *
	 * @param lerngruppe die {@link UvLerngruppe}
	 * @return die zugeordneten {@link UvKlasse}-Objekte
	 */
	public klasseGetMengeByLerngruppe(lerngruppe: UvLerngruppe): List<UvKlasse> {
		if (lerngruppe.idKlasse !== null) {
			return ListUtils.create1(this.klasseGetByIdOrException(lerngruppe.idKlasse));
		}
		const kurs: UvKurs | null = this.kursGetByLerngruppe(lerngruppe);
		if (kurs === null) {
			return new ArrayList();
		}
		return this.klasseByIdPlanungsabschnittAndIdSchuelergruppeAndIdKlasse.get12(lerngruppe.idPlanungsabschnitt, kurs.idSchuelergruppe);
	}

	/**
	 * Liefert alle {@link JahrgangsDaten} zur {@link UvLerngruppe}.
	 * In der Regel enthält die Liste genau einen Jahrgang.
	 *
	 * @param lerngruppe die {@link UvLerngruppe}
	 * @return die zugeordneten {@link JahrgangsDaten}
	 */
	public jahrgangsdatenGetMengeByLerngruppe(lerngruppe: UvLerngruppe): List<JahrgangsDaten> {
		const schuelergruppe: UvSchuelergruppe | null = this.schuelergruppeGetByLerngruppe(lerngruppe);
		if (schuelergruppe === null) {
			return new ArrayList();
		}
		return this.jahrgangsdatenGetMengeBySchuelergruppe(schuelergruppe);
	}

	/**
	 * Liefert die tatsächlich vertretenen Jahrgänge der Schüler einer Schülergruppe ohne Duplikate.
	 * Die Liste ist nach Jahrgangssortierung und bei gleicher Sortierung nach ID sortiert.
	 * Erlaubte Jahrgänge ohne zugeordnete Schüler werden nicht berücksichtigt.
	 *
	 * @param schuelergruppe die auszuwertende Schülergruppe
	 * @return die sortierten Jahrgangsdaten der zugeordneten Schüler; bei leerer Gruppe eine leere Liste
	 */
	public jahrgangsdatenGetMengeBySchuelergruppe(schuelergruppe: UvSchuelergruppe): List<JahrgangsDaten> {
		const result: List<JahrgangsDaten> = new ArrayList<JahrgangsDaten>();
		const idsJahrgaenge: JavaSet<number> = new HashSet<number>();
		for (const schueler of this.planungsabschnittSchuelerGetMengeBySchuelergruppe(schuelergruppe)) {
			if (idsJahrgaenge.add(schueler.idJahrgang)) {
				result.add(this.jahrgangsdatenGetById(schueler.idJahrgang));
			}
		}
		result.sort(this.compJahrgangsdaten);
		return result;
	}

	/**
	 * Liefert die {@link UvSchuelergruppe} zur übergebenen {@link UvLerngruppe}.
	 * Bei Klassenunterricht wird die Schülergruppe über die Klasse ermittelt,
	 * bei Kursunterricht direkt über den {@link UvKurs}.
	 *
	 * @param lerngruppe die {@link UvLerngruppe}
	 * @return die zugehörige {@link UvSchuelergruppe} oder {@code null}, falls weder eine Klasse noch ein Kurs zugeordnet ist
	 */
	public schuelergruppeGetByLerngruppe(lerngruppe: UvLerngruppe): UvSchuelergruppe | null {
		const klasse: UvKlasse | null = this.klasseGetByLerngruppe(lerngruppe);
		if (klasse !== null) {
			return this.schuelergruppeGetByKlasse(klasse);
		}
		const kurs: UvKurs | null = this.kursGetByLerngruppe(lerngruppe);
		return (kurs === null) ? null : this.schuelergruppeGetByKurs(kurs);
	}

	/**
	 * Liefert das {@link UvFach} zur {@link UvLerngruppe} oder {@code null}, falls kein Fach zugeordnet ist.
	 * Bei Kursunterricht wird das Fach aus dem zugeordneten {@link UvKurs} ermittelt,
	 * bei Klassenunterricht direkt aus der {@link UvLerngruppe}.
	 *
	 * @param lerngruppe die {@link UvLerngruppe}
	 * @return das zugeordnete {@link UvFach} oder {@code null}
	 */
	public fachGetByLerngruppe(lerngruppe: UvLerngruppe): UvFach | null {
		const kurs: UvKurs | null = this.kursGetByLerngruppe(lerngruppe);
		if (kurs !== null) {
			return this.fachGetByIdOrException(kurs.idFach);
		}
		if (lerngruppe.idFach === null) {
			return null;
		}
		return this.fachGetByIdOrException(lerngruppe.idFach);
	}

	/**
	 * Liefert zu einer {@link UvSchuelergruppe} alle zugeordneten {@link UvLerngruppe}n
	 * @param schuelergruppe die {@link UvSchuelergruppe}
	 * @return die zugeordneten {@link UvLerngruppe}n
	 */
	public lerngruppeGetMengeBySchuelergruppe(schuelergruppe: UvSchuelergruppe): List<UvLerngruppe> {
		return this.lerngruppeMengeByIdSchuelergruppe.get(schuelergruppe.id);
	}

	/**
	 * Liefert das {@link UvLehrer}-Objekt zu einem {@link UvLerngruppenLehrer}.
	 * @param lehrer der {@link UvLerngruppenLehrer}
	 * @return der {@link UvLehrer}
	 */
	public lehrerGetByLerngruppenLehrer(lehrer: UvLerngruppenLehrer): UvLehrer {
		return this.lehrerGetByIdOrException(lehrer.idLehrer);
	}

	/**
	 * Liefert das {@link UvLehrer}-Objekt zu einem {@link UvKlassenLehrer}.
	 * @param klassenLehrer der {@link UvKlassenLehrer}
	 * @return der {@link UvLehrer}
	 */
	public lehrerGetByKlassenLehrer(klassenLehrer: UvKlassenLehrer): UvLehrer {
		return this.lehrerGetByIdOrException(klassenLehrer.idLehrer);
	}

	/**
	 * Liefert einen {@link UvLehrer} basierend auf dem angegebenen {@link UvLehrerPflichtstundensoll}.
	 *
	 * @param pflichtstundensoll das {@link UvLehrerPflichtstundensoll}, das die Pflichtstunden und die Lehrer-ID enthält
	 * @return der {@link UvLehrer}, der der angegebenen Lehrer-ID im {@link UvLehrerPflichtstundensoll} entspricht
	 */
	public lehrerGetByLehrerPflichtstundensoll(pflichtstundensoll: UvLehrerPflichtstundensoll): UvLehrer {
		return this.lehrerGetByIdOrException(pflichtstundensoll.idLehrer);
	}

	/**
	 * Ruft einen Lehrer basierend auf den Anrechnungsstunden ab.
	 *
	 * @param anrechnungsstunden die {@link UvLehrerAnrechnungsstunden}, die die relevanten Informationen enthalten,
	 *                           um den Lehrer zu identifizieren.
	 * @return der {@link UvLehrer}, der den angegebenen Anrechnungsstunden zugeordnet ist.
	 */
	public lehrerGetByLehrerAnrechnungsstunden(anrechnungsstunden: UvLehrerAnrechnungsstunden): UvLehrer {
		return this.lehrerGetByIdOrException(anrechnungsstunden.idLehrer);
	}

	/**
	 * Liefert das {@link UvSchiene}-Objekt zu einer {@link UvLerngruppenSchiene}.
	 * @param schiene die {@link UvLerngruppenSchiene}
	 * @return die {@link UvSchiene}
	 */
	public schieneGetByLerngruppenSchiene(schiene: UvLerngruppenSchiene): UvSchiene {
		return this.schieneGetByIdOrException(schiene.idSchiene);
	}

	/**
	 * Prüft, ob der {@link UvLehrer} eine Lehrbefähigung für das angegebene {@link UvFach} besitzt.
	 *
	 * TODO: Die Prüfung berücksichtigt bislang nur den Fach-Eintrag. Die Erweiterung um Sekundarstufe
	 * und Gültigkeitszeitraum sowie den dafür benötigten Kontext noch fachlich festlegen.
	 *
	 * @param lehrer der {@link UvLehrer}
	 * @param fach   das {@link UvFach}
	 * @return {@code true}, falls der Lehrer eine Lehrbefähigung für das UV-Fach besitzt, sonst {@code false}
	 */
	public lehrerHatLehrbefaehigungFach(lehrer: UvLehrer, fach: UvFach): boolean {
		return this.lehrerUnterrichtsfachByIdLehrerAndIdFach.getSingle12OrNull(lehrer.id, fach.idFach) !== null;
	}

	/**
	 * Prüft, ob der {@link UvLehrer} eine Lehrbefähigung für das Fach der angegebenen {@link UvLerngruppe} besitzt.
	 * Bei Klassenunterricht wird das Fach direkt aus der {@link UvLerngruppe} ermittelt,
	 * bei Kursunterricht aus dem zugeordneten {@link UvKurs}.
	 *
	 * TODO: Die Prüfung um Sekundarstufe und Gültigkeitszeitraum anhand der Jahrgänge und des
	 * Planungsabschnitts der Lerngruppe erweitern. Zuvor die zeitliche Gültigkeitsregel und den Umgang
	 * mit gemischten oder unbekannten Jahrgangsstufen sowie fehlenden Berechtigungsangaben festlegen.
	 *
	 * @param lehrer     der {@link UvLehrer}
	 * @param lerngruppe die {@link UvLerngruppe}
	 * @return {@code true}, falls der Lehrer eine Lehrbefähigung für das Fach der Lerngruppe besitzt, sonst {@code false}
	 */
	public lehrerHatLehrbefaehigungLerngruppe(lehrer: UvLehrer, lerngruppe: UvLerngruppe): boolean {
		const fach: UvFach | null = this.fachGetByLerngruppe(lerngruppe);
		if (fach === null) {
			return false;
		}
		return this.lehrerHatLehrbefaehigungFach(lehrer, fach);
	}

	/**
	 * Prüft, ob der {@link UvPlanungsabschnittLehrer} eine Lehrbefähigung für das angegebene {@link UvFach} besitzt.
	 *
	 * @param planungsabschnittLehrer der {@link UvPlanungsabschnittLehrer}
	 * @param fach                    das {@link UvFach}
	 * @return {@code true}, falls der Lehrer eine Lehrbefähigung für das Fach besitzt, sonst {@code false}
	 */
	public planungsabschnittLehrerHatLehrbefaehigungFach(planungsabschnittLehrer: UvPlanungsabschnittLehrer, fach: UvFach): boolean {
		return this.lehrerHatLehrbefaehigungFach(this.lehrerGetByIdOrException(planungsabschnittLehrer.idLehrer), fach);
	}

	/**
	 * Prüft, ob der {@link UvPlanungsabschnittLehrer} eine Lehrbefähigung für das Fach der angegebenen {@link UvLerngruppe} besitzt.
	 *
	 * @param planungsabschnittLehrer der {@link UvPlanungsabschnittLehrer}
	 * @param lerngruppe              die {@link UvLerngruppe}
	 * @return {@code true}, falls der Lehrer eine Lehrbefähigung für das Fach der Lerngruppe besitzt, sonst {@code false}
	 */
	public planungsabschnittLehrerHatLehrbefaehigungLerngruppe(planungsabschnittLehrer: UvPlanungsabschnittLehrer, lerngruppe: UvLerngruppe): boolean {
		return this.lehrerHatLehrbefaehigungLerngruppe(this.lehrerGetByIdOrException(planungsabschnittLehrer.idLehrer), lerngruppe);
	}

	/**
	 * Liefert das {@link UvLehrerPflichtstundensoll}, das am Beginn des {@link UvPlanungsabschnitt}s gültig ist,
	 * oder {@code null}, falls kein passendes Pflichtstundensoll existiert.
	 *
	 * @param lehrer             der {@link UvLehrer}
	 * @param planungsabschnitt  der {@link UvPlanungsabschnitt}
	 * @return das am Beginn des Planungsabschnitts gültige {@link UvLehrerPflichtstundensoll} oder {@code null}
	 */
	public lehrerPflichtstundensollGetByLehrerAndPlanungsabschnitt(lehrer: UvLehrer, planungsabschnitt: UvPlanungsabschnitt): UvLehrerPflichtstundensoll | null {
		const liste: List<UvLehrerPflichtstundensoll> | null = this.lehrerPflichtstundensollMengeByLehrerId.get(lehrer.id);
		if (liste === null) {
			return null;
		}
		const stichtag: string = planungsabschnitt.gueltigVon;
		for (const p of liste) {
			if ((JavaString.compareTo(p.gueltigVon, stichtag) <= 0) && ((p.gueltigBis === null) || (JavaString.compareTo(p.gueltigBis, stichtag) >= 0))) {
				return p;
			}
		}
		return null;
	}

	/**
	 * Liefert das {@link UvLehrerPflichtstundensoll}, das am Beginn des Planungsabschnitts des
	 * {@link UvPlanungsabschnittLehrer} gültig ist, oder {@code null}, falls kein passendes Pflichtstundensoll existiert.
	 *
	 * @param planungsabschnittLehrer der {@link UvPlanungsabschnittLehrer}
	 * @return das am Beginn des Planungsabschnitts gültige {@link UvLehrerPflichtstundensoll} oder {@code null}
	 */
	public lehrerPflichtstundensollGetByPlanungsabschnittLehrer(planungsabschnittLehrer: UvPlanungsabschnittLehrer): UvLehrerPflichtstundensoll | null {
		const pa: UvPlanungsabschnitt = DeveloperNotificationException.ifMapGetIsNull(this.planungsabschnittById, planungsabschnittLehrer.idPlanungsabschnitt);
		return this.lehrerPflichtstundensollGetByLehrerAndPlanungsabschnitt(this.lehrerGetByIdOrException(planungsabschnittLehrer.idLehrer), pa);
	}

	/**
	 * Liefert den Pflichtstundensoll-Wert des {@link UvLehrer}s am Beginn des {@link UvPlanungsabschnitt}s,
	 * oder {@code null}, falls kein passendes Pflichtstundensoll existiert.
	 *
	 * @param lehrer             der {@link UvLehrer}
	 * @param planungsabschnitt  der {@link UvPlanungsabschnitt}
	 * @return der Pflichtstundensoll-Wert oder {@code null}
	 */
	public lehrerPflichtstundensollGetDoubleByLehrerAndPlanungsabschnitt(lehrer: UvLehrer, planungsabschnitt: UvPlanungsabschnitt): number | null {
		const p: UvLehrerPflichtstundensoll | null = this.lehrerPflichtstundensollGetByLehrerAndPlanungsabschnitt(lehrer, planungsabschnitt);
		return (p !== null) ? p.pflichtstdSoll : null;
	}

	/**
	 * Liefert den Pflichtstundensoll-Wert des {@link UvPlanungsabschnittLehrer}s am Beginn seines Planungsabschnitts,
	 * oder {@code null}, falls kein passendes Pflichtstundensoll existiert.
	 *
	 * @param planungsabschnittLehrer der {@link UvPlanungsabschnittLehrer}
	 * @return der Pflichtstundensoll-Wert oder {@code null}
	 */
	public lehrerPflichtstundensollGetDoubleByPlanungsabschnittLehrer(planungsabschnittLehrer: UvPlanungsabschnittLehrer): number | null {
		const p: UvLehrerPflichtstundensoll | null = this.lehrerPflichtstundensollGetByPlanungsabschnittLehrer(planungsabschnittLehrer);
		return (p !== null) ? p.pflichtstdSoll : null;
	}

	/**
	 * Liefert die Liste der {@link UvLehrerAnrechnungsstunden}, die am Beginn des {@link UvPlanungsabschnitt}s gültig sind.
	 *
	 * @param lehrer             der {@link UvLehrer}
	 * @param planungsabschnitt  der {@link UvPlanungsabschnitt}
	 * @return die Liste der am Beginn des Planungsabschnitts gültigen {@link UvLehrerAnrechnungsstunden}
	 */
	public lehrerAnrechnungsstundenGetMengeByLehrerAndPlanungsabschnitt(lehrer: UvLehrer, planungsabschnitt: UvPlanungsabschnitt): List<UvLehrerAnrechnungsstunden> {
		const liste: List<UvLehrerAnrechnungsstunden> | null = this.lehrerAnrechnungsstundenMengeByLehrerId.get(lehrer.id);
		if (liste === null) {
			return new ArrayList();
		}
		const stichtag: string = planungsabschnitt.gueltigVon;
		const result: List<UvLehrerAnrechnungsstunden> = new ArrayList<UvLehrerAnrechnungsstunden>();
		for (const a of liste) {
			if ((JavaString.compareTo(a.gueltigVon, stichtag) <= 0) && ((a.gueltigBis === null) || (JavaString.compareTo(a.gueltigBis, stichtag) >= 0))) {
				result.add(a);
			}
		}
		return result;
	}

	/**
	 * Liefert die Liste der {@link UvLehrerAnrechnungsstunden}, die am Beginn des Planungsabschnitts des
	 * {@link UvPlanungsabschnittLehrer} gültig sind.
	 *
	 * @param planungsabschnittLehrer der {@link UvPlanungsabschnittLehrer}
	 * @return die Liste der am Beginn des Planungsabschnitts gültigen {@link UvLehrerAnrechnungsstunden}
	 */
	public lehrerAnrechnungsstundenGetMengeByPlanungsabschnittLehrer(planungsabschnittLehrer: UvPlanungsabschnittLehrer): List<UvLehrerAnrechnungsstunden> {
		const pa: UvPlanungsabschnitt = DeveloperNotificationException.ifMapGetIsNull(this.planungsabschnittById, planungsabschnittLehrer.idPlanungsabschnitt);
		return this.lehrerAnrechnungsstundenGetMengeByLehrerAndPlanungsabschnitt(this.lehrerGetByIdOrException(planungsabschnittLehrer.idLehrer), pa);
	}

	/**
	 * Liefert die Summe der Anrechnungsstunden des {@link UvLehrer}s, die am Beginn des {@link UvPlanungsabschnitt}s gültig sind.
	 *
	 * @param lehrer             der {@link UvLehrer}
	 * @param planungsabschnitt  der {@link UvPlanungsabschnitt}
	 * @return die Summe der Anrechnungsstunden
	 */
	public lehrerAnrechnungsstundenGetDoubleByLehrerAndPlanungsabschnitt(lehrer: UvLehrer, planungsabschnitt: UvPlanungsabschnitt): number {
		let summe: number = 0.0;
		for (const a of this.lehrerAnrechnungsstundenGetMengeByLehrerAndPlanungsabschnitt(lehrer, planungsabschnitt)) {
			summe += a.anzahlStunden;
		}
		return summe;
	}

	/**
	 * Liefert die Summe der Anrechnungsstunden des {@link UvPlanungsabschnittLehrer}s, die am Beginn seines Planungsabschnitts gültig sind.
	 *
	 * @param planungsabschnittLehrer der {@link UvPlanungsabschnittLehrer}
	 * @return die Summe der Anrechnungsstunden
	 */
	public lehrerAnrechnungsstundenGetDoubleByPlanungsabschnittLehrer(planungsabschnittLehrer: UvPlanungsabschnittLehrer): number {
		return this.lehrerAnrechnungsstundenGetDoubleByLehrerAndPlanungsabschnitt(this.lehrerGetByIdOrException(planungsabschnittLehrer.idLehrer), DeveloperNotificationException.ifMapGetIsNull(this.planungsabschnittById, planungsabschnittLehrer.idPlanungsabschnitt));
	}

	/**
	 * Liefert alle {@link LehrerUnterrichtsfach}-Einträge zum übergebenen {@link UvFach}.
	 *
	 * @param fach das {@link UvFach}
	 * @return die zugeordneten {@link LehrerUnterrichtsfach}-Einträge
	 */
	public lehrerUnterrichtsfachGetMengeByFach(fach: UvFach): List<LehrerUnterrichtsfach> {
		return this.lehrerUnterrichtsfachByIdLehrerAndIdFach.get2(fach.idFach);
	}

	/**
	 * Liefert den {@link UvLehrer} zur übergebenen K-Lehrer-ID.
	 *
	 * @param idKLehrer die K-Lehrer-ID des gesuchten {@link UvLehrer}s
	 * @return den zugehörigen {@link UvLehrer}
	 * @throws DeveloperNotificationException falls kein {@link UvLehrer} mit dieser K-Lehrer-ID vorhanden ist
	 */
	public lehrerGetByKLehrerIdOrException(idKLehrer: number): UvLehrer {
		return DeveloperNotificationException.ifMapGetIsNull(this.lehrerByIdKLehrer, idKLehrer);
	}

	/**
	 * Liefert den {@link UvLehrer} zur übergebenen K-Lehrer-ID oder {@code null}.
	 *
	 * @param idKLehrer die K-Lehrer-ID des gesuchten {@link UvLehrer}s
	 * @return den zugehörigen {@link UvLehrer} oder {@code null}
	 */
	public lehrerGetByKLehrerIdOrNull(idKLehrer: number): UvLehrer | null {
		return this.lehrerByIdKLehrer.get(idKLehrer);
	}

	/**
	 * Liefert den {@link UvLehrer} zu einem {@link LehrerUnterrichtsfach}.
	 *
	 * @param unterrichtsfach das {@link LehrerUnterrichtsfach}
	 * @return den zugehörigen {@link UvLehrer}
	 */
	public lehrerGetByLehrerUnterrichtsfachOrException(unterrichtsfach: LehrerUnterrichtsfach): UvLehrer {
		return unterrichtsfach.istKLehrer ? this.lehrerGetByKLehrerIdOrException(unterrichtsfach.idLehrer) : this.lehrerGetByIdOrException(unterrichtsfach.idLehrer);
	}

	/**
	 * Liefert den Wert des größten Wochentages.
	 *
	 * @return den Wert des größten Wochentages.
	 */
	public zeitrasterGetMaxWochentag(): number {
		return Wochentag.SONNTAG.id;
	}

	/**
	 * Liefert den Wert des größtmöglichen letzten Stunde.
	 *
	 * @return den Wert des größtmöglichen letzten Stunde.
	 */
	public zeitrasterGetMaxStunde(): number {
		return 16;
	}

	/**
	 * Liefert den Wert des größtmöglichen Wochentyps oder 0, falls jede Woche gleich ist.
	 * <br>Hinweis: 0 = Jede Woche gleich, 1 = ungültiger Wert, 2 = A-B-Wochen, 3 = A-B-C-Wochen, ...
	 *
	 * @return den Wert des größtmöglichen Wochentyps oder 0, falls jede Woche gleich ist.
	 */
	public zeitrasterGetWochentypmodell(): number {
		return 3;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.uv.UvManager';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.uv.UvManager'].includes(name);
	}

	public static readonly class = new Class<UvManager>('de.svws_nrw.core.utils.uv.UvManager');

}

export function cast_de_svws_nrw_core_utils_uv_UvManager(obj: unknown): UvManager {
	return obj as UvManager;
}
