import { ValidatorFloskelKuerzel } from "~/components/schule/kataloge/floskeln/modelproxy/validation/ValidatorFloskelKuerzel";
import { computed } from "vue";
import { Floskelgruppenart } from "@core/asd/types/schule/Floskelgruppenart";
import type { FachDaten } from "@core/core/data/fach/FachDaten";
import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
import type { Floskel } from "@core/core/data/schule/Floskel";
import type { Floskelgruppe } from "@core/core/data/schule/Floskelgruppe";
import { ArrayList } from "@core/java/util/ArrayList";
import { ModelProxy } from "@ui/model/ModelProxy";
import type { FloskelnListeManager } from "@ui/ui/manager/kataloge/FloskelnListeManager";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorNumberRange } from "@ui/validation/common/ValidatorNumberRange";

export class FloskelModelProxy extends ModelProxy<Floskel> {

	private readonly manager: () => FloskelnListeManager;

	constructor(
		data: () => Floskel,
		alleFloskeln: () => Iterable<Floskel>,
		manager: () => FloskelnListeManager,
		patch?: (data: Partial<Floskel>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof Floskel> = ['idFloskelgruppe', 'idFach', 'niveau', 'idsJahrgaenge'];
		super({ data, patch, listOfAutopatchProps });
		this.manager = manager;
		this.addValidatoren(alleFloskeln);
		this.validate();
	}

	private addValidatoren(liste: () => Iterable<Floskel>) {
		this.addBlockingValidator(new ValidatorFloskelKuerzel(() => this.proxy, liste), 'kuerzel');
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.text), 'text');
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.idFloskelgruppe), 'idFloskelgruppe');
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.sortierung), "sortierung");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.sortierung, 0, 32000), "sortierung");
	}

	hatFloskelgruppeArtFach = computed<boolean>(() => {
		const fg = this.selectedFloskelgruppe.value;
		if (fg === null) {
			return false;
		}
		return this.istFloskelgruppeArtFach(fg.idFloskelgruppenart);
	});

	selectedFloskelgruppe = computed<Floskelgruppe | null>({
		get: (): Floskelgruppe | null => this.manager().floskelgruppenById.get(this.proxy.idFloskelgruppe ?? -1) ?? null,
		set: (value: Floskelgruppe | null) => {
			this.proxy.idFloskelgruppe = value?.id ?? null;
			if (!this.istFloskelgruppeArtFach(value?.idFloskelgruppenart ?? null)) {
				this.proxy.idFach = null;
			}
		},
	});

	selectedFach = computed<FachDaten | null>({
		get: (): FachDaten | null => this.manager().faecherById.get(this.proxy.idFach ?? -1) ?? null,
		set: (value: FachDaten | null) => this.proxy.idFach = value?.id ?? null,
	});

	selectedJahrgang = computed<JahrgangsDaten | null>({
		get: (): JahrgangsDaten | null => {
			const ids = this.proxy.idsJahrgaenge;
			if (ids === null || ids.isEmpty()) {
				return null;
			}
			return this.manager().jahrgaengeById.get(ids.get(0)) ?? null;
		},
		set: (value: JahrgangsDaten | null | undefined) => {
			const list = new ArrayList<number>();
			if (value !== null && value !== undefined) {
				list.add(value.id);
			}
			this.proxy.idsJahrgaenge = list;
		},
	});

	selectedNiveau = computed<number | null>({
		get: (): number | null => this.proxy.niveau,
		set: (value: number | null) => this.proxy.niveau = value ?? null,
	});

	private istFloskelgruppeArtFach(idFloskelgruppenart: number | null): boolean {
		return Floskelgruppenart.data().getWertByIDOrNull(idFloskelgruppenart ?? -1)?.name() === 'FACH';
	}
}
