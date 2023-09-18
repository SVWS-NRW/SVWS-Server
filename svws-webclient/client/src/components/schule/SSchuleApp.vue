<template>
	<div class="app--content--placeholder flex flex-col items-center">
		<i-ri-school-line />
	</div>
</template>
<script setup lang="ts">
	import type { SchuleAppProps } from "./SSchuleAppProps";
	import type { ComputedRef} from "vue";
	import {computed} from "vue";

	const props = defineProps<SchuleAppProps>();

	const schulname: ComputedRef<string> = computed(() => {
		const name = props.schule?.bezeichnung1;
		return name ? name : "";
	});
</script>
