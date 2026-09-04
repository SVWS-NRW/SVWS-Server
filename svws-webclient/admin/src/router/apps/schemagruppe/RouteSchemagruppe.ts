import type { SchemaAuswahlProps } from "@admin/components/schema/SSchemaAuswahlProps";
import type { SchemagruppeProps } from "@admin/components/schemagruppe/SSchemagruppeProps";
import { api } from "@admin/router/Api";
import { RouteNode } from "@admin/router/RouteNode";
import { ServerMode } from "@core/core/types/ServerMode";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { RouteApp } from "../RouteApp";
import { routeSchema } from "../schema/RouteSchema";


const SSchemaAuswahl = () => import("@admin/components/schema/SSchemaAuswahl.vue");
const SSchemagruppe = () => import("@admin/components/schemagruppe/SSchemagruppe.vue");


export class RouteSchemagruppe extends RouteNode<unknown, RouteApp> {

	public constructor() {
		super("schemagruppe", "/schemagruppe", SSchemagruppe);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schemagruppe";
		super.setView("liste", SSchemaAuswahl, (route) => this.getAuswahlProps(route));
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<void | Error | RouteLocationRaw> {
		if (routeSchema.data.auswahlGruppe.length === 0) {
			return routeSchema.getRoute();
		}
	}

	public getRoute(): RouteLocationRaw {
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
			backupSchemata: routeSchema.data.backupSchemata,
			backupFiles: () => routeSchema.data.backupFiles,
		};
	}

}

export const routeSchemagruppe = new RouteSchemagruppe();
