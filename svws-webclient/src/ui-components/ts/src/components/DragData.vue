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
		<slot :dragging="dragging"></slot>
	</component>
</template>

<script setup lang="ts">
	import { ref } from "vue";

	const props = defineProps({
		tag: { type: String, default: "div" },
		draggable: { type: Boolean, default: true },
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
		transfer.dropEffect = "move";
		dragging.value = true;
		transfer.clearData();
		transfer.setData('text/plain', JSON.stringify(props.data));
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
		dragging.value = false
		emits("dragEnd", e);
	}
</script>
