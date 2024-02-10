<template>
	<Teleport to=".router-tab-bar--subnav" v-if="isMounted">
		<s-gost-klausurplanung-quartal-auswahl :quartalsauswahl="quartalsauswahl" :halbjahr="halbjahr" />
	</Teleport>
	<div class="page--content page--content--full relative flex-col">
		<div class="text-headline">Nachschreibplan {{ jahrgangsdaten.jahrgang }}, {{ halbjahr.halbjahr }}. Halbjahr{{ quartalsauswahl.value === 0 ? "" : ", " + quartalsauswahl.value + ". Quartal" }}</div>
		<svws-ui-table v-model:sort-by-and-order="sortByAndOrder" :columns="cols" :items="itemsSorted">
			<template #noData>
				<slot name="noData">
					Keine Nachschreibklausuren geplant
				</slot>
			</template>

			<template #cell(nachname)="{ rowData }">
				{{ kMan().getSchuelerMap().get(props.kMan().schuelerklausurGetByIdOrException(rowData.idSchuelerklausur).idSchueler)?.nachname }}
			</template>
			<template #cell(vorname)="{ rowData }">
				{{ kMan().getSchuelerMap().get(props.kMan().schuelerklausurGetByIdOrException(rowData.idSchuelerklausur).idSchueler)?.vorname }}
			</template>
			<template #cell(kurs)="{ rowData }">
				<span class="svws-ui-badge" :style="`--background-color: ${ kMan().fachBgColorByKursklausur(kMan().kursklausurBySchuelerklausurTermin(rowData)) };`">{{ kMan().kursKurzbezeichnungByKursklausur(kMan().kursklausurBySchuelerklausurTermin(rowData)) }}</span>
			</template>
			<template #cell(kuerzel)="{ rowData }">
				{{ kMan().kursLehrerKuerzelByKursklausur(kMan().kursklausurBySchuelerklausurTermin(rowData)) }}
			</template>
			<template #cell(datum)="{ rowData }">
				{{ DateUtils.gibDatumGermanFormat(kMan().terminBySchuelerklausurTermin(rowData)!.datum!) }}
			</template>
			<template #cell(startzeit)="{ rowData }">
				{{ DateUtils.getStringOfUhrzeitFromMinuten(rowData.startzeit !== null ? rowData.startzeit : kMan().terminBySchuelerklausurTermin(rowData)!.startzeit!) }}
			</template>
			<template #cell(dauer)="{ rowData }">
				{{ kMan().vorgabeBySchuelerklausurTermin(rowData).dauer }}
			</template>
			<!--<template #cell(raum)>
				d
			</template>-->
		</svws-ui-table>
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
		const arr = props.kMan().schuelerklausurterminNtAktuellMitTerminUndDatumGetMengeByHalbjahrAndQuartal(props.halbjahr, props.quartalsauswahl.value).toArray() as GostSchuelerklausurTermin[];
		let temp = sortByAndOrder.value;
		if (temp === undefined || temp.order === null)
			temp = {key: 'nachname', order: true};
		arr.sort((a, b) => {
			switch (temp!.key) {
				case 'nachname':
					return props.kMan().getSchuelerMap().get(props.kMan().schuelerklausurGetByIdOrException(a.idSchuelerklausur).idSchueler)!.nachname.localeCompare(props.kMan().getSchuelerMap().get(props.kMan().schuelerklausurGetByIdOrException(b.idSchuelerklausur).idSchueler)!.nachname, "de-DE",);
				case 'vorname':
					return props.kMan().getSchuelerMap().get(props.kMan().schuelerklausurGetByIdOrException(a.idSchuelerklausur).idSchueler)!.vorname.localeCompare(props.kMan().getSchuelerMap().get(props.kMan().schuelerklausurGetByIdOrException(b.idSchuelerklausur).idSchueler)!.vorname, "de-DE",);
				case 'kurs':
					return props.kMan().kursKurzbezeichnungByKursklausur(props.kMan().kursklausurBySchuelerklausurTermin(a)).localeCompare(props.kMan().kursKurzbezeichnungByKursklausur(props.kMan().kursklausurBySchuelerklausurTermin(b)), "de-DE",);
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
			{ key: "datum", label: "Datum" },
			{ key: "startzeit", label: "Startzeit" },
			{ key: "dauer", label: "Dauer", tooltip: "Dauer in Minuten", span: 0.5, align: "right", minWidth: 3.25 },
			{ key: "raum", label: "Raum" },
		];
		return cols;
	}

	const cols = computed(() => calculateColumns());

</script>
