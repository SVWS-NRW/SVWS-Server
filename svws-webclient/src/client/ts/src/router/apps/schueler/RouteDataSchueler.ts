import type { SchuelerListeEintrag, SchuelerStammdaten, KlassenListeEintrag, JahrgangsListeEintrag, KursListeEintrag, GostJahrgang} from "@core";
import type { ShallowReactive} from "vue";
import type { Filter } from "~/components/schueler/SSchuelerAuswahlProps";
import type { RouteNode } from "~/router/RouteNode";
import { SchuelerStatus } from "@core";
import { shallowReactive, shallowRef } from "vue";
import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { routeSchueler } from "../RouteSchueler";
import { routeSchuelerIndividualdaten } from "./RouteSchuelerIndividualdaten";

import { useDebounceFn } from '@vueuse/shared';
interface RouteStateSchueler {
	idSchuljahresabschnitt: number,
	auswahl: SchuelerListeEintrag | undefined;
	auswahlGruppe: SchuelerListeEintrag[];
	stammdaten: SchuelerStammdaten | undefined;
	mapSchueler: Map<number, SchuelerListeEintrag>;
	mapKlassen: Map<number, KlassenListeEintrag>;
	mapJahrgaenge: Map<number, JahrgangsListeEintrag>;
	mapKurse: Map<number, KursListeEintrag>;
	mapAbiturjahrgaenge: Map<number, GostJahrgang>;
	filter: ShallowReactive<Filter>;
	view: RouteNode<any, any>;
}

export class RouteDataSchueler {
	private static _defaultState : RouteStateSchueler = {
		idSchuljahresabschnitt: -1,
		auswahl: undefined,
		auswahlGruppe: [],
		stammdaten: undefined,
		mapSchueler: new Map(),
		mapKlassen: new Map(),
		mapJahrgaenge: new Map(),
		mapKurse: new Map(),
		mapAbiturjahrgaenge: new Map(),
		filter: shallowReactive({
			jahrgang: undefined,
			kurs: undefined,
			klasse: undefined,
			schulgliederung: undefined,
			status: [ SchuelerStatus.AKTIV, SchuelerStatus.EXTERN ]
		}),
		view: routeSchuelerIndividualdaten,
	};

	private _state = shallowRef(RouteDataSchueler._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateSchueler>) {
		this._state.value = Object.assign({ ... RouteDataSchueler._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateSchueler>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	private firstSchueler(mapSchueler: Map<number, SchuelerListeEintrag>): SchuelerListeEintrag | undefined {
		if (mapSchueler.size === 0)
			return undefined;
		const arr = [...mapSchueler.values()]
			.filter(s => !this.filter.status.length || this.filter.status.map(s => s.id).includes(s.status))
			.filter(s => !this.filter.jahrgang || s.jahrgang === this.filter.jahrgang.kuerzel)
			.filter(s => !this.filter.klasse || s.idKlasse === this.filter.klasse.id)
			.filter(s => !this.filter.kurs || s.kurse?.toArray(new Array<number>()).includes(this.filter.kurs.id))
			.filter(s => !this.filter.schulgliederung || s.schulgliederung === this.filter.schulgliederung.daten.kuerzel)
		return arr[0];
	}

	private async ladeStammdaten(eintrag: SchuelerListeEintrag | undefined): Promise<SchuelerStammdaten | undefined> {
		if (eintrag === undefined)
			return undefined;
		return await api.server.getSchuelerStammdaten(api.schema, eintrag.id);
	}

	private async ladeMapAbiturjahrgaenge(): Promise<Map<number, GostJahrgang>> {
		// Prüfe, ob die Schulform eine gymnasiale Oberstufe hat und lade ggf. die Abiturjahrgänge
		const mapAbiturjahrgaenge = new Map<number, GostJahrgang>();
		if (api.schulform.daten.hatGymOb) {
			const listAbiturjahrgaenge = await api.server.getGostAbiturjahrgaenge(api.schema)
			for (const j of listAbiturjahrgaenge)
				mapAbiturjahrgaenge.set(j.abiturjahr, j);
		}
		return mapAbiturjahrgaenge;
	}
	/**
	 * Setzt den Schuljahresabschnitt und triggert damit das Laden der Defaults für diesen Abschnitt
	 *
	 * @param {number} idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 */
	public async setSchuljahresabschnitt(idSchuljahresabschnitt: number) {
		const mapSchueler = await api.getSchuelerListeAktuell(idSchuljahresabschnitt)
		const mapKlassen = await api.getKlassenListe(idSchuljahresabschnitt);
		const mapKurse = await api.getKursListe(idSchuljahresabschnitt);
		const mapJahrgaenge = await api.getJahrgangsListe();
		const mapAbiturjahrgaenge = await this.ladeMapAbiturjahrgaenge();
		const schuelerVorher = this._state.value.auswahl === undefined ? undefined : mapSchueler.get(this._state.value.auswahl.id);
		const auswahl = schuelerVorher === undefined ? this.firstSchueler(mapSchueler) : schuelerVorher;
		const stammdaten = await this.ladeStammdaten(auswahl);
		const view = schuelerVorher === undefined ? routeSchuelerIndividualdaten : this._state.value.view;
		this.setPatchedDefaultState({
			idSchuljahresabschnitt,
			mapSchueler,
			mapKlassen,
			mapKurse,
			mapJahrgaenge,
			mapAbiturjahrgaenge,
			auswahl,
			stammdaten,
			view
		});
	}

	/**
	 * Setzt den ausgewählten Schueler und lädt dessen Stammddaten.
	 *
	 * @param schueler   der ausgewählte Schüler
	 */
	public async setSchueler(schueler: SchuelerListeEintrag | undefined) {
		if (schueler?.id === this._state.value.auswahl?.id)
			return;
		if ((schueler === undefined) || (this.mapSchueler.size === 0))
			return;
		const auswahl = (this.mapSchueler.get(schueler.id) === undefined) ? this.firstSchueler(this.mapSchueler) : schueler;
		const stammdaten = await this.ladeStammdaten(auswahl);
		this.setPatchedState({ auswahl, stammdaten });
	}

	public async setView(view: RouteNode<any,any>) {
		if (routeSchueler.children.includes(view))
			this.setPatchedState({ view: view });
		else
			throw new Error("Diese für Schüler gewählte Ansicht wird nicht unterstützt.");
	}

	public get view(): RouteNode<any,any> {
		return this._state.value.view;
	}

	get auswahl(): SchuelerListeEintrag | undefined {
		return this._state.value.auswahl;
	}

	get auswahlGruppe(): SchuelerListeEintrag[] {
		return this._state.value.auswahlGruppe;
	}

	get hatStammdaten(): boolean {
		return this._state.value.stammdaten !== undefined;
	}

	get stammdaten(): SchuelerStammdaten {
		if (this._state.value.stammdaten === undefined)
			throw new Error("Unerwarteter Fehler: Stammdaten nicht initialisiert");
		return this._state.value.stammdaten;
	}

	get filter(): Filter {
		return this._state.value.filter;
	}

	get mapSchueler(): Map<number, SchuelerListeEintrag> {
		return this._state.value.mapSchueler;
	}

	get mapKlassen(): Map<number, KlassenListeEintrag> {
		return this._state.value.mapKlassen;
	}

	get mapKurse(): Map<number, KursListeEintrag> {
		return this._state.value.mapKurse;
	}

	get mapJahrgaenge(): Map<number, JahrgangsListeEintrag> {
		return this._state.value.mapJahrgaenge;
	}

	get mapAbiturjahrgaenge(): Map<number, GostJahrgang> {
		return this._state.value.mapAbiturjahrgaenge;
	}

	patch = useDebounceFn((data: Partial<SchuelerStammdaten>)=> this.patchit(data), 100)

	patchit = (data : Partial<SchuelerStammdaten>) => {
		if (this.auswahl === undefined)
			return;
		api.server.patchSchuelerStammdaten(data, api.schema, this.auswahl.id).then(()=>{
			const stammdaten = this.stammdaten;
			this.setPatchedState({stammdaten: Object.assign(stammdaten, data)});
		}).catch((e) => console.log(e))
		// TODO Bei Anpassungen von nachname, vorname -> routeSchueler: Schülerliste aktualisieren...
	}

	gotoSchueler = async (value: SchuelerListeEintrag | undefined) => {
		if (value === undefined || value === null) {
			await RouteManager.doRoute({ name: routeSchueler.name, params: { } });
			return;
		}
		const redirect_name: string = (routeSchueler.selectedChild === undefined) ? routeSchuelerIndividualdaten.name : routeSchueler.selectedChild.name;
		await RouteManager.doRoute({ name: redirect_name, params: { id: value.id } });
	}

	setFilter = (value: Filter) => this.setPatchedState({filter: Object.assign(this._state.value.filter, value)});
	setAuswahlGruppe = (auswahlGruppe: SchuelerListeEintrag[]) =>	this.setPatchedState({auswahlGruppe});
}
