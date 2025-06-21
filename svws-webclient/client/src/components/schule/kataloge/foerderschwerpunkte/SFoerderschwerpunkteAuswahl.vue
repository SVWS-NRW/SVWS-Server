<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1><span title="Förderschwerpunkte">Förderschwerpunkte</span></h1>
			<div><abschnitt-auswahl :daten="schuljahresabschnittsauswahl" /></div>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<div class="container">
				<svws-ui-table :clicked="auswahl" clickable @update:clicked="gotoEintrag" :items="mapKatalogeintraege.values()" :columns scroll-into-view :focus-switching-enabled :focus-help-visible />
				<!-- TODO: Beim Implementieren des '+'-Buttons zum Hinfügen eines Eintrags die property hasFocus auf die svws-ui-button-Komponente setzen. true, wenn Liste leer, sonst false (z.B. :hasFocus="rowsFiltered.length === 0") -->
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { useRegionSwitch } from "@ui";
	import type { FoerderschwerpunkteAuswahlProps } from "./SFoerderschwerpunkteAuswahlProps";

	const props = defineProps<FoerderschwerpunkteAuswahlProps>();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const columns = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: 'asc' },
		{ key: "text", label: "Bezeichnung", sortable: true }
	];

</script>
