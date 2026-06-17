import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { LehrerEinsatzstatus } from '../../../asd/types/lehrer/LehrerEinsatzstatus';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppe02LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus extends Validator {

	/**
	 * Der Einsatzstatus
	 */
	private readonly _idEinsatzstatus: Supplier<number>;


	/**
	 * Erstellt einen neuen Validator für das vorhandensein des Einsatzstatus im Katalog.
	 *
	 * @param idEinsatzstatus   der Einsatzstatus
	 * @param kontext           der Kontext des Validators
	 */
	public constructor(idEinsatzstatus: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._idEinsatzstatus = idEinsatzstatus;
	}

	protected pruefe(): boolean {
		if (!LehrerEinsatzstatus.data().isGueltig(this._idEinsatzstatus.get(), this.kontext().getSchuljahr())) {
			this.addFehler(0, "Lehrer Einsatzstatus: Der eingetragene Wert für das Feld 'Einsatzstatus' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppe02LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLppe02LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLppe02LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus>('de.svws_nrw.asd.validate.lehrer.ValidatorLppe02LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppe02LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus(obj: unknown): ValidatorLppe02LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus {
	return obj as ValidatorLppe02LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus;
}
