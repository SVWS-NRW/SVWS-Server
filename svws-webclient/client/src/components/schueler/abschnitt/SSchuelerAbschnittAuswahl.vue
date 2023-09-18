<template>
	<svws-ui-table :clicked="lernabschnitt" @update:clicked="gotoLernabschnitt" clickable :columns="[{key: 'schuljahresabschnitt', label: 'Abschnitt'}]" :items="lernabschnitte" type="navigation">
		<template #cell="{rowData: row}">
			<span>
				{{ row.schuljahr + "." + row.abschnitt }}
			</span>
			<svws-ui-tooltip v-if="row.wechselNr !== 0">
				<span class="opacity-50 inline-block cursor-pointer top-0.5 relative">
					(alt)
				</span>
				<template #content>
					Daten vor dem {{ row.wechselNr }}. Wechsel
				</template>
			</svws-ui-tooltip>
		</template>
	</svws-ui-table>
</template>

<script setup lang="ts">

	import type { SchuelerAbschnittAuswahlProps } from "./SSchuelerAbschnittAuswahlProps";

	const props = defineProps<SchuelerAbschnittAuswahlProps>();

</script>
