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
			<svws-ui-table :lock-selectable="pendingStateManagerRegistry().pendingStateExists()" :clickable="!manager().liste.auswahlExists()"
				:clicked="clickedEintrag" @update:clicked="lehrerDaten => gotoDefaultView(lehrerDaten.id)" :items="rowsFiltered"
				:model-value="[...props.manager().liste.auswahl()]" @update:model-value="items => setAuswahl(items)" :columns selectable count
				:filter-open="true" :filtered="filterChanged()" :filterReset scroll-into-view scroll v-model:sort-by-and-order="sortByAndOrder"
				:sort-by-multi allow-arrow-key-selection :focus-switching-enabled :focus-help-visible>
				<template #search>
					<svws-ui-text-input v-model="search" type="search" placeholder="Suchen" />
				</template>
				<template #filterAdvanced>
					<svws-ui-multi-select v-model="filterPersonaltyp" title="Personaltyp" :items="manager().personaltypen.list()" :item-text="textPersonaltyp"
						class="col-span-full" />
					<div class="col-span-full flex flex-wrap gap-x-5">
						<svws-ui-checkbox type="toggle" v-model="filterNurSichtbar">Nur Sichtbare</svws-ui-checkbox>
						<svws-ui-checkbox type="toggle" v-model="filterNurStatistikrelevant">Nur Statistik-Relevante</svws-ui-checkbox>
					</div>
				</template>
				<template #actions>
					<svws-ui-tooltip v-if="serverState.hasDev && hatKompetenzAendern" position="bottom">
						<svws-ui-button :disabled="activeViewType === ViewType.HINZUFUEGEN" type="icon" @click="props.gotoHinzufuegenView(true)"
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
	import type { LehrerAuswahlProps } from "./LehrerAuswahlProps";
	import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import type { PersonalTyp } from "@core/core/types/PersonalTyp";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useServerState } from "@ui/states/ServerState";
	import type { SortByAndOrder } from "@ui/types";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";
	import { ViewType } from "@ui/ui/nav/ViewType";

	const props = defineProps<LehrerAuswahlProps>();
	const benutzerState = useBenutzerState();
	const serverState = useServerState();

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
		get: () => props.manager().filterNurSichtbar(),
		set: (value) => {
			props.manager().setFilterNurSichtbar(value);
			void props.setFilter();
			void props.setFilterNurSichtbar(value);
		},
	});

	const filterNurStatistikrelevant = computed<boolean>({
		get: () => props.manager().filterNurStatistikRelevant(),
		set: (value) => {
			props.manager().setFilterNurStatistikRelevant(value);
			void props.setFilter();
			void props.setFilterNurStatistikrelevant(value);
		},
	});

	const filterPersonaltyp = computed<PersonalTyp[]>({
		get: () => [...props.manager().personaltypen.auswahl()],
		set: (value) => {
			props.manager().personaltypen.auswahlClear();
			for (const v of value) {
				props.manager().personaltypen.auswahlAdd(v);
			}
			void props.setFilter();
		},
	});

	const sortByMulti = computed<Map<string, boolean>>(() => {
		const map = new Map<string, boolean>();
		for (const { field, ascending } of props.manager().orderGet()) {
			map.set(field, ascending);
		}
		return map;
	});

	const sortByAndOrder = computed<SortByAndOrder | undefined>({
		get: () => {
			const list = props.manager().orderGet();
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
			props.manager().orderUpdate(value.key, value.order);
			void props.setFilter();
		},
	});

	const search = ref<string>("");

	const rowsFiltered = computed<LehrerListeEintrag[]>(() => {
		const arr = [];
		const locale = search.value.toLocaleLowerCase();
		const searchValueIsNumber = /^[0-9]+$/.test(locale.trim());
		for (const e of props.manager().filtered()) {
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
		props.manager().personaltypen.auswahlClear();
		props.manager().setFilterNurSichtbar(true);
		props.manager().setFilterNurStatistikRelevant(true);
		await props.setFilter();
	}

	function filterChanged(): boolean {
		return (props.manager().personaltypen.auswahlExists());
	}

	const clickedEintrag = computed(() => {
		if ((props.activeViewType === ViewType.GRUPPENPROZESSE) || (props.activeViewType === ViewType.HINZUFUEGEN)) {
			return null;
		}
		return props.manager().hasDaten() ? props.manager().auswahl() : null;
	});

	async function setAuswahl(items: LehrerListeEintrag[]) {
		props.manager().liste.auswahlClear();
		for (const item of items) {
			if (props.manager().liste.hasValue(item)) {
				props.manager().liste.auswahlAdd(item);
			}
		}
		if (props.manager().liste.auswahlExists()) {
			await props.gotoGruppenprozessView(true);
		} else {
			await props.gotoDefaultView(props.manager().getVorherigeAuswahl()?.id);
		}
	}

</script>
