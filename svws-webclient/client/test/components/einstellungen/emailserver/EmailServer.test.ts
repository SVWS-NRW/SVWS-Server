import { flushPromises, mount } from "@vue/test-utils";
import { beforeEach, describe, expect, test, vi } from "vitest";

import { SMTPServerKonfiguration } from "@core/core/data/email/SMTPServerKonfiguration";
import SvwsUiTextInput from "@ui/ui/controls/SvwsUiTextInput.vue";

import EmailServer from "~/components/einstellungen/emailserver/EmailServer.vue";

const benutzer = vi.hoisted(() => ({ istAdmin: true }));

vi.mock("@ui/states/BenutzerState", () => ({
	useBenutzerState: () => benutzer,
}));

const FINGERABDRUCK = "AB:CD:EF:01:23:45:67:89:AB:CD:EF:01:23:45:67:89:AB:CD:EF:01:23:45:67:89:AB:CD:EF:01:23:45:67:89";

function mounten(useTLS: boolean, useStartTLS: boolean) {
	const konfiguration = new SMTPServerKonfiguration();
	konfiguration.useTLS = useTLS;
	konfiguration.useStartTLS = useStartTLS;
	const patch = vi.fn(async () => { /* Der Server wird nicht gebraucht. */ });
	const wrapper = mount(EmailServer, { props: { smptServerKonfiguration: () => konfiguration, patch } });
	return { wrapper, patch };
}

async function gibFingerabdruckEin(wrapper: ReturnType<typeof mounten>["wrapper"], wert: string) {
	const feld = wrapper.findAllComponents(SvwsUiTextInput).find(c => c.props("placeholder") === "Fingerabdruck des Zertifikats (SHA-256)");
	if (feld === undefined) {
		throw new Error("Feld für den Fingerabdruck nicht gefunden");
	}
	const input = feld.find("input");
	await input.trigger("focus");
	await input.setValue(wert);
	await input.trigger("blur");
	await flushPromises();
}

/**
 * Prüft die Seite für den E-Mail-Server: Nur Admins bearbeiten die Konfiguration, ein unverschlüsselter Versand wird
 * angezeigt und der Fingerabdruck geht nur gültig und in der Form des Servers an den Server.
 */
describe("EmailServer", () => {

	beforeEach(() => {
		benutzer.istAdmin = true;
	});

	test("Ohne Admin-Recht zeigt die Seite keine bearbeitbaren Felder", () => {
		benutzer.istAdmin = false;
		const { wrapper } = mounten(true, false);
		expect(wrapper.findAllComponents(SvwsUiTextInput)).toHaveLength(0);
	});

	test("Ein Admin sieht die Felder", () => {
		const { wrapper } = mounten(true, false);
		expect(wrapper.findAllComponents(SvwsUiTextInput).length).toBeGreaterThan(0);
	});

	test("Ohne TLS und StartTLS warnt die Seite vor dem unverschlüsselten Versand", () => {
		expect(mounten(false, false).wrapper.text()).toContain("unverschlüsselt");
		expect(mounten(true, false).wrapper.text()).not.toContain("unverschlüsselt");
		expect(mounten(false, true).wrapper.text()).not.toContain("unverschlüsselt");
	});

	test("Der Fingerabdruck geht in der Form des Servers an den Server", async () => {
		const { wrapper, patch } = mounten(true, false);
		await gibFingerabdruckEin(wrapper, " " + FINGERABDRUCK.replaceAll(":", "").toLowerCase() + " ");
		expect(patch).toHaveBeenCalledWith({ zertifikatFingerabdruck: FINGERABDRUCK });
	});

	test("Ein leerer Fingerabdruck entfernt die Freigabe", async () => {
		const { wrapper, patch } = mounten(true, false);
		await gibFingerabdruckEin(wrapper, "x");
		await gibFingerabdruckEin(wrapper, "");
		expect(patch).toHaveBeenCalledWith({ zertifikatFingerabdruck: null });
	});

	test("Ein ungültiger Fingerabdruck geht nicht an den Server", async () => {
		const { wrapper, patch } = mounten(true, false);
		await gibFingerabdruckEin(wrapper, FINGERABDRUCK.slice(0, -1));
		await gibFingerabdruckEin(wrapper, FINGERABDRUCK.replace("AB", "XY"));
		expect(patch).not.toHaveBeenCalled();
	});

});
