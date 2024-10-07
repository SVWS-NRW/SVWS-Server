import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "../RouteApp";
import { routeGost, type RouteGost } from "./RouteGost";
import type { GostAbiturjahrgangNeuProps } from "~/components/gost/SGostAbiturjahrgangNeuProps";
import { RouteType } from "~/router/RouteType";

const SGostAbiturjahrgangNeu = () => import("~/components/gost/SGostAbiturjahrgangNeu.vue");

export class RouteGostAbiturjahrNeu extends RouteNode<any, RouteGost> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN ], "gost.abiturjahrNeu", "neu", SGostAbiturjahrgangNeu);
		super.types = new Set([ RouteType.HINZUFUEGEN ]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Abiturjahr anlegen";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): GostAbiturjahrgangNeuProps {
		return {
			mapJahrgaengeOhneAbiJahrgang: () => routeGost.data.mapJahrgaengeOhneAbiJahrgang,
			addAbiturjahrgang: routeGost.data.addAbiturjahrgang,
			getAbiturjahrFuerJahrgang: routeGost.data.getAbiturjahrFuerJahrgang,
			cancelCreationMode: routeGost.data.cancelCreationMode,
		};
	}

}

export const routeGostAbiturjahrNeu = new RouteGostAbiturjahrNeu();
