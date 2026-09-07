import type { RouteLocationNormalized, RouteParamsRawGeneric } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

import type { GostAbiturjahrgangNeuProps } from "~/components/gost/SGostAbiturjahrgangNeuProps";
import { RouteNode } from "~/router/RouteNode";

import { type RouteGost, routeGost } from "./RouteGost";

const SGostAbiturjahrgangNeu = () => import("~/components/gost/SGostAbiturjahrgangNeu.vue");

export class RouteGostAbiturjahrNeu extends RouteNode<any, RouteGost> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.OBERSTUFE_ABITURJAHRGAENGE_VERWALTEN], "gost.abiturjahrNeu", "neu", SGostAbiturjahrgangNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Abiturjahr anlegen";
	}

	public addRouteParamsFromState(): RouteParamsRawGeneric {
		return { abiturjahr: "" };
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
