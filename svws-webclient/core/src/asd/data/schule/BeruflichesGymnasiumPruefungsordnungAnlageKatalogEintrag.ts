import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { BeruflichesGymnasiumStundentafel } from '../../../asd/data/schule/BeruflichesGymnasiumStundentafel';
import { Class } from '../../../java/lang/Class';
import { CoreTypeData } from '../../../asd/data/CoreTypeData';

export class BeruflichesGymnasiumPruefungsordnungAnlageKatalogEintrag extends CoreTypeData {

	/**
	 * Der angestrebte allgemeinbildende Abschluss des Bildungsgangs
	 */
	public abschlussAllgemeinbildend : string = "";

	/**
	 * Die Schulgliederung
	 */
	public gliederung : string = "";

	/**
	 * Der Fachklassenschlüssel
	 */
	public fachklassenschluessel : string = "";

	/**
	 * Die Varianten der Stundentafeln eines Bildungsgangs
	 */
	public stundentafeln : List<BeruflichesGymnasiumStundentafel> = new ArrayList<BeruflichesGymnasiumStundentafel>();

	/**
	 * Die Fussnoten zu den Stundentafeln
	 */
	public fussnoten : List<string> = new ArrayList<string>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.schule.BeruflichesGymnasiumPruefungsordnungAnlageKatalogEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.data.CoreTypeData', 'de.svws_nrw.asd.data.schule.BeruflichesGymnasiumPruefungsordnungAnlageKatalogEintrag'].includes(name);
	}

	public static class = new Class<BeruflichesGymnasiumPruefungsordnungAnlageKatalogEintrag>('de.svws_nrw.asd.data.schule.BeruflichesGymnasiumPruefungsordnungAnlageKatalogEintrag');

	public static transpilerFromJSON(json : string): BeruflichesGymnasiumPruefungsordnungAnlageKatalogEintrag {
		const obj = JSON.parse(json) as Partial<BeruflichesGymnasiumPruefungsordnungAnlageKatalogEintrag>;
		const result = new BeruflichesGymnasiumPruefungsordnungAnlageKatalogEintrag();
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
		if (obj.abschlussAllgemeinbildend === undefined)
			throw new Error('invalid json format, missing attribute abschlussAllgemeinbildend');
		result.abschlussAllgemeinbildend = obj.abschlussAllgemeinbildend;
		if (obj.gliederung === undefined)
			throw new Error('invalid json format, missing attribute gliederung');
		result.gliederung = obj.gliederung;
		if (obj.fachklassenschluessel === undefined)
			throw new Error('invalid json format, missing attribute fachklassenschluessel');
		result.fachklassenschluessel = obj.fachklassenschluessel;
		if (obj.stundentafeln !== undefined) {
			for (const elem of obj.stundentafeln) {
				result.stundentafeln.add(BeruflichesGymnasiumStundentafel.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.fussnoten !== undefined) {
			for (const elem of obj.fussnoten) {
				result.fussnoten.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : BeruflichesGymnasiumPruefungsordnungAnlageKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"schluessel" : ' + JSON.stringify(obj.schluessel) + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"text" : ' + JSON.stringify(obj.text) + ',';
		result += '"gueltigVon" : ' + ((obj.gueltigVon === null) ? 'null' : obj.gueltigVon.toString()) + ',';
		result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : obj.gueltigBis.toString()) + ',';
		result += '"abschlussAllgemeinbildend" : ' + JSON.stringify(obj.abschlussAllgemeinbildend) + ',';
		result += '"gliederung" : ' + JSON.stringify(obj.gliederung) + ',';
		result += '"fachklassenschluessel" : ' + JSON.stringify(obj.fachklassenschluessel) + ',';
		result += '"stundentafeln" : [ ';
		for (let i = 0; i < obj.stundentafeln.size(); i++) {
			const elem = obj.stundentafeln.get(i);
			result += BeruflichesGymnasiumStundentafel.transpilerToJSON(elem);
			if (i < obj.stundentafeln.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"fussnoten" : [ ';
		for (let i = 0; i < obj.fussnoten.size(); i++) {
			const elem = obj.fussnoten.get(i);
			result += '"' + elem + '"';
			if (i < obj.fussnoten.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BeruflichesGymnasiumPruefungsordnungAnlageKatalogEintrag>) : string {
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
		if (obj.abschlussAllgemeinbildend !== undefined) {
			result += '"abschlussAllgemeinbildend" : ' + JSON.stringify(obj.abschlussAllgemeinbildend) + ',';
		}
		if (obj.gliederung !== undefined) {
			result += '"gliederung" : ' + JSON.stringify(obj.gliederung) + ',';
		}
		if (obj.fachklassenschluessel !== undefined) {
			result += '"fachklassenschluessel" : ' + JSON.stringify(obj.fachklassenschluessel) + ',';
		}
		if (obj.stundentafeln !== undefined) {
			result += '"stundentafeln" : [ ';
			for (let i = 0; i < obj.stundentafeln.size(); i++) {
				const elem = obj.stundentafeln.get(i);
				result += BeruflichesGymnasiumStundentafel.transpilerToJSON(elem);
				if (i < obj.stundentafeln.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.fussnoten !== undefined) {
			result += '"fussnoten" : [ ';
			for (let i = 0; i < obj.fussnoten.size(); i++) {
				const elem = obj.fussnoten.get(i);
				result += '"' + elem + '"';
				if (i < obj.fussnoten.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_schule_BeruflichesGymnasiumPruefungsordnungAnlageKatalogEintrag(obj : unknown) : BeruflichesGymnasiumPruefungsordnungAnlageKatalogEintrag {
	return obj as BeruflichesGymnasiumPruefungsordnungAnlageKatalogEintrag;
}
