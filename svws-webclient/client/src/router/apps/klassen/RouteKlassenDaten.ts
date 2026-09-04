import type { RouteLocationNormalized } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { routeKlassen, type RouteKlassen } from "~/router/apps/klassen/RouteKlassen";
import type { KlassenDatenProps } from "~/components/klassen/daten/KlassenDatenProps";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";

const KlassenDaten = () => import("~/components/klassen/daten/KlassenDaten.vue");

export class RouteKlassenDaten extends RouteNode<any, RouteKlassen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN], "klassen.daten", "daten", KlassenDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Klasse";
	}

	public getProps(to: RouteLocationNormalized): KlassenDatenProps {
		return {
			patch: routeKlassen.data.patch,
			manager: () => routeKlassen.data.manager,
			setFilter: routeKlassen.data.setFilter,
			gotoSchueler: routeKlassen.data.gotoSchueler,
			gotoLehrer: routeKlassen.data.gotoLehrer,
			addKlassenleitung: routeKlassen.data.addKlassenleitung,
			removeKlassenleitung: routeKlassen.data.removeKlassenleitung,
			updateReihenfolgeKlassenleitung: routeKlassen.data.updateReihenfolgeKlassenleitung,
		};
	}

}

export const routeKlassenDaten = new RouteKlassenDaten();
