<template>
	<div class="svws-ui-zeitraster--stunde">
		<div class="font-bold text-headline-md">{{ stunde.unterrichtstunde }}</div>
		<slot />
	</div>
</template>

<script setup lang="ts">
	import { type StundenplanZeitraster } from '@core';

	const props = defineProps<{
		stunde: StundenplanZeitraster;
	}>()

</script>

<style lang="postcss">
.svws-ui-zeitraster--stunde {
  @apply flex flex-col gap-1 h-16 justify-center items-center p-2 border border-black/25 rounded;
}
</style>
