import { BenutzerKompetenz, Schulform } from "@svws-nrw/svws-core";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { KlassenDatenProps } from "~/components/klassen/daten/SKlassenDatenProps";
import { RouteNode } from "~/router/RouteNode";
import type { RouteKlassen} from "../RouteKlassen";
import { routeKlassen } from "../RouteKlassen";

const SKlassenDaten = () => import("~/components/klassen/daten/SKlassenDaten.vue");

export class RouteKlasseDaten extends RouteNode<unknown, RouteKlassen> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "klassen.daten", "daten", SKlassenDaten);
		super.propHandler = (route) => this.getProps(route);
		super.text = "Daten";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
		if (routeKlassen.data.auswahl === undefined)
			return routeKlassen.getRoute(undefined)
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id }};
	}

	public getProps(to: RouteLocationNormalized): KlassenDatenProps {
		return {
			patch: routeKlassen.data.patch,
			data: routeKlassen.data.daten,
			mapLehrer: routeKlassen.data.mapLehrer,
			mapJahrgaenge: routeKlassen.data.mapJahrgaenge,
			gotoSchueler: routeKlassen.data.gotoSchueler,
		};
	}

}

export const routeKlasseDaten = new RouteKlasseDaten();

