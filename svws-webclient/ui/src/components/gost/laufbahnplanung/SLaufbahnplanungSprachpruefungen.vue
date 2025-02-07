<template>
	<svws-ui-table v-if="sprachendaten().pruefungen.size()" :items="sprachendaten().pruefungen" :columns>
		<template #cell(sprache)="{rowData: pruefung}">
			<svws-ui-tooltip>
				{{ pruefung.sprache }}
				<template #content>
					{{ Fach.data().getWertByKuerzel(pruefung.sprache)?.daten(schuljahr)?.text ?? '' }}
				</template>
			</svws-ui-tooltip>
		</template>
		<template #cell(typ)="{rowData: pruefung}">
			<svws-ui-tooltip>
				{{ pruefung.istHSUPruefung ? "HSU":'' }}{{ pruefung.istFeststellungspruefung ? 'SFP':'' }}
				<template #content>
					{{ pruefung.istHSUPruefung ? "Prüfung ist eine Prüfung im herkunftssprachlichen Unterricht":'' }}{{ pruefung.istFeststellungspruefung ? 'Prüfung ist eine Sprachfeststellungsprüfung':'' }}
				</template>
			</svws-ui-tooltip>
		</template>
		<template #cell(niveau)="{rowData: pruefung}">{{ Sprachpruefungniveau.getByID(pruefung.anspruchsniveauId || null)?.daten.beschreibung }}</template>
		<template #cell(ersetzt)="{rowData: pruefung}">{{ pruefung.istHSUPruefung?'–':'' }}{{ pruefung.kannErstePflichtfremdspracheErsetzen ? 'Erste Fremdsprache':'' }}{{ pruefung.kannZweitePflichtfremdspracheErsetzen ? 'Zweite Fremdsprache':'' }}{{ pruefung.kannWahlpflichtfremdspracheErsetzen ? 'Wahlpflichtfremdsprache':'' }}</template>
	</svws-ui-table>
</template>

<script setup lang="ts">
	import { Fach } from '../../../../../core/src/asd/types/fach/Fach';
	import type { Sprachendaten } from '../../../../../core/src/core/data/schueler/Sprachendaten';
	import { Sprachpruefungniveau } from '../../../../../core/src/core/types/fach/Sprachpruefungniveau';

	const props = defineProps<{
		schuljahr: number;
		sprachendaten: () => Sprachendaten;
	}>();

	const columns = [{key: 'sprache', label: 'Sprachprüfung'}, {key: 'typ', label: 'Typ'}, {key: 'anspruchsniveauId', label: "Niveau"}, {key: 'ersetzt', label: 'Ersetzt'}, {key: 'note', label: 'Note'}];

</script>