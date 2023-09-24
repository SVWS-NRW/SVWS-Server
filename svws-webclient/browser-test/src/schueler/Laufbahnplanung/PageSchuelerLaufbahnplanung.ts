import type { Page } from "@playwright/test";

export class SSchuelerLaufbahnplanungPage{

	constructor(public page: Page) {

	}

	async clickPDFherunterladen() {
		const downloadPromise = this.page.waitForEvent('download');
		await this.page.getByRole('button', { name: 'PDF herunterladen' }).click();
		const download = await downloadPromise;
	}

	async clickHilfe() {
		await this.page.getByRole('button', { name: 'Hilfe' }).click();
	}

}