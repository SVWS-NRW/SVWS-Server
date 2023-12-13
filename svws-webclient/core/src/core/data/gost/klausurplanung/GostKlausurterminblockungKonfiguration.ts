import { JavaObject } from '../../../../java/lang/JavaObject';
import { KlausurterminblockungModusKursarten } from '../../../../core/types/gost/klausurplanung/KlausurterminblockungModusKursarten';
import { KlausurterminblockungAlgorithmen } from '../../../../core/types/gost/klausurplanung/KlausurterminblockungAlgorithmen';
import { KlausurterminblockungModusQuartale } from '../../../../core/types/gost/klausurplanung/KlausurterminblockungModusQuartale';

export class GostKlausurterminblockungKonfiguration extends JavaObject {

	/**
	 * Die maximale Zeit, welche für die Blockung verwendet wird
	 */
	public maxTimeMillis : number = 1000;

	/**
	 * Der Typ des Algorithmus, welcher verwendet wird.
	 */
	public algorithmus : number = KlausurterminblockungAlgorithmen.NORMAL.id;

	/**
	 * Der Modus für die Art, ob und wie die beiden Kursarten LK und GK geblockt werden
	 */
	public modusKursarten : number = KlausurterminblockungModusKursarten.BEIDE.id;

	/**
	 * Der Modus dafür, wie die Klausuren in den Quartalen eines Halbjahres geblockt werden.
	 */
	public modusQuartale : number = KlausurterminblockungModusQuartale.ZUSAMMEN.id;

	/**
	 * True, falls Kurse mit gleicher Lehrkraft+Fach+Kursart in einem Termin geblockt werden sollen.
	 */
	public regelBeiTerminenGleicheLehrkraftFachKursart : boolean = false;

	/**
	 * True, falls die Regel "bevorzuge gleiche Kursschienen bei Terminen" aktiviert ist.
	 */
	public regelBevorzugeBeiTerminenGleicheKursschienen : boolean = false;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.klausurplanung.GostKlausurterminblockungKonfiguration';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.klausurplanung.GostKlausurterminblockungKonfiguration'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostKlausurterminblockungKonfiguration {
		const obj = JSON.parse(json);
		const result = new GostKlausurterminblockungKonfiguration();
		if (typeof obj.maxTimeMillis === "undefined")
			 throw new Error('invalid json format, missing attribute maxTimeMillis');
		result.maxTimeMillis = obj.maxTimeMillis;
		if (typeof obj.algorithmus === "undefined")
			 throw new Error('invalid json format, missing attribute algorithmus');
		result.algorithmus = obj.algorithmus;
		if (typeof obj.modusKursarten === "undefined")
			 throw new Error('invalid json format, missing attribute modusKursarten');
		result.modusKursarten = obj.modusKursarten;
		if (typeof obj.modusQuartale === "undefined")
			 throw new Error('invalid json format, missing attribute modusQuartale');
		result.modusQuartale = obj.modusQuartale;
		if (typeof obj.regelBeiTerminenGleicheLehrkraftFachKursart === "undefined")
			 throw new Error('invalid json format, missing attribute regelBeiTerminenGleicheLehrkraftFachKursart');
		result.regelBeiTerminenGleicheLehrkraftFachKursart = obj.regelBeiTerminenGleicheLehrkraftFachKursart;
		if (typeof obj.regelBevorzugeBeiTerminenGleicheKursschienen === "undefined")
			 throw new Error('invalid json format, missing attribute regelBevorzugeBeiTerminenGleicheKursschienen');
		result.regelBevorzugeBeiTerminenGleicheKursschienen = obj.regelBevorzugeBeiTerminenGleicheKursschienen;
		return result;
	}

	public static transpilerToJSON(obj : GostKlausurterminblockungKonfiguration) : string {
		let result = '{';
		result += '"maxTimeMillis" : ' + obj.maxTimeMillis + ',';
		result += '"algorithmus" : ' + obj.algorithmus + ',';
		result += '"modusKursarten" : ' + obj.modusKursarten + ',';
		result += '"modusQuartale" : ' + obj.modusQuartale + ',';
		result += '"regelBeiTerminenGleicheLehrkraftFachKursart" : ' + obj.regelBeiTerminenGleicheLehrkraftFachKursart + ',';
		result += '"regelBevorzugeBeiTerminenGleicheKursschienen" : ' + obj.regelBevorzugeBeiTerminenGleicheKursschienen + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostKlausurterminblockungKonfiguration>) : string {
		let result = '{';
		if (typeof obj.maxTimeMillis !== "undefined") {
			result += '"maxTimeMillis" : ' + obj.maxTimeMillis + ',';
		}
		if (typeof obj.algorithmus !== "undefined") {
			result += '"algorithmus" : ' + obj.algorithmus + ',';
		}
		if (typeof obj.modusKursarten !== "undefined") {
			result += '"modusKursarten" : ' + obj.modusKursarten + ',';
		}
		if (typeof obj.modusQuartale !== "undefined") {
			result += '"modusQuartale" : ' + obj.modusQuartale + ',';
		}
		if (typeof obj.regelBeiTerminenGleicheLehrkraftFachKursart !== "undefined") {
			result += '"regelBeiTerminenGleicheLehrkraftFachKursart" : ' + obj.regelBeiTerminenGleicheLehrkraftFachKursart + ',';
		}
		if (typeof obj.regelBevorzugeBeiTerminenGleicheKursschienen !== "undefined") {
			result += '"regelBevorzugeBeiTerminenGleicheKursschienen" : ' + obj.regelBevorzugeBeiTerminenGleicheKursschienen + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_klausurplanung_GostKlausurterminblockungKonfiguration(obj : unknown) : GostKlausurterminblockungKonfiguration {
	return obj as GostKlausurterminblockungKonfiguration;
}
