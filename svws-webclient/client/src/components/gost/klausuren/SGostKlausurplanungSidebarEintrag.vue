<template>
	<li :data
		:draggable
		class="cursor-pointer"
		:aria-expanded="expandable ? expanded : undefined"
		@dragstart="emit('dragstart', $event)"
		@dragend="emit('dragend', $event)">
		<s-gost-klausurplanung-card class="p-2 gap-1"
			:class="{ 'opacity-40 ring-2 ring-ui-brand': dragging }"
			:interactive
			:selected
			:unstyled>
			<div class="flex items-start gap-2">
				<span v-if="draggable" class="icon i-ri-draggable mt-1 shrink-0 cursor-grab" />
				<svws-ui-checkbox v-if="selectable" headless class="mt-0.5 shrink-0" :disabled="selectDisabled" :model-value="checked" @update:model-value="emit('update:checked', $event)" @click.stop />
				<slot name="prefix" />
				<div class="min-w-0 grow">
					<svws-ui-tooltip v-if="hasKlausurLayout" class="w-full min-w-0" :disabled="!hasTooltip" :indicator="false" autosize>
						<div class="w-full min-w-0">
							<div v-if="showHeader" class="flex min-w-0 flex-wrap items-center gap-x-1 gap-y-0.5 leading-tight">
								<span v-if="title !== undefined" class="min-w-0 truncate font-bold">{{ title }}</span>
								<span v-if="showBadges" class="inline-flex min-w-0 flex-wrap items-center gap-1">
									<slot name="badge" />
								</span>
								<span v-if="$slots.titleMeta" class="min-w-0 truncate text-base opacity-70">
									<slot name="titleMeta" />
								</span>
							</div>
							<div v-if="showMeta" class="mt-0.5 flex min-w-0 items-center gap-x-3 text-base leading-tight *:whitespace-nowrap">
								<slot name="meta" />
							</div>
						</div>
						<template #content>
							<div class="min-w-52 text-left leading-tight">
								<slot name="tooltip" />
							</div>
						</template>
					</svws-ui-tooltip>
					<slot v-else />
				</div>
				<slot name="actions" />
			</div>
			<div v-if="expanded && $slots.expanded" class="mt-2 border-t border-ui-10 pt-2">
				<slot name="expanded" />
			</div>
		</s-gost-klausurplanung-card>
	</li>
</template>

<script setup lang="ts">
	import { computed, useSlots } from "vue";

	const props = withDefaults(defineProps<{
		data?: unknown;
		draggable?: boolean;
		selected?: boolean;
		interactive?: boolean;
		unstyled?: boolean;
		title?: string;
		selectable?: boolean;
		checked?: boolean;
		selectDisabled?: boolean;
		expandable?: boolean;
		expanded?: boolean;
		dragging?: boolean;
	}>(), {
		data: undefined,
		draggable: false,
		selected: false,
		interactive: true,
		unstyled: false,
		title: undefined,
		selectable: false,
		checked: false,
		selectDisabled: false,
		expandable: false,
		expanded: false,
		dragging: false,
	});

	const slots = useSlots();
	const hasTooltip = computed<boolean>(() => slots.tooltip !== undefined);
	const hasKlausurLayout = computed<boolean>(() => (slots.badge !== undefined) || (slots.meta !== undefined) || (slots.titleMeta !== undefined) || (slots.tooltip !== undefined));
	const showBadges = computed<boolean>(() => !props.expanded && (slots.badge !== undefined));
	const showHeader = computed<boolean>(() => !props.expanded || (props.title !== undefined) || (slots.titleMeta !== undefined));
	const showMeta = computed<boolean>(() => !props.expanded && (slots.meta !== undefined));

	const emit = defineEmits<{
		dragstart: [event: DragEvent];
		dragend: [event: DragEvent];
		"update:checked": [value: boolean];
	}>();
</script>
