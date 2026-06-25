import type { Supplier } from '../../../java/util/function/Supplier';
import { ValidatorSlk01SchuelerLernabschnittsdatenKlassenart } from '../../../asd/validate/schueler/ValidatorSlk01SchuelerLernabschnittsdatenKlassenart';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSlk00SchuelerLernabschnittsdatenKlassenart extends Validator {

	/**
	 * Geburtsland
	 */
	private readonly _idKlassenart: Supplier<number | null>;

	private static readonly FEHLERTEXT: string = "Klassenart des Schülers: Das Feld darf nicht leer sein";


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idKlassenart  die Klassenart ID
	 * @param kontext       der Kontext des Validators
	 */
	public constructor(idKlassenart: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idKlassenart = idKlassenart;
		this._validatoren.add(new ValidatorSlk01SchuelerLernabschnittsdatenKlassenart(this.getNotNullSupplierLong(idKlassenart), kontext));
	}

	protected pruefe(): boolean {
		const idKlassenart: number | null = this._idKlassenart.get();
		if (idKlassenart === null) {
			this.addFehler(0, ValidatorSlk00SchuelerLernabschnittsdatenKlassenart.FEHLERTEXT);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSlk00SchuelerLernabschnittsdatenKlassenart';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSlk00SchuelerLernabschnittsdatenKlassenart', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSlk00SchuelerLernabschnittsdatenKlassenart>('de.svws_nrw.asd.validate.schueler.ValidatorSlk00SchuelerLernabschnittsdatenKlassenart');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSlk00SchuelerLernabschnittsdatenKlassenart(obj: unknown): ValidatorSlk00SchuelerLernabschnittsdatenKlassenart {
	return obj as ValidatorSlk00SchuelerLernabschnittsdatenKlassenart;
}
