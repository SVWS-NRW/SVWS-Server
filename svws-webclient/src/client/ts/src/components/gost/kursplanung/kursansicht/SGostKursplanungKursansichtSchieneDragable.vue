<template>
	<svws-ui-drop-data class="data-table__th data-table__thead__th data-table__th__align-center text-black/25 hover:text-black relative group"
		:class="{
			'bg-primary/5 text-primary hover:text-primary': is_drop_zone,
		}"
		tag="div" :drop-allowed="is_drop_zone"
		@drop="openModal">
		<svws-ui-drag-data :key="schiene.id"
			tag="div"
			:data="{schiene}"
			class="select-none cursor-grab text-center"
			:draggable="true"
			@click="openModal"
			@drag-start="drag_started"
			@drag-end="emit('dnd', undefined)">
			<span class="rounded w-3 absolute top-0 left-0">
				<i-ri-draggable class="w-5 -ml-1 text-black opacity-25 group-hover:opacity-100 group-hover:text-black" />
			</span>
			<i-ri-lock-unlock-line class="inline-block" />
		</svws-ui-drag-data>
	</svws-ui-drop-data>
	<s-gost-kursplanung-kursansicht-modal-regel-schienen :add-regel="addRegel" :von="von" :bis="bis" ref="modal" />
</template>

<script setup lang="ts">

	import type { GostBlockungRegel, GostBlockungSchiene } from "@core";
	import type { ComputedRef, Ref } from "vue";
	import { computed, ref } from "vue";

	const props = defineProps<{
		addRegel: (regel: GostBlockungRegel) => Promise<GostBlockungRegel | undefined>;
		schiene:  GostBlockungSchiene;
		dragAndDropData?: { schiene: GostBlockungSchiene | undefined, kurs?: undefined };
	}>();

	const emit = defineEmits<{
		(e: 'dnd', data: { schiene: GostBlockungSchiene | undefined, kurs?: undefined } | undefined): void;
	}>()

	const von: Ref<GostBlockungSchiene> = ref(props.schiene);
	const bis: Ref<GostBlockungSchiene> = ref(props.schiene);
	const modal = ref();

	const is_drop_zone: ComputedRef<boolean> = computed(() => {
		if (props.dragAndDropData === undefined)
			return false
		const { schiene } = props.dragAndDropData;
		if (schiene === undefined || schiene.id === props.schiene.id)
			return false;
		return true;
	});

	function openModal(data: { schiene: GostBlockungSchiene | undefined, kurs?: undefined }) {
		von.value = data.schiene || props.schiene;
		bis.value = props.schiene;
		data.schiene = undefined;
		modal.value.openModal();
	}

	function drag_started(e: DragEvent) {
		const transfer = e.dataTransfer;
		const data = JSON.parse(transfer?.getData('text/plain') || "");
		if (data === undefined)
			return;
		emit("dnd", data);
	}

</script>
