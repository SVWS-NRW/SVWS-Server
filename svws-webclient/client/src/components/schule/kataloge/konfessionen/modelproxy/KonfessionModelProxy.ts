import { ModelProxy, StringPattern, ValidatorInputRequired, ValidatorNumberRange, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";
import type { CoreTypeData, ReligionEintrag } from "@core";
import { Religion } from "@core";
import { ValidatorKonfessionBezeichnung } from "~/components/schule/kataloge/konfessionen/modelproxy/validation/ValidatorKonfessionBezeichnung";
import { computed } from "vue";

export class KonfessionModelProxy extends ModelProxy<ReligionEintrag> {
	private readonly schuljahr: number;

	/**
	 * Modelproxy für Konfession
	 *
	 * @param data Lambda für den Zugriff auf Original-Daten
	 * @param alleKonfessionen Lambda zur Liste aller Konfessionen
	 * @param schuljahr das Schuljahr
	 * @param patch Methode zum Patchen einzelner Attribute
	 */
	constructor(
		data: () => ReligionEintrag,
		alleKonfessionen: () => Iterable<ReligionEintrag>,
		schuljahr: number,
		patch?: (data: Partial<ReligionEintrag>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof ReligionEintrag> = ["kuerzel", "istSichtbar"];
		super({ data, patch, listOfAutopatchProps, checkValidBeforePatch: true });
		this.schuljahr = schuljahr;
		this.addValidatoren(alleKonfessionen);
		this.validate();
	}

	private addValidatoren(liste: () => Iterable<ReligionEintrag>) {
		this.addValidator(new ValidatorInputRequired(() => this.proxy.kuerzel), "kuerzel");
		this.addValidator(new ValidatorKonfessionBezeichnung(() => this.proxy, liste), "bezeichnung");
		this.addValidator(new ValidatorStringLength(() => this.proxy.bezeichnungZeugnis, null, 50), "bezeichnungZeugnis");
		this.addValidator(new ValidatorStringMatchesPattern(() => this.proxy.bezeichnungZeugnis, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "bezeichnungZeugnis");
		this.addValidator(new ValidatorNumberRange(() => this.proxy.sortierung, 0, 32000), "sortierung");
		this.addValidator(new ValidatorInputRequired(() => this.proxy.sortierung), "sortierung");
	}

	selectedKonfession = computed<CoreTypeData | null>({
		get: () => Religion.data().getEintragBySchuljahrUndSchluessel(this.schuljahr, this.proxy.kuerzel ?? ""),
		set: (value: CoreTypeData | null) => this.proxy.kuerzel = value?.schluessel ?? null,
	});

}
