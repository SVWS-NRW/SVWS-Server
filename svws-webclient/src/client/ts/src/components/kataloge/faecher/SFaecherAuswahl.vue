<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<nav class="secondary-menu--breadcrumbs">
				<a @click="returnToKataloge">Kataloge</a>
				<span>Fächer</span>
			</nav>
		</template>
		<template #abschnitt>
			<abschnitt-auswahl :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="setAbschnitt" :akt-schulabschnitt="aktSchulabschnitt" />
		</template>
		<template #header />
		<template #content>
			<svws-ui-table :clicked="auswahl" clickable @update:clicked="gotoEintrag" :items="mapKatalogeintraege.values()" :columns="cols">
				<!--<template #cell(bezeichnung)="{rowData}">
					<span class="svws-ui-badge" :style="`&#45;&#45;background-color: ${getBgColor(rowData.kuerzelStatistik || '')}`">{{ rowData.bezeichnung }}</span>
				</template>-->
			</svws-ui-table>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import type { FaecherAuswahlProps } from "./SFaecherAuswahlProps";
	import {ZulaessigesFach} from "@core";

	const props = defineProps<FaecherAuswahlProps>();

	const cols= [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: 'asc' },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, span: 3 }
	];

	const getBgColor = (auswahl: string) => ZulaessigesFach.getByKuerzelASD(auswahl).getHMTLFarbeRGBA(1.0);
</script>
