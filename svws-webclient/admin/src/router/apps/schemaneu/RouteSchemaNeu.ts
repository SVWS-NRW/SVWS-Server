import type { SchemaAuswahlProps } from "@admin/components/schema/SSchemaAuswahlProps";
import type { SchemaNeuProps } from "@admin/components/schemaneu/SSchemaNeuProps";
import { api } from "@admin/router/Api";
import { RouteNode } from "@admin/router/RouteNode";
import { ServerMode } from "@core/core/types/ServerMode";
import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { RouteApp } from "../RouteApp";
import { routeSchema } from "../schema/RouteSchema";

const SSchemaAuswahl = () => import("@admin/components/schema/SSchemaAuswahl.vue");
const SSchemaNeu = () => import("@admin/components/schemaneu/SSchemaNeu.vue");


export class RouteSchemaNeu extends RouteNode<unknown, RouteApp> {

	public constructor() {
		super("schemaneu", "/schemaneu", SSchemaNeu);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "SchemaNeu";
		super.setView("liste", SSchemaAuswahl, (route) => this.getAuswahlProps(route));
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name };
	}

	public getAuswahlProps(to: RouteLocationNormalized): SchemaAuswahlProps {
		return routeSchema.getAuswahlProps(to);
	}

	public getProps(to: RouteLocationNormalized): SchemaNeuProps {
		return {
			apiStatus: api.status,
			apiUsername: api.username,
			migrationQuellinformationen: () => routeSchema.data.migrationQuellinformationen.value,
			addSchema: routeSchema.data.addSchema,
			importSchema: routeSchema.data.importSchema,
			migrateSchema: routeSchema.data.migrateSchema,
			duplicateSchema: routeSchema.data.duplicateSchema,
			schema: routeSchema.data.auswahl?.name,
		};
	}

}

export const routeSchemaNeu = new RouteSchemaNeu();
