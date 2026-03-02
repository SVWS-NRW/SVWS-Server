import { describe, test, expect } from "vitest";
import { mount } from "@vue/test-utils";
import SvwsUiInputNumber from "../../../src/ui/controls/SvwsUiInputNumber.vue";
import { BasicValidator } from "../../../../core/src/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "../../../../core/src/asd/validate/ValidatorFehlerart";
import type { ValidatorFehler } from "../../../../core/src/asd/validate/ValidatorFehler";
import type { List } from "../../../../core/src/java/util/List";

describe("HTML Tests SvwsUiInputNumber", () => {
	test("HTML wird erzeugt.", () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: { modelValue: 10 },
		});
		expect(wrapper.html()).toContain("<");
		wrapper.unmount();
	});

	test("Das Drücken auf die Buttons löst du Methode onInputNumber aus", async () => {
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

	test("Wird mit der Klasse 'input-number--muss' wiedergegeben, wenn valid falsch ist", () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: { modelValue: 10, valid: () => false },
		});
		expect(wrapper.find(".input-number--muss").exists()).toBe(true);
	});

	test("Wird mit der Klasse 'input-number--muss' wiedergegeben, wenn Validierungsfehler vom Härtegrad MUSS vorhanden sind", () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: { modelValue: 10, validation: () => getValidatorFehler(ValidatorFehlerart.MUSS) },
		});
		expect(wrapper.find(".input-number--muss").exists()).toBe(true);
	});

	test("Wird mit der Klasse 'input-number--kann' wiedergegeben, wenn Validierungsfehler vom Härtegrad KANN vorhanden sind", () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: { modelValue: 10, validation: () => getValidatorFehler(ValidatorFehlerart.KANN) },
		});
		expect(wrapper.find(".input-number--kann").exists()).toBe(true);
	});

	test("Wird mit der Klasse 'input-number--hinweis' wiedergegeben, wenn Validierungsfehler vom Härtegrad HINWEIS vorhanden sind", () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: { modelValue: 10, validation: () => getValidatorFehler(ValidatorFehlerart.HINWEIS) },
		});
		expect(wrapper.find(".input-number--hinweis").exists()).toBe(true);
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

	test("Gibt ein Fehlersymbol aus, wenn valid false ist", () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: {
				modelValue: 10,
				placeholder: "Enter number",
				valid: () => false,
				headless: false,
			},
		});
		expect(wrapper.find(".i-ri-alert-line").exists()).toBe(true);
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

	test("Gibt ein Warnsymbol aus, wenn die Daten null oder undefiniert sind und die Statistik und Required true ist", () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: {
				modelValue: null,
				placeholder: "Enter number",
				statistics: true,
				required: true,
				headless: false,
			},
		});

		expect(wrapper.find(".i-ri-alert-fill").exists()).toBe(true);
	});

	test("Zeigt einen Limittext an, wenn min gesetzt wurde", () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: {
				modelValue: 10,
				min: 2,
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
			},
		});
		const limitText = wrapper.find(".input-number--limittext");
		expect(limitText.exists()).toBe(true);
		expect(limitText.text()).toBe("(zwischen 2 und 4)");
	});
});

describe("Prop Tests für onInputNumber()", async () => {
	test("Props werden korrekt weitergegeben", () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: {
				modelValue: 5,
				placeholder: "Enter a number",
				disabled: true,
				required: true,
				headless: true,
				rounded: true,
				hideStepper: true,
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

describe.concurrent("Validierung", () => {

	test("Mit Prop 'validation' wird eine Validierung von außen ausgeführt", () => {
		const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: 10, validation: () => getValidatorFehler() } });
		const validatorResult = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validationResult;

		expect(validatorResult.fehler.size()).toBe(1);
		expect(validatorResult.fehler.getFirst().getFehlermeldung()).toBe("Custom-Validierung fehlgeschlagen");
	});

	describe.concurrent("required", () => {

		test("Mit Prop 'required = false' wird kein Required-Validator hinzugefügt", () => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: null, required: false } });
			const validatorRequired = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validatorRequired;

			expect(validatorRequired).toBeNull();
		});

		test("Mit Prop 'required = false' wird keine Validierung für 'required' ausgeführt", () => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: null, required: false } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});


		test("Mit Prop 'required = true' wird ein Required-Validator hinzugefügt", () => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: null, required: true } });
			const validatorRequired = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validatorRequired;

			expect(validatorRequired).not.toBeNull();
			expect(validatorRequired).toBeInstanceOf(BasicValidator);
		});

		test("Mit Prop 'required = true' ohne Eingabe wird ein Fehler für die Required-Validierung generiert", () => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: null, required: true } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(1);
			expect(validatorResult.fehler.getFirst().getFehlermeldung()).toBe("Ein Wert muss angegeben sein.");
		});

		test("Mit Prop 'required = true' mit Eingabe ergibt die Validierung keine Fehler", () => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: 10, required: true } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});

		test("Mit Prop 'required = true' und 'validation' wird eine Validierung von außen ausgeführt und um die Required-Validierung ergänzt", () => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: null, validation: () => getValidatorFehler(), required: true } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(2);
			expect(validatorResult.fehler.get(0).getFehlermeldung()).toBe("Ein Wert muss angegeben sein.");
			expect(validatorResult.fehler.get(1).getFehlermeldung()).toBe("Custom-Validierung fehlgeschlagen");
		});

		test("Mit Prop 'required = true' und 'skipDefaultValidation = true' wird keine Validierung für 'required' ausgeführt", () => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: null, required: true, skipDefaultValidation: true } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});

		test("Mit Prop 'required = true' und 'skipDefaultValidation = { required: true }' wird keine Validierung für 'required' ausgeführt", () => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: null, required: true, skipDefaultValidation: { required: true } } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});

		test("Mit Prop 'required = true' und 'skipDefaultValidation = { required: false }' wird eine Validierung für 'required' ausgeführt", () => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: null, required: true, skipDefaultValidation: { required: false } } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(1);
			expect(validatorResult.fehler.get(0).getFehlermeldung()).toBe("Ein Wert muss angegeben sein.");
		});

		test("Mit Prop 'required = true' und 'skipDefaultValidation = true' wird keine Validierung für 'required' ausgeführt", () => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: null, required: true, skipDefaultValidation: true } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});

		test("Mit Prop 'required = true' und 'skipDefaultValidation = { required: true }' wird keine Validierung für 'required' ausgeführt", () => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: null, required: true, skipDefaultValidation: { required: true } } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});
	});

	describe.concurrent("min", () => {
		test("Mit Prop 'min = undefined' wird kein min-Validator hinzugefügt", () => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: 2, min: undefined } });
			const validatorMin = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validatorMin;

			expect(validatorMin).toBeNull();
		});

		test("Mit Prop 'min = undefined' und Eingabe '2' wird keine Validierung für 'min' ausgeführt", () => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: 2, min: undefined } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});

		test("Mit Prop 'min = 3' wird ein min-Validator hinzugefügt", () => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: 2, min: 2 } });
			const validatorMin = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validatorMin;

			expect(validatorMin).not.toBeNull();
			expect(validatorMin).toBeInstanceOf(BasicValidator);
		});

		test("Mit Prop 'min = 3' und Eingabe '2' wird ein Fehler für die min-Validierung generiert", () => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: 2, min: 3 } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(1);
			expect(validatorResult.fehler.getFirst().getFehlermeldung()).toBe("Der Wert muss mindestens 3 sein.");
		});

		test("Mit Prop 'min = 3' und Eingabe '3' ergibt die Validierung keine Fehler", () => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: 3, min: 3 } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});

		test("Mit Prop 'min = 3' ohne Eingabe wird kein Fehler für die min-Validierung generiert", () => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: 3, min: 3 } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});

		test("Mit Prop 'min = 3' und 'validation' wird eine Validierung von außen ausgeführt und um die min-Validierung ergänzt", () => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: 2, validation: () => getValidatorFehler(), min: 3 } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(2);
			expect(validatorResult.fehler.get(0).getFehlermeldung()).toBe("Der Wert muss mindestens 3 sein.");
			expect(validatorResult.fehler.get(1).getFehlermeldung()).toBe("Custom-Validierung fehlgeschlagen");
		});

		test("Mit Prop 'min = 3' und 'skipDefaultValidation = true' wird keine Validierung für 'min' ausgeführt", () => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: 2, min: 3, skipDefaultValidation: true } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});

		test("Mit Prop 'min = 3' und 'skipDefaultValidation = { min: true }' wird keine Validierung für 'min' ausgeführt", () => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: 2, min: 3, skipDefaultValidation: { min: true } } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});

		test("Mit Prop 'min = 3' und 'skipDefaultValidation = { min: false }' wird eine Validierung für 'min' ausgeführt", () => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: 2, min: 3, skipDefaultValidation: { min: false } } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(1);
			expect(validatorResult.fehler.get(0).getFehlermeldung()).toBe("Der Wert muss mindestens 3 sein.");
		});

		test("Mit Prop 'min = 2' und 'skipDefaultValidation = true' wird keine Validierung für 'min' ausgeführt", () => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: 1, min: 2, skipDefaultValidation: true } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});

		test("Mit Prop 'min = 2' und 'skipDefaultValidation = { min: true }' wird keine Validierung für 'min' ausgeführt", () => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: 1, min: 2, skipDefaultValidation: { min: true } } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});
	});

	describe.concurrent("max", () => {
		test("Mit Prop 'max = undefined' wird kein max-Validator hinzugefügt", () => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: 8, max: undefined } });
			const validatorMax = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validatorMax;

			expect(validatorMax).toBeNull();
		});

		test("Mit Prop 'max = undefined' wird keine Validierung für 'max' ausgeführt", () => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: 8, max: undefined } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});

		test("Mit Prop 'max = 5' wird ein max-Validator hinzugefügt", () => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: 8, max: 5 } });
			const validatorMax = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validatorMax;

			expect(validatorMax).not.toBeNull();
			expect(validatorMax).toBeInstanceOf(BasicValidator);
		});

		test("Mit Prop 'max = 5' und Eingabe '8' wird ein Fehler für die max-Validierung generiert", () => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: 8, max: 5 } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(1);
			expect(validatorResult.fehler.getFirst().getFehlermeldung()).toBe("Der Wert darf maximal 5 sein.");
		});

		test("Mit Prop 'max = 5' ohne Eingabe wird kein Fehler für die max-Validierung generiert", () => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: null, max: 5 } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});

		test("Mit Prop 'max = 5' und Eingabe '4' ergibt die Validierung keine Fehler", () => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: 4, max: 5 } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});

		test("Mit Prop 'max = 5' und 'validation' wird eine Validierung von außen ausgeführt und um die max-Validierung ergänzt", () => {
			const wrapper = mount(SvwsUiInputNumber,
				{ props: { modelValue: 8, validation: () => getValidatorFehler(), max: 5 } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(2);
			expect(validatorResult.fehler.get(0).getFehlermeldung()).toBe("Der Wert darf maximal 5 sein.");
			expect(validatorResult.fehler.get(1).getFehlermeldung()).toBe("Custom-Validierung fehlgeschlagen");
		});

		test("Mit Prop 'max = 5' und 'skipDefaultValidation = true' wird keine Validierung für 'max' ausgeführt", () => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: 8, max: 5, skipDefaultValidation: true } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});

		test("Mit Prop 'max = 5' und 'skipDefaultValidation = { max: true }' wird keine Validierung für 'max' ausgeführt", () => {
			const wrapper = mount(SvwsUiInputNumber,
				{ props: { modelValue: 8, max: 5, skipDefaultValidation: { max: true } } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});

		test("Mit Prop 'max = 5' und 'skipDefaultValidation = { max: false }' wird eine Validierung für 'max' ausgeführt", () => {
			const wrapper = mount(SvwsUiInputNumber,
				{ props: { modelValue: 10, max: 5, skipDefaultValidation: { max: false } } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(1);
			expect(validatorResult.fehler.get(0).getFehlermeldung()).toBe("Der Wert darf maximal 5 sein.");
		});

		test("Mit Prop 'max = 2' und 'skipDefaultValidation = true' wird keine Validierung für 'max' ausgeführt", () => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: 4, max: 2, skipDefaultValidation: true } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});

		test("Mit Prop 'max = 2' und 'skipDefaultValidation = { max: true }' wird keine Validierung für 'max' ausgeführt", () => {
			const wrapper = mount(SvwsUiInputNumber, { props: { modelValue: 4, max: 2, skipDefaultValidation: { max: true } } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});
	});

	test("Mit prop 'skipDefaultValidation' lassen sich einzelne Defaultvalidatoren abschalten", () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: {
				modelValue: 2,
				required: true,
				min: 1,
				max: 2,
				skipDefaultValidation: { required: true, min: false, max: false },
			},
		});
		const validatorMin = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validatorMin;
		const validatorMax = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validatorMax;
		const validatorRequired = wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.validatorRequired;

		expect(validatorMax).not.toBeNull();
		expect(validatorMax).toBeInstanceOf(BasicValidator);
		expect(validatorMin).not.toBeNull();
		expect(validatorMin).toBeInstanceOf(BasicValidator);
		expect(validatorRequired).toBeNull();
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
	test("inInputNumber mit Argument up erhöht den Wert", async () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: { modelValue: 10 },
		});

		// extrahier die Funktion validator Email von der Komponente
		const onInputNumber = await wrapper.findComponent({
			name: "SvwsUiInputNumber",
		}).vm.onInputNumber;
		onInputNumber("up");

		const input = wrapper.find<HTMLInputElement>({
			ref: "input",
		});

		expect(input.element.value).toBe("11");
	});

	test("inInputNumber mit Argument down verringert den Wert", async () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: { modelValue: 10 },
		});

		// extrahier die Funktion validator Email von der Komponente
		const onInputNumber = await wrapper.findComponent({
			name: "SvwsUiInputNumber",
		}).vm.onInputNumber;
		onInputNumber("down");

		const input = wrapper.find<HTMLInputElement>({
			ref: "input",
		});

		expect(input.element.value).toBe("9");
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
	const customValidator = new CustomValidatorSelectRequired(haertegrad);
	customValidator.run();
	return customValidator.getFehler();
}

class CustomValidatorSelectRequired extends BasicValidator {

	constructor(haertegrad: ValidatorFehlerart) {
		super(haertegrad);
		this.run();
	}

	protected pruefe(): boolean {
		this.addFehler(0, "Custom-Validierung fehlgeschlagen");
		return false;
	}
}
