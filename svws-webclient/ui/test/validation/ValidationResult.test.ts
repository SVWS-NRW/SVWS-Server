import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehler } from "@core/asd/validate/ValidatorFehler";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
import { ArrayList } from "@core/java/util/ArrayList";
import { ValidationResult } from "@ui/validation/ValidationResult";
import { describe, expect, test } from "vitest";


describe("initialize ValidationResult", () => {
	test("kein Fehler vorhanden", () => {
		const result = new ValidationResult(new ArrayList());

		expect(result).toBeTruthy();
		expect(result.fehler.size()).equals(0);
		expect(result.hasFehler).toBeFalsy();
		expect(result.fehlerart).equals(ValidatorFehlerart.UNGENUTZT);
	});

	test("ein Fehler (Hinweis) vorhanden", () => {
		const fehlerliste = new ArrayList<ValidatorFehler>();
		fehlerliste.add(new ValidatorFehler(new MockValidator(ValidatorFehlerart.HINWEIS), 0, "Testfehler"));

		const result = new ValidationResult(fehlerliste);

		expect(result).toBeTruthy();
		expect(result.fehler.size()).equals(1);
		expect(result.hasFehler).toBeTruthy();
		expect(result.fehlerart).equals(ValidatorFehlerart.HINWEIS);
	});

	test("zwei Fehler (Kann, Muss) vorhanden", () => {
		const fehlerliste = new ArrayList<ValidatorFehler>();
		fehlerliste.add(new ValidatorFehler(new MockValidator(ValidatorFehlerart.KANN), 0, "Testfehler1"));
		fehlerliste.add(new ValidatorFehler(new MockValidator(ValidatorFehlerart.MUSS), 1, "Testfehler2"));

		const result = new ValidationResult(fehlerliste);

		expect(result).toBeTruthy();
		expect(result.fehler.size()).equals(2);
		expect(result.hasFehler).toBeTruthy();
		expect(result.fehlerart).equals(ValidatorFehlerart.MUSS);
	});

	test("drei Fehler (Hinweis, Kann, Muss) vorhanden", () => {
		const fehlerliste = new ArrayList<ValidatorFehler>();
		fehlerliste.add(new ValidatorFehler(new MockValidator(ValidatorFehlerart.HINWEIS), 0, "Testfehler1"));
		fehlerliste.add(new ValidatorFehler(new MockValidator(ValidatorFehlerart.KANN), 1, "Testfehler2"));
		fehlerliste.add(new ValidatorFehler(new MockValidator(ValidatorFehlerart.MUSS), 2, "Testfehler3"));

		const result = new ValidationResult(fehlerliste);

		expect(result).toBeTruthy();
		expect(result.fehler.size()).equals(3);
		expect(result.hasFehler).toBeTruthy();
		expect(result.fehlerart).equals(ValidatorFehlerart.MUSS);
	});
});


class MockValidator extends BasicValidator {

	public constructor(fehlerart: ValidatorFehlerart) {
		super(fehlerart);
	}

	protected pruefe(): boolean {
		return true;
	}
}
