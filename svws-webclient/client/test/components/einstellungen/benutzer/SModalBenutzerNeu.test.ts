import { flushPromises, mount } from "@vue/test-utils";
import { describe, expect, test, vi } from "vitest";

import { BenutzerListeEintrag } from "@core/core/data/benutzer/BenutzerListeEintrag";
import { LehrerCredentialsOhneBenutzerEintrag } from "@core/core/data/benutzer/LehrerCredentialsOhneBenutzerEintrag";
import { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import { BenutzerTyp } from "@core/core/types/benutzer/BenutzerTyp";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import UiSelect from "@ui/ui/controls/select/UiSelect.vue";
import SvwsUiButton from "@ui/ui/controls/SvwsUiButton.vue";
import SvwsUiCheckbox from "@ui/ui/controls/SvwsUiCheckbox.vue";
import SvwsUiTextInput from "@ui/ui/controls/SvwsUiTextInput.vue";

import SModalBenutzerNeu from "~/components/einstellungen/benutzer/SModalBenutzerNeu.vue";

function lehrer(id: number, kuerzel: string, nachname: string, vorname: string, istSichtbar = true): LehrerListeEintrag {
	return Object.assign(new LehrerListeEintrag(), { id, kuerzel, nachname, vorname, istSichtbar });
}

function lehrerBenutzer(id: number, idLehrer: number, name: string): BenutzerListeEintrag {
	return Object.assign(new BenutzerListeEintrag(), { id, typ: BenutzerTyp.LEHRER.id, typID: idLehrer, name, anzeigename: name });
}

function credentials(idLehrer: number, benutzername: string): LehrerCredentialsOhneBenutzerEintrag {
	return Object.assign(new LehrerCredentialsOhneBenutzerEintrag(), { idLehrer, idCredentials: 100 + idLehrer, benutzername });
}

const meier = lehrer(1, "MEI", "Meier", "Paul");
const albers = lehrer(2, "ALB", "Albers", "Zoe");
const albersAnna = lehrer(3, "ALA", "Albers", "Anna");
const unsichtbar = lehrer(4, "UNS", "Unsichtbar", "Ute", false);
const mitBenutzer = lehrer(5, "MIT", "Mitbenutzer", "Max");
const mitCredentials = lehrer(6, "CRE", "Credentials", "Carla");

function mounten(lehrerListe = [meier, albers, albersAnna, unsichtbar, mitBenutzer, mitCredentials], benutzerListe = [lehrerBenutzer(50, mitBenutzer.id, "MIT")]) {
	const listLehrer = new ArrayList<LehrerListeEintrag>();
	for (const l of lehrerListe) {
		listLehrer.add(l);
	}
	const mapBenutzer = new Map<number, BenutzerListeEintrag>(benutzerListe.map(b => [b.id, b]));
	const mapLehrerCredentialsOhneBenutzer = new Map<number, LehrerCredentialsOhneBenutzerEintrag>([[mitCredentials.id, credentials(mitCredentials.id, "carla.alt")]]);
	const createBenutzerAllgemein = vi.fn(async () => { /* Der Server wird nicht gebraucht. */ });
	const createBenutzerLehrer = vi.fn(async () => { /* Der Server wird nicht gebraucht. */ });
	const wrapper = mount(SModalBenutzerNeu, {
		props: { createBenutzerAllgemein, createBenutzerLehrer, deleteBenutzerAllgemein: vi.fn(), mapBenutzer, listLehrer, mapLehrerCredentialsOhneBenutzer },
		global: { stubs: { SvwsUiModal: { template: "<div><slot name='modalContent' /><div class='aktionen'><slot name='modalActions' /></div></div>" } } },
	});
	return { wrapper, createBenutzerAllgemein, createBenutzerLehrer };
}

type Wrapper = ReturnType<typeof mounten>["wrapper"];

function knopf(wrapper: Wrapper, text: string) {
	const result = wrapper.findAllComponents(SvwsUiButton).find(b => b.text().trim() === text);
	if (result === undefined) {
		throw new Error("Knopf nicht gefunden: " + text);
	}
	return result;
}

function textfeld(wrapper: Wrapper, placeholder: string) {
	const felder = wrapper.findAllComponents(SvwsUiTextInput);
	return felder.find(c => c.props("placeholder") === placeholder) ?? felder.find(c => (c.props("placeholder") as string).startsWith(placeholder));
}

async function eingeben(wrapper: Wrapper, placeholder: string, wert: string) {
	const feld = textfeld(wrapper, placeholder);
	if (feld === undefined) {
		throw new Error("Feld nicht gefunden: " + placeholder);
	}
	const input = feld.find("input");
	await input.trigger("focus");
	await input.setValue(wert);
	await input.trigger("blur");
	await flushPromises();
}

function lehrerAuswahl(wrapper: Wrapper) {
	return wrapper.findComponent(UiSelect);
}

function lehrerOptionen(wrapper: Wrapper): string[] {
	const manager = (lehrerAuswahl(wrapper).props() as { manager: { filteredOptions: List<LehrerListeEintrag>, optionDisplayText: (l: LehrerListeEintrag) => string } }).manager;
	return manager.filteredOptions.toArray(new Array<LehrerListeEintrag>()).map(l => manager.optionDisplayText(l));
}

async function waehleLehrer(wrapper: Wrapper, l: LehrerListeEintrag) {
	await lehrerAuswahl(wrapper).vm.$emit("update:modelValue", l);
	await flushPromises();
}

async function passwortEingeben(wrapper: Wrapper) {
	await eingeben(wrapper, "Passwort", "geheim");
	await eingeben(wrapper, "Passwort wiederholen", "geheim");
}

async function oeffnen(wrapper: Wrapper) {
	const result = wrapper.findAllComponents(SvwsUiButton).find(b => b.props("type") === "icon");
	if (result === undefined) {
		throw new Error("Knopf zum Öffnen nicht gefunden");
	}
	await result.trigger("click");
	await flushPromises();
}

async function abbrechen(wrapper: Wrapper) {
	await knopf(wrapper, "Abbrechen").trigger("click");
	await flushPromises();
}

/**
 * Prüft das Modal zum Anlegen eines Benutzers: Standardmäßig wird ein Lehrer-Benutzer angelegt, dessen Lehrer aus einer
 * sortierten Auswahl gewählt wird. Vorhandene Anmeldedaten des Lehrers werden angezeigt und der Anmeldename bewusst gewählt.
 */
describe("SModalBenutzerNeu", () => {

	test("Standardmäßig ist Lehrer ausgewählt und der Filter Nur Sichtbare aktiv", () => {
		const { wrapper } = mounten();
		expect(knopf(wrapper, "Lehrer").props("type")).toBe("primary");
		expect(knopf(wrapper, "Allgemeiner Benutzer").props("type")).toBe("secondary");
		const toggle = wrapper.findAllComponents(SvwsUiCheckbox).find(c => c.text().includes("Nur Sichtbare"));
		expect(toggle?.props("modelValue")).toBe(true);
		expect(lehrerAuswahl(wrapper).exists()).toBe(true);
	});

	test("Die Auswahl enthält nur sichtbare Lehrer ohne Benutzer, sortiert nach Nachname und Vorname", () => {
		const { wrapper } = mounten();
		expect(lehrerOptionen(wrapper)).toEqual(["Albers, Anna (ALA)", "Albers, Zoe (ALB)", "Credentials, Carla (CRE)", "Meier, Paul (MEI)"]);
	});

	test("Ohne den Filter Nur Sichtbare werden auch nicht sichtbare Lehrer angeboten", async () => {
		const { wrapper } = mounten();
		const toggle = wrapper.findAllComponents(SvwsUiCheckbox).find(c => c.text().includes("Nur Sichtbare"));
		await toggle?.find("input").setValue(false);
		await flushPromises();
		expect(lehrerOptionen(wrapper)).toContain("Unsichtbar, Ute (UNS)");
		expect(lehrerOptionen(wrapper)).not.toContain("Mitbenutzer, Max (MIT)");
	});

	test("Nach dem Löschen eines Lehrer-Benutzers wird der Lehrer wieder angeboten", async () => {
		const { wrapper } = mounten();
		expect(lehrerOptionen(wrapper)).not.toContain("Mitbenutzer, Max (MIT)");
		await wrapper.setProps({ mapBenutzer: new Map<number, BenutzerListeEintrag>() });
		expect(lehrerOptionen(wrapper)).toContain("Mitbenutzer, Max (MIT)");
	});

	test("Ohne gewählten Lehrer kann kein Benutzer angelegt werden", async () => {
		const { wrapper } = mounten();
		await passwortEingeben(wrapper);
		expect(knopf(wrapper, "Weiter").props("disabled")).toBe(true);
	});

	test("Nach der Lehrerwahl ist der Anmeldename mit dem Kürzel vorbelegt und der Lehrer-Benutzer wird angelegt", async () => {
		const { wrapper, createBenutzerLehrer } = mounten();
		await waehleLehrer(wrapper, meier);
		expect(textfeld(wrapper, "Anmeldename")?.props("modelValue")).toBe("MEI");
		expect(textfeld(wrapper, "Anzeigename")?.props("modelValue")).toBe("Paul Meier");
		expect(textfeld(wrapper, "Anzeigename")?.props("readonly")).toBe(true);
		await passwortEingeben(wrapper);
		expect(knopf(wrapper, "Weiter").props("disabled")).toBe(false);
		await knopf(wrapper, "Weiter").trigger("click");
		await flushPromises();
		expect(createBenutzerLehrer).toHaveBeenCalledWith(meier.id, "MEI", "geheim");
	});

	test("Der vorbelegte Anmeldename kann geändert werden", async () => {
		const { wrapper, createBenutzerLehrer } = mounten();
		await waehleLehrer(wrapper, meier);
		await eingeben(wrapper, "Anmeldename", "paul.meier");
		await passwortEingeben(wrapper);
		await knopf(wrapper, "Weiter").trigger("click");
		await flushPromises();
		expect(createBenutzerLehrer).toHaveBeenCalledWith(meier.id, "paul.meier", "geheim");
	});

	test("Ohne vorhandene Anmeldedaten wird kein Hinweis angezeigt", async () => {
		const { wrapper } = mounten();
		await waehleLehrer(wrapper, meier);
		expect(wrapper.text()).not.toContain("bereits Anmeldedaten vorhanden");
	});

	test("Bei vorhandenen Anmeldedaten wird standardmäßig der bisherige Anmeldename beibehalten", async () => {
		const { wrapper, createBenutzerLehrer } = mounten();
		await waehleLehrer(wrapper, mitCredentials);
		expect(wrapper.text()).toContain("bereits Anmeldedaten vorhanden");
		expect(wrapper.text()).toContain("carla.alt");
		expect(textfeld(wrapper, "Anmeldename")?.props("modelValue")).toBe("carla.alt");
		expect(textfeld(wrapper, "Anmeldename")?.props("readonly")).toBe(true);
		await passwortEingeben(wrapper);
		await knopf(wrapper, "Weiter").trigger("click");
		await flushPromises();
		expect(createBenutzerLehrer).toHaveBeenCalledWith(mitCredentials.id, "carla.alt", "geheim");
	});

	test("Bei vorhandenen Anmeldedaten kann bewusst ein neuer Anmeldename gewählt werden", async () => {
		const { wrapper, createBenutzerLehrer } = mounten();
		await waehleLehrer(wrapper, mitCredentials);
		const neu = wrapper.findAll("input[type='radio']").find(r => (r.element.closest("label")?.textContent ?? "").includes("neuen Anmeldenamen"));
		await neu?.setValue(true);
		await flushPromises();
		expect(textfeld(wrapper, "Anmeldename")?.props("modelValue")).toBe("CRE");
		expect(textfeld(wrapper, "Anmeldename")?.props("readonly")).toBe(false);
		await passwortEingeben(wrapper);
		await knopf(wrapper, "Weiter").trigger("click");
		await flushPromises();
		expect(createBenutzerLehrer).toHaveBeenCalledWith(mitCredentials.id, "CRE", "geheim");
	});

	test("Als allgemeiner Benutzer wird das bisherige Formular verwendet", async () => {
		const { wrapper, createBenutzerAllgemein, createBenutzerLehrer } = mounten();
		await knopf(wrapper, "Allgemeiner Benutzer").trigger("click");
		await flushPromises();
		expect(knopf(wrapper, "Allgemeiner Benutzer").props("type")).toBe("primary");
		expect(lehrerAuswahl(wrapper).exists()).toBe(false);
		await eingeben(wrapper, "Anzeigename", "Tim Taler");
		await eingeben(wrapper, "Anmeldename", "tim");
		await passwortEingeben(wrapper);
		await knopf(wrapper, "Weiter").trigger("click");
		await flushPromises();
		expect(createBenutzerAllgemein).toHaveBeenCalledWith("Tim Taler", "tim", "geheim");
		expect(createBenutzerLehrer).not.toHaveBeenCalled();
	});

	test("Der Wechsel des Benutzertyps setzt die Eingaben zurück", async () => {
		const { wrapper } = mounten();
		await waehleLehrer(wrapper, meier);
		await knopf(wrapper, "Allgemeiner Benutzer").trigger("click");
		await flushPromises();
		expect(textfeld(wrapper, "Anmeldename")?.props("modelValue")).toBe("");
		await knopf(wrapper, "Lehrer").trigger("click");
		await flushPromises();
		expect((lehrerAuswahl(wrapper).props() as { modelValue: unknown }).modelValue).toBeNull();
	});

	test("Ohne übrige Lehrer ist der Knopf Lehrer deaktiviert und Allgemeiner Benutzer vorgewählt", async () => {
		const { wrapper } = mounten([mitBenutzer]);
		await oeffnen(wrapper);
		expect(knopf(wrapper, "Lehrer").props("disabled")).toBe(true);
		expect(knopf(wrapper, "Lehrer").props("type")).toBe("secondary");
		expect(knopf(wrapper, "Allgemeiner Benutzer").props("type")).toBe("primary");
		expect(lehrerAuswahl(wrapper).exists()).toBe(false);
		expect(textfeld(wrapper, "Anzeigename (z.B. Tim Taler)")).toBeDefined();
	});

	test("Ohne Lehrer ist der Knopf Lehrer deaktiviert und Allgemeiner Benutzer vorgewählt", async () => {
		const { wrapper } = mounten([], []);
		await oeffnen(wrapper);
		expect(knopf(wrapper, "Lehrer").props("disabled")).toBe(true);
		expect(knopf(wrapper, "Allgemeiner Benutzer").props("type")).toBe("primary");
	});

	test("Sind nur noch nicht sichtbare Lehrer übrig, ist Allgemeiner Benutzer vorgewählt und Lehrer auswählbar", async () => {
		const { wrapper } = mounten([unsichtbar, mitBenutzer]);
		await oeffnen(wrapper);
		expect(knopf(wrapper, "Lehrer").props("disabled")).toBe(false);
		expect(knopf(wrapper, "Allgemeiner Benutzer").props("type")).toBe("primary");
		await knopf(wrapper, "Lehrer").trigger("click");
		await flushPromises();
		expect(knopf(wrapper, "Lehrer").props("type")).toBe("primary");
		expect(lehrerAuswahl(wrapper).exists()).toBe(true);
	});

	test("Sind nur noch nicht sichtbare Lehrer übrig, ist der Filter Nur Sichtbare beim Öffnen ausgeschaltet", async () => {
		const { wrapper } = mounten([unsichtbar, mitBenutzer]);
		await oeffnen(wrapper);
		await knopf(wrapper, "Lehrer").trigger("click");
		await flushPromises();
		const toggle = wrapper.findAllComponents(SvwsUiCheckbox).find(c => c.text().includes("Nur Sichtbare"));
		expect(toggle?.props("modelValue")).toBe(false);
		expect(lehrerOptionen(wrapper)).toEqual(["Unsichtbar, Ute (UNS)"]);
	});

	test("Sind wieder sichtbare Lehrer übrig, ist der Filter Nur Sichtbare beim nächsten Öffnen eingeschaltet", async () => {
		const { wrapper } = mounten([unsichtbar, meier, mitBenutzer], [lehrerBenutzer(50, mitBenutzer.id, "MIT"), lehrerBenutzer(51, meier.id, "MEI")]);
		await oeffnen(wrapper);
		await abbrechen(wrapper);
		await wrapper.setProps({ mapBenutzer: new Map<number, BenutzerListeEintrag>([[50, lehrerBenutzer(50, mitBenutzer.id, "MIT")]]) });
		await oeffnen(wrapper);
		const toggle = wrapper.findAllComponents(SvwsUiCheckbox).find(c => c.text().includes("Nur Sichtbare"));
		expect(knopf(wrapper, "Lehrer").props("type")).toBe("primary");
		expect(toggle?.props("modelValue")).toBe(true);
		expect(lehrerOptionen(wrapper)).toEqual(["Meier, Paul (MEI)"]);
	});

	test("Der Benutzertyp wird bei jedem Öffnen abhängig von den übrigen sichtbaren Lehrern festgelegt", async () => {
		const { wrapper } = mounten([mitBenutzer]);
		await oeffnen(wrapper);
		expect(knopf(wrapper, "Allgemeiner Benutzer").props("type")).toBe("primary");
		await abbrechen(wrapper);
		await wrapper.setProps({ mapBenutzer: new Map<number, BenutzerListeEintrag>() });
		await oeffnen(wrapper);
		expect(knopf(wrapper, "Lehrer").props("disabled")).toBe(false);
		expect(knopf(wrapper, "Lehrer").props("type")).toBe("primary");
		await abbrechen(wrapper);
		await wrapper.setProps({ mapBenutzer: new Map<number, BenutzerListeEintrag>([[50, lehrerBenutzer(50, mitBenutzer.id, "MIT")]]) });
		await oeffnen(wrapper);
		expect(knopf(wrapper, "Lehrer").props("disabled")).toBe(true);
		expect(knopf(wrapper, "Allgemeiner Benutzer").props("type")).toBe("primary");
	});

	test("Beim Schließen werden der gewählte Lehrer und die Eingaben verworfen", async () => {
		const { wrapper } = mounten();
		await oeffnen(wrapper);
		await waehleLehrer(wrapper, meier);
		await passwortEingeben(wrapper);
		await abbrechen(wrapper);
		await oeffnen(wrapper);
		expect((lehrerAuswahl(wrapper).props() as { modelValue: unknown }).modelValue).toBeNull();
		expect(textfeld(wrapper, "Anmeldename")?.props("modelValue")).toBe("");
		expect(textfeld(wrapper, "Passwort")?.props("modelValue")).toBe("");
		expect(textfeld(wrapper, "Passwort wiederholen")?.props("modelValue")).toBe("");
	});

	test("Beim Schließen werden Benutzertyp und Filter Nur Sichtbare zurückgesetzt", async () => {
		const { wrapper } = mounten();
		await oeffnen(wrapper);
		const toggle = () => wrapper.findAllComponents(SvwsUiCheckbox).find(c => c.text().includes("Nur Sichtbare"));
		await toggle()?.find("input").setValue(false);
		await flushPromises();
		await knopf(wrapper, "Allgemeiner Benutzer").trigger("click");
		await flushPromises();
		await eingeben(wrapper, "Anzeigename", "Tim Taler");
		await eingeben(wrapper, "Anmeldename", "tim");
		await abbrechen(wrapper);
		await oeffnen(wrapper);
		expect(knopf(wrapper, "Lehrer").props("type")).toBe("primary");
		expect(toggle()?.props("modelValue")).toBe(true);
		expect(lehrerOptionen(wrapper)).not.toContain("Unsichtbar, Ute (UNS)");
		expect(textfeld(wrapper, "Anmeldename")?.props("modelValue")).toBe("");
	});

	test("Nach dem Anlegen sind die Eingaben beim nächsten Öffnen verworfen", async () => {
		const { wrapper, createBenutzerLehrer } = mounten();
		await oeffnen(wrapper);
		await waehleLehrer(wrapper, meier);
		await passwortEingeben(wrapper);
		await knopf(wrapper, "Weiter").trigger("click");
		await flushPromises();
		expect(createBenutzerLehrer).toHaveBeenCalledWith(meier.id, "MEI", "geheim");
		await oeffnen(wrapper);
		expect((lehrerAuswahl(wrapper).props() as { modelValue: unknown }).modelValue).toBeNull();
		expect(textfeld(wrapper, "Anmeldename")?.props("modelValue")).toBe("");
		expect(textfeld(wrapper, "Passwort")?.props("modelValue")).toBe("");
	});

});
