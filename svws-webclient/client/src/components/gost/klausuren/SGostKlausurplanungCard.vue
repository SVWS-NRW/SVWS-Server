<template>
	<div class="flex flex-col"
		:class="{
			'bg-ui-100 shadow-[2px_3px_6px_rgb(0_0_0_/_14%)] border border-[rgb(0_0_0_/_18%)] hover:border-[rgb(0_0_0_/_28%)] rounded-lg': !unstyled,
			'cursor-pointer': interactive && !unstyled,
			'border-ui-brand bg-ui-brand/5 ring-2 ring-ui-brand/30 shadow-[2px_4px_10px_rgb(0_0_0_/_20%)]': selected && !unstyled,
			'border-dashed border-ui-brand ring-4 ring-ui-brand/25': (dropState === 'valid') && !unstyled,
			'bg-ui-danger/5 ring-4 ring-ui-danger/10': (dropState === 'danger') && !unstyled,
			'bg-ui-danger/5 ring-2 ring-ui-danger/10': (dropState === 'disabled') && !unstyled,
			'opacity-25 border-transparent shadow-none': muted && !unstyled,
			'bg-ui-warning-weak': warning,
			'border-ui-danger': danger && !unstyled,
			'border-l-brand border-l-2': accent && !unstyled,
		}"
		@dragover="onDragover"
		@drop="onDrop">
		<slot />
	</div>
</template>

<script setup lang="ts">
	import type { KlausurplanungDropState } from "./SGostKlausurplanungDragUtils";

	const props = withDefaults(defineProps<{
		selected?: boolean;
		interactive?: boolean;
		dropState?: KlausurplanungDropState;
		dropAllowed?: boolean;
		warning?: boolean;
		danger?: boolean;
		muted?: boolean;
		accent?: boolean;
		unstyled?: boolean;
	}>(), {
		selected: false,
		interactive: false,
		dropState: "none",
		dropAllowed: false,
		warning: false,
		danger: false,
		muted: false,
		accent: false,
		unstyled: false,
	});

	const emit = defineEmits<{
		drop: [event: DragEvent];
	}>();

	function onDragover(event: DragEvent): void {
		if (!props.dropAllowed) {
			return;
		}
		event.preventDefault();
	}

	function onDrop(event: DragEvent): void {
		if (!props.dropAllowed) {
			return;
		}
		emit("drop", event);
	}
</script>
