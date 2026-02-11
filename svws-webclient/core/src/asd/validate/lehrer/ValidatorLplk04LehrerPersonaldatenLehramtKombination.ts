import { LehrerPersonaldaten } from '../../../asd/data/lehrer/LehrerPersonaldaten';
import { LehrerLehramt } from '../../../asd/types/lehrer/LehrerLehramt';
import { LehrerLehramtKatalogEintrag } from '../../../asd/data/lehrer/LehrerLehramtKatalogEintrag';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLplk04LehrerPersonaldatenLehramtKombination extends Validator {

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
		let lehramtId58Vorhanden: boolean = false;
		let lehramtId61Vorhanden: boolean = false;
		let lehrerLehramtKatalogEintrag58: LehrerLehramtKatalogEintrag | null = LehrerLehramt.ID_58.daten(this.kontext().getSchuljahr());
		let lehrerLehramtKatalogEintrag61: LehrerLehramtKatalogEintrag | null = LehrerLehramt.ID_61.daten(this.kontext().getSchuljahr());
		if (lehrerLehramtKatalogEintrag58 !== null && lehrerLehramtKatalogEintrag61 !== null) {
			for (const lehrerLehramtEintrag of this.lehrerPersonaldaten.lehraemter) {
				if (lehrerLehramtKatalogEintrag58.id === LehrerLehramt.data().getEintragByIDOrException(lehrerLehramtEintrag.idKatalogLehramt).id)
					lehramtId58Vorhanden = true;
				else
					if (lehrerLehramtKatalogEintrag61.id === LehrerLehramt.data().getEintragByIDOrException(lehrerLehramtEintrag.idKatalogLehramt).id)
						lehramtId61Vorhanden = true;
			}
		}
		if (lehramtId58Vorhanden && lehramtId61Vorhanden) {
			this.addFehler(4, "Die Lehramtseinträge 'Erzieher/-in (ohne sonderpädagogische Zusatzausbildung)' und 'Erzieher/-in (mit sonderpädagogischer Zusatzausbildung)' sollten nicht zusammen vorliegen. Falls der Lehramtseintrag 'Erzieher/-in mit sonderpädagogischer Zusatzausbildung' korrekt ist, entfernen Sie bitte den Lehramtseintrag 'Erzieher/-in ohne sonderpädagogische Zusatzausbildung'.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLplk04LehrerPersonaldatenLehramtKombination';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLplk04LehrerPersonaldatenLehramtKombination', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLplk04LehrerPersonaldatenLehramtKombination>('de.svws_nrw.asd.validate.lehrer.ValidatorLplk04LehrerPersonaldatenLehramtKombination');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLplk04LehrerPersonaldatenLehramtKombination(obj: unknown): ValidatorLplk04LehrerPersonaldatenLehramtKombination {
	return obj as ValidatorLplk04LehrerPersonaldatenLehramtKombination;
}
