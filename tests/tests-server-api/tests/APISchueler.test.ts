import { describe, expect, test } from "vitest";
import { getApiServer } from "./utils/TestUtils.js";

describe("Schueler Tests ", () => {
	describe.each([{schema: "GymAbi01"}])('gegen %s', ({schema}) => {
		const api = getApiServer("gymabi");

		test("getSchuelerErzieher", async () => {
			const result = await api.getSchuelerErzieher(schema, 1199);
			expect(result).toMatchSnapshot();
		});

		test("getSchuelerSchulbesuch", async () => {
			const result = await api.getSchuelerSchulbesuch(schema, 1199);
			expect(result).toMatchSnapshot();
		});

		test("getSchuelerStammdaten", async () => {
			const result = await api.getSchuelerStammdaten(schema, 1199);
			expect(result).toMatchSnapshot();
		});

		test("getSchuelerFuerAbschnitt", async () => {
			const result = await api.getSchuelerFuerAbschnitt(schema, 13);
			expect(result).toMatchSnapshot();
		});

		test("getSchuelerAktuell", async () => {
			const result = await api.getSchuelerAktuell(schema);
			expect(result).toMatchSnapshot();
		});

		test("getSchuelerFahrschuelerarten", async () => {
			const result = await api.getSchuelerFahrschuelerarten(schema);
			expect(result).toMatchSnapshot();
		});

		test("getSchuelerFoerderschwerpunkte", async () => {
			const result = await api.getSchuelerFoerderschwerpunkte(schema);
			expect(result).toMatchSnapshot();
		});
	})
})
