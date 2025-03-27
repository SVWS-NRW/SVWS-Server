<template>
	<div class="flex flex-col gap-4 mb-1">
		<div v-if="title.length" class="text-headline-md inline-flex gap-1 opacity-50">
			<span class="icon i-ri-box-3-line opacity-50" v-if="!hideIcon" />
			<span class="font-normal">{{ title }}</span>
		</div>
		<div class="leading-tight max-w-sm opacity-50 inline-flex gap-0.5">
			<span class="icon i-ri-box-3-line opacity-75 shrink-0" v-if="!title.length && !hideIcon" />
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
