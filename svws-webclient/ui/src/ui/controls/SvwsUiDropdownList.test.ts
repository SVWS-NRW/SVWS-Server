import { afterEach, beforeEach, describe, expect, test, vi } from "vitest";
import SvwsUiDropdownList from "./SvwsUiDropdownList.vue";
import { mount, type VueWrapper } from "@vue/test-utils";
import type { Strategy } from "@floating-ui/vue";

const default_props = {
	filteredList: [],
	strategy: "fixed" as Strategy,
	floatingLeft: "50px",
	floatingTop: "100px",
};

type item = {
	text: string;
};

const items = [
	{ text: "Item 1" },
	{ text: "Item 2" },
	{ text: "Item 3" },
	{ text: "Item 4" },
];

describe("Komponente kann gemounted werden", () => {
	test("HTML wird erzeugt", async () => {
		const wrapper = mount(SvwsUiDropdownList, { props: default_props });

		expect(wrapper.html()).includes("svws-ui-dropdown-list");
		expect(wrapper.html()).includes("svws-ui-dropdown-list--items");
	});
});

describe.concurrent("Tests für die CSS-Props", () => {
	// [Propname, Klassen- oder Stylename, Beschreibung, Class(0)|Style(1)]
	test.each([
		[
			"statistics",
			"svws-statistik",
			"Prop statistics wird an CSS übergeben",
			0,
		],
		[
			"strategy",
			"position: absolute",
			"Prop strategy wird an CSS übergeben",
			1,
		],
		[
			"floatingLeft",
			"left: 100px",
			"Prop floatingLeft wird an CSS übergeben",
			1,
		],
		[
			"floatingTop",
			"top: 150px",
			"Prop floatingTop wird an CSS übergeben",
			1,
		],
	])("%s(%s) => %s", async (x, y, w, z) => {
		// Vorbereiten
		const wrapper = mount(SvwsUiDropdownList, { props: default_props });

		// x = default | undefined
		if (x === "statistics") {
			expect(wrapper.props().statistics).toBe(false);
		}
		if (x === "strategy") {
			expect(wrapper.props().strategy).toBe("fixed" as Strategy);
		}
		if (x === "floatingLeft") {
			expect(wrapper.props().floatingLeft).toBe("50px");
		}
		if (x === "floatingTop") {
			expect(wrapper.props().floatingTop).toBe("100px");
		}

		if (z === 0) {
			expect(
				wrapper.classes().filter((value) => value === y).length
			).toBe(0);
		}
		if (z === 1) {
			expect(wrapper.attributes("style")?.search(y)).toBe(-1);
		}

		// Testen
		if (x === "statistics") {
			await wrapper.setProps({ statistics: true });
			expect(wrapper.props().statistics).toBe(true);
		}
		if (x === "strategy") {
			await wrapper.setProps({ strategy: "absolute" as Strategy });
			expect(wrapper.props().strategy).toBe("absolute" as Strategy);
		}
		if (x === "floatingLeft") {
			await wrapper.setProps({ floatingLeft: "100px" });
			expect(wrapper.props().floatingLeft).toBe("100px");
		}
		if (x === "floatingTop") {
			await wrapper.setProps({ floatingTop: "150px" });
			expect(wrapper.props().floatingTop).toBe("150px");
		}

		if (z === 0) {
			expect(wrapper.classes()).toContain(y);
		}
		if (z === 1) {
			expect(wrapper.attributes("style")).toContain(y);
		}
	});
});

describe("Bedingtes Rendern mit CSS-Prüfung ", () => {
	let wrapper: VueWrapper<any>;
	beforeEach(() => {
		wrapper = mount(SvwsUiDropdownList, { props: default_props });
	});

	test("'Keine Einträge gefunden' wird gezeigt, wenn die Liste leer ist.", async () => {
		// Testen
		expect(wrapper.text()).toContain("Keine Einträge gefunden");
		const li = wrapper.get("li");
		[
			"px-2",
			"py-1.5",
			"text-base",
			"text-ui-disabled",
			"inline-block",
		].forEach((value) => expect(li.classes()).toContain(value));
	});

	test("Zeigt 'Keine Ergebnisse für {{ searchtext }} ' an, wenn keine Ergebnisse vorhanden sind", async () => {
		// Vorbereiten
		await wrapper.setProps({ searchText: "Test123" });
		await wrapper.vm.$nextTick();

		// Testen
		expect(wrapper.text()).toContain('Keine Ergebnisse für "Test123"');

		// CSS-Prüfung
		const li = wrapper.get("li");
		[
			"px-2",
			"py-1.5",
			"text-base",
			"text-ui-disabled",
			"inline-block",
		].forEach((value) => expect(li.classes()).toContain(value));
	});

	test("Anwendung der CSS-Klasse 'svws-active' auf den Listeneintrag, wenn er aktiv ist.", async () => {
		// Vorbereiten
		await wrapper.setProps({
			filteredList: items,
			itemText: (value: item) => {
				for (const v of items)
					if (value.text === v.text)
						return v.text;
				return "";
			},
		});
		wrapper.vm.activeItemIndex = 2;
		await wrapper.vm.$nextTick();

		// Testen
		const li = wrapper.get(".svws-active");
		expect(li.text()).toBe("Item 3");
	});

	test("Anwendung der CSS-Klasse 'svws-selected' auf den Listeneintrag, wenn er in der Auswahl ist", async () => {
		// Vorbereiten
		const selectedItems = new Set();
		selectedItems.add(items[0]);
		selectedItems.add(items[1]);
		await wrapper.setProps({
			filteredList: items,
			selectedItemList: selectedItems,
			itemText: (value: item) => {
				for (const v of items) {
					if (value.text === v.text) {
						return v.text;
					}
				}
				return "";
			},
		});

		// Testen
		const lis = wrapper.findAll(".svws-selected");
		expect(lis[0].text()).toBe("Item 1");
		expect(lis[1].text()).toBe("Item 2");
	});

	test("Setzt 'aria-selected' auf true bzw. false bei dem Listeneintrag, wenn er in der Auswahl ist bwz. nicht ist.", async () => {
		// Vorbereiten
		const selectedItems = new Set();
		selectedItems.add(items[0]);
		selectedItems.add(items[2]);
		await wrapper.setProps({
			filteredList: items,
			selectedItemList: selectedItems,
			itemText: (value: item) => {
				for (const v of items) {
					if (value.text === v.text) {
						return v.text;
					}
				}
				return "";
			},
		});

		// Testen
		const lis = wrapper.findAll(".svws-ui-dropdown-list--item");
		expect(lis[0].attributes("aria-selected")).toBe("true");
		expect(lis[1].attributes("aria-selected")).toBe("false");
		expect(lis[2].attributes("aria-selected")).toBe("true");
		expect(lis[3].attributes("aria-selected")).toBe("false");
	});

	test("Zeigt '-' mit Class 'opacity-25' an, wenn der Listeneintrag keinen Text hat", async () => {
		// Vorbereiten
		await wrapper.setProps({
			filteredList: items,
			itemText: (value: item) => {
				return "";
			},
		});

		// Testen
		const span = wrapper.get("li").find(".opacity-25");
		expect(span.classes().some((e) => e === "font-bold")).toBe(false);
		expect(span.text()).toContain("—");
	});

	test("Zeigt den Text ohne Class 'font-bold' an, wenn der Listeneintrag nicht higlighted Item ist.", async () => {
		// Vorbereiten
		await wrapper.setProps({
			filteredList: items,
			itemText: (value: item) => {
				for (const v of items) {
					if (value.text === v.text) {
						return v.text;
					}
				}
				return "";
			},
		});
		// Testen
		const spans = wrapper.find("li").findAll("span");
		let i = 1;
		for (const span of spans) {
			expect(span.text()).toBe("Item " + i);
			expect(span.classes().some((e) => e === "font-bold")).toBe(false);
			i++;
		}
	});

	test("Zeigt den Text mit Class 'font-bold' an, wenn der Listeneintrag higlighted Item ist.", async () => {
		// Vorbereiten
		await wrapper.setProps({
			filteredList: items,
			highlightItem: items[2],
			itemText: (value: item) => {
				for (const v of items) {
					if (value.text === v.text) {
						return v.text;
					}
				}
				return "";
			},
		});
		// Testen
		const span = wrapper
			.findAll("span")
			.filter((node) => node.text() === "Item 3");
		expect(span.length).toBe(1);
		expect(span[0].text()).toBe("Item 3");
		expect(span[0].classes()).toContain("font-bold");
	});

	test("Zeigt das Icon Häkchen an, wenn der Listeneintrag in der Auswahl ist.", async () => {
		// Vorbereiten
		const selectedItems = new Set();
		selectedItems.add(items[0]);
		selectedItems.add(items[1]);
		await wrapper.setProps({
			filteredList: items,
			selectedItemList: selectedItems,
		});
		// Testen
		const span = wrapper.get("li").find(".icon");
		[
			"icon",
			"i-ri-check-line",
			"w-5",
			"flex-shrink-0",
			"-mr-1",
			"-my-1",
			"relative",
			"top-1.5",
		].forEach((value) => expect(span.classes()).toContain(value));
	});

	afterEach(() => {
		wrapper.unmount();
	});
});

describe.concurrent("Tests Funktionen/Computeds", () => {
	let wrapper: VueWrapper;
	beforeEach(() => {
		wrapper = mount(SvwsUiDropdownList, { props: default_props });
	});
	test("Prüft den Aufruf der Funktion 'selectItem' ", async () => {
		// Vorbereiten
		const stub = vi.fn((selectedItem) => {});
		await wrapper.setProps({
			filteredList: items,
			selectItem: stub,
		});
		// Testen
		await wrapper
			.findAll(".svws-ui-dropdown-list--item")[0]
			.trigger("click");

		expect(stub).toBeCalled();
	});

	test("function->listIdPrefix liefert einen string-Wert zurück", async () => {
		// Testen
		const listIdPrefix = await wrapper.findComponent({
			name: "SvwsUiDropdownList",
		}).vm.listIdPrefix;
		expect((listIdPrefix as string).length > 0).toBe(true);
	});

	test("computed->listEmpty liefert true, wenn die Liste leer ist", async () => {
		// Testen
		const listEmpty = await wrapper.findComponent({
			name: "SvwsUiDropdownList",
		}).vm.listEmpty;
		expect(listEmpty).toBe(true);
	});

	test("computed->listEmpty liefert false, wenn die Liste nicht leer ist", async () => {
		// Vorbereiten
		await wrapper.setProps({ filteredList: items });
		// Testen
		const listEmpty = await wrapper.findComponent({
			name: "SvwsUiDropdownList",
		}).vm.listEmpty;
		expect(listEmpty).toBe(false);
	});

	afterEach(() => {
		wrapper.unmount();
	});
});

describe.concurrent("Test Events", () => {
	test("Beim Mouse Enter wird die Variable 'activeItemIndex auf -1 gesetzt.", async () => {
		const wrapper = mount(SvwsUiDropdownList, { props: default_props });
		const ul = wrapper.get("ul");
		wrapper.vm.activeItemIndex = 2;
		await (wrapper.vm as any).$nextTick();
		await ul.trigger("mouseenter");
		expect(wrapper.vm.activeItemIndex).toBe(-1);
	});
});

describe.concurrent("Tests für Exposed-Variablen", () => {
	test("Konstante 'floating' ist korrekt initialisiert", async () => {
		const wrapper = mount(SvwsUiDropdownList, { props: default_props });

		// Zugriff auf die exposed ref 'floating'
		expect(wrapper.vm.floating).toBeDefined();
		expect(wrapper.vm.floating?.innerText).toContain("Keine Einträge gefunden");

		// Aktualisiere 'floating' manuell
		const newFloatingElement = document.createElement("div");
		wrapper.vm.floating = newFloatingElement;
		await wrapper.vm.$nextTick();

		// Überprüfen, ob 'floating' das richtige Element enthält
		expect(wrapper.vm.floating).toBe(newFloatingElement);
	});

	test("Konstante 'itemRefs' ist korrekt initialisiert", async () => {
		const wrapper = mount(SvwsUiDropdownList, { props: default_props });
		const itemRefs = wrapper.vm.itemRefs;
		expect(itemRefs).toBeDefined();
		expect(Array.isArray(itemRefs)).toBe(true);
		expect(itemRefs.length).toBe(0);

		// Zugriff auf die exposed ref 'itemRefs'
		await wrapper.setProps({ filteredList: items });
		expect(itemRefs.length).toBe(4);
	});

	test("Konstante 'activeItemIndex' ist korrekt initialisiert", () => {
		const wrapper = mount(SvwsUiDropdownList, { props: default_props });
		const activeItemIndex = wrapper.vm.activeItemIndex;

		// Überprüfe, ob 'activeItemIndex' korrekt auf -1 initialisiert ist
		expect(activeItemIndex).toBeDefined();
		expect(activeItemIndex).toBe(-1);
	});

	test("Aktualisiert 'activeItemIndex' korrekt", async () => {
		const wrapper = mount(SvwsUiDropdownList, { props: default_props });
		// Vorbereiten
		await wrapper.setProps({ filteredList: items });
		wrapper.vm.activeItemIndex = 2;

		// Testen
		await (wrapper.vm as any).$nextTick();
		expect(wrapper.vm.activeItemIndex).toBe(2);
		expect(wrapper.findAll(".svws-active").length).toBe(1);
		const activeItem = wrapper.findAll(".svws-ui-dropdown-list--item")[2];
		expect(activeItem.classes()).toContain("svws-active");
	});
});
