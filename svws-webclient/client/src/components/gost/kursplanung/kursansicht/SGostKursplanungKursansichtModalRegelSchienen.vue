<template>
	<svws-ui-modal :show="showModal" size="small" class="hidden">
		<template #modalTitle>Regel erstellen</template>
		<template #modalContent>
			<div class="flex flex-col gap-1">
				<parameter-kursart v-model="kursart" />
				<span class="mt-1" v-if="von.nummer !== bis.nummer">von Schiene {{ von.nummer }} bis Schiene {{ bis.nummer }}</span>
				<span class="mt-1" v-else>in Schiene {{ von.nummer }}</span>
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="regel_hinzufuegen(GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS)"><i-ri-lock-line /> Sperren</svws-ui-button>
			<svws-ui-button type="secondary" @click="regel_hinzufuegen(GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS)">Alleine</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref, shallowRef, type ShallowRef } from "vue";
	import { GostBlockungRegel, GostKursart, GostKursblockungRegelTyp, GostBlockungSchiene } from "@core";

	const props = defineProps<{
		addRegel: (regel: GostBlockungRegel) => Promise<GostBlockungRegel | undefined>;
	}>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const von = ref<GostBlockungSchiene>(new GostBlockungSchiene());
	const bis = ref<GostBlockungSchiene>(new GostBlockungSchiene());

	const openModal = (schieneVon: GostBlockungSchiene, schieneBis: GostBlockungSchiene) => {
		von.value = schieneVon;
		bis.value = schieneBis;
		showModal().value = true;
	};
	defineExpose({ openModal });

	const kursart: ShallowRef<GostKursart> = shallowRef(GostKursart.GK)

	async function regel_hinzufuegen(regeltyp: GostKursblockungRegelTyp) {
		showModal().value = false;
		const regel = new GostBlockungRegel();
		regel.typ = regeltyp.typ;
		regel.parameter.add(kursart.value.id);
		regel.parameter.add(von.value.nummer);
		regel.parameter.add(bis.value.nummer);
		await props.addRegel(regel);
	}

</script>
