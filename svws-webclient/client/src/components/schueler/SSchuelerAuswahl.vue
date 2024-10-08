<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<span class="select-none">Schüler</span>
		</template>
		<template #abschnitt>
			<abschnitt-auswahl :daten="schuljahresabschnittsauswahl" />
		</template>
		<template #content>
			<svws-ui-table clickable :clicked="schuelerListeManager().hasDaten() ? schuelerListeManager().auswahl() : null" @update:clicked="gotoSchueler"
				:items="rowsFiltered" :model-value="selectedItems" @update:model-value="items => setAuswahl(items)"
				:columns="cols" selectable count :filter-open="true" :filtered="filterChanged()" :filterReset="filterReset" scroll-into-view scroll
				v-model:sort-by-and-order="sortByAndOrder" :sort-by-multi="sortByMulti" allow-arrow-key-selection>
				<template #search>
					<svws-ui-text-input v-model="search" type="search" placeholder="Suchen" removable />
				</template>
				<template #filterAdvanced>
					<svws-ui-multi-select v-if="schuelerListeManager().istSchuljahresabschnittAktuell()" v-model="filterStatus" title="Status"
						:items="schuelerListeManager().schuelerstatus.list()" :item-text="status => status.daten(schuljahr)?.text ?? '—'" class="col-span-full" />
					<div v-else class="col-span-full flex flex-wrap gap-x-5">
						<svws-ui-checkbox type="toggle" v-model="filterNurMitLernabschitt">nur mit Lernabschnitt</svws-ui-checkbox>
					</div>
					<svws-ui-multi-select v-model="filterKlassen" title="Klasse" :items="schuelerListeManager().klassen.list()" :item-text="klasse => klasse.kuerzel ?? ''" :item-filter="find" />
					<svws-ui-multi-select v-model="filterJahrgaenge" title="Jahrgang" :items="schuelerListeManager().jahrgaenge.list()" :item-text="jahrgang => jahrgang.kuerzel ?? ''" :item-filter="find" />
					<svws-ui-multi-select v-model="filterKurse" title="Kurs" :items="schuelerListeManager().kurse.list()" :item-text="textKurs" :item-filter="findKurs" />
					<svws-ui-multi-select v-model="filterSchulgliederung" title="Schulgliederung" :items="schuelerListeManager().schulgliederungen.list()" :item-text="text_schulgliederung" />
					<!--					<svws-ui-button type="transparent" class="justify-center">
						<span class="icon i-ri-filter-line" />
						Erweiterte Filter
					</svws-ui-button>-->
				</template>
				<template #cell(idKlasse)="{ rowData, value }">
					{{ value === null ? "–" : (schuelerListeManager().klasseGetOrNull(value)?.kuerzel) ?? "–" }}
					<svws-ui-tooltip v-if="!schuelerListeManager().schuelerIstImSchuljahresabschnitt(rowData.id)" autosize>
						<span class="icon icon-error i-ri-alert-line" />
						<template #content>
							Der Schüler befindet sich nicht in dem ausgewählten Schuljahrsabschnitt, sondern in {{ schuelerListeManager().schuelerSchuljahresabschnittAsString(rowData.id) }}
						</template>
					</svws-ui-tooltip>
				</template>
				<!-- <template #actions>
					<svws-ui-button v-if="selectedItems.length > 0" type="transparent" @click="showModalGruppenaktionen().value = true">
					<svws-ui-button type="icon" @click="addLine()">
						<span class="icon i-ri-add-line" />
					</svws-ui-button>
				</template> -->
			</svws-ui-table>
		</template>
	</svws-ui-secondary-menu>
	<svws-ui-modal :show="showModalGruppenaktionen" size="medium">
		<template #modalTitle>
			Aktionen für {{ selectedItems.length }} ausgewählte Schüler
		</template>

		<template #modalContent>
			<div class="opacity-50 mb-4">
				{{ [ ... selectedItems ].splice(0,10).map(schueler => schueler.vorname + ' ' + schueler.nachname).join(', ') }}
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

	import { computed, ref, shallowRef, watch } from "vue";
	import type { SchuelerListeEintrag, JahrgangsDaten, KlassenDaten, Schulgliederung, KursDaten } from "@core";
	import { SchuelerStatus } from "@core";
	import type { SortByAndOrder } from "@ui";
	import type { SchuelerAuswahlProps } from "./SSchuelerAuswahlProps";

	const props = defineProps<SchuelerAuswahlProps>();

	const _showModalGruppenaktionen = ref<boolean>(false);
	const showModalGruppenaktionen = () => _showModalGruppenaktionen;

	const schuljahr = computed<number>(() => props.schuljahresabschnittsauswahl().aktuell.schuljahr);

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

	watch(() => props.schuelerListeManager().filtered(), async (neu) => {
		if (props.schuelerListeManager().hasDaten() && neu.contains(props.schuelerListeManager().auswahl()) === false)
			await props.gotoSchueler(neu.isEmpty() ? null : neu.get(0));
	})

	const rowsFiltered = computed<SchuelerListeEintrag[]>(() => {
		const arr = [];
		const searchValueIsNumber = /^[0-9]+$/.test(search.value.trim());
		const searchValueLowerCase = search.value.toLocaleLowerCase();
		for (const e of props.schuelerListeManager().filtered())
			if ((searchValueIsNumber && e.id.toString().includes(search.value))
				|| (e.nachname.toLocaleLowerCase().includes(searchValueLowerCase) || e.vorname.toLocaleLowerCase().includes(searchValueLowerCase)))
				arr.push(e);
		return arr;
	});

	const filterNurMitLernabschitt = computed<boolean>({
		get: () => props.schuelerListeManager().filterNurMitLernabschitt(),
		set: (value) => {
			props.schuelerListeManager().setFilterNurMitLernabschitt(value);
			void props.setFilter();
		}
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

	const filterJahrgaenge = computed<JahrgangsDaten[]>({
		get: () => [...props.schuelerListeManager().jahrgaenge.auswahl()],
		set: (value) => {
			props.schuelerListeManager().jahrgaenge.auswahlClear();
			for (const v of value)
				props.schuelerListeManager().jahrgaenge.auswahlAdd(v);
			void props.setFilter();
		}
	});

	const filterKlassen = computed<KlassenDaten[]>({
		get: () => [...props.schuelerListeManager().klassen.auswahl()],
		set: (value) => {
			props.schuelerListeManager().klassen.auswahlClear();
			for (const v of value)
				props.schuelerListeManager().klassen.auswahlAdd(v);
			void props.setFilter();
		}
	});

	const filterKurse = computed<KursDaten[]>({
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
		props.schuelerListeManager().schuelerstatus.auswahlAdd(SchuelerStatus.AKTIV);
		props.schuelerListeManager().schuelerstatus.auswahlAdd(SchuelerStatus.EXTERN);
		props.schuelerListeManager().jahrgaenge.auswahlClear();
		props.schuelerListeManager().klassen.auswahlClear();
		props.schuelerListeManager().kurse.auswahlClear();
		await props.setFilter();
	}

	function filterChanged(): boolean {
		if (props.schuelerListeManager().schulgliederungen.auswahlExists()
			|| props.schuelerListeManager().jahrgaenge.auswahlExists()
			|| props.schuelerListeManager().klassen.auswahlExists()
			|| props.schuelerListeManager().kurse.auswahlExists())
			return true;
		return (!(props.schuelerListeManager().schuelerstatus.auswahlSize() === 2
			&& props.schuelerListeManager().schuelerstatus.auswahlHas(SchuelerStatus.AKTIV)
			&& props.schuelerListeManager().schuelerstatus.auswahlHas(SchuelerStatus.EXTERN)))
	}

	function textKurs(kurs: KursDaten): string {
		let jahrgaenge = "";
		let index = 0;
		for (const j of kurs.idJahrgaenge) {
			const jg = props.schuelerListeManager().jahrgaenge.get(j);
			if (jg === null)
				continue;
			jahrgaenge += jg.kuerzel;
			if (index < kurs.idJahrgaenge.size()-1)
				jahrgaenge += ', ';
			index++;
		}
		return `${kurs.kuerzel} (${jahrgaenge})`;
	}

	function find(items: Iterable<JahrgangsDaten | KlassenDaten>, search: string) {
		const list = [];
		for (const i of items)
			if ((i.kuerzel !== null) && i.kuerzel.toLocaleLowerCase().includes(search.toLocaleLowerCase()))
				list.push(i);
		return list;
	}

	function findKurs(items: Iterable<KursDaten>, search: string) {
		const list = [];
		for (const i of items)
			if (i.kuerzel.toLocaleLowerCase().includes(search.toLocaleLowerCase()))
				list.push(i);
		return list;
	}

	function text_schulgliederung(schulgliederung: Schulgliederung): string {
		return schulgliederung.daten(schuljahr.value)?.kuerzel ?? '—';
	}

	const selectedItems = shallowRef<SchuelerListeEintrag[]>([]);

	function setAuswahl(items : SchuelerListeEintrag[]) {
		const schuelerauswahl = props.schuelerListeManager().liste;
		for (const vorhanden of [ ... schuelerauswahl.auswahl() ])
			if (!items.includes(vorhanden))
				schuelerauswahl.auswahlRemove(vorhanden);
		for (const item of items)
			schuelerauswahl.auswahlAdd(item);
		selectedItems.value = [ ... schuelerauswahl.auswahl() ];
	}

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
