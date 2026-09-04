import { ValidatorFloskelgruppeKuerzel } from "~/components/schule/kataloge/floskelgruppen/modelproxy/validation/ValidatorFloskelgruppeKuerzel";
import { ValidatorFloskelgruppeBezeichnung } from "~/components/schule/kataloge/floskelgruppen/modelproxy/validation/ValidatorFloskelgruppeBezeichnung";
import { computed } from "vue";
import type { FloskelgruppenartKatalogEintrag } from "@core/asd/data/schule/FloskelgruppenartKatalogEintrag";
import { Floskelgruppenart } from "@core/asd/types/schule/Floskelgruppenart";
import type { Floskelgruppe } from "@core/core/data/schule/Floskelgruppe";
import { ModelProxy } from "@ui/model/ModelProxy";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";

/**
 * ModelProxy für Floskelgruppen
 */
export class FloskelgruppeModelProxy extends ModelProxy<Floskelgruppe> {

	private readonly schuljahr: number;
	/**
	 * ModelProxy für Floskelgruppen
	 *
	 * @param data Lambda für den Zugriff auf Original-Daten
	 * @param alleFloskelgruppen Lambda zur Liste aller Floskelgruppen
	 * @param schuljahr das Schuljahr
	 * @param patch Methode zum Patchen einzelner Attribute
	 */
	constructor(
		data: () => Floskelgruppe,
		alleFloskelgruppen: () => Iterable<Floskelgruppe>,
		schuljahr: number,
		patch?: (data: Partial<Floskelgruppe>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof Floskelgruppe> = ['idFloskelgruppenart'];
		super({ data, patch, listOfAutopatchProps });
		this.schuljahr = schuljahr;
		this.addValidatoren(alleFloskelgruppen);
		this.validate();

	}

	private addValidatoren(liste: () => Iterable<Floskelgruppe>) {
		this.addBlockingValidator(new ValidatorFloskelgruppeKuerzel(() => this.proxy, liste), "kuerzel");
		this.addBlockingValidator(new ValidatorFloskelgruppeBezeichnung(() => this.proxy, liste), "bezeichnung");
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.idFloskelgruppenart), 'idFloskelgruppenart');
	}

	selectedFloskelgruppenart = computed<FloskelgruppenartKatalogEintrag | null>({
		get: (): FloskelgruppenartKatalogEintrag | null => {
			return Floskelgruppenart.data().getWertByIDOrNull(this.proxy.idFloskelgruppenart ?? -1)?.daten(this.schuljahr) ?? null;
		},
		set: (value: FloskelgruppenartKatalogEintrag | null) => this.proxy.idFloskelgruppenart = value?.id ?? null,
	});

}
