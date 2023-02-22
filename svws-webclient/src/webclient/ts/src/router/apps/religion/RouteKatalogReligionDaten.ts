import { ReligionEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { ReligionDatenProps } from "~/components/kataloge/religionen/daten/SReligionDatenProps";
import { routeLogin } from "~/router/RouteLogin";
import { RouteNode } from "~/router/RouteNode";
import { RouteKatalogReligion } from "../RouteKatalogReligion";

export class RouteDataKlassenDaten {
	item: ReligionEintrag | undefined = undefined;
	private _daten: ReligionEintrag | undefined = undefined;

	get daten(): ReligionEintrag {
		if (this._daten === undefined)
			throw new Error("Unerwarteter Fehler: Religionsdaten nicht initialisiert");
		return this._daten;
	}

	set daten(value: ReligionEintrag | undefined) {
		this._daten = value;
	}

	patch = async (data : Partial<ReligionEintrag>) => {
		if (this.item === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		await routeLogin.data.api.patchReligion(data, routeLogin.data.schema, this.item.id)
	}
}

const SReligionDaten = () => import("~/components/kataloge/religionen/daten/SReligionDaten.vue");

export class RouteKatalogReligionDaten extends RouteNode<RouteDataKlassenDaten, RouteKatalogReligion> {

	public constructor() {
		super("religionen_daten", "daten", SReligionDaten, new RouteDataKlassenDaten());
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
			await this.onSelect(this.parent.data.mapReligionen.get(id));
		}
	}

	protected async onSelect(item?: ReligionEintrag) {
		if (item === this.data.item)
			return;
		if (item === undefined) {
			this.data.item = undefined;
			this.data.daten = undefined;
		} else {
			this.data.item = item;
			this.data.daten = await routeLogin.data.api.getReligion(routeLogin.data.schema, item.id);
		}
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): ReligionDatenProps {
		return {
			patch: this.data.patch,
			data: this.data.daten
		};
	}

}

export const routeKatalogReligionDaten = new RouteKatalogReligionDaten();

