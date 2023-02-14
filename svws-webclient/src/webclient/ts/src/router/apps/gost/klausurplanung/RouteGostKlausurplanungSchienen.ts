import { RouteNode } from "~/router/RouteNode";
import { routeGostKlausurplanung, RouteGostKlausurplanung } from "../RouteGostKlausurplanung";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { GostHalbjahr, GostKursklausurManager } from "@svws-nrw/svws-core-ts";
import { routeLogin } from "~/router/RouteLogin";
import { ShallowRef, shallowRef } from "vue";
import { routeGost } from "../../RouteGost";

export class RouteDataGostKlausurplanungSchienen {

	_manager: GostKursklausurManager | undefined = undefined;

	get manager() : ShallowRef<GostKursklausurManager> {
		if (this._manager === undefined)
			throw new Error("Manager wurde noch nicht geladen. Ein Zugriff ist daher nicht möglich");
		return shallowRef(this._manager);
	}

}

const SGostKlausurplanungSchienen = () => import("~/components/gost/klausurplanung/SGostKlausurplanungSchienen.vue");

export class RouteGostKlausurplanungSchienen extends RouteNode<RouteDataGostKlausurplanungSchienen, RouteGostKlausurplanung> {

	public constructor() {
		super("gost.klausurplanung.schienen", "schienen", SGostKlausurplanungSchienen, new RouteDataGostKlausurplanungSchienen());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schienen";
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<void> {
		// Prüfe nochmals Abiturjahrgang, Halbjahr und ID der Blockung
		if (to_params.abiturjahr instanceof Array || to_params.halbjahr instanceof Array)
			throw new Error("Fehler: Die Parameter dürfen keine Arrays sein");
		const abiturjahr = to_params.abiturjahr === undefined ? undefined : parseInt(to_params.abiturjahr);
		const halbjahr = (to_params.halbjahr === undefined) ? undefined : GostHalbjahr.fromID(parseInt(to_params.halbjahr)) || undefined;
		if ((abiturjahr === undefined) || (halbjahr === undefined))
			throw new Error("Fehler: Abiturjahr und Halbjahr müssen als Parameter der Route an dieser Stelle vorhanden sein.");
		const listKursklausuren = await routeLogin.data.api.getGostKlausurenKursklausurenJahrgangHalbjahr(routeLogin.data.schema, abiturjahr, halbjahr.id);
		const listKlausurtermine = await routeLogin.data.api.getGostKlausurenKlausurtermineJahrgangHalbjahr(routeLogin.data.schema, abiturjahr, halbjahr.id);
		this.data._manager = new GostKursklausurManager(listKursklausuren, listKlausurtermine);
	}

	public getRoute(abiturjahr: number, halbjahr: number) : RouteLocationRaw {
		return { name: this.name, params: { abiturjahr: abiturjahr, halbjahr: halbjahr }};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			faecherManager: routeGost.data.faecherManager.value,
			manager: this.data.manager
		}
	}

}

export const routeGostKlausurplanungSchienen = new RouteGostKlausurplanungSchienen();


