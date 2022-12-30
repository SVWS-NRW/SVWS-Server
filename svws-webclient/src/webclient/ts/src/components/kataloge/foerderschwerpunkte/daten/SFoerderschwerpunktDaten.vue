<template>
	<div v-if="visible" class="app-container">
		<s-card-foerderschwerpunkt-daten />
	</div>
</template>

<script setup lang="ts">

	import { FoerderschwerpunktEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";

	const props = defineProps<{ id?: number; item?: FoerderschwerpunktEintrag, routename: string }>();

	const visible: ComputedRef<boolean> = computed(() => {
		// TODO: richtige Bedingung einpflegen
		return true;
	});

</script>
