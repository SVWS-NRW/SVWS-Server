import type { OrtsteileListeManager } from "@ui";
import { StringPattern, ModelProxy, ValidatorInputRequired, ValidatorNumberRange, ValidatorStringIsUniqueInList, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";
import type { OrtKatalogEintrag, OrtsteilKatalogEintrag } from "@core";
import { ArrayList } from "@core";
import { computed } from "vue";


/**
 * ModelProxy für OrtsteilKatalogEintrag
 */
export class OrtsteilModelProxy extends ModelProxy<OrtsteilKatalogEintrag> {

	private readonly manager: () => OrtsteileListeManager;

	/**
	 * ModelProxy für OrtsteilKatalogEintrag
	 *
	 * @param data 			Lambda für den Zugriff auf die Original-Daten
	 * @param manager		Lambda für den Zugriff auf den Manager
	 * @param patch 		Methode zum Patchen einzelner Attribute
	 */
	constructor(
		data: () => OrtsteilKatalogEintrag,
		manager: () => OrtsteileListeManager,
		patch?: (data: Partial<OrtsteilKatalogEintrag>) => Promise<boolean>
	) {
		super({ data, patch });
		this.manager = manager;
		this.addValidatoren(() => manager().liste.list());
		this.validate();
	}

	private addValidatoren(ortsteile: () => Iterable<OrtsteilKatalogEintrag>) {
		// Ortsteil
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.ortsteil, null, 30), 'ortsteil');
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.ortsteil), 'ortsteil');
		this.addBlockingValidator(
			new ValidatorStringIsUniqueInList(
				() => this.proxy,
				(ortsteil: OrtsteilKatalogEintrag) => ortsteil.id,
				(ortsteil: OrtsteilKatalogEintrag) => ortsteil.ortsteil,
				() => [...ortsteile()].filter(e => e.idOrt === this.proxy.idOrt),
				false
			),
			'ortsteil', 'idOrt'
		);
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.ortsteil, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), 'ortsteil');
		// Ort
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.idOrt), 'idOrt');
		// sortierung
		this.addBlockingValidator(new ValidatorInputRequired((): number => this.proxy.sortierung), "sortierung");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.sortierung, 0, 32000), "sortierung");
	}

	filteredOrte = computed<Iterable<OrtKatalogEintrag>>(() => {
		const currentOrtsteil = this.proxy.ortsteil?.trim().toLowerCase() ?? null;
		if (currentOrtsteil === null) {
			return this.manager().orteById.values();
		}

		const filteredOrte = new ArrayList<OrtKatalogEintrag>();
		for (const katalogOrt of this.manager().orteById.values()) {
			const ortsteilWithOrtIsUnique = ![...this.manager().liste.list()].some(e =>
				(e.idOrt === katalogOrt.id)
					&& (e.id !== this.proxy.id)
					&& (e.ortsteil?.trim().toLowerCase() === currentOrtsteil));
			if (ortsteilWithOrtIsUnique) {
				filteredOrte.add(katalogOrt);
			}
		}

		return filteredOrte;
	});

	ort = computed<OrtKatalogEintrag | null>({
		get: () => this.manager().orteById.get(this.proxy.idOrt ?? -1) ?? null,
		set: (ort: OrtKatalogEintrag | null | undefined) => void this.updatePendingOrt(ort),
	});

	private async updatePendingOrt(ort: OrtKatalogEintrag | null | undefined): Promise<void> {
		if (ort === null || ort === undefined) {
			return;
		}

		this.proxy.idOrt = ort.id;
		this.proxy.bezeichnungOrt = ort.ortsname;
		this.proxy.plzOrt = ort.plz;

		await this.patch();
	}


}
