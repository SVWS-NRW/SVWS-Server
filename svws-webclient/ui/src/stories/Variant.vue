<!-- eslint-disable vue/multi-word-component-names -->
<template>
	<div class="">
		<div class="border-l border-t border-r pr-3 pl-3 rounded-t-md text-lg w-fit" :class="active ? 'bg-ui-brand text-ui-onbrand' : 'bg-ui-brand-secondary text-ui-50'">{{ title }}</div>
		<div @click="storyManager.setVariant({id: uid})" class="border rounded-r-lg rounded-b-lg border-ui-50 p-4" :class="color?.label">
			<slot />
		</div>
	</div>
	<template v-if="active">
		<Teleport to="#controls" defer>
			<div class="text-2xl">Controls</div>
			<slot name="controls" />
		</Teleport>
	</template>
	<Teleport to="#source" v-if="(source.length > 0) && active">
		{{ source }}
	</Teleport>
</template>

<script setup lang="ts">

	import { computed, useId } from 'vue';
	import storyManager from './StoryManager';


	const props = withDefaults(defineProps<{
		title?: string;
		id?: string;
		layout?: {type?: 'grid'|'iframe'; width?: string};
		icon?: string;
		source?: string;
		responsiveDisabled?: boolean;
	}>(), {
		title: '',
		id: '',
		size: '',
		icon: '',
		source: '',
		layout: undefined,
		responsiveDisabled: false,
	});

	const uid = useId();

	const active = computed(() => storyManager.variant?.id === uid);

	const color = computed(() => storyManager.color);

</script>