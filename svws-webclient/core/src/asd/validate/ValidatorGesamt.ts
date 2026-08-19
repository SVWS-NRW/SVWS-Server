import { ValidatorSsSchuelerStammdaten } from '../../asd/validate/schueler/ValidatorSsSchuelerStammdaten';
import { ArrayList } from '../../java/util/ArrayList';
import { ValidatorLpLehrerPersonaldaten } from '../../asd/validate/lehrer/ValidatorLpLehrerPersonaldaten';
import { ValidatorSlSchuelerLernabschnittsdaten } from '../../asd/validate/schueler/ValidatorSlSchuelerLernabschnittsdaten';
import { ValidatorKoKlassenOrganisationsform } from '../../asd/validate/klassen/ValidatorKoKlassenOrganisationsform';
import { ValidatorLplaLehrerPersonaldatenLehramtLehrbefaehigung } from '../../asd/validate/lehrer/ValidatorLplaLehrerPersonaldatenLehramtLehrbefaehigung';
import { ValidatorGlGesamtLehrerdaten } from '../../asd/validate/gesamt/ValidatorGlGesamtLehrerdaten';
import { ValidatorKkKlassenKlassenart } from '../../asd/validate/klassen/ValidatorKkKlassenKlassenart';
import { ValidatorGsGesamtSchuelerdaten } from '../../asd/validate/gesamt/ValidatorGsGesamtSchuelerdaten';
import { LehrerLehramt } from '../../asd/types/lehrer/LehrerLehramt';
import { ValidatorSssSchuleStammdatenSchulform } from '../../asd/validate/schule/ValidatorSssSchuleStammdatenSchulform';
import type { List } from '../../java/util/List';
import type { Supplier } from '../../java/util/function/Supplier';
import { Class } from '../../java/lang/Class';
import { StatistikGesamt } from '../../asd/data/statistik/StatistikGesamt';
import { ValidatorKontext } from '../../asd/validate/ValidatorKontext';
import { Validator, cast_de_svws_nrw_asd_validate_Validator } from '../../asd/validate/Validator';
import { ValidatorLsLehrerStammdaten } from '../../asd/validate/lehrer/ValidatorLsLehrerStammdaten';

export class ValidatorGesamt extends Validator {

	/**
	 * Eine Liste von Validatoren, die bei diesem Validator mitgeprüft werden.
	 */
	protected readonly validatoren: List<Validator> = new ArrayList<Validator>();

	/**
	 * Die Daten des Validators
	 */
	protected readonly daten: Supplier<StatistikGesamt>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten: Supplier<StatistikGesamt>, kontext: ValidatorKontext) {
		super(kontext);
		this.daten = daten;
		this.validatoren.add(new ValidatorSssSchuleStammdatenSchulform({ get: () => daten.get().schule.schulform }, kontext));
		this.validatoren.add(new ValidatorGlGesamtLehrerdaten({ get: () => daten.get().lehrer }, kontext));
		this.validatoren.add(new ValidatorGsGesamtSchuelerdaten({ get: () => daten.get().schueler }, kontext));
	}

	protected pruefe(): boolean {
		this._validatoren.clear();
		this._validatoren.addAll(this.validatoren);
		const gesamt: StatistikGesamt = this.daten.get();
		for (const lehrer of gesamt.lehrer) {
			this._validatoren.add(new ValidatorLsLehrerStammdaten({ get: () => lehrer.nachname }, { get: () => lehrer.vorname }, { get: () => lehrer.geburtsdatum }, { get: () => lehrer.geschlecht }, { get: () => lehrer.kuerzel }, { get: () => lehrer.idStaatsangehoerigkeit }, { get: () => lehrer.idRechtsverhaeltnis }, this.kontext()));
			this._validatoren.add(new ValidatorLpLehrerPersonaldaten({ get: () => lehrer.id }, { get: () => gesamt.schule.idSchuljahresabschnitt }, { get: () => lehrer.idRechtsverhaeltnis }, { get: () => lehrer.pflichtstundensoll }, { get: () => lehrer.anrechnungen }, { get: () => lehrer.idEinsatzstatus }, { get: () => lehrer.idBeschaeftigungsart }, { get: () => lehrer.geburtsdatum }, { get: () => lehrer.lehraemter }, { get: () => lehrer.mehrleistung }, { get: () => lehrer.minderleistung }, this.kontext()));
			for (const lehraemter of lehrer.lehraemter) {
				for (const lehrbefaehigungen of lehraemter.lehrbefaehigungen) {
					this._validatoren.add(new ValidatorLplaLehrerPersonaldatenLehramtLehrbefaehigung({ get: () => lehrbefaehigungen.idLehrbefaehigung }, { get: () => LehrerLehramt.data().getWertByIDOrNull(lehraemter.idKatalogLehramt) }, this.kontext()));
				}
			}
		}
		for (const schueler of gesamt.schueler) {
			this._validatoren.add(new ValidatorSsSchuelerStammdaten({ get: () => schueler.geschlecht }, { get: () => schueler.geburtsdatum }, { get: () => schueler.idGeburtsland }, { get: () => schueler.idGeburtslandMutter }, { get: () => schueler.idGeburtslandVater }, { get: () => schueler.hatMigrationshintergrund }, { get: () => schueler.idStaatsangehoerigkeit }, { get: () => schueler.idStaatsangehoerigkeit2 }, this.kontext()));
			for (const lernabschnitt of schueler.lernabschnitte) {
				this._validatoren.add(new ValidatorSlSchuelerLernabschnittsdaten({ get: () => lernabschnitt.idKlassenart }, { get: () => lernabschnitt.idEpJahre }, this.kontext()));
			}
		}
		for (const klassen of gesamt.klassen) {
			this._validatoren.add(new ValidatorKkKlassenKlassenart({ get: () => null }, this.kontext()));
			this._validatoren.add(new ValidatorKoKlassenOrganisationsform({ get: () => null }, { get: () => null }, { get: () => null }, this.kontext()));
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.ValidatorGesamt';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.ValidatorGesamt', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorGesamt>('de.svws_nrw.asd.validate.ValidatorGesamt');

}

export function cast_de_svws_nrw_asd_validate_ValidatorGesamt(obj: unknown): ValidatorGesamt {
	return obj as ValidatorGesamt;
}
