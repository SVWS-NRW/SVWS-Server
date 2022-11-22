<template>
		<drop-data 
			class="border border-[#7f7f7f]/20 text-center"
			:class="{'bg-yellow-200': drag_data.schiene && drag_data.schiene?.id !== schiene.id }"
			tag="td"
			@drop="regel_schiene"
			>
			<drag-data
				:key="schiene.id"
				tag="div"
				:data="{schiene}"
				class="select-none cursor-grab"
				:draggable="true" 
				@drag-start="drag_started"
				@drag-end="drag_data.schiene=undefined"
			>
					<svws-ui-icon >
						<i-ri-lock-unlock-line class="inline-block"/>
					</svws-ui-icon>
			</drag-data>
	</drop-data>

	<svws-ui-modal ref="modal" size="small">
		<template #modalTitle>Regel erstellen</template>
		<template #modalDescription>
			<div class="flex gap-1 mb-2">
				<parameter-kursart v-model="kursart" class="mx-1" /> <div class="whitespace-nowrap pt-1">von Schiene {{von.nummer}} bis Schiene {{bis.nummer}}</div>
			</div>
			<div class="flex gap-1">
				<svws-ui-button @click="regel_hinzufuegen(GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS)">sperren</svws-ui-button>
				<svws-ui-button @click="regel_hinzufuegen(GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS)">alleine</svws-ui-button>
			</div>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
import {
	GostBlockungSchiene,
	GostKursart,
	GostKursblockungRegelTyp,
} from "@svws-nrw/svws-core-ts";
import { computed, ref, Ref, shallowRef } from "vue";

import { injectMainApp, Main } from "~/apps/Main";

const {schiene} = defineProps({
	schiene: {
		type: Object as () => GostBlockungSchiene,
		required: true
	}
});

const main: Main = injectMainApp();
const app = main.apps.gost;

const modal: Ref<any> = ref(null);
const kursart: Ref<GostKursart> = shallowRef(GostKursart.GK)
const von: Ref<GostBlockungSchiene> = ref(schiene)
const bis: Ref<GostBlockungSchiene> = ref(schiene)

const drag_data = computed({
	get(): {schiene: GostBlockungSchiene|undefined; kurs?: undefined} {
		return main.config.drag_and_drop_data || {schiene: undefined} as {schiene: GostBlockungSchiene|undefined};
	},
	set(value: {schiene: GostBlockungSchiene|undefined; kurs?: undefined}) {
		main.config.drag_and_drop_data = value
	}
});

const regel_hinzufuegen = async (regeltyp: GostKursblockungRegelTyp) => {
	toggle_modal()
	const regel = await app.dataKursblockung.add_blockung_regel(regeltyp.typ)
	if (!regel) return
	regel.parameter.set(0, kursart.value.id)
	regel.parameter.set(1, von.value.nummer)
	regel.parameter.set(2, bis.value.nummer)
	await app.dataKursblockung.patch_blockung_regel(regel)
}

function regel_schiene() {
	if (drag_data.value.kurs) return
	if (drag_data.value?.schiene && schiene.id !== drag_data.value?.schiene.id) {
		von.value = drag_data.value.schiene;
		bis.value = schiene;
		toggle_modal();
		drag_data.value.schiene = undefined
}}

function drag_started(e: DragEvent) {
	const transfer = e.dataTransfer;
	const data = JSON.parse(transfer?.getData('text/plain') || "") as {schiene: GostBlockungSchiene|undefined};
	if (!data) return;
	drag_data.value = data;
}

function toggle_modal() {
	modal.value.isOpen ? modal.value.closeModal() : modal.value.openModal();
};
</script>