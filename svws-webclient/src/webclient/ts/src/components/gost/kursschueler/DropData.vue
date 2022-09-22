<template>
	<component
		:is="tag"
		@dragover="over"
		@dragleave.prevent="active = false"
		@drop.prevent="drop"
	>
		<slot :active="active"></slot>
	</component>
</template>

<script setup lang="ts">
	import { ref } from "vue";

	const props = defineProps({
		tag: { type: String, default: "div" },
		type: { type: String, default: "data" }
	});

	const emits = defineEmits(["dragOver", "drop"]);

	const active = ref(false);

	function over(e: DragEvent) {
		const transfer = e.dataTransfer;
		if (!transfer || !transfer.getData(props.type)) return;
		active.value = true;
		transfer.effectAllowed = "move";
		const data = JSON.parse(transfer.getData(props.type));
		emits("dragOver", e);
	}

	function drop(e: DragEvent) {
		const transfer = e.dataTransfer;
		if (!transfer || !transfer.getData(props.type)) return;
		active.value = false;
		const data = JSON.parse(transfer.getData(props.type));
		emits("drop", data);
		// TODO transfer.clearData(); required? Would throw a DOMException...
	}
</script>
