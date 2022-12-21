<template>
	<div v-if="visible" class="app-container">
		<s-card-lehrer-basisdaten :stammdaten="stammdaten" />
		<s-card-lehrer-kontaktdaten :stammdaten="stammdaten" />
	</div>
</template>

<script setup lang="ts">

	import { LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { DataLehrerStammdaten } from "~/apps/lehrer/DataLehrerStammdaten";
	import { RouteLehrer, RouteDataLehrer } from "~/router/apps/RouteLehrer";
	import { routeAppData } from "~/router/RouteUtils";

	const props = defineProps<{ id?: number; item?: LehrerListeEintrag, routename: string }>();

	const data: RouteDataLehrer = routeAppData(RouteLehrer);

	const stammdaten: ComputedRef<DataLehrerStammdaten> = computed(() => {
		return data.stammdaten;
	});

	const visible: ComputedRef<boolean> = computed(() => {
		//return this.$app.lehrerStammdaten.visible; //TODO: richtige Bedingung einpflegen
		return true;
	});

</script>
