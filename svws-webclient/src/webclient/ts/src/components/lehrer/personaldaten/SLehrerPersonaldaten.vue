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
	import { computed, ComputedRef, ShallowRef } from "vue";
	import { DataLehrerPersonaldaten } from "~/apps/lehrer/DataLehrerPersonaldaten";
	import { DataLehrerStammdaten } from "~/apps/lehrer/DataLehrerStammdaten";
	import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";
	import { routeLehrerPersonaldaten } from "~/router/apps/lehrer/RouteLehrerPersonaldaten";

	const { item, personaldaten } = defineProps<{
		item: ShallowRef<LehrerListeEintrag | undefined>;
		stammdaten: DataLehrerStammdaten;
		personaldaten: DataLehrerPersonaldaten;
		schule: DataSchuleStammdaten;
	}>();

	const visible: ComputedRef<boolean> = computed(() => {
		return (item.value !== undefined) && (!routeLehrerPersonaldaten.hidden());
	});

</script>
