import { JavaObject } from '../../../java/lang/JavaObject';
import { Schulform } from '../../../asd/types/schule/Schulform';
import { LehrerStatistikGesamt } from '../../../asd/data/statistik/LehrerStatistikGesamt';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorGlpl00GesamtLehrerPersonaldatenLehramt extends Validator {

	/**
	 * Die Lehrer-Personalabschnittsdaten
	 */
	private readonly listLehrer: Supplier<List<LehrerStatistikGesamt>>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param listLehrer          die Liste der Lehrer-Personaldaten, die geprüft werden sollen
	 * @param kontext             der Kontext des Validators
	 */
	public constructor(listLehrer: Supplier<List<LehrerStatistikGesamt>>, kontext: ValidatorKontext) {
		super(kontext);
		this.listLehrer = listLehrer;
	}

	protected pruefe(): boolean {
		let success: boolean = true;
		const list: List<LehrerStatistikGesamt> = this.listLehrer.get();
		const schulform: Schulform = this.kontext().getSchulform();
		const istFW: boolean = JavaObject.equalsTranspiler(Schulform.FW, (schulform));
		for (const ls of list) {
			const anzahlLehraemter: number = ls.lehraemter.size();
			if (!istFW && anzahlLehraemter === 0) {
				this.addFehler(0, "Zu Jeder Lehrkraft muss mindest ein Lehramt vorliegen. Lehrer ID: " + ls.id);
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

	public static readonly class = new Class<ValidatorGlpl00GesamtLehrerPersonaldatenLehramt>('de.svws_nrw.asd.validate.gesamt.ValidatorGlpl00GesamtLehrerPersonaldatenLehramt');

}

export function cast_de_svws_nrw_asd_validate_gesamt_ValidatorGlpl00GesamtLehrerPersonaldatenLehramt(obj: unknown): ValidatorGlpl00GesamtLehrerPersonaldatenLehramt {
	return obj as ValidatorGlpl00GesamtLehrerPersonaldatenLehramt;
}
