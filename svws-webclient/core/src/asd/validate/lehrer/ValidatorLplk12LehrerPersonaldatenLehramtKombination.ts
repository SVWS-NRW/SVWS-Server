import { LehrerLehramtEintrag } from '../../../asd/data/lehrer/LehrerLehramtEintrag';
import { LehrerLehramt } from '../../../asd/types/lehrer/LehrerLehramt';
import { LehrerLehramtKatalogEintrag } from '../../../asd/data/lehrer/LehrerLehramtKatalogEintrag';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLplk12LehrerPersonaldatenLehramtKombination extends Validator {

	/**
	 * Die Lehrämter
	 */
	private readonly lehraemter: Supplier<List<LehrerLehramtEintrag>>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param lehraemter   			die Lehrämter, die geprüft werden sollen
	 * @param kontext               der Kontext des Validators
	 */
	public constructor(lehraemter: Supplier<List<LehrerLehramtEintrag>>, kontext: ValidatorKontext) {
		super(kontext);
		this.lehraemter = lehraemter;
	}

	protected pruefe(): boolean {
		let lehramtId56Vorhanden: boolean = false;
		let lehramtId57Vorhanden: boolean = false;
		let lehramtId58Vorhanden: boolean = false;
		let lehramtId60Vorhanden: boolean = false;
		let lehramtId61Vorhanden: boolean = false;
		let anderesLehramtVorhanden: boolean = false;
		const lehrerLehramtKatalogEintrag56: LehrerLehramtKatalogEintrag | null = LehrerLehramt.ID_56.daten(this.kontext().getSchuljahr());
		const lehrerLehramtKatalogEintrag57: LehrerLehramtKatalogEintrag | null = LehrerLehramt.ID_57.daten(this.kontext().getSchuljahr());
		const lehrerLehramtKatalogEintrag58: LehrerLehramtKatalogEintrag | null = LehrerLehramt.ID_58.daten(this.kontext().getSchuljahr());
		const lehrerLehramtKatalogEintrag60: LehrerLehramtKatalogEintrag | null = LehrerLehramt.ID_60.daten(this.kontext().getSchuljahr());
		const lehrerLehramtKatalogEintrag61: LehrerLehramtKatalogEintrag | null = LehrerLehramt.ID_61.daten(this.kontext().getSchuljahr());
		if (lehrerLehramtKatalogEintrag56 !== null && lehrerLehramtKatalogEintrag57 !== null && lehrerLehramtKatalogEintrag58 !== null && lehrerLehramtKatalogEintrag60 !== null && lehrerLehramtKatalogEintrag61 !== null)
			for (const lehrerLehramtEintrag of this.lehraemter.get())
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
		if ((lehramtId56Vorhanden || lehramtId57Vorhanden || lehramtId58Vorhanden || lehramtId60Vorhanden || lehramtId61Vorhanden) && anderesLehramtVorhanden) {
			this.addFehler(2, "Die Lehramtseinträge 'Schulkindergärtner/-in', 'Sozialarbeiter/-in, Sozialpädagoge/-in, Diplom-Pädagoge/-in (ohne sonderpädagogische Zusatzausbildung)', 'Erzieher/-in (ohne sonderpädagogische Zusatzausbildung)', 'Sozialarbeiter/-in, Sozialpädagoge/-in, Diplom-Pädagoge/-in (mit sonderpädagogische Zusatzausbildung)' und 'Erzieher/-in (mit sonderpädagogischer Zusatzausbildung)' sollten nicht in Kombination mit anderen als diesen Lehramtseinträgen vorliegen. Bitte korrigieren Sie Ihre Angaben.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLplk12LehrerPersonaldatenLehramtKombination';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLplk12LehrerPersonaldatenLehramtKombination', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLplk12LehrerPersonaldatenLehramtKombination>('de.svws_nrw.asd.validate.lehrer.ValidatorLplk12LehrerPersonaldatenLehramtKombination');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLplk12LehrerPersonaldatenLehramtKombination(obj: unknown): ValidatorLplk12LehrerPersonaldatenLehramtKombination {
	return obj as ValidatorLplk12LehrerPersonaldatenLehramtKombination;
}
