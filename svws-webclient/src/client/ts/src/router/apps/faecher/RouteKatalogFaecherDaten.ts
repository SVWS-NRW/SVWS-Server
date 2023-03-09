import { BenutzerKompetenz, FachDaten, FaecherListeEintrag, Schulform } from "@svws-nrw/svws-core";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { FachDatenProps } from "~/components/kataloge/faecher/daten/SFachDatenProps";
import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { RouteKatalogFaecher } from "../RouteKatalogFaecher";

export class RouteDataKatalogFaecherDaten {
	item: FaecherListeEintrag | undefined = undefined;
	private _daten: FachDaten | undefined = undefined;

	get daten(): FachDaten {
		if (this._daten === undefined)
			throw new Error("Unerwarteter Fehler: Fachdaten nicht initialisiert");
		return this._daten;
	}

	set daten(value: FachDaten | undefined) {
		this._daten = value;
	}

	patch = async (data : Partial<FachDaten>) => {
		if (this.item === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		console.log("TODO: Implementierung patchFachdatenDaten", data);
		//await api.server.patchFachdatenDaten(data, api.schema, this.item.id);
	}
}

const SFachDaten = () => import("~/components/kataloge/faecher/daten/SFachDaten.vue");

export class RouteKatalogFaecherDaten extends RouteNode<RouteDataKatalogFaecherDaten, RouteKatalogFaecher> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "faecher_daten", "daten", SFachDaten, new RouteDataKatalogFaecherDaten());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Daten";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (this.parent === undefined)
			throw new Error("Fehler: Die Route ist ungültig - Parent ist nicht definiert");
		if (to_params.id === undefined) {
			await this.onSelect(undefined);
		} else {
			const id = parseInt(to_params.id);
			await this.onSelect(this.parent.data.mapFaecher.get(id));
		}
	}

	protected async onSelect(item?: FaecherListeEintrag) {
		if (item === this.data.item)
			return;
		if (item === undefined) {
			this.data.item = undefined;
			this.data.daten = undefined;
		} else {
			this.data.item = item;
			this.data.daten = await api.server.getFach(api.schema, item.id);
		}
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): FachDatenProps {
		return {
			patch: this.data.patch,
			data: this.data.daten
		};
	}

}

export const routeKatalogFaecherDaten = new RouteKatalogFaecherDaten();

