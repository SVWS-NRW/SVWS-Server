<!-- eslint-disable vue/multi-word-component-names -->
<template>
	<slot />
	<Teleport to="#source" v-if="($slots.source || (source.length > 0)) && !storyManager.variant.hasSource" defer>
		<div class="flex items-center">
			<div class="text-2xl">Source</div>
			<svws-ui-button v-if="source !== ''" type="icon" @click="copyToClipboard(source)" class="mr-2">
				<span class="icon i-ri-file-copy-line" />
			</svws-ui-button>
		</div>
		<slot name="source">{{ source }}</slot>
	</Teleport>
	<Teleport to="#docs" v-if="$slots.docs" defer>
		<slot name="docs" />
	</Teleport>
	<Teleport to="#events" defer>
		<div class="text-2xl flex items-center align-middle gap-12">
			<span>Events</span>
			<SvwsUiButton type="trash" @click="storyManager.events = []" title="Entferne alle Events aus dem Log" />
		</div>
		<div v-for="l, i of storyManager.events" :key="i">{{ l }}</div>
	</Teleport>
	<template v-if="$slots.controls && !storyManager.variant.hasSlot('controls')">
		<Teleport to="#controls" defer>
			<div class="text-2xl">{{ storyManager.variant.title }}</div>
			<slot name="controls" />
		</Teleport>
	</template>
</template>

<script setup lang="ts">

	import { onBeforeMount, useId } from 'vue';
	import storyManager from './StoryManager';


	const props = withDefaults(defineProps<{
		title?: string;
		id?: string;
		layout?: { type?: 'grid' | 'iframe' | 'single'; width?: string, iframe?: boolean };
		icon?: string;
		source?: string;
		autoPropsDisabled?: boolean;
		group?: string;
		responsiveDisabled?: boolean;
	}>(), {
		title: '',
		id: () => useId(),
		size: '',
		icon: '',
		source: '',
		layout: undefined,
		autoPropsDisabled: false,
		group: '',
		responsiveDisabled: false,
	});

	onBeforeMount(() => storyManager.setStoryByID(props.id));

	document.addEventListener("log", (event) => {
		const ce = event as CustomEvent<string>;
		storyManager.events.push(ce.detail);
	});

	/**
	 * Kopiert den String in die Zwischenablage.
	 *
	 * @param string   zu kopierender String
	 */
	async function copyToClipboard(string: string | undefined) {
		if (string === undefined) {
			return;
		}
		await navigator.clipboard.writeText(string);
	}

</script>
