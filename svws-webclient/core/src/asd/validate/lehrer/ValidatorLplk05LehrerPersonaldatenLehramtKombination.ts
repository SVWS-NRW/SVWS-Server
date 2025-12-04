import { LehrerPersonaldaten } from '../../../asd/data/lehrer/LehrerPersonaldaten';
import { LehrerLehramt } from '../../../asd/types/lehrer/LehrerLehramt';
import { LehrerLehramtKatalogEintrag } from '../../../asd/data/lehrer/LehrerLehramtKatalogEintrag';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLplk05LehrerPersonaldatenLehramtKombination extends Validator {

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
		let lehramtId59Vorhanden: boolean = false;
		let lehramtId62Vorhanden: boolean = false;
		let lehrerLehramtKatalogEintrag59: LehrerLehramtKatalogEintrag | null = LehrerLehramt.ID_59.daten(this.kontext().getSchuljahr());
		let lehrerLehramtKatalogEintrag62: LehrerLehramtKatalogEintrag | null = LehrerLehramt.ID_62.daten(this.kontext().getSchuljahr());
		if (lehrerLehramtKatalogEintrag59 !== null && lehrerLehramtKatalogEintrag62 !== null) {
			for (const lehrerLehramtEintrag of this.lehrerPersonaldaten.lehraemter) {
				if (lehrerLehramtKatalogEintrag59.id === LehrerLehramt.data().getEintragByIDOrException(lehrerLehramtEintrag.idKatalogLehramt).id)
					lehramtId59Vorhanden = true;
				else
					if (lehrerLehramtKatalogEintrag62.id === LehrerLehramt.data().getEintragByIDOrException(lehrerLehramtEintrag.idKatalogLehramt).id)
						lehramtId62Vorhanden = true;
			}
		}
		if (lehramtId59Vorhanden && lehramtId62Vorhanden) {
			this.addFehler(5, "Die Lehramtseinträge 'Sonstige pädagogische Unterrichtshilfe ohne sonderpädagogische Zusatzausbildung' und 'Sonstige pädagogische Unterrichtshilfe mit sonderpädagogischer Zusatzausbildung' sollten nicht zusammen vorliegen. Falls der Lehramtseintrag 'Sonstige pädagogische Unterrichtshilfe mit sonderpädagogischer Zusatzausbildung' korrekt ist, entfernen Sie bitte den Lehramtseintrag 'Sonstige pädagogische Unterrichtshilfe ohne sonderpädagogische Zusatzausbildung'.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLplk05LehrerPersonaldatenLehramtKombination';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLplk05LehrerPersonaldatenLehramtKombination', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLplk05LehrerPersonaldatenLehramtKombination>('de.svws_nrw.asd.validate.lehrer.ValidatorLplk05LehrerPersonaldatenLehramtKombination');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLplk05LehrerPersonaldatenLehramtKombination(obj: unknown): ValidatorLplk05LehrerPersonaldatenLehramtKombination {
	return obj as ValidatorLplk05LehrerPersonaldatenLehramtKombination;
}
