import type { RouteLocationNormalizedGeneric } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import { type RouteKlassen } from "~/router/apps/klassen/RouteKlassen";
import { RouteNode } from "~/router/RouteNode";

const KlassenDaten = () => import("~/components/klassen/daten/KlassenDaten.vue");

export class RouteKlassenDaten extends RouteNode<any, RouteKlassen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN], "klassen.daten", "daten", KlassenDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => <RouteLocationNormalizedGeneric>{};
		super.text = "Klasse";
	}

}

export const routeKlassenDaten = new RouteKlassenDaten();
