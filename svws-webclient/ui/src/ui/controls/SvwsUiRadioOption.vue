<template>
	<label :id="idComponent" class="radio--label" :class="{
		'radio--label--disabled': disabled,
		'radio--statistics': statistics,
		// 'radio--label--checked-': forceChecked || (modelValue === value) || checked,
		'radio--label--no-icon': !icon,
		'radio--label--no-text': !label,
		'radio--icon-type-view': iconType === 'view'
	}">
		<input :id="idInputField" v-model="checked" type="radio" :name="name" :value="value" :disabled="disabled" class="radio--indicator">
		<span :id="idIcon" v-if="icon" class="radio--indicator-icon">
			<template v-if="iconType === 'view'">
				<span class="icon i-ri-eye-line radio--indicator-icon--checked -my-0.5" />
			</template>
			<template v-else>
				<slot>
					<span class="icon i-ri-checkbox-blank-circle-line radio--indicator-icon--blank -my-0.5" />
					<span class="icon i-ri-checkbox-circle-line radio--indicator-icon--checked -my-0.5" />
				</slot>
			</template>
		</span>
		<span :id="idLabel" class="radio--label--text">
			<span v-if="statistics" class="cursor-pointer">
				<svws-ui-tooltip position="right">
					<span class="inline-flex items-center">
						<span class="icon i-ri-bar-chart-2-line radio--statistic-icon" />
					</span>
					<template #content>
						Relevant für die Statistik
					</template>
				</svws-ui-tooltip>
			</span>
			<span>{{ label }}</span>
		</span>
	</label>
</template>

<script setup lang='ts'>

	import { computed, useId } from 'vue';

	const props = withDefaults(defineProps<{
		name?: string;
		label?: string;
		value?: object | number | string | boolean;
		disabled?: boolean;
		statistics?: boolean;
		icon?: boolean;
		iconType?: string;
		modelValue?: object | number | string | boolean;
		forceChecked?: boolean;
	}>(), {
		name: '',
		label: '',
		value: '',
		disabled: false,
		statistics: false,
		icon: true,
		iconType: 'default',
		modelValue: '',
		forceChecked: false,
	});

	defineSlots();
	const emit = defineEmits<{
		(e: 'update:modelValue', value: object | number | string | boolean): void,
	}>();

	const checked = computed<object | number | boolean | string>({
		get: () => props.modelValue,
		set: (value) => emit('update:modelValue', value),
	})

	const idComponent = useId();
	const idInputField = useId();
	const idIcon = useId();
	const idLabel = useId();

</script>
