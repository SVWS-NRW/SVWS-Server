import { JavaObject } from '../../../java/lang/JavaObject';
import { ENMBKFach } from '../../../core/data/enm/ENMBKFach';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

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
	public faecher : List<ENMBKFach> = new ArrayList<ENMBKFach>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.enm.ENMBKAbschluss';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.enm.ENMBKAbschluss'].includes(name);
	}

	public static class = new Class<ENMBKAbschluss>('de.svws_nrw.core.data.enm.ENMBKAbschluss');

	public static transpilerFromJSON(json : string): ENMBKAbschluss {
		const obj = JSON.parse(json) as Partial<ENMBKAbschluss>;
		const result = new ENMBKAbschluss();
		if (obj.hatZulassung === undefined)
			throw new Error('invalid json format, missing attribute hatZulassung');
		result.hatZulassung = obj.hatZulassung;
		if (obj.hatBestanden === undefined)
			throw new Error('invalid json format, missing attribute hatBestanden');
		result.hatBestanden = obj.hatBestanden;
		if (obj.hatZulassungErweiterteBeruflicheKenntnisse === undefined)
			throw new Error('invalid json format, missing attribute hatZulassungErweiterteBeruflicheKenntnisse');
		result.hatZulassungErweiterteBeruflicheKenntnisse = obj.hatZulassungErweiterteBeruflicheKenntnisse;
		if (obj.hatErworbenErweiterteBeruflicheKenntnisse === undefined)
			throw new Error('invalid json format, missing attribute hatErworbenErweiterteBeruflicheKenntnisse');
		result.hatErworbenErweiterteBeruflicheKenntnisse = obj.hatErworbenErweiterteBeruflicheKenntnisse;
		result.notePraktischePruefung = (obj.notePraktischePruefung === undefined) ? null : obj.notePraktischePruefung === null ? null : obj.notePraktischePruefung;
		result.noteKolloqium = (obj.noteKolloqium === undefined) ? null : obj.noteKolloqium === null ? null : obj.noteKolloqium;
		if (obj.hatZulassungBerufsabschlusspruefung === undefined)
			throw new Error('invalid json format, missing attribute hatZulassungBerufsabschlusspruefung');
		result.hatZulassungBerufsabschlusspruefung = obj.hatZulassungBerufsabschlusspruefung;
		if (obj.hatBestandenBerufsabschlusspruefung === undefined)
			throw new Error('invalid json format, missing attribute hatBestandenBerufsabschlusspruefung');
		result.hatBestandenBerufsabschlusspruefung = obj.hatBestandenBerufsabschlusspruefung;
		result.themaAbschlussarbeit = (obj.themaAbschlussarbeit === undefined) ? null : obj.themaAbschlussarbeit === null ? null : obj.themaAbschlussarbeit;
		if (obj.istVorhandenBerufsabschlusspruefung === undefined)
			throw new Error('invalid json format, missing attribute istVorhandenBerufsabschlusspruefung');
		result.istVorhandenBerufsabschlusspruefung = obj.istVorhandenBerufsabschlusspruefung;
		result.noteFachpraxis = (obj.noteFachpraxis === undefined) ? null : obj.noteFachpraxis === null ? null : obj.noteFachpraxis;
		if (obj.istFachpraktischerTeilAusreichend === undefined)
			throw new Error('invalid json format, missing attribute istFachpraktischerTeilAusreichend');
		result.istFachpraktischerTeilAusreichend = obj.istFachpraktischerTeilAusreichend;
		if (obj.faecher !== undefined) {
			for (const elem of obj.faecher) {
				result.faecher.add(ENMBKFach.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : ENMBKAbschluss) : string {
		let result = '{';
		result += '"hatZulassung" : ' + obj.hatZulassung.toString() + ',';
		result += '"hatBestanden" : ' + obj.hatBestanden.toString() + ',';
		result += '"hatZulassungErweiterteBeruflicheKenntnisse" : ' + obj.hatZulassungErweiterteBeruflicheKenntnisse.toString() + ',';
		result += '"hatErworbenErweiterteBeruflicheKenntnisse" : ' + obj.hatErworbenErweiterteBeruflicheKenntnisse.toString() + ',';
		result += '"notePraktischePruefung" : ' + ((obj.notePraktischePruefung === null) ? 'null' : JSON.stringify(obj.notePraktischePruefung)) + ',';
		result += '"noteKolloqium" : ' + ((obj.noteKolloqium === null) ? 'null' : JSON.stringify(obj.noteKolloqium)) + ',';
		result += '"hatZulassungBerufsabschlusspruefung" : ' + obj.hatZulassungBerufsabschlusspruefung.toString() + ',';
		result += '"hatBestandenBerufsabschlusspruefung" : ' + obj.hatBestandenBerufsabschlusspruefung.toString() + ',';
		result += '"themaAbschlussarbeit" : ' + ((obj.themaAbschlussarbeit === null) ? 'null' : JSON.stringify(obj.themaAbschlussarbeit)) + ',';
		result += '"istVorhandenBerufsabschlusspruefung" : ' + obj.istVorhandenBerufsabschlusspruefung.toString() + ',';
		result += '"noteFachpraxis" : ' + ((obj.noteFachpraxis === null) ? 'null' : JSON.stringify(obj.noteFachpraxis)) + ',';
		result += '"istFachpraktischerTeilAusreichend" : ' + obj.istFachpraktischerTeilAusreichend.toString() + ',';
		result += '"faecher" : [ ';
		for (let i = 0; i < obj.faecher.size(); i++) {
			const elem = obj.faecher.get(i);
			result += ENMBKFach.transpilerToJSON(elem);
			if (i < obj.faecher.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ENMBKAbschluss>) : string {
		let result = '{';
		if (obj.hatZulassung !== undefined) {
			result += '"hatZulassung" : ' + obj.hatZulassung.toString() + ',';
		}
		if (obj.hatBestanden !== undefined) {
			result += '"hatBestanden" : ' + obj.hatBestanden.toString() + ',';
		}
		if (obj.hatZulassungErweiterteBeruflicheKenntnisse !== undefined) {
			result += '"hatZulassungErweiterteBeruflicheKenntnisse" : ' + obj.hatZulassungErweiterteBeruflicheKenntnisse.toString() + ',';
		}
		if (obj.hatErworbenErweiterteBeruflicheKenntnisse !== undefined) {
			result += '"hatErworbenErweiterteBeruflicheKenntnisse" : ' + obj.hatErworbenErweiterteBeruflicheKenntnisse.toString() + ',';
		}
		if (obj.notePraktischePruefung !== undefined) {
			result += '"notePraktischePruefung" : ' + ((obj.notePraktischePruefung === null) ? 'null' : JSON.stringify(obj.notePraktischePruefung)) + ',';
		}
		if (obj.noteKolloqium !== undefined) {
			result += '"noteKolloqium" : ' + ((obj.noteKolloqium === null) ? 'null' : JSON.stringify(obj.noteKolloqium)) + ',';
		}
		if (obj.hatZulassungBerufsabschlusspruefung !== undefined) {
			result += '"hatZulassungBerufsabschlusspruefung" : ' + obj.hatZulassungBerufsabschlusspruefung.toString() + ',';
		}
		if (obj.hatBestandenBerufsabschlusspruefung !== undefined) {
			result += '"hatBestandenBerufsabschlusspruefung" : ' + obj.hatBestandenBerufsabschlusspruefung.toString() + ',';
		}
		if (obj.themaAbschlussarbeit !== undefined) {
			result += '"themaAbschlussarbeit" : ' + ((obj.themaAbschlussarbeit === null) ? 'null' : JSON.stringify(obj.themaAbschlussarbeit)) + ',';
		}
		if (obj.istVorhandenBerufsabschlusspruefung !== undefined) {
			result += '"istVorhandenBerufsabschlusspruefung" : ' + obj.istVorhandenBerufsabschlusspruefung.toString() + ',';
		}
		if (obj.noteFachpraxis !== undefined) {
			result += '"noteFachpraxis" : ' + ((obj.noteFachpraxis === null) ? 'null' : JSON.stringify(obj.noteFachpraxis)) + ',';
		}
		if (obj.istFachpraktischerTeilAusreichend !== undefined) {
			result += '"istFachpraktischerTeilAusreichend" : ' + obj.istFachpraktischerTeilAusreichend.toString() + ',';
		}
		if (obj.faecher !== undefined) {
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

}

export function cast_de_svws_nrw_core_data_enm_ENMBKAbschluss(obj : unknown) : ENMBKAbschluss {
	return obj as ENMBKAbschluss;
}
