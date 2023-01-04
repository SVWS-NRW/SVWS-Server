<script setup lang="ts">
	import { Size, Type } from "../../types";

	const {
		badge = "",
		badgeType = "",
		badgeSize = ""
	} = defineProps<{
		badge?: string;
		badgeType?: Type;
		badgeSize?: Size;
	}>();
</script>

<template>
	<div class="header--wrapper">
		<h2 class="text-headline">
			<slot />
		</h2>
		<Badge v-if="badge" :type="badgeType" :size="badgeSize">{{ badge }}</Badge>
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
