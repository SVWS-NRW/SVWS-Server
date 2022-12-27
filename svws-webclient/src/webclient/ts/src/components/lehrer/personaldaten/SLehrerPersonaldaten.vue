<template>
	<div v-if="visible" class="app-container">
		<s-card-lehrer-personaldaten-allgemein :personaldaten="personaldaten" />
		<s-card-lehrer-personaldaten-lehraemter :personaldaten="personaldaten" />
		<s-card-lehrer-personaldaten-beschaeftigung :personaldaten="personaldaten" />
		<s-card-lehrer-personaldaten-anrechnung :personaldaten="personaldaten" />
	</div>
</template>

<script setup lang="ts">

	import { LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { DataLehrerPersonaldaten } from "~/apps/lehrer/DataLehrerPersonaldaten";
	import { routeLehrerPersonaldaten } from "~/router/apps/lehrer/RouteLehrerPersonaldaten";

	const props = defineProps<{ id?: number; item?: LehrerListeEintrag, routename: string }>();

	const personaldaten: ComputedRef<DataLehrerPersonaldaten> = computed(() => {
		return routeLehrerPersonaldaten.data.personaldaten;
	});

	const visible: ComputedRef<boolean> = computed(() => {
		if (personaldaten.value.daten === undefined)
			return false;
		//TODO: richtige Bedingung einpflegen
		return true;
	});

</script>
