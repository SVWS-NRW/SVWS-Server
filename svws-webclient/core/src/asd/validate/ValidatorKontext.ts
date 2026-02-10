import { JavaObject } from '../../java/lang/JavaObject';
import { ValidatorManager } from '../../asd/validate/ValidatorManager';
import { DateManager } from '../../asd/validate/DateManager';
import { ValidatorException } from '../../asd/validate/ValidatorException';
import { HashMap } from '../../java/util/HashMap';
import { Schulform } from '../../asd/types/schule/Schulform';
import type { List } from '../../java/util/List';
import { Class } from '../../java/lang/Class';
import type { JavaMap } from '../../java/util/JavaMap';
import { Schuljahresabschnitt } from '../../asd/data/schule/Schuljahresabschnitt';

export class ValidatorKontext extends JavaObject {

	/**
	 * Die Schulnummer der Schule
	 */
	private readonly _schulNr: number;

	/**
	 * Die Stammdaten der Schule
	 */
	private readonly _schulform: Schulform;

	/**
	 * Die Laufeigenschaften der Validatoren
	 */
	private readonly _validatorManager: ValidatorManager;

	/**
	 * Die ID des aktuellen Schuljahresabschnittes der Schule
	 */
	private readonly _idSchuljahresabsbschnittAktuell: number;

	/**
	 * Die Schuljahresabschnitte der Schule, welche ihrer ID zugeordnet werden
	 */
	private readonly _mapSchuljahresabschnitte: JavaMap<number, Schuljahresabschnitt> = new HashMap<number, Schuljahresabschnitt>();


	/**
	 * Erzeugt einen neuen Kontext für Validatoren. Prüfe auch, ob die Stammdaten der Schule eine Valiadierung möglich machen
	 * oder aufgrund gravierender Fehler eine Prüfungen unmöglich machen.
	 *
	 * @param schulNr                         die Schulnummer der Schule
	 * @param schulform                       die Schulform der Schule
	 * @param abschnitte                      die Liste der Schuljahresabschnitte der Schule
	 * @param idSchuljahresabsbschnittAktuell die ID des aktuellen Schuljahresabschnittes der Schule
	 * @param zebras                          die Umgebung, in der gerade validiert wird: true: ZeBrAS, false: SVWS
	 */
	public constructor(schulNr: number, schulform: Schulform, abschnitte: List<Schuljahresabschnitt>, idSchuljahresabsbschnittAktuell: number, zebras: boolean) {
		super();
		this._schulNr = schulNr;
		this._schulform = schulform;
		this._idSchuljahresabsbschnittAktuell = idSchuljahresabsbschnittAktuell;
		for (const entry of abschnitte)
			this._mapSchuljahresabschnitte.put(entry.id, entry);
		this._validatorManager = ValidatorManager.getManager(schulform, zebras);
	}

	/**
	 * Gibt die Schulform der Schule anhand der Information aus den
	 * Stammdaten der Schule zurück.
	 *
	 * @return die Schulform als Core-Type
	 */
	public getSchulform(): Schulform {
		return this._schulform;
	}

	/**
	 * Gibt das aktuelle Schuljahr der Schule zurück.
	 *
	 * @return das aktuelle Schuljahr
	 */
	public getSchuljahr(): number {
		const abschnitt: Schuljahresabschnitt | null = this.getSchuljahresabschnitt();
		if (abschnitt !== null)
			return abschnitt.schuljahr;
		throw new ValidatorException("Es ist kein gültiger Schuljahresabschnitt in den SchuleStammdaten gesetzt")
	}

	/**
	 * Gibt den aktuellen Schuljahresabschnitt der Schule zurück.
	 *
	 * @return der Schuljahresabschnitt oder null, wenn dieser nicht korrekt gesetzt ist
	 */
	public getSchuljahresabschnitt(): Schuljahresabschnitt | null {
		return this._mapSchuljahresabschnitte.get(this._idSchuljahresabsbschnittAktuell);
	}

	/**
	 * Gibt den Schuljahresabschnitt der Schule für die übergebene ID zurück.
	 *
	 * @param id   die ID des Schuljahresabschnitts
	 *
	 * @return der Schuljahresabschnitt oder null, falls die id ungültig ist
	 */
	public getSchuljahresabschnittByID(id: number): Schuljahresabschnitt | null {
		return this._mapSchuljahresabschnitte.get(id);
	}

	/**
	 * Gibt den Datums-Manager für den Beginn des aktuellen Schuljahres zurück.
	 *
	 * @return der Datums-Manager für den Beginn des aktuellen Schuljahres
	 */
	public getSchuljahresbeginn(): DateManager {
		try {
			return DateManager.fromValues(this.getSchuljahr(), 8, 1);
		} catch(e : any) {
			throw new ValidatorException("Fehler beim Erstellen des Datums für den Beginn des Schuljahres", e)
		}
	}

	/**
	 * Gibt den Datums-Manager für das Ende des aktuellen Schuljahres zurück.
	 *
	 * @return der Datums-Manager für das Ende des aktuellen Schuljahres
	 */
	public getSchuljahresende(): DateManager {
		try {
			return DateManager.fromValues(this.getSchuljahr() + 1, 7, 31);
		} catch(e : any) {
			throw new ValidatorException("Fehler beim Erstellen des Datums für das Ende des Schuljahres", e)
		}
	}

	/**
	 * Gibt die Schulnummer der Schule zurück.
	 *
	 * @return die Schulnummer der Schule
	 */
	public getSchulnummer(): number {
		return this._schulNr;
	}

	/**
	 * Gibt den ValidatorManager zurück.
	 *
	 * @return der ValidatorManager
	 */
	public getValidatorManager(): ValidatorManager {
		return this._validatorManager;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.ValidatorKontext';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.ValidatorKontext'].includes(name);
	}

	public static readonly class = new Class<ValidatorKontext>('de.svws_nrw.asd.validate.ValidatorKontext');

}

export function cast_de_svws_nrw_asd_validate_ValidatorKontext(obj: unknown): ValidatorKontext {
	return obj as ValidatorKontext;
}
