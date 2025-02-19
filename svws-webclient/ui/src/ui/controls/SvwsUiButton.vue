<template>
	<button class="button" :class="{
		'button--primary': type === 'primary',
		'button--secondary': type === 'secondary',
		'button--danger': (type === 'error') || (type === 'danger'),
		'button--transparent': type === 'transparent',
		'button--icon': type === 'icon',
		'button--trash': type === 'trash',
		'button--small': size === 'small',
		'button--big': size === 'big',
		'filterFocusField': filterButton,
	}" :disabled ref="addButton">
		<slot v-if="type !== 'trash'" />
		<span v-if="type === 'trash'" class="button--trash-icon">
			<span class="inline-block icon i-ri-delete-bin-line" />
		</span>
		<span v-if="$slots.badge" class="button--badge">
			<slot name="badge" />
		</span>
	</button>
</template>

<script lang="ts" setup>

	import type { ButtonType } from '../../types';
	import { onMounted, ref } from "vue";

	const addButton = ref<HTMLButtonElement|null>(null);

	const props = withDefaults(defineProps<{
		type?: ButtonType;
		disabled?: boolean;
		size?: 'small' | 'normal' | 'big';
		autofocus?: boolean;
		filterButton?: boolean;
	}>(),{
		type: 'primary',
		disabled: false,
		size: 'normal',
		autofocus: false,
		filterButton: false,
	});

	defineSlots();
	onMounted(() => setAutofocus());

	function setAutofocus() {
		if(props.autofocus && (addButton.value !== null))
			addButton.value.focus();
	}

</script>
