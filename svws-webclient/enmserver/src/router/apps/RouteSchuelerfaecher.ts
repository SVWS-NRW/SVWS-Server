import { type RouteApp, routeApp } from "@wenom/router/apps/RouteApp";
import { RouteNode } from "@wenom/router/RouteNode";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { ENMLehrerFunktion } from "@core/core/data/enm/ENMLehrerFunktion";
import { ServerMode } from "@core/core/types/ServerMode";
import type { EnmManager } from "@ui/components/enm/EnmManager";

const EnmSchuelerFaecher = () => import("@ui/components/enm/EnmSchuelerFaecher.vue");

export class RouteSchuelerfaecher extends RouteNode<any, RouteApp> {

	public constructor() {
		super(Schulform.values(), "schuelerfaecher", "schuelerfaecher", EnmSchuelerFaecher);
		super.mode = ServerMode.STABLE;
		super.propHandler = () => this.getProps();
		super.text = "Notenübersicht";
		this.isHidden = () => !this.istZugaenglich(routeApp.data.manager);
	}

	private istZugaenglich(manager: EnmManager): boolean {
		if ((manager.lehrerFunktion !== ENMLehrerFunktion.KLASSENLEITUNG) && (manager.lehrerFunktion !== ENMLehrerFunktion.ABTEILUNGSLEITUNG)) {
			return false;
		}
		for (const klasse of manager.listKlassenKlassenlehrer) {
			if (((manager.lehrerFunktion === ENMLehrerFunktion.KLASSENLEITUNG) && manager.klasseIstKlassenleitung(klasse.id))
				|| ((manager.lehrerFunktion === ENMLehrerFunktion.ABTEILUNGSLEITUNG) && manager.klasseIstAbteilungsleitung(klasse.id))) {
				return true;
			}
		}
		return false;
	}

	public getProps() {
		return { enmManager: () => routeApp.data.manager };
	}

}

export const routeSchuelerfaecher = new RouteSchuelerfaecher();
