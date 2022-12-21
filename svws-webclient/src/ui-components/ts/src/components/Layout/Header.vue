<script setup lang='ts'>
const {
	badge = '',
	badgeVariant = '',
	badgeSize = '',
} = defineProps<{
	badge?: string;
	badgeVariant?: string;
	badgeSize?: string;
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

<style>
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
