import { JavaObject } from '../../../java/lang/JavaObject';
import { LehrerPersonaldaten } from '../../../asd/data/lehrer/LehrerPersonaldaten';
import { Schulform } from '../../../asd/types/schule/Schulform';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorGlpl01GesamtLehrerPersonaldatenLehramt extends Validator {

	/**
	 * Die Lehrer-Personalabschnittsdaten
	 */
	private readonly listPersonaldaten: List<LehrerPersonaldaten>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param listPersonaldaten   die Liste der Lehrer-Personaldaten, die geprüft werden sollen
	 * @param kontext             der Kontext des Validators
	 */
	public constructor(listPersonaldaten: List<LehrerPersonaldaten>, kontext: ValidatorKontext) {
		super(kontext);
		this.listPersonaldaten = listPersonaldaten;
	}

	protected pruefe(): boolean {
		let success: boolean = true;
		const schulform: Schulform = this.kontext().getSchulform();
		const istFW: boolean = JavaObject.equalsTranspiler(Schulform.FW, (schulform));
		for (const lp of this.listPersonaldaten) {
			const anzahlLehraemter: number = lp.lehraemter.size();
			if (istFW && anzahlLehraemter > 0) {
				this.addFehler(1, "Bei Freien Waldorfschulen darf kein Lehramt erfasst sein. Lehrer ID: " + lp.id);
				success = false;
			}
		}
		return success;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.gesamt.ValidatorGlpl01GesamtLehrerPersonaldatenLehramt';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.gesamt.ValidatorGlpl01GesamtLehrerPersonaldatenLehramt', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorGlpl01GesamtLehrerPersonaldatenLehramt>('de.svws_nrw.asd.validate.gesamt.ValidatorGlpl01GesamtLehrerPersonaldatenLehramt');

}

export function cast_de_svws_nrw_asd_validate_gesamt_ValidatorGlpl01GesamtLehrerPersonaldatenLehramt(obj: unknown): ValidatorGlpl01GesamtLehrerPersonaldatenLehramt {
	return obj as ValidatorGlpl01GesamtLehrerPersonaldatenLehramt;
}
