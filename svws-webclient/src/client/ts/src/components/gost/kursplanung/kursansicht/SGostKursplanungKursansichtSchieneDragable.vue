<template>
	<svws-ui-drop-data class="text-center"
		tag="td"
		@drop="openModal">
		<svws-ui-drag-data :key="schiene.id"
			tag="div"
			:data="{schiene}"
			class="select-none cursor-grab"
			:draggable="true"
			@drag-start="drag_started"
			@drag-end="emit('dnd', undefined)">
			<svws-ui-icon	:class="{'bg-yellow-200': is_drop_zone }">
				<i-ri-lock-unlock-line class="inline-block" />
			</svws-ui-icon>
		</svws-ui-drag-data>
	</svws-ui-drop-data>
	<s-gost-kursplanung-kursansicht-modal-regel-schienen :add-regel="addRegel" v-model="isModalOpen_RegelSchienen" :von="von" :bis="bis" />
</template>

<script setup lang="ts">

	import { GostBlockungRegel, GostBlockungSchiene } from "@svws-nrw/svws-core";
	import { computed, ComputedRef, ref, Ref } from "vue";

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
