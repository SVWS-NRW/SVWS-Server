import { JavaObject } from '../../java/lang/JavaObject';
import { ValidatorManager } from '../../asd/validate/ValidatorManager';
import { HashMap } from '../../java/util/HashMap';
import { CoreTypeDataManager } from '../../asd/utils/CoreTypeDataManager';
import { Schulform } from '../../asd/types/schule/Schulform';
import { SchuleStammdaten } from '../../asd/data/schule/SchuleStammdaten';
import { DateManager } from '../../asd/validate/DateManager';
import { ValidatorException } from '../../asd/validate/ValidatorException';
import { Class } from '../../java/lang/Class';
import type { JavaMap } from '../../java/util/JavaMap';
import { Schuljahresabschnitt } from '../../asd/data/schule/Schuljahresabschnitt';
import { CoreTypeException } from '../../asd/data/CoreTypeException';

export class ValidatorKontext extends JavaObject {

	/**
	 * Die Stammdaten der Schule
	 */
	private readonly _schuleStammdaten : SchuleStammdaten;

	/**
	 * Die Laufeigenschaften der Validatoren
	 */
	private readonly _validatorManager : ValidatorManager;

	/**
	 * Die Schuljahresabschnitte der Schule, welche ihrer ID zugeordnet werden
	 */
	private readonly _mapSchuljahresabschnitte : JavaMap<number, Schuljahresabschnitt> = new HashMap<number, Schuljahresabschnitt>();


	/**
	 * Erzeugt einen neuen Kontext für Validatoren. Prüfe auch, ob die Stammdaten der Schule eine Valiadierung möglich machen
	 * oder aufgrund gravierender Fehler eine Prüfungen unmöglich machen.
	 *
	 * @param schuleStammdaten   die Stammdaten der Schule für den Kontext
	 * @param zebras             die Umgebung, in der gerade validiert wird: true: ZeBrAS, false: SVWS
	 */
	public constructor(schuleStammdaten : SchuleStammdaten, zebras : boolean) {
		super();
		this._schuleStammdaten = schuleStammdaten;
		for (const entry of schuleStammdaten.abschnitte)
			this._mapSchuljahresabschnitte.put(entry.id, entry);
		const schulform : Schulform | null = CoreTypeDataManager.getManager(Schulform.class).getWertByBezeichner(schuleStammdaten.schulform);
		this._validatorManager = ValidatorManager.getManager(schulform, zebras);
	}

	/**
	 * Gibt die Stammdaten der Schule zurück.
	 *
	 * @return die Stammdaten der Schule
	 */
	public getSchuleStammdaten() : SchuleStammdaten | null {
		return this._schuleStammdaten;
	}

	/**
	 * Gibt die Schulnummer der Schule zurück.
	 *
	 * @return die Schulnummer der Schule
	 */
	public getSchulnummer() : number {
		return this._schuleStammdaten.schulNr;
	}

	/**
	 * Gibt die Schulform der Schule anhand der Information aus den
	 * Stammdaten der Schule zurück.
	 *
	 * @return die Schulform als Core-Type
	 */
	public getSchulform() : Schulform {
		const schulform : Schulform | null = Schulform.data().getWertByKuerzel(this._schuleStammdaten.schulform);
		if (schulform !== null)
			return schulform;
		throw new CoreTypeException("Die Schulform " + this._schuleStammdaten.schulform + " existiert nicht in 'Schulform.json'.")
	}

	/**
	 * Gibt das aktuelle Schuljahr der Schule zurück.
	 *
	 * @return das aktuelle Schuljahr
	 */
	public getSchuljahr() : number {
		const abschnitt : Schuljahresabschnitt | null = this.getSchuljahresabschnitt();
		if (abschnitt !== null)
			return abschnitt.schuljahr;
		throw new ValidatorException("Es ist kein gültiger Schuljahresabschnitt in den SchuleStammdaten gesetzt")
	}

	/**
	 * Gibt den Datums-Manager für den Beginn des aktuellen Schuljahres zurück.
	 *
	 * @return der Datums-Manager für den Beginn des aktuellen Schuljahres
	 */
	public getSchuljahresbeginn() : DateManager {
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
	public getSchuljahresende() : DateManager {
		try {
			return DateManager.fromValues(this.getSchuljahr() + 1, 7, 31);
		} catch(e : any) {
			throw new ValidatorException("Fehler beim Erstellen des Datums für das Ende des Schuljahres", e)
		}
	}

	/**
	 * Gibt den aktuellen Schuljahresabschnitt der Schule zurück.
	 *
	 * @return der Schuljahresabschnitt oder null, wenn dieser nicht korrekt gesetzt ist
	 */
	public getSchuljahresabschnitt() : Schuljahresabschnitt | null {
		return this._mapSchuljahresabschnitte.get(this._schuleStammdaten.idSchuljahresabschnitt);
	}

	/**
	 * Gibt den Schuljahresabschnitt der Schule für die übergebene ID zurück.
	 *
	 * @param id   die ID des Schuljahresabschnitts
	 *
	 * @return der Schuljahresabschnitt oder null, falls die id ungültig ist
	 */
	public getSchuljahresabschnittByID(id : number) : Schuljahresabschnitt | null {
		return this._mapSchuljahresabschnitte.get(id);
	}

	/**
	 * Gibt den ValidatorManager zurück.
	 *
	 * @return der ValidatorManager
	 */
	public getValidatorManager() : ValidatorManager {
		return this._validatorManager;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.ValidatorKontext';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.validate.ValidatorKontext'].includes(name);
	}

	public static class = new Class<ValidatorKontext>('de.svws_nrw.asd.validate.ValidatorKontext');

}

export function cast_de_svws_nrw_asd_validate_ValidatorKontext(obj : unknown) : ValidatorKontext {
	return obj as ValidatorKontext;
}
