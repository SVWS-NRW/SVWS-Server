import { JavaObject } from '../../../java/lang/JavaObject';
import { LehrerPersonalabschnittsdaten } from '../../../asd/data/lehrer/LehrerPersonalabschnittsdaten';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppb03LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart extends Validator {

	/**
	 * Die Lehrer-Personalabschnittsdaten
	 */
	private readonly daten: LehrerPersonalabschnittsdaten;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten: LehrerPersonalabschnittsdaten, kontext: ValidatorKontext) {
		super(kontext);
		this.daten = daten;
	}

	protected pruefe(): boolean {
		const beschaeftigungsart: string | null = this.daten.beschaeftigungsart;
		const einsatzstatus: string | null = this.daten.einsatzstatus;
		const pflichtstundensoll: number | null = this.daten.pflichtstundensoll;
		const fehlertext3: string | null = "Laut Ihren Angaben handelt es sich um eine voll abgeordnete Lehrkraft mit Gestellungsvertrag. Es ist zu erwarten, dass eine Lehrkraft mit Gestellungsvertrag Unterricht an Ihrer Schule erteilt. Bitte überprüfen Sie Ihre Angaben.";
		if (JavaObject.equalsTranspiler("G", (beschaeftigungsart)) && JavaObject.equalsTranspiler("A", (einsatzstatus)) && pflichtstundensoll === 0) {
			this.addFehler(3, fehlertext3);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppb03LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLppb03LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLppb03LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart>('de.svws_nrw.asd.validate.lehrer.ValidatorLppb03LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppb03LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(obj: unknown): ValidatorLppb03LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart {
	return obj as ValidatorLppb03LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart;
}
