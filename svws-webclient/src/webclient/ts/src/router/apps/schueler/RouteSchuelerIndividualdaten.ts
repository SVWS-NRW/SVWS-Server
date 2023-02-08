import { RouteNode } from "~/router/RouteNode";
import { FoerderschwerpunktEintrag, KatalogEintrag, ReligionEintrag, SchuelerListeEintrag, SchuelerStammdaten } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { RouteSchueler, routeSchueler } from "../RouteSchueler";
import { routeApp } from "~/router/RouteApp";
import { App } from "~/apps/BaseApp";

const SSchuelerIndividualdaten = () => import("~/components/schueler/individualdaten/SSchuelerIndividualdaten.vue");

export class RouteDataSchuelerIndividualdaten {

	item: SchuelerListeEintrag | undefined = undefined;
	mapFahrschuelerarten: Map<number, KatalogEintrag> = new Map();
	mapFoerderschwerpunkte: Map<number, FoerderschwerpunktEintrag> = new Map();
	mapHaltestellen: Map<number, KatalogEintrag> = new Map();
	mapReligionen: Map<number, ReligionEintrag> = new Map();

	patch = async (data : Partial<SchuelerStammdaten>) => {
		if (this.item === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		await App.api.patchSchuelerStammdaten(data, App.schema, this.item.id);
		// TODO Bei Anpassungen von nachname, vorname -> routeSchueler: Schülerliste aktualisieren...
	}

}

export class RouteSchuelerIndividualdaten extends RouteNode<RouteDataSchuelerIndividualdaten, RouteSchueler> {

	public constructor() {
		super("schueler_daten", "daten", SSchuelerIndividualdaten, new RouteDataSchuelerIndividualdaten());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Individualdaten";
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
		// Lade den Katalog der Fahrschülerarten
		const fahrschuelerarten = await App.api.getSchuelerFahrschuelerarten(App.schema)
		const mapFachschuelerarten = new Map();
		for (const fa of fahrschuelerarten)
			mapFachschuelerarten.set(fa.id, fa);
		this.data.mapFahrschuelerarten = mapFachschuelerarten;
		// Lade den Katalog der Förderschwerpunkte
		const foerderschwerpunkte = await App.api.getSchuelerFoerderschwerpunkte(App.schema);
		const mapFoerderschwerpunkte = new Map();
		for (const fs of foerderschwerpunkte)
			mapFoerderschwerpunkte.set(fs.id, fs);
		this.data.mapFoerderschwerpunkte = mapFoerderschwerpunkte;
		// Lade den Katalog der Haltestellen
		const haltestellen = await App.api.getHaltestellen(App.schema);
		const mapHaltestellen = new Map();
		for (const h of haltestellen)
			mapHaltestellen.set(h.id, h);
		this.data.mapHaltestellen = mapHaltestellen;
		// Lade den Katalog der Religionen
		const religionen = await App.api.getReligionen(App.schema)
		const mapReligionen = new Map();
		for (const r of religionen)
			mapReligionen.set(r.id, r);
		this.data.mapReligionen = mapReligionen;
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.id === undefined) {
			await this.onSelect(undefined);
		} else {
			const tmp = parseInt(to_params.id as string);
			await this.onSelect(this.parent!.liste.liste.find(s => s.id === tmp));
		}
	}

	protected async onSelect(item?: SchuelerListeEintrag) {
		if (item === this.data.item)
			return;
		if (item === undefined) {
			this.data.item = undefined;
		} else {
			this.data.item = item;
		}
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			patch: this.data.patch,
			data: routeSchueler.data.stammdaten.daten,
			mapOrte: routeApp.data.mapOrte,
			mapOrtsteile: routeApp.data.mapOrtsteile,
			mapFahrschuelerarten: this.data.mapFahrschuelerarten,
			mapFoerderschwerpunkte: this.data.mapFoerderschwerpunkte,
			mapHaltestellen: this.data.mapHaltestellen,
			mapReligionen: this.data.mapReligionen
		};
	}

}

export const routeSchuelerIndividualdaten = new RouteSchuelerIndividualdaten();

