import { expect, test } from "@playwright/test";
import { useLoginUtils } from "../utils/LoginUtils";
import { getContentOfActiveTooltip, getResetButton, startGruppenprozessMitSchuelern } from "../utils/SchuelerGruppenprozesseUtils";

const targetHost = process.env.VITE_targetHost ?? "http://localhost:3000/#/svws"

const tooltipFahrschuelerId = '#tooltip-fahrschuelerArtID';
const tooltipHaltestelleId = '#tooltip-haltestelleID';
const clearButtonHaltestelleId = "#Clearbutton-haltestelleID";

test.use({
	ignoreHTTPSErrors: true,
});

test('Weitere Felder zu Migrationshintergrund nur aktiv, wenn Migrationshintergrund vorhanden', async ({ page }) => {
	const { loginAdmin } = useLoginUtils(targetHost, page);

	await loginAdmin();

	await startGruppenprozessMitSchuelern(page, ['09a Daum Sven', '09a Delfes Michael']);

	expect(await page.locator("#Card-Migrationshintergrund").ariaSnapshot()).toContain('combobox "Geburtsland" [disabled]');
	await page.getByRole('checkbox', { name: 'Migrationshintergrund' }).check();
	expect(await page.locator("#Card-Migrationshintergrund").ariaSnapshot()).not.toContain('disabled');
	await page.locator('#tooltip-hatMigrationshintergrund').click();
	expect(await page.locator("#Card-Migrationshintergrund").ariaSnapshot()).toContain('combobox "Geburtsland" [disabled]');
})

test.skip('Clear Button entfernt aktuelle Daten', async ({ page }) => {
	// Destruktiv und daher nicht implementiert.
})

test.skip('Gruppenänderung findet sich bei einzelnem Schüler wieder', async ({ page }) => {
	// Destruktiv und daher nicht implementiert.
})

test('Beim Zurücksetzen der Änderungen wird die Auswahl der Schüler reaktiviert', async ({ page }) => {
	const { loginAdmin } = useLoginUtils(targetHost, page);

	await loginAdmin();

	await startGruppenprozessMitSchuelern(page, ['09a Daum Sven', '09a Delfes Michael']);

	await page.getByRole('combobox', { name: 'Fahrschüler' }).click();
	await page.getByRole('option', { name: 'Rheingold' }).click();

	expect(await page.getByRole('row', { name: '09a Arens Matthias' }).getByRole('cell').first().ariaSnapshot()).toContain(`checkbox [disabled]`);
	await page.locator(tooltipFahrschuelerId).click();
	expect(await page.getByRole('row', { name: '09a Arens Matthias' }).getByRole('cell').first().ariaSnapshot()).not.toContain(`disabled`);
})

test('Bei ausstehenden Änderungen wird die Auswahl der Schüler deaktiviert', async ({ page }) => {
	const { loginAdmin } = useLoginUtils(targetHost, page);

	await loginAdmin();

	await startGruppenprozessMitSchuelern(page, ['09a Daum Sven', '09a Delfes Michael']);

	await page.getByRole('combobox', { name: 'Fahrschüler' }).click();
	await page.getByRole('option', { name: 'Rheingold' }).click();

	expect(await page.getByRole('row', { name: '09a Arens Matthias' }).getByRole('cell').first().ariaSnapshot()).toContain(`checkbox [disabled]`);
})

test('Modal bei Routenwechsel wird aktiv, bei Fortsetzen werden Änderungen zurückgesetzt', async ({ page }) => {
	const { loginAdmin } = useLoginUtils(targetHost, page);

	await loginAdmin();

	await startGruppenprozessMitSchuelern(page, ['09a Daum Sven', '09a Delfes Michael']);

	await page.getByRole('combobox', { name: 'Fahrschüler' }).click();
	await page.getByRole('option', { name: 'Rheingold' }).click();

	await page.getByRole('link', { name: 'Schule' }).click();
	await expect(page.getByLabel('Daten gehen verloren')).toMatchAriaSnapshot(`
    - heading "Daten gehen verloren" [level=2]
    - paragraph: Beim Verlassen dieser Seite gehen alle eingetragenen Informationen verloren. Wollen Sie die Seite wirklich verlassen?
    - button "Abbrechen"
    - button "Ja"
    `);
	await page.getByRole('button', { name: 'Ja' }).click();

	await page.getByRole('link', { name: 'Schüler' , exact: true}).click();

	await expect(page.locator(tooltipFahrschuelerId)).toHaveCount(0);
})

test('Modal bei Routenwechsel wird aktiv, bei Abbrechen werden Pending States behalten und angezeigt', async ({ page }) => {
	const { loginAdmin } = useLoginUtils(targetHost, page);

	await loginAdmin();

	await startGruppenprozessMitSchuelern(page, ['09a Daum Sven', '09a Delfes Michael']);

	await page.getByRole('combobox', { name: 'Fahrschüler' }).click();
	await page.getByRole('option', { name: 'Rheingold' }).click();

	await page.getByRole('link', { name: 'Schule' }).click();
	await expect(page.getByLabel('Daten gehen verloren')).toMatchAriaSnapshot(`
    - heading "Daten gehen verloren" [level=2]
    - paragraph: Beim Verlassen dieser Seite gehen alle eingetragenen Informationen verloren. Wollen Sie die Seite wirklich verlassen?
    - button "Abbrechen"
    - button "Ja"
    `);
	await page.getByRole('button', { name: 'Abbrechen' }).click();
	await expect(page.locator(tooltipFahrschuelerId)).toHaveCount(1);
})

test('Clear Button ist disabled, wenn keine Daten vorhanden sind', async ({ page }) => {
	const { loginAdmin } = useLoginUtils(targetHost, page);

	await loginAdmin();

	await startGruppenprozessMitSchuelern(page, ['09a Arens Matthias']);

	expect(await page.locator(clearButtonHaltestelleId).innerHTML()).toContain("icon-ui-disabled")
	await page.getByRole('button', { name: 'Auswahl aufheben' }).click();
})

test('Clear Button ist enabled, wenn Daten vorhanden sind', async ({ page }) => {
	const { loginAdmin } = useLoginUtils(targetHost, page);

	await loginAdmin();

	await startGruppenprozessMitSchuelern(page, ['08c Testschueler TestLisa']);
	expect(await page.locator(clearButtonHaltestelleId).innerHTML()).not.toContain("icon-ui-disabled")
})

test('Clear Button ist enabled, wenn Daten für mindestens einen Schüler vorhanden sind', async ({ page }) => {
	const { loginAdmin } = useLoginUtils(targetHost, page);

	await loginAdmin();

	await startGruppenprozessMitSchuelern(page, ['09a Arens Matthias', '08c Testschueler TestLisa']);
	expect(await page.locator(clearButtonHaltestelleId).innerHTML()).toContain("icon-ui-danger")
})

test('Während des Hoverns des Tooltips, wird ein X im Button angezeigt', async ({ page }) => {
	const { loginAdmin } = useLoginUtils(targetHost, page);

	await loginAdmin();

	await startGruppenprozessMitSchuelern(page, ['09a Arens Matthias', '08c Testschueler TestLisa']);

	await page.getByRole('combobox', { name: 'Haltestelle' }).click();
	await page.getByRole('option', { name: '628 Fingscheid' }).click();

	const innerHtml = await page.locator(tooltipHaltestelleId).innerHTML();
	expect(innerHtml).toContain("i-ri-information-line")
	expect(innerHtml).not.toContain("i-ri-close-line");

	await page.locator(tooltipHaltestelleId).hover();

	const innerHtmlHovered = await page.locator(tooltipHaltestelleId).innerHTML();
	expect(innerHtmlHovered).not.toContain("i-ri-information-line");
	expect(innerHtmlHovered).toContain("i-ri-close-line");
})

test('Auswahl aufheben (Ja), schließt Gruppenprozess und reaktiviert Schülerauswahl', async ({ page }) => {
	const { loginAdmin } = useLoginUtils(targetHost, page);

	await loginAdmin();

	await startGruppenprozessMitSchuelern(page, ['09a Daum Sven', '09a Delfes Michael']);

	await page.getByRole('combobox', { name: 'Fahrschüler' }).click();
	await page.getByRole('option', { name: 'Rheingold' }).click();

	await expect(page.locator(tooltipFahrschuelerId)).toHaveCount(1);

	expect(await page.getByRole('row', { name: '09a Arens Matthias' }).getByRole('cell').first().ariaSnapshot()).toContain(`checkbox [disabled]`);

	await page.getByRole('button', { name: 'Auswahl aufheben' }).click();

	await expect(page.getByLabel('Daten gehen verloren')).toMatchAriaSnapshot(`
    - heading "Daten gehen verloren" [level=2]
    - paragraph: Beim Verlassen dieser Seite gehen alle eingetragenen Informationen verloren. Wollen Sie die Seite wirklich verlassen?
    - button "Abbrechen"
    - button "Ja"
    `);

	await page.getByRole('button', { name: 'Ja' }).click();

	expect(await page.getByRole('row', { name: '09a Arens Matthias' }).getByRole('cell').first().ariaSnapshot()).not.toContain(`disabled`);

	await expect(page.locator(tooltipFahrschuelerId)).toHaveCount(0);
})

test('Auswahl aufheben (Nein), bleibt in Gruppenprozess und aktuelle Änderungen bleiben bestehen', async ({ page }) => {
	const { loginAdmin } = useLoginUtils(targetHost, page);

	await loginAdmin();

	await startGruppenprozessMitSchuelern(page, ['09a Daum Sven', '09a Delfes Michael']);

	await page.getByRole('combobox', { name: 'Fahrschüler' }).click();
	await page.getByRole('option', { name: 'Rheingold' }).click();

	await expect(page.locator(tooltipFahrschuelerId)).toHaveCount(1);

	expect(await page.getByRole('row', { name: '09a Arens Matthias' }).getByRole('cell').first().ariaSnapshot()).toContain(`checkbox [disabled]`);

	await page.getByRole('button', { name: 'Auswahl aufheben' }).click();

	await expect(page.getByLabel('Daten gehen verloren')).toMatchAriaSnapshot(`
    - heading "Daten gehen verloren" [level=2]
    - paragraph: Beim Verlassen dieser Seite gehen alle eingetragenen Informationen verloren. Wollen Sie die Seite wirklich verlassen?
    - button "Abbrechen"
    - button "Ja"
    `);

	await page.getByRole('button', { name: 'Abbrechen' }).click();

	expect(await page.getByRole('row', { name: '09a Arens Matthias' }).getByRole('cell').first().ariaSnapshot()).toContain(`checkbox [disabled]`);

	await expect(page.locator(tooltipFahrschuelerId)).toHaveCount(1);
})

test('Button Zurücksetzen, setzt Pending State bei allen Feldern zurück', async ({ page }) => {
	const { loginAdmin } = useLoginUtils(targetHost, page);

	await loginAdmin();

	await startGruppenprozessMitSchuelern(page, ['09a Daum Sven', '09a Delfes Michael']);

	// Status
	await page.getByRole('combobox', { name: 'Status' }).click();
	await page.getByRole('option', { name: '2 - Aktiv' }).click();

	// Fahrschule
	await page.getByRole('combobox', { name: 'Fahrschüler' }).click();
	await page.getByRole('option', { name: 'Rheingold' }).click();

	// Haltestelle
	await page.getByRole('combobox', { name: 'Haltestelle' }).click();
	await page.getByRole('option', { name: '628 Fingscheid' }).click();

	// 1. Staatsangehörigkeit
	await page.getByRole('combobox', { name: '1. Staatsangehörigkeit' }).click();
	await page.getByRole('option', { name: 'ALB - Albanien' }).click();

	const resetButton = await getResetButton(page);
	await resetButton.click();

	await expect(page.locator(tooltipFahrschuelerId)).toHaveCount(0);
	await expect(page.locator('#tooltip-status')).toHaveCount(0);
	await expect(page.locator(tooltipHaltestelleId)).toHaveCount(0);
	await expect(page.locator('#tooltip-staatsangehoerigkeitID')).toHaveCount(0);
});

test('Änderung wird vorgenommen, anschließend kann Pending State zurückgesetzt werden', async ({ page }) => {
	const { loginAdmin } = useLoginUtils(targetHost, page);

	await loginAdmin();

	await startGruppenprozessMitSchuelern(page, ['09a Daum Sven', '09a Delfes Michael']);

	// Status
	await page.getByRole('combobox', { name: 'Status' }).click();
	await page.getByRole('option', { name: '2 - Aktiv' }).click();
	await page.locator('#tooltip-status').click();
	await expect(page.locator('#tooltip-status')).toHaveCount(0);
});

test('Jedes Feld erzeugt bei einer Änderung einen korrekten Pending State Tooltip', async ({ page }) => {
	const { loginAdmin } = useLoginUtils(targetHost, page);

	await loginAdmin();

	await startGruppenprozessMitSchuelern(page, ['09a Daum Sven', '09a Delfes Michael']);

	// Status
	await page.getByRole('combobox', { name: 'Status' }).click();
	await page.getByRole('option', { name: '2 - Aktiv' }).click();
	await page.locator('#tooltip-status').hover();
	expect(await getContentOfActiveTooltip(page)).toBe("Aktuell: Aktiv (2)  Nachher: Aktiv (2)");

	// Fahrschule
	await page.getByRole('combobox', { name: 'Fahrschüler' }).click();
	await page.getByRole('option', { name: 'Rheingold' }).click();
	await page.locator(tooltipFahrschuelerId).hover();
	expect(await getContentOfActiveTooltip(page)).toBe("Aktuell: Keine Daten (2)  Nachher: Rheingold (2)");

	// Haltestelle
	await page.getByRole('combobox', { name: 'Haltestelle' }).click();
	await page.getByRole('option', { name: '628 Fingscheid' }).click();

	await page.locator(tooltipHaltestelleId).hover();
	expect(await getContentOfActiveTooltip(page)).toBe("Aktuell: Keine Daten (2)  Nachher: 628 Fingscheid (2)");

	// 1. Staatsangehörigkeit
	await page.getByRole('combobox', { name: '1. Staatsangehörigkeit' }).click();
	await page.getByRole('option', { name: 'ALB - Albanien' }).click();
	await page.locator('#tooltip-staatsangehoerigkeitID').hover();
	expect(await getContentOfActiveTooltip(page)).toBe("Aktuell: Deutschland (2)  Nachher: Albanien (2)");

	// 2. Staatsangehörigkeit
	await page.getByRole('combobox', { name: '2. Staatsangehörigkeit' }).click();
	await page.getByRole('option', { name: 'ALB - Albanien' }).click();
	await page.locator('#tooltip-staatsangehoerigkeit2ID').hover();
	expect(await getContentOfActiveTooltip(page)).toBe("Aktuell: Keine Daten (2)  Nachher: Albanien (2)");

	// Religion / Konfession
	await page.getByRole('combobox', { name: 'Konfession' }).click();
	await page.getByRole('option', { name: 'Katholisch' }).click();
	await page.locator('#tooltip-religionID').hover();
	expect(await getContentOfActiveTooltip(page)).toBe("Aktuell: ohne Bekenntnis (1), evangelisch (1)  Nachher: katholisch (2)");

	// Religion Aufnahme
	await page.getByRole('textbox', { name: 'Abmeldung vom Religionsunterricht' }).fill('2025-10-10');
	await page.locator('#tooltip-religionabmeldung').hover();
	expect(await getContentOfActiveTooltip(page)).toBe("Aktuell: Keine Daten (2)  Nachher: 10.10.2025 (2)");

	// Religion Anmeldung
	await page.getByRole('textbox', { name: 'Wiederanmeldung' }).fill('2025-02-10');
	await page.locator('#tooltip-religionanmeldung').hover();
	expect(await getContentOfActiveTooltip(page)).toBe("Aktuell: Keine Daten (2)  Nachher: 10.02.2025 (2)");

	// Konfession auf Zeugnis
	await page.getByRole('checkbox', { name: 'Konfession aufs Zeugnis' }).check();
	await page.locator('#tooltip-druckeKonfessionAufZeugnisse').hover();
	expect(await getContentOfActiveTooltip(page)).toBe("Aktuell: Nein (2)  Nachher: Ja (2)");

	await page.getByRole('checkbox', { name: 'Volljährig' }).click();
	await page.getByRole('checkbox', { name: 'Volljährig' }).click();
	await page.locator('#tooltip-istVolljaehrig').hover();
	expect(await getContentOfActiveTooltip(page)).toBe("Aktuell: Ja (2)  Nachher: Nein (2)");

	await page.getByRole('checkbox', { name: 'Schulpflicht erfüllt' }).click();
	await page.getByRole('checkbox', { name: 'Schulpflicht erfüllt' }).click();
	await page.locator('#tooltip-istSchulpflichtErfuellt').hover();
	expect(await getContentOfActiveTooltip(page)).toBe("Aktuell: Ja (2)  Nachher: Nein (2)");

	await page.getByRole('checkbox', { name: 'Masern Impfnachweis' }).check();
	await page.locator('#tooltip-hatMasernimpfnachweis').hover();
	expect(await getContentOfActiveTooltip(page)).toBe("Aktuell: Nein (2)  Nachher: Ja (2)");

	await page.getByRole('checkbox', { name: 'BAFöG' }).check();
	await page.locator('#tooltip-hatMasernimpfnachweis').hover();
	expect(await getContentOfActiveTooltip(page)).toBe("Aktuell: Nein (2)  Nachher: Ja (2)");

	await page.getByRole('checkbox', { name: 'Schulpflicht SII erfüllt' }).check();

	await page.locator('#tooltip-istBerufsschulpflichtErfuellt').hover();
	expect(await getContentOfActiveTooltip(page)).toBe("Aktuell: Nein (2)  Nachher: Ja (2)");

	await page.getByRole('checkbox', { name: 'Keine Auskunft an Dritte' }).check();
	await page.locator('#tooltip-keineAuskunftAnDritte').hover();
	expect(await getContentOfActiveTooltip(page)).toBe("Aktuell: Nein (2)  Nachher: Ja (2)");

	await page.getByRole('checkbox', { name: 'Migrationshintergrund' }).check();
	await page.locator('#tooltip-hatMigrationshintergrund').hover();
	expect(await getContentOfActiveTooltip(page)).toBe("Aktuell: Nein (2)  Nachher: Ja (2)");

	await page.getByRole('spinbutton', { name: 'Zuzugsjahr' }).click();
	await page.getByRole('spinbutton', { name: 'Zuzugsjahr' }).fill('2020');
	await page.locator('#tooltip-zuzugsjahr').hover();
	expect(await getContentOfActiveTooltip(page)).toBe("Aktuell: Keine Daten (2)  Nachher: 2020 (2)");

	await page.getByRole('combobox', { name: 'Geburtsland', exact: true }).click();
	await page.getByRole('option', { name: 'AFG - Afghanistan' }).click();
	await page.locator('#tooltip-geburtsland').hover();
	expect(await getContentOfActiveTooltip(page)).toBe("Aktuell: Keine Daten (2)  Nachher: Afghanistan (2)");

	await page.getByRole('combobox', { name: 'Verkehrssprache', exact: true }).click();
	await page.getByRole('option', { name: 'aa - Afar' }).click();
	await page.locator('#tooltip-verkehrspracheFamilie').hover();
	expect(await getContentOfActiveTooltip(page)).toBe("Aktuell: Keine Daten (2)  Nachher: Afar (2)");

	await page.getByRole('combobox', { name: 'Geburtsland Mutter' }).first().click();
	await page.getByRole('option', { name: 'ALB - Albanien' }).click();
	await page.locator('#tooltip-geburtslandMutter').hover();
	expect(await getContentOfActiveTooltip(page)).toBe("Aktuell: Keine Daten (2)  Nachher: Albanien (2)");

	await page.getByRole('combobox', { name: 'Geburtsland Vater' }).first().click();
	await page.getByRole('option', { name: 'ALB - Albanien' }).click();
	await page.locator('#tooltip-geburtslandVater').hover();
	expect(await getContentOfActiveTooltip(page)).toBe("Aktuell: Keine Daten (2)  Nachher: Albanien (2)");
});

// Destruktiver Test derzeit noch nicht möglich
test.skip('Mehrere Schüler auswählen und Staatsangehörigkeit ändern und anschließend speichern, beide Schüler haben geänderten Wert.', async ({ page }) => {
	const { loginAdmin } = useLoginUtils(targetHost, page);

	await loginAdmin();

	await page.getByRole('row', { name: '09a Arens Matthias' }).getByRole('checkbox').check();
	await page.getByRole('row', { name: '09a Batta Dominik' }).getByRole('checkbox').check();

	const staatsangehoerigkeitSelect = page.locator('.ui-select').filter({ has: page.getByLabel('1. Staatsangehörigkeit') })
	await staatsangehoerigkeitSelect.click();
	await staatsangehoerigkeitSelect.locator('li').filter({ hasText: 'AFG - Afghanistan' }).click();

	await page.getByRole('button', { name: 'Speichern' }).click();
	await page.getByRole('row', { name: '09a Batta Dominik' }).getByRole('checkbox').uncheck();
	await page.getByRole('row', { name: '09a Arens Matthias' }).getByRole('checkbox').uncheck();
	await page.waitForTimeout(1000);

	await page.getByRole('row', { name: '09a Arens Matthias' }).click();
	await expect(page.locator('[role="combobox"][aria-label="1. Staatsangehörigkeit"]')).toHaveValue('afghanisch')
	await page.getByRole('row', { name: '09a Batta Dominik' }).click();
	await expect(page.locator('[role="combobox"][aria-label="1. Staatsangehörigkeit"]')).toHaveValue('afghanisch')
});
