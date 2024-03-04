import type { KlassenDaten, List, SchuelerListeEintrag } from "@core";
import type { Locator, Page} from "@playwright/test";
import { expect } from "@playwright/test";
import { api } from "../Api";
import type { Schueler } from "./DataSchueler";
import { dataSchuelerAuswahl } from "./DataSchuelerAuswahl";

process.env.NODE_TLS_REJECT_UNAUTHORIZED = '0';

export class SchuelerAuswahlPage {

	// Element
	rg_tabellenkopf : Locator;
	rg_tabelleninhalt : Locator;
	rg_tabellenfoot : Locator;
	tabelle : Locator;
	listeSchueler? : List<SchuelerListeEintrag>;
	listeKlassen? : Map<number, KlassenDaten>;


	constructor(public page: Page) {

		this.tabelle = this.page.getByRole('table').nth(0);
		this.rg_tabellenkopf = this.tabelle.getByRole('rowgroup').nth(0);
		this.rg_tabelleninhalt = this.tabelle.getByRole('rowgroup').nth(1);
		this.rg_tabellenfoot = this.tabelle.getByRole('rowgroup').nth(2);
	}

	async laden () {
		this.listeSchueler = await api.server.getSchuelerAktuell(api.schema);
		this.listeKlassen = await api.getKlassenListe(1);
	}

	async getrowgroupName(rg : Locator) : Promise<string | null> {
		return await rg.getAttribute('aria-label');
	}

	async pruefeAttributAriaLabel() {
		await expect(this.tabelle).toHaveAttribute('aria-label','Tabelle');
		await expect(this.rg_tabellenkopf).toHaveAttribute('aria-label','Tabellenkopf');
		await expect(this.rg_tabelleninhalt).toHaveAttribute('aria-label','Tabelleninhalt');
		await expect(this.rg_tabellenfoot).toHaveAttribute('aria-label','Fußzeile');
	}

	async clickAlleSchueler(){
		const schueler : Schueler[] = await dataSchuelerAuswahl.getSchueler();
		for(const item of schueler){
			await this.page.getByRole('row', { name: item.name2click }).click();
			await expect(this.page.getByText(item.name)).toBeVisible()
  		}
	}

	async clickSchueler(eintrag : SchuelerListeEintrag) {
		if (this.listeSchueler === undefined || this.listeKlassen === undefined)
			return;
		const klassen_kuerzel = this.listeKlassen.get(eintrag.idKlasse)?.kuerzel;
		const idjahrgang = this.listeKlassen.get(eintrag.idKlasse)?.idJahrgang;
		const role_name = klassen_kuerzel+' '+eintrag.nachname+' '+eintrag.vorname;
		//expect(role_name).toBe("EF Addens Sophie");
		//expect(this.page.getByRole('row', { name : role_name.toString()})).toBeDefined();
		await this.page.getByRole('row', { name: role_name }).click();
	}

	async clearSuchfeld(){
  		await this.page.getByPlaceholder('Suchen').fill('');
	}

	async clearFilter(){
		await this.page.locator('div').filter({ hasText: /^Extern$/ }).getByRole('img').click();
 		await this.page.getByTitle('Entfernen').locator('path').click();
	}
}