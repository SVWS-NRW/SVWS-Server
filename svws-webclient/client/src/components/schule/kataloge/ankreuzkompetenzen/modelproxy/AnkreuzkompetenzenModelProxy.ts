import { ModelProxy, ValidatorInputRequired, ValidatorNumberRange } from "@ui";
import type { Ankreuzkompetenz, FachDaten, SchulgliederungKatalogEintrag } from "@core";
import { Schulgliederung } from "@core";
import { ValidatorAnkreuzfloskelText } from "~/components/schule/kataloge/ankreuzkompetenzen/modelproxy/validation/ValidatorAnkreuzfloskelText";
import { AnkreuzkompetenzAbschnitt } from "~/components/schule/kataloge/ankreuzkompetenzen/AnkreuzkompetenzAbschnitt";
import { computed } from "vue";

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
			["idFach", "schulgliederung", "abschnitt", "istAktiv", "istSichtbar"];
		super({ data, patch, listOfAutopatchProps, checkValidBeforePatch: true });
		this._schuljahr = schuljahr;
		this._faecherById = faecherById;
		this.addValidatoren(liste);
		this.validate();
	}

	private addValidatoren(liste: () => Iterable<Ankreuzkompetenz>) {
		this.addValidator(new ValidatorAnkreuzfloskelText(() => this.proxy, liste), "floskelText");
		// Fach
		this.addValidator(new ValidatorInputRequired(() => (this.proxy.istASV ? "ASV" : this.proxy.idFach)), "idFach");
		// Abschnitt
		this.addValidator(new ValidatorInputRequired(() => this.abschnitt.value), "abschnitt");
		// sortierung
		this.addValidator(new ValidatorNumberRange(() => this.proxy.sortierung, 0, 32000), "sortierung");
	}

	fach = computed<FachDaten | null>({
		get: () => this._faecherById().get(this.proxy.idFach ?? -1) ?? null,
		set: (fach: FachDaten | null) => {
			if (fach !== null) {
				this.proxy.idFach = fach.id;
				this.proxy.istASV = false;
				this.validate();
			}
		},
	});

	istASV = computed<boolean>({
		get: () => this.proxy.istASV,
		set: (istASV: boolean) => {
			this.proxy.istASV = istASV;
			this.proxy.idFach = null;
			this.validate();
		},
	});

	schulgliederung = computed<SchulgliederungKatalogEintrag | null>({
		get: () => Schulgliederung.data().getEintragBySchuljahrUndSchluessel(this._schuljahr, this.proxy.schulgliederung ?? ""),
		set: (schulgliederung: SchulgliederungKatalogEintrag | null) => this.proxy.schulgliederung = schulgliederung?.schluessel ?? null,
	});

	abschnitt = computed<AbschnittOption | null>({
		get: () => AnkreuzkompetenzenModelProxy.abschnittOptionen.find(a => a.id === this.proxy.abschnitt as AnkreuzkompetenzAbschnitt) ?? null,
		set: (abschnitt: AbschnittOption | null) => {
			if (abschnitt !== null) {
				this.proxy.abschnitt = abschnitt.id;
			}
		},
	});
}
