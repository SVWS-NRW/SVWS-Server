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
		<h5 class="headline-2 flex items-center space-x-2 text-black">
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
	@apply py-6 px-4;
	@apply space-x-2;
	@apply flex-shrink-0;
}
</style>
