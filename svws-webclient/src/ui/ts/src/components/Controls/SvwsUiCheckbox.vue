<script lang="ts" setup>
	import { computed } from 'vue';

	type CheckboxValue = string | number | boolean | null;
	type ModelValue = boolean | Array<CheckboxValue> | undefined | 'indeterminate';

	const props = withDefaults(defineProps<{
		value?: CheckboxValue;
		modelValue: ModelValue;
		statistics?: boolean;
		disabled?: boolean;
		circle?: boolean;
		headless?: boolean;
		bw?: boolean;
	}>(), {
		value: '',
		statistics: false,
		disabled: false,
		circle: false,
		headless: false,
		bw: false
	});


	const value = computed({
		get: () => props.modelValue,
		set: (value) =>	emit("update:modelValue", value)
	})

	const emit = defineEmits<{
		(e: 'update:modelValue', event: ModelValue): void;
	}>();

</script>

<template>
	<label role="none" class="checkbox"
		:class="{
			'checkbox--disabled': disabled,
			'checkbox--statistics': statistics,
			'checkbox--checked': value,
			'checkbox--circle': circle,
			'checkbox--headless': headless,
			'checkbox--indeterminate': value === undefined || value === 'indeterminate',
			'checkbox--bw': bw,
		}">
		<input class="checkbox--control" type="checkbox" v-model="value" :value="value" :disabled="disabled" :title="disabled ? 'Deaktiviert' : ''">
		<svws-ui-icon v-if="value === 'indeterminate' && typeof value !== 'undefined'" role="checkbox">
			<i-ri-checkbox-indeterminate-line />
		</svws-ui-icon>
		<svws-ui-icon v-else-if="value" role="checkbox" :class="{'text-primary': !bw}">
			<i-ri-checkbox-fill v-if="!circle" />
			<i-ri-checkbox-circle-fill v-if="circle" />
		</svws-ui-icon>
		<svws-ui-icon v-else-if="!value" role="checkbox">
			<i-ri-checkbox-blank-line v-if="!circle" />
			<i-ri-checkbox-blank-circle-line v-if="circle" />
		</svws-ui-icon>
		<span class="checkbox--label" v-if="$slots.default || statistics">
			<slot />
			<svws-ui-icon v-if="statistics" class="ml-1">
				<i-ri-bar-chart-fill />
			</svws-ui-icon>
		</span>
	</label>
</template>

<style lang="postcss">
.checkbox {
	@apply cursor-pointer;
	@apply inline-flex;
	@apply items-start justify-start;
	@apply select-none;
	@apply text-base font-normal leading-none;
	@apply my-1;

	.icon svg {
		@apply -my-0.5;
		width: 1.4em;
		height: 1.4em;
	}

	&:hover,
	&:focus {
		.icon {
			@apply opacity-75;
		}
	}
}

.checkbox:not(.checkbox--checked):not(.checkbox--indeterminate) {
	.icon {
		@apply opacity-25;
	}

	&:hover,
	&:focus {
		.icon {
			@apply opacity-100;
		}
	}
}

.checkbox--control {
	@apply hidden;
}

.checkbox--disabled {
	@apply opacity-50;
	@apply cursor-not-allowed;
}

.checkbox--checked,
.checkbox--indeterminate {
	@apply font-medium;
}

.checkbox--label {
	margin: 0.1rem 0 0 0.2rem;
}

.checkbox--disabled .checkbox--label {
	@apply text-gray;
}
.checkbox--statistics {
	@apply text-purple-500;
}

.checkbox--statistics .checkbox--label .icon {
	@apply opacity-100 inline-block;
	width: 0.8em;
	height: 0.8em;

	svg {
		@apply w-full h-full;
	}
}

.checkbox--wrapper {
	margin-left: -1rem;
}

.checkbox--wrapper .checkbox {
	margin-left: 1rem;
}

.checkbox--headless {
	margin: 0;
}
</style>
