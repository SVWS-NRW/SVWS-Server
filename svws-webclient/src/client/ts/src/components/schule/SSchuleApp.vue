<template>
	<div class="app-layout--main--placeholder flex flex-col items-center">
		<i-ri-school-line />
		<span class="text-headline">{{ schulname }}</span>
	</div>
</template>
<script setup lang="ts">
	import type {SchuleStammdaten} from "@svws-nrw/svws-core";
	import type { ComputedRef} from "vue";
	import {computed} from "vue";

	const props = defineProps<{
		schule: SchuleStammdaten;
	}>();

	const schulname: ComputedRef<string> = computed(() => {
		const name = props.schule?.bezeichnung1;
		return name ? name : "";
	});
</script>
