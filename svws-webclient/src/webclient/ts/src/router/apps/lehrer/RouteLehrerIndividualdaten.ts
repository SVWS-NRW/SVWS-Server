import { RouteNode } from "~/router/RouteNode";
import { RouteLehrer, routeLehrer } from "~/router/apps/RouteLehrer";
import { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { routeApp } from "~/router/RouteApp";
import { LehrerListeEintrag, LehrerStammdaten } from "@svws-nrw/svws-core-ts";
import { routeLogin } from "~/router/RouteLogin";

const SLehrerIndividualdaten = () => import("~/components/lehrer/individualdaten/SLehrerIndividualdaten.vue");
export class RouteDataLehrerIndividualdaten {

	item: LehrerListeEintrag | undefined = undefined;

	patch = async (data : Partial<LehrerStammdaten>) => {
		if (this.item === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		await routeLogin.data.api.patchLehrerStammdaten(data, routeLogin.data.schema, this.item.id);
		// TODO Bei Anpassungen von nachname, vorname -> routeSchueler: Schülerliste aktualisieren...
	}

}

export class RouteLehrerIndividualdaten extends RouteNode<RouteDataLehrerIndividualdaten, RouteLehrer> {

	public constructor() {
		super("lehrer_daten", "daten", SLehrerIndividualdaten, new RouteDataLehrerIndividualdaten());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Daten";
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			patch: this.data.patch,
			stammdaten: routeLehrer.data.stammdaten,
			orte: routeApp.data.orte,
			ortsteile: routeApp.data.ortsteile
		};
	}

}

export const routeLehrerIndividualdaten = new RouteLehrerIndividualdaten();

