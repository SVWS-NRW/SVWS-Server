import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import type { SchuelerVermerkartZusammenfassung} from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeKatalogVermerkarten, type RouteKatalogVermerkarten } from "~/router/apps/kataloge/vermerke/RouteKatalogVermerkarten";

import type { VermerkartDatenProps } from "~/components/kataloge/vermerke/daten/SVermerkartDatenProps";
import { routeApp } from "../../RouteApp";
import { RouteManager } from "~/router/RouteManager";
import { routeSchuelerVermerke } from "../../schueler/vermerke/RouteSchuelerVermerke";

const SVermerkDaten = () => import("~/components/kataloge/vermerke/daten/SVermerkartDaten.vue");

export class RouteKatalogVermerkartenDaten extends RouteNode<any, RouteKatalogVermerkarten> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kataloge.vermerke.daten", "daten", SVermerkDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Vermerkart";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {

		if (routeKatalogVermerkarten.data.vermerkartenManager.auswahl() === undefined)
			return routeKatalogVermerkarten.getRoute(undefined)
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: id }};
	}

	gotoSchueler = async (schuelerVermerkartZusammenfassung: SchuelerVermerkartZusammenfassung) => {
		await RouteManager.doRoute(routeSchuelerVermerke.getRoute(schuelerVermerkartZusammenfassung.id));
	}

	public getProps(to: RouteLocationNormalized): VermerkartDatenProps {
		return {
			patch: routeKatalogVermerkarten.data.patch,
			vermerkartenManager: () => routeKatalogVermerkarten.data.vermerkartenManager,
			gotoSchueler: this.gotoSchueler,
		};
	}

}

export const routeKatalogVermerkartenDaten = new RouteKatalogVermerkartenDaten();

