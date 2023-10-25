<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<span>Schüler</span>
		</template>
		<template #abschnitt>
			<abschnitt-auswahl :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="setAbschnitt" :akt-schulabschnitt="aktSchulabschnitt" />
		</template>
		<template #content>
			<svws-ui-table :clicked="auswahl" @update:clicked="gotoSchueler" :model-value="selectedItems" @update:model-value="setAuswahlGruppe" :items="rowsFiltered"
				:columns="cols" clickable selectable count :filter-open="true" filtered :filterReset="filterReset" scroll-into-view scroll
				v-model:sort-by-and-order="sortByAndOrder" :sort-by-multi="sortByMulti">
				<template #search>
					<svws-ui-text-input v-model="search" type="search" placeholder="Suchen" />
				</template>
				<template #filterAdvanced>
					<svws-ui-multi-select v-model="filterStatus" :items="schuelerListeManager().schuelerstatus.list()" :item-text="text_status" class="col-span-full" />
					<svws-ui-multi-select v-model="filterKlassen" title="Klasse" :items="schuelerListeManager().klassen.list()" :item-text="text" autocomplete :item-filter="find" />
					<svws-ui-multi-select v-model="filterJahrgaenge" title="Jahrgang" :items="schuelerListeManager().jahrgaenge.list()" :item-text="text" autocomplete :item-filter="find" />
					<svws-ui-multi-select v-model="filterKurse" title="Kurs" :items="schuelerListeManager().kurse.list()" :item-text="text" autocomplete :item-filter="find" />
					<svws-ui-multi-select v-model="filterSchulgliederung" title="Schulgliederung" :items="schuelerListeManager().schulgliederungen.list()" :item-text="text_schulgliederung" />
					<!--					<svws-ui-button type="transparent" class="justify-center">
						<i-ri-filter-line />
						Erweiterte Filter
					</svws-ui-button>-->
				</template>
				<template #cell(idKlasse)="{ value }">
					{{ value === null ? "–" : schuelerListeManager().klassen.get(value)?.kuerzel }}
				</template>
				<template #actions>
					<svws-ui-button v-if="selectedItems.length" type="transparent" @click="showModalGruppenaktionen().value = true">
						<i-ri-edit-2-line />
						Auswahl bearbeiten
					</svws-ui-button>
					<svws-ui-button type="icon" @click="addLine()">
						<i-ri-add-line />
					</svws-ui-button>
					<svws-ui-button type="icon" disabled>
						<i-ri-file-copy-line />
					</svws-ui-button>
					<svws-ui-button type="icon" disabled>
						<i-ri-more-2-line />
					</svws-ui-button>
				</template>
			</svws-ui-table>
		</template>
	</svws-ui-secondary-menu>
	<svws-ui-modal :show="showModalGruppenaktionen" size="medium">
		<template #modalTitle>
			Aktionen für {{ selectedItems.length }} ausgewählte Schüler
		</template>

		<template #modalContent>
			<div class="opacity-50 mb-4">
				{{ selectedItems.splice(0,10).map(schueler => schueler.vorname + ' ' + schueler.nachname).join(', ') }}
				{{ selectedItems.length > 10 ? ' und ' + (selectedItems.length - 10) + ' weitere' : '' }}
			</div>
			<svws-ui-input-wrapper :grid="4">
				<svws-ui-button type="transparent">Button</svws-ui-button>
				<svws-ui-button type="transparent">Button</svws-ui-button>
				<svws-ui-button type="transparent">Button</svws-ui-button>
				<svws-ui-button type="transparent">Button</svws-ui-button>
				<svws-ui-button type="transparent">Button</svws-ui-button>
			</svws-ui-input-wrapper>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import type { SchuelerListeEintrag, JahrgangsListeEintrag, KlassenListeEintrag, KursListeEintrag, Schulgliederung, SchuelerStatus } from "@core";
	import type { SchuelerAuswahlProps } from "./SSchuelerAuswahlProps";
	import type { SortByAndOrder } from "@ui";
	import { ArrayList, Pair } from "@core";
	import { computed, ref, watch } from "vue";

	const props = defineProps<SchuelerAuswahlProps>();

	const _showModalGruppenaktionen = ref<boolean>(false);
	const showModalGruppenaktionen = () => _showModalGruppenaktionen;

	const search = ref<string>("");

	const sortByMulti = computed<Map<string, boolean>>(() => {
		const map = new Map<string, boolean>();
		for (const pair of props.schuelerListeManager().orderGet())
			if (pair.b !== null)
				map.set(pair.a === "klassen" ? "idKlasse" : pair.a, pair.b);
		return map;
	})

	const sortByAndOrder = computed<SortByAndOrder | undefined>({
		get: () => {
			const list = props.schuelerListeManager().orderGet();
			if (list.isEmpty())
				return undefined;
			else {
				const { a: key, b: order} = list.get(0);
				return {key: key === 'klassen' ? 'idKlasse' : key, order};
			}
		},
		set: (value) => {
			if ((value === undefined) || (value.key === null))
				return;
			const key = value.key === 'idKlasse' ? 'klassen' : value.key;
			props.schuelerListeManager().orderUpdate(key, value.order);
			void props.setFilter();
		}
	})

	const cols = [
		{ key: "idKlasse", label: "Klasse", sortable: true, span: 1 },
		{ key: "nachname", label: "Nachname", sortable: true, span: 2 },
		{ key: "vorname", label: "Vorname", sortable: true, span: 2 },
	]

	watch(()=>props.schuelerListeManager().filtered(), async (neu)=> {
		if (props.auswahl && neu.contains(props.auswahl) === false)
			await props.gotoSchueler(neu.get(0));
	})

	const rowsFiltered = computed<SchuelerListeEintrag[]>(() => {
		const arr = [];
		for (const e of props.schuelerListeManager().filtered())
			if (e.nachname.toLocaleLowerCase().includes(search.value.toLocaleLowerCase()) ||
				e.vorname.toLocaleLowerCase().includes(search.value.toLocaleLowerCase()))
				arr.push(e);
		return arr;
	});

	const filterStatus = computed<SchuelerStatus[]>({
		get: () => [...props.schuelerListeManager().schuelerstatus.auswahl()],
		set: (value) => {
			props.schuelerListeManager().schuelerstatus.auswahlClear();
			for (const v of value)
				props.schuelerListeManager().schuelerstatus.auswahlAdd(v);
			void props.setFilter();
		}
	});

	const filterSchulgliederung = computed<Schulgliederung[]>({
		get: () => [...props.schuelerListeManager().schulgliederungen.auswahl()],
		set: (value) => {
			props.schuelerListeManager().schulgliederungen.auswahlClear();
			for (const v of value)
				props.schuelerListeManager().schulgliederungen.auswahlAdd(v);
			void props.setFilter();
		}
	});

	const filterJahrgaenge = computed<JahrgangsListeEintrag[]>({
		get: () => [...props.schuelerListeManager().jahrgaenge.auswahl()],
		set: (value) => {
			props.schuelerListeManager().jahrgaenge.auswahlClear();
			for (const v of value)
				props.schuelerListeManager().jahrgaenge.auswahlAdd(v);
			void props.setFilter();
		}
	});

	const filterKlassen = computed<KlassenListeEintrag[]>({
		get: () => [...props.schuelerListeManager().klassen.auswahl()],
		set: (value) => {
			props.schuelerListeManager().klassen.auswahlClear();
			for (const v of value)
				props.schuelerListeManager().klassen.auswahlAdd(v);
			void props.setFilter();
		}
	});

	const filterKurse = computed<KursListeEintrag[]>({
		get: () => [...props.schuelerListeManager().kurse.auswahl()],
		set: (value) => {
			props.schuelerListeManager().kurse.auswahlClear();
			for (const v of value)
				props.schuelerListeManager().kurse.auswahlAdd(v);
			void props.setFilter();
		}
	});

	async function filterReset() {
		props.schuelerListeManager().schulgliederungen.auswahlClear();
		props.schuelerListeManager().schuelerstatus.auswahlClear();
		props.schuelerListeManager().jahrgaenge.auswahlClear();
		props.schuelerListeManager().klassen.auswahlClear();
		props.schuelerListeManager().kurse.auswahlClear();
		await props.setFilter();
	}

	function text_status(status: SchuelerStatus): string {
		if (status instanceof Array) return "";
		return status.bezeichnung;
	}

	function text(klasse: KlassenListeEintrag|KursListeEintrag|JahrgangsListeEintrag): string {
		return klasse.kuerzel ?? "";
	}

	const find = (items: Iterable<KlassenListeEintrag|KursListeEintrag|JahrgangsListeEintrag>, search: string) => {
		const list = [];
		for (const i of items)
			if (i.kuerzel?.toLocaleLowerCase().includes(search.toLocaleLowerCase()))
				list.push(i);
		return list;
	}

	function text_schulgliederung(schulgliederung: Schulgliederung): string {
		return schulgliederung.daten.kuerzel;
	}

	const selectedItems = computed<SchuelerListeEintrag[]>({
		get: () => props.auswahlGruppe,
		set: (items) => props.setAuswahlGruppe(items)
	});

	function onAction(action: string, item: SchuelerListeEintrag) {
		switch(action) {
			case 'delete':
				deleteSchueler(item);
				break;
			case 'copy':
				copySchuelerEintrag(item);
				break;
		}
	}

	function copySchuelerEintrag(item: SchuelerListeEintrag) {
		// TODO: Funktion implementieren
		console.log('copy geklickt', item);
	}

	function deleteSchueler(item: SchuelerListeEintrag) {
		// TODO delete Schueler
		console.log("delete", item);
	}

	function addLine() {
		// TODO: Funktion implementieren
		console.log('addLine geklickt');
	}

</script>
