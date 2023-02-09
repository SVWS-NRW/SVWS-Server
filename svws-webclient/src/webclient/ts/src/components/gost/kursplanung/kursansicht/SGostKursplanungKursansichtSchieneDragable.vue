<template>
	<svws-ui-drop-data class="text-center"
		:class="{'bg-yellow-200': drag_data.schiene && drag_data.schiene?.id !== schiene.id }"
		tag="td"
		@drop="openModal">
		<svws-ui-drag-data :key="schiene.id"
			tag="div"
			:data="{schiene}"
			class="select-none cursor-grab"
			:draggable="true"
			@drag-start="drag_started"
			@drag-end="drag_data.schiene=undefined">
			<svws-ui-icon>
				<i-ri-lock-unlock-line class="inline-block" />
			</svws-ui-icon>
		</svws-ui-drag-data>
	</svws-ui-drop-data>
	<s-gost-kursplanung-kursansicht-modal-regel-schienen :add-regel="addRegel" v-model="isModalOpen_RegelSchienen" :von="von" :bis="bis" />
</template>

<script setup lang="ts">

	import { GostBlockungRegel, GostBlockungSchiene } from "@svws-nrw/svws-core-ts";
	import { computed, ref, Ref } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	const props = defineProps<{
		addRegel: (regel: GostBlockungRegel) => Promise<GostBlockungRegel | undefined>;
		schiene:  GostBlockungSchiene;
	}>();

	const main: Main = injectMainApp();

	const isModalOpen_RegelSchienen: Ref<boolean> = ref(false);

	const von: Ref<GostBlockungSchiene> = ref(props.schiene)
	const bis: Ref<GostBlockungSchiene> = ref(props.schiene)

	const drag_data = computed({
		get(): { schiene: GostBlockungSchiene | undefined; kurs?: undefined } {
			return main.config.drag_and_drop_data || {schiene: undefined} as { schiene: GostBlockungSchiene | undefined };
		},
		set(value: {schiene: GostBlockungSchiene|undefined; kurs?: undefined}) {
			main.config.drag_and_drop_data = value
		}
	});

	function openModal() {
		if (drag_data.value.kurs)
			return;
		if (drag_data.value?.schiene && props.schiene.id !== drag_data.value?.schiene.id) {
			von.value = drag_data.value.schiene;
			bis.value = props.schiene;
			isModalOpen_RegelSchienen.value = true;
			drag_data.value.schiene = undefined
		}
	}

	function drag_started(e: DragEvent) {
		const transfer = e.dataTransfer;
		const data = JSON.parse(transfer?.getData('text/plain') || "") as { schiene: GostBlockungSchiene | undefined };
		if (!data)
			return;
		drag_data.value = data;
	}

</script>
