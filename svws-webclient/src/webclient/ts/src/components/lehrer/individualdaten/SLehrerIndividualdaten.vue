<template>
	<div v-if="visible" class="app-container">
		<s-card-lehrer-basisdaten :stammdaten="stammdaten" />
		<s-card-lehrer-kontaktdaten :stammdaten="stammdaten" />
	</div>
</template>

<script setup lang="ts">

	import { LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ShallowRef } from "vue";
	import { DataLehrerStammdaten } from "~/apps/lehrer/DataLehrerStammdaten";
	import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";
	import { routeLehrerIndividualdaten } from "~/router/apps/lehrer/RouteLehrerIndividualdaten";

	const props = defineProps<{
		item: ShallowRef<LehrerListeEintrag | undefined>;
		stammdaten: DataLehrerStammdaten;
		schule: DataSchuleStammdaten;
	}>();

	const visible: ComputedRef<boolean> = computed(() => {
		return (props.item.value !== undefined) && (!routeLehrerIndividualdaten.hidden());
	});

</script>
