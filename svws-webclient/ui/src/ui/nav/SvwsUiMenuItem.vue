<template>
	<a class="sidebar--menu-item" :class="{
		'sidebar--menu-item--active': active,
		'sidebar--menu-item--collapsed': collapsed,
		'sidebar--menu-item--disabled': disabled,
		'sidebar--menu-item--statistik': hatlabel === 'Statistik' || statistik,
		'menuFocusField': active && secondary,
		'navigationFocusField': active && !secondary,
	}" href="#" @click.prevent="onClick"
		:title="disabled ? 'Nicht verfügbar' : hatlabel" ref="menuLink">
		<span v-if="$slots.icon" class="sidebar--menu-item--icon">
			<slot name="icon" />
		</span>
		<span class="sidebar--menu-item--label">
			<slot name="label" />
			<span v-if="subline" class="sidebar--menu-item--subline">
				{{ subline }}
			</span>
		</span>
	</a>
</template>

<script setup lang='ts'>

	import type { SetupContext } from 'vue';
	import { computed, useSlots, onMounted, ref, watch } from 'vue';

	const menuLink = ref<HTMLElement>();

	const props = withDefaults(defineProps<{
		active?: boolean;
		collapsed?: boolean;
		disabled?: boolean;
		subline?: string;
		statistik?: boolean;
		focus?: boolean;
		secondary?: boolean;
	}>(), {
		active: false,
		collapsed: false,
		disabled: false,
		subline: '',
		statistik: false,
		focus: false,
		secondary: false,
	});

	defineSlots();
	const emit = defineEmits<{
		(e: 'click', event: MouseEvent): void;
	}>();

	onMounted(() => doFocus());

	watch(() => props.focus, () => doFocus());

	function doFocus() {
		if (props.focus && menuLink.value)
			menuLink.value.focus();
	}

	function onClick(event: MouseEvent) {
		emit("click", event);
	}

	const slots: SetupContext['slots'] = useSlots()

	const hatlabel = computed(() => {
		const label = slots.label?.()
		if ((label !== undefined) && (label.length > 0) && (typeof label[0].children === 'string'))
			return label[0].children;
		return "";
	});

</script>
