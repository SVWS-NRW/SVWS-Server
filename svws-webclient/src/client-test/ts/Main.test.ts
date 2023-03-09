// Wir verwenden xhr2, damit wir die openapi-ts auch in Node verwenden können.
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
//@ts-ignore
global.XMLHttpRequest = require("xhr2");
import { Schuljahresabschnitt } from "@svws-nrw/svws-core";
import { describe, test, expect, beforeAll } from "vitest";
import { Main } from "~/apps/Main";

let instance: Main;

describe("Main", () => {
	beforeAll(async () => {
		instance = new Main();
		await instance.connectTo("localhost");
	});
	test("connectTo: verbindet sich mit dem Server", async () => {
		expect(instance.schema).toBe("svwsschema");
	});
	test("authenticate: authentifiziert den Benutzer", async () => {
		await instance.authenticate("Admin", "");
		expect(instance.authenticated).toBeTruthy();
	});
	test("authenticate: authentifiziert nicht den falschen Benutzer", async () => {
		await instance.authenticate("Admin", "wrong");
		expect(instance.authenticated).toBeFalsy();
	});
	test("getter apps: Alle Apps holen", () => {
		expect(instance.apps.schueler).toBeDefined();
		expect(instance.apps.lehrer).toBeDefined();
		expect(instance.apps.kurse).toBeDefined();
		expect(instance.apps.klassen).toBeDefined();
		expect(instance.apps.jahrgaenge).toBeDefined();
		expect(instance.apps.schule).toBeDefined();
	});
	test.todo("getter akt_abschnitt: gibt aktuellen Abschnitt zurück", () => {
		expect(instance.akt_abschnitt).toBeInstanceOf(Schuljahresabschnitt);
	});
	test("setter akt_abschnitt: setze aktuellen Abschnitt", () => {
		instance.akt_abschnitt = new Schuljahresabschnitt();
		expect(instance.akt_abschnitt).toBeInstanceOf(Schuljahresabschnitt);
	});
	test("logout: Benutzer wird abgemeldet", () => {
		instance.logout();
		expect(instance.authenticated).toBeFalsy();
	});
});
