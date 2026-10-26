import { privilegedApiServer } from "@testUtils/APIUtils";
import { describe, expect, test } from "vitest";

import { ENMLehrerFunktion } from "@core/core/data/enm/ENMLehrerFunktion";
import { ArrayList } from "@core/java/util/ArrayList";

describe("APIENM Schulleitung Tests", () => {
	describe.each([{ schema: "GymAbi01" }])('gegen %s', ({ schema }) => {
		const api = privilegedApiServer;

		test("getLehrerENMv2Daten | Mehrere Lehrer mit SL und Stv. SL dürfen die Funktion Schulleitung nutzen", async () => {
			const vorhanden = new Set((await api.getAllSchulleitungen(schema)).toArray().map(s => s.idLehrer));
			const lehrer = (await api.getLehrer(schema)).toArray().map(l => l.id).filter(id => !vorhanden.has(id));
			const [idSl1, idSl2, idStv1, idStv2, idOhne] = lehrer;
			const ids = new ArrayList<number>();
			try {
				for (const [idLehrer, idLeitungsfunktion] of [[idSl1, 1], [idSl2, 1], [idStv1, 2], [idStv2, 2]]) {
					const eintrag = await api.addSchulleitung({ idLehrer, idLeitungsfunktion, bezeichnung: "Test", datumBeginnLeitungsfunktion: "2000-01-01", datumEndeLeitungsfunktion: null }, schema);
					ids.add(eintrag.id);
				}
				// Alle Mitglieder der Schulleitung erhalten die Daten, nicht nur der zuletzt eingetragene
				for (const idLehrer of [idSl1, idSl2, idStv1, idStv2]) {
					const daten = await api.getLehrerENMv2Daten(schema, idLehrer, ENMLehrerFunktion.SCHULLEITUNG.id);
					expect(daten.lehrerID).toBe(idLehrer);
				}
				// Ein Lehrer ohne Leitungsfunktion darf die Funktion Schulleitung weiterhin nicht nutzen
				await expect(api.getLehrerENMv2Daten(schema, idOhne, ENMLehrerFunktion.SCHULLEITUNG.id)).rejects.toThrow();
			} finally {
				if (!ids.isEmpty())
					await api.deleteSchulleitungen(ids, schema);
			}
		});
	});
});
