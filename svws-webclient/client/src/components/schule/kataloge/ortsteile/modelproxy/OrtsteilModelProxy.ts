import type { OrtsteileListeManager } from "@ui";
import { ModelProxy, ValidatorInputRequired, ValidatorNumberRange, ValidatorStringIsUniqueInList, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";
import type { OrtKatalogEintrag, OrtsteilKatalogEintrag } from "@core";
import { computed } from "vue";
import { StringPattern } from "../../../../../../../ui/src/validation/common/ValidatorStringMatchesPattern";


/**
 * ModelProxy für Einwilligungsarten
 */
export class OrtsteilModelProxy extends ModelProxy<OrtsteilKatalogEintrag> {

	private readonly orteById: Map<number, OrtKatalogEintrag>;
	private readonly manager: () => OrtsteileListeManager;

	/**
	 * ModelProxy für Einwilligungsarten
	 *
	 * @param data 			Lambda für den Zugriff auf die Original-Daten
	 * @param manager		Lambda für den Zugriff auf den Manager
	 * @param orteById		Katalog OrteById
	 * @param patch 		Methode zum Patchen einzelner Attribute
	 */
	constructor(
		data: () => OrtsteilKatalogEintrag,
		manager: () => OrtsteileListeManager,
		orteById: Map<number, OrtKatalogEintrag>,
		patch?: (data: Partial<OrtsteilKatalogEintrag>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof OrtsteilKatalogEintrag> = ["ort_id"];
		super({ data, patch, listOfAutopatchProps, checkValidBeforePatch: true });
		this.manager = manager;
		this.orteById = orteById;
		this.addValidatoren(() => manager().liste.list());
		this.validate();
	}

	private addValidatoren(ortsteile: () => Iterable<OrtsteilKatalogEintrag>) {
		this.addValidator(new ValidatorStringLength(() => this.proxy.ortsteil, null, 30), 'ortsteil');
		this.addValidator(new ValidatorInputRequired(() => this.proxy.ortsteil), 'ortsteil');
		this.addValidator(new ValidatorStringIsUniqueInList(() => this.proxy, (data: OrtsteilKatalogEintrag) => data.id, (data: OrtsteilKatalogEintrag) => data.ortsteil, ortsteile, false), 'ortsteil');
		this.addValidator(new ValidatorStringMatchesPattern(() => this.proxy.ortsteil, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), 'ortsteil');
		// sortierung
		this.addValidator(new ValidatorNumberRange(() => this.proxy.sortierung, 0, 32000), "sortierung");
	}

	ort = computed<OrtKatalogEintrag | null>({
		get: () => this.orteById.get(this.proxy.ort_id ?? -1) ?? null,
		set: (v: OrtKatalogEintrag | null) => {
			const ort = this.orteById.get(v?.id ?? -1) ?? null;
			if (ort !== null) {
				this.proxy.ort_id = ort.id;
				this.proxy.bezeichnungOrt = ort.ortsname;
				this.proxy.plzOrt = ort.plz;
				// notwendig, damit plz und ortsname nach patchen in der Auswahlliste angezeigt werden
				this.manager().daten().ort_id = ort.id;
				this.manager().daten().bezeichnungOrt = ort.ortsname;
				this.manager().daten().plzOrt = ort.plz;
			}
		},
	});

}
