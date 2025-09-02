import { LehrerStammdaten } from '../../../asd/data/lehrer/LehrerStammdaten';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLehrerStammdatenNachnameOhneLeerzeichenVorNachBindestrich extends Validator {

	/**
	 * Die Lehrer-Stammdaten
	 */
	private readonly daten : LehrerStammdaten;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten : LehrerStammdaten, kontext : ValidatorKontext) {
		super(kontext);
		this.daten = daten;
	}

	protected pruefe() : boolean {
		const nachname : string | null = this.daten.nachname;
		let success : boolean = true;
		if (JavaString.contains(nachname, " -") || JavaString.contains(nachname, "- ")) {
			this.addFehler("Bitte entfernen Sie beim Nachnamen überflüssige Leerzeichen vor und/oder nach dem Bindestrich.(AA7)");
			success = false;
		}
		return success;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLehrerStammdatenNachnameOhneLeerzeichenVorNachBindestrich';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLehrerStammdatenNachnameOhneLeerzeichenVorNachBindestrich', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static class = new Class<ValidatorLehrerStammdatenNachnameOhneLeerzeichenVorNachBindestrich>('de.svws_nrw.asd.validate.lehrer.ValidatorLehrerStammdatenNachnameOhneLeerzeichenVorNachBindestrich');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLehrerStammdatenNachnameOhneLeerzeichenVorNachBindestrich(obj : unknown) : ValidatorLehrerStammdatenNachnameOhneLeerzeichenVorNachBindestrich {
	return obj as ValidatorLehrerStammdatenNachnameOhneLeerzeichenVorNachBindestrich;
}
