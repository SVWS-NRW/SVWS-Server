<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<span>Lehrkräfte</span>
		</template>
		<template #abschnitt>
			<abschnitt-auswahl :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="setAbschnitt" :akt-schulabschnitt="aktSchulabschnitt" />
		</template>
		<template #content>
			<svws-ui-table clickable :clicked="lehrerListeManager().hasDaten() ? lehrerListeManager().auswahl() : null" @update:clicked="gotoLehrer"
				:items="rowsFiltered.values()" :model-value="selectedItems" @update:model-value="items => setAuswahl(items)"
				:columns="cols" selectable count :filter-open="true" :filtered="filterChanged()" :filterReset="filterReset" scroll-into-view scroll
				v-model:sort-by-and-order="sortByAndOrder" :sort-by-multi="sortByMulti">
				<template #search>
					<svws-ui-text-input v-model="search" type="search" placeholder="Suchen" />
				</template>
				<template #filterAdvanced>
					<svws-ui-multi-select v-model="filterPersonaltyp" title="Personaltyp" :items="lehrerListeManager().personaltypen.list()" :item-text="textPersonaltyp" class="col-span-full" />
					<div class="col-span-full flex flex-wrap gap-x-5">
						<svws-ui-checkbox type="toggle" v-model="filterNurSichtbare">Nur Sichtbare</svws-ui-checkbox>
						<svws-ui-checkbox type="toggle" v-model="filterNurStatistikrelevant">Nur Statistik-Relevante</svws-ui-checkbox>
					</div>
				</template>
			</svws-ui-table>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { computed, ref, shallowRef } from "vue";
	import { type SortByAndOrder } from "@ui";
	import { type PersonalTyp, type LehrerListeEintrag } from "@core";
	import { type LehrerAuswahlProps } from "./SLehrerAuswahlProps";

	const props = defineProps<LehrerAuswahlProps>();

	const cols = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: "asc" },
		{ key: "nachname", label: "Nachname", sortable: true, span: 2 },
		{ key: "vorname", label: "Vorname", sortable: true, span: 2 }
	];

	function textPersonaltyp(personaltyp: PersonalTyp): string {
		return personaltyp.bezeichnung;
	}

	const filterNurSichtbare = computed<boolean>({
		get: () => props.lehrerListeManager().filterNurSichtbar(),
		set: (value) => {
			props.lehrerListeManager().setFilterNurSichtbar(value);
			void props.setFilter();
		}
	});

	const filterNurStatistikrelevant = computed<boolean>({
		get: () => props.lehrerListeManager().filterNurStatistikRelevant(),
		set: (value) => {
			props.lehrerListeManager().setFilterNurStatistikRelevant(value);
			void props.setFilter();
		}
	});

	const filterPersonaltyp = computed<PersonalTyp[]>({
		get: () => [...props.lehrerListeManager().personaltypen.auswahl()],
		set: (value) => {
			props.lehrerListeManager().personaltypen.auswahlClear();
			for (const v of value)
				props.lehrerListeManager().personaltypen.auswahlAdd(v);
			void props.setFilter();
		}
	});


	const sortByMulti = computed<Map<string, boolean>>(() => {
		const map = new Map<string, boolean>();
		for (const pair of props.lehrerListeManager().orderGet())
			if (pair.b !== null)
				map.set(pair.a, pair.b);
		return map;
	})

	const sortByAndOrder = computed<SortByAndOrder | undefined>({
		get: () => {
			const list = props.lehrerListeManager().orderGet();
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
			props.lehrerListeManager().orderUpdate(value.key, value.order);
			void props.setFilter();
		}
	})


	const actions = [
		{ label: "Löschen", action: "delete" },
		{ label: "Kopieren", action: "copy" }
	];

	const search = ref<string>("");

	const rowsFiltered = computed<LehrerListeEintrag[]>(() => {
		const arr = [];
		for (const e of props.lehrerListeManager().filtered())
			if (e.nachname.toLocaleLowerCase().includes(search.value.toLocaleLowerCase())
				|| e.vorname.toLocaleLowerCase().includes(search.value.toLocaleLowerCase())
				|| e.kuerzel.toLocaleLowerCase().includes(search.value.toLocaleLowerCase()))
				arr.push(e);
		return arr;
	});

	async function filterReset() {
		props.lehrerListeManager().personaltypen.auswahlClear();
		props.lehrerListeManager().setFilterNurSichtbar(true);
		props.lehrerListeManager().setFilterNurStatistikRelevant(true);
		await props.setFilter();
	}

	function filterChanged(): boolean {
		return (props.lehrerListeManager().personaltypen.auswahlExists());
	}

	const selectedItems = shallowRef<LehrerListeEintrag[]>([]);

	function setAuswahl(items : LehrerListeEintrag[]) {
		const auswahl = props.lehrerListeManager().liste;
		for (const vorhanden of [ ... auswahl.auswahl() ])
			if (!items.includes(vorhanden))
				auswahl.auswahlRemove(vorhanden);
		for (const item of items)
			auswahl.auswahlAdd(item);
		selectedItems.value = [ ... auswahl.auswahl() ];
	}

	function onAction(action: string, item: LehrerListeEintrag) {
		console.log(action, item);
	}

</script>
