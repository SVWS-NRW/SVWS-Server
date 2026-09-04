import type { JahrgaengeKatalogEintrag } from "@core/asd/data/jahrgang/JahrgaengeKatalogEintrag";
import type { SchuelerSchulbesuchSchule } from "@core/asd/data/schueler/SchuelerSchulbesuchSchule";
import type { SchulgliederungKatalogEintrag } from "@core/asd/data/schule/SchulgliederungKatalogEintrag";
import { Jahrgaenge } from "@core/asd/types/jahrgang/Jahrgaenge";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { Schulgliederung } from "@core/asd/types/schule/Schulgliederung";
import type { SchulEintrag } from "@core/core/data/kataloge/SchulEintrag";
import { AdressenUtils } from "@core/core/utils/AdressenUtils";
import { ModelProxy } from "@ui/model/ModelProxy";
import type { SchuelerSchulbesuchManager } from "@ui/ui/manager/schueler/SchuelerSchulbesuchManager";
import { computed } from "vue";

export class SchuelerSchulbesuchSchuleModelProxy extends ModelProxy<SchuelerSchulbesuchSchule> {

	private readonly manager: () => SchuelerSchulbesuchManager;

	constructor(
		data: () => SchuelerSchulbesuchSchule,
		manager: () => SchuelerSchulbesuchManager) {
		super({ data });
		this.manager = manager;
	}

	schule = computed<SchulEintrag | null>({
		get: () => this.manager().schulenById.get(this.proxy.idSchule ?? -1) ?? null,
		set: (v: SchulEintrag | null) => this.proxy.idSchule = v?.id ?? null,
	});

	adresseSchule = computed<string>(() => {
		if (!this.schule.value) {
			return '';
		}
		const strasse = AdressenUtils.combineStrasse(
			this.schule.value.strassenname,
			this.schule.value.hausnummer,
			this.schule.value.zusatzHausnummer
		);
		return strasse + ', ' + this.schule.value.plz + ' ' + this.schule.value.ort;
	});

	schulform = computed<Schulform | null>(() => Schulform.data().getWertByIDOrNull(this.schule.value?.idSchulform ?? -1));

	jahrgangVon = computed<JahrgaengeKatalogEintrag | null>({
		get: () => Jahrgaenge.data().getEintragBySchuljahrUndSchluessel(this.manager().schuljahr, this.proxy.jahrgangVon ?? '') ?? null,
		set: (v: JahrgaengeKatalogEintrag | null) => this.proxy.jahrgangVon = v?.schluessel ?? null,
	});

	jahrgangBis = computed<JahrgaengeKatalogEintrag | null>({
		get: () => Jahrgaenge.data().getEintragBySchuljahrUndSchluessel(this.manager().schuljahr, this.proxy.jahrgangBis ?? '') ?? null,
		set: (v: JahrgaengeKatalogEintrag | null) => this.proxy.jahrgangBis = v?.schluessel ?? null,
	});

	schulgliederung = computed<SchulgliederungKatalogEintrag | null>({
		get: () => Schulgliederung.data().getEintragBySchuljahrUndSchluessel(this.manager().schuljahr, this.proxy.schluesselSchulgliederung ?? '') ?? null,
		set: (v: SchulgliederungKatalogEintrag | null) => this.proxy.schluesselSchulgliederung = v?.schluessel ?? null,
	});


}
