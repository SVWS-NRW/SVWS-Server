import type { RouteLocationNormalized } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { routeKurse, type RouteKurse } from "~/router/apps/kurse/RouteKurse";
import type { KursDatenProps } from "~/components/kurse/daten/SKursDatenProps";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";

const SKursDaten = () => import("~/components/kurse/daten/SKursDaten.vue");

export class RouteKursDaten extends RouteNode<any, RouteKurse> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN], "kurse.daten", "daten", SKursDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Kurs";
	}

	public getProps(to: RouteLocationNormalized): KursDatenProps {
		return {
			patch: routeKurse.data.patch,
			manager: () => routeKurse.data.manager,
			setFilter: routeKurse.data.setFilter,
			gotoSchueler: routeKurse.data.gotoSchueler,
			addKursLehrer: routeKurse.data.addKurLehrer,
			patchKursLehrer: routeKurse.data.patchKursLehrer,
			deleteKursLehrer: routeKurse.data.deleteKursLehrer,
		};
	}

}

export const routeKursDaten = new RouteKursDaten();

