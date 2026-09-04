import type { BilingualeSpracheKatalogEintrag } from "@core/asd/data/fach/BilingualeSpracheKatalogEintrag";
import type { FachKatalogEintrag } from "@core/asd/data/fach/FachKatalogEintrag";
import { BilingualeSprache } from "@core/asd/types/fach/BilingualeSprache";
import { Fach } from "@core/asd/types/fach/Fach";
import type { FachDaten } from "@core/core/data/fach/FachDaten";
import { JavaInteger } from "@core/java/lang/JavaInteger";
import { ModelProxy } from "@ui/model/ModelProxy";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorNumberRange } from "@ui/validation/common/ValidatorNumberRange";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { ValidatorStringMatchesPattern, StringPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";
import { computed } from "vue";
import { ValidatorFachBezeichnung } from "./validation/ValidatorFachBezeichnung";
import { ValidatorFachKuerzel } from "./validation/ValidatorFachKuerzel";

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
		super({ data, patch, listOfAutopatchProps });
		this.schuljahr = schuljahr;

		this.addValidatoren(alleFaecher);
		this.validate();
	}

	private addValidatoren(liste: () => Iterable<FachDaten>) {
		this.addBlockingValidator(new ValidatorFachKuerzel(() => this.proxy, liste), 'kuerzel');
		this.addBlockingValidator(new ValidatorFachBezeichnung(() => this.proxy, liste), 'bezeichnung');
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.kuerzelStatistik), 'kuerzelStatistik');
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.bezeichnungZeugnis, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), 'bezeichnungZeugnis');
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.bezeichnungZeugnis, null, 255), 'bezeichnungZeugnis');
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.bezeichnungUeberweisungszeugnis, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), 'bezeichnungUeberweisungszeugnis');
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.bezeichnungUeberweisungszeugnis, null, 255), 'bezeichnungUeberweisungszeugnis');
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.maxZeichenInFachbemerkungen, 0, JavaInteger.MAX_VALUE), 'maxZeichenInFachbemerkungen');
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.sortierung), 'sortierung');
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.sortierung, 0, 32000), 'sortierung');
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
