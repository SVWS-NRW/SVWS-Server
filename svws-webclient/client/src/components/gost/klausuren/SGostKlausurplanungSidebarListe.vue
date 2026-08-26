<template>
	<div class="flex flex-col gap-2 overflow-hidden">
		<div v-if="empty" class="leading-tight flex flex-col gap-0.5">
			<slot name="empty" />
		</div>
		<template v-else>
			<slot name="beforeList" />
			<ul class="flex flex-col gap-2 px-1 pb-2 overflow-y-auto">
				<slot />
			</ul>
		</template>
		<div v-if="$slots.actions" class="flex flex-wrap items-center justify-end gap-2 px-1 pt-1">
			<slot name="actions" />
		</div>
	</div>
</template>

<script setup lang="ts">
	defineProps<{
		empty: boolean;
	}>();
</script>
