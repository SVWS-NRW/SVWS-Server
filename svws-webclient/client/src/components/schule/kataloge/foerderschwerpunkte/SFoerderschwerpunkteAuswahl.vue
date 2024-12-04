<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<span title="Förderschwerpunkte">Förderschwerpunkte</span>
		</template>
		<template #abschnitt>
			<abschnitt-auswahl :daten="schuljahresabschnittsauswahl" />
		</template>
		<template #header />
		<template #content>
			<div class="container">
				<svws-ui-table :clicked="auswahl" clickable @update:clicked="gotoEintrag" :items="mapKatalogeintraege.values()" :columns scroll-into-view :focus-switching-enabled :focus-help-visible />
				<!-- TODO: Beim Implementieren des '+'-Buttons zum Hinfügen eines Eintrags die property hasFocus auf die svws-ui-button-Komponente setzen. true, wenn Liste leer, sonst false (z.B. :hasFocus="rowsFiltered.length === 0") -->
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import type { FoerderschwerpunkteAuswahlProps } from "./SFoerderschwerpunkteAuswahlProps";
	import {useRegionSwitch} from "~/components/useRegionSwitch";

	const props = defineProps<FoerderschwerpunkteAuswahlProps>();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const columns = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: 'asc' },
		{ key: "text", label: "Bezeichnung", sortable: true }
	];
</script>
