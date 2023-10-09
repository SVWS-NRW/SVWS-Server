<template>
	<div class="flex flex-col gap-4 mb-1">
		<div v-if="title.length" class="text-headline-md inline-flex gap-1 opacity-50">
			<i-ri-box-3-line v-if="!hideIcon" class="opacity-50" />
			<span class="font-normal">{{ title }}</span>
		</div>
		<div class="leading-tight max-w-sm opacity-50 inline-flex gap-0.5">
			<i-ri-box-3-line v-if="!title.length && !hideIcon" class="opacity-75 flex-shrink-0" />
			<div>
				<slot>
					Dieser Bereich ist noch in Entwicklung.
				</slot>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	const props = withDefaults(defineProps<{
		title?: string;
		hideIcon?: boolean;
	}>(), {
		title: '',
		hideIcon: false,
	})

</script>
