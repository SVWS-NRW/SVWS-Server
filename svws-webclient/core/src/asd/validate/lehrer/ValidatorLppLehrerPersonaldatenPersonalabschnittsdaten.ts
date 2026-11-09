import { LehrerLehramtEintrag } from '../../../asd/data/lehrer/LehrerLehramtEintrag';
import { ValidatorLpppLehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll } from '../../../asd/validate/lehrer/ValidatorLpppLehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll';
import { ValidatorLppbbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsartBlockmodell } from '../../../asd/validate/lehrer/ValidatorLppbbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsartBlockmodell';
import { ArrayList } from '../../../java/util/ArrayList';
import { LehrerPersonalabschnittsdatenAnrechnungsstunden } from '../../../asd/data/lehrer/LehrerPersonalabschnittsdatenAnrechnungsstunden';
import { ValidatorLpprbLehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart } from '../../../asd/validate/lehrer/ValidatorLpprbLehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart';
import { ValidatorLppaLehrerPersonaldatenPersonalabschnittsdatenAnrechnungen } from '../../../asd/validate/lehrer/ValidatorLppaLehrerPersonaldatenPersonalabschnittsdatenAnrechnungen';
import { ValidatorLppbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart } from '../../../asd/validate/lehrer/ValidatorLppbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart';
import { ValidatorLpprLehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis } from '../../../asd/validate/lehrer/ValidatorLpprLehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis';
import { ValidatorLppeLehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus } from '../../../asd/validate/lehrer/ValidatorLppeLehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus';
import type { List } from '../../../java/util/List';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten extends Validator {

	/**
	 * Eine Liste von Einzel-Validatoren, die von diesem Sammel-Validator verwaltet werden.
	 */
	private readonly validatoren: List<Validator> = new ArrayList<Validator>();


	/**
	 * Erstellt einen neuen Sammel-Validator für Personalabschnittsdaten.
	 *
	 * @param idSchuljahresabschnitt  die ID des Schuljahresabschnittes
	 * @param idStaatsangehoerigkeit  die idStaatsangehoerigkeit des Lehrers
	 * @param idRechtsverhaeltnis     das Rechtsverhältnis
	 * @param pflichtstundensoll      das Pflichtstundensoll
	 * @param anrechnungen            die Liste der Anrechnungsstunden
	 * @param idEinsatzstatus         der Einsatz-Status
	 * @param idBeschaeftigungsart    die Beschäftigungsart
	 * @param geburtsdatum            das Geburtsdatum des Lehrers
	 * @param lehraemter              die Liste der Lehrämter der Lehrkraft
	 * @param mehrleistungen          die Liste der Mehrleistungen
	 * @param minderleistungen        die Liste der Minderleistungen
	 * @param kontext                 der Kontext des Validators
	 */
	public constructor(idSchuljahresabschnitt: Supplier<number>, idStaatsangehoerigkeit: Supplier<number | null>, idRechtsverhaeltnis: Supplier<number | null>, pflichtstundensoll: Supplier<number | null>, anrechnungen: Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>>, idEinsatzstatus: Supplier<number | null>, idBeschaeftigungsart: Supplier<number | null>, geburtsdatum: Supplier<string | null>, lehraemter: Supplier<List<LehrerLehramtEintrag>>, mehrleistungen: Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>>, minderleistungen: Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>>, kontext: ValidatorKontext) {
		super(kontext);
		this.validatoren.add(new ValidatorLppaLehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(idSchuljahresabschnitt, anrechnungen, lehraemter, pflichtstundensoll, kontext));
		this.validatoren.add(new ValidatorLppbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(idSchuljahresabschnitt, idBeschaeftigungsart, this.getNotNullSupplierLong(idEinsatzstatus), pflichtstundensoll, kontext));
		this.validatoren.add(new ValidatorLppbbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsartBlockmodell(idBeschaeftigungsart, pflichtstundensoll, idEinsatzstatus, mehrleistungen, minderleistungen, kontext));
		this.validatoren.add(new ValidatorLppeLehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus(idSchuljahresabschnitt, idEinsatzstatus, kontext));
		this.validatoren.add(new ValidatorLpppLehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll(pflichtstundensoll, idEinsatzstatus, idBeschaeftigungsart, kontext));
		this._validatoren.add(new ValidatorLpprLehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(idSchuljahresabschnitt, idStaatsangehoerigkeit, idRechtsverhaeltnis, geburtsdatum, this.kontext()));
		this._validatoren.add(new ValidatorLpprbLehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart(idRechtsverhaeltnis, idBeschaeftigungsart, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten>('de.svws_nrw.asd.validate.lehrer.ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten(obj: unknown): ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten {
	return obj as ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten;
}
