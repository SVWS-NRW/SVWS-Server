<script setup lang='ts'>
	import { computed } from 'vue';

	const props = withDefaults(defineProps<{
		name?: string;
		label?: string;
		value?: string;
		disabled?: boolean;
		statistics?: boolean;
		icon?: boolean;
		iconType?: string;
		modelValue?: string;
	}>(), {
		name: '',
		label: '',
		value: '',
		disabled: false,
		statistics: false,
		icon: true,
		iconType: 'default',
		modelValue: '',
	});

	const emit = defineEmits<{
		(e: 'update:modelValue', value: string): void,
	}>();

	const checked = computed({
		get() {
			return props.modelValue;
		},
		set(value: string) {
			emit('update:modelValue', value);
		}
	})
</script>

<template>
	<label class="radio--label" :class="{
		'radio--label--disabled': disabled,
		'radio--statistics': statistics,
		'radio--label--checked': modelValue === value,
		'radio--label--no-icon': !icon,
		'radio--icon-type-view': iconType === 'view'
	}">
		<input v-model="checked" type="radio" :name="name" :value="value" :disabled="disabled" class="radio--indicator">
		<span v-if="icon" class="radio--indicator-icon">
			<template v-if="iconType === 'view'">
				<i-ri-eye-line class="radio--indicator-icon--checked" />
			</template>
			<template v-else>
				<slot />
				<i-ri-checkbox-blank-circle-line v-if="!$slots.default" class="radio--indicator-icon--blank" />
				<i-ri-checkbox-circle-line v-if="!$slots.default" class="radio--indicator-icon--checked" />
			</template>
		</span>
		<span class="radio--label--text"> {{ label }} <i-ri-bar-chart-fill v-if="statistics" class="ml-2" /> </span>
	</label>
</template>

<style>
.radio--label {
	@apply cursor-pointer relative;
	@apply select-none;
	@apply text-button;
}

.radio--label--text {
	@apply flex items-center rounded-md;
	@apply space-x-2;
	min-height: 1.4em;
	padding: 0.45em 0.75em 0.45em 2em;

	.radio--icon-type-view:not(.radio--label--checked) & {
		padding-left: 0.75em;
	}

	.radio--row .radio--label--no-icon & {
		padding-left: 0.75em;
	}

	&:focus {
		@apply ring-primary ring-opacity-50;
	}
}

.radio--label:hover {
	.radio--label--text {
		@apply bg-transparent;
	}

	.radio--indicator ~ .radio--indicator-icon {
		@apply opacity-100 text-primary;
	}
}

.radio--indicator {
	@apply appearance-none absolute inset-0 w-full h-full pointer-events-none;

	&:focus {
		@apply ring-0;
	}
}

.radio--indicator:checked ~ .radio--label--text {
	@apply bg-primary bg-opacity-5 text-primary;
}

.radio--indicator ~ .radio--indicator-icon {
	@apply absolute inset-0 opacity-25 pointer-events-none;
	height: 1.2em;
	width: 1.2em;
	top: 0.35em;
	left: 0.5em;

	.router-tab-bar--subnav & {
		height: 1em;
		width: 1em;
		top: 0.4em;
	}
}

.radio--indicator:not(:checked) ~ .radio--indicator-icon .radio--indicator-icon--checked {
	@apply hidden;
}

.radio--indicator:checked ~ .radio--indicator-icon {
	@apply opacity-100 text-primary;
}

.radio--indicator:checked ~ .radio--indicator-icon .radio--indicator-icon--blank {
	@apply hidden;
}

.radio--label--disabled {
	&,
	&:hover,
	&:focus {
		@apply opacity-20;
		@apply cursor-not-allowed pointer-events-none;

		.radio--label--text {
			@apply bg-black bg-opacity-25 text-black;
			@apply bg-black bg-opacity-25 text-black;
		}
	}
}
</style>
