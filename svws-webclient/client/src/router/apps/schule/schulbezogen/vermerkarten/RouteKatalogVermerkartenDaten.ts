import type { RouteLocationNormalized } from "vue-router";
import type { VermerkartenDatenProps } from "~/components/schule/schulbezogen/vermerkarten/daten/SVermerkartenDatenProps";
import type { SchuelerVermerkartZusammenfassung } from "@core";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { routeKatalogVermerkarten, type RouteKatalogVermerkarten } from "~/router/apps/schule/schulbezogen/vermerkarten/RouteKatalogVermerkarten";
import { RouteManager } from "~/router/RouteManager";
import { routeSchuelerVermerke } from "../../../schueler/vermerke/RouteSchuelerVermerke";
import { api } from "~/router/Api";

const SVermerkartenDaten = () => import("~/components/schule/schulbezogen/vermerkarten/daten/SVermerkartenDaten.vue");

export class RouteKatalogVermerkartenDaten extends RouteNode<any, RouteKatalogVermerkarten> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN ], "schule.vermerke.daten", "daten", SVermerkartenDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Vermerkart";
	}

	gotoSchueler = async (schuelerVermerkartZusammenfassung: SchuelerVermerkartZusammenfassung) => {
		await RouteManager.doRoute(routeSchuelerVermerke.getRoute({ id: schuelerVermerkartZusammenfassung.id }));
	}

	public getProps(to: RouteLocationNormalized): VermerkartenDatenProps {
		return {
			patch: routeKatalogVermerkarten.data.patch,
			vermerkartenManager: () => routeKatalogVermerkarten.data.manager,
			gotoSchueler: this.gotoSchueler,
			benutzerKompetenzen: api.benutzerKompetenzen,
		};
	}

}

export const routeKatalogVermerkartenDaten = new RouteKatalogVermerkartenDaten();

