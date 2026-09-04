import { ValidatorJahrgangBezeichnung } from "~/components/schule/kataloge/jahrgaenge/modelproxy/validation/ValidatorJahrgangBezeichnung";
import { ValidatorJahrgangKuerzel } from "~/components/schule/kataloge/jahrgaenge/modelproxy/validation/ValidatorJahrgangKuerzel";
import { ValidatorJahrgangKurzbezeichnung } from "~/components/schule/kataloge/jahrgaenge/modelproxy/validation/ValidatorJahrgangKurzbezeichnung";
import { computed } from "vue";
import type { JahrgaengeKatalogEintrag } from "@core/asd/data/jahrgang/JahrgaengeKatalogEintrag";
import type { BildungsstufeKatalogEintrag } from "@core/asd/data/schule/BildungsstufeKatalogEintrag";
import type { SchulgliederungKatalogEintrag } from "@core/asd/data/schule/SchulgliederungKatalogEintrag";
import { Jahrgaenge } from "@core/asd/types/jahrgang/Jahrgaenge";
import { Bildungsstufe } from "@core/asd/types/schule/Bildungsstufe";
import { Schulgliederung } from "@core/asd/types/schule/Schulgliederung";
import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
import { ModelProxy } from "@ui/model/ModelProxy";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorNumberRange } from "@ui/validation/common/ValidatorNumberRange";

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
			["idFolgejahrgang", "idSchulgliederung", "idJahrgang", "idBildungsstufe", "istSichtbar"];
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
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.idJahrgang), "idJahrgang");
		// anzahlRestabschnitte
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.anzahlRestabschnitte, 0, 40), "anzahlRestabschnitte");
		// sortierung
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.sortierung), "sortierung");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.sortierung, 0, 32000), "sortierung");
	}

	schulgliederung = computed<SchulgliederungKatalogEintrag | null>({
		get: () => Schulgliederung.data().getEintragByID(this.proxy.idSchulgliederung ?? -1),
		set: (value: SchulgliederungKatalogEintrag | null) => this.proxy.idSchulgliederung = value?.id ?? null,
	});

	asdJahrgang = computed<JahrgaengeKatalogEintrag | null>({
		get: () => Jahrgaenge.data().getEintragByID(this.proxy.idJahrgang ?? -1),
		set: (value: JahrgaengeKatalogEintrag | null) => this.proxy.idJahrgang = value?.id ?? null,
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
