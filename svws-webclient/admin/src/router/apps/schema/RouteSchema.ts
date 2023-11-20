import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";
import { routeError } from "~/router/error/RouteError";
import type { RouteApp } from "~/router/apps/RouteApp";
import { routeSchemaUebersicht } from "~/router/apps/schema/uebersicht/RouteSchemaUebersicht";

import { RouteDataSchema } from "~/router/apps/schema/RouteDataSchema";

import type { AuswahlChildData } from "~/components/AuswahlChildData";
import type { SchemaAppProps } from "~/components/schema/SSchemaAppProps";
import type { SchemaAuswahlProps } from "~/components/schema/SSchemaAuswahlProps";
import { api } from "~/router/Api";

const SSchemaAuswahl = () => import("~/components/schema/SSchemaAuswahl.vue")
const SSchemaApp = () => import("~/components/schema/SSchemaApp.vue")


export class RouteSchema extends RouteNode<RouteDataSchema, RouteApp> {

	public constructor() {
		super("schema", "/schema/:schema?", SSchemaApp, new RouteDataSchema());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schema";
		super.setView("liste", SSchemaAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeSchemaUebersicht,
		];
		super.defaultChild = routeSchemaUebersicht;
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.schema instanceof Array)
			return routeError.getRoute(new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein"));
		// Prüfe, ob bereits ein Schema ausgewählt wurde. Wenn nicht, dann lade die Liste vom Server und wähle ein Default-Schema aus
		const auswahl = this.data.auswahl;
		if (auswahl === undefined) {
			await this.data.init(to_params.schema);
			// Prüfe, ob ein Schema in der neu geladenen Liste vorhanden ist
			if (this.data.auswahl !== undefined)
				return this.getChildRoute(this.data.auswahl.name);
			return;
		}
		await this.data.setSchema(this.data.mapSchema.get(to_params.schema));
		if (to.name === this.name)
			return this.getChildRoute(to_params.schema);
		if (!to.name.startsWith(this.data.view.name))
			for (const child of this.children)
				if (to.name.startsWith(child.name))
					await this.data.setView(child);
	}


	public getRoute(schema?: string) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { schema: schema } };
	}

	public getChildRoute(schema: string) : RouteLocationRaw {
		const redirect_name: string = (routeSchema.selectedChild === undefined) ? routeSchemaUebersicht.name : routeSchema.selectedChild.name;
		return { name: redirect_name, params: { schema: schema } };
	}

	public getAuswahlProps(to: RouteLocationNormalized): SchemaAuswahlProps {
		return {
			hasRootPrivileges: api.hasRootPrivileges,
			auswahl: this.data.auswahl,
			auswahlGruppe: this.data.auswahlGruppe,
			mapSchema: () => this.data.mapSchema,
			gotoSchema: this.data.gotoSchema,
			setAuswahlGruppe: this.data.setAuswahlGruppe,
			removeSchemata: this.data.removeSchemata,
			addSchema: this.data.addSchema,
			importSchema: this.data.importSchema,
		};
	}

	public getProps(to: RouteLocationNormalized): SchemaAppProps {
		return {
			auswahl: this.data.auswahl,
			// Props für die Navigation
			setTab: this.setTab,
			tab: this.getTab(),
			tabs: this.getTabs(),
			tabsHidden: this.children_hidden().value,
		};
	}

	private getTab(): AuswahlChildData {
		return { name: this.data.view.name, text: this.data.view.text };
	}

	private getTabs(): AuswahlChildData[] {
		const result: AuswahlChildData[] = [];
		for (const c of this.children)
			result.push({ name: c.name, text: c.text });
		return result;
	}

	private setTab = async (value: AuswahlChildData) => {
		if (value.name === this.data.view.name)
			return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new Error("Unbekannte Route");
		await RouteManager.doRoute({ name: value.name, params: { schema: this.data.auswahl?.name } });
		await this.data.setView(node);
	}

}

export const routeSchema = new RouteSchema();
