import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { RouteSchueler } from "~/router/apps/RouteSchueler";
import { SchuelerSchulbesuchsdaten } from "@svws-nrw/svws-core-ts";
import { Ref, ref } from "vue";
import { routeLogin } from "~/router/RouteLogin";
import { SchuelerSchulbesuchProps } from "~/components/schueler/schulbesuch/SSchuelerSchulbesuchProps";

const SSchuelerSchulbesuch = () => import("~/components/schueler/schulbesuch/SSchuelerSchulbesuch.vue");

class RouteDataSchuelerSchulbesuch {

	_daten: Ref<SchuelerSchulbesuchsdaten | undefined>;

	public constructor() {
		this._daten = ref(undefined);
	}

	public get daten(): SchuelerSchulbesuchsdaten {
		if (this._daten.value === undefined)
			throw new Error("Beim Zugriff auf die Daten sind noch keine gültigen Daten geladen.");
		return this._daten.value;
	}

	public get visible(): boolean {
		return !(routeSchuelerSchulbesuch.hidden()) && (this._daten.value !== undefined);
	}

	public async onSelect(id?: number) {
		if (((id === undefined) && (this._daten.value === undefined)) || ((this._daten.value !== undefined) && (this.daten.id === id)))
			return;
		this._daten.value = (id === undefined) ? undefined : await routeLogin.data.api.getSchuelerSchulbesuch(routeLogin.data.schema, id);
	}

	patch = async (data : Partial<SchuelerSchulbesuchsdaten>) => {
		if (this._daten.value === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		await routeLogin.data.api.patchSchuelerSchulbesuch(data, routeLogin.data.schema, this.daten.id);
	}
}

export class RouteSchuelerSchulbesuch extends RouteNode<RouteDataSchuelerSchulbesuch, RouteSchueler> {

	public constructor() {
		super("schueler_schulbesuch", "schulbesuch", SSchuelerSchulbesuch, new RouteDataSchuelerSchulbesuch());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schulbesuch";
	}


	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (this.parent === undefined)
			throw new Error("Fehler: Die Route ist ungültig - Parent ist nicht definiert");
		if (to_params.id === undefined) {
			await this.data.onSelect(undefined);
		} else {
			const id = parseInt(to_params.id);
			await this.data.onSelect(this.parent.data.mapSchueler.get(id)?.id);
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

