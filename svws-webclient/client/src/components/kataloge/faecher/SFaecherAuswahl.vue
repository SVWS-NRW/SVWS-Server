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
			<svws-ui-table :clicked="auswahl" clickable @update:clicked="gotoEintrag" :items="faecher" :columns="cols">
				<!--<template #cell(bezeichnung)="{rowData}">
					<span class="svws-ui-badge" :style="`&#45;&#45;background-color: ${getBgColor(rowData.kuerzelStatistik || '')}`">{{ rowData.bezeichnung }}</span>
				</template>-->
				<template #actions>
					<template v-if="schulform.daten.hatGymOb">
						<s-faecher-auswahl-sortierung-sek-i-i-modal v-slot="{ openModal }" :setze-default-sortierung-sek-i-i="setzeDefaultSortierungSekII">
							<svws-ui-button type="secondary" @click="openModal">Standardsortierung Sek II anwenden …</svws-ui-button>
						</s-faecher-auswahl-sortierung-sek-i-i-modal>
					</template>
				</template>
			</svws-ui-table>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { FaecherAuswahlProps } from "./SFaecherAuswahlProps";
	import type { FaecherListeEintrag } from "@core";

	const props = defineProps<FaecherAuswahlProps>();

	const cols= [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: 'asc' },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, span: 3 }
	];

	const faecher = computed<FaecherListeEintrag[]>(() => {
		const result : FaecherListeEintrag[] = [];
		for (const fach of props.mapKatalogeintraege().values()) {
			result.push(fach);
		}
		result.sort((a, b) => a.sortierung - b.sortierung);
		return result;
	});

</script>
