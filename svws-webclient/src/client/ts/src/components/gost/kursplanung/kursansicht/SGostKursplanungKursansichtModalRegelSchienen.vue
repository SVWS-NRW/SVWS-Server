<template>
	<svws-ui-modal ref="modal" size="small" class="hidden">
		<template #modalTitle>Regel erstellen</template>
		<template #modalContent>
			<div class="flex flex-col gap-1">
				<parameter-kursart v-model="kursart" />
				<span class="mt-1">von Schiene {{ von.nummer }} bis Schiene {{ bis.nummer }}</span>
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button @click="regel_hinzufuegen(GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS)">Sperren</svws-ui-button>
			<svws-ui-button @click="regel_hinzufuegen(GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS)">Alleine</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import type { GostBlockungSchiene} from "@core";
	import type { ShallowRef} from "vue";
	import { GostBlockungRegel, GostKursart, GostKursblockungRegelTyp } from "@core";
	import { ref, shallowRef } from "vue";

	const props = defineProps<{
		addRegel: (regel: GostBlockungRegel) => Promise<GostBlockungRegel | undefined>;
		von: GostBlockungSchiene;
		bis: GostBlockungSchiene;
	}>();

	const modal = ref();
	const openModal = () => modal.value.openModal();
	defineExpose({ openModal });

	const kursart: ShallowRef<GostKursart> = shallowRef(GostKursart.GK)

	async function regel_hinzufuegen(regeltyp: GostKursblockungRegelTyp) {
		modal.value.closeModal();
		const regel = new GostBlockungRegel();
		regel.typ = regeltyp.typ;
		regel.parameter.add(kursart.value.id);
		regel.parameter.add(props.von.nummer);
		regel.parameter.add(props.bis.nummer);
		await props.addRegel(regel);
	}

</script>
