import { describe, expect, test } from "vitest";
import { BasicValidator } from "../../../../svws-webclient/core/src/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "../../../../svws-webclient/core/src/asd/validate/ValidatorFehlerart";
import type { ModelProxyConfiguration } from "./ModelProxy";
import { ModelProxy } from "./ModelProxy";
import { nextTick, ref } from "vue";

describe("ModelProxy Testsuite", () => {
	describe("ModelProxy Initialisierung", () => {
		test("ModelProxy wird erfolgreich initialisiert", () => {
			const initialModel = ref<TestModel>({ id: 1, firstName: 'Max', lastName: 'Mustermann' });
			const modelProxy = new ModelProxyMock({ data: () => initialModel.value }, false);

			expect(modelProxy).toBeTruthy();
			expect(modelProxy.proxy).toBeTruthy();
			expect(modelProxy.getAlleFehler().isEmpty()).toBe(true);
			expect(modelProxy.hatFehler()).toBe(false);
			expect(modelProxy.data).toEqual(initialModel.value);
		});
	});

	describe("ModelProxy Validierung", () => {
		test("Fehler bei initialer Validierung", () => {
			const initialModel = ref<TestModel>({ id: 1, firstName: '', lastName: '' });
			const modelProxy = new ModelProxyMock({ data: () => initialModel.value }, true);
			expect(modelProxy.getAlleFehler().size()).toBe(3);
			expect(modelProxy.hatFehler()).toBe(true);
			expect(modelProxy.getFehler('firstName').size()).toBe(1);
			expect(modelProxy.getFehler('firstName').get(0)).toEqual(
				expect.objectContaining({
					_pruefschritt: 0,
					_fehlermeldung: 'FirstName is empty',
				})
			);
			expect(modelProxy.getFehler('lastName').size()).toBe(2);
			expect(modelProxy.getFehler('lastName').get(0)).toEqual(
				expect.objectContaining({
					_pruefschritt: 0,
					_fehlermeldung: 'LastName is empty',
				})
			);
			expect(modelProxy.getFehler('lastName').get(1)).toEqual(
				expect.objectContaining({
					_pruefschritt: 1,
					_fehlermeldung: 'LastName and FirstName must be different',
				})
			);
		});

		test("Kein Fehler bei initialer Validierung", () => {
			const initialModel = ref<TestModel>({ id: 1, firstName: 'Max', lastName: 'Mustermann' });
			const modelProxy = new ModelProxyMock({ data: () => initialModel.value }, true);

			expect(modelProxy.proxy).toBeTruthy();
			expect(modelProxy.getAlleFehler().size()).toBe(0);
			expect(modelProxy.getFehler('firstName').isEmpty()).toBe(true);
			expect(modelProxy.getFehler('lastName').isEmpty()).toBe(true);
		});

		test("Model wird mit zwei unzulässigen Werten verändert -> Validatoren schlagen an und es existieren drei Validierungsfehler", async () => {
			const initialModel = ref<TestModel>({ id: 1, firstName: 'Max', lastName: 'Mustermann' });
			const modelProxy = new ModelProxyMock({ data: () => initialModel.value }, false);

			expect(modelProxy.getAlleFehler().size()).toBe(0);

			// Model wird unzulässig verändert -> Es werden Validierungsfehler erzeugt
			modelProxy.proxy.firstName = '';
			modelProxy.proxy.lastName = '';

			expect(modelProxy.getAlleFehler().size()).toBe(3);
			expect(modelProxy.getFehler('firstName').size()).toBe(1);
			expect(modelProxy.getFehler('firstName').get(0)).toEqual(
				expect.objectContaining({
					_pruefschritt: 0,
					_fehlermeldung: 'FirstName is empty',
				})
			);
			expect(modelProxy.getFehler('lastName').size()).toBe(2);
			expect(modelProxy.getFehler('lastName').get(0)).toEqual(
				expect.objectContaining({
					_pruefschritt: 0,
					_fehlermeldung: 'LastName is empty',
				})
			);
			expect(modelProxy.getFehler('lastName').get(1)).toEqual(
				expect.objectContaining({
					_pruefschritt: 1,
					_fehlermeldung: 'LastName and FirstName must be different',
				})
			);
		});

		test("Model wird per setPendingState mit zwei unzulässigen Werten verändert -> Validatoren schlagen an und es existieren drei Validierungsfehler", async () => {
			const initialModel = ref<TestModel>({ id: 1, firstName: 'Max', lastName: 'Mustermann' });
			const modelProxy = new ModelProxyMock({ data: () => initialModel.value }, false);

			expect(modelProxy.getAlleFehler().size()).toBe(0);

			// Model wird unzulässig verändert -> Es werden Validierungsfehler erzeugt
			modelProxy.pending = {
				firstName: '',
				lastName: '',
			};

			expect(modelProxy.getAlleFehler().size()).toBe(3);
			expect(modelProxy.getFehler('firstName').size()).toBe(1);
			expect(modelProxy.getFehler('firstName').get(0)).toEqual(
				expect.objectContaining({
					_pruefschritt: 0,
					_fehlermeldung: 'FirstName is empty',
				})
			);
			expect(modelProxy.getFehler('lastName').size()).toBe(2);
			expect(modelProxy.getFehler('lastName').get(0)).toEqual(
				expect.objectContaining({
					_pruefschritt: 0,
					_fehlermeldung: 'LastName is empty',
				})
			);
			expect(modelProxy.getFehler('lastName').get(1)).toEqual(
				expect.objectContaining({
					_pruefschritt: 1,
					_fehlermeldung: 'LastName and FirstName must be different',
				})
			);
		});

		test("Model LastName wird auf gleichen Wert wie FirstName gesetzt -> Validierung sowohl für FirstName als auch LastName schlägt an", async () => {
			const initialModel = ref<TestModel>({ id: 1, firstName: 'Max', lastName: 'Mustermann' });
			const modelProxy = new ModelProxyMock({ data: () => initialModel.value }, true);

			expect(modelProxy.getAlleFehler().size()).toBe(0);

			// Model wird unzulässig verändert -> Es werden Validierungsfehler erzeugt
			modelProxy.proxy.firstName = 'Mustermann';

			expect(modelProxy.getAlleFehler().size()).toBe(1);
			expect(modelProxy.getFehler('firstName').isEmpty()).toBe(true);
			expect(modelProxy.getFehler('lastName').size()).toBe(1);
			expect(modelProxy.getFehler('lastName').get(0)).toEqual(
				expect.objectContaining({
					_pruefschritt: 1,
					_fehlermeldung: 'LastName and FirstName must be different',
				})
			);
		});

		test("ModelProxy initialData Ref wird nachträglich verändert -> Proxy wird aktualisiert", async () => {
			const initialModel = ref<TestModel>({ id: 1, firstName: 'Max', lastName: 'Mustermann' });
			const modelProxy = new ModelProxyMock({ data: () => initialModel.value }, false);

			expect(modelProxy).toBeTruthy();
			const proxyObject = modelProxy.proxy;
			expect(proxyObject).toEqual(
				expect.objectContaining({
					id: 1,
					firstName: 'Max',
					lastName: 'Mustermann',
				})
			);

			// inital Data Ref wird verändert -> Proxy Objekt wird neu erzeugt
			initialModel.value = { ...initialModel.value, firstName: 'Maria', lastName: 'Musterfrau' };

			await nextTick();

			expect(proxyObject !== modelProxy.proxy).toBe(true);
			expect(modelProxy.proxy).toEqual(
				expect.objectContaining({
					id: 1,
					firstName: 'Maria',
					lastName: 'Musterfrau',
				})
			);
		});

		test("ModelProxy wird für FirstName direkt nach dem Erstellen deaktiviert, anschließend wird FirstName zu einem unzulässigen Wert geändert -> Es existieren keine Validierungsfehler", async () => {
			const initialModel = ref<TestModel>({ id: 1, firstName: 'Max', lastName: 'Mustermann' });
			const modelProxy = new ModelProxyMock({ data: () => initialModel.value, listOfDisabledPropValidations: ["firstName"] }, true);

			// FirstName wird unzulässig verändert -> Es existiert kein Validierungsfehler, da die Validierung deaktiviert ist
			modelProxy.proxy.firstName = '';

			expect(modelProxy.getAlleFehler().size()).toBe(0);
		});

		test("ModelProxy-Validierung wird für FirstName zunächst deaktiviert, dieser fehlerhaft initialisiert, anschließend per toggleValidation wieder aktiviert -> Es existieren erst keine Validierungsfehler, dann aber doch", async () => {
			const initialModel = ref<TestModel>({ id: 1, firstName: '', lastName: 'Mustermann' });
			const modelProxy = new ModelProxyMock({ data: () => initialModel.value, listOfDisabledPropValidations: ["firstName"] }, true);

			expect(modelProxy.hatFehler()).toBe(false);

			// Validierung wird wieder aktiviert
			modelProxy.toggleValidation("firstName");

			expect(modelProxy.hatFehler()).toBe(true);
		});

	});


	describe("ModelProxy Model-Wechsel", () => {
		test("ModelProxy initialData Ref wird nachträglich verändert -> PendingState wird zurückgesetzt", async () => {
			const initialModel = ref<TestModel>({ id: 1, firstName: 'Max', lastName: 'Mustermann' });
			const modelProxy = new ModelProxyMock({ data: () => initialModel.value }, false);

			// PendingState soll initial leer sein
			expect(modelProxy.pending).toEqual({});

			// Model wird verändert -> PendingState existiert
			modelProxy.proxy.lastName = "Musterfrau";
			modelProxy.proxy.firstName = "Maria";
			expect(modelProxy.pending).toEqual(
				expect.objectContaining({
					firstName: 'Maria',
					lastName: 'Musterfrau',
				})
			);

			// initialData Ref wird verändert -> PendingState wird zurückgesetzt
			initialModel.value = { ...initialModel.value, firstName: 'Maria', lastName: 'Musterfrau' };

			await nextTick();

			expect(modelProxy.pending).toEqual({});
		});

		test("ModelProxy initialData Ref wird nachträglich verändert -> Die Validierung wird automatisch neu ausgeführt", async () => {
			const initialModel = ref<TestModel>({ id: 1, firstName: 'Max', lastName: 'Mustermann' });
			const modelProxy = new ModelProxyMock({ data: () => initialModel.value }, false);

			// initialData Ref wird mit unzulässigen verändert -> Validatoren schlagen an
			initialModel.value = { ...initialModel.value, firstName: 'Maria', lastName: 'Maria' };

			await nextTick();

			expect(modelProxy.getAlleFehler().size()).toBe(1);
			expect(modelProxy.getFehler('firstName').isEmpty()).toBe(true);
			expect(modelProxy.getFehler('lastName').size()).toBe(1);
			expect(modelProxy.getFehler('lastName').get(0)).toEqual(
				expect.objectContaining({
					_pruefschritt: 1,
					_fehlermeldung: 'LastName and FirstName must be different',
				})
			);
		});
	});


	describe("ModelProxy Patch", () => {
		test("Model wird mehrfach geändert und anschließend gepatcht -> Patch-Action wird mit den geänderten Daten aufgerufen", async () => {
			const initialModel = ref<TestModel>({ id: 1, firstName: 'Max', lastName: 'Mustermann', email: 'max.mustermann@test.de' });
			let valueAfterPatch;
			const patchAction = async (data: Partial<TestModel>): Promise<boolean> => {
				valueAfterPatch = data;
				await Promise.resolve();
				return true;
			};

			const modelProxy = new ModelProxyMock({ data: () => initialModel.value, patch: patchAction }, false);

			modelProxy.proxy.firstName = 'Maria';
			modelProxy.proxy.lastName = 'Musterfrau';
			modelProxy.proxy.email = null;

			await modelProxy.patch();
			expect(valueAfterPatch).toEqual(
				expect.objectContaining({
					firstName: 'Maria',
					lastName: 'Musterfrau',
					email: null,
				})
			);
		});

		test("Die Methode applyToPendingState wird mit einem Prop und einem ungültigen Update aufgerufen -> Exception", async () => {
			const initialModel = ref<TestModel>({ id: 1, firstName: 'Max', lastName: 'Mustermann' });
			const modelProxy = new ModelProxyMock({ data: () => initialModel.value }, false);

			await expect(async () => modelProxy.applyToPending({}, "firstName")).rejects.toThrowError("Ist der Parameter prop gesetzt, so muss das update genau ein Attribut enthalten.");
		});

		test("Es wurden keine Änderung gemacht -> Die Patch-Action wird nicht aufgerufen, weil der PendingState leer ist", async () => {
			const initialModel = ref<TestModel>({ id: 1, firstName: 'Max', lastName: 'Mustermann' });
			let dataAfterPatch = undefined;
			const patchAction = async (data: Partial<TestModel>): Promise<boolean> => {
				dataAfterPatch = data;
				await Promise.resolve();
				return true;
			};

			const modelProxy = new ModelProxyMock({ data: () => initialModel.value, patch: patchAction }, false);
			await modelProxy.patch();

			expect(dataAfterPatch).toBeUndefined();
		});

		test("FirstName wird zu unzulässigem Wert geändert und gepatcht -> Die Patch-Action wird dennoch aufgerufen und es existieren Validierungsfehler", async () => {
			const initialModel = ref<TestModel>({ id: 1, firstName: 'Max', lastName: 'Mustermann' });
			let valueAfterPatch;
			const patchAction = async (data: Partial<TestModel>): Promise<boolean> => {
				valueAfterPatch = data.firstName;
				await Promise.resolve();
				return true;
			};

			const modelProxy = new ModelProxyMock({ data: () => initialModel.value, patch: patchAction }, false);

			// FirstName wird zu unzulässigem Wert geändert und gepatcht -> Patch-Action wird nicht aufgerufen
			modelProxy.proxy.firstName = '';

			await modelProxy.patch();

			expect(modelProxy.getFehler("firstName").size()).toBe(1);
			expect(valueAfterPatch).toBeDefined();
		});

		test("FirstName wird zu unzulässigem Wert geändert und gepatcht, eine Validierung vor dem Patch ist aktiviert -> der Patch schlägt fehl", async () => {
			const initialModel = ref<TestModel>({ id: 1, firstName: 'Max', lastName: 'Mustermann' });
			let patchresult;
			const patchAction = async (data: Partial<TestModel>): Promise<boolean> => {
				patchresult = data.firstName;
				await Promise.resolve();
				return true;
			};

			const modelProxy = new ModelProxyMock({ data: () => initialModel.value, patch: patchAction, checkValidBeforePatch: true }, false);

			// FirstName wird zu unzulässigem Wert geändert und gepatcht -> Patch-Action wird nicht aufgerufen
			modelProxy.proxy.firstName = '';

			const success = await modelProxy.patch();

			expect(success).toBe(false);
			expect(modelProxy.getFehler("firstName").size()).toBe(1);
			expect(patchresult).toBeUndefined();
		});
	});

});


interface TestModel {
	id: number,
	firstName: string,
	lastName: string,
	email?: string | null,
}

class ModelProxyMock extends ModelProxy<TestModel> {
	constructor(cfg: ModelProxyConfiguration<TestModel>, immediateValidation: boolean) {
		super(cfg);
		this.addValidator(new FirstNameValidatorMock(() => this.proxy.firstName), 'firstName');
		this.addValidator(new LastNameValidatorMock(() => this.proxy.lastName, () => this.proxy.firstName), 'lastName', 'firstName');
		if (immediateValidation) {
			this.validate();
		}
	}
}

class FirstNameValidatorMock extends BasicValidator {
	private readonly firstName: () => string | null;

	constructor(firstName: () => string | null) {
		super(ValidatorFehlerart.MUSS);
		this.firstName = firstName;
	}

	protected pruefe(): boolean {
		if (this.firstName() === null || this.firstName() === '') {
			this.addFehler(0, "FirstName is empty");
			return false;
		}

		return this.getFehler().isEmpty();
	}
}

class LastNameValidatorMock extends BasicValidator {
	private readonly firstName: () => string | null;
	private readonly lastName: () => string | null;

	constructor(lastName: () => string | null, firstName: () => string | null) {
		super(ValidatorFehlerart.MUSS);
		this.lastName = lastName;
		this.firstName = firstName;
	}

	protected pruefe(): boolean {
		if (this.lastName() === null || this.lastName() === '') {
			this.addFehler(0, "LastName is empty");
		}

		if (this.lastName() === this.firstName()) {
			this.addFehler(1, "LastName and FirstName must be different");
		}

		return this.getFehler().isEmpty();
	}
}

