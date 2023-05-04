import type { List, SchulenKatalogEintrag} from "@svws-nrw/svws-core";
import type { RouteLocationRaw } from "vue-router";
import type { Ref} from "vue";
import type { InitSchulkatalogProps } from "~/components/init/SInitSchulkatalogProps";
import type { RouteInit } from "../RouteInit";
import { ArrayList, BenutzerKompetenz, Schulform } from "@svws-nrw/svws-core";
import { RouteNode } from "~/router/RouteNode";
import { api } from "../Api";
import { ref } from "vue";
import { RouteManager } from "../RouteManager";
import { routeApp } from "../RouteApp";

const SInitSchulkatalog = () => import("~/components/init/SInitSchulkatalog.vue")

export class RouteInitSchulkatalog extends RouteNode<unknown, RouteInit> {

	protected listSchulkatalog: Ref<List<SchulenKatalogEintrag>> = ref(new ArrayList());

	protected initSchule = async (schule: SchulenKatalogEintrag): Promise<void> => {
		try {
			await api.server.initSchule(api.schema, Number(schule.SchulNr));
			await RouteManager.doRoute(routeApp.getRoute());
		} catch(error) {
			console.warn(`Das Initialiseren des Schemas mit der Schulnummer ${schule.SchulNr} ist fehlgeschlagen.`);
		}
	}

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.ADMIN ], "init.schulkatalog", "/init/schulkatalog", SInitSchulkatalog);
		super.propHandler = (route) => this.getProps();
		super.text = "Initialisierung mit Schulkatalog";
	}

	public async enter() {
		this.listSchulkatalog.value = await api.server.getKatalogSchulen(api.schema);
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name };
	}

	public getProps(): InitSchulkatalogProps {
		return {
			listSchulkatalog: this.listSchulkatalog.value,
			initSchule: this.initSchule,
		}
	}

}

export const routeInitSchulkatalog = new RouteInitSchulkatalog();
