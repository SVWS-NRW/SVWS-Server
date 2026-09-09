<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Klassen</h1>
			<div><abschnitt-auswahl /></div>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table :clickable="!klassenState.manager.liste.auswahlExists()" :clicked="clickedEintrag" @update:clicked="klassendaten => klassenState.gotoDefaultView(klassendaten.id)"
				:items="rowsFiltered" :model-value="[...klassenState.manager.liste.auswahl()]" @update:model-value="items => setAuswahl(items)"
				:columns selectable count :filter-open="true" :filtered="filterChanged()" :filterReset scroll-into-view scroll allow-arrow-key-selection
				:focus-switching-enabled :focus-help-visible>
				<template #search>
					<svws-ui-text-input v-model="search" type="search" placeholder="Suchen" />
				</template>
				<template #filterAdvanced>
					<svws-ui-multi-select v-model="filterJahrgaenge" title="Jahrgang" :items="klassenState.manager.jahrgaenge.list()" :item-text="text" :item-filter="find" />
					<svws-ui-multi-select v-model="filterLehrer" title="Klassenleitung" :items="klassenState.manager.lehrer.list()" :item-text="text" :item-filter="find" />
					<svws-ui-multi-select v-model="filterSchulgliederung" title="Schulgliederung" :items="klassenState.manager.schulgliederungen.list()" :item-text="textSchulgliederung" />
				</template>
				<template #cell(anzahlZugeordneteSchueler)="{value}"> {{ value }} </template>
				<template #cell(idsKlassenleitungen)="{value}">
					{{ lehrerkuerzel(value) }}
				</template>
				<template #actions v-if="hatKompetenzAendern">
					<div class="flex gap-5">
						<template v-if="klassenState.manager.liste.size() > 0">
							<klassen-auswahl-sortierung-modal v-slot="{ openModal }">
								<svws-ui-tooltip position="bottom">
									<svws-ui-button type="secondary" @click="openModal">
										Standardsortierung anwenden&nbsp;...
									</svws-ui-button>
									<template #content>
										Standardsortierung wiederherstellen
									</template>
								</svws-ui-tooltip>
							</klassen-auswahl-sortierung-modal>
						</template>
						<svws-ui-tooltip position="bottom">
							<svws-ui-button :disabled="klassenState.activeViewType === ViewType.HINZUFUEGEN" type="icon" @click="klassenState.gotoHinzufuegenView(true)" :has-focus="rowsFiltered.length === 0">
								<span class="icon i-ri-add-line" />
							</svws-ui-button>
							<template #content>
								Neue Klasse anlegen
							</template>
						</svws-ui-tooltip>
					</div>
				</template>
			</svws-ui-table>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";

	import type { KlassenListeEintrag } from "@core/asd/data/klassen/KlassenListeEintrag";
	import type { Schulgliederung } from "@core/asd/types/schule/Schulgliederung";
	import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
	import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useAbschnittState } from "@ui/states/AbschnittState";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";
	import { ViewType } from "@ui/ui/nav/ViewType";

	import { useKlassenState } from "~/states/klassen/KlassenState";

	const klassenState = useKlassenState();
	const benutzerState = useBenutzerState();
	const abschnittState = useAbschnittState();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const hatKompetenzAendern = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN));

	const columns = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: "asc", span: 0.5 },
		{ key: "idsKlassenleitungen", label: "Klassenleitung" },
		{ key: "anzahlZugeordneteSchueler", label: "Schüler", span: 0.5, sortable: true },
	];

	function text(eintrag: LehrerListeEintrag | JahrgangsDaten): string {
		return eintrag.kuerzel ?? "";
	}

	function find(items: Iterable<LehrerListeEintrag | JahrgangsDaten>, search: string) {
		const list = [];
		for (const i of items) {
			if ((i.kuerzel !== null) && i.kuerzel.toLocaleLowerCase().includes(search.toLocaleLowerCase())) {
				list.push(i);
			}
		}
		return list;
	}

	function textSchulgliederung(schulgliederung: Schulgliederung): string {
		return schulgliederung.daten(abschnittState.auswahl.schuljahr)?.kuerzel ?? '—';
	}

	const filterSchulgliederung = computed<Schulgliederung[]>({
		get: () => [...klassenState.manager.schulgliederungen.auswahl()],
		set: (value) => {
			klassenState.manager.schulgliederungen.auswahlClear();
			for (const v of value) {
				klassenState.manager.schulgliederungen.auswahlAdd(v);
			}
			void klassenState.setFilter();
		},
	});

	const filterJahrgaenge = computed<JahrgangsDaten[]>({
		get: () => [...klassenState.manager.jahrgaenge.auswahl()],
		set: (value) => {
			klassenState.manager.jahrgaenge.auswahlClear();
			for (const v of value) {
				klassenState.manager.jahrgaenge.auswahlAdd(v);
			}
			void klassenState.setFilter();
		},
	});

	const filterLehrer = computed<LehrerListeEintrag[]>({
		get: () => [...klassenState.manager.lehrer.auswahl()],
		set: (value) => {
			klassenState.manager.lehrer.auswahlClear();
			for (const v of value) {
				klassenState.manager.lehrer.auswahlAdd(v);
			}
			void klassenState.setFilter();
		},
	});

	const search = ref<string>("");

	const rowsFiltered = computed<KlassenListeEintrag[]>(() => {
		const arr = [];
		for (const e of klassenState.manager.filtered()) {
			if ((e.kuerzel !== null) && e.kuerzel.toLocaleLowerCase().includes(search.value.toLocaleLowerCase())) {
				arr.push(e);
			}
		}
		arr.sort((a, b) => a.sortierung - b.sortierung);
		return arr;
	});

	async function filterReset() {
		klassenState.manager.schulgliederungen.auswahlClear();
		klassenState.manager.lehrer.auswahlClear();
		klassenState.manager.jahrgaenge.auswahlClear();
		await klassenState.setFilter();
	}

	function filterChanged(): boolean {
		return (klassenState.manager.schulgliederungen.auswahlExists()
			|| klassenState.manager.lehrer.auswahlExists()
			|| klassenState.manager.jahrgaenge.auswahlExists());
	}

	const clickedEintrag = computed(() => {
		if ((klassenState.activeViewType === ViewType.GRUPPENPROZESSE) || (klassenState.activeViewType === ViewType.HINZUFUEGEN)) {
			return null;
		}
		return klassenState.manager.hasDaten() ? klassenState.manager.auswahl() : null;
	});

	async function setAuswahl(items: KlassenListeEintrag[]) {
		klassenState.manager.liste.auswahlClear();
		for (const item of items) {
			if (klassenState.manager.liste.hasValue(item)) {
				klassenState.manager.liste.auswahlAdd(item);
			}
		}
		if (klassenState.manager.liste.auswahlExists()) {
			await klassenState.gotoGruppenprozessView(true);
		} else {
			await klassenState.gotoDefaultView(klassenState.manager.getVorherigeAuswahl()?.id);
		}
	}

	function lehrerkuerzel(list: number[]) {
		let s = '';
		if (klassenState.manager.hasDaten()) {
			for (const id of list) {
				const lehrer = klassenState.manager.lehrer.get(id);
				if (lehrer !== null) {
					if (s.length > 0) {
						s += `, ${lehrer.kuerzel}`;
					} else {
						s = lehrer.kuerzel;
					}
				}
			}
		}
		return s;
	}

</script>
