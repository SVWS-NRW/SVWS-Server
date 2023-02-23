import { BenutzerKompetenz, FoerderschwerpunktEintrag, KatalogEintrag, ReligionEintrag, SchuelerListeEintrag, SchuelerStammdaten, Schulform } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { SchuelerIndividualdatenProps } from "~/components/schueler/individualdaten/SSchuelerIndividualdatenProps";
import { api } from "~/router/Api";
import { routeApp } from "~/router/RouteApp";
import { RouteNode } from "~/router/RouteNode";
import { RouteSchueler, routeSchueler } from "../RouteSchueler";

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
		await api.server.patchSchuelerStammdaten(data, api.schema, this.item.id);
		// TODO Bei Anpassungen von nachname, vorname -> routeSchueler: Schülerliste aktualisieren...
	}

}

export class RouteSchuelerIndividualdaten extends RouteNode<RouteDataSchuelerIndividualdaten, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schueler_daten", "daten", SSchuelerIndividualdaten, new RouteDataSchuelerIndividualdaten());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Individualdaten";
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
		// Lade den Katalog der Fahrschülerarten
		const fahrschuelerarten = await api.server.getSchuelerFahrschuelerarten(api.schema)
		const mapFachschuelerarten = new Map();
		for (const fa of fahrschuelerarten)
			mapFachschuelerarten.set(fa.id, fa);
		this.data.mapFahrschuelerarten = mapFachschuelerarten;
		// Lade den Katalog der Förderschwerpunkte
		const foerderschwerpunkte = await api.server.getSchuelerFoerderschwerpunkte(api.schema);
		const mapFoerderschwerpunkte = new Map();
		for (const fs of foerderschwerpunkte)
			mapFoerderschwerpunkte.set(fs.id, fs);
		this.data.mapFoerderschwerpunkte = mapFoerderschwerpunkte;
		// Lade den Katalog der Haltestellen
		const haltestellen = await api.server.getHaltestellen(api.schema);
		const mapHaltestellen = new Map();
		for (const h of haltestellen)
			mapHaltestellen.set(h.id, h);
		this.data.mapHaltestellen = mapHaltestellen;
		// Lade den Katalog der Religionen
		const religionen = await api.server.getReligionen(api.schema)
		const mapReligionen = new Map();
		for (const r of religionen)
			mapReligionen.set(r.id, r);
		this.data.mapReligionen = mapReligionen;
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (this.parent === undefined)
			throw new Error("Fehler: Die Route ist ungültig - Parent ist nicht definiert");
		if (to_params.id === undefined) {
			await this.onSelect(undefined);
		} else {
			const id = parseInt(to_params.id);
			await this.onSelect(this.parent.data.mapSchueler.get(id));
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

	public getProps(to: RouteLocationNormalized): SchuelerIndividualdatenProps {
		return {
			patch: this.data.patch,
			data: routeSchueler.data.stammdaten,
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

