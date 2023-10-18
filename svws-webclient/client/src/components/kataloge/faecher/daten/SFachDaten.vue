<template>
	<div class="page--content">
		<s-card-fach-basisdaten :data="data" :patch="patch" />
	</div>
</template>

<script setup lang="ts">

	import type { FachDatenProps } from "./SFachDatenProps";

	const props = defineProps<FachDatenProps>();

</script>
