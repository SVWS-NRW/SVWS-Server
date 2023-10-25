import type { SchuelerListeEintrag, SchuelerStammdaten, KlassenListeEintrag, JahrgangsListeEintrag, KursListeEintrag, GostJahrgang, Schuljahresabschnitt, List} from "@core";
import type { RouteNode } from "~/router/RouteNode";
import { shallowRef } from "vue";

import { SchuelerListeManager, ArrayList} from "@core";
import { SchuelerStatus } from "@core";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";

import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { routeSchuelerIndividualdaten } from "~/router/apps/schueler/individualdaten/RouteSchuelerIndividualdaten";


interface RouteStateSchueler {
	idSchuljahresabschnitt: number,
	auswahl: SchuelerListeEintrag | null;
	schuelerListeManager: SchuelerListeManager;
	filtered: List<SchuelerListeEintrag>;
	auswahlGruppe: SchuelerListeEintrag[];
	stammdaten: SchuelerStammdaten | null;
	view: RouteNode<any, any>;
}

export class RouteDataSchueler {
	private static _defaultState : RouteStateSchueler = {
		idSchuljahresabschnitt: -1,
		auswahl: null,
		schuelerListeManager: new SchuelerListeManager(new ArrayList<SchuelerListeEintrag>(), new ArrayList<JahrgangsListeEintrag>, new ArrayList<KlassenListeEintrag>, new ArrayList<KursListeEintrag>(), new ArrayList<Schuljahresabschnitt>(), new ArrayList<GostJahrgang>()),
		filtered: new ArrayList<SchuelerListeEintrag>(),
		auswahlGruppe: [],
		stammdaten: null,
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

	private async ladeStammdaten(eintrag: SchuelerListeEintrag | null): Promise<SchuelerStammdaten | null> {
		if (eintrag === null)
			return null;
		return await api.server.getSchuelerStammdaten(api.schema, eintrag.id);
	}

	/**
	 * Setzt den Schuljahresabschnitt und triggert damit das Laden der Defaults für diesen Abschnitt
	 *
	 * @param {number} idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 */
	public async setSchuljahresabschnitt(idSchuljahresabschnitt: number) {
		const listSchueler = await api.server.getSchuelerFuerAbschnitt(api.schema, idSchuljahresabschnitt);
		const listKlassen = await api.server.getKlassenFuerAbschnitt(api.schema, idSchuljahresabschnitt);
		const listKurse = await api.server.getKurseFuerAbschnitt(api.schema, idSchuljahresabschnitt);
		const listJahrgaenge = await api.server.getJahrgaenge(api.schema);
		const listAbiturjahrgaenge = api.schulform.daten.hatGymOb ? await api.server.getGostAbiturjahrgaenge(api.schema) : new ArrayList<GostJahrgang>();
		const schuelerListeManager = new SchuelerListeManager(listSchueler, listJahrgaenge, listKlassen, listKurse, api.schuleStammdaten.abschnitte, listAbiturjahrgaenge);
		schuelerListeManager.schuelerstatus.auswahlAdd(SchuelerStatus.AKTIV);
		schuelerListeManager.schuelerstatus.auswahlAdd(SchuelerStatus.EXTERN);
		const filtered = schuelerListeManager.filtered();
		const schuelerVorher = this._state.value.auswahl === null ? null : schuelerListeManager.schueler.get(this._state.value.auswahl.id);
		const auswahl = (schuelerVorher === null)
			? filtered.isEmpty() ? null : filtered.get(0)
			: schuelerVorher;
		const stammdaten = await this.ladeStammdaten(auswahl);
		const view = schuelerVorher === null ? routeSchuelerIndividualdaten : this._state.value.view;
		this.setPatchedDefaultState({
			idSchuljahresabschnitt,
			auswahl,
			schuelerListeManager,
			filtered,
			stammdaten,
			view
		});
	}

	/**
	 * Setzt den ausgewählten Schueler und lädt dessen Stammddaten.
	 *
	 * @param schueler   der ausgewählte Schüler
	 */
	public async setSchueler(schueler: SchuelerListeEintrag | null) {
		if (schueler?.id === this._state.value.auswahl?.id)
			return;
		if ((schueler === null) || (this.schuelerListeManager.schueler.list().isEmpty()))
			return;
		const auswahl = (this.schuelerListeManager.schueler.get(schueler.id) === null)
			? this._state.value.filtered.isEmpty() ? null : this._state.value.filtered.get(0)
			: schueler;
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

	get auswahl(): SchuelerListeEintrag | null {
		return this._state.value.auswahl;
	}

	get auswahlGruppe(): SchuelerListeEintrag[] {
		return this._state.value.auswahlGruppe;
	}

	get hatStammdaten(): boolean {
		return this._state.value.stammdaten !== null;
	}

	get stammdaten(): SchuelerStammdaten {
		if (this._state.value.stammdaten === null)
			throw new Error("Unerwarteter Fehler: Stammdaten nicht initialisiert");
		return this._state.value.stammdaten;
	}

	get schuelerListeManager(): SchuelerListeManager {
		return this._state.value.schuelerListeManager;
	}

	patch = (data : Partial<SchuelerStammdaten>) => {
		if (this.auswahl === null)
			return;
		api.server.patchSchuelerStammdaten(data, api.schema, this.auswahl.id).then(()=>{
			const stammdaten = this.stammdaten;
			this.setPatchedState({stammdaten: Object.assign(stammdaten, data)});
		}).catch((e) => console.log(e))
		// TODO Bei Anpassungen von nachname, vorname -> routeSchueler: Schülerliste aktualisieren...
	}

	gotoSchueler = async (value: SchuelerListeEintrag | null) => {
		if (value === null || value === undefined) {
			await RouteManager.doRoute({ name: routeSchueler.name, params: { } });
			return;
		}
		const redirect_name: string = (routeSchueler.selectedChild === undefined) ? routeSchuelerIndividualdaten.name : routeSchueler.selectedChild.name;
		await RouteManager.doRoute({ name: redirect_name, params: { id: value.id } });
	}

	setFilter = async () => this.commit();

	setAuswahlGruppe = (auswahlGruppe: SchuelerListeEintrag[]) =>	this.setPatchedState({auswahlGruppe});
}
