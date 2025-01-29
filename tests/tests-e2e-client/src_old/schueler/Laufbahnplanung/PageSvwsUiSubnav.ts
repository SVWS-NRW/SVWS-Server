import type { Locator} from "@playwright/test";
import { expect, type Page } from "@playwright/test";
import type { Schueler } from "../DataSchueler";
import type ErrorPage from "~/PageError";

export class PageSvwsUiSubnav {
	public schueler : Schueler | undefined;
	menue_items : string[] = [];
	modus : string = '';
	constructor(public page: Page) {}

	async ladeConfig(schueler : Schueler) {
		expect(this.page.locator('.router-tab-bar--subnav')).toBeVisible;
		expect(this.page.locator('.router-tab-bar--subnav').getByTitle('Modus wechseln')).toBeVisible;
		this.menue_items = await this.page.locator('.router-tab-bar--subnav').getByRole('button').allInnerTexts();
		const schreibmodus = await this.page.locator('.router-tab-bar--subnav').getByTitle('Modus wechseln').allTextContents(); //.getByTitle('Modus wechseln')
		if(schreibmodus[0].trim().includes('Modus: normal')){
			this.modus = 'Modus: normal';
		} else if(schreibmodus[0].trim().includes('Modus: hochschreiben')){
			this.modus = 'Modus: hochschreiben';
		}else if(schreibmodus[0].trim().includes('Modus: manuell')){
			this.modus = 'Modus: manuell';
		}else {
			expect("Fehler bei"+schueler.name2click).toBe("Ein Modus sollte angezeigt werden!");
		}

		expect(this.menue_items).toContain('Exportieren');
		expect(this.menue_items).toContain('Importieren…');
		expect(this.menue_items).toContain('Planung merken');
		expect(this.menue_items).toContain('Fachwahlen löschen');
		this.schueler= schueler;
	}

	async clickExportieren(errorPage : ErrorPage) {
		const downloadPromise = this.page.waitForEvent('download');
		await this.page.getByRole('button', { name: 'Exportieren' }).click();
  		const download = await downloadPromise;
		// Save downloaded file somewhere
		if (this.schueler?.import_lp_name)
			await download.saveAs("./data/schueler/"+this.schueler?.export_lp_name);
		else
			expect("Fehlermeldung").toBe("Name der Lupodatei ist nicht vorhanden.");

		if (await errorPage.isVisible())
			expect((await errorPage.getMessage())).toBe("t");
	}
	// TODO funktioniert nur für einen Schüler!
	async clickImportieren(errorPage : ErrorPage) {

		await this.page.getByRole('button', { name: 'Importieren…' }).click();
		const fileChooserPromise = this.page.waitForEvent('filechooser');
		await this.page.getByRole('textbox').click();
		const fileChooser = await fileChooserPromise;
		console.log(this.schueler?.name2click);
		console.log(this.schueler?.import_lp_name);
		if (this.schueler?.import_lp_name)
			await fileChooser.setFiles("./data/schueler/"+this.schueler?.import_lp_name);
		else
			expect("Fehlermeldung").toBe("Name der Lupodatei ist nicht vorhanden.");

		if (await errorPage.isVisible())
			expect((await errorPage.getMessage())).toBe("t");


  	}

	async clickPanungmerken() {
		await this.page.getByRole('button', { name: 'Planung merken' }).click();
		await this.page.waitForLoadState('networkidle');

	}

	async clickPlanungWiederherstellen() {
		await this.page.getByRole('button', { name: 'Planung wiederherstellen' }).click();
		await this.page.waitForLoadState('networkidle');
		await expect(this.page.getByRole('button', { name: 'Planung wiederherstellen' })).not.toBeVisible();
	}

	async clickModusJG9() {
		switch(this.modus) {
			case 'Modus: normal':
				await expect(this.page.getByRole('button', { name: 'Modus: normal' })).toBeVisible();
				await this.clickNormalermodus();
				await expect(this.page.getByRole('button', { name: 'Modus: hochschreiben' })).toBeVisible();
		        await expect(this.page.getByRole('button', { name: 'Modus: manuell' })).not.toBeVisible();
		        await expect(this.page.getByRole('button', { name: 'Modus: normal' })).not.toBeVisible();
		        this.modus = 'Modus: hochschreiben';
				break;
			case 'Modus: hochschreiben':
				await expect(this.page.getByRole('button', { name: 'Modus: hochschreiben' })).toBeVisible();
				await this.page.getByRole('button', { name: 'Modus: hochschreiben' }).click();
		        await expect(this.page.getByRole('button', { name: 'Modus: manuell' })).toBeVisible();
		        await expect(this.page.getByRole('button', { name: 'Modus: normal' })).not.toBeVisible();
		        await expect(this.page.getByRole('button', { name: 'Modus: hochschreiben' })).not.toBeVisible();
		        this.modus = 'Modus: manuell';
				break;
			case 'Modus: manuell':
				await expect(this.page.getByRole('button', { name: 'Modus: manuell' })).toBeVisible();
				await this.clickManuellermodus();
				await expect(this.page.getByRole('button', { name: 'Modus: normal' })).toBeVisible();
		        await expect(this.page.getByRole('button', { name: 'Modus: manuell' })).not.toBeVisible();
		        await expect(this.page.getByRole('button', { name: 'Modus: hochschreiben' })).not.toBeVisible();
		        this.modus = 'Modus: normal';
				break;
		}
	}

	async clickModus_ab_10(){
		switch(this.modus) {
			case 'Modus: normal':
				await expect(this.page.getByRole('button', { name: 'Modus: normal' })).toBeVisible();
				await this.clickNormalermodus();
				await expect(this.page.getByRole('button', { name: 'Modus: manuell' })).toBeVisible();
		        await expect(this.page.getByRole('button', { name: 'Modus: normal' })).not.toBeVisible();
				this.modus = 'Modus: manuell';
				break;
			case 'Modus: manuell':
				await expect(this.page.getByRole('button', { name: 'Modus: manuell' })).toBeVisible();
				await this.clickManuellermodus();
				await expect(this.page.getByRole('button', { name: 'Modus: normal' })).toBeVisible();
				await expect(this.page.getByRole('button', { name: 'Modus: manuell' })).not.toBeVisible();
				this.modus = 'Modus: normal';
				break;
		}
	}

	async clickNormalermodus() {
		await this.page.getByRole('button', { name: 'Modus: normal' }).click();
	}

	async clickHochschreibmodus() {
		await this.page.getByRole('button', { name: 'Modus: hochschreiben' }).click();
	}

	async clickManuellermodus() {
		await this.page.getByRole('button', { name: 'Modus: manuell' }).click();
	}

	async clickZurücksetzenmitAbbrechen() {
		await this.page.getByRole('button', { name: 'Fachwahlen löschen' }).click();
		await expect(this.page.getByRole('button', { name: 'Abbrechen' })).toBeVisible();
		await expect(this.page.getByRole('button', { name: 'Ja' })).toBeVisible();
  		await this.page.getByRole('button', { name: 'Abbrechen' }).click();
		await expect(this.page.getByRole('button', { name: 'Ja' })).not.toBeVisible();
	}

	async clickZurücksetzenmitJaLoeschen() {
		await this.page.getByRole('button', { name: 'Fachwahlen löschen' }).click();
		await expect(this.page.getByRole('button', { name: 'Abbrechen' })).toBeVisible();
		await expect(this.page.getByRole('button', { name: 'Ja' })).toBeVisible();
  		await this.page.getByRole('button', { name: 'Ja' }).click();
		await expect(this.page.getByRole('button', { name: 'Ja' })).not.toBeVisible();
	}
}