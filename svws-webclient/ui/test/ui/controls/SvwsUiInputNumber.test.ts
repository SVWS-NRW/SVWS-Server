import { describe, test, expect } from "vitest";
import { mount } from "@vue/test-utils";
import SvwsUiInputNumber from "@ui/ui/controls/SvwsUiInputNumber.vue";
import { BasicValidator } from "@core/asd/validate/BasicValidator";
import type { ValidatorFehler } from "@core/asd/validate/ValidatorFehler";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
import type { List } from "@core/java/util/List";

describe("HTML Tests SvwsUiInputNumber", () => {
	test("HTML wird erzeugt.", () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: { modelValue: 10 },
		});
		expect(wrapper.html()).toContain("<");
		wrapper.unmount();
	});

	test("Das Drücken auf die Buttons löst du Methode onStepperClick aus", async () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: { modelValue: 10 },
		});

		await wrapper.vm.$nextTick();

		const button = wrapper.find("button");

		await button.trigger("click");
		const input = wrapper.find<HTMLInputElement>({
			ref: "input",
		});

		expect(input.element.value).toBe("9");
	});

	test("Wird mit der Klasse 'input-number--filled' wiedergegeben, wenn die Daten nicht null oder undefiniert sind", () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: { modelValue: 10 },
		});
		expect(wrapper.find(".input-number--filled").exists()).toBe(true);
	});

	test("Rendert mit der Klasse 'input-number--disabled', wenn disabled wahr ist", () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: { modelValue: 10, disabled: true },
		});
		expect(wrapper.find(".input-number--disabled").exists()).toBe(true);
	});

	test("Wird mit der Klasse 'input-number--readonly' wiedergegeben, wenn readonly true ist", () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: { modelValue: 10, readonly: true },
		});
		expect(wrapper.find(".input-number--readonly").exists()).toBe(true);
	});

	test("Wird mit der Klasse 'input-number--statistics' wiedergegeben, wenn statistics wahr ist", () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: { modelValue: 10, statistics: true },
		});
		expect(wrapper.find(".input-number--statistics").exists()).toBe(true);
	});

	test("Rendert mit der Klasse 'input-number-component--headless', wenn headless wahr ist", () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: { modelValue: 10, headless: true },
		});
		expect(wrapper.find(".input-number-component--headless").exists()).toBe(
			true
		);
	});

	test("Wird mit der Klasse 'col-span-full' wiedergegeben, wenn span 'full' ist", () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: { modelValue: 10, span: "full" },
		});
		expect(wrapper.find(".col-span-full").exists()).toBe(true);
	});

	test("Wird mit der Klasse 'col-span-2' wiedergegeben, wenn span '2' ist", () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: { modelValue: 10, span: "2" },
		});
		expect(wrapper.find(".col-span-2").exists()).toBe(true);
	});

	test("Rendert Platzhalter-Spannweite, wenn Platzhalter bereitgestellt wird und headless falsch ist", () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: {
				modelValue: 10,
				placeholder: "Enter number",
				headless: false,
			},
		});
		expect(wrapper.find(".input-number--placeholder").exists()).toBe(true);
	});

	test("Rendert keine Platzhalterspanne, wenn headless true ist", () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: {
				modelValue: 10,
				placeholder: "Enter number",
				headless: true,
			},
		});
		expect(wrapper.find(".input-number--placeholder").exists()).toBe(false);
	});

	test("Gibt die Klasse 'input-number--placeholder--required' wieder, wenn 'required' wahr ist", () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: {
				modelValue: 10,
				placeholder: "Enter number",
				required: true,
				headless: false,
			},
		});
		expect(
			wrapper.find(".input-number--placeholder--required").exists()
		).toBe(true);
	});

	test("Zeigt das Statistiksymbol an, wenn die Statistik true ist", () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: {
				modelValue: 10,
				placeholder: "Enter number",
				statistics: true,
				headless: false,
			},
		});
		expect(wrapper.find(".i-ri-bar-chart-2-line").exists()).toBe(true);
	});

	test("Zeigt einen Limittext an, wenn min gesetzt wurde", () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: {
				modelValue: 10,
				min: 2,
				placeholder: "Enter Number",
			},
		});
		const limitText = wrapper.find(".input-number--limittext");
		expect(limitText.exists()).toBe(true);
		expect(limitText.text()).toBe("(min. 2)");
	});

	test("Zeigt einen Limittext an, wenn max gesetzt wurde", () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: {
				modelValue: 10,
				max: 2,
				placeholder: "Enter Number",
			},
		});
		const limitText = wrapper.find(".input-number--limittext");
		expect(limitText.exists()).toBe(true);
		expect(limitText.text()).toBe("(max. 2)");
	});

	test("Zeigt einen Limittext an, wenn min und max gesetzt wurde", () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: {
				modelValue: 10,
				min: 2,
				max: 4,
				placeholder: "Enter Number",
			},
		});
		const limitText = wrapper.find(".input-number--limittext");
		expect(limitText.exists()).toBe(true);
		expect(limitText.text()).toBe("(zwischen 2 und 4)");
	});
});

describe("Prop Tests", async () => {
	test("Props werden korrekt weitergegeben", () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: {
				modelValue: 5,
				placeholder: "Enter a number",
				disabled: true,
				required: true,
				headless: true,
				rounded: true,
				span: "full",
			},
		});
		const input = wrapper.find("input");
		expect(input.element.value).toBe("5");
		expect(input.attributes("placeholder")).toBe("Enter a number");
		expect(input.attributes("disabled")).toBeDefined();
		expect(input.attributes("required")).toBeDefined();
	});

	test("Initialer Wert wird korrekt gesetzt.", () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: {
				modelValue: 10,
				placeholder: "Enter a number",
				required: true,
			},
		});
		const input = wrapper.find("input");
		expect(input.element.value).toBe("10");
	});

	test("Eingabewert wird aktualisiert.", async () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: {
				modelValue: 10,
			},
		});
		const input = wrapper.find("input");
		await input.setValue("20");
		expect(wrapper.emitted()["update:modelValue"][0]).toEqual([20]);
	});
});

describe("Parsing und Synchronisation zwischen data und visualData", () => {
	test.each([
		["4", "4", 4],
		["4,", "4,", 4],
		["4.", "4.", 4],
		["4,5", "4,5", 4.5],
		["4.5", "4.5", 4.5],
		["45-", "45", 45],
		["-45", "-45", -45],
		["abc123cde:;", "123", 123],
		[null, null, null],
	])("Mit der Input-Eingabe %s ist visualData = %s und data = %s", async (input, visualData, data) => {
		const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: null, decimalPlaces: 4 } });
		const inputElement = wrapper.find("input");
		await inputElement.setValue(input);

		const dataRef = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.data;
		const visualDataRef = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.visualData;

		expect(visualDataRef).toBe(visualData);
		expect(dataRef).toBe(data);
	});

	test.each([
		[4, "4", 4],
		[4.5, "4,5", 4.5],
		[null, null, null],
	])("Mit modelValue %s ist visualData = %s und data = %s", (modelValue, visualData, data) => {
		const wrapper = mount(SvwsUiInputNumber, { props: { modelValue, decimalPlaces: 4 } });

		const dataRef = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.data;
		const visualDataRef = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.visualData;

		expect(visualDataRef).toBe(visualData);
		expect(dataRef).toBe(data);
	});

	test.each([
		[0, "4,123456789", "4123456789", 4123456789],
		[1, "4,123456789", "4,1", 4.1],
		[2, "4,123456789", "4,12", 4.12],
		[3, "4,123456789", "4,123", 4.123],
		[4, "4,123456789", "4,1234", 4.1234],
		[0, "4", "4", 4],
		[1, "4", "4", 4],
		[2, "4", "4", 4],
		[3, "4", "4", 4],
		[4, "4", "4", 4],
	])("Mit Prop 'decimalPlaces = %s' und der Input-Eingabe %s ist visualData = %s und data = %s (Sprache: %s)", async (decimalPlaces, input, visualData, data) => {
		const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: null, decimalPlaces: decimalPlaces as 0 | 1 | 2 | 3 | 4 } });
		const inputElement = wrapper.find("input");
		await inputElement.setValue(input);

		const dataRef = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.data;
		const visualDataRef = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.visualData;

		expect(visualDataRef).toBe(visualData);
		expect(dataRef).toBe(data);
	});

	test.each([
		[0, 4.111111, "4"],
		[1, 4.111111, "4,1"],
		[1, 4.555555, "4,6"],
		[1, 4, "4"],
		[2, 4.111111, "4,11"],
		[2, 4.555555, "4,56"],
		[2, 4, "4"],
		[3, 4.111111, "4,111"],
		[3, 4.555555, "4,556"],
		[3, 4, "4"],
		[4, 4.111111, "4,1111"],
		[4, 4.555555, "4,5556"],
		[4, 4, "4"],
	])("Mit Prop 'decimalPlaces = %s' und modelValue %s ist visualData = %s und data = %s", async (decimalPlaces, modelValue, visualData) => {
		const wrapper = mount(SvwsUiInputNumber, { props: { modelValue, decimalPlaces: decimalPlaces as 0 | 1 | 2 | 3 | 4 } });

		const dataRef = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.data;
		const visualDataRef = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.visualData;

		expect(visualDataRef).toBe(visualData);
		expect(dataRef).toBe(modelValue);
	});
});

describe.concurrent("Validierung", () => {

	test("Mit Prop 'validation' wird eine Validierung von außen ausgeführt", () => {
		const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: 10, validation: () => getValidatorFehler() } });
		const validatorResult = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validationResult;

		expect(validatorResult.fehler.size()).toBe(1);
		expect(validatorResult.fehler.getFirst().getFehlermeldung()).toBe("Custom-Validierung fehlgeschlagen");
	});

	test("Wird mit der Klasse 'input-number--muss' wiedergegeben, wenn valid falsch ist", () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: { modelValue: 10, valid: () => false },
		});
		expect(wrapper.find(".input-number--muss").exists()).toBe(true);
	});

	test.each([
		["input-number--muss", ValidatorFehlerart.MUSS],
		["input-number--kann", ValidatorFehlerart.KANN],
		["input-number--hinweis", ValidatorFehlerart.HINWEIS],
	])(
		"Wird mit der Klasse '%s' wiedergegeben, wenn Validierungsfehler vom Härtegrad %s vorhanden sind",
		(expectedClass, fehlerart) => {
			const wrapper = mount(SvwsUiInputNumber, {
				props: { modelValue: 4, validation: () => getValidatorFehler(fehlerart) },
			});
			expect(wrapper.find(`.${expectedClass}`).exists()).toBe(true);
		}
	);

	test("Bei Validierungsfehlern wird ein Validation-Icon angezeigt", () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: { modelValue: 4, placeholder: "Enter Number", validation: () => getValidatorFehler() },
		});
		expect(wrapper.find(".validation-tooltip-icon").exists()).toBeTruthy();
	});

	describe.concurrent("required", () => {
		test.each([
			[false, "ohne Prop 'validation'", undefined],
			[false, "mit Prop 'validation'", () => getValidatorFehler()],
			[true, "mit Prop 'validation'", () => getValidatorFehler()],
		])(
			"Mit Prop 'required = %s' und %s wird kein Required-Validator hinzugefügt",
			(required, _vString, validation) => {
				const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: null, required, validation } });
				const validatorRequired = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validatorRequired;

				expect(validatorRequired).toBeNull();
			}
		);

		test("Mit Prop 'required = true' und ohne Prop 'validation' wird ein Required-Validator hinzugefügt", () => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: null, required: true } });
			const validatorRequired = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validatorRequired;

			expect(validatorRequired).not.toBeNull();
			expect(validatorRequired).toBeInstanceOf(BasicValidator);
		});

		test("Mit Prop 'required = true' ohne Eingabe wird ein Fehler für die Required-Validierung generiert", () => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: null, required: true } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(1);
			expect(validatorResult.fehler.getFirst().getFehlermeldung()).toBe("Bitte geben Sie einen Wert an.");
		});

		test("Mit Prop 'required = true' mit Eingabe ergibt die Validierung keine Fehler", () => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: 10, required: true } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});

	});

	describe.concurrent("min", () => {
		test.each([
			[undefined, "ohne Prop 'validation'", undefined],
			[undefined, "mit Prop 'validation'", () => getValidatorFehler()],
			[3, "mit Prop 'validation'", () => getValidatorFehler()],
		])("Mit Prop 'min = %s' und %s wird kein Length-Validator hinzugefügt", (min, _validationString, validation) => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: null, min, validation } });
			const validatorRange = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validatorRange;

			expect(validatorRange).toBeNull();
		});

		test("Mit Prop 'min = 3' und ohne Prop 'validation' wird ein range-Validator hinzugefügt", () => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: 2, min: 3 } });
			const validatorRange = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validatorRange;

			expect(validatorRange).not.toBeNull();
			expect(validatorRange).toBeInstanceOf(BasicValidator);
		});

		test.each([
			[undefined, 2],
			[3, null],
			[3, 3],
		])("Mit Prop 'min = %s' und Eingabe = %s wird kein Fehler für die Length-Validierung generiert", (min, modelValue) => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue, min } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});

		test("Mit Prop 'min = 3' und Eingabe '2' wird ein Fehler für die range-Validierung generiert", () => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: 2, min: 3 } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(1);
			expect(validatorResult.fehler.getFirst().getFehlermeldung()).toBe("Der Wert muss mindestens 3 sein.");
		});
	});

	describe.concurrent("max", () => {
		test.each([
			[undefined, "ohne Prop 'validation'", undefined],
			[undefined, "mit Prop 'validation'", () => getValidatorFehler()],
			[3, "mit Prop 'validation'", () => getValidatorFehler()],
		])("Mit Prop 'max = %s' und %s wird kein Length-Validator hinzugefügt", (max, _validationString, validation) => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: null, max, validation } });
			const validatorRange = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validatorRange;

			expect(validatorRange).toBeNull();
		});

		test("Mit Prop 'max = 3' und ohne Prop 'validation' wird ein range-Validator hinzugefügt", () => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: 2, max: 3 } });
			const validatorRange = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validatorRange;

			expect(validatorRange).not.toBeNull();
			expect(validatorRange).toBeInstanceOf(BasicValidator);
		});

		test.each([
			[undefined, 3],
			[5, null],
			[5, 3],
		])("Mit Prop 'max = %s' und Eingabe = %s wird kein Fehler für die Length-Validierung generiert", (max, modelValue) => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue, max } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});

		test("Mit Prop 'max = 3' und Eingabe '5' wird ein Fehler für die range-Validierung generiert", () => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: 5, max: 3 } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(1);
			expect(validatorResult.fehler.getFirst().getFehlermeldung()).toBe("Der Wert darf höchstens 3 sein.");
		});
	});
});

describe("Computed values in SvwsUiInputNumber", () => {
	test("Validierung funktioniert korrekt.", async () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: {
				modelValue: 10,
				valid: (value) => (value === null ? true : value > 0),
			},
		});
		const props = wrapper.props();

		expect(
			props.valid?.(-1)
		).toBe(false);
		expect(
			props.valid?.(5)
		).toBe(true);
	});
});

describe("Funktionen in SvwsUiInputNumber", () => {
	test.each([
		["up", undefined, 11],
		["up", false as const, 10],
		["up", 1, 11],
		["up", 0.1, 10.1],
		["down", undefined, 9],
		["down", false as const, 10],
		["down", 1, 9],
		["down", 0.1, 9.9],
	])("onStepperClick mit Argument %s und steps = %s verändert den Wert auf %s", async (direction, steps, result) => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: { modelValue: 10, steps, decimalPlaces: 4 },
		});

		const onStepperClick = await wrapper.findComponent({
			name: "SvwsUiInputNumber",
		}).vm.onStepperClick;
		onStepperClick(direction);

		const dataRef = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.data;

		expect(dataRef).toBe(result);
	});

	test("Wenn prop steps = 0.1 und decimalPlaces = 0 wird eine Fehlermeldung generiert", () => {
		expect(() => mount(SvwsUiInputNumber, { props: { placeholder: "Titel", modelValue: 10, steps: 0.1, decimalPlaces: 0 } }))
			.toThrow("Für das Input mit dem Label 'Titel' wurde mit der prop 'steps = 0.1' eine Schrittweite mit mehr " +
						"Nachkommastellen definiert, als die prop 'decimalPlaces = 0' zulässt.");
	});

	test("onBlur wird korrekt ausgelöst", async () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: {
				modelValue: 10,
			},
		});
		const input = wrapper.find("input");
		await input.trigger("blur");
		expect(wrapper.emitted().blur).toBeTruthy();
		expect(wrapper.emitted().blur[0]).toEqual([10]);
	});

	test("onBlur korrigiert visualData wieder auf data aber mit Komma", async () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: {
				modelValue: 100,
			},
		});
		const input = wrapper.find("input");
		await input.setValue("10,");
		await input.trigger("blur");

		const dataRef = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.data;
		const visualDataRef = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.visualData;
		expect(wrapper.emitted().blur).toBeTruthy();
		expect(wrapper.emitted().blur[0]).toEqual([10]);
		expect(dataRef).toBe(10);
		expect(visualDataRef).toBe("10");
		expect(input.element.value).toBe("10");
	});

	test("onBlur wird nicht ausgelöst, wenn der Fokus zwischen Button und Input switched", async () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: {
				modelValue: 10,
			},
		});

		const input = wrapper.find({ ref: 'input' });
		const btnPlus = wrapper.find({ ref: 'btnPlus' });
		const btnMinus = wrapper.find({ ref: 'btnMinus' });

		await input.trigger("blur", { relatedTarget: btnPlus.element });
		await input.trigger("blur", { relatedTarget: btnMinus.element });
		await input.trigger("btnPlus", { relatedTarget: input.element });
		await input.trigger("btnPlus", { relatedTarget: btnMinus.element });
		await input.trigger("btnMinus", { relatedTarget: input.element });
		await input.trigger("btnMinus", { relatedTarget: btnPlus.element });
		expect(wrapper.emitted('blur')).toBe(undefined);
	});

	test.skip("onKeyEnter wird korrekt ausgelöst", async () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: {
				modelValue: 10,
			},
		});

		const input = wrapper.find("input");
		input.element.value = "20";
		await wrapper.vm.$nextTick();

		await input.trigger("keyup.enter");

		expect(wrapper.emitted().change).toBeTruthy();
		expect(wrapper.emitted().change[0]).toEqual([10]);
	});

	test("reset setzt den Wert zurück", async () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: {
				modelValue: 10,
			},
		});
		const input = wrapper.find("input");
		await input.setValue("20");
		await wrapper
			.findComponent({
				name: "SvwsUiInputNumber",
			})
			.vm.reset();
		expect(input.element.value).toBe("10");
	});
});

function getValidatorFehler(haertegrad: ValidatorFehlerart = ValidatorFehlerart.MUSS): List<ValidatorFehler> {
	const customValidator = new CustomValidatorRequired(haertegrad);
	customValidator.run();
	return customValidator.getFehler();
}

class CustomValidatorRequired extends BasicValidator {

	constructor(haertegrad: ValidatorFehlerart) {
		super(haertegrad);
		this.run();
	}

	protected pruefe(): boolean {
		this.addFehler(0, "Custom-Validierung fehlgeschlagen");
		return false;
	}
}
