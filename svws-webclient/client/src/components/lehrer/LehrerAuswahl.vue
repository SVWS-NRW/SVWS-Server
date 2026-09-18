<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Lehrkräfte</h1>
			<div>
				<abschnitt-auswahl />
			</div>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table :lock-selectable="pendingStateManagerRegistry().pendingStateExists()" :clickable="!lehrerAuswahlState.manager.liste.auswahlExists()"
				:clicked="clickedEintrag" @update:clicked="lehrerDaten => lehrerAuswahlState.gotoDefaultView(lehrerDaten.id)" :items="rowsFiltered"
				:model-value="[...lehrerAuswahlState.manager.liste.auswahl()]" @update:model-value="items => setAuswahl(items)" :columns selectable count
				:filter-open="true" :filtered="filterChanged()" :filterReset scroll-into-view scroll v-model:sort-by-and-order="sortByAndOrder"
				:sort-by-multi allow-arrow-key-selection :focus-switching-enabled :focus-help-visible>
				<template #search>
					<svws-ui-text-input v-model="search" type="search" placeholder="Suchen" />
				</template>
				<template #filterAdvanced>
					<svws-ui-multi-select v-model="filterPersonaltyp" title="Personaltyp" :items="lehrerAuswahlState.manager.personaltypen.list()" :item-text="textPersonaltyp"
						class="col-span-full" />
					<div class="col-span-full flex flex-wrap gap-x-5">
						<svws-ui-checkbox type="toggle" v-model="filterNurSichtbar">Nur Sichtbare</svws-ui-checkbox>
						<svws-ui-checkbox type="toggle" v-model="filterNurStatistikrelevant">Nur Statistik-Relevante</svws-ui-checkbox>
					</div>
				</template>
				<template #actions>
					<svws-ui-tooltip v-if="serverState.hasDev && hatKompetenzAendern" position="bottom">
						<svws-ui-button :disabled="lehrerAuswahlState.activeViewType === ViewType.HINZUFUEGEN" type="icon" @click="lehrerAuswahlState.gotoHinzufuegenView(true)"
							:has-focus="rowsFiltered.length === 0">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
						<template #content>
							Neuen Lehrer anlegen
						</template>
					</svws-ui-tooltip>
				</template>
			</svws-ui-table>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";

	import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import type { PersonalTyp } from "@core/core/types/PersonalTyp";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useServerState } from "@ui/states/ServerState";
	import type { SortByAndOrder } from "@ui/types";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";
	import { ViewType } from "@ui/ui/nav/ViewType";

	import { useLehrerAuswahlState } from "~/states/lehrer/LehrerAuswahlState";

	import type { LehrerAuswahlProps } from "./LehrerAuswahlProps";

	const props = defineProps<LehrerAuswahlProps>();
	const benutzerState = useBenutzerState();
	const serverState = useServerState();
	const lehrerAuswahlState = useLehrerAuswahlState();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const hatKompetenzAendern = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.LEHRERDATEN_AENDERN));

	const columns = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: "asc" },
		{ key: "nachname", label: "Nachname", sortable: true, span: 2 },
		{ key: "vorname", label: "Rufname", sortable: true, span: 2 },
	];

	function textPersonaltyp(personaltyp: PersonalTyp): string {
		return personaltyp.bezeichnung;
	}

	const filterNurSichtbar = computed<boolean>({
		get: () => lehrerAuswahlState.manager.filterNurSichtbar(),
		set: (value) => {
			lehrerAuswahlState.manager.setFilterNurSichtbar(value);
			void lehrerAuswahlState.setFilter();
			void lehrerAuswahlState.setFilterNurSichtbar(value);
		},
	});

	const filterNurStatistikrelevant = computed<boolean>({
		get: () => lehrerAuswahlState.manager.filterNurStatistikRelevant(),
		set: (value) => {
			lehrerAuswahlState.manager.setFilterNurStatistikRelevant(value);
			void lehrerAuswahlState.setFilter();
			void lehrerAuswahlState.setFilterNurStatistikrelevant(value);
		},
	});

	const filterPersonaltyp = computed<PersonalTyp[]>({
		get: () => [...lehrerAuswahlState.manager.personaltypen.auswahl()],
		set: (value) => {
			lehrerAuswahlState.manager.personaltypen.auswahlClear();
			for (const v of value) {
				lehrerAuswahlState.manager.personaltypen.auswahlAdd(v);
			}
			void lehrerAuswahlState.setFilter();
		},
	});

	const sortByMulti = computed<Map<string, boolean>>(() => {
		const map = new Map<string, boolean>();
		for (const { field, ascending } of lehrerAuswahlState.manager.orderGet()) {
			map.set(field, ascending);
		}
		return map;
	});

	const sortByAndOrder = computed<SortByAndOrder | undefined>({
		get: () => {
			const list = lehrerAuswahlState.manager.orderGet();
			if (list.length === 0) {
				return undefined;
			} else {
				const { field: key, ascending: order } = list[0];
				return { key, order };
			}
		},
		set: (value) => {
			if ((value === undefined) || (value.key === null)) {
				return;
			}
			lehrerAuswahlState.manager.orderUpdate(value.key, value.order);
			void lehrerAuswahlState.setFilter();
		},
	});

	const search = ref<string>("");

	const rowsFiltered = computed<LehrerListeEintrag[]>(() => {
		const arr = [];
		const locale = search.value.toLocaleLowerCase();
		const searchValueIsNumber = /^\d+$/.test(locale.trim());
		for (const e of lehrerAuswahlState.manager.filtered()) {
			if ((searchValueIsNumber && e.id.toString().includes(locale))
				|| e.nachname.toLocaleLowerCase().includes(locale)
				|| e.vorname.toLocaleLowerCase().includes(locale)
				|| e.kuerzel.toLocaleLowerCase().includes(locale)) {
				arr.push(e);
			}
		}
		return arr;
	});

	async function filterReset() {
		lehrerAuswahlState.manager.personaltypen.auswahlClear();
		lehrerAuswahlState.manager.setFilterNurSichtbar(true);
		lehrerAuswahlState.manager.setFilterNurStatistikRelevant(true);
		await lehrerAuswahlState.setFilter();
	}

	function filterChanged(): boolean {
		return (lehrerAuswahlState.manager.personaltypen.auswahlExists());
	}

	const clickedEintrag = computed(() => {
		if ((lehrerAuswahlState.activeViewType === ViewType.GRUPPENPROZESSE) || (lehrerAuswahlState.activeViewType === ViewType.HINZUFUEGEN)) {
			return null;
		}
		return lehrerAuswahlState.manager.hasDaten() ? lehrerAuswahlState.manager.auswahl() : null;
	});

	async function setAuswahl(items: LehrerListeEintrag[]) {
		lehrerAuswahlState.manager.liste.auswahlClear();
		for (const item of items) {
			if (lehrerAuswahlState.manager.liste.hasValue(item)) {
				lehrerAuswahlState.manager.liste.auswahlAdd(item);
			}
		}
		if (lehrerAuswahlState.manager.liste.auswahlExists()) {
			await lehrerAuswahlState.gotoGruppenprozessView(true);
		} else {
			await lehrerAuswahlState.gotoDefaultView(lehrerAuswahlState.manager.getVorherigeAuswahl()?.id);
		}
	}

</script>
