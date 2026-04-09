import { ModelProxy, ValidatorInputRequired, ValidatorNumberRange, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";
import type { BilingualeSpracheKatalogEintrag, FachDaten, FachKatalogEintrag } from "@core";
import { BilingualeSprache, Fach, JavaInteger } from "@core";
import { ValidatorFachKuerzel } from "~/components/schule/kataloge/faecher/modelproxy/validation/ValidatorFachKuerzel";
import { ValidatorFachBezeichnung } from "~/components/schule/kataloge/faecher/modelproxy/validation/ValidatorFachBezeichnung";
import { StringPattern } from "../../../../../../../ui/src/validation/common/ValidatorStringMatchesPattern";
import { computed } from "vue";

export class FachModelProxy extends ModelProxy<FachDaten> {

	private readonly schuljahr: number;
	/**
	 * Modelproxy für Fach
	 *
	 * @param data Lambda für den Zugriff auf Original-Daten
	 * @param alleFaecher Lambda zur Liste aller Fächer
	 * @param schuljahr das Schuljahr
	 * @param patch Methode zum Patchen einzelner Attribute
	 */
	constructor(
		data: () => FachDaten,
		alleFaecher: () => Iterable<FachDaten>,
		schuljahr: number,
		patch?: (data: Partial<FachDaten>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof FachDaten> = [
			'aufZeugnis', 'istSichtbar', 'istOberstufenFach',
			'istPruefungsordnungsRelevant', 'istMoeglichAlsNeueFremdspracheInSekII', 'istFremdsprache',
			'istNachpruefungErlaubt', 'istSchriftlichZK', 'holeAusAltenLernabschnitten',
			'kuerzelStatistik', 'bilingualeSprache', 'aufgabenfeld'];
		super({ data, patch, listOfAutopatchProps, checkValidBeforePatch: true });
		this.schuljahr = schuljahr;

		this.addValidatoren(alleFaecher);
		this.validate();
	}

	private addValidatoren(liste: () => Iterable<FachDaten>) {
		this.addValidator(new ValidatorFachKuerzel(() => this.proxy, liste), 'kuerzel');
		this.addValidator(new ValidatorFachBezeichnung(() => this.proxy, liste), 'bezeichnung');
		this.addValidator(new ValidatorInputRequired(() => this.proxy.kuerzelStatistik), 'kuerzelStatistik');
		this.addValidator(new ValidatorStringMatchesPattern(() => this.proxy.bezeichnungZeugnis, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), 'bezeichnungZeugnis');
		this.addValidator(new ValidatorStringLength(() => this.proxy.bezeichnungZeugnis, null, 255), 'bezeichnungZeugnis');
		this.addValidator(new ValidatorStringMatchesPattern(() => this.proxy.bezeichnungUeberweisungszeugnis, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), 'bezeichnungUeberweisungszeugnis');
		this.addValidator(new ValidatorStringLength(() => this.proxy.bezeichnungUeberweisungszeugnis, null, 255), 'bezeichnungUeberweisungszeugnis');
		this.addValidator(new ValidatorNumberRange(() => this.proxy.maxZeichenInFachbemerkungen, 0, JavaInteger.MAX_VALUE), 'maxZeichenInFachbemerkungen');
		this.addValidator(new ValidatorInputRequired(() => this.proxy.sortierung), 'sortierung');
		this.addValidator(new ValidatorNumberRange(() => this.proxy.sortierung, 0, 32000), 'sortierung');
	}

	selectedFach = computed<FachKatalogEintrag | null>({
		get: () => Fach.data().getEintragBySchuljahrUndSchluessel(this.schuljahr, this.proxy.kuerzelStatistik),
		set: (value: FachKatalogEintrag | null) => this.proxy.kuerzelStatistik = value?.schluessel ?? '',
	});

	selectedSachfachsprache = computed<BilingualeSpracheKatalogEintrag | null>({
		get: () => BilingualeSprache.data().getEintragBySchuljahrUndSchluessel(this.schuljahr, this.proxy.bilingualeSprache ?? ''),
		set: (value: BilingualeSpracheKatalogEintrag | null) => this.proxy.bilingualeSprache = value?.schluessel ?? null,
	});

	selectedAufgabenfeld = computed<string | null>({
		get: () => this.proxy.aufgabenfeld,
		set: (value: string | null) => this.proxy.aufgabenfeld = value,
	});

}
