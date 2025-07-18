import { describe, expect, test } from "vitest";
import type { LehrerListeEintrag, List } from "@core";
import { privilegedApiServer } from "../../utils/APIUtils";

describe("Lehrer Tests ", () => {
	describe.each([{schema: "GymAbi01"}])('gegen %s', ({schema}) => {
		const api = privilegedApiServer;

		test("getLehrerListe", async () => {
			const liste: List<LehrerListeEintrag> = await api.getLehrer(schema);
			expect(liste).toMatchSnapshot();
			expect(liste.get(0)).toMatchSnapshot();
		});

		test("getLehrerPersonaldaten", async () => {
			const result = await api.getLehrerPersonaldaten(schema, 76);
			expect(result).toMatchSnapshot();
		});

		test("getLehrerStammdaten", async () => {
			const result = await api.getLehrerStammdaten(schema, 76);
			expect(result).toMatchSnapshot();
		});

		test("getLehrerLeitungsfunktionen", async () => {
			const result = await api.getLehrerLeitungsfunktionen(schema);
			expect(result).toMatchSnapshot();
		});

	})

})
