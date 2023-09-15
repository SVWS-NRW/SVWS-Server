<template>
	<div v-if="apiStatus.hasError || apiStatus.pending" class="flex gap-1 items-center mb-5 px-7 3xl:px-8" :class="{'animate-pulse': !apiStatus.hasError}">
		<template v-if="apiStatus.pending">
			<svws-ui-spinner spinning />
			<span class="text-button text-black/50 dark:text-white/50">Ergebnisse werden berechnet…</span>
		</template>
		<template v-if="apiStatus.hasError">
			<span class="text-error font-bold">Fehler beim Berechnen der Blockung.</span>
		</template>
	</div>
</template>

<script setup lang="ts">

	import type { GostBlockungListeneintrag } from "@core";
	import type { ApiStatus } from "~/components/ApiStatus";

	const props = defineProps<{
		blockung: GostBlockungListeneintrag;
		apiStatus: ApiStatus;
	}>();

</script>
