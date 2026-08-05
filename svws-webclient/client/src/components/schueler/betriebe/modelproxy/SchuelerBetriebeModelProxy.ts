import { computed } from "vue";
import type { SchuelerBetriebeManager } from "@ui";
import { ModelProxy, ValidatorInputRequired, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";
import type { Beschaeftigungsart, Betrieb, BetriebeAnsprechpartner, LehrerListeEintrag, OrtKatalogEintrag, SchuelerBetrieb } from "@core";
import { StringPattern } from "../../../../../../ui/src/validation/common/ValidatorStringMatchesPattern";
import { orteStateImpl } from "~/states/kataloge/OrteStateImpl";

export class SchuelerBetriebeModelProxy extends ModelProxy<SchuelerBetrieb> {

	private readonly manager: () => SchuelerBetriebeManager;

	constructor(
		data: () => SchuelerBetrieb,
		manager: () => SchuelerBetriebeManager,
		patch?: (data: Partial<SchuelerBetrieb>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof SchuelerBetrieb> =
			["id", "idBetrieb", "erhaeltAnschreiben", "istPraktikum", "idBeschaeftigungsart", "idBetreuungslehrer", "idAnsprechpartner"];
		super({ data, patch, listOfAutopatchProps });
		this.manager = manager;
		this.addValidatoren();
		this.validate();
	}

	private addValidatoren() {
		// betrieb
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.idBetrieb), 'idBetrieb');
		// ausbilder
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.nameAusbilder, null, 30), "nameAusbilder");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.nameAusbilder, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "nameAusbilder");
	}

	betrieb = computed<Betrieb | null>({
		get: () => this.manager().betriebeById.get(this.proxy.idBetrieb ?? -1) ?? null,
		set: (v: Betrieb | null) => this.proxy.idBetrieb = v?.id ?? -1,
	});

	beschaeftigungsart = computed<Beschaeftigungsart | null>({
		get: () => this.manager().beschaeftigungsartenById.get(this.proxy.idBeschaeftigungsart ?? -1) ?? null,
		set: (v: Beschaeftigungsart | null) => this.proxy.idBeschaeftigungsart = v?.id ?? null,
	});

	betreuendeLehrkraft = computed<LehrerListeEintrag | null>({
		get: () => this.manager().lehrerById.get(this.proxy.idBetreuungslehrer ?? -1) ?? null,
		set: (v: LehrerListeEintrag | null) => this.proxy.idBetreuungslehrer = v?.id ?? null,
	});

	ansprechpartner = computed<BetriebeAnsprechpartner | null>({
		get: () => this.manager().ansprechpartnerById.get(this.proxy.idAnsprechpartner ?? -1) ?? null,
		set: (v: BetriebeAnsprechpartner | null) => this.proxy.idAnsprechpartner = v?.id ?? null,
	});

	ort = computed<OrtKatalogEintrag | null>(() => orteStateImpl.orte.byId.get(this.betrieb.value?.idOrt ?? -1) ?? null);

}
