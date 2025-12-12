<template>
	<div class="page page-flex-col">
		<div class="overflow-y-scroll">
			<div class="min-w-fit flex flex-col gap-8">
				<notenmodul-konfiguration-sichtbarkeit :manager="managerSichtbareSpalten" />
				<notenmodul-konfiguration-sperrungen :manager="managerSperrungen" />
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { NotenmodulKonfigurationProps } from './NotenmodulKonfigurationProps';

	const props = defineProps<NotenmodulKonfigurationProps>();

</script>
