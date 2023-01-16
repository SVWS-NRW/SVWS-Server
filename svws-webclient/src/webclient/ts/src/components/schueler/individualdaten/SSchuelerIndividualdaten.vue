<template>
	<div v-if="visible" class="app-container">
		<s-card-schueler-basisdaten :stammdaten="props.stammdaten" />
		<s-card-schueler-kontaktdaten :stammdaten="props.stammdaten" />
		<s-card-schueler-staat-religion :stammdaten="props.stammdaten" />
		<s-card-schueler-migrationshintergrund :stammdaten="props.stammdaten" />
		<s-card-schueler-foerderbedarf :stammdaten="props.stammdaten" :foerderschwerpunkte="props.foerderschwerpunkte" />
		<s-card-schueler-statusdaten :stammdaten="props.stammdaten" :fachschuelerarten="props.fachschuelerarten" />
		<s-card-schueler-bemerkungen :stammdaten="props.stammdaten" />
	</div>
</template>

<script setup lang="ts">

	import { SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ShallowRef } from "vue";
	import { DataKatalogFahrschuelerarten } from "~/apps/schueler/DataKatalogFahrschuelerarten";
	import { DataKatalogFoerderschwerpunkte } from "~/apps/schueler/DataKatalogFoerderschwerpunkte";
	import { DataSchuelerStammdaten } from "~/apps/schueler/DataSchuelerStammdaten";
	import { routeSchuelerIndividualdaten } from "~/router/apps/schueler/RouteSchuelerIndividualdaten";

	const props = defineProps<{
		item: ShallowRef<SchuelerListeEintrag | undefined>;
		stammdaten: DataSchuelerStammdaten;
		fachschuelerarten: DataKatalogFahrschuelerarten;
		foerderschwerpunkte: DataKatalogFoerderschwerpunkte;
	}>();

	const visible: ComputedRef<boolean> = computed(() => {
		return !(routeSchuelerIndividualdaten.hidden()) && (props.item.value?.id !== undefined);
	});

</script>
