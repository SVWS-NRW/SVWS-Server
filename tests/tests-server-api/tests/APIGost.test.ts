import { describe, expect, test } from "vitest";
import { privilegedApiServer } from "../../utils/APIUtils";
import { GostFach } from "../../../svws-webclient/core/src/core/data/gost/GostFach";

const allowDestructiveTests = process.env.MODE === 'allowDestructiveTests'

describe("Gesamtschule Tests", () => {
	describe.each([{schema: "GymAbi01"}])('gegen %s', ({schema}) => {
		const api = privilegedApiServer;

		test("getGostAbiturjahrgaenge", async () => {
			const result = await api.getGostAbiturjahrgaenge(schema);
			expect(result).toMatchSnapshot();
		});

		test("getGostAbiturjahrgang", async () => {
			const result = await api.getGostAbiturjahrgang(schema, 2021);
			expect(result).toMatchSnapshot();
		});

		test("getGostAbiturjahrgangBlockungsliste", async () => {
			const result = await api.getGostAbiturjahrgangBlockungsliste(schema, 2021, 3);
			expect(result).toMatchSnapshot();
		});

		test.runIf(allowDestructiveTests)("createGostAbiturjahrgangBlockung", async () => {
			const result = await api.createGostAbiturjahrgangBlockung(schema, 2021, 1);
			expect(result).toBeTruthy();
		});

		// TODO: FIX ME
		test("getGostAbiturjahrgangFach", async () => {
			const result = await api.getGostAbiturjahrgangFach(schema, 2021, 16);
			expect(result).toMatchSnapshot();
			expect(result).toBeInstanceOf(GostFach);
		});

		test.skip("patchGostAbiturjahrgangFach", async () => {
			await api.patchGostAbiturjahrgangFach({bezeichnung: "NewName"}, schema, 2021, 16);
			expect((await api.getGostAbiturjahrgangFach(schema, 2021, 16)).bezeichnung).toEqual("NewName");
		});

		// TODO: FIX ME
		test.skip("getGostAbiturjahrgangHalbjahrFachwahlen", async () => {
			// const result = await api.getGostAbiturjahrgangHalbjahrFachwahlen(schema, 2021);
			// expect(result).toMatchSnapshot();
		});

		test("getGostAbiturjahrgangFaecher", async () => {
			const result = await api.getGostAbiturjahrgangFaecher(schema, 2021);
			expect(result).toMatchSnapshot();
		});

		// TODO: FIX ME
		test.skip("getGostAbiturBelegpruefungEF1", async () => {
			// const result = await server.getGostAbiturBelegpruefungEF1();
			// expect(result).toMatchSnapshot();
		});

		// TODO: FIX ME
		test.skip("getGostAbiturBelegpruefungGesamt", async () => {
			// const result = await api.getGostAbiturBelegpruefungGesamt();
			// expect(result).toMatchSnapshot();
		});

		test.skip("getGostBlockung", async () => {
			const result = await api.getGostBlockung(schema, 1);
			expect(result).toMatchSnapshot();
		});

		// TODO: FIX ME
		test.skip("getGostFach", async () => {
			// const result = await api.getGostFach(schema, 16);
			// expect(result).toMatchSnapshot();
		});

		// TODO: FIX ME
		test.skip("patchGostFach", async () => {
			// const result = await api.patchGostFach(schema, 1)
			// expect(result).toBeTruthy()
		});

		// TODO: FIX ME
		test.skip("getGostFaecher", async () => {
			// const result = await api.getGostFaecher(schema);
			// expect(result).toMatchSnapshot();
		});

		// TODO: FIX ME
		test.skip("getGostSchuelerAbiturdaten", async () => {
			const result = await api.getGostSchuelerAbiturdaten(schema, 1199);
			expect(result).toMatchSnapshot();
		});

		test.skip("getGostSchuelerLaufbahnplanung", async () => {
			const result = await api.getGostSchuelerLaufbahnplanung(schema, 1199);
			expect(result).toMatchSnapshot();
		});

		test("getGostSchuelerLeistungsdaten", async () => {
			const result = await api.getGostSchuelerLeistungsdaten(schema, 1199);
			expect(result).toMatchSnapshot();
		});

		// TODO: FIX ME
		test.skip("getGostSchuelerFachwahl", async () => {
			//Parameter ok, mit der alten API getestet
			const result = await api.getGostSchuelerFachwahl(schema, 1199, 16);
			expect(result).toMatchSnapshot();
		});

		// TODO: FIX ME
		test.skip("patchGostBlockung", async () => {
			// const result = await api.patchGostBlockung(schema, 1)
			// expect(result).toBeTruthy()
		});

		// TODO: FIX ME
		test.skip("patchGostBlockungKurs", async () => {
			// const result = await api.patchGostBlockungKurs(schema, 1)
			// expect(result).toBeTruthy()
		});

		// TODO: FIX ME
		test.skip("patchGostBlockungRegel", async () => {
			// const result = await api.patchGostBlockungRegel(schema, 1)
			// expect(result).toBeTruthy()
		});

		// TODO: FIX ME
		test.skip("patchGostBlockungSchiene", async () => {
			// const result = await api.patchGostBlockungSchiene(schema, 1)
			// expect(result).toBeTruthy()
		});

		// TODO: FIX ME
		test.skip("patchGostSchuelerFachwahl", async () => {
			// const result = await api.patchGostSchuelerFachwahl(schema, 1199, 16);
			// expect(result).toBeTruthy();
		});
	})
})
