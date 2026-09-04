import { ValidatorAnkreuzfloskelText } from "~/components/schule/kataloge/ankreuzkompetenzen/modelproxy/validation/ValidatorAnkreuzfloskelText";
import { AnkreuzkompetenzAbschnitt } from "~/components/schule/kataloge/ankreuzkompetenzen/AnkreuzkompetenzAbschnitt";
import { computed } from "vue";
import type { SchulgliederungKatalogEintrag } from "@core/asd/data/schule/SchulgliederungKatalogEintrag";
import { Schulgliederung } from "@core/asd/types/schule/Schulgliederung";
import type { FachDaten } from "@core/core/data/fach/FachDaten";
import type { Ankreuzkompetenz } from "@core/core/data/schule/Ankreuzkompetenz";
import { ModelProxy } from "@ui/model/ModelProxy";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorNumberRange } from "@ui/validation/common/ValidatorNumberRange";

type AbschnittOption = { id: AnkreuzkompetenzAbschnitt; text: string };

/**
 * ModelProxy für Ankreuzkompetenzen.
 */
export class AnkreuzkompetenzenModelProxy extends ModelProxy<Ankreuzkompetenz> {

	private readonly _schuljahr: number;
	private readonly _faecherById: () => Map<number, FachDaten>;


	public static readonly abschnittOptionen: AbschnittOption[] = [
		{ id: AnkreuzkompetenzAbschnitt.HJ1, text: "1. HJ" },
		{ id: AnkreuzkompetenzAbschnitt.HJ2, text: "2. HJ" },
		{ id: AnkreuzkompetenzAbschnitt.BEIDE, text: "Beide" },
	];

	/**
	 * ModelProxy für Ankreuzkompetenzen.
	 *
	 * @param data          Lambda für den Zugriff auf die Original-Daten
	 * @param liste         Lambda zur Liste aller Ankreuzkompetenzen
	 * @param faecherById   Map der Fächer
	 * @param schuljahr     Das aktuelle Schuljahr
	 * @param patch         Methode zum Patchen einzelner Attribute
	 */
	constructor(
		data: () => Ankreuzkompetenz,
		liste: () => Iterable<Ankreuzkompetenz>,
		faecherById: () => Map<number, FachDaten>,
		schuljahr: number,
		patch?: (data: Partial<Ankreuzkompetenz>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof Ankreuzkompetenz> =
			["schulgliederung", "abschnitt", "istAktiv", "istSichtbar"];
		super({ data, patch, listOfAutopatchProps });
		this._schuljahr = schuljahr;
		this._faecherById = faecherById;
		this.addValidatoren(liste);
		this.validate();
	}

	private addValidatoren(liste: () => Iterable<Ankreuzkompetenz>) {
		this.addBlockingValidator(new ValidatorAnkreuzfloskelText(() => this.proxy, liste), "floskelText");
		// Fach
		this.addBlockingValidator(new ValidatorInputRequired(() => (this.proxy.istASV ? "ASV" : this.proxy.idFach)), "idFach");
		// Abschnitt
		this.addBlockingValidator(new ValidatorInputRequired(() => this.abschnitt.value), "abschnitt");
		// sortierung
		this.addBlockingValidator(new ValidatorInputRequired((): number => this.proxy.sortierung), 'sortierung');
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.sortierung, 0, 32000), "sortierung");
	}

	fach = computed<FachDaten | null>({
		get: () => this._faecherById().get(this.proxy.idFach ?? -1) ?? null,
		set: (fach: FachDaten | null) => {
			if (fach !== null) {
				this.proxy.idFach = fach.id;
				this.proxy.istASV = false;
				this.validate();
			}
			if (this.getFehler("floskelText").isEmpty() && this.getFehler("idFach").isEmpty()) {
				void this.patch();
			}
		},
	});

	istASV = computed<boolean>({
		get: () => this.proxy.istASV,
		set: (istASV: boolean) => {
			this.proxy.istASV = istASV;
			this.proxy.idFach = null;
			this.validate();
			if (this.getFehler("floskelText").isEmpty() && this.getFehler("idFach").isEmpty()) {
				void this.patch();
			}
		},
	});

	schulgliederung = computed<SchulgliederungKatalogEintrag | null>({
		get: () => Schulgliederung.data().getEintragBySchuljahrUndSchluessel(this._schuljahr, this.proxy.schulgliederung ?? ""),
		set: (schulgliederung: SchulgliederungKatalogEintrag | null) => this.proxy.schulgliederung = schulgliederung?.schluessel ?? null,
	});

	abschnitt = computed<AbschnittOption | null>({
		get: () => AnkreuzkompetenzenModelProxy.abschnittOptionen.find(a => a.id === this.proxy.abschnitt) ?? null,
		set: (abschnitt: AbschnittOption | null) => this.proxy.abschnitt = abschnitt?.id ?? null,
	});
}
