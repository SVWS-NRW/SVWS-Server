<template>
	<div v-if="visible" class="app-container">
		<s-card-schueler-basisdaten :stammdaten="props.stammdaten" />
		<s-card-schueler-kontaktdaten :stammdaten="props.stammdaten" />
		<s-card-schueler-staat-religion :stammdaten="props.stammdaten" />
		<s-card-schueler-migrationshintergrund :stammdaten="props.stammdaten" />
		<s-card-schueler-foerderbedarf :stammdaten="props.stammdaten" />
		<s-card-schueler-statusdaten :stammdaten="props.stammdaten" />
		<s-card-schueler-bemerkungen :stammdaten="props.stammdaten" />
	</div>
</template>

<script setup lang="ts">

	import { SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { DataSchuelerStammdaten } from "~/apps/schueler/DataSchuelerStammdaten";
	import { routeSchuelerIndividualdaten } from "~/router/apps/schueler/RouteSchuelerIndividualdaten";

	const props = defineProps<{ id?: number, item?: SchuelerListeEintrag, stammdaten: DataSchuelerStammdaten, routename: string }>();

	const visible: ComputedRef<boolean> = computed(() => {
		return !(routeSchuelerIndividualdaten.hidden) && (props.id !== undefined);
	});

</script>
