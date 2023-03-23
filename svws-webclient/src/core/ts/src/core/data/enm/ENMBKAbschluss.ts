import { JavaObject } from '../../../java/lang/JavaObject';
import { ENMBKFach } from '../../../core/data/enm/ENMBKFach';
import { Vector } from '../../../java/util/Vector';

export class ENMBKAbschluss extends JavaObject {

	/**
	 * Gibt an, ob der Schüler die Zulassung für die Abschlussprüfung hat.
	 */
	public hatZulassung : boolean = false;

	/**
	 * Gibt an, ob der Schüler die Abschlussprüfung bestanden hat.
	 */
	public hatBestanden : boolean = false;

	/**
	 * Gibt an, ob der Schüler die Zulassung zum Erwerb der erweiterten beruflichen Kenntnisse hat.
	 */
	public hatZulassungErweiterteBeruflicheKenntnisse : boolean = false;

	/**
	 * Gibt an, ob der Schüler die erweiterten beruflichen Kenntnisse erworben hat.
	 */
	public hatErworbenErweiterteBeruflicheKenntnisse : boolean = false;

	/**
	 * Das Notenkürzel der Note der praktischen Prüfung.
	 */
	public notePraktischePruefung : string | null = null;

	/**
	 * Das Notenkürzel der Note aus dem Kolloqium.
	 */
	public noteKolloqium : string | null = null;

	/**
	 * Gibt an, ob der Schüler die Zulassung zur Berufsabschlussprüfung hat.
	 */
	public hatZulassungBerufsabschlusspruefung : boolean = false;

	/**
	 * Gibt an, ob der Schüler die Berufsabschlussprüfung bestanden hat.
	 */
	public hatBestandenBerufsabschlusspruefung : boolean = false;

	/**
	 * Gibt das Thema der Abschlussarbeit an.
	 */
	public themaAbschlussarbeit : string | null = null;

	/**
	 * Gibt an, ob eine Berufsabschlussprüfung vorhanden ist oder nicht.
	 */
	public istVorhandenBerufsabschlusspruefung : boolean = false;

	/**
	 * Das Notenkürzel der Note aus der Fachpraxis.
	 */
	public noteFachpraxis : string | null = null;

	/**
	 * Gibt an, ob der fachpraktische Teil ausreichend ist oder nicht.
	 */
	public istFachpraktischerTeilAusreichend : boolean = false;

	/**
	 * Die Informationen zu den einzelnen Fächern, die dem Abschluss zugeordnet sind.
	 */
	public faecher : Vector<ENMBKFach> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.enm.ENMBKAbschluss'].includes(name);
	}

	public static transpilerFromJSON(json : string): ENMBKAbschluss {
		const obj = JSON.parse(json);
		const result = new ENMBKAbschluss();
		if (typeof obj.hatZulassung === "undefined")
			 throw new Error('invalid json format, missing attribute hatZulassung');
		result.hatZulassung = obj.hatZulassung;
		if (typeof obj.hatBestanden === "undefined")
			 throw new Error('invalid json format, missing attribute hatBestanden');
		result.hatBestanden = obj.hatBestanden;
		if (typeof obj.hatZulassungErweiterteBeruflicheKenntnisse === "undefined")
			 throw new Error('invalid json format, missing attribute hatZulassungErweiterteBeruflicheKenntnisse');
		result.hatZulassungErweiterteBeruflicheKenntnisse = obj.hatZulassungErweiterteBeruflicheKenntnisse;
		if (typeof obj.hatErworbenErweiterteBeruflicheKenntnisse === "undefined")
			 throw new Error('invalid json format, missing attribute hatErworbenErweiterteBeruflicheKenntnisse');
		result.hatErworbenErweiterteBeruflicheKenntnisse = obj.hatErworbenErweiterteBeruflicheKenntnisse;
		result.notePraktischePruefung = typeof obj.notePraktischePruefung === "undefined" ? null : obj.notePraktischePruefung === null ? null : obj.notePraktischePruefung;
		result.noteKolloqium = typeof obj.noteKolloqium === "undefined" ? null : obj.noteKolloqium === null ? null : obj.noteKolloqium;
		if (typeof obj.hatZulassungBerufsabschlusspruefung === "undefined")
			 throw new Error('invalid json format, missing attribute hatZulassungBerufsabschlusspruefung');
		result.hatZulassungBerufsabschlusspruefung = obj.hatZulassungBerufsabschlusspruefung;
		if (typeof obj.hatBestandenBerufsabschlusspruefung === "undefined")
			 throw new Error('invalid json format, missing attribute hatBestandenBerufsabschlusspruefung');
		result.hatBestandenBerufsabschlusspruefung = obj.hatBestandenBerufsabschlusspruefung;
		result.themaAbschlussarbeit = typeof obj.themaAbschlussarbeit === "undefined" ? null : obj.themaAbschlussarbeit === null ? null : obj.themaAbschlussarbeit;
		if (typeof obj.istVorhandenBerufsabschlusspruefung === "undefined")
			 throw new Error('invalid json format, missing attribute istVorhandenBerufsabschlusspruefung');
		result.istVorhandenBerufsabschlusspruefung = obj.istVorhandenBerufsabschlusspruefung;
		result.noteFachpraxis = typeof obj.noteFachpraxis === "undefined" ? null : obj.noteFachpraxis === null ? null : obj.noteFachpraxis;
		if (typeof obj.istFachpraktischerTeilAusreichend === "undefined")
			 throw new Error('invalid json format, missing attribute istFachpraktischerTeilAusreichend');
		result.istFachpraktischerTeilAusreichend = obj.istFachpraktischerTeilAusreichend;
		if ((obj.faecher !== undefined) && (obj.faecher !== null)) {
			for (const elem of obj.faecher) {
				result.faecher?.add(ENMBKFach.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : ENMBKAbschluss) : string {
		let result = '{';
		result += '"hatZulassung" : ' + obj.hatZulassung + ',';
		result += '"hatBestanden" : ' + obj.hatBestanden + ',';
		result += '"hatZulassungErweiterteBeruflicheKenntnisse" : ' + obj.hatZulassungErweiterteBeruflicheKenntnisse + ',';
		result += '"hatErworbenErweiterteBeruflicheKenntnisse" : ' + obj.hatErworbenErweiterteBeruflicheKenntnisse + ',';
		result += '"notePraktischePruefung" : ' + ((!obj.notePraktischePruefung) ? 'null' : '"' + obj.notePraktischePruefung + '"') + ',';
		result += '"noteKolloqium" : ' + ((!obj.noteKolloqium) ? 'null' : '"' + obj.noteKolloqium + '"') + ',';
		result += '"hatZulassungBerufsabschlusspruefung" : ' + obj.hatZulassungBerufsabschlusspruefung + ',';
		result += '"hatBestandenBerufsabschlusspruefung" : ' + obj.hatBestandenBerufsabschlusspruefung + ',';
		result += '"themaAbschlussarbeit" : ' + ((!obj.themaAbschlussarbeit) ? 'null' : '"' + obj.themaAbschlussarbeit + '"') + ',';
		result += '"istVorhandenBerufsabschlusspruefung" : ' + obj.istVorhandenBerufsabschlusspruefung + ',';
		result += '"noteFachpraxis" : ' + ((!obj.noteFachpraxis) ? 'null' : '"' + obj.noteFachpraxis + '"') + ',';
		result += '"istFachpraktischerTeilAusreichend" : ' + obj.istFachpraktischerTeilAusreichend + ',';
		if (!obj.faecher) {
			result += '"faecher" : []';
		} else {
			result += '"faecher" : [ ';
			for (let i = 0; i < obj.faecher.size(); i++) {
				const elem = obj.faecher.get(i);
				result += ENMBKFach.transpilerToJSON(elem);
				if (i < obj.faecher.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ENMBKAbschluss>) : string {
		let result = '{';
		if (typeof obj.hatZulassung !== "undefined") {
			result += '"hatZulassung" : ' + obj.hatZulassung + ',';
		}
		if (typeof obj.hatBestanden !== "undefined") {
			result += '"hatBestanden" : ' + obj.hatBestanden + ',';
		}
		if (typeof obj.hatZulassungErweiterteBeruflicheKenntnisse !== "undefined") {
			result += '"hatZulassungErweiterteBeruflicheKenntnisse" : ' + obj.hatZulassungErweiterteBeruflicheKenntnisse + ',';
		}
		if (typeof obj.hatErworbenErweiterteBeruflicheKenntnisse !== "undefined") {
			result += '"hatErworbenErweiterteBeruflicheKenntnisse" : ' + obj.hatErworbenErweiterteBeruflicheKenntnisse + ',';
		}
		if (typeof obj.notePraktischePruefung !== "undefined") {
			result += '"notePraktischePruefung" : ' + ((!obj.notePraktischePruefung) ? 'null' : '"' + obj.notePraktischePruefung + '"') + ',';
		}
		if (typeof obj.noteKolloqium !== "undefined") {
			result += '"noteKolloqium" : ' + ((!obj.noteKolloqium) ? 'null' : '"' + obj.noteKolloqium + '"') + ',';
		}
		if (typeof obj.hatZulassungBerufsabschlusspruefung !== "undefined") {
			result += '"hatZulassungBerufsabschlusspruefung" : ' + obj.hatZulassungBerufsabschlusspruefung + ',';
		}
		if (typeof obj.hatBestandenBerufsabschlusspruefung !== "undefined") {
			result += '"hatBestandenBerufsabschlusspruefung" : ' + obj.hatBestandenBerufsabschlusspruefung + ',';
		}
		if (typeof obj.themaAbschlussarbeit !== "undefined") {
			result += '"themaAbschlussarbeit" : ' + ((!obj.themaAbschlussarbeit) ? 'null' : '"' + obj.themaAbschlussarbeit + '"') + ',';
		}
		if (typeof obj.istVorhandenBerufsabschlusspruefung !== "undefined") {
			result += '"istVorhandenBerufsabschlusspruefung" : ' + obj.istVorhandenBerufsabschlusspruefung + ',';
		}
		if (typeof obj.noteFachpraxis !== "undefined") {
			result += '"noteFachpraxis" : ' + ((!obj.noteFachpraxis) ? 'null' : '"' + obj.noteFachpraxis + '"') + ',';
		}
		if (typeof obj.istFachpraktischerTeilAusreichend !== "undefined") {
			result += '"istFachpraktischerTeilAusreichend" : ' + obj.istFachpraktischerTeilAusreichend + ',';
		}
		if (typeof obj.faecher !== "undefined") {
			if (!obj.faecher) {
				result += '"faecher" : []';
			} else {
				result += '"faecher" : [ ';
				for (let i = 0; i < obj.faecher.size(); i++) {
					const elem = obj.faecher.get(i);
					result += ENMBKFach.transpilerToJSON(elem);
					if (i < obj.faecher.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_enm_ENMBKAbschluss(obj : unknown) : ENMBKAbschluss {
	return obj as ENMBKAbschluss;
}
