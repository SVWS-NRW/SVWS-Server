import { JavaObject } from '../../../../java/lang/JavaObject';
import { KursDaten } from '../../../../asd/data/kurse/KursDaten';
import { GostFach } from '../../../../core/data/gost/GostFach';
import { SchuelerListeEintrag } from '../../../../core/data/schueler/SchuelerListeEintrag';
import { ArrayList } from '../../../../java/util/ArrayList';
import { GostKlausurenKlausurdaten } from '../../../../core/data/gost/klausuren/GostKlausurenKlausurdaten';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';
import { GostKlausurenRaumdaten } from '../../../../core/data/gost/klausuren/GostKlausurenRaumdaten';

export class GostKlausurenHalbjahresdaten extends JavaObject {

	/**
	 * Der Abiturjahrgang.
	 */
	public abiturjahrgang: number = -1;

	/**
	 * Das Gost-Halbjahr.
	 */
	public gostHalbjahr: number = -1;

	/**
	 * Die ID des Schuljahresabschnitts.
	 */
	public idSchuljahresabschnitt: number = -1;

	/**
	 * Die zu den Klausurdaten gehörenden Meta-Informationen wie Fachdaten, Kursdaten, Lehrerdaten, Schülerdaten.
	 */
	public klausurdaten: GostKlausurenKlausurdaten = new GostKlausurenKlausurdaten();

	/**
	 * Ein Array mit den Daten der Fächer.
	 */
	public faecher: List<GostFach> | null = null;

	/**
	 * Ein Array mit den Daten der Schüler.
	 */
	public schueler: List<SchuelerListeEintrag> | null = null;

	/**
	 * Ein Array mit den Daten der Kurse.
	 */
	public kurse: List<KursDaten> = new ArrayList<KursDaten>();

	/**
	 * Die zu den Klausurdaten gehörenden Raumdaten.
	 */
	public raumdaten: GostKlausurenRaumdaten = new GostKlausurenRaumdaten();


	/**
	 * Default-Konstruktor
	 */
	public constructor();

	/**
	 * Konstruktor für die Angabe des Abiturjahrgangs und des Gost-Halbjahres.
	 * @param abiturjahrgang der Abiturjahrgang
	 * @param gostHalbjahr das Gost-Halbjahr
	 */
	public constructor(abiturjahrgang: number, gostHalbjahr: number);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0?: number, __param1?: number) {
		super();
		if ((__param0 === undefined) && (__param1 === undefined)) {
			// empty method body
		} else if (((__param0 !== undefined) && typeof __param0 === "number") && ((__param1 !== undefined) && typeof __param1 === "number")) {
			const abiturjahrgang: number = __param0 as number;
			const gostHalbjahr: number = __param1 as number;
			this.abiturjahrgang = abiturjahrgang;
			this.gostHalbjahr = gostHalbjahr;
		} else throw new Error('invalid method overload');
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.klausuren.GostKlausurenHalbjahresdaten';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.gost.klausuren.GostKlausurenHalbjahresdaten'].includes(name);
	}

	public static readonly class = new Class<GostKlausurenHalbjahresdaten>('de.svws_nrw.core.data.gost.klausuren.GostKlausurenHalbjahresdaten');

	public static transpilerFromJSON(json: string): GostKlausurenHalbjahresdaten {
		const obj = JSON.parse(json) as Partial<GostKlausurenHalbjahresdaten>;
		const result = new GostKlausurenHalbjahresdaten();
		if (obj.abiturjahrgang === undefined)
			throw new Error('invalid json format, missing attribute abiturjahrgang');
		result.abiturjahrgang = obj.abiturjahrgang;
		if (obj.gostHalbjahr === undefined)
			throw new Error('invalid json format, missing attribute gostHalbjahr');
		result.gostHalbjahr = obj.gostHalbjahr;
		if (obj.idSchuljahresabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idSchuljahresabschnitt');
		result.idSchuljahresabschnitt = obj.idSchuljahresabschnitt;
		if (obj.klausurdaten === undefined)
			throw new Error('invalid json format, missing attribute klausurdaten');
		result.klausurdaten = GostKlausurenKlausurdaten.transpilerFromJSON(JSON.stringify(obj.klausurdaten));
		if ((obj.faecher !== undefined) && (obj.faecher !== null)) {
			result.faecher = new ArrayList();
			for (const elem of obj.faecher) {
				result.faecher.add(GostFach.transpilerFromJSON(JSON.stringify(elem)));
			}
		} else {
			result.faecher = null;
		}
		if ((obj.schueler !== undefined) && (obj.schueler !== null)) {
			result.schueler = new ArrayList();
			for (const elem of obj.schueler) {
				result.schueler.add(SchuelerListeEintrag.transpilerFromJSON(JSON.stringify(elem)));
			}
		} else {
			result.schueler = null;
		}
		if (obj.kurse !== undefined) {
			for (const elem of obj.kurse) {
				result.kurse.add(KursDaten.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.raumdaten === undefined)
			throw new Error('invalid json format, missing attribute raumdaten');
		result.raumdaten = GostKlausurenRaumdaten.transpilerFromJSON(JSON.stringify(obj.raumdaten));
		return result;
	}

	public static transpilerToJSON(obj: GostKlausurenHalbjahresdaten): string {
		let result = '{';
		result += '"abiturjahrgang" : ' + obj.abiturjahrgang.toString() + ',';
		result += '"gostHalbjahr" : ' + obj.gostHalbjahr.toString() + ',';
		result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		result += '"klausurdaten" : ' + GostKlausurenKlausurdaten.transpilerToJSON(obj.klausurdaten) + ',';
		if (!obj.faecher) {
			result += '"faecher" : null' + ',';
		} else {
			result += '"faecher" : [ ';
			for (let i = 0; i < obj.faecher.size(); i++) {
				const elem = obj.faecher.get(i);
				result += GostFach.transpilerToJSON(elem);
				if (i < obj.faecher.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.schueler) {
			result += '"schueler" : null' + ',';
		} else {
			result += '"schueler" : [ ';
			for (let i = 0; i < obj.schueler.size(); i++) {
				const elem = obj.schueler.get(i);
				result += SchuelerListeEintrag.transpilerToJSON(elem);
				if (i < obj.schueler.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"kurse" : [ ';
		for (let i = 0; i < obj.kurse.size(); i++) {
			const elem = obj.kurse.get(i);
			result += KursDaten.transpilerToJSON(elem);
			if (i < obj.kurse.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"raumdaten" : ' + GostKlausurenRaumdaten.transpilerToJSON(obj.raumdaten) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<GostKlausurenHalbjahresdaten>): string {
		let result = '{';
		if (obj.abiturjahrgang !== undefined) {
			result += '"abiturjahrgang" : ' + obj.abiturjahrgang.toString() + ',';
		}
		if (obj.gostHalbjahr !== undefined) {
			result += '"gostHalbjahr" : ' + obj.gostHalbjahr.toString() + ',';
		}
		if (obj.idSchuljahresabschnitt !== undefined) {
			result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		}
		if (obj.klausurdaten !== undefined) {
			result += '"klausurdaten" : ' + GostKlausurenKlausurdaten.transpilerToJSON(obj.klausurdaten) + ',';
		}
		if (obj.faecher !== undefined) {
			if (!obj.faecher) {
				result += '"faecher" : null' + ',';
			} else {
				result += '"faecher" : [ ';
				for (let i = 0; i < obj.faecher.size(); i++) {
					const elem = obj.faecher.get(i);
					result += GostFach.transpilerToJSON(elem);
					if (i < obj.faecher.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (obj.schueler !== undefined) {
			if (!obj.schueler) {
				result += '"schueler" : null' + ',';
			} else {
				result += '"schueler" : [ ';
				for (let i = 0; i < obj.schueler.size(); i++) {
					const elem = obj.schueler.get(i);
					result += SchuelerListeEintrag.transpilerToJSON(elem);
					if (i < obj.schueler.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (obj.kurse !== undefined) {
			result += '"kurse" : [ ';
			for (let i = 0; i < obj.kurse.size(); i++) {
				const elem = obj.kurse.get(i);
				result += KursDaten.transpilerToJSON(elem);
				if (i < obj.kurse.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.raumdaten !== undefined) {
			result += '"raumdaten" : ' + GostKlausurenRaumdaten.transpilerToJSON(obj.raumdaten) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_klausuren_GostKlausurenHalbjahresdaten(obj: unknown): GostKlausurenHalbjahresdaten {
	return obj as GostKlausurenHalbjahresdaten;
}
