import { JavaObject } from '../../../java/lang/JavaObject';
import { LehrerPersonaldaten } from '../../../asd/data/lehrer/LehrerPersonaldaten';
import { Schulform } from '../../../asd/types/schule/Schulform';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorGlpl00GesamtLehrerPersonaldatenLehramt extends Validator {

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
			if (!istFW && anzahlLehraemter === 0) {
				this.addFehler(0, "Zu Jeder Lehrkraft muss mindest ein Lehramt vorliegen. Lehrer ID: " + lp.id);
				success = false;
			}
		}
		return success;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.gesamt.ValidatorGlpl00GesamtLehrerPersonaldatenLehramt';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.gesamt.ValidatorGlpl00GesamtLehrerPersonaldatenLehramt', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static class = new Class<ValidatorGlpl00GesamtLehrerPersonaldatenLehramt>('de.svws_nrw.asd.validate.gesamt.ValidatorGlpl00GesamtLehrerPersonaldatenLehramt');

}

export function cast_de_svws_nrw_asd_validate_gesamt_ValidatorGlpl00GesamtLehrerPersonaldatenLehramt(obj: unknown): ValidatorGlpl00GesamtLehrerPersonaldatenLehramt {
	return obj as ValidatorGlpl00GesamtLehrerPersonaldatenLehramt;
}
