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
		<h5 class="text-headline flex items-center space-x-2">
			<slot />
		</h5>
		<Badge v-if="badge" :variant="badgeVariant" :size="badgeSize">{{
				badge
		}}</Badge>
	</div>
</template>

<style>
.header--wrapper {
	@apply flex flex-row items-center;
	@apply py-2 px-6;
	@apply space-x-2;
	@apply flex-shrink-0;
	min-height: 6.5rem;
}

.tab-container .header--wrapper {
	@apply px-0;
}
</style>
