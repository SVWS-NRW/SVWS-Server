<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Lehrkräfte</h1>
			<div class="input--schule-abschnitte">
				<abschnitt-auswahl :daten="schuljahresabschnittsauswahl" />
			</div>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table :clickable="!manager().liste.auswahlExists()" :clicked="clickedEintrag" @update:clicked="lehrerDaten => gotoDefaultView(lehrerDaten.id)"
				:items="rowsFiltered" :model-value="[...props.manager().liste.auswahl()]" @update:model-value="items => setAuswahl(items)"
				:columns selectable count :filter-open="true" :filtered="filterChanged()" :filterReset scroll-into-view scroll
				v-model:sort-by-and-order="sortByAndOrder" :sort-by-multi allow-arrow-key-selection :focus-switching-enabled :focus-help-visible>
				<template #search>
					<svws-ui-text-input v-model="search" type="search" placeholder="Suchen" removable />
				</template>
				<template #filterAdvanced>
					<svws-ui-multi-select v-model="filterPersonaltyp" title="Personaltyp" :items="manager().personaltypen.list()" :item-text="textPersonaltyp" class="col-span-full" />
					<div class="col-span-full flex flex-wrap gap-x-5">
						<svws-ui-checkbox type="toggle" v-model="filterNurSichtbar">Nur Sichtbare</svws-ui-checkbox>
						<svws-ui-checkbox type="toggle" v-model="filterNurStatistikrelevant">Nur Statistik-Relevante</svws-ui-checkbox>
					</div>
				</template>
				<template #actions>
					<svws-ui-tooltip position="bottom" v-if="ServerMode.DEV.checkServerMode(serverMode) && hatKompetenzAendern">
						<svws-ui-button :disabled="activeViewType === ViewType.HINZUFUEGEN" type="icon" @click="props.gotoHinzufuegenView(true)" :has-focus="rowsFiltered.length === 0">
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
	import type { SortByAndOrder} from "@ui";
	import { ViewType } from "@ui";
	import type { PersonalTyp, LehrerListeEintrag } from "@core";
	import { ServerMode, BenutzerKompetenz } from "@core";
	import type { LehrerAuswahlProps } from "./SLehrerAuswahlProps";
	import { useRegionSwitch } from "~/components/useRegionSwitch";

	const props = defineProps<LehrerAuswahlProps>();
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const hatKompetenzAendern = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.LEHRERDATEN_AENDERN));

	const columns = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: "asc" },
		{ key: "nachname", label: "Nachname", sortable: true, span: 2 },
		{ key: "vorname", label: "Vorname", sortable: true, span: 2 },
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
			for (const v of value)
				props.manager().personaltypen.auswahlAdd(v);
			void props.setFilter();
		},
	});

	const sortByMulti = computed<Map<string, boolean>>(() => {
		const map = new Map<string, boolean>();
		for (const pair of props.manager().orderGet())
			if (pair.b !== null)
				map.set(pair.a, pair.b);
		return map;
	})

	const sortByAndOrder = computed<SortByAndOrder | undefined>({
		get: () => {
			const list = props.manager().orderGet();
			if (list.isEmpty())
				return undefined;
			else {
				const { a: key, b: order} = list.get(0);
				return { key, order };
			}
		},
		set: (value) => {
			if ((value === undefined) || (value.key === null))
				return;
			props.manager().orderUpdate(value.key, value.order);
			void props.setFilter();
		},
	})

	const search = ref<string>("");

	const rowsFiltered = computed<LehrerListeEintrag[]>(() => {
		const arr = [];
		for (const e of props.manager().filtered())
			if (e.nachname.toLocaleLowerCase().includes(search.value.toLocaleLowerCase())
				|| e.vorname.toLocaleLowerCase().includes(search.value.toLocaleLowerCase())
				|| e.kuerzel.toLocaleLowerCase().includes(search.value.toLocaleLowerCase()))
				arr.push(e);
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
		if ((props.activeViewType === ViewType.GRUPPENPROZESSE) || (props.activeViewType === ViewType.HINZUFUEGEN))
			return null;
		return props.manager().hasDaten() ? props.manager().auswahl() : null;
	});

	async function setAuswahl(items : LehrerListeEintrag[]) {
		props.manager().liste.auswahlClear();
		for (const item of items)
			if (props.manager().liste.hasValue(item))
				props.manager().liste.auswahlAdd(item);
		if (props.manager().liste.auswahlExists())
			await props.gotoGruppenprozessView(true);
		else
			await props.gotoDefaultView(props.manager().getVorherigeAuswahl()?.id);
	}

</script>
