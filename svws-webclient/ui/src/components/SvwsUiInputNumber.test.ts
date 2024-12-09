import { describe, test, expect } from "vitest";
import { mount, shallowMount } from "@vue/test-utils";
import SvwsUiInputNumber from "./SvwsUiInputNumber.vue";

describe("HTML Tests SvwsUiInputNumber", () => {
	test("HTML wird erzeugt.", () => {
		const wrapper = mount(SvwsUiInputNumber);
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

	test("Wird mit der Klasse 'input-number--invalid' wiedergegeben, wenn isValid falsch ist", () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: { modelValue: 10, valid: () => false },
		});
		expect(wrapper.find(".input-number--invalid").exists()).toBe(true);
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

	test("Gibt ein Fehlersymbol aus, wenn isValid falsch ist", () => {
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

	test("Gibt ein Warnsymbol aus, wenn die Daten null oder undefiniert sind und die Statistik true ist", () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: {
				modelValue: null,
				placeholder: "Enter number",
				statistics: true,
				headless: false,
			},
		});

		expect(wrapper.find(".i-ri-alert-fill").exists()).toBe(true);
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

describe("Computed values in SvwsUiInputNumber", () => {
	test("Validierung funktioniert korrekt.", async () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: {
				modelValue: 10,
				valid: (value) => (value !== null ? value > 0 : true),
			},
		});
		const input = wrapper.find("input");

		await input.setValue("-1");
		expect(
			wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.isValid
		).toBe(false);
		await input.setValue("5");
		expect(
			wrapper.findComponent({ name: "SvwsUiInputNumber" }).vm.isValid
		).toBe(true);
	});

	test("isValid wird korrekt berechnet", async () => {
		const wrapper = mount(SvwsUiInputNumber, {
			props: {
				modelValue: null,
				required: true,
			},
		});
		expect(
			await wrapper.findComponent({
				name: "SvwsUiInputNumber",
			}).vm.isValid
		).toBe(false);
		await wrapper.setProps({ modelValue: 5 });
		expect(
			await wrapper.findComponent({
				name: "SvwsUiInputNumber",
			}).vm.isValid
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
		const btnPlus = wrapper.find({ref: 'btnPlus'});
		const btnMinus = wrapper.find({ref: 'btnMinus'});

		await input.trigger("blur",{ relatedTarget: btnPlus.element });
		await input.trigger("blur",{ relatedTarget: btnMinus.element });
		await input.trigger("btnPlus",{ relatedTarget: input.element });
		await input.trigger("btnPlus",{ relatedTarget: btnMinus.element });
		await input.trigger("btnMinus",{ relatedTarget: input.element });
		await input.trigger("btnMinus",{ relatedTarget: btnPlus.element });
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
