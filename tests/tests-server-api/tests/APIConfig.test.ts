import { describe, expect, test } from "vitest";
import { privilegedApiServer } from "../../utils/APIUtils";
import { DBSchemaListeEintrag } from "../../../svws-webclient/core/src/core/data/db/DBSchemaListeEintrag";

const allowDestructiveTests = process.env.MODE === 'allowDestructiveTests'

describe("Config Tests", () => {
	describe.each([{schema: "GymAbi01"}])('gegen %s', ({schema}) => {
		const api = privilegedApiServer;

		test.skip("setClientConfigGlobalKey", async () => {
			// const result = await api.setClientConfigGlobalKey(schema, 1)
			// expect(result).toBeTruthy();
		});

		test.skip("setClientConfigUserKey", async () => {
			// const result = await api.setClientConfigUserKey(schema, 1)
			// expect(result).toBeTruthy();
		});

		test("getConfigCertificate", async () => {
			const result = await api.getConfigCertificate();
			expect(result).toBeDefined();
		});

		test("getConfigCertificateBase64", async () => {
			const result = await api.getConfigCertificateBase64();
			expect(result).toBeDefined();
		});

		test.runIf(allowDestructiveTests)("getConfigDBSchemata: hole Liste mit DBs", async () => {
			const result = await api.getConfigDBSchemata();
			expect(result).toMatchSnapshot();
			expect(result.toArray()[0]).toBeInstanceOf(DBSchemaListeEintrag);
		});

		test("getConfigPublicKeyBase64", async () => {
			const result = await api.getConfigPublicKeyBase64();
			expect(result).toBeDefined();
		});
	})
})
