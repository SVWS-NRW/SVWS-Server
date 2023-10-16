<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<span>Schüler</span>
		</template>
		<template #abschnitt>
			<abschnitt-auswahl :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="setAbschnitt" :akt-schulabschnitt="aktSchulabschnitt" />
		</template>
		<template #content>
			<svws-ui-table :clicked="auswahl" @update:clicked="gotoSchueler" :model-value="selectedItems" @update:model-value="setAuswahlGruppe" :items="rowsFiltered.values()"
				:columns="cols" clickable selectable count :filter-open="true" :filtered="filtered" :filterReset="filterReset" scroll-into-view scroll>
				<template #search>
					<svws-ui-text-input v-model="search" type="search" placeholder="Suchen" />
				</template>
				<template #filterAdvanced>
					<svws-ui-multi-select v-model="filterStatus" :items="SchuelerStatus.values()" :item-text="text_status" title="Status" class="col-span-full" />
					<svws-ui-select v-model="filterKlassen" title="Klasse" :items="mapKlassenFiltered" :item-text="text" removable autocomplete :item-filter="find" />
					<svws-ui-select v-model="filterJahrgaenge" title="Jahrgang" :items="mapJahrgaenge" :item-text="text" removable autocomplete :item-filter="find" />
					<svws-ui-select v-model="filterKurse" title="Kurs" :items="mapKurse" :item-text="text" removable autocomplete :item-filter="find" />
					<svws-ui-select v-model="filterSchulgliederung" title="Schulgliederung" :items="schulgliederungen" :item-text="text_schulgliederung" removable />
					<!--					<svws-ui-button type="transparent" class="justify-center">
						<i-ri-filter-line />
						Erweiterte Filter
					</svws-ui-button>-->
				</template>
				<template #cell(idKlasse)="{ value }">
					{{ value === null ? "–" : mapKlassen.get(value)?.kuerzel }}
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

	import type { SchuelerListeEintrag, JahrgangsListeEintrag, KlassenListeEintrag, KursListeEintrag, Schulgliederung } from "@core";
	import type { ComputedRef, Ref, WritableComputedRef } from "vue";
	import type { SchuelerAuswahlProps } from "./SSchuelerAuswahlProps";
	import { computed, ref, watch } from "vue";
	import { SchuelerStatus } from "@core";

	const props = defineProps<SchuelerAuswahlProps>();

	const _showModalGruppenaktionen = ref<boolean>(false);
	const showModalGruppenaktionen = () => _showModalGruppenaktionen;

	// TODO Speichere in einem speziellen Filter-Objekt
	const filtered: Ref<boolean> = ref(false);
	const search: Ref<string> = ref("");
	const cols = [
		{ key: "idKlasse", label: "Klasse", sortable: true, span: 1 },
		{ key: "nachname", label: "Nachname", sortable: true, span: 2 },
		{ key: "vorname", label: "Vorname", sortable: true, span: 2 },
	]

	const rows = computed(() =>
		[...props.mapSchueler.values()]
			.filter(s => !props.filter.status.length || props.filter.status.map(s => s.id).includes(s.status))
			.filter(s => !props.filter.jahrgang || s.jahrgang === props.filter.jahrgang.kuerzel)
			.filter(s => !props.filter.klasse || s.idKlasse === props.filter.klasse.id)
			.filter(s => !props.filter.kurs || s.kurse?.toArray(new Array<number>()).includes(props.filter.kurs.id))
			.filter(s => !props.filter.schulgliederung || s.schulgliederung === props.filter.schulgliederung.daten.kuerzel)
	);

	watch(()=>rows.value, async (neu)=> {
		if (props.auswahl && neu.includes(props.auswahl) === false)
			await props.gotoSchueler(neu[0]);
	})

	const rowsFiltered = computed(() => rows.value.filter((e: any) =>
		e.nachname.toLocaleLowerCase().includes(search.value.toLocaleLowerCase()) ||
		e.vorname.toLocaleLowerCase().includes(search.value.toLocaleLowerCase())
	));

	const filterStatus: WritableComputedRef<Array<SchuelerStatus>> =
		computed({
			get(): Array<SchuelerStatus> {
				return props.filter.status;
			},
			set(value: Array<SchuelerStatus>) {
				const filter = props.filter;
				filter.status = value || [];
				props.setFilter(filter);
				filtered.value = true;
			}
		});

	const filterSchulgliederung: WritableComputedRef<Schulgliederung | undefined> = computed({
		get(): Schulgliederung | undefined {
			return props.filter.schulgliederung;
		},
		set(value: Schulgliederung | undefined) {
			const filter = props.filter;
			filter.schulgliederung = value;
			props.setFilter(filter);
			filtered.value = true;
		}
	});

	const filterJahrgaenge: WritableComputedRef<JahrgangsListeEintrag | undefined> = computed({
		get(): JahrgangsListeEintrag | undefined {
			return props.filter.jahrgang;
		},
		set(value: JahrgangsListeEintrag | undefined) {
			const filter = props.filter;
			filter.jahrgang = value;
			filter.klasse = undefined;
			filter.kurs = undefined;
			props.setFilter(filter);
			filtered.value = true;
		}
	});

	const mapKlassenFiltered: ComputedRef<Map<number, KlassenListeEintrag>> = computed(() => {
		const result: Map<number, KlassenListeEintrag> = new Map();
		const jahrgang = props.filter.jahrgang;
		for (const kl of props.mapKlassen.values())
			if ((jahrgang === undefined) || (kl.idJahrgang === jahrgang.id))
				result.set(kl.id, kl);
		return result;
	});

	const filterKlassen: WritableComputedRef<KlassenListeEintrag | undefined> = computed({
		get(): KlassenListeEintrag | undefined {
			return props.filter.klasse;
		},
		set(value: KlassenListeEintrag | undefined) {
			const filter = props.filter;
			filter.klasse = value;
			props.setFilter(filter);
			filtered.value = true;
		}
	});

	const filterKurse: WritableComputedRef<KursListeEintrag | undefined> = computed({
		get(): KursListeEintrag | undefined {
			return props.filter.kurs;
		},
		set(value: KursListeEintrag | undefined) {
			const filter = props.filter;
			filter.kurs = value;
			props.setFilter(filter);
			filtered.value = true;
		}
	});

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

	const selectedItems: WritableComputedRef<SchuelerListeEintrag[]> = computed({
		get: () => props.auswahlGruppe,
		set: (items: SchuelerListeEintrag[]) => props.setAuswahlGruppe(items)
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

	function filterReset() {
		search.value = "";
		const filter = props.filter;
		filter.jahrgang = undefined;
		filter.klasse = undefined;
		filter.kurs = undefined;
		filter.schulgliederung = undefined;
		filter.status = [ SchuelerStatus.AKTIV, SchuelerStatus.EXTERN ];
		props.setFilter(filter);
		filtered.value = false;
	}

</script>
