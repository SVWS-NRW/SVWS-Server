<script setup lang="ts">
	import { ref, computed } from "vue";
	import SvwsUiDragData from "./DragData.vue";
	import SvwsUiDropData from "./DropData.vue";

	const items = ref([
		{
			id: 0,
			title: "Item A",
			list: 1
		},
		{
			id: 1,
			title: "Item B",
			list: 1
		},
		{
			id: 2,
			title: "Item C",
			list: 2
		}
	]);

	const listOne = computed(() => {
		return items.value.filter(item => item.list === 1);
	});

	const listTwo = computed(() => {
		return items.value.filter(item => item.list === 2);
	});

	function onDrop(evt, list) {
		const itemID = evt.id;
		items.value = items.value.map(item =>
			item.id !== itemID
				? item
				: {
						...item,
						list,
				  }
		);
	}
</script>

<template>
	<Story title="SVWS UI/Drag-n-Drop" auto-props-disabled>
		<div class="h-screen w-screen">
			<svws-ui-drop-data class="drop-zone" @drop="onDrop($event, 1)">
        <div>List 1</div>
				<svws-ui-drag-data
					class="drag-el"
					v-for="item in listOne"
					:key="item.title"
					:data="{ id: item.id }"
					draggable
				>
					{{ item.title }}
				</svws-ui-drag-data>
			</svws-ui-drop-data>
			<svws-ui-drop-data class="drop-zone" @drop="onDrop($event, 2)">
        <div>List 2</div>
				<svws-ui-drag-data
					class="drag-el"
					v-for="item in listTwo"
					:key="item.title"
					:data="{ id: item.id }"
					draggable
				>
					{{ item.title }}
				</svws-ui-drag-data>
			</svws-ui-drop-data>
		</div>
	</Story>
</template>

<style scoped>
	.drop-zone {
		background-color: #eee;
		margin-bottom: 10px;
		padding: 10px;
	}
	.drag-el {
		background-color: #fff;
		margin-bottom: 10px;
		padding: 5px;
	}
</style>
