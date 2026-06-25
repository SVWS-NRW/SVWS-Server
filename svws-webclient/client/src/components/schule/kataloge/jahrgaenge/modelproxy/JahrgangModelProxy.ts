import type { BildungsstufeKatalogEintrag, JahrgaengeKatalogEintrag, JahrgangsDaten, SchulgliederungKatalogEintrag } from "@core";
import { Bildungsstufe, Jahrgaenge, Schulgliederung } from "@core";
import { ModelProxy } from "@ui";
import { ValidatorNumberRange } from "../../../../../../../ui/src/validation/common/ValidatorNumberRange";
import { ValidatorJahrgangBezeichnung } from "~/components/schule/kataloge/jahrgaenge/modelproxy/validation/ValidatorJahrgangBezeichnung";
import { ValidatorJahrgangKuerzel } from "~/components/schule/kataloge/jahrgaenge/modelproxy/validation/ValidatorJahrgangKuerzel";
import { ValidatorJahrgangKurzbezeichnung } from "~/components/schule/kataloge/jahrgaenge/modelproxy/validation/ValidatorJahrgangKurzbezeichnung";
import { ValidatorInputRequired } from "../../../../../../../ui/src/validation/common/ValidatorInputRequired";
import { computed } from "vue";

/**
 * ModelProxy für Jahrgangsdaten.
 */
export class JahrgangModelProxy extends ModelProxy<JahrgangsDaten> {

	private readonly schuljahr: number;
	private readonly jahrgaengeById: Map<number, JahrgangsDaten>;

	/**
	 * ModelProxy für Jahrgangsdaten.
	 *
	 * @param data Lambda für den Zugriff auf die Original-Daten
	 * @param jahrgaenge Lambda zur Liste aller Jahrgangsdaten
	 * @param schuljahr Das aktuelle Schuljahr
	 * @param patch Methode zum Patchen einzelner Attribute
	 */
	constructor(
		data: () => JahrgangsDaten,
		jahrgaenge: () => Iterable<JahrgangsDaten>,
		schuljahr: number,
		patch?: (data: Partial<JahrgangsDaten>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof JahrgangsDaten> =
			["idFolgejahrgang", "kuerzelSchulgliederung", "kuerzelStatistik", "idBildungsstufe", "istSichtbar"];
		super({ data, patch, listOfAutopatchProps });
		this.schuljahr = schuljahr;
		this.jahrgaengeById = this.mapJahrgaenge(jahrgaenge());
		this.addValidatoren(jahrgaenge);
		this.validate();
	}

	private mapJahrgaenge(jahrgaenge: Iterable<JahrgangsDaten>) {
		const result = new Map<number, JahrgangsDaten>();
		for (const jahrgang of jahrgaenge) {
			result.set(jahrgang.id, jahrgang);
		}
		return result;
	}

	private addValidatoren(jahrgaenge: () => Iterable<JahrgangsDaten>) {
		this.addBlockingValidator(new ValidatorJahrgangKuerzel(() => this.proxy, jahrgaenge), "kuerzel");
		this.addBlockingValidator(new ValidatorJahrgangBezeichnung(() => this.proxy, jahrgaenge), "bezeichnung");
		this.addBlockingValidator(new ValidatorJahrgangKurzbezeichnung(() => this.proxy, jahrgaenge), "kurzbezeichnung");
		// ASD-Jahrgang
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.kuerzelStatistik), "kuerzelStatistik");
		// anzahlRestabschnitte
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.anzahlRestabschnitte, 0, 40), "anzahlRestabschnitte");
		// sortierung
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.sortierung, 0, 32000), "sortierung");
	}

	schulgliederung = computed<SchulgliederungKatalogEintrag | null>({
		get: () => Schulgliederung.data().getEintragBySchuljahrUndSchluessel(this.schuljahr, this.proxy.kuerzelSchulgliederung ?? ""),
		set: (value: SchulgliederungKatalogEintrag | null) => this.proxy.kuerzelSchulgliederung = value?.schluessel ?? null,
	});

	statistikJahrgang = computed<JahrgaengeKatalogEintrag | null>({
		get: () => Jahrgaenge.data().getEintragBySchuljahrUndSchluessel(this.schuljahr, this.proxy.kuerzelStatistik ?? ""),
		set: (value: JahrgaengeKatalogEintrag | null) => this.proxy.kuerzelStatistik = value?.schluessel ?? null,
	});

	bildungsstufe = computed<BildungsstufeKatalogEintrag | null>({
		get: () => Bildungsstufe.data().getEintragByID(this.proxy.idBildungsstufe ?? -1),
		set: (value: BildungsstufeKatalogEintrag | null) => this.proxy.idBildungsstufe = value?.id ?? null,
	});

	folgejahrgang = computed<JahrgangsDaten | null>({
		get: () => this.jahrgaengeById.get(this.proxy.idFolgejahrgang ?? -1) ?? null,
		set: (value: JahrgangsDaten | null) => this.proxy.idFolgejahrgang = value?.id ?? null,
	});

}
