import { JavaObject } from '../../../java/lang/JavaObject';
import { LehrerLehramtEintrag } from '../../../asd/data/lehrer/LehrerLehramtEintrag';
import { Schulform } from '../../../asd/types/schule/Schulform';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLp00LehrerPersonaldatenLehramt extends Validator {

	/**
	 * Die Lehrämter
	 */
	private readonly lehraemter: Supplier<List<LehrerLehramtEintrag>>;

	/**
	 * Die LehrerId
	 */
	private readonly lehrerId: Supplier<number>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param lehraemter   			die Lehrämter, die geprüft werden sollen
	 * @param lehrerId   			die LehrerId
	 * @param kontext               der Kontext des Validators
	 */
	public constructor(lehraemter: Supplier<List<LehrerLehramtEintrag>>, lehrerId: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this.lehraemter = lehraemter;
		this.lehrerId = lehrerId;
	}

	protected pruefe(): boolean {
		const schulform: Schulform = this.kontext().getSchulform();
		const istFW: boolean = JavaObject.equalsTranspiler(Schulform.FW, (schulform));
		const anzahlLehraemter: number = this.lehraemter.get().size();
		if (!istFW && anzahlLehraemter === 0) {
			this.addFehler(0, "Zu jeder Lehrkraft muss mindest ein Lehramt vorliegen. Lehrer ID: " + this.lehrerId.get());
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLp00LehrerPersonaldatenLehramt';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLp00LehrerPersonaldatenLehramt', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLp00LehrerPersonaldatenLehramt>('de.svws_nrw.asd.validate.lehrer.ValidatorLp00LehrerPersonaldatenLehramt');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLp00LehrerPersonaldatenLehramt(obj: unknown): ValidatorLp00LehrerPersonaldatenLehramt {
	return obj as ValidatorLp00LehrerPersonaldatenLehramt;
}
