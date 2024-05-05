import { GostKursklausur } from '../../../../core/data/gost/klausurplanung/GostKursklausur';
import { GostKlausurvorgabe } from '../../../../core/data/gost/klausurplanung/GostKlausurvorgabe';
import { GostKursklausurManager, cast_de_svws_nrw_core_utils_gost_klausurplanung_GostKursklausurManager } from '../../../../core/utils/gost/klausurplanung/GostKursklausurManager';
import { JavaLong } from '../../../../java/lang/JavaLong';
import { JavaObject } from '../../../../java/lang/JavaObject';
import { GostSchuelerklausurTermin, cast_de_svws_nrw_core_data_gost_klausurplanung_GostSchuelerklausurTermin } from '../../../../core/data/gost/klausurplanung/GostSchuelerklausurTermin';

export class GostSchuelerklausurTerminRich extends JavaObject {

	/**
	 * Die ID des Stundenplans.
	 */
	public id : number = -1;

	/**
	 * Die Startzeit der Klausur in Minuten seit 0 Uhr, sofern abweichend von Startzeit des gesamten Termins.
	 */
	public startzeit : number = -1;

	/**
	 * Die textuelle Beschreibung des Stundenplans.
	 */
	public idKursklausur : number = -1;

	/**
	 * Die ID des Faches.
	 */
	public idFach : number = -1;

	/**
	 * Das Kürzel einer verallgemeinerten Kursart.
	 */
	public kursart : string = "";

	/**
	 * Die Dauer der Klausur in Minuten.
	 */
	public dauer : number = 0;

	/**
	 * Die Auswahlzeit in Minuten, sofern vorhanden.
	 */
	public auswahlzeit : number = 0;

	/**
	 * Die Information, ob es sich um eine mündliche Prüfung handelt.
	 */
	public istMdlPruefung : boolean = false;

	/**
	 * Die Information, ob Audioequipment nötig ist, z.B. für Klausuren mit Hörverstehensanteilen.
	 */
	public istAudioNotwendig : boolean = false;

	/**
	 * Die Information, ob Videoequipment nötig ist, z.B. für Klausuren mit Videoanalyse.
	 */
	public istVideoNotwendig : boolean = false;


	/**
	 * Konstruktor zur Erstellung des Rich-Objekts.
	 *
	 * @param termin     das zu vergleichende Objekt
	 * @param manager
	 */
	public constructor(termin : GostSchuelerklausurTermin | null, manager : GostKursklausurManager | null);

	/**
	 * Konstruktor für Transpiler.
	 */
	public constructor();

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : GostSchuelerklausurTermin | null, __param1? : GostKursklausurManager | null) {
		super();
		if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && ((__param0 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurTermin'))) || (__param0 === null)) && ((typeof __param1 !== "undefined") && ((__param1 instanceof JavaObject) && ((__param1 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.utils.gost.klausurplanung.GostKursklausurManager'))) || (__param1 === null))) {
			const termin : GostSchuelerklausurTermin | null = cast_de_svws_nrw_core_data_gost_klausurplanung_GostSchuelerklausurTermin(__param0);
			const manager : GostKursklausurManager | null = cast_de_svws_nrw_core_utils_gost_klausurplanung_GostKursklausurManager(__param1);
			this.id = termin.id;
			const kursklausur : GostKursklausur | null = manager.kursklausurBySchuelerklausurTermin(termin);
			this.startzeit = manager.startzeitByKursklausurOrException(kursklausur);
			this.idKursklausur = kursklausur.id;
			const vorgabe : GostKlausurvorgabe | null = manager.vorgabeBySchuelerklausurTermin(termin);
			this.idFach = vorgabe.idFach;
			this.kursart = vorgabe.kursart;
			this.dauer = vorgabe.dauer;
			this.auswahlzeit = vorgabe.auswahlzeit;
			this.istMdlPruefung = vorgabe.istMdlPruefung;
			this.istAudioNotwendig = vorgabe.istAudioNotwendig;
			this.istVideoNotwendig = vorgabe.istVideoNotwendig;
		} else if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined")) {
			// empty method body
		} else throw new Error('invalid method overload');
	}

	/**
	 * Vergleicht, ob das aktuelle dasselbe Objekt, wie ein anderes übergebenes Objekt ist.
	 *
	 * @param another     das zu vergleichende Objekt
	 * @return true, falls die Objekte identisch sind, sonst false
	 */
	public equals(another : unknown | null) : boolean {
		return another !== null && ((another instanceof JavaObject) && ((another as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurTerminRich'))) && this.id === (cast_de_svws_nrw_core_data_gost_klausurplanung_GostSchuelerklausurTerminRich(another)).id;
	}

	/**
	 * Erzeugt den Hashcode zu Objekt auf Basis der id.
	 *
	 * @return den HashCode
	 */
	public hashCode() : number {
		return JavaLong.hashCode((this.id));
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurTerminRich';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurTerminRich'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostSchuelerklausurTerminRich {
		const obj = JSON.parse(json);
		const result = new GostSchuelerklausurTerminRich();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.startzeit === "undefined")
			 throw new Error('invalid json format, missing attribute startzeit');
		result.startzeit = obj.startzeit;
		if (typeof obj.idKursklausur === "undefined")
			 throw new Error('invalid json format, missing attribute idKursklausur');
		result.idKursklausur = obj.idKursklausur;
		if (typeof obj.idFach === "undefined")
			 throw new Error('invalid json format, missing attribute idFach');
		result.idFach = obj.idFach;
		if (typeof obj.kursart === "undefined")
			 throw new Error('invalid json format, missing attribute kursart');
		result.kursart = obj.kursart;
		if (typeof obj.dauer === "undefined")
			 throw new Error('invalid json format, missing attribute dauer');
		result.dauer = obj.dauer;
		if (typeof obj.auswahlzeit === "undefined")
			 throw new Error('invalid json format, missing attribute auswahlzeit');
		result.auswahlzeit = obj.auswahlzeit;
		if (typeof obj.istMdlPruefung === "undefined")
			 throw new Error('invalid json format, missing attribute istMdlPruefung');
		result.istMdlPruefung = obj.istMdlPruefung;
		if (typeof obj.istAudioNotwendig === "undefined")
			 throw new Error('invalid json format, missing attribute istAudioNotwendig');
		result.istAudioNotwendig = obj.istAudioNotwendig;
		if (typeof obj.istVideoNotwendig === "undefined")
			 throw new Error('invalid json format, missing attribute istVideoNotwendig');
		result.istVideoNotwendig = obj.istVideoNotwendig;
		return result;
	}

	public static transpilerToJSON(obj : GostSchuelerklausurTerminRich) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"startzeit" : ' + obj.startzeit + ',';
		result += '"idKursklausur" : ' + obj.idKursklausur + ',';
		result += '"idFach" : ' + obj.idFach + ',';
		result += '"kursart" : ' + JSON.stringify(obj.kursart!) + ',';
		result += '"dauer" : ' + obj.dauer + ',';
		result += '"auswahlzeit" : ' + obj.auswahlzeit + ',';
		result += '"istMdlPruefung" : ' + obj.istMdlPruefung + ',';
		result += '"istAudioNotwendig" : ' + obj.istAudioNotwendig + ',';
		result += '"istVideoNotwendig" : ' + obj.istVideoNotwendig + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostSchuelerklausurTerminRich>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.startzeit !== "undefined") {
			result += '"startzeit" : ' + obj.startzeit + ',';
		}
		if (typeof obj.idKursklausur !== "undefined") {
			result += '"idKursklausur" : ' + obj.idKursklausur + ',';
		}
		if (typeof obj.idFach !== "undefined") {
			result += '"idFach" : ' + obj.idFach + ',';
		}
		if (typeof obj.kursart !== "undefined") {
			result += '"kursart" : ' + JSON.stringify(obj.kursart!) + ',';
		}
		if (typeof obj.dauer !== "undefined") {
			result += '"dauer" : ' + obj.dauer + ',';
		}
		if (typeof obj.auswahlzeit !== "undefined") {
			result += '"auswahlzeit" : ' + obj.auswahlzeit + ',';
		}
		if (typeof obj.istMdlPruefung !== "undefined") {
			result += '"istMdlPruefung" : ' + obj.istMdlPruefung + ',';
		}
		if (typeof obj.istAudioNotwendig !== "undefined") {
			result += '"istAudioNotwendig" : ' + obj.istAudioNotwendig + ',';
		}
		if (typeof obj.istVideoNotwendig !== "undefined") {
			result += '"istVideoNotwendig" : ' + obj.istVideoNotwendig + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_klausurplanung_GostSchuelerklausurTerminRich(obj : unknown) : GostSchuelerklausurTerminRich {
	return obj as GostSchuelerklausurTerminRich;
}
