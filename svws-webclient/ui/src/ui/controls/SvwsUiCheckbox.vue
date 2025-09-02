<template>
	<div class="inline-flex">
		<label class="svws-ui-checkbox" :class="{'svws-statistik': statistics, 'svws-headless': headless && type !== 'toggle', 'svws-bw': bw, 'svws-ui-toggle': type === 'toggle'}" :title :color>
			<input v-if="!readonly" type="checkbox" v-model="checked" :class="{'svws-headless': headless && type !== 'toggle', 'contentFocusField': focusClassContent}" :disabled :indeterminate :color ref="input">
			<span v-else :class="checked ? 'icon i-ri-checkbox-line' : 'icon i-ri-checkbox-blank-line'" />
			<span v-if="type === 'toggle'" class="svws-ui-toggle--icon" />
			<span v-if="$slots.default" class="svws-ui-checkbox--label">
				<span v-if="statistics" class="-mb-1 inline-block align-top">
					<svws-ui-tooltip position="right">
						<span v-if="statistics" class="cursor-pointer">
							<svws-ui-tooltip position="right">
								<span class="inline-flex items-center">
									<span class="icon i-ri-bar-chart-2-line svws-ui-checkbox--statistic-icon" />
								</span>
								<template #content>
									Relevant für die Statistik
								</template>
							</svws-ui-tooltip>
						</span>
						<template #content>Relevant für die Statistik</template>
					</svws-ui-tooltip>
				</span>
				<slot />
			</span>
		</label>
	</div>
</template>

<script lang="ts" setup>

	import { onMounted } from 'vue';
	import { computed, ref } from 'vue';

	const props = withDefaults(defineProps<{
		modelValue: boolean;
		statistics?: boolean;
		disabled?: boolean;
		bw?: boolean;
		title?: string;
		type?: 'checkbox' | 'toggle';
		headless?: boolean;
		indeterminate?: boolean;
		readonly?: boolean;
		color?: 'success' | 'error' | 'warning';
		autofocus?: boolean;
		focusClassContent?: boolean;
	}>(), {
		statistics: false,
		disabled: false,
		bw: false,
		title: undefined,
		type: 'checkbox',
		headless: false,
		indeterminate: false,
		readonly: false,
		color: undefined,
		autofocus: false,
		focusClassContent: false,
	});

	defineSlots();
	onMounted(() => doFocus())

	function doFocus() {
		if (props.autofocus)
			input.value?.focus();
	}

	const emit = defineEmits<{
		(e: 'update:modelValue', event: boolean): void;
	}>();

	const checked = computed<boolean>({
		get: () => props.modelValue,
		set: (value) =>	{
			if (props.readonly === false)
				emit("update:modelValue", value);
		},
	})

	const input = ref<null | HTMLInputElement>(null);
	const content = computed<boolean>(() => checked.value);

	defineExpose({ content, input });
</script>
