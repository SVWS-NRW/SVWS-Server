import { ValidatorLplf01LehrerPersonaldatenLehramtFachrichtung } from '../../../asd/validate/lehrer/ValidatorLplf01LehrerPersonaldatenLehramtFachrichtung';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLplf00LehrerPersonaldatenLehramtFachrichtung extends Validator {

	/**
	 * Die Katalog-ID der Fachrichtung.
	 */
	private readonly _idFachrichtung: Supplier<number>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext.
	 *
	 * @param idFachrichtung   die Katalog-ID der Fachrichtung
	 * @param kontext          der Kontext des Validators
	 */
	public constructor(idFachrichtung: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._idFachrichtung = idFachrichtung;
		this._validatoren.add(new ValidatorLplf01LehrerPersonaldatenLehramtFachrichtung(this.getNotNullSupplierLong(idFachrichtung), kontext));
	}

	protected pruefe(): boolean {
		const idFachrichtung: number | null = this._idFachrichtung.get();
		if (idFachrichtung === null) {
			this.addFehler(0, "Lehrer Fachrichtung: Das Feld darf nicht leer sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLplf00LehrerPersonaldatenLehramtFachrichtung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLplf00LehrerPersonaldatenLehramtFachrichtung', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLplf00LehrerPersonaldatenLehramtFachrichtung>('de.svws_nrw.asd.validate.lehrer.ValidatorLplf00LehrerPersonaldatenLehramtFachrichtung');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLplf00LehrerPersonaldatenLehramtFachrichtung(obj: unknown): ValidatorLplf00LehrerPersonaldatenLehramtFachrichtung {
	return obj as ValidatorLplf00LehrerPersonaldatenLehramtFachrichtung;
}
