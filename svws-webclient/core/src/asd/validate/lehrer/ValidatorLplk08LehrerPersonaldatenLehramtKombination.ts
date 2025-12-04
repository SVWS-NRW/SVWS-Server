import { LehrerPersonaldaten } from '../../../asd/data/lehrer/LehrerPersonaldaten';
import { LehrerLehramt } from '../../../asd/types/lehrer/LehrerLehramt';
import { LehrerLehramtKatalogEintrag } from '../../../asd/data/lehrer/LehrerLehramtKatalogEintrag';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLplk08LehrerPersonaldatenLehramtKombination extends Validator {

	/**
	 * Die Lehrer-Personaldaten
	 */
	private readonly lehrerPersonaldaten: LehrerPersonaldaten;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param lehrerPersonaldaten   die Lehrer-Personaldaten, die geprüft werden sollen
	 * @param kontext               der Kontext des Validators
	 */
	public constructor(lehrerPersonaldaten: LehrerPersonaldaten, kontext: ValidatorKontext) {
		super(kontext);
		this.lehrerPersonaldaten = lehrerPersonaldaten;
	}

	protected pruefe(): boolean {
		let lehramtId63Vorhanden: boolean = false;
		let anderesLehramtVorhanden: boolean = false;
		let lehrerLehramtKatalogEintrag: LehrerLehramtKatalogEintrag | null = LehrerLehramt.ID_63.daten(this.kontext().getSchuljahr());
		if (lehrerLehramtKatalogEintrag !== null) {
			for (const lehrerLehramtEintrag of this.lehrerPersonaldaten.lehraemter) {
				if (lehrerLehramtKatalogEintrag.id === LehrerLehramt.data().getEintragByIDOrException(lehrerLehramtEintrag.idKatalogLehramt).id)
					lehramtId63Vorhanden = true;
				else
					anderesLehramtVorhanden = true;
			}
		}
		if (lehramtId63Vorhanden && anderesLehramtVorhanden) {
			this.addFehler(8, "Neben dem Lehramtseintrag 'Heilpädagogen/-innen' sollten keine weiteren Lehramtseinträge vorliegen. Bitte korrigieren Sie Ihre Angaben.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLplk08LehrerPersonaldatenLehramtKombination';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLplk08LehrerPersonaldatenLehramtKombination', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLplk08LehrerPersonaldatenLehramtKombination>('de.svws_nrw.asd.validate.lehrer.ValidatorLplk08LehrerPersonaldatenLehramtKombination');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLplk08LehrerPersonaldatenLehramtKombination(obj: unknown): ValidatorLplk08LehrerPersonaldatenLehramtKombination {
	return obj as ValidatorLplk08LehrerPersonaldatenLehramtKombination;
}
