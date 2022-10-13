<template>
	<component
		:is="tag"
		:draggable="draggable"
		@drag="drag"
		@dragstart="start"
		@dragenter="enter"
		@dragleave="leave"
		@dragend="end"
	>
		<slot></slot>
	</component>
</template>

<script setup lang="ts">
	import { ref } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();

	const props = defineProps({
		tag: { type: String, default: "div" },
		draggable: { type: Boolean, default: true },
		type: { type: String, default: "data" },
		data: { type: Object, required: true }
	});

	const emits = defineEmits([
		"dragStart",
		"dragDrag",
		"dragEnter",
		"dragLeave",
		"dragEnd"
	]);

	const dragging = ref(false);

	function start(e: DragEvent) {
		const transfer = e.dataTransfer;
		if (!transfer) return;
		transfer.setData(props.type, JSON.stringify(props.data));
		if (main) {
			main.config.drag_and_drop_data = props.data;
		}
		emits("dragStart", e);
	}

	function drag(e: DragEvent) {
		const transfer = e.dataTransfer;
		if (!transfer) return;
		emits("dragDrag", e);
	}

	function enter(e: DragEvent) {
		const transfer = e.dataTransfer;
		if (!transfer) return;
		emits("dragEnter", e);
	}

	function leave(e: DragEvent) {
		const transfer = e.dataTransfer;
		if (!transfer) return;
		emits("dragLeave", e);
	}

	function end(e: DragEvent) {
		const transfer = e.dataTransfer;
		if (!transfer) return;
		emits("dragEnd", e);
		if (main) {
			main.config.drag_and_drop_data = undefined;
		}
	}
</script>
