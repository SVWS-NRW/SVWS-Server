<template>
	<div v-if="visible" class="app-container">
		<s-card-schueler-basisdaten :stammdaten="stammdaten" />
		<s-card-schueler-kontaktdaten :stammdaten="stammdaten" :orte="orte" :ortsteile="ortsteile" />
		<s-card-schueler-staat-religion :stammdaten="stammdaten" />
		<s-card-schueler-migrationshintergrund :stammdaten="stammdaten" />
		<s-card-schueler-foerderbedarf :stammdaten="stammdaten" :foerderschwerpunkte="foerderschwerpunkte" />
		<s-card-schueler-statusdaten :stammdaten="stammdaten" :fachschuelerarten="fachschuelerarten" />
		<s-card-schueler-bemerkungen :stammdaten="stammdaten" />
	</div>
</template>

<script setup lang="ts">

	import { List, OrtKatalogEintrag, OrtsteilKatalogEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { DataKatalogFahrschuelerarten } from "~/apps/schueler/DataKatalogFahrschuelerarten";
	import { DataKatalogFoerderschwerpunkte } from "~/apps/schueler/DataKatalogFoerderschwerpunkte";
	import { DataSchuelerStammdaten } from "~/apps/schueler/DataSchuelerStammdaten";

	const props = defineProps<{
		stammdaten: DataSchuelerStammdaten;
		orte: List<OrtKatalogEintrag>;
		ortsteile: List<OrtsteilKatalogEintrag>;
		fachschuelerarten: DataKatalogFahrschuelerarten;
		foerderschwerpunkte: DataKatalogFoerderschwerpunkte;
	}>();

	const visible: ComputedRef<boolean> = computed(() => {
		return props.stammdaten.daten?.id !== undefined;
	});

</script>
