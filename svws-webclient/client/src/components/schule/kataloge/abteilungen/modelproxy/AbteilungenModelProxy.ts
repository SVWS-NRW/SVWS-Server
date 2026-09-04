import { ValidatorAbteilungBezeichnung } from "~/components/schule/kataloge/abteilungen/modelproxy/validation/ValidatorAbteilungBezeichnung";
import { computed } from "vue";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import type { Abteilung } from "@core/core/data/schule/Abteilung";
import { JavaInteger } from "@core/java/lang/JavaInteger";
import { ModelProxy } from "@ui/model/ModelProxy";
import type { AbteilungenListeManager } from "@ui/ui/manager/kataloge/AbteilungenListeManager";
import type { ViewType } from "@ui/ui/nav/ViewType";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorNumberRange } from "@ui/validation/common/ValidatorNumberRange";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { ValidatorStringMatchesPattern, StringPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";

/**
 * ModelProxy für Abteilungen.
 */
export class AbteilungenModelProxy extends ModelProxy<Abteilung> {

	private readonly manager: () => AbteilungenListeManager;
	private readonly viewType: ViewType;

	constructor(data: () => Abteilung, manager: () => AbteilungenListeManager, viewType: ViewType, patch?: (data: Partial<Abteilung>) => Promise<boolean>) {
		const listOfAutopatchProps: Iterable<keyof Abteilung> = ["idAbteilungsleiter", "istSichtbar"];
		super({ data, patch, listOfAutopatchProps });
		this.manager = manager;
		this.viewType = viewType;

		this.addValidatoren();

		this.validate();
	}

	private addValidatoren() {
		this.addBlockingValidator(new ValidatorAbteilungBezeichnung(() => this.proxy, () => this.manager().liste.list(),
			() => this.manager().abteilungenFolgeAbschnittById.values(), this.viewType), "bezeichnung");

		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.raum, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), 'raum');
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.raum, null, 20), 'raum');

		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.email, StringPattern.IS_EMAIL), 'email');
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.email, null, 100), 'email');

		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.durchwahl, StringPattern.IS_PHONE_NUMBER_OR_EXTENSION), 'durchwahl');
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.durchwahl, null, 20), 'durchwahl');

		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.sortierung), "sortierung");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.sortierung, 0, JavaInteger.MAX_VALUE), "sortierung");
	}

	abteilungsleiter = computed<LehrerListeEintrag | null>({
		get: () => this.manager().lehrerById.get(this.proxy.idAbteilungsleiter ?? -1) ?? null,
		set: (value: LehrerListeEintrag | null) => this.proxy.idAbteilungsleiter = value?.id ?? null,
	});

}
