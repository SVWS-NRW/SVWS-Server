import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { RouteNode } from "~/router/RouteNode";
import type { RouteApp } from "~/router/apps/RouteApp";

import { routeSchema } from "../schema/RouteSchema";

import type { SchemagruppeProps } from "~/components/schemagruppe/SSchemagruppeProps";
import type { SchemaAuswahlProps } from "~/components/schema/SSchemaAuswahlProps";
import { api } from "~/router/Api";
import { ServerMode } from "../../../../../core/src/core/types/ServerMode";

const SSchemaAuswahl = () => import("~/components/schema/SSchemaAuswahl.vue")
const SSchemagruppe = () => import("~/components/schemagruppe/SSchemagruppe.vue")


export class RouteSchemagruppe extends RouteNode<unknown, RouteApp> {

	public constructor() {
		super("schemagruppe", "/schemagruppe", SSchemagruppe);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schemagruppe";
		super.setView("liste", SSchemaAuswahl, (route) => this.getAuswahlProps(route));
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (routeSchema.data.auswahlGruppe.length === 0)
			return routeSchema.getRoute();
	}

	public getRoute() : RouteLocationRaw {
		return { name: this.name };
	}

	public getAuswahlProps(to: RouteLocationNormalized): SchemaAuswahlProps {
		return routeSchema.getAuswahlProps(to);
	}

	public getProps(to: RouteLocationNormalized): SchemagruppeProps {
		return {
			apiStatus: api.status,
			apiUsername: api.username,
			auswahlGruppe: routeSchema.data.auswahlGruppe,
			removeSchemata: routeSchema.data.removeSchemata,
		};
	}

}

export const routeSchemagruppe = new RouteSchemagruppe();
