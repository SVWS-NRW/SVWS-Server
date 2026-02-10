import { JavaObject } from '../../../java/lang/JavaObject';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppb11LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart extends Validator {

	/**
	 * Die Beschäftigungsart
	 */
	private readonly beschaeftigungsart: Supplier<string>;

	/**
	 * Der Einsatzstatus
	 */
	private readonly einsatzstatus: Supplier<string>;

	/**
	 * Das Pflichtstundensoll
	 */
	private readonly pflichtstundensoll: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param beschaeftigungsart     	die Beschäftigungsart
	 * @param einsatzstatus     		der Einsatzstatus
	 * @param pflichtstundensoll     	das Pflichtstundensoll
	 * @param kontext   				der Kontext des Validators
	 */
	public constructor(beschaeftigungsart: Supplier<string>, einsatzstatus: Supplier<string>, pflichtstundensoll: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this.beschaeftigungsart = beschaeftigungsart;
		this.einsatzstatus = einsatzstatus;
		this.pflichtstundensoll = pflichtstundensoll;
	}

	protected pruefe(): boolean {
		const beschaeftigungsart: string | null = this.beschaeftigungsart.get();
		const einsatzstatus: string | null = this.einsatzstatus.get();
		const pflichtstundensoll: number | null = this.pflichtstundensoll.get();
		if (pflichtstundensoll === null)
			return true;
		const fehlertext3: string | null = "Laut Ihren Angaben handelt es sich um eine voll abgeordnete Lehrkraft mit Gestellungsvertrag. Es ist zu erwarten, dass eine Lehrkraft mit Gestellungsvertrag Unterricht an Ihrer Schule erteilt. Bitte überprüfen Sie Ihre Angaben.";
		if (JavaObject.equalsTranspiler("G", (beschaeftigungsart)) && JavaObject.equalsTranspiler("A", (einsatzstatus)) && pflichtstundensoll === 0) {
			this.addFehler(3, fehlertext3);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppb11LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLppb11LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLppb11LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart>('de.svws_nrw.asd.validate.lehrer.ValidatorLppb11LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppb11LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(obj: unknown): ValidatorLppb11LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart {
	return obj as ValidatorLppb11LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart;
}
