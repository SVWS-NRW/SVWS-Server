<template>
	<svws-ui-secondary-menu>
		<template #headline>Stundenplan</template>
		<template #abschnitt>
			<abschnitt-auswahl :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="setAbschnitt" :akt-schulabschnitt="aktSchulabschnitt" />
		</template>
		<template #content>
			<div class="flex flex-col gap-12">
				<svws-ui-data-table :clicked="auswahl" clickable @update:clicked="gotoEintrag" :items="mapKatalogeintraege.values()" :columns="cols" />
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import type { StundenplanAuswahlProps } from "./SStundenplanAuswahlProps";

	const props = defineProps<StundenplanAuswahlProps>();
	const cols = [
		{ key: "bezeichnung", label: "Bezeichnung", span: 2, sortable: false },
		{ key: "gueltigAb", label: "von", span: 1, sortable: false, defaultSort: 'asc' },
		{ key: "gueltigBis", label: "bis", span: 1, sortable: false }
	];

</script>
