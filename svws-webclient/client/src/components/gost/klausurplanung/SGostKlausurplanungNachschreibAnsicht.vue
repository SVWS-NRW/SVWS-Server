<template>
	<Teleport to=".router-tab-bar--subnav" v-if="isMounted">
		<s-gost-klausurplanung-quartal-auswahl :quartalsauswahl="quartalsauswahl" :halbjahr="halbjahr" />
	</Teleport>
	<div class="page page-flex-col">
		<svws-ui-content-card class="col-span-full" :title="`Nachschreibplan ${jahrgangsdaten.jahrgang}, ${halbjahr.halbjahr}. Halbjahr${quartalsauswahl.value === 0 ? '' : ', ' + quartalsauswahl.value + '. Quartal'}`">
			<svws-ui-table v-model:sort-by-and-order="sortByAndOrder" :columns="cols" :items="itemsSorted">
				<template #noData>
					<slot name="noData">
						Keine Nachschreibklausuren geplant
					</slot>
				</template>

				<template #cell(nachname)="{ rowData }">
					{{ kMan().schuelerGetByIdOrException(props.kMan().schuelerklausurGetByIdOrException(rowData.idSchuelerklausur).idSchueler)?.nachname }}
				</template>
				<template #cell(vorname)="{ rowData }">
					{{ kMan().schuelerGetByIdOrException(props.kMan().schuelerklausurGetByIdOrException(rowData.idSchuelerklausur).idSchueler)?.vorname }}
				</template>
				<template #cell(kurs)="{ rowData }">
					<span class="svws-ui-badge" :style="`color: var(--color-text-uistatic); background-color: ${ kMan().fachHTMLFarbeRgbaByKursklausur(kMan().kursklausurBySchuelerklausurTermin(rowData)) };`">{{ kMan().kursKurzbezeichnungByKursklausur(kMan().kursklausurBySchuelerklausurTermin(rowData)) }}</span>
				</template>
				<template #cell(kuerzel)="{ rowData }">
					{{ kMan().kursLehrerKuerzelByKursklausur(kMan().kursklausurBySchuelerklausurTermin(rowData)) }}
				</template>
				<template #cell(datum)="{ rowData }">
					{{ DateUtils.gibDatumGermanFormat(kMan().terminOrExceptionBySchuelerklausurTermin(rowData).datum!) }}
				</template>
				<template #cell(startzeit)="{ rowData }">
					{{ DateUtils.getStringOfUhrzeitFromMinuten(rowData.startzeit !== null ? rowData.startzeit : kMan().terminOrExceptionBySchuelerklausurTermin(rowData).startzeit!) }}
				</template>
				<template #cell(dauer)="{ rowData }">
					{{ kMan().vorgabeBySchuelerklausurTermin(rowData).dauer }}
				</template>
				<template #cell(raum)="{ rowData }">
					{{ kMan().stundenplanraumGetBySchuelerklausurtermin(rowData)?.kuerzel ?? "-" }}
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">
	import { computed, onMounted, ref } from 'vue';
	import type { GostKlausurplanungNachschreibAnsichtProps } from './SGostKlausurplanungNachschreibAnsichtProps';
	import type { DataTableColumn, SortByAndOrder } from '@ui';
	import type { GostSchuelerklausurTermin } from '@core';
	import { DateUtils } from '@core';

	const props = defineProps<GostKlausurplanungNachschreibAnsichtProps>();

	const isMounted = ref(false);

	onMounted(() => {
		isMounted.value = true;
	});

	const sortByAndOrder = ref<SortByAndOrder | undefined>()

	const itemsSorted = computed(() => {
		const arr = props.kMan().schuelerklausurterminNtAktuellMitTerminUndDatumGetMengeByHalbjahrAndQuartal(props.jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value).toArray() as GostSchuelerklausurTermin[];
		let temp = sortByAndOrder.value;
		if (temp === undefined || temp.order === null)
			temp = {key: 'nachname', order: true};
		arr.sort((a, b) => {
			switch (temp.key) {
				case 'nachname':
					return props.kMan().schuelerGetByIdOrException(props.kMan().schuelerklausurGetByIdOrException(a.idSchuelerklausur).idSchueler).nachname.localeCompare(props.kMan().schuelerGetByIdOrException(props.kMan().schuelerklausurGetByIdOrException(b.idSchuelerklausur).idSchueler).nachname, "de-DE");
				case 'vorname':
					return props.kMan().schuelerGetByIdOrException(props.kMan().schuelerklausurGetByIdOrException(a.idSchuelerklausur).idSchueler).vorname.localeCompare(props.kMan().schuelerGetByIdOrException(props.kMan().schuelerklausurGetByIdOrException(b.idSchuelerklausur).idSchueler).vorname, "de-DE");
				case 'kurs':
					return props.kMan().kursKurzbezeichnungByKursklausur(props.kMan().kursklausurBySchuelerklausurTermin(a)).localeCompare(props.kMan().kursKurzbezeichnungByKursklausur(props.kMan().kursklausurBySchuelerklausurTermin(b)), "de-DE");
				case 'datum': {
					const ord = props.kMan().terminGetByIdOrException(a.idTermin!).datum!.localeCompare(props.kMan().terminGetByIdOrException(b.idTermin!).datum!, "de-DE");
					return ord !== 0 ? ord : props.kMan().kursKurzbezeichnungByKursklausur(props.kMan().kursklausurBySchuelerklausurTermin(a)).localeCompare(props.kMan().kursKurzbezeichnungByKursklausur(props.kMan().kursklausurBySchuelerklausurTermin(b)), "de-DE");
				}
				default:
					return 0;
			}
		})
		return temp.order === true ? arr : arr.reverse();
	})

	function calculateColumns() {
		const cols: DataTableColumn[] = [
			{ key: "nachname", label: "Nachame", minWidth: 8.25, sortable: true },
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
