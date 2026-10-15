import type { Page, TestInfo } from '@playwright/test';

import { ENVIRONMENTS, getEnv } from '../kataloge.config';

import { test } from './fixtures';

// Bei gesetztem VITE_targetHost liefert getEnv() 'docker', dessen baseUrl
// absichtlich eine ungültige Sentinel-URL ist (siehe kataloge.config.ts) —
// login() darf ENVIRONMENTS[getEnv()] daher in diesem Fall nicht direkt
// lesen. VITE_targetHost ist stattdessen immer eine vollständige Ziel-URL
// inkl. Schema im Hash (z.B. "https://nightly.svws-nrw.de/#/GymAbiLite"),
// sodass die App die Schema-Dropdown-Auswahl selbst überspringt und nur
// Benutzername/Passwort/Anmelden nötig sind (siehe login() unten). Der
// bestehende Dropdown-Weg (ohne VITE_targetHost, SVWS_ENV/Default) bleibt
// davon unabhängig.
// Schema-Dropdown-Login-Ablauf, genutzt von login() (ohne VITE_targetHost).
const LOGIN_VERSUCHE = 3;

// Zusätzliches Zeitbudget, damit alle Versuche ins Testlimit passen und die
// gebündelte Fehlermeldung auch bei langsamen Fehlschlägen erscheint.
const LOGIN_ZUSATZ_ZEITBUDGET_MS = 60_000;

// Login kann in einem Test mehrfach aufgerufen werden; das Budget wird je
// Test (TestInfo) nur einmal erhöht. Wiederholungen eines Tests (Retries)
// bekommen eine neue TestInfo und damit wieder ihr eigenes Budget.
const budgetErhoeht = new WeakSet<TestInfo>();

function erhoeheTestBudgetEinmalig(): void {
	const info = test.info();
	if (budgetErhoeht.has(info)) {
		return;
	}
	budgetErhoeht.add(info);
	info.setTimeout(info.timeout + LOGIN_ZUSATZ_ZEITBUDGET_MS);
}

async function loginViaSchemaDropdown(page: Page, env: { baseUrl: string; schema: string }) {
	erhoeheTestBudgetEinmalig();
	// Unter Last liefert der Login-POST manchmal einen Server-Fehler, die SPA
	// landet dann auf /#/login?redirect=/ bzw. /#/error statt der Zielroute;
	// gelegentlich fehlt auch das Schema-Dropdown oder dessen Option
	// (login() ist ein gemeinsamer Helper — betrifft dadurch viele
	// unabhängige Testdateien). Jeder Versuch lädt die Login-Seite neu und
	// führt die komplette Sequenz aus; scheitert irgendein Schritt, wird der
	// Grund vermerkt und neu versucht. Nach dem letzten Versuch erscheinen alle
	// Gründe gebündelt in der Fehlermeldung.
	const fehler: string[] = [];
	for (let versuch = 1; versuch <= LOGIN_VERSUCHE; versuch++) {
		try {
			await page.goto(env.baseUrl, { waitUntil: 'networkidle', timeout: 30000 });
			await page.getByLabel('Datenbank-Schema').waitFor({ state: 'visible', timeout: 10000 });
			await page.getByLabel('Datenbank-Schema').click({ timeout: 10000 });
			await page.getByRole('option', { name: env.schema, exact: true }).click({ timeout: 10000 });
			await page.getByLabel('Benutzername').fill('Admin', { timeout: 10000 });
			await page.getByLabel('Passwort').fill('', { timeout: 10000 });
			await page.getByRole('button', { name: /anmelden|login/i }).click({ timeout: 10000 });
			await page.waitForURL(`**/${env.schema}/**`, { timeout: 20000 });
			return;
		} catch (e) {
			const grund = e instanceof Error ? (e.message.split('\n')[0] ?? e.message) : String(e);
			fehler.push(`Versuch ${versuch}: ${grund}`);
		}
	}
	throw new Error(`Login fehlgeschlagen nach ${LOGIN_VERSUCHE} Versuchen (Schema: ${env.schema})\n${fehler.join('\n')}`);
}

export async function login(page: Page) {
	if (process.env.VITE_targetHost !== undefined && process.env.VITE_targetHost !== '') {
		await page.goto(process.env.VITE_targetHost, { waitUntil: 'networkidle', timeout: 30000 });
		await page.getByLabel('Benutzername').waitFor({ state: 'visible', timeout: 10000 });
		await page.getByLabel('Benutzername').fill('Admin');
		await page.getByLabel('Passwort').fill('');
		await page.getByRole('button', { name: /anmelden|login/i }).click();
		await page.waitForURL(url => !url.href.includes('/login'), { timeout: 20000 });
		return;
	}
	await loginViaSchemaDropdown(page, ENVIRONMENTS[getEnv()]);
}

