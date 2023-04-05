<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<span>Schüler</span>
		</template>
		<template #abschnitt>
			<abschnitt-auswahl :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="setAbschnitt" :akt-schulabschnitt="aktSchulabschnitt" />
		</template>
		<template #content>
			<svws-ui-data-table :clicked="auswahl" @update:clicked="gotoSchueler" :model-value="selectedItems" @update:model-value="setAuswahlGruppe" :items="rowsFiltered.values()"
				:columns="cols" clickable selectable count :filter-open="true" :filtered="filtered" :filterReset="filterReset">
				<template #search>
					<svws-ui-text-input v-model="search" type="search" placeholder="Suche nach Namen oder Klasse" />
				</template>
				<template #filter>
					<svws-ui-multi-select v-model="filterStatus" :items="SchuelerStatus.values()" :item-text="text_status" tags title="Status" class="col-span-full mb-3" />
					<svws-ui-multi-select v-model="filterKlassen" title="Klasse" :items="mapKlassenFiltered" :item-text="text_klasse" removable />
					<svws-ui-multi-select v-model="filterJahrgaenge" title="Jahrgang" :items="mapJahrgaenge" :item-text="text_jahrgang" removable />
					<svws-ui-multi-select v-model="filterKurse" title="Kurs" :items="mapKurse" :item-text="text_kurs" removable />
					<svws-ui-multi-select v-model="filterSchulgliederung" title="Schulgliederung" :items="schulgliederungen" :item-text="text_schulgliederung" removable />
					<!--					<svws-ui-button type="transparent" class="justify-center">
						<i-ri-filter-line />
						Erweiterte Filter
					</svws-ui-button>-->
				</template>
				<template #cell(idKlasse)="{ value }">
					{{ value === null ? "--" : mapKlassen.get(value)?.kuerzel }}
				</template>
				<template #footerActions>
					<button class="button button--icon" @click="addLine()">
						<svws-ui-icon><i-ri-add-line /></svws-ui-icon>
					</button>
					<button class="button button--icon">
						<svws-ui-icon><i-ri-file-copy-line /></svws-ui-icon>
					</button>
					<button class="button button--icon">
						<svws-ui-icon><i-ri-more-2-line /></svws-ui-icon>
					</button>
				</template>
			</svws-ui-data-table>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { computed, ComputedRef, Ref, ref, watch, WritableComputedRef } from "vue";
	import { SchuelerListeEintrag, SchuelerStatus, JahrgangsListeEintrag,
		KlassenListeEintrag, KursListeEintrag, Schulgliederung } from "@svws-nrw/svws-core";
	import { DataTableColumn } from "@ui";
	import { SchuelerAuswahlProps } from "./SSchuelerAuswahlProps";


	const props = defineProps<SchuelerAuswahlProps>();

	// TODO Speichere in einem speziellen Filter-Objekt
	const filtered: Ref<boolean> = ref(false);
	const search: Ref<string> = ref("");
	const cols: DataTableColumn[] = [
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

	const filterStatus: WritableComputedRef<Array<SchuelerStatus> | undefined> =
		computed({
			get(): Array<SchuelerStatus> | undefined {
				return props.filter.status;
			},
			set(value: Array<SchuelerStatus> | undefined) {
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

	function text_klasse(klasse: KlassenListeEintrag): string {
		return klasse.kuerzel ?? "";
	}

	function text_jahrgang(jahrgang: JahrgangsListeEintrag): string {
		return jahrgang.kuerzel ?? "";
	}

	function text_kurs(kurs: KursListeEintrag): string {
		return kurs.kuerzel;
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
