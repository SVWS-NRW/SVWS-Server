import { expect, type Page } from "@playwright/test";

/**
 * Wählt die übergebenen Schüler aus und startet damit den Gruppenprozess. Die Default-Route ist "Allgemeines".
 *
 * @param page             die Playwright-Page
 * @param schuelerNames    die Namen der auszuwählenden Schüler, wie sie in der Auswahlliste erscheinen
 * @param zuIndividualdaten gibt an, ob anschließend auf den Tab "Individualdaten" gewechselt werden soll
 */
export async function startGruppenprozessMitSchuelern(page: Page, schuelerNames: Array<string>, zuIndividualdaten: boolean = true) {
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
	await expect(page).toHaveURL(/.*\/schueler\/gruppenprozesse\/allgemeines/);

	if (zuIndividualdaten) {
		await page.locator('.svws-ui-tab-button').filter({ hasText: 'Individualdaten' }).click();
		await expect(page).toHaveURL(/.*\/schueler\/gruppenprozesse\/daten/);
	}
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
