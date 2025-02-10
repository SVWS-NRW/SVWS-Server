import type { RouteParams } from "vue-router";
import { BenutzerKompetenz, type ReligionListeManager, Schulform, ServerMode } from "@core";
import type { RouteNode } from "~/router/RouteNode";
import type { RouteApp } from "~/router/apps/RouteApp";
import { routeKatalogReligionDaten } from "~/router/apps/schule/religionen/RouteKatalogReligionDaten";
import { RouteDataKatalogReligionen } from "./RouteDataKatalogReligionen";
import { RouteSchuleMenuGroup } from "../RouteSchuleMenuGroup";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { routeKatalogReligionGruppenprozesse } from "~/router/apps/schule/religionen/RouteKatalogReligionGruppenprozesse";
import { routeKatalogReligionNeu } from "~/router/apps/schule/religionen/RouteKatalogReligionNeu";
import { routeApp} from "~/router/apps/RouteApp";
import type { KatalogReligionAuswahlProps } from "~/components/schule/kataloge/religionen/SReligionenAuswahlPops";

const SReligionenAuswahl = () => import("~/components/schule/kataloge/religionen/SReligionenAuswahl.vue");
const SReligionenApp = () => import("~/components/schule/kataloge/religionen/SReligionenApp.vue");

export class RouteKatalogReligionen extends RouteAuswahlNode<ReligionListeManager, RouteDataKatalogReligionen, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN ], "schule.religionen", "schule/religion/:id(\\d+)?", SReligionenApp, SReligionenAuswahl, new RouteDataKatalogReligionen());
		super.mode = ServerMode.STABLE;
		super.text = "Religionen";
		super.menugroup = RouteSchuleMenuGroup.ALLGEMEIN;
		super.children = [
			routeKatalogReligionDaten,
			routeKatalogReligionGruppenprozesse,
			routeKatalogReligionNeu,
		];
		super.defaultChild = routeKatalogReligionDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
		super.getAuswahlListProps = (props) => (<KatalogReligionAuswahlProps> {
			...props,
			schuljahresabschnittsauswahl: () => routeApp.data.getSchuljahresabschnittsauswahl(false),
		})
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten())
			return;
		return this.getRouteSelectedChild();
	};
}

export const routeKatalogReligionen = new RouteKatalogReligionen();
