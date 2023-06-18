import { BenutzerKompetenz, Schulform } from "@core";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { SchuelerStundenplanDatenProps } from "~/components/schueler/stundenplan/SSchuelerStundenplanDatenProps";
import { routeSchueler } from "~/router/apps/RouteSchueler";
import { routeSchuelerStundenplan, type RouteSchuelerStundenplan } from "~/router/apps/schueler/RouteSchuelerStundenplan";
import { RouteNode } from "~/router/RouteNode";

const SSchuelerStundenplanDaten = () => import("~/components/schueler/stundenplan/SSchuelerStundenplanDaten.vue");

export class RouteSchuelerStundenplanDaten extends RouteNode<unknown, RouteSchuelerStundenplan> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schueler.stundenplan.daten", ":idStundenplan(\\d+)?", SSchuelerStundenplanDaten);
		super.propHandler = (route) => this.getProps(route);
		super.text = "Stundenplan";
		super.children = [
		];
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<any> {
		if (to_params.id instanceof Array || to_params.idStundenplan instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const idSchueler = to_params.id === undefined ? undefined : parseInt(to_params.id);
		if (idSchueler === undefined)
			return routeSchueler.getRoute();
		if (to_params.idStundenplan === undefined) {
			if (routeSchuelerStundenplan.data.mapStundenplaene.size === 0)
				return false;
			return { name: this.name, params: { id: to_params.id, idStundenplan: routeSchuelerStundenplan.data.auswahl.id }};
		}
		const idStundenplan = parseInt(to_params.idStundenplan);
		try {
			await routeSchuelerStundenplan.data.setEintrag(idSchueler, idStundenplan);
		} catch (e) {
			console.log("Kein Stundenplan für diesen Schüler gefunden.")
			return routeSchueler.getRoute(idSchueler);
		}
	}

	public getRoute(id: number, idStundenplan: number) : RouteLocationRaw {
		return { name: this.name, params: { id, idStundenplan }};
	}

	public getProps(to: RouteLocationNormalized): SchuelerStundenplanDatenProps {
		return {
			manager: routeSchuelerStundenplan.data.manager,
		};
	}

}

export const routeSchuelerStundenplanDaten = new RouteSchuelerStundenplanDaten();

