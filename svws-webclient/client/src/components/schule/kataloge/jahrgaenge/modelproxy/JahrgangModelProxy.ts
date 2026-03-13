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
	 * @param liste Lambda zur Liste aller Jahrgangsdaten
	 * @param schuljahr Das aktuelle Schuljahr
	 * @param patch Methode zum Patchen einzelner Attribute
	 */
	constructor(
		data: () => JahrgangsDaten,
		liste: () => Iterable<JahrgangsDaten>,
		schuljahr: number,
		patch?: (data: Partial<JahrgangsDaten>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof JahrgangsDaten> =
			["idFolgejahrgang", "kuerzelSchulgliederung", "kuerzelStatistik", "idBildungsstufe", "istSichtbar"];
		super({ data, patch, listOfAutopatchProps, checkValidBeforePatch: true });
		this.schuljahr = schuljahr;
		this.jahrgaengeById = this.mapJahrgaenge(liste());
		this.addValidatoren(liste);
		this.validate();
	}
	private mapJahrgaenge(liste: Iterable<JahrgangsDaten>) {
		const result = new Map<number, JahrgangsDaten>();
		for (const jahrgang of liste) {
			result.set(jahrgang.id, jahrgang);
		}
		return result;
	}

	private addValidatoren(liste: () => Iterable<JahrgangsDaten>) {
		this.addValidator(new ValidatorJahrgangKuerzel(() => this.proxy, liste), "kuerzel");
		this.addValidator(new ValidatorJahrgangBezeichnung(() => this.proxy, liste), "bezeichnung");
		this.addValidator(new ValidatorJahrgangKurzbezeichnung(() => this.proxy, liste), "kurzbezeichnung");
		// ASD-Jahrgang
		this.addValidator(new ValidatorInputRequired(() => this.proxy.kuerzelStatistik), "kuerzelStatistik");
		// anzahlRestabschnitte
		this.addValidator(new ValidatorNumberRange(() => this.proxy.anzahlRestabschnitte, 0, 40), "anzahlRestabschnitte");
		// sortierung
		this.addValidator(new ValidatorNumberRange(() => this.proxy.sortierung, 0, 32000), "sortierung");
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
