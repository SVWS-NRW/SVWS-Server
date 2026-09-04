<template>
	<Teleport to=".router-tab-bar--subnav" v-if="isMounted">
		<s-gost-klausurplanung-quartal-auswahl />
	</Teleport>
	<div class="page page-flex-col">
		<svws-ui-content-card class="col-span-full" :title="`Nachschreibplan ${state.jahrgangsdaten.jahrgang}, ${state.halbjahr.halbjahr}. Halbjahr${state.quartal === 0 ? '' : ', ' + state.quartal + '. Quartal'}`">
			<svws-ui-table v-model:sort-by-and-order="sortByAndOrder" :columns="cols" :items="itemsSorted">
				<template #noData>
					<slot name="noData">
						Keine Nachschreibklausuren geplant
					</slot>
				</template>

				<template #cell(nachname)="{ rowData }">
					{{ presenter.schuelerklausurterminNachname(rowData) }}
				</template>
				<template #cell(vorname)="{ rowData }">
					{{ presenter.schuelerklausurterminVorname(rowData) }}
				</template>
				<template #cell(kurs)="{ rowData }">
					<s-gost-klausurplanung-kurs-badge :schuelerklausurtermin="rowData" :tooltip="false" />
				</template>
				<template #cell(kuerzel)="{ rowData }">
					{{ presenter.schuelerklausurterminLehrerKuerzel(rowData) }}
				</template>
				<template #cell(datum)="{ rowData }">
					{{ presenter.schuelerklausurterminDatumText(rowData) }}
				</template>
				<template #cell(startzeit)="{ rowData }">
					{{ presenter.startzeitBySchuelerklausurtermin(rowData) }}
				</template>
				<template #cell(dauer)="{ rowData }">
					{{ presenter.schuelerklausurterminDauerText(rowData) }}
				</template>
				<template #cell(raum)="{ rowData }">
					{{ presenter.schuelerklausurterminRaumText(rowData) }}
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">
	import { computed, onMounted, ref } from 'vue';
	import { useKlausurplanungPresenter } from "./SGostKlausurplanungPresenter";
	import type { GostSchuelerklausurtermin } from '@core/core/data/gost/klausuren/GostSchuelerklausurtermin';
	import { useGostKlausurplanungState } from '@ui/states/GostKlausurplanungState';
	import type { SortByAndOrder, DataTableColumn } from '@ui/types';

	const state = useGostKlausurplanungState();
	const presenter = useKlausurplanungPresenter(state);

	const isMounted = ref(false);

	onMounted(() => {
		isMounted.value = true;
	});

	const sortByAndOrder = ref<SortByAndOrder | undefined>();

	const itemsSorted = computed(() => {
		const arr = state.manager.schuelerklausurterminNtAktuellMitTerminUndDatumGetMengeByHalbjahrAndQuartal(state.jahrgangsdaten.abiturjahr, state.halbjahr, state.quartal).toArray() as GostSchuelerklausurtermin[];
		let temp = sortByAndOrder.value;
		if ((temp === undefined) || (temp.order === null)) {
			temp = { key: 'nachname', order: true };
		}
		arr.sort((a, b) => {
			switch (temp.key) {
				case 'nachname':
					return presenter.compareSchuelerklausurterminNachname(a, b);
				case 'vorname':
					return presenter.compareSchuelerklausurterminVorname(a, b);
				case 'kurs':
					return presenter.compareSchuelerklausurterminKurs(a, b);
				case 'datum':
					return presenter.compareSchuelerklausurterminDatum(a, b);
				default:
					return 0;
			}
		});
		return temp.order === true ? arr : arr.reverse();
	});

	function calculateColumns() {
		const cols: DataTableColumn[] = [
			{ key: "nachname", label: "Nachname", minWidth: 8.25, sortable: true },
			{ key: "vorname", label: "Vorname", minWidth: 8, sortable: true },
			{ key: "kurs", label: "Kurs", span: 1.25, sortable: true },
			{ key: "kuerzel", label: "Lehrkraft" },
			{ key: "datum", label: "Datum", sortable: true },
			{ key: "startzeit", label: "Startzeit" },
			{ key: "dauer", label: "Dauer", tooltip: "Dauer in Minuten", span: 0.5, minWidth: 3.25 },
			{ key: "raum", label: "Raum" },
		];
		return cols;
	}

	const cols = computed(() => calculateColumns());

</script>
