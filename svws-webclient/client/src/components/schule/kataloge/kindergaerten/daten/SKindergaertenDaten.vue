<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				ToDo: Daten
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { KindergaertenDatenProps } from "~/components/schule/kataloge/kindergaerten/daten/SKindergaertenDatenProps";
	import { BenutzerKompetenz } from "@core";
	import { computed } from "vue";

	const props = defineProps<KindergaertenDatenProps>()
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const readonly = computed<boolean>(() => !hatKompetenzUpdate.value);

</script>
