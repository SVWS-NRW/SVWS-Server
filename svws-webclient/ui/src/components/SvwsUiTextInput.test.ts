import { describe, test, expect } from "vitest";
import { mount } from "@vue/test-utils";

import SvwsUiTextInput from "./SvwsUiTextInput.vue";

export type InputType = "text" | "date" | "email" | "search" | "tel" | "password";

describe("Komponente kann gemounted werden", () => {
	test("HTML wird erzeugt", () => {
		const wrapper = mount(SvwsUiTextInput);
		expect(wrapper.html()).includes("text-input-component");
	});
});

describe.concurrent("PropHandhabung läuft korrekt", () => {
	test.each([
		[ "text", 'type="text"', "Type Prop as text"],
		[ "date", 'type="date"', "Type Prop as date"],
		[ "email", 'type="email"', "Type Prop as email"],
		[ "search", 'type="search"', "Type Prop as search"],
		[ "tel", 'type="tel"', "Type Prop as tel"],
		[ "password", 'type="password"', "Type Prop as password"],
		[ "text", 'type="text"', "Type Prop as number"],
		[ "search", "search-icon", "Typ Prop als Suche rendert Icons"],
		[ "search", '<span class="icon', "Typ Prop als Suche rendert Icons Klasse"],
		[ "date", '<span class="svws-icon icon i-ri-calendar-2-line">', "Typ Prop als Datum zeigt das Datumssymbol an"],
	])('Mit Props type="%s" HTML enthält "%s" | %s ', async (x, y, _) => {
		const props = { type: x as "text" | "date" | "email" | "search" | "tel" | "password" };

		const wrapper = mount(SvwsUiTextInput, { props: props });

		// trigger Update-Logik welche für manche tests benötigt werden
		await wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.$nextTick();

		expect(wrapper.html()).includes(y);
	})

	test('Mit Prop disabled ist der input disabled', () => {
		const wrapper = mount(SvwsUiTextInput, { props: { disabled: true } });
		expect(wrapper.find('input').attributes('disabled')).toBe("");
	});

	test('Mit Prop disabled = false ist der input nicht disabled', () => {
		const wrapper = mount(SvwsUiTextInput, { props: { disabled: false } });
		expect(wrapper.find('input').html()).not.includes('disabled');
	});

	test('Prop required wird an das Input Feld übergeben', () => {
		const wrapper = mount(SvwsUiTextInput, { props: { required: true } });
		console.log(wrapper.html())
		expect(wrapper.find('input').attributes('required')).toBe("");
	});

	test('Mit Prop required = false ist der input nicht required', () => {
		const wrapper = mount(SvwsUiTextInput, { props: { required: false } });
		expect(wrapper.find('input').html()).not.includes('required');
	});

	test('Prop readonly wird an das Input Feld übergeben, sofern es sich um ein Input-Feld für ein Select handelt', () => {
		const wrapper = mount(SvwsUiTextInput, { props: { readonly: true, isSelectInput: true } });
		expect(wrapper.find('input').attributes('readonly')).toBe("");
	});

	test("Wird mit der Klasse 'text-input--readonly' wiedergegeben, wenn readonly true ist", () => {
		const wrapper = mount(SvwsUiTextInput, { props: { readonly: true } });
		expect(wrapper.find(".text-input--readonly").exists()).toBe(true);
	});

	test('Mit Prop readonly = false ist der input nicht readonly', () => {
		const wrapper = mount(SvwsUiTextInput, { props: { required: false } });
		expect(wrapper.find('input').html()).not.includes('readonly');
	});

	test('Prop headless rendert die Komponente im headless mode', () => {
		const wrapper = mount(SvwsUiTextInput, { props: { headless: true } });
		expect(wrapper.find('.text-input--headless').exists()).toBe(true);
	});

	test('Prop focus aktiviert den Fokus des Objekts', async () => {
		const wrapper = mount(SvwsUiTextInput, { props: { focus: true }, attachTo: document.body });
		await wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.$nextTick();
		expect(wrapper.find('input').element).toBe(document.activeElement);
	});

	test('Prop rounded wird an CSS übergeben', () => {
		const wrapper = mount(SvwsUiTextInput, { props: { rounded: true } });
		expect(wrapper.find('.text-input--rounded').exists()).toBe(true);
	});

	test('Prop url wird richtig angezeigt', () => {
		const wrapper = mount(SvwsUiTextInput, { props: { url: true } });
		expect(wrapper.find('.text-input--prefix').exists()).toBe(true);
	});

	test('Prop minLen wird angezeigt', async () => {
		const wrapper = mount(SvwsUiTextInput, { props: { minLen: 3, placeholder: " " } });
		await wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.$nextTick();
		expect(wrapper.find('.text-input--placeholder').text()).toContain('mind. 3 Zeichen');
	});

	test('Prop maxLen wird angezeigt', async () => {
		const wrapper = mount(SvwsUiTextInput, { props: { maxLen: 10, placeholder: " " } });
		await wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.$nextTick();
		expect(wrapper.find('.text-input--placeholder').text()).toContain('max. 10 Zeichen');
	});

	test('Prop minLen und maxLen wird angezeigt', async () => {
		const wrapper = mount(SvwsUiTextInput, { props: { minLen: 3, maxLen: 10, placeholder: " " } });
		await wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.$nextTick();
		expect(wrapper.find('.text-input--placeholder').text()).toContain('zwischen 3 und 10 Zeichen');
	});

	type spanType = "full" | "2" | undefined;

	test.each([
		['full', 'innerspan="full"'],
		['2', 'innerspan="2"'],
	])('Prop span="%s" wird ab CSS übergeben', (span, className) => {
		const innerSpan : spanType = span as spanType;
		const wrapper = mount(SvwsUiTextInput, { props: { innerSpan } });
		expect(wrapper.html()).includes(className);
	});

	test('Prop removable wird an das Input Feld übergeben', () => {
		const wrapper = mount(SvwsUiTextInput, { props: { removable: true, type: 'date' } });
		expect(wrapper.find('.svws-icon--remove').exists()).toBe(true);
	});
});




describe.concurrent("Modelvalue (prop), two-way-binding und Aktualisierungs- und Emitlogik", () => {
	test("Daten Prop werden wiedergegeben", async () => {
		const props = { modelValue: "Lorem123" };

		const wrapper = mount(SvwsUiTextInput, { props: props });

		expect(wrapper.html()).includes("Lorem123");
	});

	test("Updating data Prop will be rerendered", async () => {
		const props = { modelValue: "Lorem123" };

		const wrapper = mount(SvwsUiTextInput, { props: props });
		await wrapper.setProps({ modelValue: "Lorem321" });

		expect(wrapper.html()).includes("Lorem321");
	});

	test("Aktualisierung der Daten Prop wird emit updateData ausgelöst", async () => {
		const props = { modelValue: "Lorem123" };

		const wrapper = mount(SvwsUiTextInput, { props: props, });

		await wrapper.setProps({ modelValue: "Lorem321" });

		const emittedEvents = wrapper.emitted("update:modelValue");

		expect(emittedEvents).toBeTruthy(); // Überprüft, ob Events emittiert wurden
		if ((emittedEvents !== undefined) && (emittedEvents[0].length > 0)) {
			expect(emittedEvents[0][0]).toEqual("Lorem321");
		} else {
			throw new Error("Keine Ereignisse ausgesendet");
		}
	});

	test("Die Aktualisierung des Eingabewerts für das v-model führt zu einem emit von updateData mit dem neuen Wert", async () => {
		const props = { modelValue: "Lorem123" };

		const wrapper = mount(SvwsUiTextInput, { props: props, });

		const input = wrapper.find("input");
		input.element.value = "Lorem321";
		await input.trigger("input");

		const emittedEvents = wrapper.emitted("update:modelValue");

		expect(emittedEvents).toBeTruthy(); // Überprüft, ob Events emittiert wurden
		if ((emittedEvents !== undefined) && (emittedEvents[0].length > 0)) {
			expect(emittedEvents[0][0]).toEqual("Lorem321");
		} else {
			throw new Error("No events emitted");
		}
	});

	test("Die Aktualisierung eines Eingabewerts mit demselben Wert führt nicht zur Ausgabe von updateData", async () => {
		const props = { modelValue: "Lorem123" };

		const wrapper = mount(SvwsUiTextInput, { props: props, });

		const input = wrapper.find("input");
		input.element.value = "Lorem123";
		await input.trigger("input");

		const emittedEvents = wrapper.emitted("update:modelValue");
		expect(emittedEvents).toBeUndefined();
	})
})

describe.concurrent("Unit Tests für validatorEmail()", async () => {
	const wrapper = mount(SvwsUiTextInput);

	// extrahier die Funktion validator Email von der Komponente
	const validatorEmail = await wrapper.findComponent({
		name: "SvwsUiTextInput",
	}).vm.validatorEmail;

	// [Input Parameter, Erwartungswert, Beschreibung]
	test.each([
		[ "fake@sadsdd.de", true, "Korrekte Email" ],
		[ "asdasdsadsdd.de", true, "Kein @ ist erlaubt und bezieht sich auf \"diese\" Domain" ],
		[ "test.email+alex@leetcode.com", true, "+ wird für tags und ähnliches verwendet" ],
		[ "asdasds@ad@sdd.de", false, "Zwei @ wird nicht akzeptiert" ],
		[ "asdasd sdd.de", false, "Leerzeichen erzeugen Fehler" ],
		[ "asdasd@asdasd", false, "Fehlerhafte URL wird nicht akzeptiert" ],
		[ "asdasd@asdasd.d", false, "Zu kurze URL wird nicht akzeptiert" ],
		[ null, true, "null wird akzeptiert" ],
		[ " test@test.de", false, "Leerzeichen am Anfang wird nicht akzeptiert" ]
	])('validatorEmail(%s) => %s | %s ', (x, y, _) => {
		expect(validatorEmail(x)).toBe(y)
	})
});

describe.concurrent("Unit Tests für computed value maxLenValid", async () => {
	test.each([
		[ 10, "test", true, "Kürzeres Wort als MaxLen wird akzeptiert" ],
		[ 1, "test", false, "Längeres Wort als MaxLen wird nicht akzeptiert" ],
		[ undefined, "test", true, "Falls MaxLen undefined ist wird jede Länge akzeptiert" ],
		[ 4, null, true, "Null als Wort ist nicht zu lang" ],
		[ 10, "", true, "Leerstring wird akzeptiert" ],
	])('maxLenValid {maxLen: %s, modelValue: %s} => %s | %s ', (x1, x2, y, _) => {
		const props = { maxLen: x1, modelValue: x2 };
		const wrapper = mount(SvwsUiTextInput, { props: props });
		const maxLenValid = wrapper.findComponent({
			name: "SvwsUiTextInput",
		}).vm.maxLenValid;
		expect(maxLenValid).toEqual(y);
	})
})

describe.concurrent("Unit Tests für computed value minLenValid", async () => {
	test.each([
		[ 1, "test", true, "Längeres Wort als MinLen wird akzeptiert" ],
		[ 10, "test", false, "Kürzeres Wort als MinLen wird nicht akzeptiert" ],
		[ undefined, "test", true, "Falls MinLen undefined ist wird jede Länge akzeptiert" ],
		[ 4, null, false, "Wert null wird nicht akzeptiert, wenn MinLen > 0 gesetzt ist" ],
		[ 4, "", false, "Leerstring wird nicht akzeptiert, wenn MinLen > 0 gesetzt ist" ],
		[ 0, null, true, "Leerstring wird akzeptiert, wenn MinLen == 0 gesetzt ist" ],
		[ 0, "", true, "Leerstring wird akzeptiert, wenn MinLen == 0 gesetzt ist" ],
	])('minLenValid {minLen: %s, modelValue: %s} => %s | %s ', (x1, x2, y, _) => {
		const props = { minLen: x1, modelValue: x2 };
		const wrapper = mount(SvwsUiTextInput, { props: props });
		const minLenValid = wrapper.findComponent({
			name: "SvwsUiTextInput",
		}).vm.minLenValid;
		expect(minLenValid).toEqual(y);
	})
})

describe.concurrent("Unit Test für computed isValid", () => {
	test("Erforderliches Feld mit null-Wert wird nicht akzeptiert", async () => {
		const props = { required: true, modelValue: null };
		const wrapper = mount(SvwsUiTextInput, { props: props });

		const isValid = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.isValid;
		expect(isValid).toEqual(false);
	});

	test("Erforderliches Feld mit Leerstring wird nicht akzeptiert", async () => {
		const props = { required: true, modelValue: '' };
		const wrapper = mount(SvwsUiTextInput, { props: props });

		const isValid = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.isValid;
		expect(isValid).toEqual(false);
	});

	test("E-Mail-Validierung gibt false für ungültige E-Mail zurück", async () => {
		const props = {
			type: "email" as InputType,
			modelValue: "invalid-email@",
		};
		const wrapper = mount(SvwsUiTextInput, { props: props });

		const isValid = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.isValid;
		expect(isValid).toEqual(false);
	});

	test("E-Mail-Validierung gibt true für gültige E-Mail zurück", async () => {
		const props = {
			type: "email" as InputType,
			modelValue: "test@example.com",
		};
		const wrapper = mount(SvwsUiTextInput, { props: props });

		const isValid = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.isValid;
		expect(isValid).toEqual(true);
	});

	test("MaxLen-Validierung gibt false für zu langen String zurück", async () => {
		const props = { maxLen: 5, modelValue: "too long" };
		const wrapper = mount(SvwsUiTextInput, { props: props });

		const isValid = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.isValid;
		expect(isValid).toEqual(false);
	});

	test("MinLen-Validierung gibt false für zu kurzen String zurück", async () => {
		const props = { minLen: 10, modelValue: "too short" };
		const wrapper = mount(SvwsUiTextInput, { props: props });

		const isValid = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.isValid;
		expect(isValid).toEqual(false);
	});

	test("Benutzerdefinierte Validierung wird nicht akzeptiert", async () => {
		const props = {
			valid: (value: string | null) => value === "valid",
			modelValue: "invalid",
		};
		const wrapper = mount(SvwsUiTextInput, { props: props });

		const isValid = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.isValid;
		expect(isValid).toEqual(false);
	});

	test("Benutzerdefinierte Validierung gibt true zurück", async () => {
		const props = {
			valid: (value: string | null) => value === "valid",
			modelValue: "valid",
		};
		const wrapper = mount(SvwsUiTextInput, { props: props });

		const isValid = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.isValid;
		expect(isValid).toEqual(true);
	});
})
