import type { JahrgaengeKatalogEintrag } from "@core/asd/data/jahrgang/JahrgaengeKatalogEintrag";
import type { KAOAAnschlussoptionenKatalogEintrag } from "@core/asd/data/kaoa/KAOAAnschlussoptionenKatalogEintrag";
import type { KAOABerufsfeldKatalogEintrag } from "@core/asd/data/kaoa/KAOABerufsfeldKatalogEintrag";
import type { KAOAEbene4KatalogEintrag } from "@core/asd/data/kaoa/KAOAEbene4KatalogEintrag";
import type { KAOAKategorieKatalogEintrag } from "@core/asd/data/kaoa/KAOAKategorieKatalogEintrag";
import type { KAOAMerkmalKatalogEintrag } from "@core/asd/data/kaoa/KAOAMerkmalKatalogEintrag";
import type { KAOAZusatzmerkmalKatalogEintrag } from "@core/asd/data/kaoa/KAOAZusatzmerkmalKatalogEintrag";
import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
import { Jahrgaenge } from "@core/asd/types/jahrgang/Jahrgaenge";
import { KAOAAnschlussoptionen } from "@core/asd/types/kaoa/KAOAAnschlussoptionen";
import { KAOABerufsfeld } from "@core/asd/types/kaoa/KAOABerufsfeld";
import { KAOAEbene4 } from "@core/asd/types/kaoa/KAOAEbene4";
import { KAOAKategorie } from "@core/asd/types/kaoa/KAOAKategorie";
import { KAOAMerkmal } from "@core/asd/types/kaoa/KAOAMerkmal";
import { KAOAZusatzmerkmal } from "@core/asd/types/kaoa/KAOAZusatzmerkmal";
import type { SchuelerKAoADaten } from "@core/core/data/schueler/SchuelerKAoADaten";
import { ModelProxy } from "@ui/model/ModelProxy";
import type { SchuelerKAoAManager } from "@ui/ui/manager/schueler/SchuelerKAoAManager";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { ValidatorStringMatchesPattern, StringPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";
import { computed } from "vue";

export class SchuelerKaoaModelProxy extends ModelProxy<SchuelerKAoADaten> {

	private readonly manager: () => SchuelerKAoAManager;

	constructor(
		data: () => SchuelerKAoADaten,
		manager: () => SchuelerKAoAManager,
		patch?: (data: Partial<SchuelerKAoADaten>) => Promise<boolean>
	) {
		super({ data, patch });
		this.manager = manager;
		this.addValidatoren();
		this.validate();
	}

	private addValidatoren() {
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.idSchuljahresabschnitt === -1 ? null : this.proxy.idSchuljahresabschnitt), "idSchuljahresabschnitt");
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.idKategorie === -1 ? null : this.proxy.idKategorie), "idKategorie");
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.idMerkmal === -1 ? null : this.proxy.idMerkmal), "idMerkmal");
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.idZusatzmerkmal === -1 ? null : this.proxy.idZusatzmerkmal), "idZusatzmerkmal");
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.idBerufsfeld === -1 ? null : this.proxy.idBerufsfeld), "idBerufsfeld");
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.idAnschlussoption === -1 ? null : this.proxy.idAnschlussoption), "idAnschlussoption");
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.idEbene4 === -1 ? null : this.proxy.idEbene4), "idEbene4");
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.bemerkung, null, 255), "bemerkung");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.bemerkung, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "bemerkung");
		this.setInactiveValidators();
	}

	selectedSchuljahresabschnitt = computed<Schuljahresabschnitt | null>({
		get: () => this.manager().schuljahresabschnitteById.get(this.proxy.idSchuljahresabschnitt) ?? null,
		set: (v: Schuljahresabschnitt | null) => this.updateModel(1, v?.id ?? -1),
	});

	selectedKategorie = computed<KAOAKategorieKatalogEintrag | null>({
		get: () => KAOAKategorie.data().getEintragByID(this.proxy.idKategorie),
		set: (v: KAOAKategorieKatalogEintrag | null) => this.updateModel(2, v?.id ?? -1),
	});

	selectedMerkmal = computed<KAOAMerkmalKatalogEintrag | null>({
		get: () => KAOAMerkmal.data().getEintragByID(this.proxy.idMerkmal),
		set: (v: KAOAMerkmalKatalogEintrag | null) => this.updateModel(3, v?.id ?? -1),
	});

	selectedZusatzmerkmal = computed<KAOAZusatzmerkmalKatalogEintrag | null>({
		get: () => KAOAZusatzmerkmal.data().getEintragByID(this.proxy.idZusatzmerkmal),
		set: (v: KAOAZusatzmerkmalKatalogEintrag | null) => this.updateModel(4, v?.id ?? -1),
	});

	selectedEbene4 = computed<KAOAEbene4KatalogEintrag | null>({
		get: () => KAOAEbene4.data().getEintragByID(this.proxy.idEbene4 ?? -1),
		set: (v: KAOAEbene4KatalogEintrag | null) => this.proxy.idEbene4 = v?.id ?? null,
	});

	selectedAnschlussoption = computed<KAOAAnschlussoptionenKatalogEintrag | null>({
		get: () => KAOAAnschlussoptionen.data().getEintragByID(this.proxy.idAnschlussoption ?? -1),
		set: (v: KAOAAnschlussoptionenKatalogEintrag | null) => this.proxy.idAnschlussoption = v?.id ?? null,
	});

	selectedBerufsfeld = computed<KAOABerufsfeldKatalogEintrag | null>({
		get: () => KAOABerufsfeld.data().getEintragByID(this.proxy.idBerufsfeld ?? null),
		set: (v: KAOABerufsfeldKatalogEintrag | null) => this.proxy.idBerufsfeld = v?.id ?? null,
	});

	jahrgang = computed<JahrgaengeKatalogEintrag | null>(() => {
		const kuerzelJahrgang = this.manager().lernabschnitteBySchuljahr.get(this.manager().schuljahr)?.jahrgang ?? '';
		return Jahrgaenge.data().getWertByKuerzel(kuerzelJahrgang)?.daten(this.manager().schuljahr) ?? null;
	});

	// setzt die selektierten Felder abhängig vom Ziellevel zurück
	private updateModel(targetLevel: 1 | 2 | 3 | 4, value: number) {
		if (targetLevel === 1) {
			this.proxy.idSchuljahresabschnitt = value;
			this.proxy.idJahrgang = this.jahrgang.value?.id ?? -1;
		}
		if (targetLevel <= 2) {
			this.proxy.idKategorie = targetLevel === 2 ? value : -1;
		}
		if (targetLevel <= 3) {
			this.proxy.idMerkmal = targetLevel === 3 ? value : -1;
		}
		if (targetLevel <= 4) {
			this.proxy.idZusatzmerkmal = targetLevel === 4 ? value : -1;
		}
		this.proxy.idEbene4 = null;
		this.proxy.idAnschlussoption = null;
		this.proxy.idBerufsfeld = null;
		this.proxy.bemerkung = null;
		this.setInactiveValidators();
	}

	private setInactiveValidators() {
		const zusatzmerkmal = KAOAZusatzmerkmal.data().getEintragByID(this.proxy.idZusatzmerkmal);
		this.disableValidation("idEbene4");
		this.disableValidation("idAnschlussoption");
		this.disableValidation("idBerufsfeld");

		if (zusatzmerkmal === null) {
			return;
		}

		switch (zusatzmerkmal.optionsart) {
			case "SBO_EBENE_4":
				this.enableValidation("idEbene4");
				return;
			case "ANSCHLUSSOPTION":
				this.enableValidation("idAnschlussoption");
				return;
			case "BERUFSFELD":
				this.enableValidation("idBerufsfeld");
				return;
			case "FREITEXT":
			case "FREITEXT_BERUF":
				this.enableValidation("bemerkung");
				return;
			default:
				return;
		}
	}
}
