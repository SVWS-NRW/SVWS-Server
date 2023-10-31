<template>
	<svws-ui-secondary-menu>
		<template #headline>Klassen</template>
		<template #abschnitt>
			<abschnitt-auswahl :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="setAbschnitt" :akt-schulabschnitt="aktSchulabschnitt" />
		</template>
		<template #content>
			<svws-ui-table clickable :clicked="klassenListeManager().hasDaten() ? klassenListeManager().auswahl() : null" @update:clicked="gotoEintrag"
				:items="rowsFiltered" :model-value="selectedItems" @update:model-value="items => setAuswahl(items)"
				:columns="cols" selectable count :filter-open="true" :filtered="filterChanged()" :filterReset="filterReset" scroll-into-view scroll
				v-model:sort-by-and-order="sortByAndOrder" :sort-by-multi="sortByMulti">
				<template #search>
					<svws-ui-text-input v-model="search" type="search" placeholder="Suchen" />
				</template>
				<template #filterAdvanced>
					<svws-ui-checkbox type="toggle" v-model="filterNurSichtbare">Nur Sichtbare</svws-ui-checkbox>
					<svws-ui-multi-select v-model="filterJahrgaenge" title="Jahrgang" :items="klassenListeManager().jahrgaenge.list()" :item-text="text" :item-filter="find" />
					<svws-ui-multi-select v-model="filterLehrer" title="Klassenleitung" :items="klassenListeManager().lehrer.list()" :item-text="text" :item-filter="find" />
					<svws-ui-multi-select v-model="filterSchulgliederung" title="Schulgliederung" :items="klassenListeManager().schulgliederungen.list()" :item-text="text_schulgliederung" />
				</template>
				<template #cell(schueler)="{value}"> {{ value.size() }} </template>
				<template #cell(klassenLehrer)="{value}">
					{{ lehrerkuerzel(value) }}
				</template>
			</svws-ui-table>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import type { JahrgangsListeEintrag, KlassenListeEintrag, LehrerListeEintrag, Schulgliederung } from "@core";
	import type { KlassenAuswahlProps } from "./SKlassenAuswahlProps";
	import type { SortByAndOrder } from "@ui";
	import { computed, ref, shallowRef } from "vue";

	const props = defineProps<KlassenAuswahlProps>();

	const cols = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: "asc", span: 0.5 },
		{ key: "klassenLehrer", label: "Klassenleitung" },
		{ key: "schueler", label: "Schüler", span: 0.5, sortable: true }
	];

	function text(klasse: LehrerListeEintrag|JahrgangsListeEintrag): string {
		return klasse.kuerzel ?? "";
	}

	const find = (items: Iterable<LehrerListeEintrag|JahrgangsListeEintrag>, search: string) => {
		const list = [];
		for (const i of items)
			if (i.kuerzel?.toLocaleLowerCase().includes(search.toLocaleLowerCase()))
				list.push(i);
		return list;
	}

	function text_schulgliederung(schulgliederung: Schulgliederung): string {
		return schulgliederung.daten.kuerzel;
	}

	const filterNurSichtbare = computed<boolean>({
		get: () => props.klassenListeManager().filterNurSichtbar(),
		set: (value) => {
			props.klassenListeManager().setFilterNurSichtbar(value);
			void props.setFilter();
		}
	});

	const filterSchulgliederung = computed<Schulgliederung[]>({
		get: () => [...props.klassenListeManager().schulgliederungen.auswahl()],
		set: (value) => {
			props.klassenListeManager().schulgliederungen.auswahlClear();
			for (const v of value)
				props.klassenListeManager().schulgliederungen.auswahlAdd(v);
			void props.setFilter();
		}
	});

	const filterJahrgaenge = computed<JahrgangsListeEintrag[]>({
		get: () => [...props.klassenListeManager().jahrgaenge.auswahl()],
		set: (value) => {
			props.klassenListeManager().jahrgaenge.auswahlClear();
			for (const v of value)
				props.klassenListeManager().jahrgaenge.auswahlAdd(v);
			void props.setFilter();
		}
	});

	const filterLehrer = computed<LehrerListeEintrag[]>({
		get: () => [...props.klassenListeManager().lehrer.auswahl()],
		set: (value) => {
			props.klassenListeManager().lehrer.auswahlClear();
			for (const v of value)
				props.klassenListeManager().lehrer.auswahlAdd(v);
			void props.setFilter();
		}
	});

	const sortByMulti = computed<Map<string, boolean>>(() => {
		const map = new Map<string, boolean>();
		for (const pair of props.klassenListeManager().orderGet()) {
			if (pair.b !== null) {
				let colname = pair.a;
				if (colname === "klassen")
					colname = "kuerzel";
				if (colname === "schueleranzahl")
					colname = "schueler";
				map.set(colname, pair.b);
			}
		}
		return map;
	})

	const sortByAndOrder = computed<SortByAndOrder | undefined>({
		get: () => {
			const list = props.klassenListeManager().orderGet();
			if (list.isEmpty())
				return undefined;
			else {
				const { a: tmpkey, b: order} = list.get(0);
				let colname = tmpkey;
				if (colname === "klassen")
					colname = "kuerzel";
				if (colname === "schueleranzahl")
					colname = "schueler";
				return {key: colname, order};
			}
		},
		set: (value) => {
			if ((value === undefined) || (value.key === null))
				return;
			let key = value.key;
			if (key === "kuerzel")
				key = "klassen";
			if (key === "schueler")
				key = "schueleranzahl";
			props.klassenListeManager().orderUpdate(key, value.order);
			void props.setFilter();
		}
	})

	const search = ref<string>("");

	const rowsFiltered = computed<KlassenListeEintrag[]>(() => {
		const arr = [];
		for (const e of props.klassenListeManager().filtered())
			if (e.kuerzel?.toLocaleLowerCase().includes(search.value.toLocaleLowerCase()))
				arr.push(e);
		return arr;
	});

	async function filterReset() {
		props.klassenListeManager().schulgliederungen.auswahlClear();
		props.klassenListeManager().lehrer.auswahlClear();
		props.klassenListeManager().jahrgaenge.auswahlClear();
		await props.setFilter();
	}

	function filterChanged(): boolean {
		return (props.klassenListeManager().schulgliederungen.auswahlExists()
			|| props.klassenListeManager().lehrer.auswahlExists()
			|| props.klassenListeManager().jahrgaenge.auswahlExists());
	}

	const selectedItems = shallowRef<KlassenListeEintrag[]>([]);

	function setAuswahl(items : KlassenListeEintrag[]) {
		const schuelerauswahl = props.klassenListeManager().liste;
		for (const vorhanden of [ ... schuelerauswahl.auswahl() ])
			if (!items.includes(vorhanden))
				schuelerauswahl.auswahlRemove(vorhanden);
		for (const item of items)
			schuelerauswahl.auswahlAdd(item);
		selectedItems.value = [ ... schuelerauswahl.auswahl() ];
	}

	function lehrerkuerzel(list: number[]) {
		let s = '';
		if (props.klassenListeManager().hasDaten())
			for (const id of list) {
				const lehrer = props.klassenListeManager().lehrer.get(id);
				if (lehrer !== null) {
					if (s.length)
						s += `, ${lehrer.kuerzel}`;
					else s = lehrer.kuerzel;
				}
			}
		return s;
	}

</script>
