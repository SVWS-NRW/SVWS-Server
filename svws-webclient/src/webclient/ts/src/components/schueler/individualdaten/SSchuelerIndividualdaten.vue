<template>
	<div v-if="visible" class="app-container">
		<s-card-schueler-basisdaten :stammdaten="stammdaten" />
		<s-card-schueler-kontaktdaten :stammdaten="stammdaten" />
		<s-card-schueler-staat-religion :stammdaten="stammdaten" />
		<s-card-schueler-migrationshintergrund :stammdaten="stammdaten" />
		<s-card-schueler-foerderbedarf :stammdaten="stammdaten" />
		<s-card-schueler-statusdaten :stammdaten="stammdaten" />
		<s-card-schueler-bemerkungen :stammdaten="stammdaten" />
	</div>
</template>

<script setup lang="ts">

	import { SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { DataSchuelerStammdaten } from "~/apps/schueler/DataSchuelerStammdaten";
	import { routeSchueler, RouteDataSchueler } from "~/router/apps/RouteSchueler";

	const props = defineProps<{ id?: number; item?: SchuelerListeEintrag, routename: string }>();

	const data: RouteDataSchueler = routeSchueler.data;

	const stammdaten: ComputedRef<DataSchuelerStammdaten> = computed(() => {
		return data.stammdaten;
	});

	const visible: ComputedRef<boolean> = computed(() => {
		// TODO: richtige Bedingung einpflegen
		return true;
	});

</script>
