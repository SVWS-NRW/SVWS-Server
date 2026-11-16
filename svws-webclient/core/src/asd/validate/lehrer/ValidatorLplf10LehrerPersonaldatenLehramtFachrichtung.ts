import type { JavaSet } from '../../../java/util/JavaSet';
import { java_util_Set_of } from '../../../java/util/JavaSet';
import { LehrerLehramt } from '../../../asd/types/lehrer/LehrerLehramt';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLplf10LehrerPersonaldatenLehramtFachrichtung extends Validator {

	/**
	 * Das aktuelle Schuljahr des Lehrers
	 */
	private readonly _schuljahr: Supplier<number>;

	/**
	 * Die Katalog-ID der Fachrichtung.
	 */
	private readonly _idFachrichtung: Supplier<number | null>;

	/**
	 * Die Katalog-ID des Lehramtes.
	 */
	private readonly _idLehramt: Supplier<number | null>;

	private static readonly listeLehraemter: JavaSet<LehrerLehramt> = java_util_Set_of(LehrerLehramt.ID_08, LehrerLehramt.ID_09, LehrerLehramt.ID_10, LehrerLehramt.ID_11, LehrerLehramt.ID_12, LehrerLehramt.ID_14, LehrerLehramt.ID_27, LehrerLehramt.ID_31, LehrerLehramt.ID_35, LehrerLehramt.ID_90, LehrerLehramt.ID_96, LehrerLehramt.ID_98);


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext.
	 *
	 * @param schuljahr        Schuljahr
	 * @param idLehramt        die Katalog-ID des Lehramtes
	 * @param idFachrichtung   die Katalog-ID der Fachrichtung
	 * @param kontext          der Kontext des Validators
	 */
	public constructor(schuljahr: Supplier<number>, idLehramt: Supplier<number | null>, idFachrichtung: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._schuljahr = schuljahr;
		this._idLehramt = idLehramt;
		this._idFachrichtung = idFachrichtung;
	}

	protected pruefe(): boolean {
		const idFachrichtung: number | null = this._idFachrichtung.get();
		const lehrerLehramt: LehrerLehramt | null = LehrerLehramt.data().getWertByIDOrNull(this._idLehramt.get());
		if (lehrerLehramt !== null && ValidatorLplf10LehrerPersonaldatenLehramtFachrichtung.listeLehraemter.contains(lehrerLehramt) && idFachrichtung !== null) {
			this.addFehler(0, "Fachrichtung des Lehrers: Bei Lehrkräften, die über das angegebene Lehramt verfügen, darf das Feld 'Fachrichtung' nicht besetzt sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLplf10LehrerPersonaldatenLehramtFachrichtung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLplf10LehrerPersonaldatenLehramtFachrichtung', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLplf10LehrerPersonaldatenLehramtFachrichtung>('de.svws_nrw.asd.validate.lehrer.ValidatorLplf10LehrerPersonaldatenLehramtFachrichtung');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLplf10LehrerPersonaldatenLehramtFachrichtung(obj: unknown): ValidatorLplf10LehrerPersonaldatenLehramtFachrichtung {
	return obj as ValidatorLplf10LehrerPersonaldatenLehramtFachrichtung;
}
