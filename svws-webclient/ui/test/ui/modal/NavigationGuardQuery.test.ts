import { describe, expect, test } from "vitest";

import { NavigationGuardQuery } from "@ui/ui/modal/NavigationGuardQuery";

describe("NavigationGuardQuery", () => {

	test("ist zunächst geschlossen", () => {
		expect(new NavigationGuardQuery().isOpen).toBe(false);
	});

	test("frage öffnet die Abfrage und liefert die Antwort", async () => {
		const query = new NavigationGuardQuery();
		const reply = query.query();
		expect(query.isOpen).toBe(true);
		query.reply("discard");
		await expect(reply).resolves.toBe("discard");
		expect(query.isOpen).toBe(false);
	});

	test("jede Antwort wird weitergegeben", async () => {
		const query = new NavigationGuardQuery();
		for (const erwartet of ["cancel", "discard", "save"] as const) {
			const reply = query.query();
			query.reply(erwartet);
			await expect(reply).resolves.toBe(erwartet);
		}
	});

	test("eine zweite Abfrage beantwortet die erste mit abbrechen", async () => {
		const query = new NavigationGuardQuery();
		const erste = query.query();
		const zweite = query.query();
		await expect(erste).resolves.toBe("cancel");
		expect(query.isOpen).toBe(true);
		query.reply("save");
		await expect(zweite).resolves.toBe("save");
	});

	test("antworten ohne offene Abfrage tut nichts", () => {
		const query = new NavigationGuardQuery();
		expect(() => query.reply("discard")).not.toThrow();
		expect(query.isOpen).toBe(false);
	});

	test("ohne Angaben gelten die Standardangaben: keine Bereiche, Speichern möglich, keine Hinweise", () => {
		const query = new NavigationGuardQuery();
		expect(query.info).toEqual({ bereiche: [], isPatchAllowed: true, patchHinweise: [] });
		void query.query();
		expect(query.info).toEqual({ bereiche: [], isPatchAllowed: true, patchHinweise: [] });
	});

	test("die Angaben des Guards gelten, solange die Abfrage offen ist, danach wieder die Standardangaben", async () => {
		const query = new NavigationGuardQuery();
		const info = { bereiche: ["Individualdaten"], isPatchAllowed: false, patchHinweise: ["Zuzugsjahr ungültig"] };
		const reply = query.query(info);
		expect(query.info).toEqual(info);
		query.reply("cancel");
		await reply;
		expect(query.info).toEqual({ bereiche: [], isPatchAllowed: true, patchHinweise: [] });
	});

	test("eine zweite Abfrage ersetzt die Angaben der ersten", async () => {
		const query = new NavigationGuardQuery();
		const erste = query.query({ bereiche: ["A"], isPatchAllowed: true, patchHinweise: [] });
		void query.query({ bereiche: ["B"], isPatchAllowed: true, patchHinweise: [] });
		await erste;
		expect(query.info.bereiche).toEqual(["B"]);
	});

});
