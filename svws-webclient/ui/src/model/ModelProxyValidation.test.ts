import { describe, expect, test } from "vitest";
import { BasicValidator } from "../../../core/src/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "../../../core/src/asd/validate/ValidatorFehlerart";
import { ModelProxyValidation } from "./ModelProxyValidation";
import { ref } from "vue";

describe("ModelProxyValidation Testsuite", () => {
	describe("ModelProxyValidation Initialisierung", () => {
		test("ModelProxyValidation wird erfolgreich mit zwei Validatoren initialisiert", () => {
			const initialModel = ref<TestModel>({ id: 1, firstName: 'Max', lastName: 'Mustermann' });
			const modelValidation = getModelProxyValidation(() => initialModel.value, false);

			expect(modelValidation).toBeTruthy();
			expect(modelValidation.getAlleFehler().isEmpty()).toBe(true);
		});

		test("ModelProxyValidation Initialisierung schlägt fehl, wenn ein Validator doppelt zugewiesen wird", () => {
			const initialModel = ref<TestModel>({ id: 1, firstName: 'Max', lastName: 'Mustermann' });
			const modelValidation = new ModelProxyValidation<TestModel>(false, []);
			expect(modelValidation).toBeTruthy();

			const validatorFirstName = new FirstNameValidatorMock(() => initialModel.value.firstName);
			modelValidation.addValidator(validatorFirstName, "firstName");
			expect(() => modelValidation.addValidator(validatorFirstName, "firstName")).toThrowError("Ein Validator sollte nur einmalig zu der Konfiguration hinzugefügt werden. Bitte fassen sie die Aufrufe zusammen.");
		});

		test("ModelProxyValidation Initialisierung führt, wenn konfiguriert, eine initiale Validierung aus", () => {
			const initialModel = ref<TestModel>({ id: 1, firstName: '', lastName: 'Mustermann' });
			const modelValidation = getModelProxyValidation(() => initialModel.value, true);

			expect(modelValidation.getFehler("firstName").size()).toBe(1);
			expect(modelValidation.getFehler("lastName").size()).toBe(0);
			expect(modelValidation.getAlleFehler().size()).toBe(1);
		});

	});

	describe("ModelProxyValidation Validierung", () => {

		test("ModelProxyValidation Validierung wird erfolgreich durchgeführt.", () => {
			const initialModel = ref<TestModel>({ id: 1, firstName: 'Max', lastName: 'Mustermann' });
			const validatorLastName = new LastNameValidatorMock(() => initialModel.value.lastName, () => initialModel.value.firstName);
			const validatorFirstName = new FirstNameValidatorMock(() => initialModel.value.firstName);

			const modelValidation = new ModelProxyValidation<TestModel>(false, ["firstName"]);
			modelValidation.addValidator(validatorFirstName, "firstName", "lastName"); // lastName ist hier nicht nötig, wird aber für die Coverage ergänzt.
			modelValidation.addValidator(validatorLastName, "lastName", "firstName");
			modelValidation.validate();

			expect(modelValidation.getAlleFehler().isEmpty()).toBe(true);
		});

		test("ModelProxyValidation Validatoren werden korrekt registriert.", () => {
			const initialModel = ref<TestModel>({ id: 1, firstName: 'Max', lastName: 'Mustermann' });
			const validatorLastName = new LastNameValidatorMock(() => initialModel.value.lastName, () => initialModel.value.firstName);
			const validatorFirstName = new FirstNameValidatorMock(() => initialModel.value.firstName);

			const modelValidation = new ModelProxyValidation<TestModel>(false, ["firstName"]);
			modelValidation.addValidator(validatorFirstName, "firstName");
			modelValidation.addValidator(validatorLastName, "lastName", "firstName");

			// eslint-disable-next-line @typescript-eslint/dot-notation
			const isValidatorForProp = modelValidation['isValidatorForProp'] as (prop: keyof TestModel, validator: BasicValidator) => boolean;
			expect(isValidatorForProp.call(modelValidation, "id", validatorFirstName)).toBe(false);
			expect(isValidatorForProp.call(modelValidation, "id", validatorLastName)).toBe(false);
			expect(isValidatorForProp.call(modelValidation, "firstName", validatorFirstName)).toBe(true);
			expect(isValidatorForProp.call(modelValidation, "firstName", validatorLastName)).toBe(true);
			expect(isValidatorForProp.call(modelValidation, "lastName", validatorFirstName)).toBe(false);
			expect(isValidatorForProp.call(modelValidation, "lastName", validatorLastName)).toBe(true);

			// eslint-disable-next-line @typescript-eslint/dot-notation
			const getPropsForValidator = modelValidation['getPropsForValidator'] as (validator: BasicValidator) => ReadonlySet<keyof TestModel>;
			expect(getPropsForValidator.call(modelValidation, validatorFirstName)).toEqual(new Set(["firstName"]));
			expect(getPropsForValidator.call(modelValidation, validatorLastName)).toEqual(new Set(["lastName", "firstName"]));

			// eslint-disable-next-line @typescript-eslint/dot-notation
			const getPropForValidatorResults = modelValidation['getPropForValidatorResults'] as (validator: BasicValidator) => keyof TestModel | null;
			expect(getPropForValidatorResults.call(modelValidation, validatorFirstName)).toBe("firstName");
			expect(getPropForValidatorResults.call(modelValidation, validatorLastName)).toBe("lastName");
		});

		test("ModelProxyValidation Validierung kann mit enable, disable und toggle für eine Property deaktiviert werden.", () => {
			const initialModel = ref<TestModel>({ id: 1, firstName: '', lastName: '' });

			const validatorFirstName = new FirstNameValidatorMock(() => initialModel.value.firstName);
			const validatorLastName = new LastNameValidatorMock(() => initialModel.value.lastName, () => initialModel.value.firstName);

			const modelValidation = new ModelProxyValidation<TestModel>(true, ["firstName"]);
			modelValidation.addValidator(validatorFirstName, "firstName");
			modelValidation.addValidator(validatorLastName, "lastName", "firstName");

			// Der Validator für lastName erzeugt zwei Fehler, der Validator für firstName wurde deaktiviert
			expect(modelValidation.getFehler("firstName").size()).toBe(0);
			expect(modelValidation.getFehler("lastName").size()).toBe(2);
			expect(modelValidation.getAlleFehler().size()).toBe(2);

			// Aktiviere den Validator für firstName, es wird ein Fehler mehr gemeldet
			modelValidation.enable("firstName");
			expect(modelValidation.getFehler("firstName").size()).toBe(1);
			expect(modelValidation.getFehler("lastName").size()).toBe(2);
			expect(modelValidation.getAlleFehler().size()).toBe(3);

			// Deaktiviere den Validator für lastName, es werden zwei Fehler weniger gemeldet
			modelValidation.disable("lastName");
			expect(modelValidation.getFehler("firstName").size()).toBe(1);
			expect(modelValidation.getFehler("lastName").size()).toBe(0);
			expect(modelValidation.getAlleFehler().size()).toBe(1);

			// Ein Toggle auf den Validator für firstName deaktiviert diesen wieder
			modelValidation.toggle("firstName");
			expect(modelValidation.getFehler("firstName").size()).toBe(0);
			expect(modelValidation.getFehler("lastName").size()).toBe(0);
			expect(modelValidation.getAlleFehler().size()).toBe(0);

			// Ein Toggle auf den Validator für lastName aktiviert diesen wieder
			modelValidation.toggle("lastName");
			expect(modelValidation.getFehler("firstName").size()).toBe(0);
			expect(modelValidation.getFehler("lastName").size()).toBe(2);
			expect(modelValidation.getAlleFehler().size()).toBe(2);

			// Ein disable auf den Validator für firstName ändert nichts
			modelValidation.disable("firstName");
			expect(modelValidation.getFehler("firstName").size()).toBe(0);
			expect(modelValidation.getFehler("lastName").size()).toBe(2);
			expect(modelValidation.getAlleFehler().size()).toBe(2);

			// Ein enable auf den Validator für lastName ändert auch nichts
			modelValidation.enable("lastName");
			expect(modelValidation.getFehler("firstName").size()).toBe(0);
			expect(modelValidation.getFehler("lastName").size()).toBe(2);
			expect(modelValidation.getAlleFehler().size()).toBe(2);
		});

	});

});


interface TestModel {
	id: number,
	firstName: string,
	lastName: string,
	email?: string | null,
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

function getModelProxyValidation(data: () => TestModel, autoRevalidate: boolean, disabled: Iterable<keyof TestModel> = []): ModelProxyValidation<TestModel> {
	const modelValidation = new ModelProxyValidation<TestModel>(autoRevalidate, disabled);
	modelValidation.addValidator(new FirstNameValidatorMock(() => data().firstName), 'firstName');
	modelValidation.addValidator(new LastNameValidatorMock(() => data().lastName, () => data().firstName), 'lastName', 'firstName');
	return modelValidation;
}