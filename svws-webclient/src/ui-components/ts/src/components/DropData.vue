<template>
	<component :is="tag"
		@dragover.prevent="over"
		@dragenter.prevent="active = true"
		@dragleave.prevent="active = false"
		@drop.prevent="drop">
		<slot :active="active" />
	</component>
</template>

<script setup lang="ts">
	import { ref } from "vue";

	const { tag } = defineProps({
		tag: { type: String, default: "div" },
	});

	const emits = defineEmits(["dragOver", "drop"]);

	const active = ref(false);

	function over(e: DragEvent) {
		const transfer = e.dataTransfer;
		if (!transfer) return;
		active.value = true;
		transfer.effectAllowed = "move";
		emits("dragOver", e);
	}

	function drop(e: DragEvent) {
		const transfer = e.dataTransfer;
		if (!transfer || !transfer.getData('text/plain')) return;
		active.value = false;
		const data = JSON.parse(transfer.getData('text/plain'));
		emits("drop", data);
	}
</script>
