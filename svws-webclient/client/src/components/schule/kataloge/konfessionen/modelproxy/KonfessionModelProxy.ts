import { ModelProxy, StringPattern, ValidatorInputRequired, ValidatorNumberRange, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";
import type { CoreTypeData, ReligionEintrag } from "@core";
import { Religion } from "@core";
import { ValidatorKonfessionBezeichnung } from "~/components/schule/kataloge/konfessionen/modelproxy/validation/ValidatorKonfessionBezeichnung";
import { computed } from "vue";

export class KonfessionModelProxy extends ModelProxy<ReligionEintrag> {

	/**
	 * Modelproxy für Konfession
	 *
	 * @param data Lambda für den Zugriff auf Original-Daten
	 * @param alleKonfessionen Lambda zur Liste aller Konfessionen
	 * @param patch Methode zum Patchen einzelner Attribute
	 */
	constructor(
		data: () => ReligionEintrag,
		alleKonfessionen: () => Iterable<ReligionEintrag>,
		patch?: (data: Partial<ReligionEintrag>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof ReligionEintrag> = ["idReligion", "istSichtbar"];
		super({ data, patch, listOfAutopatchProps });
		this.addValidatoren(alleKonfessionen);
		this.validate();
	}

	private addValidatoren(liste: () => Iterable<ReligionEintrag>) {
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.idReligion), "idReligion");
		this.addBlockingValidator(new ValidatorKonfessionBezeichnung(() => this.proxy, liste), "bezeichnung");
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.bezeichnungZeugnis, null, 50), "bezeichnungZeugnis");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.bezeichnungZeugnis, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "bezeichnungZeugnis");
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.sortierung), "sortierung");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.sortierung, 0, 32000), "sortierung");
	}

	selectedKonfession = computed<CoreTypeData | null>({
		get: () => Religion.data().getEintragByID(this.proxy.idReligion ?? -1),
		set: (value: CoreTypeData | null) => this.proxy.idReligion = value?.id ?? null,
	});

}
