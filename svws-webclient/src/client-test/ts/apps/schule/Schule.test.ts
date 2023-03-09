// Wir verwenden xhr2, damit wir die openapi-ts auch in Node verwenden können.
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
//@ts-ignore
global.XMLHttpRequest = require("xhr2");
import { describe, test, expect, beforeAll } from "vitest";
import { Schule } from "~/apps/schule/Schule";
import { ListSchule } from "~/apps/schule/ListSchule";
import { App } from "~/apps/BaseApp";

describe("Schule", () => {
	let instance: Schule;

	beforeAll(async () => {
		App.setup({
			url: "localhost",
			username: "Admin",
			password: "",
			schema: "svwsschema"
		});
		instance = new Schule();
		await instance.init();
	});

	test("auswahl: liefert ein Objekt ListSchule", async () => {
		expect(instance.auswahl).toBeInstanceOf(ListSchule);
	});
	test("schuleStammdaten: ist definiert", () => {
		expect(instance.schuleStammdaten.daten?.ort).toBeDefined();
	});
});
