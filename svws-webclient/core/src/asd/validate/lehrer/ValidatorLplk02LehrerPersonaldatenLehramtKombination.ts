import { LehrerPersonaldaten } from '../../../asd/data/lehrer/LehrerPersonaldaten';
import { LehrerLehramt } from '../../../asd/types/lehrer/LehrerLehramt';
import { LehrerLehramtKatalogEintrag } from '../../../asd/data/lehrer/LehrerLehramtKatalogEintrag';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLplk02LehrerPersonaldatenLehramtKombination extends Validator {

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
		let lehramtId56Vorhanden: boolean = false;
		let lehramtId57Vorhanden: boolean = false;
		let lehramtId58Vorhanden: boolean = false;
		let lehramtId60Vorhanden: boolean = false;
		let lehramtId61Vorhanden: boolean = false;
		let anderesLehramtVorhanden: boolean = false;
		let lehrerLehramtKatalogEintrag56: LehrerLehramtKatalogEintrag | null = LehrerLehramt.ID_56.daten(this.kontext().getSchuljahr());
		let lehrerLehramtKatalogEintrag57: LehrerLehramtKatalogEintrag | null = LehrerLehramt.ID_57.daten(this.kontext().getSchuljahr());
		let lehrerLehramtKatalogEintrag58: LehrerLehramtKatalogEintrag | null = LehrerLehramt.ID_58.daten(this.kontext().getSchuljahr());
		let lehrerLehramtKatalogEintrag60: LehrerLehramtKatalogEintrag | null = LehrerLehramt.ID_60.daten(this.kontext().getSchuljahr());
		let lehrerLehramtKatalogEintrag61: LehrerLehramtKatalogEintrag | null = LehrerLehramt.ID_61.daten(this.kontext().getSchuljahr());
		if (lehrerLehramtKatalogEintrag56 !== null && lehrerLehramtKatalogEintrag57 !== null && lehrerLehramtKatalogEintrag58 !== null && lehrerLehramtKatalogEintrag60 !== null && lehrerLehramtKatalogEintrag61 !== null) {
			for (const lehrerLehramtEintrag of this.lehrerPersonaldaten.lehraemter) {
				if (lehrerLehramtKatalogEintrag56.id === LehrerLehramt.data().getEintragByIDOrException(lehrerLehramtEintrag.idKatalogLehramt).id)
					lehramtId56Vorhanden = true;
				else
					if (lehrerLehramtKatalogEintrag57.id === LehrerLehramt.data().getEintragByIDOrException(lehrerLehramtEintrag.idKatalogLehramt).id)
						lehramtId57Vorhanden = true;
					else
						if (lehrerLehramtKatalogEintrag58.id === LehrerLehramt.data().getEintragByIDOrException(lehrerLehramtEintrag.idKatalogLehramt).id)
							lehramtId58Vorhanden = true;
						else
							if (lehrerLehramtKatalogEintrag60.id === LehrerLehramt.data().getEintragByIDOrException(lehrerLehramtEintrag.idKatalogLehramt).id)
								lehramtId60Vorhanden = true;
							else
								if (lehrerLehramtKatalogEintrag61.id === LehrerLehramt.data().getEintragByIDOrException(lehrerLehramtEintrag.idKatalogLehramt).id)
									lehramtId61Vorhanden = true;
								else
									anderesLehramtVorhanden = true;
			}
		}
		if ((lehramtId56Vorhanden || lehramtId57Vorhanden || lehramtId58Vorhanden || lehramtId60Vorhanden || lehramtId61Vorhanden) && anderesLehramtVorhanden) {
			this.addFehler(2, "Die Lehramtseinträge 'Schulkindergärtner/-in', 'Sozialarbeiter/-in, Sozialpädagoge/-in, Diplom-Pädagoge/-in (ohne sonderpädagogische Zusatzausbildung)', 'Erzieher/-in (ohne sonderpädagogische Zusatzausbildung)', 'Sozialarbeiter/-in, Sozialpädagoge/-in, Diplom-Pädagoge/-in (mit sonderpädagogische Zusatzausbildung)' und 'Erzieher/-in (mit sonderpädagogischer Zusatzausbildung)' sollten nicht in Kombination mit anderen als diesen Lehramtseinträgen vorliegen. Bitte korrigieren Sie Ihre Angaben.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLplk02LehrerPersonaldatenLehramtKombination';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLplk02LehrerPersonaldatenLehramtKombination', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLplk02LehrerPersonaldatenLehramtKombination>('de.svws_nrw.asd.validate.lehrer.ValidatorLplk02LehrerPersonaldatenLehramtKombination');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLplk02LehrerPersonaldatenLehramtKombination(obj: unknown): ValidatorLplk02LehrerPersonaldatenLehramtKombination {
	return obj as ValidatorLplk02LehrerPersonaldatenLehramtKombination;
}
