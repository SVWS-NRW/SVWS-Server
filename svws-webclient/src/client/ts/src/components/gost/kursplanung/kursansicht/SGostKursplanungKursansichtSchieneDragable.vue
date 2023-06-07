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
			@drag-start="drag_started"
			@drag-end="emit('dnd', undefined)">
			<span class="rounded w-3 absolute top-0 left-0">
				<i-ri-draggable class="w-5 -ml-1 text-black opacity-25 group-hover:opacity-100 group-hover:text-black" />
			</span>
			<i-ri-lock-unlock-line class="inline-block" />
		</svws-ui-drag-data>
	</svws-ui-drop-data>
	<s-gost-kursplanung-kursansicht-modal-regel-schienen :add-regel="addRegel" v-model="isModalOpen_RegelSchienen" :von="von" :bis="bis" />
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
	const isModalOpen_RegelSchienen: Ref<boolean> = ref(false);

	const von: Ref<GostBlockungSchiene> = ref(props.schiene)
	const bis: Ref<GostBlockungSchiene> = ref(props.schiene)

	const is_drop_zone: ComputedRef<boolean> = computed(() => {
		if (props.dragAndDropData === undefined)
			return false
		const { schiene } = props.dragAndDropData;
		if (schiene === undefined || schiene.id === props.schiene.id)
			return false;
		return true;
	});

	function openModal(data: { schiene: GostBlockungSchiene | undefined, kurs?: undefined }) {
		if (data === undefined || data.kurs !== undefined)
			return;
		if (data.schiene && props.schiene.id !== data.schiene.id) {
			von.value = data.schiene;
			bis.value = props.schiene;
			isModalOpen_RegelSchienen.value = true;
			data.schiene = undefined
		}
	}

	function drag_started(e: DragEvent) {
		const transfer = e.dataTransfer;
		const data = JSON.parse(transfer?.getData('text/plain') || "");
		if (data === undefined)
			return;
		emit("dnd", data);
	}

</script>
