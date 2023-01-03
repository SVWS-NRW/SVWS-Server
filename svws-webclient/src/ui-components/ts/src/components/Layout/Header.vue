<script setup lang='ts'>
import type { Variant, Size } from '../Typography/Badge.vue';
const {
	badge = '',
	badgeVariant = '',
	badgeSize = '',
} = defineProps<{
	badge?: string;
	badgeVariant?: Variant;
	badgeSize?: Size;
}>();
</script>

<template>
	<div class="header--wrapper">
		<h2 class="text-headline">
			<slot />
		</h2>
		<Badge v-if="badge" :variant="badgeVariant" :size="badgeSize">{{
		badge
			}}</Badge>
	</div>
</template>

<style lang="postcss">
.header--wrapper {
	@apply flex flex-row items-center justify-start;
	@apply py-2 px-6;
	@apply space-x-2;
	@apply flex-shrink-0;
}

.tab-container .header--wrapper {
	@apply px-0;
}
</style>
