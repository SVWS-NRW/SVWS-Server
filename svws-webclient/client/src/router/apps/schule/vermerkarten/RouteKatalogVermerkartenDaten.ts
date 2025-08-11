import type { RouteLocationNormalized } from "vue-router";
import type { VermerkartDatenProps } from "~/components/schule/kataloge/vermerke/daten/SVermerkartDatenProps";
import type { SchuelerVermerkartZusammenfassung } from "@core";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { routeKatalogVermerkarten, type RouteKatalogVermerkarten } from "~/router/apps/schule/vermerkarten/RouteKatalogVermerkarten";
import { RouteManager } from "~/router/RouteManager";
import { routeSchuelerVermerke } from "../../schueler/vermerke/RouteSchuelerVermerke";
import { api } from "~/router/Api";

const SVermerkDaten = () => import("~/components/schule/kataloge/vermerke/daten/SVermerkartDaten.vue");

export class RouteKatalogVermerkartenDaten extends RouteNode<any, RouteKatalogVermerkarten> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN ], "schule.vermerke.daten", "daten", SVermerkDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Vermerkart";
	}

	gotoSchueler = async (schuelerVermerkartZusammenfassung: SchuelerVermerkartZusammenfassung) => {
		await RouteManager.doRoute(routeSchuelerVermerke.getRoute({ id: schuelerVermerkartZusammenfassung.id }));
	}

	public getProps(to: RouteLocationNormalized): VermerkartDatenProps {
		return {
			patch: routeKatalogVermerkarten.data.patch,
			vermerkartenManager: () => routeKatalogVermerkarten.data.manager,
			gotoSchueler: this.gotoSchueler,
			benutzerKompetenzen: api.benutzerKompetenzen,
		};
	}

}

export const routeKatalogVermerkartenDaten = new RouteKatalogVermerkartenDaten();

