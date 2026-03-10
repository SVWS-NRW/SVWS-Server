import { JavaObject } from '../../../java/lang/JavaObject';
import type { JavaSet } from '../../../java/util/JavaSet';
import { java_util_Set_of } from '../../../java/util/JavaSet';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart extends Validator {

	/**
	 * Die Beschäftigungsart
	 */
	private readonly beschaeftigungsart: Supplier<string>;

	/**
	 * Der Einsatzstatus
	 */
	private readonly einsatzstatus: Supplier<string>;

	private static readonly setEinsatzstatus2: JavaSet<string> = java_util_Set_of("A", "B");

	private static readonly FEHLERTEXT: string = "Bei einer unentgeltlich beschäftigten Lehrkraft (Feld 'Beschäftigungsart' = 'Unentgeltlich Beschäftigte') dürfen im Feld 'Einsatzstatus' nicht die Einträge 'Stammschule, ganz oder teilweise auch an anderen Schulen tätig' oder 'nicht Stammschule, aber auch hier tätig' eingetragen sein.";


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param beschaeftigungsart     die Beschäftigungsart
	 * @param einsatzstatus     der Einsatzstatus
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(beschaeftigungsart: Supplier<string>, einsatzstatus: Supplier<string>, kontext: ValidatorKontext) {
		super(kontext);
		this.beschaeftigungsart = beschaeftigungsart;
		this.einsatzstatus = einsatzstatus;
	}

	protected pruefe(): boolean {
		const beschaeftigungsart: string | null = this.beschaeftigungsart.get();
		const einsatzstatus: string | null = this.einsatzstatus.get();
		if (ValidatorLppb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart.setEinsatzstatus2.contains(einsatzstatus) && JavaObject.equalsTranspiler("X", (beschaeftigungsart))) {
			this.addFehler(2, ValidatorLppb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart.FEHLERTEXT);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLppb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLppb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart>('de.svws_nrw.asd.validate.lehrer.ValidatorLppb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(obj: unknown): ValidatorLppb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart {
	return obj as ValidatorLppb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart;
}
