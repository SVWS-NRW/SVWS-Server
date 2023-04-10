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

	import type { GostBlockungSchiene} from "@svws-nrw/svws-core";
	import { GostBlockungRegel, GostKursart, GostKursblockungRegelTyp } from "@svws-nrw/svws-core";
	import type { Ref, ShallowRef} from "vue";
	import { ref, shallowRef, watch } from "vue";

	const props = defineProps<{
		addRegel: (regel: GostBlockungRegel) => Promise<GostBlockungRegel | undefined>;
		modelValue: boolean;
		von: GostBlockungSchiene;
		bis: GostBlockungSchiene;
	}>();

	const emit = defineEmits<{
		(e: 'update:modelValue', v: boolean): void;
	}>()

	const modal: Ref<any> = ref(null);

	watch(() => props.modelValue, (newValue : boolean, oldValue : boolean) => {
		if (newValue !== oldValue) {
			if (newValue)
				modal.value.openModal();
			else
				modal.value.closeModal();
		}
	});

	function close() {
		emit('update:modelValue', false);
	}

	const kursart: ShallowRef<GostKursart> = shallowRef(GostKursart.GK)

	async function regel_hinzufuegen(regeltyp: GostKursblockungRegelTyp) {
		close();
		const regel = new GostBlockungRegel();
		regel.typ = regeltyp.typ;
		regel.parameter.add(kursart.value.id);
		regel.parameter.add(props.von.nummer);
		regel.parameter.add(props.bis.nummer);
		await props.addRegel(regel);
	}

</script>
