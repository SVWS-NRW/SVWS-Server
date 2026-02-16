import { type Page, expect } from "@playwright/test";

export async function startGruppenprozessMitSchuelern(page: Page, schuelerNames: Array<string>) {
	for (let i = 0; i < schuelerNames.length; i++) {
		await page.getByRole('row', { name: schuelerNames[i] }).getByRole('checkbox').click();
		// Prüfen, ob die Subheadline korrekt ist (ebenfalls Indikator dafür, ob Seite geladen wurde)
		if (i < 3) {
			const nameParts = schuelerNames[i].split(" ");
			await expect(page.locator('.svws-subline')).toContainText(nameParts[1]);
			await expect(page.locator('.svws-subline')).toContainText(nameParts[2]);
		} else {
			await expect(page.locator('.svws-subline')).toContainText((i + 1) + ' Schüler ausgewählt');
		}
	}

	// Prüfen, ob weitere Parameter korrekt sind
	await expect(page.locator('.svws-headline')).toContainText('Mehrfachauswahl');
	await expect(page).toHaveURL(/.*\/schueler\/gruppenprozesse\/daten/);
}

export const getContentOfActiveTooltip = async (page: Page) => {
	return await page.locator(".tooltip-content .py-1.px-1.flex.flex-col").last().textContent();
};

export const getSaveButton = async (page: Page) => {
	return page.getByRole('button').filter({ hasText: 'Speichern' });
};

export const getResetButton = async (page: Page) => {
	return page.getByRole('button').filter({ hasText: 'Zurücksetzen' });
};
