<template>
	<component :is="tag" :draggable="draggable" @drag="drag" @dragstart="start" @dragenter="enter" @dragleave="leave" @dragend="end" :class="{'cursor-grab': draggable, 'cursor-grabbing': dragging}">
		<slot :dragging="dragging" />
	</component>
</template>

<script setup lang="ts">
	import { ref } from "vue";
	/**
	 *
	 * @deprecated Die Drag&Drop-Komponenten sind *deprecated*
 	*/
	console.warn("Diese Komponente ist veraltet, bitte entsprechend der Doku auf eine andere Komponente umstellen. In einer der folgenden Versionen wird diese Komponente entfernt.");
	const props = withDefaults(defineProps<{
		tag?: string;
		draggable?: boolean;
		data: object;
	}>(), {
		tag: 'div',
		draggable: true,
	});

	const emit = defineEmits<{
		(e: 'dragStart', data: DragEvent): void;
		(e: 'dragDrag', data: DragEvent): void;
		(e: 'dragEnter', data: DragEvent): void;
		(e: 'dragLeave', data: DragEvent): void;
		(e: 'dragEnd', data: DragEvent): void;
	}>()

	const dragging = ref(false);

	function start(e: DragEvent) {
		const transfer = e.dataTransfer;
		if (!transfer)
			return;
		transfer.dropEffect = "move";
		dragging.value = true;
		transfer.clearData();
		transfer.setData('text/plain', JSON.stringify(props.data));
		emit("dragStart", e);
	}

	function drag(e: DragEvent) {
		const transfer = e.dataTransfer;
		if (!transfer)
			return;
		emit("dragDrag", e);
	}

	function enter(e: DragEvent) {
		const transfer = e.dataTransfer;
		if (!transfer)
			return;
		emit("dragEnter", e);
	}

	function leave(e: DragEvent) {
		const transfer = e.dataTransfer;
		if (!transfer)
			return;
		emit("dragLeave", e);
	}

	function end(e: DragEvent) {
		const transfer = e.dataTransfer;
		if (!transfer)
			return;
		dragging.value = false;
		emit("dragEnd", e);
	}

</script>
