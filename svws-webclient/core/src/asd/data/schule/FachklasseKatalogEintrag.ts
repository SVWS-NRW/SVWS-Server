import { Class } from '../../../java/lang/Class';
import { CoreTypeData } from '../../../asd/data/CoreTypeData';

export class FachklasseKatalogEintrag extends CoreTypeData {

	/**
	 * Teil 1 des Fachklassen Schlüssels.
	 */
	public fkSchluessel: string | null = null;

	/**
	 * Teil 2 des Fachklassen Schlüssels.
	 */
	public fkSchluessel2: string | null = null;

	/**
	 * Index zur Ermittlung der Schulgliederung für Fachklassen des Berufskollegs. Referenziert auf {@link SchulgliederungKatalogEintrag}.
	 */
	public bkIndex: number = -1;

	/**
	 * ID des DQRNiveaus im CoreType DQRNiveau.
	 */
	public dqrNiveau: number = -1;

	/**
	 * Gibt an, ob die Fachklassen ausgelaufen ist oder nicht
	 */
	public istAusgelaufen: boolean = false;

	/**
	 * Die Gruppe des Berufsfeldes.
	 */
	public berufsfeldGruppe: string | null = null;

	/**
	 * Das Berufsfeld.
	 */
	public berufsfeld: string | null = null;

	/**
	 * Ebene 1 des Berufsfeldes
	 */
	public ebene1: string | null = null;

	/**
	 * Ebene 2 des Berufsfeldes
	 */
	public ebene2: string | null = null;

	/**
	 * Ebene 3 des Berufsfeldes
	 */
	public ebene3: string | null = null;

	/**
	 * Die Bezeichnung der Fachklasse (männlich)
	 */
	public bezeichnungM: string = "";

	/**
	 * Die Bezeichnung der Fachklasse (weiblich)
	 */
	public bezeichnungW: string = "";


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.schule.FachklasseKatalogEintrag';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.data.schule.FachklasseKatalogEintrag', 'de.svws_nrw.asd.data.CoreTypeData'].includes(name);
	}

	public static readonly class = new Class<FachklasseKatalogEintrag>('de.svws_nrw.asd.data.schule.FachklasseKatalogEintrag');

	public static transpilerFromJSON(json: string): FachklasseKatalogEintrag {
		const obj = JSON.parse(json) as Partial<FachklasseKatalogEintrag>;
		const result = new FachklasseKatalogEintrag();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.schluessel === undefined)
			throw new Error('invalid json format, missing attribute schluessel');
		result.schluessel = obj.schluessel;
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (obj.text === undefined)
			throw new Error('invalid json format, missing attribute text');
		result.text = obj.text;
		result.gueltigVon = (obj.gueltigVon === undefined) ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = (obj.gueltigBis === undefined) ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		result.fkSchluessel = (obj.fkSchluessel === undefined) ? null : obj.fkSchluessel === null ? null : obj.fkSchluessel;
		result.fkSchluessel2 = (obj.fkSchluessel2 === undefined) ? null : obj.fkSchluessel2 === null ? null : obj.fkSchluessel2;
		if (obj.bkIndex === undefined)
			throw new Error('invalid json format, missing attribute bkIndex');
		result.bkIndex = obj.bkIndex;
		if (obj.dqrNiveau === undefined)
			throw new Error('invalid json format, missing attribute dqrNiveau');
		result.dqrNiveau = obj.dqrNiveau;
		if (obj.istAusgelaufen === undefined)
			throw new Error('invalid json format, missing attribute istAusgelaufen');
		result.istAusgelaufen = obj.istAusgelaufen;
		result.berufsfeldGruppe = (obj.berufsfeldGruppe === undefined) ? null : obj.berufsfeldGruppe === null ? null : obj.berufsfeldGruppe;
		result.berufsfeld = (obj.berufsfeld === undefined) ? null : obj.berufsfeld === null ? null : obj.berufsfeld;
		result.ebene1 = (obj.ebene1 === undefined) ? null : obj.ebene1 === null ? null : obj.ebene1;
		result.ebene2 = (obj.ebene2 === undefined) ? null : obj.ebene2 === null ? null : obj.ebene2;
		result.ebene3 = (obj.ebene3 === undefined) ? null : obj.ebene3 === null ? null : obj.ebene3;
		if (obj.bezeichnungM === undefined)
			throw new Error('invalid json format, missing attribute bezeichnungM');
		result.bezeichnungM = obj.bezeichnungM;
		if (obj.bezeichnungW === undefined)
			throw new Error('invalid json format, missing attribute bezeichnungW');
		result.bezeichnungW = obj.bezeichnungW;
		return result;
	}

	public static transpilerToJSON(obj: FachklasseKatalogEintrag): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"schluessel" : ' + JSON.stringify(obj.schluessel) + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"text" : ' + JSON.stringify(obj.text) + ',';
		result += '"gueltigVon" : ' + ((obj.gueltigVon === null) ? 'null' : obj.gueltigVon.toString()) + ',';
		result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : obj.gueltigBis.toString()) + ',';
		result += '"fkSchluessel" : ' + ((obj.fkSchluessel === null) ? 'null' : JSON.stringify(obj.fkSchluessel)) + ',';
		result += '"fkSchluessel2" : ' + ((obj.fkSchluessel2 === null) ? 'null' : JSON.stringify(obj.fkSchluessel2)) + ',';
		result += '"bkIndex" : ' + obj.bkIndex + ',';
		result += '"dqrNiveau" : ' + obj.dqrNiveau + ',';
		result += '"istAusgelaufen" : ' + obj.istAusgelaufen.toString() + ',';
		result += '"berufsfeldGruppe" : ' + ((obj.berufsfeldGruppe === null) ? 'null' : JSON.stringify(obj.berufsfeldGruppe)) + ',';
		result += '"berufsfeld" : ' + ((obj.berufsfeld === null) ? 'null' : JSON.stringify(obj.berufsfeld)) + ',';
		result += '"ebene1" : ' + ((obj.ebene1 === null) ? 'null' : JSON.stringify(obj.ebene1)) + ',';
		result += '"ebene2" : ' + ((obj.ebene2 === null) ? 'null' : JSON.stringify(obj.ebene2)) + ',';
		result += '"ebene3" : ' + ((obj.ebene3 === null) ? 'null' : JSON.stringify(obj.ebene3)) + ',';
		result += '"bezeichnungM" : ' + JSON.stringify(obj.bezeichnungM) + ',';
		result += '"bezeichnungW" : ' + JSON.stringify(obj.bezeichnungW) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<FachklasseKatalogEintrag>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.schluessel !== undefined) {
			result += '"schluessel" : ' + JSON.stringify(obj.schluessel) + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		}
		if (obj.text !== undefined) {
			result += '"text" : ' + JSON.stringify(obj.text) + ',';
		}
		if (obj.gueltigVon !== undefined) {
			result += '"gueltigVon" : ' + ((obj.gueltigVon === null) ? 'null' : obj.gueltigVon.toString()) + ',';
		}
		if (obj.gueltigBis !== undefined) {
			result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : obj.gueltigBis.toString()) + ',';
		}
		if (obj.fkSchluessel !== undefined) {
			result += '"fkSchluessel" : ' + ((obj.fkSchluessel === null) ? 'null' : JSON.stringify(obj.fkSchluessel)) + ',';
		}
		if (obj.fkSchluessel2 !== undefined) {
			result += '"fkSchluessel2" : ' + ((obj.fkSchluessel2 === null) ? 'null' : JSON.stringify(obj.fkSchluessel2)) + ',';
		}
		if (obj.bkIndex !== undefined) {
			result += '"bkIndex" : ' + obj.bkIndex + ',';
		}
		if (obj.dqrNiveau !== undefined) {
			result += '"dqrNiveau" : ' + obj.dqrNiveau + ',';
		}
		if (obj.istAusgelaufen !== undefined) {
			result += '"istAusgelaufen" : ' + obj.istAusgelaufen.toString() + ',';
		}
		if (obj.berufsfeldGruppe !== undefined) {
			result += '"berufsfeldGruppe" : ' + ((obj.berufsfeldGruppe === null) ? 'null' : JSON.stringify(obj.berufsfeldGruppe)) + ',';
		}
		if (obj.berufsfeld !== undefined) {
			result += '"berufsfeld" : ' + ((obj.berufsfeld === null) ? 'null' : JSON.stringify(obj.berufsfeld)) + ',';
		}
		if (obj.ebene1 !== undefined) {
			result += '"ebene1" : ' + ((obj.ebene1 === null) ? 'null' : JSON.stringify(obj.ebene1)) + ',';
		}
		if (obj.ebene2 !== undefined) {
			result += '"ebene2" : ' + ((obj.ebene2 === null) ? 'null' : JSON.stringify(obj.ebene2)) + ',';
		}
		if (obj.ebene3 !== undefined) {
			result += '"ebene3" : ' + ((obj.ebene3 === null) ? 'null' : JSON.stringify(obj.ebene3)) + ',';
		}
		if (obj.bezeichnungM !== undefined) {
			result += '"bezeichnungM" : ' + JSON.stringify(obj.bezeichnungM) + ',';
		}
		if (obj.bezeichnungW !== undefined) {
			result += '"bezeichnungW" : ' + JSON.stringify(obj.bezeichnungW) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_schule_FachklasseKatalogEintrag(obj: unknown): FachklasseKatalogEintrag {
	return obj as FachklasseKatalogEintrag;
}
