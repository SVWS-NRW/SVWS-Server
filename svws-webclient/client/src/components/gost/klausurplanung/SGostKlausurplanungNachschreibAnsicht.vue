<template>
	<Teleport to=".router-tab-bar--subnav" v-if="isMounted">
		<s-gost-klausurplanung-quartal-auswahl :quartalsauswahl="quartalsauswahl" :halbjahr="halbjahr" />
	</Teleport>
	<div class="page--content page--content--full relative flex-col">
		<div class="text-headline">Nachschreibplan {{ jahrgangsdaten.jahrgang }}, {{ halbjahr.halbjahr }}. Halbjahr{{ quartalsauswahl.value === 0 ? "" : ", " + quartalsauswahl.value + ". Quartal" }}</div>

		<svws-ui-table :columns="cols" :items="kMan().schuelerklausurterminNtAktuellMitTerminUndDatumGetMengeByHalbjahrAndQuartal(halbjahr, quartalsauswahl.value)">
			<template #noData>
				<slot name="noData">
					Keine Nachschreibklausuren geplant
				</slot>
			</template>

			<template #cell(nachname)="{ rowData }">
				{{ mapSchueler.get(props.kMan().schuelerklausurGetByIdOrException(rowData.idSchuelerklausur).idSchueler)?.nachname }}
			</template>
			<template #cell(vorname)="{ rowData }">
				{{ mapSchueler.get(props.kMan().schuelerklausurGetByIdOrException(rowData.idSchuelerklausur).idSchueler)?.vorname }}
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
			<template #cell(raum)="{ rowData }">
				d
			</template>
		</svws-ui-table>
	</div>
</template>

<script setup lang="ts">
	import { computed, onMounted, ref } from 'vue';
	import type { GostKlausurplanungNachschreibAnsichtProps } from './SGostKlausurplanungNachschreibAnsichtProps';
	import type { DataTableColumn } from '@ui';
	import { DateUtils } from '@core';

	const props = defineProps<GostKlausurplanungNachschreibAnsichtProps>();

	const isMounted = ref(false);

	onMounted(() => {
		isMounted.value = true;
	});

	function calculateColumns() {
		const cols: DataTableColumn[] = [
			{ key: "nachname", label: "Nachame", minWidth: 8.25 },
			{ key: "vorname", label: "Vorname", minWidth: 8 },
			{ key: "kurs", label: "Kurs", span: 1.25 },
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
