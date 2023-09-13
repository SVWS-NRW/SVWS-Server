import type { Locator, Page} from "@playwright/test";
import { expect } from "@playwright/test";
import { api } from "../../api/Api";
import type { GostFach, List } from "@core";
import type { Schueler } from "../../config/data.schueler";


interface fach_webseite_zelle{
	text? : string,
	node? : Locator
}

interface fach_webseite {
	kuerzel: fach_webseite_zelle,
	bezeichnung: fach_webseite_zelle,
	ws? : fach_webseite_zelle,
	folge? : fach_webseite_zelle,
	abJg? : fach_webseite_zelle,
	ef_1? : fach_webseite_zelle,
	ef_2? : fach_webseite_zelle,
	q1_1? : fach_webseite_zelle,
	q1_2? : fach_webseite_zelle,
	q2_1? : fach_webseite_zelle,
	q2_2? : fach_webseite_zelle,
	abi_fach? : fach_webseite_zelle,
}

const abiturjahr = 2022

export class SchuelerLaufbahntabellePage {
	rg_tabellenkopf : Locator;
	rg_tabelleninhalt : Locator;
	rg_tabellenfoot : Locator;
	fachbelegung = new Map<string|undefined, fach_webseite>();
	schueler : Schueler;

	constructor(public page: Page) {
		this.rg_tabellenkopf = this.page.getByRole('table').nth(1).getByRole('rowgroup').nth(0);
		this.rg_tabelleninhalt = this.page.getByRole('table').nth(1).getByRole('rowgroup').nth(1);
		this.rg_tabellenfoot = this.page.getByRole('table').nth(1).getByRole('rowgroup').nth(2);
		this.schueler = { id:1, name:"", export_lp_name:"", import_lp_name:"", abiturjahr:2022}
	}

	ladeConfig(schueler: Schueler) {
		this.schueler = schueler;
	}

	async pruefeAttributAriaLabel() {
		await expect(this.rg_tabellenkopf).toHaveAttribute('aria-label','Tabellenkopf');
		await expect(this.rg_tabelleninhalt).toHaveAttribute('aria-label','Tabelleninhalt');
		await expect(this.rg_tabellenfoot).toHaveAttribute('aria-label','Fußzeile');
	}

	async pruefeSichtbarkeit(){
		await this.rg_tabellenkopf.waitFor({state:"visible"});
		await this.rg_tabelleninhalt.waitFor({state:"visible"});
		await this.rg_tabellenfoot.waitFor({state:"visible"});
		await this.pruefeAttributAriaLabel();
		await this.pruefeSichtbarkeit_tabellenkopf();
		await this.pruefeSichtbarkeit_tabellenfoot();

	}

	/**
	 * Die Zelleninhalte der ersten beiden Zeilen der Schülerlaufbahntabelle wird mit statichen Inhalten aus der Methode
	 * überprüft.
	 */
	async pruefeSichtbarkeit_tabellenkopf() {
		const row1 = this.rg_tabellenkopf.getByRole('row').nth(0);
		const r1_columns = row1.getByRole('columnheader');
		const r1_column_header = [ "Fach", "Sprachen", "EF", "Qualifikationsphase", "Abitur" ];
		await expect(row1).toBeVisible();
		await expect(r1_columns).toHaveCount(5);
		for (const column of await r1_columns.all())
			await expect(column).toBeVisible();
		expect(await r1_columns.allInnerTexts()).toEqual(r1_column_header);

		const row2 = this.rg_tabellenkopf.getByRole('row').nth(1);
		const r2_columns = row2.getByRole('columnheader');
		const r2_column_header = [ "Kürzel", "Bezeichnung", "WS", "Folge", "ab Jg", "EF.1", "EF.2", "Q1.1", "Q1.2", "Q2.1", "Q2.2", "Fach"];
		await expect(row2).toBeVisible();
		await expect(r2_columns).toHaveCount(12);
		for (const column of await r2_columns.all())
			await expect(column).toBeVisible();
		expect(await r2_columns.allInnerTexts()).toEqual(r2_column_header);
	}

	async pruefeSichtbarkeit_tabellenfoot() {
		const column_header = [ "Anrechenbare Kurse", "Wochenstunden", "Durchschnitt" ];
		for (let i = 0; i < 3; i++)
			expect((await this.rg_tabellenfoot.getByRole('row').nth(i).getByRole('rowheader').nth(0).allTextContents()).at(0)).toEqual(column_header[i]);
	}

	/**
	 * Erstellt aus den Zeilen der Laufbahntabelle Objekte des Typs "fach_webseite" und speichert sie in "fachbelegung".
	 */
	async ladeFachbelegungeng_von_Webseite(){
		const rows = this.rg_tabelleninhalt.getByRole('row');
		await expect(rows).toHaveCount(35);
		let zeile = 0;
		for (const row of await rows.all()) {
			await row.waitFor( {state: "visible"});
			const row_texte = await row.getByRole('cell').allInnerTexts();
			const row_zelle = row.getByRole('cell');
			const fachBelegung : fach_webseite = {
				kuerzel: { text: await row_zelle.nth(0).textContent() ?? undefined, node:  row_zelle.nth(0) },
				bezeichnung: { text:row_texte[1], node: row_zelle.nth(1) },
				ws: { text:row_texte[2], node: row.nth(2) },
				folge: { text: row_texte[3], node: row_zelle.nth(3) },
				abJg: { text:row_texte[4], node: row_zelle.nth(4) },
				ef_1: { text:row_texte[5], node: row_zelle.nth(5) },
				ef_2: { text:row_texte[6], node: row_zelle.nth(6) },
				q1_1: { text:row_texte[7], node: row_zelle.nth(7) },
			    q1_2: { text:row_texte[8], node: row_zelle.nth(8) },
			    q2_1: { text:row_texte[9], node: row_zelle.nth(9) },
				q2_2: { text:row_texte[10], node: row_zelle.nth(10) },
				abi_fach: { text:row_texte[11], node: row_zelle.nth(11) },
			};
			this.fachbelegung.set(fachBelegung.kuerzel.text,fachBelegung);
			zeile++;
		}
		// for (const [key, value] of this.fachbelegung) {
		// 	console.log("Key:" + key);
		// 	console.log("Value.text:" + value.q1_1?.text + "---nodetext" + await value.q1_1?.node?.allInnerTexts());
		// }
	}

	/**
	 * überprüft, ob alle Gostfächer von der API auf der Webseite in Laufbahntabelle angezeigt werden.
	 * Dazue werden die Inhalte der Objekte listGostFaecher(api) und fachbelegung(webseite) miteinander verglichen.
	 */
	async vergleicheFaecher_API_Webseite() : Promise<void> {
		await this.ladeFachbelegungeng_von_Webseite();
		const listGostFaecher = await this.getGostFaechervonAPI();
		await expect(this.rg_tabelleninhalt.getByRole('row')).toHaveCount(listGostFaecher.size());

		for (const [key, value] of this.fachbelegung) {
			const gostfach = this.getGostFachmitKuerzelvonAPI(key,listGostFaecher);
			expect(gostfach?.kuerzelAnzeige).toBe(value.kuerzel.text);
			expect(gostfach?.bezeichnung).toBe(value.bezeichnung.text);
			expect(gostfach?.wochenstundenQualifikationsphase).toBe(Number(value.ws?.text));
			if ((key != undefined) && [ "E", "F", "L", "S", "S0" ].includes(key)) {
				await this.pruefeSchuelerZeile_Sprache_WebToApi(value);
			}
		}
	}

	async getGostFaechervonAPI() : Promise<List<GostFach>> {
		await api.connectTo('localhost');
		await api.login('gymabi', 'Admin', '');
		const listGostFaecher = await api.server.getGostAbiturjahrgangFaecher(api.schema,this.schueler.abiturjahr);
		await api.server.getGostSchuelerLaufbahnplanung(api.schema, this.schueler.id);
		return listGostFaecher;
	}

	getGostFachmitKuerzelvonAPI(kuerzel:string | undefined, listGostFaecher:List<GostFach>) : GostFach | undefined {
		if (listGostFaecher)
			for (const fach of listGostFaecher)
				if (fach.kuerzelAnzeige === kuerzel)
					return fach;
		return undefined;
	}

	async pruefeSchuelerZeile_Sprache_WebToApi(value : fach_webseite) : Promise<void> {
		const daten = undefined;
		expect(this.schueler.id).toBeDefined();
		if (this.schueler.id) {
			const daten = await api.server.getGostSchuelerLaufbahnplanung(api.schema, this.schueler.id);
			const sprachen = daten.sprachendaten;
			for (const belegung of sprachen.belegungen) {
				if (belegung.sprache === value.kuerzel) {
					expect(belegung.belegungVonJahrgang).toEqual(value.abJg?.text);
					expect(belegung.reihenfolge).toEqual(Number(value.folge?.text));
				}
			}
		}
	}

	async clickZelle_EF_bis_Q2_alle_Faecher(){
		await this.ladeFachbelegungeng_von_Webseite();
		for (const [key, value] of this.fachbelegung) {
			if (value.kuerzel.text) {
				await this.clickZelle(value.ef_1?.node);
				await this.clickZelle(value.ef_2?.node);
				await this.clickZelle(value.q1_1?.node);
				await this.clickZelle(value.q1_2?.node);
				await this.clickZelle(value.q2_1?.node);
				await this.clickZelle(value.q2_2?.node);
			}
		}
	}

	async clickZelle(value : Locator | undefined){
		await value?.waitFor({ state: "visible" });
		await value?.click();
	}

}