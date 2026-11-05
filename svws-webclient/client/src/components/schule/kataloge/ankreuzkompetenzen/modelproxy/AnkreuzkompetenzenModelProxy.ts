import { computed } from "vue";

import type { SchulgliederungKatalogEintrag } from "@core/asd/data/schule/SchulgliederungKatalogEintrag";
import { Schulgliederung } from "@core/asd/types/schule/Schulgliederung";
import type { FachDaten } from "@core/core/data/fach/FachDaten";
import type { Ankreuzkompetenz } from "@core/core/data/schule/Ankreuzkompetenz";
import { JavaInteger } from "@core/java/lang/JavaInteger";
import { ModelProxy } from "@ui/model/ModelProxy";
import { useSchuleState } from "@ui/states/SchuleState";
import type { AnkreuzkompetenzenListeManager } from "@ui/ui/manager/kataloge/AnkreuzkompetenzenListeManager";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorNumberRange } from "@ui/validation/common/ValidatorNumberRange";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { StringPattern, ValidatorStringMatchesPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";

import { AnkreuzkompetenzAbschnitt } from "~/components/schule/kataloge/ankreuzkompetenzen/AnkreuzkompetenzAbschnitt";

import { ValidatorAnkreuzfloskelTextIsUniqueInList } from "./validation/ValidatorAnkreuzfloskelTextIsUniqueInList";

type AbschnittOption = { id: AnkreuzkompetenzAbschnitt; text: string };

/**
 * ModelProxy für Ankreuzkompetenzen.
 */
export class AnkreuzkompetenzenModelProxy extends ModelProxy<Ankreuzkompetenz> {

	private readonly _manager: () => AnkreuzkompetenzenListeManager;


	public static readonly abschnittOptionen: AbschnittOption[] = [
		{ id: AnkreuzkompetenzAbschnitt.HJ1, text: "1. HJ" },
		{ id: AnkreuzkompetenzAbschnitt.HJ2, text: "2. HJ" },
		{ id: AnkreuzkompetenzAbschnitt.BEIDE, text: "Beide" },
	];

	/**
	 * ModelProxy für Ankreuzkompetenzen.
	 *
	 * @param data      Lambda für den Zugriff auf die Original-Daten
	 * @param manager   Lambda für den Zugriff auf den Manager (Liste der Ankreuzkompetenzen und Fächer)
	 * @param patch     Methode zum Patchen einzelner Attribute
	 */
	constructor(
		data: () => Ankreuzkompetenz,
		manager: () => AnkreuzkompetenzenListeManager,
		patch?: (data: Partial<Ankreuzkompetenz>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof Ankreuzkompetenz> =
			["schulgliederung", "abschnitt", "istAktiv", "istSichtbar", "idFach", "istASV"];
		super({ data, patch, listOfAutopatchProps });
		this._manager = manager;
		this.addValidatoren();
		this.setFachValidation(this.proxy.istASV);
		this.validate();
	}

	private addValidatoren() {
		// Floskeltext
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.floskelText), "floskelText");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.floskelText, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "floskelText");
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.floskelText, null, 255), "floskelText");
		this.addBlockingValidator(new ValidatorAnkreuzfloskelTextIsUniqueInList(() => this.proxy, () => this._manager().liste.list()), "floskelText", "idFach", "istASV");
		// Fach
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.idFach), "idFach");
		// istASV
		// Wird ASV gesetzt und das Fach ist noch leer, dann darf die Validierung an istASV nicht durchlaufen, sodass der autopatch nicht ausgeführt wird. Es
		// wird revalidiert, sobald idFach gesetzt wurde.
		this.addBlockingValidator(new ValidatorInputRequired(() => (this.proxy.istASV ? "ASV" : this.proxy.idFach)), "istASV", "idFach");
		// Abschnitt
		this.addBlockingValidator(new ValidatorInputRequired(() => this.abschnitt.value), "abschnitt");
		// sortierung
		this.addBlockingValidator(new ValidatorInputRequired((): number => this.proxy.sortierung), 'sortierung');
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.sortierung, 0, JavaInteger.MAX_VALUE), "sortierung");
	}

	fach = computed<FachDaten | null>({
		get: () => this._manager().faecherById.get(this.proxy.idFach ?? -1) ?? null,
		set: (fach: FachDaten | null) =>
			void this.applyToPending((fach !== null) ? { idFach: fach.id, istASV: false } : { idFach: null }),
	});

	istASV = computed<boolean>({
		get: () => this.proxy.istASV,
		set: (istASV: boolean) => {
			this.setFachValidation(istASV);
			void this.applyToPending(istASV ? { istASV, idFach: null } : { istASV });
		},
	});

	schulgliederung = computed<SchulgliederungKatalogEintrag | null>({
		get: () => Schulgliederung.data().getEintragBySchuljahrUndSchluessel(useSchuleState().abschnitt.schuljahr, this.proxy.schulgliederung ?? ""),
		set: (schulgliederung: SchulgliederungKatalogEintrag | null) => this.proxy.schulgliederung = schulgliederung?.schluessel ?? null,
	});

	abschnitt = computed<AbschnittOption | null>({
		get: () => AnkreuzkompetenzenModelProxy.abschnittOptionen.find(a => a.id === this.proxy.abschnitt) ?? null,
		set: (abschnitt: AbschnittOption | null) => this.proxy.abschnitt = abschnitt?.id ?? null,
	});

	/**
	 * Das Fach ist nur Pflicht, wenn es sich nicht um eine ASV-Ankreuzkompetenz handelt.
	 *
	 * @param istASV   gibt an, ob es sich um eine ASV-Ankreuzkompetenz handelt
	 */
	private setFachValidation(istASV: boolean) {
		if (istASV) {
			this.disableValidation("idFach");
		} else {
			this.enableValidation("idFach");
		}
	}

	/**
	 * Setzt beim Wechsel der Daten die Fach-Validierung passend zum neuen Eintrag.
	 */
	public override reset(): void {
		this.setFachValidation(this.data.istASV);
		super.reset();
	}
}
