import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { BenutzerprofilDatenProps } from "~/components/benutzerprofil/daten/SBenutzerProfilDatenProps";
import type { RouteBenutzerprofil} from "../RouteBenutzerprofil";
import { routeBenutzerprofil } from "../RouteBenutzerprofil";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";

const SBenutzerprofilDaten = () => import("~/components/benutzerprofil/daten/SBenutzerprofilDaten.vue");

export class RouteBenutzerprofilDaten extends RouteNode<unknown, RouteBenutzerprofil> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "benutzerprofil.daten", "daten", SBenutzerprofilDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Daten";
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name };
	}

	public getProps(to: RouteLocationNormalized): BenutzerprofilDatenProps {
		return {
			benutzer: routeBenutzerprofil.data.benutzer,
			patch: routeBenutzerprofil.data.patch,
			patchPasswort: routeBenutzerprofil.data.patchPasswort,
		};
	}

}

export const routeBenutzerprofilDaten = new RouteBenutzerprofilDaten();

