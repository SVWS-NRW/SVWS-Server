import { RouteNode } from "~/router/RouteNode";
import { routeSchueler, type RouteSchueler } from "~/router/apps/RouteSchueler";
import { routeSchuelerStundenplanDaten } from "~/router/apps/schueler/stundenplan/RouteSchuelerStundenplanDaten";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { BenutzerKompetenz, Schulform } from "@core";
import { RouteDataSchuelerStundenplan } from "./stundenplan/RouteDataSchuelerStundenplan";
import type { SchuelerStundenplanAuswahlProps } from "~/components/schueler/stundenplan/SSchuelerStundenplanAuswahlProps";

const SSchuelerStundenplan = () => import("~/components/schueler/stundenplan/SSchuelerStundenplan.vue");

export class RouteSchuelerStundenplan extends RouteNode<RouteDataSchuelerStundenplan, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schueler.stundenplan", "stundenplan", SSchuelerStundenplan, new RouteDataSchuelerStundenplan());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Stundenplan";
		super.children = [
			routeSchuelerStundenplanDaten
		];
		super.defaultChild = routeSchuelerStundenplanDaten;
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<any> {
		if (to_params.id instanceof Array || to_params.idStundenplan instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const idSchueler = to_params.id === undefined ? undefined : parseInt(to_params.id);
		if (idSchueler === undefined)
			return routeSchueler.getRoute();
		if (to.name === this.name) {
			await routeSchuelerStundenplan.data.ladeListe();
			if (routeSchuelerStundenplan.data.mapStundenplaene.size !== 0) {
				const stundenplan = routeSchuelerStundenplan.data.mapStundenplaene.entries().next().value;
				if (stundenplan !== undefined)
					return routeSchuelerStundenplanDaten.getRoute(idSchueler, stundenplan.id);
			}
		}
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): SchuelerStundenplanAuswahlProps {
		return {
			stundenplan: this.data.mapStundenplaene.size === 0 ? undefined : this.data.auswahl,
			mapStundenplaene: this.data.mapStundenplaene,
			gotoStundenplan: this.data.gotoStundenplan,
		};
	}

}

export const routeSchuelerStundenplan = new RouteSchuelerStundenplan();

