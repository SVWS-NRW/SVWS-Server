import { type Page, expect } from "@playwright/test";

export async function startGruppenprozessMitSchuelern(page: Page, schuelerNames: Array<string>) {
	for (const schuelerName of schuelerNames) {
		await page.getByRole('row', { name: schuelerName }).getByRole('checkbox').click();
		await page.waitForTimeout(50); // Führt sonst zu Problemem in Chrome
	}

	// Prüfen, ob die Gruppenprozesse App geladen wurde
	await expect(page.locator('.svws-headline')).toContainText('Mehrfachauswahl');
	await expect(page).toHaveURL(new RegExp('.*/schueler/gruppenprozesse/daten'));

	for (const s of schuelerNames) {
		const nameParts = s.split(" ");
		await expect(page.locator('header')).toContainText(nameParts[1]);
		await expect(page.locator('header')).toContainText(nameParts[2]);
	}
}

export const getContentOfActiveTooltip = async (page: Page) => {
	return await page.locator(".tooltip-content .py-1.px-1.flex.flex-col").last().textContent();
}

export const getSaveButton = async (page: Page) => {
	return page.getByRole('button').filter({ hasText: 'Speichern' });
}

export const getResetButton = async (page: Page) => {
	return page.getByRole('button').filter({ hasText: 'Zurücksetzen' });
}
