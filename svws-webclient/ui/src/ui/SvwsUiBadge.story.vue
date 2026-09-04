<template>
	<Story title="Badge" id="svws-ui-badge" :layout="{type: 'grid', width: '45%'}"
		icon="ri:shield-line" :source="sourceCode">
		<Variant v-for="[id, state] in variantControlsMap" :key="id" :title="id" :id>
			<div class="p-4">
				<svws-ui-badge :type="state.type"
					:size="state.size"
					:rounded="state.rounded"
					:short="state.short">
					Badge
				</svws-ui-badge>
			</div>
		</Variant>
		<template #controls>
			<div class="flex items-start gap-2 text-headline-sm mb-6">
				<HstRadio title="type" v-model="activeState.type" :options="[
					{ label: 'light', value: 'light' },
					{ label: 'primary', value: 'primary' },
					{ label: 'success', value: 'success' },
					{ label: 'error', value: 'error' },
					{ label: 'highlight', value: 'highlight' },
				]" />

				<svws-ui-tooltip position="top">
					<span class="icon i-ri-question-line" />
					<template #content>
						Der Typ des Badges. Bestimmt die Farbe bzw. Erscheinung des Badges.<br>
						<span class="font-bold">Default:</span> <code class="bg-ui-selected">type: "light"</code>
					</template>
				</svws-ui-tooltip>
			</div>
			<div class="flex items-start gap-2 text-headline-sm mb-6">
				<HstRadio title="size" v-model="activeState.size" :options="[
					{ label: 'small', value: 'small' },
					{ label: 'normal', value: 'normal' },
					{ label: 'medium', value: 'medium' },
					{ label: 'big', value: 'big' },
				]" />
				<svws-ui-tooltip position="top">
					<span class="icon i-ri-question-line" />
					<template #content>
						Die Größe des Badges.<br>
						<span class="font-bold">Default:</span> <code class="bg-ui-selected">size: "normal"</code>
					</template>
				</svws-ui-tooltip>
			</div>
			<div class="mb-6">
				<p class="text-headline-sm">rounded</p>
				<div class="flex items-start gap-2">
					<HstCheckbox title="rounded" v-model="activeState.rounded" />
					<svws-ui-tooltip position="top">
						<span class="icon i-ri-question-line" />
						<template #content>
							Gibt an, ob der Badge abgerundete Ecken hat.<br>
							<span class="font-bold">Default:</span> <code class="bg-ui-selected">rounded: false</code>
						</template>
					</svws-ui-tooltip>
				</div>
			</div>
			<div class="mb-6">
				<p class="text-headline-sm">short</p>
				<div class="flex items-start gap-2">
					<HstCheckbox title="short" v-model="activeState.short" />
					<svws-ui-tooltip position="top">
						<span class="icon i-ri-question-line" />
						<template #content>
							Kürzt den Badge auf eine feste Breite. Der vollständige Inhalt wird als Tooltip angezeigt.<br>
							<span class="font-bold">Default:</span> <code class="bg-ui-selected">short: false</code>
						</template>
					</svws-ui-tooltip>
				</div>
			</div>
		</template>
	</Story>
</template>

<script setup lang="ts">
	import { computed, reactive } from "vue";
	import storyManager from '../stories/StoryManager';

	type BadgeType = 'light' | 'primary' | 'success' | 'error' | 'highlight';
	type BadgeSize = 'small' | 'normal' | 'medium' | 'big';

	const variantControlsMap = new Map<string, { type: BadgeType; size: BadgeSize; rounded: boolean; short: boolean }>([
		['Primary', reactive({ type: 'primary', size: 'normal', rounded: false, short: false })],
		['Success', reactive({ type: 'success', size: 'normal', rounded: false, short: false })],
		['Error', reactive({ type: 'error', size: 'normal', rounded: false, short: false })],
		['Highlight', reactive({ type: 'highlight', size: 'normal', rounded: false, short: false })],
		['Light', reactive({ type: 'light', size: 'normal', rounded: false, short: false })],
	]);

	const activeState = computed(() => variantControlsMap.get(storyManager.variant.id) ?? variantControlsMap.get('Light')!);

	const sourceCode = computed(() => {
		const indent = "\t";
		const lines = [
			`type="${activeState.value.type}"`,
			activeState.value.size === 'normal' ? '' : `size="${activeState.value.size}"`,
			activeState.value.rounded ? `rounded` : '',
			activeState.value.short ? `short` : '',
		].filter(Boolean).map(l => indent + l).join("\n");
		return `<svws-ui-badge\n${lines}>Badge</svws-ui-badge>`;
	});
</script>
