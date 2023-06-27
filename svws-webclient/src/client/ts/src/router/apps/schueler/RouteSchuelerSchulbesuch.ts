import type { SchuelerSchulbesuchsdaten} from "@core";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { shallowRef} from "vue";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { SchuelerSchulbesuchProps } from "~/components/schueler/schulbesuch/SSchuelerSchulbesuchProps";
import { api } from "~/router/Api";
import type { RouteSchueler } from "~/router/apps/RouteSchueler";
import { RouteNode } from "~/router/RouteNode";

const SSchuelerSchulbesuch = () => import("~/components/schueler/schulbesuch/SSchuelerSchulbesuch.vue");

interface RouteStateDataSchuelerSchulbesuch {
	daten: SchuelerSchulbesuchsdaten | undefined;
	idSchueler: number | undefined;
}

class RouteDataSchuelerSchulbesuch {

	private static _defaultState: RouteStateDataSchuelerSchulbesuch = {
		daten: undefined,
		idSchueler: undefined,
	}

	private _state = shallowRef(RouteDataSchuelerSchulbesuch._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateDataSchuelerSchulbesuch>) {
		this._state.value = Object.assign({ ... RouteDataSchuelerSchulbesuch._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateDataSchuelerSchulbesuch>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	get daten(): SchuelerSchulbesuchsdaten {
		if (this._state.value.daten === undefined)
			throw new Error("Beim Zugriff auf die Daten sind noch keine gültigen Daten geladen.");
		return this._state.value.daten;
	}

	public async setEintrag(idSchueler?: number) {
		if (idSchueler === undefined || idSchueler === this._state.value.idSchueler)
			return;
		const daten = await api.server.getSchuelerSchulbesuch(api.schema, idSchueler);
		this.setPatchedState({idSchueler, daten});
	}

	patch = async (data : Partial<SchuelerSchulbesuchsdaten>) => {
		await api.server.patchSchuelerSchulbesuch(data, api.schema, this.daten.id);
	}
}

export class RouteSchuelerSchulbesuch extends RouteNode<RouteDataSchuelerSchulbesuch, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schueler.schulbesuch", "schulbesuch", SSchuelerSchulbesuch, new RouteDataSchuelerSchulbesuch());
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schulbesuch";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (to_params.id !== undefined) {
			const id = parseInt(to_params.id);
			await this.data.setEintrag(id);
		}
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): SchuelerSchulbesuchProps {
		return {
			data: this.data.daten,
			patch: this.data.patch
		};
	}

}

export const routeSchuelerSchulbesuch = new RouteSchuelerSchulbesuch();

