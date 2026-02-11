import type { RouteLocationNormalized } from "vue-router";
import type { VermerkartenDatenProps } from "~/components/schule/kataloge/vermerkarten/daten/VermerkartenDatenProps";
import type { SchuelerVermerkartZusammenfassung } from "@core";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { routeVermerkarten, type RouteVermerkarten } from "~/router/apps/schule/kataloge/vermerkarten/RouteVermerkarten";
import { RouteManager } from "~/router/RouteManager";
import { routeSchuelerVermerke } from "../../../schueler/vermerke/RouteSchuelerVermerke";
import { api } from "~/router/Api";

const VermerkartenDaten = () => import("~/components/schule/kataloge/vermerkarten/daten/VermerkartenDaten.vue");

export class RouteVermerkartenDaten extends RouteNode<any, RouteVermerkarten> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN],
			"schule.vermerke.daten", "daten", VermerkartenDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Vermerkart";
	}

	gotoSchueler = async (schuelerVermerkartZusammenfassung: SchuelerVermerkartZusammenfassung) => {
		await RouteManager.doRoute(routeSchuelerVermerke.getRoute({ id: schuelerVermerkartZusammenfassung.id }));
	};

	public getProps(to: RouteLocationNormalized): VermerkartenDatenProps {
		return {
			patch: routeVermerkarten.data.patch,
			manager: () => routeVermerkarten.data.manager,
			gotoSchueler: this.gotoSchueler,
			benutzerKompetenzen: api.benutzerKompetenzen,
		};
	}

}

export const routeVermerkartenDaten = new RouteVermerkartenDaten();

